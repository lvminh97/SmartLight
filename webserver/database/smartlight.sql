-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th12 09, 2022 lúc 04:57 PM
-- Phiên bản máy phục vụ: 10.4.18-MariaDB
-- Phiên bản PHP: 7.4.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `smartlight`
--
--CREATE DATABASE IF NOT EXISTS `smartlight` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
--USE `smartlight`;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `device`
--
-- Tạo: Th12 08, 2022 lúc 03:47 PM
--

CREATE TABLE `device` (
  `id` int(11) NOT NULL,
  `room_id` int(11) NOT NULL,
  `type` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- RELATIONSHIPS FOR TABLE `device`:
--   `room_id`
--       `room` -> `id`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `device_control`
--
-- Tạo: Th12 08, 2022 lúc 03:47 PM
--

CREATE TABLE `device_control` (
  `id` int(11) NOT NULL,
  `temp` int(11) NOT NULL,
  `light` int(11) NOT NULL,
  `power` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- RELATIONSHIPS FOR TABLE `device_control`:
--   `id`
--       `device` -> `id`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `device_data`
--
-- Tạo: Th12 09, 2022 lúc 12:38 PM
--

CREATE TABLE `device_data` (
  `id` int(11) NOT NULL,
  `time` datetime NOT NULL,
  `light` int(11) NOT NULL,
  `power` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- RELATIONSHIPS FOR TABLE `device_data`:
--   `id`
--       `device` -> `id`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `room`
--
-- Tạo: Th12 08, 2022 lúc 03:47 PM
--

CREATE TABLE `room` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- RELATIONSHIPS FOR TABLE `room`:
--   `user_id`
--       `user` -> `id`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `user`
--
-- Tạo: Th12 09, 2022 lúc 12:38 PM
-- Cập nhật lần cuối: Th12 09, 2022 lúc 12:38 PM
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `email` varchar(255) NOT NULL,
  `fullname` varchar(255) NOT NULL,
  `mobile` varchar(11) NOT NULL,
  `password` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- RELATIONSHIPS FOR TABLE `user`:
--

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `device`
--
ALTER TABLE `device`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id` (`id`,`room_id`),
  ADD KEY `room_id` (`room_id`);

--
-- Chỉ mục cho bảng `device_control`
--
ALTER TABLE `device_control`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id` (`id`);

--
-- Chỉ mục cho bảng `device_data`
--
ALTER TABLE `device_data`
  ADD PRIMARY KEY (`id`,`time`),
  ADD KEY `id` (`id`);

--
-- Chỉ mục cho bảng `room`
--
ALTER TABLE `room`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id` (`id`,`user_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Chỉ mục cho bảng `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `id` (`id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `device`
--
ALTER TABLE `device`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `room`
--
ALTER TABLE `room`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `device`
--
ALTER TABLE `device`
  ADD CONSTRAINT `device_ibfk_1` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `device_control`
--
ALTER TABLE `device_control`
  ADD CONSTRAINT `device_control_ibfk_1` FOREIGN KEY (`id`) REFERENCES `device` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `device_data`
--
ALTER TABLE `device_data`
  ADD CONSTRAINT `device_data_ibfk_1` FOREIGN KEY (`id`) REFERENCES `device` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `room`
--
ALTER TABLE `room`
  ADD CONSTRAINT `room_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
