package com.deliverysystem.model;

import java.math.BigDecimal;

/**
 * OrderItem model class representing items in an order.
 * Links an order with a menu item with quantity and pricing.
 */
public class OrderItem {
    private int orderItemId;
    private int orderId;
    private int menuItemId;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    
    // Transient fields for display
    private String itemName;
    private String itemDescription;

    /**
     * Default constructor
     */
    public OrderItem() {
    }

    /**
     * Parameterized constructor
     */
    public OrderItem(int orderItemId, int orderId, int menuItemId, 
                    int quantity, BigDecimal unitPrice) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    /**
     * Constructor with item details
     */
    public OrderItem(int menuItemId, String itemName, String itemDescription,
                    int quantity, BigDecimal unitPrice) {
        this.menuItemId = menuItemId;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    // Getters and Setters
    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateSubtotal();
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        calculateSubtotal();
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    /**
     * Calculate subtotal based on quantity and unit price
     */
    public void calculateSubtotal() {
        this.subtotal = this.unitPrice.multiply(BigDecimal.valueOf(this.quantity));
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemId=" + orderItemId +
                ", menuItemId=" + menuItemId +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", subtotal=" + subtotal +
                '}';
    }
}