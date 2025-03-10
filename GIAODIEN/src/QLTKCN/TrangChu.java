package QLTKCN;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import SQL.AdminDao;
import SQL.ConnectSQL;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

class UserManager {
	private static ArrayList<User> userList = new ArrayList<>();

	public static void addUser(User user) {
		userList.add(user);
	}

	public static ArrayList<User> getUserList() {
		return userList;
	}

	public static boolean isValidLogin(String username, String password) {
		for (User user : userList) {
			if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
				return true;
			}
		}
		return false;
	}
}

//Lớp User để lưu thông tin tài khoản
class User {
	private String username;
	private String password;

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}

//Lớp ảnh nền 
class BackgroundPanel1 extends JPanel {
	private Image backgroundImage;

	public BackgroundPanel1(String imagePath) {
		try {
			backgroundImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (backgroundImage != null) {
			// Vẽ ảnh nền lấp đầy toàn bộ JPanel
			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
		}
	}
}

class TrangChu extends JFrame {
	public TrangChu() {
		// Tạo JFrame
		setTitle("Trang Chủ Quản Lý");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(900, 600);
		setLayout(new BorderLayout());

		// Tạo JPanel tùy chỉnh với ảnh nền
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		// Tạo khoảng trống phía trên thanh menu
		JPanel topPanel = new JPanel();
		topPanel.setPreferredSize(new Dimension(0, 40));

		// Tạo thanh menu bar
		JMenuBar menuBar = new JMenuBar();

		// Tạo các menu chính
		JMenu homeMenu = new JMenu("Trang Chủ");
		JMenu aboutMenu = new JMenu("Giới Thiệu");
		JMenu serviceMenu = new JMenu("Dịch Vụ");
		JMenu courseMenu = new JMenu("Khóa Học");
		JMenu contactMenu = new JMenu("Liên Hệ");

		// Tăng kích thước chữ trong menu
		Font menuFont = new Font("Arial", Font.BOLD, 16);
		homeMenu.setFont(menuFont);
		aboutMenu.setFont(menuFont);
		serviceMenu.setFont(menuFont);
		courseMenu.setFont(menuFont);
		contactMenu.setFont(menuFont);

		// Thêm các menu vào menu bar
		menuBar.add(homeMenu);
		menuBar.add(aboutMenu);
		menuBar.add(serviceMenu);
		menuBar.add(courseMenu);
		menuBar.add(contactMenu);

		// Tạo menu "Tài Khoản"
		JMenu accountMenu = new JMenu("Tài Khoản");
		accountMenu.setFont(menuFont);

		// Tạo các mục con cho "Tài Khoản"
		JMenuItem registerItem = new JMenuItem("Đăng Ký");
		registerItem.setFont(menuFont);
		JMenuItem userLoginItem = new JMenuItem("Đăng Nhập User");
		userLoginItem.setFont(menuFont);
		JMenuItem adminLoginItem = new JMenuItem("Đăng Nhập Admin");
		adminLoginItem.setFont(menuFont);

		// Thêm các mục con vào menu "Tài Khoản"
		accountMenu.add(registerItem);
		accountMenu.addSeparator();
		accountMenu.add(userLoginItem);
		accountMenu.add(adminLoginItem);

		menuBar.add(Box.createHorizontalGlue()); // Đẩy "Tài Khoản" về phía bên phải
		menuBar.add(accountMenu);

		setJMenuBar(menuBar);

		panel.add(topPanel, BorderLayout.NORTH);
		panel.add(menuBar, BorderLayout.NORTH);

		// Tạo danh sách dịch vụ dưới dạng JPopupMenu
		JPopupMenu servicePopup = new JPopupMenu();
		String[] services = { "Tư vấn giải pháp CNTT", "Phát triển phần mềm", "Phát triển phần mềm bảo mật" };

		JComboBox<String> serviceItem = new JComboBox<>(services);

		serviceItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String selectedService = (String) serviceItem.getSelectedItem();

				switch (selectedService) {
				case "Tư vấn giải pháp CNTT":
					new TuVanCNTTFrame();
					break;
				case "Phát triển phần mềm":
					new PhatTrienPhanMemFrame();
					break;
				case "Phát triển phần mềm bảo mật":
					new BaoMatPhanMemFrame();
					break;
				default:
					JOptionPane.showMessageDialog(null, "Chưa có giao diện cho dịch vụ này!");
				}
				dispose();
			}
		});
		servicePopup.add(serviceItem);

		// Tạo danh sách khóa học dưới dạng JPopupMenu
		JPopupMenu coursePopup = new JPopupMenu();
		String[] courses = { "Khóa học Java", "Khóa học Python", "Khóa học Web Development",
				"Khóa học an ninh mạng và bảo mật thông tin" };

		JComboBox<String> courseItem = new JComboBox<>(courses);

		courseItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String coursed = (String) courseItem.getSelectedItem();

				switch (coursed) {
				case "Khóa học Java":
					new JavaCourseFrame();
					break;
				case "Khóa học Python":
					new PythonCourseFrame();
					break;
				case "Khóa học Web Development":
					new WebDevCourseFrame();
					break;
				case "Khóa học an ninh mạng và bảo mật thông tin":
					new CyberSecurityCourseFrame();
					break;
				default:
					JOptionPane.showMessageDialog(null, "Chưa có giao diện cho khóa học này!");
				}
				dispose();
			}
		});
		coursePopup.add(courseItem);

		serviceMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				servicePopup.show(serviceMenu, 0, serviceMenu.getHeight());
			}

			@Override
			public void mouseExited(MouseEvent e) {
				Point mousePosition = MouseInfo.getPointerInfo().getLocation();
				SwingUtilities.convertPointFromScreen(mousePosition, servicePopup);
				if (!servicePopup.contains(mousePosition)) {
					servicePopup.setVisible(false);
				}
			}
		});

		courseMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				coursePopup.show(courseMenu, 0, courseMenu.getHeight());
			}

			@Override
			public void mouseExited(MouseEvent e) {
				Point mousePosition = MouseInfo.getPointerInfo().getLocation();
				SwingUtilities.convertPointFromScreen(mousePosition, coursePopup);
				if (!coursePopup.contains(mousePosition)) {
					coursePopup.setVisible(false);
				}
			}
		});

		registerItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new RegisterFrame();
				dispose();
			}
		});

		userLoginItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new UserLoginFrame();
				dispose();
			}
		});

		adminLoginItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new AdminLoginFrame();
				dispose();
			}
		});

		aboutMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new GiaoDienGioiThieu();
				dispose();
			}
		});

		contactMenu.setFont(new Font("Arial", Font.BOLD, 16));

		contactMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new GiaoDienLienHe();
				dispose();
			}
		});

		JMenuBar menuBar1 = new JMenuBar();
		menuBar1.add(homeMenu);
		menuBar1.add(aboutMenu);
		menuBar1.add(serviceMenu);
		menuBar1.add(courseMenu);
		menuBar1.add(contactMenu);

		// Đặt thanh menu vào JFrame
		setJMenuBar(menuBar1);

		// Tạo JPanel tùy chỉnh với ảnh nền
		BackgroundPanel1 backgroundPanel = new BackgroundPanel1("/Image/poster1.png");

		// Đặt layout cho JPanel chính
		backgroundPanel.setLayout(new BorderLayout());
		backgroundPanel.setPreferredSize(new Dimension(0, 725));

		panel.add(backgroundPanel, BorderLayout.SOUTH);

		add(panel, BorderLayout.NORTH);

		setVisible(true);
	}

	public static void main(String[] args) {
		new TrangChu();
	}
}

class GiaoDienGioiThieu extends JFrame {

	public GiaoDienGioiThieu() {
		// Thiết lập JFrame
		setTitle("Giới Thiệu Bản Thân");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(900, 600);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		// Header - Tiêu đề
		JPanel headerPanel = new JPanel();
		headerPanel.setBackground(new Color(51, 153, 255));
		headerPanel.setPreferredSize(new Dimension(0, 80));
		headerPanel.setLayout(new GridBagLayout());

		JLabel headerLabel = new JLabel("GIỚI THIỆU BẢN THÂN");
		headerLabel.setForeground(Color.WHITE);
		headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
		headerPanel.add(headerLabel);

		add(headerPanel, BorderLayout.NORTH);

		// Nội dung chính
		JPanel contentPanel = new JPanel();
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setLayout(new BorderLayout(20, 20));
		contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// Phần trái: Hình ảnh đại diện
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(Color.WHITE);
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

		try {
		    // Tải ảnh và điều chỉnh kích thước
		    ImageIcon avatarIcon = new ImageIcon(getClass().getResource("/Image/ảnh.png"));
		    Image avatarImg = avatarIcon.getImage().getScaledInstance(800, 500, Image.SCALE_SMOOTH); // Điều chỉnh kích thước phù hợp
		    JLabel avatarLabel = new JLabel(new ImageIcon(avatarImg));

		    // Căn giữa ảnh và thêm vào JPanel
		    avatarLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		    // Đặt khoảng cách cho hình ảnh (padding)
		    avatarLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

		    leftPanel.add(avatarLabel);
		} catch (Exception e) {
		    // Hiển thị thông báo lỗi nếu không tải được ảnh
		    JLabel errorLabel = new JLabel("Không thể tải ảnh đại diện");
		    errorLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		    leftPanel.add(errorLabel);
		}


		contentPanel.add(leftPanel, BorderLayout.WEST);

		// Phần trung tâm: Văn bản thông tin
		JTextArea infoTextArea = new JTextArea();
		infoTextArea.setEditable(false);
		infoTextArea.setText("Tên: Đinh Công Tiến\n" + "Nghề Nghiệp: Lập trình viên\n"
				+ "Sở Thích: Chơi game, đọc sách, học lập trình\n"
				+ "Mục Tiêu: Trở thành chuyên gia trong lĩnh vực phát triển phần mềm\n\n" + "Kỹ Năng:\n" + "- 0.9 ielts"
				+ "- Lập trình: Java, Python, JavaScript, C++\n" + "- Web Development: HTML, CSS, React, Spring Boot\n"
				+ "- Quản lý cơ sở dữ liệu: MySQL, PostgreSQL\n" + "- Công cụ: Git, Docker, Jenkins\n\n"
				+ "Dự Án Tiêu Biểu:\n" + "- Hệ thống quản lý nhân viên (Java, MySQL)\n"
				+ "- Website thương mại điện tử (React, Node.js)\n" + "- Công cụ phân tích log bảo mật (Python)\n");
		infoTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		infoTextArea.setForeground(new Color(51, 51, 51));
		infoTextArea.setBackground(new Color(245, 245, 245));
		infoTextArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
		infoTextArea.setLineWrap(true);
		infoTextArea.setWrapStyleWord(true);

		JScrollPane scrollPane = new JScrollPane(infoTextArea);
		contentPanel.add(scrollPane, BorderLayout.CENTER);

		add(contentPanel, BorderLayout.CENTER);

		// Footer - Nút quay lại và thông tin bản quyền
		JPanel footerPanel = new JPanel();
		footerPanel.setBackground(new Color(51, 153, 255));
		footerPanel.setPreferredSize(new Dimension(0, 60));
		footerPanel.setLayout(new BorderLayout());

		JLabel footerLabel = new JLabel("© 2024 Đinh Công Tiến", JLabel.CENTER);
		footerLabel.setForeground(Color.WHITE);
		footerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
		footerPanel.add(footerLabel, BorderLayout.CENTER);

		JButton backButton = new JButton("Quay lại");
		backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
		backButton.setBackground(Color.WHITE);
		backButton.setForeground(new Color(51, 153, 255));
		backButton.setFocusPainted(false);
		backButton.setPreferredSize(new Dimension(100, 40));

		backButton.addActionListener(e -> {
			new TrangChu();
			dispose();
		});

		footerPanel.add(backButton, BorderLayout.WEST);

		add(footerPanel, BorderLayout.SOUTH);

		// Hiển thị JFrame
		setVisible(true);
	}
}

class GiaoDienLienHe extends JFrame {

	public GiaoDienLienHe() {
		// Thiết lập JFrame
		setTitle("Liên Hệ");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 400);
		setLayout(new BorderLayout());

		// Tạo JPanel để chứa các thành phần
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBackground(new Color(245, 245, 245));

		// Tạo một label cho tiêu đề
		JLabel titleLabel = new JLabel("Thông Tin Liên Hệ", JLabel.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
		titleLabel.setForeground(new Color(34, 49, 63));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		panel.add(titleLabel, BorderLayout.NORTH);

		// Tạo thông tin liên hệ dưới dạng JTextArea
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setText("Email: tien.dinhcong2006@gmail.com\n" + "Điện thoại: 0123456789\n"
				+ "Địa chỉ: Hoà Phước, Hoà Vang, Thành phố Đà Nẵng\n");
		textArea.setFont(new Font("Arial", Font.PLAIN, 16));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setBackground(new Color(255, 255, 255));
		textArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
		textArea.setCaretColor(new Color(34, 49, 63));
		panel.add(new JScrollPane(textArea), BorderLayout.CENTER);

		// Tạo JPanel cho các nút
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
		buttonPanel.setBackground(new Color(245, 245, 245));

		// Tạo nút "Gửi Email"
		JButton sendEmailButton = new JButton("Gửi Email");
		sendEmailButton.setFont(new Font("Arial", Font.BOLD, 16));
		sendEmailButton.setBackground(new Color(34, 139, 34));
		sendEmailButton.setForeground(Color.WHITE);
		sendEmailButton.setFocusPainted(false);
		sendEmailButton.setPreferredSize(new Dimension(150, 40));

		// Thêm sự kiện khi nhấn nút "Gửi Email"
		sendEmailButton.addActionListener(e -> {
			JOptionPane.showMessageDialog(this, "Email đã được gửi thành công!", "Thông báo",
					JOptionPane.INFORMATION_MESSAGE);
		});

		// Tạo nút "Quay lại"
		JButton backButton = new JButton("Quay lại");
		backButton.setFont(new Font("Arial", Font.BOLD, 14));
		backButton.setBackground(new Color(0, 123, 255));
		backButton.setForeground(Color.WHITE);
		backButton.setPreferredSize(new Dimension(100, 40));
		backButton.setFocusPainted(false);

		// Sự kiện cho nút "Quay lại"
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new TrangChu();
				dispose();
			}
		});

		// Thêm các nút vào panel
		buttonPanel.add(sendEmailButton); // Nút "Gửi Email"
		buttonPanel.add(Box.createHorizontalStrut(20)); // Thêm khoảng trống giữa nút "Gửi Email" và "Quay lại"
		buttonPanel.add(backButton); // Nút "Quay lại"

		// Thêm buttonPanel vào panel chính
		panel.add(buttonPanel, BorderLayout.SOUTH); // Thêm vào phần dưới của panel chính

		// Thêm panel vào JFrame
		add(panel, BorderLayout.CENTER);

		// Hiển thị JFrame
		setVisible(true);
	}
}

class TuVanCNTTFrame extends JFrame {
	public TuVanCNTTFrame() {
		setTitle("Tư Vấn Giải Pháp CNTT");
		setSize(850, 700); // Kích thước tổng thể của cửa sổ
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// Màu nền tổng thể (xanh nhạt)
		getContentPane().setBackground(new Color(232, 248, 248)); // Màu xanh nhạt

		// Panel chính
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBackground(new Color(255, 255, 255)); // Màu nền sáng cho panel chính

		// Tiêu đề
		JLabel titleLabel = new JLabel("Dịch Vụ Tư Vấn Giải Pháp CNTT", JLabel.CENTER);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
		titleLabel.setForeground(new Color(34, 49, 63)); // Màu chữ cho tiêu đề
		titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
		panel.add(titleLabel, BorderLayout.NORTH);

		// Phần mô tả dịch vụ
		JPanel servicePanel = new JPanel();
		servicePanel.setLayout(new GridLayout(0, 1, 10, 10)); // Layout Grid cho dịch vụ
		servicePanel.setBackground(new Color(255, 255, 255)); // Màu nền sáng cho panel dịch vụ

		// Dịch vụ 1: Tư vấn chuyển đổi số
		JPanel service1 = createServicePanel("Tư vấn chuyển đổi số",
				"Giải pháp chuyển đổi số giúp doanh nghiệp tối ưu hóa quy trình làm việc, tăng trưởng nhanh chóng và bắt kịp xu thế công nghệ.");
		// Dịch vụ 2: Thiết kế và triển khai hệ thống CNTT
		JPanel service2 = createServicePanel("Thiết kế và triển khai hệ thống CNTT",
				"Chúng tôi thiết kế các hệ thống CNTT tùy chỉnh đáp ứng yêu cầu riêng của doanh nghiệp, từ phần cứng đến phần mềm.");
		// Dịch vụ 3: Tư vấn bảo mật và an ninh mạng
		JPanel service3 = createServicePanel("Tư vấn bảo mật và an ninh mạng",
				"Giải pháp bảo mật toàn diện giúp bảo vệ dữ liệu doanh nghiệp khỏi các mối đe dọa bên ngoài và bên trong.");
		// Dịch vụ 4: Quản lý hệ thống và hỗ trợ kỹ thuật
		JPanel service4 = createServicePanel("Quản lý hệ thống và hỗ trợ kỹ thuật",
				"Dịch vụ hỗ trợ và bảo trì hệ thống CNTT liên tục, giúp doanh nghiệp duy trì hoạt động hiệu quả và tránh gián đoạn.");

		servicePanel.add(service1);
		servicePanel.add(service2);
		servicePanel.add(service3);
		servicePanel.add(service4);

		// Cuối phần mô tả dịch vụ
		JScrollPane serviceScrollPane = new JScrollPane(servicePanel);
		panel.add(serviceScrollPane, BorderLayout.CENTER);

		// Phần liên hệ
		JPanel contactPanel = new JPanel();
		contactPanel.setLayout(new BoxLayout(contactPanel, BoxLayout.Y_AXIS));
		contactPanel.setBackground(new Color(255, 255, 255)); // Màu nền nhẹ cho phần liên hệ

		JLabel contactTitleLabel = new JLabel("Liên Hệ Với Chúng Tôi");
		contactTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
		contactTitleLabel.setForeground(new Color(34, 49, 63));

		JTextArea contactInfoArea = new JTextArea();
		contactInfoArea.setEditable(false);
		contactInfoArea.setText("Email: tien.dinhcong2006@gmail.com\nĐiện thoại: 0123456789\n...");
		contactInfoArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		contactInfoArea.setLineWrap(true);
		contactInfoArea.setWrapStyleWord(true);
		contactInfoArea.setBackground(new Color(255, 255, 255)); // Màu trắng cho trường nhập liệu
		contactInfoArea.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

		contactPanel.add(contactTitleLabel);
		contactPanel.add(Box.createVerticalStrut(10));
		contactPanel.add(new JScrollPane(contactInfoArea));

		// Phần button
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
		buttonPanel.setBackground(new Color(255, 255, 255));

		// Đăng ký tư vấn
		JButton registerButton = new JButton("Đăng ký tư vấn");
		registerButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
		registerButton.setBackground(new Color(40, 167, 69)); // Màu xanh lá
		registerButton.setForeground(Color.WHITE);
		registerButton.setPreferredSize(new Dimension(180, 45));
		registerButton.setFocusPainted(false);
		registerButton.setBorder(BorderFactory.createLineBorder(new Color(40, 167, 69), 2));

		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Kiểm tra nếu dialog đã tồn tại, không mở thêm lần nữa
				if (SwingUtilities.getWindowAncestor(registerButton) instanceof RegistrationForm) {
					JOptionPane.showMessageDialog(TuVanCNTTFrame.this, "Bạn đã đăng ký tư vấn trước đó!");
				} else {
					dispose();
					new RegistrationForm();
				}
			}
		});

		// Quay lại
		JButton backButton = new JButton("Quay lại");
		backButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
		backButton.setBackground(new Color(255, 87, 34)); // Màu cam
		backButton.setForeground(Color.WHITE);
		backButton.setPreferredSize(new Dimension(180, 45));
		backButton.setFocusPainted(false);
		backButton.setBorder(BorderFactory.createLineBorder(new Color(255, 87, 34), 2));

		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new TrangChu(); // Đi tới trang chủ
				dispose();
			}
		});

		// Hiệu ứng hover cho nút bấm
		registerButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				registerButton.setBackground(new Color(36, 139, 56)); // Đổi màu khi di chuột
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				registerButton.setBackground(new Color(40, 167, 69)); // Trở về màu ban đầu
			}
		});

		backButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				backButton.setBackground(new Color(255, 100, 20)); // Đổi màu khi di chuột
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				backButton.setBackground(new Color(255, 87, 34)); // Trở về màu ban đầu
			}
		});

		buttonPanel.add(registerButton);
		buttonPanel.add(backButton);

		contactPanel.add(Box.createVerticalStrut(10));
		contactPanel.add(buttonPanel);

		panel.add(contactPanel, BorderLayout.SOUTH);

		add(panel, BorderLayout.CENTER);
		setVisible(true);
	}

	// Tạo Panel cho từng dịch vụ
	private JPanel createServicePanel(String serviceName, String serviceDescription) {
		JPanel servicePanel = new JPanel();
		servicePanel.setLayout(new BorderLayout());
		servicePanel.setBackground(new Color(255, 255, 255)); // Nền sáng cho dịch vụ

		JLabel serviceTitle = new JLabel(serviceName);
		serviceTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
		serviceTitle.setForeground(new Color(34, 49, 63));
		serviceTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JTextArea serviceDescriptionArea = new JTextArea(serviceDescription);
		serviceDescriptionArea.setEditable(false);
		serviceDescriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		serviceDescriptionArea.setBackground(new Color(255, 255, 255));
		serviceDescriptionArea.setWrapStyleWord(true);
		serviceDescriptionArea.setLineWrap(true);
		serviceDescriptionArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		servicePanel.add(serviceTitle, BorderLayout.NORTH);
		servicePanel.add(serviceDescriptionArea, BorderLayout.CENTER);

		return servicePanel;
	}
}

class RegistrationForm extends JFrame {
	public RegistrationForm() {
		setTitle("Đăng Ký Khóa Học");
		setSize(600, 700); // Tăng kích thước form
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);

		JPanel backgroundPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				int width = getWidth();
				int height = getHeight();
				Color color1 = new Color(147, 112, 219);
				Color color2 = new Color(218, 112, 214);
				GradientPaint gradient = new GradientPaint(0, 0, color1, width, height, color2);
				g2d.setPaint(gradient);
				g2d.fillRect(0, 0, width, height);
			}
		};
		backgroundPanel.setLayout(new BorderLayout());
		add(backgroundPanel);

		// Tiêu đề
		JLabel titleLabel = new JLabel("Tư vấn CNTT", JLabel.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
		backgroundPanel.add(titleLabel, BorderLayout.NORTH);

		// Form đăng ký sử dụng GridBagLayout
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new GridBagLayout());
		formPanel.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.WEST;

		// Tên đăng nhập
		JLabel usernameLabel = new JLabel("Tên đăng nhập:");
		usernameLabel.setForeground(Color.WHITE);
		usernameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 0;
		formPanel.add(usernameLabel, gbc);

		JTextField usernameField = new JTextField(20);
		usernameField.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 1;
		formPanel.add(usernameField, gbc);

		// Mật khẩu
		JLabel passwordLabel = new JLabel("Mật khẩu:");
		passwordLabel.setForeground(Color.WHITE);
		passwordLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 1;
		formPanel.add(passwordLabel, gbc);

		JPasswordField passwordField = new JPasswordField(20);
		passwordField.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 1;
		formPanel.add(passwordField, gbc);

		// Họ và tên
		JLabel nameLabel = new JLabel("Họ và tên:");
		nameLabel.setForeground(Color.WHITE);
		nameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 2;
		formPanel.add(nameLabel, gbc);

		JTextField nameField = new JTextField(20);
		nameField.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 1;
		formPanel.add(nameField, gbc);

		// Giới tính
		JLabel genderLabel = new JLabel("Giới tính:");
		genderLabel.setForeground(Color.WHITE);
		genderLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 3;
		formPanel.add(genderLabel, gbc);

		JRadioButton maleButton = new JRadioButton("Nam");
		maleButton.setOpaque(false);
		JRadioButton femaleButton = new JRadioButton("Nữ");
		femaleButton.setOpaque(false);
		ButtonGroup genderGroup = new ButtonGroup();
		genderGroup.add(maleButton);
		genderGroup.add(femaleButton);

		JPanel genderPanel = new JPanel();
		genderPanel.setOpaque(false);
		genderPanel.add(maleButton);
		genderPanel.add(femaleButton);

		gbc.gridx = 1;
		formPanel.add(genderPanel, gbc);

		// Ngày sinh
		JLabel dobLabel = new JLabel("Ngày sinh:");
		dobLabel.setForeground(Color.WHITE);
		dobLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 4;
		formPanel.add(dobLabel, gbc);

		JComboBox<String> dayBox = new JComboBox<>();
		for (int i = 1; i <= 31; i++) {
			dayBox.addItem(String.format("%02d", i));
		}

		JComboBox<String> monthBox = new JComboBox<>();
		for (int i = 1; i <= 12; i++) {
			monthBox.addItem(String.format("%02d", i));
		}

		JComboBox<String> yearBox = new JComboBox<>();
		for (int i = 1900; i <= 2025; i++) {
			yearBox.addItem(String.valueOf(i));
		}

		JPanel dobPanel = new JPanel();
		dobPanel.setOpaque(false);
		dobPanel.add(dayBox);
		dobPanel.add(monthBox);
		dobPanel.add(yearBox);

		gbc.gridx = 1;
		formPanel.add(dobPanel, gbc);

		// Email
		JLabel emailLabel = new JLabel("Email:");
		emailLabel.setForeground(Color.WHITE);
		emailLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 5;
		formPanel.add(emailLabel, gbc);

		JTextField emailField = new JTextField(20);
		emailField.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 1;
		formPanel.add(emailField, gbc);

		// Số điện thoại
		JLabel phoneLabel = new JLabel("Số điện thoại:");
		phoneLabel.setForeground(Color.WHITE);
		phoneLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 6;
		formPanel.add(phoneLabel, gbc);

		JTextField phoneField = new JTextField(20);
		phoneField.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 1;
		formPanel.add(phoneField, gbc);

		// Checkbox đồng ý điều khoản
		JCheckBox termsCheckBox = new JCheckBox("Tôi đồng ý với các điều khoản và điều kiện.");
		termsCheckBox.setFont(new Font("Arial", Font.PLAIN, 16));
		termsCheckBox.setOpaque(false);
		gbc.gridx = 1;
		gbc.gridy = 7;
		formPanel.add(termsCheckBox, gbc);

		backgroundPanel.add(formPanel, BorderLayout.CENTER);

		// Nút Xác Nhận và Reset
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		buttonPanel.setOpaque(false);

		JButton confirmButton = new JButton("Xác Nhận");
		confirmButton.setFont(new Font("Arial", Font.BOLD, 22));
		confirmButton.setBackground(new Color(34, 139, 34));
		confirmButton.setForeground(Color.WHITE);
		confirmButton.setFocusPainted(false);
		confirmButton.setPreferredSize(new Dimension(180, 50));
		confirmButton.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2));
		confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText();
				String password = new String(passwordField.getPassword());
				String name = nameField.getText();
				String gender = maleButton.isSelected() ? "Nam" : (femaleButton.isSelected() ? "Nữ" : "");
				String dob = yearBox.getSelectedItem() + "-" + monthBox.getSelectedItem() + "-"
						+ dayBox.getSelectedItem();
				String email = emailField.getText();
				String phone = phoneField.getText();

				// Kiểm tra hợp lệ dữ liệu đầu vào
				if (username.isEmpty()) {
					JOptionPane.showMessageDialog(RegistrationForm.this, "Tên đăng nhập không được để trống!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (password.isEmpty() || password.length() < 6) {
					JOptionPane.showMessageDialog(RegistrationForm.this, "Mật khẩu phải chứa ít nhất 6 ký tự!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (name.isEmpty()) {
					JOptionPane.showMessageDialog(RegistrationForm.this, "Họ và tên không được để trống!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (gender.isEmpty()) {
					JOptionPane.showMessageDialog(RegistrationForm.this, "Vui lòng chọn giới tính!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (email.isEmpty() || !email.contains("@")) {
					JOptionPane.showMessageDialog(RegistrationForm.this, "Email không hợp lệ!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (phone.isEmpty() || !phone.matches("\\d{10,11}")) {
					JOptionPane.showMessageDialog(RegistrationForm.this, "Số điện thoại phải chứa 10-11 chữ số!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				// Lưu dữ liệu vào cơ sở dữ liệu
				String request = "Tư vấn về giải pháp công nghệ thông tin";
				if (saveDataToDatabase(request, username, password, name, gender, dob, email, phone)) {
					JOptionPane.showMessageDialog(RegistrationForm.this, "Cảm ơn bạn đã đăng ký khóa học!",
							"Đăng Ký Thành Công", JOptionPane.INFORMATION_MESSAGE);

					// Đóng cửa sổ đăng ký và quay lại trang chủ
					RegistrationForm.this.dispose();
					new TrangChu(); // Gọi giao diện Trang Chủ
				} else {
					JOptionPane.showMessageDialog(RegistrationForm.this, "Lỗi khi đăng ký, vui lòng thử lại.",
							"Lỗi Đăng Ký", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		JButton resetButton = new JButton("Làm mới");
		resetButton.setFont(new Font("Arial", Font.BOLD, 22));
		resetButton.setBackground(Color.GRAY);
		resetButton.setForeground(Color.WHITE);
		resetButton.setFocusPainted(false);
		resetButton.setPreferredSize(new Dimension(180, 50));
		resetButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				usernameField.setText("");
				passwordField.setText("");
				nameField.setText("");
				genderGroup.clearSelection();
				dayBox.setSelectedIndex(0);
				monthBox.setSelectedIndex(0);
				yearBox.setSelectedIndex(0);
				emailField.setText("");
				phoneField.setText("");
				termsCheckBox.setSelected(false);
			}
		});

		buttonPanel.add(confirmButton);
		buttonPanel.add(resetButton);
		backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

		setVisible(true);
	}

	private boolean saveDataToDatabase(String request, String username, String password, String name, String gender,
			String dob, String email, String phone) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dtbdemo", "root", ""); // Thay đổi cơ sở dữ
																									// liệu theo cấu
																									// hình của bạn

			// Kiểm tra xem tên người dùng đã tồn tại chưa
			String checkUserQuery = "SELECT COUNT(*) FROM thongke WHERE `tên đăng nhập` = ?";
			pstmt = conn.prepareStatement(checkUserQuery);
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next() && rs.getInt(1) > 0) {
				JOptionPane.showMessageDialog(this, "Tên đăng nhập đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
				return false;
			}

			// Thêm thông tin vào cơ sở dữ liệu
			String insertQuery = "INSERT INTO thongke (`yêu cầu tư vấn`, `tên đăng nhập`, `mật khẩu`, `tên người dùng`, `giới tính`, `ngày sinh`, `email`, `số điện thoại`, `ngày tạo`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
			pstmt = conn.prepareStatement(insertQuery);
			pstmt.setString(1, request);
			pstmt.setString(2, username);
			pstmt.setString(3, password);
			pstmt.setString(4, name);
			pstmt.setString(5, gender);
			pstmt.setString(6, dob);
			pstmt.setString(7, email);
			pstmt.setString(8, phone);

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private boolean isUsernameExists(String username) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dtbdemo", "root", ""); // Thay đổi cơ sở dữ
																									// liệu
			String query = "SELECT COUNT(*) FROM thongke WHERE `tên đăng nhập` = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();
			if (rs.next() && rs.getInt(1) > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}

class PhatTrienPhanMemFrame extends JFrame {

	public PhatTrienPhanMemFrame() {
		setTitle("Phát triển phần mềm");
		setSize(600, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// Panel chính
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBackground(new Color(238, 130, 238));

		// Tiêu đề
		JLabel titleLabel = new JLabel("Dịch Vụ Phát Triển Phần Mềm", JLabel.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setForeground(new Color(34, 49, 63));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		panel.add(titleLabel, BorderLayout.NORTH);

		JPanel servicePanel = new JPanel();
		servicePanel.setLayout(new GridLayout(0, 1, 10, 10));
		servicePanel.setBackground(new Color(245, 245, 245));

		// Dịch vụ 1: Phát triển phần mềm theo yêu cầu
		servicePanel.add(createServicePanel("Phát triển phần mềm theo yêu cầu",
				"Chúng tôi cung cấp các giải pháp phần mềm tùy chỉnh, thiết kế riêng biệt để phù hợp với nhu cầu đặc thù của doanh nghiệp."));

		// Dịch vụ 2: Phát triển ứng dụng di động (iOS, Android)
		servicePanel.add(createServicePanel("Phát triển ứng dụng di động (iOS, Android)",
				"Ứng dụng di động giúp kết nối doanh nghiệp với khách hàng dễ dàng hơn. Chúng tôi phát triển ứng dụng cho cả iOS và Android."));

		// Dịch vụ 3: Phát triển hệ thống web và ứng dụng doanh nghiệp
		servicePanel.add(createServicePanel("Phát triển hệ thống web và ứng dụng doanh nghiệp",
				"Chúng tôi phát triển các hệ thống web mạnh mẽ và các ứng dụng doanh nghiệp giúp tối ưu hóa quy trình làm việc."));

		// Dịch vụ 4: Tư vấn và triển khai giải pháp phần mềm
		servicePanel.add(createServicePanel("Tư vấn và triển khai giải pháp phần mềm",
				"Chúng tôi cung cấp dịch vụ tư vấn và triển khai các giải pháp phần mềm tối ưu cho doanh nghiệp."));

		JScrollPane serviceScrollPane = new JScrollPane(servicePanel);
		panel.add(serviceScrollPane, BorderLayout.CENTER);

		// Thông tin liên hệ
		JPanel contactPanel = new JPanel();
		contactPanel.setLayout(new BoxLayout(contactPanel, BoxLayout.Y_AXIS));
		contactPanel.setBackground(new Color(245, 245, 245));

		JLabel contactTitleLabel = new JLabel("Liên Hệ Với Chúng Tôi");
		contactTitleLabel.setFont(new Font("Arial", Font.BOLD, 18));
		contactTitleLabel.setForeground(new Color(34, 49, 63));

		JTextArea contactInfoArea = new JTextArea();
		contactInfoArea.setEditable(false);
		contactInfoArea.setText("Email: tien.dinhcong2006@gmail.com\n" + "Điện thoại: 0123456789\n"
				+ "Địa chỉ: Hoà Phước, Hoà Vang, Thành phố Đà Nẵng\n" + "Website: www.dctEntertainment.com");
		contactInfoArea.setFont(new Font("Arial", Font.PLAIN, 14));
		contactInfoArea.setLineWrap(true);
		contactInfoArea.setWrapStyleWord(true);
		contactInfoArea.setBackground(new Color(255, 255, 255));
		contactInfoArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

		contactPanel.add(contactTitleLabel);
		contactPanel.add(Box.createVerticalStrut(10));
		contactPanel.add(new JScrollPane(contactInfoArea));

		// Panel cho các nút (nằm ngang)
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		buttonPanel.setBackground(new Color(245, 245, 245));

		// Nút Đăng ký dùng thử
		JButton trialButton = new JButton("Đăng ký dùng thử");
		trialButton.setFont(new Font("Arial", Font.BOLD, 16));
		trialButton.setBackground(new Color(40, 167, 69));
		trialButton.setForeground(Color.WHITE);
		trialButton.setPreferredSize(new Dimension(150, 40));
		trialButton.setFocusPainted(false);

		// Thêm sự kiện cho nút Đăng ký dùng thử
		trialButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Registration();
				dispose();
			}
		});

		// Nút Quay lại trang chủ
		JButton backButton = new JButton("Quay lại trang chủ");
		backButton.setFont(new Font("Arial", Font.BOLD, 16));
		backButton.setBackground(new Color(255, 87, 34)); // Màu cam
		backButton.setForeground(Color.WHITE);
		backButton.setPreferredSize(new Dimension(150, 40));
		backButton.setFocusPainted(false);

		// Thêm sự kiện cho nút quay lại trang chủ
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Đóng cửa sổ hiện tại và quay lại trang chủ (có thể là một JFrame khác)
				JOptionPane.showMessageDialog(PhatTrienPhanMemFrame.this, "Đang quay lại trang chủ!", "Thông báo",
						JOptionPane.INFORMATION_MESSAGE);
				dispose(); // Đóng cửa sổ hiện tại
				new TrangChu(); // Khởi tạo trang chủ (TrangChuFrame là lớp JFrame đại diện cho trang chủ)
			}
		});

		// Thêm các nút vào buttonPanel
		buttonPanel.add(trialButton);
		buttonPanel.add(backButton);

		// Thêm phần buttonPanel vào contactPanel dưới cùng
		contactPanel.add(Box.createVerticalStrut(10));
		contactPanel.add(buttonPanel);

		// Thêm phần thông tin liên hệ vào panel chính
		panel.add(contactPanel, BorderLayout.SOUTH);

		// Thêm panel vào JFrame
		add(panel, BorderLayout.CENTER);

		// Hiển thị JFrame
		setVisible(true);
	}

	// Hàm tạo Panel cho mỗi dịch vụ
	private JPanel createServicePanel(String serviceTitle, String serviceDescription) {
		JPanel servicePanel = new JPanel();
		servicePanel.setLayout(new BorderLayout());
		servicePanel.setBackground(new Color(255, 255, 255)); // Nền trắng cho mỗi dịch vụ

		JLabel titleLabel = new JLabel(serviceTitle);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
		titleLabel.setForeground(new Color(34, 49, 63));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

		JTextArea descriptionArea = new JTextArea();
		descriptionArea.setEditable(false);
		descriptionArea.setText(serviceDescription);
		descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
		descriptionArea.setLineWrap(true);
		descriptionArea.setWrapStyleWord(true);
		descriptionArea.setBackground(new Color(255, 255, 255));
		descriptionArea.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

		servicePanel.add(titleLabel, BorderLayout.NORTH);
		servicePanel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);

		return servicePanel;
	}
}

class Registration extends JFrame {
	public Registration() {
		setTitle("Đăng Ký Khóa Học");
		setSize(600, 700); // Tăng kích thước form
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);

		JPanel backgroundPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				int width = getWidth();
				int height = getHeight();
				Color color1 = new Color(147, 112, 219);
				Color color2 = new Color(218, 112, 214);
				GradientPaint gradient = new GradientPaint(0, 0, color1, width, height, color2);
				g2d.setPaint(gradient);
				g2d.fillRect(0, 0, width, height);
			}
		};
		backgroundPanel.setLayout(new BorderLayout());
		add(backgroundPanel);

		// Tiêu đề
		JLabel titleLabel = new JLabel("Tư vấn CNTT", JLabel.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
		backgroundPanel.add(titleLabel, BorderLayout.NORTH);

		// Form đăng ký sử dụng GridBagLayout
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new GridBagLayout());
		formPanel.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.WEST;

		// Tên đăng nhập
		JLabel usernameLabel = new JLabel("Tên đăng nhập:");
		usernameLabel.setForeground(Color.WHITE);
		usernameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 0;
		formPanel.add(usernameLabel, gbc);

		JTextField usernameField = new JTextField(20);
		usernameField.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 1;
		formPanel.add(usernameField, gbc);

		// Mật khẩu
		JLabel passwordLabel = new JLabel("Mật khẩu:");
		passwordLabel.setForeground(Color.WHITE);
		passwordLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 1;
		formPanel.add(passwordLabel, gbc);

		JPasswordField passwordField = new JPasswordField(20);
		passwordField.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 1;
		formPanel.add(passwordField, gbc);

		// Họ và tên
		JLabel nameLabel = new JLabel("Họ và tên:");
		nameLabel.setForeground(Color.WHITE);
		nameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 2;
		formPanel.add(nameLabel, gbc);

		JTextField nameField = new JTextField(20);
		nameField.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 1;
		formPanel.add(nameField, gbc);

		// Giới tính
		JLabel genderLabel = new JLabel("Giới tính:");
		genderLabel.setForeground(Color.WHITE);
		genderLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 3;
		formPanel.add(genderLabel, gbc);

		JRadioButton maleButton = new JRadioButton("Nam");
		maleButton.setOpaque(false);
		JRadioButton femaleButton = new JRadioButton("Nữ");
		femaleButton.setOpaque(false);
		ButtonGroup genderGroup = new ButtonGroup();
		genderGroup.add(maleButton);
		genderGroup.add(femaleButton);

		JPanel genderPanel = new JPanel();
		genderPanel.setOpaque(false);
		genderPanel.add(maleButton);
		genderPanel.add(femaleButton);

		gbc.gridx = 1;
		formPanel.add(genderPanel, gbc);

		// Ngày sinh
		JLabel dobLabel = new JLabel("Ngày sinh:");
		dobLabel.setForeground(Color.WHITE);
		dobLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 4;
		formPanel.add(dobLabel, gbc);

		JComboBox<String> dayBox = new JComboBox<>();
		for (int i = 1; i <= 31; i++) {
			dayBox.addItem(String.format("%02d", i));
		}

		JComboBox<String> monthBox = new JComboBox<>();
		for (int i = 1; i <= 12; i++) {
			monthBox.addItem(String.format("%02d", i));
		}

		JComboBox<String> yearBox = new JComboBox<>();
		for (int i = 1900; i <= 2025; i++) {
			yearBox.addItem(String.valueOf(i));
		}

		JPanel dobPanel = new JPanel();
		dobPanel.setOpaque(false);
		dobPanel.add(dayBox);
		dobPanel.add(monthBox);
		dobPanel.add(yearBox);

		gbc.gridx = 1;
		formPanel.add(dobPanel, gbc);

		// Email
		JLabel emailLabel = new JLabel("Email:");
		emailLabel.setForeground(Color.WHITE);
		emailLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 5;
		formPanel.add(emailLabel, gbc);

		JTextField emailField = new JTextField(20);
		emailField.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 1;
		formPanel.add(emailField, gbc);

		// Số điện thoại
		JLabel phoneLabel = new JLabel("Số điện thoại:");
		phoneLabel.setForeground(Color.WHITE);
		phoneLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 6;
		formPanel.add(phoneLabel, gbc);

		JTextField phoneField = new JTextField(20);
		phoneField.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 1;
		formPanel.add(phoneField, gbc);

		// Checkbox đồng ý điều khoản
		JCheckBox termsCheckBox = new JCheckBox("Tôi đồng ý với các điều khoản và điều kiện.");
		termsCheckBox.setFont(new Font("Arial", Font.PLAIN, 16));
		termsCheckBox.setOpaque(false);
		gbc.gridx = 1;
		gbc.gridy = 7;
		formPanel.add(termsCheckBox, gbc);

		backgroundPanel.add(formPanel, BorderLayout.CENTER);

		// Nút Xác Nhận và Reset
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		buttonPanel.setOpaque(false);

		JButton confirmButton = new JButton("Xác Nhận");
		confirmButton.setFont(new Font("Arial", Font.BOLD, 22));
		confirmButton.setBackground(new Color(34, 139, 34));
		confirmButton.setForeground(Color.WHITE);
		confirmButton.setFocusPainted(false);
		confirmButton.setPreferredSize(new Dimension(180, 50));
		confirmButton.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2));
		confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText();
				String password = new String(passwordField.getPassword());
				String name = nameField.getText();
				String gender = maleButton.isSelected() ? "Nam" : (femaleButton.isSelected() ? "Nữ" : "");
				String dob = yearBox.getSelectedItem() + "-" + monthBox.getSelectedItem() + "-"
						+ dayBox.getSelectedItem();
				String email = emailField.getText();
				String phone = phoneField.getText();

				// Kiểm tra hợp lệ dữ liệu đầu vào
				if (username.isEmpty()) {
					JOptionPane.showMessageDialog(Registration.this, "Tên đăng nhập không được để trống!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (password.isEmpty() || password.length() < 6) {
					JOptionPane.showMessageDialog(Registration.this, "Mật khẩu phải chứa ít nhất 6 ký tự!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (name.isEmpty()) {
					JOptionPane.showMessageDialog(Registration.this, "Họ và tên không được để trống!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (gender.isEmpty()) {
					JOptionPane.showMessageDialog(Registration.this, "Vui lòng chọn giới tính!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (email.isEmpty() || !email.contains("@")) {
					JOptionPane.showMessageDialog(Registration.this, "Email không hợp lệ!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (phone.isEmpty() || !phone.matches("\\d{10,11}")) {
					JOptionPane.showMessageDialog(Registration.this, "Số điện thoại phải chứa 10-11 chữ số!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				// Lưu dữ liệu vào cơ sở dữ liệu
				String request = "Tư vấn về giải pháp công nghệ thông tin";
				if (saveDataToDatabase(request, username, password, name, gender, dob, email, phone)) {
					JOptionPane.showMessageDialog(Registration.this, "Cảm ơn bạn đã đăng ký khóa học!",
							"Đăng Ký Thành Công", JOptionPane.INFORMATION_MESSAGE);

					// Đóng cửa sổ đăng ký và quay lại trang chủ
					Registration.this.dispose();
					new TrangChu(); // Gọi giao diện Trang Chủ
				} else {
					JOptionPane.showMessageDialog(Registration.this, "Lỗi khi đăng ký, vui lòng thử lại.",
							"Lỗi Đăng Ký", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		JButton resetButton = new JButton("Làm mới");
		resetButton.setFont(new Font("Arial", Font.BOLD, 22));
		resetButton.setBackground(Color.GRAY);
		resetButton.setForeground(Color.WHITE);
		resetButton.setFocusPainted(false);
		resetButton.setPreferredSize(new Dimension(180, 50));
		resetButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				usernameField.setText("");
				passwordField.setText("");
				nameField.setText("");
				genderGroup.clearSelection();
				dayBox.setSelectedIndex(0);
				monthBox.setSelectedIndex(0);
				yearBox.setSelectedIndex(0);
				emailField.setText("");
				phoneField.setText("");
				termsCheckBox.setSelected(false);
			}
		});

		buttonPanel.add(confirmButton);
		buttonPanel.add(resetButton);
		backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

		setVisible(true);
	}

	private boolean saveDataToDatabase(String request, String username, String password, String name, String gender,
			String dob, String email, String phone) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dtbdemo", "root", ""); // Thay đổi cơ sở dữ
																									// liệu theo cấu
																									// hình của bạn

			// Kiểm tra xem tên người dùng đã tồn tại chưa
			String checkUserQuery = "SELECT COUNT(*) FROM thongke WHERE `tên đăng nhập` = ?";
			pstmt = conn.prepareStatement(checkUserQuery);
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next() && rs.getInt(1) > 0) {
				JOptionPane.showMessageDialog(this, "Tên đăng nhập đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
				return false;
			}

			// Thêm thông tin vào cơ sở dữ liệu
			String insertQuery = "INSERT INTO thongke (`yêu cầu tư vấn`, `tên đăng nhập`, `mật khẩu`, `tên người dùng`, `giới tính`, `ngày sinh`, `email`, `số điện thoại`, `ngày tạo`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
			pstmt = conn.prepareStatement(insertQuery);
			pstmt.setString(1, request);
			pstmt.setString(2, username);
			pstmt.setString(3, password);
			pstmt.setString(4, name);
			pstmt.setString(5, gender);
			pstmt.setString(6, dob);
			pstmt.setString(7, email);
			pstmt.setString(8, phone);

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private boolean isUsernameExists(String username) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dtbdemo", "root", ""); // Thay đổi cơ sở dữ
																									// liệu
			String query = "SELECT COUNT(*) FROM thongke WHERE `tên đăng nhập` = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();
			if (rs.next() && rs.getInt(1) > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}

class BaoMatPhanMemFrame extends JFrame {

	public BaoMatPhanMemFrame() {
		// Thiết lập JFrame
		setTitle("Phát triển phần mềm bảo mật");
		setSize(600, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// Panel chính
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBackground(new Color(238, 130, 238));

		// Tiêu đề
		JLabel titleLabel = new JLabel("Dịch Vụ Phát Triển Phần Mềm Bảo Mật", JLabel.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setForeground(new Color(34, 49, 63));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		panel.add(titleLabel, BorderLayout.NORTH);

		// Panel mô tả dịch vụ bảo mật với các dịch vụ riêng biệt
		JPanel servicePanel = new JPanel();
		servicePanel.setLayout(new GridLayout(0, 1, 10, 10)); // Sử dụng GridLayout để bố trí các dịch vụ
		servicePanel.setBackground(new Color(245, 245, 245));

		// Dịch vụ 1: Phân tích và đánh giá bảo mật phần mềm
		servicePanel.add(createServicePanel("Phân tích và đánh giá bảo mật phần mềm",
				"Chúng tôi tiến hành đánh giá toàn diện về mức độ bảo mật của phần mềm, từ đó đưa ra các biện pháp cải thiện."));

		// Dịch vụ 2: Kiểm tra lỗ hổng bảo mật
		servicePanel.add(createServicePanel("Kiểm tra lỗ hổng bảo mật",
				"Chúng tôi sử dụng các công cụ và phương pháp hiện đại để tìm kiếm các lỗ hổng bảo mật và đảm bảo an toàn tối đa."));

		// Dịch vụ 3: Xây dựng các giải pháp bảo mật phần mềm
		servicePanel.add(createServicePanel("Xây dựng các giải pháp bảo mật phần mềm",
				"Cung cấp các giải pháp bảo mật từ đầu cho các phần mềm, giúp ngăn chặn các mối đe dọa tiềm ẩn ngay từ khi bắt đầu phát triển."));

		// Dịch vụ 4: Đảm bảo tính bảo mật trong suốt vòng đời phát triển phần mềm
		servicePanel.add(createServicePanel("Đảm bảo tính bảo mật trong suốt vòng đời phát triển phần mềm",
				"Chúng tôi theo dõi và duy trì mức độ bảo mật trong suốt quá trình phát triển phần mềm, từ khi bắt đầu đến khi triển khai."));

		// Thêm mô tả dịch vụ vào JScrollPane để có thể cuộn được
		JScrollPane serviceScrollPane = new JScrollPane(servicePanel);
		panel.add(serviceScrollPane, BorderLayout.CENTER);

		// Thông tin liên hệ
		JPanel contactPanel = new JPanel();
		contactPanel.setLayout(new BoxLayout(contactPanel, BoxLayout.Y_AXIS));
		contactPanel.setBackground(new Color(245, 245, 245));

		JLabel contactTitleLabel = new JLabel("Liên Hệ Với Chúng Tôi");
		contactTitleLabel.setFont(new Font("Arial", Font.BOLD, 18));
		contactTitleLabel.setForeground(new Color(34, 49, 63));

		JTextArea contactInfoArea = new JTextArea();
		contactInfoArea.setEditable(false);
		contactInfoArea.setText("Email: tien.dinhcong2006@gmail.com\n" + "Điện thoại: 0123456789\n"
				+ "Địa chỉ: Hoà Phước, Hoà Vang, Thành phố Đà Nẵng\n" + "Website: www.dctEntertainment.com");
		contactInfoArea.setFont(new Font("Arial", Font.PLAIN, 14));
		contactInfoArea.setLineWrap(true);
		contactInfoArea.setWrapStyleWord(true);
		contactInfoArea.setBackground(new Color(255, 255, 255));
		contactInfoArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

		contactPanel.add(contactTitleLabel);
		contactPanel.add(Box.createVerticalStrut(10));
		contactPanel.add(new JScrollPane(contactInfoArea));

		// Panel cho các nút (nằm ngang)
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Các nút xếp ngang
		buttonPanel.setBackground(new Color(245, 245, 245));

		// Nút đăng ký dùng thử dịch vụ
		JButton trialButton = new JButton("Đăng ký dùng thử");
		trialButton.setFont(new Font("Arial", Font.BOLD, 16));
		trialButton.setBackground(new Color(40, 167, 69)); // Màu xanh lá
		trialButton.setForeground(Color.WHITE);
		trialButton.setPreferredSize(new Dimension(150, 40));
		trialButton.setFocusPainted(false);

		// Thêm sự kiện cho nút Đăng ký dùng thử
		trialButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Registration1();
				dispose();
			}
		});

		// Nút Quay lại trang chủ
		JButton backButton = new JButton("Quay lại trang chủ");
		backButton.setFont(new Font("Arial", Font.BOLD, 16));
		backButton.setBackground(new Color(255, 87, 34)); // Màu cam
		backButton.setForeground(Color.WHITE);
		backButton.setPreferredSize(new Dimension(150, 40));
		backButton.setFocusPainted(false);

		// Thêm sự kiện cho nút quay lại trang chủ
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Đóng cửa sổ hiện tại và quay lại trang chủ (có thể là một JFrame khác)
				JOptionPane.showMessageDialog(BaoMatPhanMemFrame.this, "Đang quay lại trang chủ!", "Thông báo",
						JOptionPane.INFORMATION_MESSAGE);
				dispose(); // Đóng cửa sổ hiện tại
				new TrangChu(); // Khởi tạo trang chủ (TrangChuFrame là lớp JFrame đại diện cho trang chủ)
			}
		});

		// Thêm các nút vào buttonPanel
		buttonPanel.add(trialButton);
		buttonPanel.add(backButton);

		// Thêm phần buttonPanel vào phần contactPanel dưới cùng
		contactPanel.add(Box.createVerticalStrut(10));
		contactPanel.add(buttonPanel);

		// Thêm phần thông tin liên hệ vào panel chính
		panel.add(contactPanel, BorderLayout.SOUTH);

		// Thêm panel vào JFrame
		add(panel, BorderLayout.CENTER);

		// Hiển thị JFrame
		setVisible(true);
	}

	// Hàm tạo Panel cho mỗi dịch vụ
	private JPanel createServicePanel(String serviceTitle, String serviceDescription) {
		JPanel servicePanel = new JPanel();
		servicePanel.setLayout(new BorderLayout());
		servicePanel.setBackground(new Color(255, 255, 255)); // Nền trắng cho mỗi dịch vụ

		JLabel titleLabel = new JLabel(serviceTitle);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
		titleLabel.setForeground(new Color(34, 49, 63));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

		JTextArea descriptionArea = new JTextArea();
		descriptionArea.setEditable(false);
		descriptionArea.setText(serviceDescription);
		descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
		descriptionArea.setLineWrap(true);
		descriptionArea.setWrapStyleWord(true);
		descriptionArea.setBackground(new Color(255, 255, 255));
		descriptionArea.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

		servicePanel.add(titleLabel, BorderLayout.NORTH);
		servicePanel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);

		return servicePanel;
	}
}

class Registration1 extends JFrame {
	public Registration1() {
		setTitle("Đăng Ký Khóa Học");
		setSize(600, 700); // Tăng kích thước form
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);

		JPanel backgroundPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				int width = getWidth();
				int height = getHeight();
				Color color1 = new Color(147, 112, 219);
				Color color2 = new Color(218, 112, 214);
				GradientPaint gradient = new GradientPaint(0, 0, color1, width, height, color2);
				g2d.setPaint(gradient);
				g2d.fillRect(0, 0, width, height);
			}
		};
		backgroundPanel.setLayout(new BorderLayout());
		add(backgroundPanel);

		// Tiêu đề
		JLabel titleLabel = new JLabel("Tư vấn CNTT", JLabel.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
		backgroundPanel.add(titleLabel, BorderLayout.NORTH);

		// Form đăng ký sử dụng GridBagLayout
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new GridBagLayout());
		formPanel.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.WEST;

		// Tên đăng nhập
		JLabel usernameLabel = new JLabel("Tên đăng nhập:");
		usernameLabel.setForeground(Color.WHITE);
		usernameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 0;
		formPanel.add(usernameLabel, gbc);

		JTextField usernameField = new JTextField(20);
		usernameField.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 1;
		formPanel.add(usernameField, gbc);

		// Mật khẩu
		JLabel passwordLabel = new JLabel("Mật khẩu:");
		passwordLabel.setForeground(Color.WHITE);
		passwordLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 1;
		formPanel.add(passwordLabel, gbc);

		JPasswordField passwordField = new JPasswordField(20);
		passwordField.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 1;
		formPanel.add(passwordField, gbc);

		// Họ và tên
		JLabel nameLabel = new JLabel("Họ và tên:");
		nameLabel.setForeground(Color.WHITE);
		nameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 2;
		formPanel.add(nameLabel, gbc);

		JTextField nameField = new JTextField(20);
		nameField.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 1;
		formPanel.add(nameField, gbc);

		// Giới tính
		JLabel genderLabel = new JLabel("Giới tính:");
		genderLabel.setForeground(Color.WHITE);
		genderLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 3;
		formPanel.add(genderLabel, gbc);

		JRadioButton maleButton = new JRadioButton("Nam");
		maleButton.setOpaque(false);
		JRadioButton femaleButton = new JRadioButton("Nữ");
		femaleButton.setOpaque(false);
		ButtonGroup genderGroup = new ButtonGroup();
		genderGroup.add(maleButton);
		genderGroup.add(femaleButton);

		JPanel genderPanel = new JPanel();
		genderPanel.setOpaque(false);
		genderPanel.add(maleButton);
		genderPanel.add(femaleButton);

		gbc.gridx = 1;
		formPanel.add(genderPanel, gbc);

		// Ngày sinh
		JLabel dobLabel = new JLabel("Ngày sinh:");
		dobLabel.setForeground(Color.WHITE);
		dobLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 4;
		formPanel.add(dobLabel, gbc);

		JComboBox<String> dayBox = new JComboBox<>();
		for (int i = 1; i <= 31; i++) {
			dayBox.addItem(String.format("%02d", i));
		}

		JComboBox<String> monthBox = new JComboBox<>();
		for (int i = 1; i <= 12; i++) {
			monthBox.addItem(String.format("%02d", i));
		}

		JComboBox<String> yearBox = new JComboBox<>();
		for (int i = 1900; i <= 2025; i++) {
			yearBox.addItem(String.valueOf(i));
		}

		JPanel dobPanel = new JPanel();
		dobPanel.setOpaque(false);
		dobPanel.add(dayBox);
		dobPanel.add(monthBox);
		dobPanel.add(yearBox);

		gbc.gridx = 1;
		formPanel.add(dobPanel, gbc);

		// Email
		JLabel emailLabel = new JLabel("Email:");
		emailLabel.setForeground(Color.WHITE);
		emailLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 5;
		formPanel.add(emailLabel, gbc);

		JTextField emailField = new JTextField(20);
		emailField.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 1;
		formPanel.add(emailField, gbc);

		// Số điện thoại
		JLabel phoneLabel = new JLabel("Số điện thoại:");
		phoneLabel.setForeground(Color.WHITE);
		phoneLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 6;
		formPanel.add(phoneLabel, gbc);

		JTextField phoneField = new JTextField(20);
		phoneField.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 1;
		formPanel.add(phoneField, gbc);

		// Checkbox đồng ý điều khoản
		JCheckBox termsCheckBox = new JCheckBox("Tôi đồng ý với các điều khoản và điều kiện.");
		termsCheckBox.setFont(new Font("Arial", Font.PLAIN, 16));
		termsCheckBox.setOpaque(false);
		gbc.gridx = 1;
		gbc.gridy = 7;
		formPanel.add(termsCheckBox, gbc);

		backgroundPanel.add(formPanel, BorderLayout.CENTER);

		// Nút Xác Nhận và Reset
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		buttonPanel.setOpaque(false);

		JButton confirmButton = new JButton("Xác Nhận");
		confirmButton.setFont(new Font("Arial", Font.BOLD, 22));
		confirmButton.setBackground(new Color(34, 139, 34));
		confirmButton.setForeground(Color.WHITE);
		confirmButton.setFocusPainted(false);
		confirmButton.setPreferredSize(new Dimension(180, 50));
		confirmButton.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2));
		confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText();
				String password = new String(passwordField.getPassword());
				String name = nameField.getText();
				String gender = maleButton.isSelected() ? "Nam" : (femaleButton.isSelected() ? "Nữ" : "");
				String dob = yearBox.getSelectedItem() + "-" + monthBox.getSelectedItem() + "-"
						+ dayBox.getSelectedItem();
				String email = emailField.getText();
				String phone = phoneField.getText();

				// Kiểm tra hợp lệ dữ liệu đầu vào
				if (username.isEmpty()) {
					JOptionPane.showMessageDialog(Registration1.this, "Tên đăng nhập không được để trống!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (password.isEmpty() || password.length() < 6) {
					JOptionPane.showMessageDialog(Registration1.this, "Mật khẩu phải chứa ít nhất 6 ký tự!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (name.isEmpty()) {
					JOptionPane.showMessageDialog(Registration1.this, "Họ và tên không được để trống!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (gender.isEmpty()) {
					JOptionPane.showMessageDialog(Registration1.this, "Vui lòng chọn giới tính!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (email.isEmpty() || !email.contains("@")) {
					JOptionPane.showMessageDialog(Registration1.this, "Email không hợp lệ!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (phone.isEmpty() || !phone.matches("\\d{10,11}")) {
					JOptionPane.showMessageDialog(Registration1.this, "Số điện thoại phải chứa 10-11 chữ số!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				// Lưu dữ liệu vào cơ sở dữ liệu
				String request = "Tư vấn về giải pháp công nghệ thông tin";
				if (saveDataToDatabase(request, username, password, name, gender, dob, email, phone)) {
					JOptionPane.showMessageDialog(Registration1.this, "Cảm ơn bạn đã đăng ký khóa học!",
							"Đăng Ký Thành Công", JOptionPane.INFORMATION_MESSAGE);

					// Đóng cửa sổ đăng ký và quay lại trang chủ
					Registration1.this.dispose();
					new TrangChu(); // Gọi giao diện Trang Chủ
				} else {
					JOptionPane.showMessageDialog(Registration1.this, "Lỗi khi đăng ký, vui lòng thử lại.",
							"Lỗi Đăng Ký", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		JButton resetButton = new JButton("Làm mới");
		resetButton.setFont(new Font("Arial", Font.BOLD, 22));
		resetButton.setBackground(Color.GRAY);
		resetButton.setForeground(Color.WHITE);
		resetButton.setFocusPainted(false);
		resetButton.setPreferredSize(new Dimension(180, 50));
		resetButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				usernameField.setText("");
				passwordField.setText("");
				nameField.setText("");
				genderGroup.clearSelection();
				dayBox.setSelectedIndex(0);
				monthBox.setSelectedIndex(0);
				yearBox.setSelectedIndex(0);
				emailField.setText("");
				phoneField.setText("");
				termsCheckBox.setSelected(false);
			}
		});

		buttonPanel.add(confirmButton);
		buttonPanel.add(resetButton);
		backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

		setVisible(true);
	}

	private boolean saveDataToDatabase(String request, String username, String password, String name, String gender,
			String dob, String email, String phone) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dtbdemo", "root", ""); // Thay đổi cơ sở dữ
																									// liệu theo cấu
																									// hình của bạn

			// Kiểm tra xem tên người dùng đã tồn tại chưa
			String checkUserQuery = "SELECT COUNT(*) FROM thongke WHERE `tên đăng nhập` = ?";
			pstmt = conn.prepareStatement(checkUserQuery);
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next() && rs.getInt(1) > 0) {
				JOptionPane.showMessageDialog(this, "Tên đăng nhập đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
				return false;
			}

			// Thêm thông tin vào cơ sở dữ liệu
			String insertQuery = "INSERT INTO thongke (`yêu cầu tư vấn`, `tên đăng nhập`, `mật khẩu`, `tên người dùng`, `giới tính`, `ngày sinh`, `email`, `số điện thoại`, `ngày tạo`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
			pstmt = conn.prepareStatement(insertQuery);
			pstmt.setString(1, request);
			pstmt.setString(2, username);
			pstmt.setString(3, password);
			pstmt.setString(4, name);
			pstmt.setString(5, gender);
			pstmt.setString(6, dob);
			pstmt.setString(7, email);
			pstmt.setString(8, phone);

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private boolean isUsernameExists(String username) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dtbdemo", "root", ""); // Thay đổi cơ sở dữ
																									// liệu
			String query = "SELECT COUNT(*) FROM thongke WHERE `tên đăng nhập` = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();
			if (rs.next() && rs.getInt(1) > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}

//các khoá học 
class JavaCourseFrame extends JFrame {
	public JavaCourseFrame() {
		// Thiết lập JFrame
		setTitle("Khóa học Java");
		setSize(800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// Panel chính
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(new Color(240, 248, 255));

		// Tiêu đề chính
		JLabel titleLabel = new JLabel("Chào mừng bạn đến với Khóa học Java!", JLabel.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
		titleLabel.setForeground(new Color(0, 102, 204));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		mainPanel.add(titleLabel, BorderLayout.NORTH);

		// Nội dung khóa học
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new GridLayout(2, 2, 10, 10));
		contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		contentPanel.setBackground(new Color(240, 248, 255));

		// Phần giới thiệu tổng quát
		JTextArea introductionArea = new JTextArea("Khóa học Java giúp bạn học từ cơ bản đến nâng cao."
				+ "Bạn sẽ được học cách sử dụng Java để giải quyết các bài toán thực tế.");
		introductionArea.setFont(new Font("Arial", Font.PLAIN, 16));
		introductionArea.setLineWrap(true);
		introductionArea.setWrapStyleWord(true);
		introductionArea.setEditable(false);
		introductionArea.setBackground(new Color(255, 255, 255));
		introductionArea.setBorder(BorderFactory.createTitledBorder("Giới thiệu"));
		introductionArea.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Thêm hình ảnh minh họa cho phần giới thiệu
		ImageIcon introImage = new ImageIcon("C:\\Users\\Admin\\eclipse-workspace\\GIAODIEN\\bin\\Image\\java.jpg");
		Image img = introImage.getImage();

		// Điều chỉnh kích thước ảnh sao cho phù hợp (300x200 pixels)
		Image scaledImg = img.getScaledInstance(500, 400, Image.SCALE_SMOOTH);

		// Tạo lại ImageIcon với ảnh đã thay đổi kích thước
		introImage = new ImageIcon(scaledImg);

		// Tạo JLabel chứa ảnh đã thay đổi kích thước (không có khung)
		JLabel introImageLabel = new JLabel(introImage);
		introImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Căn giữa ảnh

		// Phần danh sách bài học
		JTextArea lessonsArea = new JTextArea("• Giới thiệu về Java\n" + "• Các cấu trúc điều khiển\n"
				+ "• Lập trình hướng đối tượng\n" + "• Thư viện Java phổ biến");
		lessonsArea.setFont(new Font("Arial", Font.PLAIN, 16));
		lessonsArea.setLineWrap(true);
		lessonsArea.setWrapStyleWord(true);
		lessonsArea.setEditable(false);
		lessonsArea.setBackground(new Color(255, 255, 255));
		lessonsArea.setBorder(BorderFactory.createTitledBorder("Danh sách bài học"));
		lessonsArea.setAlignmentX(Component.CENTER_ALIGNMENT);

		ImageIcon lessonsImage = new ImageIcon("C:\\Users\\Admin\\eclipse-workspace\\GIAODIEN\\bin\\Image\\java1.png");
		Image lessonsImg = lessonsImage.getImage();

		Image scaledLessonsImg = lessonsImg.getScaledInstance(500, 400, Image.SCALE_SMOOTH);

		lessonsImage = new ImageIcon(scaledLessonsImg);

		JLabel lessonsImageLabel = new JLabel(lessonsImage);

		contentPanel.add(introductionArea);
		contentPanel.add(introImageLabel);
		contentPanel.add(lessonsArea);
		contentPanel.add(lessonsImageLabel);

		// Thông tin liên hệ
		JLabel contactLabel = new JLabel("Liên hệ: tien.dinhcong2006@gmail.com | Hotline: 0123 456 789", JLabel.CENTER);
		contactLabel.setFont(new Font("Arial", Font.ITALIC, 14));
		contactLabel.setForeground(new Color(100, 100, 100));
		mainPanel.add(contactLabel, BorderLayout.SOUTH);

		mainPanel.add(contentPanel, BorderLayout.CENTER);

		// Nút Quay lại trang chủ
		JButton backButton = new JButton("Quay lại Trang chủ");
		backButton.setFont(new Font("Arial", Font.BOLD, 16));
		backButton.setBackground(new Color(255, 102, 102));
		backButton.setForeground(Color.WHITE);
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(JavaCourseFrame.this, "Quay lại Trang chủ", "Thông báo",
						JOptionPane.INFORMATION_MESSAGE);
				dispose();
				new TrangChu();
			}
		});

		// Nút Đăng ký khóa học
		JButton registerButton = new JButton("Đăng ký khóa học");
		registerButton.setFont(new Font("Arial", Font.BOLD, 16));
		registerButton.setBackground(new Color(0, 123, 255));
		registerButton.setForeground(Color.WHITE);
		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openRegistrationDialog();
			}
		});

		// Tạo JPanel chứa các nút
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
		buttonPanel.add(backButton);
		buttonPanel.add(registerButton);

		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(mainPanel);

		setVisible(true);
	}

	// Phương thức mở cửa sổ đăng ký khóa học
	private void openRegistrationDialog() {
		JDialog registrationDialog = new JDialog(this, "Đăng ký khóa học Java", true);
		registrationDialog.setSize(400, 300);
		registrationDialog.setLayout(new BorderLayout());

		JPanel formPanel = new JPanel();
		formPanel.setLayout(new GridLayout(4, 2, 10, 10));

		formPanel.add(new JLabel("Họ và Tên:"));
		JTextField nameField = new JTextField();
		formPanel.add(nameField);

		formPanel.add(new JLabel("Email:"));
		JTextField emailField = new JTextField();
		formPanel.add(emailField);

		formPanel.add(new JLabel("Số điện thoại:"));
		JTextField phoneField = new JTextField();
		formPanel.add(phoneField);

		formPanel.add(new JLabel("Chọn thời gian bắt đầu:"));
		JTextField startTimeField = new JTextField();
		formPanel.add(startTimeField);

		registrationDialog.add(formPanel, BorderLayout.CENTER);

		// Nút gửi đăng ký
		JButton submitButton = new JButton("Đăng ký");
		submitButton.setFont(new Font("Arial", Font.BOLD, 16));
		submitButton.setBackground(new Color(0, 123, 255)); // Màu xanh dương
		submitButton.setForeground(Color.WHITE);
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Xử lý đăng ký khóa học
				String name = nameField.getText();
				String email = emailField.getText();
				String phone = phoneField.getText();
				String startTime = startTimeField.getText();

				// Kiểm tra dữ liệu hợp lệ (có thể thêm kiểm tra thêm ở đây)
				if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || startTime.isEmpty()) {
					JOptionPane.showMessageDialog(registrationDialog, "Vui lòng điền đầy đủ thông tin!", "Thông báo",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				JOptionPane.showMessageDialog(registrationDialog, "Đăng ký thành công!", "Thông báo",
						JOptionPane.INFORMATION_MESSAGE);
				registrationDialog.dispose(); // Đóng cửa sổ đăng ký
			}
		});

		registrationDialog.add(submitButton, BorderLayout.SOUTH);

		// Hiển thị cửa sổ đăng ký khóa học
		registrationDialog.setLocationRelativeTo(this);
		registrationDialog.setVisible(true);
	}
}

//Khoá học Python
class PythonCourseFrame extends JFrame {
	public PythonCourseFrame() {
		setTitle("Khóa học Python");
		setSize(800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// Thiết kế màu nền chính
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(new Color(240, 248, 255));

		// Tiêu đề chính
		JLabel titleLabel = new JLabel("Chào mừng bạn đến với Khóa học Python!", JLabel.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
		titleLabel.setForeground(new Color(0, 102, 204)); 
		titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		mainPanel.add(titleLabel, BorderLayout.NORTH);

		// Nội dung khóa học
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new GridLayout(2, 2, 10, 10)); 
		contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		contentPanel.setBackground(new Color(240, 248, 255));

		// Phần giới thiệu tổng quát
		JTextArea introductionArea = new JTextArea(
				"<html><div style='text-align: center;'>Khóa học Python giúp bạn học từ cơ bản đến nâng cao.<br>Bạn sẽ được học cách sử dụng Python để giải quyết các bài toán thực tế.</div></html>");
		introductionArea.setFont(new Font("Arial", Font.PLAIN, 16));
		introductionArea.setLineWrap(true);
		introductionArea.setWrapStyleWord(true);
		introductionArea.setEditable(false);
		introductionArea.setBackground(new Color(255, 255, 255));
		introductionArea.setBorder(BorderFactory.createTitledBorder("Giới thiệu"));
		introductionArea.setAlignmentX(Component.CENTER_ALIGNMENT); 
		
		// Tải ảnh từ đường dẫn
		ImageIcon introImage = new ImageIcon("C:\\Users\\Admin\\eclipse-workspace\\GIAODIEN\\bin\\Image\\python.jpg");

		// Điều chỉnh kích thước ảnh
		Image image = introImage.getImage().getScaledInstance(700, 600, Image.SCALE_SMOOTH);

		ImageIcon scaledImageIcon = new ImageIcon(image);

		// Tạo JLabel với ảnh đã được thay đổi kích thước
		JLabel introImageLabel = new JLabel(scaledImageIcon); 
		introImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Phần danh sách bài học
		JTextArea lessonsArea = new JTextArea("• Cú pháp Python cơ bản\n" + "• Lập trình hướng đối tượng\n"
				+ "• Quản lý và phân tích dữ liệu\n" + "• Xây dựng ứng dụng thực tế");
		lessonsArea.setFont(new Font("Arial", Font.PLAIN, 16));
		lessonsArea.setLineWrap(true);
		lessonsArea.setWrapStyleWord(true);
		lessonsArea.setEditable(false);
		lessonsArea.setBackground(new Color(255, 255, 255));
		lessonsArea.setBorder(BorderFactory.createTitledBorder("Danh sách bài học"));
		lessonsArea.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Tải hình ảnh và thay đổi kích thước
		ImageIcon lessonsImageIcon = new ImageIcon("C:\\Users\\Admin\\eclipse-workspace\\GIAODIEN\\bin\\Image\\python1.jpg");
		Image lessonsImage = lessonsImageIcon.getImage().getScaledInstance(700, 600, Image.SCALE_SMOOTH); 

		// Tạo JLabel từ ảnh đã thay đổi kích thước
		JLabel lessonsImageLabel = new JLabel(new ImageIcon(lessonsImage));

		// Thêm các thành phần vào contentPanel
		contentPanel.add(introductionArea);
		contentPanel.add(introImageLabel);
		contentPanel.add(lessonsArea);
		contentPanel.add(lessonsImageLabel);

		// Thông tin liên hệ
		JLabel contactLabel = new JLabel("Liên hệ: tien.dinhcong2006@gmail.com | Hotline: 0123 456 789", JLabel.CENTER);
		contactLabel.setFont(new Font("Arial", Font.ITALIC, 14));
		contactLabel.setForeground(new Color(100, 100, 100));
		mainPanel.add(contactLabel, BorderLayout.SOUTH);

		mainPanel.add(contentPanel, BorderLayout.CENTER);

		// Nút Quay lại trang chủ
		JButton backButton = new JButton("Quay lại Trang chủ");
		backButton.setFont(new Font("Arial", Font.BOLD, 16));
		backButton.setBackground(new Color(255, 102, 102)); // Màu đỏ nhạt
		backButton.setForeground(Color.WHITE);
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(PythonCourseFrame.this, "Quay lại Trang chủ", "Thông báo",
						JOptionPane.INFORMATION_MESSAGE);
				dispose();
				new TrangChu(); // Giả sử TrangChu là lớp đại diện cho trang chủ
			}
		});

		// Nút Đăng ký khóa học
		JButton registerButton = new JButton("Đăng ký khóa học");
		registerButton.setFont(new Font("Arial", Font.BOLD, 16));
		registerButton.setBackground(new Color(0, 123, 255)); // Màu xanh dương
		registerButton.setForeground(Color.WHITE);
		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openRegistrationDialog(); // Mở cửa sổ đăng ký khóa học
			}
		});

		// Tạo JPanel chứa các nút
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Nút sẽ được căn giữa, khoảng cách giữa các
																			// nút là 20
		buttonPanel.add(backButton);
		buttonPanel.add(registerButton);

		mainPanel.add(buttonPanel, BorderLayout.SOUTH); // Thêm panel chứa các nút vào phần dưới của frame

		// Thêm panel chính vào JFrame
		add(mainPanel);

		setVisible(true);
	}

	// Phương thức mở cửa sổ đăng ký khóa học
	private void openRegistrationDialog() {
		JDialog registrationDialog = new JDialog(this, "Đăng ký khóa học Python", true);
		registrationDialog.setSize(400, 300);
		registrationDialog.setLayout(new BorderLayout());

		JPanel formPanel = new JPanel();
		formPanel.setLayout(new GridLayout(4, 2, 10, 10));

		formPanel.add(new JLabel("Họ và Tên:"));
		JTextField nameField = new JTextField();
		formPanel.add(nameField);

		formPanel.add(new JLabel("Email:"));
		JTextField emailField = new JTextField();
		formPanel.add(emailField);

		formPanel.add(new JLabel("Số điện thoại:"));
		JTextField phoneField = new JTextField();
		formPanel.add(phoneField);

		formPanel.add(new JLabel("Chọn thời gian bắt đầu:"));
		JTextField startTimeField = new JTextField();
		formPanel.add(startTimeField);

		registrationDialog.add(formPanel, BorderLayout.CENTER);

		// Nút gửi đăng ký
		JButton submitButton = new JButton("Đăng ký");
		submitButton.setFont(new Font("Arial", Font.BOLD, 16));
		submitButton.setBackground(new Color(0, 123, 255)); // Màu xanh dương
		submitButton.setForeground(Color.WHITE);
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Xử lý đăng ký khóa học
				String name = nameField.getText();
				String email = emailField.getText();
				String phone = phoneField.getText();
				String startTime = startTimeField.getText();

				// Kiểm tra dữ liệu hợp lệ (có thể thêm kiểm tra thêm ở đây)
				if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || startTime.isEmpty()) {
					JOptionPane.showMessageDialog(registrationDialog, "Vui lòng điền đầy đủ thông tin!", "Thông báo",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				JOptionPane.showMessageDialog(registrationDialog, "Đăng ký thành công!", "Thông báo",
						JOptionPane.INFORMATION_MESSAGE);
				registrationDialog.dispose(); // Đóng cửa sổ đăng ký
			}
		});

		registrationDialog.add(submitButton, BorderLayout.SOUTH);

		// Hiển thị cửa sổ đăng ký khóa học
		registrationDialog.setLocationRelativeTo(this);
		registrationDialog.setVisible(true);
	}
}

//cần thu nhỏ khung chứa ảnh
class WebDevCourseFrame extends JFrame {
	public WebDevCourseFrame() {
		setTitle("Khóa học Phát triển Web");
		setSize(900, 700);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// Tiêu đề khóa học
		JLabel titleLabel = new JLabel("Khóa học Phát triển Web", JLabel.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
		titleLabel.setForeground(new Color(34, 139, 34));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
		add(titleLabel, BorderLayout.NORTH);

		// Phần nội dung chính
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// Mô tả khóa học
		JLabel descriptionLabel = new JLabel("<html><body style='text-align: justify;'>"
				+ "Khóa học này cung cấp các kiến thức và kỹ năng để phát triển và triển khai các trang web hiện đại. "
				+ "Bạn sẽ học cách xây dựng giao diện người dùng, làm việc với các công nghệ back-end, và triển khai "
				+ "trang web trên môi trường thực tế." + "</body></html>");
		descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		descriptionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
		mainPanel.add(descriptionLabel, BorderLayout.NORTH);

		JPanel imagePanel = new JPanel();
		imagePanel.setLayout(new GridLayout(1, 3, 20, 20));
		imagePanel.setBackground(new Color(240, 240, 240));

		String[] imagePaths = { "C:\\Users\\Admin\\eclipse-workspace\\GIAODIEN\\bin\\Image\\anh4.jpg",
				"C:\\Users\\Admin\\eclipse-workspace\\GIAODIEN\\bin\\Image\\anh5.jpg",
				"C:\\Users\\Admin\\eclipse-workspace\\GIAODIEN\\bin\\Image\\anh6.jpg" };

		for (String imagePath : imagePaths) {
			JLabel imageLabel = new JLabel();
			try {
				File imgFile = new File(imagePath);
				if (imgFile.exists()) {
					BufferedImage img = ImageIO.read(imgFile);
					Image scaledImage = img.getScaledInstance(500, 400, Image.SCALE_SMOOTH);
					imageLabel.setIcon(new ImageIcon(scaledImage));
					imageLabel.setHorizontalAlignment(JLabel.CENTER);

					Border outerBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2);
					Border innerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5); 
					imageLabel.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
					imageLabel.setOpaque(true);
					imageLabel.setBackground(Color.WHITE); 
				} else {
					imageLabel.setText("Ảnh không tìm thấy.");
					imageLabel.setFont(new Font("Arial", Font.ITALIC, 16));
					imageLabel.setHorizontalAlignment(JLabel.CENTER);
				}
			} catch (IOException e) {
				imageLabel.setText("Lỗi khi tải ảnh.");
				imageLabel.setFont(new Font("Arial", Font.ITALIC, 16));
				imageLabel.setHorizontalAlignment(JLabel.CENTER);
				System.out.println("Lỗi tải ảnh: " + e.getMessage());
			}

			imagePanel.add(imageLabel);
		}

		mainPanel.add(imagePanel, BorderLayout.CENTER);

		// Lợi ích khóa học
		JPanel benefitsPanel = new JPanel();
		benefitsPanel.setLayout(new BoxLayout(benefitsPanel, BoxLayout.Y_AXIS));
		benefitsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY),
				"Lợi ích khi tham gia khóa học", 0, 0, new Font("Arial", Font.BOLD, 18)));

		String[] benefits = { "✔️ Thành thạo HTML, CSS, và JavaScript.",
				"✔️ Học cách sử dụng các framework phổ biến như React và Angular.",
				"✔️ Tìm hiểu về các công nghệ backend như Node.js và Express.",
				"✔️ Xây dựng các dự án thực tế để bổ sung vào portfolio.",
				"✔️ Nắm vững kỹ năng triển khai và tối ưu hóa website." };

		for (String benefit : benefits) {
			JLabel benefitLabel = new JLabel(benefit);
			benefitLabel.setFont(new Font("Arial", Font.PLAIN, 16));
			benefitsPanel.add(benefitLabel);
			benefitsPanel.add(Box.createVerticalStrut(10)); 
		}
		mainPanel.add(benefitsPanel, BorderLayout.SOUTH);

		add(mainPanel, BorderLayout.CENTER);

		// Nút Đăng Ký
		JPanel footerPanel = new JPanel();
		footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

		JButton registerButton = new JButton("Đăng Ký Khóa Học");
		registerButton.setFont(new Font("Arial", Font.BOLD, 18));
		registerButton.setFocusPainted(false);
		registerButton.setBackground(new Color(34, 139, 34));
		registerButton.setForeground(Color.WHITE);
		registerButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

		registerButton.addActionListener(e -> {
			new DangKyKhoaHocFrame();
			dispose();
		});

		footerPanel.add(registerButton);

		// Nút quay lại
		JButton backButton = new JButton("Quay Lại");
		backButton.setFont(new Font("Arial", Font.PLAIN, 16));
		backButton.setBackground(new Color(255, 102, 102));
		backButton.setForeground(Color.WHITE);
		backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

		backButton.addActionListener(e -> {
			new TrangChu();
			dispose();
		});

		footerPanel.add(backButton);
		add(footerPanel, BorderLayout.SOUTH);

		// Hiển thị giao diện
		setVisible(true);
	}
}

//
class DangKyKhoaHocFrame extends JFrame {
	public DangKyKhoaHocFrame() {
		setTitle("Đăng Ký Khóa Học");
		setSize(600, 700); 
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);

		JPanel backgroundPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				int width = getWidth();
				int height = getHeight();
				Color color1 = new Color(147, 112, 219);
				Color color2 = new Color(218, 112, 214);
				GradientPaint gradient = new GradientPaint(0, 0, color1, width, height, color2);
				g2d.setPaint(gradient);
				g2d.fillRect(0, 0, width, height);
			}
		};
		backgroundPanel.setLayout(new BorderLayout());
		add(backgroundPanel);

		// Tiêu đề
		JLabel titleLabel = new JLabel("Đăng Ký Khóa Học", JLabel.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
		backgroundPanel.add(titleLabel, BorderLayout.NORTH);

		// Form đăng ký sử dụng GridBagLayout
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new GridBagLayout());
		formPanel.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.WEST;

		// Tên đăng nhập
		JLabel usernameLabel = new JLabel("Tên đăng nhập:");
		usernameLabel.setForeground(Color.WHITE);
		usernameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 0;
		formPanel.add(usernameLabel, gbc);

		JTextField usernameField = new JTextField(20);
		usernameField.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 1;
		formPanel.add(usernameField, gbc);

		// Mật khẩu
		JLabel passwordLabel = new JLabel("Mật khẩu:");
		passwordLabel.setForeground(Color.WHITE);
		passwordLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 1;
		formPanel.add(passwordLabel, gbc);

		JPasswordField passwordField = new JPasswordField(20);
		passwordField.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 1;
		formPanel.add(passwordField, gbc);

		// Họ và tên
		JLabel nameLabel = new JLabel("Họ và tên:");
		nameLabel.setForeground(Color.WHITE);
		nameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 2;
		formPanel.add(nameLabel, gbc);

		JTextField nameField = new JTextField(20);
		nameField.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 1;
		formPanel.add(nameField, gbc);

		// Giới tính
		JLabel genderLabel = new JLabel("Giới tính:");
		genderLabel.setForeground(Color.WHITE);
		genderLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 3;
		formPanel.add(genderLabel, gbc);

		JRadioButton maleButton = new JRadioButton("Nam");
		maleButton.setOpaque(false);
		JRadioButton femaleButton = new JRadioButton("Nữ");
		femaleButton.setOpaque(false);
		ButtonGroup genderGroup = new ButtonGroup();
		genderGroup.add(maleButton);
		genderGroup.add(femaleButton);

		JPanel genderPanel = new JPanel();
		genderPanel.setOpaque(false);
		genderPanel.add(maleButton);
		genderPanel.add(femaleButton);

		gbc.gridx = 1;
		formPanel.add(genderPanel, gbc);

		// Ngày sinh
		JLabel dobLabel = new JLabel("Ngày sinh:");
		dobLabel.setForeground(Color.WHITE);
		dobLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 4;
		formPanel.add(dobLabel, gbc);

		JComboBox<String> dayBox = new JComboBox<>();
		for (int i = 1; i <= 31; i++) {
			dayBox.addItem(String.format("%02d", i));
		}

		JComboBox<String> monthBox = new JComboBox<>();
		for (int i = 1; i <= 12; i++) {
			monthBox.addItem(String.format("%02d", i));
		}

		JComboBox<String> yearBox = new JComboBox<>();
		for (int i = 1900; i <= 2025; i++) {
			yearBox.addItem(String.valueOf(i));
		}

		JPanel dobPanel = new JPanel();
		dobPanel.setOpaque(false);
		dobPanel.add(dayBox);
		dobPanel.add(monthBox);
		dobPanel.add(yearBox);

		gbc.gridx = 1;
		formPanel.add(dobPanel, gbc);

		// Email
		JLabel emailLabel = new JLabel("Email:");
		emailLabel.setForeground(Color.WHITE);
		emailLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 5;
		formPanel.add(emailLabel, gbc);

		JTextField emailField = new JTextField(20);
		emailField.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 1;
		formPanel.add(emailField, gbc);

		// Số điện thoại
		JLabel phoneLabel = new JLabel("Số điện thoại:");
		phoneLabel.setForeground(Color.WHITE);
		phoneLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 6;
		formPanel.add(phoneLabel, gbc);

		JTextField phoneField = new JTextField(20);
		phoneField.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 1;
		formPanel.add(phoneField, gbc);

		// Checkbox đồng ý điều khoản
		JCheckBox termsCheckBox = new JCheckBox("Tôi đồng ý với các điều khoản và điều kiện.");
		termsCheckBox.setFont(new Font("Arial", Font.PLAIN, 16));
		termsCheckBox.setOpaque(false);
		gbc.gridx = 1;
		gbc.gridy = 7;
		formPanel.add(termsCheckBox, gbc);

		backgroundPanel.add(formPanel, BorderLayout.CENTER);

		// Nút Xác Nhận và Reset
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		buttonPanel.setOpaque(false);

		JButton confirmButton = new JButton("Xác Nhận");
		confirmButton.setFont(new Font("Arial", Font.BOLD, 22));
		confirmButton.setBackground(new Color(34, 139, 34));
		confirmButton.setForeground(Color.WHITE);
		confirmButton.setFocusPainted(false);
		confirmButton.setPreferredSize(new Dimension(180, 50));
		confirmButton.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2));
		confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText();
				String password = new String(passwordField.getPassword());
				String name = nameField.getText();
				String gender = maleButton.isSelected() ? "Nam" : (femaleButton.isSelected() ? "Nữ" : "");
				String dob = yearBox.getSelectedItem() + "-" + monthBox.getSelectedItem() + "-"
						+ dayBox.getSelectedItem();
				String email = emailField.getText();
				String phone = phoneField.getText();

				// Kiểm tra hợp lệ dữ liệu đầu vào
				if (username.isEmpty()) {
					JOptionPane.showMessageDialog(DangKyKhoaHocFrame.this, "Tên đăng nhập không được để trống!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (password.isEmpty() || password.length() < 6) {
					JOptionPane.showMessageDialog(DangKyKhoaHocFrame.this, "Mật khẩu phải chứa ít nhất 6 ký tự!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (name.isEmpty()) {
					JOptionPane.showMessageDialog(DangKyKhoaHocFrame.this, "Họ và tên không được để trống!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (gender.isEmpty()) {
					JOptionPane.showMessageDialog(DangKyKhoaHocFrame.this, "Vui lòng chọn giới tính!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (email.isEmpty() || !email.contains("@")) {
					JOptionPane.showMessageDialog(DangKyKhoaHocFrame.this, "Email không hợp lệ!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (phone.isEmpty() || !phone.matches("\\d{10,11}")) {
					JOptionPane.showMessageDialog(DangKyKhoaHocFrame.this, "Số điện thoại phải chứa 10-11 chữ số!",
							"Lỗi", JOptionPane.ERROR_MESSAGE);
					return;
				}

				// Lưu dữ liệu vào cơ sở dữ liệu
				String courseName = "Phát triển Web Development";
				if (saveDataToDatabase(courseName, username, password, name, gender, dob, email, phone)) {
					JOptionPane.showMessageDialog(DangKyKhoaHocFrame.this, "Cảm ơn bạn đã đăng ký khóa học!",
							"Đăng Ký Thành Công", JOptionPane.INFORMATION_MESSAGE);

					// Đóng cửa sổ đăng ký và quay lại trang chủ
					DangKyKhoaHocFrame.this.dispose();
					new TrangChu(); // Gọi giao diện Trang Chủ
				} else {
					JOptionPane.showMessageDialog(DangKyKhoaHocFrame.this, "Lỗi khi đăng ký, vui lòng thử lại.",
							"Lỗi Đăng Ký", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		JButton resetButton = new JButton("Làm mới");
		resetButton.setFont(new Font("Arial", Font.BOLD, 22));
		resetButton.setBackground(Color.GRAY);
		resetButton.setForeground(Color.WHITE);
		resetButton.setFocusPainted(false);
		resetButton.setPreferredSize(new Dimension(180, 50));
		resetButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				usernameField.setText("");
				passwordField.setText("");
				nameField.setText("");
				genderGroup.clearSelection();
				dayBox.setSelectedIndex(0);
				monthBox.setSelectedIndex(0);
				yearBox.setSelectedIndex(0);
				emailField.setText("");
				phoneField.setText("");
				termsCheckBox.setSelected(false);
			}
		});

		buttonPanel.add(confirmButton);
		buttonPanel.add(resetButton);
		backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

		setVisible(true);
	}

	private boolean saveDataToDatabase(String courseName, String username, String password, String name, String gender,
			String dob, String email, String phone) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dtbdemo", "root", ""); // Thay đổi cơ sở dữ
																									// liệu theo cấu
																									// hình của bạn

			// Kiểm tra xem tên người dùng đã tồn tại chưa
			String checkUserQuery = "SELECT COUNT(*) FROM thongke WHERE `tên đăng nhập` = ?";
			pstmt = conn.prepareStatement(checkUserQuery);
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next() && rs.getInt(1) > 0) {
				JOptionPane.showMessageDialog(this, "Tên đăng nhập đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
				return false;
			}

			// Thêm thông tin vào cơ sở dữ liệu
			String insertQuery = "INSERT INTO thongke (`khoahoc`, `tên đăng nhập`, `mật khẩu`, `tên người dùng`, `giới tính`, `ngày sinh`, `email`, `số điện thoại`, `ngày tạo`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
			pstmt = conn.prepareStatement(insertQuery);
			pstmt.setString(1, courseName);
			pstmt.setString(2, username);
			pstmt.setString(3, password);
			pstmt.setString(4, name);
			pstmt.setString(5, gender);
			pstmt.setString(6, dob);
			pstmt.setString(7, email);
			pstmt.setString(8, phone);

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private boolean isUsernameExists(String username) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dtbdemo", "root", ""); // Thay đổi cơ sở dữ
																									// liệu
			String query = "SELECT COUNT(*) FROM thongke WHERE `tên đăng nhập` = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();
			if (rs.next() && rs.getInt(1) > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}

//khoá học an ninh mạng
class CyberSecurityCourseFrame extends JFrame {
	public CyberSecurityCourseFrame() {
		setTitle("Khóa học An Ninh Mạng và Bảo Mật Thông Tin");
		setSize(900, 750);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);

		// Tiêu đề khóa học
		JLabel titleLabel = new JLabel("Khóa học An Ninh Mạng và Bảo Mật Thông Tin", JLabel.CENTER);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
		titleLabel.setForeground(new Color(30, 144, 255));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
		titleLabel.setOpaque(true);
		titleLabel.setBackground(new Color(240, 248, 255));
		add(titleLabel, BorderLayout.NORTH);

		// Phần nội dung chính
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		mainPanel.setBackground(new Color(255, 255, 255));

		// Mô tả khóa học
		JLabel descriptionLabel = new JLabel("<html><body style='text-align: justify;'>"
				+ "Khóa học này giúp bạn nắm vững các kiến thức và kỹ năng cần thiết để bảo vệ thông tin "
				+ "trong môi trường kỹ thuật số. Bạn sẽ học cách phát hiện và xử lý các mối đe dọa bảo mật, "
				+ "đồng thời thực hành các phương pháp tiên tiến để quản lý an ninh mạng hiệu quả." + "</body></html>");
		descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		descriptionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
		descriptionLabel.setPreferredSize(new Dimension(850, 120));
		mainPanel.add(descriptionLabel, BorderLayout.NORTH);

		JPanel imagePanel = new JPanel();
		imagePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 30));
		imagePanel.setBackground(Color.WHITE);

		String[] imagePaths = { "C:\\Users\\Admin\\eclipse-workspace\\GIAODIEN\\bin\\Image\\anh1.jpg",
				"C:\\Users\\Admin\\eclipse-workspace\\GIAODIEN\\bin\\Image\\anh2.jpg",
				"C:\\Users\\Admin\\eclipse-workspace\\GIAODIEN\\bin\\Image\\anh3.jpg" };

		for (String imagePath : imagePaths) {
			JLabel imageLabel = new JLabel();
			try {
				File imgFile = new File(imagePath);
				if (imgFile.exists()) {
					BufferedImage img = ImageIO.read(imgFile);
					Image scaledImage = img.getScaledInstance(300, 200, Image.SCALE_SMOOTH);
					imageLabel.setIcon(new ImageIcon(scaledImage));
					imageLabel.setHorizontalAlignment(JLabel.CENTER);
					imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
					imageLabel.setOpaque(false); // Bỏ nền cho ảnh
				} else {
					imageLabel.setText("Ảnh không tìm thấy.");
					imageLabel.setFont(new Font("Arial", Font.ITALIC, 16));
					imageLabel.setHorizontalAlignment(JLabel.CENTER);
				}
			} catch (IOException e) {
				imageLabel.setText("Lỗi khi tải ảnh: " + e.getMessage());
				imageLabel.setFont(new Font("Arial", Font.ITALIC, 16));
				imageLabel.setHorizontalAlignment(JLabel.CENTER);
				System.out.println("Lỗi tải ảnh: " + e.getMessage());
			}

			imagePanel.add(imageLabel);
		}

		mainPanel.add(imagePanel, BorderLayout.CENTER);

		// Lợi ích khóa học
		JPanel benefitsPanel = new JPanel();
		benefitsPanel.setLayout(new BoxLayout(benefitsPanel, BoxLayout.Y_AXIS));
		benefitsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY),
				"Lợi ích khi tham gia khóa học", 0, 0, new Font("Segoe UI", Font.BOLD, 18)));
		benefitsPanel.setBackground(new Color(255, 255, 255));

		String[] benefits = { "- Hiểu rõ các khái niệm về an ninh mạng và bảo mật thông tin.",
				"- Thành thạo phát hiện và xử lý các lỗ hổng bảo mật.",
				"- Nắm vững kỹ thuật mã hóa và bảo mật dữ liệu.",
				"- Thực hành các tình huống thực tế với chuyên gia." };

		for (String benefit : benefits) {
			JLabel benefitLabel = new JLabel(benefit);
			benefitLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
			benefitLabel.setPreferredSize(new Dimension(850, 30));
			benefitsPanel.add(benefitLabel);
			benefitsPanel.add(Box.createVerticalStrut(10));
		}
		mainPanel.add(benefitsPanel, BorderLayout.SOUTH);

		add(mainPanel, BorderLayout.CENTER);

		// Nút Đăng Ký
		JPanel footerPanel = new JPanel();
		footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
		footerPanel.setBackground(new Color(255, 255, 255));

		JButton registerButton = new JButton("Đăng Ký Khóa Học");
		registerButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
		registerButton.setFocusPainted(false);
		registerButton.setBackground(new Color(30, 144, 255));
		registerButton.setForeground(Color.WHITE);
		registerButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		registerButton.setPreferredSize(new Dimension(200, 50));
		registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new RegistrationFrame(); // Hiển thị giao diện đăng ký mới
				dispose();
			}
		});

		footerPanel.add(registerButton);

		// Nút Quay lại Trang Chủ
		JButton backButton = new JButton("Quay lại Trang chủ");
		backButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
		backButton.setBackground(new Color(255, 102, 102));
		backButton.setForeground(Color.WHITE);
		backButton.setFocusPainted(false);
		backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		backButton.setPreferredSize(new Dimension(200, 50));
		backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new TrangChu();
				dispose();
			}
		});

		footerPanel.add(backButton);

		add(footerPanel, BorderLayout.SOUTH);

		setVisible(true);
	}
}

class RegistrationFrame extends JFrame {
	public RegistrationFrame() {
		setTitle("Đăng Ký Khóa Học");
		setSize(600, 600);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);

		JPanel backgroundPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				int width = getWidth();
				int height = getHeight();
				Color color1 = new Color(147, 112, 219);
				Color color2 = new Color(218, 112, 214);
				GradientPaint gradient = new GradientPaint(0, 0, color1, width, height, color2);
				g2d.setPaint(gradient);
				g2d.fillRect(0, 0, width, height);
			}
		};
		backgroundPanel.setLayout(new BorderLayout());
		add(backgroundPanel);

		// Tiêu đề
		JLabel titleLabel = new JLabel("Đăng Ký Khóa Học", JLabel.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
		backgroundPanel.add(titleLabel, BorderLayout.NORTH);

		// Form đăng ký sử dụng GridBagLayout
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new GridBagLayout());
		formPanel.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.WEST;

		// Tên đăng nhập
		JLabel usernameLabel = new JLabel("Tên đăng nhập:");
		usernameLabel.setForeground(Color.WHITE);
		usernameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 0;
		formPanel.add(usernameLabel, gbc);

		JTextField usernameField = new JTextField(20);
		usernameField.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 1;
		formPanel.add(usernameField, gbc);

		// Mật khẩu
		JLabel passwordLabel = new JLabel("Mật khẩu:");
		passwordLabel.setForeground(Color.WHITE);
		passwordLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 1;
		formPanel.add(passwordLabel, gbc);

		JPasswordField passwordField = new JPasswordField(20);
		passwordField.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 1;
		formPanel.add(passwordField, gbc);

		// Họ và tên
		JLabel nameLabel = new JLabel("Họ và tên:");
		nameLabel.setForeground(Color.WHITE);
		nameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 2;
		formPanel.add(nameLabel, gbc);

		JTextField nameField = new JTextField(20);
		nameField.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 1;
		formPanel.add(nameField, gbc);

		// Giới tính
		JLabel genderLabel = new JLabel("Giới tính:");
		genderLabel.setForeground(Color.WHITE);
		genderLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 3;
		formPanel.add(genderLabel, gbc);

		JRadioButton maleButton = new JRadioButton("Nam");
		maleButton.setOpaque(false);
		JRadioButton femaleButton = new JRadioButton("Nữ");
		femaleButton.setOpaque(false);
		ButtonGroup genderGroup = new ButtonGroup();
		genderGroup.add(maleButton);
		genderGroup.add(femaleButton);

		JPanel genderPanel = new JPanel();
		genderPanel.add(maleButton);
		genderPanel.add(femaleButton);
		genderPanel.setOpaque(false);

		gbc.gridx = 1;
		formPanel.add(genderPanel, gbc);

		// Ngày sinh
		JLabel dobLabel = new JLabel("Ngày sinh:");
		dobLabel.setForeground(Color.WHITE);
		dobLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 4;
		formPanel.add(dobLabel, gbc);

		JComboBox<String> dayBox = new JComboBox<>();
		for (int i = 1; i <= 31; i++) {
			dayBox.addItem(String.format("%02d", i));
		}

		JComboBox<String> monthBox = new JComboBox<>();
		for (int i = 1; i <= 12; i++) {
			monthBox.addItem(String.format("%02d", i));
		}

		JComboBox<String> yearBox = new JComboBox<>();
		for (int i = 1900; i <= 2025; i++) {
			yearBox.addItem(String.valueOf(i));
		}

		JPanel dobPanel = new JPanel();
		dobPanel.setOpaque(false);
		dobPanel.add(dayBox);
		dobPanel.add(monthBox);
		dobPanel.add(yearBox);

		gbc.gridx = 1;
		formPanel.add(dobPanel, gbc);

		// Email
		JLabel emailLabel = new JLabel("Email:");
		emailLabel.setForeground(Color.WHITE);
		emailLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 5;
		formPanel.add(emailLabel, gbc);

		JTextField emailField = new JTextField(20);
		emailField.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 1;
		formPanel.add(emailField, gbc);

		// Số điện thoại
		JLabel phoneLabel = new JLabel("Số điện thoại:");
		phoneLabel.setForeground(Color.WHITE);
		phoneLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 6;
		formPanel.add(phoneLabel, gbc);

		JTextField phoneField = new JTextField(20);
		phoneField.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 1;
		formPanel.add(phoneField, gbc);

		backgroundPanel.add(formPanel, BorderLayout.CENTER);

		// Nút Xác Nhận
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		buttonPanel.setOpaque(false);

		JButton confirmButton = new JButton("Xác Nhận");
		confirmButton.setFont(new Font("Arial", Font.BOLD, 22));
		confirmButton.setBackground(new Color(34, 139, 34));
		confirmButton.setForeground(Color.WHITE);
		confirmButton.setFocusPainted(false);
		confirmButton.setPreferredSize(new Dimension(180, 50));
		confirmButton.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2));
		confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText();
				String password = new String(passwordField.getPassword());
				String name = nameField.getText();
				String gender = maleButton.isSelected() ? "Nam" : (femaleButton.isSelected() ? "Nữ" : "");
				String dob = yearBox.getSelectedItem() + "-" + monthBox.getSelectedItem() + "-"
						+ dayBox.getSelectedItem();
				String email = emailField.getText();
				String phone = phoneField.getText();

				// Kiểm tra hợp lệ dữ liệu đầu vào
				if (username.isEmpty()) {
					JOptionPane.showMessageDialog(RegistrationFrame.this, "Tên đăng nhập không được để trống!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (password.isEmpty() || password.length() < 6) {
					JOptionPane.showMessageDialog(RegistrationFrame.this, "Mật khẩu phải chứa ít nhất 6 ký tự!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (name.isEmpty()) {
					JOptionPane.showMessageDialog(RegistrationFrame.this, "Họ và tên không được để trống!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (gender.isEmpty()) {
					JOptionPane.showMessageDialog(RegistrationFrame.this, "Vui lòng chọn giới tính!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (email.isEmpty() || !email.contains("@")) {
					JOptionPane.showMessageDialog(RegistrationFrame.this, "Email không hợp lệ!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (phone.isEmpty() || !phone.matches("\\d{10,11}")) {
					JOptionPane.showMessageDialog(RegistrationFrame.this, "Số điện thoại phải chứa 10-11 chữ số!",
							"Lỗi", JOptionPane.ERROR_MESSAGE);
					return;
				}

				// Lưu dữ liệu vào cơ sở dữ liệu
				String courseName = "Phát triển Web Development";
				if (saveDataToDatabase(courseName, username, password, name, gender, dob, email, phone)) {
					JOptionPane.showMessageDialog(RegistrationFrame.this, "Cảm ơn bạn đã đăng ký khóa học!",
							"Đăng Ký Thành Công", JOptionPane.INFORMATION_MESSAGE);

					// Đóng cửa sổ đăng ký và quay lại trang chủ
					RegistrationFrame.this.dispose();
					new TrangChu(); // Gọi giao diện Trang Chủ
				} else {
					JOptionPane.showMessageDialog(RegistrationFrame.this, "Lỗi khi đăng ký, vui lòng thử lại.",
							"Lỗi Đăng Ký", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		buttonPanel.add(confirmButton);
		backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

		setVisible(true);
	}

	private boolean saveDataToDatabase(String courseName, String username, String password, String name, String gender,
			String dob, String email, String phone) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dtbdemo", "root", ""); // Thay đổi cơ sở dữ
																									// liệu theo cấu
																									// hình của bạn

			// Kiểm tra xem tên người dùng đã tồn tại chưa
			String checkUserQuery = "SELECT COUNT(*) FROM thongke WHERE `tên đăng nhập` = ?";
			pstmt = conn.prepareStatement(checkUserQuery);
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next() && rs.getInt(1) > 0) {
				JOptionPane.showMessageDialog(this, "Tên đăng nhập đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
				return false;
			}

			// Thêm thông tin vào cơ sở dữ liệu
			String insertQuery = "INSERT INTO thongke (`khoahoc`, `tên đăng nhập`, `mật khẩu`, `tên người dùng`, `giới tính`, `ngày sinh`, `email`, `số điện thoại`, `ngày tạo`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
			pstmt = conn.prepareStatement(insertQuery);
			pstmt.setString(1, courseName);
			pstmt.setString(2, username);
			pstmt.setString(3, password);
			pstmt.setString(4, name);
			pstmt.setString(5, gender);
			pstmt.setString(6, dob);
			pstmt.setString(7, email);
			pstmt.setString(8, phone);

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private boolean isUsernameExists(String username) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dtbdemo", "root", ""); // Thay đổi cơ sở dữ
																									// liệu
			String query = "SELECT COUNT(*) FROM thongke WHERE `tên đăng nhập` = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();
			if (rs.next() && rs.getInt(1) > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}

//Đăng ký người dùng
class RegisterFrame extends JFrame {
	private static String savedUsername = null;
	private static String savedPassword = null;

	public RegisterFrame() {
		setTitle("Đăng ký");
		setSize(800, 1000);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		// Tạo JPanel tùy chỉnh với ảnh nền
		BackgroundPanel1 backgroundPanel = new BackgroundPanel1("/Image/nen.jpg");
		backgroundPanel.setLayout(new BorderLayout());

		// Panel chính
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBackground(new Color(240, 240, 240));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 120, 50, 100));
		mainPanel.setOpaque(false);

		ImageIcon icon = new ImageIcon(getClass().getResource("/Image/dangnhap.png"));
		Image ic1 = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		ImageIcon dnicon = new ImageIcon(ic1);

		ImageIcon icon1 = new ImageIcon(getClass().getResource("/Image/matkhau.png"));
		Image ic2 = icon1.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		ImageIcon mkicon = new ImageIcon(ic2);

		ImageIcon icon2 = new ImageIcon(getClass().getResource("/Image/email.png"));
		Image ic3 = icon2.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		ImageIcon mailicon = new ImageIcon(ic3);

		ImageIcon icon3 = new ImageIcon(getClass().getResource("/Image/sdt.png"));
		Image ic4 = icon3.getImage().getScaledInstance(30, 40, Image.SCALE_SMOOTH);
		ImageIcon sdticon = new ImageIcon(ic4);

		ImageIcon icon4 = new ImageIcon(getClass().getResource("/Image/lich.png"));
		Image ic5 = icon4.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		ImageIcon nsicon = new ImageIcon(ic5);

		ImageIcon icon5 = new ImageIcon(getClass().getResource("/Image/gioitinh.png"));
		Image ic6 = icon5.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		ImageIcon gticon = new ImageIcon(ic6);

		ImageIcon icon6 = new ImageIcon(getClass().getResource("/Image/hoten.png"));
		Image ic7 = icon6.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		ImageIcon nameicon = new ImageIcon(ic7);

		// Panel tiêu đề
		JPanel formPanel1 = new JPanel();
		formPanel1.setLayout(new BoxLayout(formPanel1, BoxLayout.Y_AXIS));
		formPanel1.setOpaque(false);

		JLabel formlbl = new JLabel("ĐĂNG KÝ");
		formlbl.setFont(new Font("TimeNewRoman", Font.BOLD, 40));
		formlbl.setForeground(new Color(0, 0, 0));
		formlbl.setHorizontalAlignment(SwingConstants.CENTER);
		formlbl.setForeground(Color.BLACK);
		formlbl.setIcon(dnicon);

		formPanel1.add(Box.createRigidArea(new Dimension(0, 30)));
		formPanel1.add(formlbl);

		mainPanel.add(formPanel1);

		// Panel chứa form đăng ký
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new GridBagLayout());
		formPanel.setBorder(BorderFactory.createEmptyBorder(40, 150, 10, 100));
		formPanel.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5); 
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Tên đăng nhập
		JLabel lblUsername = new JLabel("Tên đăng nhập:");
		lblUsername.setFont(new Font("Arial", Font.BOLD, 20));
		JTextField txtUsername = new JTextField(20);
		txtUsername.setFont(new Font("Arial", Font.PLAIN, 14));
		lblUsername.setHorizontalAlignment(SwingConstants.LEFT);
		lblUsername.setIcon(dnicon);

		// Mật khẩu
		JLabel lblPassword = new JLabel("Mật khẩu:");
		lblPassword.setFont(new Font("Arial", Font.BOLD, 20));
		JPasswordField txtPassword = new JPasswordField(20);
		txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
		lblPassword.setIcon(mkicon);

		// Xác nhận mật khẩu
		JLabel lblConfirmPassword = new JLabel("Xác nhận mật khẩu:");
		lblConfirmPassword.setFont(new Font("Arial", Font.BOLD, 20));
		JPasswordField txtConfirmPassword = new JPasswordField(20);
		txtConfirmPassword.setFont(new Font("Arial", Font.PLAIN, 14));
		lblConfirmPassword.setIcon(mkicon);

		// Mật khẩu Panel
		JPanel passPanel = new JPanel(new BorderLayout());
		passPanel.add(txtPassword, BorderLayout.CENTER);
		passPanel.setOpaque(false);

		// Xác nhận mật khẩu Panel
		JPanel confirmPassPanel = new JPanel(new BorderLayout());
		confirmPassPanel.add(txtConfirmPassword, BorderLayout.CENTER);
		confirmPassPanel.setOpaque(false);

		// Tạo nút để hiện/ẩn mật khẩu cho cả hai trường
		JButton showPasswordButton = new JButton("👁️");
		showPasswordButton.setFocusPainted(false);
		showPasswordButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				togglePasswordVisibility(txtPassword); 
			}
		});
		passPanel.add(showPasswordButton, BorderLayout.EAST);

		// Tạo nút để hiện/ẩn mật khẩu cho trường xác nhận mật khẩu
		JButton showConfirmPasswordButton = new JButton("👁️");
		showConfirmPasswordButton.setFocusPainted(false);
		showConfirmPasswordButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				togglePasswordVisibility(txtConfirmPassword);
			}
		});
		confirmPassPanel.add(showConfirmPasswordButton, BorderLayout.EAST);

		// Tên đăng nhập
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.5; // Chiếm không gian nhỏ hơn
		gbc.weighty = 0.0; // Không giãn theo chiều dọc
		gbc.fill = GridBagConstraints.HORIZONTAL;
		formPanel.add(lblUsername, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.5;
		formPanel.add(txtUsername, gbc);

		// Đặt các thành phần vào form
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.5;
		formPanel.add(lblPassword, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.2;
		formPanel.add(passPanel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0.2;
		formPanel.add(lblConfirmPassword, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.2;
		formPanel.add(confirmPassPanel, gbc);

		// Địa chỉ email
		JLabel lblEmail = new JLabel("Địa chỉ email:");
		lblEmail.setFont(new Font("Arial", Font.BOLD, 20));
		JTextField txtEmail = new JTextField(20);
		txtEmail.setFont(new Font("Arial", Font.PLAIN, 14));
		lblEmail.setIcon(mailicon);

		// Họ và tên
		JLabel lblFullName = new JLabel("Tên người dùng:");
		lblFullName.setFont(new Font("Arial", Font.BOLD, 20));
		JTextField txtFullName = new JTextField(20);
		txtFullName.setFont(new Font("Arial", Font.PLAIN, 14));
		lblFullName.setIcon(nameicon);

		// Số điện thoại
		JLabel lblPhone = new JLabel("Số điện thoại:");
		lblPhone.setFont(new Font("Arial", Font.BOLD, 20));
		JTextField txtPhone = new JTextField(20);
		txtPhone.setFont(new Font("Arial", Font.PLAIN, 14));
		lblPhone.setIcon(sdticon);

		// Ngày sinh
		JLabel lblDob = new JLabel("Ngày sinh:");
		lblDob.setFont(new Font("Arial", Font.BOLD, 20));
		lblDob.setIcon(nsicon);
		String[] days = new String[31];
		for (int i = 1; i <= 31; i++) {
			days[i - 1] = String.valueOf(i);
		}
		JComboBox<String> dayBox = new JComboBox<>(days);

		String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December" };
		JComboBox<String> monthBox = new JComboBox<>(months);

		String[] years = new String[200];
		for (int i = 0; i < 200; i++) {
			years[i] = String.valueOf(1900 + i);
		}
		JComboBox<String> yearBox = new JComboBox<>(years);

		// Tạo một JPanel để chứa các ComboBox
		JPanel dobPanel = new JPanel();
		dobPanel.setOpaque(false);
		dobPanel.add(dayBox);
		dobPanel.add(monthBox);
		dobPanel.add(yearBox);

		// Giới tính
		JLabel lblGender = new JLabel("Giới tính:");
		lblGender.setFont(new Font("Arial", Font.BOLD, 20));
		lblGender.setIcon(gticon);

		JPanel genderPanel = new JPanel();
		genderPanel.setOpaque(false);

		JRadioButton rbMale = new JRadioButton("Nam");
		rbMale.setFont(new Font("Arial", Font.PLAIN, 18));
		rbMale.setOpaque(false);

		JRadioButton rbFemale = new JRadioButton("Nữ");
		rbFemale.setFont(new Font("Arial", Font.PLAIN, 18));
		rbFemale.setOpaque(false);

		ButtonGroup genderGroup = new ButtonGroup();
		genderGroup.add(rbMale);
		genderGroup.add(rbFemale);
		genderPanel.add(rbMale);
		genderPanel.add(rbFemale);

		JCheckBox chkTerms = new JCheckBox("Đồng ý với điều khoản sử dụng và chính sách bảo mật");
		chkTerms.setFont(new Font("Arial", Font.PLAIN, 20));
		chkTerms.setOpaque(false);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 0.2;
		formPanel.add(lblEmail, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.2;
		formPanel.add(txtEmail, gbc);

		// Họ và tên
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 0.2;
		formPanel.add(lblFullName, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.2;
		formPanel.add(txtFullName, gbc);

		// Số điện thoại
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.weightx = 0.2;
		formPanel.add(lblPhone, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.2;
		formPanel.add(txtPhone, gbc);

		// Ngày sinh
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.weightx = 0.2;
		formPanel.add(lblDob, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.2;
		formPanel.add(dobPanel, gbc);

		// Giới tính
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.weightx = 0.2;
		formPanel.add(lblGender, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.2;
		formPanel.add(genderPanel, gbc);

		// Điều khoản sử dụng
		gbc.gridx = 0;
		gbc.gridy = 8;
		gbc.gridwidth = 2; // Chiếm cả hai cột
		formPanel.add(chkTerms, gbc);

		// Panel chứa các nút đăng ký và chuyển sang trang đăng nhập
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		buttonPanel.setOpaque(false);

		JButton btnRegister = new JButton("Đăng ký");
		btnRegister.setFont(new Font("Arial", Font.BOLD, 20));
		btnRegister.setBackground(new Color(34, 139, 34));
		btnRegister.setForeground(Color.WHITE);
		btnRegister.setFocusPainted(false);

		JButton btnGoToLogin = new JButton("Đã có tài khoản? Đăng nhập");
		btnGoToLogin.setFont(new Font("Arial", Font.PLAIN, 20));
		btnGoToLogin.setFocusPainted(false);

		buttonPanel.add(btnRegister);
		buttonPanel.add(btnGoToLogin);

		// Thêm các phần vào mainPanel
		mainPanel.add(Box.createVerticalGlue());
		mainPanel.add(formPanel);
		mainPanel.add(Box.createVerticalStrut(20));
		mainPanel.add(chkTerms);
		mainPanel.add(buttonPanel);
		mainPanel.add(Box.createVerticalGlue());

		backgroundPanel.add(mainPanel, BorderLayout.CENTER);

		setContentPane(backgroundPanel);

		setVisible(true);

        btnRegister.addActionListener(e -> {

            // Lấy dữ liệu từ giao diện
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();
            String confirmPassword = new String(txtConfirmPassword.getPassword()).trim();
            String email = txtEmail.getText().trim();
            String fullName = txtFullName.getText().trim();
            String phone = txtPhone.getText().trim();
            String gender = rbMale.isSelected() ? "Nam" : rbFemale.isSelected() ? "Nữ" : null;
            String day = (String) dayBox.getSelectedItem();
            String month = String.valueOf(monthBox.getSelectedIndex() + 1);
            String year = (String) yearBox.getSelectedItem();
            String dob = yearBox.getSelectedItem() + "-" + (monthBox.getSelectedIndex() + 1) + "-" + dayBox.getSelectedItem();

            // Thời gian hiện tại
            LocalDateTime now = LocalDateTime.now();
            String currentTime = now.toString();

            // Kiểm tra đầu vào
            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(null, "Mật khẩu và xác nhận mật khẩu không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Lưu thông tin vào cơ sở dữ liệu
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dtbdemo", "root", "");
                 PreparedStatement pst = conn.prepareStatement(
                        "INSERT INTO thongke (`tên đăng nhập`, `mật khẩu`, `tên người dùng`, `giới tính`, `email`, `số điện thoại`, `ngày sinh`, `ngày tạo`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
                pst.setString(1, username);
                pst.setString(2, password);
                pst.setString(3, fullName);
                pst.setString(4, gender);
                pst.setString(5, email);
                pst.setString(6, phone);
                pst.setString(7, dob); 
                pst.setString(8, currentTime); 

                int rowsInserted = pst.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(null, "Đăng ký thành công!\nThời gian đăng ký: " + currentTime, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    new TrangChu().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Không thể lưu thông tin người dùng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi khi lưu thông tin người dùng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        });

		btnGoToLogin.addActionListener(e -> {
			String username = txtUsername.getText().trim();
			String password = new String(txtPassword.getPassword()).trim();

			if (username.isEmpty() || password.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Vui lòng nhập tên đăng nhập và mật khẩu!", "Lỗi",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Kiểm tra thông tin đăng nhập
			try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dtbdemo", "root", "");
					PreparedStatement pst = conn
							.prepareStatement("SELECT * FROM thongke WHERE `tên đăng nhập` = ? AND `mật khẩu` = ?")) {
				pst.setString(1, username);
				pst.setString(2, password);

				ResultSet rs = pst.executeQuery();
				if (rs.next()) {
					JOptionPane.showMessageDialog(null, "Đăng nhập thành công!", "Thông báo",
							JOptionPane.INFORMATION_MESSAGE);

					new MainFrame(username).setVisible(true);
					dispose(); 
				} else {
					JOptionPane.showMessageDialog(null, "Tên đăng nhập hoặc mật khẩu không chính xác!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, "Lỗi khi đăng nhập: " + ex.getMessage(), "Lỗi",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		add(mainPanel);
	}

//Hàm toggle mật khẩu
	private void togglePasswordVisibility(JPasswordField passwordField) {
		if (passwordField.getEchoChar() == '*') {
			passwordField.setEchoChar((char) 0); // Hiển thị mật khẩu
		} else {
			passwordField.setEchoChar('*'); // Ẩn mật khẩu
		}
	}

	public boolean isValidPhoneNumber(String phone) {
		String phoneRegex = "^(0[1-9]{1}[0-9]{8,9})$";
		Pattern pattern = Pattern.compile(phoneRegex);
		Matcher matcher = pattern.matcher(phone);
		return matcher.matches();
	}

	public boolean isValidEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		Pattern pattern = Pattern.compile(emailRegex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
}

//Đăng nhập người dùng
class UserLoginFrame extends JFrame {
	private static String savedUsername = null;
	private static String savedPassword = null;

	public UserLoginFrame() {
		setTitle("Đăng nhập");
		setSize(500, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		// Tạo JPanel tùy chỉnh với ảnh nền
		BackgroundPanel1 backgroundPanel = new BackgroundPanel1("/Image/nen.jpg");
		backgroundPanel.setLayout(new BorderLayout());

		// Panel chính chứa form và nút
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Tạo tiêu đề
		JLabel title = new JLabel("ĐĂNG NHẬP");
		title.setFont(new Font("Arial", Font.BOLD, 36));
		title.setForeground(Color.WHITE);
		title.setHorizontalAlignment(SwingConstants.CENTER);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		mainPanel.add(title, gbc);

		// Tên đăng nhập
		JLabel lblUsername = new JLabel("Tên đăng nhập:");
		lblUsername.setFont(new Font("Arial", Font.BOLD, 16));
		lblUsername.setForeground(Color.WHITE);

		JTextField txtUsername = new JTextField(20);
		txtUsername.setFont(new Font("Arial", Font.PLAIN, 14));

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		mainPanel.add(lblUsername, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		mainPanel.add(txtUsername, gbc);

		// Mật khẩu
		JLabel lblPassword = new JLabel("Mật khẩu:");
		lblPassword.setFont(new Font("Arial", Font.BOLD, 16));
		lblPassword.setForeground(Color.WHITE);

		JPasswordField txtPassword = new JPasswordField(20);
		txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
		txtPassword.setEchoChar('*');

		JPanel passPanel = new JPanel(new BorderLayout());
		passPanel.add(txtPassword, BorderLayout.CENTER);
		passPanel.setOpaque(false);

		// Nút con mắt để ẩn/hiện mật khẩu
		JButton showPasswordButton = new JButton("👁️");
		showPasswordButton.setFocusPainted(false);
		showPasswordButton.setOpaque(false);
		showPasswordButton.setContentAreaFilled(false);
		showPasswordButton.setBorderPainted(false);

		showPasswordButton.addActionListener(new ActionListener() {
			private boolean isPasswordVisible = false;

			@Override
			public void actionPerformed(ActionEvent e) {
				isPasswordVisible = !isPasswordVisible;
				if (isPasswordVisible) {
					txtPassword.setEchoChar((char) 0);
					showPasswordButton.setText("🙈");
				} else {
					txtPassword.setEchoChar('*');
					showPasswordButton.setText("👁️");
				}
			}
		});

		passPanel.add(showPasswordButton, BorderLayout.EAST);

		gbc.gridx = 0;
		gbc.gridy = 2;
		mainPanel.add(lblPassword, gbc);

		gbc.gridx = 1;
		gbc.gridy = 2;
		mainPanel.add(passPanel, gbc);

		// Nút đăng nhập
		JButton btnLogin = new JButton("Đăng nhập");
		btnLogin.setFont(new Font("Arial", Font.BOLD, 16));
		btnLogin.setBackground(new Color(34, 139, 34));
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setFocusPainted(false);
		btnLogin.setPreferredSize(new Dimension(150, 40));

		// Nút quên mật khẩu
		JButton btnForgotPassword = new JButton("Quên mật khẩu");
		btnForgotPassword.setFont(new Font("Arial", Font.PLAIN, 14));
		btnForgotPassword.setBackground(new Color(34, 139, 34));
		btnForgotPassword.setForeground(Color.WHITE);
		btnForgotPassword.setFocusPainted(false);
		btnForgotPassword.setPreferredSize(new Dimension(150, 40));

		// Panel chứa các nút
		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
		buttonPanel.add(btnLogin);
		buttonPanel.add(btnForgotPassword);

		// Bố trí form và nút
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		mainPanel.add(buttonPanel, gbc);

		// Nút quay lại trang chủ
		JButton btnBackToHome = new JButton("Quay lại Trang chủ");
		btnBackToHome.setFont(new Font("Arial", Font.BOLD, 16));
		btnBackToHome.setForeground(new Color(225, 225, 225));
		btnBackToHome.setContentAreaFilled(false);
		btnBackToHome.setBorderPainted(false);
		btnBackToHome.setFocusPainted(false);

		btnBackToHome.addActionListener(e -> {
			dispose();
			new TrangChu();
		});

		JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		topRightPanel.setOpaque(false);
		topRightPanel.add(btnBackToHome);

		backgroundPanel.add(topRightPanel, BorderLayout.NORTH);
		backgroundPanel.add(mainPanel, BorderLayout.CENTER);

		setContentPane(backgroundPanel);
		setVisible(true);

		// Xử lý sự kiện nút
		btnLogin.addActionListener(e -> {
			String username = txtUsername.getText().trim();
			String password = new String(txtPassword.getPassword()).trim();

			if (username.isEmpty() || password.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Vui lòng nhập tên đăng nhập và mật khẩu!", "Lỗi",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dtbdemo", "root", "");
					PreparedStatement pst = conn
							.prepareStatement("SELECT * FROM thongke WHERE `tên đăng nhập` = ? AND `mật khẩu` = ?")) {
				pst.setString(1, username);
				pst.setString(2, password);

				ResultSet rs = pst.executeQuery();
				if (rs.next()) {
					JOptionPane.showMessageDialog(null, "Đăng nhập thành công!", "Thông báo",
							JOptionPane.INFORMATION_MESSAGE);
					new MainFrame(username).setVisible(true);
					dispose();
				} else {
					JOptionPane.showMessageDialog(null, "Tên đăng nhập hoặc mật khẩu không chính xác!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, "Lỗi khi đăng nhập: " + ex.getMessage(), "Lỗi",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		btnForgotPassword.addActionListener(e -> new ForgotPasswordFrame().setVisible(true));
	}

	// kết nối tài khoản của người dùng
	class UserManager {
		public static boolean isValidLogin(String username, String password) {
			String query = "SELECT * FROM thongke WHERE `tên đăng nhập` = ? AND `mật khẩu` = ?";

			try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dtbdemo", "root", "");
					PreparedStatement pst = conn.prepareStatement(query)) {

				pst.setString(1, username);
				pst.setString(2, password);

				ResultSet rs = pst.executeQuery();

				// Nếu tìm thấy bản ghi, trả về true
				return rs.next();
			} catch (SQLException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, "Lỗi khi kiểm tra đăng nhập: " + ex.getMessage());
			}
			return false;
		}
	}
}

//Quên mật khẩu
class ForgotPasswordFrame extends JFrame {

	public ForgotPasswordFrame() {
		setTitle("Quên Mật Khẩu");
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null); // Trung tâm màn hình

		// Tạo JPanel tùy chỉnh để vẽ nền
		JPanel panel = new GradientPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);

		// Thêm nhãn hướng dẫn với phông chữ và màu sắc hiện đại
		JLabel lblInfo = new JLabel("Nhập email hoặc tên đăng nhập:");
		lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		lblInfo.setForeground(new Color(80, 80, 80));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		panel.add(lblInfo, gbc);

		// Thêm trường nhập liệu với hiệu ứng viền mềm mại và kích thước hợp lý
		JTextField txtEmail = new JTextField(20);
		txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtEmail.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		txtEmail.setPreferredSize(new Dimension(280, 35));
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		panel.add(txtEmail, gbc);

		// Thêm nút Gửi với màu sắc và hiệu ứng hover hiện đại
		JButton btnSend = new JButton("Gửi");
		btnSend.setFont(new Font("Segoe UI", Font.BOLD, 16));
		btnSend.setBackground(new Color(34, 139, 34));
		btnSend.setForeground(Color.WHITE);
		btnSend.setFocusPainted(false);
		btnSend.setPreferredSize(new Dimension(280, 40));
		btnSend.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2, true));

		// Hiệu ứng khi di chuột vào nút Gửi
		btnSend.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnSend.setBackground(new Color(0, 128, 0));
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnSend.setBackground(new Color(34, 139, 34));
			}
		});

		// Thêm JLabel để hiển thị thông báo khi bấm gửi
		JLabel lblMessage = new JLabel();
		lblMessage.setFont(new Font("Segoe UI", Font.ITALIC, 14));
		lblMessage.setForeground(Color.BLUE);
		lblMessage.setVisible(false);

		// Hành động khi nhấn nút Gửi
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String email = txtEmail.getText().trim();
				if (email.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Vui lòng nhập email hoặc tên đăng nhập!", "Cảnh Báo",
							JOptionPane.WARNING_MESSAGE);
				} else if (!isValidEmail(email)) {
					JOptionPane.showMessageDialog(null, "Email không hợp lệ! Vui lòng nhập lại.", "Cảnh Báo",
							JOptionPane.ERROR_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Email đặt lại mật khẩu đã được gửi!", "Thông Báo",
							JOptionPane.INFORMATION_MESSAGE);
					lblMessage.setText("Đã gửi email. Vui lòng kiểm tra hộp thư của bạn.");
					lblMessage.setVisible(true);
				}
			}
		});

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		panel.add(btnSend, gbc);

		// Thêm thông báo bên dưới nút gửi
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		panel.add(lblMessage, gbc);

		// Thêm panel vào frame
		add(panel);
	}

	// Kiểm tra email hợp lệ bằng regex
	private boolean isValidEmail(String email) {
		String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	// Lớp con của JPanel để vẽ nền Gradient sáng hơn
	class GradientPanel extends JPanel {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;

			Color startColor = new Color(144, 238, 144);
			Color endColor = new Color(173, 216, 230);

			// Tạo gradient từ trên xuống dưới
			GradientPaint gradient = new GradientPaint(0, 0, startColor, 0, getHeight(), endColor);
			g2d.setPaint(gradient);
			g2d.fillRect(0, 0, getWidth(), getHeight());
		}
	}
}

class MainFrame extends JFrame {
	private JTextField idField, nameField, mailField, phoneField;
	private boolean isPasswordVisible = false;
	private JPasswordField passField;
	private JComboBox<String> day, month, year, courseComboBox;
	private JRadioButton gt1, gt2;

	public MainFrame(String username) {

		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		setTitle("QUẢN LÍ TÀI KHOẢN CÁ NHÂN");
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		// Tạo JPanel tùy chỉnh với ảnh nền
		BackgroundPanel1 backgroundPanel = new BackgroundPanel1("/Image/hoso.jpg");

		// Đặt layout cho JPanel chính
		backgroundPanel.setLayout(new BorderLayout());
		
		// Panel chính
		JPanel t1 = new JPanel();
		t1.setLayout(new BorderLayout());
		t1.setBackground(Color.WHITE);
		t1.setOpaque(false);

		ImageIcon icon3 = new ImageIcon(getClass().getResource("/Image/ht.png"));
		Image ic4 = icon3.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		ImageIcon hticon = new ImageIcon(ic4);

		ImageIcon icon4 = new ImageIcon(getClass().getResource("/Image/logout.png"));
		Image ic5 = icon4.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		ImageIcon lgicon = new ImageIcon(ic4);

		// Panel chứa tiêu đề
		JPanel jpn = new JPanel();
		jpn.setLayout(new BoxLayout(jpn, BoxLayout.Y_AXIS));
		jpn.setBackground(Color.BLUE);
		jpn.setOpaque(false);

		JLabel sc = new JLabel("Hồ Sơ Cá Nhân");
		sc.setFont(new Font("TimeNewRoman", Font.BOLD, 40));
		sc.setForeground(new Color(186, 85, 211));
		sc.setAlignmentX(Component.CENTER_ALIGNMENT);
		sc.setOpaque(false);

		JLabel sc1 = new JLabel("Thông tin cá nhân của người dùng.");
		sc1.setFont(new Font("TimeNewRoman", Font.ITALIC, 30));
		sc1.setBackground(new Color(0, 0, 255));
		sc1.setAlignmentX(Component.CENTER_ALIGNMENT);
		sc1.setOpaque(false);

		jpn.add(Box.createRigidArea(new Dimension(0, 30)));
		jpn.add(sc);
		jpn.add(Box.createRigidArea(new Dimension(0, 10)));
		jpn.add(sc1);

		t1.add(jpn, BorderLayout.NORTH);

		// Panel chứa thông tin
		JPanel t2 = new JPanel();
		t2.setLayout(new GridLayout(10, 2, 10, 15));
		t2.setBorder(BorderFactory.createEmptyBorder(100, 200, 30, 300));
		t2.setOpaque(false);

		t2.add(new JLabel("ID:")).setFont(new Font("Arial", Font.PLAIN, 20));
		idField = new JTextField();
		idField.setFont(new Font("Arial", Font.PLAIN, 16));
		t2.add(idField);

		t2.add(new JLabel("Tên người dùng:")).setFont(new Font("Arial", Font.PLAIN, 20));
		nameField = new JTextField();
		nameField.setFont(new Font("Arial", Font.PLAIN, 16));
		t2.add(nameField);

		t2.add(new JLabel("Giới tính:")).setFont(new Font("Arial", Font.PLAIN, 20));
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		gt1 = new JRadioButton("Nam");
		gt1.setFont(new Font("Arial", Font.PLAIN, 14));
		gt2 = new JRadioButton("Nữ");
		gt2.setFont(new Font("Arial", Font.PLAIN, 14));

		ButtonGroup group = new ButtonGroup();
		group.add(gt1);
		group.add(gt2);

		panel.add(gt1);
		panel.add(gt2);
		t2.add(panel);

		t2.add(new JLabel("Ngày Sinh")).setFont(new Font("Arial", Font.PLAIN, 20));
		JPanel dobpanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		dobpanel.setOpaque(false);

		String[] days = new String[31];
		for (int i = 0; i < 31; i++) {
			days[i] = String.valueOf(i + 1);
		}
		day = new JComboBox<>(days);

		String[] months = { "Th1", "Th2", "Th3", "Th4", "Th5", "Th6", "Th7", "Th8", "Th9", "Th10", "Th11", "Th12" };
		month = new JComboBox<>(months);

		String[] years = new String[200];
		for (int i = 0; i < 200; i++) {
			years[i] = String.valueOf(1825 + i);
		}
		year = new JComboBox<>(years);

		dobpanel.add(day);
		dobpanel.add(month);
		dobpanel.add(year);
		t2.add(dobpanel);

		t2.add(new JLabel("Email:")).setFont(new Font("Arial", Font.PLAIN, 20));
		mailField = new JTextField();
		mailField.setFont(new Font("Arial", Font.PLAIN, 16));
		t2.add(mailField);

		t2.add(new JLabel("Số Điện Thoại:")).setFont(new Font("Arial", Font.PLAIN, 20));
		phoneField = new JTextField();
		phoneField.setFont(new Font("Arial", Font.PLAIN, 16));
		t2.add(phoneField);

		t2.add(new JLabel("Khóa Học:")).setFont(new Font("Arial", Font.PLAIN, 20));
		String[] courses = { "Khóa học Java", "Khóa học Python", "Khóa học Web Development",
				"Khóa học an ninh và bảo mật thông tin" };
		courseComboBox = new JComboBox<>(courses); // Khởi tạo courseComboBox
		courseComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
		t2.add(courseComboBox);

		t2.add(new JLabel("Mật Khẩu: ")).setFont(new Font("Arial", Font.PLAIN, 20));
		passField = new JPasswordField();
		passField.setFont(new Font("Arial", Font.PLAIN, 16));
		passField.setEchoChar('*');
		t2.add(passField);

		JPanel passPanel = new JPanel(new BorderLayout());
		passPanel.add(passField, BorderLayout.CENTER);
		passPanel.setOpaque(false);

		JButton showPasswordButton = new JButton("👁️");
		showPasswordButton.setFocusPainted(false);
		showPasswordButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				togglePasswordVisibility();
			}
		});
		passPanel.add(showPasswordButton, BorderLayout.EAST);

		t2.add(passPanel);

		t1.add(t2, BorderLayout.CENTER);

		JPanel sc3 = new JPanel();
		sc3.setLayout(new FlowLayout());
		sc3.setBackground(Color.WHITE);
		sc3.setOpaque(false);

		JButton logout = new JButton("Log out");
		logout.setBackground(new Color(60, 179, 113));
		logout.setForeground(Color.BLACK);
		logout.setFocusPainted(false);
		logout.setOpaque(false);
		logout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int option = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận",
						JOptionPane.YES_NO_OPTION);

				if (option == JOptionPane.YES_OPTION) {
					logout();
				} else {
					System.out.println("Không đăng xuất");
				}
			}
		});

		// Tạo nút "Hoàn tất"
		JButton doneButton = new JButton("Hoàn tất");
		doneButton.setBackground(new Color(60, 179, 113));
		doneButton.setForeground(Color.BLACK);
		doneButton.setFocusPainted(false);
		doneButton.setOpaque(false);
		doneButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				int option = JOptionPane.showConfirmDialog(null, "Bạn có muốn sửa đổi thông tin này không?", "Xác nhận",
						JOptionPane.YES_NO_OPTION);

				if (option == JOptionPane.YES_OPTION) {
					saveChanges();
				} else {
					System.out.println("Không sửa đổi thông tin");
				}
			}
		});

		sc3.add(logout);
		sc3.add(doneButton);

		t1.add(sc3, BorderLayout.SOUTH);

		// Bỏ bảng và các phần không cần thiết
		backgroundPanel.add(t1, BorderLayout.CENTER);
		setContentPane(backgroundPanel);

		loadUserInfo(username);
		setVisible(true);
	}

	private void saveChanges() {
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dtbdemo", "root", "")) {

			// Lấy thông tin từ các trường nhập liệu
			String id = idField.getText();
			String name = nameField.getText();
			String email = mailField.getText();
			String phone = phoneField.getText();
			String course = (String) courseComboBox.getSelectedItem();
			String password = new String(passField.getPassword());

			String yearValue = (String) year.getSelectedItem();
			String monthValue = String.format("%02d", month.getSelectedIndex() + 1);
			String dayValue = (String) day.getSelectedItem();

			String dob = null;
			if (yearValue != null && monthValue != null && dayValue != null) {
				dob = yearValue + "-" + monthValue + "-" + dayValue;
			}

			String gender = null;
			if (gt1.isSelected()) {
				gender = "Nam";
			} else if (gt2.isSelected()) {
				gender = "Nữ";
			}

			// Kiểm tra nếu dữ liệu đầu vào hợp lệ
			if (id.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty() || course.isEmpty()
					|| password.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
				return; // Dừng lại nếu thiếu thông tin
			}

			// Kiểm tra sự tồn tại của bản ghi với id người dùng
			String checkQuery = "SELECT COUNT(*) FROM thongke WHERE id = ?";
			try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
				checkStmt.setString(1, id);
				ResultSet rs = checkStmt.executeQuery();
				rs.next();
				int count = rs.getInt(1);
				if (count == 0) {
					// Không tìm thấy thông tin để cập nhật
					JOptionPane.showMessageDialog(null, "Không tìm thấy thông tin để cập nhật!", "Thông báo",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
			}

			// Cập nhật thông tin vào cơ sở dữ liệu
			String updateQuery = "UPDATE thongke SET `tên người dùng` = ?, `email` = ?, `số điện thoại` = ?, `ngày sinh` = ?, `giới tính` = ?, `khoahoc` = ?, `mật khẩu` = ? WHERE `id` = ?";
			try (PreparedStatement pst = conn.prepareStatement(updateQuery)) {
				pst.setString(1, name);
				pst.setString(2, email);
				pst.setString(3, phone);
				pst.setString(4, dob);
				pst.setString(5, gender);
				pst.setString(6, course);
				pst.setString(7, password);
				pst.setString(8, id);

				int rowsAffected = pst.executeUpdate();
				if (rowsAffected > 0) {
					// Cập nhật thành công
					JOptionPane.showMessageDialog(null, "Cập nhật thông tin thành công!", "Thông báo",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					// Nếu không có bản ghi để cập nhật
					JOptionPane.showMessageDialog(null, "Không tìm thấy thông tin để cập nhật!", "Thông báo",
							JOptionPane.WARNING_MESSAGE);
				}
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật thông tin: " + ex.getMessage(), "Lỗi",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// ẩn hiện mật khậu
	private void togglePasswordVisibility() {
		if (isPasswordVisible) {
			passField.setEchoChar('*');
		} else {
			passField.setEchoChar((char) 0);
		}
		isPasswordVisible = !isPasswordVisible;
	}

	// tải dữ liệu từ MySQL
	public void loadUserInfo(String username) {
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dtbdemo", "root", "");
				PreparedStatement pst = conn.prepareStatement("SELECT * FROM thongke WHERE `tên đăng nhập` = ?")) {
			pst.setString(1, username);

			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				idField.setText(String.valueOf(rs.getInt("id")));
				nameField.setText(rs.getString("tên người dùng"));
				mailField.setText(rs.getString("email"));
				phoneField.setText(rs.getString("số điện thoại"));

				// Hiển thị ngày sinh
				String dob = rs.getString("ngày sinh");
				if (dob != null && !dob.isEmpty()) {
					try {
						String[] dobParts = dob.split("-");
						if (dobParts.length == 3) { // Kiểm tra định dạng đúng
							year.setSelectedItem(dobParts[0]);
							month.setSelectedIndex(Integer.parseInt(dobParts[1]) - 1);
							day.setSelectedItem(dobParts[2]);
						} else {
							JOptionPane.showMessageDialog(this, "Ngày sinh không hợp lệ!", "Thông báo",
									JOptionPane.WARNING_MESSAGE);
						}
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(this, "Ngày sinh không hợp lệ!", "Thông báo",
								JOptionPane.WARNING_MESSAGE);
					}
				} else {
					// Nếu ngày sinh là null hoặc rỗng
					JOptionPane.showMessageDialog(this, "Ngày sinh không có thông tin!", "Thông báo",
							JOptionPane.WARNING_MESSAGE);
					year.setSelectedItem(null);
					month.setSelectedIndex(0);
					day.setSelectedItem(null);
				}

				// Hiển thị giới tính
				String gender = rs.getString("giới tính");
				if ("Nam".equalsIgnoreCase(gender)) {
					gt1.setSelected(true);
				} else if ("Nữ".equalsIgnoreCase(gender)) {
					gt2.setSelected(true);
				} else {
					gt1.setSelected(false);
					gt2.setSelected(false);
				}

				// Hiển thị mật khẩu (nếu cần)
				passField.setText(rs.getString("mật khẩu"));
				courseComboBox.setSelectedItem(rs.getString("khoahoc"));
			} else {
				JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin người dùng!", "Thông báo",
						JOptionPane.WARNING_MESSAGE);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Lỗi khi tải thông tin người dùng: " + ex.getMessage(), "Lỗi",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// kiểm tra email
	private boolean isValidEmail(String email) {
		return email.matches("^[\\w.]+@[\\w.]+\\.[a-zA-Z]{2,6}$");
	}

	// kiểm tra sdt
	private boolean isValidPhone(String phone) {
		return phone.matches("\\d{10,15}");
	}

	// kích hoạt nút thoát
	private void logout() {
		dispose();
		new UserLoginFrame().setVisible(true);
	}
}

class AdminIcon extends JPanel {
	private int size;

	public AdminIcon(int size) {
		this.size = size;
		setPreferredSize(new Dimension(size, size));
		setOpaque(false); // Make background transparent
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		// Enable anti-aliasing for smoother shapes
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Draw the background circle
		g2.setColor(new Color(193, 108, 245));
		g2.fillOval(0, 0, size, size);

		// Draw the border
		g2.setColor(new Color(150, 50, 200));
		g2.setStroke(new BasicStroke(4));
		g2.drawOval(2, 2, size - 4, size - 4);

		// Draw the "Admin" text
		g2.setColor(Color.WHITE);
		g2.setFont(new Font("Segoe UI", Font.BOLD, size / 5));
		FontMetrics fm = g2.getFontMetrics();

		// Calculate the position to center the text
		String text = "Admin";
		int textX = (size - fm.stringWidth(text)) / 2;
		int textY = ((size - fm.getHeight()) / 2) + fm.getAscent();

		// Draw the text on the icon
		g2.drawString(text, textX, textY);
	}
}

//Giao diện đăng nhập của admin
class AdminLoginFrame extends JFrame {

	public AdminLoginFrame() {
		setTitle("Đăng nhập Admin");
		setSize(500, 450);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		// Set Window Icon
		setIconImage(new ImageIcon("/Image/dangnhap.png").getImage());

		// Main panel with gradient background
		GradientPanel mainPanel = new GradientPanel();
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.CENTER;

		JLabel logoLabel = new JLabel(new ImageIcon("/Image/dangnhap.png"));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		mainPanel.add(logoLabel, gbc);

		AdminIcon adminIcon = new AdminIcon(100);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		mainPanel.add(adminIcon, gbc);

		JLabel usernameLabel = new JLabel("Tên đăng nhập", new ImageIcon("Image/dangnhap.png"), JLabel.LEFT);
		usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
		usernameLabel.setForeground(new Color(100, 0, 100));
		JTextField usernameField = createRoundedTextField(20);

		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 2;
		mainPanel.add(usernameLabel, gbc);
		gbc.gridx = 1;
		mainPanel.add(usernameField, gbc);

		JLabel passwordLabel = new JLabel("Mật khẩu", new ImageIcon("Image/matkhau.png"), JLabel.LEFT);
		passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
		passwordLabel.setForeground(new Color(100, 0, 100)); // Purple text
		JPasswordField passwordField = createRoundedPasswordField(20);

		gbc.gridx = 0;
		gbc.gridy = 3;
		mainPanel.add(passwordLabel, gbc);
		gbc.gridx = 1;
		mainPanel.add(passwordField, gbc);

		JButton loginButton = createRoundedButton("Đăng nhập", new Color(0, 153, 204));
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		mainPanel.add(loginButton, gbc);

		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText();
				String password = new String(passwordField.getPassword());

				if (AdminDao.validateAdminLogin(username, password)) {
					JOptionPane.showMessageDialog(AdminLoginFrame.this, "Đăng nhập thành công!", "Thông báo",
							JOptionPane.INFORMATION_MESSAGE);
					dispose();
					new UserTableFrame();
				} else {
					JOptionPane.showMessageDialog(AdminLoginFrame.this, "Tên đăng nhập hoặc mật khẩu không đúng!",
							"Lỗi", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		topRightPanel.setOpaque(false);

		JButton btnBackToHome = new JButton("Quay lại");
		btnBackToHome.setFont(new Font("Arial", Font.PLAIN, 14));
		btnBackToHome.setForeground(new Color(255, 255, 255));
		btnBackToHome.setContentAreaFilled(false);
		btnBackToHome.setBorderPainted(false);
		btnBackToHome.setFocusPainted(false);

		btnBackToHome.addActionListener(e -> {
			dispose();
			new TrangChu();
		});

		topRightPanel.add(btnBackToHome);

		GradientPanel containerPanel = new GradientPanel();
		containerPanel.setLayout(new BorderLayout());
		containerPanel.add(topRightPanel, BorderLayout.NORTH);
		containerPanel.add(mainPanel, BorderLayout.CENTER);

		add(containerPanel);
		setVisible(true);
	}

	// Method to create rounded JTextField
	private JTextField createRoundedTextField(int columns) {
		JTextField field = new JTextField(columns);
		field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		field.setBackground(Color.WHITE);
		return field;
	}

	// Method to create rounded JPasswordField
	private JPasswordField createRoundedPasswordField(int columns) {
		JPasswordField field = new JPasswordField(columns);
		field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		field.setBackground(Color.WHITE);
		return field;
	}

	// Method to create rounded JButton
	private JButton createRoundedButton(String text, Color color) {
		JButton button = new JButton(text);
		button.setFont(new Font("Segoe UI", Font.BOLD, 14));
		button.setForeground(Color.WHITE);
		button.setBackground(color);
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return button;
	}

	//Tuỳ chỉnh màu nền 
	class GradientPanel extends JPanel {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			int width = getWidth();
			int height = getHeight();

			Color color1 = new Color(153, 51, 255);
			Color color2 = new Color(255, 102, 204);
			Color color3 = new Color(102, 0, 153);

			GradientPaint gp1 = new GradientPaint(0, 0, color1, 0, height / 2, color2);
			GradientPaint gp2 = new GradientPaint(0, height / 2, color2, 0, height, color3);

			g2d.setPaint(gp1);
			g2d.fillRect(0, 0, width, height / 2);

			g2d.setPaint(gp2);
			g2d.fillRect(0, height / 2, width, height);

			g2d.setColor(new Color(255, 255, 255, 80));
			g2d.fillRect(0, height / 4, width, height / 20);
		}
	}
}

class ChartFrame extends JFrame {

	public ChartFrame() {
		setTitle("Biểu đồ Thống kê");
		setSize(1000, 700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		getContentPane().setBackground(new Color(34, 34, 34));

		JPanel chartPanel = new JPanel();
		chartPanel.setLayout(new GridLayout(1, 2, 30, 0));

		// Biểu đồ Giới tính
		JPanel genderChartPanel = createGenderPieChartPanel();
		genderChartPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(new Color(70, 130, 180), 2), "Giới tính", TitledBorder.CENTER,
				TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 18), new Color(70, 130, 180)));
		chartPanel.add(genderChartPanel);

		// Biểu đồ Khóa học
		JPanel courseChartPanel = createCoursePieChartPanel();
		courseChartPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(new Color(255, 165, 0), 2), "Khóa học", TitledBorder.CENTER,
				TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 18), new Color(255, 165, 0)));
		chartPanel.add(courseChartPanel);

		add(chartPanel, BorderLayout.CENTER);

		// Thêm nút "Quay lại" ở phía dưới
		JButton backButton = new JButton("Quay lại");
		backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
		backButton.setBackground(new Color(255, 165, 0));
		backButton.setForeground(Color.WHITE);
		backButton.setFocusPainted(false);
		backButton.setBorder(BorderFactory.createLineBorder(new Color(255, 140, 0), 2));
		backButton.addActionListener(e -> {
			dispose();
			new UserTableFrame();
		});

		JPanel footerPanel = new JPanel(new BorderLayout());
		footerPanel.setBackground(new Color(34, 34, 34));
		footerPanel.add(backButton, BorderLayout.WEST);

		add(footerPanel, BorderLayout.SOUTH);

		setVisible(true);
	}

	// Tỉ lệ về giới tính
	private JPanel createGenderPieChartPanel() {
		Map<String, Integer> genderStats = ConnectSQL.getGenderStatistics();
		DefaultPieDataset dataset = new DefaultPieDataset();

		int total = genderStats.values().stream().mapToInt(Integer::intValue).sum();

		for (Map.Entry<String, Integer> entry : genderStats.entrySet()) {
			double percentage = (double) entry.getValue() / total * 100;
			dataset.setValue(entry.getKey() + " (" + String.format("%.1f", percentage) + "%)", entry.getValue());
		}

		JFreeChart chart = ChartFactory.createPieChart("", dataset, true, true, false);
		chart.setBackgroundPaint(Color.WHITE);
		chart.getTitle().setVisible(false);

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setSectionOutlinesVisible(true); // Đường viền 

		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(450, 350));
		chartPanel.setMouseWheelEnabled(true);   //bật tính năng bằng lăn chuột

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(chartPanel, BorderLayout.CENTER);
		panel.setBackground(new Color(50, 50, 50));

		return panel;
	}

	// Tỉ lệ về khoá học
	private JPanel createCoursePieChartPanel() {
		Map<String, Integer> courseStats = ConnectSQL.getCourseStatistics();
		DefaultPieDataset dataset = new DefaultPieDataset();

		int total = courseStats.values().stream().mapToInt(Integer::intValue).sum();

		for (Map.Entry<String, Integer> entry : courseStats.entrySet()) {
			double percentage = (double) entry.getValue() / total * 100;
			dataset.setValue(entry.getKey() + " (" + String.format("%.1f", percentage) + "%)", entry.getValue());
		}

		JFreeChart chart = ChartFactory.createPieChart("", dataset, true, true, false);
		chart.setBackgroundPaint(Color.WHITE);
		chart.getTitle().setVisible(false);

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setSectionOutlinesVisible(true);

		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(450, 350));
		chartPanel.setMouseWheelEnabled(true);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(chartPanel, BorderLayout.CENTER);
		panel.setBackground(new Color(50, 50, 50));

		return panel;
	}
}

class UserTableFrame extends JFrame {
	private static JComboBox<String> FilterGenderComboBox = null, dayComboBox, monthComboBox, yearComboBox;
	private JRadioButton maleButton, femaleButton;
	private DefaultTableModel model;
	private JTable table;
	private JTextField filterUsernameField, filterFullNameField, filterRegistrationTimeField, filterEmailField,
			filterCourseField, filterDateField;
	public JTextField idField, usernameField, emailField, fullNameField, dateField, passwordField,
			registrationTimeField, genderField, phoneField, requestField, courseField, birthField;

	public UserTableFrame() {
		setTitle("Quản lý tài khoản người dùng");
		setSize(1000, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		mainPanel.setBackground(new Color(240, 240, 240));

		JLabel headerLabel = new JLabel("Quản lý tài khoản người dùng", SwingConstants.CENTER);
		headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
		headerLabel.setForeground(new Color(0, 102, 204));
		mainPanel.add(headerLabel, BorderLayout.NORTH);

		// Panel thông tin chi tiết
		JPanel detailPanel = new JPanel(new GridLayout(6, 4, 5, 5));
		detailPanel.setBorder(new TitledBorder("Thông tin chi tiết"));
		detailPanel.setBackground(Color.WHITE);

		detailPanel.add(new JLabel("ID"));
		idField = new JTextField();
		idField.setEditable(false);
		detailPanel.add(idField);

		detailPanel.add(new JLabel("Tên đăng nhập:"));
		usernameField = new JTextField();
		detailPanel.add(usernameField);

		detailPanel.add(new JLabel("Mật khẩu:"));
		passwordField = new JTextField();
		detailPanel.add(passwordField);

		detailPanel.add(new JLabel("Tên người dùng:"));
		fullNameField = new JTextField();
		detailPanel.add(fullNameField);

		detailPanel.add(new JLabel("Giới tính:"));
		JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		genderPanel.setBackground(Color.WHITE);
		maleButton = new JRadioButton("Nam");
		maleButton.setBackground(Color.WHITE);
		femaleButton = new JRadioButton("Nữ");
		femaleButton.setBackground(Color.WHITE);

		ButtonGroup genderGroup = new ButtonGroup();
		genderGroup.add(maleButton);
		genderGroup.add(femaleButton);

		genderPanel.add(maleButton);
		genderPanel.add(femaleButton);
		detailPanel.add(genderPanel);

		// Ngày sinh
		detailPanel.add(new JLabel("Ngày sinh:"));
		JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		datePanel.setBackground(Color.WHITE);

		// Ngày
		String[] days = new String[31];
		for (int i = 1; i <= 31; i++) {
			days[i - 1] = String.format("%02d", i);
		}
		dayComboBox = new JComboBox<>(days);
		dayComboBox.setPreferredSize(new Dimension(60, 25));
		dayComboBox.setBackground(Color.WHITE);
		datePanel.add(dayComboBox);

		// Tháng
		String[] months = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
		monthComboBox = new JComboBox<>(months);
		monthComboBox.setPreferredSize(new Dimension(60, 25));
		monthComboBox.setBackground(Color.WHITE);
		datePanel.add(monthComboBox);

		// Năm
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		String[] years = new String[100];
		for (int i = 0; i < 100; i++) {
			years[i] = String.valueOf(currentYear - 50 + i);
		}
		yearComboBox = new JComboBox<>(years);
		yearComboBox.setPreferredSize(new Dimension(60, 25));
		yearComboBox.setBackground(Color.WHITE);
		datePanel.add(yearComboBox);

		detailPanel.add(datePanel);

		// Cập nhật số ngày khi chọn tháng hoặc năm
		monthComboBox.addActionListener(e -> updateDays(dayComboBox, monthComboBox, yearComboBox));
		yearComboBox.addActionListener(e -> updateDays(dayComboBox, monthComboBox, yearComboBox));

		detailPanel.add(new JLabel("E-Mail:"));
		emailField = new JTextField();
		detailPanel.add(emailField);

		detailPanel.add(new JLabel("Số điện thoại"));
		phoneField = new JTextField();
		detailPanel.add(phoneField);

		detailPanel.add(new JLabel("Ngày tạo (yyyy-mm-dd):"));
		dateField = new JTextField();
		detailPanel.add(dateField);

		detailPanel.add(new JLabel("Thời gian đăng ký:"));
		registrationTimeField = new JTextField();
		detailPanel.add(registrationTimeField);

		registrationTimeField.setInputVerifier(new InputVerifier() {
		    @Override
		    public boolean verify(JComponent input) {
		        JTextField textField = (JTextField) input;
		        String text = textField.getText();
		        try {
		            int value = Integer.parseInt(text);
		            if (value >= 1 && value <= 31) {
		                return true; 
		            }
		        } catch (NumberFormatException e) {
		        }
		        JOptionPane.showMessageDialog(input, "Vui lòng nhập giá trị từ 1 đến 31.", "Lỗi", JOptionPane.ERROR_MESSAGE);
		        return false; 
		    }
		});

		detailPanel.add(new JLabel("Khoá học:"));
		courseField = new JTextField();
		detailPanel.add(courseField);

		detailPanel.add(new JLabel("Yêu cầu"));
		requestField = new JTextField();
		detailPanel.add(requestField);

		// Tạo JComboBox để chọn thứ tự sắp xếp
		JComboBox<String> sortOrderComboBox = new JComboBox<>(new String[]{"Tăng dần (ASC)", "Giảm dần (DESC)"});

		// Tạo nút sắp xếp
		JButton sortButton = new JButton("Sắp xếp");
		JButton addButton = new JButton("Thêm");
		JButton editButton = new JButton("Sửa");
		JButton deleteButton = new JButton("Xóa");
		JButton logoutButton = new JButton("Đăng xuất");

		styleButton(addButton, new Color(0, 153, 76));
		styleButton(editButton, new Color(255, 204, 0));
		styleButton(deleteButton, new Color(255, 51, 51));
		styleButton(logoutButton, new Color(0, 153, 76));
		styleButton(sortButton, new Color(218,112,214));

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		buttonPanel.setBackground(Color.WHITE);
		buttonPanel.add(addButton);
		buttonPanel.add(editButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(logoutButton);
		buttonPanel.add(sortButton);
		buttonPanel.add(sortOrderComboBox);
		
		// Thêm sự kiện cho nút sắp xếp
        sortButton.addActionListener(e -> {
            String sortOrder = sortOrderComboBox.getSelectedItem().toString();
            sortUser(sortOrder);
        });

		addButton.addActionListener(e -> addNewUser());
		editButton.addActionListener(e -> editSelectedUser());
		deleteButton.addActionListener(e -> deleteSelectedUser());
		logoutButton.addActionListener(e -> logout());

		JPanel detailAndButtons = new JPanel(new BorderLayout());
		detailAndButtons.add(detailPanel, BorderLayout.CENTER);
		detailAndButtons.add(buttonPanel, BorderLayout.SOUTH);

		mainPanel.add(detailAndButtons, BorderLayout.NORTH);

		// Bảng dữ liệu
		model = new DefaultTableModel();
		model.setColumnIdentifiers(
				new String[] { "ID", "Tên đăng nhập", "Mật khẩu", "Tên người dùng", "Giới tính", "Ngày sinh", "E-Mail",
						"Số điện thoại", "Ngày tạo", "Thời gian đăng ký", "Khoá học", "Yêu cầu tư vấn" });

		table = new JTable(model);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		table.setRowHeight(30);
		table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
		table.getTableHeader().setBackground(new Color(0, 0, 255));
		table.getTableHeader().setForeground(Color.WHITE);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
		scrollPane.setPreferredSize(new Dimension(600, 300));
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		// Sự kiện khi chọn dòng trong bảng
		table.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow != -1) {
					// Cập nhật thông tin vào các trường chi tiết, kiểm tra null trước khi gọi
					idField.setText(getStringValue(model.getValueAt(selectedRow, 0)));
					usernameField.setText(getStringValue(model.getValueAt(selectedRow, 1)));
					passwordField.setText(getStringValue(model.getValueAt(selectedRow, 2)));
					fullNameField.setText(getStringValue(model.getValueAt(selectedRow, 3)));
					String gender = getStringValue(model.getValueAt(selectedRow, 4));
					if ("Nam".equalsIgnoreCase(gender)) {
						maleButton.setSelected(true);
					} else if ("Nữ".equalsIgnoreCase(gender)) {
						femaleButton.setSelected(true);
					}

					String birth = getStringValue(model.getValueAt(selectedRow, 5));
					if (birth != null && !birth.isEmpty()) {
						String[] birthParts = birth.split("-");
						if (birthParts.length == 3) {
							String birthYear = birthParts[0];
							String birthMonth = birthParts[1];
							String birthDay = birthParts[2];

							// Cập nhật JComboBox cho ngày, tháng, năm
							yearComboBox.setSelectedItem(birthYear);
							monthComboBox.setSelectedItem(birthMonth);
							dayComboBox.setSelectedItem(birthDay);
						}
					}

					emailField.setText(getStringValue(model.getValueAt(selectedRow, 6)));
					phoneField.setText(getStringValue(model.getValueAt(selectedRow, 7)));
					dateField.setText(getStringValue(model.getValueAt(selectedRow, 8)));
					registrationTimeField.setText(getStringValue(model.getValueAt(selectedRow, 9)));
					courseField.setText(getStringValue(model.getValueAt(selectedRow, 10)));
					requestField.setText(getStringValue(model.getValueAt(selectedRow, 11)));
				}
			}
		});

		// Bộ lọc ở bên phải
		JPanel filterPanel = new JPanel();
		filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
		filterPanel.setBorder(new TitledBorder("Bộ lọc"));
		filterPanel.setBackground(Color.WHITE);
		filterPanel.setPreferredSize(new Dimension(250, 400));

		filterPanel.add(new JLabel("tên đăng nhập:"));
		filterUsernameField = new JTextField();
		filterPanel.add(filterUsernameField);

		filterPanel.add(new JLabel("Tên người dùng:"));
		filterFullNameField = new JTextField();
		filterPanel.add(filterFullNameField);

		filterPanel.add(new JLabel("E-Mail:"));
		filterEmailField = new JTextField();
		filterPanel.add(filterEmailField);

		filterPanel.add(new JLabel("Giới tính:"));
		FilterGenderComboBox = new JComboBox<>(new String[] { "Tất cả", "Nam", "Nữ" });
		filterPanel.add(FilterGenderComboBox);

		filterPanel.add(new JLabel("Thời gian đăng ký:"));
		filterRegistrationTimeField = new JTextField();
		filterPanel.add(filterRegistrationTimeField);
		
		filterPanel.add(new JLabel("Ngày tạo"));
		filterDateField = new JTextField();
		filterPanel.add(filterDateField);

		filterPanel.add(new JLabel("Khoá học:"));
		filterCourseField = new JTextField();
		filterPanel.add(filterCourseField);

		JButton filterButton = new JButton("Lọc");
		styleButton(filterButton, new Color(0, 153, 0));
		filterPanel.add(filterButton);

		JButton resetButton = new JButton("Đặt lại");
		styleButton(resetButton, new Color(255, 102, 0));
		filterPanel.add(resetButton);

		filterButton.addActionListener(e -> applyFilter());
		resetButton.addActionListener(e -> loadDataFromDatabase());

		mainPanel.add(filterPanel, BorderLayout.EAST);

		// Tạo nút "Xem Biểu đồ"
		JButton chartButton = new JButton("Xem Biểu đồ");
		styleButton(chartButton, new Color(0, 153, 204));
		chartButton.addActionListener(e -> showChartFrame());

		JPanel buttonPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		buttonPanel1.setBackground(Color.WHITE);
		buttonPanel1.add(chartButton);

		mainPanel.add(buttonPanel1, BorderLayout.SOUTH);

		loadDataFromDatabase();

		add(mainPanel);
		setVisible(true);
	}

	// Hàm cập nhật số ngày trong tháng
	private void updateDays(JComboBox<String> dayComboBox, JComboBox<String> monthComboBox,
			JComboBox<String> yearComboBox) {
		int month = monthComboBox.getSelectedIndex() + 1;
		int year = Integer.parseInt((String) yearComboBox.getSelectedItem());
		int daysInMonth = getDaysInMonth(month, year);

		// Cập nhật các ngày vào JComboBox
		String[] days = new String[daysInMonth];
		for (int i = 1; i <= daysInMonth; i++) {
			days[i - 1] = String.format("%02d", i);
		}
		dayComboBox.setModel(new DefaultComboBoxModel<>(days));
	}

	// Hàm tính số ngày trong tháng
	private int getDaysInMonth(int month, int year) {
		switch (month) {
		case 2:
			// Kiểm tra năm nhuận
			if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
				return 29;
			} else {
				return 28;
			}
		case 4:
		case 6:
		case 9:
		case 11:
			return 30;
		default:
			return 31;
		}
	}

	// Phương thức mở giao diện biểu đồ
	private void showChartFrame() {
		this.dispose();
		new ChartFrame();
	}

	// Phương thức giúp tránh NullPointerException khi gọi toString()
	private String getStringValue(Object value) {
		return value == null ? "" : value.toString(); // Trả về chuỗi trống nếu giá trị là null
	}

	// Tải dữ liệu từ MySQL
	private void loadDataFromDatabase() {
		model.setRowCount(0);
		model.setColumnCount(0);

		try (Connection conn = ConnectSQL.getConnection()) {
			String query = "SELECT * FROM thongke";
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();

			for (int i = 1; i <= columnCount; i++) {
				model.addColumn(rsmd.getColumnName(i));
			}

			while (rs.next()) {
				Object[] row = new Object[columnCount];
				for (int i = 1; i <= columnCount; i++) {
					row[i - 1] = rs.getObject(i);
				}
				model.addRow(row);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Lỗi kết nối cơ sở dữ liệu!");
			e.printStackTrace();
		}
	}
	
	private void sortUser(String sortOrder) {

	    String order = sortOrder.equals("Tăng dần (ASC)") ? "ASC" : "DESC";
	    
	    try (Connection conn = ConnectSQL.getConnection()) {

	        String query = "SELECT * FROM thongke ORDER BY `tên đăng nhập` " + order;
	        PreparedStatement stmt = conn.prepareStatement(query);
	        ResultSet rs = stmt.executeQuery();

	        model.setRowCount(0);

	        // Cập nhật dữ liệu mới vào bảng sau khi truy vấn
	        while (rs.next()) {
	            int id = rs.getInt("id");
	            String userName = rs.getString("tên đăng nhập");
	            String password = rs.getString("mật khẩu");
	            String fullName = rs.getString("tên người dùng");
	            String gender = rs.getString("giới tính");
	            String dob = rs.getString("ngày sinh");
	            String email = rs.getString("email");
	            String phone = rs.getString("số điện thoại");
	            String regDate = rs.getString("ngày tạo");
	            String timeUsed = rs.getString("thời gian đăng ký dùng thử(Ngày)");
	            String department = rs.getString("khoahoc");
	            String note = rs.getString("yêu cầu tư vấn");

	            // Thêm dữ liệu vào bảng (model)
	            model.addRow(new Object[]{
	                id, userName, password, fullName, gender, dob, email, phone, regDate, timeUsed, department, note
	            });
	        }
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(null, "Lỗi kết nối hoặc truy vấn cơ sở dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
	        e.printStackTrace();
	    }
	}


	// thêm người dùng
	private void addNewUser() {
		// Lấy thông tin từ các trường nhập liệu (JTextField)
		String username = usernameField.getText().trim();
		String password = passwordField.getText().trim();
		String fullName = fullNameField.getText().trim();
		String gender = "";
		if (maleButton.isSelected()) {
			gender = "Nam";
		} else if (femaleButton.isSelected()) {
			gender = "Nữ";
		}

		// Kiểm tra nếu không có lựa chọn giới tính
		if (gender.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Vui lòng chọn giới tính!");
			return;
		}

		String birthDay = (String) dayComboBox.getSelectedItem();
		String birthMonth = (String) monthComboBox.getSelectedItem();
		String birthYear = (String) yearComboBox.getSelectedItem();
		// Kiểm tra nếu các trường ngày tháng năm không bị null hoặc trống
		if (birthDay == null || birthMonth == null || birthYear == null) {
			JOptionPane.showMessageDialog(null, "Vui lòng chọn đầy đủ ngày, tháng, năm!");
			return;
		}
		String birth = birthYear + "-" + birthMonth + "-" + birthDay;
		String email = emailField.getText().trim();
		String phone = phoneField.getText().trim();
		String registrationTime = registrationTimeField.getText().trim();
		String course = courseField.getText().trim();
		String request = requestField.getText().trim();

		// Kiểm tra xem có trường nào bị trống không
		if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || gender.isEmpty() || birth.isEmpty()
				|| email.isEmpty() || phone.isEmpty() || registrationTime.isEmpty() || course.isEmpty()
				|| request.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin!");
			return;
		}

		// Kiểm tra định dạng email hợp lệ
		if (!isValidEmail(email)) {
			JOptionPane.showMessageDialog(null, "Email không hợp lệ! Vui lòng nhập email đúng định dạng.");
			return;
		}

		// Nếu giá trị "Yêu cầu" là "không", đặt thành chuỗi trống
		if ("không".equalsIgnoreCase(request.trim())) {
			request = "";
		}

		// Nếu người dùng không nhập "khóa học", đặt thành chuỗi trống
		if (course.isEmpty()) {
			course = "";
		}

		// Kiểm tra định dạng thời gian đăng ký
		if (!isValidTime(registrationTime)) {
			JOptionPane.showMessageDialog(null, "Định dạng thời gian đăng ký không hợp lệ! Vui lòng nhập số ngày.");
			return;
		}

		// Lấy ngày hiện tại (ngày tạo)
		String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()); // Ngày giờ hiện tại

		// Thêm vào cơ sở dữ liệu
		try (Connection conn = ConnectSQL.getConnection()) {
			String query = "INSERT INTO thongke (`tên đăng nhập`, `mật khẩu`, `tên người dùng`, `giới tính`, `ngày sinh`, `email`, `số điện thoại`, `ngày tạo`, `thời gian đăng ký dùng thử(ngày)`, `khoahoc`, `yêu cầu tư vấn`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, username);
			stmt.setString(2, password);
			stmt.setString(3, fullName);
			stmt.setString(4, gender);
			stmt.setString(5, birth);
			stmt.setString(6, email);
			stmt.setString(7, phone);
			stmt.setString(8, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			stmt.setString(9, registrationTime);
			stmt.setString(10, course);
			stmt.setString(11, request);

			stmt.executeUpdate();
			JOptionPane.showMessageDialog(null, "Thêm người dùng thành công!");
			loadDataFromDatabase();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Lỗi khi thêm người dùng!");
			e.printStackTrace();
		}
	}

	// Kiểm tra định dạng ngày tháng (YYYY-MM-DD)
	private boolean isValidDate(String date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setLenient(false); //không điều chỉnh tự động 
			sdf.parse(date); // Nếu không hợp lệ, sẽ ném ngoại lệ
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	// Kiểm tra định dạng thời gian đăng ký (số ngày)
	private boolean isValidTime(String time) {
		try {
			Integer.parseInt(time); // Kiểm tra nếu có thể chuyển thành số nguyên
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	// Hàm kiểm tra email hợp lệ
	private boolean isValidEmail(String email) {
		// Biểu thức chính quy kiểm tra email hợp lệ
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		return email.matches(emailRegex);
	}

	// Sửa người dùng
	private void editSelectedUser() {
		int row = table.getSelectedRow();
		if (row >= 0) {
			// Lấy ID từ cột 0 (ID là khóa chính)
			String id = table.getValueAt(row, 0).toString();

			// Lấy thông tin từ các trường nhập liệu
			String newUsername = usernameField.getText().trim();
			String newPassword = passwordField.getText().trim();
			String newName = fullNameField.getText().trim();
			String newGender = maleButton.isSelected() ? "Nam" : femaleButton.isSelected() ? "Nữ" : "";
			String newBirthDay = (String) dayComboBox.getSelectedItem();
			String newBirthMonth = (String) monthComboBox.getSelectedItem();
			String newBirthYear = (String) yearComboBox.getSelectedItem();
			String newBirth = newBirthYear + "-" + newBirthMonth + "-" + newBirthDay;
			String email = emailField.getText().trim();
			String phone = phoneField.getText().trim();
			String dateCreated = dateField.getText().trim();
			String newTime = registrationTimeField.getText().trim();
			String newCourse = courseField.getText().trim();
			String request = requestField.getText();

			// Kiểm tra xem có trường nào bị trống không
			if (newUsername.isEmpty() || newPassword.isEmpty() || newName.isEmpty() || newGender.isEmpty()
					|| newBirth.isEmpty() || email.isEmpty() || phone.isEmpty() || newTime.isEmpty()
					|| newCourse.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin!");
				return;
			}

			// Kiểm tra định dạng email hợp lệ
			if (!isValidEmail1(email)) {
				JOptionPane.showMessageDialog(null, "Email không hợp lệ! Vui lòng nhập email đúng định dạng.");
				return;
			}

			// Nếu giá trị "Yêu cầu" là "không", đặt thành chuỗi trống
			if ("không".equalsIgnoreCase(request.trim())) {
				request = "";
			}

			// Nếu người dùng không nhập "khóa học", đặt thành chuỗi trống
			if (newCourse.isEmpty()) {
				newCourse = "";
			}

			// Kiểm tra định dạng thời gian đăng ký
			if (!isValidTime(newTime)) {
				JOptionPane.showMessageDialog(null, "Định dạng thời gian đăng ký không hợp lệ! Vui lòng nhập số ngày.");
				return;
			}

			// Lấy ngày hiện tại (ngày tạo) nếu người dùng không nhập ngày tạo
			String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

			// Cập nhật vào cơ sở dữ liệu
			try (Connection conn = ConnectSQL.getConnection()) {
				String query = "UPDATE thongke SET `tên đăng nhập` = ?, `mật khẩu` = ?, `tên người dùng` = ?, `giới tính` = ?, `ngày sinh` = ?, `email` = ?, `số điện thoại` = ?, `ngày tạo` = ?, `thời gian đăng ký dùng thử(Ngày)` = ?, `khoahoc` = ? WHERE `id` = ?";
				PreparedStatement stmt = conn.prepareStatement(query);

				// Gán các giá trị tham số vào câu lệnh SQL
				stmt.setString(1, newUsername);
				stmt.setString(2, newPassword);
				stmt.setString(3, newName);
				stmt.setString(4, newGender);
				stmt.setString(5, newBirth);
				stmt.setString(6, email);
				stmt.setString(7, phone);
				stmt.setString(8, currentDate);
				stmt.setString(9, newTime);
				stmt.setString(10, newCourse);
				stmt.setString(11, id);

				// Thực thi câu lệnh UPDATE
				int updatedRows = stmt.executeUpdate();
				if (updatedRows > 0) {
					JOptionPane.showMessageDialog(null, "Cập nhật thành công!");
					loadDataFromDatabase();
				} else {
					JOptionPane.showMessageDialog(null, "Cập nhật không thành công!");
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật dữ liệu: " + e.getMessage());
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(null, "Vui lòng chọn một dòng để cập nhật.");
		}
	}

	// Hàm kiểm tra email hợp lệ
	private boolean isValidEmail1(String email) {
		// Biểu thức chính quy kiểm tra email hợp lệ
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		return email.matches(emailRegex);
	}

	// Xóa người dùng
	private void deleteSelectedUser() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(null, "Vui lòng chọn người dùng cần xoá!");
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xoá người dùng này?", "Xác nhận",
				JOptionPane.YES_NO_OPTION);
		if (confirm != JOptionPane.YES_OPTION)
			return;

		// Lấy ID từ dòng được chọn
		int userId = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());

		try (Connection conn = ConnectSQL.getConnection()) {
			conn.setAutoCommit(false); // Bắt đầu giao dịch

			// Xóa bản ghi với ID đã chọn
			String deleteQuery = "DELETE FROM thongke WHERE id = ?";
			try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
				deleteStmt.setInt(1, userId);
				int result = deleteStmt.executeUpdate();

				if (result > 0) {
					// Lấy danh sách các ID hiện tại và sắp xếp lại
					String selectQuery = "SELECT id FROM thongke ORDER BY id ASC";
					try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
							ResultSet rs = selectStmt.executeQuery()) {

						int newId = 1;
						while (rs.next()) {
							int currentId = rs.getInt("id");

							// Chỉ cập nhật nếu ID hiện tại không khớp với giá trị tuần tự mong muốn
							if (currentId != newId) {
								String updateQuery = "UPDATE thongke SET id = ? WHERE id = ?";
								try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
									updateStmt.setInt(1, newId);
									updateStmt.setInt(2, currentId);
									updateStmt.executeUpdate();
								}
							}
							newId++;
						}
					}

					// Bước 3: Đặt lại AUTO_INCREMENT
					String resetAutoIncrementQuery = "ALTER TABLE thongke AUTO_INCREMENT = ?";
					try (PreparedStatement autoIncrementStmt = conn.prepareStatement(resetAutoIncrementQuery)) {
						autoIncrementStmt.setInt(1, getNextAutoIncrementValue(conn));
						autoIncrementStmt.executeUpdate();
					}

					// Cam kết giao dịch
					conn.commit();
					JOptionPane.showMessageDialog(null, "Xoá người dùng thành công và sắp xếp lại ID!");

					// Cập nhật lại dữ liệu trên bảng hiển thị
					model.removeRow(selectedRow);
					reloadTableData();
				} else {
					JOptionPane.showMessageDialog(null, "Không thể xoá người dùng!");
				}
			} catch (SQLException e) {
				conn.rollback(); // Khôi phục giao dịch nếu lỗi
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Lỗi khi xoá người dùng: " + e.getMessage());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Lỗi khi kết nối cơ sở dữ liệu: " + e.getMessage());
		}
	}

	// Hàm lấy giá trị tiếp theo cho AUTO_INCREMENT
	private int getNextAutoIncrementValue(Connection conn) throws SQLException {
		String query = "SELECT MAX(id) + 1 AS next_id FROM thongke";
		try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
			if (rs.next()) {
				return rs.getInt("next_id");
			}
		}
		return 1; // Nếu bảng rỗng, bắt đầu từ 1
	}

	private void reloadTableData() {
		try (Connection conn = ConnectSQL.getConnection()) {
			String query = "SELECT * FROM thongke ORDER BY id ASC";
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			// Xóa toàn bộ dữ liệu cũ trong bảng
			model.setRowCount(0);

			// Thêm dữ liệu mới vào bảng
			while (rs.next()) {
				int id = rs.getInt("id");
				String username = rs.getString("tên đăng nhập");
				String password = rs.getString("mật khẩu");
				String name = rs.getString("tên người dùng");
				String gender = rs.getString("giới tính");
				String dob = rs.getString("ngày sinh");
				String email = rs.getString("email");
				String phone = rs.getString("số điện thoại");
				String createdDate = rs.getString("ngày tạo");

				model.addRow(new Object[] { id, username, password, name, gender, dob, email, phone, createdDate });
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Lỗi khi tải lại dữ liệu: " + e.getMessage());
		}
	}

	private void logout() {
		// Đóng cửa sổ hiện tại
		this.dispose();

		// Hiển thị thông báo đăng xuất thành công
		JOptionPane.showMessageDialog(null, "Đăng xuất thành công!");

		new TrangChu(); // Lớp TrangChu cần phải được tạo ra
	}

	private void applyFilter() {
	    // Lấy dữ liệu từ các trường lọc
	    String username = filterUsernameField.getText().trim();
	    String fullName = filterFullNameField.getText().trim();
	    String gender = FilterGenderComboBox.getSelectedItem().toString();
	    String date = filterDateField.getText().trim();
	    String email = filterEmailField.getText().trim();
	    String registrationTime = filterRegistrationTimeField.getText().trim();
	    String course = filterCourseField.getText().trim();

	    // Xây dựng câu truy vấn SQL động
	    StringBuilder queryBuilder = new StringBuilder("SELECT * FROM thongke WHERE 1=1");

	    // Thêm các điều kiện lọc nếu có
	    if (!username.isEmpty()) queryBuilder.append(" AND `tên đăng nhập` LIKE ?");
	    if (!fullName.isEmpty()) queryBuilder.append(" AND `tên người dùng` LIKE ?");
	    if (!gender.equals("Tất cả")) queryBuilder.append(" AND `giới tính` = ?");
	    if (!date.isEmpty()) queryBuilder.append(" AND `ngày tạo` LIKE ?");
	    if (!email.isEmpty()) queryBuilder.append(" AND `email` LIKE ?");
	    if (!registrationTime.isEmpty()) queryBuilder.append(" AND `thời gian đăng ký dùng thử(Ngày)` LIKE ?");
	    if (!course.isEmpty()) queryBuilder.append(" AND `khoahoc` LIKE ?");

	    System.out.println("Query: " + queryBuilder.toString());

	    // Gọi phương thức để tải dữ liệu với các tham số lọc
	    loadFilteredDataFromDatabase(queryBuilder.toString(), username, fullName, gender, date, email, registrationTime, course);
	}

	private void loadFilteredDataFromDatabase(String query, String username, String fullName, String gender, String date,
	                                          String email, String registrationTime, String course) {
	    Connection conn = null;
	    PreparedStatement pst = null;
	    ResultSet rs = null;

	    try {
	        // Kết nối cơ sở dữ liệu
	        String dbUrl = "jdbc:mysql://localhost:3306/dtbdemo";
	        String dbUsername = "root";
	        String dbPassword = "";
	        conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

	        // Chuẩn bị câu lệnh truy vấn SQL
	        pst = conn.prepareStatement(query);

	        // Gắn các tham số vào câu truy vấn SQL
	        int paramIndex = 1;
	        if (!username.isEmpty()) pst.setString(paramIndex++, "%" + username + "%");
	        if (!fullName.isEmpty()) pst.setString(paramIndex++, "%" + fullName + "%");
	        if (!gender.equals("Tất cả")) pst.setString(paramIndex++, gender);
	        if (!date.isEmpty()) pst.setString(paramIndex++, "%" + date + "%");
	        if (!email.isEmpty()) pst.setString(paramIndex++, "%" + email + "%");
	        if (!registrationTime.isEmpty()) pst.setString(paramIndex++, "%" + registrationTime + "%");
	        if (!course.isEmpty()) pst.setString(paramIndex++, "%" + course + "%");

	        // Thực thi câu truy vấn và lấy kết quả
	        rs = pst.executeQuery();

	        // Xóa dữ liệu cũ trong bảng trước khi cập nhật mới
	        model.setRowCount(0);

	        // Duyệt qua kết quả và thêm dữ liệu vào model của bảng
	        while (rs.next()) {
	            Vector<Object> row = new Vector<>();
	            row.add(rs.getInt("id"));
	            row.add(rs.getString("tên đăng nhập"));
	            row.add(rs.getString("mật khẩu"));
	            row.add(rs.getString("tên người dùng"));
	            row.add(rs.getString("giới tính"));
	            row.add(rs.getString("ngày sinh"));
	            row.add(rs.getString("email"));
	            row.add(rs.getString("số điện thoại"));
	            row.add(rs.getString("ngày tạo"));
	            row.add(rs.getString("thời gian đăng ký dùng thử(Ngày)"));
	            row.add(rs.getString("khoahoc"));
	            model.addRow(row);
	        }

	        // Nếu không có kết quả, thông báo cho người dùng
	        if (model.getRowCount() == 0) {
	            JOptionPane.showMessageDialog(null, "Không tìm thấy dữ liệu phù hợp.");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Lỗi khi lấy dữ liệu: " + e.getMessage());
	    } finally {
	        // Đóng các tài nguyên
	        try {
	            if (rs != null) rs.close();
	            if (pst != null) pst.close();
	            if (conn != null) conn.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}


	private void styleButton(JButton button, Color color) {
		button.setBackground(color);
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Tahoma", Font.BOLD, 14));
		button.setFocusPainted(false);
	}
}
