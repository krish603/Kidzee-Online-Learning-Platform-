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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

public class TimetableManagementFrame extends JFrame {
    private JTextField titleField;
    private JComboBox<String> departmentComboBox;
    private JTextField pdfFilePathField;
    private MainFrame mainFrame;
    private JTextField idField;
    private JTextField searchField;
    private DefaultTableModel tableModel;
    private JTable timetableTable;
    private JButton insertButton, updateButton, removeButton, resetButton, browseButton;
    private String[] departments = {"ICT", "ET", "BST"};

    // HashMap for quick access to timetables by ID
    private HashMap<String, Timetable> timetableMap = new HashMap<>();
    // LinkedList to maintain order of timetables
    private LinkedList<Timetable> timetableList = new LinkedList<>();
    
    public TimetableManagementFrame() {
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

        JButton goBackButton = new JButton(new ImageIcon("img/buttons/arrow.png")); // Replace with the actual path
        goBackButton.setBounds(30, 20, 50, 50); // Adjust as needed
        goBackButton.addActionListener(e -> showDashboardFrame());
        add(goBackButton);

        JLabel titleLabel = new JLabel("TIMETABLE MANAGEMENT PORTAL");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(350, 20, 350, 30);
        headerPanel.add(titleLabel);

        JLabel facultyLabel = new JLabel("© Faculty of Technology");
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
        JLabel departmentLabel = new JLabel("Department");
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
        String id = "T000" + (tableModel.getRowCount() + 1); // Generate new ID
        String department = departmentComboBox.getSelectedItem().toString();
        String title = titleField.getText();
        String pdfPath = pdfFilePathField.getText();

        if (!title.isEmpty() && !pdfPath.isEmpty()) {
            Timetable timetable = new Timetable(id, department, title, pdfPath);

            // Save to database
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO timetables (id, department, title, pdf_path) VALUES (?, ?, ?, ?)")) {
                
                stmt.setString(1, id);
                stmt.setString(2, department);
                stmt.setString(3, title);
                stmt.setString(4, pdfPath);
                stmt.executeUpdate();
                
                // Add to data structures
                timetableMap.put(id, timetable);
                timetableList.add(timetable);

                Vector<String> row = new Vector<>();
                row.add(id);
                row.add(department);
                row.add(title);
                row.add("Download");
                tableModel.addRow(row);
                
                JOptionPane.showMessageDialog(this, "Timetable inserted successfully!");

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error inserting timetable: " + e.getMessage());
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
            Timetable timetable = timetableMap.get(id);
            if (timetable != null) {
                // Update the data
                timetable.setDepartment(departmentComboBox.getSelectedItem().toString());
                timetable.setTitle(titleField.getText());
                timetable.setPdfPath("Download");

                // Update database
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement("UPDATE timetables SET department = ?, title = ?, pdf_path = ? WHERE id = ?")) {
                    
                    stmt.setString(1, timetable.getDepartment());
                    stmt.setString(2, timetable.getTitle());
                    stmt.setString(3, timetable.getPdfPath());
                    stmt.setString(4, id);
                    stmt.executeUpdate();
                    
                    // Update table
                    tableModel.setValueAt(departmentComboBox.getSelectedItem(), selectedRow, 1);
                    tableModel.setValueAt(titleField.getText(), selectedRow, 2);
                    tableModel.setValueAt("Download", selectedRow, 3);
                    
                    JOptionPane.showMessageDialog(this, "Timetable updated successfully!");

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Error updating timetable: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a timetable to update!");
        }
    }

    private void removeTimetable() {
        int selectedRow = timetableTable.getSelectedRow();
        if (selectedRow != -1) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);

            // Remove from database
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM timetables WHERE id = ?")) {
                
                stmt.setString(1, id);
                stmt.executeUpdate();
                
                timetableMap.remove(id);
                timetableList.removeIf(timetable -> timetable.getId().equals(id));
                tableModel.removeRow(selectedRow);
                
                JOptionPane.showMessageDialog(this, "Timetable removed successfully!");

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error removing timetable: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a timetable to remove!");
        }
    }

    private void addSampleData() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM timetables")) {
            
            while (rs.next()) {
                String id = rs.getString("id");
                String department = rs.getString("department");
                String title = rs.getString("title");
                String pdfPath = rs.getString("pdf_path");

                Timetable timetable = new Timetable(id, department, title, pdfPath);
                timetableMap.put(id, timetable);
                timetableList.add(timetable);

                tableModel.addRow(new Object[]{id, department, title, pdfPath});
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

    private void searchTimetable(String query) {
        // Search in the timetable table
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        timetableTable.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter(query));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TimetableManagementFrame::new);
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

class Timetable {
    private String id;
    private String department;
    private String title;
    private String pdfPath;

    public Timetable(String id, String department, String title, String pdfPath) {
        this.id = id;
        this.department = department;
        this.title = title;
        this.pdfPath = pdfPath;
    }

    public String getId() {
        return id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
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
