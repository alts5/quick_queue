-- --------------------------------------------------------
-- Хост:                         127.0.0.1
-- Версия сервера:               8.0.39 - MySQL Community Server - GPL
-- Операционная система:         Win64
-- HeidiSQL Версия:              12.8.0.6908
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Дамп структуры базы данных quick_queue
CREATE DATABASE IF NOT EXISTS `quick_queue` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `quick_queue`;

-- Дамп структуры для таблица quick_queue.applicants
CREATE TABLE IF NOT EXISTS `applicants` (
  `applicant_id` smallint NOT NULL AUTO_INCREMENT COMMENT 'ID заявителя',
  `hash` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Уникальный SHA1-хэш заявителя',
  `stat` enum('Активен','Заблокирован') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'Активен' COMMENT 'Статус аккаунта заявителя',
  PRIMARY KEY (`applicant_id`),
  UNIQUE KEY `applicant_id` (`applicant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Таблица-справочник заявителей';

-- Дамп данных таблицы quick_queue.applicants: ~2 rows (приблизительно)
DELETE FROM `applicants`;
INSERT INTO `applicants` (`applicant_id`, `hash`, `stat`) VALUES
	(4, '9932d4133a6ef6fd5cd2ddf8bb9057ddc33c2d81', 'Активен'),
	(5, '8e1f6478d30277f4ed321982969a0d262add0759', 'Активен');

-- Дамп структуры для таблица quick_queue.applicants_documents
CREATE TABLE IF NOT EXISTS `applicants_documents` (
  `applicants_documents_id` smallint NOT NULL AUTO_INCREMENT COMMENT 'ID записи',
  `applicant` smallint NOT NULL COMMENT 'ID заявителя',
  `document_type` smallint NOT NULL COMMENT 'ID вида документа',
  `document_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Номер документа',
  `document_owner` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ФИО владельца документа',
  `stat` enum('Актуален','Заблокировано') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'Актуален' COMMENT 'Статус',
  `create_date` timestamp NOT NULL DEFAULT (now()) COMMENT 'Дата добавления записи',
  `block_date` timestamp NULL DEFAULT NULL COMMENT 'Дата блокировки записи',
  PRIMARY KEY (`applicants_documents_id`),
  UNIQUE KEY `applicants_documents_id` (`applicants_documents_id`),
  KEY `applicants_list` (`applicant`),
  KEY `document_types_list` (`document_type`),
  CONSTRAINT `applicants_list` FOREIGN KEY (`applicant`) REFERENCES `applicants` (`applicant_id`),
  CONSTRAINT `document_types_list` FOREIGN KEY (`document_type`) REFERENCES `documenttypes` (`document_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Таблица-связка, хранящая сведения о документах, удостоверяющих личность заявителей (заполняется в случае, когда требуется неанонимная очередь)';

-- Дамп данных таблицы quick_queue.applicants_documents: ~1 rows (приблизительно)
DELETE FROM `applicants_documents`;
INSERT INTO `applicants_documents` (`applicants_documents_id`, `applicant`, `document_type`, `document_number`, `document_owner`, `stat`, `create_date`, `block_date`) VALUES
	(1, 5, 2, '9090900900', 'Сафин Ракип Асгатович', 'Актуален', '2024-09-24 10:00:03', NULL);

-- Дамп структуры для таблица quick_queue.categories
CREATE TABLE IF NOT EXISTS `categories` (
  `category_id` smallint NOT NULL AUTO_INCREMENT COMMENT 'ID категории',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Наименование категории',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'Отсутствует' COMMENT 'Описание категории',
  `stat` enum('Доступно','Заблокировано') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'Доступно' COMMENT 'Статус категории',
  PRIMARY KEY (`category_id`),
  UNIQUE KEY `category_id` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Таблица-справочник, хранящая сведения о категориях заявителей ';

-- Дамп данных таблицы quick_queue.categories: ~3 rows (приблизительно)
DELETE FROM `categories`;
INSERT INTO `categories` (`category_id`, `name`, `description`, `stat`) VALUES
	(3, 'Гражданин РФ', 'Отсутствует', 'Доступно'),
	(4, 'Студент', 'Отсутствует', 'Доступно'),
	(5, 'Лицо без гражданства РФ', 'Отсутствует', 'Доступно');

-- Дамп структуры для таблица quick_queue.categories_services
CREATE TABLE IF NOT EXISTS `categories_services` (
  `categories_services_id` smallint NOT NULL AUTO_INCREMENT COMMENT 'ID записи',
  `category` smallint NOT NULL COMMENT 'ID категории',
  `service` smallint NOT NULL COMMENT 'ID услуги',
  `stat` enum('Доступно','Временно недоступно','Заблокировано') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'Доступно' COMMENT 'Статус',
  `create_date` timestamp NOT NULL DEFAULT (now()) COMMENT 'Дата добавления записи',
  `block_date` timestamp NULL DEFAULT NULL COMMENT 'Дата блокировки записи',
  PRIMARY KEY (`categories_services_id`),
  UNIQUE KEY `categories_services_id` (`categories_services_id`),
  KEY `FK_categories_services_categories` (`category`),
  KEY `FK_categories_services_services` (`service`),
  CONSTRAINT `FK_categories_services_categories` FOREIGN KEY (`category`) REFERENCES `categories` (`category_id`),
  CONSTRAINT `FK_categories_services_services` FOREIGN KEY (`service`) REFERENCES `services` (`service_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Таблица-связка, прикрепляющая категорию заявителей к доступным им услугам';

-- Дамп данных таблицы quick_queue.categories_services: ~6 rows (приблизительно)
DELETE FROM `categories_services`;
INSERT INTO `categories_services` (`categories_services_id`, `category`, `service`, `stat`, `create_date`, `block_date`) VALUES
	(3, 3, 5, 'Доступно', '2024-09-24 09:51:53', NULL),
	(4, 3, 3, 'Доступно', '2024-09-24 09:52:02', NULL),
	(6, 3, 7, 'Доступно', '2024-09-24 09:54:17', NULL),
	(7, 5, 4, 'Доступно', '2024-09-24 09:55:04', NULL),
	(8, 3, 2, 'Доступно', '2024-09-24 09:55:14', NULL),
	(9, 4, 6, 'Доступно', '2024-09-24 09:55:30', NULL);

-- Дамп структуры для таблица quick_queue.documenttypes
CREATE TABLE IF NOT EXISTS `documenttypes` (
  `document_type_id` smallint NOT NULL AUTO_INCREMENT COMMENT 'ID вида документа',
  `label` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Наименование вида документа',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'Отсутствует' COMMENT 'Описание вида документа',
  `stat` enum('Доступно','Заблокировано') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'Доступно' COMMENT 'Статус вида документа',
  PRIMARY KEY (`document_type_id`),
  UNIQUE KEY `document_type_id` (`document_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Таблица-справочник для документов, удостоверяющих личность заявителя';

-- Дамп данных таблицы quick_queue.documenttypes: ~2 rows (приблизительно)
DELETE FROM `documenttypes`;
INSERT INTO `documenttypes` (`document_type_id`, `label`, `description`, `stat`) VALUES
	(2, 'Паспорт РФ', 'Отсутствует', 'Доступно'),
	(3, 'СНИЛС', 'Отсутствует', 'Доступно');

-- Дамп структуры для таблица quick_queue.main
CREATE TABLE IF NOT EXISTS `main` (
  `applicants_ws_id` smallint NOT NULL AUTO_INCREMENT COMMENT 'ID записи',
  `applicant` smallint NOT NULL COMMENT 'ID заявителя',
  `category_service` smallint NOT NULL COMMENT 'ID доступной услуги по категории',
  `window_staff` smallint DEFAULT NULL COMMENT 'ID окна с сотрудником',
  `stat` enum('Ожидает','Приглашен','Обслужен','Снят','Заблокировано') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'Ожидает',
  `create_date` timestamp NOT NULL DEFAULT (now()),
  `block_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`applicants_ws_id`),
  UNIQUE KEY `applicants_ws_id` (`applicants_ws_id`),
  KEY `FK_main_windows_staffs` (`window_staff`),
  KEY `FK_applicants_ws_applicants` (`applicant`) USING BTREE,
  KEY `FK_main_categories_services` (`category_service`),
  CONSTRAINT `FK_applicants_ws_applicants` FOREIGN KEY (`applicant`) REFERENCES `applicants` (`applicant_id`),
  CONSTRAINT `FK_main_categories_services` FOREIGN KEY (`category_service`) REFERENCES `categories_services` (`categories_services_id`),
  CONSTRAINT `FK_main_windows_staffs` FOREIGN KEY (`window_staff`) REFERENCES `windows_staffs` (`windows_staffs_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Главная таблица, содержащая список заявителей и услуги, которые они хотят получить';

-- Дамп данных таблицы quick_queue.main: ~2 rows (приблизительно)
DELETE FROM `main`;
INSERT INTO `main` (`applicants_ws_id`, `applicant`, `category_service`, `window_staff`, `stat`, `create_date`, `block_date`) VALUES
	(2, 5, 3, NULL, 'Ожидает', '2024-09-24 10:02:06', NULL),
	(3, 4, 4, NULL, 'Ожидает', '2024-09-24 10:02:11', NULL);

-- Дамп структуры для таблица quick_queue.services
CREATE TABLE IF NOT EXISTS `services` (
  `service_id` smallint NOT NULL AUTO_INCREMENT COMMENT 'ID услуги',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Наименование услуги',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'Отсутствует' COMMENT 'Описание услуги',
  `stat` enum('Доступно','Заблокировано') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'Доступно' COMMENT 'Статус услуги',
  PRIMARY KEY (`service_id`),
  UNIQUE KEY `service_id` (`service_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Таблица-справочник предоставляемых услуг';

-- Дамп данных таблицы quick_queue.services: ~6 rows (приблизительно)
DELETE FROM `services`;
INSERT INTO `services` (`service_id`, `name`, `description`, `stat`) VALUES
	(2, 'Смена адреса постоянной регистрации', 'Услуга предоставляется МВД РФ', 'Доступно'),
	(3, 'Получение готовых документов', 'Услуга распространяется на все виды документов', 'Доступно'),
	(4, 'Постановка на миграционный учёт', 'Услуга предоставляется МВД РФ', 'Доступно'),
	(5, 'Оплата штрафов', 'Оплата любых видов штрафов', 'Доступно'),
	(6, 'Подача заявки на выпуск Карты студента РТ', 'Услуга доступна только студентам', 'Доступно'),
	(7, 'Оплата коммунальных платежей', 'Отсутствует', 'Доступно');

-- Дамп структуры для таблица quick_queue.settings
CREATE TABLE IF NOT EXISTS `settings` (
  `setting_id` smallint NOT NULL AUTO_INCREMENT COMMENT 'ID параметра',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Наименование параметра',
  `value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT 'Значение параметра',
  PRIMARY KEY (`setting_id`),
  UNIQUE KEY `setting_id` (`setting_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Таблица для хранения настроек системы';

-- Дамп данных таблицы quick_queue.settings: ~2 rows (приблизительно)
DELETE FROM `settings`;
INSERT INTO `settings` (`setting_id`, `name`, `value`) VALUES
	(1, 'registration_start_time', '08:00'),
	(2, 'registration_stop_time', '16:00');

-- Дамп структуры для таблица quick_queue.staff
CREATE TABLE IF NOT EXISTS `staff` (
  `staff_id` smallint NOT NULL AUTO_INCREMENT COMMENT 'ID сотрудника',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ФИО сотрудника',
  `login` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Логин сотрудника',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'MD5-хэш пароля аккаунта',
  `stat` enum('Активен','Заблокирован') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'Активен' COMMENT 'Статус аккаунта сотрудника',
  PRIMARY KEY (`staff_id`) USING BTREE,
  UNIQUE KEY `user_id` (`staff_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Таблица-справочник сотрудников, работающих с системой';

-- Дамп данных таблицы quick_queue.staff: ~3 rows (приблизительно)
DELETE FROM `staff`;
INSERT INTO `staff` (`staff_id`, `name`, `login`, `password`, `stat`) VALUES
	(1, 'Габидуллин Ринат Анварович', 'gabidullin_ra', '161ebd7d45089b3446ee4e0d86dbcf92', 'Активен'),
	(2, 'Шафигуллина Алсу Мунавировна', 'shafigullina_am', '161ebd7d45089b3446ee4e0d86dbcf92', 'Активен'),
	(3, 'Насырова Венера Марсовна', 'nasyrova_vm', '161ebd7d45089b3446ee4e0d86dbcf92', 'Активен');

-- Дамп структуры для таблица quick_queue.windows
CREATE TABLE IF NOT EXISTS `windows` (
  `window_id` smallint NOT NULL AUTO_INCREMENT COMMENT 'ID окна',
  `label` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Наименование окна',
  `stat` enum('Доступно','Заблокировано') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'Доступно' COMMENT 'Статус окна',
  PRIMARY KEY (`window_id`),
  UNIQUE KEY `window_id` (`window_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Таблица-справочник обслуживающих окон';

-- Дамп данных таблицы quick_queue.windows: ~3 rows (приблизительно)
DELETE FROM `windows`;
INSERT INTO `windows` (`window_id`, `label`, `stat`) VALUES
	(1, 'Окно 1 (Выдача документов)', 'Доступно'),
	(2, 'Окно 2 (Приём документов, выдача документов)', 'Доступно'),
	(3, 'Окно 3 (Приём документов, выдача документов)', 'Доступно');

-- Дамп структуры для таблица quick_queue.windows_staffs
CREATE TABLE IF NOT EXISTS `windows_staffs` (
  `windows_staffs_id` smallint NOT NULL AUTO_INCREMENT COMMENT 'ID записи',
  `window` smallint NOT NULL COMMENT 'ID окна',
  `staff` smallint NOT NULL COMMENT 'ID сотрудника',
  `stat` enum('Работает','Временно не работает','Заблокировано') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'Работает' COMMENT 'Статус',
  `create_date` timestamp NOT NULL DEFAULT (now()) COMMENT 'Дата добавления записи',
  `block_date` timestamp NULL DEFAULT NULL COMMENT 'Дата блокировки записи',
  PRIMARY KEY (`windows_staffs_id`),
  UNIQUE KEY `windows_staffs_id` (`windows_staffs_id`),
  KEY `FK__windows` (`window`),
  KEY `FK__staff` (`staff`),
  CONSTRAINT `FK__staff` FOREIGN KEY (`staff`) REFERENCES `staff` (`staff_id`),
  CONSTRAINT `FK__windows` FOREIGN KEY (`window`) REFERENCES `windows` (`window_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Таблица-связка, прикрепляющая сотрудников к окнам';

-- Дамп данных таблицы quick_queue.windows_staffs: ~3 rows (приблизительно)
DELETE FROM `windows_staffs`;
INSERT INTO `windows_staffs` (`windows_staffs_id`, `window`, `staff`, `stat`, `create_date`, `block_date`) VALUES
	(1, 1, 1, 'Работает', '2024-09-24 09:56:33', NULL),
	(2, 2, 3, 'Работает', '2024-09-24 09:56:39', NULL),
	(3, 3, 2, 'Работает', '2024-09-24 09:56:47', NULL);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
