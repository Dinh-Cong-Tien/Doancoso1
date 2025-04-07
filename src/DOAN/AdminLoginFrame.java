package DOAN;

import javax.swing.*;

import MySQL.DatabaseConnection;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminLoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnTogglePassword;
    private boolean isPasswordVisible = false;

    public AdminLoginFrame() {
        setTitle("Đăng Nhập Quản Trị Viên");
        setSize(500, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(false);

        JPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ===== Circle "ADMIN" =====
        CirclePanel circlePanel = new CirclePanel("ADMIN");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(circlePanel, gbc);

        // ===== Username =====
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(createLabel("Tên đăng nhập:"), gbc);

        txtUsername = new CustomTextField();
        gbc.gridx = 1;
        mainPanel.add(txtUsername, gbc);

        // ===== Password =====
        gbc.gridy = 2;
        gbc.gridx = 0;
        mainPanel.add(createLabel("Mật khẩu:"), gbc);

        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setOpaque(false);

        txtPassword = new CustomPasswordField();

        btnTogglePassword = new JButton(new ImageIcon("assets/eye.png"));
        btnTogglePassword.setPreferredSize(new Dimension(32, 32));
        btnTogglePassword.setFocusPainted(false);
        btnTogglePassword.setContentAreaFilled(false);
        btnTogglePassword.setBorderPainted(false);
        btnTogglePassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTogglePassword.addActionListener(e -> togglePasswordVisibility());

        passwordPanel.add(txtPassword, BorderLayout.CENTER);
        passwordPanel.add(btnTogglePassword, BorderLayout.EAST);

        gbc.gridx = 1;
        mainPanel.add(passwordPanel, gbc);

        // ===== Button =====
        btnLogin = new CustomButton("Đăng Nhập");
        btnLogin.addActionListener(this::loginAdmin);
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        mainPanel.add(btnLogin, gbc);

        add(mainPanel);
        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        return label;
    }

    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        txtPassword.setEchoChar(isPasswordVisible ? (char) 0 : '•');
        btnTogglePassword.setIcon(new ImageIcon(isPasswordVisible ? "assets/eye-slash.png" : "assets/eye.png"));
    }

    // ==== Custom UI Components ====
    class GradientPanel extends JPanel {
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            GradientPaint gp = new GradientPaint(0, 0, new Color(0, 85, 145), 0, getHeight(), new Color(0, 35, 102));
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    class CirclePanel extends JPanel {
        private String text;

        public CirclePanel(String text) {
            this.text = text;
            int size = 120;
            setPreferredSize(new Dimension(size, size));
            setMinimumSize(new Dimension(size, size));
            setMaximumSize(new Dimension(size, size));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int diameter = Math.min(getWidth(), getHeight());
            int x = (getWidth() - diameter) / 2;
            int y = (getHeight() - diameter) / 2;

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(new Color(255, 255, 255, 30));
            g2d.fillOval(x, y, diameter, diameter);

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 20));

            FontMetrics fm = g2d.getFontMetrics();
            int textX = (getWidth() - fm.stringWidth(text)) / 2;
            int textY = (getHeight() + fm.getAscent()) / 2 - 4;

            g2d.drawString(text, textX, textY);
        }
    }

    class CustomTextField extends JTextField {
        public CustomTextField() {
            setPreferredSize(new Dimension(200, 40));
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            setBackground(new Color(245, 245, 245));
        }
    }

    class CustomPasswordField extends JPasswordField {
        public CustomPasswordField() {
            setPreferredSize(new Dimension(200, 40));
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            setBackground(new Color(245, 245, 245));
        }
    }

    class CustomButton extends JButton {
        public CustomButton(String text) {
            super(text);
            setPreferredSize(new Dimension(200, 45));
            setForeground(Color.WHITE);
            setBackground(new Color(0, 120, 215));
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent evt) {
                    setBackground(new Color(0, 100, 185));
                }
                public void mouseExited(MouseEvent evt) {
                    setBackground(new Color(0, 120, 215));
                }
            });
        }
    }

    private void loginAdmin(ActionEvent e) {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT mat_khau FROM admin WHERE ten_dang_nhap = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("mat_khau");
                if (password.equals(storedPassword)) {
                    // Lưu thông tin người dùng vào session (nếu bạn có class Session)
                    Session.getInstance().setUsername(username);

                    JOptionPane.showMessageDialog(this, "Đăng nhập thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new AdminDashboardFrame(); // Chuyển sang giao diện quản trị
                } else {
                    JOptionPane.showMessageDialog(this, "Sai mật khẩu!", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Tên đăng nhập không tồn tại!", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
