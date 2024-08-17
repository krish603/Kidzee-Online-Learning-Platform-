import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class DashboardPanel extends JPanel {
    private Image backgroundImage;
    private MainFrame mainFrame;
    // private First_Interface firstInterface;

    public DashboardPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

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

        JLabel emailLabel = new JLabel("admin@gmail.com");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setFont(new Font("Arial", Font.BOLD, 16));
        emailLabel.setBounds(50, 260, 200, 30);
        add(emailLabel);

        // Edit profile button with image
        JButton editProfileButton = new JButton(new ImageIcon("img/buttons/user_profile.png")); // Replace with the actual path
        editProfileButton.setBounds(70, 300, 100, 50); // Adjust as needed
        editProfileButton.addActionListener(e -> showEditProfilePanel());
        add(editProfileButton);

        // User logout button with image
        JButton logoutButton = new JButton(new ImageIcon("img/buttons/logout.png")); // Replace with the actual path
        logoutButton.setBounds(70, 370, 100, 50); // Adjust as needed
        add(logoutButton);

        JLabel adminPanelLabel = new JLabel("Admin Panel");
        adminPanelLabel.setForeground(Color.WHITE);
        adminPanelLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        adminPanelLabel.setBounds(70, 560, 200, 30);
        add(adminPanelLabel);

        JButton notificationBellButton = new JButton(new ImageIcon("img/notification_bell.png"));
        notificationBellButton.setBounds(910, 20, 50, 50); 
        add(notificationBellButton);

        JLabel studentLabel = createRoundedLabel("img/students.png", "img/students_hover.png", 320, 100);
        add(studentLabel);

        JLabel lecturerLabel = createRoundedLabel("img/lecturers.png", "img/lecturers_hover.png", 540, 100);
        add(lecturerLabel);

        JLabel courseLabel = createRoundedLabel("img/courses.png", "img/courses_hover.png", 760, 100);
        add(courseLabel);  

        JLabel timetable = new JLabel(new ImageIcon("img/timetable.png"));
        timetable.setBounds(350, 300, 150, 150); // Example positioning, adjust as needed
        add(timetable);

        JButton timetableButton = new JButton(new ImageIcon("img/buttons/timetableButton.png")); // Replace with the actual path
        timetableButton.setBounds(380, 475, 100, 50); // Adjust as needed
        add(timetableButton);

        JLabel notification = new JLabel(new ImageIcon("img/notification.png"));
        notification.setBounds(500, 300, 150, 150); // Example positioning, adjust as needed
        add(notification);

        JButton notificationButton = new JButton(new ImageIcon("img/buttons/notificationButton.png")); // Replace with the actual path
        notificationButton.setBounds(530, 475, 100, 50); // Adjust as needed
        add(notificationButton);
        
        JLabel userManage = new JLabel(new ImageIcon("img/user_manage.png"));
        userManage.setBounds(650, 300, 150, 150); // Example positioning, adjust as needed
        add(userManage);

        JButton userManageButton = new JButton(new ImageIcon("img/buttons/userManageButton.png")); // Replace with the actual path
        userManageButton.setBounds(680, 475, 100, 50); // Adjust as needed
        add(userManageButton);
        
        JLabel course = new JLabel(new ImageIcon("img/course.png"));
        course.setBounds(800, 300, 150, 150); // Example positioning, adjust as needed
        add(course);

        JButton courseButton = new JButton(new ImageIcon("img/buttons/courseButton.png")); // Replace with the actual path
        courseButton.setBounds(830, 475, 100, 50); // Adjust as needed
        add(courseButton);

        JLabel greyBox = new JLabel(new ImageIcon("img/grey_box.png"));
        greyBox.setBounds(320, 250, 650, 300); // Example positioning, adjust as needed
        add(greyBox);
    }

    private JLabel createRoundedLabel(String imagePath, String hoverImagePath, int x, int y) {
        ImageIcon normalIcon = RoundedImage.getRoundedImage(new ImageIcon(imagePath).getImage(), 200, 100, 20);
        ImageIcon hoverIcon = RoundedImage.getRoundedImage(new ImageIcon(hoverImagePath).getImage(), 200, 100, 20);

        JLabel label = new JLabel(normalIcon);
        label.setBounds(x, y, 200, 100);

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setIcon(hoverIcon);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setIcon(normalIcon);
            }
        });

        return label;
    }

   private void showEditProfilePanel() {
        if (mainFrame != null) { // Check if mainFrame is not null
            EditProfilePanel editProfilePanel = new EditProfilePanel();
            mainFrame.setContentPane(editProfilePanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        } else {
            System.err.println("Error: MainFrame reference is null.");
        }
    }    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
