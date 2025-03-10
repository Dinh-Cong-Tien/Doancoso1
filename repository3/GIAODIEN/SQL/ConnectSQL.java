package SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class ConnectSQL {
	private static final String URL = "jdbc:mysql://localhost:3306/dtbdemo";
    private static final String USER = "root"; // Tên người dùng MySQL
    private static final String PASSWORD = ""; // Mật khẩu MySQL

	public static Connection getConnection() {
	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        return DriverManager.getConnection(URL, USER, PASSWORD);
	    } catch (ClassNotFoundException e) {
	        System.out.println("JDBC Driver không được tìm thấy!");
	        e.printStackTrace();
	    } catch (Exception e) {
	        System.out.println("Lỗi kết nối cơ sở dữ liệu!");
	        e.printStackTrace();
	    }
	    return null;
	}
	
	// Phương thức để lấy và hiển thị tên đã sắp xếp từ cơ sở dữ liệu
    public static void getSortedNames(String sortOrder) {
        // Kiểm tra giá trị sortOrder để tránh lỗi SQL injection
        if (!sortOrder.equalsIgnoreCase("ASC") && !sortOrder.equalsIgnoreCase("DESC")) {
            return;
        }

        // Định nghĩa câu lệnh SQL với lệnh sắp xếp động
        String sql = "SELECT tên người dùng FROM thongke ORDER BY tên người dùng " + sortOrder;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String name = rs.getString("tên người dùng");
                System.out.println(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	// Lấy thống kê Giới tính
    public static Map<String, Integer> getGenderStatistics() {
        Map<String, Integer> genderStats = new HashMap<>();
        String query = "SELECT `giới tính` AS gender, COUNT(*) AS count FROM thongke GROUP BY `giới tính`";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {       //đọc dữ liệu

            while (rs.next()) {
                String gender = rs.getString("gender");
                int count = rs.getInt("count");
                genderStats.put(gender, count);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genderStats;
    }

 // Lấy thống kê Khóa học
    public static Map<String, Integer> getCourseStatistics() {
        Map<String, Integer> courseStats = new HashMap<>();
        String query = "SELECT khoahoc, COUNT(*) AS count FROM thongke GROUP BY khoahoc";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String course = rs.getString("khoahoc");
                int count = rs.getInt("count");

                // Bỏ qua các khóa bị null
                if (course != null) {
                    courseStats.put(course, count);
                }
            }

        } catch (SQLException e) {	
            e.printStackTrace();
        }
        return courseStats;
    }
}


