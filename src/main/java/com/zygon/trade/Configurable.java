
package com.zygon.trade;

import com.zygon.configuration.Configuration;

/**
 *
 * @author zygon
 */
public interface Configurable {
    public void configure(Configuration configuration);
    public Configuration getConfiguration();
}
