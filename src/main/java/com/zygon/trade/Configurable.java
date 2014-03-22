
package com.zygon.trade;

import com.zygon.schema.ConfigurationSchema;

/**
 *
 * @author zygon
 */
public interface Configurable {
    public void configure(Configuration configuration);
    public Configuration getConfiguration();
    public ConfigurationSchema getSchema();
}
