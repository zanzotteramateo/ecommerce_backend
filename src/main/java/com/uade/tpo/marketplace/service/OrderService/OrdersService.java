package com.uade.tpo.marketplace.service.OrderService;

import java.util.List;

import com.uade.tpo.marketplace.entity.dto.OrderResponseDTO;
import com.uade.tpo.marketplace.exceptions.CartEmptyException;
import com.uade.tpo.marketplace.exceptions.UserNotFoundException;


public interface OrdersService {
    OrderResponseDTO createOrder(String email) throws CartEmptyException, UserNotFoundException;
    void deleteOrder(Long orderId);
    List<OrderResponseDTO> getOrdersByEmail(String email);
    List<OrderResponseDTO> getAllOrders();

}