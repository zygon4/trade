package com.zygon.trade;

/**
 * This represents a store for "installable" object - this will generally be
 * used for persisting/retrieving modules.
 *
 * @author davec
 */
public interface InstallableStorage {

    public String[] getStoredIds();

    public MetaData retrieve(String id);

    public void store(String id, MetaData metadata);
}
