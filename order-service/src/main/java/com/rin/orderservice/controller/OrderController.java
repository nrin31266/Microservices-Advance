package com.rin.orderservice.controller;

import com.rin.orderservice.client.UserClient;
import com.rin.orderservice.dto.UserDto;
import com.rin.orderservice.dto.response.OrderResponse;
import com.rin.orderservice.entity.Order;
import com.rin.orderservice.repository.OrderRepository;
import com.rin.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class OrderController {

    OrderService orderService;
    OrderRepository orderRepository;
    UserClient userClient;



//    @PostMapping
//    public Order placeOrder(@RequestBody Order order) {
//        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        order.setUserId(userId);
//        return orderService.createOrder(order);
//    }

    @PostMapping
    public Order placeOrder(@RequestBody Order order, JwtAuthenticationToken auth) {
        String sub = auth.getToken().getSubject(); // lấy Keycloak-sub (UUID)
        UserDto user = userClient.getUserByKeycloakId(sub); // Feign gọi user-service

        order.setUserId(user.getId()); // gán Long userId
        return orderService.createOrder(order);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    public OrderResponse getOrderById(@PathVariable Long id) {
        Order order = orderRepository.findById(id).orElseThrow();

        // Gọi user-service bằng Feign
        UserDto user = userClient.getUserById(order.getUserId());

        return new OrderResponse(order.getId(), order.getProduct(), order.getPrice(), user);
    }
}
