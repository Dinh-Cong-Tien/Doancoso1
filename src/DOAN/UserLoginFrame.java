package DOAN;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.MessageDigest;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import MySQL.*;

public class UserLoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private static final int MAX_ATTEMPTS = 3; // Số lần thử tối đa trước khi khóa tài khoản
    private static Map<String, Integer> loginAttempts = new HashMap<>();

    public UserLoginFrame() {
        setTitle("Login");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(75, 0, 130), 0, getHeight(), new Color(186, 85, 211));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        add(mainPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tiêu đề Login 
        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(75, 0, 130));
                int size = Math.min(getWidth(), getHeight());
                g2d.fillOval((getWidth() - size) / 2, (getHeight() - size) / 2, size, size);
            }
        };
        titlePanel.setPreferredSize(new Dimension(100, 100));
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new GridBagLayout());
        
        JLabel lblTitle = new JLabel("Đăng Nhập", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titlePanel, gbc);

        // Tên đăng nhập
        JLabel lblUsername = new JLabel("Tên Đăng Nhập:");
        lblUsername.setForeground(Color.WHITE);
        txtUsername = new JTextField(15);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(lblUsername, gbc);
        gbc.gridx = 1;
        mainPanel.add(txtUsername, gbc);

        // Mật khẩu
        JLabel lblPassword = new JLabel("Mật Khẩu:");
        lblPassword.setForeground(Color.WHITE);
        txtPassword = new JPasswordField(15);
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(lblPassword, gbc);
        gbc.gridx = 1;
        mainPanel.add(txtPassword, gbc);

        // Nút đăng nhập
        btnLogin = createStyledButton("Đăng nhập", new Color(186, 85, 211));
        btnLogin.addActionListener(e -> loginUser());

        JPanel buttonPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(186, 85, 211));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnLogin);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);
        
        // Nhãn "Quên mật khẩu?" và "Bạn chưa có tài khoản?" 
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);

        JLabel lblForgotPassword = new JLabel("Quên mật khẩu?");
        lblForgotPassword.setForeground(Color.WHITE);
        lblForgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblForgotPassword.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Window currentWindow = SwingUtilities.getWindowAncestor(lblForgotPassword);
                if (currentWindow != null) {
                    currentWindow.dispose();
                }
                
                // Hiển thị giao diện quên mật khẩu
                ForgotPasswordFrame forgotPasswordFrame = new ForgotPasswordFrame();
                forgotPasswordFrame.setVisible(true);
            }
        });

        JLabel lblRegister = new JLabel("Bạn chưa có tài khoản?");
        lblRegister.setForeground(Color.WHITE);
        lblRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblRegister.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new RegisterFrame();
            }
        });
        
        bottomPanel.add(lblForgotPassword);
        bottomPanel.add(new JLabel(" | ")); 
        bottomPanel.add(lblRegister);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        mainPanel.add(bottomPanel, gbc);
        
        setVisible(true);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void loginUser() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT ho_ten, mat_khau FROM nguoi_dung WHERE ten_dang_nhap = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String fullName = rs.getString("ho_ten");
                String hashedPassword = rs.getString("mat_khau");

                if (validatePassword(password, hashedPassword)) {
                    // 🔥 Lưu thông tin đăng nhập vào Session
                    Session.getInstance().setUser(username, fullName, false);

                    JOptionPane.showMessageDialog(this, "Đăng nhập thành công!\nChào mừng " + fullName, "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    loginAttempts.remove(username); 

                    // Mở HomeFrame và cập nhật giao diện
                    dispose();
                    new UserInterfaceFrame().setVisible(true);
                } else {
                    handleFailedLogin(username);
                }
            } else {
                handleFailedLogin(username);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối đến cơ sở dữ liệu. Vui lòng thử lại sau!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void handleFailedLogin(String username) {
        int attempts = loginAttempts.getOrDefault(username, 0) + 1;
        loginAttempts.put(username, attempts);
        int remainingAttempts = MAX_ATTEMPTS - attempts;

        if (remainingAttempts > 0) {
            JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu!\nBạn còn " + remainingAttempts + " lần thử.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Tài khoản đã bị khóa do nhập sai quá nhiều lần!\nVui lòng liên hệ quản trị viên để mở khóa.", "Khóa tài khoản", JOptionPane.ERROR_MESSAGE);
            btnLogin.setEnabled(false);
        }
    }

    // Kiểm tra độ mạnh của mật khẩu
    private String evaluatePasswordStrength(String password) {
        if (password.length() < 6) return "Yếu";

        boolean hasUpper = Pattern.compile("[A-Z]").matcher(password).find();
        boolean hasLower = Pattern.compile("[a-z]").matcher(password).find();
        boolean hasDigit = Pattern.compile("[0-9]").matcher(password).find();
        boolean hasSpecial = Pattern.compile("[!@#$%^&*()-+=]").matcher(password).find();

        if (hasUpper && hasLower && hasDigit && hasSpecial && password.length() >= 8) return "Mạnh";
        if (hasUpper && hasLower && hasDigit) return "Trung bình";

        return "Yếu";
    }

    // So sánh mật khẩu đã nhập với mật khẩu đã mã hóa
    private boolean validatePassword(String inputPassword, String storedHashedPassword) {
        return MessageDigest.isEqual(inputPassword.getBytes(), storedHashedPassword.getBytes());
    }
}
