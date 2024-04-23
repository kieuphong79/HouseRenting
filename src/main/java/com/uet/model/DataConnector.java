package com.uet.model;

import java.sql.Connection;
import java.sql.SQLException;


public interface DataConnector {
    void connect() throws SQLException;
    void close();
}
