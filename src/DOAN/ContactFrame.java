package DOAN;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ContactFrame extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/vexemphim";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private JTextArea txtFeedback;

    public ContactFrame() {
        setTitle("Liên hệ hỗ trợ");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new GridLayout(4, 1, 10, 10));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panelInfo.add(createLabel("📞 Hotline: 1900 1234"));
        panelInfo.add(createLabel("📧 Email: support@vexemphim.com"));
        panelInfo.add(createLabel("📍 Địa chỉ: 123 Đường ABC, TP.HCM"));

        add(panelInfo, BorderLayout.NORTH);

        // Khu vực nhập phản hồi
        JPanel panelFeedback = new JPanel(new BorderLayout());
        panelFeedback.setBorder(BorderFactory.createTitledBorder("Gửi phản hồi"));

        txtFeedback = new JTextArea(5, 30);
        panelFeedback.add(new JScrollPane(txtFeedback), BorderLayout.CENTER);

        add(panelFeedback, BorderLayout.CENTER);

        // Nút gửi và đóng
        JPanel panelButtons = new JPanel();
        JButton btnSend = new JButton("📩 Gửi");
        JButton btnClose = new JButton("✖ Đóng");

        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFeedbackToDatabase(txtFeedback.getText());
            }
        });

        btnClose.addActionListener(e -> dispose());

        panelButtons.add(btnSend);
        panelButtons.add(btnClose);
        add(panelButtons, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        return label;
    }

    private void saveFeedbackToDatabase(String feedback) {
        if (feedback.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập phản hồi trước khi gửi!");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "INSERT INTO phan_hoi (noi_dung) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, feedback);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Cảm ơn bạn đã gửi phản hồi!");
            txtFeedback.setText(""); // Xóa nội dung sau khi gửi
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi gửi phản hồi!");
        }
    }

    public static void main(String[] args) {
        new ContactFrame();
    }
}
