package com.deliverysystem.service;

import com.deliverysystem.dao.DeliveryAgentDAO;
import com.deliverysystem.dao.MenuItemDAO;
import com.deliverysystem.dao.OrderDAO;
import com.deliverysystem.dao.OrderItemDAO;
import com.deliverysystem.dao.PaymentDAO;
import com.deliverysystem.model.*;
import com.deliverysystem.model.DeliveryAgent.AgentStatus;
import com.deliverysystem.model.Order.OrderStatus;
import com.deliverysystem.model.Payment.PaymentMethod;
import com.deliverysystem.model.Payment.PaymentStatus;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service layer for Order operations.
 * Handles business logic for order management.
 */
public class OrderService {
    private final OrderDAO orderDAO;
    private final OrderItemDAO orderItemDAO;
    private final DeliveryAgentDAO agentDAO;
    private final PaymentDAO paymentDAO;
    private final MenuItemDAO menuItemDAO;

    /**
     * Constructor
     */
    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.orderItemDAO = new OrderItemDAO();
        this.agentDAO = new DeliveryAgentDAO();
        this.paymentDAO = new PaymentDAO();
        this.menuItemDAO = new MenuItemDAO();
    }

    /**
     * Place a new order
     */
    public Order placeOrder(int userId, int restaurantId, List<CartItem> cartItems, 
                           String deliveryAddress, PaymentMethod paymentMethod, String notes) throws SQLException {
        
        // Validate input
        if (userId <= 0 || restaurantId <= 0) {
            throw new IllegalArgumentException("Invalid user or restaurant");
        }
        
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }
        
        if (deliveryAddress == null || deliveryAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Delivery address is required");
        }
        
        // Calculate total amount
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            totalAmount = totalAmount.add(item.getSubtotal());
        }
        
        // Create order
        Order order = new Order();
        order.setUserId(userId);
        order.setRestaurantId(restaurantId);
        order.setOrderStatus(OrderStatus.PLACED);
        order.setTotalAmount(totalAmount);
        order.setDeliveryAddress(deliveryAddress);
        order.setNotes(notes);
        
        // Insert order
        int orderId = orderDAO.insert(order);
        
        if (orderId <= 0) {
            throw new SQLException("Failed to create order");
        }
        
        order.setOrderId(orderId);
        
        // Convert cart items to order items and insert
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            MenuItem menuItem = cartItem.getMenuItem();
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setMenuItemId(menuItem.getMenuItemId());
            orderItem.setItemName(menuItem.getItemName());
            orderItem.setItemDescription(menuItem.getDescription());
            // Set unit price BEFORE quantity to ensure subtotal is calculated correctly
            orderItem.setUnitPrice(menuItem.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItems.add(orderItem);
        }
        
        // Insert order items
        orderItemDAO.insertBatch(orderItems);
        order.setOrderItems(orderItems);
        
        // Create payment
        Payment payment = new Payment(orderId, paymentMethod, totalAmount);
        paymentDAO.insert(payment);
        
        return order;
    }

    /**
     * Get order by ID
     */
    public Order getOrderById(int orderId) throws SQLException {
        Order order = orderDAO.findById(orderId);
        
        if (order != null) {
            // Load order items
            List<OrderItem> items = orderItemDAO.findByOrderId(orderId);
            order.setOrderItems(items);
        }
        
        return order;
    }

    /**
     * Get all orders
     */
    public List<Order> getAllOrders() throws SQLException {
        return orderDAO.findAll();
    }

    /**
     * Get orders by user
     */
    public List<Order> getOrdersByUser(int userId) throws SQLException {
        return orderDAO.findByUser(userId);
    }

    /**
     * Get orders by restaurant
     */
    public List<Order> getOrdersByRestaurant(int restaurantId) throws SQLException {
        return orderDAO.findByRestaurant(restaurantId);
    }

    /**
     * Update order status
     */
    public boolean updateOrderStatus(int orderId, OrderStatus status) throws SQLException {
        Order order = orderDAO.findById(orderId);
        
        if (order == null) {
            throw new IllegalArgumentException("Order not found");
        }
        
        boolean success = orderDAO.updateStatus(orderId, status);
        
        // If order is delivered, mark payment as completed
        if (success && status == OrderStatus.DELIVERED) {
            Payment payment = paymentDAO.findByOrderId(orderId);
            if (payment != null) {
                paymentDAO.markAsCompleted(payment.getPaymentId());
            }
            
            // Free up the delivery agent
            if (order.getDeliveryAgentId() != null) {
                agentDAO.updateStatus(order.getDeliveryAgentId(), AgentStatus.AVAILABLE);
            }
        }
        
        return success;
    }

    /**
     * Assign delivery agent to order
     */
    public boolean assignDeliveryAgent(int orderId, int agentId) throws SQLException {
        Order order = orderDAO.findById(orderId);
        
        if (order == null) {
            throw new IllegalArgumentException("Order not found");
        }
        
        // Check if agent exists and is available
        DeliveryAgent agent = agentDAO.findById(agentId);
        
        if (agent == null) {
            throw new IllegalArgumentException("Delivery agent not found");
        }
        
        if (agent.getAvailabilityStatus() != AgentStatus.AVAILABLE) {
            throw new IllegalArgumentException("Delivery agent is not available");
        }
        
        // Assign agent
        boolean success = orderDAO.assignDeliveryAgent(orderId, agentId);
        
        // Update agent status to busy
        if (success) {
            agentDAO.updateStatus(agentId, AgentStatus.BUSY);
            
            // Update order status to preparing
            orderDAO.updateStatus(orderId, OrderStatus.PREPARING);
        }
        
        return success;
    }

    /**
     * Get available delivery agents
     */
    public List<DeliveryAgent> getAvailableAgents() throws SQLException {
        return agentDAO.findAvailable();
    }

    /**
     * Cancel order
     */
    public boolean cancelOrder(int orderId) throws SQLException {
        Order order = orderDAO.findById(orderId);
        
        if (order == null) {
            throw new IllegalArgumentException("Order not found");
        }
        
        // Can only cancel placed or preparing orders
        if (order.getOrderStatus() != OrderStatus.PLACED && 
            order.getOrderStatus() != OrderStatus.PREPARING) {
            throw new IllegalArgumentException("Cannot cancel order in current status");
        }
        
        boolean success = orderDAO.updateStatus(orderId, OrderStatus.CANCELLED);
        
        // Free up delivery agent if assigned
        if (success && order.getDeliveryAgentId() != null) {
            agentDAO.updateStatus(order.getDeliveryAgentId(), AgentStatus.AVAILABLE);
        }
        
        return success;
    }

    /**
     * Process payment
     */
    public boolean processPayment(int orderId, PaymentMethod method) throws SQLException {
        Order order = orderDAO.findById(orderId);
        
        if (order == null) {
            throw new IllegalArgumentException("Order not found");
        }
        
        Payment payment = paymentDAO.findByOrderId(orderId);
        
        if (payment == null) {
            throw new IllegalArgumentException("Payment not found");
        }
        
        payment.setPaymentMethod(method);
        
        // Simulate payment processing (in real system, integrate with payment gateway)
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        
        return paymentDAO.updateStatus(payment.getPaymentId(), PaymentStatus.COMPLETED);
    }
}