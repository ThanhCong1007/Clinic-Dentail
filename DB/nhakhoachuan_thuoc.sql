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
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-09 10:08:28
