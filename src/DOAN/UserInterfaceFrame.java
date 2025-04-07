package DOAN;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

class UserInterfaceFrame extends JFrame {
    private JPanel imagePanel, hotMoviesPanel, topPanel;
    private JLabel imageLabel, cinemaTitle, userIcon, usernameLabel;
    private JMenu menuBar;
    private JPopupMenu userMenu;
    private JMenuItem menuChinhSua, menuDangXuat, menuTrangChu, menuLichChieu, menuDatVe, menuKhuyenMai, menuLienHe;
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
    
    public UserInterfaceFrame() {
        setTitle("StarLight Cinema");
        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(40, 40, 40));
        menuBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

     // Tạo các menu và gắn hành động vào ngay khi click
        JMenu menuTrangChu = createMenu("Trang Chủ", () -> {
            dispose();
            new HomeFrame();
        });

        JMenu menuLichChieu = createMenu("Lịch Chiếu", () -> {
            dispose();
            new ViewFilmsFrame(1);
        });

        JMenu menuDatVe = createMenu("Đặt Vé", () -> {
            dispose();
            new ViewBookingFrame(Session.getInstance().getUsername());
        });

        JMenu menuKhuyenMai = createMenu("Khuyến Mãi", () -> {
            dispose();
            new PromotionFrame();
        });

        JMenu menuLienHe = createMenu("Liên Hệ", () -> {
            dispose();
            new ContactFrame();
        });

        // Thêm các menu vào menuBar
        menuBar.add(menuTrangChu);
        menuBar.add(menuLichChieu);
        menuBar.add(menuDatVe);
        menuBar.add(menuKhuyenMai);
        menuBar.add(menuLienHe);

        // Cập nhật thanh menu
        setJMenuBar(menuBar);
        
        // Biểu tượng người dùng
        userIcon = new JLabel(new ImageIcon("C:/Users/Admin/eclipse-workspace/DOANCOSO1/bin/picture DOAN/user_icon.png"));
        userIcon.setVisible(true);
        userIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Nhãn hiển thị tên người dùng
        usernameLabel = new JLabel("Hello, " + Session.getInstance().getUsername());
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameLabel.setVisible(true);

        // Menu người dùng (chỉ có "Chỉnh sửa thông tin" và "Đăng xuất")
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
            dispose();
            new HomeFrame();  
        });

        userMenu.add(menuChinhSua);
        userMenu.add(menuDangXuat);

        // Hiển thị menu khi click vào icon user
        userIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                userMenu.show(userIcon, e.getX(), e.getY());
            }
        });

        // Hiển thị menu khi click vào tên người dùng
        usernameLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                userMenu.show(usernameLabel, e.getX(), e.getY());
            }
        });

        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(usernameLabel);
        menuBar.add(userIcon);

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
        
        setVisible(true);
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

    // Phương thức chuyển đổi hình ảnh
    private void slideToNextImage() {
        currentIndex = (currentIndex + 1) % imagePaths.length;
        setImage(imageLabel, imagePaths[currentIndex]);
    }

    // Phương thức giúp hiển thị hình ảnh
    private void setImage(JLabel label, String imagePath) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(1200, 400, Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);
        label.setIcon(icon);
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
}
