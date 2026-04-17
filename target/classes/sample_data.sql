-- =============================================================================
-- Delivery Management System - Sample Data
-- =============================================================================
-- This script inserts sample data for testing the application
-- =============================================================================

USE delivery_system;

-- =============================================================================
-- Insert Users (Customers, Restaurant Owners, Admin)
-- =============================================================================
INSERT INTO users (username, password, full_name, email, phone, address, role) VALUES
('john_doe', 'password123', 'John Doe', 'john@example.com', '9876543210', '123 Main Street, Mumbai', 'CUSTOMER'),
('jane_smith', 'password123', 'Jane Smith', 'jane@example.com', '9876543211', '456 Oak Avenue, Mumbai', 'CUSTOMER'),
('bob_wilson', 'password123', 'Bob Wilson', 'bob@example.com', '9876543212', '789 Pine Road, Mumbai', 'CUSTOMER'),
('admin', 'admin123', 'Admin User', 'admin@example.com', '9876543213', 'Admin Office', 'ADMIN'),
('restaurant1', 'rest123', 'Pizza Palace', 'pizza@palace.com', '9876543214', '100 Food Street, Mumbai', 'RESTAURANT'),
('restaurant2', 'rest123', 'Burger Hub', 'burger@hub.com', '9876543215', '200 Fast Food Lane, Mumbai', 'RESTAURANT');

-- =============================================================================
-- Insert Restaurants
-- =============================================================================
INSERT INTO restaurants (user_id, restaurant_name, description, address, phone, rating, opening_time, closing_time) VALUES
(5, 'Pizza Palace', 'Best pizza in town with authentic Italian recipes', '100 Food Street, Mumbai', '9876543214', 4.5, '10:00:00', '22:00:00'),
(6, 'Burger Hub', 'Juicy burgers and delicious sides', '200 Fast Food Lane, Mumbai', '9876543215', 4.2, '11:00:00', '23:00:00');

-- =============================================================================
-- Insert Menu Items
-- =============================================================================
INSERT INTO menu_items (restaurant_id, item_name, description, category, price) VALUES
(1, 'Margherita Pizza', 'Classic tomato and mozzarella pizza', 'Pizza', 350.00),
(1, 'Pepperoni Pizza', 'Pizza with pepperoni slices', 'Pizza', 420.00),
(1, 'Veggie Pizza', 'Loaded with fresh vegetables', 'Pizza', 380.00),
(1, 'Garlic Bread', 'Crispy garlic bread', 'Sides', 120.00),
(1, 'Caesar Salad', 'Fresh Caesar salad', 'Salad', 180.00),
(2, 'Classic Burger', 'Beef burger with cheese', 'Burgers', 250.00),
(2, 'Chicken Burger', 'Grilled chicken burger', 'Burgers', 280.00),
(2, 'Veggie Burger', 'Plant-based burger', 'Burgers', 260.00),
(2, 'French Fries', 'Crispy golden fries', 'Sides', 100.00),
(2, 'Onion Rings', 'Crispy onion rings', 'Sides', 90.00);

-- =============================================================================
-- Insert Delivery Agents
-- =============================================================================
INSERT INTO delivery_agents (user_id, agent_name, phone, vehicle_number, availability_status) VALUES
(1, 'Agent John', '9000111222', 'MH-01-AB-1234', 'AVAILABLE'),
(2, 'Agent Jane', '9000111333', 'MH-01-CD-5678', 'AVAILABLE'),
(3, 'Agent Bob', '9000111444', 'MH-01-EF-9012', 'BUSY');

-- =============================================================================
-- Insert Orders
-- =============================================================================
INSERT INTO orders (user_id, restaurant_id, delivery_agent_id, order_status, total_amount, delivery_address, order_date) VALUES
(1, 1, 1, 'DELIVERED', 820.00, '123 Main Street, Mumbai', '2026-04-10 12:30:00'),
(2, 2, 3, 'OUT_FOR_DELIVERY', 450.00, '456 Oak Avenue, Mumbai', '2026-04-13 13:00:00'),
(1, 2, NULL, 'PREPARING', 350.00, '123 Main Street, Mumbai', '2026-04-13 14:00:00');

-- =============================================================================
-- Insert Order Items
-- =============================================================================
INSERT INTO order_items (order_id, menu_item_id, quantity, unit_price, subtotal) VALUES
(1, 1, 1, 350.00, 350.00),
(1, 4, 1, 120.00, 120.00),
(1, 5, 2, 180.00, 360.00),
(2, 6, 1, 250.00, 250.00),
(2, 9, 2, 100.00, 200.00),
(3, 7, 1, 280.00, 280.00),
(3, 9, 1, 100.00, 100.00);

-- =============================================================================
-- Insert Payments
-- =============================================================================
INSERT INTO payments (order_id, payment_method, amount, payment_status, transaction_id) VALUES
(1, 'UPI', 820.00, 'COMPLETED', 'TXN-20260410-001'),
(2, 'CASH', 450.00, 'PENDING', 'TXN-20260413-002'),
(3, 'CARD', 350.00, 'PENDING', 'TXN-20260413-003');

-- Update order 3 delivery agent after agent table is populated
UPDATE orders SET delivery_agent_id = 2 WHERE order_id = 3;