package MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/vexemphim?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Đảm bảo MySQL JDBC Driver được tải (cần thiết cho Java cũ)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Kết nối đến database
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Kết nối CSDL thành công!");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Lỗi: Không tìm thấy JDBC Driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Lỗi: Không thể kết nối đến CSDL!");
            System.err.println("➡ Kiểm tra MySQL Server hoặc thông tin đăng nhập.");
            e.printStackTrace();
        }
        return conn;
    }

    public static void main(String[] args) {
        // Kiểm tra kết nối
        Connection testConn = DatabaseConnection.getConnection();
        if (testConn != null) {
            System.out.println("🔹 Kết nối hoạt động bình thường!");
        } else {
            System.out.println("⚠ Không thể kết nối đến CSDL!");
        }
    }
}
