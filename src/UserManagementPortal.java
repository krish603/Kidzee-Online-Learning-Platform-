import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UserManagementPortal extends JFrame {
    private MainFrame mainFrame;
    
    public UserManagementPortal() {
        // Frame settings
        setTitle("User Management Portal");
        setSize(1025, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Top Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(100, 149, 237));
        headerPanel.setBounds(0, 0, 1025, 80);
        headerPanel.setLayout(null);
        add(headerPanel);

        JButton goBackButton = new JButton(new ImageIcon("img/buttons/arrow.png")); // Replace with the actual path
        goBackButton.setBounds(30, 20, 50, 50); // Adjust as needed
        goBackButton.addActionListener(e -> showDashboardFrame());
        add(goBackButton);

        JLabel titleLabel = new JLabel("USER MANAGEMENT PORTAL");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(350, 20, 350, 30);
        headerPanel.add(titleLabel);

        JLabel facultyLabel = new JLabel("© Kidzee E-Learning Platform");
        facultyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        facultyLabel.setForeground(Color.WHITE);
        facultyLabel.setBounds(420, 50, 200, 20);
        headerPanel.add(facultyLabel);

        // Left Section for User Details
        JPanel leftPanel = new JPanel();
        leftPanel.setBounds(20, 110, 520, 230);
        leftPanel.setLayout(null);
        leftPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        String[] labels1 = {"ID", "User Name", "Name", "Email", "Password","Contact No", "UserType"};
        String[] labels2 = {"Gender", "Department", "DOB", "Address", "Level"};

        int yPos1 = 20;
        for (String label : labels1) {
            JLabel lbl = new JLabel(label);
            lbl.setBounds(10, yPos1, 100, 25);
            leftPanel.add(lbl);
            yPos1 += 30;
        }

        JTextField idField = new JTextField();
        idField.setBounds(90, 20, 150, 25);
        leftPanel.add(idField);

        JTextField userNameField = new JTextField();
        userNameField.setBounds(90, 50, 150, 25);
        leftPanel.add(userNameField);

        JTextField nameField = new JTextField();
        nameField.setBounds(90, 80, 150, 25);
        leftPanel.add(nameField);

        JTextField emailField = new JTextField();
        emailField.setBounds(90, 110, 150, 25);
        leftPanel.add(emailField);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(90, 140, 150, 25);
        leftPanel.add(passwordField);

        JTextField contactField = new JTextField();
        contactField.setBounds(90, 170, 150, 25);
        leftPanel.add(contactField);

        JComboBox<String> userTypeCombo = new JComboBox<>(new String[] {"student", "lecturer", "admin"});
        userTypeCombo.setBounds(90, 200, 150, 25);
        leftPanel.add(userTypeCombo);

        int yPos2 = 20;
        for (String label : labels2) {
            JLabel lbl = new JLabel(label);
            lbl.setBounds(260, yPos2, 100, 25);
            leftPanel.add(lbl);
            yPos2 += 40;
        }

        JComboBox<String> genderCombo = new JComboBox<>(new String[] {"male", "female"});
        genderCombo.setBounds(360, 20, 150, 25);
        leftPanel.add(genderCombo);

        JComboBox<String> departmentCombo = new JComboBox<>(new String[] {"ict", "bst", "et"});
        departmentCombo.setBounds(360, 60, 150, 25);
        leftPanel.add(departmentCombo);

        JTextField dobField = new JTextField();
        dobField.setBounds(360, 100, 150, 25);
        leftPanel.add(dobField);

        JTextField addressField = new JTextField();
        addressField.setBounds(360, 140, 150, 25);
        leftPanel.add(addressField);

        JComboBox<String> levelCombo = new JComboBox<>(new String[] {"L1S1", "L1S2", "NULL"});
        levelCombo.setBounds(360, 180, 150, 25);
        leftPanel.add(levelCombo);

        add(leftPanel);

        // Right Section for Buttons
        JPanel rightPanel = new JPanel();
        rightPanel.setBounds(560, 110, 440, 100);
        rightPanel.setLayout(new GridLayout(2, 2, 10, 10));

        JButton insertButton = new JButton("INSERT");
        rightPanel.add(insertButton);

        JButton updateButton = new JButton("UPDATE");
        rightPanel.add(updateButton);

        JButton removeButton = new JButton("REMOVE");
        rightPanel.add(removeButton);

        JTextField searchField = new JTextField();
        rightPanel.add(searchField);

        JButton resetButton = new JButton("RESET");
        rightPanel.add(resetButton);

        add(rightPanel);

        // Table Section
        JPanel tablePanel = new JPanel();
        tablePanel.setBounds(20, 350, 980, 200);
        tablePanel.setLayout(new BorderLayout());

        String[] columnNames = {"id", "username", "name", "email", "password", "userType", "gender", "dob", 
                                "contactNumber", "address", "depID", "Level/Sem"};

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable userTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(userTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel);

        // Sample Data for testing
        model.addRow(new Object[]{"A0001", "admin", "Admin Name", "admin@example.com", "adminpass", "admin", 
                                  "male", "2000-01-01", "1234567890", "Address", "ict", "NULL"});
        model.addRow(new Object[]{"S0002", "student1", "Student Name", "student@example.com", "studentpass", "student", 
                                  "female", "1999-02-15", "0987654321", "Address", "et", "L1S1"});

        // Button Functionality (Sample)
        insertButton.addActionListener(e -> {
            String id = idField.getText();
            String username = userNameField.getText();
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String contactNo = contactField.getText();
            String userType = (String) userTypeCombo.getSelectedItem();
            String gender = (String) genderCombo.getSelectedItem();
            String department = (String) departmentCombo.getSelectedItem();
            String dob = dobField.getText();
            String address = addressField.getText();
            String level = (String) levelCombo.getSelectedItem();

            model.addRow(new Object[]{id, username, name, email, password, userType, gender, dob, contactNo, address, department, level});
        });

        updateButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                model.setValueAt(idField.getText(), selectedRow, 0);
                model.setValueAt(userNameField.getText(), selectedRow, 1);
                model.setValueAt(nameField.getText(), selectedRow, 2);
                model.setValueAt(emailField.getText(), selectedRow, 3);
                model.setValueAt(new String(passwordField.getPassword()), selectedRow, 4);
                model.setValueAt(userTypeCombo.getSelectedItem(), selectedRow, 5);
                model.setValueAt(genderCombo.getSelectedItem(), selectedRow, 6);
                model.setValueAt(dobField.getText(), selectedRow, 7);
                model.setValueAt(contactField.getText(), selectedRow, 8);
                model.setValueAt(addressField.getText(), selectedRow, 9);
                model.setValueAt(departmentCombo.getSelectedItem(), selectedRow, 10);
                model.setValueAt(levelCombo.getSelectedItem(), selectedRow, 11);
            }
        });

        removeButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                model.removeRow(selectedRow);
            }
        });

        resetButton.addActionListener(e -> {
            idField.setText("");
            userNameField.setText("");
            nameField.setText("");
            emailField.setText("");
            passwordField.setText("");
            contactField.setText("");
            dobField.setText("");
            addressField.setText("");
        });
    }

    private void showDashboardFrame() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

        if (parentFrame != null) {
            parentFrame.dispose(); // Close the parent JFrame
        }

        // Create and show the new frame
        DashboardPanel dFrame = new DashboardPanel(mainFrame);
        dFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserManagementPortal frame = new UserManagementPortal();
            frame.setVisible(true);
        });
    }
}
