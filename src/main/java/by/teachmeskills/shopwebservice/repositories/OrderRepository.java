package by.teachmeskills.shopwebservice.repositories;

import by.teachmeskills.shopwebservice.entities.Order;
import by.teachmeskills.shopwebservice.entities.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findByCreatedAt(LocalDateTime date);

    List<Order> findByUserId(int id);

    List<Order> findByOrderStatus(OrderStatus orderStatus);
}
