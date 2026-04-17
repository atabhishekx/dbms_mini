package com.deliverysystem.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Order model class representing orders in the delivery system.
 * Contains order details and associated order items.
 */
public class Order {
    private int orderId;
    private int userId;
    private int restaurantId;
    private Integer deliveryAgentId;
    private OrderStatus orderStatus;
    private BigDecimal totalAmount;
    private String deliveryAddress;
    private LocalDateTime orderDate;
    private LocalDateTime deliveryDate;
    private String notes;
    private List<OrderItem> orderItems;

    /**
     * Default constructor
     */
    public Order() {
        this.orderItems = new ArrayList<>();
        this.orderStatus = OrderStatus.PLACED;
    }

    /**
     * Parameterized constructor
     */
    public Order(int orderId, int userId, int restaurantId, OrderStatus orderStatus,
               BigDecimal totalAmount, String deliveryAddress) {
        this.orderId = orderId;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.orderStatus = orderStatus;
        this.totalAmount = totalAmount;
        this.deliveryAddress = deliveryAddress;
        this.orderItems = new ArrayList<>();
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer getDeliveryAgentId() {
        return deliveryAgentId;
    }

    public void setDeliveryAgentId(Integer deliveryAgentId) {
        this.deliveryAgentId = deliveryAgentId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void addOrderItem(OrderItem item) {
        this.orderItems.add(item);
    }

    /**
     * Calculate total amount from order items
     */
    public void calculateTotal() {
        this.totalAmount = BigDecimal.ZERO;
        for (OrderItem item : orderItems) {
            this.totalAmount = this.totalAmount.add(item.getSubtotal());
        }
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", restaurantId=" + restaurantId +
                ", orderStatus=" + orderStatus +
                ", totalAmount=" + totalAmount +
                ", orderDate=" + orderDate +
                '}';
    }

    /**
     * Enum for order status
     */
    public enum OrderStatus {
        PLACED("Order placed"),
        PREPARING("Preparing order"),
        OUT_FOR_DELIVERY("Out for delivery"),
        DELIVERED("Delivered"),
        CANCELLED("Cancelled");

        private final String description;

        OrderStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}