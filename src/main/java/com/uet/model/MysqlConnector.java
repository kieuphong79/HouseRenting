package com.uet.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnector implements DataConnector {
    private Connection connection;
    private static String host = "localhost";
    private static String username = "root";
    private static String password = "1234";
    private static String database = "houserenting";

    private static MysqlConnector singleton;

    public static MysqlConnector getInstance() {
        if (singleton == null) singleton = new MysqlConnector();
        return singleton;
    }
    public Connection getConnection() {
        return connection;
    }

    public MysqlConnector() {
        connect();
    }



    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + host + "/" + database;
            this.connection = DriverManager.getConnection(url, username, password); 
            System.out.println("Connected to MySQL database successfully!");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Không kết nối được với database");
        }
    } 
    
    public void close() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
