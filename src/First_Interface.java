import javax.swing.*;
import java.awt.*;
import java.sql.*;

@SuppressWarnings("unused")
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
                showSelectionPage(); // Show selection page after loading completes
            }
        });
    }

    void showSelectionPage() {
        ImageIcon icon = new ImageIcon("img/OptionPageBG.png");
        Image backgroundImage = icon.getImage();

        JPanel selectionPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        selectionPanel.setLayout(null);
        
        // Add "User" button
        JButton userButton = new JButton(new ImageIcon("img/buttons/UserButton.png"));
        userButton.setBounds(142, 462, 200, 75);
        userButton.addActionListener(e -> showUserLoginPage());
        selectionPanel.add(userButton);

        // Add "Admin" button
        JButton adminButton = new JButton(new ImageIcon("img/buttons/AdminButton.png"));
        adminButton.setBounds(682, 462, 200, 75);
        adminButton.addActionListener(e -> showAdminLoginPage());
        selectionPanel.add(adminButton);

        // Set the selection panel as the content pane of the frame
        setContentPane(selectionPanel);
        revalidate();
        repaint();
    }

    private void showAdminLoginPage() {
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

        JButton goBackButton = new JButton(new ImageIcon("img/buttons/arrow.png")); // Replace with the actual path
        goBackButton.setBounds(30, 20, 50, 50); // Adjust as needed
        goBackButton.addActionListener(e -> showSelectionPage());
        loginPanel.add(goBackButton);

        // Add components to the login panel (same as before)
        JLabel userIcon = new JLabel(new ImageIcon("img/krish.png"));
        userIcon.setBounds(475, 100, 100, 100); // Example positioning, adjust as needed
        loginPanel.add(userIcon);

        JLabel loginLabel = new JLabel("Welcome to the Admin Login Panel!");
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
            boolean authenticated = AdminAuthenticator.authenticate(username, password);
            if (authenticated) {
                DashboardPanel dashboardPanel = new DashboardPanel(mainFrame);
                setContentPane(dashboardPanel);
                revalidate();
                repaint();  
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        loginPanel.add(loginButton);

        setContentPane(loginPanel);
        revalidate();
    }

    private void showUserLoginPage() {
        // Load the login page image
        ImageIcon icon = new ImageIcon("img/Login_page_bg.png");
        Image loginBackgroundImage = icon.getImage();

        // Create a custom JPanel to draw the login background image
        JPanel userLoginPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the login background image
                g.drawImage(loginBackgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        userLoginPanel.setLayout(null); // Use absolute positioning

        JButton goBackButton = new JButton(new ImageIcon("img/buttons/arrow.png")); // Replace with the actual path
        goBackButton.setBounds(30, 20, 50, 50); // Adjust as needed
        goBackButton.addActionListener(e -> showSelectionPage());
        userLoginPanel.add(goBackButton);
        
        JLabel userIcon = new JLabel(new ImageIcon("img/krish.png"));
        userIcon.setBounds(475, 100, 100, 100); // Example positioning, adjust as needed
        userLoginPanel.add(userIcon);

        JLabel userLoginLabel = new JLabel("Welcome to the User Login Panel!");
        userLoginLabel.setFont(new Font("Serif", Font.PLAIN, 22));
        userLoginLabel.setBounds(400, 206, 500, 30); 
        userLoginPanel.add(userLoginLabel);
        
        PlaceholderTextField usernameField = new PlaceholderTextField("Username");
        usernameField.setBounds(400, 250, 300, 30); 
        userLoginPanel.add(usernameField);

        PlaceholderPasswordField passwordField = new PlaceholderPasswordField("Password");
        passwordField.setBounds(400, 300, 300, 30); 
        userLoginPanel.add(passwordField);

        JButton loginButton = new JButton(new ImageIcon("img/buttons/login.png"));
        loginButton.setBounds(500, 350, 100, 50);
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            boolean authenticated = UserAuthenticator.authenticate(username, password);
            if (authenticated) {
                UserDashboardFrame dashboardFrame = new UserDashboardFrame();
                dashboardFrame.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        userLoginPanel.add(loginButton);

        setContentPane(userLoginPanel);
        revalidate();
        repaint();
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new First_Interface().setVisible(true));
    }
}
