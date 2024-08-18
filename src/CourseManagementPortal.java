import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CourseManagementPortal extends JFrame {

    private JTextField courseIdField, nameField, creditField, depIdField, lecIdField, searchField;
    private JComboBox<String> isGpaComboBox, semesterComboBox;
    private JTable courseTable;
    private DefaultTableModel tableModel;

    public CourseManagementPortal() {
        // Frame settings
        setTitle("Course Management Portal");
        setSize(1025, 640);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        // Top Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(100, 149, 237));
        headerPanel.setBounds(0, 0, 1025, 80);
        headerPanel.setLayout(null);
        add(headerPanel);

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

        JTextArea descriptionField = new JTextArea();
        descriptionField.setBounds(370, 100, 220, 100);
        getContentPane().add(descriptionField);

        JLabel lblSemester = new JLabel("Semester:");
        lblSemester.setBounds(280, 220, 80, 25);
        getContentPane().add(lblSemester);

        semesterComboBox = new JComboBox<>(new String[]{"L1S1", "L1S2", "L2S1", "L2S2"});
        semesterComboBox.setBounds(370, 220, 220, 25);
        getContentPane().add(semesterComboBox);

        JButton btnMaterials = new JButton("MATERIALS");
        btnMaterials.setBounds(370, 260, 220, 30);
        getContentPane().add(btnMaterials);

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
        getContentPane().add(searchField);

        JButton searchButton = new JButton("SEARCH");
        searchButton.setBounds(740, 260, 100, 30);
        getContentPane().add(searchButton);

        // Table
        String[] columnNames = {"CourseID", "CourseName", "Credit", "isGPA", "Description", "Materials", "userID", "DepID", "LevelSem"};
        tableModel = new DefaultTableModel(columnNames, 0);
        courseTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(courseTable);
        scrollPane.setBounds(30, 340, 950, 250);
        getContentPane().add(scrollPane);

        // Action listeners
        insertButton.addActionListener(e -> {
            String[] data = {
                    courseIdField.getText(),
                    nameField.getText(),
                    creditField.getText(),
                    isGpaComboBox.getSelectedItem().toString(),
                    descriptionField.getText(),
                    "Download",  // Placeholder for Materials button
                    lecIdField.getText(),
                    depIdField.getText(),
                    semesterComboBox.getSelectedItem().toString()
            };
            tableModel.addRow(data);
        });

        updateButton.addActionListener(e -> {
            int selectedRow = courseTable.getSelectedRow();
            if (selectedRow >= 0) {
                tableModel.setValueAt(courseIdField.getText(), selectedRow, 0);
                tableModel.setValueAt(nameField.getText(), selectedRow, 1);
                tableModel.setValueAt(creditField.getText(), selectedRow, 2);
                tableModel.setValueAt(isGpaComboBox.getSelectedItem(), selectedRow, 3);
                tableModel.setValueAt(descriptionField.getText(), selectedRow, 4);
                tableModel.setValueAt("Download", selectedRow, 5);  // Placeholder for Materials button
                tableModel.setValueAt(lecIdField.getText(), selectedRow, 6);
                tableModel.setValueAt(depIdField.getText(), selectedRow, 7);
                tableModel.setValueAt(semesterComboBox.getSelectedItem(), selectedRow, 8);
            }
        });

        removeButton.addActionListener(e -> {
            int selectedRow = courseTable.getSelectedRow();
            if (selectedRow >= 0) {
                tableModel.removeRow(selectedRow);
            }
        });

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

        searchButton.addActionListener(e -> {
            String searchText = searchField.getText();
            for (int row = 0; row < tableModel.getRowCount(); row++) {
                if (tableModel.getValueAt(row, 0).toString().contains(searchText)) {
                    courseTable.setRowSelectionInterval(row, row);
                    break;
                }
            }
        });
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
