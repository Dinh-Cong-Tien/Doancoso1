package DOAN;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

public class SeatSelectionFrame extends JFrame {
    private JComboBox<String> timeCombo;
    private JDateChooser dateChooser;
    private JPanel seatPanel;
    private Map<JButton, String> seatMap = new HashMap<>();

    private final int ROWS = 10;
    private final int COLS = 12;

    private int phimId;
    private String phimTitle;
    private int userId;

    public SeatSelectionFrame(int phimId, String phimTitle, int userId) {
        this.phimId = phimId;
        this.phimTitle = phimTitle;
        this.userId = userId;

        setTitle("Đặt vé xem phim");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Top Panel
        JPanel topPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel filmLabel = new JLabel(phimTitle);
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        timeCombo = new JComboBox<>(new String[] {"17:00", "19:30", "21:00"});

        topPanel.add(new JLabel("Phim:"));
        topPanel.add(filmLabel);
        topPanel.add(new JLabel("Ngày chiếu:"));
        topPanel.add(dateChooser);
        topPanel.add(new JLabel("Giờ chiếu:"));
        topPanel.add(timeCombo);

        add(topPanel, BorderLayout.NORTH);

        // Seat Panel
        seatPanel = new JPanel(new GridLayout(ROWS, COLS, 5, 5));
        generateSeats();
        add(seatPanel, BorderLayout.CENTER);

        // Legend
        JPanel legendPanel = new JPanel();
        legendPanel.add(createLegend("Ghế thường", Color.GREEN));
        legendPanel.add(createLegend("Ghế VIP", Color.BLUE));
        legendPanel.add(createLegend("Ghế đã đặt", Color.RED));
        legendPanel.add(createLegend("Ghế đang chọn", Color.YELLOW));
        add(legendPanel, BorderLayout.SOUTH);

        dateChooser.addPropertyChangeListener("date", e -> loadBookedSeats());
        timeCombo.addActionListener(e -> loadBookedSeats());

        loadBookedSeats();
        setVisible(true);
    }

    private JPanel createLegend(String label, Color color) {
        JPanel panel = new JPanel();
        JButton btn = new JButton();
        btn.setBackground(color);
        btn.setEnabled(false);
        btn.setPreferredSize(new Dimension(20, 20));
        panel.add(btn);
        panel.add(new JLabel(label));
        return panel;
    }

    private void generateSeats() {
        seatPanel.removeAll();
        seatMap.clear();
        char rowChar = 'A';
        for (int row = 0; row < ROWS; row++) {
            for (int col = 1; col <= COLS; col++) {
                String seatCode = rowChar + String.valueOf(col);
                JButton seatBtn = new JButton(seatCode);
                seatBtn.setPreferredSize(new Dimension(40, 40));
                seatBtn.setBackground(isVIP(row, col) ? Color.BLUE : Color.GREEN);
                seatBtn.setOpaque(true);
                seatBtn.setBorderPainted(false);
                seatBtn.addActionListener(e -> handleSeatClick(seatBtn));
                seatMap.put(seatBtn, seatCode);
                seatPanel.add(seatBtn);
            }
            rowChar++;
        }
        seatPanel.revalidate();
        seatPanel.repaint();
    }

    private boolean isVIP(int row, int col) {
        return row >= 6;
    }

    private void handleSeatClick(JButton seatBtn) {
        Color bg = seatBtn.getBackground();
        if (bg.equals(Color.RED)) return;
        if (bg.equals(Color.YELLOW)) {
            seatBtn.setBackground(isVIP(getRow(seatBtn), getCol(seatBtn)) ? Color.BLUE : Color.GREEN);
        } else {
            seatBtn.setBackground(Color.YELLOW);
        }
    }

    private int getRow(JButton btn) {
        String seat = seatMap.get(btn);
        return seat.charAt(0) - 'A';
    }

    private int getCol(JButton btn) {
        String seat = seatMap.get(btn);
        return Integer.parseInt(seat.substring(1));
    }

    private void loadBookedSeats() {
        for (JButton btn : seatMap.keySet()) {
            btn.setEnabled(true);
            int row = getRow(btn);
            int col = getCol(btn);
            btn.setBackground(isVIP(row, col) ? Color.BLUE : Color.GREEN);
        }

        java.util.Date selectedDate = dateChooser.getDate();
        String selectedTime = (String) timeCombo.getSelectedItem();

        if (selectedDate == null) return;
        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/vexemphim", "root", "")) {
            String sql = "SELECT id FROM suat_chieu WHERE phim_id = ? AND ngay_khoi_chieu = ? AND thoi_gian_chieu = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, phimId);
            stmt.setString(2, dateStr);
            stmt.setString(3, selectedTime);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int suatChieuId = rs.getInt("id");

                String sqlGhe = "SELECT ghe_ngoi FROM ve_da_dat WHERE suat_chieu_id = ?";
                PreparedStatement stmtGhe = conn.prepareStatement(sqlGhe);
                stmtGhe.setInt(1, suatChieuId);
                ResultSet rsGhe = stmtGhe.executeQuery();
                while (rsGhe.next()) {
                    String seatCode = rsGhe.getString("ghe_ngoi");
                    for (Map.Entry<JButton, String> entry : seatMap.entrySet()) {
                        if (entry.getValue().equals(seatCode)) {
                            entry.getKey().setBackground(Color.RED);
                            entry.getKey().setEnabled(false);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
