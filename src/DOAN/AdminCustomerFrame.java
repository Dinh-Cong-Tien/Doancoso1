package DOAN;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import MySQL.DatabaseConnection;

public class AdminCustomerFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnEdit, btnDelete;
    private Connection conn;

    public AdminCustomerFrame() {
        setTitle("Quản Lý Khách Hàng - StarLight Cinema");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Kết nối database
        conn = DatabaseConnection.getConnection();

        // Tiêu đề
        JLabel titleLabel = new JLabel("📋 Danh Sách Khách Hàng", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(new Color(0, 102, 204));
        add(titleLabel, BorderLayout.NORTH);

        // Bảng khách hàng
        String[] columnNames = {"ID", "Tên Đăng Nhập", "Họ Tên", "Email", "SĐT", "Giới Tính", "Ngày Sinh"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Nút chức năng
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnAdd = new JButton("➕ Thêm");
        btnEdit = new JButton("✏️ Sửa");
        btnDelete = new JButton("🗑 Xóa");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load dữ liệu từ database
        loadCustomers();

        // Xử lý sự kiện
        btnAdd.addActionListener(e -> addCustomer());
        btnEdit.addActionListener(e -> editCustomer());
        btnDelete.addActionListener(e -> deleteCustomer());

        setVisible(true);
    }

    private void loadCustomers() {
        try {
            String sql = "SELECT id, ten_dang_nhap, ho_ten, email, so_dien_thoai, gioi_tinh, ngay_sinh FROM nguoi_dung";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            tableModel.setRowCount(0);

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("ten_dang_nhap"),
                    rs.getString("ho_ten"),
                    rs.getString("email"),
                    rs.getString("so_dien_thoai"),
                    rs.getString("gioi_tinh"),
                    rs.getString("ngay_sinh")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addCustomer() {
        String username = JOptionPane.showInputDialog("Nhập tên đăng nhập:");
        String name = JOptionPane.showInputDialog("Nhập họ tên:");
        String email = JOptionPane.showInputDialog("Nhập email:");
        String phone = JOptionPane.showInputDialog("Nhập số điện thoại:");
        String gender = JOptionPane.showInputDialog("Nhập giới tính (Nam/Nữ):");
        String birthdate = JOptionPane.showInputDialog("Nhập ngày sinh (YYYY-MM-DD):");

        if (username != null && name != null && email != null && phone != null && gender != null && birthdate != null) {
            try {
                String sql = "INSERT INTO nguoi_dung (ten_dang_nhap, ho_ten, email, so_dien_thoai, gioi_tinh, ngay_sinh) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, name);
                stmt.setString(3, email);
                stmt.setString(4, phone);
                stmt.setString(5, gender);
                stmt.setString(6, birthdate);

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
                loadCustomers();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm khách hàng!");
            }
        }
    }

    private void editCustomer() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Chọn khách hàng cần chỉnh sửa!");
            return;
        }

        int id = (int) table.getValueAt(selectedRow, 0);
        String newName = JOptionPane.showInputDialog("Nhập họ tên mới:", table.getValueAt(selectedRow, 2));
        String newEmail = JOptionPane.showInputDialog("Nhập email mới:", table.getValueAt(selectedRow, 3));
        String newPhone = JOptionPane.showInputDialog("Nhập số điện thoại mới:", table.getValueAt(selectedRow, 4));
        String newGender = JOptionPane.showInputDialog("Nhập giới tính mới (Nam/Nữ):", table.getValueAt(selectedRow, 5));
        String newBirthdate = JOptionPane.showInputDialog("Nhập ngày sinh mới (YYYY-MM-DD):", table.getValueAt(selectedRow, 6));

        if (newName != null && newEmail != null && newPhone != null && newGender != null && newBirthdate != null) {
            try {
                String sql = "UPDATE nguoi_dung SET ho_ten=?, email=?, so_dien_thoai=?, gioi_tinh=?, ngay_sinh=? WHERE id=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, newName);
                stmt.setString(2, newEmail);
                stmt.setString(3, newPhone);
                stmt.setString(4, newGender);
                stmt.setString(5, newBirthdate);
                stmt.setInt(6, id);

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadCustomers();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật!");
            }
        }
    }

    private void deleteCustomer() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Chọn khách hàng cần xóa!");
            return;
        }

        int id = (int) table.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM nguoi_dung WHERE id=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, id);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadCustomers();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa!");
            }
        }
    }
}
