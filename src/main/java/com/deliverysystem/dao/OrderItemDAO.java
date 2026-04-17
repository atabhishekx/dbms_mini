package com.deliverysystem.dao;

import com.deliverysystem.database.DatabaseManager;
import com.deliverysystem.model.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for OrderItem entity.
 * Handles all database operations related to order items.
 */
public class OrderItemDAO {

    /**
     * Insert a new order item
     */
    public int insert(OrderItem orderItem) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, menu_item_id, quantity, unit_price, subtotal) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, orderItem.getOrderId());
            pstmt.setInt(2, orderItem.getMenuItemId());
            pstmt.setInt(3, orderItem.getQuantity());
            pstmt.setBigDecimal(4, orderItem.getUnitPrice());
            pstmt.setBigDecimal(5, orderItem.getSubtotal());
            
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
     * Insert multiple order items in batch
     */
    public boolean insertBatch(List<OrderItem> orderItems) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, menu_item_id, quantity, unit_price, subtotal) VALUES (?, ?, ?, ?, ?)";
        
        Connection conn = null;
        try {
            conn = DatabaseManager.getInstance().getConnection();
            conn.setAutoCommit(false);
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (OrderItem item : orderItems) {
                    pstmt.setInt(1, item.getOrderId());
                    pstmt.setInt(2, item.getMenuItemId());
                    pstmt.setInt(3, item.getQuantity());
                    pstmt.setBigDecimal(4, item.getUnitPrice());
                    pstmt.setBigDecimal(5, item.getSubtotal());
                    pstmt.addBatch();
                }
                
                pstmt.executeBatch();
                conn.commit();
                return true;
            }
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    /**
     * Update an order item
     */
    public boolean update(OrderItem orderItem) throws SQLException {
        String sql = "UPDATE order_items SET order_id = ?, menu_item_id = ?, quantity = ?, unit_price = ?, subtotal = ? WHERE order_item_id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, orderItem.getOrderId());
            pstmt.setInt(2, orderItem.getMenuItemId());
            pstmt.setInt(3, orderItem.getQuantity());
            pstmt.setBigDecimal(4, orderItem.getUnitPrice());
            pstmt.setBigDecimal(5, orderItem.getSubtotal());
            pstmt.setInt(6, orderItem.getOrderItemId());
            
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Delete an order item
     */
    public boolean delete(int orderItemId) throws SQLException {
        String sql = "DELETE FROM order_items WHERE order_item_id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, orderItemId);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Delete all items for an order
     */
    public boolean deleteByOrderId(int orderId) throws SQLException {
        String sql = "DELETE FROM order_items WHERE order_id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, orderId);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Find order item by ID
     */
    public OrderItem findById(int orderItemId) throws SQLException {
        String sql = "SELECT * FROM order_items WHERE order_item_id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, orderItemId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToOrderItem(rs);
                }
            }
        }
        return null;
    }

    /**
     * Get all items for an order
     */
    public List<OrderItem> findByOrderId(int orderId) throws SQLException {
        String sql = "SELECT oi.*, mi.item_name, mi.description " +
                    "FROM order_items oi " +
                    "JOIN menu_items mi ON oi.menu_item_id = mi.menu_item_id " +
                    "WHERE oi.order_id = ?";
        List<OrderItem> orderItems = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, orderId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = mapResultSetToOrderItem(rs);
                    item.setItemName(rs.getString("item_name"));
                    item.setItemDescription(rs.getString("description"));
                    orderItems.add(item);
                }
            }
        }
        return orderItems;
    }

    /**
     * Map ResultSet to OrderItem object
     */
    private OrderItem mapResultSetToOrderItem(ResultSet rs) throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(rs.getInt("order_item_id"));
        orderItem.setOrderId(rs.getInt("order_id"));
        orderItem.setMenuItemId(rs.getInt("menu_item_id"));
        orderItem.setQuantity(rs.getInt("quantity"));
        orderItem.setUnitPrice(rs.getBigDecimal("unit_price"));
        orderItem.setSubtotal(rs.getBigDecimal("subtotal"));
        
        return orderItem;
    }
}