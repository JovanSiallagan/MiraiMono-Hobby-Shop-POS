-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 10, 2026 at 01:27 PM
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
-- Database: `smartcashier`
--

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE `product` (
  `ProductID` varchar(10) NOT NULL,
  `ProductName` varchar(100) NOT NULL,
  `type` varchar(15) NOT NULL,
  `Price` double NOT NULL,
  `stock` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`ProductID`, `ProductName`, `type`, `Price`, `stock`) VALUES
('CLO-001', 'T-Shirt One Piece Luffy', 'Clothing', 120000, 25),
('CLO-002', 'Hoodie Kimetsu no Yaiba', 'Clothing', 550000, 30),
('FIG-001', 'Figure Hatsune Miku 1/7 Scale', 'Figure', 2500000, 5),
('FIG-002', 'Nendoroid Bocchi The Rock', 'Figure', 750000, 10),
('FIG-003', 'Nendoroid Yuru Camp', 'Figure', 650000, 5),
('GAC-001', 'Gacha Genshin Impact Vision', 'Gacha', 85000, 200),
('MER-001', 'Acrylic Stand Hololive Suisei', 'Merch', 150000, 50),
('MER-002', 'Poster A2 Jujutsu Kaisen', 'Merch', 50000, 100),
('MER-003', 'Lanyard Bocchi The Rock!', 'Merch', 25000, 50),
('MER-004', 'Plush Adachi Rei', 'Merch', 1250000, 3),
('MER-005', 'Keychain Project SEKAI: COLORFUL STAGE!Proyek SEKAI COLORFUL STAGE!', 'Merch', 35000, 20);

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions` (
  `transactionID` varchar(20) NOT NULL,
  `UserID` varchar(10) DEFAULT NULL,
  `subtotal` double DEFAULT NULL,
  `discountAmount` double DEFAULT NULL,
  `taxAmount` double DEFAULT NULL,
  `totalPrice` double DEFAULT NULL,
  `transactionDate` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transactions`
--

INSERT INTO `transactions` (`transactionID`, `UserID`, `subtotal`, `discountAmount`, `taxAmount`, `totalPrice`, `transactionDate`) VALUES
('TRX-1768047834757', 'K01', 750000, 37500, 78375, 790875, '2026-01-10 19:23:54');

-- --------------------------------------------------------

--
-- Table structure for table `transaction_detail`
--

CREATE TABLE `transaction_detail` (
  `id` int(11) NOT NULL,
  `transactionID` varchar(50) DEFAULT NULL,
  `ProductID` varchar(50) DEFAULT NULL,
  `qty` int(11) DEFAULT NULL,
  `subtotal` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transaction_detail`
--

INSERT INTO `transaction_detail` (`id`, `transactionID`, `ProductID`, `qty`, `subtotal`) VALUES
(1, 'TRX-1768047834757', 'FIG-002', 1, 750000);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `UserID` varchar(10) NOT NULL,
  `Username` varchar(100) NOT NULL,
  `passwords` varchar(15) NOT NULL,
  `nama` varchar(15) NOT NULL,
  `role` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`UserID`, `Username`, `passwords`, `nama`, `role`) VALUES
('A01', 'admin', 'admin', 'Sucipto', 'Admin'),
('K01', 'kasir', 'kasir', 'Joni', 'Kasir');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`ProductID`);

--
-- Indexes for table `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`transactionID`),
  ADD KEY `UserID` (`UserID`);

--
-- Indexes for table `transaction_detail`
--
ALTER TABLE `transaction_detail`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`UserID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `transaction_detail`
--
ALTER TABLE `transaction_detail`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `transactions`
--
ALTER TABLE `transactions`
  ADD CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
