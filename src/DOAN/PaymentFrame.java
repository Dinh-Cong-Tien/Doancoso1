package DOAN;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Set;
import MySQL.DatabaseConnection;

public class PaymentFrame extends JFrame {
    private JFrame previousFrame;
    private int userId;
    private int filmId;
    private Set<String> selectedSeats;
    private double ticketPrice;
    private JLabel lblTotalPrice;

    public PaymentFrame(JFrame previousFrame, int userId, int filmId, Set<String> selectedSeats) {
        this.previousFrame = previousFrame;
        this.userId = userId;
        this.filmId = filmId;
        this.selectedSeats = selectedSeats;

        setTitle("Thanh Toán Online");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE); // Nền trắng

        // ==== Phần thông tin thanh toán ====
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        infoPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Xác Nhận Thanh Toán", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(0x2E86C1));

        JLabel lblSeats = new JLabel("Ghế đã chọn: " + String.join(", ", selectedSeats), SwingConstants.CENTER);
        lblSeats.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        lblTotalPrice = new JLabel("Đang tải giá vé...", SwingConstants.CENTER);
        lblTotalPrice.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotalPrice.setForeground(new Color(0x27AE60));

        infoPanel.add(lblTitle);
        infoPanel.add(lblSeats);
        infoPanel.add(lblTotalPrice);
        add(infoPanel, BorderLayout.NORTH);

        // ==== Nút chọn phương thức thanh toán ====
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnMomo = createStyledButton("MoMo", new Color(0xD81B60));
        JButton btnZaloPay = createStyledButton("ZaloPay", new Color(0x2196F3));
        JButton btnCard = createStyledButton("Thẻ Ngân Hàng", new Color(0x34495E));

        btnMomo.addActionListener(e -> processPayment("MoMo"));
        btnZaloPay.addActionListener(e -> processPayment("ZaloPay"));
        btnCard.addActionListener(e -> processPayment("Thẻ Ngân Hàng"));

        buttonPanel.add(btnMomo);
        buttonPanel.add(btnZaloPay);
        buttonPanel.add(btnCard);
        add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
        fetchTicketPrice();
    }

    // Hàm tạo button đẹp
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return button;
    }

    private void fetchTicketPrice() {
        String sql = """
            SELECT sc.gia_ve
            FROM suat_chieu sc
            JOIN lich_chieu_phim lcp ON sc.phim_id = lcp.id
            WHERE lcp.id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, filmId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ticketPrice = rs.getDouble("gia_ve");
                lblTotalPrice.setText("Tổng tiền: " + calculateTotalPrice() + " VND");
            } else {
                lblTotalPrice.setText("Không tìm thấy giá vé.");
            }
        } catch (SQLException e) {
            lblTotalPrice.setText("Lỗi khi tải giá vé.");
            e.printStackTrace();
        }
    }

    private double calculateTotalPrice() {
        return ticketPrice * selectedSeats.size();
    }

    private void processPayment(String method) {
        double totalAmount = calculateTotalPrice();
        int response = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn thanh toán " + totalAmount + " VND qua " + method + "?",
                "Xác nhận Thanh Toán", JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            if (savePaymentToDatabase(method, totalAmount)) {
                JOptionPane.showMessageDialog(this, "Thanh toán thành công qua " + method + "!");

                String[] scheduleInfo = fetchScheduleInfo();
                if (scheduleInfo != null) {
                    String film = scheduleInfo[0];
                    String date = scheduleInfo[1];
                    String time = scheduleInfo[2];
                    new BookingConfirmationFrame(userId, film, date, time, String.join(", ", selectedSeats));
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Thanh toán thất bại! Vui lòng thử lại.");
            }
        }
    }

    private String[] fetchScheduleInfo() {
        String sql = """
            SELECT lcp.ten_phim, sc.ngay_khoi_chieu, sc.thoi_gian_chieu 
            FROM lich_chieu_phim lcp
            JOIN suat_chieu sc ON sc.phim_id = lcp.id
            WHERE lcp.id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, filmId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new String[]{
                        rs.getString("ten_phim"),
                        rs.getString("ngay_khoi_chieu"),
                        rs.getString("thoi_gian_chieu")
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean savePaymentToDatabase(String method, double amount) {
        String sql = "INSERT INTO thanh_toan (id_ve_xem_phim, phuong_thuc, trang_thai, ngay_thanh_toan) VALUES (?, ?, ?, NOW())";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);

            for (String seat : selectedSeats) {
                int ticketId = saveTicketToDatabase(seat);
                if (ticketId == -1) {
                    conn.rollback();
                    return false;
                }

                stmt.setInt(1, ticketId);
                stmt.setString(2, method);
                stmt.setString(3, "Đã Thanh Toán");
                stmt.addBatch();
            }

            stmt.executeBatch();
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private int saveTicketToDatabase(String seat) {
        String sql = "INSERT INTO ve_xem_phim (id_nguoi_dung, id_lich_chieu, so_ghe, ngay_dat) VALUES (?, ?, ?, NOW())";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, filmId);
            stmt.setString(3, seat);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
