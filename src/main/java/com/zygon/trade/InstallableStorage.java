package com.zygon.trade;

import java.sql.SQLException;

/**
 * This represents a store for "installable" object - this will generally be
 * used for persisting/retrieving modules.
 *
 * @author davec
 */
public interface InstallableStorage {
    public void remove(String id) throws SQLException;
    public String[] getStoredIds() throws SQLException;
    public Installable retrieve(String id) throws SQLException;
    public void store(Installable installable) throws SQLException;
}
