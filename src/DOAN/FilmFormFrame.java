// FilmFormFrame.java
package DOAN;

import MySQL.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;

public class FilmFormFrame extends JFrame {
    private JTextField txtTitle;
    private JTextArea txtDescription;
    private JTextField txtPoster;
    private JButton btnChooseImage, btnSave, btnBack;
    private File selectedFile;
    private AdminFilmsFrame.Film film;
    private AdminFilmsFrame parentFrame;

    public FilmFormFrame(AdminFilmsFrame parentFrame, String title, AdminFilmsFrame.Film film) {
        super(title);
        this.film = film;
        this.parentFrame = parentFrame;

        setSize(550, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.DARK_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtTitle = new JTextField(30);
        txtDescription = new JTextArea(5, 30);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);

        txtPoster = new JTextField(20);
        txtPoster.setEditable(false);

        btnChooseImage = new JButton("🖼 Chọn ảnh");
        btnChooseImage.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();
                txtPoster.setText(selectedFile.getAbsolutePath());
            }
        });

        btnSave = new JButton("💾 Lưu");
        btnSave.addActionListener(e -> saveFilm());

        btnBack = new JButton("⬅ Quay lại");
        btnBack.addActionListener(e -> {
            this.dispose();
            parentFrame.setVisible(true);
        });

        int y = 0;
        gbc.gridx = 0; gbc.gridy = y; panel.add(new JLabel("Tên phim:"), gbc);
        gbc.gridx = 1; gbc.gridy = y++; panel.add(txtTitle, gbc);

        gbc.gridx = 0; gbc.gridy = y; panel.add(new JLabel("Mô tả:"), gbc);
        gbc.gridx = 1; gbc.gridy = y++; panel.add(new JScrollPane(txtDescription), gbc);

        gbc.gridx = 0; gbc.gridy = y; panel.add(new JLabel("Poster:"), gbc);
        gbc.gridx = 1; gbc.gridy = y; panel.add(txtPoster, gbc);
        gbc.gridx = 2; gbc.gridy = y++; panel.add(btnChooseImage, gbc);

        gbc.gridx = 1; gbc.gridy = y++; panel.add(btnSave, gbc);
        gbc.gridx = 1; gbc.gridy = y; panel.add(btnBack, gbc);

        add(panel, BorderLayout.CENTER);

        if (film != null) {
            txtTitle.setText(film.title);
            txtDescription.setText(film.description);
            txtPoster.setText(film.poster);
        }
    }

    private void saveFilm() {
        String title = txtTitle.getText().trim();
        String desc = txtDescription.getText().trim();
        String poster = txtPoster.getText().trim();

        if (title.isEmpty() || desc.isEmpty() || poster.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (film == null) {
                String sql = "INSERT INTO lich_chieu_phim (ten_phim, mo_ta, poster) VALUES (?, ?, ?)";
                try (PreparedStatement st = conn.prepareStatement(sql)) {
                    st.setString(1, title);
                    st.setString(2, desc);
                    st.setString(3, poster);
                    st.executeUpdate();
                }
            } else {
                String sql = "UPDATE lich_chieu_phim SET ten_phim=?, mo_ta=?, poster=? WHERE id=?";
                try (PreparedStatement st = conn.prepareStatement(sql)) {
                    st.setString(1, title);
                    st.setString(2, desc);
                    st.setString(3, poster);
                    st.setInt(4, film.id);
                    st.executeUpdate();
                }
            }

            JOptionPane.showMessageDialog(this, "Lưu thành công!");
            this.dispose();
            parentFrame.setVisible(true);
            parentFrame.loadFilmPosters();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu phim: " + e.getMessage());
        }
    }
}
