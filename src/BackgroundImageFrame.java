import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// PlaceholderTextField class remains the same

// PlaceholderPasswordField class remains the same

public class BackgroundImageFrame extends JFrame {
    private JProgressBar progressBar;
    private Timer timer;

    public BackgroundImageFrame() {
        // Load the image
        ImageIcon icon = new ImageIcon("/mnt/data/Screenshot 2024-07-18 161609.png");
        Image backgroundImage = icon.getImage();

        // Create a custom JPanel to draw the background image
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        panel.setLayout(null); // Use absolute positioning

        // Add the main label
        JLabel label = new JLabel("Learning Management System", JLabel.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 24));
        label.setBounds(100, 400, 600, 50); // Example positioning, adjust as needed
        panel.add(label);

        // Add the "Loading..." text label
        JLabel loadingLabel = new JLabel("Loading...");
        loadingLabel.setFont(new Font("Serif", Font.PLAIN, 14));
        loadingLabel.setBounds(10, 550, 100, 20); // Position above the loading line
        panel.add(loadingLabel);

        // Add the loading line (JProgressBar)
        progressBar = new JProgressBar();
        progressBar.setBounds(10, 570, 780, 20); // Position at the bottom of the window
        progressBar.setValue(0); // Start with 0 progress
        panel.add(progressBar);

        // Set the custom panel as the content pane of the JFrame
        setContentPane(panel);

        // Set the frame properties
        setTitle("Background Image Frame");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Initialize and start the loading timer
        timer = new Timer(100, e -> updateProgress());
        timer.start();
    }

    private void updateProgress() {
        SwingUtilities.invokeLater(() -> {
            int value = progressBar.getValue();
            if (value < 100) {
                progressBar.setValue(value + 1);
            } else {
                timer.stop();
                showLoginPage();
            }
        });
    }

    private void showLoginPage() {
        SwingUtilities.invokeLater(() -> {
            // Dispose the current frame
            dispose();

            // Create a new frame for the login page
            JFrame loginFrame = new JFrame("Login Page");
            loginFrame.setSize(800, 600);
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.setLocationRelativeTo(null);

            // Load the login page image
            ImageIcon icon = new ImageIcon("img/Login_page_bg.png");
            Image loginBackgroundImage = icon.getImage();

            // Create a custom JPanel to draw the login background image
            JPanel loginPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    // Draw the login background image
                    g.drawImage(loginBackgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            };
            loginPanel.setLayout(null); // Use absolute positioning

            // Load the user icon image
            ImageIcon userIconImage = new ImageIcon("img/krish.png");

            // Scale the image to the desired size (e.g., 100x100 pixels)
            Image scaledImage = userIconImage.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            userIconImage = new ImageIcon(scaledImage);

            JLabel userIcon = new JLabel(userIconImage);
            userIcon.setBounds(350, 100, 100, 100); // Example positioning, adjust as needed
            loginPanel.add(userIcon);

            // Add text fields with placeholders
            PlaceholderTextField usernameField = new PlaceholderTextField("Username");
            usernameField.setBounds(250, 250, 300, 30); // Example positioning, adjust as needed
            loginPanel.add(usernameField);

            PlaceholderPasswordField passwordField = new PlaceholderPasswordField("Password");
            passwordField.setBounds(250, 300, 300, 30); // Example positioning, adjust as needed
            loginPanel.add(passwordField);

            // Add the login button and authentication logic
            JButton loginButton = new JButton("LOGIN");
            loginButton.setBounds(350, 350, 100, 30); // Example positioning, adjust as needed
            loginButton.addActionListener(e -> {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                boolean authenticated = UserAuthenticator.authenticate(username, password);
                if (authenticated) {
                    JOptionPane.showMessageDialog(null, "Login successful!");
                    // Proceed to the next step in your application
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password.");
                }
            });
            loginPanel.add(loginButton);

            // Set the custom panel as the content pane of the login frame
            loginFrame.setContentPane(loginPanel);
            loginFrame.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BackgroundImageFrame().setVisible(true));
    }
}

// Database Connection Utility
class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/kidzee_login";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

// User Authentication
class UserAuthenticator {
    public static boolean authenticate(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            System.out.println("Executing query: " + preparedStatement.toString()); // Debug statement

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("User authenticated: " + username); // Debug statement
                return true;
            } else {
                System.out.println("Authentication failed for user: " + username); // Debug statement
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
