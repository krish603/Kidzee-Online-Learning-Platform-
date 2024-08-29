import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class First_Interface extends JFrame {
    private JProgressBar progressBar;
    private Timer timer;
    private MainFrame mainFrame;

    public First_Interface() {
        // Load the image
        ImageIcon icon = new ImageIcon("img/first_bg.png");
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

        // Add the "Loading..." text label
        JLabel loadingLabel = new JLabel("Loading...");
        loadingLabel.setFont(new Font("Serif", Font.PLAIN, 14));
        loadingLabel.setBounds(10, 550, 100, 20); // Position above the loading line
        panel.add(loadingLabel);

        // Add the loading line (JProgressBar)
        progressBar = new JProgressBar();
        progressBar.setBounds(10, 570, 980, 20); // Position at the bottom of the window
        progressBar.setValue(0); // Start with 0 progress
        panel.add(progressBar);

        // Set the custom panel as the content pane of the JFrame
        setContentPane(panel);

        // Set the frame properties
        setTitle("Kidzee E-Learning Platform");
        setSize(1025, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Initialize and start the loading timer
        timer = new Timer(10, e -> updateProgress());
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

        // Add components to the login panel
        JLabel userIcon = new JLabel(new ImageIcon("img/krish.png"));
        userIcon.setBounds(475, 100, 100, 100); // Example positioning, adjust as needed
        loginPanel.add(userIcon);

        JLabel loginLabel = new JLabel("Welcome to the Login Panel!");
        loginLabel.setFont(new Font("Serif", Font.PLAIN, 22));
        loginLabel.setBounds(420, 206, 500, 30); // Position above the loading line
        loginPanel.add(loginLabel);

        PlaceholderTextField usernameField = new PlaceholderTextField("Username");
        usernameField.setBounds(400, 250, 300, 30); // Example positioning, adjust as needed
        loginPanel.add(usernameField);

        PlaceholderPasswordField passwordField = new PlaceholderPasswordField("Password");
        passwordField.setBounds(400, 300, 300, 30); // Example positioning, adjust as needed
        loginPanel.add(passwordField);

        JButton loginButton = new JButton(new ImageIcon("img/buttons/login.png"));
        loginButton.setBounds(500, 350, 100, 50);
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            boolean authenticated = UserAuthenticator.authenticate(username, password);
            if (authenticated) {
                // Pass this instance (the frame) to the DashboardPanel
                DashboardPanel dashboardPanel = new DashboardPanel(mainFrame);
                setContentPane(dashboardPanel);
                revalidate();
                repaint();  
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        loginPanel.add(loginButton);

        // Set the custom panel as the content pane of the frame
        setContentPane(loginPanel);
        revalidate();
    }

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

    class UserAuthenticator {
        public static boolean authenticate(String username, String password) {
            String query = "SELECT * FROM login_details WHERE username = ? AND password = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new First_Interface().setVisible(true));
    }
}