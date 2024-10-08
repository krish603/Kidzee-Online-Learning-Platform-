import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public MainFrame() {
        setTitle("Kidzee E-Learning Platform");
        setSize(1025, 640);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        DashboardPanel dashboardPanel = new DashboardPanel(this); // Pass the MainFrame instance
        EditProfilePanel editProfilePanel = new EditProfilePanel();
        
        cardPanel.add(dashboardPanel, "Dashboard");
        cardPanel.add(editProfilePanel, "EditProfile");
        
        add(cardPanel);
    }

    public void switchToPanel(String panelName) {
        cardLayout.show(cardPanel, panelName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
