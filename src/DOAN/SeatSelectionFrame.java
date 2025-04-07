package DOAN;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import MySQL.*;

public class SeatSelectionFrame extends JFrame {
    private int filmId;
    private String filmTitle;
    private JButton[][] seats;
    private Set<String> selectedSeats;
    private JButton btnConfirm, btnBack;
    private JDateChooser dateChooser;
    private int userId;
    private JComboBox<String> dateComboBox, timeComboBox;

    public SeatSelectionFrame(int filmId, String filmTitle, int userId) {
        this.filmId = filmId;
        this.filmTitle = filmTitle;
        this.userId = userId;
        this.selectedSeats = new HashSet<>();

        setTitle("Chọn Ghế - " + filmTitle);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel backgroundPanel = new JPanel(new BorderLayout());
        backgroundPanel.setBackground(new Color(20, 30, 60));
        add(backgroundPanel);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(10, 20, 40));
        JLabel lblTitle = new JLabel("Chọn Ghế Cho: " + filmTitle, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        topPanel.add(lblTitle, BorderLayout.CENTER);

        btnBack = new JButton("⬅ Quay lại");
        btnBack.setBackground(new Color(200, 50, 50));
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(e -> closeWindow());
        topPanel.add(btnBack, BorderLayout.WEST);
        backgroundPanel.add(topPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        backgroundPanel.add(mainPanel, BorderLayout.CENTER);

        // Sidebar for Date & Time Selection
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(20, 30, 60));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel chọn ngày
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.setBackground(new Color(30, 40, 70));

        JLabel lblDate = new JLabel("📅 Chọn ngày:");
        lblDate.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblDate.setForeground(new Color(255, 180, 80));

        // Thay thế JComboBox bằng JDateChooser để chọn ngày dạng lịch
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy"); // Định dạng ngày
        dateChooser.setFont(new Font("SansSerif", Font.PLAIN, 14));
        dateChooser.setPreferredSize(new Dimension(150, 30));
        dateChooser.setBackground(new Color(50, 60, 90));
        dateChooser.getCalendarButton().setBackground(new Color(100, 100, 150)); // Nút mở lịch

        datePanel.add(lblDate);
        datePanel.add(dateChooser);

        // Panel chọn giờ
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timePanel.setBackground(new Color(30, 40, 70));

        JLabel lblTime = new JLabel("⏰ Chọn giờ:");
        lblTime.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTime.setForeground(new Color(255, 180, 80));

        timeComboBox = new JComboBox<>(generateTimeOptions());
        timeComboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        timeComboBox.setPreferredSize(new Dimension(150, 30));
        timeComboBox.setBackground(new Color(50, 60, 90));
        timeComboBox.setForeground(Color.WHITE);
        timeComboBox.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 150), 1));

        timePanel.add(lblTime);
        timePanel.add(timeComboBox);

        // Thêm vào sidebar
        sidebarPanel.add(datePanel);
        sidebarPanel.add(Box.createVerticalStrut(10)); // Khoảng cách giữa các thành phần
        sidebarPanel.add(timePanel);
        mainPanel.add(sidebarPanel, BorderLayout.WEST);

        // Seat Selection Grid
        JPanel seatPanel = new JPanel(new GridLayout(6, 8, 5, 5));
        seatPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        seatPanel.setBackground(new Color(20, 30, 60));
        seats = new JButton[6][8];
        String[] rowLabels = {"A", "B", "C", "D", "E", "F"};
        
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                String seatPos = rowLabels[i] + (j + 1);
                JButton seatButton = new JButton(seatPos);
                seatButton.setFocusPainted(false);
                seatButton.setForeground(Color.WHITE);
                
                if (i >= 2 && i <= 3 && j >= 2 && j <= 5) {
                    seatButton.setBackground(Color.RED);
                } else {
                    seatButton.setBackground(Color.MAGENTA);
                }
                int row = i, col = j;
                seatButton.addActionListener(e -> toggleSeat(row, col));
                seats[i][j] = seatButton;
                seatPanel.add(seatButton);
            }
        }
        mainPanel.add(seatPanel, BorderLayout.CENTER);

        // Bottom Panel with Confirmation and Legend
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(10, 20, 40));

        btnConfirm = new JButton("🎟 Xác Nhận Đặt Vé");
        btnConfirm.setEnabled(false);
        btnConfirm.setBackground(new Color(50, 180, 80));
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.addActionListener(e -> {
            this.setVisible(false); // Ẩn giao diện đặt vé
            new PaymentFrame(this, userId, filmId, selectedSeats); // Mở giao diện thanh toán
        });
        bottomPanel.add(btnConfirm, BorderLayout.EAST);

        JPanel legendPanel = new JPanel(new FlowLayout());
        legendPanel.setBackground(new Color(10, 20, 40));

        addLegend(legendPanel, Color.MAGENTA, "Ghế thường");
        addLegend(legendPanel, Color.RED, "Ghế VIP");
        addLegend(legendPanel, Color.GRAY, "Đã đặt");
        addLegend(legendPanel, Color.GREEN, "Vùng trung tâm");
        bottomPanel.add(legendPanel, BorderLayout.CENTER);

        backgroundPanel.add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private static final String DB_URL = "jdbc:mysql://localhost:3306/vexemphim"; 
    private static final String USER = "root"; 
    private static final String PASSWORD = "";
    private String[] generateTimeOptions() {
        ArrayList<String> timeSlots = new ArrayList<>();
        String query = "SELECT DISTINCT thoi_gian_chieu FROM suat_chieu WHERE phim_id = ? ORDER BY thoi_gian_chieu";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, filmId);  // lấy theo ID phim hiện tại
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                timeSlots.add(rs.getString("thoi_gian_chieu"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return timeSlots.toArray(new String[0]);
    }

    private void updateBookedSeats() {
        // Giả sử dateChooser là biến thành viên đã được khởi tạo trước đó
        Date date = dateChooser.getDate(); // KHÔNG khai báo lại JDateChooser ở đây
        String selectedTime = (String) timeComboBox.getSelectedItem();

        if (date == null || selectedTime == null) return;

        // Chuyển định dạng ngày sang yyyy-MM-dd để so khớp với cơ sở dữ liệu
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String selectedDate = sdf.format(date);

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                try {
                    int scheduleId = getScheduleId(filmId, selectedDate, selectedTime);
                    if (scheduleId != -1) {
                        Set<String> bookedSeats = getBookedSeats(scheduleId);
                        SwingUtilities.invokeLater(() -> disableBookedSeats(bookedSeats));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                // Không cần xử lý thêm sau khi ghế đã được cập nhật
            }
        }.execute();
    }

    private int getScheduleId(int filmId, String date, String time) {
        String query = "SELECT id FROM suat_chieu WHERE phim_id = ? AND ngay_chieu = ? AND thoi_gian_chieu = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, filmId);
            stmt.setString(2, date);
            stmt.setString(3, time);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // Không tìm thấy
    }

    private Set<String> getBookedSeats(int scheduleId) {
        Set<String> bookedSeats = new HashSet<>();
        String sql = "SELECT so_ghe FROM ve_xem_phim WHERE id_lich_chieu = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, scheduleId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookedSeats.add(rs.getString("so_ghe"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookedSeats;
    }

    private void disableBookedSeats(Set<String> bookedSeats) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                String seat = (char) ('A' + i) + String.valueOf(j + 1);
                if (bookedSeats.contains(seat)) {
                    seats[i][j].setEnabled(false);
                    seats[i][j].setBackground(Color.GRAY);
                }
            }
        }
    }

    private void toggleSeat(int row, int col) {
        String seatPos = (char) ('A' + row) + String.valueOf(col + 1);
        if (selectedSeats.contains(seatPos)) {
            selectedSeats.remove(seatPos);
            seats[row][col].setBackground(Color.MAGENTA);
        } else {
            selectedSeats.add(seatPos);
            seats[row][col].setBackground(Color.GREEN);
        }
        btnConfirm.setEnabled(!selectedSeats.isEmpty());
    }

    private void addLegend(JPanel legendPanel, Color color, String label) {
        JPanel colorPanel = new JPanel();
        colorPanel.setBackground(color);
        colorPanel.setPreferredSize(new Dimension(20, 20));
        legendPanel.add(colorPanel);
        legendPanel.add(new JLabel(label));
    }

    private void closeWindow() {
        this.setVisible(false);
    }
}
