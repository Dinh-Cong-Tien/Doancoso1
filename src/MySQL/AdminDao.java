package MySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDao {
    
    public static boolean validateAdminLogin(String username, String password) {
        Connection conn = DatabaseConnection.getConnection();
        String query = "SELECT * FROM admin WHERE ten_dang_nhap = ? AND mat_khau = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("✅ Đăng nhập thành công!");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi kiểm tra đăng nhập: " + e.getMessage());
        }
        return false;
    }
}
