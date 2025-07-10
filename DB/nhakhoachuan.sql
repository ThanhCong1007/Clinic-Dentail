-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: nhakhoachuan
-- ------------------------------------------------------
-- Server version	9.1.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `anh_benh_an`
--

DROP TABLE IF EXISTS `anh_benh_an`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `anh_benh_an` (
  `ma_anh` int NOT NULL AUTO_INCREMENT,
  `ma_benh_an` int NOT NULL,
  `url` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `mo_ta` text COLLATE utf8mb4_unicode_ci,
  `ngay_tao` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_anh`),
  KEY `ma_benh_an` (`ma_benh_an`),
  CONSTRAINT `anh_benh_an_ibfk_1` FOREIGN KEY (`ma_benh_an`) REFERENCES `benh_an` (`ma_benh_an`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `anh_benh_an`
--

LOCK TABLES `anh_benh_an` WRITE;
/*!40000 ALTER TABLE `anh_benh_an` DISABLE KEYS */;
INSERT INTO `anh_benh_an` VALUES (1,147,'team1.jpg',NULL,'2025-07-06 00:04:47'),(4,149,'benh-an/benh_an_149_1751787710310.png',NULL,'2025-07-06 00:41:50'),(5,149,'benh-an/benh_an_149_1751787710322.png',NULL,'2025-07-06 00:41:50'),(10,152,'benh-an/benh_an_152_1751788663425.png',NULL,'2025-07-06 00:57:43'),(11,152,'benh-an/benh_an_152_1751788663437.png',NULL,'2025-07-06 00:57:43'),(12,153,'benh-an/benh_an_153_1751789337440.png','sdsdsds','2025-07-06 01:08:57'),(13,153,'benh-an/benh_an_153_1751789337452.png','sdsds','2025-07-06 01:08:57'),(14,153,'benh-an/benh_an_153_1752048718885.png','Kết quả siêu âm bụng','2025-07-09 01:11:59'),(15,152,'benh-an/benh_an_152_1752048878725.png','Kết quả siêu âm bụng','2025-07-09 01:14:39'),(16,152,'benh-an/benh_an_152_1752049002217.png','Kết quả siêu âm bụng','2025-07-09 01:16:42'),(17,152,'benh-an/benh_an_152_1752049290421.png','Kết quả siêu âm bụng','2025-07-09 01:22:49'),(18,152,'benh-an/benh_an_152_1752049403120.png','Kết quả siêu âm bụng','2025-07-09 01:23:32'),(19,152,'benh-an/benh_an_152_1752049757895.png','Kết quả siêu âm bụng','2025-07-09 01:29:18'),(20,156,'benh-an/benh_an_156_1752050865138.png',NULL,'2025-07-09 01:47:45'),(21,156,'benh-an/benh_an_156_1752050865149.png',NULL,'2025-07-09 01:47:45'),(22,156,'benh-an/benh_an_156_1752050865160.png',NULL,'2025-07-09 01:47:45'),(23,156,'benh-an/benh_an_156_1752050865169.png',NULL,'2025-07-09 01:47:45'),(24,156,'benh-an/benh_an_156_1752050865178.png',NULL,'2025-07-09 01:47:45'),(25,156,'benh-an/benh_an_156_1752050865187.png',NULL,'2025-07-09 01:47:45'),(26,156,'benh-an/benh_an_156_1752050865196.png',NULL,'2025-07-09 01:47:45'),(27,153,'benh-an/benh_an_153_1752051515625.png','Kết quả siêu âm bụng','2025-07-09 01:58:36'),(28,153,'benh-an/benh_an_153_1752051560761.png',NULL,'2025-07-09 01:59:21'),(29,120,'benh-an/benh_an_120_1752051603583.png',NULL,'2025-07-09 02:00:04'),(30,120,'benh-an/benh_an_120_1752051603595.png',NULL,'2025-07-09 02:00:04'),(32,157,'benh-an/benh_an_157_1752052029456.png',NULL,'2025-07-09 02:07:09');
/*!40000 ALTER TABLE `anh_benh_an` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bac_si`
--

DROP TABLE IF EXISTS `bac_si`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bac_si` (
  `ma_bac_si` int NOT NULL AUTO_INCREMENT,
  `ma_nguoi_dung` int NOT NULL,
  `chuyen_khoa` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `so_nam_kinh_nghiem` int DEFAULT NULL,
  `trang_thai_lam_viec` tinyint(1) DEFAULT '1',
  `ngay_tao` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_bac_si`),
  KEY `ma_nguoi_dung` (`ma_nguoi_dung`),
  CONSTRAINT `bac_si_ibfk_1` FOREIGN KEY (`ma_nguoi_dung`) REFERENCES `nguoi_dung` (`ma_nguoi_dung`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bac_si`
--

LOCK TABLES `bac_si` WRITE;
/*!40000 ALTER TABLE `bac_si` DISABLE KEYS */;
INSERT INTO `bac_si` VALUES (1,2,'Răng hàm mặt',5,1,'2025-05-28 07:35:37'),(2,3,'Nha chu',3,1,'2025-05-28 07:35:37'),(3,6,NULL,NULL,1,'2025-05-28 02:16:19'),(4,24,NULL,NULL,1,'2025-07-09 22:42:39');
/*!40000 ALTER TABLE `bac_si` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `benh_an`
--

DROP TABLE IF EXISTS `benh_an`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `benh_an` (
  `ma_benh_an` int NOT NULL AUTO_INCREMENT,
  `ma_lich_hen` int DEFAULT NULL,
  `ma_benh_nhan` int NOT NULL,
  `ma_bac_si` int NOT NULL,
  `ly_do_kham` text COLLATE utf8mb4_unicode_ci,
  `chan_doan` text COLLATE utf8mb4_unicode_ci,
  `ghi_chu_dieu_tri` text COLLATE utf8mb4_unicode_ci,
  `ngay_tai_kham` date DEFAULT NULL,
  `ngay_tao` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_benh_an`),
  KEY `ma_lich_hen` (`ma_lich_hen`),
  KEY `ma_benh_nhan` (`ma_benh_nhan`),
  KEY `ma_bac_si` (`ma_bac_si`),
  CONSTRAINT `benh_an_ibfk_1` FOREIGN KEY (`ma_lich_hen`) REFERENCES `lich_hen` (`ma_lich_hen`),
  CONSTRAINT `benh_an_ibfk_2` FOREIGN KEY (`ma_benh_nhan`) REFERENCES `benh_nhan` (`ma_benh_nhan`),
  CONSTRAINT `benh_an_ibfk_3` FOREIGN KEY (`ma_bac_si`) REFERENCES `bac_si` (`ma_bac_si`)
) ENGINE=InnoDB AUTO_INCREMENT=158 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `benh_an`
--

LOCK TABLES `benh_an` WRITE;
/*!40000 ALTER TABLE `benh_an` DISABLE KEYS */;
INSERT INTO `benh_an` VALUES (67,NULL,2,3,'Đau răng hàm dưới bên phải, đau khi nhai','Sâu răng số 46, viêm tủy răng cấp tính','Đã thực hiện nội nha, hàn trám răng. Tái khám sau 1 tuần để kiểm tra.','2025-06-12','2025-06-06 02:25:35'),(68,NULL,2,3,'Đau răng hàm dưới bên phải, đau khi nhai','Sâu răng số 46, viêm tủy răng cấp tính','Đã thực hiện nội nha, hàn trám răng. Tái khám sau 1 tuần để kiểm tra.','2025-06-12','2025-06-06 02:30:34'),(83,13,1,2,'3243232','23423','234234','2025-06-22','2025-06-11 05:19:18'),(84,15,2,2,'sadasd','ádasd','ádassa',NULL,'2025-06-11 05:24:08'),(85,19,2,2,'ÁDASD','ÂSDAS','ÁD',NULL,'2025-06-11 06:48:51'),(86,25,1,2,'SADAS','SDS','',NULL,'2025-06-11 06:49:10'),(87,24,1,2,'ádsd','dsfsd','fsdfsdf','2025-06-12','2025-06-11 06:52:32'),(88,28,2,2,'qưeqwe','ưqewq','qưeqwewq',NULL,'2025-06-11 21:30:54'),(93,NULL,2,3,'Đau răng hàm dưới bên phải, đau khi nhai','Sâu răng số 46, viêm tủy răng cấp tính','Đã thực hiện nội nha, . Tái khám sau 1 tuần để kiểm tra.','2025-06-12','2025-06-11 22:28:22'),(95,27,2,2,'ádasd','ádas','dsadass',NULL,'2025-06-11 22:30:43'),(96,23,1,2,'Viêm lợi mãn tính','Viêm lợi mãn tính','Viêm lợi mãn tính','2025-06-22','2025-06-11 23:45:35'),(99,NULL,12,2,'ádasd','ádas','dsadass',NULL,'2025-06-19 03:26:07'),(100,NULL,13,2,'ádasd','ádas','dsadass',NULL,'2025-06-19 03:26:28'),(101,NULL,14,2,'ádasd','ádas','dsadass',NULL,'2025-06-19 03:26:39'),(102,NULL,15,2,'ádasd','ádas','dsadass',NULL,'2025-06-19 03:32:50'),(103,NULL,16,2,'ádasd','ádas','dsadass',NULL,'2025-06-19 03:36:39'),(104,NULL,17,2,'ádasd','ádas','dsadass',NULL,'2025-06-19 03:38:28'),(105,NULL,18,2,'ádasd','ádas','dsadass',NULL,'2025-06-19 03:39:01'),(106,NULL,19,2,'ádasd','ádas','dsadass',NULL,'2025-06-19 03:46:24'),(107,NULL,20,2,'ádasd','ádas','dsadass',NULL,'2025-06-19 03:51:57'),(108,NULL,21,2,'ádasd','ádas','dsadass',NULL,'2025-06-19 04:32:02'),(109,NULL,22,2,'Đau răng hàm dưới bên phải, đau khi nhai','Sâu răng số 46, viêm tủy răng cấp tính','Đã thực hiện nội nha, . Tái khám sau 1 tuần để kiểm tra.','2025-06-12','2025-06-19 04:33:50'),(112,NULL,23,2,'ádasd','ádas','dsadass',NULL,'2025-06-19 09:19:24'),(113,NULL,24,2,'ádasd','ádas','dsadass',NULL,'2025-06-19 09:20:07'),(114,NULL,25,2,'ádasd','ádas','dsadass',NULL,'2025-06-19 09:21:06'),(115,NULL,26,2,'ádasd','ádas','dsadass',NULL,'2025-06-19 09:22:00'),(116,NULL,27,2,'ádasd','ádas','dsadass',NULL,'2025-06-19 09:22:39'),(117,NULL,29,2,'ádasd','ádas','dsadass',NULL,'2025-06-19 09:35:31'),(118,NULL,32,2,'ádas','sdfsdf','sdfsd',NULL,'2025-06-19 09:37:03'),(119,NULL,33,2,'ádas','da','da',NULL,'2025-06-19 09:39:25'),(120,NULL,34,2,'nhức răng L3','bị viêm lợi','3 ngày sau tái khám lại',NULL,'2025-06-29 20:49:19'),(121,NULL,34,2,'đasad','sadasd','âsdasdsa',NULL,'2025-06-29 21:00:56'),(122,NULL,35,2,'đau răng','Sâu răng L5','trám răng',NULL,'2025-06-29 21:32:57'),(123,NULL,36,2,'đau răng R3','viêm lợi','uống thuốc',NULL,'2025-06-29 21:36:09'),(124,NULL,37,2,'ádasd','ádas','dsadass',NULL,'2025-07-03 03:02:47'),(125,NULL,38,2,'ádasd','ádas','dsadass',NULL,'2025-07-04 01:20:27'),(126,NULL,38,2,'ádasd','ádas','dsadass','2025-07-05','2025-07-04 01:23:13'),(127,NULL,2,2,'ádasd','ádas','dsadass','2025-07-05','2025-07-04 01:24:02'),(128,NULL,2,2,'ádasd','ádas','dsadass','2025-07-05','2025-07-04 01:24:31'),(129,NULL,2,2,'ádasd','ádas','dsadass','2025-07-05','2025-07-04 01:33:26'),(130,NULL,2,2,'ádasd','ádas','dsadass','2025-07-05','2025-07-04 01:41:25'),(131,NULL,2,2,'ádasd','ádas','dsadass','2025-07-05','2025-07-04 01:41:51'),(133,NULL,2,2,'ádasd','ádas','dsadass','2025-07-05','2025-07-04 02:16:39'),(135,NULL,2,2,'ádasd','ádas','dsadass','2025-07-05','2025-07-04 02:20:41'),(137,NULL,2,2,'ádasd','ádas','dsadass','2025-07-05','2025-07-04 02:22:02'),(138,NULL,39,1,'đasadasd','ádsad','âsdasdas','2025-07-17','2025-07-04 03:04:12'),(139,NULL,40,1,'ểtrtererter','tểtrter','tểtrtreer','2025-07-24','2025-07-04 03:05:27'),(140,NULL,41,1,'ádasdas','ádasdas','đâsdasd','2025-07-18','2025-07-04 03:07:55'),(142,NULL,38,2,'ádasd','ádas','dsadass','2025-07-15','2025-07-05 23:41:13'),(144,NULL,38,2,'ádasd','ádas','dsadass','2025-07-12','2025-07-05 23:43:02'),(147,NULL,38,2,'ádasd','ádas','dsadass','2025-07-13','2025-07-06 00:04:47'),(149,NULL,38,2,'ádasd','ádas','dsadass','2025-07-14','2025-07-06 00:41:50'),(152,NULL,38,2,'Tái khám theo dõi','Đau dạ dày mãn tính - đã cải thiện','Bệnh nhân đã tuân thủ điều trị tốt','2024-01-15','2025-07-06 00:57:43'),(153,NULL,42,2,'Tái khám theo dõi','Đau dạ dày mãn tính - đã cải thiện','Bệnh nhân đã tuân thủ điều trị tốt','2024-01-15','2025-07-06 01:08:57'),(156,NULL,43,2,'ádasdasas','đâs','âsdsaasdas',NULL,'2025-07-09 01:47:45'),(157,NULL,38,2,'ád','ád','ád',NULL,'2025-07-09 02:07:09');
/*!40000 ALTER TABLE `benh_an` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `benh_an_dich_vu`
--

DROP TABLE IF EXISTS `benh_an_dich_vu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `benh_an_dich_vu` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ma_benh_an` int NOT NULL,
  `ma_dich_vu` int NOT NULL,
  `gia` decimal(12,2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ma_benh_an` (`ma_benh_an`,`ma_dich_vu`),
  KEY `ma_dich_vu` (`ma_dich_vu`)
) ENGINE=InnoDB AUTO_INCREMENT=116 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `benh_an_dich_vu`
--

LOCK TABLES `benh_an_dich_vu` WRITE;
/*!40000 ALTER TABLE `benh_an_dich_vu` DISABLE KEYS */;
INSERT INTO `benh_an_dich_vu` VALUES (3,99,1,150000.00),(4,99,2,350000.00),(5,100,1,150000.00),(6,100,2,350000.00),(7,101,1,150000.00),(8,101,2,350000.00),(9,102,1,150000.00),(10,102,2,350000.00),(11,103,1,150000.00),(12,103,2,350000.00),(13,104,1,150000.00),(14,104,2,350000.00),(15,105,1,150000.00),(16,105,2,350000.00),(17,106,1,150000.00),(18,106,2,350000.00),(19,107,1,150000.00),(20,107,2,350000.00),(21,108,1,150000.00),(22,108,2,350000.00),(23,109,1,150000.00),(24,109,2,350000.00),(29,112,1,150000.00),(30,112,2,350000.00),(31,113,1,150000.00),(32,113,2,350000.00),(33,114,1,150000.00),(34,114,2,350000.00),(35,115,1,150000.00),(36,115,2,350000.00),(37,116,1,250000.00),(38,116,2,350000.00),(39,117,1,250000.00),(40,117,2,350000.00),(41,118,3,300000.00),(42,118,2,200000.00),(43,118,7,25000000.00),(44,118,6,1200000.00),(45,118,8,250000.00),(46,118,9,100000.00),(47,119,2,200000.00),(48,119,3,300000.00),(49,120,1,120000.00),(50,121,1,1300000.00),(51,122,2,200000.00),(52,122,3,300000.00),(53,124,1,150000.00),(54,124,2,350000.00),(55,125,1,150000.00),(56,125,2,350000.00),(57,126,1,150000.00),(58,126,2,350000.00),(59,127,1,150000.00),(60,127,2,350000.00),(61,128,1,150000.00),(62,128,2,350000.00),(63,129,1,150000.00),(64,129,2,350000.00),(65,130,1,150000.00),(66,130,2,350000.00),(67,131,1,150000.00),(68,131,2,350000.00),(71,133,1,150000.00),(72,133,2,350000.00),(75,135,1,150000.00),(76,135,2,350000.00),(79,137,1,150000.00),(80,137,2,350000.00),(81,138,3,300000.00),(82,139,1,100000.00),(83,140,2,200000.00),(84,140,1,100000.00),(87,142,1,150000.00),(88,142,2,350000.00),(91,144,1,150000.00),(92,144,2,350000.00),(97,147,1,150000.00),(98,147,2,350000.00),(101,149,1,150000.00),(102,149,2,350000.00),(107,152,1,150000.00),(108,152,2,350000.00),(109,153,3,300000.00),(114,156,3,300000.00),(115,157,3,300000.00);
/*!40000 ALTER TABLE `benh_an_dich_vu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `benh_nhan`
--

DROP TABLE IF EXISTS `benh_nhan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `benh_nhan` (
  `ma_benh_nhan` int NOT NULL AUTO_INCREMENT,
  `ma_nguoi_dung` int DEFAULT NULL,
  `ho_ten` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ngay_sinh` date DEFAULT NULL,
  `gioi_tinh` enum('Nam','Nữ','Khác') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `so_dien_thoai` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dia_chi` text COLLATE utf8mb4_unicode_ci,
  `tien_su_benh` text COLLATE utf8mb4_unicode_ci,
  `di_ung` text COLLATE utf8mb4_unicode_ci,
  `ngay_tao` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_benh_nhan`),
  KEY `ma_nguoi_dung` (`ma_nguoi_dung`),
  CONSTRAINT `benh_nhan_ibfk_1` FOREIGN KEY (`ma_nguoi_dung`) REFERENCES `nguoi_dung` (`ma_nguoi_dung`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `benh_nhan`
--

LOCK TABLES `benh_nhan` WRITE;
/*!40000 ALTER TABLE `benh_nhan` DISABLE KEYS */;
INSERT INTO `benh_nhan` VALUES (1,4,'Phạm Thị D','1995-04-05','Nữ','0934567890','user01@example.com','123 Lê Lợi, TP.HCM','Viêm lợi mãn tính','Penicillin','2025-05-28 07:35:37'),(2,5,'Phạm Thị Dieu','1995-04-12','Nữ','0934567890','user03@example.com','123 Lê Lợi, TP.HCM','Viêm lợi mãn tính','Penicillin','2025-05-28 07:35:37'),(5,NULL,'Trần Thị B','1990-05-15','Nữ','0987654321','tranthib@email.com','123 Đường ABC, Quận 1, TP.HCM','Không có tiền sử bệnh đặc biệt','Không có dị ứng','2025-05-28 23:28:19'),(6,8,'Nguyen van si','2000-01-15',NULL,'0987654321','user07@example.com',NULL,NULL,NULL,'2025-05-29 00:04:28'),(7,NULL,'Trần Thị B','1990-05-15','Nữ','0987654322','tranthib@email.com','123 Đường ABC, Quận 1, TP.HCM','Không có tiền sử bệnh đặc biệt','Không có dị ứng','2025-05-29 00:33:44'),(12,NULL,'Phạm Thị Dieu','1995-04-05',NULL,'0934567890',NULL,NULL,'ádsadsa','ádsadasdsadas','2025-06-19 03:26:07'),(13,NULL,'Phạm Thị Dieu','1995-04-05',NULL,'0934567890',NULL,NULL,'ádsadsa','ádsadasdsadas','2025-06-19 03:26:28'),(14,NULL,'Phạm Thị Dieu','1995-04-05',NULL,'093456789',NULL,NULL,'ádsadsa','ádsadasdsadas','2025-06-19 03:26:39'),(15,NULL,'Phạm Thị Dieu','1995-04-05',NULL,'093456789','asdasdsa@gmai.com','đâsadassadsa','ádsadsa','ádsadasdsadas','2025-06-19 03:31:12'),(16,NULL,'Phạm Thị Dieu','1995-04-05',NULL,'093456789','asdasdsa@gmai.com','đâsadassadsa','ádsadsa','ádsadasdsadas','2025-06-19 03:36:36'),(17,NULL,'Phạm Thị Dieu','1995-04-05',NULL,'093456789','asdasdsa@gmai.com','đâsadassadsa','ádsadsa','ádsadasdsadas','2025-06-19 03:38:28'),(18,NULL,'Phạm Thị Dieu','1995-04-05',NULL,'093456789','asdasdsa@gmai.com','đâsadassadsa','ádsadsa','ádsadasdsadas','2025-06-19 03:38:48'),(19,NULL,'Phạm Thị Dieu','1995-04-05',NULL,'093456789','asdasdsa@gmai.com','đâsadassadsa','ádsadsa','ádsadasdsadas','2025-06-19 03:46:21'),(20,NULL,'Phạm Thị Dieu','1995-04-05',NULL,'093456789','asdasdsa@gmai.com','đâsadassadsa','ádsadsa','ádsadasdsadas','2025-06-19 03:51:43'),(21,NULL,'Phạm Thị Dieu','1995-04-05',NULL,'093456789','asdasdsa@gmai.com','đâsadassadsa','ádsadsa','ádsadasdsadas','2025-06-19 04:32:02'),(22,NULL,'Phạm Thị Dieu','1995-04-12',NULL,'0934567890','user03@example.com','123 Lê Lợi, TP.HCM','Viêm lợi mãn tính','Penicillin','2025-06-19 04:33:50'),(23,NULL,'Phạm Thị Dieu','1995-04-05',NULL,'093456789','asdasdsa@gmai.com','đâsadassadsa','ádsadsa','ádsadasdsadas','2025-06-19 09:19:24'),(24,NULL,'Phạm Thị Dieu','1995-04-05',NULL,'093456789','asdasdsa@gmai.com','đâsadassadsa','ádsadsa','ádsadasdsadas','2025-06-19 09:20:07'),(25,NULL,'Phạm Thị Dieu','1995-04-05',NULL,'093456789','asdasdsa@gmai.com','đâsadassadsa','ádsadsa','ádsadasdsadas','2025-06-19 09:21:06'),(26,NULL,'Phạm Thị Dieu','1995-04-05',NULL,'093456789','asdasdsa@gmai.com','đâsadassadsa','ádsadsa','ádsadasdsadas','2025-06-19 09:22:00'),(27,NULL,'Phạm Thị Dieu','1995-04-05',NULL,'093456789','asdasdsa@gmai.com','đâsadassadsa','ádsadsa','ádsadasdsadas','2025-06-19 09:22:39'),(29,NULL,'Phạm Thị Dieu','1995-04-15',NULL,'093456789','asdasdsa@gmai.com','đâsadassadsa','ádsadsa','ádsadasdsadas','2025-06-19 09:35:31'),(32,NULL,'đây','2004-03-12',NULL,'0987777777',NULL,NULL,'dsfsdf','sdfsdf','2025-06-19 09:37:03'),(33,NULL,'ádasdas','2025-06-01',NULL,'0987777777',NULL,NULL,'ád','da','2025-06-19 09:39:25'),(34,9,'Nguyễn Thành Đạt','2003-08-15',NULL,'0123456789',NULL,NULL,'không có','không có','2025-06-29 20:49:19'),(35,10,'Nguyễn Thành Công','2003-07-10',NULL,'0962239242',NULL,NULL,'không có','không có','2025-06-29 21:32:53'),(36,14,'Nguyễn Hoàng Bảo','2003-05-15',NULL,'0937877312',NULL,NULL,NULL,NULL,'2025-06-29 21:36:04'),(37,15,'Phạm Thị Dieu','1995-04-05',NULL,'093456789','asdasdsa@gmai.com','đâsadassadsa','ádsadsa','ádsadasdsadas','2025-07-03 03:02:42'),(38,17,'Phạm Thị Dieu','1995-04-05',NULL,'093456779','asasasdsa@gmai.com','đâsadassadsa','ádsadsa','ádsadasdsadas','2025-07-04 01:20:22'),(39,18,'ádsad','2025-07-18',NULL,'0988888867',NULL,NULL,NULL,NULL,'2025-07-04 03:04:08'),(40,19,'áddsfgfdgdftygergvxc','2025-06-10',NULL,'0955565655',NULL,NULL,NULL,NULL,'2025-07-04 03:05:23'),(41,21,'ádsadas','2025-07-01',NULL,'09777777777',NULL,NULL,NULL,NULL,'2025-07-04 03:07:51'),(42,22,'Nguyễn thị ví dụ','2025-07-01','Nữ','12321321321','tringuyen@gmail.com','1231232112','','','2025-07-06 01:08:53'),(43,23,'ádas','2025-07-02','Nam','dsasadsadsadsa','asdsadsadsa@gmas.cs','ádasdsasadassa',NULL,NULL,'2025-07-09 01:47:40');
/*!40000 ALTER TABLE `benh_nhan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chi_tiet_don_thuoc`
--

DROP TABLE IF EXISTS `chi_tiet_don_thuoc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chi_tiet_don_thuoc` (
  `ma_chi_tiet` int NOT NULL AUTO_INCREMENT,
  `ma_don_thuoc` int NOT NULL,
  `ma_thuoc` int NOT NULL,
  `lieu_dung` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tan_suat` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `thoi_diem` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `thoi_gian_dieu_tri` int DEFAULT NULL,
  `so_luong` int NOT NULL,
  `don_vi_dung` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `don_gia` decimal(12,2) NOT NULL,
  `thanh_tien` decimal(12,2) NOT NULL,
  `canh_bao_tuong_tac` text COLLATE utf8mb4_unicode_ci,
  `canh_bao_lieu_dung` text COLLATE utf8mb4_unicode_ci,
  `ly_do_ke_don` text COLLATE utf8mb4_unicode_ci,
  `ghi_chu` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`ma_chi_tiet`),
  KEY `ma_ke_thuoc` (`ma_don_thuoc`),
  KEY `ma_thuoc` (`ma_thuoc`),
  CONSTRAINT `chi_tiet_don_thuoc_ibfk_1` FOREIGN KEY (`ma_don_thuoc`) REFERENCES `don_thuoc` (`ma_don_thuoc`),
  CONSTRAINT `chi_tiet_don_thuoc_ibfk_2` FOREIGN KEY (`ma_thuoc`) REFERENCES `thuoc` (`ma_thuoc`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chi_tiet_don_thuoc`
--

LOCK TABLES `chi_tiet_don_thuoc` WRITE;
/*!40000 ALTER TABLE `chi_tiet_don_thuoc` DISABLE KEYS */;
INSERT INTO `chi_tiet_don_thuoc` VALUES (17,45,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(18,46,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(19,47,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(20,48,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(21,49,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(22,50,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(23,51,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(24,52,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(25,53,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(26,54,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(27,55,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(28,58,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(29,59,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(30,60,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(31,61,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(32,62,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(33,63,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(34,64,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(35,64,114,'40','Lợi tiểu quai','',7,1,'mg',5000.00,5000.00,NULL,NULL,'Lợi tiểu quai',''),(36,65,114,'40','Lợi tiểu quai','',7,1,'mg',5000.00,5000.00,NULL,NULL,'Lợi tiểu quai',''),(37,65,111,'5','Glucocorticoid giảm viêm','',7,1,'mg',5000.00,5000.00,NULL,NULL,'Glucocorticoid giảm viêm',''),(38,66,114,'','','',7,1,'viên',5000.00,5000.00,NULL,NULL,'Furosemide',''),(39,66,108,'','','',7,1,'viên',10000.00,10000.00,NULL,NULL,'Omeprazole',''),(40,67,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(41,68,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(42,69,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(43,70,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(44,71,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(45,72,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(46,73,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(47,74,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(49,76,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(51,78,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(53,80,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(55,82,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(57,84,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(60,87,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone',''),(62,89,106,'500','Kháng sinh fluoroquinolone','',7,1,'mg',20000.00,20000.00,NULL,NULL,'Kháng sinh fluoroquinolone','');
/*!40000 ALTER TABLE `chi_tiet_don_thuoc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chi_tiet_hoa_don`
--

DROP TABLE IF EXISTS `chi_tiet_hoa_don`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chi_tiet_hoa_don` (
  `ma_muc` int NOT NULL AUTO_INCREMENT,
  `ma_hoa_don` int NOT NULL,
  `mo_ta` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `so_luong` int NOT NULL DEFAULT '1',
  `don_gia` decimal(12,2) NOT NULL,
  `thanh_tien` decimal(12,2) NOT NULL,
  `ngay_tao` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `ma_benh_an_dich_vu` int DEFAULT NULL,
  PRIMARY KEY (`ma_muc`),
  KEY `ma_hoa_don` (`ma_hoa_don`),
  KEY `fk_benh_an_dich_vu` (`ma_benh_an_dich_vu`),
  CONSTRAINT `chi_tiet_hoa_don_ibfk_1` FOREIGN KEY (`ma_hoa_don`) REFERENCES `hoa_don` (`ma_hoa_don`),
  CONSTRAINT `fk_benh_an_dich_vu` FOREIGN KEY (`ma_benh_an_dich_vu`) REFERENCES `benh_an_dich_vu` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=318 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chi_tiet_hoa_don`
--

LOCK TABLES `chi_tiet_hoa_don` WRITE;
/*!40000 ALTER TABLE `chi_tiet_hoa_don` DISABLE KEYS */;
INSERT INTO `chi_tiet_hoa_don` VALUES (121,43,'Khám tổng quát',1,100000.00,100000.00,'2025-06-06 02:25:35',NULL),(122,43,'Trám răng',1,300000.00,300000.00,'2025-06-06 02:25:35',NULL),(123,43,'Nhổ răng vĩnh viễn',1,500000.00,500000.00,'2025-06-06 02:25:35',NULL),(124,44,'Khám tổng quát',1,100000.00,100000.00,'2025-06-06 02:33:03',NULL),(125,44,'Trám răng',1,300000.00,300000.00,'2025-06-06 02:33:03',NULL),(126,44,'Nhổ răng vĩnh viễn',1,500000.00,500000.00,'2025-06-06 02:33:03',NULL),(145,59,'Khám tổng quát',1,100000.00,100000.00,'2025-06-11 05:19:18',NULL),(146,59,'Trám răng',1,300000.00,300000.00,'2025-06-11 05:19:18',NULL),(147,60,'Khám tổng quát',1,100000.00,100000.00,'2025-06-11 05:24:08',NULL),(148,60,'Trám răng',1,300000.00,300000.00,'2025-06-11 05:24:08',NULL),(149,61,'Khám tổng quát',1,100000.00,100000.00,'2025-06-11 06:48:51',NULL),(150,62,'Khám tổng quát',1,100000.00,100000.00,'2025-06-11 06:49:10',NULL),(151,63,'Khám tổng quát',1,100000.00,100000.00,'2025-06-11 06:52:32',NULL),(152,63,'Trám răng',1,300000.00,300000.00,'2025-06-11 06:52:32',NULL),(153,63,'Thuốc: Paracetamol',1,10000.00,10000.00,'2025-06-11 06:52:32',NULL),(154,64,'Khám tổng quát',1,100000.00,100000.00,'2025-06-11 21:30:55',NULL),(155,64,'Trám răng',1,300000.00,300000.00,'2025-06-11 21:30:55',NULL),(156,64,'Thuốc: Paracetamol',21,10000.00,210000.00,'2025-06-11 21:30:55',NULL),(157,64,'Thuốc: Furosemide',1,5000.00,5000.00,'2025-06-11 21:30:55',NULL),(158,64,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-06-11 21:30:55',NULL),(159,64,'Thuốc: Aspirin',1,8000.00,8000.00,'2025-06-11 21:30:55',NULL),(170,69,'Khám tổng quát',1,100000.00,100000.00,'2025-06-11 22:28:22',NULL),(171,69,'Trám răng',1,300000.00,300000.00,'2025-06-11 22:28:22',NULL),(172,69,'Nhổ răng vĩnh viễn',1,500000.00,500000.00,'2025-06-11 22:28:22',NULL),(175,71,'Khám tổng quát',1,100000.00,100000.00,'2025-06-11 22:30:43',NULL),(176,71,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-06-11 22:30:43',NULL),(177,72,'Khám tổng quát',1,100000.00,100000.00,'2025-06-11 23:45:35',NULL),(178,72,'Trám răng',1,300000.00,300000.00,'2025-06-11 23:45:35',NULL),(179,72,'Thuốc: Paracetamol',1,10000.00,10000.00,'2025-06-11 23:45:35',NULL),(180,74,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-06-19 03:26:07',NULL),(181,75,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-06-19 03:26:28',NULL),(182,76,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-06-19 03:26:39',NULL),(183,77,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-06-19 03:34:17',NULL),(184,78,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-06-19 03:36:54',NULL),(185,79,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-06-19 03:38:28',NULL),(186,80,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-06-19 03:45:15',NULL),(187,81,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-06-19 03:47:55',NULL),(188,82,'Kiểm tra tổng quát tình trạng răng miệng',1,150000.00,150000.00,'2025-06-19 03:52:28',19),(189,82,'Làm sạch cao răng và mảng bám',1,350000.00,350000.00,'2025-06-19 03:52:33',20),(190,82,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-06-19 03:52:42',NULL),(191,83,'Kiểm tra tổng quát tình trạng răng miệng',1,150000.00,150000.00,'2025-06-19 04:32:02',21),(192,83,'Làm sạch cao răng và mảng bám',1,350000.00,350000.00,'2025-06-19 04:32:02',22),(193,83,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-06-19 04:32:02',NULL),(194,84,'Kiểm tra tổng quát tình trạng răng miệng',1,150000.00,150000.00,'2025-06-19 04:33:50',23),(195,84,'Làm sạch cao răng và mảng bám',1,350000.00,350000.00,'2025-06-19 04:33:50',24),(196,84,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-06-19 04:33:50',NULL),(197,87,'Kiểm tra tổng quát tình trạng răng miệng',1,150000.00,150000.00,'2025-06-19 09:19:24',29),(198,87,'Làm sạch cao răng và mảng bám',1,350000.00,350000.00,'2025-06-19 09:19:24',30),(199,87,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-06-19 09:19:24',NULL),(200,88,'Kiểm tra tổng quát tình trạng răng miệng',1,150000.00,150000.00,'2025-06-19 09:20:07',31),(201,88,'Làm sạch cao răng và mảng bám',1,350000.00,350000.00,'2025-06-19 09:20:07',32),(202,88,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-06-19 09:20:07',NULL),(203,89,'Kiểm tra tổng quát tình trạng răng miệng',1,150000.00,150000.00,'2025-06-19 09:21:06',33),(204,89,'Làm sạch cao răng và mảng bám',1,350000.00,350000.00,'2025-06-19 09:21:06',34),(205,89,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-06-19 09:21:06',NULL),(206,90,'Kiểm tra tổng quát tình trạng răng miệng',1,150000.00,150000.00,'2025-06-19 09:22:00',35),(207,90,'Làm sạch cao răng và mảng bám',1,350000.00,350000.00,'2025-06-19 09:22:00',36),(208,90,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-06-19 09:22:00',NULL),(209,91,'Kiểm tra tổng quát tình trạng răng miệng',1,250000.00,250000.00,'2025-06-19 09:22:39',37),(210,91,'Làm sạch cao răng và mảng bám',1,350000.00,350000.00,'2025-06-19 09:22:39',38),(211,91,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-06-19 09:22:39',NULL),(212,92,'Kiểm tra tổng quát tình trạng răng miệng',1,250000.00,250000.00,'2025-06-19 09:35:31',39),(213,92,'Làm sạch cao răng và mảng bám',1,350000.00,350000.00,'2025-06-19 09:35:31',40),(214,92,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-06-19 09:35:31',NULL),(215,93,'Trám răng sâu bằng vật liệu composite',1,300000.00,300000.00,'2025-06-19 09:37:03',41),(216,93,'Làm sạch cao răng và mảng bám',1,200000.00,200000.00,'2025-06-19 09:37:03',42),(217,93,'Tư vấn và thực hiện niềng răng chỉnh nha',1,25000000.00,25000000.00,'2025-06-19 09:37:03',43),(218,93,'Tẩy trắng răng bằng công nghệ laser',1,1200000.00,1200000.00,'2025-06-19 09:37:03',44),(219,93,'Chụp phim X-quang toàn hàm',1,250000.00,250000.00,'2025-06-19 09:37:03',45),(220,93,'Tái khám nha khoa',1,100000.00,100000.00,'2025-06-19 09:37:03',46),(221,93,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-06-19 09:37:03',NULL),(222,93,'Thuốc: Furosemide',1,5000.00,5000.00,'2025-06-19 09:37:03',NULL),(223,94,'Làm sạch cao răng và mảng bám',1,200000.00,200000.00,'2025-06-19 09:39:25',47),(224,94,'Trám răng sâu bằng vật liệu composite',1,300000.00,300000.00,'2025-06-19 09:39:25',48),(225,94,'Thuốc: Furosemide',1,5000.00,5000.00,'2025-06-19 09:39:25',NULL),(226,94,'Thuốc: Prednisolone',1,5000.00,5000.00,'2025-06-19 09:39:25',NULL),(227,95,'Kiểm tra tổng quát tình trạng răng miệng',1,120000.00,120000.00,'2025-06-29 20:49:19',49),(228,96,'Kiểm tra tổng quát tình trạng răng miệng',1,1300000.00,1300000.00,'2025-06-29 21:00:56',50),(229,97,'Làm sạch cao răng và mảng bám',1,200000.00,200000.00,'2025-06-29 21:32:57',51),(230,97,'Trám răng sâu bằng vật liệu composite',1,300000.00,300000.00,'2025-06-29 21:32:57',52),(231,98,'Thuốc: Furosemide',1,5000.00,5000.00,'2025-06-29 21:36:09',NULL),(232,98,'Thuốc: Omeprazole',1,10000.00,10000.00,'2025-06-29 21:36:09',NULL),(233,99,'Kiểm tra tổng quát tình trạng răng miệng',1,150000.00,150000.00,'2025-07-03 03:02:47',53),(234,99,'Làm sạch cao răng và mảng bám',1,350000.00,350000.00,'2025-07-03 03:02:47',54),(235,99,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-07-03 03:02:47',NULL),(236,100,'Kiểm tra tổng quát tình trạng răng miệng',1,150000.00,150000.00,'2025-07-04 01:20:27',55),(237,100,'Làm sạch cao răng và mảng bám',1,350000.00,350000.00,'2025-07-04 01:20:27',56),(238,100,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-07-04 01:20:27',NULL),(239,101,'Kiểm tra tổng quát tình trạng răng miệng',1,150000.00,150000.00,'2025-07-04 01:23:13',57),(240,101,'Làm sạch cao răng và mảng bám',1,350000.00,350000.00,'2025-07-04 01:23:13',58),(241,101,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-07-04 01:23:13',NULL),(242,102,'Kiểm tra tổng quát tình trạng răng miệng',1,150000.00,150000.00,'2025-07-04 01:24:02',59),(243,102,'Làm sạch cao răng và mảng bám',1,350000.00,350000.00,'2025-07-04 01:24:02',60),(244,102,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-07-04 01:24:02',NULL),(245,103,'Kiểm tra tổng quát tình trạng răng miệng',1,150000.00,150000.00,'2025-07-04 01:24:31',61),(246,103,'Làm sạch cao răng và mảng bám',1,350000.00,350000.00,'2025-07-04 01:24:31',62),(247,103,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-07-04 01:24:31',NULL),(248,104,'Kiểm tra tổng quát tình trạng răng miệng',1,150000.00,150000.00,'2025-07-04 01:33:32',63),(249,104,'Làm sạch cao răng và mảng bám',1,350000.00,350000.00,'2025-07-04 01:33:32',64),(250,104,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-07-04 01:33:32',NULL),(251,105,'Kiểm tra tổng quát tình trạng răng miệng',1,150000.00,150000.00,'2025-07-04 01:41:25',65),(252,105,'Làm sạch cao răng và mảng bám',1,350000.00,350000.00,'2025-07-04 01:41:25',66),(253,105,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-07-04 01:41:25',NULL),(254,106,'Kiểm tra tổng quát tình trạng răng miệng',1,150000.00,150000.00,'2025-07-04 01:41:57',67),(255,106,'Làm sạch cao răng và mảng bám',1,350000.00,350000.00,'2025-07-04 01:41:57',68),(256,106,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-07-04 01:41:57',NULL),(260,108,'Kiểm tra tổng quát tình trạng răng miệng',1,150000.00,150000.00,'2025-07-04 02:16:39',71),(261,108,'Làm sạch cao răng và mảng bám',1,350000.00,350000.00,'2025-07-04 02:16:39',72),(262,108,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-07-04 02:16:39',NULL),(266,110,'Kiểm tra tổng quát tình trạng răng miệng',1,150000.00,150000.00,'2025-07-04 02:20:42',75),(267,110,'Làm sạch cao răng và mảng bám',1,350000.00,350000.00,'2025-07-04 02:20:42',76),(268,110,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-07-04 02:20:42',NULL),(272,112,'Kiểm tra tổng quát tình trạng răng miệng',1,150000.00,150000.00,'2025-07-04 02:22:02',79),(273,112,'Làm sạch cao răng và mảng bám',1,350000.00,350000.00,'2025-07-04 02:22:02',80),(274,112,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-07-04 02:22:02',NULL),(275,113,'Trám răng sâu bằng vật liệu composite',1,300000.00,300000.00,'2025-07-04 03:04:12',81),(276,114,'Kiểm tra tổng quát tình trạng răng miệng',1,100000.00,100000.00,'2025-07-04 03:05:27',82),(277,115,'Làm sạch cao răng và mảng bám',1,200000.00,200000.00,'2025-07-04 03:07:55',83),(278,115,'Kiểm tra tổng quát tình trạng răng miệng',1,100000.00,100000.00,'2025-07-04 03:07:55',84),(282,117,'Kiểm tra tổng quát tình trạng răng miệng',1,150000.00,150000.00,'2025-07-05 23:41:13',87),(283,117,'Làm sạch cao răng và mảng bám',1,350000.00,350000.00,'2025-07-05 23:41:13',88),(284,117,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-07-05 23:41:13',NULL),(288,119,'Kiểm tra tổng quát tình trạng răng miệng',1,150000.00,150000.00,'2025-07-05 23:43:02',91),(289,119,'Làm sạch cao răng và mảng bám',1,350000.00,350000.00,'2025-07-05 23:43:02',92),(290,119,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-07-05 23:43:02',NULL),(297,122,'Kiểm tra tổng quát tình trạng răng miệng',1,150000.00,150000.00,'2025-07-06 00:04:47',97),(298,122,'Làm sạch cao răng và mảng bám',1,350000.00,350000.00,'2025-07-06 00:04:47',98),(299,122,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-07-06 00:04:47',NULL),(303,124,'Kiểm tra tổng quát tình trạng răng miệng',1,150000.00,150000.00,'2025-07-06 00:41:50',101),(304,124,'Làm sạch cao răng và mảng bám',1,350000.00,350000.00,'2025-07-06 00:41:50',102),(305,124,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-07-06 00:41:50',NULL),(312,127,'Kiểm tra tổng quát tình trạng răng miệng',1,150000.00,150000.00,'2025-07-06 00:57:43',107),(313,127,'Làm sạch cao răng và mảng bám',1,350000.00,350000.00,'2025-07-06 00:57:43',108),(314,127,'Thuốc: Ciprofloxacin',1,20000.00,20000.00,'2025-07-06 00:57:43',NULL),(315,128,'Trám răng sâu bằng vật liệu composite',1,300000.00,300000.00,'2025-07-06 01:08:57',109),(316,129,'Trám răng sâu bằng vật liệu composite',1,300000.00,300000.00,'2025-07-09 01:47:45',114),(317,130,'Trám răng sâu bằng vật liệu composite',1,300000.00,300000.00,'2025-07-09 02:07:09',115);
/*!40000 ALTER TABLE `chi_tiet_hoa_don` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chi_tiet_nhap_thuoc`
--

DROP TABLE IF EXISTS `chi_tiet_nhap_thuoc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chi_tiet_nhap_thuoc` (
  `ma_chi_tiet` int NOT NULL AUTO_INCREMENT,
  `ma_nhap_thuoc` int NOT NULL,
  `ma_thuoc` int NOT NULL,
  `so_luong` int NOT NULL,
  `don_gia` decimal(12,2) NOT NULL,
  `thanh_tien` decimal(12,2) NOT NULL,
  `han_su_dung` date DEFAULT NULL,
  `so_lo` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ngay_tao` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_chi_tiet`),
  KEY `ma_nhap_thuoc` (`ma_nhap_thuoc`),
  KEY `ma_thuoc` (`ma_thuoc`),
  CONSTRAINT `chi_tiet_nhap_thuoc_ibfk_1` FOREIGN KEY (`ma_nhap_thuoc`) REFERENCES `nhap_thuoc` (`ma_nhap_thuoc`),
  CONSTRAINT `chi_tiet_nhap_thuoc_ibfk_2` FOREIGN KEY (`ma_thuoc`) REFERENCES `thuoc` (`ma_thuoc`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chi_tiet_nhap_thuoc`
--

LOCK TABLES `chi_tiet_nhap_thuoc` WRITE;
/*!40000 ALTER TABLE `chi_tiet_nhap_thuoc` DISABLE KEYS */;
/*!40000 ALTER TABLE `chi_tiet_nhap_thuoc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chi_tiet_rang`
--

DROP TABLE IF EXISTS `chi_tiet_rang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chi_tiet_rang` (
  `ma_chi_tiet_rang` int NOT NULL AUTO_INCREMENT,
  `ma_so_do` int NOT NULL,
  `so_rang` int NOT NULL,
  `tinh_trang_rang` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ghi_chu` text COLLATE utf8mb4_unicode_ci,
  `nguoi_cap_nhat` int DEFAULT NULL,
  `ngay_cap_nhat` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_chi_tiet_rang`),
  KEY `ma_so_do` (`ma_so_do`),
  KEY `nguoi_cap_nhat` (`nguoi_cap_nhat`),
  CONSTRAINT `chi_tiet_rang_ibfk_1` FOREIGN KEY (`ma_so_do`) REFERENCES `so_do_rang` (`ma_so_do`),
  CONSTRAINT `chi_tiet_rang_ibfk_2` FOREIGN KEY (`nguoi_cap_nhat`) REFERENCES `nguoi_dung` (`ma_nguoi_dung`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chi_tiet_rang`
--

LOCK TABLES `chi_tiet_rang` WRITE;
/*!40000 ALTER TABLE `chi_tiet_rang` DISABLE KEYS */;
/*!40000 ALTER TABLE `chi_tiet_rang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dich_vu`
--

DROP TABLE IF EXISTS `dich_vu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dich_vu` (
  `ma_dich_vu` int NOT NULL AUTO_INCREMENT,
  `ten_dich_vu` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `mo_ta` text COLLATE utf8mb4_unicode_ci,
  `gia` decimal(12,2) NOT NULL,
  `thoi_gian_du_kien` int NOT NULL,
  `trang_thai_hoat_dong` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`ma_dich_vu`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dich_vu`
--

LOCK TABLES `dich_vu` WRITE;
/*!40000 ALTER TABLE `dich_vu` DISABLE KEYS */;
INSERT INTO `dich_vu` VALUES (1,'Khám tổng quát','Kiểm tra tổng quát tình trạng răng miệng',100000.00,30,1),(2,'Lấy cao răng','Làm sạch cao răng và mảng bám',200000.00,45,1),(3,'Trám răng','Trám răng sâu bằng vật liệu composite',300000.00,60,1),(4,'Nhổ răng sữa','Nhổ răng sữa cho trẻ em',150000.00,30,1),(5,'Nhổ răng vĩnh viễn','Nhổ răng vĩnh viễn thông thường',500000.00,60,1),(6,'Tẩy trắng răng','Tẩy trắng răng bằng công nghệ laser',1200000.00,90,1),(7,'Niềng răng','Tư vấn và thực hiện niềng răng chỉnh nha',25000000.00,120,1),(8,'Chụp X-quang răng','Chụp phim X-quang toàn hàm',250000.00,20,1),(9,'Tái khám','Tái khám nha khoa',100000.00,30,1);
/*!40000 ALTER TABLE `dich_vu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `don_thuoc`
--

DROP TABLE IF EXISTS `don_thuoc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `don_thuoc` (
  `ma_don_thuoc` int NOT NULL AUTO_INCREMENT,
  `ma_benh_an` int NOT NULL,
  `ma_benh_nhan` int NOT NULL,
  `ma_bac_si` int NOT NULL,
  `ngay_ke` date NOT NULL,
  `so_toa` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ma_icd` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mo_ta_chan_doan` text COLLATE utf8mb4_unicode_ci,
  `trang_thai_kiem_tra` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ket_qua_kiem_tra` text COLLATE utf8mb4_unicode_ci,
  `ma_kiem_tra` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `trang_thai_toa` enum('MOI','DA_PHAT_HANH','DA_PHAT_THUOC','HUY') COLLATE utf8mb4_unicode_ci DEFAULT 'MOI',
  `ngay_phat_hanh` datetime DEFAULT NULL,
  `ngay_het_han` date DEFAULT NULL,
  `nguoi_phat_thuoc` int DEFAULT NULL,
  `ghi_chu` text COLLATE utf8mb4_unicode_ci,
  `ngay_tao` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `ngay_cap_nhat` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_don_thuoc`),
  KEY `ma_benh_an` (`ma_benh_an`),
  KEY `ma_benh_nhan` (`ma_benh_nhan`),
  KEY `ma_bac_si` (`ma_bac_si`),
  KEY `nguoi_phat_thuoc` (`nguoi_phat_thuoc`),
  CONSTRAINT `don_thuoc_ibfk_1` FOREIGN KEY (`ma_benh_an`) REFERENCES `benh_an` (`ma_benh_an`),
  CONSTRAINT `don_thuoc_ibfk_2` FOREIGN KEY (`ma_benh_nhan`) REFERENCES `benh_nhan` (`ma_benh_nhan`),
  CONSTRAINT `don_thuoc_ibfk_3` FOREIGN KEY (`ma_bac_si`) REFERENCES `bac_si` (`ma_bac_si`),
  CONSTRAINT `don_thuoc_ibfk_4` FOREIGN KEY (`nguoi_phat_thuoc`) REFERENCES `nguoi_dung` (`ma_nguoi_dung`)
) ENGINE=InnoDB AUTO_INCREMENT=96 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `don_thuoc`
--

LOCK TABLES `don_thuoc` WRITE;
/*!40000 ALTER TABLE `don_thuoc` DISABLE KEYS */;
INSERT INTO `don_thuoc` VALUES (45,99,12,2,'2025-06-19',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-06-19 03:26:07',NULL),(46,100,13,2,'2025-06-19',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-06-19 03:26:28',NULL),(47,101,14,2,'2025-06-19',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-06-19 03:26:39',NULL),(48,102,15,2,'2025-06-19',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-06-19 03:33:36',NULL),(49,103,16,2,'2025-06-19',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-06-19 03:36:46',NULL),(50,104,17,2,'2025-06-19',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-06-19 03:38:28',NULL),(51,105,18,2,'2025-06-19',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-06-19 03:39:17',NULL),(52,106,19,2,'2025-06-19',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-06-19 03:46:30',NULL),(53,107,20,2,'2025-06-19',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-06-19 03:52:04',NULL),(54,108,21,2,'2025-06-19',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-06-19 04:32:02',NULL),(55,109,22,2,'2025-06-19',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-06-19 04:33:50',NULL),(58,112,23,2,'2025-06-19',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-06-19 09:19:24',NULL),(59,113,24,2,'2025-06-19',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-06-19 09:20:07',NULL),(60,114,25,2,'2025-06-19',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-06-19 09:21:06',NULL),(61,115,26,2,'2025-06-19',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-06-19 09:22:00',NULL),(62,116,27,2,'2025-06-19',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-06-19 09:22:39',NULL),(63,117,29,2,'2025-06-19',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-06-19 09:35:31',NULL),(64,118,32,2,'2025-06-19',NULL,NULL,'sdfsdf','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-06-19 09:37:03',NULL),(65,119,33,2,'2025-06-19',NULL,NULL,'da','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-06-19 09:39:25',NULL),(66,123,36,2,'2025-06-30',NULL,NULL,'viêm lợi','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'','2025-06-29 21:36:09',NULL),(67,124,37,2,'2025-07-03',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-07-03 03:02:47',NULL),(68,125,38,2,'2025-07-04',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-07-04 01:20:27',NULL),(69,126,38,2,'2025-07-04',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-07-04 01:23:13',NULL),(70,127,2,2,'2025-07-04',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-07-04 01:24:02',NULL),(71,128,2,2,'2025-07-04',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-07-04 01:24:31',NULL),(72,129,2,2,'2025-07-04',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-07-04 01:33:29',NULL),(73,130,2,2,'2025-07-04',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-07-04 01:41:25',NULL),(74,131,2,2,'2025-07-04',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-07-04 01:41:56',NULL),(76,133,2,2,'2025-07-04',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-07-04 02:16:39',NULL),(78,135,2,2,'2025-07-04',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-07-04 02:20:41',NULL),(80,137,2,2,'2025-07-04',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-07-04 02:22:02',NULL),(82,142,38,2,'2025-07-06',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-07-05 23:41:13',NULL),(84,144,38,2,'2025-07-06',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-07-05 23:43:02',NULL),(87,147,38,2,'2025-07-06',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-07-06 00:04:47',NULL),(89,149,38,2,'2025-07-06',NULL,NULL,'ádas','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Bệnh nhân cần tuân thủ đúng liều lượng và thời gian dùng thuốc','2025-07-06 00:41:50',NULL),(92,152,38,2,'2025-07-06',NULL,NULL,'Viêm dạ dày mãn tính','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Uống thuốc sau ăn','2025-07-06 00:57:43',NULL),(93,153,42,2,'2025-07-09',NULL,NULL,'Viêm dạ dày mãn tính','CHO_KIEM_TRA',NULL,NULL,'MOI',NULL,NULL,NULL,'Uống thuốc sau ăn','2025-07-09 01:11:59',NULL);
/*!40000 ALTER TABLE `don_thuoc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hoa_don`
--

DROP TABLE IF EXISTS `hoa_don`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hoa_don` (
  `ma_hoa_don` int NOT NULL AUTO_INCREMENT,
  `ma_benh_nhan` int NOT NULL,
  `ma_lich_hen` int DEFAULT NULL,
  `tong_tien` decimal(12,2) NOT NULL,
  `thanh_tien` decimal(12,2) NOT NULL,
  `ngay_hoa_don` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `trang_thai` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nguoi_tao` int DEFAULT NULL,
  `ngay_tao` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_hoa_don`),
  KEY `ma_benh_nhan` (`ma_benh_nhan`),
  KEY `ma_lich_hen` (`ma_lich_hen`),
  KEY `nguoi_tao` (`nguoi_tao`),
  CONSTRAINT `hoa_don_ibfk_1` FOREIGN KEY (`ma_benh_nhan`) REFERENCES `benh_nhan` (`ma_benh_nhan`),
  CONSTRAINT `hoa_don_ibfk_2` FOREIGN KEY (`ma_lich_hen`) REFERENCES `lich_hen` (`ma_lich_hen`),
  CONSTRAINT `hoa_don_ibfk_3` FOREIGN KEY (`nguoi_tao`) REFERENCES `nguoi_dung` (`ma_nguoi_dung`)
) ENGINE=InnoDB AUTO_INCREMENT=131 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hoa_don`
--

LOCK TABLES `hoa_don` WRITE;
/*!40000 ALTER TABLE `hoa_don` DISABLE KEYS */;
INSERT INTO `hoa_don` VALUES (43,2,NULL,900000.00,900000.00,'2025-06-06 02:25:35','DA_THANH_TOAN',NULL,'2025-06-06 02:25:35'),(44,2,NULL,900000.00,900000.00,'2025-02-06 02:33:03','DA_THANH_TOAN',NULL,'2025-06-06 02:33:03'),(59,1,13,400000.00,400000.00,'2025-02-11 05:19:18','DA_THANH_TOAN',NULL,'2025-06-11 05:19:18'),(60,2,15,400000.00,400000.00,'2025-03-11 05:24:08','CHUA_THANH_TOAN',NULL,'2025-06-11 05:24:08'),(61,2,19,100000.00,100000.00,'2025-02-11 06:48:51','CHUA_THANH_TOAN',NULL,'2025-06-11 06:48:51'),(62,1,25,100000.00,100000.00,'2025-01-11 06:49:10','DA_THANH_TOAN',NULL,'2025-06-11 06:49:10'),(63,1,24,410000.00,410000.00,'2025-02-11 06:52:32','DA_THANH_TOAN',NULL,'2025-06-11 06:52:32'),(64,2,28,643000.00,643000.00,'2025-06-11 21:30:55','CHUA_THANH_TOAN',NULL,'2025-06-11 21:30:55'),(69,2,NULL,900000.00,900000.00,'2025-06-11 22:28:22','DA_THANH_TOAN',NULL,'2025-06-11 22:28:22'),(71,2,27,120000.00,120000.00,'2025-06-11 22:30:43','DA_THANH_TOAN',NULL,'2025-06-11 22:30:43'),(72,1,23,410000.00,410000.00,'2025-06-11 23:45:35','DA_THANH_TOAN',NULL,'2025-06-11 23:45:35'),(74,12,NULL,20000.00,20000.00,'2025-06-19 03:26:07','DA_THANH_TOAN',2,'2025-06-19 03:26:07'),(75,13,NULL,20000.00,20000.00,'2025-06-19 03:26:28','DA_THANH_TOAN',2,'2025-06-19 03:26:28'),(76,14,NULL,20000.00,20000.00,'2025-06-19 03:26:39','DA_THANH_TOAN',2,'2025-06-19 03:26:39'),(77,15,NULL,20000.00,20000.00,'2025-06-19 03:33:47','CHUA_THANH_TOAN',2,'2025-06-19 03:33:47'),(78,16,NULL,20000.00,20000.00,'2025-06-19 03:36:47','DA_THANH_TOAN',2,'2025-06-19 03:36:47'),(79,17,NULL,20000.00,20000.00,'2025-06-19 03:38:28','DA_THANH_TOAN',2,'2025-06-19 03:38:28'),(80,18,NULL,20000.00,20000.00,'2025-06-19 03:39:23','CHUA_THANH_TOAN',2,'2025-06-19 03:39:23'),(81,19,NULL,20000.00,20000.00,'2025-06-19 03:46:30','DA_THANH_TOAN',2,'2025-06-19 03:46:30'),(82,20,NULL,520000.00,520000.00,'2025-06-19 03:52:05','CHUA_THANH_TOAN',2,'2025-06-19 03:52:05'),(83,21,NULL,520000.00,520000.00,'2025-06-19 04:32:02','DA_THANH_TOAN',2,'2025-06-19 04:32:02'),(84,22,NULL,520000.00,520000.00,'2025-06-19 04:33:50','DA_THANH_TOAN',2,'2025-06-19 04:33:50'),(87,23,NULL,520000.00,520000.00,'2025-06-19 09:19:24','DA_THANH_TOAN',2,'2025-06-19 09:19:24'),(88,24,NULL,520000.00,520000.00,'2025-06-19 09:20:07','CHUA_THANH_TOAN',2,'2025-06-19 09:20:07'),(89,25,NULL,520000.00,520000.00,'2025-06-19 09:21:06','DA_THANH_TOAN',2,'2025-06-19 09:21:06'),(90,26,NULL,520000.00,520000.00,'2025-06-19 09:22:00','DA_THANH_TOAN',2,'2025-06-19 09:22:00'),(91,27,NULL,620000.00,620000.00,'2025-06-19 09:22:39','DA_THANH_TOAN',2,'2025-06-19 09:22:39'),(92,29,NULL,620000.00,620000.00,'2025-05-19 09:35:31','CHUA_THANH_TOAN',2,'2025-06-19 09:35:31'),(93,32,NULL,27075000.00,27075000.00,'2025-05-19 09:37:03','DA_THANH_TOAN',2,'2025-06-19 09:37:03'),(94,33,NULL,510000.00,510000.00,'2025-04-19 09:39:25','DA_THANH_TOAN',2,'2025-06-19 09:39:25'),(95,34,NULL,120000.00,120000.00,'2025-03-29 20:49:19','DA_THANH_TOAN',2,'2025-06-29 20:49:19'),(96,34,NULL,1300000.00,1300000.00,'2025-04-29 21:00:56','DA_THANH_TOAN',2,'2025-06-29 21:00:56'),(97,35,NULL,500000.00,500000.00,'2025-03-29 21:32:57','CHUA_THANH_TOAN',2,'2025-06-29 21:32:57'),(98,36,NULL,15000.00,15000.00,'2025-06-29 21:36:09','DA_THANH_TOAN',2,'2025-06-29 21:36:09'),(99,37,NULL,520000.00,520000.00,'2025-07-03 03:02:47','CHUA_THANH_TOAN',2,'2025-07-03 03:02:47'),(100,38,NULL,520000.00,520000.00,'2025-07-04 01:20:27','CHUA_THANH_TOAN',2,'2025-07-04 01:20:27'),(101,38,NULL,520000.00,520000.00,'2025-07-04 01:23:13','CHUA_THANH_TOAN',2,'2025-07-04 01:23:13'),(102,2,NULL,520000.00,520000.00,'2025-07-04 01:24:02','CHUA_THANH_TOAN',2,'2025-07-04 01:24:02'),(103,2,NULL,520000.00,520000.00,'2025-07-04 01:24:31','CHUA_THANH_TOAN',2,'2025-07-04 01:24:31'),(104,2,NULL,520000.00,520000.00,'2025-07-04 01:33:32','CHUA_THANH_TOAN',2,'2025-07-04 01:33:32'),(105,2,NULL,520000.00,520000.00,'2025-07-04 01:41:25','CHUA_THANH_TOAN',2,'2025-07-04 01:41:25'),(106,2,NULL,520000.00,520000.00,'2025-07-04 01:41:57','CHUA_THANH_TOAN',2,'2025-07-04 01:41:57'),(108,2,NULL,520000.00,520000.00,'2025-07-04 02:16:39','CHUA_THANH_TOAN',2,'2025-07-04 02:16:39'),(110,2,NULL,520000.00,520000.00,'2025-07-04 02:20:41','CHUA_THANH_TOAN',2,'2025-07-04 02:20:41'),(112,2,NULL,520000.00,520000.00,'2025-07-04 02:22:02','CHUA_THANH_TOAN',2,'2025-07-04 02:22:02'),(113,39,NULL,300000.00,300000.00,'2025-07-04 03:04:12','CHUA_THANH_TOAN',1,'2025-07-04 03:04:12'),(114,40,NULL,100000.00,100000.00,'2025-07-04 03:05:27','CHUA_THANH_TOAN',1,'2025-07-04 03:05:27'),(115,41,NULL,300000.00,300000.00,'2025-07-04 03:07:55','CHUA_THANH_TOAN',1,'2025-07-04 03:07:55'),(117,38,NULL,520000.00,520000.00,'2025-07-05 23:41:13','CHUA_THANH_TOAN',2,'2025-07-05 23:41:13'),(119,38,NULL,520000.00,520000.00,'2025-07-05 23:43:02','CHUA_THANH_TOAN',2,'2025-07-05 23:43:02'),(122,38,NULL,520000.00,520000.00,'2025-07-06 00:04:47','CHUA_THANH_TOAN',2,'2025-07-06 00:04:47'),(124,38,NULL,520000.00,520000.00,'2025-07-06 00:41:50','CHUA_THANH_TOAN',2,'2025-07-06 00:41:50'),(127,38,NULL,520000.00,520000.00,'2025-07-06 00:57:43','CHUA_THANH_TOAN',2,'2025-07-06 00:57:43'),(128,42,NULL,300000.00,300000.00,'2025-07-06 01:08:57','CHUA_THANH_TOAN',2,'2025-07-06 01:08:57'),(129,43,NULL,300000.00,300000.00,'2025-07-09 01:47:45','CHUA_THANH_TOAN',2,'2025-07-09 01:47:45'),(130,38,NULL,300000.00,300000.00,'2025-07-09 02:07:09','CHUA_THANH_TOAN',2,'2025-07-09 02:07:09');
/*!40000 ALTER TABLE `hoa_don` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kiem_tra_don_thuoc`
--

DROP TABLE IF EXISTS `kiem_tra_don_thuoc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kiem_tra_don_thuoc` (
  `ma_kiem_tra` int NOT NULL AUTO_INCREMENT,
  `ma_don_thuoc` int NOT NULL,
  `thoi_gian_kiem_tra` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `ma_api_ref` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ket_qua` json DEFAULT NULL,
  `trang_thai` enum('Đạt','Cảnh báo','Không đạt') COLLATE utf8mb4_unicode_ci NOT NULL,
  `mo_ta` text COLLATE utf8mb4_unicode_ci,
  `nguoi_kiem_tra` int DEFAULT NULL,
  PRIMARY KEY (`ma_kiem_tra`),
  KEY `ma_ke_thuoc` (`ma_don_thuoc`),
  KEY `nguoi_kiem_tra` (`nguoi_kiem_tra`),
  CONSTRAINT `kiem_tra_don_thuoc_ibfk_1` FOREIGN KEY (`ma_don_thuoc`) REFERENCES `don_thuoc` (`ma_don_thuoc`),
  CONSTRAINT `kiem_tra_don_thuoc_ibfk_2` FOREIGN KEY (`nguoi_kiem_tra`) REFERENCES `nguoi_dung` (`ma_nguoi_dung`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kiem_tra_don_thuoc`
--

LOCK TABLES `kiem_tra_don_thuoc` WRITE;
/*!40000 ALTER TABLE `kiem_tra_don_thuoc` DISABLE KEYS */;
/*!40000 ALTER TABLE `kiem_tra_don_thuoc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lich_hen`
--

DROP TABLE IF EXISTS `lich_hen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lich_hen` (
  `ma_lich_hen` int NOT NULL AUTO_INCREMENT,
  `ma_benh_nhan` int NOT NULL,
  `ma_bac_si` int NOT NULL,
  `ma_dich_vu` int DEFAULT NULL,
  `ngay_hen` date NOT NULL,
  `gio_bat_dau` time NOT NULL,
  `gio_ket_thuc` time NOT NULL,
  `ma_trang_thai` int NOT NULL,
  `ghi_chu` text COLLATE utf8mb4_unicode_ci,
  `ly_do` text COLLATE utf8mb4_unicode_ci,
  `ngay_tao` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_lich_hen`),
  KEY `ma_benh_nhan` (`ma_benh_nhan`),
  KEY `ma_bac_si` (`ma_bac_si`),
  KEY `ma_dich_vu` (`ma_dich_vu`),
  KEY `ma_trang_thai` (`ma_trang_thai`),
  CONSTRAINT `lich_hen_ibfk_1` FOREIGN KEY (`ma_benh_nhan`) REFERENCES `benh_nhan` (`ma_benh_nhan`),
  CONSTRAINT `lich_hen_ibfk_2` FOREIGN KEY (`ma_bac_si`) REFERENCES `bac_si` (`ma_bac_si`),
  CONSTRAINT `lich_hen_ibfk_3` FOREIGN KEY (`ma_dich_vu`) REFERENCES `dich_vu` (`ma_dich_vu`),
  CONSTRAINT `lich_hen_ibfk_4` FOREIGN KEY (`ma_trang_thai`) REFERENCES `trang_thai_lich_hen` (`ma_trang_thai`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lich_hen`
--

LOCK TABLES `lich_hen` WRITE;
/*!40000 ALTER TABLE `lich_hen` DISABLE KEYS */;
INSERT INTO `lich_hen` VALUES (1,1,2,3,'2025-05-29','09:00:00','10:00:00',5,'Đau đầu ','Tự động hủy do quá hạn khám bệnh','2025-05-28 23:19:49'),(2,1,2,3,'2025-06-01','09:00:00','10:00:00',5,'Đau đầu, sốt nhẹ','Tự động hủy do quá hạn khám bệnh','2025-05-28 23:20:15'),(3,1,2,3,'2025-06-02','09:00:00','10:00:00',5,'Đau đầu, sốt nhẹ','Tự động hủy do quá hạn khám bệnh','2025-05-28 23:20:19'),(4,1,2,3,'2025-06-03','09:00:00','10:00:00',5,'Đau đầu, sốt nhẹ\r ','Tự động hủy do quá hạn khám bệnh','2025-05-28 23:20:22'),(7,1,2,3,'2025-06-04','00:00:00','01:30:00',5,'Đau đầu, sốt nhẹ','Tự động hủy do quá hạn khám bệnh','2025-05-29 00:19:44'),(9,5,2,1,'2025-05-31','10:00:00','11:00:00',5,'12321','Tự động hủy do quá hạn khám bệnh','2025-05-29 02:28:02'),(10,5,3,NULL,'2025-06-01','09:00:00','10:00:00',5,'2342','Tự động hủy do quá hạn khám bệnh','2025-05-29 02:28:29'),(11,5,1,1,'2025-06-01','08:00:00','09:00:00',5,'2213','Tự động hủy do quá hạn khám bệnh','2025-05-30 19:16:05'),(12,5,1,1,'2025-06-13','08:00:00','09:00:00',5,'2213','Tự động hủy do quá hạn khám bệnh','2025-05-30 19:17:22'),(13,1,2,3,'2025-06-25','09:00:00','09:30:00',4,'Đau đầu, sốt nhẹ',NULL,'2025-05-30 19:39:53'),(14,5,1,2,'2025-06-01','10:00:00','11:00:00',5,'21321','Tự động hủy do quá hạn khám bệnh','2025-05-30 19:46:41'),(15,2,2,3,'2025-06-15','09:00:00','09:30:00',4,'Đau đầu, sốt nhẹ',NULL,'2025-05-30 19:58:48'),(16,2,2,1,'2025-06-01','14:00:00','15:00:00',5,'234','Tự động hủy do quá hạn khám bệnh','2025-05-30 20:13:26'),(17,2,1,2,'2025-06-05','08:00:00','09:00:00',5,'ád','Tự động hủy do quá hạn khám bệnh','2025-05-30 20:31:52'),(18,2,2,3,'2025-06-07','09:00:00','09:30:00',5,'Đau đầu, sốt nhẹ','Tự động hủy do quá hạn khám bệnh','2025-06-01 21:47:18'),(19,2,2,3,'2025-06-15','14:00:00','14:30:00',4,'Tái khám sau trám răng',NULL,'2025-06-02 00:53:19'),(20,1,1,1,'2025-06-21','08:00:00','09:00:00',5,'','Tự động hủy do quá hạn khám bệnh','2025-06-04 03:30:53'),(21,1,1,1,'2025-06-22','08:00:00','09:00:00',5,'','Tự động hủy do quá hạn khám bệnh','2025-06-04 03:31:06'),(22,1,1,1,'2025-06-22','10:30:00','11:00:00',5,'','','2025-06-04 03:50:49'),(23,1,2,3,'2025-07-10','15:00:00','15:30:00',4,'ád',NULL,'2025-06-05 03:49:20'),(24,1,2,3,'2025-07-10','16:00:00','16:30:00',5,'ád','','2025-06-05 03:49:26'),(25,1,2,3,'2025-07-10','16:30:00','17:00:00',5,'ád','Bệnh nhân bận đột xuất, xin hủy lịch','2025-06-05 03:49:29'),(26,1,3,3,'2025-07-10','15:30:00','16:00:00',1,'ád','','2025-06-05 03:49:34'),(27,2,2,NULL,'2025-06-13','09:00:00','09:30:00',4,'',NULL,'2025-06-11 05:36:42'),(28,2,2,3,'2025-06-13','10:30:00','11:00:00',4,'',NULL,'2025-06-11 05:36:52'),(29,1,1,2,'2025-06-20','13:00:00','13:30:00',5,'','Tự động hủy do quá hạn khám bệnh','2025-06-11 22:35:10'),(30,2,3,3,'2025-06-12','09:00:00','09:30:00',5,NULL,'Tự động hủy do quá hạn khám bệnh','2025-06-17 03:16:44'),(31,1,2,2,'2025-06-26','10:30:00','11:00:00',5,'','Tự động hủy do quá hạn khám bệnh','2025-06-24 04:38:26'),(32,2,2,4,'2025-08-02','10:00:00','10:30:00',1,'',NULL,'2025-07-01 23:34:20'),(33,38,2,NULL,'2025-07-05','09:00:00','09:30:00',5,NULL,'Tự động hủy do quá hạn khám bệnh','2025-07-04 01:23:13'),(34,38,2,NULL,'2025-07-05','09:00:00','09:30:00',5,NULL,'Tự động hủy do quá hạn khám bệnh','2025-07-04 01:24:02'),(35,38,2,NULL,'2025-07-05','09:00:00','09:30:00',5,NULL,'Tự động hủy do quá hạn khám bệnh','2025-07-04 01:24:31'),(36,38,2,NULL,'2025-07-05','09:00:00','09:30:00',5,NULL,'Tự động hủy do quá hạn khám bệnh','2025-07-04 01:34:09'),(37,38,2,NULL,'2025-07-05','09:00:00','09:30:00',5,NULL,'Tự động hủy do quá hạn khám bệnh','2025-07-04 01:41:25'),(38,2,2,NULL,'2025-07-05','09:00:00','09:30:00',5,NULL,'Tự động hủy do quá hạn khám bệnh','2025-07-04 01:42:07'),(39,2,2,NULL,'2025-07-05','10:00:00','10:30:00',5,NULL,'Tự động hủy do quá hạn khám bệnh','2025-07-04 02:16:39'),(40,2,2,NULL,'2025-07-05','11:00:00','12:30:00',5,NULL,'Tự động hủy do quá hạn khám bệnh','2025-07-04 02:20:42'),(41,2,2,NULL,'2025-07-05','13:00:00','14:30:00',5,NULL,'Tự động hủy do quá hạn khám bệnh','2025-07-04 02:22:02'),(42,39,1,NULL,'2025-07-17','08:30:00','09:00:00',1,NULL,'Tái khám: ádsad','2025-07-04 03:04:12'),(43,40,1,NULL,'2025-07-24','08:30:00','09:00:00',1,NULL,'Tái khám: tểtrter','2025-07-04 03:05:27'),(44,41,1,NULL,'2025-07-18','09:00:00','09:30:00',1,NULL,'Tái khám: ádasdas','2025-07-04 03:07:55'),(45,38,2,NULL,'2025-07-15','13:00:00','14:30:00',1,NULL,'Tái khám: ádas','2025-07-05 23:41:13'),(46,38,2,NULL,'2025-07-12','13:00:00','14:30:00',1,NULL,'Tái khám: ádas','2025-07-05 23:43:02'),(47,38,2,NULL,'2025-07-13','13:00:00','14:30:00',1,NULL,'Tái khám: ádas','2025-07-06 00:04:47'),(48,38,2,NULL,'2025-07-14','13:00:00','14:30:00',1,NULL,'Tái khám: ádas','2025-07-06 00:41:50'),(49,38,2,NULL,'2025-07-16','13:00:00','14:30:00',1,NULL,'Tái khám: ádas','2025-07-06 00:57:43');
/*!40000 ALTER TABLE `lich_hen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lich_su_api`
--

DROP TABLE IF EXISTS `lich_su_api`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lich_su_api` (
  `ma_lich_su` int NOT NULL AUTO_INCREMENT,
  `loai_api` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `thoi_gian_goi` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `request` text COLLATE utf8mb4_unicode_ci,
  `response` text COLLATE utf8mb4_unicode_ci,
  `ma_doi_tuong` int DEFAULT NULL,
  `loai_doi_tuong` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `trang_thai` int DEFAULT NULL,
  `thoi_gian_xu_ly` int DEFAULT NULL,
  `nguoi_dung` int DEFAULT NULL,
  PRIMARY KEY (`ma_lich_su`),
  KEY `nguoi_dung` (`nguoi_dung`),
  CONSTRAINT `lich_su_api_ibfk_1` FOREIGN KEY (`nguoi_dung`) REFERENCES `nguoi_dung` (`ma_nguoi_dung`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lich_su_api`
--

LOCK TABLES `lich_su_api` WRITE;
/*!40000 ALTER TABLE `lich_su_api` DISABLE KEYS */;
/*!40000 ALTER TABLE `lich_su_api` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `loai_thuoc`
--

DROP TABLE IF EXISTS `loai_thuoc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `loai_thuoc` (
  `ma_loai_thuoc` int NOT NULL AUTO_INCREMENT,
  `ten_loai_thuoc` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `mo_ta` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`ma_loai_thuoc`),
  UNIQUE KEY `ten_loai_thuoc` (`ten_loai_thuoc`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `loai_thuoc`
--

LOCK TABLES `loai_thuoc` WRITE;
/*!40000 ALTER TABLE `loai_thuoc` DISABLE KEYS */;
INSERT INTO `loai_thuoc` VALUES (1,'Kháng sinh','Các loại thuốc kháng sinh dùng trong nha khoa'),(2,'Giảm đau','Các loại thuốc giảm đau dùng trong nha khoa'),(3,'Kháng viêm','Các loại thuốc kháng viêm dùng trong nha khoa'),(4,'Gây tê','Các loại thuốc gây tê/gây mê dùng trong nha khoa'),(5,'Bổ sung','Các loại thuốc bổ sung vitamin, khoáng chất');
/*!40000 ALTER TABLE `loai_thuoc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nguoi_dung`
--

DROP TABLE IF EXISTS `nguoi_dung`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nguoi_dung` (
  `ma_nguoi_dung` int NOT NULL AUTO_INCREMENT,
  `ma_vai_tro` int NOT NULL,
  `ten_dang_nhap` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `mat_khau` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ho_ten` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `so_dien_thoai` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `trang_thai_hoat_dong` tinyint(1) DEFAULT '1',
  `ngay_tao` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `image_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ma_nguoi_dung`),
  UNIQUE KEY `ten_dang_nhap` (`ten_dang_nhap`),
  UNIQUE KEY `email` (`email`),
  KEY `ma_vai_tro` (`ma_vai_tro`),
  CONSTRAINT `nguoi_dung_ibfk_1` FOREIGN KEY (`ma_vai_tro`) REFERENCES `vai_tro` (`ma_vai_tro`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nguoi_dung`
--

LOCK TABLES `nguoi_dung` WRITE;
/*!40000 ALTER TABLE `nguoi_dung` DISABLE KEYS */;
INSERT INTO `nguoi_dung` VALUES (1,5,'admin01','$2a$10$rzdK72kT7/cM6G0z/Tz1V.lXUFPjqqvl1Dj3r.ym/L6XCySWuzdhi','admin01@example.com','Nguyễn Văn A','0901234567',1,'2025-05-28 07:35:37',''),(2,6,'bacsi01','$2a$10$wxOgEc/xV5/M6AMZMVX7muVmWfNKBoudAnoINu4OGaGBpVHvQgI2O','bacsi01@example.com','Trần Thị B','0912345678',1,'2025-05-28 07:35:37','team-1.jpg'),(3,6,'bacsi02','$2a$10$qWl5GfRnA37UUaRqwDl96e00FQ7lKJGKZtf6WIbApcP3L.I1ek38C','bacsi02@example.com','Lê Văn C','0923456789',1,'2025-05-28 07:35:37','team-2.jpg'),(4,7,'user01','$2a$10$9vTL0DZbcLrHV7K63bekOOyo6OY/Zp.ZRTl3rT3Hg62WCdaNrBT.i','user01@example.com','Phạm Thị D','0934567890',1,'2025-05-28 07:35:37',''),(5,7,'user02','$2a$10$35traqZ1wK2WvthHBAcpFOt9stY9LR0K6OmNikpUl20QfgIeyG8li','user03@example.com','Phạm Thị Dieu','0934567890',1,'2025-05-28 07:35:37',''),(6,6,'nguyenvana235','$2a$10$//uHJEgfCMWjCph5Rs01wud2bIHvLtYyo.9..fkHv/w.CbWC.yKk2','vana12354@example.com','Nguyễn Văn A','0987654321',1,'2025-05-28 02:16:19',''),(7,5,'nguyenvana2335','$2a$10$0KLyGrmucEBaY3.iZLGwBu9JjflEY9cwU4P3/Gv6xnfCIAlKW5sVa','vana123542@example.com','Nguyễn Văn A','0987654321',1,'2025-05-28 19:04:28',''),(8,7,'user07','$2a$10$OipsvMPgcCIHKd6k2dZkcO1A2qbzfPFdx87VrsTJEk21pXA5fITse','user07@example.com','Nguyen van si','0987654321',1,'2025-05-29 00:04:28',''),(9,7,'0123456789','$2a$10$ZVpw5O7iNNqbURdCGKdsiO0Lj9JNnXrDR1zN5HMfzv3LBMP4bYH1e','0123456789@temp.com','Nguyễn Thành Đạt','0123456789',1,'2025-06-29 20:49:19',''),(10,7,'0962239242','$2a$10$hBDGFeqwZNrMuBANkhfdYePCobXUPJQntf7/BKKgXse1jGKNHJbQW','0962239242@temp.com','Nguyễn Thành Công','0962239242',1,'2025-06-29 21:32:53',''),(14,7,'0937877312','$2a$10$axqXN/nayOkFzlU865cX5OCLwNyGiBZrOzeSXclmDEr.KjeKWnGcG','0937877312@temp.com','Nguyễn Hoàng Bảo','0937877312',1,'2025-06-29 21:36:04',''),(15,7,'093456789','$2a$10$CBp//9p5fwWU/uTwSzeWWOk7VCHAXlbaHvdTCsotbWWdOWVoO5zh.','asdasdsa@gmai.com','Phạm Thị Dieu','093456789',1,'2025-07-03 03:02:42',''),(17,7,'093456779','$2a$10$sph/u5aGUrLTKyWXDRl7ueqefCGqf3QTPGkyDF82yPwvFmmFrEE16','asasasdsa@gmai.com','Phạm Thị Dieu','093456779',1,'2025-07-04 01:20:22',''),(18,7,'0988888867','$2a$10$yghogPWLURD0Cx0RiEgDUO21acQsGbuWHTNVw1hFCVtbdBOCFwZQi','0988888867@temp.com','ádsad','0988888867',1,'2025-07-04 03:04:07',''),(19,7,'0955565655','$2a$10$qBm3ArGpv4kNXwO5KOwgM.BTUcTd2LQ8GfQLAWLP67jzpPgBGX9pO','0955565655@temp.com','áddsfgfdgdftygergvxc','0955565655',0,'2025-07-04 03:05:23',''),(21,7,'09777777777','$2a$10$ZD5/x5vFHj1O5.4zlMVu5.ViSRtxdM/nhTmlv/.5FXZxQiiTraKG6','09777777777@temp.com','ádsadas','09777777777',1,'2025-07-04 03:07:51',''),(22,7,'12321321321','$2a$10$Ux3AIhYiawuLU8WXN/Wrpe/.j.Oz731HSwqWzhoJvuekqIGy2.RVC','tringuyen@gmail.com','Nguyễn thị ví dụ','12321321321',1,'2025-07-06 01:08:53',NULL),(23,7,'dsasadsadsadsa','$2a$10$oYyybX5CBnCH7wDCKgijP.Z5kwibZmtDEEN2VHQHUYo5WGQOqc3iW','asdsadsadsa@gmas.cs','ádas','dsasadsadsadsa',1,'2025-07-09 01:47:40',NULL),(24,6,'PhamThiLinh','$2a$10$dAqT7Oye5wm.hqlor6jCoOUNpQ1PsMGGIowAiAhk7u71zIiNHUa8e','PhamThiLinh@gmail.com','Phạm Thị Linh','0988888887',1,'2025-07-09 22:42:39',NULL);
/*!40000 ALTER TABLE `nguoi_dung` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nha_cung_cap`
--

DROP TABLE IF EXISTS `nha_cung_cap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nha_cung_cap` (
  `ma_nha_cung_cap` int NOT NULL AUTO_INCREMENT,
  `ten_nha_cung_cap` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `dia_chi` text COLLATE utf8mb4_unicode_ci,
  `so_dien_thoai` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nguoi_lien_he` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ghi_chu` text COLLATE utf8mb4_unicode_ci,
  `trang_thai_hoat_dong` tinyint(1) DEFAULT '1',
  `ngay_tao` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_nha_cung_cap`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nha_cung_cap`
--

LOCK TABLES `nha_cung_cap` WRITE;
/*!40000 ALTER TABLE `nha_cung_cap` DISABLE KEYS */;
/*!40000 ALTER TABLE `nha_cung_cap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nhap_thuoc`
--

DROP TABLE IF EXISTS `nhap_thuoc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nhap_thuoc` (
  `ma_nhap_thuoc` int NOT NULL AUTO_INCREMENT,
  `ma_nha_cung_cap` int NOT NULL,
  `ma_nguoi_dung` int NOT NULL,
  `ngay_nhap` date NOT NULL,
  `tong_tien` decimal(12,2) NOT NULL,
  `ghi_chu` text COLLATE utf8mb4_unicode_ci,
  `ngay_tao` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_nhap_thuoc`),
  KEY `ma_nha_cung_cap` (`ma_nha_cung_cap`),
  KEY `ma_nguoi_dung` (`ma_nguoi_dung`),
  CONSTRAINT `nhap_thuoc_ibfk_1` FOREIGN KEY (`ma_nha_cung_cap`) REFERENCES `nha_cung_cap` (`ma_nha_cung_cap`),
  CONSTRAINT `nhap_thuoc_ibfk_2` FOREIGN KEY (`ma_nguoi_dung`) REFERENCES `nguoi_dung` (`ma_nguoi_dung`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nhap_thuoc`
--

LOCK TABLES `nhap_thuoc` WRITE;
/*!40000 ALTER TABLE `nhap_thuoc` DISABLE KEYS */;
/*!40000 ALTER TABLE `nhap_thuoc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phuong_thuc_thanh_toan`
--

DROP TABLE IF EXISTS `phuong_thuc_thanh_toan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `phuong_thuc_thanh_toan` (
  `ma_phuong_thuc` int NOT NULL AUTO_INCREMENT,
  `ten_phuong_thuc` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `mo_ta` text COLLATE utf8mb4_unicode_ci,
  `trang_thai_hoat_dong` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`ma_phuong_thuc`),
  UNIQUE KEY `ten_phuong_thuc` (`ten_phuong_thuc`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phuong_thuc_thanh_toan`
--

LOCK TABLES `phuong_thuc_thanh_toan` WRITE;
/*!40000 ALTER TABLE `phuong_thuc_thanh_toan` DISABLE KEYS */;
INSERT INTO `phuong_thuc_thanh_toan` VALUES (1,'Tiền mặt','Thanh toán bằng tiền mặt tại quầy',1),(2,'Thẻ tín dụng/ghi nợ','Thanh toán bằng thẻ tín dụng hoặc thẻ ghi nợ',1),(3,'Chuyển khoản ngân hàng','Thanh toán bằng chuyển khoản ngân hàng',1),(4,'VNPay','Thanh toán trực tuyến qua VNPay',1);
/*!40000 ALTER TABLE `phuong_thuc_thanh_toan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `so_do_rang`
--

DROP TABLE IF EXISTS `so_do_rang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `so_do_rang` (
  `ma_so_do` int NOT NULL AUTO_INCREMENT,
  `ma_benh_nhan` int NOT NULL,
  `ngay_tao` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_so_do`),
  KEY `ma_benh_nhan` (`ma_benh_nhan`),
  CONSTRAINT `so_do_rang_ibfk_1` FOREIGN KEY (`ma_benh_nhan`) REFERENCES `benh_nhan` (`ma_benh_nhan`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `so_do_rang`
--

LOCK TABLES `so_do_rang` WRITE;
/*!40000 ALTER TABLE `so_do_rang` DISABLE KEYS */;
/*!40000 ALTER TABLE `so_do_rang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `thanh_toan`
--

DROP TABLE IF EXISTS `thanh_toan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `thanh_toan` (
  `ma_thanh_toan` int NOT NULL AUTO_INCREMENT,
  `ma_hoa_don` int NOT NULL,
  `ma_phuong_thuc` int NOT NULL,
  `so_tien` decimal(12,2) NOT NULL,
  `ngay_thanh_toan` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `trang_thai_thanh_toan` enum('Thành công','Đang xử lý','Thất bại','Chưa thanh toán') COLLATE utf8mb4_unicode_ci DEFAULT 'Chưa thanh toán',
  `nguoi_tao` int DEFAULT NULL,
  `ngay_tao` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_thanh_toan`),
  KEY `ma_hoa_don` (`ma_hoa_don`),
  KEY `ma_phuong_thuc` (`ma_phuong_thuc`),
  KEY `nguoi_tao` (`nguoi_tao`),
  CONSTRAINT `thanh_toan_ibfk_1` FOREIGN KEY (`ma_hoa_don`) REFERENCES `hoa_don` (`ma_hoa_don`),
  CONSTRAINT `thanh_toan_ibfk_2` FOREIGN KEY (`ma_phuong_thuc`) REFERENCES `phuong_thuc_thanh_toan` (`ma_phuong_thuc`),
  CONSTRAINT `thanh_toan_ibfk_3` FOREIGN KEY (`nguoi_tao`) REFERENCES `nguoi_dung` (`ma_nguoi_dung`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `thanh_toan`
--

LOCK TABLES `thanh_toan` WRITE;
/*!40000 ALTER TABLE `thanh_toan` DISABLE KEYS */;
INSERT INTO `thanh_toan` VALUES (16,71,4,120000.00,'2025-06-27 02:38:09','Thành công',5,'2025-06-27 02:37:56'),(17,61,4,100000.00,'2025-06-27 02:52:16','Thành công',5,'2025-06-27 02:51:58'),(18,64,4,643000.00,'2025-06-27 02:57:01','Thành công',5,'2025-06-27 02:56:43'),(19,61,4,100000.00,'2025-07-01 23:38:04','Đang xử lý',5,'2025-07-01 23:38:04'),(20,60,4,400000.00,'2025-07-01 23:38:28','Đang xử lý',5,'2025-07-01 23:38:28');
/*!40000 ALTER TABLE `thanh_toan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `thuoc`
--

DROP TABLE IF EXISTS `thuoc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `thuoc` (
  `ma_thuoc` int NOT NULL AUTO_INCREMENT,
  `ma_loai_thuoc` int DEFAULT NULL,
  `ten_thuoc` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ma_atc` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ma_ndc` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ma_rxnorm` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ma_snomed` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ma_product` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ma_gtin` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `hoat_chat` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ham_luong` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nha_san_xuat` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nuoc_san_xuat` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dang_bao_che` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `don_vi_tinh` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `duong_dung` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `huong_dan_su_dung` text COLLATE utf8mb4_unicode_ci,
  `cach_bao_quan` text COLLATE utf8mb4_unicode_ci,
  `phan_loai_don_thuoc` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `chong_chi_dinh` text COLLATE utf8mb4_unicode_ci,
  `tac_dung_phu` text COLLATE utf8mb4_unicode_ci,
  `tuong_tac_thuoc` text COLLATE utf8mb4_unicode_ci,
  `nhom_thuoc_thai_ky` varchar(5) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gia` decimal(12,2) NOT NULL,
  `so_luong_ton` int NOT NULL DEFAULT '0',
  `nguong_canh_bao` int DEFAULT '10',
  `trang_thai_hoat_dong` tinyint(1) DEFAULT '1',
  `ngay_tao` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `ngay_cap_nhat` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_thuoc`),
  KEY `ma_loai_thuoc` (`ma_loai_thuoc`),
  KEY `idx_ma_atc` (`ma_atc`),
  KEY `idx_ma_rxnorm` (`ma_rxnorm`),
  KEY `idx_ma_ndc` (`ma_ndc`),
  KEY `idx_ma_snomed` (`ma_snomed`),
  CONSTRAINT `thuoc_ibfk_1` FOREIGN KEY (`ma_loai_thuoc`) REFERENCES `loai_thuoc` (`ma_loai_thuoc`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=126 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `thuoc`
--

LOCK TABLES `thuoc` WRITE;
/*!40000 ALTER TABLE `thuoc` DISABLE KEYS */;
INSERT INTO `thuoc` VALUES (101,NULL,'Paracetamol','N02BE01',NULL,NULL,NULL,NULL,NULL,'Paracetamol','500','Hoffmann-La Roche','Thụy Sĩ','Viên nén','mg','Uống','Giảm đau, hạ sốt','Nơi khô mát dưới 30°C','KE_DON','Suy gan, quá mẫn','Tăng men gan, dị ứng','Warfarin','B',10000.00,100,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(102,NULL,'Ibuprofen','M01AE01',NULL,NULL,NULL,NULL,NULL,'Ibuprofen','200','Abbott Laboratories','Mỹ','Viên nén','mg','Uống','Giảm đau, chống viêm','Nơi khô mát dưới 30°C','KE_DON','Loét tiêu hóa, suy thận nặng','Loét dạ dày, suy thận','Thuốc chống đông, ACEI','D',12000.00,50,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(103,NULL,'Aspirin','N02BA01',NULL,NULL,NULL,NULL,NULL,'Axít acetylsalicylic','325','Bayer AG','Đức','Viên nén','mg','Uống','Giảm đau, hạ sốt, kháng tiểu cầu','Nơi khô mát dưới 25°C','KE_DON','Loét dạ dày, thiếu máu, trẻ em','Xuất huyết, loét','Kháng đông','D',8000.00,80,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(104,NULL,'Amoxicillin','J01CA04',NULL,NULL,NULL,NULL,NULL,'Amoxicillin','500','GlaxoSmithKline','Anh','Viên nang','mg','Uống','Kháng sinh phổ rộng','Nơi khô mát dưới 25°C','KE_DON','Dị ứng penicillin','Tiêu chảy, dị ứng','Methotrexate','B',15000.00,30,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(105,NULL,'Azithromycin','J01FA10',NULL,NULL,NULL,NULL,NULL,'Azithromycin','250','Pfizer','Mỹ','Viên nén','mg','Uống','Kháng sinh macrolide','Nơi khô mát dưới 25°C','KE_DON','Dị ứng macrolide, bệnh tim','Tiêu chảy, buồn nôn','Warfarin','B',30000.00,20,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(106,NULL,'Ciprofloxacin','J01MA02',NULL,NULL,NULL,NULL,NULL,'Ciprofloxacin','500','Bayer','Đức','Viên nén','mg','Uống','Kháng sinh fluoroquinolone','Nơi khô mát dưới 30°C','KE_DON','Trẻ em, thai kỳ','Buồn nôn, đau đầu','Antacid, Theophylline','C',20000.00,25,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(107,NULL,'Metformin','A10BA02',NULL,NULL,NULL,NULL,NULL,'Metformin','500','Merck','Mỹ','Viên nén','mg','Uống','Hạ đường máu nhóm Biguanide','Nơi khô mát dưới 25°C','KE_DON','Suy thận nặng, suy tim','Tiêu chảy, buồn nôn','Cản quang','B',2000.00,50,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(108,NULL,'Omeprazole','A02BC01',NULL,NULL,NULL,NULL,NULL,'Omeprazole','20','AstraZeneca','Anh','Viên bao tan','mg','Uống','Giảm tiết acid, điều trị loét','Nơi khô mát dưới 25°C','KE_DON','Dị ứng thuốc nhóm PPI','Đau bụng, nhức đầu','Clopidogrel','B',10000.00,40,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(109,NULL,'Salbutamol','R03AC02',NULL,NULL,NULL,NULL,NULL,'Salbutamol','100','GlaxoSmithKline','Anh','Bình xịt','µg/phút','Hít','Giãn phế quản điều trị hen','Nơi khô mát dưới 30°C','KE_DON','Tim mạch không ổn định','Run tay, đánh trống ngực','Thuốc chẹn β','B',50000.00,15,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(110,NULL,'Insulin (trung tính)','A10AC01',NULL,NULL,NULL,NULL,NULL,'Insulin','100','Novo Nordisk','Đan Mạch','Dung dịch tiêm','IU/ml','Tiêm dưới da','Hạ đường huyết điều trị tiểu đường','Bảo quản 2–8°C','KE_DON','Hạ đường huyết cấp','Hạ đường huyết, phản ứng tại chỗ','Salicylat, rượu','B',200000.00,10,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(111,NULL,'Prednisolone','H02AB06',NULL,NULL,NULL,NULL,NULL,'Prednisolone','5','Pfizer','Ireland','Viên nén','mg','Uống','Glucocorticoid giảm viêm','Nơi khô mát dưới 25°C','KE_DON','Nhiễm trùng toàn thân không kiểm soát','Tăng đường huyết, xương giòn','NSAID','C',5000.00,40,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(112,NULL,'Atorvastatin','C10AA05',NULL,NULL,NULL,NULL,NULL,'Atorvastatin','20','Pfizer','Mỹ','Viên nén','mg','Uống','Statin hạ mỡ máu','Nơi khô mát dưới 25°C','KE_DON','Bệnh gan nặng, thai kỳ','Đau cơ, tăng men gan','CYP3A4 inhibitors','X',20000.00,30,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(113,NULL,'Simvastatin','C10AA01',NULL,NULL,NULL,NULL,NULL,'Simvastatin','10','Merck','Mỹ','Viên nén','mg','Uống','Statin hạ mỡ máu','Nơi khô mát dưới 25°C','KE_DON','Bệnh gan nặng, thai kỳ','Đau cơ, tăng men gan','Fibrate, Amiodarone','X',15000.00,30,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(114,NULL,'Furosemide','C03CA01',NULL,NULL,NULL,NULL,NULL,'Furosemide','40','Roche','Thụy Sĩ','Viên nén','mg','Uống','Lợi tiểu quai','Nơi khô mát dưới 30°C','KE_DON','Vô niệu','Hạ kali, hạ áp','NSAID','C',5000.00,40,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(115,NULL,'Metronidazol','J01XD01',NULL,NULL,NULL,NULL,NULL,'Metronidazol','400','Bayer','Đức','Viên nén','mg','Uống','Kháng khuẩn anaerobe, thuốc ký sinh','Nơi khô mát dưới 25°C','KE_DON','Rượu (tương tác nặng), thiếu máu tán huyết','Vị kim loại, đau đầu','Rượu, Warfarin','B',7000.00,30,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(116,NULL,'Doxycycline','J01AA02',NULL,NULL,NULL,NULL,NULL,'Doxycycline','100','Pfizer','Mỹ','Viên nang','mg','Uống','Tetracycline phổ rộng','Nơi khô mát dưới 30°C','KE_DON','Thai kỳ, trẻ nhỏ','Nhạy cảm ánh sáng, tiêu chảy','Retinoid, Ca/Sắt','D',7000.00,25,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(117,NULL,'Gentamicin','J01GB03',NULL,NULL,NULL,NULL,NULL,'Gentamicin','10','Sandoz','Hà Lan','Dung dịch tiêm','mg/ml','Tiêm bắp/tĩnh mạch','Kháng sinh aminoglycoside','Bảo quản 2–8°C','KE_DON','Suy thận nặng, nhược cơ','Độc thận, độc tai','Thuốc độc thận khác','D',25000.00,20,10,1,'2025-06-06 11:38:25','2025-06-06 11:39:42'),(118,NULL,'Loratadin','R06AX13',NULL,NULL,NULL,NULL,NULL,'Loratadin','10','Schering-Plough','Mỹ','Viên nén','mg','Uống','Kháng histamin H1 thế hệ 2','Nơi khô mát dưới 30°C','KE_DON','Dị ứng loratadine','Khô miệng, mệt mỏi','Rượu','B',20000.00,40,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(119,NULL,'Chloramphenicol','J01BA01',NULL,NULL,NULL,NULL,NULL,'Chloramphenicol','250','Roche','Thụy Sĩ','Viên nang','mg','Uống','Kháng sinh phổ rộng','Nơi khô mát dưới 25°C','KE_DON','Thiếu máu nặng, mang thai','Ức chế tủy xương, ban phát ban','Thuốc ức chế tủy khác','C',8000.00,15,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(120,NULL,'Atenolol','C07AB03',NULL,NULL,NULL,NULL,NULL,'Atenolol','50','AstraZeneca','Anh','Viên nén','mg','Uống','Chẹn β1 điều trị tăng HA','Nơi khô mát dưới 30°C','KE_DON','Block tim độ cao, hen nặng','Mệt mỏi, nhịp tim chậm','Thuốc HA khác','C',10000.00,30,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(121,NULL,'Amoxicillin','J01CA04',NULL,NULL,NULL,NULL,NULL,'Amoxicillin','500','GSK','Anh','Viên nang','mg','Uống','500 mg x 3 lần/ngày','Dưới 25°C','KE_DON','Dị ứng penicillin','Tiêu chảy, dị ứng','Methotrexate','B',12000.00,120,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(122,NULL,'Metformin','A10BA02',NULL,NULL,NULL,NULL,NULL,'Metformin','500','Merck','Mỹ','Viên nén','mg','Uống','500 mg x 2 lần/ngày','Dưới 25°C','KE_DON','Suy thận','Buồn nôn, tiêu chảy','Cản quang, rượu','B',15000.00,90,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(123,NULL,'Salbutamol','R03AC02',NULL,NULL,NULL,NULL,NULL,'Salbutamol','100','GSK','Anh','Bình xịt định liều','µg','Hít','1–2 hít khi cần','Dưới 30°C','KE_DON','Bệnh tim nặng','Run, hồi hộp','Chẹn beta','B',30000.00,50,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(124,NULL,'Prednisolone','H02AB06',NULL,NULL,NULL,NULL,NULL,'Prednisolone','5','Pfizer','Ireland','Viên nén','mg','Uống','5–10 mg/ngày','Dưới 25°C','KE_DON','Nhiễm trùng nặng','Tăng đường huyết, loãng xương','NSAID, vaccine sống','C',8000.00,70,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(125,NULL,'Azithromycin','J01FA10',NULL,NULL,NULL,NULL,NULL,'Azithromycin','250','Pfizer','Mỹ','Viên nén','mg','Uống','500 mg ngày 1, sau đó 250 mg/ngày','Dưới 25°C','KE_DON','Dị ứng, kéo dài QT','Tiêu chảy, buồn nôn','Warfarin','B',20000.00,85,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07');
/*!40000 ALTER TABLE `thuoc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trang_thai_lich_hen`
--

DROP TABLE IF EXISTS `trang_thai_lich_hen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trang_thai_lich_hen` (
  `ma_trang_thai` int NOT NULL AUTO_INCREMENT,
  `ten_trang_thai` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `mo_ta` text COLLATE utf8mb4_unicode_ci,
  `ma_mau` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ma_trang_thai`),
  UNIQUE KEY `ten_trang_thai` (`ten_trang_thai`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trang_thai_lich_hen`
--

LOCK TABLES `trang_thai_lich_hen` WRITE;
/*!40000 ALTER TABLE `trang_thai_lich_hen` DISABLE KEYS */;
INSERT INTO `trang_thai_lich_hen` VALUES (1,'Đã đặt','Lịch hẹn đã được đặt thành công','#3498db'),(2,'Đã xác nhận','Lịch hẹn đã được xác nhận','#2ecc71'),(3,'Đang thực hiện','Bệnh nhân đang được khám','#f39c12'),(4,'Hoàn thành','Lịch hẹn đã hoàn thành','#27ae60'),(5,'Đã hủy','Lịch hẹn đã bị hủy','#e74c3c');
/*!40000 ALTER TABLE `trang_thai_lich_hen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tuong_tac_thuoc`
--

DROP TABLE IF EXISTS `tuong_tac_thuoc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tuong_tac_thuoc` (
  `ma_tuong_tac` int NOT NULL AUTO_INCREMENT,
  `ma_thuoc_1` int NOT NULL,
  `ma_thuoc_2` int NOT NULL,
  `muc_do_nghiem_trong` enum('Nhẹ','Trung bình','Nghiêm trọng','Chống chỉ định') COLLATE utf8mb4_unicode_ci NOT NULL,
  `mo_ta` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `khuyen_nghi` text COLLATE utf8mb4_unicode_ci,
  `ma_ref_ext` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ngay_tao` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `ngay_cap_nhat` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_tuong_tac`),
  UNIQUE KEY `idx_tuong_tac_thuoc` (`ma_thuoc_1`,`ma_thuoc_2`),
  KEY `ma_thuoc_2` (`ma_thuoc_2`),
  CONSTRAINT `tuong_tac_thuoc_ibfk_1` FOREIGN KEY (`ma_thuoc_1`) REFERENCES `thuoc` (`ma_thuoc`),
  CONSTRAINT `tuong_tac_thuoc_ibfk_2` FOREIGN KEY (`ma_thuoc_2`) REFERENCES `thuoc` (`ma_thuoc`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tuong_tac_thuoc`
--

LOCK TABLES `tuong_tac_thuoc` WRITE;
/*!40000 ALTER TABLE `tuong_tac_thuoc` DISABLE KEYS */;
/*!40000 ALTER TABLE `tuong_tac_thuoc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vai_tro`
--

DROP TABLE IF EXISTS `vai_tro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vai_tro` (
  `ma_vai_tro` int NOT NULL AUTO_INCREMENT,
  `ten_vai_tro` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `mo_ta` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`ma_vai_tro`),
  UNIQUE KEY `ten_vai_tro` (`ten_vai_tro`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vai_tro`
--

LOCK TABLES `vai_tro` WRITE;
/*!40000 ALTER TABLE `vai_tro` DISABLE KEYS */;
INSERT INTO `vai_tro` VALUES (1,'Quản trị','Quản trị viên hệ thống'),(2,'Bác sĩ','Bác sĩ nha khoa'),(3,'Lễ tân','Nhân viên lễ tân'),(4,'Bệnh nhân','Bệnh nhân'),(5,'ADMIN','Quản trị hệ thống'),(6,'BACSI','Bác sĩ trong hệ thống'),(7,'USER','Người dùng bình thường');
/*!40000 ALTER TABLE `vai_tro` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-10 22:40:03
