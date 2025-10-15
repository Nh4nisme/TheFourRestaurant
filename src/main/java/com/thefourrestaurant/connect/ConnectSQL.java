package com.thefourrestaurant.connect;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectSQL {
	private static final String URL = "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=NhaHangDB;encrypt=false";
    private static final String USER = "sa";
    private static final String PASS = "sapassword";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}

