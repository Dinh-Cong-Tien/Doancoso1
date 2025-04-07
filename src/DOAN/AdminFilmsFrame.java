package DOAN;

import MySQL.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminFilmsFrame extends JFrame {
    private JPanel filmPanel;
    private JButton btnAdd;
    private JPanel detailsPanel;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JScrollPane scrollPane;

    public AdminFilmsFrame() {
        setTitle("🎬 Quản Lý Phim - ADMIN");
        setSize(1400, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

     // Panel danh sách phim
        JPanel listPanel = new JPanel(new BorderLayout());

        // ✅ Sử dụng GridLayout giống ViewFilmsFrame
        filmPanel = new JPanel(new GridLayout(0, 4, 20, 20));
        filmPanel.setBackground(new Color(40, 40, 40));

        // ✅ Không cần setPreferredSize nữa
        scrollPane = new JScrollPane(filmPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);


        btnAdd = createStyledButton("➕ Thêm phim mới", new Color(0, 180, 80), e -> openAddFilmDialog());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(30, 30, 30));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(btnAdd);

        listPanel.add(topPanel, BorderLayout.NORTH);
        listPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel chi tiết phim
        detailsPanel = new JPanel();

        mainPanel.add(listPanel, "FilmList");
        mainPanel.add(detailsPanel, "FilmDetails");

        add(mainPanel);
        loadFilmPosters();
        setVisible(true);
    }

    void loadFilmPosters() {
        filmPanel.removeAll();

        String query = "SELECT id, ten_phim, mo_ta, poster FROM lich_chieu_phim";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int filmId = rs.getInt("id");
                String filmTitle = rs.getString("ten_phim");
                String description = rs.getString("mo_ta");
                String posterPath = rs.getString("poster");

                ImageIcon icon = getPosterImage(posterPath, 300, 450);
                JLabel posterLabel = new JLabel(icon);
                posterLabel.setToolTipText(filmTitle);
                posterLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

                posterLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        showFilmDetails(filmId, filmTitle, description, posterPath);
                    }
                });

                filmPanel.add(posterLabel);
            }

            filmPanel.revalidate();
            filmPanel.repaint();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải danh sách phim: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showFilmDetails(int filmId, String title, String description, String imagePath) {
        detailsPanel.removeAll();
        detailsPanel.setLayout(new BorderLayout(20, 20));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        detailsPanel.setBackground(new Color(40, 40, 40));

        JLabel posterLabel = new JLabel(getPosterImage(imagePath, 450, 650));
        JPanel posterPanel = new JPanel();
        posterPanel.setBackground(new Color(40, 40, 40));
        posterPanel.add(posterLabel);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(50, 50, 50));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel("🎬 " + title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitle.setForeground(new Color(255, 204, 100));

        JTextArea lblDescription = new JTextArea(description, 6, 30);
        lblDescription.setWrapStyleWord(true);
        lblDescription.setLineWrap(true);
        lblDescription.setEditable(false);
        lblDescription.setOpaque(false);
        lblDescription.setForeground(Color.WHITE);
        lblDescription.setFont(new Font("SansSerif", Font.PLAIN, 16));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(new Color(50, 50, 50));

        JButton btnEdit = createStyledButton("✏️ Sửa phim", new Color(80, 150, 250), e -> {
            openEditFilmDialog(filmId, title, description, imagePath);
        });

        JButton btnDelete = createStyledButton("🗑 Xoá phim", new Color(200, 60, 60), e -> deleteFilm(filmId));

        JButton btnBack = createStyledButton("⬅ Quay lại", new Color(150, 150, 150), e -> cardLayout.show(mainPanel, "FilmList"));

        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnBack);

        infoPanel.add(lblTitle);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(lblDescription);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(buttonPanel);

        detailsPanel.add(posterPanel, BorderLayout.WEST);
        detailsPanel.add(infoPanel, BorderLayout.CENTER);

        cardLayout.show(mainPanel, "FilmDetails");
    }

    private void openAddFilmDialog() {
        this.setVisible(false);
        FilmFormFrame form = new FilmFormFrame(this, "➕ Thêm Phim Mới", null);
        form.setVisible(true);
    }

    private void openEditFilmDialog(int filmId, String title, String description, String posterPath) {
        this.setVisible(false);
        FilmFormFrame form = new FilmFormFrame(this, "✏️ Sửa Phim", new Film(filmId, title, description, posterPath));
        form.setVisible(true);
    }

    private void deleteFilm(int filmId) {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xoá phim này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                try (PreparedStatement st = conn.prepareStatement("DELETE FROM suat_chieu WHERE phim_id = ?")) {
                    st.setInt(1, filmId);
                    st.executeUpdate();
                }

                try (PreparedStatement st = conn.prepareStatement("DELETE FROM lich_chieu_phim WHERE id = ?")) {
                    st.setInt(1, filmId);
                    st.executeUpdate();
                }

                JOptionPane.showMessageDialog(this, "Đã xoá phim thành công!");
                loadFilmPosters();
                cardLayout.show(mainPanel, "FilmList");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xoá phim: " + e.getMessage());
            }
        }
    }

    private ImageIcon getPosterImage(String path, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            return new ImageIcon();
        }
    }

    private JButton createStyledButton(String text, Color color, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.addActionListener(action);
        return button;
    }

    static class Film {
        int id;
        String title;
        String description;
        String poster;

        Film(int id, String title, String desc, String poster) {
            this.id = id;
            this.title = title;
            this.description = desc;
            this.poster = poster;
        }
    }
}
