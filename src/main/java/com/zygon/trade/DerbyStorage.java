package com.zygon.trade;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author david.charubini
 */
public class DerbyStorage implements InstallableStorage {

    static {
	System.setProperty("derby.system.home", "/tmp");
    }
    
    private static final String TABLE_NAME = "install";
    private static final String SCHEMA = "(id int)";
    private final Connection con;
    private boolean installed = false;

    public DerbyStorage(Connection con) {
        this.con = con;
    }
    
    private void checkInstallation(Connection con) {
        
        if (!this.installed) {
            try {
                if (!DBUtil.tableExists(con, TABLE_NAME)) {
                    DBUtil.createTable(con, TABLE_NAME, SCHEMA);
                }
            } catch (SQLException sqe) {
                // log/do something better
                throw new RuntimeException(sqe);
            }
            this.installed = true;
        }
    }
    
    // this, right here, is just some loggygagging - this needs to 
    // come from storage - and cleaner.
    private final Map<String, MetaData> metadataById = new HashMap<>();
    
    @Override
    public String[] getStoredIds() {
        this.checkInstallation(this.con);
        
        return this.metadataById.keySet().toArray(new String[this.metadataById.keySet().size()]);
    }
    
    @Override
    public MetaData retrieve(String id) {
        this.checkInstallation(this.con);
        
        return this.metadataById.get(id);
    }

    @Override
    public void store(String id, MetaData metadata) {
       this.checkInstallation(this.con);
       
       this.metadataById.put(id, metadata);
    }
    
    
//    public static void main(String[] args) throws Exception {
//        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
//
//        InstallableStorage storage = null;
//        
//        ConnectionManager cm = new ConnectionManager("org.apache.derby.jdbc.EmbeddedDriver");
//        try {
//            storage = new DerbyStorage(cm.getConnection());
//        } finally {
//            cm.close();
//        }
//        
//        storage.store("1", null);
//        storage.retrieve("1");
//        String[] storedIds = storage.getStoredIds();
//                             
//        //        
//        //        
//        //        Properties p = new Properties();
//        //
//        //        p.put("user", "sa");
//        //        p.put("password", "");
//        //
//        //        Connection con = null;
//        //        
//        //        try {
//        //            con = DriverManager.getConnection("jdbc:derby:mynewDB;create=true", p);
//        //            
//        //            boolean tableExists = false;
//        //            try {
//        //                tableExists = tableExists(con, "foo");
//        //            } catch (SQLException sql) {
//        //                tableExists = false;
//        //            }
//        //            
//        //            Statement stmt = null;
//        //            if (tableExists) {
//        //                try {
//        //                    stmt = con.createStatement();
//        //                    stmt.execute("drop table mynewDB.foo");
//        //                } finally {
//        //                    if (stmt != null) { stmt.close(); }
//        //                }
//        //            }
//        //            
//        //            try {
//        //                stmt = con.createStatement();
//        //                stmt.execute("create table foo(id int)");
//        //            } finally {
//        //                if (stmt != null) { stmt.close(); }
//        //            }
//        //            
//        //            try {
//        //                stmt = con.createStatement();
//        //                stmt.execute("insert into foo values(1)");
//        //            } finally {
//        //                if (stmt != null) { stmt.close(); }
//        //            }
//        //            
//        //            try {
//        //                stmt = con.createStatement();
//        //                ResultSet results = stmt.executeQuery("select * from foo");
//        //                
//        //                while (results.next()) {
//        //                    System.out.println("result: " + results.getInt("id"));
//        //                }
//        //            } finally {
//        //                if (stmt != null) { stmt.close(); }
//        //            }
//        //            
//        //        } catch (SQLException se) {
//        //            se.printStackTrace();
//        //        } finally {
//        //            if (con != null) {
//        //                con.close();
//        //            }
//        //        }
//    }
}
