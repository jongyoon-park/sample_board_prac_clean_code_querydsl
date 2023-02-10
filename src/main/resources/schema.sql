CREATE TABLE IF NOT EXISTS account_type (
    type_id BIGINT NOT NULL AUTO_INCREMENT,
    type_name varchar(200) NOT NULL UNIQUE,
    PRIMARY KEY (type_id)
);

CREATE TABLE IF NOT EXISTS user
(
    user_id BIGINT NOT NULL AUTO_INCREMENT,
    nickname VARCHAR(200) NOT NULL UNIQUE,
    type_id BIGINT NOT NULL,
    account_id VARCHAR(200) NOT NULL UNIQUE,
    quit BOOLEAN NOT NULL DEFAULT FALSE,
    created_dt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id),
    FOREIGN KEY (type_id) REFERENCES account_type (type_id)
);

CREATE TABLE IF NOT EXISTS board
(
    board_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(600) NOT NULL,
    content varchar(3000) NOT NULL,
    created_dt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_dt DATETIME NOT NULL ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (board_id),
    FOREIGN KEY (user_id) REFERENCES user (user_id)
);

CREATE TABLE IF NOT EXISTS recommend
(
    recommend_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    board_id BIGINT NOT NULL,
    recommend_value BOOLEAN NOT NULL DEFAULT TRUE,
    created_dt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_dt DATETIME NOT NULL ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (recommend_id),
    FOREIGN KEY (user_id) REFERENCES user (user_id),
    FOREIGN KEY (board_id) REFERENCES board (board_id)
);