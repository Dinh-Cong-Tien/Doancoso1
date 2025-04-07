package DOAN;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddFilmsFrame extends JFrame {
    private JTextField txtTenPhim, txtTheLoai, txtThoiLuong, txtMoTa, txtNgayChieu, txtPhong, txtSoGhe, txtGioChieu, txtGiaVe, txtPoster;
    private JButton btnSave, btnCancel;

    private Connection conn;

    public AddFilmsFrame() {
        setTitle("Thêm Phim Mới");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(11, 2, 10, 10));

        // Kết nối database
        connectToDatabase();

        // Các thành phần giao diện
        JLabel lblTenPhim = new JLabel("Tên phim:");
        txtTenPhim = new JTextField();

        JLabel lblTheLoai = new JLabel("Thể loại:");
        txtTheLoai = new JTextField();

        JLabel lblThoiLuong = new JLabel("Thời lượng (phút):");
        txtThoiLuong = new JTextField();

        JLabel lblMoTa = new JLabel("Mô tả:");
        txtMoTa = new JTextField();

        JLabel lblNgayChieu = new JLabel("Ngày khởi chiếu (YYYY-MM-DD):");
        txtNgayChieu = new JTextField();

        JLabel lblPhong = new JLabel("Phòng chiếu:");
        txtPhong = new JTextField();

        JLabel lblSoGhe = new JLabel("Số lượng ghế:");
        txtSoGhe = new JTextField();

        JLabel lblGioChieu = new JLabel("Giờ chiếu (HH:MM:SS):");
        txtGioChieu = new JTextField();

        JLabel lblGiaVe = new JLabel("Giá vé:");
        txtGiaVe = new JTextField();

        JLabel lblPoster = new JLabel("Đường dẫn poster:");
        txtPoster = new JTextField();

        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");

        // Thêm vào frame
        add(lblTenPhim);
        add(txtTenPhim);
        add(lblTheLoai);
        add(txtTheLoai);
        add(lblThoiLuong);
        add(txtThoiLuong);
        add(lblMoTa);
        add(txtMoTa);
        add(lblNgayChieu);
        add(txtNgayChieu);
        add(lblPhong);
        add(txtPhong);
        add(lblSoGhe);
        add(txtSoGhe);
        add(lblGioChieu);
        add(txtGioChieu);
        add(lblGiaVe);
        add(txtGiaVe);
        add(lblPoster);
        add(txtPoster);
        add(btnSave);
        add(btnCancel);

        // Sự kiện nút
        btnSave.addActionListener(e -> saveFilm());
        btnCancel.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void connectToDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/vexemphim", "root", "");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Không thể kết nối đến database!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveFilm() {
        String tenPhim = txtTenPhim.getText();
        String theLoai = txtTheLoai.getText();
        String thoiLuong = txtThoiLuong.getText();
        String moTa = txtMoTa.getText();
        String ngayChieu = txtNgayChieu.getText();
        String phong = txtPhong.getText();
        String soGhe = txtSoGhe.getText();
        String gioChieu = txtGioChieu.getText();
        String giaVe = txtGiaVe.getText();
        String poster = txtPoster.getText();

        if (tenPhim.isEmpty() || theLoai.isEmpty() || thoiLuong.isEmpty() || ngayChieu.isEmpty() || phong.isEmpty() || soGhe.isEmpty() || gioChieu.isEmpty() || giaVe.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String query = "INSERT INTO lich_chieu_phim (ten_phim, the_loai, thoi_luong, mo_ta, ngay_khoi_chieu, ten_phong, so_luong_ghe, thoi_gian_chieu, gia_ve, poster, tong_so_ve, ve_con_lai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, tenPhim);
            stmt.setString(2, theLoai);
            stmt.setInt(3, Integer.parseInt(thoiLuong));
            stmt.setString(4, moTa);
            stmt.setString(5, ngayChieu);
            stmt.setString(6, phong);
            stmt.setInt(7, Integer.parseInt(soGhe));
            stmt.setString(8, gioChieu);
            stmt.setDouble(9, Double.parseDouble(giaVe));
            stmt.setString(10, poster);
            stmt.setInt(11, Integer.parseInt(soGhe)); // Tổng số vé = số lượng ghế
            stmt.setInt(12, Integer.parseInt(soGhe)); // Vé còn lại = số lượng ghế ban đầu
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Thêm phim thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm phim!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
