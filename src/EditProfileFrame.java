import javax.swing.*;
import java.awt.*;

public class EditProfileFrame extends JFrame {
    
    public EditProfileFrame() {


        
        setTitle("Edit Profile");
        setSize(1025, 640);
        setLocationRelativeTo(null); // Center the frame
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose frame on close, so it doesn't close the main application

        ImageIcon icon = new ImageIcon("img/editProfileBG.png");
        Image editProfileBG = icon.getImage();

        // Panel setup
        JPanel editProfilePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(editProfileBG, 0, 0, getWidth(), getHeight(), this);
            }
        };
        editProfilePanel.setLayout(null);

        
        // Save Button
        JButton saveButton = new JButton("Save");
        saveButton.setBounds(150, 350, 100, 40);
        saveButton.addActionListener(e -> {
            // Save action logic
            // You could save the changes or just close the window
            JOptionPane.showMessageDialog(this, "Profile updated successfully!");
            dispose(); // Close the frame after saving
        });
        editProfilePanel.add(saveButton);

        // Adding editProfilePanel to the frame
        add(editProfilePanel);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EditProfileFrame frame = new EditProfileFrame();
            frame.setVisible(true);
        });
    }
}
