package DOAN;

import MySQL.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AdminViewBookingFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnRefresh;

    public AdminViewBookingFrame() {
        setTitle("🎟️ Quản Lý Vé Xem Phim - Admin Panel");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout(15, 15));

        // ====== Tiêu đề ======
        JLabel lblTitle = new JLabel("QUẢN LÝ VÉ ĐÃ ĐẶT", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(33, 150, 243));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        // ====== Bảng vé ======
        String[] columnNames = {"ID Vé", "Người Đặt", "Tên Phim", "Ngày Chiếu", "Ghế", "Ngày Đặt"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(230, 240, 250));
        table.setSelectionBackground(new Color(200, 230, 255));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        add(scrollPane, BorderLayout.CENTER);

        // ====== Tìm kiếm & Làm mới ======
        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panelBottom.setBackground(Color.WHITE);

        txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        btnRefresh = new JButton("Làm Mới");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRefresh.setBackground(new Color(33, 150, 243));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRefresh.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        panelBottom.add(new JLabel("🔍 Tìm kiếm:"));
        panelBottom.add(txtSearch);
        panelBottom.add(btnRefresh);
        add(panelBottom, BorderLayout.SOUTH);

        // ====== Sự kiện ======
        btnRefresh.addActionListener(e -> loadTickets());
        txtSearch.addActionListener(e -> searchTickets());

        loadTickets();
        setVisible(true);
    }

    private void loadTickets() {
        tableModel.setRowCount(0);
        String query = "SELECT v.id, n.ho_ten, p.ten_phim, s.ngay_khoi_chieu, v.so_ghe, v.ngay_dat " +
                       "FROM ve_xem_phim v " +
                       "JOIN nguoi_dung n ON v.id_nguoi_dung = n.id " +
                       "JOIN lich_chieu_phim p ON v.id_lich_chieu = p.id " +
                       "JOIN suat_chieu s ON p.id = s.phim_id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("ho_ten"),
                    rs.getString("ten_phim"),
                    rs.getDate("ngay_khoi_chieu"),
                    rs.getString("so_ghe"),
                    rs.getDate("ngay_dat")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ Lỗi tải danh sách vé!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void searchTickets() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadTickets();
            return;
        }
        tableModel.setRowCount(0);
        String query = "SELECT v.id, n.ho_ten, p.ten_phim, s.ngay_khoi_chieu, v.so_ghe, v.ngay_dat " +
                       "FROM ve_xem_phim v " +
                       "JOIN nguoi_dung n ON v.id_nguoi_dung = n.id " +
                       "JOIN lich_chieu_phim p ON v.id_lich_chieu = p.id " +
                       "JOIN suat_chieu s ON p.id = s.phim_id " +
                       "WHERE n.ho_ten LIKE ? OR p.ten_phim LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("ho_ten"),
                        rs.getString("ten_phim"),
                        rs.getDate("ngay_khoi_chieu"),
                        rs.getString("so_ghe"),
                        rs.getDate("ngay_dat")
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ Lỗi khi tìm kiếm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
