package DOAN;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminPromotionsFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh, btnSearch, btnBack;
    private JTextField txtSearch;
    private Connection conn;
    private JTextField txtName, txtCondition, txtDiscount, txtGift;

    public AdminPromotionsFrame() {
        setTitle("Quản lý khuyến mãi");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Tiêu đề
        JLabel titleLabel = new JLabel("QUẢN LÝ KHUYẾN MÃI", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

     // Tìm kiếm
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Tạo trường nhập liệu tìm kiếm
        txtSearch = new JTextField(20);

        // Tạo icon và chỉnh kích thước
        ImageIcon searchIcon = new ImageIcon("C:\\Users\\Admin\\eclipse-workspace\\DOANCOSO1\\bin\\picture DOAN\\timkiem.png"); // Thay đường dẫn icon ở đây
        Image scaledImage = searchIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);  // Thay đổi kích thước icon
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Tạo nút tìm kiếm với icon đã chỉnh kích thước
        btnSearch = new JButton("Tìm kiếm", scaledIcon);

        // Tùy chỉnh màu sắc cho nút tìm kiếm
        btnSearch.setBackground(new Color(52, 152, 219));  // Màu xanh dương
        btnSearch.setForeground(Color.WHITE);  // Màu chữ trắng
        btnSearch.setFont(new Font("Arial", Font.BOLD, 14)); // Đặt phông chữ cho nút

        // Thêm nút và trường tìm kiếm vào panel
        searchPanel.add(new JLabel("Tìm kiếm:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        // Thêm panel tìm kiếm vào giao diện
        add(searchPanel, BorderLayout.NORTH);


        // Bảng danh sách khuyến mãi
        String[] columnNames = {"ID", "Tên Combo", "Điều kiện", "Giảm giá", "Quà tặng"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

     // Panel chứa các nút chức năng
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));  // Căn chỉnh các nút theo chiều ngang

        btnAdd = new JButton("Thêm KM", new ImageIcon(new ImageIcon("C:\\Users\\Admin\\eclipse-workspace\\DOANCOSO1\\bin\\picture DOAN\\them.png")
                .getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        btnEdit = new JButton("Sửa KM", new ImageIcon(new ImageIcon("C:\\Users\\Admin\\eclipse-workspace\\DOANCOSO1\\bin\\picture DOAN\\sua.png")
                .getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        btnDelete = new JButton("Xóa KM", new ImageIcon(new ImageIcon("C:\\Users\\Admin\\eclipse-workspace\\DOANCOSO1\\bin\\picture DOAN\\xoa.png")
                .getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        btnRefresh = new JButton("Làm mới", new ImageIcon(new ImageIcon("C:\\Users\\Admin\\eclipse-workspace\\DOANCOSO1\\bin\\picture DOAN\\lammoi.png")
                .getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        btnBack = new JButton("Quay lại", new ImageIcon(new ImageIcon("C:\\Users\\Admin\\eclipse-workspace\\DOANCOSO1\\bin\\picture DOAN\\quaylai.png")
                .getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));

        // Đặt màu cho các nút
        btnAdd.setBackground(new Color(46, 204, 113));        // Xanh lá - Thêm
        btnAdd.setForeground(Color.WHITE);

        btnEdit.setBackground(new Color(241, 196, 15));       // Vàng - Sửa
        btnEdit.setForeground(Color.BLACK);

        btnDelete.setBackground(new Color(231, 76, 60));      // Đỏ - Xoá
        btnDelete.setForeground(Color.WHITE);

        btnRefresh.setBackground(new Color(52, 152, 219));    // Xanh dương - Làm mới
        btnRefresh.setForeground(Color.WHITE);

        btnBack.setBackground(new Color(149, 165, 166));      // Xám - Quay lại
        btnBack.setForeground(Color.WHITE);

        // Chỉnh khoảng cách giữa icon và text
        btnAdd.setIconTextGap(10);   // Cách khoảng icon và chữ
        btnEdit.setIconTextGap(10);
        btnDelete.setIconTextGap(10);
        btnRefresh.setIconTextGap(10);
        btnBack.setIconTextGap(10);

        // Thêm các nút vào panel
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnBack);

        // Thêm panel vào giao diện
        add(buttonPanel, BorderLayout.SOUTH);


        // Panel chứa các trường nhập liệu
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Tên Combo:"));
        txtName = new JTextField(20);
        inputPanel.add(txtName);
        inputPanel.add(new JLabel("Điều kiện:"));
        txtCondition = new JTextField(20);
        inputPanel.add(txtCondition);
        inputPanel.add(new JLabel("Giảm giá:"));
        txtDiscount = new JTextField(20);
        inputPanel.add(txtDiscount);
        inputPanel.add(new JLabel("Quà tặng:"));
        txtGift = new JTextField(20);
        inputPanel.add(txtGift);
        add(inputPanel, BorderLayout.WEST);

        // Kết nối database
        connectToDatabase();
        loadPromotions();

        // Sự kiện các nút
        btnAdd.addActionListener(e -> addPromotion());
        btnEdit.addActionListener(e -> editPromotion());
        btnDelete.addActionListener(e -> deletePromotion());
        btnRefresh.addActionListener(e -> loadPromotions());
        btnSearch.addActionListener(e -> searchPromotion(txtSearch.getText()));
        btnBack.addActionListener(e -> {
            dispose(); 
            new AdminDashboardFrame(); 
        });

        // Sự kiện khi click vào bảng
        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                txtName.setText(tableModel.getValueAt(selectedRow, 1).toString());
                txtCondition.setText(tableModel.getValueAt(selectedRow, 2).toString());
                txtDiscount.setText(tableModel.getValueAt(selectedRow, 3).toString());
                txtGift.setText(tableModel.getValueAt(selectedRow, 4).toString());
            }
        });

        setVisible(true);
    }

    private void connectToDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/vexemphim", "root", "");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Không thể kết nối đến database!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadPromotions() {
        tableModel.setRowCount(0);
        String query = "SELECT id, ten_combo, dieu_kien, giam_gia, qua_tang FROM khuyen_mai";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("ten_combo") != null ? rs.getString("ten_combo") : "",
                    rs.getString("dieu_kien") != null ? rs.getString("dieu_kien") : "",
                    rs.getDouble("giam_gia"),
                    rs.getString("qua_tang") != null ? rs.getString("qua_tang") : ""
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addPromotion() {
        if (!validateInputs()) return;
        
        // Kiểm tra và thay thế "không" thành null hoặc giá trị trống
        String name = txtName.getText().equalsIgnoreCase("không") ? null : txtName.getText();
        String condition = txtCondition.getText().equalsIgnoreCase("không") ? null : txtCondition.getText();
        String gift = txtGift.getText().equalsIgnoreCase("không") ? null : txtGift.getText();
        double discount = txtDiscount.getText().equalsIgnoreCase("không") || txtDiscount.getText().isEmpty() ? 0 : Double.parseDouble(txtDiscount.getText());

        String query = "INSERT INTO khuyen_mai(ten_combo, dieu_kien, giam_gia, qua_tang) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, condition);
            stmt.setDouble(3, discount);
            stmt.setString(4, gift);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Khuyến mãi đã được thêm!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            loadPromotions();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm khuyến mãi!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editPromotion() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khuyến mãi để chỉnh sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int promoId = (int) tableModel.getValueAt(selectedRow, 0);
        String query = "UPDATE khuyen_mai SET ten_combo = ?, dieu_kien = ?, giam_gia = ?, qua_tang = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, txtName.getText().isEmpty() ? null : txtName.getText());
            stmt.setString(2, txtCondition.getText().isEmpty() ? null : txtCondition.getText());
            stmt.setDouble(3, txtDiscount.getText().isEmpty() ? 0 : Double.parseDouble(txtDiscount.getText()));
            stmt.setString(4, txtGift.getText().isEmpty() ? null : txtGift.getText());
            stmt.setInt(5, promoId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Khuyến mãi đã được sửa!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            loadPromotions();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi sửa khuyến mãi!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePromotion() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khuyến mãi để xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int promoId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa khuyến mãi này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            conn.setAutoCommit(false); // Bắt đầu giao dịch

            // 1. Xóa khuyến mãi được chọn
            String deleteQuery = "DELETE FROM khuyen_mai WHERE id = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                deleteStmt.setInt(1, promoId);
                int result = deleteStmt.executeUpdate();

                if (result > 0) {
                    // 2. Sắp xếp lại ID
                    String selectQuery = "SELECT id FROM khuyen_mai ORDER BY id ASC";
                    try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
                         ResultSet rs = selectStmt.executeQuery()) {

                        int newId = 1;
                        while (rs.next()) {
                            int currentId = rs.getInt("id");
                            if (currentId != newId) {
                                String updateQuery = "UPDATE khuyen_mai SET id = ? WHERE id = ?";
                                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                                    updateStmt.setInt(1, newId);
                                    updateStmt.setInt(2, currentId);
                                    updateStmt.executeUpdate();
                                }
                            }
                            newId++;
                        }
                    }

                    // 3. Reset AUTO_INCREMENT
                    String alterQuery = "ALTER TABLE khuyen_mai AUTO_INCREMENT = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(alterQuery)) {
                        stmt.setInt(1, getNextAutoIncrementValue());
                        stmt.executeUpdate();
                    }

                    conn.commit(); // Cam kết thay đổi
                    JOptionPane.showMessageDialog(this, "Đã xoá và cập nhật lại ID!");

                    // 4. Làm mới dữ liệu bảng
                    loadPromotions();
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể xoá khuyến mãi!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                conn.rollback(); // Hoàn tác nếu lỗi
                JOptionPane.showMessageDialog(this, "Lỗi khi xoá: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi cơ sở dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getNextAutoIncrementValue() {
        String query = "SELECT MAX(id) + 1 AS next_id FROM khuyen_mai";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("next_id") == 0 ? 1 : rs.getInt("next_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }
    
    private void searchPromotion(String query) {
        tableModel.setRowCount(0);
        String searchQuery = "SELECT id, ten_combo, dieu_kien, giam_gia, qua_tang FROM khuyen_mai WHERE ten_combo LIKE ?";

        try (PreparedStatement stmt = conn.prepareStatement(searchQuery)) {
            stmt.setString(1, "%" + query + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("ten_combo") != null ? rs.getString("ten_combo") : "",
                    rs.getString("dieu_kien") != null ? rs.getString("dieu_kien") : "",
                    rs.getDouble("giam_gia"),
                    rs.getString("qua_tang") != null ? rs.getString("qua_tang") : ""
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateInputs() {
        if (txtName.getText().isEmpty() || txtCondition.getText().isEmpty() || txtGift.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            if (!txtDiscount.getText().isEmpty()) {
                Double.parseDouble(txtDiscount.getText());  // Kiểm tra giảm giá có phải là số không
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giảm giá phải là một số!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}
