package com.deliverysystem.ui;

import com.deliverysystem.model.*;
import com.deliverysystem.model.MenuItem;
import com.deliverysystem.model.Order.OrderStatus;
import com.deliverysystem.model.Payment.PaymentMethod;
import com.deliverysystem.service.MenuService;
import com.deliverysystem.service.OrderService;
import com.deliverysystem.service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main application frame for the Delivery Management System.
 * Provides GUI for customers, restaurants, and admins.
 */
public class MainFrame extends JFrame {
    private UserService userService;
    private MenuService menuService;
    private OrderService orderService;
    
    private User currentUser;
    private List<CartItem> cartItems;
    
    private JPanel cards;
    private CardLayout cardLayout;
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField addressField;
    
    private JTable restaurantTable;
    private JTable menuTable;
    private JTable cartTable;
    private JTable orderTable;
    private JTable agentTable;
    
    private DefaultTableModel restaurantModel;
    private DefaultTableModel menuModel;
    private DefaultTableModel cartModel;
    private DefaultTableModel orderModel;
    private DefaultTableModel agentModel;
    
    private int selectedRestaurantId;
    private int selectedOrderId;
    private int selectedAgentId;

    /**
     * Constructor
     */
    public MainFrame() {
        this.userService = new UserService();
        this.menuService = new MenuService();
        this.orderService = new OrderService();
        this.cartItems = new ArrayList<>();
        
        initializeUI();
    }

    /**
     * Initialize UI components
     */
    private void initializeUI() {
        setTitle("Delivery Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        
        // Add panels
        cards.add(createLoginPanel(), "LOGIN");
        cards.add(createCustomerPanel(), "CUSTOMER");
        cards.add(createAdminPanel(), "ADMIN");
        
        add(cards);
        cardLayout.show(cards, "LOGIN");
        
        setLocationRelativeTo(null);
    }

    /**
     * Create login panel
     */
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Title
        JLabel titleLabel = new JLabel("Delivery Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        // Username
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1;
        usernameField = new JTextField(20);
        panel.add(usernameField, gbc);
        
        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        panel.add(passwordField, gbc);
        
        // Address
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Delivery Address:"), gbc);
        
        gbc.gridx = 1;
        addressField = new JTextField(20);
        panel.add(addressField, gbc);
        
        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> handleLogin());
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);
        
        // Register button
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> handleRegister());
        gbc.gridy = 5;
        panel.add(registerButton, gbc);
        
        return panel;
    }

    /**
     * Create customer panel
     */
    private JPanel createCustomerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Top - Welcome message
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Welcome, "));
        JLabel nameLabel = new JLabel();
        topPanel.add(nameLabel);
        topPanel.add(new JLabel(" | "));
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> handleLogout());
        topPanel.add(logoutButton);
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Center - Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        // Left - Restaurants
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Restaurants"));
        
        restaurantModel = new DefaultTableModel(
            new Object[]{"ID", "Name", "Rating", "Phone"}, 0
        );
        restaurantTable = new JTable(restaurantModel);
        restaurantTable.getColumnModel().getColumn(0).setMinWidth(0);
        restaurantTable.getColumnModel().getColumn(0).setMaxWidth(0);
        restaurantTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectRestaurant();
            }
        });
        
        JScrollPane restaurantScroll = new JScrollPane(restaurantTable);
        restaurantScroll.setPreferredSize(new Dimension(300, 400));
        leftPanel.add(restaurantScroll, BorderLayout.CENTER);
        
        JButton refreshRestaurants = new JButton("Refresh");
        refreshRestaurants.addActionListener(e -> loadRestaurants());
        leftPanel.add(refreshRestaurants, BorderLayout.SOUTH);
        
        splitPane.setLeftComponent(leftPanel);
        
        // Right - Menu and Cart
        JPanel rightPanel = new JPanel(new BorderLayout());
        
        // Menu
        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.setBorder(BorderFactory.createTitledBorder("Menu"));
        
        menuModel = new DefaultTableModel(
            new Object[]{"ID", "Name", "Category", "Price", "Description"}, 0
        );
        menuTable = new JTable(menuModel);
        menuTable.getColumnModel().getColumn(0).setMinWidth(0);
        menuTable.getColumnModel().getColumn(0).setMaxWidth(0);
        
        JScrollPane menuScroll = new JScrollPane(menuTable);
        menuScroll.setPreferredSize(new Dimension(400, 250));
        menuPanel.add(menuScroll, BorderLayout.CENTER);
        
        JPanel menuButtonPanel = new JPanel();
        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(e -> addToCart());
        menuButtonPanel.add(addToCartButton);
        menuPanel.add(menuButtonPanel, BorderLayout.SOUTH);
        
        rightPanel.add(menuPanel, BorderLayout.NORTH);
        
        // Cart
        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBorder(BorderFactory.createTitledBorder("Cart"));
        
        cartModel = new DefaultTableModel(
            new Object[]{"Item", "Qty", "Price", "Subtotal"}, 0
        );
        cartTable = new JTable(cartModel);
        
        JScrollPane cartScroll = new JScrollPane(cartTable);
        cartScroll.setPreferredSize(new Dimension(400, 150));
        cartPanel.add(cartScroll, BorderLayout.CENTER);
        
        JPanel cartButtonPanel = new JPanel();
        JButton removeItemButton = new JButton("Remove");
        removeItemButton.addActionListener(e -> removeFromCart());
        JButton clearCartButton = new JButton("Clear");
        clearCartButton.addActionListener(e -> clearCart());
        cartButtonPanel.add(removeItemButton);
        cartButtonPanel.add(clearCartButton);
        
        JButton placeOrderButton = new JButton("Place Order");
        placeOrderButton.addActionListener(e -> placeOrder());
        cartButtonPanel.add(placeOrderButton);
        
        cartPanel.add(cartButtonPanel, BorderLayout.SOUTH);
        
        rightPanel.add(cartPanel, BorderLayout.CENTER);
        
        splitPane.setRightComponent(rightPanel);
        
        panel.add(splitPane, BorderLayout.CENTER);
        
        // Bottom - Orders
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createTitledBorder("My Orders"));
        
        orderModel = new DefaultTableModel(
            new Object[]{"ID", "Date", "Status", "Total", "Address"}, 0
        );
        orderTable = new JTable(orderModel);
        orderTable.getColumnModel().getColumn(0).setMinWidth(0);
        orderTable.getColumnModel().getColumn(0).setMaxWidth(0);
        
        JScrollPane orderScroll = new JScrollPane(orderTable);
        orderScroll.setPreferredSize(new Dimension(800, 150));
        bottomPanel.add(orderScroll, BorderLayout.CENTER);
        
        JButton refreshOrders = new JButton("Refresh Orders");
        refreshOrders.addActionListener(e -> loadCustomerOrders());
        bottomPanel.add(refreshOrders, BorderLayout.SOUTH);
        
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    /**
     * Create admin panel
     */
    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Top
        JPanel topPanel = new JPanel();
        JLabel adminLabel = new JLabel("Admin Panel");
        adminLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(adminLabel);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> handleLogout());
        topPanel.add(logoutButton);
        
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Tabbed pane
        JTabbedPane tabs = new JTabbedPane();
        
        // All Orders tab
        JPanel ordersPanel = new JPanel(new BorderLayout());
        
        orderModel = new DefaultTableModel(
            new Object[]{"ID", "User", "Restaurant", "Status", "Total", "Date"}, 0
        );
        orderTable = new JTable(orderModel);
        orderTable.getColumnModel().getColumn(0).setMinWidth(0);
        orderTable.getColumnModel().getColumn(0).setMaxWidth(0);
        
        JScrollPane orderScroll = new JScrollPane(orderTable);
        ordersPanel.add(orderScroll, BorderLayout.CENTER);
        
        JPanel orderButtonPanel = new JPanel();
        JButton refreshOrders = new JButton("Refresh");
        refreshOrders.addActionListener(e -> loadAllOrders());
        
        JButton updateStatusButton = new JButton("Update Status");
        updateStatusButton.addActionListener(e -> updateOrderStatus());
        
        orderButtonPanel.add(refreshOrders);
        orderButtonPanel.add(updateStatusButton);
        ordersPanel.add(orderButtonPanel, BorderLayout.SOUTH);
        
        tabs.addTab("Orders", ordersPanel);
        
        // Delivery Agents tab
        JPanel agentPanel = new JPanel(new BorderLayout());
        
        agentModel = new DefaultTableModel(
            new Object[]{"ID", "Name", "Phone", "Vehicle", "Status"}, 0
        );
        agentTable = new JTable(agentModel);
        
        JScrollPane agentScroll = new JScrollPane(agentTable);
        agentPanel.add(agentScroll, BorderLayout.CENTER);
        
        JPanel agentButtonPanel = new JPanel();
        JButton refreshAgents = new JButton("Refresh");
        refreshAgents.addActionListener(e -> loadAllAgents());
        
        JButton assignButton = new JButton("Assign to Order");
        assignButton.addActionListener(e -> assignAgent());
        
        agentButtonPanel.add(refreshAgents);
        agentButtonPanel.add(assignButton);
        agentPanel.add(agentButtonPanel, BorderLayout.SOUTH);
        
        tabs.addTab("Delivery Agents", agentPanel);
        
        panel.add(tabs, BorderLayout.CENTER);
        
        return panel;
    }

    /**
     * Handle login
     */
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter username and password");
            return;
        }
        
        try {
            currentUser = userService.authenticate(username, password);
            
            if (currentUser == null) {
                showError("Invalid username or password");
                return;
            }
            
            if (currentUser.getRole() == User.UserRole.ADMIN) {
                cardLayout.show(cards, "ADMIN");
                loadAllOrders();
                loadAllAgents();
            } else {
                cardLayout.show(cards, "CUSTOMER");
                loadRestaurants();
                loadCustomerOrders();
            }
            
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }

    /**
     * Handle register
     */
    private void handleRegister() {
        // Create registration dialog
        JDialog registerDialog = new JDialog(this, "Register New Account", true);
        registerDialog.setSize(400, 350);
        registerDialog.setLocationRelativeTo(this);
        registerDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        // Username
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);
        
        JTextField regUsernameField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(regUsernameField, gbc);
        
        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Password (min 6 chars):"), gbc);
        
        JPasswordField regPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(regPasswordField, gbc);
        
        // Confirm Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Confirm Password:"), gbc);
        
        JPasswordField regConfirmPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(regConfirmPasswordField, gbc);
        
        // Full Name
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Full Name:"), gbc);
        
        JTextField regFullNameField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(regFullNameField, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Email:"), gbc);
        
        JTextField regEmailField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(regEmailField, gbc);
        
        // Phone
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Phone:"), gbc);
        
        JTextField regPhoneField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(regPhoneField, gbc);
        
        // Address
        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(new JLabel("Address:"), gbc);
        
        JTextField regAddressField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(regAddressField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton registerBtn = new JButton("Register");
        registerBtn.addActionListener(e -> {
            String username = regUsernameField.getText().trim();
            String password = new String(regPasswordField.getPassword());
            String confirmPassword = new String(regConfirmPasswordField.getPassword());
            String fullName = regFullNameField.getText().trim();
            String email = regEmailField.getText().trim();
            String phone = regPhoneField.getText().trim();
            String address = regAddressField.getText().trim();
            
            // Validation
            if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || 
                email.isEmpty() || phone.isEmpty()) {
                showError("Please fill all fields");
                return;
            }
            
            if (!password.equals(confirmPassword)) {
                showError("Passwords do not match");
                return;
            }
            
            try {
                userService.register(username, password, fullName, email, phone, address, User.UserRole.CUSTOMER);
                showMessage("Registration successful! Please login with your new account.");
                registerDialog.dispose();
                
                // Clear login fields
                usernameField.setText(username);
                passwordField.setText(password);
                addressField.setText(address);
                
            } catch (IllegalArgumentException ex) {
                showError(ex.getMessage());
            } catch (SQLException ex) {
                showError("Database error: " + ex.getMessage());
            }
        });
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> registerDialog.dispose());
        
        buttonPanel.add(registerBtn);
        buttonPanel.add(cancelBtn);
        
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(buttonPanel, gbc);
        
        registerDialog.add(panel);
        registerDialog.setVisible(true);
    }

    /**
     * Handle logout
     */
    private void handleLogout() {
        currentUser = null;
        cartItems.clear();
        usernameField.setText("");
        passwordField.setText("");
        addressField.setText("");
        cardLayout.show(cards, "LOGIN");
    }

    /**
     * Load restaurants
     */
    private void loadRestaurants() {
        restaurantModel.setRowCount(0);
        
        try {
            List<Restaurant> restaurants = menuService.getAllRestaurants();
            
            for (Restaurant r : restaurants) {
                restaurantModel.addRow(new Object[]{
                    r.getRestaurantId(),
                    r.getRestaurantName(),
                    r.getRating(),
                    r.getPhone()
                });
            }
            
        } catch (SQLException e) {
            showError("Error loading restaurants: " + e.getMessage());
        }
    }

    /**
     * Select restaurant and load menu
     */
    private void selectRestaurant() {
        int selectedRow = restaurantTable.getSelectedRow();
        
        if (selectedRow >= 0) {
            selectedRestaurantId = (int) restaurantModel.getValueAt(selectedRow, 0);
            loadMenu();
        }
    }

    /**
     * Load menu for selected restaurant
     */
    private void loadMenu() {
        menuModel.setRowCount(0);
        
        try {
            List<MenuItem> items = menuService.getMenuItems(selectedRestaurantId);
            
            for (MenuItem item : items) {
                menuModel.addRow(new Object[]{
                    item.getMenuItemId(),
                    item.getItemName(),
                    item.getCategory(),
                    item.getPrice(),
                    item.getDescription()
                });
            }
            
        } catch (SQLException e) {
            showError("Error loading menu: " + e.getMessage());
        }
    }

    /**
     * Add item to cart
     */
    private void addToCart() {
        int selectedRow = menuTable.getSelectedRow();
        
        if (selectedRow < 0) {
            showError("Please select a menu item");
            return;
        }
        
        int menuItemId = (int) menuModel.getValueAt(selectedRow, 0);
        String itemName = (String) menuModel.getValueAt(selectedRow, 1);
        BigDecimal price = (BigDecimal) menuModel.getValueAt(selectedRow, 3);
        
        MenuItem menuItem = new MenuItem();
        menuItem.setMenuItemId(menuItemId);
        menuItem.setItemName(itemName);
        menuItem.setPrice(price);
        
        CartItem cartItem = new CartItem(menuItem, 1);
        cartItems.add(cartItem);
        
        updateCartTable();
    }

    /**
     * Remove item from cart
     */
    private void removeFromCart() {
        int selectedRow = cartTable.getSelectedRow();
        
        if (selectedRow >= 0 && selectedRow < cartItems.size()) {
            cartItems.remove(selectedRow);
            updateCartTable();
        }
    }

    /**
     * Clear cart
     */
    private void clearCart() {
        cartItems.clear();
        updateCartTable();
    }

    /**
     * Update cart table
     */
    private void updateCartTable() {
        cartModel.setRowCount(0);
        
        BigDecimal total = BigDecimal.ZERO;
        
        for (CartItem item : cartItems) {
            cartModel.addRow(new Object[]{
                item.getMenuItem().getItemName(),
                item.getQuantity(),
                item.getMenuItem().getPrice(),
                item.getSubtotal()
            });
            total = total.add(item.getSubtotal());
        }
        
        cartModel.addRow(new Object[]{"TOTAL", "", "", total});
    }

    /**
     * Place order
     */
    private void placeOrder() {
        if (cartItems.isEmpty()) {
            showError("Cart is empty");
            return;
        }
        
        if (selectedRestaurantId == 0) {
            showError("Please select a restaurant");
            return;
        }
        
        String address = addressField.getText().trim();
        
        if (address.isEmpty()) {
            showError("Please enter delivery address");
            return;
        }
        
        try {
            // Show payment method selection
            String[] methods = {"CASH", "UPI", "CARD"};
            String method = (String) JOptionPane.showInputDialog(
                this,
                "Select payment method:",
                "Payment",
                JOptionPane.QUESTION_MESSAGE,
                null,
                methods,
                methods[0]
            );
            
            if (method == null) return;
            
            PaymentMethod paymentMethod = PaymentMethod.valueOf(method);
            
            Order order = orderService.placeOrder(
                currentUser.getUserId(),
                selectedRestaurantId,
                cartItems,
                address,
                paymentMethod,
                ""
            );
            
            showMessage("Order placed successfully! Order ID: " + order.getOrderId());
            
            cartItems.clear();
            updateCartTable();
            loadCustomerOrders();
            
        } catch (Exception e) {
            showError("Error placing order: " + e.getMessage());
        }
    }

    /**
     * Load customer orders
     */
    private void loadCustomerOrders() {
        orderModel.setRowCount(0);
        
        try {
            List<Order> orders = orderService.getOrdersByUser(currentUser.getUserId());
            
            for (Order o : orders) {
                orderModel.addRow(new Object[]{
                    o.getOrderId(),
                    o.getOrderDate(),
                    o.getOrderStatus(),
                    o.getTotalAmount(),
                    o.getDeliveryAddress()
                });
            }
            
        } catch (SQLException e) {
            showError("Error loading orders: " + e.getMessage());
        }
    }

    /**
     * Load all orders (admin)
     */
    private void loadAllOrders() {
        orderModel.setRowCount(0);
        
        try {
            List<Order> orders = orderService.getAllOrders();
            
            for (Order o : orders) {
                orderModel.addRow(new Object[]{
                    o.getOrderId(),
                    o.getUserId(),
                    o.getRestaurantId(),
                    o.getOrderStatus(),
                    o.getTotalAmount(),
                    o.getOrderDate()
                });
            }
            
        } catch (SQLException e) {
            showError("Error loading orders: " + e.getMessage());
        }
    }

    /**
     * Load all agents (admin)
     */
    private void loadAllAgents() {
        agentModel.setRowCount(0);
        
        try {
            List<DeliveryAgent> agents = orderService.getAvailableAgents();
            
            for (DeliveryAgent a : agents) {
                agentModel.addRow(new Object[]{
                    a.getAgentId(),
                    a.getAgentName(),
                    a.getPhone(),
                    a.getVehicleNumber(),
                    a.getAvailabilityStatus()
                });
            }
            
        } catch (SQLException e) {
            showError("Error loading agents: " + e.getMessage());
        }
    }

    /**
     * Update order status
     */
    private void updateOrderStatus() {
        int selectedRow = orderTable.getSelectedRow();
        
        if (selectedRow < 0) {
            showError("Please select an order");
            return;
        }
        
        selectedOrderId = (int) orderModel.getValueAt(selectedRow, 0);
        
        String[] statuses = {"PLACED", "PREPARING", "OUT_FOR_DELIVERY", "DELIVERED", "CANCELLED"};
        String status = (String) JOptionPane.showInputDialog(
            this,
            "Select new status:",
            "Update Status",
            JOptionPane.QUESTION_MESSAGE,
            null,
            statuses,
            statuses[0]
        );
        
        if (status == null) return;
        
        try {
            orderService.updateOrderStatus(selectedOrderId, OrderStatus.valueOf(status));
            showMessage("Status updated!");
            loadAllOrders();
            
        } catch (Exception e) {
            showError("Error updating status: " + e.getMessage());
        }
    }

    /**
     * Assign agent to order
     */
    private void assignAgent() {
        int selectedAgentRow = agentTable.getSelectedRow();
        
        if (selectedAgentRow < 0) {
            showError("Please select an agent first");
            return;
        }
        
        selectedAgentId = (int) agentModel.getValueAt(selectedAgentRow, 0);
        
        // Create assignment dialog
        JDialog assignDialog = new JDialog(this, "Assign Agent to Order", true);
        assignDialog.setSize(350, 200);
        assignDialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel("Assign Delivery Agent to Order");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        // Selected agent
        String agentName = (String) agentModel.getValueAt(selectedAgentRow, 1);
        JLabel agentInfo = new JLabel("Agent: " + agentName);
        agentInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy = 1;
        panel.add(agentInfo, gbc);
        
        // Order ID label
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Order ID:"), gbc);
        
        // Order ID field
        JTextField orderIdField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(orderIdField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton assignBtn = new JButton("Assign");
        assignBtn.addActionListener(e -> {
            String orderIdStr = orderIdField.getText().trim();
            
            if (orderIdStr.isEmpty()) {
                showError("Please enter an Order ID");
                return;
            }
            
            try {
                int orderId = Integer.parseInt(orderIdStr);
                orderService.assignDeliveryAgent(orderId, selectedAgentId);
                showMessage("Agent assigned successfully!");
                assignDialog.dispose();
                loadAllOrders();
                loadAllAgents();
                
            } catch (NumberFormatException ex) {
                showError("Order ID must be a number");
            } catch (IllegalArgumentException ex) {
                showError(ex.getMessage());
            } catch (SQLException ex) {
                showError("Database error: " + ex.getMessage());
            }
        });
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> assignDialog.dispose());
        
        buttonPanel.add(assignBtn);
        buttonPanel.add(cancelBtn);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(buttonPanel, gbc);
        
        assignDialog.add(panel);
        assignDialog.setVisible(true);
    }

    /**
     * Show error message
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Show message
     */
    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Main method
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            new MainFrame().setVisible(true);
        });
    }
}