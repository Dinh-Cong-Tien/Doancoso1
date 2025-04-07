//package DOAN;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//
//public class AddPromotionFrame extends JFrame {
//    private JTextField txtTenCombo, txtDieuKien, txtGiamGia, txtQuaTang;
//    private JButton btnSave, btnCancel;
//    private Connection conn;
//
//    public AddPromotionFrame() {
//        setTitle("Thêm Khuyến Mãi");
//        setSize(400, 300);
//        setLocationRelativeTo(null);
//        setLayout(new GridLayout(5, 2, 10, 10));
//
//        // Kết nối database
//        connectToDatabase();
//
//        // Các thành phần giao diện
//        JLabel lblTenCombo = new JLabel("Tên Combo:");
//        txtTenCombo = new JTextField();
//
//        JLabel lblDieuKien = new JLabel("Điều kiện:");
//        txtDieuKien = new JTextField();
//
//        JLabel lblGiamGia = new JLabel("Giảm giá:");
//        txtGiamGia = new JTextField();
//
//        JLabel lblQuaTang = new JLabel("Quà tặng:");
//        txtQuaTang = new JTextField();
//
//        btnSave = new JButton("Lưu");
//        btnCancel = new JButton("Hủy");
//
//        // Thêm vào frame
//        add(lblTenCombo);
//        add(txtTenCombo);
//        add(lblDieuKien);
//        add(txtDieuKien);
//        add(lblGiamGia);
//        add(txtGiamGia);
//        add(lblQuaTang);
//        add(txtQuaTang);
//        add(btnSave);
//        add(btnCancel);
//
//        // Sự kiện nút
//        btnSave.addActionListener(e -> savePromotion());
//        btnCancel.addActionListener(e -> dispose());
//
//        setVisible(true);
//    }
//
//    private void connectToDatabase() {
//        try {
//            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/vexemphim", "root", "");
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(this, "Không thể kết nối đến database!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private void savePromotion() {
//        String tenCombo = txtTenCombo.getText();
//        String dieuKien = txtDieuKien.getText();
//        String giamGia = txtGiamGia.getText();
//        String quaTang = txtQuaTang.getText();
//
//        if (tenCombo.isEmpty() || dieuKien.isEmpty() || giamGia.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//
//        String query = "INSERT INTO khuyen_mai (ten_combo, dieu_kien, giam_gia, qua_tang) VALUES (?, ?, ?, ?)";
//        try (PreparedStatement stmt = conn.prepareStatement(query)) {
//            stmt.setString(1, tenCombo);
//            stmt.setInt(2, Integer.parseInt(dieuKien));
//            stmt.setDouble(3, Double.parseDouble(giamGia));
//            stmt.setString(4, quaTang.isEmpty() ? null : quaTang);
//            stmt.executeUpdate();
//            JOptionPane.showMessageDialog(this, "Thêm khuyến mãi thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//            dispose();
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(this, "Lỗi khi thêm khuyến mãi!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//}
