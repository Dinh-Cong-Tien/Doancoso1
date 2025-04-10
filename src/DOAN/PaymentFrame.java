package DOAN;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Map;
import MySQL.DatabaseConnection;

public class PaymentFrame extends JFrame {
    private JFrame previousFrame;
    private int userId;
    private int suatChieuId;
    private Map<String, Integer> selectedSeats;
    private double ticketPrice;
    private JLabel lblTotalPrice;

    public PaymentFrame(JFrame previousFrame, int userId, int suatChieuId, Map<String, Integer> selectedSeats) {
        this.previousFrame = previousFrame;
        this.userId = userId;
        this.suatChieuId = suatChieuId;
        this.selectedSeats = selectedSeats;

        setTitle("Thanh Toán Online");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        infoPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Xác Nhận Thanh Toán", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(0x2E86C1));

        JLabel lblSeats = new JLabel("Ghế đã chọn: " + String.join(", ", selectedSeats.keySet()), SwingConstants.CENTER);
        lblSeats.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        lblTotalPrice = new JLabel("Đang tải giá vé...", SwingConstants.CENTER);
        lblTotalPrice.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotalPrice.setForeground(new Color(0x27AE60));

        infoPanel.add(lblTitle);
        infoPanel.add(lblSeats);
        infoPanel.add(lblTotalPrice);
        add(infoPanel, BorderLayout.NORTH);

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
        String sql = "SELECT gia_ve FROM suat_chieu WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, suatChieuId);
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
            if (savePaymentToDatabase(method)) {
                JOptionPane.showMessageDialog(this, "Thanh toán thành công qua " + method + "!");
                new BookingConfirmationFrame(userId, suatChieuId, String.join(", ", selectedSeats.keySet()));
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Thanh toán thất bại! Vui lòng thử lại.");
            }
        }
    }

    private boolean savePaymentToDatabase(String method) {
        String getPhimIdSql = "SELECT phim_id, ve_con_lai FROM suat_chieu WHERE id = ?";
        String insertVeSql = "INSERT INTO ve_xem_phim (id_lich_chieu, id_nguoi_dung, so_ghe, ngay_dat) VALUES (?, ?, ?, NOW())";
        String insertPaymentSql = "INSERT INTO thanh_toan (id_ve_xem_phim, phuong_thuc, trang_thai, ngay_thanh_toan) VALUES (?, ?, ?, NOW())";
        String updateSoVeSql = "UPDATE suat_chieu SET ve_con_lai = ve_con_lai - ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement getPhimStmt = conn.prepareStatement(getPhimIdSql);
             PreparedStatement insertVeStmt = conn.prepareStatement(insertVeSql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement insertPayStmt = conn.prepareStatement(insertPaymentSql);
             PreparedStatement updateVeStmt = conn.prepareStatement(updateSoVeSql)) {

            conn.setAutoCommit(false);

            // Lấy id phim để gán vào id_lich_chieu (vì id_lich_chieu = phim_id)
            getPhimStmt.setInt(1, suatChieuId);
            ResultSet rs = getPhimStmt.executeQuery();
            if (!rs.next()) {
                conn.rollback();
                return false;
            }
            int lichChieuId = rs.getInt("phim_id");
            int veConLai = rs.getInt("ve_con_lai");

            if (veConLai < selectedSeats.size()) {
                JOptionPane.showMessageDialog(this, "Không đủ vé còn lại cho số lượng ghế đã chọn.");
                conn.rollback();
                return false;
            }

            for (String seat : selectedSeats.keySet()) {
                // Lưu vé
                insertVeStmt.setInt(1, lichChieuId);
                insertVeStmt.setInt(2, userId);
                insertVeStmt.setString(3, seat);
                insertVeStmt.executeUpdate();
                ResultSet rsVe = insertVeStmt.getGeneratedKeys();
                if (!rsVe.next()) {
                    conn.rollback();
                    return false;
                }
                int veId = rsVe.getInt(1);

                // Lưu thanh toán
                insertPayStmt.setInt(1, veId);
                insertPayStmt.setString(2, method);
                insertPayStmt.setString(3, "Đã Thanh Toán");
                insertPayStmt.executeUpdate();
            }

            // Cập nhật số vé còn lại
            updateVeStmt.setInt(1, selectedSeats.size());
            updateVeStmt.setInt(2, suatChieuId);
            updateVeStmt.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
