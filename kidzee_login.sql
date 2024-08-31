-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 31, 2024 at 11:25 AM
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
-- Database: `kidzee_login`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin_login`
--

CREATE TABLE `admin_login` (
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin_login`
--

INSERT INTO `admin_login` (`username`, `password`) VALUES
('admin', 'admin');

-- --------------------------------------------------------

--
-- Table structure for table `courses`
--

CREATE TABLE `courses` (
  `courseId` varchar(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `credit` int(11) NOT NULL,
  `isGpa` enum('gpa','non-gpa') NOT NULL,
  `description` text DEFAULT NULL,
  `materials` varchar(255) DEFAULT NULL,
  `userId` varchar(10) DEFAULT NULL,
  `depId` varchar(10) DEFAULT NULL,
  `levelSem` enum('L1S1','L1S2','L2S1','L2S2') DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `courses`
--

INSERT INTO `courses` (`courseId`, `name`, `credit`, `isGpa`, `description`, `materials`, `userId`, `depId`, `levelSem`, `created_at`, `updated_at`) VALUES
('1', 'JAVA', 6, 'gpa', 'aksdjf', 'D:\\Krish Sem2\\PRACTICE BOOK_SEM II_JAVA-II.pdf', '1', '1', 'L1S1', '2024-08-29 09:53:18', '2024-08-29 09:53:18'),
('2', 'DBMS', 4, 'gpa', 'amojlskd', 'D:\\Krish Sem2\\02 PRACTICE BOOK_DBMS_SEM-2_2024_CE RELATED BRANCHES.pdf', '2', '2', 'L1S1', '2024-08-29 09:30:25', '2024-08-29 09:30:25');

-- --------------------------------------------------------

--
-- Table structure for table `notices`
--

CREATE TABLE `notices` (
  `id` varchar(11) NOT NULL,
  `department` varchar(50) NOT NULL,
  `title` varchar(255) NOT NULL,
  `pdf_path` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `notices`
--

INSERT INTO `notices` (`id`, `department`, `title`, `pdf_path`) VALUES
('N0001', 'departments', 'New Notice ', 'D:\\Krish Sem2\\Final_SEM II_DS_PRACTICE BOOK _25 June 2024.pdf'),
('N0002', 'students', 'Notice 2', 'D:\\Krish Sem2\\Final_SEM II_DS_PRACTICE BOOK _25 June 2024.pdf'),
('N0003', 'lecturers', 'Notice 3', 'D:\\Krish Sem2\\Final_SEM II_DS_PRACTICE BOOK _25 June 2024.pdf');

-- --------------------------------------------------------

--
-- Table structure for table `timetables`
--

CREATE TABLE `timetables` (
  `id` varchar(11) NOT NULL,
  `department` varchar(50) NOT NULL,
  `title` varchar(255) NOT NULL,
  `pdf_path` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `timetables`
--

INSERT INTO `timetables` (`id`, `department`, `title`, `pdf_path`) VALUES
('T0001', 'ICT', 'New Timetable', 'D:\\Krish Sem2\\FY_T1 to T4 Syllabus Details_SEM-II_BATCH-2023_CE Related All Subjects.pdf'),
('T0002', 'ET', 'Timetable 2', 'D:\\Krish Sem2\\FY_T1 to T4 Syllabus Details_SEM-II_BATCH-2023_CE Related All Subjects.pdf'),
('T0003', 'BST', 'Timetable 3', 'D:\\Krish Sem2\\FY_T1 to T4 Syllabus Details_SEM-II_BATCH-2023_CE Related All Subjects.pdf');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `userType` enum('student','lecturer','admin') NOT NULL,
  `gender` enum('male','female') NOT NULL,
  `dob` date NOT NULL,
  `contactNumber` varchar(15) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `depID` varchar(10) DEFAULT NULL,
  `level` enum('L1S1','L1S2','NULL') DEFAULT 'NULL',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `name`, `email`, `password`, `userType`, `gender`, `dob`, `contactNumber`, `address`, `depID`, `level`, `created_at`, `updated_at`) VALUES
(1, 'krish', 'Krish Pandya', 'krish@mail', '654321', 'student', 'male', '2001-06-23', '789', 'kajsdnf', 'bst', 'L1S2', '2024-08-29 08:52:17', '2024-08-31 00:25:27'),
(2, 'rakesh', 'Rakesh', 'rakesh@mail', '123456', 'student', 'male', '2023-04-22', '123', 'adsjfw', 'ict', 'L1S1', '2024-08-29 09:20:58', '2024-08-29 09:20:58'),
(3, 'sidhant', 'Sidhant Chavan', 'sidhant@mail', '123456', 'student', 'male', '2024-04-23', '123', 'piqjrlskdn', 'bst', 'L1S2', '2024-08-31 00:23:04', '2024-08-31 00:23:04');

-- --------------------------------------------------------

--
-- Table structure for table `user_login`
--

CREATE TABLE `user_login` (
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user_login`
--

INSERT INTO `user_login` (`username`, `password`) VALUES
('user', 'user');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `courses`
--
ALTER TABLE `courses`
  ADD PRIMARY KEY (`courseId`);

--
-- Indexes for table `notices`
--
ALTER TABLE `notices`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `timetables`
--
ALTER TABLE `timetables`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
