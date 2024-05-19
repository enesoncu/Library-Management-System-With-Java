import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardForm {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Dashboard");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);

        placeComponents(panel);

        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JButton bookManagementButton = new JButton("Book Management");
        bookManagementButton.setBounds(50, 30, 200, 30);
        panel.add(bookManagementButton);

        JButton staffManagementButton = new JButton("Staff Management");
        staffManagementButton.setBounds(50, 80, 200, 30);
        panel.add(staffManagementButton);

        bookManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BookManagementForm.main(new String[]{});
            }
        });

        staffManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StaffManagementForm.main(new String[]{});
            }
        });
    }
}
