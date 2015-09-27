-- phpMyAdmin SQL Dump
-- version 4.2.11
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Sep 27, 2015 at 08:18 PM
-- Server version: 5.6.21
-- PHP Version: 5.5.19

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `planning`
--

-- --------------------------------------------------------

--
-- Table structure for table `budgetdetails`
--

CREATE TABLE IF NOT EXISTS `budgetdetails` (
  `journeyid` varchar(30) NOT NULL,
  `itemname` varchar(50) NOT NULL,
  `value` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `budgetdetails`
--

INSERT INTO `budgetdetails` (`journeyid`, `itemname`, `value`) VALUES
('2574', 'Clothes', 100),
('2574', 'Food', 1000),
('2574', 'Hotel Booking', 4000),
('2574', 'Train Tickets', 3000);

-- --------------------------------------------------------

--
-- Table structure for table `journeydetails`
--

CREATE TABLE IF NOT EXISTS `journeydetails` (
  `Name` varchar(50) NOT NULL,
  `Role` varchar(50) NOT NULL,
  `JName` varchar(100) NOT NULL,
  `JPass` varchar(100) NOT NULL,
  `DOJ` date NOT NULL,
  `journeyid` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `journeydetails`
--

INSERT INTO `journeydetails` (`Name`, `Role`, `JName`, `JPass`, `DOJ`, `journeyid`) VALUES
('arpan', 'Member', 'Bengaluru', '1234567890', '2015-08-14', '2574'),
('sidd.pandey', 'Admin', 'Bengaluru', '1234567890', '2015-08-14', '2574');

-- --------------------------------------------------------

--
-- Table structure for table `journeyid`
--

CREATE TABLE IF NOT EXISTS `journeyid` (
  `journeyid` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `journeyid`
--

INSERT INTO `journeyid` (`journeyid`) VALUES
('2574');

-- --------------------------------------------------------

--
-- Table structure for table `scheduledetails`
--

CREATE TABLE IF NOT EXISTS `scheduledetails` (
  `journeyid` varchar(30) NOT NULL,
  `scheduledetails` varchar(100) NOT NULL,
  `position` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `todolistdetails`
--

CREATE TABLE IF NOT EXISTS `todolistdetails` (
  `journeyid` varchar(30) NOT NULL,
  `Name` varchar(50) NOT NULL,
  `ItemName` varchar(30) NOT NULL,
  `Checked` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `todolistdetails`
--

INSERT INTO `todolistdetails` (`journeyid`, `Name`, `ItemName`, `Checked`) VALUES
('2574', 'arpan', 'Bring your laptops', 1),
('2574', 'sidd.pandey', 'Bring your laptops', 1);

-- --------------------------------------------------------

--
-- Table structure for table `topicchat`
--

CREATE TABLE IF NOT EXISTS `topicchat` (
  `journeyid` varchar(30) NOT NULL,
  `TName` varchar(50) NOT NULL,
  `Sender` varchar(50) NOT NULL,
  `Time` bigint(20) NOT NULL,
  `Message` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `topicchat`
--

INSERT INTO `topicchat` (`journeyid`, `TName`, `Sender`, `Time`, `Message`) VALUES
('2574', 'Departure', 'arpan', 1443373402375, 'Hello'),
('2574', 'Departure', 'sidd.pandey', 1443373387980, 'Hi everyone');

-- --------------------------------------------------------

--
-- Table structure for table `topicdetails`
--

CREATE TABLE IF NOT EXISTS `topicdetails` (
  `journeyid` varchar(30) NOT NULL,
  `TName` varchar(50) NOT NULL,
  `TSummary` varchar(200) DEFAULT 'Add your summary here!'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `topicdetails`
--

INSERT INTO `topicdetails` (`journeyid`, `TName`, `TSummary`) VALUES
('2574', 'Arrival Date', 'So we should arrive at 18th October, final :)'),
('2574', 'Departure', 'Departure date is 20th oCtober');

-- --------------------------------------------------------

--
-- Table structure for table `userdetails`
--

CREATE TABLE IF NOT EXISTS `userdetails` (
  `USERNAME` varchar(30) NOT NULL,
  `PASSWORD` varchar(30) NOT NULL,
  `EMAIL` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `userdetails`
--

INSERT INTO `userdetails` (`USERNAME`, `PASSWORD`, `EMAIL`) VALUES
('arpan', '123456', 'dgutf'),
('himu', '1234', 'xyz'),
('sidd.pandey', '123456', 'sidd@gmail.com');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `budgetdetails`
--
ALTER TABLE `budgetdetails`
 ADD PRIMARY KEY (`journeyid`,`itemname`);

--
-- Indexes for table `journeydetails`
--
ALTER TABLE `journeydetails`
 ADD PRIMARY KEY (`Name`,`JName`), ADD KEY `journeyid` (`journeyid`);

--
-- Indexes for table `journeyid`
--
ALTER TABLE `journeyid`
 ADD PRIMARY KEY (`journeyid`);

--
-- Indexes for table `scheduledetails`
--
ALTER TABLE `scheduledetails`
 ADD PRIMARY KEY (`journeyid`,`position`);

--
-- Indexes for table `todolistdetails`
--
ALTER TABLE `todolistdetails`
 ADD PRIMARY KEY (`journeyid`,`Name`,`ItemName`);

--
-- Indexes for table `topicchat`
--
ALTER TABLE `topicchat`
 ADD PRIMARY KEY (`journeyid`,`TName`,`Sender`,`Time`), ADD KEY `Sender` (`Sender`);

--
-- Indexes for table `topicdetails`
--
ALTER TABLE `topicdetails`
 ADD PRIMARY KEY (`journeyid`,`TName`);

--
-- Indexes for table `userdetails`
--
ALTER TABLE `userdetails`
 ADD PRIMARY KEY (`USERNAME`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `journeydetails`
--
ALTER TABLE `journeydetails`
ADD CONSTRAINT `journeydetails_ibfk_1` FOREIGN KEY (`Name`) REFERENCES `userdetails` (`USERNAME`),
ADD CONSTRAINT `journeydetails_ibfk_2` FOREIGN KEY (`journeyid`) REFERENCES `journeyid` (`journeyid`);

--
-- Constraints for table `topicchat`
--
ALTER TABLE `topicchat`
ADD CONSTRAINT `topicchat_ibfk_1` FOREIGN KEY (`journeyid`) REFERENCES `journeyid` (`journeyid`),
ADD CONSTRAINT `topicchat_ibfk_2` FOREIGN KEY (`Sender`) REFERENCES `userdetails` (`USERNAME`);

--
-- Constraints for table `topicdetails`
--
ALTER TABLE `topicdetails`
ADD CONSTRAINT `topicdetails_ibfk_1` FOREIGN KEY (`journeyid`) REFERENCES `journeyid` (`journeyid`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
