package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;
import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;
import ru.lab.model.Vehicle;
import ru.lab.model.VehicleType;

/**
 * Окно карты для отображения координат транспортных средств
 */
public class MapWindow extends JFrame {
    private int mouseX, mouseY;
    private LanguageManager languageManager;
    private Hashtable<Integer, Vehicle> vehicles;
    private MapPanel mapPanel;
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int MAP_MARGIN = 50;
    
    public MapWindow(Hashtable<Integer, Vehicle> vehicles) {
        this.languageManager = LanguageManager.getInstance();
        this.vehicles = vehicles;
        
        setTitle(languageManager.getText("map_window_title"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setMinimumSize(new Dimension(600, 400));
        setLocationRelativeTo(null);
        setUndecorated(true);
        setResizable(true);
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
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 
                         AuthFrame.PANEL_CORNER_RADIUS, AuthFrame.PANEL_CORNER_RADIUS));
            }
        });

        initializeUI();
    }
    
    private void initializeUI() {
        RoundedPanel mainPanel = new RoundedPanel(new BorderLayout(), 
                                                 AuthFrame.PANEL_CORNER_RADIUS, 
                                                 AuthFrame.MAIN_BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        // Title and close button panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(languageManager.getText("map_window_title"), SwingConstants.CENTER);
        titleLabel.setForeground(AuthFrame.FOREGROUND_COLOR);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        CustomCloseButton closeButton = new CustomCloseButton();
        closeButton.addActionListener(e -> dispose());
        
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(closeButton, BorderLayout.EAST);
        
        // Map panel
        mapPanel = new MapPanel();
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(mapPanel, BorderLayout.CENTER);
    }
    
    /**
     * Панель для отрисовки карты
     */
    private class MapPanel extends JPanel {
        public MapPanel() {
            setBackground(AuthFrame.MAIN_BACKGROUND_COLOR);
            setOpaque(true);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            
            // Enable antialiasing
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            
            // Reserve space for legend
            int legendWidth = 120;
            int availableWidth = panelWidth - legendWidth - 20;
            
            // Calculate map dimensions considering margins
            int mapWidth = availableWidth - 2 * MAP_MARGIN;
            int mapHeight = panelHeight - 2 * MAP_MARGIN;
            
            // Ensure positive dimensions
            if (mapWidth <= 0 || mapHeight <= 0) {
                g2d.dispose();
                return;
            }
            
            // Position map
            int mapX = MAP_MARGIN;
            int mapY = MAP_MARGIN;
            
            // Draw background
            g2d.setColor(new Color(240, 240, 240));
            g2d.fillRect(mapX, mapY, mapWidth, mapHeight);
            
            // Draw border
            g2d.setColor(AuthFrame.TEXT_FIELD_BORDER_COLOR);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(mapX, mapY, mapWidth, mapHeight);
            
            // Draw coordinate grid
            drawGrid(g2d, mapX, mapY, mapWidth, mapHeight);
            
            // Draw axes labels
            drawAxes(g2d, mapX, mapY, mapWidth, mapHeight);
            
            // Draw vehicles
            drawVehicles(g2d, mapX, mapY, mapWidth, mapHeight);
            
            g2d.dispose();
        }
        
        private void drawGrid(Graphics2D g2d, int mapX, int mapY, int mapWidth, int mapHeight) {
            g2d.setColor(new Color(200, 200, 200));
            g2d.setStroke(new BasicStroke(1));
            
            // Adaptive grid density based on map size
            int gridLinesX = Math.max(5, Math.min(15, mapWidth / 50));
            int gridLinesY = Math.max(5, Math.min(15, mapHeight / 50));
            
            // Vertical grid lines (X coordinates)
            for (int i = 0; i <= gridLinesX; i++) {
                int screenX = mapX + (i * mapWidth) / gridLinesX;
                g2d.drawLine(screenX, mapY, screenX, mapY + mapHeight);
            }
            
            // Horizontal grid lines (Y coordinates)
            for (int i = 0; i <= gridLinesY; i++) {
                int screenY = mapY + (i * mapHeight) / gridLinesY;
                g2d.drawLine(mapX, screenY, mapX + mapWidth, screenY);
            }
        }
        
        private void drawAxes(Graphics2D g2d, int mapX, int mapY, int mapWidth, int mapHeight) {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            FontMetrics fm = g2d.getFontMetrics();
            
            // X axis label
            String xLabel = "X (0-225)";
            int xLabelWidth = fm.stringWidth(xLabel);
            g2d.drawString(xLabel, mapX + mapWidth - xLabelWidth, mapY + mapHeight + 20);
            
            // Y axis label  
            String yLabel = "Y (0-493)";
            g2d.rotate(-Math.PI / 2);
            g2d.drawString(yLabel, -(mapY + mapHeight / 2 + fm.stringWidth(yLabel) / 2), mapX - 10);
            g2d.rotate(Math.PI / 2);
            
            // Draw coordinate values at corners
            g2d.drawString("(0,0)", mapX + 2, mapY + mapHeight - 2);
            g2d.drawString("(225,493)", mapX + mapWidth - 60, mapY + 15);
        }
        
        private void drawVehicles(Graphics2D g2d, int mapX, int mapY, int mapWidth, int mapHeight) {
            if (vehicles == null || vehicles.isEmpty()) {
                return;
            }
            
            // Draw vehicles as colored circles
            List<Vehicle> vehicleList = new ArrayList<>(vehicles.values());
            
            for (Vehicle vehicle : vehicleList) {
                long x = vehicle.getCoordinates().getX();
                int y = vehicle.getCoordinates().getY();
                
                // Convert vehicle coordinates to screen coordinates
                int screenX = mapX + (int) (((double) x / 225) * mapWidth);
                int screenY = mapY + mapHeight - (int) (((double) y / 493) * mapHeight);
                
                // Color based on vehicle type
                Color vehicleColor = getVehicleColor(vehicle);
                g2d.setColor(vehicleColor);
                
                // Draw vehicle point
                int pointSize = 8;
                g2d.fillOval(screenX - pointSize / 2, screenY - pointSize / 2, pointSize, pointSize);
                
                // Draw border
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(1));
                g2d.drawOval(screenX - pointSize / 2, screenY - pointSize / 2, pointSize, pointSize);
                
                // Draw tooltip if map is large enough
                if (mapWidth > 400 && mapHeight > 300) {
                    g2d.setColor(Color.BLACK);
                    g2d.setFont(new Font("Arial", Font.PLAIN, 10));
                    String tooltip = vehicle.getName() + " (" + x + "," + y + ")";
                    g2d.drawString(tooltip, screenX + pointSize, screenY - pointSize);
                }
            }
            
            // Draw legend
            drawLegend(g2d, mapX + mapWidth + 20, mapY);
        }
        
        private Color getVehicleColor(Vehicle vehicle) {
            if (vehicle.getType() == null) {
                return Color.GRAY;
            }
            
            switch (vehicle.getType()) {
                case BOAT: return Color.BLUE;
                case CHOPPER: return Color.RED;
                case HOVERBOARD: return Color.GREEN;
                case SPACESHIP: return Color.ORANGE;
                case AUTO: return Color.CYAN;
                default: return Color.GRAY;
            }
        }
        
        private void drawLegend(Graphics2D g2d, int x, int y) {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString(languageManager.getText("legend"), x, y + 15);
            
            g2d.setFont(new Font("Arial", Font.PLAIN, 11));
            int legendY = y + 35;
            int lineHeight = 20;
            
            // Draw legend items
            String[] types = {"BOAT", "CHOPPER", "HOVERBOARD", "SPACESHIP", "AUTO", "NULL"};
            Color[] colors = {Color.BLUE, Color.RED, Color.GREEN, Color.ORANGE, Color.CYAN, Color.GRAY};
            
            for (int i = 0; i < types.length; i++) {
                g2d.setColor(colors[i]);
                g2d.fillOval(x, legendY + i * lineHeight - 8, 12, 12);
                g2d.setColor(Color.BLACK);
                g2d.drawOval(x, legendY + i * lineHeight - 8, 12, 12);
                g2d.setColor(Color.BLACK);
                g2d.drawString(types[i], x + 20, legendY + i * lineHeight);
            }
        }
    }
    
    /**
     * Обновляет данные на карте
     */
    public void updateVehicles(Hashtable<Integer, Vehicle> newVehicles) {
        this.vehicles = newVehicles;
        if (mapPanel != null) {
            mapPanel.repaint();
        }
    }
} 