import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class UserNoticeManagementFrame extends JFrame {
    private JTextField searchField;
    private DefaultTableModel tableModel;
    private JTable timetableTable;
    private JButton goBackButton;
    
    public UserNoticeManagementFrame() {
        setTitle("Kidzee E-Learning Platform");
        setSize(1025, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(100, 149, 237));
        headerPanel.setBounds(0, 0, 1025, 80);
        headerPanel.setLayout(null);
        add(headerPanel);

        // Go back button
        goBackButton = new JButton(new ImageIcon("img/buttons/arrow.png")); // Replace with the actual path
        goBackButton.setBounds(30, 20, 50, 50); // Adjust as needed
        goBackButton.addActionListener(e -> showDashboardFrame());
        headerPanel.add(goBackButton);

        JLabel titleLabel = new JLabel("YOUR NOTICES");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(440, 20, 350, 30);
        headerPanel.add(titleLabel);

        JLabel facultyLabel = new JLabel("© Kidzee E-Learning Platform");
        facultyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        facultyLabel.setForeground(Color.WHITE);
        facultyLabel.setBounds(420, 50, 200, 20);
        headerPanel.add(facultyLabel);

        // Search
        searchField = new JTextField();
        searchField.setBounds(50, 100, 300, 30);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchTimetable(searchField.getText());
            }
        });
        add(searchField);

        // Table
        String[] columnNames = {"ID", "Department", "Title", "Download"};
        tableModel = new DefaultTableModel(columnNames, 0);
        timetableTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        timetableTable.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        timetableTable.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox()));
        JScrollPane tableScrollPane = new JScrollPane(timetableTable);
        tableScrollPane.setBounds(50, 150, 900, 400);
        add(tableScrollPane);

        // Fill the table with data
        addSampleData();

        setVisible(true);
    }

    private void addSampleData() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM notices")) {
            
            while (rs.next()) {
                String id = rs.getString("id");
                String department = rs.getString("department");
                String title = rs.getString("title");
                String pdfPath = rs.getString("pdf_path");
            
                Vector<String> row = new Vector<>();
                row.add(id);
                row.add(department);
                row.add(title);
                row.add(pdfPath);
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading sample data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void searchTimetable(String query) {
        // Search in the timetable table
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        timetableTable.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter(query));
    }

    private void showDashboardFrame() {
        this.dispose();
        UserDashboardFrame dashboardFrame = new UserDashboardFrame();
        dashboardFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UserNoticeManagementFrame::new);
    }
}

// Renderer and Editor for the Download button in the table
class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "" : value.toString());
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {
    private String label;
    private boolean isPushed;

    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        label = (value == null) ? "" : value.toString();
        JButton button = new JButton(label);
        button.addActionListener(e -> {
            if (isPushed) {
                // Handle download logic here, for now we just show a message
                JOptionPane.showMessageDialog(button, "Downloading: " + table.getValueAt(row, 2));
            }
            isPushed = false;
            fireEditingStopped();
        });
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        isPushed = true;
        return label;
    }
}
