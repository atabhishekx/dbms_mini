package com.deliverysystem.dao;

import com.deliverysystem.database.DatabaseManager;
import com.deliverysystem.model.DeliveryAgent;
import com.deliverysystem.model.DeliveryAgent.AgentStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for DeliveryAgent entity.
 * Handles all database operations related to delivery agents.
 */
public class DeliveryAgentDAO {

    /**
     * Insert a new delivery agent
     */
    public int insert(DeliveryAgent agent) throws SQLException {
        String sql = "INSERT INTO delivery_agents (user_id, agent_name, phone, vehicle_number, availability_status, current_latitude, current_longitude) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, agent.getUserId());
            pstmt.setString(2, agent.getAgentName());
            pstmt.setString(3, agent.getPhone());
            pstmt.setString(4, agent.getVehicleNumber());
            pstmt.setString(5, agent.getAvailabilityStatus().name());
            
            if (agent.getCurrentLatitude() != null) {
                pstmt.setBigDecimal(6, agent.getCurrentLatitude());
            } else {
                pstmt.setNull(6, Types.DECIMAL);
            }
            
            if (agent.getCurrentLongitude() != null) {
                pstmt.setBigDecimal(7, agent.getCurrentLongitude());
            } else {
                pstmt.setNull(7, Types.DECIMAL);
            }
            
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
     * Update an existing delivery agent
     */
    public boolean update(DeliveryAgent agent) throws SQLException {
        String sql = "UPDATE delivery_agents SET user_id = ?, agent_name = ?, phone = ?, vehicle_number = ?, availability_status = ?, current_latitude = ?, current_longitude = ? WHERE agent_id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, agent.getUserId());
            pstmt.setString(2, agent.getAgentName());
            pstmt.setString(3, agent.getPhone());
            pstmt.setString(4, agent.getVehicleNumber());
            pstmt.setString(5, agent.getAvailabilityStatus().name());
            
            if (agent.getCurrentLatitude() != null) {
                pstmt.setBigDecimal(6, agent.getCurrentLatitude());
            } else {
                pstmt.setNull(6, Types.DECIMAL);
            }
            
            if (agent.getCurrentLongitude() != null) {
                pstmt.setBigDecimal(7, agent.getCurrentLongitude());
            } else {
                pstmt.setNull(7, Types.DECIMAL);
            }
            
            pstmt.setInt(8, agent.getAgentId());
            
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Update agent availability status
     */
    public boolean updateStatus(int agentId, AgentStatus status) throws SQLException {
        String sql = "UPDATE delivery_agents SET availability_status = ? WHERE agent_id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status.name());
            pstmt.setInt(2, agentId);
            
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Delete a delivery agent
     */
    public boolean delete(int agentId) throws SQLException {
        String sql = "DELETE FROM delivery_agents WHERE agent_id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, agentId);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Find agent by ID
     */
    public DeliveryAgent findById(int agentId) throws SQLException {
        String sql = "SELECT * FROM delivery_agents WHERE agent_id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, agentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDeliveryAgent(rs);
                }
            }
        }
        return null;
    }

    /**
     * Find agent by user ID
     */
    public DeliveryAgent findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM delivery_agents WHERE user_id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDeliveryAgent(rs);
                }
            }
        }
        return null;
    }

    /**
     * Get all delivery agents
     */
    public List<DeliveryAgent> findAll() throws SQLException {
        String sql = "SELECT * FROM delivery_agents ORDER BY agent_name";
        List<DeliveryAgent> agents = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                agents.add(mapResultSetToDeliveryAgent(rs));
            }
        }
        return agents;
    }

    /**
     * Get available agents
     */
    public List<DeliveryAgent> findAvailable() throws SQLException {
        String sql = "SELECT * FROM delivery_agents WHERE availability_status = 'AVAILABLE' ORDER BY agent_name";
        List<DeliveryAgent> agents = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                agents.add(mapResultSetToDeliveryAgent(rs));
            }
        }
        return agents;
    }

    /**
     * Get agents by status
     */
    public List<DeliveryAgent> findByStatus(AgentStatus status) throws SQLException {
        String sql = "SELECT * FROM delivery_agents WHERE availability_status = ? ORDER BY agent_name";
        List<DeliveryAgent> agents = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status.name());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    agents.add(mapResultSetToDeliveryAgent(rs));
                }
            }
        }
        return agents;
    }

    /**
     * Map ResultSet to DeliveryAgent object
     */
    private DeliveryAgent mapResultSetToDeliveryAgent(ResultSet rs) throws SQLException {
        DeliveryAgent agent = new DeliveryAgent();
        agent.setAgentId(rs.getInt("agent_id"));
        agent.setUserId(rs.getInt("user_id"));
        agent.setAgentName(rs.getString("agent_name"));
        agent.setPhone(rs.getString("phone"));
        agent.setVehicleNumber(rs.getString("vehicle_number"));
        agent.setAvailabilityStatus(AgentStatus.valueOf(rs.getString("availability_status")));
        
        Object latitude = rs.getObject("current_latitude");
        if (latitude != null) {
            agent.setCurrentLatitude((java.math.BigDecimal) latitude);
        }
        
        Object longitude = rs.getObject("current_longitude");
        if (longitude != null) {
            agent.setCurrentLongitude((java.math.BigDecimal) longitude);
        }
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            agent.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return agent;
    }
}