
package com.zygon.trade;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author david.charubini
 */
public class DBUtil {
    
    public void createTable(Connection con, String tableName, String schema) throws SQLException {
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.execute("create table "+tableName+" "+schema);
        } finally {
            if (stmt != null) { stmt.close(); }
        }
    }
    
    public void deleteTable(Connection con, String tableName) throws SQLException {
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.execute("drop table " + tableName);
        } finally {
            if (stmt != null) { stmt.close(); }
        }
    }
}
