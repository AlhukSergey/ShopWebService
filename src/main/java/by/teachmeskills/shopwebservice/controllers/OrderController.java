package by.teachmeskills.shopwebservice.controllers;

import by.teachmeskills.shopwebservice.dto.OrderDto;
import by.teachmeskills.shopwebservice.dto.ProductDto;
import by.teachmeskills.shopwebservice.entities.OrderStatus;
import by.teachmeskills.shopwebservice.exceptions.ExportToFIleException;
import by.teachmeskills.shopwebservice.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Tag(name = "order", description = "Order Endpoint")
@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(
            summary = "Find all orders",
            description = "Find all existed orders in Shop",
            tags = {"order"}
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "All orders were found",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrderDto.class)))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Orders not found - server error"
                    )
            }
    )
    @GetMapping("/all")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
    }

    @Operation(
            summary = "Find certain order",
            description = "Find certain existed order in Shop by its id",
            tags = {"order"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order was found by its id",
                    content = @Content(schema = @Schema(contentSchema = OrderDto.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Order not fount - forbidden operation"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@Parameter(required = true, description = "Order ID") @PathVariable int id) {
        return Optional.ofNullable(orderService.getOrder(id))
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "Find user orders",
            description = "Find all user orders in Shop",
            tags = {"order"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "All orders for user found",
                    content = @Content(schema = @Schema(contentSchema = OrderDto.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Orders not fount - forbidden operation"
            )
    })
    @GetMapping("/userOrders/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersByUserId(@Parameter(required = true, description = "User ID") @PathVariable int userId) {
        return new ResponseEntity<>(orderService.getOrdersByUserId(userId), HttpStatus.OK);
    }

    @Operation(
            summary = "Find certain order",
            description = "Find certain existed order in Shop by its status",
            tags = {"order"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order was found by its status",
                    content = @Content(schema = @Schema(contentSchema = OrderDto.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Order not fount - forbidden operation"
            )
    })
    @GetMapping("/orders/{status}")
    public ResponseEntity<List<OrderDto>> getOrdersByStatus(@Parameter(required = true, description = "Order status") @PathVariable OrderStatus status) {
        return new ResponseEntity<>(orderService.getOrdersByStatus(status), HttpStatus.OK);
    }

    @Operation(
            summary = "Find order products",
            description = "Find all order products in Shop",
            tags = {"order"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "All products for order found",
                    content = @Content(schema = @Schema(contentSchema = ProductDto.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Products not fount - forbidden operation"
            )
    })
    @GetMapping("/products/{id}")
    public ResponseEntity<List<ProductDto>> getOrderProducts(@Parameter(required = true, description = "Order ID") @PathVariable int id) {
        return new ResponseEntity<>(orderService.getOrderProducts(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Find certain order",
            description = "Find certain existed order in Shop by its creation date",
            tags = {"order"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order was found by its creation date",
                    content = @Content(schema = @Schema(contentSchema = OrderDto.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Order not fount - forbidden operation"
            )
    })
    @GetMapping("/date/{date}")
    public ResponseEntity<OrderDto> getOrderByDate(@Parameter(required = true, description = "Order creation date") @PathVariable LocalDateTime date) {
        return new ResponseEntity<>(orderService.getOrderByDate(date), HttpStatus.OK);
    }

    @Operation(
            summary = "Create order",
            description = "Create new order",
            tags = {"order"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Order was created",
                    content = @Content(schema = @Schema(contentSchema = OrderDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Order not created - server error"
            )
    })
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody @Valid OrderDto orderDto) {
        return new ResponseEntity<>(orderService.createOrder(orderDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update order",
            description = "Update existed order",
            tags = {"order"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order was updated",
                    content = @Content(schema = @Schema(contentSchema = OrderDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Order not updated - server error"
            )
    })
    @PutMapping
    public ResponseEntity<OrderDto> updateOrder(@RequestBody @Valid OrderDto orderDto) {
        return new ResponseEntity<>(orderService.updateOrder(orderDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete order",
            description = "Delete existed order",
            tags = {"order"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order was deleted"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Order not deleted - server error"
            )
    })
    @DeleteMapping("/{id}")
    public void deleteUser(@Parameter(required = true, description = "Order ID") @PathVariable int id) {
        orderService.deleteOrder(id);
    }

    @Operation(
            summary = "Import new orders",
            description = "Add new orders to Shop database from csv file",
            tags = {"order"}
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "All orders were added",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrderDto.class)))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Orders not added - server error"
                    )
            }
    )
    @PostMapping("/csv/import")
    public ResponseEntity<List<OrderDto>> importOrdersFromCsv(@RequestParam("file") MultipartFile file) throws Exception {
        return new ResponseEntity<>(orderService.importOrdersFromCsv(file), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Export all user orders",
            description = "Export all existed user orders from Shop database  to csv file",
            tags = {"order"}
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "All user orders were exported",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrderDto.class)))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Orders not exported - bad request"
                    )
            }
    )
    @GetMapping("/csv/export/{userId}")
    public void exportOrdersToCsv(HttpServletResponse response, @Parameter(required = true, description = "User ID") @PathVariable int userId) throws ExportToFIleException {
        orderService.exportOrdersToCsv(response, userId);
    }
}
