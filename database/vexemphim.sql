-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 10, 2025 at 03:11 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `vexemphim`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `id` int(11) NOT NULL,
  `ten_dang_nhap` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `mat_khau` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `poster` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`id`, `ten_dang_nhap`, `mat_khau`, `poster`) VALUES
(1, 'admin1', '123', 'admin1.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `danh_gia`
--

CREATE TABLE `danh_gia` (
  `id` int(11) NOT NULL,
  `id_nguoi_dung` int(11) DEFAULT NULL,
  `id_phim` int(11) DEFAULT NULL,
  `diem_so` int(11) DEFAULT NULL CHECK (`diem_so` between 1 and 10),
  `nhan_xet` text CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `ngay_danh_gia` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `doanh_thu`
--

CREATE TABLE `doanh_thu` (
  `id` int(11) NOT NULL,
  `doanh_thu` decimal(10,2) NOT NULL DEFAULT 0.00,
  `id_phim` int(11) DEFAULT NULL,
  `id_suat_chieu` int(11) DEFAULT NULL,
  `id_ve_da_dat` int(11) DEFAULT NULL,
  `loai_doanh_thu` varchar(50) DEFAULT NULL,
  `giam_gia` decimal(10,2) DEFAULT 0.00,
  `doanh_thu_thuc_te` decimal(10,2) GENERATED ALWAYS AS (`doanh_thu` - `giam_gia`) STORED,
  `id_khuyen_mai` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `khuyen_mai`
--

CREATE TABLE `khuyen_mai` (
  `id` int(11) NOT NULL,
  `ten_combo` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `dieu_kien` int(11) NOT NULL,
  `giam_gia` float NOT NULL,
  `qua_tang` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `khuyen_mai`
--

INSERT INTO `khuyen_mai` (`id`, `ten_combo`, `dieu_kien`, `giam_gia`, `qua_tang`) VALUES
(1, 'Combo 5: Mua 6 vé - Giảm 25%', 6, 0.25, NULL),
(2, 'Combo 6: Mua 2 vé - Tặng nước ngọt', 2, 0, 'Nước ngọt'),
(3, 'Combo 7: Mua 5 vé - Giảm 18% + Tặng bắp rang', 5, 0.18, 'Bắp rang bơ'),
(4, 'Combo 4: Mua 4 vé - Giảm 15%', 4, 0.15, NULL),
(5, 'Combo 2: Mua 3 vé - Bắp rang bơ - 2 ly nước ngọt', 1, 0.1, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `lich_chieu_phim`
--

CREATE TABLE `lich_chieu_phim` (
  `id` int(11) NOT NULL,
  `ten_phim` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `the_loai` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `thoi_luong` int(11) NOT NULL,
  `mo_ta` text CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `so_luong_ghe` int(11) NOT NULL,
  `poster` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `lich_chieu_phim`
--

INSERT INTO `lich_chieu_phim` (`id`, `ten_phim`, `the_loai`, `thoi_luong`, `mo_ta`, `so_luong_ghe`, `poster`) VALUES
(1, 'Hidden Strike', 'Hành động', 123, 'Một nhiệm vụ nguy hiểm khi hai cựu quân nhân tình ...', 120, 'C:\\Users\\Admin\\eclipse-workspace\\DOANCOSO1\\bin\\picture DOAN\\poster1.jpg'),
(2, 'Titanic', 'Tình cảm', 195, 'Một câu chuyện tình yêu lãng mạn nhưng bi thương g...', 150, 'C:\\Users\\Admin\\eclipse-workspace\\DOANCOSO1\\bin\\picture DOAN\\poster2.jpg'),
(3, 'The Matrix', 'Khoa học viễn tưởng', 136, 'Neo, một hacker tài ba, phát hiện ra sự thật kinh ...', 140, 'C:\\Users\\Admin\\eclipse-workspace\\DOANCOSO1\\bin\\picture DOAN\\poster3.jpg'),
(4, 'Galaxy Odyssey', 'Phiêu lưu', 145, 'Một hành trình đến vũ trụ tìm lối vào một thế th...', 130, 'C:\\Users\\Admin\\eclipse-workspace\\DOANCOSO1\\bin\\picture DOAN\\poster4.jpg'),
(5, 'Dunkirk', 'Chiến tranh', 106, 'Bộ phim tái hiện lại cuộc di tản Dunkirk đầy ám ...', 110, 'C:\\Users\\Admin\\eclipse-workspace\\DOANCOSO1\\bin\\picture DOAN\\poster5.jpg'),
(6, 'Avatar', 'Khoa học viễn tưởng', 162, 'Hành trình của Jake Sully - một chiến binh đấu t...', 160, 'C:\\Users\\Admin\\eclipse-workspace\\DOANCOSO1\\bin\\picture DOAN\\poster6.jpg'),
(7, 'The Batman', 'Hành động', 176, 'Bruce Wayne tiếp tục hành trình trở thành biểu t...', 170, 'C:\\Users\\Admin\\eclipse-workspace\\DOANCOSO1\\bin\\picture DOAN\\poster7.jpg'),
(8, 'Interstellar', 'Khoa học viễn tưởng', 169, 'Một nhóm phi hành gia vượt qua hố sâu xuyên không...', 140, 'C:\\Users\\Admin\\eclipse-workspace\\DOANCOSO1\\bin\\picture DOAN\\poster8.jpg'),
(9, 'Inception', 'Khoa học viễn tưởng', 148, 'Dom Cobb, một bậc thầy vào giấc mơ, thực hiện...', 130, 'C:\\Users\\Admin\\eclipse-workspace\\DOANCOSO1\\bin\\picture DOAN\\poster9.jpg'),
(10, 'Minions', 'Hoạt hình', 91, 'Những sinh vật màu vàng đáng yêu đã xuất hiện từ t...', 100, 'C:\\Users\\Admin\\eclipse-workspace\\DOANCOSO1\\bin\\picture DOAN\\poster10.jpg'),
(11, 'Toy Story 4', 'Hoạt hình', 100, 'Woody, Buzz và nhóm đồ chơi tiếp tục một cuộc hành...', 110, 'C:\\Users\\Admin\\eclipse-workspace\\DOANCOSO1\\bin\\picture DOAN\\poster11.jpg'),
(12, 'Kung Fu Panda', 'Hoạt hình', 92, 'Po, một chú gấu trúc hậu đậu, phát hiện mình được ...', 125, 'C:\\Users\\Admin\\eclipse-workspace\\DOANCOSO1\\bin\\picture DOAN\\poster12.jpg'),
(13, 'Frozen', 'Hoạt hình', 102, 'Câu chuyện về công chúa Elsa - người sở hữu sức ma...', 115, 'C:\\Users\\Admin\\eclipse-workspace\\DOANCOSO1\\bin\\picture DOAN\\poster13.jpg'),
(14, 'Fast & Furious 9', 'Hành động', 143, 'Dominic Toretto và nhóm của anh phải đối mặt với m...', 145, 'C:\\Users\\Admin\\eclipse-workspace\\DOANCOSO1\\bin\\picture DOAN\\poster14.jpg'),
(15, 'Dân Chơi Không Sợ Con Rơi', 'Hài', 120, 'Một câu chuyện đầy hài hước nhưng cảm động về hành...', 125, 'C:\\Users\\Admin\\eclipse-workspace\\DOANCOSO1\\bin\\picture DOAN\\poster15.jpg'),
(16, 'Lật Mặt 48h', 'Hành động', 111, 'Bộ phim hành động nói về cuộc đời người bố và sự...', 135, 'C:\\Users\\Admin\\eclipse-workspace\\DOANCOSO1\\bin\\picture DOAN\\poster16.jpg'),
(17, 'Bố Già', 'Hài, Tình cảm', 128, 'Một câu chuyện đầy cảm xúc về Ba Sang - một người...', 155, 'C:\\Users\\Admin\\eclipse-workspace\\DOANCOSO1\\bin\\picture DOAN\\poster17.jpg'),
(18, 'Chị Chị Em Em', 'Tâm lý', 104, 'Câu chuyện tâm lý giật gân về cuộc chiến ngầm giữa...', 110, 'C:\\Users\\Admin\\eclipse-workspace\\DOANCOSO1\\bin\\picture DOAN\\poster18.jpg'),
(19, 'Lật Mặt: Tấm Vé Định Mệnh', 'Hành động', 125, 'Một nhóm bạn bất ngờ trúng vé số độc đắc nhưng từ...', 120, 'C:\\Users\\Admin\\eclipse-workspace\\DOANCOSO1\\bin\\picture DOAN\\poster19.jpg'),
(20, 'Đêm Ám Hồn', 'Kinh dị', 110, 'Một bộ phim kinh dị Việt Nam đưa khán giả vào thề...', 120, 'C:\\Users\\Admin\\eclipse-workspace\\DOANCOSO1\\bin\\picture DOAN\\poster20.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `nguoi_dung`
--

CREATE TABLE `nguoi_dung` (
  `id` int(11) NOT NULL,
  `ten_dang_nhap` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `mat_khau` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `ho_ten` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `so_dien_thoai` varchar(15) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `gioi_tinh` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `ngay_sinh` date NOT NULL,
  `ngay_tao` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `nguoi_dung`
--

INSERT INTO `nguoi_dung` (`id`, `ten_dang_nhap`, `mat_khau`, `ho_ten`, `email`, `so_dien_thoai`, `gioi_tinh`, `ngay_sinh`, `ngay_tao`) VALUES
(1, 't1', 'Tien123', 'Đinh Công Tiến', 't1.dinh2006@gmail.com', '0987654321', 'Nam', '2006-01-24', '2025-03-30 19:35:14'),
(2, 'n1', 'Nhung123', 'Nguyễn Thị Kim Nhung', 'n2@gmail.com', '0978123456', 'Nữ', '2016-03-08', '2025-03-30 19:35:14'),
(3, 'd123', 'Danh123@', 'Nguyễn Đức Danh', 'd3@gmail.com', '0965234789', 'Nam', '1972-03-01', '2025-03-30 19:35:14');

-- --------------------------------------------------------

--
-- Table structure for table `phan_hoi`
--

CREATE TABLE `phan_hoi` (
  `id` int(11) NOT NULL,
  `id_nguoi_dung` int(11) DEFAULT NULL,
  `noi_dung` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `ngay_gui` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `suat_chieu`
--

CREATE TABLE `suat_chieu` (
  `id` int(11) NOT NULL,
  `phim_id` int(11) NOT NULL,
  `ngay_khoi_chieu` date NOT NULL,
  `thoi_gian_chieu` time NOT NULL,
  `ten_phong` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `gia_ve` decimal(10,2) NOT NULL,
  `tong_so_ve` int(11) NOT NULL,
  `ve_con_lai` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `suat_chieu`
--

INSERT INTO `suat_chieu` (`id`, `phim_id`, `ngay_khoi_chieu`, `thoi_gian_chieu`, `ten_phong`, `gia_ve`, `tong_so_ve`, `ve_con_lai`) VALUES
(1, 11, '2024-01-02', '10:00:00', 'Phòng 6', 75000.00, 110, 110),
(2, 11, '2024-01-03', '14:30:00', 'Phòng 6', 75000.00, 110, 110),
(3, 11, '2024-01-04', '19:00:00', 'Phòng 2', 75000.00, 110, 110),
(4, 12, '2024-01-05', '09:00:00', 'Phòng 2', 65000.00, 125, 125),
(5, 12, '2024-01-06', '15:00:00', 'Phòng 2', 65000.00, 125, 125),
(6, 12, '2024-01-07', '20:30:00', 'Phòng 1', 65000.00, 125, 125),
(7, 13, '2024-01-08', '10:00:00', 'Phòng 4', 70000.00, 115, 115),
(8, 13, '2024-01-09', '16:30:00', 'Phòng 4', 70000.00, 115, 115),
(9, 13, '2024-01-10', '19:45:00', 'Phòng 3', 70000.00, 115, 115),
(10, 15, '2024-01-11', '09:30:00', 'Phòng 1', 68000.00, 125, 125),
(11, 15, '2024-06-12', '14:30:00', 'Phòng 1', 68000.00, 125, 125),
(12, 15, '2024-06-13', '21:00:00', 'Phòng 2', 68000.00, 125, 125),
(13, 16, '2024-06-14', '11:00:00', 'Phòng 6', 65000.00, 135, 135),
(14, 16, '2024-06-15', '17:00:00', 'Phòng 6', 65000.00, 135, 135),
(15, 16, '2024-06-16', '22:30:00', 'Phòng 3', 65000.00, 135, 135),
(16, 17, '2024-06-17', '10:30:00', 'Phòng 2', 70000.00, 155, 155),
(17, 17, '2024-06-18', '16:00:00', 'Phòng 2', 70000.00, 155, 155),
(18, 17, '2024-06-19', '22:30:00', 'Phòng 4', 70000.00, 155, 155),
(19, 18, '2024-06-20', '10:00:00', 'Phòng 3', 70000.00, 110, 110),
(20, 18, '2024-06-21', '15:00:00', 'Phòng 3', 70000.00, 110, 110),
(21, 18, '2025-03-22', '18:45:00', 'Phòng 1', 70000.00, 110, 110),
(22, 19, '2025-03-23', '09:30:00', 'Phòng 1', 65000.00, 120, 120),
(23, 19, '2025-03-24', '14:30:00', 'Phòng 1', 65000.00, 120, 120),
(24, 19, '2025-03-25', '19:15:00', 'Phòng 2', 65000.00, 120, 120),
(25, 1, '2025-03-26', '10:00:00', 'Phòng 1', 70000.00, 120, 120),
(26, 1, '2025-03-27', '14:00:00', 'Phòng 1', 75000.00, 120, 119),
(27, 1, '2025-03-28', '18:00:00', 'Phòng 2', 70000.00, 120, 120),
(28, 1, '2025-03-01', '21:00:00', 'Phòng 3', 70000.00, 120, 120),
(29, 2, '2025-03-02', '09:00:00', 'Phòng 2', 68000.00, 150, 150),
(30, 2, '2025-03-03', '15:30:00', 'Phòng 2', 70000.00, 150, 150),
(31, 2, '2025-10-04', '20:30:00', 'Phòng 3', 80000.00, 150, 150),
(32, 3, '2025-10-05', '11:00:00', 'Phòng 4', 70000.00, 140, 140),
(33, 3, '2025-10-06', '17:00:00', 'Phòng 4', 70000.00, 140, 140),
(34, 3, '2025-10-07', '19:30:00', 'Phòng 5', 70000.00, 140, 140),
(35, 4, '2025-10-08', '10:30:00', 'Phòng 3', 70000.00, 130, 130),
(36, 4, '2025-10-09', '14:45:00', 'Phòng 3', 70000.00, 130, 130),
(37, 4, '2025-10-10', '21:00:00', 'Phòng 6', 70000.00, 130, 130),
(38, 5, '2025-10-11', '08:30:00', 'Phòng 5', 65000.00, 110, 110),
(39, 5, '2025-10-12', '13:30:00', 'Phòng 5', 65000.00, 110, 110),
(40, 5, '2025-10-13', '17:30:00', 'Phòng 2', 65000.00, 110, 110),
(41, 6, '2025-07-28', '10:30:00', 'Phòng 4', 70000.00, 160, 160),
(42, 6, '2024-07-28', '15:00:00', 'Phòng 4', 70000.00, 160, 160),
(43, 6, '2025-07-28', '18:30:00', 'Phòng 1', 70000.00, 160, 160),
(44, 7, '2024-07-28', '12:00:00', 'Phòng 6', 73000.00, 170, 170),
(45, 7, '2025-07-28', '19:00:00', 'Phòng 6', 73000.00, 170, 170),
(46, 7, '2024-07-28', '22:00:00', 'Phòng 3', 73000.00, 170, 170),
(47, 8, '2025-07-28', '09:30:00', 'Phòng 5', 72000.00, 140, 140),
(48, 8, '2024-07-28', '16:30:00', 'Phòng 5', 72000.00, 140, 140),
(49, 8, '2025-07-28', '19:30:00', 'Phòng 2', 72000.00, 140, 140),
(50, 9, '2024-07-28', '10:00:00', 'Phòng 1', 75000.00, 130, 129),
(51, 9, '2025-07-28', '15:00:00', 'Phòng 1', 75000.00, 130, 130),
(52, 9, '2024-07-28', '21:30:00', 'Phòng 3', 75000.00, 130, 130),
(53, 10, '2025-07-28', '08:30:00', 'Phòng 3', 68000.00, 100, 100),
(54, 10, '2024-07-28', '14:00:00', 'Phòng 3', 68000.00, 100, 100),
(55, 10, '2025-07-28', '16:00:00', 'Phòng 1', 68000.00, 100, 100),
(56, 14, '2024-07-28', '10:30:00', 'Phòng 5', 75000.00, 145, 145),
(57, 14, '2025-07-28', '19:45:00', 'Phòng 5', 75000.00, 145, 145),
(58, 14, '2024-07-28', '22:00:00', 'Phòng 4', 75000.00, 145, 145),
(59, 20, '2025-07-28', '18:00:00', 'Phòng 3', 75000.00, 120, 120),
(60, 20, '2024-07-28', '20:00:00', 'Phòng 3', 75000.00, 120, 120),
(61, 20, '2025-07-28', '22:30:00', 'Phòng 6', 75000.00, 120, 120);

-- --------------------------------------------------------

--
-- Table structure for table `thanh_toan`
--

CREATE TABLE `thanh_toan` (
  `id` int(11) NOT NULL,
  `id_ve_xem_phim` int(11) NOT NULL,
  `phuong_thuc` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `trang_thai` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `ngay_thanh_toan` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ve_da_dat`
--

CREATE TABLE `ve_da_dat` (
  `id` int(11) NOT NULL,
  `id_lich_chieu` int(11) DEFAULT NULL,
  `tai_khoan` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `ghe_da_dat` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ve_phim`
--

CREATE TABLE `ve_phim` (
  `id` int(11) NOT NULL,
  `id_ve_xem_phim` int(11) NOT NULL,
  `loai_ve` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ve_xem_phim`
--

CREATE TABLE `ve_xem_phim` (
  `id` int(11) NOT NULL,
  `id_nguoi_dung` int(11) NOT NULL,
  `id_lich_chieu` int(11) NOT NULL,
  `so_ghe` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `ngay_dat` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Triggers `ve_xem_phim`
--
DELIMITER $$
CREATE TRIGGER `trg_giam_ve_con_lai` AFTER INSERT ON `ve_xem_phim` FOR EACH ROW BEGIN
    UPDATE suat_chieu
    SET ve_con_lai = ve_con_lai - 1
    WHERE id = NEW.id_lich_chieu; -- Thay id_suat_chieu bằng id_lich_chieu (hoặc cột phù hợp)
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `trg_tang_ve_con_lai` AFTER DELETE ON `ve_xem_phim` FOR EACH ROW BEGIN
    UPDATE suat_chieu
    SET ve_con_lai = ve_con_lai + 1
    WHERE id = OLD.id_lich_chieu;
END
$$
DELIMITER ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `ten_dang_nhap` (`ten_dang_nhap`);

--
-- Indexes for table `danh_gia`
--
ALTER TABLE `danh_gia`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_nguoi_dung` (`id_nguoi_dung`),
  ADD KEY `id_phim` (`id_phim`);

--
-- Indexes for table `doanh_thu`
--
ALTER TABLE `doanh_thu`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_phim` (`id_phim`),
  ADD KEY `id_suat_chieu` (`id_suat_chieu`),
  ADD KEY `id_ve_da_dat` (`id_ve_da_dat`),
  ADD KEY `id_khuyen_mai` (`id_khuyen_mai`);

--
-- Indexes for table `khuyen_mai`
--
ALTER TABLE `khuyen_mai`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `lich_chieu_phim`
--
ALTER TABLE `lich_chieu_phim`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `nguoi_dung`
--
ALTER TABLE `nguoi_dung`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `ten_dang_nhap` (`ten_dang_nhap`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `so_dien_thoai` (`so_dien_thoai`);

--
-- Indexes for table `phan_hoi`
--
ALTER TABLE `phan_hoi`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_nguoi_dung` (`id_nguoi_dung`);

--
-- Indexes for table `suat_chieu`
--
ALTER TABLE `suat_chieu`
  ADD PRIMARY KEY (`id`),
  ADD KEY `phim_id` (`phim_id`);

--
-- Indexes for table `thanh_toan`
--
ALTER TABLE `thanh_toan`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_ve_xem_phim` (`id_ve_xem_phim`);

--
-- Indexes for table `ve_da_dat`
--
ALTER TABLE `ve_da_dat`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_lich_chieu` (`id_lich_chieu`);

--
-- Indexes for table `ve_phim`
--
ALTER TABLE `ve_phim`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_ve_xem_phim` (`id_ve_xem_phim`);

--
-- Indexes for table `ve_xem_phim`
--
ALTER TABLE `ve_xem_phim`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_nguoi_dung` (`id_nguoi_dung`),
  ADD KEY `id_lich_chieu` (`id_lich_chieu`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `danh_gia`
--
ALTER TABLE `danh_gia`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `doanh_thu`
--
ALTER TABLE `doanh_thu`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `khuyen_mai`
--
ALTER TABLE `khuyen_mai`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `lich_chieu_phim`
--
ALTER TABLE `lich_chieu_phim`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `nguoi_dung`
--
ALTER TABLE `nguoi_dung`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `phan_hoi`
--
ALTER TABLE `phan_hoi`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `suat_chieu`
--
ALTER TABLE `suat_chieu`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=62;

--
-- AUTO_INCREMENT for table `thanh_toan`
--
ALTER TABLE `thanh_toan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `ve_da_dat`
--
ALTER TABLE `ve_da_dat`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ve_phim`
--
ALTER TABLE `ve_phim`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ve_xem_phim`
--
ALTER TABLE `ve_xem_phim`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `danh_gia`
--
ALTER TABLE `danh_gia`
  ADD CONSTRAINT `danh_gia_ibfk_1` FOREIGN KEY (`id_nguoi_dung`) REFERENCES `nguoi_dung` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `danh_gia_ibfk_2` FOREIGN KEY (`id_phim`) REFERENCES `lich_chieu_phim` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `doanh_thu`
--
ALTER TABLE `doanh_thu`
  ADD CONSTRAINT `doanh_thu_ibfk_1` FOREIGN KEY (`id_phim`) REFERENCES `lich_chieu_phim` (`id`),
  ADD CONSTRAINT `doanh_thu_ibfk_2` FOREIGN KEY (`id_suat_chieu`) REFERENCES `suat_chieu` (`id`),
  ADD CONSTRAINT `doanh_thu_ibfk_3` FOREIGN KEY (`id_ve_da_dat`) REFERENCES `ve_da_dat` (`id`),
  ADD CONSTRAINT `doanh_thu_ibfk_4` FOREIGN KEY (`id_khuyen_mai`) REFERENCES `khuyen_mai` (`id`);

--
-- Constraints for table `phan_hoi`
--
ALTER TABLE `phan_hoi`
  ADD CONSTRAINT `phan_hoi_ibfk_1` FOREIGN KEY (`id_nguoi_dung`) REFERENCES `nguoi_dung` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `suat_chieu`
--
ALTER TABLE `suat_chieu`
  ADD CONSTRAINT `suat_chieu_ibfk_1` FOREIGN KEY (`phim_id`) REFERENCES `lich_chieu_phim` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `thanh_toan`
--
ALTER TABLE `thanh_toan`
  ADD CONSTRAINT `thanh_toan_ibfk_1` FOREIGN KEY (`id_ve_xem_phim`) REFERENCES `ve_xem_phim` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `ve_da_dat`
--
ALTER TABLE `ve_da_dat`
  ADD CONSTRAINT `ve_da_dat_ibfk_1` FOREIGN KEY (`id_lich_chieu`) REFERENCES `lich_chieu_phim` (`id`);

--
-- Constraints for table `ve_phim`
--
ALTER TABLE `ve_phim`
  ADD CONSTRAINT `ve_phim_ibfk_1` FOREIGN KEY (`id_ve_xem_phim`) REFERENCES `ve_xem_phim` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `ve_xem_phim`
--
ALTER TABLE `ve_xem_phim`
  ADD CONSTRAINT `ve_xem_phim_ibfk_1` FOREIGN KEY (`id_nguoi_dung`) REFERENCES `nguoi_dung` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `ve_xem_phim_ibfk_2` FOREIGN KEY (`id_lich_chieu`) REFERENCES `lich_chieu_phim` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
