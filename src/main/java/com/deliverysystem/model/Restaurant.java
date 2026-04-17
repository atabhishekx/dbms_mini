package com.deliverysystem.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Restaurant model class representing restaurants in the system.
 */
public class Restaurant {
    private int restaurantId;
    private int userId;
    private String restaurantName;
    private String description;
    private String address;
    private String phone;
    private BigDecimal rating;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private boolean isActive;
    private LocalDateTime createdAt;

    /**
     * Default constructor
     */
    public Restaurant() {
    }

    /**
     * Parameterized constructor
     */
    public Restaurant(int restaurantId, int userId, String restaurantName, 
                      String description, String address, String phone,
                      BigDecimal rating, LocalTime openingTime, LocalTime closingTime) {
        this.restaurantId = restaurantId;
        this.userId = userId;
        this.restaurantName = restaurantName;
        this.description = description;
        this.address = address;
        this.phone = phone;
        this.rating = rating;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.isActive = true;
    }

    // Getters and Setters
    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "restaurantId=" + restaurantId +
                ", restaurantName='" + restaurantName + '\'' +
                ", phone='" + phone + '\'' +
                ", rating=" + rating +
                '}';
    }
}