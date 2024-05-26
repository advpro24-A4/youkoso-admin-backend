package id.ac.ui.cs.advprog.youkosoadmin.modules.primary.order;

import id.ac.ui.cs.advprog.youkosoadmin.exceptions.UnauthorizedException;
import id.ac.ui.cs.advprog.youkosoadmin.models.primary.Order;
import id.ac.ui.cs.advprog.youkosoadmin.modules.primary.order.dto.UpdateOrderStatusRequest;
import id.ac.ui.cs.advprog.youkosoadmin.utils.AuthResponse;
import id.ac.ui.cs.advprog.youkosoadmin.utils.AuthService;
import id.ac.ui.cs.advprog.youkosoadmin.utils.DefaultResponse;
import id.ac.ui.cs.advprog.youkosoadmin.utils.DefaultResponseBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final IOrderService orderService;
    private final AuthService authService;


    public OrderController(IOrderService orderService, AuthService authService) {
        this.orderService = orderService;
        this.authService = authService;
    }

    @GetMapping("")
    public ResponseEntity<DefaultResponse<Page<Order>>> getAllOrders(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
        Pageable pageable,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String userId
        )
    {
        AuthResponse authResponse = authService.validateToken(authHeader).join();

        if (authResponse == null) {
            throw new UnauthorizedException("Authorization token is invalid");
        }

        if(status != null && userId != null){
            Page<Order> orders = orderService.findByStatusAndUserId(status, userId, pageable);
            DefaultResponse<Page<Order>> response = new DefaultResponseBuilder<Page<Order>>()
                    .statusCode(HttpStatus.OK.value())
                    .success(true)
                    .message("Success get orders")
                    .data(orders)
                    .build();
            return ResponseEntity.ok(response);
        }

        if(status != null){
            Page<Order> orders = orderService.findByStatus(status, pageable);
            DefaultResponse<Page<Order>> response = new DefaultResponseBuilder<Page<Order>>()
                    .statusCode(HttpStatus.OK.value())
                    .success(true)
                    .message("Success get orders")
                    .data(orders)
                    .build();
            return ResponseEntity.ok(response);
        }

        if(userId != null){
            Page<Order> orders = orderService.findByUserId(userId, pageable);
            DefaultResponse<Page<Order>> response = new DefaultResponseBuilder<Page<Order>>()
                    .statusCode(HttpStatus.OK.value())
                    .success(true)
                    .message("Success get orders")
                    .data(orders)
                    .build();
            return ResponseEntity.ok(response);
        }

        Page<Order> orders = orderService.findAll(pageable);
        DefaultResponse<Page<Order>> response = new DefaultResponseBuilder<Page<Order>>()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Success get orders")
                .data(orders)
                .build();

        return ResponseEntity.ok(response);
    }


    @PostMapping("")
    public ResponseEntity<DefaultResponse<Order>> updateOrderStatus(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
        @RequestBody UpdateOrderStatusRequest request
        )
    {
        AuthResponse authResponse = authService.validateToken(authHeader).join();

        if (authResponse == null) {
            throw new UnauthorizedException("Authorization token is invalid");
        }

        Order order = orderService.updateOrderStatus(request.getOrderId(), request.getOrderStatus());
        DefaultResponse<Order> response = new DefaultResponseBuilder<Order>()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Success update order status")
                .data(order)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<DefaultResponse<Order>> getOrderById(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
        @PathVariable Long orderId
        )
    {
        AuthResponse authResponse = authService.validateToken(authHeader).join();

        if (authResponse == null) {
            throw new UnauthorizedException("Authorization token is invalid");
        }

        Order order = orderService.findById(orderId);
        DefaultResponse<Order> response = new DefaultResponseBuilder<Order>()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Success get order")
                .data(order)
                .build();

        return ResponseEntity.ok(response);
    }
}
