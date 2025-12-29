package ui;

import model.Room;
import model.RoomType;
import util.FileManager;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class RoomForm extends JFrame {

    private ArrayList<Room> rooms;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtId;
    private javax.swing.JComboBox<RoomType> cmbRoomType;
    private JTextField txtMonthlyFee;
    private JCheckBox chkCafeteria;
    private JCheckBox chkAvailable;

    public RoomForm() {
        setTitle("Room Management");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        rooms = FileManager.loadRooms();

        initComponents();
        loadTable();
    }

    private void initComponents() {
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));

        formPanel.add(new JLabel("Room ID:"));
        txtId = new JTextField();
        formPanel.add(txtId);

        formPanel.add(new JLabel("Room Type:"));
        cmbRoomType = new javax.swing.JComboBox<>(RoomType.values());
        formPanel.add(cmbRoomType);

        formPanel.add(new JLabel("Monthly Fee:"));
        txtMonthlyFee = new JTextField();
        formPanel.add(txtMonthlyFee);

        formPanel.add(new JLabel("Cafeteria Included:"));
        chkCafeteria = new JCheckBox();
        formPanel.add(chkCafeteria);

        formPanel.add(new JLabel("Available:"));
        chkAvailable = new JCheckBox();
        formPanel.add(chkAvailable);

        JPanel buttonPanel = new JPanel();
        JButton btnSave = new JButton("Save");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Type", "Monthly Fee", "Cafeteria", "Available"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        setLayout(new BorderLayout(5, 5));
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> saveRoom());
        btnEdit.addActionListener(e -> editRoom());
        btnDelete.addActionListener(e -> deleteRoom());

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loadSelectedRoom();
            }
        });
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        for (Room r : rooms) {
            tableModel.addRow(new Object[]{
                    r.getRoomId(),
                    r.getRoomType(),
                    r.getMonthlyFee(),
                    r.isCafeteriaIncluded(),
                    r.isAvailable()
            });
        }
    }

    private void clearFields() {
        txtId.setText("");
        txtMonthlyFee.setText("");
        cmbRoomType.setSelectedIndex(0);
        chkCafeteria.setSelected(false);
        chkAvailable.setSelected(true);
    }

    private void saveRoom() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            double fee = Double.parseDouble(txtMonthlyFee.getText().trim());
            RoomType type = (RoomType) cmbRoomType.getSelectedItem();
            boolean cafeteria = chkCafeteria.isSelected();
            boolean available = chkAvailable.isSelected();

            Room room = new Room(id, type, fee, cafeteria, available);
            rooms.add(room);
            FileManager.saveRooms(rooms);
            loadTable();
            clearFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid numeric value.");
        }
    }

    private void loadSelectedRoom() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            txtId.setText(tableModel.getValueAt(row, 0).toString());
            cmbRoomType.setSelectedItem(tableModel.getValueAt(row, 1));
            txtMonthlyFee.setText(tableModel.getValueAt(row, 2).toString());
            chkCafeteria.setSelected(Boolean.parseBoolean(tableModel.getValueAt(row, 3).toString()));
            chkAvailable.setSelected(Boolean.parseBoolean(tableModel.getValueAt(row, 4).toString()));
        }
    }

    private void editRoom() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a row to edit.");
            return;
        }
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            double fee = Double.parseDouble(txtMonthlyFee.getText().trim());
            RoomType type = (RoomType) cmbRoomType.getSelectedItem();
            boolean cafeteria = chkCafeteria.isSelected();
            boolean available = chkAvailable.isSelected();

            Room room = rooms.get(row);
            room.setRoomId(id);
            room.setRoomType(type);
            room.setMonthlyFee(fee);
            room.setCafeteriaIncluded(cafeteria);
            room.setAvailable(available);

            FileManager.saveRooms(rooms);
            loadTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid numeric value.");
        }
    }

    private void deleteRoom() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
            return;
        }
        rooms.remove(row);
        FileManager.saveRooms(rooms);
        loadTable();
        clearFields();
    }
}


