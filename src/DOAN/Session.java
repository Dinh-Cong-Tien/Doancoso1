package DOAN;

public class Session {
    private static Session instance;
    private String username;
    private String fullName; 
    private boolean isAdmin;

    private Session() {}

    public static synchronized Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    // Cập nhật thông tin người dùng, bao gồm username, fullName và isAdmin
    public void setUser(String username, String fullName, boolean isAdmin) {
        this.username = username;
        this.fullName = fullName;
        this.isAdmin = isAdmin;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName; 
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getRole() {
        return isAdmin ? "admin" : "user";
    }

    // Đăng xuất người dùng, xóa thông tin khỏi session
    public void logout() {
        this.username = null;
        this.fullName = null;
        this.isAdmin = false;
    }

    // Kiểm tra xem người dùng đã đăng nhập hay chưa
    public boolean isLoggedIn() {
        return username != null;
    }

    // Các phương thức set cho username, role và trạng thái đăng nhập
    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(String role) {
        this.isAdmin = role.equalsIgnoreCase("admin");
    }

    public void setLoggedIn(boolean loggedIn) {
        if (!loggedIn) {
            this.username = null;
            this.fullName = null;
            this.isAdmin = false;
        }
    }
}
