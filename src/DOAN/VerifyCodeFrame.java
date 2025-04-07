package DOAN;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class VerifyCodeFrame extends JFrame {
    private JTextField txtCode;
    private String email;

    public VerifyCodeFrame(String email) {
        this.email = email;

        setTitle("Xác nhận mã");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(3, 1, 10, 10));

        JLabel lbl = new JLabel("Nhập mã xác nhận:");
        txtCode = new JTextField();

        JButton btnVerify = new JButton("Xác nhận");
        btnVerify.addActionListener(e -> verifyCode());

        add(lbl);
        add(txtCode);
        add(btnVerify);
    }

    private void verifyCode() {
        String enteredCode = txtCode.getText().trim();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/vexemphim", "root", "");
             PreparedStatement ps = conn.prepareStatement("SELECT reset_code FROM nguoi_dung WHERE email = ?")) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String correctCode = rs.getString("reset_code");

                if (enteredCode.equals(correctCode)) {
                    JOptionPane.showMessageDialog(this, "Mã xác nhận chính xác!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    new ResetPasswordFrame(email).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Mã xác nhận không đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy email!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
