import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class UserDashboardFrame extends JFrame {

    public UserDashboardFrame() {
        setTitle("User Dashboard");
        setSize(1025, 640);  // Adjust the size as needed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Center the frame on the screen

        // Create the DashboardPanel and add it to the frame
        UserDashboardPanel dashboardPanel = new UserDashboardPanel(this);
        add(dashboardPanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        new UserDashboardFrame();
    }
}

class UserDashboardPanel extends JPanel {
    private Image backgroundImage;
    private JButton notificationBellButton;

    public UserDashboardPanel(JFrame parentFrame) {
        // Load the background image
        ImageIcon icon = new ImageIcon("img/dashboard_bg.png"); // Update with the actual path to your image
        backgroundImage = icon.getImage();

        // Use absolute positioning for components
        setLayout(null);

        // Example components
        ImageIcon circularIcon = CircularImage.getCircularImage(new ImageIcon("img/krish.png").getImage(), 100); // Adjust size as needed
        JLabel userIcon = new JLabel(circularIcon);
        userIcon.setBounds(75, 160, 100, 100);
        add(userIcon);

        JLabel emailLabel = new JLabel("user@gmail.com");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setFont(new Font("Arial", Font.BOLD, 16));
        emailLabel.setBounds(55, 260, 200, 30);
        add(emailLabel);

        // Edit profile button with image
        JButton editProfileButton = new JButton(new ImageIcon("img/buttons/user_profile.png")); // Replace with the actual path
        editProfileButton.setBounds(70, 300, 100, 50); // Adjust as needed
        add(editProfileButton);

        // Logout button with image
        JButton logoutButton = new JButton(new ImageIcon("img/buttons/logout.png")); // Replace with the actual path
        logoutButton.setBounds(70, 370, 100, 50); // Adjust as needed
        logoutButton.addActionListener(e -> logout(parentFrame)); // Call the logout method
        add(logoutButton);

        notificationBellButton = new JButton(new ImageIcon("img/notification_bell.png"));
        notificationBellButton.setBounds(910, 20, 50, 50); 
        notificationBellButton.addActionListener(e -> showNoticesPopup(notificationBellButton)); // Add this line
        add(notificationBellButton);

        JButton coursesButton = new JButton("Courses");
        coursesButton.setForeground(Color.WHITE);
        coursesButton.setBackground(Color.decode("#1699f2"));
        coursesButton.setBounds(525, 50, 300, 150); // Adjust as needed
        coursesButton.addActionListener(e -> openCoursesFrame());
        add(coursesButton);

        JButton notificationsButton = new JButton("Notices");
        notificationsButton.setForeground(Color.WHITE); 
        notificationsButton.setBackground(Color.decode("#1699f2"));
        notificationsButton.setBounds(525, 225, 300, 150); // Adjust as needed
        notificationsButton.addActionListener(e -> showNotifications());
        add(notificationsButton);

        JButton lecturesButton = new JButton("Lectures");
        lecturesButton.setForeground(Color.WHITE); 
        lecturesButton.setBackground(Color.decode("#1699f2"));
        lecturesButton.setBounds(525, 400, 300, 150); // Adjust as needed
        lecturesButton.addActionListener(e -> showProgress());
        add(lecturesButton);
    }

    private void logout(JFrame parentFrame) {
        // Logic to log out and return to the login screen
        int confirmed = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (confirmed == JOptionPane.YES_OPTION) {
            parentFrame.dispose();
            // Open login frame or first interface
            new First_Interface().setVisible(true); 
        }
    }

    private void showNoticesPopup(Component parent) {
        // Fetch notices from the database
        List<String> notices = fetchNoticesFromDatabase();
        List<String> notices1 = notices.reversed();
        JPopupMenu popupMenu = new JPopupMenu();
    
        if (notices.isEmpty()) {
            popupMenu.add(new JMenuItem("No new notices"));
        } else {
            for (String notice : notices1) {
                JMenuItem menuItem = new JMenuItem(notice);
                popupMenu.add(menuItem);
            }
        }
    
        // Get the current mouse location
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        
        // Convert the screen coordinates to component-relative coordinates
        SwingUtilities.convertPointFromScreen(mouseLocation, parent);
    
        // Show the popup menu at the mouse location
        popupMenu.show(parent, mouseLocation.x, mouseLocation.y);
    }

    private List<String> fetchNoticesFromDatabase() {
        List<String> notices = new ArrayList<>();
        
        // Database connection parameters
        final String DB_URL = "jdbc:mysql://localhost:3306/kidzee_login"; // Replace with your database name
        final String DB_USER = "root"; // Replace with your database username
        final String DB_PASSWORD = ""; // Replace with your database password
        
        String sql = "SELECT title FROM notices"; // Replace 'notice' with your column name and 'notices' with your table name
    
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
    
            while (rs.next()) {
                notices.add(rs.getString("title")); // Replace 'notice' with your column name
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notices;
    }

    private void openCoursesFrame() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

        if (parentFrame != null) {
            parentFrame.dispose(); 
        }

        UserCoursePage ucp = new UserCoursePage();
        ucp.setVisible(true);
    }

    private void showNotifications() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

        if (parentFrame != null) {
            parentFrame.dispose(); 
        }
        UserNoticeManagementFrame unp = new UserNoticeManagementFrame();
        unp.setVisible(true);
    }

    private void showProgress() {
        // Logic to display user progress
        JOptionPane.showMessageDialog(this, "Lectures");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
