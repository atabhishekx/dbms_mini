package com.deliverysystem.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DeliveryAgent model class representing delivery personnel.
 */
public class DeliveryAgent {
    private int agentId;
    private int userId;
    private String agentName;
    private String phone;
    private String vehicleNumber;
    private AgentStatus availabilityStatus;
    private BigDecimal currentLatitude;
    private BigDecimal currentLongitude;
    private LocalDateTime createdAt;

    /**
     * Default constructor
     */
    public DeliveryAgent() {
    }

    /**
     * Parameterized constructor
     */
    public DeliveryAgent(int agentId, int userId, String agentName, 
                        String phone, String vehicleNumber, AgentStatus availabilityStatus) {
        this.agentId = agentId;
        this.userId = userId;
        this.agentName = agentName;
        this.phone = phone;
        this.vehicleNumber = vehicleNumber;
        this.availabilityStatus = availabilityStatus;
    }

    // Getters and Setters
    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public AgentStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(AgentStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public BigDecimal getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(BigDecimal currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public BigDecimal getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(BigDecimal currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Check if agent is available for delivery
     */
    public boolean isAvailable() {
        return availabilityStatus == AgentStatus.AVAILABLE;
    }

    @Override
    public String toString() {
        return "DeliveryAgent{" +
                "agentId=" + agentId +
                ", agentName='" + agentName + '\'' +
                ", phone='" + phone + '\'' +
                ", vehicleNumber='" + vehicleNumber + '\'' +
                ", availabilityStatus=" + availabilityStatus +
                '}';
    }

    /**
     * Enum for agent availability status
     */
    public enum AgentStatus {
        AVAILABLE("Available for delivery"),
        BUSY("Currently on delivery"),
        OFF_DUTY("Not on duty");

        private final String description;

        AgentStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}