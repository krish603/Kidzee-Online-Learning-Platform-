import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Vector;

public class NoticeManagementFrame extends JFrame {
    private JTextField titleField;
    private JComboBox<String> departmentComboBox;
    private JTextField pdfFilePathField;
    private JTextField searchField;
    private MainFrame mainFrame;
    private DefaultTableModel tableModel;
    private JTable timetableTable;
    private JButton insertButton, updateButton, removeButton, resetButton, browseButton;
    private String[] departments = {"students", "lecturers", "departments"};
    
    public NoticeManagementFrame() {
        setTitle("Notice Management Portal");
        setSize(1025, 640);
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

        JLabel idValueLabel = new JLabel("T0005"); // Placeholder value
        idValueLabel.setBounds(150, 100, 100, 30);
        add(idValueLabel);

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
        // Insert new timetable to the table
        String id = "T000" + (tableModel.getRowCount() + 1); // Generate new ID
        String department = departmentComboBox.getSelectedItem().toString();
        String title = titleField.getText();
        String pdfPath = pdfFilePathField.getText();

        if (!title.isEmpty() && !pdfPath.isEmpty()) {
            Vector<String> row = new Vector<>();
            row.add(id);
            row.add(department);
            row.add(title);
            row.add("Download");
            tableModel.addRow(row);
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all the fields!");
        }
    }

    private void updateTimetable() {
        // Update selected timetable
        int selectedRow = timetableTable.getSelectedRow();
        if (selectedRow != -1) {
            tableModel.setValueAt(departmentComboBox.getSelectedItem(), selectedRow, 1);
            tableModel.setValueAt(titleField.getText(), selectedRow, 2);
            tableModel.setValueAt("Download", selectedRow, 3);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a timetable to update!");
        }
    }

    private void removeTimetable() {
        // Remove selected timetable
        int selectedRow = timetableTable.getSelectedRow();
        if (selectedRow != -1) {
            tableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a timetable to remove!");
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
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

        if (parentFrame != null) {
            parentFrame.dispose(); // Close the parent JFrame
        }

        // Create and show the new frame
        DashboardPanel dFrame = new DashboardPanel(mainFrame);
        dFrame.setVisible(true);
    }


    private void addSampleData() {
        // Sample data to test
        tableModel.addRow(new Object[]{"T0001", "ICT", "TT for ICT Dep 2023 All Batches", "Download"});
        tableModel.addRow(new Object[]{"T0002", "ET", "TT for ET Dep 2023 All Batches", "Download"});
        tableModel.addRow(new Object[]{"T0003", "BST", "TT for BST Dep 2023 All Batches", "Download"});
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
