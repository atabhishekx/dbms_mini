package com.deliverysystem.dao;

import com.deliverysystem.database.DatabaseManager;
import com.deliverysystem.model.Payment;
import com.deliverysystem.model.Payment.PaymentMethod;
import com.deliverysystem.model.Payment.PaymentStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Payment entity.
 * Handles all database operations related to payments.
 */
public class PaymentDAO {

    /**
     * Insert a new payment
     */
    public int insert(Payment payment) throws SQLException {
        String sql = "INSERT INTO payments (order_id, payment_method, amount, payment_status, transaction_id) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, payment.getOrderId());
            pstmt.setString(2, payment.getPaymentMethod().name());
            pstmt.setBigDecimal(3, payment.getAmount());
            pstmt.setString(4, payment.getPaymentStatus().name());
            pstmt.setString(5, payment.getTransactionId());
            
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
     * Update payment status
     */
    public boolean updateStatus(int paymentId, PaymentStatus status) throws SQLException {
        String sql = "UPDATE payments SET payment_status = ? WHERE payment_id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status.name());
            pstmt.setInt(2, paymentId);
            
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Mark payment as completed
     */
    public boolean markAsCompleted(int paymentId) throws SQLException {
        String sql = "UPDATE payments SET payment_status = 'COMPLETED', payment_date = NOW() WHERE payment_id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, paymentId);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Delete a payment
     */
    public boolean delete(int paymentId) throws SQLException {
        String sql = "DELETE FROM payments WHERE payment_id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, paymentId);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Find payment by ID
     */
    public Payment findById(int paymentId) throws SQLException {
        String sql = "SELECT * FROM payments WHERE payment_id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, paymentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPayment(rs);
                }
            }
        }
        return null;
    }

    /**
     * Find payment by order ID
     */
    public Payment findByOrderId(int orderId) throws SQLException {
        String sql = "SELECT * FROM payments WHERE order_id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, orderId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPayment(rs);
                }
            }
        }
        return null;
    }

    /**
     * Find payment by transaction ID
     */
    public Payment findByTransactionId(String transactionId) throws SQLException {
        String sql = "SELECT * FROM payments WHERE transaction_id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, transactionId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPayment(rs);
                }
            }
        }
        return null;
    }

    /**
     * Get all payments
     */
    public List<Payment> findAll() throws SQLException {
        String sql = "SELECT * FROM payments ORDER BY payment_date DESC";
        List<Payment> payments = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
        }
        return payments;
    }

    /**
     * Get payments by status
     */
    public List<Payment> findByStatus(PaymentStatus status) throws SQLException {
        String sql = "SELECT * FROM payments WHERE payment_status = ? ORDER BY payment_date DESC";
        List<Payment> payments = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status.name());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    payments.add(mapResultSetToPayment(rs));
                }
            }
        }
        return payments;
    }

    /**
     * Get payments by method
     */
    public List<Payment> findByMethod(PaymentMethod method) throws SQLException {
        String sql = "SELECT * FROM payments WHERE payment_method = ? ORDER BY payment_date DESC";
        List<Payment> payments = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, method.name());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    payments.add(mapResultSetToPayment(rs));
                }
            }
        }
        return payments;
    }

    /**
     * Get total revenue (completed payments)
     */
    public double getTotalRevenue() throws SQLException {
        String sql = "SELECT SUM(amount) as total FROM payments WHERE payment_status = 'COMPLETED'";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
        }
        return 0.0;
    }

    /**
     * Map ResultSet to Payment object
     */
    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setPaymentId(rs.getInt("payment_id"));
        payment.setOrderId(rs.getInt("order_id"));
        payment.setPaymentMethod(PaymentMethod.valueOf(rs.getString("payment_method")));
        payment.setAmount(rs.getBigDecimal("amount"));
        payment.setPaymentStatus(PaymentStatus.valueOf(rs.getString("payment_status")));
        payment.setTransactionId(rs.getString("transaction_id"));
        
        Timestamp paymentDate = rs.getTimestamp("payment_date");
        if (paymentDate != null) {
            payment.setPaymentDate(paymentDate.toLocalDateTime());
        }
        
        return payment;
    }
}