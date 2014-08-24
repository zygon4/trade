
package com.zygon.trade;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author david.charubini
 */
public class DBUtil {
    
    private DBUtil() {}
    
    public static void createTable(Connection con, String tableName, String schema) throws SQLException {
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.execute("create table "+tableName+" "+schema);
        } finally {
            if (stmt != null) { stmt.close(); }
        }
    }
    
    public static void deleteTable(Connection con, String tableName) throws SQLException {
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.execute("drop table " + tableName);
        } finally {
            if (stmt != null) { stmt.close(); }
        }
    }
    
    public static boolean tableExists(Connection con, String tableName) throws SQLException {

        DatabaseMetaData metaData = con.getMetaData();
        ResultSet resultSet = metaData.getTables(null, null, tableName, null);

        return resultSet != null;
    }
}
