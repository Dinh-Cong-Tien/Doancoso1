package DOAN;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import MySQL.*;

public class ResetPasswordFrame extends JFrame {
    private JPasswordField txtNewPassword, txtConfirmPassword;
    private String email;

    public ResetPasswordFrame(String email) {
        this.email = email;
        setTitle("Đặt lại mật khẩu");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 1, 10, 10));

        JLabel lbl1 = new JLabel("Nhập mật khẩu mới:");
        txtNewPassword = new JPasswordField();

        JLabel lbl2 = new JLabel("Xác nhận mật khẩu:");
        txtConfirmPassword = new JPasswordField();

        JButton btnReset = new JButton("Đặt lại mật khẩu");
        btnReset.addActionListener(e -> resetPassword());

        add(lbl1);
        add(txtNewPassword);
        add(lbl2);
        add(txtConfirmPassword);
        add(btnReset);
    }

    private void resetPassword() {
        String newPassword = new String(txtNewPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE nguoi_dung SET mat_khau = ? WHERE email = ?")) {

            ps.setString(1, newPassword);
            ps.setString(2, email);
            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Mật khẩu đã được cập nhật!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi cập nhật mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
