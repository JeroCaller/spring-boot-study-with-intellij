-- 참고용

CREATE TABLE `user` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`username` VARCHAR(30) NOT NULL COLLATE 'utf8mb4_general_ci',
	`password` VARCHAR(200) NOT NULL COLLATE 'utf8mb4_general_ci',
	`age` INT(11) NULL DEFAULT NULL,
	`created_at` DATETIME(6) NULL DEFAULT NULL,
	`updated_at` DATETIME(6) NULL DEFAULT NULL,
	`last_login_at` DATETIME(6) NULL DEFAULT NULL,
	PRIMARY KEY (`id`) USING BTREE,
	UNIQUE INDEX `UKsb8bbouer5wak8vyiiy4pf2bx` (`username`) USING BTREE
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

CREATE TABLE `auth_token` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`user_id` INT(11) NULL DEFAULT NULL,
	`refresh_token` VARCHAR(200) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`previous_refresh_token` VARCHAR(200) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`is_valid` BIT(1) NULL DEFAULT NULL,
	`created_at` DATETIME(6) NULL DEFAULT NULL,
	`updated_at` DATETIME(6) NULL DEFAULT NULL,
	PRIMARY KEY (`id`) USING BTREE,
	UNIQUE INDEX `UKaiqc20kpjasth5bxogsragoif` (`user_id`) USING BTREE,
	CONSTRAINT `FKptnc32s7h74npe9p918cm4emk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON UPDATE RESTRICT ON DELETE RESTRICT
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;
