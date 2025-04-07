package DOAN;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class PromotionFrame extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/vexemphim";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private JTable table;
    private DefaultTableModel tableModel;

    public PromotionFrame() {
        setTitle("Chương trình khuyến mãi");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel lblTitle = new JLabel("DANH SÁCH KHUYẾN MÃI", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.RED);
        add(lblTitle, BorderLayout.NORTH);

        // Tạo bảng hiển thị danh sách khuyến mãi
        tableModel = new DefaultTableModel(new String[]{"ID", "Tên Combo", "Điều kiện", "Giảm giá (%)", "Quà tặng"}, 0);
        table = new JTable(tableModel);
        loadPromotionsFromDatabase();
        
        // Sự kiện click vào hàng để xem chi tiết
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    int promoId = (int) tableModel.getValueAt(row, 0);
                    showPromotionDetails(promoId);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Nút đóng
        JButton btnClose = new JButton("✖ Đóng");
        btnClose.addActionListener(e -> dispose());
        JPanel panel = new JPanel();
        panel.add(btnClose);
        add(panel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadPromotionsFromDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "SELECT id, ten_combo, dieu_kien, giam_gia, qua_tang FROM khuyen_mai";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String comboName = rs.getString("ten_combo");
                int condition = rs.getInt("dieu_kien");
                double discount = rs.getDouble("giam_gia");
                String gift = rs.getString("qua_tang");

                tableModel.addRow(new Object[]{id, comboName, condition, discount, gift});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL!");
        }
    }

    private void showPromotionDetails(int promoId) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM khuyen_mai WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, promoId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String comboName = rs.getString("ten_combo");
                int condition = rs.getInt("dieu_kien");
                double discount = rs.getDouble("giam_gia");
                String gift = rs.getString("qua_tang");

                JOptionPane.showMessageDialog(this,
                        "Tên Combo: " + comboName + 
                        "\nĐiều kiện: Mua " + condition + " vé" +
                        "\nGiảm giá: " + (discount * 100) + "%" +
                        "\nQuà tặng: " + gift,
                        "Chi tiết khuyến mãi", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy chi tiết khuyến mãi!");
        }
    }

    public static void main(String[] args) {
        new PromotionFrame();
    }
}
