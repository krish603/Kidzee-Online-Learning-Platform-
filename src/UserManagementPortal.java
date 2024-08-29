import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.Vector;

public class UserManagementPortal extends JFrame {
    private MainFrame mainFrame;
    private Connection conn; // Database connection
    private DefaultTableModel model;
    private JTextField idField, userNameField, nameField, emailField, dobField, addressField, contactField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeCombo, genderCombo, departmentCombo, levelCombo;
    private JTable userTable;

    public UserManagementPortal() {
        // Frame settings
        setTitle("Kidzee E-Learning Platform");
        setSize(1025, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Database connection
        connectToDatabase();

        // UI Setup
        setupUI();
        loadUserData(); // Load data from the database
    }

    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kidzee_login", "root", ""); // Update with your DB credentials
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupUI() {
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

        // Left Panel
        setupLeftPanel();

        // Right Panel
        setupRightPanel();

        // Table Panel
        setupTablePanel();
    }

    private void setupLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setBounds(20, 110, 520, 230);
        leftPanel.setLayout(null);
        leftPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        String[] labels1 = {"ID", "User Name", "Name", "Email", "Password", "Contact No", "UserType"};
        String[] labels2 = {"Gender", "Department", "DOB", "Address", "Level"};

        int yPos1 = 20;
        for (String label : labels1) {
            JLabel lbl = new JLabel(label);
            lbl.setBounds(10, yPos1, 100, 25);
            leftPanel.add(lbl);
            yPos1 += 30;
        }

        idField = new JTextField();
        idField.setBounds(90, 20, 150, 25);
        leftPanel.add(idField);

        userNameField = new JTextField();
        userNameField.setBounds(90, 50, 150, 25);
        leftPanel.add(userNameField);

        nameField = new JTextField();
        nameField.setBounds(90, 80, 150, 25);
        leftPanel.add(nameField);

        emailField = new JTextField();
        emailField.setBounds(90, 110, 150, 25);
        leftPanel.add(emailField);

        passwordField = new JPasswordField();
        passwordField.setBounds(90, 140, 150, 25);
        leftPanel.add(passwordField);

        contactField = new JTextField();
        contactField.setBounds(90, 170, 150, 25);
        leftPanel.add(contactField);

        userTypeCombo = new JComboBox<>(new String[] {"student", "lecturer", "admin"});
        userTypeCombo.setBounds(90, 200, 150, 25);
        leftPanel.add(userTypeCombo);

        int yPos2 = 20;
        for (String label : labels2) {
            JLabel lbl = new JLabel(label);
            lbl.setBounds(260, yPos2, 100, 25);
            leftPanel.add(lbl);
            yPos2 += 40;
        }

        genderCombo = new JComboBox<>(new String[] {"male", "female"});
        genderCombo.setBounds(360, 20, 150, 25);
        leftPanel.add(genderCombo);

        departmentCombo = new JComboBox<>(new String[] {"ict", "bst", "et"});
        departmentCombo.setBounds(360, 60, 150, 25);
        leftPanel.add(departmentCombo);

        dobField = new JTextField();
        dobField.setBounds(360, 100, 150, 25);
        leftPanel.add(dobField);

        addressField = new JTextField();
        addressField.setBounds(360, 140, 150, 25);
        leftPanel.add(addressField);

        levelCombo = new JComboBox<>(new String[] {"L1S1", "L1S2", "NULL"});
        levelCombo.setBounds(360, 180, 150, 25);
        leftPanel.add(levelCombo);

        add(leftPanel);
    }

    private void setupRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setBounds(560, 110, 440, 100);
        rightPanel.setLayout(new GridLayout(2, 2, 10, 10));

        JButton insertButton = new JButton("INSERT");
        insertButton.addActionListener(e -> insertUser());
        rightPanel.add(insertButton);

        JButton updateButton = new JButton("UPDATE");
        updateButton.addActionListener(e -> updateUser());
        rightPanel.add(updateButton);

        JButton removeButton = new JButton("REMOVE");
        removeButton.addActionListener(e -> removeUser());
        rightPanel.add(removeButton);

        JTextField searchField = new JTextField();
        // searchField.addActionListener(e -> searchUser(searchField.getText()));
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchUser(searchField.getText());
            }
        });
        rightPanel.add(searchField);

        JButton resetButton = new JButton("RESET");
        resetButton.addActionListener(e -> resetFields());
        rightPanel.add(resetButton);

        add(rightPanel);
    }

    private void setupTablePanel() {
        JPanel tablePanel = new JPanel();
        tablePanel.setBounds(20, 350, 980, 200);
        tablePanel.setLayout(new BorderLayout());

        String[] columnNames = {"id", "username", "name", "email", "password", "userType", "gender", "dob", 
                                "contactNumber", "address", "depID", "Level/Sem"};

        model = new DefaultTableModel(columnNames, 0);
        userTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(userTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel);
    }

    private void loadUserData() {
        try {
            String query = "SELECT * FROM users";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vector<String> row = new Vector<>();
                for (int i = 1; i <= 12; i++) {
                    row.add(rs.getString(i));
                }
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertUser() {
        try {
            String query = "INSERT INTO users (id, username, name, email, password, userType, gender, dob, contactNumber, address, depID, level) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, idField.getText());
            stmt.setString(2, userNameField.getText());
            stmt.setString(3, nameField.getText());
            stmt.setString(4, emailField.getText());
            stmt.setString(5, new String(passwordField.getPassword()));
            stmt.setString(6, (String) userTypeCombo.getSelectedItem());
            stmt.setString(7, (String) genderCombo.getSelectedItem());
            stmt.setString(8, dobField.getText());
            stmt.setString(9, contactField.getText());
            stmt.setString(10, addressField.getText());
            stmt.setString(11, (String) departmentCombo.getSelectedItem());
            stmt.setString(12, (String) levelCombo.getSelectedItem());
            stmt.executeUpdate();

            model.addRow(new Object[]{idField.getText(), userNameField.getText(), nameField.getText(), emailField.getText(), new String(passwordField.getPassword()), 
                                      userTypeCombo.getSelectedItem(), genderCombo.getSelectedItem(), dobField.getText(), contactField.getText(), addressField.getText(), 
                                      departmentCombo.getSelectedItem(), levelCombo.getSelectedItem()});
            resetFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
            try {
                String query = "UPDATE users SET username=?, name=?, email=?, password=?, userType=?, gender=?, dob=?, contactNumber=?, address=?, depID=?, level=? WHERE id=?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, userNameField.getText());
                stmt.setString(2, nameField.getText());
                stmt.setString(3, emailField.getText());
                stmt.setString(4, new String(passwordField.getPassword()));
                stmt.setString(5, (String) userTypeCombo.getSelectedItem());
                stmt.setString(6, (String) genderCombo.getSelectedItem());
                stmt.setString(7, dobField.getText());
                stmt.setString(8, contactField.getText());
                stmt.setString(9, addressField.getText());
                stmt.setString(10, (String) departmentCombo.getSelectedItem());
                stmt.setString(11, (String) levelCombo.getSelectedItem());
                stmt.setString(12, idField.getText());
                stmt.executeUpdate();

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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
            try {
                String query = "DELETE FROM users WHERE id=?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, (String) model.getValueAt(selectedRow, 0));
                stmt.executeUpdate();

                model.removeRow(selectedRow);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void searchUser(String query) {
        // Search in the timetable table
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        userTable.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter(query));
    }

    private void resetFields() {
        idField.setText("");
        userNameField.setText("");
        nameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        contactField.setText("");
        dobField.setText("");
        addressField.setText("");
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserManagementPortal frame = new UserManagementPortal();
            frame.setVisible(true);
        });
    }
}
