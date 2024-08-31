import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class CourseManagementPortal extends JFrame {
    private MainFrame mainFrame;
    private JTextField pdfFilePathField;
    private JTextField courseIdField, nameField, creditField, depIdField, lecIdField, searchField;
    private JTextArea descriptionField;
    private JComboBox<String> isGpaComboBox, semesterComboBox;
    private JTable courseTable;
    private Connection conn;
    private DefaultTableModel tableModel;
    private final String DB_URL = "jdbc:mysql://localhost:3306/kidzee_login"; // Replace with your database name
    private final String DB_USER = "root"; // Replace with your database username
    private final String DB_PASSWORD = "";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public CourseManagementPortal() {
        // Frame settings
        setTitle("Kidzee E-Learning Platform");
        setSize(1025, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        connectToDatabase();

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

        JLabel titleLabel = new JLabel("COURSE MANAGEMENT PORTAL");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(350, 20, 350, 30);
        headerPanel.add(titleLabel);

        JLabel facultyLabel = new JLabel("© Kidzee E-Learning Platform");
        facultyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        facultyLabel.setForeground(Color.WHITE);
        facultyLabel.setBounds(420, 50, 200, 20);
        headerPanel.add(facultyLabel);

        // Labels and Text fields
        JLabel lblCourseId = new JLabel("CourseID:");
        lblCourseId.setBounds(30, 100, 80, 25);
        getContentPane().add(lblCourseId);

        courseIdField = new JTextField();
        courseIdField.setBounds(110, 100, 150, 25);
        getContentPane().add(courseIdField);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(30, 140, 80, 25);
        getContentPane().add(lblName);

        nameField = new JTextField();
        nameField.setBounds(110, 140, 150, 25);
        getContentPane().add(nameField);

        JLabel lblIsGpa = new JLabel("isGPA:");
        lblIsGpa.setBounds(30, 180, 80, 25);
        getContentPane().add(lblIsGpa);

        isGpaComboBox = new JComboBox<>(new String[]{"gpa", "non-gpa"});
        isGpaComboBox.setBounds(110, 180, 150, 25);
        getContentPane().add(isGpaComboBox);

        JLabel lblCredit = new JLabel("Credit:");
        lblCredit.setBounds(30, 220, 80, 25);
        getContentPane().add(lblCredit);

        creditField = new JTextField();
        creditField.setBounds(110, 220, 150, 25);
        getContentPane().add(creditField);

        JLabel lblDepId = new JLabel("DepID:");
        lblDepId.setBounds(30, 260, 80, 25);
        getContentPane().add(lblDepId);

        depIdField = new JTextField();
        depIdField.setBounds(110, 260, 150, 25);
        getContentPane().add(depIdField);

        JLabel lblLecId = new JLabel("LecID:");
        lblLecId.setBounds(30, 300, 80, 25);
        getContentPane().add(lblLecId);

        lecIdField = new JTextField();
        lecIdField.setBounds(110, 300, 150, 25);
        getContentPane().add(lecIdField);

        JLabel lblDescription = new JLabel("Description:");
        lblDescription.setBounds(280, 100, 80, 25);
        getContentPane().add(lblDescription);

        descriptionField = new JTextArea();
        descriptionField.setBounds(370, 100, 220, 100);
        getContentPane().add(descriptionField);

        JLabel lblSemester = new JLabel("Semester:");
        lblSemester.setBounds(280, 220, 80, 25);
        getContentPane().add(lblSemester);

        semesterComboBox = new JComboBox<>(new String[]{"L1S1", "L1S2", "L2S1", "L2S2"});
        semesterComboBox.setBounds(370, 220, 220, 25);
        getContentPane().add(semesterComboBox);

        // JButton btnMaterials = new JButton("MATERIALS");
        // btnMaterials.setBounds(370, 260, 220, 30);
        // getContentPane().add(btnMaterials);

        JLabel pdfLabel = new JLabel("Materials :");
        pdfLabel.setBounds(280, 260, 100, 30);
        add(pdfLabel);

        pdfFilePathField = new JTextField();
        pdfFilePathField.setBounds(370, 260, 220, 30);
        add(pdfFilePathField);

        JButton btnMaterials = new JButton("Browse PDF");
        btnMaterials.setBounds(370, 300, 220, 30);
        btnMaterials.addActionListener(e -> browseFile());
        add(btnMaterials);

        // Right-side buttons
        JButton insertButton = new JButton("INSERT");
        insertButton.setBounds(620, 100, 100, 30);
        getContentPane().add(insertButton);

        JButton updateButton = new JButton("UPDATE");
        updateButton.setBounds(620, 140, 100, 30);
        getContentPane().add(updateButton);

        JButton removeButton = new JButton("REMOVE");
        removeButton.setBounds(620, 180, 100, 30);
        getContentPane().add(removeButton);

        JButton resetButton = new JButton("RESET");
        resetButton.setBounds(620, 220, 100, 30);
        getContentPane().add(resetButton);

        searchField = new JTextField();
        searchField.setBounds(620, 260, 100, 30);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchCourse(searchField.getText());
            }
        });
        getContentPane().add(searchField);

        // JButton searchButton = new JButton("SEARCH");
        // searchButton.setBounds(740, 260, 100, 30);
        // getContentPane().add(searchButton);

        // Table
        String[] columnNames = {"CourseID", "CourseName", "Credit", "isGPA", "Description", "Materials", "userID", "DepID", "LevelSem"};
        tableModel = new DefaultTableModel(columnNames, 0);
        courseTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        courseTable.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        courseTable.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox()));
        JScrollPane scrollPane = new JScrollPane(courseTable);
        scrollPane.setBounds(30, 340, 950, 250);
        getContentPane().add(scrollPane);

        addSampleData();

        insertButton.addActionListener(e -> insertCourse());
        updateButton.addActionListener(e -> updateCourse());
        removeButton.addActionListener(e -> removeCourse());

        resetButton.addActionListener(e -> {
            courseIdField.setText("");
            nameField.setText("");
            creditField.setText("");
            isGpaComboBox.setSelectedIndex(0);
            descriptionField.setText("");
            lecIdField.setText("");
            depIdField.setText("");
            semesterComboBox.setSelectedIndex(0);
        });
    }
    
    private void addSampleData() {
        try {
            String query = "SELECT * FROM courses";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vector<String> row = new Vector<>();
                for (int i = 1; i <= 11; i++) {
                    row.add(rs.getString(i));
                }
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertCourse() {
        String sql = "INSERT INTO courses (courseId, name, credit, isGpa, description, materials, userId, depId, levelSem) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseIdField.getText());
            pstmt.setString(2, nameField.getText());
            pstmt.setInt(3, Integer.parseInt(creditField.getText()));
            pstmt.setString(4, isGpaComboBox.getSelectedItem().toString());
            pstmt.setString(5, descriptionField.getText());
            pstmt.setString(6, pdfFilePathField.getText());
            pstmt.setString(7, lecIdField.getText());
            pstmt.setString(8, depIdField.getText());
            pstmt.setString(9, semesterComboBox.getSelectedItem().toString());
            pstmt.executeUpdate();
            tableModel.addRow(new Object[]{courseIdField.getText(), nameField.getText(), creditField.getText(), isGpaComboBox.getSelectedItem().toString(), descriptionField.getText(), pdfFilePathField.getText(), lecIdField.getText(), depIdField.getText(), semesterComboBox.getSelectedItem().toString()});
            JOptionPane.showMessageDialog(this, "Course added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update course in the database
    private void updateCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow >= 0) {
            String courseId = courseIdField.getText();
            String sql = "UPDATE courses SET name=?, credit=?, isGpa=?, description=?, materials=?, userId=?, depId=?, levelSem=? WHERE courseId=?";
            try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nameField.getText());
                pstmt.setInt(2, Integer.parseInt(creditField.getText()));
                pstmt.setString(3, isGpaComboBox.getSelectedItem().toString());
                pstmt.setString(4, descriptionField.getText());
                pstmt.setString(5, pdfFilePathField.getText());
                pstmt.setString(6, lecIdField.getText());
                pstmt.setString(7, depIdField.getText());
                pstmt.setString(8, semesterComboBox.getSelectedItem().toString());
                pstmt.setString(9, courseId);
                pstmt.executeUpdate();
                tableModel.setValueAt(nameField.getText(), selectedRow, 1);
                tableModel.setValueAt(creditField.getText(), selectedRow, 2);
                tableModel.setValueAt(isGpaComboBox.getSelectedItem().toString(), selectedRow, 3);
                tableModel.setValueAt(descriptionField.getText(), selectedRow, 4);
                tableModel.setValueAt(pdfFilePathField.getText(), selectedRow, 5);
                tableModel.setValueAt(lecIdField.getText(), selectedRow, 6);
                tableModel.setValueAt(depIdField.getText(), selectedRow, 7);
                tableModel.setValueAt(semesterComboBox.getSelectedItem().toString(), selectedRow, 8);
                JOptionPane.showMessageDialog(this, "Course updated successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Remove course from the database
    private void removeCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow >= 0) {
            String courseId = courseTable.getValueAt(selectedRow, 0).toString();
            String sql = "DELETE FROM courses WHERE courseId=?";
            try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, courseId);
                pstmt.executeUpdate();
                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Course removed successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Search for a course in the database
    private void searchCourse(String query) {
        // Search in the timetable table
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        courseTable.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter(query));
    }

    private void browseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Documents", "pdf"));
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            pdfFilePathField.setText(selectedFile.getAbsolutePath());
        }
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

    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kidzee_login", "root", ""); // Update with your DB credentials
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                CourseManagementPortal frame = new CourseManagementPortal();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
