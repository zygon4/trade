package com.zygon.trade;

/**
 *
 * @author davec
 */
public interface InstallableStorage {

    public String[] getStoredIds();

    public MetaData retrieve(String id);

    public void store(String id, MetaData metadata);
}
