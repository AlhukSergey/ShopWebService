package by.teachmeskills.shopwebservice.services;

import by.teachmeskills.shopwebservice.dto.OrderDto;
import by.teachmeskills.shopwebservice.dto.ProductDto;
import by.teachmeskills.shopwebservice.entities.OrderStatus;
import by.teachmeskills.shopwebservice.exceptions.ExportToFIleException;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    OrderDto getOrder(int id);

    OrderDto getOrderByDate(LocalDateTime date);

    List<OrderDto> getOrdersByUserId(int id);

    List<OrderDto> getOrdersByStatus(OrderStatus orderStatus);

    List<OrderDto> getAllOrders();

    List<ProductDto> getOrderProducts(int id);

    OrderDto updateOrder(OrderDto orderDto);

    OrderDto createOrder(OrderDto orderDto);

    void deleteOrder(int id);

    List<OrderDto> saveOrdersFromFile(MultipartFile file) throws Exception;

    String saveUserOrdersFromBD(int userId, String fileName) throws ExportToFIleException;
}
