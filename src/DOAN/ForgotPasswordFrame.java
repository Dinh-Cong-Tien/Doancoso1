package DOAN;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.Random;
import MySQL.*;

public class ForgotPasswordFrame extends JFrame {
    private JTextField txtEmail;
    private JButton btnSubmit, btnCancel;
    private String verificationCode;

    public ForgotPasswordFrame() {
        setTitle("Quên Mật Khẩu");
        setSize(450, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(45, 45, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel lblTitle = new JLabel("🔒 Quên Mật Khẩu?");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        panel.add(lblTitle, gbc);

        gbc.gridy++;
        JLabel lblEmail = new JLabel("Nhập email của bạn:");
        lblEmail.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblEmail.setForeground(Color.LIGHT_GRAY);
        panel.add(lblEmail, gbc);

        gbc.gridy++;
        txtEmail = new JTextField(20);
        panel.add(txtEmail, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;

        btnSubmit = new JButton("Gửi Mã Xác Nhận");
        btnSubmit.addActionListener(this::checkEmailInDatabase);

        btnCancel = new JButton("Hủy");
        btnCancel.addActionListener(e -> dispose());

        gbc.gridy++;
        panel.add(btnSubmit, gbc);
        gbc.gridx = 1;
        panel.add(btnCancel, gbc);

        add(panel, BorderLayout.CENTER);
    }

    private void checkEmailInDatabase(ActionEvent e) {
        String email = txtEmail.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập email!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/vexemphim", "root", "");
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM nguoi_dung WHERE email = ?")) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Tạo mã xác nhận ngẫu nhiên
                verificationCode = generateVerificationCode();

                // Cập nhật mã xác nhận vào CSDL
                try (PreparedStatement updatePs = conn.prepareStatement("UPDATE nguoi_dung SET reset_code = ? WHERE email = ?")) {
                    updatePs.setString(1, verificationCode);
                    updatePs.setString(2, email);
                    updatePs.executeUpdate();
                }

                // Gửi email chứa mã xác nhận
                sendEmail(email, verificationCode);

                // Mở giao diện xác nhận mã
                new VerifyCodeFrame(email).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Email không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    private void sendEmail(String email, String code) {
        System.out.println("📧 Mã xác nhận " + code + " đã gửi đến " + email);
        JOptionPane.showMessageDialog(this, "Mã xác nhận đã được gửi qua email!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ForgotPasswordFrame().setVisible(true));
    }
}
