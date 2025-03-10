//package QLTKCN;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
//
//class TuVanCNTTFrame extends JFrame {
//
//    public TuVanCNTTFrame() {
//        // Thiết lập JFrame
//        setTitle("Tư vấn giải pháp CNTT");
//        setSize(600, 550); // Tăng kích thước cửa sổ
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setLayout(new BorderLayout());
//
//        // Tạo panel chính để chứa các thành phần
//        JPanel panel = new JPanel();
//        panel.setLayout(new BorderLayout());
//        panel.setBackground(new Color(245, 245, 245)); // Màu nền nhẹ nhàng
//
//        // Tiêu đề
//        JLabel titleLabel = new JLabel("Dịch Vụ Tư Vấn Giải Pháp CNTT", JLabel.CENTER);
//        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
//        titleLabel.setForeground(new Color(34, 49, 63));
//        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
//        panel.add(titleLabel, BorderLayout.NORTH);
//
//        // Thêm logo hoặc hình ảnh vào giao diện (ví dụ là một hình logo)
//        ImageIcon logoIcon = new ImageIcon("logo.png"); // Thay "logo.png" bằng đường dẫn logo của bạn
//        JLabel logoLabel = new JLabel(logoIcon);
//        panel.add(logoLabel, BorderLayout.WEST);
//
//        // Mô tả chi tiết dịch vụ
//        JTextArea descriptionArea = new JTextArea();
//        descriptionArea.setEditable(false);
//        descriptionArea.setText("Chúng tôi cung cấp các giải pháp công nghệ thông tin tối ưu, giúp doanh nghiệp tối đa hóa hiệu suất công việc.\n"
//                                + "Các dịch vụ của chúng tôi bao gồm:\n"
//                                + "- Tư vấn chuyển đổi số\n"
//                                + "- Thiết kế và triển khai hệ thống CNTT\n"
//                                + "- Tư vấn bảo mật và an ninh mạng\n"
//                                + "- Quản lý hệ thống và hỗ trợ kỹ thuật\n"
//                                + "Liên hệ với chúng tôi để được tư vấn miễn phí và báo giá chi tiết.");
//        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 16));
//        descriptionArea.setLineWrap(true);
//        descriptionArea.setWrapStyleWord(true);
//        descriptionArea.setBackground(new Color(255, 255, 255));
//        descriptionArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
//        panel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);
//
//        // Thêm phần thông tin liên hệ
//        JPanel contactPanel = new JPanel();
//        contactPanel.setLayout(new BoxLayout(contactPanel, BoxLayout.Y_AXIS));
//        contactPanel.setBackground(new Color(245, 245, 245));
//
//        JLabel contactTitleLabel = new JLabel("Liên Hệ Với Chúng Tôi");
//        contactTitleLabel.setFont(new Font("Arial", Font.BOLD, 18));
//        contactTitleLabel.setForeground(new Color(34, 49, 63));
//
//        JTextArea contactInfoArea = new JTextArea();
//        contactInfoArea.setEditable(false);
//        contactInfoArea.setText("Email: tien.dinhcong2006@gmail.com\n"
//                                + "Điện thoại: 0123456789\n"
//                                + "Địa chỉ: Hoà Phước, Hoà Vang, Thành phố Đà Nẵng\n"
//                                + "Website: www.dctEntertainment.com");
//        contactInfoArea.setFont(new Font("Arial", Font.PLAIN, 14));
//        contactInfoArea.setLineWrap(true);
//        contactInfoArea.setWrapStyleWord(true);
//        contactInfoArea.setBackground(new Color(255, 255, 255));
//        contactInfoArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
//
//        contactPanel.add(contactTitleLabel);
//        contactPanel.add(Box.createVerticalStrut(10)); // Khoảng cách giữa tiêu đề và thông tin
//        contactPanel.add(new JScrollPane(contactInfoArea));
//
//        // Tạo một JPanel cho các nút liên hệ và đăng ký tư vấn
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Layout theo hàng ngang
//        buttonPanel.setBackground(new Color(245, 245, 245)); // Màu nền giống panel chính
//
//        // Nút "Gửi yêu cầu tư vấn"
//        JButton contactButton = new JButton("Gửi yêu cầu tư vấn");
//        contactButton.setFont(new Font("Arial", Font.BOLD, 16));
//        contactButton.setBackground(new Color(0, 123, 255)); // Màu xanh lam
//        contactButton.setForeground(Color.WHITE);
//        contactButton.setPreferredSize(new Dimension(150, 40));
//        contactButton.setFocusPainted(false);
//
//        // Thêm sự kiện cho nút "Gửi yêu cầu tư vấn"
//        contactButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                JOptionPane.showMessageDialog(TuVanCNTTFrame.this, "Chúng tôi sẽ liên hệ với bạn ngay!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//            }
//        });
//
//        // Nút "Đăng ký tư vấn"
//        JButton registerButton = new JButton("Đăng ký tư vấn");
//        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
//        registerButton.setBackground(new Color(40, 167, 69)); // Màu xanh lá
//        registerButton.setForeground(Color.WHITE);
//        registerButton.setPreferredSize(new Dimension(150, 40));
//        registerButton.setFocusPainted(false);
//
//        // Thêm sự kiện cho nút đăng ký tư vấn
//        registerButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // Mở cửa sổ đăng ký tư vấn
//                openRegistrationDialog();
//            }
//        });
//
//        // Nút "Quay lại"
//        JButton backButton = new JButton("Quay lại");
//        backButton.setFont(new Font("Arial", Font.BOLD, 16));
//        backButton.setBackground(new Color(255, 87, 34)); // Màu cam cho nút
//        backButton.setForeground(Color.WHITE);
//        backButton.setPreferredSize(new Dimension(150, 40));
//        backButton.setFocusPainted(false);
//
//        // Thêm sự kiện cho nút "Quay lại"
//        backButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // Ví dụ quay lại trang chủ (TrangChuFrame)
//                new TrangChu(); // Giả sử bạn có một lớp TrangChuFrame để hiển thị trang chủ
//                dispose(); // Đóng cửa sổ hiện tại
//            }
//        });
//
//        // Thêm các nút vào buttonPanel
//        buttonPanel.add(contactButton); // Nút "Gửi yêu cầu tư vấn"
//        buttonPanel.add(registerButton); // Nút "Đăng ký tư vấn"
//        buttonPanel.add(backButton); // Nút "Quay lại"
//
//        // Thêm buttonPanel vào contactPanel
//        contactPanel.add(Box.createVerticalStrut(10)); // Khoảng cách giữa các nút
//        contactPanel.add(buttonPanel);
//
//        // Thêm phần thông tin liên hệ vào panel chính
//        panel.add(contactPanel, BorderLayout.SOUTH);
//
//        // Thêm panel vào JFrame
//        add(panel, BorderLayout.CENTER);
//
//        // Hiển thị JFrame
//        setVisible(true);
//    }
//
//    // Phương thức mở cửa sổ đăng ký tư vấn
//    private void openRegistrationDialog() {
//        JDialog registrationDialog = new JDialog(this, "Đăng Ký Tư Vấn", true);
//        registrationDialog.setSize(400, 300);
//        registrationDialog.setLayout(new BorderLayout());
//
//        JPanel formPanel = new JPanel();
//        formPanel.setLayout(new GridLayout(4, 2, 10, 10));
//
//        formPanel.add(new JLabel("Họ và Tên:"));
//        JTextField nameField = new JTextField();
//        formPanel.add(nameField);
//
//        formPanel.add(new JLabel("Email:"));
//        JTextField emailField = new JTextField();
//        formPanel.add(emailField);
//
//        formPanel.add(new JLabel("Số điện thoại:"));
//        JTextField phoneField = new JTextField();
//        formPanel.add(phoneField);
//
//        formPanel.add(new JLabel("Yêu cầu tư vấn:"));
//        JTextArea requestArea = new JTextArea();
//        formPanel.add(new JScrollPane(requestArea));
//
//        registrationDialog.add(formPanel, BorderLayout.CENTER);
//
//        // Nút gửi đăng ký
//        JButton submitButton = new JButton("Gửi Đăng Ký");
//        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
//        submitButton.setBackground(new Color(0, 123, 255)); // Màu xanh lam
//        submitButton.setForeground(Color.WHITE);
//        submitButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // Kiểm tra xem các trường thông tin có trống không
//                if (nameField.getText().isEmpty() || emailField.getText().isEmpty() || phoneField.getText().isEmpty()) {
//                    JOptionPane.showMessageDialog(registrationDialog, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//                } else {
//                    JOptionPane.showMessageDialog(registrationDialog, "Đăng ký tư vấn thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//                    registrationDialog.dispose();
//                }
//            }
//        });
//
//        registrationDialog.add(submitButton, BorderLayout.SOUTH);
//
//        // Hiển thị cửa sổ đăng ký
//        registrationDialog.setLocationRelativeTo(this);
//        registrationDialog.setVisible(true);
//    }
//}
