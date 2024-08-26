import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EditProfilePanel extends JFrame {
    private MainFrame mainFrame;
    
    public EditProfilePanel() {
        setTitle("Kidzee E-Learning Platform");
        setSize(1025, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

       // Top Panel
       JPanel headerPanel = new JPanel();
       headerPanel.setBackground(new Color(100, 149, 237));
       headerPanel.setBounds(0, 0, 1025, 80);
       headerPanel.setLayout(null);
       add(headerPanel);

       JButton goBackButton = new JButton(new ImageIcon("img/buttons/arrow.png")); // Replace with the actual path
       goBackButton.setBounds(30, 20, 50, 50); // Adjust as needed
       goBackButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               showDashboardFrame();
           }
       });
       add(goBackButton);

       // Main Label
       
    }

    private void showDashboardFrame() {
        if (mainFrame == null) {
            DashboardPanel dashboardFrame = new DashboardPanel(mainFrame);
            setContentPane(dashboardFrame);
            revalidate();
            repaint();
            
        } else {
            System.err.println("Error: MainFrame reference is null.");
        }
    }
}
