package by.teachmeskills.shopwebservice.repositories;

import by.teachmeskills.shopwebservice.entities.Order;
import by.teachmeskills.shopwebservice.entities.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository {
    Order findById(int id);

    Order findByDate(LocalDateTime date);

    List<Order> findByUserId(int id);

    List<Order> findAll();

    List<Order> findByStatus(OrderStatus orderStatus);

    Order createOrUpdate(Order order);

    void delete(int id);
}
