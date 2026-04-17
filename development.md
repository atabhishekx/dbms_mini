# Delivery Management System - Development Documentation

## 1. Project Overview

The Delivery Management System is a comprehensive Java-based desktop application that simulates a food delivery platform similar to Swiggy or Zomato. The system manages orders, restaurants, menus, delivery agents, and payments using MySQL as the backend database.

## 2. Technology Stack

- **Language**: Java 8+
- **Database**: MySQL 8.0
- **UI Framework**: Swing (Java Foundation Classes)
- **JDBC**: MySQL Connector/J 8.0
- **Architecture**: Three-Tier (Presentation, Business Logic, Data Layer)

## 3. Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── deliverysystem/
│   │           ├── model/          # Entity classes
│   │           ├── dao/           # Data Access Objects
│   │           ├── service/        # Business Logic
│   │           ├── ui/            # User Interface
│   │           ├── util/          # Utilities
│   │           └── database/      # DB Connection
│   └── resources/
│       ├── schema.sql        # Database schema
│       ├── sample_data.sql  # Test data
│       └── database.properties
└── test/                   # Test files
```

## 4. Database Design

### 4.1 Tables

| Table | Purpose |
|-------|---------|
| `users` | Customer, Restaurant owner, and Admin accounts |
| `restaurants` | Restaurant information |
| `menu_items` | Food items offered by restaurants |
| `orders` | Customer orders |
| `order_items` | Items in each order |
| `delivery_agents` | Delivery personnel |
| `payments` | Payment transactions |

### 4.2 Relationships

- **Users** 1:N **Restaurants** (One user can own multiple restaurants)
- **Restaurants** 1:N **Menu Items**
- **Users** 1:N **Orders** (One customer can place multiple orders)
- **Restaurants** 1:N **Orders**
- **Orders** 1:N **Order Items**
- **Menu Items** 1:N **Order Items**
- **Delivery Agents** 1:N **Orders**
- **Orders** 1:1 **Payments**

### 4.3 Views

- `view_order_summary`: Complete order details with user, restaurant, and payment info
- `view_daily_sales`: Daily sales statistics

### 4.4 Indexes

Indexes are created on frequently queried columns for performance optimization.

## 5. Application Architecture

### 5.1 Three-Tier Architecture

```
┌─────────────────┐
│  Presentation   │  ← UI Layer (Swing)
│    Layer       │
└────────┬────────┘
         │
┌────────▼────────┐
│ Application   │  ← Service Layer
│    Layer     │    (Business Logic)
└──────┬───────┘
       │
┌──────▼───────┐
│    Data      │  ← DAO Layer
│    Layer     │    (MySQL/JDBC)
└──────────────┘
```

### 5.2 Key Components

#### Model Layer (`model/`)
- `User.java` - User entity with roles (CUSTOMER, RESTAURANT, ADMIN)
- `Restaurant.java` - Restaurant details
- `MenuItem.java` - Menu item with pricing
- `Order.java` - Order with status tracking
- `OrderItem.java` - Individual items in order
- `DeliveryAgent.java` - Delivery personnel
- `Payment.java` - Payment transactions
- `CartItem.java` - Shopping cart items

#### DAO Layer (`dao/`)
- `UserDAO.java` - User CRUD operations
- `RestaurantDAO.java` - Restaurant CRUD operations
- `MenuItemDAO.java` - Menu item CRUD operations
- `OrderDAO.java` - Order CRUD operations
- `OrderItemDAO.java` - Order item CRUD operations
- `DeliveryAgentDAO.java` - Agent CRUD operations
- `PaymentDAO.java` - Payment CRUD operations

#### Service Layer (`service/`)
- `UserService.java` - User authentication and management
- `MenuService.java` - Restaurant and menu management
- `OrderService.java` - Order processing and agent assignment

#### UI Layer (`ui/`)
- `MainFrame.java` - Main application window

## 6. Database Operations Covered

### 6.1 DDL (Data Definition Language)
- CREATE TABLE with constraints
- Primary keys, foreign keys
- NOT NULL constraints
- ENUM types

### 6.2 DML (Data Manipulation Language)
- INSERT - Add new records
- UPDATE - Modify existing records
- DELETE - Remove records

### 6.3 DQL (Data Query Language)
- SELECT with WHERE clauses
- JOINs (INNER, LEFT)
- Aggregate functions (SUM, COUNT, AVG)
- GROUP BY
- ORDER BY

### 6.4 Views
- Order summary view
- Daily sales view

### 6.5 Transactions
- Order placement with order items (batch insert)

## 7. Security Features

### 7.1 SQL Injection Prevention
- Prepared statements used throughout
- Parameterized queries

### 7.2 Input Validation
- Email format validation
- Phone number validation
- Username/password validation

### 7.3 Role-based Access
- Different UI for CUSTOMER, RESTAURANT, ADMIN

## 8. Workflow

### 8.1 Customer Order Flow
1. Login/Register
2. Browse Restaurants
3. Select Restaurant
4. Browse Menu
5. Add Items to Cart
6. Enter Delivery Address
7. Select Payment Method
8. Place Order
9. Track Order Status

### 8.2 Admin Flow
1. Login as Admin
2. View All Orders
3. Update Order Status
4. View Delivery Agents
5. Assign Agent to Order

## 9. Setup Instructions

### 9.1 Prerequisites
- Java JDK 8 or higher
- MySQL 8.0 or higher
- MySQL Connector/J 8.0

### 9.2 Database Setup
1. Create database: `CREATE DATABASE delivery_system;`
2. Run schema: `source schema.sql`
3. Load sample data: `source sample_data.sql`

### 9.3 Configuration
Update `database.properties` with your MySQL credentials:
```properties
db.url=jdbc:mysql://localhost:3306/delivery_system
db.username=root
db.password=your_password
```

### 9.4 Compile and Run
```bash
# Compile
javac -cp "lib/*:src/main/java" -d out src/main/java/com/deliverysystem/**/*.java

# Run
java -cp "out:lib/*:src/main/resources" com.deliverysystem.ui.MainFrame
```

## 10. Features Implemented

### 10.1 User Management
- User registration
- User login/authentication
- Role-based access

### 10.2 Restaurant Management
- View restaurants
- Search restaurants
- View menus

### 10.3 Order Management
- Add to cart
- Place order
- View order history
- Track order status

### 10.4 Delivery Management
- View available agents
- Assign agent to order
- Update delivery status

### 10.5 Payment
- Multiple payment methods (Cash, UPI, Card)
- Payment status tracking

### 10.6 Admin Features
- View all orders
- Update order status
- Assign delivery agents

## 11. Scalability Considerations

### 11.1 Modular Design
- Separate layers allow easy expansion
- New features can be added without modifying existing code

### 11.2 Database Design
- Normalized tables reduce redundancy
- Indexes improve query performance

### 11.3 Future Enhancements
- Add more payment gateways
- GPS tracking integration
- Mobile app support
- Cloud deployment
- AI-based recommendations

## 12. Project Files Summary

| File | Description |
|------|------------|
| `schema.sql` | MySQL database schema |
| `sample_data.sql` | Sample test data |
| `database.properties` | Database configuration |
| `User.java` | User model |
| `Restaurant.java` | Restaurant model |
| `MenuItem.java` | Menu item model |
| `Order.java` | Order model |
| `OrderItem.java` | Order item model |
| `DeliveryAgent.java` | Delivery agent model |
| `Payment.java` | Payment model |
| `CartItem.java` | Cart item model |
| `UserDAO.java` | User data access |
| `RestaurantDAO.java` | Restaurant data access |
| `MenuItemDAO.java` | Menu item data access |
| `OrderDAO.java` | Order data access |
| `OrderItemDAO.java` | Order item data access |
| `DeliveryAgentDAO.java` | Agent data access |
| `PaymentDAO.java` | Payment data access |
| `DatabaseManager.java` | Database connection |
| `UserService.java` | User business logic |
| `MenuService.java` | Menu business logic |
| `OrderService.java` | Order business logic |
| `MainFrame.java` | Main UI |
| `AppLogger.java` | Logging utility |
| `ValidationUtils.java` | Validation utility |

## 13. Conclusion

This Delivery Management System demonstrates:
- Complete DBMS implementation with MySQL
- Java JDBC integration
- Three-tier architecture
- Modular and scalable design
- Real-world delivery workflow simulation
- Role-based access control

The project follows industry best practices and provides a solid foundation for further development.