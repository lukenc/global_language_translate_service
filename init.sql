CREATE DATABASE  IF NOT EXISTS `content_global_translate` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `content_global_translate`;
-- MySQL dump 10.13  Distrib 8.0.27, for macos11 (arm64)
--
-- Host: localhost    Database: content_global_translate
-- ------------------------------------------------------
-- Server version	8.0.27

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
-- Table structure for table `language`
--

DROP TABLE IF EXISTS `language`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `language` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '语言的唯一标识符',
  `name` varchar(50) NOT NULL COMMENT '语言名称，运维人员管理使用',
  `iso_code` varchar(10) NOT NULL COMMENT 'ISO语言代码',
  `native_name` varchar(50) NOT NULL COMMENT '语言的本地名称,用于展示给用户找到自己的语言',
  `created_by` varchar(255) NOT NULL COMMENT '创建人',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_by` varchar(255) DEFAULT NULL COMMENT '最后修改人',
  `modified_at` timestamp NULL DEFAULT NULL COMMENT '最后修改时间',
  `enable` bit(1) DEFAULT b'1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=152 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='语言表，用于存储支持的语言信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `language_region`
--

DROP TABLE IF EXISTS `language_region`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `language_region` (
  `language_id` int NOT NULL COMMENT '语言的ID',
  `region_id` int NOT NULL COMMENT '地区的ID',
  `created_by` varchar(255) NOT NULL COMMENT '创建人',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_by` varchar(255) DEFAULT NULL COMMENT '最后修改人',
  `modified_at` timestamp NULL DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`language_id`,`region_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='语言与地区的关联表，用于存储语言与地区的关系信息，全球推广后用于根据ip自动选择';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `region`
--

DROP TABLE IF EXISTS `region`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `region` (
  `id` int NOT NULL COMMENT '地区的唯一标识符',
  `name` varchar(50) NOT NULL COMMENT '地区名称',
  `created_by` varchar(255) NOT NULL COMMENT '创建人',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_by` varchar(255) DEFAULT NULL COMMENT '最后修改人',
  `modified_at` timestamp NULL DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='地区表，用于存储地区信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `translation`
--

DROP TABLE IF EXISTS `translation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `translation` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '翻译条目的唯一标识符',
  `content_id` int NOT NULL COMMENT '对应网页内容表的主键，表示所属的网页内容的ID',
  `language_id` int NOT NULL COMMENT '语言的ID，用于标识属于哪个语言',
  `language_code` varchar(10) NOT NULL COMMENT '目标语言的标识符，如ISO语言代码',
  `translated_text` text NOT NULL COMMENT '翻译后的文本内容',
  `created_by` varchar(255) NOT NULL COMMENT '创建人',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_by` varchar(255) DEFAULT NULL COMMENT '最后修改人',
  `modified_at` timestamp NULL DEFAULT NULL COMMENT '最后修改时间',
  `enable` bit(1) DEFAULT b'1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=284 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='翻译表，用于存储网页内容的翻译信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_preference`
--

DROP TABLE IF EXISTS `user_preference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_preference` (
  `id` int NOT NULL COMMENT '用户偏好条目的唯一标识符',
  `user_id` int NOT NULL COMMENT '用户的ID，用于标识所属用户',
  `language_id` int NOT NULL COMMENT '偏好的语言ID',
  `region_id` int DEFAULT NULL COMMENT '偏好的地区ID',
  `timezone` varchar(50) DEFAULT NULL COMMENT '偏好的时区',
  `created_by` varchar(255) NOT NULL COMMENT '创建人',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_by` varchar(255) DEFAULT NULL COMMENT '最后修改人',
  `modified_at` timestamp NULL DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户偏好表，用于存储用户的偏好设置';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `web_content`
--

DROP TABLE IF EXISTS `web_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `web_content` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '网页内容的唯一标识符',
  `url` varchar(255) NOT NULL COMMENT '网页的URL',
  `original_content` text NOT NULL COMMENT '原始网页内容',
  `created_by` varchar(255) NOT NULL COMMENT '创建人',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_by` varchar(255) DEFAULT NULL COMMENT '最后修改人',
  `modified_at` timestamp NULL DEFAULT NULL COMMENT '最后修改时间',
  `type` varchar(255) DEFAULT NULL COMMENT '例如button，list',
  `enable` bit(1) DEFAULT b'1' COMMENT '是否生效',
  PRIMARY KEY (`id`),
  UNIQUE KEY `url_UNIQUE` (`url`)
) ENGINE=InnoDB AUTO_INCREMENT=575 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='网页内容表，用于存储网页的原始内容和相关信息';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-05-29 17:22:34
