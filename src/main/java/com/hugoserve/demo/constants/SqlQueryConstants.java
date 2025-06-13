package com.hugoserve.demo.constants;

public class SqlQueryConstants {

    private SqlQueryConstants() {
    }

    // Users Table Queries
    public static final String INSERT_USER = "INSERT INTO users (username, password) VALUES (:username, :password);";
    public static final String SELECT_USER = "SELECT * FROM users WHERE username = :username;";

    // Market Data Table Queries
    public static final String INSERT_MARKET_DATA =
            "INSERT INTO market_data (metal, currency ,date, weight_unit, buy_price, average_price, sell_price, value, performance) VALUES (:metal,:currency,:date, :weight_unit, :buy_price, :average_price, :sell_price, :value, :performance);";
    public static final String SELECT_MARKET_DATA_BY_METAL =
            "SELECT * FROM market_data WHERE metal = :metal ORDER BY date DESC;";
    public static final String SELECT_LATEST_MARKET_DATA_BY_METAL =
            "SELECT * FROM market_data WHERE metal = :metal ORDER BY date DESC LIMIT 1;";

    // wallet Table Queries
    public static final String INSERT_WALLET =
            "INSERT INTO wallet (wallet_id, username, status) VALUES (:wallet_id, :username, :status);";
    public static final String SELECT_WALLET_BY_USERNAME =
            "SELECT * FROM wallet WHERE username = :username;";
    public static final String SELECT_WALLET_BY_WALLETID =
            "SELECT * FROM wallet WHERE wallet_id=:wallet_id;";
    public static final String ADD_WALLET_BALANCE =
            "UPDATE wallet SET balance = balance + :amount WHERE wallet_id = :wallet_id;";
    public static final String SUBTRACT_WALLET_BALANCE =
            "UPDATE wallet SET balance = balance - :amount WHERE wallet_id = :wallet_id;";

    // asset Table Queries
    public static final String CREDIT_ASSET =
            "INSERT INTO asset (username, metal, balance_gram) VALUES (:username, :metal, :quantity) ON DUPLICATE KEY UPDATE balance_gram=balance_gram+:quantity;";

    public static final String SELECT_ASSET_BY_USERNAME =
            "SELECT * FROM asset WHERE username = :username;";
    public static final String HOLD_ASSET_BALANCE =
            "UPDATE asset SET balance_gram = balance_gram-:quantity , balance_hold_gram = balance_hold_gram + :quantity WHERE username = :username AND metal = :metal;";
    public static final String SUBTRACT_HOLD_ASSET_BALANCE =
            "UPDATE asset SET balance_hold_gram = balance_hold_gram-:quantity  WHERE username = :username AND metal = :metal;";
    public static final String DEBIT_ASSET_BALANCE =
            "UPDATE asset SET balance_hold_gram = balance_hold_gram -:quantity WHERE username = :username AND metal = :metal;";
    public static final String SELECT_ASSET_USERNAME_METAL =
            "Select * FROM asset WHERE username = :username AND metal = :metal;";

    // asset Transaction Table Queries
    public static final String INSERT_ASSET_TRANSACTION =
            "INSERT INTO asset_transaction (transaction_id, username, metal, transaction_type, quantity_in_gram, price,status,wallet_transaction_id) VALUES (:transaction_id, :username, :metal, :transaction_type, :quantity_in_gram, :price,:status,:wallet_transaction_id);";
    public static final String SELECT_ASSET_TRANSACTIONS_BY_USERNAME =
            "SELECT * FROM asset_transaction WHERE username = :username;";
    public static final String SELECT_TRANSACTION_BY_ASSET_ID =
            "SELECT * FROM asset_transaction WHERE transaction_id = :transaction_id;";

    // wallet Transaction Table Queries
    public static final String INSERT_WALLET_TRANSACTION =
            "INSERT INTO wallet_transaction (transaction_id, wallet_id, transaction_type,status,amount) VALUES (:transaction_id, :wallet_id, :transaction_type, :status, :amount);";
    public static final String SELECT_WALLET_TRANSACTIONS_BY_WALLET_ID =
            "SELECT * FROM wallet_transaction WHERE wallet_id = :wallet_id;";
    public static final String SELECT_WALLET_TRANSACTION =
            "SELECT * FROM wallet_transaction WHERE transaction_id = :transaction_id;";
    public static final String UPDATE_WALLET_TRANSACTION_STATUS =
            "UPDATE wallet_transaction  SET status=:status WHERE transaction_id=:transaction_id;";

    public static final String UPDATE_TRANSACTION =
            "UPDATE asset_transaction SET wallet_transaction_id = :wallet_transaction_id ,status=:status ,price=:price WHERE transaction_id=:transaction_id;";

    public static final String UPDATE_TRANSACTION_STATUS =
            "UPDATE asset_transaction SET status=:status WHERE transaction_id=:transaction_id;";

}
