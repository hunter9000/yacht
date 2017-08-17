USE yacht;

CREATE TABLE `yahtzee_game` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `user` BIGINT(20) NOT NULL,
	PRIMARY KEY (`id`),
	INDEX `yahtzee_game__user` (`user`),
	CONSTRAINT `yahtzee_game__user` FOREIGN KEY (`user`) REFERENCES `user` (`id`)
) COLLATE='utf8_general_ci' ENGINE=InnoDB;

CREATE TABLE `game_sheet` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `yahtzee_game` BIGINT(20) NOT NULL,
	PRIMARY KEY (`id`),
	INDEX `game_sheet__yahtzee_game` (`yahtzee_game`),
	CONSTRAINT `game_sheet__yahtzee_game` FOREIGN KEY (`yahtzee_game`) REFERENCES `yahtzee_game` (`id`)
) COLLATE='utf8_general_ci' ENGINE=InnoDB;

CREATE TABLE `roll_score` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `game_sheet` BIGINT(20) NOT NULL,
    `roll_type` VARCHAR(255) NOT NULL,
    `score` INTEGER(11),
    `chosen` BIT(1) NOT NULL,
	PRIMARY KEY (`id`),
	INDEX `roll_score__game_sheet` (`game_sheet`),
	CONSTRAINT `roll_score__game_sheet` FOREIGN KEY (`game_sheet`) REFERENCES `game_sheet` (`id`)
) COLLATE='utf8_general_ci' ENGINE=InnoDB;

CREATE TABLE `die_roll` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `yahtzee_game` BIGINT(20) NOT NULL,
    `roll_value` INTEGER(11) NOT NULL,
    `is_marked` BIT(1) NOT NULL,
    `roll_order` INTEGER(11) NOT NULL,
	PRIMARY KEY (`id`),
	INDEX `die_roll__yahtzee_game` (`yahtzee_game`),
	CONSTRAINT `die_roll__yahtzee_game` FOREIGN KEY (`yahtzee_game`) REFERENCES `yahtzee_game` (`id`)
) COLLATE='utf8_general_ci' ENGINE=InnoDB;

ALTER TABLE `yahtzee_game` ADD COLUMN `num_rolls` INTEGER(11) NOT NULL;

ALTER TABLE `game_sheet` ADD COLUMN `top_section_subtotal` INTEGER(11) NOT NULL;
ALTER TABLE `game_sheet` ADD COLUMN `has_top_section_bonus` BIT(1) NOT NULL;
ALTER TABLE `game_sheet` ADD COLUMN `top_section_score` INTEGER(11) NOT NULL;
ALTER TABLE `game_sheet` ADD COLUMN `bottom_section_score` INTEGER(11) NOT NULL;
ALTER TABLE `game_sheet` ADD COLUMN `total_score` INTEGER(11) NOT NULL;

ALTER TABLE `yahtzee_game` ADD COLUMN `completed` BIT(1) NOT NULL;

ALTER TABLE `yahtzee_game` ADD COLUMN `start_date` TIMESTAMP NOT NULL;

INSERT INTO `sql_files` (`sql_file_name`, `run_date`) VALUES ('3.sql', NOW());