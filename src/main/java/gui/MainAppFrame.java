package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;
import ru.lab.util.DBUserManager;
import ru.lab.util.CollectionManager;
import ru.lab.util.DBCollectionManager;
import ru.lab.model.Vehicle;
import ru.lab.model.Coordinates;
import ru.lab.model.VehicleType;
import ru.lab.model.FuelType;
import java.util.Hashtable;
import java.util.Date;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.awt.Rectangle;
import java.text.SimpleDateFormat;

public class MainAppFrame extends JFrame {
    private int mouseX, mouseY;
    private CollectionManager collectionManager;
    private VehicleTableModel vehicleTableModel;
    private JTable table;
    private LanguageManager languageManager;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    // UI components that need to be updated when language changes
    private RoundedButton addButton, deleteButton, sortButton, saveButton, accountButton;
    private RoundedButton infoButton, helpButton, mapButton;
    private RoundedButton langButton;

    public MainAppFrame() {
        languageManager = LanguageManager.getInstance();
        
        // Initialize collection manager and load data from DB
        DBCollectionManager dbManager = new DBCollectionManager();
        Hashtable<Integer, Vehicle> vehicleCollection = dbManager.getCollection();
        this.collectionManager = new CollectionManager(vehicleCollection, dbManager);
        
        setTitle(languageManager.getText("app_title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        // Make window draggable
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int newX = getLocation().x + (e.getX() - mouseX);
                int newY = getLocation().y + (e.getY() - mouseY);
                setLocation(newX, newY);
            }
        });
        
        // Rounded corners
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), AuthFrame.PANEL_CORNER_RADIUS, AuthFrame.PANEL_CORNER_RADIUS));
            }
        });

        initializeUI();
        refreshTableData();
        setVisible(true);
    }
    
    private void initializeUI() {
        RoundedPanel mainPanel = new RoundedPanel(new BorderLayout(), AuthFrame.PANEL_CORNER_RADIUS, AuthFrame.MAIN_BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        // Top Panel for buttons
        JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topButtonPanel.setOpaque(false);

        addButton = new RoundedButton(languageManager.getText("add"), false);
        deleteButton = new RoundedButton(languageManager.getText("delete"), false);
        sortButton = new RoundedButton(languageManager.getText("sort"), false);
        saveButton = new RoundedButton(languageManager.getText("save"), false);
        
        // Add action listeners for buttons
        addButton.addActionListener(e -> addNewVehicle());
        deleteButton.addActionListener(e -> deleteSelectedVehicle());
        sortButton.addActionListener(e -> showSortFilterDialog());
        saveButton.addActionListener(e -> saveChangesToDB());
        infoButton = new RoundedButton(languageManager.getText("info"), false);
        infoButton.addActionListener(e -> showCollectionInfo());
        
        topButtonPanel.add(addButton);
        topButtonPanel.add(deleteButton);
        topButtonPanel.add(sortButton);
        topButtonPanel.add(saveButton);

        JPanel accountButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 5));
        accountButtonPanel.setOpaque(false);
        accountButton = new RoundedButton(languageManager.getText("account"), false);
        accountButtonPanel.add(accountButton);

        accountButton.addActionListener(e -> {
            if (showLogoutConfirmationDialog()) {
                DBUserManager.getInstance().logoutUser();
                dispose();
                SwingUtilities.invokeLater(() -> {
                    AuthFrame authFrame = new AuthFrame();
                    authFrame.setVisible(true);
                });
            }
        });

        JPanel topOuterPanel = new JPanel(new BorderLayout());
        topOuterPanel.setOpaque(false);
        topOuterPanel.add(topButtonPanel, BorderLayout.WEST);
        topOuterPanel.add(accountButtonPanel, BorderLayout.EAST);
        
        // Close button in the top-left corner
        CustomCloseButton closeButton = new CustomCloseButton();
        closeButton.addActionListener(e -> System.exit(0));
        JPanel topLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topLeftPanel.setOpaque(false);
        topLeftPanel.add(closeButton);

        // Create custom table model and table
        vehicleTableModel = new VehicleTableModel();
        table = new JTable(vehicleTableModel);
        setupTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(AuthFrame.MAIN_BACKGROUND_COLOR);
        scrollPane.setBorder(BorderFactory.createLineBorder(AuthFrame.TEXT_FIELD_BORDER_COLOR, 1));
        
        // Hide scroll bars but keep scrolling functionality
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        
        // Add mouse wheel scrolling
        scrollPane.getViewport().addMouseWheelListener(e -> {
            if (e.getWheelRotation() < 0) {
                // Scroll up
                JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
                verticalScrollBar.setValue(verticalScrollBar.getValue() - 30);
            } else {
                // Scroll down
                JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
                verticalScrollBar.setValue(verticalScrollBar.getValue() + 30);
            }
        });

        // Bottom Panel for buttons
        JPanel bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomButtonPanel.setOpaque(false);
        infoButton = new RoundedButton(languageManager.getText("info"), false);
        helpButton = new RoundedButton(languageManager.getText("help"), false);
        mapButton = new RoundedButton(languageManager.getText("map"), false);
        
        // Add action listeners
        infoButton.addActionListener(e -> showCollectionInfo());
        helpButton.addActionListener(e -> showHelpDialog());
        mapButton.addActionListener(e -> showMapWindow());
        
        bottomButtonPanel.add(infoButton);
        bottomButtonPanel.add(helpButton);
        bottomButtonPanel.add(mapButton);

        // Language Button (Globe Icon) with language switching
        langButton = new RoundedButton("\uD83C\uDF10", AuthFrame.MAIN_BACKGROUND_COLOR, AuthFrame.FOREGROUND_COLOR);
        langButton.setFont(new Font("Arial Unicode MS", Font.PLAIN, 20));
        langButton.addActionListener(e -> showLanguageSelector());
        
        JPanel langPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        langPanel.setOpaque(false);
        langPanel.add(langButton);
        
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setOpaque(false);
        southPanel.add(langPanel, BorderLayout.WEST);
        southPanel.add(bottomButtonPanel, BorderLayout.CENTER);

        // Layout for mainPanel
        JPanel contentWrapper = new JPanel(new BorderLayout(0, 10));
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        JPanel topControlsPanel = new JPanel(new BorderLayout());
        topControlsPanel.setOpaque(false);
        topControlsPanel.add(topButtonPanel, BorderLayout.WEST);
        topControlsPanel.add(accountButtonPanel, BorderLayout.EAST);
        
        JPanel horizontalHeader = new JPanel(new BorderLayout());
        horizontalHeader.setOpaque(false);
        horizontalHeader.add(topLeftPanel, BorderLayout.WEST);
        horizontalHeader.add(topControlsPanel, BorderLayout.CENTER);

        contentWrapper.add(horizontalHeader, BorderLayout.NORTH);
        contentWrapper.add(scrollPane, BorderLayout.CENTER);
        contentWrapper.add(southPanel, BorderLayout.SOUTH);
        
        mainPanel.add(contentWrapper, BorderLayout.CENTER);
    }
    
    private void setupTable() {
        table.setBackground(AuthFrame.MAIN_BACKGROUND_COLOR);
        table.setForeground(AuthFrame.FOREGROUND_COLOR);
        table.setGridColor(new Color(100, 100, 100));
        
        // Enable table grid display
        table.setShowGrid(true);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        
        // Fix header colors - create custom renderer to override Nimbus theme
        table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            {
                setHorizontalAlignment(SwingConstants.CENTER);
                setOpaque(true);
            }
            
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                comp.setBackground(AuthFrame.MAIN_BACKGROUND_COLOR);
                comp.setForeground(AuthFrame.FOREGROUND_COLOR);
                comp.setFont(new Font("Arial", Font.BOLD, 12));
                
                // Add borders to separate header cells and from table content
                if (comp instanceof JLabel) {
                    // Add bottom border (2px) and right border (1px) for column separation
                    // First column gets left border too, last column doesn't need right border
                    if (column == 0) {
                        // First column: left, bottom, right borders
                        ((JLabel) comp).setBorder(BorderFactory.createMatteBorder(0, 1, 2, 1, AuthFrame.TEXT_FIELD_BORDER_COLOR));
                    } else if (column == table.getColumnCount() - 1) {
                        // Last column: bottom border only (right border already from previous cell)
                        ((JLabel) comp).setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, AuthFrame.TEXT_FIELD_BORDER_COLOR));
                    } else {
                        // Middle columns: bottom and right borders
                        ((JLabel) comp).setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, AuthFrame.TEXT_FIELD_BORDER_COLOR));
                    }
                }
                
                return comp;
            }
        });
        
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Set custom cell editors for enum columns
        setupCellEditors();
        
        // Add mouse listeners for row insertion
        setupRowInsertionListeners();
        
        // Add header click listeners for sorting
        setupHeaderClickListeners();
        
        // Add keyboard listener for delete key
        table.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    deleteSelectedVehicle();
                }
            }
            
            @Override
            public void keyTyped(KeyEvent e) {}
            
            @Override
            public void keyReleased(KeyEvent e) {}
        });
    }
    
    private void setupHeaderClickListeners() {
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int columnIndex = table.getTableHeader().columnAtPoint(e.getPoint());
                if (columnIndex >= 0) {
                    showColumnSortFilterDialog(columnIndex);
                }
            }
        });
    }
    
    private void showColumnSortFilterDialog(int columnIndex) {
        String columnName = vehicleTableModel.getColumnName(columnIndex);
        showSortFilterDialog(columnName);
    }
    
    private void showSortFilterDialog() {
        showSortFilterDialog(null);
    }
    
    private void showSortFilterDialog(String preselectedColumn) {
        JDialog dialog = new JDialog(this, languageManager.getText("sort_filter_title"), true);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0));
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);

        RoundedPanel dialogPanel = new RoundedPanel(new BorderLayout(10, 10), 
                                                AuthFrame.PANEL_CORNER_RADIUS, 
                                                AuthFrame.MAIN_BACKGROUND_COLOR, 
                                                AuthFrame.TEXT_FIELD_BORDER_COLOR, 
                                                1);
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        dialog.setContentPane(dialogPanel);

        dialog.setShape(new RoundRectangle2D.Double(0, 0, dialog.getWidth(), dialog.getHeight(), AuthFrame.PANEL_CORNER_RADIUS, AuthFrame.PANEL_CORNER_RADIUS));

        JLabel titleLabel = new JLabel(languageManager.getText("sort_filter_title"), SwingConstants.CENTER);
        titleLabel.setForeground(AuthFrame.FOREGROUND_COLOR);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        dialogPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Sort section - all columns available for sorting
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel sortLabel = new JLabel(languageManager.getText("sort_by"));
        sortLabel.setForeground(AuthFrame.FOREGROUND_COLOR);
        mainPanel.add(sortLabel, gbc);

        gbc.gridx = 1;
        JComboBox<String> sortCombo = new JComboBox<>();
        String[] allColumns = {languageManager.getText("id"), languageManager.getText("name"), 
                              languageManager.getText("coordinates_x"), languageManager.getText("coordinates_y"),
                              languageManager.getText("creation"), languageManager.getText("engine_power"),
                              languageManager.getText("type"), languageManager.getText("fuel_type"),
                              languageManager.getText("owner")};
        for (String col : allColumns) {
            sortCombo.addItem(col);
        }
        if (preselectedColumn != null) {
            sortCombo.setSelectedItem(preselectedColumn);
        }
        sortCombo.setBackground(AuthFrame.TEXT_FIELD_BACKGROUND);
        sortCombo.setForeground(AuthFrame.FOREGROUND_COLOR);
        mainPanel.add(sortCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel orderLabel = new JLabel(languageManager.getText("sort_order"));
        orderLabel.setForeground(AuthFrame.FOREGROUND_COLOR);
        mainPanel.add(orderLabel, gbc);

        gbc.gridx = 1;
        JComboBox<String> orderCombo = new JComboBox<>();
        orderCombo.addItem(languageManager.getText("ascending"));
        orderCombo.addItem(languageManager.getText("descending"));
        orderCombo.setBackground(AuthFrame.TEXT_FIELD_BACKGROUND);
        orderCombo.setForeground(AuthFrame.FOREGROUND_COLOR);
        mainPanel.add(orderCombo, gbc);

        // Filter section - only type, fuel_type, owner
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel filterLabel = new JLabel(languageManager.getText("filter_by"));
        filterLabel.setForeground(AuthFrame.FOREGROUND_COLOR);
        mainPanel.add(filterLabel, gbc);

        gbc.gridx = 1;
        JComboBox<String> filterCombo = new JComboBox<>();
        filterCombo.addItem(languageManager.getText("all_values"));
        filterCombo.addItem(languageManager.getText("type"));
        filterCombo.addItem(languageManager.getText("fuel_type"));
        filterCombo.addItem(languageManager.getText("owner"));
        filterCombo.setBackground(AuthFrame.TEXT_FIELD_BACKGROUND);
        filterCombo.setForeground(AuthFrame.FOREGROUND_COLOR);
        mainPanel.add(filterCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        JLabel valueLabel = new JLabel(languageManager.getText("filter_value"));
        valueLabel.setForeground(AuthFrame.FOREGROUND_COLOR);
        mainPanel.add(valueLabel, gbc);

        gbc.gridx = 1;
        JPanel valueInputPanel = new JPanel(new CardLayout());
        valueInputPanel.setOpaque(false);
        
        // Text field for owner filtering
        JTextField valueField = new JTextField(15);
        valueField.setBackground(AuthFrame.TEXT_FIELD_BACKGROUND);
        valueField.setForeground(AuthFrame.FOREGROUND_COLOR);
        valueField.setBorder(BorderFactory.createLineBorder(AuthFrame.TEXT_FIELD_BORDER_COLOR, 1));
        
        // Combo boxes for enum filtering
        JComboBox<String> vehicleTypeFilterCombo = new JComboBox<>();
        vehicleTypeFilterCombo.addItem("");
        for (VehicleType type : VehicleType.values()) {
            vehicleTypeFilterCombo.addItem(type.name());
        }
        vehicleTypeFilterCombo.setBackground(AuthFrame.TEXT_FIELD_BACKGROUND);
        vehicleTypeFilterCombo.setForeground(AuthFrame.FOREGROUND_COLOR);
        
        JComboBox<String> fuelTypeFilterCombo = new JComboBox<>();
        fuelTypeFilterCombo.addItem("");
        for (FuelType type : FuelType.values()) {
            fuelTypeFilterCombo.addItem(type.name());
        }
        fuelTypeFilterCombo.setBackground(AuthFrame.TEXT_FIELD_BACKGROUND);
        fuelTypeFilterCombo.setForeground(AuthFrame.FOREGROUND_COLOR);
        
        valueInputPanel.add(valueField, "text");
        valueInputPanel.add(vehicleTypeFilterCombo, "vehicleType");
        valueInputPanel.add(fuelTypeFilterCombo, "fuelType");
        
        // Switch input method based on selected filter column
        filterCombo.addActionListener(e -> {
            int selectedIndex = filterCombo.getSelectedIndex();
            CardLayout cl = (CardLayout) valueInputPanel.getLayout();
            if (selectedIndex == 1) { // Type column
                cl.show(valueInputPanel, "vehicleType");
            } else if (selectedIndex == 2) { // Fuel Type column
                cl.show(valueInputPanel, "fuelType");
            } else {
                cl.show(valueInputPanel, "text");
            }
        });
        
        mainPanel.add(valueInputPanel, gbc);

        dialogPanel.add(mainPanel, BorderLayout.CENTER);

        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPane.setOpaque(false);

        RoundedButton applyButton = new RoundedButton(languageManager.getText("apply"), true);
        applyButton.addActionListener(e -> {
            String filterValue = "";
            int filterColumnIndex = filterCombo.getSelectedIndex();
            
            // Map filter combo indices to actual column indices
            int actualColumnIndex = -1;
            if (filterColumnIndex == 1) actualColumnIndex = 6; // type
            else if (filterColumnIndex == 2) actualColumnIndex = 7; // fuel_type
            else if (filterColumnIndex == 3) actualColumnIndex = 8; // owner
            
            if (filterColumnIndex == 1) { // Type
                filterValue = (String) vehicleTypeFilterCombo.getSelectedItem();
            } else if (filterColumnIndex == 2) { // Fuel Type
                filterValue = (String) fuelTypeFilterCombo.getSelectedItem();
            } else {
                filterValue = valueField.getText();
            }
            
            applySortFilter(sortCombo.getSelectedIndex(), orderCombo.getSelectedIndex() == 0, 
                           actualColumnIndex, filterValue);
            dialog.dispose();
        });

        RoundedButton clearButton = new RoundedButton(languageManager.getText("clear_filter"), false);
        clearButton.addActionListener(e -> {
            clearSortFilter();
            dialog.dispose();
        });

        RoundedButton cancelButton = new RoundedButton(languageManager.getText("cancel"), false);
        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPane.add(applyButton);
        buttonPane.add(clearButton);
        buttonPane.add(cancelButton);
        dialogPanel.add(buttonPane, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
    
    private void applySortFilter(int sortColumn, boolean ascending, int filterColumn, String filterValue) {
        Hashtable<Integer, Vehicle> collection = collectionManager.getCollection();
        List<Vehicle> vehicleList = new ArrayList<>(collection.values());
        
        // Apply filter first
        if (filterColumn >= 0 && !filterValue.trim().isEmpty()) {
            vehicleList = vehicleList.stream().filter(vehicle -> {
                String cellValue = getCellValueAsString(vehicle, filterColumn);
                if (cellValue == null) cellValue = "";
                return cellValue.toLowerCase().contains(filterValue.toLowerCase());
            }).collect(ArrayList::new, (list, item) -> list.add(item), ArrayList::addAll);
        }
        
        // Apply sort
        Comparator<Vehicle> comparator = getComparatorForColumn(sortColumn);
        if (!ascending) {
            comparator = comparator.reversed();
        }
        vehicleList.sort(comparator);
        
        // Update table model with sorted/filtered data
        vehicleTableModel.setFilteredVehicleList(vehicleList);
    }
    
    private void clearSortFilter() {
        refreshTableData();
    }
    
    private String getCellValueAsString(Vehicle vehicle, int column) {
        switch (column) {
            case 0: return String.valueOf(vehicle.getId());
            case 1: return vehicle.getName();
            case 2: return String.valueOf(vehicle.getCoordinates().getX());
            case 3: return String.valueOf(vehicle.getCoordinates().getY());
            case 4: return dateFormat.format(vehicle.getCreationDate());
            case 5: return String.valueOf(vehicle.getEnginePower());
            case 6: return vehicle.getType() != null ? vehicle.getType().name() : "";
            case 7: return vehicle.getFuelType() != null ? vehicle.getFuelType().name() : "";
            case 8: return vehicle.getOwner();
            default: return "";
        }
    }
    
    private Comparator<Vehicle> getComparatorForColumn(int column) {
        switch (column) {
            case 0: return Comparator.comparingInt(Vehicle::getId);
            case 1: return Comparator.comparing(Vehicle::getName);
            case 2: return Comparator.comparing(v -> v.getCoordinates().getX());
            case 3: return Comparator.comparing(v -> v.getCoordinates().getY());
            case 4: return Comparator.comparing(Vehicle::getCreationDate);
            case 5: return Comparator.comparing(Vehicle::getEnginePower);
            case 6: return Comparator.comparing(v -> v.getType() != null ? v.getType().name() : "");
            case 7: return Comparator.comparing(v -> v.getFuelType() != null ? v.getFuelType().name() : "");
            case 8: return Comparator.comparing(Vehicle::getOwner);
            default: return Comparator.comparingInt(Vehicle::getId);
        }
    }
    
    private void setupCellEditors() {
        // VehicleType dropdown (column 6)
        JComboBox<String> vehicleTypeCombo = new JComboBox<>();
        vehicleTypeCombo.addItem(""); // для null значения
        for (VehicleType type : VehicleType.values()) {
            vehicleTypeCombo.addItem(type.name());
        }
        vehicleTypeCombo.setBackground(AuthFrame.TEXT_FIELD_BACKGROUND);
        vehicleTypeCombo.setForeground(AuthFrame.FOREGROUND_COLOR);
        table.getColumnModel().getColumn(6).setCellEditor(new javax.swing.DefaultCellEditor(vehicleTypeCombo));
        
        // FuelType dropdown (column 7)
        JComboBox<String> fuelTypeCombo = new JComboBox<>();
        fuelTypeCombo.addItem(""); // для null значения
        for (FuelType type : FuelType.values()) {
            fuelTypeCombo.addItem(type.name());
        }
        fuelTypeCombo.setBackground(AuthFrame.TEXT_FIELD_BACKGROUND);
        fuelTypeCombo.setForeground(AuthFrame.FOREGROUND_COLOR);
        table.getColumnModel().getColumn(7).setCellEditor(new javax.swing.DefaultCellEditor(fuelTypeCombo));
        
        // Custom editors for numeric fields to start with empty text
        setupNumericCellEditor(2); // coordinates x
        setupNumericCellEditor(3); // coordinates y  
        setupNumericCellEditor(5); // engine power
    }
    
    private void setupNumericCellEditor(int columnIndex) {
        JTextField textField = new JTextField();
        textField.setBackground(AuthFrame.TEXT_FIELD_BACKGROUND);
        textField.setForeground(AuthFrame.FOREGROUND_COLOR);
        textField.setBorder(BorderFactory.createLineBorder(AuthFrame.TEXT_FIELD_BORDER_COLOR, 1));
        
        javax.swing.DefaultCellEditor editor = new javax.swing.DefaultCellEditor(textField) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value,
                    boolean isSelected, int row, int column) {
                JTextField component = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
                // Clear the text field when editing starts - this prevents the "0" prefix issue
                SwingUtilities.invokeLater(() -> {
                    component.setText("");
                    component.requestFocus();
                });
                return component;
            }
        };
        
        table.getColumnModel().getColumn(columnIndex).setCellEditor(editor);
    }
    
    private void setupRowInsertionListeners() {
        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int y = e.getY();
                
                // Check if mouse is near the border between rows
                if (row >= 0) {
                    Rectangle rowRect = table.getCellRect(row, 0, false);
                    int rowCenter = rowRect.y + rowRect.height / 2;
                    int distanceFromTop = Math.abs(y - rowRect.y);
                    int distanceFromBottom = Math.abs(y - (rowRect.y + rowRect.height));
                    
                    // If mouse is within 3 pixels of row border, show insertion cursor
                    if (distanceFromTop <= 3 || distanceFromBottom <= 3) {
                        table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        table.setToolTipText(languageManager.getText("insert_row_tooltip"));
                    } else {
                        table.setCursor(Cursor.getDefaultCursor());
                        table.setToolTipText(null);
                    }
                }
            }
        });
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int y = e.getY();
                
                if (row >= 0) {
                    Rectangle rowRect = table.getCellRect(row, 0, false);
                    int distanceFromTop = Math.abs(y - rowRect.y);
                    int distanceFromBottom = Math.abs(y - (rowRect.y + rowRect.height));
                    
                    // If clicked near row border, insert new row
                    if (distanceFromTop <= 3) {
                        insertRowAt(row); // Insert before this row
                    } else if (distanceFromBottom <= 3) {
                        insertRowAt(row + 1); // Insert after this row
                    }
                }
            }
        });
    }
    
    private void insertRowAt(int position) {
        try {
            // Create a new vehicle with null type and fuelType
            Vehicle newVehicle = new Vehicle(
                position + 1, // Temporary ID, will be adjusted
                "New Vehicle",
                new Coordinates(0L, 0),
                1.0f,
                null,
                null
            );
            newVehicle.setOwner(DBUserManager.getInstance().getCurrentUser().getUsername());
            
            // Get current collection and adjust IDs
            Hashtable<Integer, Vehicle> collection = collectionManager.getCollection();
            List<Vehicle> vehicleList = new ArrayList<>(collection.values());
            vehicleList.sort(Comparator.comparingInt(Vehicle::getId));
            
            // Clear collection and rebuild with new vehicle inserted
            collection.clear();
            
            int newId = 1;
            boolean inserted = false;
            
            for (Vehicle vehicle : vehicleList) {
                if (newId == position + 1 && !inserted) {
                    // Insert new vehicle at this position
                    newVehicle.setId(newId);
                    collection.put(newId, newVehicle);
                    newId++;
                    inserted = true;
                }
                
                // Update existing vehicle ID and add to collection
                vehicle.setId(newId);
                collection.put(newId, vehicle);
                newId++;
            }
            
            // If inserting at the end
            if (!inserted) {
                newVehicle.setId(newId);
                collection.put(newId, newVehicle);
            }
            
            // Refresh table
            refreshTableData();
            
            // Select the newly inserted row
            if (position < table.getRowCount()) {
                table.setRowSelectionInterval(position, position);
            }
            
        } catch (Exception ex) {
            showStyledErrorDialog(languageManager.getText("error_insert_row") + ex.getMessage());
        }
    }
    
    private void refreshTableData() {
        vehicleTableModel.setVehicleCollection(collectionManager.getCollection());
    }
    
    private void addNewVehicle() {
        try {
            // Create a new vehicle with default values, type and fuelType as null
            Vehicle newVehicle = new Vehicle(
                0, // ID will be assigned by collection manager
                "New Vehicle",
                new Coordinates(0L, 0),
                1.0f,
                null, // type should be null by default
                null  // fuelType should be null by default
            );
            newVehicle.setOwner(DBUserManager.getInstance().getCurrentUser().getUsername());
            
            // Add to collection (CollectionManager will assign proper sequential ID)
            int assignedId = collectionManager.addVehicle(newVehicle);
            
            // Refresh table to show updated data with proper sorting
            refreshTableData();
            
            // Find and select the new row for editing
            for (int i = 0; i < vehicleTableModel.getRowCount(); i++) {
                if ((Integer) vehicleTableModel.getValueAt(i, 0) == assignedId) {
                    table.setRowSelectionInterval(i, i);
                    break;
                }
            }
            
        } catch (Exception ex) {
            showStyledErrorDialog(languageManager.getText("error_add_vehicle") + ex.getMessage());
        }
    }
    
    private void deleteSelectedVehicle() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showStyledWarningDialog(languageManager.getText("warning_select_vehicle"));
            return;
        }
        
        try {
            // Get vehicle ID from the selected row
            Integer vehicleId = (Integer) vehicleTableModel.getValueAt(selectedRow, 0);
            Vehicle vehicle = collectionManager.getCollection().get(vehicleId);
            
            if (vehicle == null) {
                showStyledErrorDialog(languageManager.getText("error_vehicle_not_found"));
                return;
            }
            
            // Check if user owns this vehicle
            String currentUser = DBUserManager.getInstance().getCurrentUser().getUsername();
            if (!vehicle.getOwner().equals(currentUser)) {
                showStyledWarningDialog(languageManager.getText("warning_only_own_vehicles"));
                return;
            }
            
            // Remove from collection and reindex
            collectionManager.getCollection().remove(vehicleId);
            reindexCollection();
            
            // Refresh table to show updated data with proper sorting
            refreshTableData();
            
        } catch (Exception ex) {
            showStyledErrorDialog(languageManager.getText("error_delete_vehicle") + ex.getMessage());
        }
    }
    
    private void reindexCollection() {
        Hashtable<Integer, Vehicle> collection = collectionManager.getCollection();
        if (collection.isEmpty()) {
            return;
        }

        List<Vehicle> sortedVehicles = new ArrayList<>(collection.values());
        sortedVehicles.sort(Comparator.comparingInt(Vehicle::getId));

        collection.clear();
        int newCurrentId = 1;
        for (Vehicle vehicle : sortedVehicles) {
            vehicle.setId(newCurrentId);
            collection.put(newCurrentId, vehicle);
            newCurrentId++;
        }
    }
    
    private void saveChangesToDB() {
        try {
            collectionManager.save();
            showStyledInfoDialog(languageManager.getText("info_changes_saved"));
        } catch (Exception ex) {
            showStyledErrorDialog(languageManager.getText("error_save_db") + ex.getMessage());
        }
    }

    private boolean showLogoutConfirmationDialog() {
        JDialog dialog = new JDialog(this, languageManager.getText("logout_confirm"), true);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0)); 
        dialog.setSize(320, 180);
        dialog.setLocationRelativeTo(this);

        RoundedPanel dialogPanel = new RoundedPanel(new BorderLayout(10, 10), 
                                                AuthFrame.PANEL_CORNER_RADIUS, 
                                                AuthFrame.MAIN_BACKGROUND_COLOR, 
                                                AuthFrame.TEXT_FIELD_BORDER_COLOR, 
                                                1);
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        dialog.setContentPane(dialogPanel);

        dialog.setShape(new RoundRectangle2D.Double(0, 0, dialog.getWidth(), dialog.getHeight(), AuthFrame.PANEL_CORNER_RADIUS, AuthFrame.PANEL_CORNER_RADIUS));

        dialog.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                dialog.setShape(new RoundRectangle2D.Double(0, 0, dialog.getWidth(), dialog.getHeight(), AuthFrame.PANEL_CORNER_RADIUS, AuthFrame.PANEL_CORNER_RADIUS));
            }
        });

        JLabel titleLabel = AuthFrame.createHeaderLabel(languageManager.getText("logout_confirm"));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        dialogPanel.add(titleLabel, BorderLayout.NORTH);

        JLabel messageLabel = new JLabel(languageManager.getText("logout_question"), SwingConstants.CENTER);
        messageLabel.setForeground(AuthFrame.FOREGROUND_COLOR);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dialogPanel.add(messageLabel, BorderLayout.CENTER);

        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPane.setOpaque(false);

        final boolean[] result = {false};

        RoundedButton yesButton = new RoundedButton(languageManager.getText("yes"), true); 
        yesButton.setFont(new Font("Arial", Font.BOLD, 13));
        yesButton.setPreferredSize(new Dimension(yesButton.getPreferredSize().width, 30));
        yesButton.addActionListener(e -> {
            result[0] = true;
            dialog.dispose();
        });

        RoundedButton noButton = new RoundedButton(languageManager.getText("no"), false); 
        noButton.setFont(new Font("Arial", Font.BOLD, 13));
        noButton.setPreferredSize(new Dimension(noButton.getPreferredSize().width, 30));
        noButton.addActionListener(e -> {
            result[0] = false;
            dialog.dispose();
        });

        buttonPane.add(yesButton);
        buttonPane.add(noButton);
        dialogPanel.add(buttonPane, BorderLayout.SOUTH);

        dialog.setVisible(true);
        return result[0];
    }
    
    // Custom TableModel for Vehicle data
    private class VehicleTableModel extends DefaultTableModel {
        private String[] columnNames;
        private List<Vehicle> filteredVehicles = null;
        
        public VehicleTableModel() {
            super();
            updateColumnNames();
        }
        
        public void updateColumnNames() {
            columnNames = new String[]{
                languageManager.getText("id"), 
                languageManager.getText("name"), 
                languageManager.getText("coordinates_x"), 
                languageManager.getText("coordinates_y"), 
                languageManager.getText("creation"), 
                languageManager.getText("engine_power"), 
                languageManager.getText("type"), 
                languageManager.getText("fuel_type"), 
                languageManager.getText("owner")
            };
            setColumnIdentifiers(columnNames);
        }
        
        public void setVehicleCollection(Hashtable<Integer, Vehicle> collection) {
            // Clear existing data
            setRowCount(0);
            
            // Convert to list and sort by ID for proper display order
            List<Vehicle> sortedVehicles = new ArrayList<>(collection.values());
            sortedVehicles.sort(Comparator.comparingInt(Vehicle::getId));
            
            addVehiclesToTable(sortedVehicles);
            filteredVehicles = null; // Clear filter
        }
        
        public void setFilteredVehicleList(List<Vehicle> vehicles) {
            setRowCount(0);
            addVehiclesToTable(vehicles);
            filteredVehicles = vehicles;
        }
        
        private void addVehiclesToTable(List<Vehicle> vehicles) {
            // Add data to table with consistent date formatting
            vehicles.forEach(vehicle -> {
                Object[] rowData = {
                    vehicle.getId(),
                    vehicle.getName(),
                    vehicle.getCoordinates().getX(),
                    vehicle.getCoordinates().getY(),
                    dateFormat.format(vehicle.getCreationDate()),
                    vehicle.getEnginePower(),
                    vehicle.getType() != null ? vehicle.getType().name() : "",
                    vehicle.getFuelType() != null ? vehicle.getFuelType().name() : "",
                    vehicle.getOwner()
                };
                addRow(rowData);
            });
        }
        
        @Override
        public boolean isCellEditable(int row, int column) {
            // ID, creation date, and owner should not be editable
            if (column == 0 || column == 4 || column == 8) {
                return false;
            }
            
            // Check if current user owns this vehicle
            Integer vehicleId = (Integer) getValueAt(row, 0);
            Vehicle vehicle = collectionManager.getCollection().get(vehicleId);
            if (vehicle != null) {
                String currentUser = DBUserManager.getInstance().getCurrentUser().getUsername();
                return vehicle.getOwner().equals(currentUser);
            }
            
            return false;
        }
        
        @Override
        public void setValueAt(Object value, int row, int column) {
            try {
                Integer vehicleId = (Integer) getValueAt(row, 0);
                Vehicle vehicle = collectionManager.getCollection().get(vehicleId);
                
                if (vehicle == null) return;
                
                // Check ownership again for security
                String currentUser = DBUserManager.getInstance().getCurrentUser().getUsername();
                if (!vehicle.getOwner().equals(currentUser)) {
                    showStyledWarningDialog(languageManager.getText("warning_only_edit_own"));
                    return;
                }
                
                // Update the vehicle object based on the column
                switch (column) {
                    case 1: // name
                        if (value.toString().trim().isEmpty()) {
                            showStyledErrorDialog(languageManager.getText("error_name_empty"));
                            return;
                        }
                        vehicle.setName(value.toString());
                        break;
                    case 2: // coordinates x
                        try {
                            long x = Long.parseLong(value.toString());
                            if (x < 0) {
                                showStyledErrorDialog(languageManager.getText("error_coord_x_negative"));
                                return;
                            }
                            if (x > 225) {
                                showStyledErrorDialog(languageManager.getText("error_coord_x_max"));
                                return;
                            }
                            vehicle.getCoordinates().setX(x);
                        } catch (NumberFormatException e) {
                            showStyledErrorDialog(languageManager.getText("error_coord_x_number"));
                            return;
                        }
                        break;
                    case 3: // coordinates y
                        try {
                            Integer y = Integer.parseInt(value.toString());
                            if (y < 0) {
                                showStyledErrorDialog(languageManager.getText("error_coord_y_negative"));
                                return;
                            }
                            if (y > 493) {
                                showStyledErrorDialog(languageManager.getText("error_coord_y_max"));
                                return;
                            }
                            vehicle.getCoordinates().setY(y);
                        } catch (NumberFormatException e) {
                            showStyledErrorDialog(languageManager.getText("error_coord_y_number"));
                            return;
                        }
                        break;
                    case 5: // engine power
                        try {
                            float power = Float.parseFloat(value.toString());
                            if (power <= 0) {
                                showStyledErrorDialog(languageManager.getText("error_engine_power_positive"));
                                return;
                            }
                            vehicle.setEnginePower(power);
                        } catch (NumberFormatException e) {
                            showStyledErrorDialog(languageManager.getText("error_engine_power_number"));
                            return;
                        }
                        break;
                    case 6: // type
                        try {
                            VehicleType type = value.toString().isEmpty() ? null : VehicleType.valueOf(value.toString());
                            vehicle.setType(type);
                            // Update display value to show the enum name or empty string
                            value = type != null ? type.name() : "";
                        } catch (IllegalArgumentException e) {
                            showStyledErrorDialog(languageManager.getText("error_vehicle_type_invalid"));
                            return;
                        }
                        break;
                    case 7: // fuel type
                        try {
                            FuelType fuelType = value.toString().isEmpty() ? null : FuelType.valueOf(value.toString());
                            vehicle.setFuelType(fuelType);
                            // Update display value to show the enum name or empty string
                            value = fuelType != null ? fuelType.name() : "";
                        } catch (IllegalArgumentException e) {
                            showStyledErrorDialog(languageManager.getText("error_fuel_type_invalid"));
                            return;
                        }
                        break;
                }
                
                // Update the collection
                collectionManager.updateVehicle(vehicle);
                
                // Update the table model
                super.setValueAt(value, row, column);
                
            } catch (Exception ex) {
                showStyledErrorDialog(languageManager.getText("error_update_data") + ex.getMessage());
            }
        }
    }
    
    // Styled dialog methods for consistent UI
    private void showStyledErrorDialog(String message) {
        showStyledDialog(languageManager.getText("error"), message, Color.RED);
    }
    
    private void showStyledWarningDialog(String message) {
        showStyledDialog(languageManager.getText("warning"), message, new Color(255, 165, 0));
    }
    
    private void showStyledInfoDialog(String message) {
        showStyledDialog(languageManager.getText("information"), message, new Color(70, 130, 180));
    }
    
    private void showStyledDialog(String title, String message, Color titleColor) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0)); 
        
        // Calculate dialog size based on message length
        int baseWidth = 350;
        int baseHeight = 160;
        if (message.length() > 50) {
            baseWidth = Math.min(500, 250 + message.length() * 3);
            baseHeight = Math.min(250, 120 + (message.length() / 40) * 20);
        }
        
        dialog.setSize(baseWidth, baseHeight);
        dialog.setLocationRelativeTo(this);

        RoundedPanel dialogPanel = new RoundedPanel(new BorderLayout(10, 10), 
                                                AuthFrame.PANEL_CORNER_RADIUS, 
                                                AuthFrame.MAIN_BACKGROUND_COLOR, 
                                                AuthFrame.TEXT_FIELD_BORDER_COLOR, 
                                                1);
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        dialog.setContentPane(dialogPanel);

        dialog.setShape(new RoundRectangle2D.Double(0, 0, dialog.getWidth(), dialog.getHeight(), AuthFrame.PANEL_CORNER_RADIUS, AuthFrame.PANEL_CORNER_RADIUS));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setForeground(titleColor);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Same size for all titles
        dialogPanel.add(titleLabel, BorderLayout.NORTH);

        // Improved message display with proper wrapping
        JTextArea messageArea = new JTextArea(message);
        messageArea.setForeground(AuthFrame.FOREGROUND_COLOR);
        messageArea.setFont(new Font("Arial", Font.PLAIN, 13));
        messageArea.setBackground(AuthFrame.MAIN_BACKGROUND_COLOR);
        messageArea.setEditable(false);
        messageArea.setWrapStyleWord(true);
        messageArea.setLineWrap(true);
        messageArea.setOpaque(false);
        messageArea.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JScrollPane messageScrollPane = new JScrollPane(messageArea);
        messageScrollPane.setOpaque(false);
        messageScrollPane.getViewport().setOpaque(false);
        messageScrollPane.setBorder(BorderFactory.createEmptyBorder());
        messageScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        messageScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // Add mouse wheel scrolling for dialog
        messageScrollPane.addMouseWheelListener(e -> {
            if (e.getWheelRotation() < 0) {
                // Scroll up
                JScrollBar verticalScrollBar = messageScrollPane.getVerticalScrollBar();
                verticalScrollBar.setValue(verticalScrollBar.getValue() - 10);
            } else {
                // Scroll down
                JScrollBar verticalScrollBar = messageScrollPane.getVerticalScrollBar();
                verticalScrollBar.setValue(verticalScrollBar.getValue() + 10);
            }
        });

        dialogPanel.add(messageScrollPane, BorderLayout.CENTER);

        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPane.setOpaque(false);

        RoundedButton okButton = new RoundedButton(languageManager.getText("ok"), true); 
        okButton.setFont(new Font("Arial", Font.BOLD, 13));
        okButton.setPreferredSize(new Dimension(60, 30));
        okButton.addActionListener(e -> dialog.dispose());

        buttonPane.add(okButton);
        dialogPanel.add(buttonPane, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void showCollectionInfo() {
        Hashtable<Integer, Vehicle> collection = collectionManager.getCollection();
        String info = languageManager.getText("collection_type") + collection.getClass().getName() + "\n" +
                     languageManager.getText("init_date") + dateFormat.format(collectionManager.getInitializationDate()) + "\n" +
                     languageManager.getText("elements_count") + collection.size();
        
        showStyledInfoDialog(info);
    }
    
    private void showHelpDialog() {
        showStyledInfoDialog(languageManager.getText("help_content"));
    }
    
    private void showLanguageSelector() {
        JDialog dialog = new JDialog(this, "Language / Idioma / Язык", true);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0)); 
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);

        RoundedPanel dialogPanel = new RoundedPanel(new BorderLayout(10, 10), 
                                                AuthFrame.PANEL_CORNER_RADIUS, 
                                                AuthFrame.MAIN_BACKGROUND_COLOR, 
                                                AuthFrame.TEXT_FIELD_BORDER_COLOR, 
                                                1);
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        dialog.setContentPane(dialogPanel);

        dialog.setShape(new RoundRectangle2D.Double(0, 0, dialog.getWidth(), dialog.getHeight(), AuthFrame.PANEL_CORNER_RADIUS, AuthFrame.PANEL_CORNER_RADIUS));

        JLabel titleLabel = new JLabel("Select Language", SwingConstants.CENTER);
        titleLabel.setForeground(AuthFrame.FOREGROUND_COLOR);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        dialogPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        buttonPanel.setOpaque(false);

        String[] languages = languageManager.getAvailableLanguages();
        for (String lang : languages) {
            RoundedButton langBtn = new RoundedButton(languageManager.getLanguageDisplayName(lang), false);
            langBtn.addActionListener(e -> {
                languageManager.setLanguage(lang);
                updateUILanguage();
                dialog.dispose();
            });
            buttonPanel.add(langBtn);
        }

        dialogPanel.add(buttonPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
    
    private void updateUILanguage() {
        // Update window title
        setTitle(languageManager.getText("app_title"));
        
        // Update button texts
        addButton.setText(languageManager.getText("add"));
        deleteButton.setText(languageManager.getText("delete"));
        sortButton.setText(languageManager.getText("sort"));
        saveButton.setText(languageManager.getText("save"));
        accountButton.setText(languageManager.getText("account"));
        infoButton.setText(languageManager.getText("info"));
        helpButton.setText(languageManager.getText("help"));
        mapButton.setText(languageManager.getText("map"));
        
        // Update table headers
        vehicleTableModel.updateColumnNames();
        
        // Re-setup cell editors after language change to maintain functionality
        setupCellEditors();
        
        // Repaint to reflect changes
        repaint();
    }

    private void showMapWindow() {
        try {
            MapWindow mapWindow = new MapWindow(collectionManager.getCollection());
            mapWindow.setVisible(true);
        } catch (Exception ex) {
            showStyledErrorDialog(languageManager.getText("error_open_map") + ex.getMessage());
        }
    }
} 