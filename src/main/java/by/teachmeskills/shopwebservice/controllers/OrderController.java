package by.teachmeskills.shopwebservice.controllers;

import by.teachmeskills.shopwebservice.dto.OrderDto;
import by.teachmeskills.shopwebservice.dto.ProductDto;
import by.teachmeskills.shopwebservice.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable int id) {
        return Optional.ofNullable(orderService.getOrder(id)).map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/byUserId/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersByUserId(@PathVariable int userId) {
        return new ResponseEntity<>(orderService.getOrdersByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/orderProducts/{id}")
    public ResponseEntity<List<ProductDto>> getOrderProducts(@PathVariable int id) {
        return new ResponseEntity<>(orderService.getOrderProducts(id), HttpStatus.OK);
    }

    @GetMapping("/byDate/{date}")
    public ResponseEntity<List<OrderDto>> getOrdersByDate(@PathVariable LocalDateTime date) {
        return new ResponseEntity<>(orderService.getOrderByDate(date), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody @Valid OrderDto orderDto) {
        return new ResponseEntity<>(orderService.createOrder(orderDto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<OrderDto> updateOrder(@RequestBody @Valid OrderDto orderDto) {
        return new ResponseEntity<>(orderService.updateOrder(orderDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        orderService.deleteOrder(id);
    }
}
