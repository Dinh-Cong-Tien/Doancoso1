package DOAN;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.util.HashMap;

import MySQL.*;

public class ViewFilmsFrame extends JFrame {
    private JPanel filmPanel, detailsPanel;
    private JScrollPane scrollPane;
    private JButton btnBack;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private int userId;

    public ViewFilmsFrame(int userId) {
        this.userId = userId;
        setTitle("Danh Sách Phim");
        setSize(1500, 1000);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);

        filmPanel = new JPanel(new GridLayout(0, 4, 20, 20));
        scrollPane = new JScrollPane(filmPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, "FilmList");

        detailsPanel = new JPanel(new BorderLayout(20, 20));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(detailsPanel, "FilmDetails");

        loadFilmPosters();
        cardLayout.show(mainPanel, "FilmList");
        setVisible(true);
    }

    private void loadFilmPosters() {
        filmPanel.removeAll();

        // Truy vấn danh sách phim có suất chiếu 
        String query = "SELECT DISTINCT lcp.id AS film_id, lcp.ten_phim, lcp.mo_ta, lcp.poster " +
                       "FROM lich_chieu_phim lcp " +
                       "JOIN suat_chieu sc ON lcp.id = sc.phim_id " +
                       "WHERE sc.ve_con_lai > 0";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                int filmId = rs.getInt("film_id");
                String filmTitle = rs.getString("ten_phim");
                String description = rs.getString("mo_ta");
                String posterPath = validatePosterPath(rs.getString("poster"));

                JLabel filmLabel = new JLabel(getPosterImage(posterPath, 350, 450));
                filmLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

                filmLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        showFilmDetails(filmId, filmTitle, description, posterPath);
                    }
                });

                filmPanel.add(filmLabel);
            }

            filmPanel.revalidate();
            filmPanel.repaint();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String validatePosterPath(String posterPath) {
        if (posterPath == null || posterPath.trim().isEmpty() || !new File(posterPath).exists()) {
            return "default_poster.jpg";
        }
        return posterPath;
    }

    private ImageIcon getPosterImage(String imagePath, int width, int height) {
        return new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    private void showFilmDetails(int filmId, String title, String description, String imagePath) {
        detailsPanel.removeAll();
        detailsPanel.setLayout(new BorderLayout(20, 20));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        detailsPanel.setBackground(new Color(40, 40, 40));

        // Ảnh Poster
        JLabel posterLabel = new JLabel(getPosterImage(imagePath, 350, 500));
        JPanel posterPanel = new JPanel();
        posterPanel.setBackground(new Color(40, 40, 40));
        posterPanel.add(posterLabel);

        // Panel chứa thông tin phim
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(50, 50, 50));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitle.setForeground(new Color(255, 180, 80));

        JTextArea lblDescription = new JTextArea(description, 6, 30);
        lblDescription.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblDescription.setWrapStyleWord(true);
        lblDescription.setLineWrap(true);
        lblDescription.setOpaque(false);
        lblDescription.setEditable(false);
        lblDescription.setForeground(Color.WHITE);

     // Nút đặt vé (mặc định lấy suất chiếu đầu tiên)
        JButton btnBook = createStyledButton("🎟 Đặt vé ngay", new Color(0, 180, 80), e -> {
            int suatChieuId = getFirstAvailableShowtime(filmId);
            if (suatChieuId != -1) {
                openSeatSelection(suatChieuId, title);
            } else {
                JOptionPane.showMessageDialog(this, "Hiện tại chưa có suất chiếu khả dụng!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        btnBook.setFont(new Font("SansSerif", Font.BOLD, 18));

        // Nút quay lại
        JButton btnBack = createStyledButton("⬅ Quay lại", new Color(200, 50, 50), e -> cardLayout.show(mainPanel, "FilmList"));
        btnBack.setFont(new Font("SansSerif", Font.BOLD, 16));

        infoPanel.add(lblTitle);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(lblDescription);
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(btnBook);
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(btnBack);

        detailsPanel.add(posterPanel, BorderLayout.WEST);
        detailsPanel.add(infoPanel, BorderLayout.CENTER);

        cardLayout.show(mainPanel, "FilmDetails");
    }
    
    private int getFirstAvailableShowtime(int filmId) {
        String query = "SELECT id FROM suat_chieu WHERE phim_id = ? AND ve_con_lai > 0 ORDER BY ngay_khoi_chieu LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, filmId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy suất chiếu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return -1;
    }


    // Hàm hỗ trợ tạo JLabel với kiểu dáng đẹp hơn
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        label.setForeground(Color.WHITE);
        return label;
    }

    // Hàm mở giao diện chọn ghế
    private void openSeatSelection(int filmId, String filmTitle) {
        this.setVisible(false);
        SeatSelectionFrame seatFrame = new SeatSelectionFrame(filmId, filmTitle, userId);
        seatFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                setVisible(true);
            }
        });
    }

    private JButton createStyledButton(String text, Color bgColor, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.addActionListener(actionListener);
        return button;
    }
}