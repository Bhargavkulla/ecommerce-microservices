package com.example.order.controllers;

import com.example.order.entities.Order;
import com.example.order.kafka.OrderProducer;
import com.example.order.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProducer orderProducer;

    @PostMapping("/")
    public ResponseEntity<String> placeOrder(@RequestBody Order order) {
        // Save order to database
        Order savedOrder = orderRepository.save(order);
        
        // Send order event to Kafka
        orderProducer.sendOrderEvent(savedOrder);
        
        return ResponseEntity.ok("Order placed successfully! Order ID: " + savedOrder.getId());
    }

    @GetMapping("/")
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
