//package QLTKCN;
//
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.FlowLayout;
//import java.awt.Font;
//import java.awt.GridLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//import javax.swing.BorderFactory;
//import javax.swing.Box;
//import javax.swing.BoxLayout;
//import javax.swing.ImageIcon;
//import javax.swing.JButton;
//import javax.swing.JDialog;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTextArea;
//import javax.swing.JTextField;
//
//class PhatTrienPhanMemFrame extends JFrame {
//
//    public PhatTrienPhanMemFrame() {
//        // Thiết lập JFrame
//        setTitle("Phát triển phần mềm");
//        setSize(600, 500);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setLayout(new BorderLayout());
//
//        // Panel chính
//        JPanel panel = new JPanel();
//        panel.setLayout(new BorderLayout());
//        panel.setBackground(new Color(245, 245, 245)); // Màu nền nhẹ nhàng
//
//        // Tiêu đề
//        JLabel titleLabel = new JLabel("Dịch Vụ Phát Triển Phần Mềm", JLabel.CENTER);
//        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
//        titleLabel.setForeground(new Color(34, 49, 63));
//        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
//        panel.add(titleLabel, BorderLayout.NORTH);
//
//        // Mô tả dịch vụ
//        JTextArea descriptionArea = new JTextArea();
//        descriptionArea.setEditable(false);
//        descriptionArea.setText("Chúng tôi cung cấp các dịch vụ phát triển phần mềm chuyên nghiệp, bao gồm:\n"
//                + "- Phát triển phần mềm theo yêu cầu\n"
//                + "- Phát triển ứng dụng di động (iOS, Android)\n"
//                + "- Phát triển hệ thống web và ứng dụng doanh nghiệp\n"
//                + "- Tư vấn và triển khai giải pháp phần mềm\n"
//                + "Liên hệ với chúng tôi để nhận tư vấn miễn phí và báo giá chi tiết.");
//        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 16));
//        descriptionArea.setLineWrap(true);
//        descriptionArea.setWrapStyleWord(true);
//        descriptionArea.setBackground(new Color(255, 255, 255));
//        descriptionArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
//        panel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);
//
//        // Thêm hình ảnh logo
//        ImageIcon logoIcon = new ImageIcon("software_logo.png"); // Thay "software_logo.png" bằng logo thực tế
//        JLabel logoLabel = new JLabel(logoIcon);
//        panel.add(logoLabel, BorderLayout.WEST);
//
//        // Thông tin liên hệ
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
//                + "Điện thoại: 0123456789\n"
//                + "Địa chỉ: Hoà Phước, Hoà Vang, Thành phố Đà Nẵng\n"
//                + "Website: www.dctEntertainment.com");
//        contactInfoArea.setFont(new Font("Arial", Font.PLAIN, 14));
//        contactInfoArea.setLineWrap(true);
//        contactInfoArea.setWrapStyleWord(true);
//        contactInfoArea.setBackground(new Color(255, 255, 255));
//        contactInfoArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
//
//        contactPanel.add(contactTitleLabel);
//        contactPanel.add(Box.createVerticalStrut(10));
//        contactPanel.add(new JScrollPane(contactInfoArea));
//
//        // Panel cho các nút (nằm ngang)
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // FlowLayout cho các nút nằm ngang
//        buttonPanel.setBackground(new Color(245, 245, 245));
//        
//        // Nút gửi yêu cầu tư vấn
//        JButton contactButton = new JButton("Gửi yêu cầu tư vấn");
//        contactButton.setFont(new Font("Arial", Font.BOLD, 16));
//        contactButton.setBackground(new Color(0, 123, 255)); // Màu xanh lam
//        contactButton.setForeground(Color.WHITE);
//        contactButton.setPreferredSize(new Dimension(150, 40));
//        contactButton.setFocusPainted(false);
//
//        // Thêm sự kiện cho nút gửi yêu cầu tư vấn
//        contactButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                JOptionPane.showMessageDialog(PhatTrienPhanMemFrame.this, "Chúng tôi sẽ liên hệ với bạn ngay!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//            }
//        });
//
//        // Nút Đăng ký dùng thử
//        JButton trialButton = new JButton("Đăng ký dùng thử");
//        trialButton.setFont(new Font("Arial", Font.BOLD, 16));
//        trialButton.setBackground(new Color(40, 167, 69)); // Màu xanh lá
//        trialButton.setForeground(Color.WHITE);
//        trialButton.setPreferredSize(new Dimension(150, 40));
//        trialButton.setFocusPainted(false);
//
//        // Thêm sự kiện cho nút Đăng ký dùng thử
//        trialButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                openTrialRegistrationDialog();
//            }
//        });
//
//        // Nút Quay lại trang chủ
//        JButton backButton = new JButton("Quay lại trang chủ");
//        backButton.setFont(new Font("Arial", Font.BOLD, 16));
//        backButton.setBackground(new Color(255, 87, 34)); // Màu cam
//        backButton.setForeground(Color.WHITE);
//        backButton.setPreferredSize(new Dimension(150, 40));
//        backButton.setFocusPainted(false);
//
//        // Thêm sự kiện cho nút quay lại trang chủ
//        backButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // Đóng cửa sổ hiện tại và quay lại trang chủ (có thể là một JFrame khác)
//                JOptionPane.showMessageDialog(PhatTrienPhanMemFrame.this, "Đang quay lại trang chủ!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//                dispose(); // Đóng cửa sổ hiện tại
//                new TrangChu(); // Khởi tạo trang chủ (TrangChuFrame là lớp JFrame đại diện cho trang chủ)
//            }
//        });
//
//        // Thêm các nút vào buttonPanel
//        buttonPanel.add(contactButton);
//        buttonPanel.add(trialButton);
//        buttonPanel.add(backButton);
//
//        // Thêm phần buttonPanel vào contactPanel dưới cùng
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
//    // Phương thức mở cửa sổ đăng ký dùng thử
//    private void openTrialRegistrationDialog() {
//        JDialog trialDialog = new JDialog(this, "Đăng Ký Dùng Thử", true);
//        trialDialog.setSize(400, 350);
//        trialDialog.setLayout(new BorderLayout());
//
//        JPanel formPanel = new JPanel();
//        formPanel.setLayout(new GridLayout(6, 2, 10, 10));
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
//        formPanel.add(new JLabel("Thời gian dùng thử (ngày):"));
//        JTextField timeField = new JTextField();
//        formPanel.add(timeField);
//
//        trialDialog.add(formPanel, BorderLayout.CENTER);
//
//        // Nút gửi đăng ký
//        JButton submitButton = new JButton("Gửi Đăng Ký");
//        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
//        submitButton.setBackground(new Color(0, 123, 255)); // Màu xanh lam
//        submitButton.setForeground(Color.WHITE);
//        submitButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String email = emailField.getText();
//                String phone = phoneField.getText();
//                String timeText = timeField.getText();
//
//                boolean isValidEmail = validateEmail(email);
//                boolean isValidPhone = validatePhone(phone);
//                boolean isValidTime = validateTime(timeText);
//
//                if (!isValidEmail) {
//                    JOptionPane.showMessageDialog(trialDialog, "Email không hợp lệ!", "Thông báo", JOptionPane.WARNING_MESSAGE);
//                    return;
//                }
//
//                if (!isValidPhone) {
//                    JOptionPane.showMessageDialog(trialDialog, "Số điện thoại không hợp lệ!", "Thông báo", JOptionPane.WARNING_MESSAGE);
//                    return;
//                }
//
//                if (!isValidTime) {
//                    JOptionPane.showMessageDialog(trialDialog, "Vui lòng nhập thời gian dùng thử hợp lệ (1-30 ngày).", "Thông báo", JOptionPane.WARNING_MESSAGE);
//                    return;
//                }
//
//                JOptionPane.showMessageDialog(trialDialog, "Đăng ký dùng thử thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//                trialDialog.dispose();
//            }
//        });
//
//        trialDialog.add(submitButton, BorderLayout.SOUTH);
//
//        // Hiển thị cửa sổ đăng ký dùng thử
//        trialDialog.setLocationRelativeTo(this);
//        trialDialog.setVisible(true);
//    }
//
//    // Kiểm tra định dạng email hợp lệ
//    private boolean validateEmail(String email) {
//        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
//        return email.matches(regex);
//    }
//
//    // Kiểm tra số điện thoại hợp lệ
//    private boolean validatePhone(String phone) {
//        String regex = "^[0-9]{10}$"; // Số điện thoại 10 chữ số
//        return phone.matches(regex);
//    }
//
//    // Kiểm tra thời gian dùng thử hợp lệ
//    private boolean validateTime(String timeText) {
//        try {
//            int time = Integer.parseInt(timeText);
//            return time >= 1 && time <= 30;
//        } catch (NumberFormatException e) {
//            return false;
//        }
//    }
//}
