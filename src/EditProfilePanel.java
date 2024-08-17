import javax.swing.*;
import java.awt.*;

public class EditProfilePanel extends JPanel {
    
    public EditProfilePanel() {
        setLayout(null);
        setBackground(Color.WHITE);

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
        editProfilePanel.setBounds(0, 0, 1000, 600); // Adjust as needed
        
        // Save Button
        JButton saveButton = new JButton("Save");
        saveButton.setBounds(150, 350, 100, 40);
        saveButton.addActionListener(e -> {
            // Save action logic
            JOptionPane.showMessageDialog(this, "Profile updated successfully!");
            // Switch back to dashboard panel after saving
            ((CardLayout) getParent().getLayout()).show(getParent(), "Dashboard");
        });
        editProfilePanel.add(saveButton);

        // Adding editProfilePanel to the EditProfilePanel (which is now a JPanel)
        add(editProfilePanel);
    }
}
