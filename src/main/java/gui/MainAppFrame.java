package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;

public class MainAppFrame extends JFrame {
    private int mouseX, mouseY;

    public MainAppFrame() {
        setTitle("Главное окно");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700); // Adjusted size
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setUndecorated(true); // Undecorated for custom chrome
        setBackground(new Color(0, 0, 0, 0)); // Transparent background for rounded corners

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


        RoundedPanel mainPanel = new RoundedPanel(new BorderLayout(), AuthFrame.PANEL_CORNER_RADIUS, AuthFrame.MAIN_BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add some padding
        setContentPane(mainPanel);

        // Top Panel for buttons
        JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5)); // Align left, add gaps
        topButtonPanel.setOpaque(false);

        RoundedButton addButton = new RoundedButton("add", false);
        RoundedButton deleteButton = new RoundedButton("delete", false);
        RoundedButton sortButton = new RoundedButton("sort", false);
        RoundedButton saveButton = new RoundedButton("save", false);
        
        topButtonPanel.add(addButton);
        topButtonPanel.add(deleteButton);
        topButtonPanel.add(sortButton);
        topButtonPanel.add(saveButton);

        JPanel accountButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 5));
        accountButtonPanel.setOpaque(false);
        RoundedButton accountButton = new RoundedButton("account", false);
        accountButtonPanel.add(accountButton);

        accountButton.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(MainAppFrame.this,
                    "Выйти?",
                    "Подтверждение выхода",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                dispose(); // Close current MainAppFrame
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
        closeButton.addActionListener(e -> System.exit(0)); // Or dispose() if you want to handle it differently
        JPanel topLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topLeftPanel.setOpaque(false);
        topLeftPanel.add(closeButton);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(topLeftPanel, BorderLayout.WEST); // Add close button to the west of header
        // We will add the topOuterPanel (with other buttons) to the mainPanel later, below this.

        // Table
        String[] columnNames = {"ID", "name", "coordinates x", "coordinates y", "creation", "engine power", "type", "fuel type", "owner"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells not editable
            }
        };
        JTable table = new JTable(tableModel);
        table.setBackground(AuthFrame.MAIN_BACKGROUND_COLOR);
        table.setForeground(AuthFrame.FOREGROUND_COLOR);
        table.setGridColor(new Color(100, 100, 100)); // Grid color
        table.getTableHeader().setBackground(AuthFrame.BUTTON_COLOR_DARK);
        table.getTableHeader().setForeground(AuthFrame.FOREGROUND_COLOR);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setFillsViewportHeight(true); // Table uses entire space of scroll pane

        // Example data - replace with actual data loading
        for (int i = 0; i < 10; i++) {
            tableModel.addRow(new Object[]{"", "", "", "", "", "", "", "", ""});
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(AuthFrame.MAIN_BACKGROUND_COLOR);
        scrollPane.setBorder(BorderFactory.createLineBorder(AuthFrame.TEXT_FIELD_BORDER_COLOR, 1));


        // Bottom Panel for buttons
        JPanel bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Centered buttons
        bottomButtonPanel.setOpaque(false);
        RoundedButton infoButton = new RoundedButton("info", false);
        RoundedButton helpButton = new RoundedButton("help", false);
        RoundedButton mapButton = new RoundedButton("map", false);
        bottomButtonPanel.add(infoButton);
        bottomButtonPanel.add(helpButton);
        bottomButtonPanel.add(mapButton);

        // Language Button (Globe Icon)
        // For simplicity, using text. Replace with an actual icon if available.
        RoundedButton langButton = new RoundedButton("\uD83C\uDF10", AuthFrame.MAIN_BACKGROUND_COLOR, AuthFrame.FOREGROUND_COLOR);
        langButton.setFont(new Font("Arial Unicode MS", Font.PLAIN, 20)); // Ensure font supports globe, increased size
        JPanel langPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        langPanel.setOpaque(false);
        langPanel.add(langButton);
        
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setOpaque(false);
        southPanel.add(langPanel, BorderLayout.WEST);
        southPanel.add(bottomButtonPanel, BorderLayout.CENTER);

        // Layout for mainPanel
        JPanel contentWrapper = new JPanel(new BorderLayout(0, 10)); // Add vertical gap
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(BorderFactory.createEmptyBorder(5,5,5,5)); // Padding around content

        // Panel to hold top buttons (add, delete, etc.) and account button
        JPanel topControlsPanel = new JPanel(new BorderLayout());
        topControlsPanel.setOpaque(false);
        topControlsPanel.add(topButtonPanel, BorderLayout.WEST);
        topControlsPanel.add(accountButtonPanel, BorderLayout.EAST);
        
        // New horizontal arrangement for header elements
        JPanel horizontalHeader = new JPanel(new BorderLayout());
        horizontalHeader.setOpaque(false);
        horizontalHeader.add(topLeftPanel, BorderLayout.WEST);    // Panel with the 'X' close button
        horizontalHeader.add(topControlsPanel, BorderLayout.CENTER); // Panel with (add..del..save) and (account) buttons

        contentWrapper.add(horizontalHeader, BorderLayout.NORTH); // Use new horizontalHeader
        contentWrapper.add(scrollPane, BorderLayout.CENTER);
        contentWrapper.add(southPanel, BorderLayout.SOUTH);
        
        mainPanel.add(contentWrapper, BorderLayout.CENTER);


        setVisible(true);
    }

    // Optional: Add a main method for testing this frame directly
    // public static void main(String[] args) {
    //     SwingUtilities.invokeLater(() -> {
    //         try {
    //             for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
    //                 if ("Nimbus".equals(info.getName())) {
    //                     UIManager.setLookAndFeel(info.getClassName());
    //                     break;
    //                 }
    //             }
    //         } catch (Exception e) {
    //             // If Nimbus is not available, use the default L&F
    //             try {
    //                 UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    //             } catch (Exception ex) {
    //                 ex.printStackTrace();
    //             }
    //         }
    //         new MainAppFrame().setVisible(true);
    //     });
    // }
} 