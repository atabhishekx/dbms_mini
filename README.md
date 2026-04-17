# 🚚 Delivery Management System

A comprehensive Java-based desktop application for managing food delivery operations. This system simulates a real-world food delivery platform similar to Swiggy or Zomato, with support for customers, restaurants, delivery agents, and administrators.

---

## 📋 Table of Contents

1. [Project Overview](#project-overview)
2. [Features](#features)
3. [System Architecture](#system-architecture)
4. [Technology Stack](#technology-stack)
5. [Prerequisites](#prerequisites)
6. [Installation & Setup](#installation--setup)
7. [Usage Guide](#usage-guide)
   - [Customer Features](#customer-features)
   - [Restaurant Features](#restaurant-features)
   - [Admin Features](#admin-features)
8. [Database Schema](#database-schema)
9. [Project Structure](#project-structure)
10. [FAQ & Troubleshooting](#faq--troubleshooting)
11. [Security & Updates](#security--updates)

---

## 📱 Project Overview

The **Delivery Management System** is an end-to-end solution for managing food delivery operations. It provides three distinct user roles with different functionalities:

- **Customers**: Browse restaurants, place orders, track deliveries, and make payments
- **Restaurants**: Manage menus, view orders, and update delivery status
- **Administrators**: Oversee system operations, manage users, and generate reports
- **Delivery Agents**: Accept deliveries and manage logistics

The system uses a **three-tier architecture** with a MySQL database backend, Swing-based GUI, and robust business logic layer (JDBC).

---

## ✨ Features

### 🛍️ Customer Features

| Feature | Description |
|---------|-------------|
| **User Registration** | Create account with email verification |
| **Browse Restaurants** | View list of all active restaurants with ratings |
| **View Menus** | Browse restaurant menus by category |
| **Add to Cart** | Select items and quantities, build shopping cart |
| **Place Orders** | Create orders with delivery address and special notes |
| **Track Orders** | Real-time order status tracking (PLACED → DELIVERED) |
| **Payment Methods** | Support for Cash, UPI, and Card payments |
| **Order History** | View past orders with details |
| **Rating & Review** | Rate restaurants and delivery experience |

**User Account:**
- Username: `customer1`
- Password: `password123`

---

### 🍽️ Restaurant/Owner Features

| Feature | Description |
|---------|-------------|
| **Restaurant Dashboard** | View restaurant overview and statistics |
| **Menu Management** | Add, update, delete menu items |
| **Item Categorization** | Organize items by category (Appetizers, Mains, Desserts, etc.) |
| **View Incoming Orders** | Real-time list of new customer orders |
| **Update Order Status** | Mark orders as PREPARING, OUT_FOR_DELIVERY, DELIVERED |
| **Pricing & Availability** | Set prices and toggle item availability |
| **Order Analytics** | View sales data and popular items |

**Restaurant Account:**
- Username: `restaurant1`
- Password: `password123`

---

### ⚙️ Admin Features

| Feature | Description |
|---------|-------------|
| **User Management** | View, activate, deactivate user accounts |
| **Restaurant Approval** | Verify and activate new restaurant registrations |
| **Delivery Agent Management** | Manage delivery personnel and assignments |
| **Order Management** | Full visibility into all system orders |
| **Payment Management** | Track and verify all payment transactions |
| **System Reports** | Generate sales, order, and delivery reports |
| **Revenue Analytics** | Track platform revenue and commissions |
| **System Audit** | View user activity logs and transactions |

**Admin Account:**
- Username: `admin`
- Password: `admin123`

---

## 🏗️ System Architecture

```
┌─────────────────────────────────────────────────────────┐
│           PRESENTATION LAYER (Swing GUI)               │
│  ┌──────────────┬──────────────┬──────────────┐         │
│  │   Customer   │ Restaurant   │    Admin     │         │
│  │   Interface  │  Interface   │  Interface   │         │
│  └──────────────┴──────────────┴──────────────┘         │
└─────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────┐
│           BUSINESS LOGIC LAYER (Services)              │
│  ┌──────────────┬──────────────┬──────────────┐         │
│  │ UserService  │ MenuService  │OrderService  │         │
│  └──────────────┴──────────────┴──────────────┘         │
└─────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────┐
│       DATA ACCESS LAYER (DAO/JDBC)                     │
│  ┌──────────────┬──────────────┬──────────────┐         │
│  │ User/Order   │ Menu/Payment │  Delivery    │         │
│  │    DAO       │     DAO      │    Agent DAO │         │
│  └──────────────┴──────────────┴──────────────┘         │
└─────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────┐
│              DATABASE LAYER                            │
│  ┌─────────────────────────────────────────────┐        │
│  │      MySQL Database (delivery_system)       │        │
│  │  users | restaurants | menu_items | orders  │        │
│  │  order_items | delivery_agents | payments   │        │
│  └─────────────────────────────────────────────┘        │
└─────────────────────────────────────────────────────────┘
```

---

## 💻 Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| **Language** | Java | 8+ |
| **Database** | MySQL | 8.0+ |
| **Database Driver** | MySQL Connector/J | 8.2.0 |
| **UI Framework** | Swing (JFC) | Java Built-in |
| **Data Persistence** | JDBC | Java Built-in |
| **Testing** | JUnit | 4.13.2 |
| **Build Tool** | Maven | 3.6+ |
| **IDE Recommendation** | IntelliJ IDEA / Eclipse | Latest |

---

## 📋 Prerequisites

Before running the application, ensure you have:

### System Requirements
- **Operating System**: Windows, macOS, or Linux
- **RAM**: Minimum 2GB (4GB recommended)
- **Disk Space**: 500MB free space

### Software Requirements
1. **Java Development Kit (JDK)**
   - Java 8 or higher
   - Download from: https://www.oracle.com/java/technologies/javase-downloads.html

2. **MySQL Server**
   - MySQL 8.0.33 or higher
   - Download from: https://www.mysql.com/downloads/
   - Verify installation: `mysql --version`

3. **Maven** (for building)
   - Maven 3.6 or higher
   - Download from: https://maven.apache.org/
   - Verify installation: `mvn --version`

4. **MySQL Workbench** (Optional, for database management)
   - Download from: https://www.mysql.com/products/workbench/

---

## 🔧 Installation & Setup

### Step 1: Clone or Download the Project
```bash
# Navigate to your desired directory
cd path/to/your/projects

# Clone repository (if applicable)
# git clone <repository-url>

# Or extract the ZIP file
unzip delivery-management-system.zip
cd delivery-management-system
```

### Step 2: Set Up MySQL Database

#### Option A: Using Command Line
```bash
# Start MySQL Server
mysql -u root -p

# Run the schema script
mysql -u root -p < src/main/resources/schema.sql

# Load sample data
mysql -u root -p delivery_system < src/main/resources/sample_data.sql
```

#### Option B: Using MySQL Workbench
1. Open MySQL Workbench
2. Create new connection to localhost:3306
3. Open `src/main/resources/schema.sql`
4. Execute the script (Ctrl+Shift+Enter)
5. Open `src/main/resources/sample_data.sql`
6. Execute to load sample data

### Step 3: Configure Database Properties

Edit `src/main/resources/database.properties`:

```properties
# Database Configuration
db.host=localhost
db.port=3306
db.name=delivery_system
db.username=root
db.password=your_mysql_password
db.driver=com.mysql.cj.jdbc.Driver
```

### Step 4: Build the Project

```bash
# Navigate to project root
cd /path/to/delivery-management-system

# Clean and build
mvn clean compile

# Package into JAR
mvn package
```

### Step 5: Run the Application

#### Option A: Using Maven
```bash
mvn exec:java -Dexec.mainClass="com.deliverysystem.ui.MainFrame"
```

#### Option B: Using JAR File
```bash
# After running mvn package, find the JAR in target/ directory
java -jar target/delivery-management-system-1.0.0.jar
```

#### Option C: Using IDE
1. Open project in IntelliJ IDEA or Eclipse
2. Right-click on `MainFrame.java` → Run

---

## 📖 Usage Guide

### 🌐 Application Startup

1. **Launch Application**: Run the JAR file or execute via Maven
2. **Login Screen**: You'll see the Delivery Management System login screen
3. **Select Role**: Login with appropriate credentials based on your role

```
┌─────────────────────────────────────┐
│  Delivery Management System         │
│                                     │
│  Username: [_______________]        │
│  Password: [_______________]        │
│  Role:     [Dropdown ▼]            │
│                                     │
│         [Login] [Exit]             │
└─────────────────────────────────────┘
```

---

### 🛍️ Customer Usage Guide

#### 1. **Registration**
```
Login Screen → Register Tab
├── Username: Create a unique username
├── Password: Enter secure password
├── Full Name: Your name
├── Email: Valid email address
├── Phone: Contact number
├── Address: Delivery address
└── Click "Register"
```

#### 2. **Browse & Order**
```
Customer Dashboard
├── Step 1: View Available Restaurants
│   └── See list of active restaurants with ratings
├── Step 2: Select Restaurant
│   └── Click on restaurant to view menu
├── Step 3: Browse Menu Items
│   ├── Items organized by category
│   ├── Each item shows: Name, Description, Price
│   └── Check availability status
├── Step 4: Add to Cart
│   ├── Select quantity
│   ├── Click "Add to Cart"
│   └── Item added to shopping cart
├── Step 5: Review Cart
│   ├── View all items
│   ├── Modify quantities
│   ├── See itemized total
│   └── Remove items if needed
└── Step 6: Checkout
    ├── Enter/Confirm delivery address
    ├── Add special instructions (notes)
    ├── Next Step: Select Payment Method
```

#### 3. **Payment**
```
Payment Options Available:
├── Cash on Delivery (COD)
├── UPI Payment
│   └── Supported: Google Pay, PhonePe, Paytm
└── Card Payment
    └── Debit/Credit cards accepted
```

#### 4. **Track Order**
```
Order Tracking:
├── View Order Status
│   ├── PLACED: Order confirmed
│   ├── PREPARING: Restaurant preparing
│   ├── OUT_FOR_DELIVERY: Driver on the way
│   └── DELIVERED: Order completed
├── Real-time Updates
├── Driver Contact Information
└── Estimated Delivery Time
```

#### 5. **Order History**
```
Customer Dashboard → Order History
├── View all past orders
├── Search by Order ID or Date
├── Download receipts
└── Reorder items from previous orders
```

---

### 🍽️ Restaurant Usage Guide

#### 1. **Login & Dashboard**
```
Restaurant Dashboard
├── Quick Stats
│   ├── Total Orders (Today/This Month)
│   ├── Revenue
│   └── Average Rating
├── Active Orders Section
├── Menu Management
└── Settings
```

#### 2. **Menu Management**
```
Menu Management Interface
├── Add New Item
│   ├── Item Name
│   ├── Category (Appetizer, Main, Dessert, Beverage)
│   ├── Description
│   ├── Price
│   ├── Availability (Toggle)
│   └── Save
├── Edit Existing Items
│   ├── Select Item
│   ├── Modify Details
│   └── Save Changes
└── Delete Items
    └── Confirm deletion
```

#### 3. **Order Management**
```
Incoming Orders Tab
├── View new orders in real-time
├── Order Details:
│   ├── Order ID
│   ├── Customer Name
│   ├── Items Ordered
│   ├── Total Amount
│   ├── Special Instructions
│   └── Order Time
├── Update Status:
│   ├── PLACED → PREPARING (Start cooking)
│   ├── PREPARING → OUT_FOR_DELIVERY (Ready for pickup)
│   └── OUT_FOR_DELIVERY → DELIVERED
└── Assign Delivery Agent
```

#### 4. **Analytics**
```
Restaurant Analytics
├── Sales Dashboard
│   ├── Daily Revenue
│   ├── Weekly/Monthly Trends
│   └── Top Items
├── Order Analytics
│   ├── Total Orders
│   ├── Average Order Value
│   └── Peak Hours
└── Customer Ratings
    ├── Average Rating
    ├── Review Comments
    └── Response Option
```

---

### ⚙️ Admin Usage Guide

#### 1. **Admin Dashboard**
```
Admin Dashboard
├── System Overview
│   ├── Total Users (Customers/Restaurants/Agents)
│   ├── Total Orders
│   ├── Platform Revenue
│   └── Active Users Online
├── Quick Actions
│   ├── User Management
│   ├── Restaurant Approval
│   ├── Delivery Agent Management
│   └── Payment Management
└── Reports & Analytics
```

#### 2. **User Management**
```
User Management Interface
├── View All Users
│   ├── Filter by: All/Customers/Restaurants/Admins
│   ├── Sort by: Name/Registration Date/Status
│   └── Search: Username/Email
├── User Details:
│   ├── Profile Information
│   ├── Account Status
│   ├── Registration Date
│   ├── Activity Log
│   └── Actions:
│       ├── View Details
│       ├── Activate/Deactivate
│       ├── Reset Password
│       └── Send Message
└── Bulk Actions
    ├── Deactivate Multiple Users
    └── Export User List
```

#### 3. **Restaurant Approval**
```
Restaurant Approval Workflow
├── Pending Registrations
│   ├── New Restaurants awaiting approval
│   ├── Review Documentation
│   ├── Verify Contact Information
│   └── Check Business License
├── Actions:
│   ├── APPROVE: Activate restaurant
│   ├── REJECT: Mark as inactive
│   └── REQUEST MORE INFO: Ask for details
└── Active Restaurants
    ├── List all approved restaurants
    ├── Suspend if needed
    └── View Performance Metrics
```

#### 4. **Delivery Agent Management**
```
Delivery Agent Management
├── All Delivery Agents
│   ├── Agent Name
│   ├── Phone Number
│   ├── Vehicle Number
│   ├── Status: AVAILABLE/BUSY/OFF_DUTY
│   ├── Current Location (GPS)
│   └── Ratings
├── Assign Orders
│   ├── Automatic Assignment Algorithm
│   ├── Manual Assignment
│   └── Load Balancing
├── Performance Tracking
│   ├── Deliveries Completed
│   ├── Average Rating
│   ├── Response Time
│   └── Incentives & Bonuses
└── Actions:
    ├── Add New Agent
    ├── Suspend Agent
    └── View Detailed Stats
```

#### 5. **Order Management**
```
System-Wide Order Management
├── View All Orders
│   ├── Filter by Status: PLACED/PREPARING/OUT_FOR_DELIVERY/DELIVERED/CANCELLED
│   ├── Filter by Date Range
│   ├── Filter by Restaurant/Customer
│   └── Search by Order ID
├── Order Details:
│   ├── Full Order Information
│   ├── Customer & Restaurant Details
│   ├── Assigned Delivery Agent
│   ├── Payment Information
│   └── Timeline/Timestamps
└── Order Actions:
    ├── Monitor Progress
    ├── Reassign Delivery Agent
    ├── Cancel Order (with refund)
    └── Generate Report
```

#### 6. **Payment Management**
```
Payment Processing & Verification
├── View All Payments
│   ├── Filter by Status: PENDING/COMPLETED/FAILED/REFUNDED
│   ├── Filter by Payment Method: CASH/UPI/CARD
│   ├── Date Range Filtering
│   └── Search by Transaction ID
├── Payment Details:
│   ├── Order ID
│   ├── Amount
│   ├── Method Used
│   ├── Restaurant Commission
│   ├── Platform Commission
│   └── Settlement Status
├── Actions:
│   ├── Mark as Completed
│   ├── Process Refund
│   ├── Verify Payment
│   └── Generate Receipt
└── Settlement
    ├── Automated Payout to Restaurants
    ├── Commission Calculation
    └── Daily/Weekly Settlement Reports
```

#### 7. **System Reports**
```
Analytics & Reporting
├── Sales Report
│   ├── Daily/Weekly/Monthly Sales
│   ├── Revenue by Restaurant
│   ├── Top Items
│   └── Export to Excel/PDF
├── Order Statistics
│   ├── Total Orders
│   ├── Average Order Value
│   ├── Cancellation Rate
│   └── Peak Hours Analysis
├── User Statistics
│   ├── New Registrations
│   ├── Active Users
│   ├── User Retention Rate
│   └── Geographic Distribution
└── Delivery Performance
    ├── Average Delivery Time
    ├── On-Time Delivery Rate
    ├── Agent Performance Ranking
    └── Customer Satisfaction Score
```

---

## 🗄️ Database Schema

### Core Tables

#### 1. **Users Table**
```sql
users
├── user_id (INT, PK, Auto-increment)
├── username (VARCHAR 50, UNIQUE)
├── password (VARCHAR 255)
├── full_name (VARCHAR 100)
├── email (VARCHAR 100, UNIQUE)
├── phone (VARCHAR 20)
├── address (TEXT)
├── role (ENUM: CUSTOMER, RESTAURANT, ADMIN)
├── created_at (TIMESTAMP)
├── updated_at (TIMESTAMP)
└── is_active (BOOLEAN)
```

#### 2. **Restaurants Table**
```sql
restaurants
├── restaurant_id (INT, PK, Auto-increment)
├── user_id (INT, FK → users)
├── restaurant_name (VARCHAR 100)
├── description (TEXT)
├── address (TEXT)
├── phone (VARCHAR 20)
├── rating (DECIMAL 3,2)
├── opening_time (TIME)
├── closing_time (TIME)
├── is_active (BOOLEAN)
└── created_at (TIMESTAMP)
```

#### 3. **Menu Items Table**
```sql
menu_items
├── menu_item_id (INT, PK, Auto-increment)
├── restaurant_id (INT, FK → restaurants)
├── item_name (VARCHAR 100)
├── description (TEXT)
├── category (VARCHAR 50)
├── price (DECIMAL 10,2)
├── availability (BOOLEAN)
├── image_url (VARCHAR 255)
├── created_at (TIMESTAMP)
└── updated_at (TIMESTAMP)
```

#### 4. **Orders Table**
```sql
orders
├── order_id (INT, PK, Auto-increment)
├── user_id (INT, FK → users)
├── restaurant_id (INT, FK → restaurants)
├── delivery_agent_id (INT, FK → delivery_agents)
├── order_status (ENUM: PLACED, PREPARING, OUT_FOR_DELIVERY, DELIVERED, CANCELLED)
├── total_amount (DECIMAL 10,2)
├── delivery_address (TEXT)
├── order_date (TIMESTAMP)
├── delivery_date (TIMESTAMP NULL)
└── notes (TEXT)
```

#### 5. **Order Items Table**
```sql
order_items
├── order_item_id (INT, PK, Auto-increment)
├── order_id (INT, FK → orders)
├── menu_item_id (INT, FK → menu_items)
├── quantity (INT)
├── unit_price (DECIMAL 10,2)
└── subtotal (DECIMAL 10,2)
```

#### 6. **Delivery Agents Table**
```sql
delivery_agents
├── agent_id (INT, PK, Auto-increment)
├── user_id (INT, FK → users)
├── agent_name (VARCHAR 100)
├── phone (VARCHAR 20)
├── vehicle_number (VARCHAR 20)
├── availability_status (ENUM: AVAILABLE, BUSY, OFF_DUTY)
├── current_latitude (DECIMAL 10,8)
├── current_longitude (DECIMAL 11,8)
└── created_at (TIMESTAMP)
```

#### 7. **Payments Table**
```sql
payments
├── payment_id (INT, PK, Auto-increment)
├── order_id (INT, FK → orders)
├── payment_method (ENUM: CASH, UPI, CARD)
├── amount (DECIMAL 10,2)
├── payment_status (ENUM: PENDING, COMPLETED, FAILED, REFUNDED)
├── transaction_id (VARCHAR 100, UNIQUE)
└── payment_date (TIMESTAMP)
```

### Database Views

#### View: Order Summary
```sql
view_order_summary
├── order_id
├── customer (username, full_name)
├── restaurant_name
├── order_status
├── total_amount
├── order_date
├── delivery_agent
├── payment_status
└── payment_method
```

---

## 📁 Project Structure

```
delivery-management-system/
│
├── src/
│   ├── main/
│   │   ├── java/com/deliverysystem/
│   │   │   ├── model/                    # Data Models
│   │   │   │   ├── User.java
│   │   │   │   ├── Restaurant.java
│   │   │   │   ├── MenuItem.java
│   │   │   │   ├── Order.java
│   │   │   │   ├── OrderItem.java
│   │   │   │   ├── Payment.java
│   │   │   │   ├── DeliveryAgent.java
│   │   │   │   └── CartItem.java
│   │   │   │
│   │   │   ├── dao/                     # Data Access Objects
│   │   │   │   ├── UserDAO.java
│   │   │   │   ├── RestaurantDAO.java
│   │   │   │   ├── MenuItemDAO.java
│   │   │   │   ├── OrderDAO.java
│   │   │   │   ├── OrderItemDAO.java
│   │   │   │   ├── PaymentDAO.java
│   │   │   │   └── DeliveryAgentDAO.java
│   │   │   │
│   │   │   ├── service/                 # Business Logic Layer
│   │   │   │   ├── UserService.java
│   │   │   │   ├── MenuService.java
│   │   │   │   └── OrderService.java
│   │   │   │
│   │   │   ├── database/                # Database Connection
│   │   │   │   └── DatabaseManager.java
│   │   │   │
│   │   │   ├── ui/                      # User Interface (Swing)
│   │   │   │   └── MainFrame.java       # Main Application GUI
│   │   │   │
│   │   │   └── util/                    # Utilities
│   │   │       ├── AppLogger.java       # Logging utility
│   │   │       └── ValidationUtils.java # Input validation
│   │   │
│   │   └── resources/
│   │       ├── database.properties      # Database config
│   │       ├── schema.sql              # Database schema
│   │       └── sample_data.sql         # Sample data
│   │
│   └── test/
│       └── java/                        # Unit tests (optional)
│
├── target/                              # Build output (generated)
│   ├── classes/
│   ├── test-classes/
│   └── delivery-management-system-1.0.0.jar
│
├── pom.xml                              # Maven configuration
├── README.md                            # This file
├── development.md                       # Development documentation
└── .gitignore                          # Git ignore rules
```

### Key Files Description

| File/Folder | Purpose |
|-------------|---------|
| `MainFrame.java` | Main GUI entry point - card-based layout for Login/Customer/Admin panels |
| `DatabaseManager.java` | Manages MySQL connections and database operations |
| `UserDAO.java` | CRUD operations for User table |
| `OrderService.java` | Business logic for order management (create, update, track) |
| `database.properties` | MySQL connection configuration |
| `schema.sql` | Database table creation script |
| `sample_data.sql` | Pre-populated test data (demo accounts, restaurants, items) |

---

## 👥 Sample Data & Demo Accounts

### Pre-loaded User Accounts

#### Customer Accounts
| Username | Password | Full Name | Role |
|----------|----------|-----------|------|
| customer1 | password123 | Rahul Kumar | CUSTOMER |
| customer2 | password123 | Priya Singh | CUSTOMER |
| customer3 | password123 | Amit Patel | CUSTOMER |

#### Restaurant Accounts
| Username | Password | Restaurant Name | Role |
|----------|----------|-----------------|------|
| restaurant1 | password123 | Pizza Palace | RESTAURANT |
| restaurant2 | password123 | Biryani Bazaar | RESTAURANT |
| restaurant3 | password123 | Veg Garden | RESTAURANT |

#### Admin Account
| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | ADMIN |

#### Delivery Agent Accounts
| Username | Password | Agent Name | Role |
|----------|----------|-----------|------|
| agent1 | password123 | Rajesh Singh | DELIVERY_AGENT |
| agent2 | password123 | Vikram Kumar | DELIVERY_AGENT |

---

## 🐛 FAQ & Troubleshooting

### Common Issues & Solutions

#### 1. **"Unable to Connect to Database" Error**
```
Problem: com.mysql.cj.jdbc.exceptions.CommunicationsException
Solution:
1. Verify MySQL Server is running
   - Windows: Check Services (mysql80)
   - Mac: brew services list | grep mysql
   - Linux: sudo systemctl status mysql
2. Verify database exists: mysql -u root -p -e "USE delivery_system;"
3. Check database.properties file has correct credentials
4. Ensure no firewall is blocking localhost:3306
```

#### 2. **"Table Already Exists" During Schema Setup**
```
Problem: You'll see error if running schema.sql multiple times
Solution:
1. Drop existing database: mysql -u root -p -e "DROP DATABASE delivery_system;"
2. Re-run schema.sql from scratch
3. Or modify schema.sql to use "DROP TABLE IF EXISTS"
```

#### 3. **Application Crashes on Startup**
```
Problem: NullPointerException or Connection refused
Solution:
1. Ensure Java is installed: java -version
2. Check JAVA_HOME environment variable: echo %JAVA_HOME% (Windows)
3. Rebuild project: mvn clean compile
4. Delete target folder and rebuild: rm -rf target && mvn package
```

#### 4. **Maven Build Fails - "Artefact Not Found"**
```
Problem: Could not find artifact com.mysql:mysql-connector-j:8.2.0
Solution:
1. Update Maven: mvn -version
2. Clear Maven cache: rm -rf ~/.m2/repository
3. Run: mvn clean install
```

#### 5. **GUI Not Displaying Properly**
```
Problem: Swing components not rendering correctly
Solution:
1. Update Java to latest version
2. Set UI Look & Feel: UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
3. Test on different JDK (11, 17, 21)
```

#### 6. **Sample Data Not Loading**
```
Problem: Tables are empty after running sample_data.sql
Solution:
1. Verify schema.sql ran successfully: SHOW TABLES;
2. Check if insert statements have correct table names
3. Run: mysql -u root -p delivery_system < src/main/resources/sample_data.sql
```

---

### Debugging Tips

#### Enable Detailed Logging
```java
// In AppLogger.java
import java.util.logging.Logger;
import java.util.logging.Level;

Logger logger = Logger.getLogger(YourClass.class.getName());
logger.log(Level.FINE, "Debug message");
```

#### Database Query Testing
```sql
-- Test database connectivity
SELECT VERSION();

-- Verify tables
SHOW TABLES;

-- Check sample data
SELECT COUNT(*) FROM users;
SELECT * FROM restaurants;
```

#### Maven Debugging
```bash
# Enable verbose output
mvn -X clean compile

# Skip tests
mvn clean compile -DskipTests

# Run with debugging
mvn exec:java@debug
```

---

## 🔐 Security & Updates

### Security Considerations

1. **Password Security**
   - Currently using plain text (for demo only - NOT PRODUCTION READY)
   - Recommended: Use BCrypt or Argon2 for password hashing
   - Implementation:
     ```java
     // Use: org.mindrot:jbcrypt:0.4
     String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
     boolean passwordMatch = BCrypt.checkpw(password, hashedPassword);
     ```

2. **SQL Injection Prevention**
   - Already using Prepared Statements in DAO classes ✅
   - Never use string concatenation for SQL queries

3. **Session Management**
   - Implement session timeouts (e.g., 30 minutes of inactivity)
   - Use HttpSession management

4. **Data Validation**
   - Use ValidationUtils.java for input validation
   - Validate on both client and server side

### Recommended Updates

1. **Upgrade to Java 17 or 21**
   ```bash
   # Modify pom.xml
   <source>21</source>
   <target>21</target>
   ```

2. **Add Security Framework**
   ```xml
   <!-- Spring Security (if moving to Spring) -->
   <dependency>
       <groupId>org.springframework.security</groupId>
       <artifactId>spring-security-core</artifactId>
       <version>6.0.0</version>
   </dependency>
   ```

3. **Add Logging Framework**
   ```xml
   <!-- SLF4J + Logback -->
   <dependency>
       <groupId>ch.qos.logback</groupId>
       <artifactId>logback-classic</artifactId>
       <version>1.4.5</version>
   </dependency>
   ```

### CVE Updates

**Current CVE Status**: ✅ All critical/high-severity CVEs fixed
- MySQL Connector/J: Updated to **8.2.0** (fixes CVE-2023-22102)
- JUnit: **4.13.2** - No known high-severity CVEs

---

## 📚 Additional Resources

### Documentation
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [Java Swing Tutorial](https://docs.oracle.com/javase/tutorial/uiswing/)
- [JDBC Best Practices](https://www.oracle.com/java/technologies/jdbc.html)

### Related Tools
- [MySQL Workbench](https://www.mysql.com/products/workbench/) - Database management
- [IntelliJ IDEA](https://www.jetbrains.com/idea/) - IDE
- [Apache Maven](https://maven.apache.org/) - Build tool

### Learning Resources
- **MVC Architecture**: https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller
- **JDBC Tutorial**: https://www.oracle.com/java/technologies/jdbc.html
- **Swing Components**: https://docs.oracle.com/javase/tutorial/uiswing/components/

---

## 📞 Support & Contact

### Issues & Bug Reports
- Document the issue clearly
- Include error messages and stack traces
- Specify Java version, OS, and MySQL version
- Provide steps to reproduce

### Contributing
- Follow existing code style
- Add comments for complex logic
- Test changes before submitting
- Update documentation

---

## 📄 License

This project is provided as-is for educational purposes in the AIML program.

---

## 🙏 Acknowledgments

- Developed as part of ST AIML Sem 4 DBMS Mini Project
- Built with Java Swing, MySQL, and JDBC
- Thank you to all contributors and testers

---

## ✅ Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0.0 | 2026-04-14 | Initial Release |
| | | • Core features implemented |
| | | • Three user roles (Customer, Restaurant, Admin) |
| | | • MySQL database setup |
| | | • Swing GUI interface |
| | | • CVE fixes applied |

---

**Last Updated**: April 14, 2026  
**Project Status**: ✅ Active Development  
**Java Version**: 8+  
**MySQL Version**: 8.0.33+

---

*For questions or clarifications, refer to the project files or development.md documentation.*
