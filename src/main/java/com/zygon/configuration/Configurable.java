
package com.zygon.configuration;

import com.zygon.schema.parse.ConfigurationSchema;

/**
 *
 * @author zygon
 */
public interface Configurable {
    public void configure(Configuration configuration);
    public String getId();
    public ConfigurationSchema getSchema();
}
