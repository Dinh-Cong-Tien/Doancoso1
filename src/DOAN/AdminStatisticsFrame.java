package DOAN;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.DecimalFormat;

public class AdminStatisticsFrame extends JFrame {
    private JLabel lblTongVe, lblDoanhThu, lblSoLuongSuat, lblPhimHot, lblVeTrungBinh;
    private JButton btnRefresh, btnClose;
    private DecimalFormat currencyFormat = new DecimalFormat("#,### VND");

    public AdminStatisticsFrame() {
        setTitle("📊 Thống Kê Hệ Thống");
        setSize(550, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 1, 10, 10));

        JPanel panelStats = new JPanel(new GridLayout(5, 2, 10, 10));

        lblTongVe = new JLabel("🎟 Tổng vé đã bán: ");
        lblDoanhThu = new JLabel("💰 Tổng doanh thu: ");
        lblSoLuongSuat = new JLabel("🎬 Tổng suất chiếu: ");
        lblPhimHot = new JLabel("🔥 Phim doanh thu cao nhất: ");
        lblVeTrungBinh = new JLabel("📈 Vé trung bình mỗi suất: ");

        panelStats.add(lblTongVe);
        panelStats.add(new JLabel());
        panelStats.add(lblDoanhThu);
        panelStats.add(new JLabel());
        panelStats.add(lblSoLuongSuat);
        panelStats.add(new JLabel());
        panelStats.add(lblPhimHot);
        panelStats.add(new JLabel());
        panelStats.add(lblVeTrungBinh);
        panelStats.add(new JLabel());

        JPanel panelButtons = new JPanel();
        btnRefresh = new JButton("🔄 Làm mới");
        btnClose = new JButton("❌ Đóng");

        panelButtons.add(btnRefresh);
        panelButtons.add(btnClose);

        btnRefresh.addActionListener(e -> loadStatistics());
        btnClose.addActionListener(e -> dispose());

        add(new JLabel("📊 Bảng Thống Kê", JLabel.CENTER));
        add(panelStats);
        add(panelButtons);

        loadStatistics();
        setVisible(true);
    }

    // 📌 Load dữ liệu từ database
    private void loadStatistics() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/vexemphim", "root", "password")) {
            
            // Lấy tổng số vé bán được
            try (PreparedStatement ps = conn.prepareStatement("SELECT SUM(tong_so_ve) FROM doanh_thu")) {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    lblTongVe.setText("🎟 Tổng vé đã bán: " + rs.getInt(1));
                }
            }

            // Lấy tổng doanh thu
            try (PreparedStatement ps = conn.prepareStatement("SELECT SUM(doanh_thu_thuc_te) FROM doanh_thu")) {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    lblDoanhThu.setText("💰 Tổng doanh thu: " + currencyFormat.format(rs.getDouble(1)));
                }
            }

            // Lấy tổng số suất chiếu
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM suat_chieu")) {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    lblSoLuongSuat.setText("🎬 Tổng suất chiếu: " + rs.getInt(1));
                }
            }

            // Lấy phim có doanh thu cao nhất
            try (PreparedStatement ps = conn.prepareStatement(
                "SELECT p.ten_phim, SUM(d.doanh_thu_thuc_te) AS doanh_thu " +
                "FROM doanh_thu d " +
                "JOIN lich_chieu_phim p ON d.id_phim = p.id " +
                "GROUP BY p.ten_phim ORDER BY doanh_thu DESC LIMIT 1")) {
                
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    lblPhimHot.setText("🔥 Phim hot: " + rs.getString(1) + " (" + currencyFormat.format(rs.getDouble(2)) + ")");
                }
            }

            // Lấy số vé trung bình mỗi suất
            try (PreparedStatement ps = conn.prepareStatement(
                "SELECT AVG(tong_so_ve) FROM doanh_thu")) {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    lblVeTrungBinh.setText("📈 Vé trung bình mỗi suất: " + String.format("%.2f", rs.getDouble(1)));
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Lỗi khi lấy dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminStatisticsFrame::new);
    }
}
