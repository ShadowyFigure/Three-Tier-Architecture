-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 23, 2020 at 01:24 PM
-- Server version: 5.7.31-0ubuntu0.16.04.1
-- PHP Version: 7.0.33-0ubuntu0.16.04.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `cs4743proj`
--

-- --------------------------------------------------------

--
-- Table structure for table `firstnames`
--

CREATE TABLE `firstnames` (
  `id` int(11) NOT NULL,
  `first_name` char(20) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `firstnames`
--

INSERT INTO `firstnames` (`id`, `first_name`) VALUES
(1, 'AARON'),
(2, 'ALLEGRA'),
(3, 'ANDRIA'),
(4, 'AUDRY'),
(5, 'AUGUSTA'),
(6, 'BETTIE'),
(7, 'BOBBIE'),
(8, 'BRENDAN'),
(9, 'BRIDGETT'),
(10, 'CALLIE'),
(11, 'CAMILLA'),
(12, 'CINDERELLA'),
(13, 'DAYSI'),
(14, 'DENIS'),
(15, 'DIEGO'),
(16, 'DONNIE'),
(17, 'FELICITA'),
(18, 'GIUSEPPE'),
(19, 'GLENDA'),
(20, 'HUBERT'),
(21, 'IESHA'),
(22, 'JEANINE'),
(23, 'LANG'),
(24, 'LEEANN'),
(25, 'LINA'),
(26, 'LINDSEY'),
(27, 'MAFALDA'),
(28, 'MARGARETA'),
(29, 'MARIANELA'),
(30, 'MICHELE'),
(31, 'MIRTHA'),
(32, 'MISHA'),
(33, 'MISTY'),
(34, 'MONTE'),
(35, 'PETRA'),
(36, 'RAINA'),
(37, 'RANDY'),
(38, 'ROSANNA'),
(39, 'SANDI'),
(40, 'SHONDRA'),
(41, 'SUANNE'),
(42, 'TAJUANA'),
(43, 'TAWANA'),
(44, 'TISA'),
(45, 'TOD'),
(46, 'VERONIKA'),
(47, 'VERTIE'),
(48, 'VON'),
(49, 'WENDI'),
(50, 'WILLIS');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `firstnames`
--
ALTER TABLE `firstnames`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `firstnames`
--
ALTER TABLE `firstnames`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=51;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
