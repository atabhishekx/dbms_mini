-- =============================================================================
-- Delivery Management System - Database Schema
-- =============================================================================
-- This script creates all necessary tables for the Delivery Management System
-- =============================================================================

-- Drop database if exists and create new one
DROP DATABASE IF EXISTS delivery_system;
CREATE DATABASE delivery_system;
USE delivery_system;

-- =============================================================================
-- TABLE 1: Users (Customer details)
-- =============================================================================
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    address TEXT,
    role ENUM('CUSTOMER', 'RESTAURANT', 'ADMIN') NOT NULL DEFAULT 'CUSTOMER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    CONSTRAINT chk_username CHECK (username != ''),
    CONSTRAINT chk_email CHECK (email LIKE '%@%')
) ENGINE=InnoDB;

-- =============================================================================
-- TABLE 2: Restaurants
-- =============================================================================
CREATE TABLE restaurants (
    restaurant_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    restaurant_name VARCHAR(100) NOT NULL,
    description TEXT,
    address TEXT NOT NULL,
    phone VARCHAR(20) NOT NULL,
    rating DECIMAL(3,2) DEFAULT 0.00,
    opening_time TIME NOT NULL,
    closing_time TIME NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- =============================================================================
-- TABLE 3: Menu Items
-- =============================================================================
CREATE TABLE menu_items (
    menu_item_id INT PRIMARY KEY AUTO_INCREMENT,
    restaurant_id INT NOT NULL,
    item_name VARCHAR(100) NOT NULL,
    description TEXT,
    category VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    availability BOOLEAN DEFAULT TRUE,
    image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(restaurant_id) ON DELETE CASCADE,
    CONSTRAINT chk_price CHECK (price > 0)
) ENGINE=InnoDB;

-- =============================================================================
-- TABLE 4: Orders
-- =============================================================================
CREATE TABLE orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    restaurant_id INT NOT NULL,
    delivery_agent_id INT,
    order_status ENUM('PLACED', 'PREPARING', 'OUT_FOR_DELIVERY', 'DELIVERED', 'CANCELLED') DEFAULT 'PLACED',
    total_amount DECIMAL(10,2) NOT NULL,
    delivery_address TEXT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    delivery_date TIMESTAMP NULL,
    notes TEXT,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(restaurant_id) ON DELETE CASCADE,
    FOREIGN KEY (delivery_agent_id) REFERENCES delivery_agents(agent_id) ON DELETE SET NULL,
    CONSTRAINT chk_total CHECK (total_amount > 0)
) ENGINE=InnoDB;

-- =============================================================================
-- TABLE 5: Order Items (Items in each order)
-- =============================================================================
CREATE TABLE order_items (
    order_item_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    menu_item_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    unit_price DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(menu_item_id) ON DELETE CASCADE,
    CONSTRAINT chk_quantity CHECK (quantity > 0),
    CONSTRAINT chk_subtotal CHECK (subtotal > 0)
) ENGINE=InnoDB;

-- =============================================================================
-- TABLE 6: Delivery Agents
-- =============================================================================
CREATE TABLE delivery_agents (
    agent_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    agent_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    vehicle_number VARCHAR(20),
    availability_status ENUM('AVAILABLE', 'BUSY', 'OFF_DUTY') DEFAULT 'AVAILABLE',
    current_latitude DECIMAL(10,8),
    current_longitude DECIMAL(11,8),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- =============================================================================
-- TABLE 7: Payments
-- =============================================================================
CREATE TABLE payments (
    payment_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    payment_method ENUM('CASH', 'UPI', 'CARD') NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_status ENUM('PENDING', 'COMPLETED', 'FAILED', 'REFUNDED') DEFAULT 'PENDING',
    transaction_id VARCHAR(100) UNIQUE,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    CONSTRAINT chk_amount CHECK (amount > 0)
) ENGINE=InnoDB;

-- =============================================================================
-- VIEW 1: Order Summary (for Admin Reports)
-- =============================================================================
CREATE VIEW view_order_summary AS
SELECT 
    o.order_id,
    u.username,
    u.full_name AS customer_name,
    r.restaurant_name,
    o.order_status,
    o.total_amount,
    o.order_date,
    da.agent_name AS delivery_agent,
    p.payment_status,
    p.payment_method
FROM orders o
JOIN users u ON o.user_id = u.user_id
JOIN restaurants r ON o.restaurant_id = r.restaurant_id
LEFT JOIN delivery_agents da ON o.delivery_agent_id = da.agent_id
LEFT JOIN payments p ON o.order_id = p.order_id;

-- =============================================================================
-- VIEW 2: Daily Sales Report
-- =============================================================================
CREATE VIEW view_daily_sales AS
SELECT 
    DATE(order_date) AS order_date,
    COUNT(order_id) AS total_orders,
    SUM(total_amount) AS total_revenue,
    AVG(total_amount) AS average_order_value
FROM orders
WHERE order_status = 'DELIVERED'
GROUP BY DATE(order_date);

-- =============================================================================
-- INDEXES for Performance
-- =============================================================================
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_restaurant_id ON orders(restaurant_id);
CREATE INDEX idx_orders_status ON orders(order_status);
CREATE INDEX idx_orders_date ON orders(order_date);
CREATE INDEX idx_menu_items_restaurant ON menu_items(restaurant_id);
CREATE INDEX idx_menu_items_category ON menu_items(category);
CREATE INDEX idx_delivery_agents_status ON delivery_agents(availability_status);
CREATE INDEX idx_payments_order ON payments(order_id);