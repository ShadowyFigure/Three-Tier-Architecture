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
-- Table structure for table `lastnames`
--

CREATE TABLE `lastnames` (
  `id` int(11) NOT NULL,
  `last_name` char(30) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `lastnames`
--

INSERT INTO `lastnames` (`id`, `last_name`) VALUES
(1, 'ADAMS'),
(2, 'ALLEN'),
(3, 'ANDERSON'),
(4, 'BAKER'),
(5, 'BROWN'),
(6, 'CAMPBELL'),
(7, 'CARTER'),
(8, 'CLARK'),
(9, 'COLLINS'),
(10, 'DAVIS'),
(11, 'EVANS'),
(12, 'GARCIA'),
(13, 'GONZALEZ'),
(14, 'GREEN'),
(15, 'HALL'),
(16, 'HERNANDEZ'),
(17, 'HILL'),
(18, 'JACKSON'),
(19, 'JOHNSON'),
(20, 'JONES'),
(21, 'KING'),
(22, 'LEE'),
(23, 'LOPEZ'),
(24, 'MARTIN'),
(25, 'MARTINEZ'),
(26, 'MILLER'),
(27, 'MITCHELL'),
(28, 'MOORE'),
(29, 'NELSON'),
(30, 'PARKER'),
(31, 'PEREZ'),
(32, 'PHILLIPS'),
(33, 'RAMIREZ'),
(34, 'ROBERTS'),
(35, 'ROBINSON'),
(36, 'RODRIGUEZ'),
(37, 'SANCHEZ'),
(38, 'SCOTT'),
(39, 'SMITH'),
(40, 'TAYLOR'),
(41, 'THOMAS'),
(42, 'THOMPSON'),
(43, 'TORRES'),
(44, 'TURNER'),
(45, 'WALKER'),
(46, 'WHITE'),
(47, 'WILLIAMS'),
(48, 'WILSON'),
(49, 'WRIGHT'),
(50, 'YOUNG');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `lastnames`
--
ALTER TABLE `lastnames`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `lastnames`
--
ALTER TABLE `lastnames`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=51;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
