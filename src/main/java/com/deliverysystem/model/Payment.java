package com.deliverysystem.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Payment model class representing payment transactions.
 */
public class Payment {
    private int paymentId;
    private int orderId;
    private PaymentMethod paymentMethod;
    private BigDecimal amount;
    private PaymentStatus paymentStatus;
    private String transactionId;
    private LocalDateTime paymentDate;

    /**
     * Default constructor
     */
    public Payment() {
        this.transactionId = generateTransactionId();
        this.paymentStatus = PaymentStatus.PENDING;
    }

    /**
     * Parameterized constructor
     */
    public Payment(int orderId, PaymentMethod paymentMethod, BigDecimal amount) {
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentStatus = PaymentStatus.PENDING;
        this.transactionId = generateTransactionId();
    }

    /**
     * Generate unique transaction ID
     */
    private String generateTransactionId() {
        return "TXN-" + LocalDateTime.now().toString().replace(":", "") + "-" + 
               UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    // Getters and Setters
    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * Mark payment as completed
     */
    public void markAsCompleted() {
        this.paymentStatus = PaymentStatus.COMPLETED;
        this.paymentDate = LocalDateTime.now();
    }

    /**
     * Mark payment as failed
     */
    public void markAsFailed() {
        this.paymentStatus = PaymentStatus.FAILED;
        this.paymentDate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", orderId=" + orderId +
                ", paymentMethod=" + paymentMethod +
                ", amount=" + amount +
                ", paymentStatus=" + paymentStatus +
                ", transactionId='" + transactionId + '\'' +
                '}';
    }

    /**
     * Enum for payment methods
     */
    public enum PaymentMethod {
        CASH("Cash on delivery"),
        UPI("UPI payment"),
        CARD("Card payment");

        private final String description;

        PaymentMethod(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * Enum for payment status
     */
    public enum PaymentStatus {
        PENDING("Payment pending"),
        COMPLETED("Payment completed"),
        FAILED("Payment failed"),
        REFUNDED("Payment refunded");

        private final String description;

        PaymentStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}