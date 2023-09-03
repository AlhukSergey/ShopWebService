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
import by.teachmeskills.shopwebservice.utils.FileService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
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
    private final FileService<Order> fileService;

    public OrderServiceImpl(OrderRepository orderRepository, OrderConverter orderConverter, ProductConverter productConverter, FileService<Order> fileService) {
        this.orderRepository = orderRepository;
        this.orderConverter = orderConverter;
        this.productConverter = productConverter;
        this.fileService = fileService;
    }

    @Override
    public OrderDto getOrder(int id) {
        return orderConverter.toDto(orderRepository.findById(id));
    }

    @Override
    public OrderDto getOrderByDate(LocalDateTime date) {
        Order order = Optional.ofNullable(orderRepository.findByDate(date))
                .orElseThrow(() -> new EntityNotFoundException(String.format("Заказа с id %s не найдено.", date)));
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
        Order order = Optional.ofNullable(orderRepository.findById(id))
                .orElseThrow(() -> new EntityNotFoundException(String.format("Заказа с id %d не найдено.", id)));
        return order.getProducts().stream().map(productConverter::toDto).toList();
    }

    @Override
    public OrderDto updateOrder(OrderDto orderDto) {
        Order order = Optional.ofNullable(orderRepository.findById(orderDto.getId()))
                .orElseThrow(() -> new EntityNotFoundException(String.format("Заказа с id %d не найдено.", orderDto.getId())));
        order.setOrderStatus(orderDto.getOrderStatus());
        order.setPrice(orderDto.getPrice());
        return orderConverter.toDto(orderRepository.createOrUpdate(order));
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = orderConverter.fromDto(orderDto);
        order = orderRepository.createOrUpdate(order);
        return orderConverter.toDto(order);
    }

    @Override
    public void deleteOrder(int id) {
        orderRepository.delete(id);
    }

    @Override
    public List<OrderDto> saveOrdersFromFile(MultipartFile file) {
        List<OrderDto> csvOrders = parseCsv(file);
        List<Order> orders = Optional.ofNullable(csvOrders)
                .map(list -> list.stream()
                        .map(orderConverter::fromDto)
                        .toList())
                .orElse(null);
        if (Optional.ofNullable(orders).isPresent()) {
            orders.forEach(orderRepository::createOrUpdate);
            return orders.stream().map(orderConverter::toDto).toList();
        }
        return Collections.emptyList();
    }

    @Override
    public String saveUserOrdersFromBD(int userId, String fileName) throws ExportToFIleException {
        return fileService.writeToFile(fileName, orderRepository.findByUserId(userId));
    }

    private List<OrderDto> parseCsv(MultipartFile file) {
        if (Optional.ofNullable(file).isPresent()) {
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                CsvToBean<OrderDto> csvToBean = new CsvToBeanBuilder(reader)
                        .withType(OrderDto.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .withSeparator(',')
                        .build();

                return csvToBean.parse();
            } catch (Exception ex) {
                throw new ParsingException(String.format("Ошибка во время парсинга данных: %s", ex.getMessage()));
            }
        }
        return Collections.emptyList();
    }
}
