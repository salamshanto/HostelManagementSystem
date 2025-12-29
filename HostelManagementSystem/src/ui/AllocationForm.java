package ui;

import model.Allocation;
import model.Room;
import model.Student;
import util.FileManager;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AllocationForm extends JFrame {

    private ArrayList<Allocation> allocations;
    private ArrayList<Student> students;
    private ArrayList<Room> rooms;

    private JTable table;
    private DefaultTableModel tableModel;

    private JComboBox<Student> cmbStudent;
    private JComboBox<Room> cmbRoom;
    private JSpinner spStartDate;
    private JSpinner spEndDate;

    private int selectedIndex = -1;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public AllocationForm() {
        setTitle("Allocation Management");
        setSize(800, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        students = FileManager.loadStudents();
        rooms = FileManager.loadRooms();
        allocations = FileManager.loadAllocations();

        initComponents();
        loadStudentsIntoCombo();
        loadAvailableRoomsIntoCombo();
        loadTable();
    }

    private void initComponents() {
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));

        formPanel.add(new JLabel("Student:"));
        cmbStudent = new JComboBox<>();
        formPanel.add(cmbStudent);

        formPanel.add(new JLabel("Room:"));
        cmbRoom = new JComboBox<>();
        formPanel.add(cmbRoom);

        formPanel.add(new JLabel("Start Date:"));
        spStartDate = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
        spStartDate.setEditor(new JSpinner.DateEditor(spStartDate, "dd-MM-yyyy"));
        formPanel.add(spStartDate);

        formPanel.add(new JLabel("End Date:"));
        spEndDate = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
        spEndDate.setEditor(new JSpinner.DateEditor(spEndDate, "dd-MM-yyyy"));
        formPanel.add(spEndDate);

        JPanel buttonPanel = new JPanel();
        JButton btnSave = new JButton("Save");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        tableModel = new DefaultTableModel(
                new Object[]{"Allocation ID", "Student ID", "Room ID", "Start Date", "End Date"}, 0) {
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

        btnSave.addActionListener(e -> saveAllocation());
        btnEdit.addActionListener(e -> editAllocation());
        btnDelete.addActionListener(e -> deleteAllocation());

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loadSelectedAllocation();
            }
        });
    }

    private void loadStudentsIntoCombo() {
        cmbStudent.removeAllItems();
        for (Student s : students) {
            cmbStudent.addItem(s);
        }
    }

    private void loadAvailableRoomsIntoCombo() {
        cmbRoom.removeAllItems();
        for (Room r : rooms) {
            if (r.isAvailable()) {
                cmbRoom.addItem(r);
            }
        }
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        for (Allocation a : allocations) {
            tableModel.addRow(new Object[]{
                    a.getAllocationId(),
                    a.getStudentId(),
                    a.getRoomId(),
                    dateFormat.format(a.getStartDate()),
                    dateFormat.format(a.getEndDate())
            });
        }
    }

    private void clearFields() {
        cmbStudent.setSelectedIndex(students.isEmpty() ? -1 : 0);
        loadAvailableRoomsIntoCombo();
        spStartDate.setValue(new Date());
        spEndDate.setValue(new Date());
        selectedIndex = -1;
        cmbStudent.setEnabled(true);
        cmbRoom.setEnabled(true);
    }

    private int getNextAllocationId() {
        int max = 0;
        for (Allocation a : allocations) {
            if (a.getAllocationId() > max) {
                max = a.getAllocationId();
            }
        }
        return max + 1;
    }

    private void saveAllocation() {
        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students available.");
            return;
        }
        if (cmbRoom.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No available rooms.");
            return;
        }
        Student student = (Student) cmbStudent.getSelectedItem();
        Room room = (Room) cmbRoom.getSelectedItem();
        if (student == null || room == null) {
            JOptionPane.showMessageDialog(this, "Please select student and room.");
            return;
        }

        Date start = (Date) spStartDate.getValue();
        Date end = (Date) spEndDate.getValue();

        int id = getNextAllocationId();
        Allocation allocation = new Allocation(id, student.getStudentId(), room.getRoomId(), start, end);
        allocations.add(allocation);

        room.setAvailable(false);
        FileManager.saveAllocations(allocations);
        FileManager.saveRooms(rooms);

        loadAvailableRoomsIntoCombo();
        loadTable();
        clearFields();
    }

    private void loadSelectedAllocation() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return;
        }
        selectedIndex = row;

        int studentId = Integer.parseInt(tableModel.getValueAt(row, 1).toString());
        int roomId = Integer.parseInt(tableModel.getValueAt(row, 2).toString());

        Student selectedStudent = null;
        for (Student s : students) {
            if (s.getStudentId() == studentId) {
                selectedStudent = s;
                break;
            }
        }

        Room selectedRoom = null;
        for (Room r : rooms) {
            if (r.getRoomId() == roomId) {
                selectedRoom = r;
                break;
            }
        }

        cmbStudent.setSelectedItem(selectedStudent);
        cmbRoom.removeAllItems();
        if (selectedRoom != null) {
            cmbRoom.addItem(selectedRoom);
        }

        try {
            Date start = dateFormat.parse(tableModel.getValueAt(row, 3).toString());
            Date end = dateFormat.parse(tableModel.getValueAt(row, 4).toString());
            spStartDate.setValue(start);
            spEndDate.setValue(end);
        } catch (Exception ex) {
            spStartDate.setValue(new Date());
            spEndDate.setValue(new Date());
        }

        cmbStudent.setEnabled(false);
        cmbRoom.setEnabled(false);
    }

    private void editAllocation() {
        if (selectedIndex < 0) {
            JOptionPane.showMessageDialog(this, "Please select a row to edit.");
            return;
        }
        Allocation allocation = allocations.get(selectedIndex);
        Date start = (Date) spStartDate.getValue();
        Date end = (Date) spEndDate.getValue();
        allocation.setStartDate(start);
        allocation.setEndDate(end);

        FileManager.saveAllocations(allocations);
        loadTable();
    }

    private void deleteAllocation() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
            return;
        }
        Allocation allocation = allocations.get(row);

        for (Room r : rooms) {
            if (r.getRoomId() == allocation.getRoomId()) {
                r.setAvailable(true);
                break;
            }
        }

        allocations.remove(row);
        FileManager.saveAllocations(allocations);
        FileManager.saveRooms(rooms);

        loadAvailableRoomsIntoCombo();
        loadTable();
        clearFields();
    }
}


