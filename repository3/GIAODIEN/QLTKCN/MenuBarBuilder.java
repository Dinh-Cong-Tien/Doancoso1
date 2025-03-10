//package QLTKCN;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//public class MenuBarBuilder {
//    public JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
//
//        // Tạo các menu chính
//        JMenu homeMenu = new JMenu("Trang Chủ");
//        JMenu aboutMenu = new JMenu("Giới Thiệu");
//        JMenu serviceMenu = new JMenu("Dịch Vụ");
//        JMenu courseMenu = new JMenu("Khóa Học");
//        JMenu contactMenu = new JMenu("Liên Hệ");
//
//        // Tăng kích thước chữ trong menu
//        Font menuFont = new Font("Arial", Font.BOLD, 16);
//        homeMenu.setFont(menuFont);
//        aboutMenu.setFont(menuFont);
//        serviceMenu.setFont(menuFont);
//        courseMenu.setFont(menuFont);
//        contactMenu.setFont(menuFont);
//
//        // Thêm các menu vào menu bar
//        menuBar.add(homeMenu);
//        menuBar.add(aboutMenu);
//        menuBar.add(serviceMenu);
//        menuBar.add(courseMenu);
//        menuBar.add(contactMenu);
//
//        // Tạo nhóm các nút Đăng Ký và Đăng Nhập
//        JPanel authPanel = new JPanel();
//        authPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0)); // Căn phải và điều chỉnh khoảng cách giữa các nút
//
//        // Tạo nút Đăng Ký và Đăng Nhập
//        JMenuItem registerMenu = new JMenuItem("Đăng Ký");
//        registerMenu.setFont(menuFont);
//        JMenu loginMenu = new JMenu("Đăng Nhập");
//        loginMenu.setFont(menuFont);
//
//        // Tạo các mục con trong menu Đăng Nhập
//        JMenuItem userLoginItem = new JMenuItem("Đăng Nhập User");
//        JMenuItem adminLoginItem = new JMenuItem("Đăng Nhập Admin");
//        loginMenu.add(userLoginItem);
//        loginMenu.add(adminLoginItem);
//
//        // Thêm các mục vào JPanel authPanel
//        authPanel.add(registerMenu);
//        authPanel.add(loginMenu);
//
//        // Thêm authPanel vào menuBar
//        menuBar.add(Box.createHorizontalGlue());  // Đẩy sang phải
//        menuBar.add(authPanel);  // Đặt authPanel vào menuBar
//
//        // Đăng ký sự kiện cho Đăng Ký
//        registerMenu.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // Mở giao diện đăng ký
//                new Register1();
//            }
//        });
//
//        // Đăng ký sự kiện cho Đăng Nhập User
//        userLoginItem.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                new UserLoginFrame();
//            }
//        });
//
//        // Đăng ký sự kiện cho Đăng Nhập Admin
//        adminLoginItem.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                new AdminLoginFrame();
//            }
//        });
//
//        return menuBar;
//    }
//}
//
//
