
package com.zygon.util;

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
        try (Statement stmt = con.createStatement()) {
            stmt.execute("CREATE TABLE " + tableName + " " + schema);
        }
    }
    
    public static void deleteTable(Connection con, String tableName) throws SQLException {
        try (Statement stmt = con.createStatement()) {
            stmt.execute("DROP TABLE " + tableName);
        }
    }
    
    public static int executeUpdate(Connection con, String query) throws SQLException {
        try (Statement stmt = con.createStatement()) {
            return stmt.executeUpdate(query);
        }
    }
    
    public static ResultSet executeQuery(Connection con, String query) throws SQLException {
        try (Statement stmt = con.createStatement()) {
            return stmt.executeQuery(query);
        }
    }
    
    public static boolean tableExists(Connection con, String tableName) throws SQLException {

        DatabaseMetaData metaData = con.getMetaData();
        ResultSet resultSet = metaData.getTables(null, null, tableName, null);

        return resultSet != null;
    }
}
