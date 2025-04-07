package DOAN;

import java.awt.*;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.*;

class HomeFrame extends JFrame {
    private JPanel imagePanel, hotMoviesPanel,topPanel;
    private JLabel imageLabel, cinemaTitle, userIcon, usernameLabel;
    private JMenu menuTaiKhoan, menuBar;
    private JPopupMenu userMenu;
    private JMenuItem menuChinhSua, menuDangXuat, menuDangNhap, menuDangKy, menuDangNhapAdmin, menuTrangChu, menuLichChieu, menuDatVe, menuKhuyenMai, menuLienHe;
    private String[] imagePaths = {
        "C:/Users/Admin/eclipse-workspace/DOANCOSO1/bin/picture DOAN/poster4.jpg",
        "C:/Users/Admin/eclipse-workspace/DOANCOSO1/bin/picture DOAN/poster5.jpg",
        "C:/Users/Admin/eclipse-workspace/DOANCOSO1/bin/picture DOAN/poster6.jpg",
        "C:/Users/Admin/eclipse-workspace/DOANCOSO1/bin/picture DOAN/poster7.jpg"
    };
    private String[] hotMoviePaths = {
        "C:/Users/Admin/eclipse-workspace/DOANCOSO1/bin/picture DOAN/poster1.jpg",
        "C:/Users/Admin/eclipse-workspace/DOANCOSO1/bin/picture DOAN/poster2.jpg",
        "C:/Users/Admin/eclipse-workspace/DOANCOSO1/bin/picture DOAN/poster3.jpg",
        "C:/Users/Admin/eclipse-workspace/DOANCOSO1/bin/picture DOAN/poster8.jpg",
        "C:/Users/Admin/eclipse-workspace/DOANCOSO1/bin/picture DOAN/poster9.jpg"
    };
    private int currentIndex = 0;
    private Timer timer;
    
    public HomeFrame() {
        setTitle("StarLight Cinema");
        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(40, 40, 40));
        menuBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JMenu menuTrangChu = createMenu("Trang Chủ", () -> {
            dispose();
            new HomeFrame();
        });
        JMenu menuLichChieu = createMenu("Lịch Chiếu", () -> {
            dispose();
            new ViewFilmsFrame(1);
        });
        JMenu menuDatVe = createMenu("Đặt Vé", () -> {
            if (Session.getInstance().getUsername() != null) {
                dispose();
                new ViewBookingFrame(Session.getInstance().getUsername());
            } else {
                JOptionPane.showMessageDialog(null, "Bạn cần đăng nhập để đặt vé!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        });
        JMenu menuKhuyenMai = createMenu("Khuyến Mãi", () -> {
            dispose();
            new PromotionFrame();
        });
        JMenu menuLienHe = createMenu("Liên Hệ", () -> {
            dispose();
            new ContactFrame();
        });

        menuTaiKhoan = new JMenu("Tài Khoản");
        menuTaiKhoan.setForeground(Color.WHITE);

        // Các menu khi chưa đăng nhập
        menuDangNhap = new JMenuItem("Đăng Nhập");
        menuDangKy = new JMenuItem("Đăng Ký");
        menuDangNhapAdmin = new JMenuItem("Đăng Nhập Admin");

        menuDangNhap.addActionListener(e -> {
            dispose();
            new UserLoginFrame();
            updateUserMenu();
        });
        menuDangKy.addActionListener(e -> {
            dispose();
            new RegisterFrame();
        });
        menuDangNhapAdmin.addActionListener(e -> {
            dispose();
            new AdminLoginFrame();
        });

        // Thêm vào menu tài khoản ban đầu
        menuTaiKhoan.add(menuDangNhap);
        menuTaiKhoan.add(menuDangKy);
        menuTaiKhoan.add(menuDangNhapAdmin);

        // Biểu tượng người dùng
        userIcon = new JLabel(new ImageIcon("C:/Users/Admin/eclipse-workspace/DOANCOSO1/bin/picture DOAN/user_icon.png"));
        userIcon.setVisible(false);
        userIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Nhãn hiển thị tên người dùng
        usernameLabel = new JLabel();
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameLabel.setVisible(false);

        // Menu người dùng sau khi đăng nhập
        userMenu = new JPopupMenu();
        menuChinhSua = new JMenuItem("Chỉnh sửa thông tin");
        menuDangXuat = new JMenuItem("Đăng xuất");

        menuChinhSua.addActionListener(e -> {
            dispose();
            new EditProfileFrame(Session.getInstance().getUsername());
        });
        menuDangXuat.addActionListener(e -> {
            Session.getInstance().logout();
            JOptionPane.showMessageDialog(null, "Bạn đã đăng xuất!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            updateUserMenu(); // Cập nhật giao diện sau khi đăng xuất
        });

        // Thêm vào menu người dùng
        userMenu.add(menuChinhSua);
        userMenu.add(menuDangXuat);

        // Hiển thị menu khi click vào icon user
        userIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                userMenu.show(userIcon, e.getX(), e.getY());
            }
        });

        menuBar.add(menuTrangChu);
        menuBar.add(menuLichChieu);
        menuBar.add(menuDatVe);
        menuBar.add(menuKhuyenMai);
        menuBar.add(menuLienHe);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(usernameLabel);
        menuBar.add(userIcon);
        menuBar.add(menuTaiKhoan);

        setJMenuBar(menuBar);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(1200, 400)); 
        imageLabel = new JLabel();
        setImage(imageLabel, imagePaths[currentIndex]);
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        
        cinemaTitle = new JLabel(" StarLight Cinema ", SwingConstants.CENTER);
        cinemaTitle.setFont(new Font("Segoe UI", Font.BOLD, 28)); 
        cinemaTitle.setForeground(Color.YELLOW);
        cinemaTitle.setOpaque(true);
        cinemaTitle.setBackground(new Color(0, 0, 0, 200));
        cinemaTitle.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
        imagePanel.add(cinemaTitle, BorderLayout.NORTH);
        
        mainPanel.add(imagePanel, BorderLayout.NORTH);
        mainPanel.add(createHotMoviesPanel(), BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                slideToNextImage();
            }
        }, 3000, 3000);

        updateUserMenu(); 
        setVisible(true);
    }

    private JButton usernameButton; 

    private void updateUserMenu() {
        Session session = Session.getInstance();

        if (menuBar == null || menuTaiKhoan == null) {
            return;
        }

        // Xóa menu tài khoản trước khi cập nhật
        menuTaiKhoan.removeAll();
        menuBar.removeAll(); // Xóa toàn bộ menu trước khi cập nhật

        if (session.isLoggedIn()) {
            String username = session.getUsername();
            String role = session.getRole();

            // 🟢 Hiển thị tên người dùng khi đăng nhập
            usernameLabel.setText("👤 " + username);
            usernameLabel.setVisible(true);
            userIcon.setVisible(true);
            menuTaiKhoan.setVisible(false); // Ẩn menu đăng nhập khi đã đăng nhập

            if (role.equalsIgnoreCase("admin")) {
                // 🟢 Nếu là Admin, mở giao diện Admin
                // Mở giao diện Admin
                new AdminDashboardFrame();  // Thay "AdminDashboardFrame" bằng giao diện admin của bạn

                // Thêm các menu quản lý Admin
                JMenu menuQuanLyPhim = createMenu("Quản Lý Phim", () -> new AdminFilmsFrame());
                JMenu menuQuanLyVe = createMenu("Quản Lý Vé", () -> new AdminFrame());
                JMenu menuThongKe = createMenu("Thống Kê", () -> new AdminStatisticsFrame());
                JMenu menuTaiKhoanAdmin = new JMenu("Tài Khoản");

                menuTaiKhoanAdmin.add(menuChinhSua);
                menuTaiKhoanAdmin.add(menuDangXuat);

                // Thêm menu quản lý và tài khoản admin vào menu bar
                menuBar.add(menuQuanLyPhim);
                menuBar.add(menuQuanLyVe);
                menuBar.add(menuThongKe);
                menuBar.add(Box.createHorizontalGlue());
                menuBar.add(usernameLabel);
                menuBar.add(userIcon);
                menuBar.add(menuTaiKhoanAdmin);
            } else {
                // 🟢 Nếu là người dùng bình thường, mở giao diện người dùng
                // Mở giao diện người dùng
                new UserInterfaceFrame();  // Thay "UserInterfaceFrame" bằng giao diện người dùng của bạn

                // Thêm menu người dùng
                menuBar.add(menuTrangChu);
                menuBar.add(menuLichChieu);
                menuBar.add(menuDatVe);
                menuBar.add(menuKhuyenMai);
                menuBar.add(menuLienHe);
                menuBar.add(Box.createHorizontalGlue());
                menuBar.add(usernameLabel);
                menuBar.add(userIcon);
            }
        } else {
            // 🟢 Nếu chưa đăng nhập, hiển thị menu mặc định
            usernameLabel.setVisible(false);
            userIcon.setVisible(false);
            menuTaiKhoan.setVisible(true);

            menuTaiKhoan.add(menuDangNhap);
            menuTaiKhoan.add(menuDangKy);
            menuTaiKhoan.add(menuDangNhapAdmin);

            menuBar.add(menuTaiKhoan);
        }

        // Cập nhật lại giao diện
        menuBar.revalidate();
        menuBar.repaint();
    }
    
    private void showUserMenu(Component component) {
        JPopupMenu userMenu = new JPopupMenu();

        JMenuItem editProfile = new JMenuItem("Chỉnh sửa thông tin");
        JMenuItem logout = new JMenuItem("Đăng xuất");

        editProfile.addActionListener(e -> openEditProfile());
        logout.addActionListener(e -> logoutUser());

        userMenu.add(editProfile);
        userMenu.add(logout);
        
        userMenu.show(component, 0, component.getHeight());
    }

    private void logoutUser() {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Session.getInstance().logout(); // Xóa session
            dispose(); // Đóng cửa sổ hiện tại
            new UserLoginFrame().setVisible(true); // Mở lại màn hình đăng nhập
        }
    }

    private void openEditProfile() {
        new EditProfileFrame(getTitle()).setVisible(true); 
    }


    private void addAdminMenu() {
        if (!Session.getInstance().isAdmin()) {
            return; // Nếu không phải Admin, không hiển thị menu
        }

        JMenu menuQuanLy = new JMenu("🛠 Quản Lý");
        menuQuanLy.setForeground(Color.WHITE);

        JMenuItem menuQLPhim = new JMenuItem("Quản Lý Phim");
        JMenuItem menuQLVe = new JMenuItem("Quản Lý Vé");
        JMenuItem menuQLKhachHang = new JMenuItem("Quản Lý Khách Hàng");
        JMenuItem menuQLKhuyenMai = new JMenuItem("Quản Lý Khuyến Mãi");

        menuQLPhim.addActionListener(e -> {
            dispose();
            new AdminFilmsFrame();
        });

        menuQLVe.addActionListener(e -> {
            dispose();
            new AdminFrame();
        });

        menuQLKhachHang.addActionListener(e -> {
            dispose();
            new AdminViewBookingFrame();
        });

        menuQLKhuyenMai.addActionListener(e -> {
            dispose();
            new AdminPromotionsFrame();
        });

        menuQuanLy.add(menuQLPhim);
        menuQuanLy.add(menuQLVe);
        menuQuanLy.add(menuQLKhachHang);
        menuQuanLy.add(menuQLKhuyenMai);

        menuBar.add(menuQuanLy);
    }

    private JMenu createMenu(String title, Runnable action) {
        JMenu menu = new JMenu(title);
        menu.setForeground(Color.WHITE);
        menu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }
        });
        return menu;
    }
    
    private JPanel createHotMoviesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);

        // Tiêu đề "Phim Hot"
        JLabel hotMoviesTitle = new JLabel("🎬 Phim Hot", SwingConstants.CENTER);
        hotMoviesTitle.setFont(new Font("Arial", Font.BOLD, 32));
        hotMoviesTitle.setForeground(Color.YELLOW);
        hotMoviesTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        panel.add(hotMoviesTitle, BorderLayout.NORTH);

        // Container chứa danh sách phim
        JPanel moviesContainer = new JPanel(new GridLayout(1, hotMoviePaths.length, 20, 20));
        moviesContainer.setBackground(Color.BLACK);

        for (String moviePath : hotMoviePaths) {
           
            ImageIcon icon = new ImageIcon(new ImageIcon(moviePath).getImage()
                    .getScaledInstance(220, 320, Image.SCALE_SMOOTH));
            JLabel movieLabel = new JLabel(icon);
            movieLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3));
            movieLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // Hiệu ứng hover
            movieLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    movieLabel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 4));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    movieLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3));
                }
            });

            moviesContainer.add(movieLabel);
        }

        // Bọc container trong JScrollPane (Cuộn ngang nếu cần)
        JScrollPane scrollPane = new JScrollPane(moviesContainer, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.BLACK);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void setImage(JLabel label, String imagePath) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImage = icon.getImage().getScaledInstance(1200, 500, Image.SCALE_SMOOTH);
        
        // Hiển thị ảnh với bo góc đẹp hơn
        label.setIcon(new ImageIcon(scaledImage));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void slideToNextImage() {
        currentIndex = (currentIndex + 1) % imagePaths.length;
        setImage(imageLabel, imagePaths[currentIndex]);
    }
}
