package com.deliverysystem.dao;

import com.deliverysystem.database.DatabaseManager;
import com.deliverysystem.model.Restaurant;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Restaurant entity.
 * Handles all database operations related to restaurants.
 */
public class RestaurantDAO {

    /**
     * Insert a new restaurant
     */
    public int insert(Restaurant restaurant) throws SQLException {
        String sql = "INSERT INTO restaurants (user_id, restaurant_name, description, address, phone, rating, opening_time, closing_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, restaurant.getUserId());
            pstmt.setString(2, restaurant.getRestaurantName());
            pstmt.setString(3, restaurant.getDescription());
            pstmt.setString(4, restaurant.getAddress());
            pstmt.setString(5, restaurant.getPhone());
            pstmt.setObject(6, restaurant.getRating());
            pstmt.setObject(7, restaurant.getOpeningTime());
            pstmt.setObject(8, restaurant.getClosingTime());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        }
        return -1;
    }

    /**
     * Update an existing restaurant
     */
    public boolean update(Restaurant restaurant) throws SQLException {
        String sql = "UPDATE restaurants SET user_id = ?, restaurant_name = ?, description = ?, address = ?, phone = ?, rating = ?, opening_time = ?, closing_time = ? WHERE restaurant_id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, restaurant.getUserId());
            pstmt.setString(2, restaurant.getRestaurantName());
            pstmt.setString(3, restaurant.getDescription());
            pstmt.setString(4, restaurant.getAddress());
            pstmt.setString(5, restaurant.getPhone());
            pstmt.setObject(6, restaurant.getRating());
            pstmt.setObject(7, restaurant.getOpeningTime());
            pstmt.setObject(8, restaurant.getClosingTime());
            pstmt.setInt(9, restaurant.getRestaurantId());
            
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Delete a restaurant by ID
     */
    public boolean delete(int restaurantId) throws SQLException {
        String sql = "DELETE FROM restaurants WHERE restaurant_id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, restaurantId);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Find restaurant by ID
     */
    public Restaurant findById(int restaurantId) throws SQLException {
        String sql = "SELECT * FROM restaurants WHERE restaurant_id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, restaurantId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRestaurant(rs);
                }
            }
        }
        return null;
    }

    /**
     * Find restaurant by user ID (owner)
     */
    public Restaurant findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM restaurants WHERE user_id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRestaurant(rs);
                }
            }
        }
        return null;
    }

    /**
     * Get all active restaurants
     */
    public List<Restaurant> findAll() throws SQLException {
        String sql = "SELECT * FROM restaurants WHERE is_active = TRUE ORDER BY restaurant_name";
        List<Restaurant> restaurants = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                restaurants.add(mapResultSetToRestaurant(rs));
            }
        }
        return restaurants;
    }

    /**
     * Search restaurants by name
     */
    public List<Restaurant> searchByName(String searchTerm) throws SQLException {
        String sql = "SELECT * FROM restaurants WHERE restaurant_name LIKE ? AND is_active = TRUE ORDER BY restaurant_name";
        List<Restaurant> restaurants = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + searchTerm + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    restaurants.add(mapResultSetToRestaurant(rs));
                }
            }
        }
        return restaurants;
    }

    /**
     * Update restaurant rating
     */
    public boolean updateRating(int restaurantId, double rating) throws SQLException {
        String sql = "UPDATE restaurants SET rating = ? WHERE restaurant_id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, rating);
            pstmt.setInt(2, restaurantId);
            
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Update restaurant status
     */
    public boolean updateStatus(int restaurantId, boolean isActive) throws SQLException {
        String sql = "UPDATE restaurants SET is_active = ? WHERE restaurant_id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setBoolean(1, isActive);
            pstmt.setInt(2, restaurantId);
            
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Map ResultSet to Restaurant object
     */
    private Restaurant mapResultSetToRestaurant(ResultSet rs) throws SQLException {
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(rs.getInt("restaurant_id"));
        restaurant.setUserId(rs.getInt("user_id"));
        restaurant.setRestaurantName(rs.getString("restaurant_name"));
        restaurant.setDescription(rs.getString("description"));
        restaurant.setAddress(rs.getString("address"));
        restaurant.setPhone(rs.getString("phone"));
        
        Object rating = rs.getObject("rating");
        if (rating != null) {
            restaurant.setRating((java.math.BigDecimal) rating);
        }
        
        Object openingTime = rs.getObject("opening_time");
        if (openingTime != null) {
            restaurant.setOpeningTime(LocalTime.parse(openingTime.toString()));
        }
        
        Object closingTime = rs.getObject("closing_time");
        if (closingTime != null) {
            restaurant.setClosingTime(LocalTime.parse(closingTime.toString()));
        }
        
        restaurant.setActive(rs.getBoolean("is_active"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            restaurant.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return restaurant;
    }
}