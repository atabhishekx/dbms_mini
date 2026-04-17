package com.deliverysystem.dao;

import com.deliverysystem.database.DatabaseManager;
import com.deliverysystem.model.Order;
import com.deliverysystem.model.OrderItem;
import com.deliverysystem.model.Order.OrderStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Order entity.
 * Handles all database operations related to orders.
 */
public class OrderDAO {

    /**
     * Insert a new order
     */
    public int insert(Order order) throws SQLException {
        String sql = "INSERT INTO orders (user_id, restaurant_id, delivery_agent_id, order_status, total_amount, delivery_address, notes) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, order.getUserId());
            pstmt.setInt(2, order.getRestaurantId());
            if (order.getDeliveryAgentId() != null) {
                pstmt.setInt(3, order.getDeliveryAgentId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            pstmt.setString(4, order.getOrderStatus().name());
            pstmt.setBigDecimal(5, order.getTotalAmount());
            pstmt.setString(6, order.getDeliveryAddress());
            pstmt.setString(7, order.getNotes());
            
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
     * Update an existing order
     */
    public boolean update(Order order) throws SQLException {
        String sql = "UPDATE orders SET user_id = ?, restaurant_id = ?, delivery_agent_id = ?, order_status = ?, total_amount = ?, delivery_address = ?, notes = ? WHERE order_id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, order.getUserId());
            pstmt.setInt(2, order.getRestaurantId());
            if (order.getDeliveryAgentId() != null) {
                pstmt.setInt(3, order.getDeliveryAgentId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            pstmt.setString(4, order.getOrderStatus().name());
            pstmt.setBigDecimal(5, order.getTotalAmount());
            pstmt.setString(6, order.getDeliveryAddress());
            pstmt.setString(7, order.getNotes());
            pstmt.setInt(8, order.getOrderId());
            
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Update order status
     */
    public boolean updateStatus(int orderId, OrderStatus status) throws SQLException {
        String sql = "UPDATE orders SET order_status = ? WHERE order_id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status.name());
            pstmt.setInt(2, orderId);
            
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Assign delivery agent to order
     */
    public boolean assignDeliveryAgent(int orderId, int agentId) throws SQLException {
        String sql = "UPDATE orders SET delivery_agent_id = ? WHERE order_id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, agentId);
            pstmt.setInt(2, orderId);
            
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Delete an order by ID
     */
    public boolean delete(int orderId) throws SQLException {
        String sql = "DELETE FROM orders WHERE order_id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, orderId);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Find order by ID
     */
    public Order findById(int orderId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, orderId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToOrder(rs);
                }
            }
        }
        return null;
    }

    /**
     * Get all orders
     */
    public List<Order> findAll() throws SQLException {
        String sql = "SELECT * FROM orders ORDER BY order_date DESC";
        List<Order> orders = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
        }
        return orders;
    }

    /**
     * Get orders by user
     */
    public List<Order> findByUser(int userId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC";
        List<Order> orders = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSetToOrder(rs));
                }
            }
        }
        return orders;
    }

    /**
     * Get orders by restaurant
     */
    public List<Order> findByRestaurant(int restaurantId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE restaurant_id = ? ORDER BY order_date DESC";
        List<Order> orders = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, restaurantId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSetToOrder(rs));
                }
            }
        }
        return orders;
    }

    /**
     * Get orders by status
     */
    public List<Order> findByStatus(OrderStatus status) throws SQLException {
        String sql = "SELECT * FROM orders WHERE order_status = ? ORDER BY order_date DESC";
        List<Order> orders = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status.name());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSetToOrder(rs));
                }
            }
        }
        return orders;
    }

    /**
     * Get orders by delivery agent
     */
    public List<Order> findByDeliveryAgent(int agentId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE delivery_agent_id = ? ORDER BY order_date DESC";
        List<Order> orders = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, agentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSetToOrder(rs));
                }
            }
        }
        return orders;
    }

    /**
     * Map ResultSet to Order object
     */
    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setUserId(rs.getInt("user_id"));
        order.setRestaurantId(rs.getInt("restaurant_id"));
        
        int agentId = rs.getInt("delivery_agent_id");
        if (!rs.wasNull()) {
            order.setDeliveryAgentId(agentId);
        }
        
        order.setOrderStatus(OrderStatus.valueOf(rs.getString("order_status")));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setDeliveryAddress(rs.getString("delivery_address"));
        
        Timestamp orderDate = rs.getTimestamp("order_date");
        if (orderDate != null) {
            order.setOrderDate(orderDate.toLocalDateTime());
        }
        
        Timestamp deliveryDate = rs.getTimestamp("delivery_date");
        if (deliveryDate != null) {
            order.setDeliveryDate(deliveryDate.toLocalDateTime());
        }
        
        order.setNotes(rs.getString("notes"));
        
        return order;
    }
}