import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class UserCoursePage extends JFrame {
    private JTable courseTable;
    private Connection conn;
    private DefaultTableModel tableModel;
    private final String DB_URL = "jdbc:mysql://localhost:3306/kidzee_login"; // Replace with your database name
    private final String DB_USER = "root"; // Replace with your database username
    private final String DB_PASSWORD = "";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public UserCoursePage() {
        // Frame settings
        setTitle("Kidzee E-Learning Platform - Courses");
        setSize(1025, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        connectToDatabase();

        // Top Panel (Header)
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

        JLabel titleLabel = new JLabel("YOUR COURSES");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(440, 20, 350, 30);
        headerPanel.add(titleLabel);

        JLabel facultyLabel = new JLabel("© Kidzee E-Learning Platform");
        facultyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        facultyLabel.setForeground(Color.WHITE);
        facultyLabel.setBounds(420, 50, 200, 20);
        headerPanel.add(facultyLabel);

        // Table
        String[] columnNames = {"CourseID", "CourseName", "Credit", "isGPA", "Description", "Materials", "DepID", "LevelSem"};
        tableModel = new DefaultTableModel(columnNames, 0);
        courseTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JScrollPane scrollPane = new JScrollPane(courseTable);
        scrollPane.setBounds(30, 100, 950, 500);
        getContentPane().add(scrollPane);

        loadCourseData();
    }

    private void loadCourseData() {
        try {
            String query = "SELECT courseId, name, credit, isGpa, description, materials, depId, levelSem FROM courses";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vector<String> row = new Vector<>();
                for (int i = 1; i <= 8; i++) {
                    row.add(rs.getString(i));
                }
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showDashboardFrame() {
        this.dispose();
        UserDashboardFrame dashboardFrame = new UserDashboardFrame();
        dashboardFrame.setVisible(true);
    }

    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UserCoursePage frame = new UserCoursePage();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
