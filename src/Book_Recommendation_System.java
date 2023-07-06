import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

public class Book_Recommendation_System extends JFrame implements ActionListener 
{
    private JRadioButton adminRadioButton, hobbieRadioButton, careerRadioButton;
    private JPanel radioPanel, insertPanel, adminPanel,viewPanel;
    private JTable bookTable,careerTable,hobbieTable;

    public Book_Recommendation_System() 
    {
        setTitle("Book Recommendation App");
        setSize(700, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainPage();
        setVisible(true);
    }

    private void mainPage() 
    {
        adminRadioButton = new JRadioButton("Admin");
        hobbieRadioButton = new JRadioButton("Hobbies");
        careerRadioButton = new JRadioButton("Career");

        ButtonGroup radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(adminRadioButton);
        radioButtonGroup.add(hobbieRadioButton);
        radioButtonGroup.add(careerRadioButton);

        radioPanel = new JPanel();
        radioPanel.setLayout(new FlowLayout());
        radioPanel.add(adminRadioButton);
        radioPanel.add(hobbieRadioButton);
        radioPanel.add(careerRadioButton);

        add(radioPanel, BorderLayout.NORTH);

        adminRadioButton.addActionListener(this);
        hobbieRadioButton.addActionListener(this);
        careerRadioButton.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) 
    {
        if (e.getSource() == adminRadioButton) 
        {
            final JMenuBar adminMenuBar = new JMenuBar();
            final JMenu adminMenu = new JMenu("Admin");
            final JMenu booksMenu = new JMenu("Books");
            final JMenu careersMenu = new JMenu("Careers");
            final JMenu hobbiesMenu = new JMenu("Hobbies");
            final JMenuItem exitItem = new JMenuItem("Exit");
            
            booksMenu.add(new JMenuItem("View Books")).addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent e) 
                {
                    showViewBookPanel();
                }
            });
            
            booksMenu.add(new JMenuItem("Add Book")).addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent e) 
                {
                    showInsertBookPanel();
                }
            });

            booksMenu.add(new JMenuItem("Edit Book")).addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent e) 
                {
                    showEditBookPanel();
                }
            });

            booksMenu.add(new JMenuItem("Delete Book")).addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent e) 
                {
                    showDeleteBookPanel();
                }
            });
            /////////////////
            careersMenu.add(new JMenuItem("View Careers")).addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent e) 
                {
                    showViewCareerPanel();
                }
            });
            
            careersMenu.add(new JMenuItem("Add Career")).addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent e) 
                {
                    showAddCareerPanel();
                }
            });
            
            careersMenu.add(new JMenuItem("Edit Career")).addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent e) 
                {
                    showEditCareerPanel();
                }
            });
            
            careersMenu.add(new JMenuItem("Delete Career")).addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent e) 
                {
                    showDeleteCareerPanel();
                }
            });
            ////////////////
            hobbiesMenu.add(new JMenuItem("View Hobbies")).addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent e) 
                {
                    showViewHobbiePanel();
                }
            });
            
            hobbiesMenu.add(new JMenuItem("Add Hobbies")).addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent e) 
                {
                    showAddHobbiePanel();
                }
            });
            
            hobbiesMenu.add(new JMenuItem("Edit Hobbies")).addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent e) 
                {
                    showEditHobbiePanel();
                }
            });
            
            hobbiesMenu.add(new JMenuItem("Delete Hobbies")).addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent e) 
                {
                    showDeleteHobbiePanel();
                }
            });

           exitItem.addActionListener(new ActionListener() 
           {
                public void actionPerformed(ActionEvent e) 
                {
                      System.exit(0);
                }
            });
            remove(radioPanel);
            revalidate();
            repaint();

            adminMenu.add(booksMenu);
            adminMenu.add(careersMenu);
            adminMenu.add(hobbiesMenu);
            adminMenu.add(exitItem);
            adminMenuBar.add(adminMenu);
            setJMenuBar(adminMenuBar);

            getContentPane().remove(radioPanel);
            revalidate();
            repaint();
        } 
        
        else if (e.getSource() == hobbieRadioButton) 
        {
            String[] hobbies = getHobbies();
            String selectedHobby = (String) JOptionPane.showInputDialog(
                    this,
                    "Select a hobby:",
                    "Hobbies",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    hobbies,
                    hobbies[0]);
            if (selectedHobby != null) 
            {
                String[] bookTitles = getBookTitlesForHobby(selectedHobby);
                StringBuilder message = new StringBuilder();
                for (String bookTitle : bookTitles) {
                    message.append(bookTitle).append("\n");
                }
                JOptionPane.showMessageDialog(
                        this,
                        "Book Titles for Hobby '" + selectedHobby + "':\n" + message.toString(),
                        "Books",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            
        }
        else if (e.getSource() == careerRadioButton) {
            String[] careers = getCareers();
            String selectedCareer = (String) JOptionPane.showInputDialog(
                    this,
                    "Select a career:",
                    "Careers",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    careers,
                    careers[0]);
            if (selectedCareer != null) 
            {
                String[] bookTitles = getBookTitlesForCareer(selectedCareer);
                StringBuilder message = new StringBuilder();
                for (String bookTitle : bookTitles) {
                    message.append(bookTitle).append("\n");
                }
                JOptionPane.showMessageDialog(
                        this,
                        "Book Titles for Career '" + selectedCareer + "':\n" + message.toString(),
                        "Books",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            else {}}
            
        }
    
        private String[] getBookTitlesForHobby(String hobby) {
            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet resultSet = null;
            ArrayList<String> bookTitles = new ArrayList<>();

            try {
                connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "vineel", "vennela");
                String sql = "select b.book_title from Books b,Books_Hobbies bh,Hobbies h where h.hobbie_id = bh.hobby_id and bh.book_id = b.book_id and h.hobbie_name = ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, hobby);
                resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String bookTitle = resultSet.getString("book_title");
                    bookTitles.add(bookTitle);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (resultSet != null) resultSet.close();
                    if (statement != null) statement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return bookTitles.toArray(new String[0]);
        }

    private String[] getHobbies() {
        ArrayList<String> hobbies = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "vineel", "vennela")) {
        	java.sql.Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT hobbie_name FROM Hobbies");
            while (resultSet.next()) {
                String hobbyName = resultSet.getString("hobbie_name");
                hobbies.add(hobbyName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hobbies.toArray(new String[0]);
    }
    
    //////////////
    private String[] getBookTitlesForCareer(String career) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ArrayList<String> bookTitles = new ArrayList<>();

        try {
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "vineel", "vennela");
            String sql = "select b.book_title from Books b,Books_Careers bc,Careers c where c.career_id = bc.career_id and bc.book_id = b.book_id and c.career_name = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, career);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String bookTitle = resultSet.getString("book_title");
                bookTitles.add(bookTitle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return bookTitles.toArray(new String[0]);
    }

private String[] getCareers() {
    ArrayList<String> careers = new ArrayList<>();
    try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "vineel", "vennela")) {
        // Create a statement
    	java.sql.Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT career_name FROM Careers");
        while (resultSet.next()) {
            String careerName = resultSet.getString("career_name");
            careers.add(careerName);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return careers.toArray(new String[0]);
}
    
    
    private void showViewBookPanel() {
        removePreviousPanel();

        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "vineel", "vennela")) {
            String selectQuery = "SELECT * FROM Books";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            java.sql.ResultSet resultSet = preparedStatement.executeQuery();

            DefaultTableModel model = new DefaultTableModel(){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            model.addColumn("Book ID");
            model.addColumn("Book Title");
            model.addColumn("Book Author");
            model.addColumn("Genre");

            while (resultSet.next()) {
                String bookId = resultSet.getString("book_id");
                String bookTitle = resultSet.getString("book_title");
                String bookAuthor = resultSet.getString("book_author");
                String genre = resultSet.getString("book_genre");
                model.addRow(new Object[]{bookId, bookTitle, bookAuthor, genre});
            }

            bookTable = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(bookTable);
            

            viewPanel = new JPanel(new BorderLayout());
            viewPanel.add(scrollPane, BorderLayout.CENTER);

            getContentPane().add(viewPanel, BorderLayout.CENTER);
            revalidate();
            repaint();

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the database", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showInsertBookPanel() {
        removePreviousPanel();

        JLabel bookidLabel = new JLabel("Book ID:");
        JTextField bookidTextField = new JTextField(10);
        JLabel bookTitleLabel = new JLabel("Book Title:");
        JTextField bookTitleTextField = new JTextField(10);
        JLabel bookAuthorLabel = new JLabel("Book Author:");
        JTextField bookAuthorTextField = new JTextField(10);
        JLabel genreLabel = new JLabel("Genre:");
        JTextField genreTextField = new JTextField(10);
        JButton addButton = new JButton("Add");

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String bookId = bookidTextField.getText();
                String bookTitle = bookTitleTextField.getText();
                String bookAuthor = bookAuthorTextField.getText();
                String genre = genreTextField.getText();
                if (bookId.isEmpty() || bookTitle.isEmpty() || bookAuthor.isEmpty() || genre.isEmpty()) {
                    JOptionPane.showMessageDialog(insertPanel, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "vineel", "vennela")) {
                    String insertQuery = "INSERT INTO Books (book_id, book_title, book_author, book_genre) VALUES (?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                    preparedStatement.setString(1, bookId);
                    preparedStatement.setString(2, bookTitle);
                    preparedStatement.setString(3, bookAuthor);
                    preparedStatement.setString(4, genre);

                    int count = preparedStatement.executeUpdate();
                    if (count > 0) {
                        JOptionPane.showMessageDialog(insertPanel, "Book added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(insertPanel, "Failed to add Book", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    // Clear input fields
                    bookidTextField.setText("");
                    bookTitleTextField.setText("");
                    bookAuthorTextField.setText("");
                    genreTextField.setText("");

                    preparedStatement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(insertPanel, "Failed to connect to the database", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        insertPanel = new JPanel();
        insertPanel.setLayout(new GridLayout(5, 2));
        insertPanel.add(bookidLabel);
        insertPanel.add(bookidTextField);
        insertPanel.add(bookTitleLabel);
        insertPanel.add(bookTitleTextField);
        insertPanel.add(bookAuthorLabel);
        insertPanel.add(bookAuthorTextField);
        insertPanel.add(genreLabel);
        insertPanel.add(genreTextField);
        insertPanel.add(addButton);

        getContentPane().add(insertPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void showEditBookPanel() {
        removePreviousPanel();

        JLabel bookidLabel = new JLabel("Book ID:");
        JTextField bookidTextField = new JTextField(10);
        JLabel bookTitleLabel = new JLabel("Book Title:");
        JTextField bookTitleTextField = new JTextField(10);
        JLabel bookAuthorLabel = new JLabel("Book Author:");
        JTextField bookAuthorTextField = new JTextField(10);
        JLabel genreLabel = new JLabel("Genre:");
        JTextField genreTextField = new JTextField(10);
        JButton addButton = new JButton("Update");

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String bookId = bookidTextField.getText();
                String bookTitle = bookTitleTextField.getText();
                String bookAuthor = bookAuthorTextField.getText();
                String genre = genreTextField.getText();
                if (bookId.isEmpty() || bookTitle.isEmpty() || bookAuthor.isEmpty() || genre.isEmpty()) {
                    JOptionPane.showMessageDialog(insertPanel, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "vineel", "vennela")) {
                    String insertQuery = "UPDATE Books SET book_title = ?, book_author = ?, book_genre = ? WHERE book_id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                    preparedStatement.setString(4, bookId);
                    preparedStatement.setString(1, bookTitle);
                    preparedStatement.setString(2, bookAuthor);
                    preparedStatement.setString(3, genre);

                    int count = preparedStatement.executeUpdate();
                    if (count > 0) {
                        JOptionPane.showMessageDialog(insertPanel, "Book Updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(insertPanel, "Failed to Update Book", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    // Clear input fields
                    bookidTextField.setText("");
                    bookTitleTextField.setText("");
                    bookAuthorTextField.setText("");
                    genreTextField.setText("");

                    preparedStatement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(insertPanel, "Failed to connect to the database", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        insertPanel = new JPanel();
        insertPanel.setLayout(new GridLayout(5, 2));
        insertPanel.add(bookidLabel);
        insertPanel.add(bookidTextField);
        insertPanel.add(bookTitleLabel);
        insertPanel.add(bookTitleTextField);
        insertPanel.add(bookAuthorLabel);
        insertPanel.add(bookAuthorTextField);
        insertPanel.add(genreLabel);
        insertPanel.add(genreTextField);
        insertPanel.add(addButton);

        getContentPane().add(insertPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    private void showDeleteBookPanel() {
        removePreviousPanel();

        JLabel bookIdLabel = new JLabel("Book ID:");
        JTextField bookIdTextField = new JTextField(10);
        JButton deleteButton = new JButton("Delete");

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String bookId = bookIdTextField.getText();
                if (bookId.isEmpty()) {
                    JOptionPane.showMessageDialog(insertPanel, "Please enter Book ID", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "vineel", "vennela")) {
                    String deleteQuery = "DELETE FROM Books WHERE book_id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
                    preparedStatement.setString(1, bookId);

                    int count = preparedStatement.executeUpdate();
                    if (count > 0) {
                        JOptionPane.showMessageDialog(insertPanel, "Book deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(insertPanel, "Failed to delete Book", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    bookIdTextField.setText("");

                    preparedStatement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(insertPanel, "Failed to connect to the database", "Error", JOptionPane.ERROR_MESSAGE);
                }

                bookIdTextField.setText("");
            }
        });

        insertPanel = new JPanel();
        insertPanel.setLayout(new GridLayout(2, 2));
        insertPanel.add(bookIdLabel);
        insertPanel.add(bookIdTextField);
        insertPanel.add(deleteButton);

        getContentPane().add(insertPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    /////////////////////////////////
    private void showViewCareerPanel() {
        removePreviousPanel();

        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "vineel", "vennela")) {
            String selectQuery = "SELECT * FROM Careers";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            java.sql.ResultSet resultSet = preparedStatement.executeQuery();

            DefaultTableModel model = new DefaultTableModel()
            {
                public boolean isCellEditable(int row, int column) 
                {
                    return false;
                }
            };
            model.addColumn("CAREER ID");
            model.addColumn("CAREER NAME");

            while (resultSet.next()) {
                String careerId = resultSet.getString("career_id");
                String careerName = resultSet.getString("career_name");
                model.addRow(new Object[]{careerId, careerName});
            }

            careerTable = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(careerTable);
            

            viewPanel = new JPanel(new BorderLayout());
            viewPanel.add(scrollPane, BorderLayout.CENTER);

            getContentPane().add(viewPanel, BorderLayout.CENTER);
            revalidate();
            repaint();

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the database", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAddCareerPanel() {
        removePreviousPanel();

        JLabel careeridLabel = new JLabel("Career ID:");
        JTextField careeridTextField = new JTextField(10);
        JLabel careerNameLabel = new JLabel("Career Name:");
        JTextField careerNameTextField = new JTextField(10);
        JButton addButton = new JButton("Add");

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String careerId = careeridTextField.getText();
                String careerName = careerNameTextField.getText();
                if (careerId.isEmpty() || careerName.isEmpty()) {
                    JOptionPane.showMessageDialog(insertPanel, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "vineel", "vennela")) {
                    String insertQuery = "INSERT INTO Careers (career_id, career_name) VALUES (?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                    preparedStatement.setString(1, careerId);
                    preparedStatement.setString(2, careerName);

                    int count = preparedStatement.executeUpdate();
                    if (count > 0) {
                        JOptionPane.showMessageDialog(insertPanel, "Career added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(insertPanel, "Failed to add Career", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    careeridTextField.setText("");
                    careerNameTextField.setText("");

                    preparedStatement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(insertPanel, "Failed to connect to the database", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        insertPanel = new JPanel();
        insertPanel.setLayout(new GridLayout(3, 2));
        insertPanel.add(careeridLabel);
        insertPanel.add(careeridTextField);
        insertPanel.add(careerNameLabel);
        insertPanel.add(careerNameTextField);
        insertPanel.add(addButton);

        getContentPane().add(insertPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void showEditCareerPanel() {
        removePreviousPanel();
        JLabel careeridLabel = new JLabel("Career ID:");
        JTextField careeridTextField = new JTextField(10);
        JLabel careerNameLabel = new JLabel("Career Name:");
        JTextField careerNameTextField = new JTextField(10);
        JButton addButton = new JButton("Update");

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String careerId = careeridTextField.getText();
                String careerName = careerNameTextField.getText();
                if (careerId.isEmpty() || careerName.isEmpty()) {
                    JOptionPane.showMessageDialog(insertPanel, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "vineel", "vennela")) {
                    String insertQuery = "UPDATE Careers SET career_name = ? WHERE career_id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                    preparedStatement.setString(2, careerId);
                    preparedStatement.setString(1, careerName);

                    int count = preparedStatement.executeUpdate();
                    if (count > 0) {
                        JOptionPane.showMessageDialog(insertPanel, "Career updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(insertPanel, "Failed to update Career", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    careeridTextField.setText("");
                    careerNameTextField.setText("");

                    preparedStatement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(insertPanel, "Failed to connect to the database", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        insertPanel = new JPanel();
        insertPanel.setLayout(new GridLayout(3, 2));
        insertPanel.add(careeridLabel);
        insertPanel.add(careeridTextField);
        insertPanel.add(careerNameLabel);
        insertPanel.add(careerNameTextField);
        insertPanel.add(addButton);

        getContentPane().add(insertPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    
    private void showDeleteCareerPanel() {
        removePreviousPanel();

        JLabel careerIdLabel = new JLabel("Career ID:");
        JTextField careerIdTextField = new JTextField(10);
        JButton deleteButton = new JButton("Delete");

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String bookId = careerIdTextField.getText();
                if (bookId.isEmpty()) {
                    JOptionPane.showMessageDialog(insertPanel, "Please enter Career ID", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "vineel", "vennela")) {
                    String deleteQuery = "DELETE FROM Careers WHERE career_id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
                    preparedStatement.setString(1, bookId);

                    int count = preparedStatement.executeUpdate();
                    if (count > 0) {
                        JOptionPane.showMessageDialog(insertPanel, "Career deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(insertPanel, "Failed to delete Book", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    careerIdTextField.setText("");

                    preparedStatement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(insertPanel, "Failed to connect to the database", "Error", JOptionPane.ERROR_MESSAGE);
                }

                // Clear input field
                careerIdTextField.setText("");
            }
        });

        insertPanel = new JPanel();
        insertPanel.setLayout(new GridLayout(2, 2));
        insertPanel.add(careerIdLabel);
        insertPanel.add(careerIdTextField);
        insertPanel.add(deleteButton);

        getContentPane().add(insertPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    /////////////////////////////
    private void showViewHobbiePanel() {
        removePreviousPanel();

        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "vineel", "vennela")) {
            String selectQuery = "SELECT * FROM Hobbies";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            java.sql.ResultSet resultSet = preparedStatement.executeQuery();

            DefaultTableModel model = new DefaultTableModel()
            {
                public boolean isCellEditable(int row, int column) 
                {
                    return false;
                }
            };
            model.addColumn("HOBBIE ID");
            model.addColumn("HOBBIE NAME");

            while (resultSet.next()) {
                String hobbieId = resultSet.getString("hobbie_id");
                String hobbieName = resultSet.getString("hobbie_name");
                model.addRow(new Object[]{hobbieId, hobbieName});
            }

            hobbieTable = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(hobbieTable);
            

            viewPanel = new JPanel(new BorderLayout());
            viewPanel.add(scrollPane, BorderLayout.CENTER);

            getContentPane().add(viewPanel, BorderLayout.CENTER);
            revalidate();
            repaint();

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the database", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAddHobbiePanel() {
        removePreviousPanel();

        JLabel hobbieidLabel = new JLabel("Hobbie ID:");
        JTextField hobbieidTextField = new JTextField(10);
        JLabel hobbieNameLabel = new JLabel("Hobbie Name:");
        JTextField hobbieNameTextField = new JTextField(10);
        JButton addButton = new JButton("Add");

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String hobbieId = hobbieidTextField.getText();
                String hobbieName = hobbieNameTextField.getText();
                if (hobbieId.isEmpty() || hobbieName.isEmpty()) {
                    JOptionPane.showMessageDialog(insertPanel, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "vineel", "vennela")) {
                    String insertQuery = "INSERT INTO Hobbies (hobbie_id, hobbie_name) VALUES (?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                    preparedStatement.setString(1, hobbieId);
                    preparedStatement.setString(2, hobbieName);

                    int count = preparedStatement.executeUpdate();
                    if (count > 0) {
                        JOptionPane.showMessageDialog(insertPanel, "Hobbie added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(insertPanel, "Failed to add Hobbie", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    hobbieidTextField.setText("");
                    hobbieNameTextField.setText("");

                    preparedStatement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(insertPanel, "Failed to connect to the database", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        insertPanel = new JPanel();
        insertPanel.setLayout(new GridLayout(3, 2));
        insertPanel.add(hobbieidLabel);
        insertPanel.add(hobbieidTextField);
        insertPanel.add(hobbieNameLabel);
        insertPanel.add(hobbieNameTextField);
        insertPanel.add(addButton);

        getContentPane().add(insertPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void showEditHobbiePanel() {
        removePreviousPanel();
        JLabel hobbieidLabel = new JLabel("Hobbie ID:");
        JTextField hobbieidTextField = new JTextField(10);
        JLabel hobbieNameLabel = new JLabel("Hobbie Name:");
        JTextField hobbieNameTextField = new JTextField(10);
        JButton addButton = new JButton("Update");

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String hobbieId = hobbieidTextField.getText();
                String hobbieName = hobbieNameTextField.getText();
                if (hobbieId.isEmpty() || hobbieName.isEmpty()) {
                    JOptionPane.showMessageDialog(insertPanel, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "vineel", "vennela")) {
                    String insertQuery = "UPDATE Hobbies SET hobbie_name = ? WHERE hobbie_id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                    preparedStatement.setString(2, hobbieId);
                    preparedStatement.setString(1, hobbieName);

                    int count = preparedStatement.executeUpdate();
                    if (count > 0) {
                        JOptionPane.showMessageDialog(insertPanel, "Hobbie updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(insertPanel, "Failed to update Hobbie", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    hobbieidTextField.setText("");
                    hobbieNameTextField.setText("");

                    preparedStatement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(insertPanel, "Failed to connect to the database", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            }
        });

        insertPanel = new JPanel();
        insertPanel.setLayout(new GridLayout(3, 2));
        insertPanel.add(hobbieidLabel);
        insertPanel.add(hobbieidTextField);
        insertPanel.add(hobbieNameLabel);
        insertPanel.add(hobbieNameTextField);
        insertPanel.add(addButton);

        getContentPane().add(insertPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    
    private void showDeleteHobbiePanel() {
        removePreviousPanel();

        JLabel hobbieIdLabel = new JLabel("Hobbie ID:");
        JTextField hobbieIdTextField = new JTextField(10);
        JButton deleteButton = new JButton("Delete");

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String hobbieId = hobbieIdTextField.getText();
                if (hobbieId.isEmpty()) {
                    JOptionPane.showMessageDialog(insertPanel, "Please enter Hobbie ID", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "vineel", "vennela")) {
                    String deleteQuery = "DELETE FROM Hobbies WHERE hobbie_id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
                    preparedStatement.setString(1, hobbieId);

                    int count = preparedStatement.executeUpdate();
                    if (count > 0) {
                        JOptionPane.showMessageDialog(insertPanel, "Hobbie deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(insertPanel, "Failed to delete Hobbie", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    hobbieIdTextField.setText("");

                    preparedStatement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(insertPanel, "Failed to connect to the database", "Error", JOptionPane.ERROR_MESSAGE);
                }
                hobbieIdTextField.setText("");
            }
        });

        insertPanel = new JPanel();
        insertPanel.setLayout(new GridLayout(2, 2));
        insertPanel.add(hobbieIdLabel);
        insertPanel.add(hobbieIdTextField);
        insertPanel.add(deleteButton);

        getContentPane().add(insertPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
        
    }

    private void removePreviousPanel() {
        if (insertPanel != null) {
            getContentPane().remove(insertPanel);
        }
        if (adminPanel != null) {
            getContentPane().remove(adminPanel);
        }
        if (viewPanel != null) {
            getContentPane().remove(viewPanel);
        }
    }

    public static void main(String[] args) {
        new Book_Recommendation_System();
    }
}
