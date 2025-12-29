import ui.AllocationForm;
import ui.RoomForm;
import ui.StudentForm;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.GridLayout;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowMainMenu);
    }

    private static void createAndShowMainMenu() {
        JFrame frame = new JFrame("Hostel Management System");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));

        JButton btnStudents = new JButton("Student Management");
        JButton btnRooms = new JButton("Room Management");
        JButton btnAllocations = new JButton("Allocation Management");

        panel.add(btnStudents);
        panel.add(btnRooms);
        panel.add(btnAllocations);

        frame.add(panel);

        btnStudents.addActionListener(e -> new StudentForm().setVisible(true));
        btnRooms.addActionListener(e -> new RoomForm().setVisible(true));
        btnAllocations.addActionListener(e -> new AllocationForm().setVisible(true));

        frame.setVisible(true);
    }
}


