package com.deliverysystem.model;

import java.math.BigDecimal;

/**
 * CartItem model class representing items in a shopping cart.
 */
public class CartItem {
    private MenuItem menuItem;
    private int quantity;

    /**
     * Default constructor
     */
    public CartItem() {
    }

    /**
     * Parameterized constructor
     */
    public CartItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    /**
     * Get subtotal for this cart item
     */
    public BigDecimal getSubtotal() {
        return menuItem.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    /**
     * Increment quantity
     */
    public void incrementQuantity() {
        this.quantity++;
    }

    /**
     * Decrement quantity
     */
    public void decrementQuantity() {
        if (this.quantity > 1) {
            this.quantity--;
        }
    }

    // Getters and Setters
    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "menuItem=" + menuItem.getItemName() +
                ", quantity=" + quantity +
                ", subtotal=" + getSubtotal() +
                '}';
    }
}