//package QLTKCN;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//public class GiaoDienLienHe extends JFrame {
//
//    public GiaoDienLienHe() {
//        // Thiết lập JFrame
//        setTitle("Liên Hệ");
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setSize(600, 400); // Kích thước cửa sổ
//        setLayout(new BorderLayout());
//
//        // Tạo JPanel để chứa các thành phần
//        JPanel panel = new JPanel();
//        panel.setLayout(new BorderLayout());
//        panel.setBackground(new Color(245, 245, 245)); // Màu nền nhẹ nhàng
//
//        // Tạo một label cho tiêu đề
//        JLabel titleLabel = new JLabel("Thông Tin Liên Hệ", JLabel.CENTER);
//        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
//        titleLabel.setForeground(new Color(34, 49, 63)); // Màu chữ đẹp và dễ nhìn
//        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Khoảng cách trên dưới
//        panel.add(titleLabel, BorderLayout.NORTH);
//
//        // Tạo thông tin liên hệ dưới dạng JTextArea
//        JTextArea textArea = new JTextArea();
//        textArea.setEditable(false);
//        textArea.setText("Email: tien.dinhcong2006@gmail.com\n"
//                         + "Điện thoại: 0123456789\n"
//                         + "Địa chỉ: Hoà Phước, Hoà Vang, Thành phố Đà Nẵng\n");
//        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
//        textArea.setLineWrap(true);
//        textArea.setWrapStyleWord(true);
//        textArea.setBackground(new Color(255, 255, 255)); // Màu nền trắng cho text area
//        textArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1)); // Đường viền mỏng
//        textArea.setCaretColor(new Color(34, 49, 63)); // Màu con trỏ khi người dùng chỉnh sửa (nếu có)
//        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
//
//        // Tạo JPanel cho các nút
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Các nút căn giữa
//        buttonPanel.setBackground(new Color(245, 245, 245)); // Giữ màu nền giống panel chính
//
//        // Tạo nút "Gửi Email"
//        JButton sendEmailButton = new JButton("Gửi Email");
//        sendEmailButton.setFont(new Font("Arial", Font.BOLD, 16));
//        sendEmailButton.setBackground(new Color(34, 139, 34)); // Màu xanh lá nổi bật
//        sendEmailButton.setForeground(Color.WHITE);
//        sendEmailButton.setFocusPainted(false); // Loại bỏ viền khi chọn nút
//        sendEmailButton.setPreferredSize(new Dimension(150, 40));
//
//        // Thêm sự kiện khi nhấn nút "Gửi Email"
//        sendEmailButton.addActionListener(e -> {
//            JOptionPane.showMessageDialog(this, "Email đã được gửi thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//        });
//
//        // Tạo nút "Quay lại"
//        JButton backButton = new JButton("Quay lại");
//        backButton.setFont(new Font("Arial", Font.BOLD, 14));
//        backButton.setBackground(new Color(0, 123, 255)); // Màu xanh cho nút
//        backButton.setForeground(Color.WHITE);
//        backButton.setPreferredSize(new Dimension(100, 40));
//        backButton.setFocusPainted(false); // Loại bỏ viền khi chọn nút
//
//        // Sự kiện cho nút "Quay lại"
//        backButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // Ví dụ quay lại trang chủ (TrangChuFrame)
//                new TrangChu(); // Giả sử bạn có một lớp TrangChuFrame để hiển thị trang chủ
//                dispose(); // Đóng cửa sổ hiện tại
//            }
//        });
//
//        // Thêm các nút vào panel
//        buttonPanel.add(sendEmailButton); // Nút "Gửi Email"
//        buttonPanel.add(Box.createHorizontalStrut(20)); // Thêm khoảng trống giữa nút "Gửi Email" và "Quay lại"
//        buttonPanel.add(backButton); // Nút "Quay lại"
//
//        // Thêm buttonPanel vào panel chính
//        panel.add(buttonPanel, BorderLayout.SOUTH); // Thêm vào phần dưới của panel chính
//
//        // Thêm panel vào JFrame
//        add(panel, BorderLayout.CENTER);
//
//        // Hiển thị JFrame
//        setVisible(true);
//    }
//}
//
