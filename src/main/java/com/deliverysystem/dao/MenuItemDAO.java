package com.deliverysystem.dao;

import com.deliverysystem.database.DatabaseManager;
import com.deliverysystem.model.MenuItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for MenuItem entity.
 * Handles all database operations related to menu items.
 */
public class MenuItemDAO {

    /**
     * Insert a new menu item
     */
    public int insert(MenuItem menuItem) throws SQLException {
        String sql = "INSERT INTO menu_items (restaurant_id, item_name, description, category, price, availability, image_url) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, menuItem.getRestaurantId());
            pstmt.setString(2, menuItem.getItemName());
            pstmt.setString(3, menuItem.getDescription());
            pstmt.setString(4, menuItem.getCategory());
            pstmt.setBigDecimal(5, menuItem.getPrice());
            pstmt.setBoolean(6, menuItem.isAvailability());
            pstmt.setString(7, menuItem.getImageUrl());
            
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
     * Update an existing menu item
     */
    public boolean update(MenuItem menuItem) throws SQLException {
        String sql = "UPDATE menu_items SET restaurant_id = ?, item_name = ?, description = ?, category = ?, price = ?, availability = ?, image_url = ? WHERE menu_item_id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, menuItem.getRestaurantId());
            pstmt.setString(2, menuItem.getItemName());
            pstmt.setString(3, menuItem.getDescription());
            pstmt.setString(4, menuItem.getCategory());
            pstmt.setBigDecimal(5, menuItem.getPrice());
            pstmt.setBoolean(6, menuItem.isAvailability());
            pstmt.setString(7, menuItem.getImageUrl());
            pstmt.setInt(8, menuItem.getMenuItemId());
            
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Delete a menu item by ID
     */
    public boolean delete(int menuItemId) throws SQLException {
        String sql = "DELETE FROM menu_items WHERE menu_item_id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, menuItemId);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Find menu item by ID
     */
    public MenuItem findById(int menuItemId) throws SQLException {
        String sql = "SELECT * FROM menu_items WHERE menu_item_id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, menuItemId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMenuItem(rs);
                }
            }
        }
        return null;
    }

    /**
     * Get all menu items for a restaurant
     */
    public List<MenuItem> findByRestaurant(int restaurantId) throws SQLException {
        String sql = "SELECT * FROM menu_items WHERE restaurant_id = ? AND availability = TRUE ORDER BY category, item_name";
        List<MenuItem> menuItems = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, restaurantId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    menuItems.add(mapResultSetToMenuItem(rs));
                }
            }
        }
        return menuItems;
    }

    /**
     * Get all menu items (including unavailable)
     */
    public List<MenuItem> findAll() throws SQLException {
        String sql = "SELECT * FROM menu_items ORDER BY restaurant_id, category, item_name";
        List<MenuItem> menuItems = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                menuItems.add(mapResultSetToMenuItem(rs));
            }
        }
        return menuItems;
    }

    /**
     * Get menu items by category
     */
    public List<MenuItem> findByCategory(int restaurantId, String category) throws SQLException {
        String sql = "SELECT * FROM menu_items WHERE restaurant_id = ? AND category = ? AND availability = TRUE ORDER BY item_name";
        List<MenuItem> menuItems = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, restaurantId);
            pstmt.setString(2, category);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    menuItems.add(mapResultSetToMenuItem(rs));
                }
            }
        }
        return menuItems;
    }

    /**
     * Get all categories for a restaurant
     */
    public List<String> findCategoriesByRestaurant(int restaurantId) throws SQLException {
        String sql = "SELECT DISTINCT category FROM menu_items WHERE restaurant_id = ? ORDER BY category";
        List<String> categories = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, restaurantId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    categories.add(rs.getString("category"));
                }
            }
        }
        return categories;
    }

    /**
     * Update item availability
     */
    public boolean updateAvailability(int menuItemId, boolean availability) throws SQLException {
        String sql = "UPDATE menu_items SET availability = ? WHERE menu_item_id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setBoolean(1, availability);
            pstmt.setInt(2, menuItemId);
            
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Search menu items
     */
    public List<MenuItem> search(String searchTerm) throws SQLException {
        String sql = "SELECT * FROM menu_items WHERE item_name LIKE ? OR description LIKE ? ORDER BY item_name";
        List<MenuItem> menuItems = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + searchTerm + "%");
            pstmt.setString(2, "%" + searchTerm + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    menuItems.add(mapResultSetToMenuItem(rs));
                }
            }
        }
        return menuItems;
    }

    /**
     * Map ResultSet to MenuItem object
     */
    private MenuItem mapResultSetToMenuItem(ResultSet rs) throws SQLException {
        MenuItem menuItem = new MenuItem();
        menuItem.setMenuItemId(rs.getInt("menu_item_id"));
        menuItem.setRestaurantId(rs.getInt("restaurant_id"));
        menuItem.setItemName(rs.getString("item_name"));
        menuItem.setDescription(rs.getString("description"));
        menuItem.setCategory(rs.getString("category"));
        menuItem.setPrice(rs.getBigDecimal("price"));
        menuItem.setAvailability(rs.getBoolean("availability"));
        menuItem.setImageUrl(rs.getString("image_url"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            menuItem.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            menuItem.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return menuItem;
    }
}