/**
 * 
 */

package com.zygon.trade.db;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zygon
 */
public class DatabaseMetadataImpl implements DatabaseMetadata {

    private final Map<String, String> properties = new HashMap<>();

    @Override
    public String getProperty(String name) {
        return this.properties.get(name);
    }

    public Map<String, String> getProperties() {
        return this.properties;
    }
}
