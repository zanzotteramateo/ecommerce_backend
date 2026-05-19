package com.uade.tpo.marketplace.service.OrderService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entity.*;
import com.uade.tpo.marketplace.entity.dto.*;
import com.uade.tpo.marketplace.exceptions.CartEmptyException;
import com.uade.tpo.marketplace.exceptions.UserNotFoundException;
import com.uade.tpo.marketplace.repository.CartItemRepository;
import com.uade.tpo.marketplace.repository.CartRepository;
import com.uade.tpo.marketplace.repository.OrdersRepository;
import com.uade.tpo.marketplace.repository.ProductRepository;
import com.uade.tpo.marketplace.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service

public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderResponseDTO createOrder(String email) throws UserNotFoundException, CartEmptyException {

        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        Cart cart = cartRepository.findByUserEmail(email).orElseThrow(CartEmptyException::new);

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        if (cartItems.isEmpty()) {
            throw new CartEmptyException();
        }

        List<String> erroresStock = new ArrayList<>();

        // Primero verificamos stock y acumulamos errores
        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            int quantity = item.getQuantity();

            if (product.getStock() < quantity) {
                erroresStock.add("Stock insuficiente para \"" + product.getName() + "\" (disponible: "
                        + product.getStock() + ")");
            }
        }

        if (!erroresStock.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join("\n", erroresStock));
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDate.now());

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0;

        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            int quantity = item.getQuantity();

            product.setStock(product.getStock() - quantity);
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setPrice(product.getPrice());
            orderItem.setOrder(order);

            total += product.getPrice() * quantity;
            orderItems.add(orderItem);
        }

        order.setTotal(total);
        order.setOrderItems(orderItems);
        order = ordersRepository.save(order);

        cartItemRepository.deleteAll(cartItems);
        cartRepository.delete(cart);

        List<OrderItemDTO> itemsDTOs = orderItems.stream().map(item -> new OrderItemDTO(
                item.getProduct().getDescription(),
                item.getPrice(),
                item.getQuantity())).toList();

        return new OrderResponseDTO(order.getId(), order.getOrderDate(), order.getTotal(), itemsDTOs);
    }

    @Override
    public void deleteOrder(Long orderId) {
        Order order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        ordersRepository.delete(order);
    }

    @Override
    public List<OrderResponseDTO> getOrdersByEmail(String email) {
        List<Order> orders = ordersRepository.findByUserEmail(email);

        ArrayList<OrderResponseDTO> orderResponseDTOs = new ArrayList<>();

        for (Order order : orders) {
            List<OrderItem> orderItems = order.getOrderItems();
            List<OrderItemDTO> itemsDTOs = new ArrayList<>();

            for (OrderItem item : orderItems) {
                OrderItemDTO itemDTO = new OrderItemDTO(
                        item.getProduct().getName(),
                        item.getPrice(),
                        item.getQuantity());
                itemsDTOs.add(itemDTO);
            }

            OrderResponseDTO orderResponseDTO = new OrderResponseDTO(
                    order.getId(),
                    order.getOrderDate(),
                    order.getTotal(),
                    itemsDTOs);
            orderResponseDTOs.add(orderResponseDTO);
        }
        return orderResponseDTOs;
    }

    @Override
public List<OrderResponseDTO> getAllOrders() {
    List<Order> orders = ordersRepository.findAll();

    List<OrderResponseDTO> orderResponseDTOs = new ArrayList<>();

    for (Order order : orders) {
        List<OrderItemDTO> itemsDTOs = new ArrayList<>();

        for (OrderItem item : order.getOrderItems()) {
            OrderItemDTO itemDTO = new OrderItemDTO(
                item.getProduct().getName(),
                item.getPrice(),
                item.getQuantity()
            );
            itemsDTOs.add(itemDTO);
        }

        OrderResponseDTO dto = new OrderResponseDTO(
            order.getId(),
            order.getOrderDate(),
            order.getTotal(),
            itemsDTOs
        );

        orderResponseDTOs.add(dto);
    }

    return orderResponseDTOs;
}

}
