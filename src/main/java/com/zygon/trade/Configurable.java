
package com.zygon.trade;

import com.zygon.configuration.Configuration;
import com.zygon.schema.parse.ConfigurationSchema;

/**
 * This could even be a core (trade) package concept.
 *
 * @author zygon
 */
public interface Configurable {
    public void configure(Configuration configuration);
    public Configuration getConfiguration();
    public ConfigurationSchema getSchema();
}
