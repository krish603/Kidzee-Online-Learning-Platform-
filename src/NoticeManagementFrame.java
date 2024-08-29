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

public class NoticeManagementFrame extends JFrame {
    private JTextField titleField;
    private JComboBox<String> departmentComboBox;
    private JTextField pdfFilePathField;
    private JTextField searchField;
    private MainFrame mainFrame;
    private JTextField idField;
    private DefaultTableModel tableModel;
    private JTable timetableTable;
    private JButton insertButton, updateButton, removeButton, resetButton, browseButton;
    private String[] departments = {"students", "lecturers", "departments"};
    
    public NoticeManagementFrame() {
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

        //on clicking this button this frame should close and DashboardPanel class should run

        //go back button

        JButton goBackButton = new JButton(new ImageIcon("img/buttons/arrow.png")); // Replace with the actual path
        goBackButton.setBounds(30, 20, 50, 50); // Adjust as needed
        goBackButton.addActionListener(e -> showDashboardFrame());
        add(goBackButton);

        JLabel titleLabel = new JLabel("NOTICE MANAGEMENT PORTAL");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(350, 20, 350, 30);
        headerPanel.add(titleLabel);

        JLabel facultyLabel = new JLabel("© Kidzee E-Learning Platform");
        facultyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        facultyLabel.setForeground(Color.WHITE);
        facultyLabel.setBounds(420, 50, 200, 20);
        headerPanel.add(facultyLabel);

        // ID Label
        JLabel idLabel = new JLabel("ID:");
        idLabel.setBounds(50, 100, 100, 30);
        add(idLabel);

        // JLabel idValueLabel = new JLabel("T0005"); // Placeholder value
        // idValueLabel.setBounds(150, 100, 100, 30);
        // add(idValueLabel);

        idField = new JTextField();
        idField.setBounds(150, 100, 100, 30);
        getContentPane().add(idField);

        // Title
        JLabel titleLabel2 = new JLabel("Title");
        titleLabel2.setBounds(50, 140, 100, 30);
        add(titleLabel2);

        titleField = new JTextField();
        titleField.setBounds(150, 140, 300, 30);
        add(titleField);

        // Department
        JLabel departmentLabel = new JLabel("For Whom");
        departmentLabel.setBounds(50, 180, 100, 30);
        add(departmentLabel);

        departmentComboBox = new JComboBox<>(departments);
        departmentComboBox.setBounds(150, 180, 100, 30);
        add(departmentComboBox);

        // Browse PDF
        JLabel pdfLabel = new JLabel("PDF File:");
        pdfLabel.setBounds(50, 220, 100, 30);
        add(pdfLabel);

        pdfFilePathField = new JTextField();
        pdfFilePathField.setBounds(150, 220, 300, 30);
        add(pdfFilePathField);

        browseButton = new JButton("Browse PDF");
        browseButton.setBounds(460, 220, 120, 30);
        browseButton.addActionListener(e -> browseFile());
        add(browseButton);

        // Buttons
        insertButton = new JButton("INSERT");
        insertButton.setBounds(600, 140, 100, 30);
        insertButton.addActionListener(e -> insertTimetable());
        add(insertButton);

        updateButton = new JButton("UPDATE");
        updateButton.setBounds(600, 180, 100, 30);
        updateButton.addActionListener(e -> updateTimetable());
        add(updateButton);

        removeButton = new JButton("REMOVE");
        removeButton.setBounds(600, 220, 100, 30);
        removeButton.addActionListener(e -> removeTimetable());
        add(removeButton);

        resetButton = new JButton("RESET");
        resetButton.setBounds(600, 260, 100, 30);
        resetButton.addActionListener(e -> resetFields());
        add(resetButton);

        // Search
        searchField = new JTextField();
        searchField.setBounds(710, 140, 100, 30);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchTimetable(searchField.getText());
            }
        });
        add(searchField);

        // Table
        String[] columnNames = {"ID", "DepID", "Title", "TableContent (click to open)"};
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
        tableScrollPane.setBounds(50, 300, 900, 200);
        add(tableScrollPane);

        // Fill the table with sample data
        addSampleData();

        setVisible(true);
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

    private void insertTimetable() {
        String id = "N000" + (tableModel.getRowCount() + 1); // Generate new ID
        String department = departmentComboBox.getSelectedItem().toString();
        String title = titleField.getText();
        String pdfPath = pdfFilePathField.getText();
        
        if (!title.isEmpty() && !pdfPath.isEmpty()) {
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO notices (id, department, title, pdf_path) VALUES (?, ?, ?, ?)")) {
                
                stmt.setString(1, id);
                stmt.setString(2, department);
                stmt.setString(3, title);
                stmt.setString(4, pdfPath);
                stmt.executeUpdate();
                
                // Add to table model
                Vector<String> row = new Vector<>();
                row.add(id);
                row.add(department);
                row.add(title);
                row.add("Download");
                tableModel.addRow(row);
                
                JOptionPane.showMessageDialog(this, "Notice inserted successfully!");
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error inserting notice: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all the fields!");
        }
    }
    
    private void updateTimetable() {
        int selectedRow = timetableTable.getSelectedRow();
        if (selectedRow != -1) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            String department = departmentComboBox.getSelectedItem().toString();
            String title = titleField.getText();
            String pdfPath = pdfFilePathField.getText();
        
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("UPDATE notices SET department = ?, title = ?, pdf_path = ? WHERE id = ?")) {
                
                stmt.setString(1, department);
                stmt.setString(2, title);
                stmt.setString(3, pdfPath);
                stmt.setString(4, id);
                stmt.executeUpdate();
                
                // Update table model
                tableModel.setValueAt(department, selectedRow, 1);
                tableModel.setValueAt(title, selectedRow, 2);
                tableModel.setValueAt("Download", selectedRow, 3);
                
                JOptionPane.showMessageDialog(this, "Notice updated successfully!");
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error updating notice: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a notice to update!");
        }
    }
    
    private void removeTimetable() {
        int selectedRow = timetableTable.getSelectedRow();
        if (selectedRow != -1) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
        
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM notices WHERE id = ?")) {
                
                stmt.setString(1, id);
                stmt.executeUpdate();
                
                tableModel.removeRow(selectedRow);
                
                JOptionPane.showMessageDialog(this, "Notice removed successfully!");
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error removing notice: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a notice to remove!");
        }
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


    private void resetFields() {
        // Reset input fields
        titleField.setText("");
        departmentComboBox.setSelectedIndex(0);
        pdfFilePathField.setText("");
    }

    private void searchTimetable(String query) {
        // Search in the timetable table
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        timetableTable.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter(query));
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
        SwingUtilities.invokeLater(NoticeManagementFrame::new);
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
