
package com.zygon.trade;

/**
 *
 * @author david.charubini
 */
public class MetaData {
    private final String id;
    private final String clazz;
    private final Configurable configuration;

    public MetaData(String id, String clazz, Configurable configurable) {
        this.id = id;
        this.clazz = clazz;
        this.configuration = configurable;
    }

    public String getClazz() {
        return this.clazz;
    }

    public Configurable getConfigurable() {
        return this.configuration;
    }

    public String getId() {
        return this.id;
    }
}
