package DOAN;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegisterFrame extends JFrame {
    private JTextField txtUsername, txtFullName, txtEmail, txtPhone, txtBirthDate;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JComboBox<String> genderComboBox;
    private JButton btnRegister, btnBack;

    public RegisterFrame() {
        setTitle("Đăng Ký Người Dùng");
        setSize(500, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Tiêu đề + Avatar trong khung tròn
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.LIGHT_GRAY);
                g.fillOval(10, 10, getWidth() - 20, getHeight() - 20);
            }
        };
        titlePanel.setPreferredSize(new Dimension(150, 150));
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Đăng Ký Tài Khoản", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(41, 128, 185));
        mainPanel.add(lblTitle, gbc);

        // Ô nhập liệu
        gbc.gridwidth = 1;
        gbc.gridy++;
        txtUsername = createLabeledInputField("Tên đăng nhập:", mainPanel, gbc);
        gbc.gridy++;
        txtPassword = createLabeledPasswordField("Mật khẩu:", mainPanel, gbc);
        gbc.gridy++;
        txtConfirmPassword = createLabeledPasswordField("Xác nhận mật khẩu:", mainPanel, gbc);
        gbc.gridy++;
        txtFullName = createLabeledInputField("Họ và tên:", mainPanel, gbc);
        gbc.gridy++;
        txtEmail = createLabeledInputField("Email:", mainPanel, gbc);
        gbc.gridy++;
        txtPhone = createLabeledInputField("Số điện thoại:", mainPanel, gbc);
        gbc.gridy++;
        genderComboBox = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
        JPanel genderPanel = createLabeledComboBox("Giới tính:", genderComboBox);
        mainPanel.add(genderPanel, gbc);
        gbc.gridy++;
        txtBirthDate = createLabeledInputField("Ngày sinh (YYYY-MM-DD):", mainPanel, gbc);

        // Nút Đăng Ký
        gbc.gridy++;
        btnRegister = createButton("Đăng Ký", new Color(41, 128, 185));
        mainPanel.add(btnRegister, gbc);

        // Nút Quay Lại
        gbc.gridy++;
        btnBack = createButton("Quay lại", new Color(192, 57, 43));
        mainPanel.add(btnBack, gbc);

        // Xử lý sự kiện
        btnRegister.addActionListener(e -> registerUser());
        btnBack.addActionListener(e -> {
            dispose();
            new HomeFrame();
        });

        add(mainPanel);
        setVisible(true);
    }

    private JTextField createLabeledInputField(String labelText, JPanel panel, GridBagConstraints gbc) {
        JPanel fieldPanel = new JPanel(new BorderLayout(10, 10));
        fieldPanel.setBackground(Color.WHITE);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField textField = new JTextField(20);

        fieldPanel.add(label, BorderLayout.WEST);
        fieldPanel.add(textField, BorderLayout.CENTER);
        panel.add(fieldPanel, gbc);
        return textField;
    }

    private JPasswordField createLabeledPasswordField(String labelText, JPanel panel, GridBagConstraints gbc) {
        JPanel fieldPanel = new JPanel(new BorderLayout(10, 10));
        fieldPanel.setBackground(Color.WHITE);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        JPasswordField passwordField = new JPasswordField(20);
        
        fieldPanel.add(label, BorderLayout.WEST);
        fieldPanel.add(passwordField, BorderLayout.CENTER);
        panel.add(fieldPanel, gbc);
        return passwordField;
    }

    private JPanel createLabeledComboBox(String labelText, JComboBox<String> comboBox) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(label, BorderLayout.WEST);
        panel.add(comboBox, BorderLayout.CENTER);
        return panel;
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    private void registerUser() {
        JOptionPane.showMessageDialog(this, "Chức năng đăng ký chưa được triển khai!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
}
