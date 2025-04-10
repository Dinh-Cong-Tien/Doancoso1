package DOAN;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class BookingConfirmationFrame extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/vexemphim";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public BookingConfirmationFrame(int userId, int suatChieuId, String seats) {
        if (userId <= 0) {
            JOptionPane.showMessageDialog(this, "Lỗi: Không xác định được người dùng!");
            return;
        }

        setTitle("Xác nhận đặt vé");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel nền tối
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(20, 30, 60));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        add(backgroundPanel);

        // Tiêu đề
        JLabel lblTitle = new JLabel("BOOKED SUCCESSFULLY!", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.RED);
        backgroundPanel.add(lblTitle, BorderLayout.NORTH);

        // Lấy tên người dùng từ CSDL
        String name = getUserName(userId);
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lỗi: Không tìm thấy thông tin người dùng!");
            return;
        }

        // Lấy thông tin suất chiếu từ CSDL
        ShowtimeInfo showtime = getShowtimeInfo(suatChieuId);
        if (showtime == null) {
            JOptionPane.showMessageDialog(this, "Lỗi: Không tìm thấy thông tin suất chiếu!");
            return;
        }

        // Panel hiển thị thông tin vé
        JPanel ticketPanel = new JPanel();
        ticketPanel.setLayout(new GridLayout(6, 1, 10, 10));
        ticketPanel.setOpaque(false);

        ticketPanel.add(createLabel("Tên: " + name));
        ticketPanel.add(createLabel("Phim: " + showtime.filmName));
        ticketPanel.add(createLabel("Ngày: " + showtime.date));
        ticketPanel.add(createLabel("Giờ: " + showtime.time));
        ticketPanel.add(createLabel("Phòng: " + showtime.room));
        ticketPanel.add(createLabel("Ghế: " + seats));

        backgroundPanel.add(ticketPanel, BorderLayout.CENTER);

        // Nút thao tác
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);

        JButton btnEmail = new JButton("📧 Email");
        JButton btnClose = new JButton("✖ Close");

        styleButton(btnEmail, new Color(50, 150, 250));
        btnEmail.addActionListener(e -> JOptionPane.showMessageDialog(this, "Email sent successfully!"));

        styleButton(btnClose, new Color(200, 50, 50));
        btnClose.addActionListener(e -> dispose());

        buttonPanel.add(btnEmail);
        buttonPanel.add(btnClose);
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Class để lưu thông tin suất chiếu
    private static class ShowtimeInfo {
        String filmName;
        String date;
        String time;
        String room;

        ShowtimeInfo(String filmName, String date, String time, String room) {
            this.filmName = filmName;
            this.date = date;
            this.time = time;
            this.room = room;
        }
    }

    // Hàm lấy thông tin suất chiếu từ bảng suat_chieu
    private ShowtimeInfo getShowtimeInfo(int suatChieuId) {
        String query = "SELECT p.ten_phim, sc.ngay_khoi_chieu, sc.thoi_gian_chieu, sc.ten_phong " +
                       "FROM suat_chieu sc JOIN lich_chieu_phim p ON sc.phim_id = p.id WHERE sc.id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, suatChieuId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ShowtimeInfo(
                        rs.getString("ten_phim"),
                        rs.getString("ngay_khoi_chieu"),
                        rs.getString("thoi_gian_chieu"),
                        rs.getString("ten_phong")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi truy vấn suất chiếu!");
        }
        return null;
    }

    // Hàm lấy tên người dùng từ bảng nguoi_dung
    private String getUserName(int userId) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "SELECT ho_ten FROM nguoi_dung WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("ho_ten");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL!");
        }
        return "";
    }

    // Hàm tạo label
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        label.setForeground(Color.WHITE);
        return label;
    }

    // Style button
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
    }
}
