package DOAN;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewBookingFrame extends JFrame {
    private JTable bookingTable;
    private DefaultTableModel tableModel;
    private String username;

    public ViewBookingFrame(String username) {
        this.username = username;
        setTitle("Lịch Chiếu Phim Đã Đặt");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tiêu đề bảng
        String[] columnNames = {"ID", "Tên Phim", "Thể Loại", "Thời Lượng", "Ngày Chiếu", "Phòng", "Số Ghế Đã Đặt", "Giá Vé"};
        tableModel = new DefaultTableModel(columnNames, 0);
        bookingTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookingTable);

        loadBookings();

        // Tiêu đề giao diện
        JLabel lblTitle = new JLabel("LỊCH CHIẾU PHIM ĐÃ ĐẶT", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(50, 50, 50));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        add(lblTitle, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private void loadBookings() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/vexemphim", "root", "")) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String query = "SELECT lc.id, lc.ten_phim, lc.the_loai, lc.thoi_luong, sc.ngay_khoi_chieu, " +
                           "sc.ten_phong, vxp.so_ghe_da_dat, sc.gia_ve " +
                           "FROM lich_chieu_phim lc " +
                           "JOIN suat_chieu sc ON lc.id = sc.phim_id " +
                           "JOIN ve_xem_phim vxp ON sc.id = vxp.id_lich_chieu " +
                           "JOIN nguoi_dung nd ON vxp.id_nguoi_dung = nd.id " +
                           "WHERE nd.ten_dang_nhap = ?";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            tableModel.setRowCount(0); // Xóa dữ liệu cũ trước khi tải mới
            while (rs.next()) {
                int id = rs.getInt("id");
                String tenPhim = rs.getString("ten_phim");
                String theLoai = rs.getString("the_loai");
                int thoiLuong = rs.getInt("thoi_luong");
                String ngayKhoiChieu = rs.getString("ngay_khoi_chieu");
                String tenPhong = rs.getString("ten_phong");
                String soGheDaDat = rs.getString("so_ghe_da_dat"); // ✅ Đổi từ getInt() thành getString()
                double giaVe = rs.getDouble("gia_ve");

                tableModel.addRow(new Object[]{id, tenPhim, theLoai, thoiLuong, ngayKhoiChieu, tenPhong, soGheDaDat, giaVe});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
