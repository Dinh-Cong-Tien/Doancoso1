package DOAN;

import MySQL.DatabaseConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public AdminFrame() {
        setTitle("Quản Lý Vé Xem Phim");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Tên Phim", "Suất Chiếu", "Người Đặt", "Ghế Ngồi", "Giá Vé", "Trạng Thái"});
        table = new JTable(model);
        loadBookings();

        add(new JScrollPane(table), BorderLayout.CENTER);
        setVisible(true);
    }

    private void loadBookings() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT lcp.ten_phim, lcp.thoi_gian_chieu, nd.ten_dang_nhap, v.ghe_ngoi, lcp.gia_ve, v.trang_thai " +
                           "FROM ve_xem_phim v " +
                           "JOIN lich_chieu_phim lcp ON v.id_lich_chieu_phim = lcp.id " +
                           "JOIN nguoi_dung nd ON v.id_nguoi_dung = nd.id";

            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // In dữ liệu ra console để kiểm tra
                System.out.println(
                    "Tên phim: " + rs.getString("ten_phim") + 
                    " | Giờ chiếu: " + rs.getString("thoi_gian_chieu") + 
                    " | Người đặt: " + rs.getString("ten_dang_nhap") + 
                    " | Ghế: " + rs.getString("ghe_ngoi") + 
                    " | Giá: " + rs.getDouble("gia_ve") + 
                    " | Trạng thái: " + rs.getString("trang_thai")
                );

                // Thêm dữ liệu vào bảng
                model.addRow(new Object[]{
                    rs.getString("ten_phim"),
                    rs.getString("thoi_gian_chieu"),
                    rs.getString("ten_dang_nhap"),
                    rs.getString("ghe_ngoi"),
                    rs.getDouble("gia_ve"),
                    rs.getString("trang_thai")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
