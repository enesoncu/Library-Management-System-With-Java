import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BookManagementForm {
    private static final String DB_URL = "jdbc:mysql://localhost/librarydb";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "1234";

    private static Connection connection;
    private static JTextField nameText, categoryText, authorText, unitNumberText;
    private static JTextArea dataTextArea;

    public static void main(String[] args) {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JFrame frame = new JFrame("Book Management");
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

        JLabel nameLabel = new JLabel("Book Name:");
        nameLabel.setBounds(10, 20, 80, 25);
        panel.add(nameLabel);

        nameText = new JTextField(20);
        nameText.setBounds(100, 20, 200, 25);
        panel.add(nameText);

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setBounds(10, 50, 80, 25);
        panel.add(categoryLabel);

        categoryText = new JTextField(20);
        categoryText.setBounds(100, 50, 200, 25);
        panel.add(categoryText);

        JLabel authorLabel = new JLabel("Author:");
        authorLabel.setBounds(10, 80, 80, 25);
        panel.add(authorLabel);

        authorText = new JTextField(20);
        authorText.setBounds(100, 80, 200, 25);
        panel.add(authorText);

        JLabel unitNumberLabel = new JLabel("Unit Number:");
        unitNumberLabel.setBounds(10, 110, 80, 25);
        panel.add(unitNumberLabel);

        unitNumberText = new JTextField(20);
        unitNumberText.setBounds(100, 110, 200, 25);
        panel.add(unitNumberText);

        JButton saveButton = new JButton("Save");
        saveButton.setBounds(10, 140, 80, 25);
        panel.add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bookName = nameText.getText();
                String category = categoryText.getText();
                String author = authorText.getText();
                int unitNumber = Integer.parseInt(unitNumberText.getText());

                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO books (book_name, category, author, unit_number) VALUES (?, ?, ?, ?)");
                    preparedStatement.setString(1, bookName);
                    preparedStatement.setString(2, category);
                    preparedStatement.setString(3, author);
                    preparedStatement.setInt(4, unitNumber);
                    preparedStatement.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Book saved successfully");
                    displayData();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error saving book");
                }
            }
        });

        JButton updateButton = new JButton("Update");
        updateButton.setBounds(100, 140, 80, 25);
        panel.add(updateButton);

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bookName = nameText.getText();
                String category = categoryText.getText();
                String author = authorText.getText();
                int unitNumber = Integer.parseInt(unitNumberText.getText());

                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("UPDATE books SET category=?, author=?, unit_number=? WHERE book_name=?");
                    preparedStatement.setString(1, category);
                    preparedStatement.setString(2, author);
                    preparedStatement.setInt(3, unitNumber);
                    preparedStatement.setString(4, bookName);
                    preparedStatement.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Book updated successfully");
                    displayData();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error updating book");
                }
            }
        });

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(190, 140, 80, 25);
        panel.add(deleteButton);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bookName = nameText.getText();

                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM books WHERE book_name=?");
                    preparedStatement.setString(1, bookName);
                    preparedStatement.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Book deleted successfully");
                    displayData();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error deleting book");
                }
            }
        });

        // Data panel
        JPanel dataPanel = new JPanel();
        dataPanel.setBorder(BorderFactory.createTitledBorder("Book Data"));
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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM books");
            ResultSet resultSet = preparedStatement.executeQuery();

            dataTextArea.setText("");
            while (resultSet.next()) {
                String bookName = resultSet.getString("book_name");
                String category = resultSet.getString("category");
                String author = resultSet.getString("author");
                int unitNumber = resultSet.getInt("unit_number");

                dataTextArea.append("Book Name: " + bookName + ", Category: " + category + ", Author: " + author + ", Unit Number: " + unitNumber + "\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error retrieving data");
        }
    }
}
