package by.teachmeskills.shopwebservice.services.impl;

import by.teachmeskills.shopwebservice.dto.OrderDto;
import by.teachmeskills.shopwebservice.dto.ProductDto;
import by.teachmeskills.shopwebservice.dto.converters.OrderConverter;
import by.teachmeskills.shopwebservice.entities.Order;
import by.teachmeskills.shopwebservice.repositories.OrderRepository;
import by.teachmeskills.shopwebservice.services.OrderService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderConverter orderConverter;

    public OrderServiceImpl(OrderRepository orderRepository, OrderConverter orderConverter) {
        this.orderRepository = orderRepository;
        this.orderConverter = orderConverter;
    }

    @Override
    public OrderDto getOrder(int id) {
        return orderConverter.toDto(orderRepository.findById(id));
    }

    @Override
    public List<OrderDto> getOrderByDate(LocalDateTime date) {
        return orderRepository.findByDate(date).stream().map(orderConverter::toDto).toList();
    }

    @Override
    public List<OrderDto> getOrdersByUserId(int id) {
        return orderRepository.findByUserId(id).stream().map(orderConverter::toDto).toList();
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream().map(orderConverter::toDto).toList();
    }

    @Override
    public List<ProductDto> getOrderProducts(int id) {
        return getOrder(id).getProducts();
    }

    @Override
    public OrderDto updateOrder(OrderDto orderDto) {
        Order order = Optional.ofNullable(orderRepository.findById(orderDto.getId()))
                .orElseThrow(() -> new EntityNotFoundException(String.format("Продукта с id %d не найдено.", orderDto.getId())));
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
}
