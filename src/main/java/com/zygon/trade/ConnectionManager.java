package com.zygon.trade;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author david.charubini
 */
/*pkg*/ class ConnectionManager {

    private static final String DATABASE_NAME = "trade";
    private static final String DBURL = "jdbc:derby:" + DATABASE_NAME +";create=true;user=sa;password=";

    // could handle pooling, limiting, etc
    private final String driverClazz;
    private Connection con = null;

    public ConnectionManager(String driverClazz) throws ClassNotFoundException {
        this.driverClazz = driverClazz;
    }

    public void shutdown() throws SQLException {
        if (this.con != null) {
            DriverManager.getConnection(DBURL + ";shutdown=true");
            this.con.close();
            this.con = null;
        }
    }    
    
    public Connection getConnection() throws SQLException {

        if (this.con == null) {

            try {
                Class.forName(this.driverClazz);
            } catch (ClassNotFoundException cnf) {
                // TBD:?? what else to do?
                throw new RuntimeException(cnf);
            }

            this.con = DriverManager.getConnection(DBURL);
        }

        return this.con;
    }
}
