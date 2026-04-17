package com.deliverysystem.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * MenuItem model class representing food items in a restaurant's menu.
 */
public class MenuItem {
    private int menuItemId;
    private int restaurantId;
    private String itemName;
    private String description;
    private String category;
    private BigDecimal price;
    private boolean availability;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Default constructor
     */
    public MenuItem() {
    }

    /**
     * Parameterized constructor
     */
    public MenuItem(int menuItemId, int restaurantId, String itemName,
                   String description, String category, BigDecimal price) {
        this.menuItemId = menuItemId;
        this.restaurantId = restaurantId;
        this.itemName = itemName;
        this.description = description;
        this.category = category;
        this.price = price;
        this.availability = true;
    }

    // Getters and Setters
    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "menuItemId=" + menuItemId +
                ", itemName='" + itemName + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                '}';
    }
}