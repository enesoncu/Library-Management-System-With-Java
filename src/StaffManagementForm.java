import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StaffManagementForm {
    private static final String DB_URL = "jdbc:mysql://localhost/librarydb";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "1234";

    private static Connection connection;
    private static JTextField nameText, contactText;
    private static JTextArea dataTextArea;

    public static void main(String[] args) {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JFrame frame = new JFrame("Staff Management");
        frame.setSize(700, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                DashboardForm.main(new String[]{});
            }
        });

        JPanel panel = new JPanel();
        frame.add(panel);

        placeComponents(panel);

        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel nameLabel = new JLabel("Staff Name:");
        nameLabel.setBounds(10, 20, 80, 25);
        panel.add(nameLabel);

        nameText = new JTextField(20);
        nameText.setBounds(100, 20, 200, 25);
        panel.add(nameText);

        JLabel contactLabel = new JLabel("Contact:");
        contactLabel.setBounds(10, 50, 80, 25);
        panel.add(contactLabel);

        contactText = new JTextField(20);
        contactText.setBounds(100, 50, 200, 25);
        panel.add(contactText);

        JButton saveButton = new JButton("Save");
        saveButton.setBounds(10, 80, 80, 25);
        panel.add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String staffName = nameText.getText();
                String contact = contactText.getText();

                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO staff (staff_name, contact) VALUES (?, ?)");
                    preparedStatement.setString(1, staffName);
                    preparedStatement.setString(2, contact);
                    preparedStatement.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Staff saved successfully");
                    displayData();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error saving staff");
                }
            }
        });

        JButton updateButton = new JButton("Update");
        updateButton.setBounds(100, 80, 80, 25);
        panel.add(updateButton);

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String staffName = nameText.getText();
                String contact = contactText.getText();

                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("UPDATE staff SET contact=? WHERE staff_name=?");
                    preparedStatement.setString(1, contact);
                    preparedStatement.setString(2, staffName);
                    preparedStatement.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Staff updated successfully");
                    displayData();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error updating staff");
                }
            }
        });

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(190, 80, 80, 25);
        panel.add(deleteButton);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String staffName = nameText.getText();

                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM staff WHERE staff_name=?");
                    preparedStatement.setString(1, staffName);
                    preparedStatement.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Staff deleted successfully");
                    displayData();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error deleting staff");
                }
            }
        });

        // Data panel
        JPanel dataPanel = new JPanel();
        dataPanel.setBorder(BorderFactory.createTitledBorder("Staff Data"));
        dataPanel.setBounds(350, 20, 300, 300);
        panel.add(dataPanel);

        dataTextArea = new JTextArea(15, 25);
        JScrollPane scrollPane = new JScrollPane(dataTextArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        dataPanel.add(scrollPane);

        displayData();
    }

    private static void displayData() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM staff");
            ResultSet resultSet = preparedStatement.executeQuery();

            dataTextArea.setText("");
            while (resultSet.next()) {
                String staffName = resultSet.getString("staff_name");
                String contact = resultSet.getString("contact");

                dataTextArea.append("Staff Name: " + staffName + ", Contact: " + contact + "\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error retrieving data");
        }
    }
}
