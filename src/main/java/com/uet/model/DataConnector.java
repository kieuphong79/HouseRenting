package com.uet.model;

import java.sql.Connection;

public interface DataConnector {
    void connect();
    void close();
}
