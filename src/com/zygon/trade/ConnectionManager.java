
package com.zygon.trade;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author david.charubini
 */
/*pkg*/ class ConnectionManager {

    private static final String DATABASE_NAME = "trade";
    
    // could handle pooling, limiting, etc
    
    private final String driverClazz;
    private Connection con = null;

    public ConnectionManager(String driverClazz) throws ClassNotFoundException {
        this.driverClazz = driverClazz;
    }
    
    public void close() throws SQLException {
        this.con.close();
        this.con = null;
    }
    
    public Connection getConnection() throws SQLException {
        
        if (this.con == null) {
            
            try {
                Class.forName(this.driverClazz);
            } catch (ClassNotFoundException cnf) {
                // TBD:?? what else to do?
                throw new RuntimeException(cnf);
            }
            
            Properties p = new Properties();
            p.put("user", "sa");
            p.put("password", "");
            
            this.con = DriverManager.getConnection("jdbc:derby:"+DATABASE_NAME+";create=true", p);
        }
        
        return this.con;
    }
}
