package com.deliverysystem.service;

import com.deliverysystem.dao.MenuItemDAO;
import com.deliverysystem.dao.RestaurantDAO;
import com.deliverysystem.model.MenuItem;
import com.deliverysystem.model.Restaurant;

import java.sql.SQLException;
import java.util.List;

/**
 * Service layer for Menu and Restaurant operations.
 * Handles business logic for menu and restaurant management.
 */
public class MenuService {
    private final RestaurantDAO restaurantDAO;
    private final MenuItemDAO menuItemDAO;

    /**
     * Constructor
     */
    public MenuService() {
        this.restaurantDAO = new RestaurantDAO();
        this.menuItemDAO = new MenuItemDAO();
    }

    /**
     * Get all restaurants
     */
    public List<Restaurant> getAllRestaurants() throws SQLException {
        return restaurantDAO.findAll();
    }

    /**
     * Get restaurant by ID
     */
    public Restaurant getRestaurantById(int restaurantId) throws SQLException {
        return restaurantDAO.findById(restaurantId);
    }

    /**
     * Search restaurants
     */
    public List<Restaurant> searchRestaurants(String searchTerm) throws SQLException {
        return restaurantDAO.searchByName(searchTerm);
    }

    /**
     * Get menu items for a restaurant
     */
    public List<MenuItem> getMenuItems(int restaurantId) throws SQLException {
        return menuItemDAO.findByRestaurant(restaurantId);
    }

    /**
     * Get menu items by category
     */
    public List<MenuItem> getMenuItemsByCategory(int restaurantId, String category) throws SQLException {
        return menuItemDAO.findByCategory(restaurantId, category);
    }

    /**
     * Get categories for a restaurant
     */
    public List<String> getCategories(int restaurantId) throws SQLException {
        return menuItemDAO.findCategoriesByRestaurant(restaurantId);
    }

    /**
     * Get menu item by ID
     */
    public MenuItem getMenuItemById(int menuItemId) throws SQLException {
        return menuItemDAO.findById(menuItemId);
    }

    /**
     * Add menu item
     */
    public int addMenuItem(MenuItem menuItem) throws SQLException {
        if (menuItem.getRestaurantId() <= 0) {
            throw new IllegalArgumentException("Invalid restaurant");
        }
        
        if (menuItem.getItemName() == null || menuItem.getItemName().trim().isEmpty()) {
            throw new IllegalArgumentException("Item name is required");
        }
        
        if (menuItem.getPrice() == null || menuItem.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid price");
        }
        
        return menuItemDAO.insert(menuItem);
    }

    /**
     * Update menu item
     */
    public boolean updateMenuItem(MenuItem menuItem) throws SQLException {
        return menuItemDAO.update(menuItem);
    }

    /**
     * Toggle item availability
     */
    public boolean toggleAvailability(int menuItemId, boolean available) throws SQLException {
        return menuItemDAO.updateAvailability(menuItemId, available);
    }

    /**
     * Delete menu item
     */
    public boolean deleteMenuItem(int menuItemId) throws SQLException {
        return menuItemDAO.delete(menuItemId);
    }

    /**
     * Add restaurant
     */
    public int addRestaurant(Restaurant restaurant) throws SQLException {
        if (restaurant.getUserId() <= 0) {
            throw new IllegalArgumentException("Invalid user");
        }
        
        if (restaurant.getRestaurantName() == null || restaurant.getRestaurantName().trim().isEmpty()) {
            throw new IllegalArgumentException("Restaurant name is required");
        }
        
        return restaurantDAO.insert(restaurant);
    }

    /**
     * Update restaurant
     */
    public boolean updateRestaurant(Restaurant restaurant) throws SQLException {
        return restaurantDAO.update(restaurant);
    }

    /**
     * Delete restaurant
     */
    public boolean deleteRestaurant(int restaurantId) throws SQLException {
        return restaurantDAO.delete(restaurantId);
    }
}