CREATE TABLE users (
                       username   VARCHAR(255) NOT NULL PRIMARY KEY ,
                       password   VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE market_data (
                             metal       VARCHAR(3) NOT NULL,
                             currency    VARCHAR(10) NOT NULL,
                             date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,
                             weight_unit VARCHAR(10) NOT NULL,
                             buy_price        DECIMAL(10, 2) NOT NULL,
                             average_price         DECIMAL(10, 2) NOT NULL,
                             sell_price DECIMAL(10, 2) NOT NULL,
                             value       DECIMAL(10, 2) NOT NULL,
                             performance DECIMAL(10, 2) NOT NULL,
                             create_ts   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                             update_ts   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,
                             PRIMARY KEY (metal, date)
);

CREATE INDEX date_index ON market_data (date);

CREATE TABLE wallet (
                        wallet_id VARCHAR(255) NOT NULL  PRIMARY KEY ,
                        balance   DECIMAL(10, 2) DEFAULT 0.00 NOT NULL,
                        status    VARCHAR(255) NOT NULL,
                        create_ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                        update_ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,
                        username  VARCHAR(36) NOT NULL UNIQUE
);

CREATE TABLE wallet_transaction (
                                    transaction_id VARCHAR(36) NOT NULL PRIMARY KEY UNIQUE,
                                    wallet_id      VARCHAR(36) NOT NULL,
                                    transaction_type           ENUM ('CREDIT', 'DEBIT') NOT NULL,
                                    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                    status          ENUM ('SUCCESS', 'FAILED', 'PENDING') NOT NULL,
                                    amount         DECIMAL(10,2) NOT NULL,
                                    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL  ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE asset (
                       username       VARCHAR(36) NOT NULL,
                       metal          VARCHAR(3) NOT NULL,
                       balance_gram      DECIMAL(10, 2) DEFAULT 0.00 NOT NULL,
                       balance_hold_gram DECIMAL DEFAULT 0 NOT NULL,
                       create_ts      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                       update_ts      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL  ON UPDATE CURRENT_TIMESTAMP,
                       PRIMARY KEY (username, metal)

);

CREATE TABLE asset_transaction (
                                   transaction_id        VARCHAR(36) NOT NULL PRIMARY KEY UNIQUE,
                                   username              VARCHAR(36) NOT NULL,
                                   metal                 VARCHAR(3) NOT NULL,
                                   transaction_type      ENUM ('BUY', 'SELL') NOT NULL,
                                   quantity_in_gram         DECIMAL(10, 2) NOT NULL,
                                   price                 DECIMAL(10, 2) NOT NULL,
                                   created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                   status                ENUM ('SUCCESS', 'FAILED', 'PENDING') NULL,
                                   updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL  ON UPDATE CURRENT_TIMESTAMP,
                                   wallet_transaction_id VARCHAR(36) NULL
);

CREATE INDEX username_index ON asset_transaction (username);
CREATE INDEX wallet_transaction_id_index ON asset_transaction (wallet_transaction_id);
