package by.teachmeskills.shopwebservice.services.impl;

import by.teachmeskills.shopwebservice.dto.OrderDto;
import by.teachmeskills.shopwebservice.dto.ProductDto;
import by.teachmeskills.shopwebservice.dto.converters.OrderConverter;
import by.teachmeskills.shopwebservice.dto.converters.ProductConverter;
import by.teachmeskills.shopwebservice.entities.Order;
import by.teachmeskills.shopwebservice.entities.OrderStatus;
import by.teachmeskills.shopwebservice.exceptions.ExportToFIleException;
import by.teachmeskills.shopwebservice.exceptions.ParsingException;
import by.teachmeskills.shopwebservice.repositories.OrderRepository;
import by.teachmeskills.shopwebservice.services.OrderService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderConverter orderConverter;
    private final ProductConverter productConverter;

    public OrderServiceImpl(OrderRepository orderRepository, OrderConverter orderConverter, ProductConverter productConverter) {
        this.orderRepository = orderRepository;
        this.orderConverter = orderConverter;
        this.productConverter = productConverter;
    }

    @Override
    public OrderDto getOrder(int id) {
        return orderConverter.toDto(orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Заказа с id %d не найдено.", id))));
    }

    @Override
    public OrderDto getOrderByDate(LocalDateTime date) {
        Order order = Optional.ofNullable(orderRepository.findByCreatedAt(date))
                .orElseThrow(() -> new EntityNotFoundException("Заказа не найдено."));
        return orderConverter.toDto(order);
    }

    @Override
    public List<OrderDto> getOrdersByUserId(int id) {
        return orderRepository.findByUserId(id).stream().map(orderConverter::toDto).toList();
    }

    @Override
    public List<OrderDto> getOrdersByStatus(OrderStatus orderStatus) {
        return orderRepository.findByStatus(orderStatus).stream().map(orderConverter::toDto).toList();
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream().map(orderConverter::toDto).toList();
    }

    @Override
    public List<ProductDto> getOrderProducts(int id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Заказа с id %d не найдено.", id)));
        return order.getProducts().stream().map(productConverter::toDto).toList();
    }

    @Override
    public OrderDto updateOrder(OrderDto orderDto) {
        Order order = orderRepository.findById(orderDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Заказа с id %d не найдено.", orderDto.getId())));
        order.setOrderStatus(orderDto.getOrderStatus());
        order.setPrice(orderDto.getPrice());
        return orderConverter.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = orderConverter.fromDto(orderDto);
        order = orderRepository.save(order);
        return orderConverter.toDto(order);
    }

    @Override
    public void deleteOrder(int id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Заказа с id %d не найдено.", id)));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> importOrdersFromCsv(MultipartFile file) {
        List<OrderDto> csvOrders = parseCsv(file);
        List<Order> orders = Optional.ofNullable(csvOrders)
                .map(list -> list.stream()
                        .map(orderConverter::fromDto)
                        .toList())
                .orElse(null);
        if (Optional.ofNullable(orders).isPresent()) {
            orders.forEach(orderRepository::save);
            return orders.stream().map(orderConverter::toDto).toList();
        }
        return Collections.emptyList();
    }

    @Override
    public void exportOrdersToCsv(HttpServletResponse response, int userId) throws ExportToFIleException {
        response.setContentType("text/csv");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=ordersOfUserWithId" + userId + ".csv";
        response.setHeader(headerKey, headerValue);
        response.setCharacterEncoding("UTF-8");

        List<OrderDto> dtoOrders = orderRepository.findByUserId(userId).stream().map(orderConverter::toDto).toList();

        try (ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE)) {
            String[] csvHeader = {"Order ID", "Order status", "Created at", "Products", "Total price", "User ID"};
            String[] nameMapping = {"id", "orderStatus", "createdAt", "products", "price", "userId"};

            csvWriter.writeHeader(csvHeader);

            for (OrderDto orderDto : dtoOrders) {
                csvWriter.write(orderDto, nameMapping);
            }
        } catch (IOException e) {
            throw new ExportToFIleException("Во время записи в файл произошла непредвиденная ошибка. Попробуйте позже.");
        }
    }

    private List<OrderDto> parseCsv(MultipartFile file) {
        if (Optional.ofNullable(file).isPresent()) {
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                CsvToBean<OrderDto> csvToBean = new CsvToBeanBuilder<OrderDto>(reader)
                        .withType(OrderDto.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .withSeparator(',')
                        .build();

                return csvToBean.parse();
            } catch (Exception ex) {
                throw new ParsingException(String.format("Ошибка во время преобразования данных: %s", ex.getMessage()));
            }
        }
        return Collections.emptyList();
    }
}
