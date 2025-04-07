package DOAN;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import MySQL.*;

public class EditProfileFrame extends JFrame {
    private JTextField txtFirstName, txtLastName, txtEmail, txtPhone;
    private JComboBox<String> cbGender;
    private JComboBox<Integer> cbDay, cbMonth, cbYear;
    private JButton btnSave, btnBack;
    private JLabel lblOldName, lblOldEmail, lblOldPhone, lblOldGender, lblOldBirthDate;
    private String username;

    public EditProfileFrame(String username) {
        this.username = username;
        setTitle("Edit Customer Profile");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 255));

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        JPanel editPanel = createEditPanel();
        JPanel detailsPanel = createDetailsPanel();

        mainPanel.add(editPanel);
        mainPanel.add(detailsPanel);
        add(mainPanel, BorderLayout.CENTER);
        
        loadProfileData();
        setVisible(true);
    }

    private JPanel createEditPanel() {
        JPanel editPanel = new JPanel(new GridBagLayout());
        editPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLUE, 2, true), "Edit Profile"));
        editPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        JLabel lblFirstName = new JLabel("First Name:");
        txtFirstName = new JTextField(15);
        editPanel.add(lblFirstName, gbc);
        gbc.gridx = 1;
        editPanel.add(txtFirstName, gbc);
        
        gbc.gridx = 0; gbc.gridy++;
        JLabel lblLastName = new JLabel("Last Name:");
        txtLastName = new JTextField(15);
        editPanel.add(lblLastName, gbc);
        gbc.gridx = 1;
        editPanel.add(txtLastName, gbc);
        
        gbc.gridx = 0; gbc.gridy++;
        JLabel lblEmail = new JLabel("Email:");
        txtEmail = new JTextField(15);
        editPanel.add(lblEmail, gbc);
        gbc.gridx = 1;
        editPanel.add(txtEmail, gbc);
        
        gbc.gridx = 0; gbc.gridy++;
        JLabel lblPhone = new JLabel("Phone:");
        txtPhone = new JTextField(15);
        editPanel.add(lblPhone, gbc);
        gbc.gridx = 1;
        editPanel.add(txtPhone, gbc);
        
        gbc.gridx = 0; gbc.gridy++;
        JLabel lblGender = new JLabel("Gender:");
        cbGender = new JComboBox<>(new String[]{"Nam", "Nữ"});
        editPanel.add(lblGender, gbc);
        gbc.gridx = 1;
        editPanel.add(cbGender, gbc);
        
        gbc.gridx = 0; gbc.gridy++;
        JLabel lblBirthDate = new JLabel("Birth Date:");
        JPanel birthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        cbDay = new JComboBox<>();
        cbMonth = new JComboBox<>();
        cbYear = new JComboBox<>();
        for (int i = 1900; i <= 2025; i++) cbYear.addItem(i);
        for (int i = 1; i <= 12; i++) cbMonth.addItem(i);
        updateDays();
        
        cbMonth.addActionListener(e -> updateDays());
        cbYear.addActionListener(e -> updateDays());
        
        birthPanel.add(cbDay);
        birthPanel.add(new JLabel("/"));
        birthPanel.add(cbMonth);
        birthPanel.add(new JLabel("/"));
        birthPanel.add(cbYear);
        
        editPanel.add(lblBirthDate, gbc);
        gbc.gridx = 1;
        editPanel.add(birthPanel, gbc);
        
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        
        btnSave = createStyledButton("Save", new Color(50, 150, 250));
        btnSave.addActionListener(e -> saveProfile());
        btnBack = createStyledButton("Back", new Color(220, 50, 50));
        btnBack.addActionListener(e -> {
            dispose();
            new HomeFrame().setVisible(true); 
        });

        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnSave);
        buttonPanel.add(btnBack);
        
        editPanel.add(buttonPanel, gbc);
        
        return editPanel;
    }
    
    private JPanel createDetailsPanel() {
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLUE, 2, true), "Current Details"));
        detailsPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        detailsPanel.add(new JLabel("Old Name:"), gbc);
        gbc.gridx = 1;
        lblOldName = new JLabel();
        detailsPanel.add(lblOldName, gbc);
        
        gbc.gridx = 0; gbc.gridy++;
        detailsPanel.add(new JLabel("Old Email:"), gbc);
        gbc.gridx = 1;
        lblOldEmail = new JLabel();
        detailsPanel.add(lblOldEmail, gbc);
        
        gbc.gridx = 0; gbc.gridy++;
        detailsPanel.add(new JLabel("Old Phone:"), gbc);
        gbc.gridx = 1;
        lblOldPhone = new JLabel();
        detailsPanel.add(lblOldPhone, gbc);
        
        gbc.gridx = 0; gbc.gridy++;
        detailsPanel.add(new JLabel("Old Gender:"), gbc);
        gbc.gridx = 1;
        lblOldGender = new JLabel();
        detailsPanel.add(lblOldGender, gbc);
        
        gbc.gridx = 0; gbc.gridy++;
        detailsPanel.add(new JLabel("Old Birth Date:"), gbc);
        gbc.gridx = 1;
        lblOldBirthDate = new JLabel();
        detailsPanel.add(lblOldBirthDate, gbc);
        
        return detailsPanel;
    }

    private void updateDays() {
        int selectedDay = cbDay.getSelectedItem() != null ? (int) cbDay.getSelectedItem() : 1;
        int month = cbMonth.getSelectedItem() != null ? (int) cbMonth.getSelectedItem() : 1;
        int year = cbYear.getSelectedItem() != null ? (int) cbYear.getSelectedItem() : 1900;
        
        int daysInMonth;
        switch (month) {
            case 4: case 6: case 9: case 11:
                daysInMonth = 30;
                break;
            case 2:
                daysInMonth = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) ? 29 : 28;
                break;
            default:
                daysInMonth = 31;
        }
        
        cbDay.removeAllItems();
        for (int i = 1; i <= daysInMonth; i++) {
            cbDay.addItem(i);
        }
        if (selectedDay <= daysInMonth) {
            cbDay.setSelectedItem(selectedDay);
        }
    }


    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void loadProfileData() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT ho_ten, email, so_dien_thoai, gioi_tinh, ngay_sinh FROM nguoi_dung WHERE ten_dang_nhap = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                lblOldName.setText(rs.getString("ho_ten"));
                lblOldEmail.setText(rs.getString("email"));
                lblOldPhone.setText(rs.getString("so_dien_thoai"));
                lblOldGender.setText(rs.getString("gioi_tinh"));
                lblOldBirthDate.setText(rs.getString("ngay_sinh"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void saveProfile() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE nguoi_dung SET ho_ten = ?, email = ?, so_dien_thoai = ?, gioi_tinh = ?, ngay_sinh = ? WHERE ten_dang_nhap = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            String fullName = txtFirstName.getText().trim() + " " + txtLastName.getText().trim();
            String birthDate = cbYear.getSelectedItem() + "-" + cbMonth.getSelectedItem() + "-" + cbDay.getSelectedItem();
            
            stmt.setString(1, fullName);
            stmt.setString(2, txtEmail.getText().trim());
            stmt.setString(3, txtPhone.getText().trim());
            stmt.setString(4, cbGender.getSelectedItem().toString());
            stmt.setString(5, birthDate);
            stmt.setString(6, username);
            stmt.executeUpdate();
            
            loadProfileData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getFullName() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT ho_ten FROM nguoi_dung WHERE ten_dang_nhap = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("ho_ten");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
}
