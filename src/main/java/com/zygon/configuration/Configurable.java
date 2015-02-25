
package com.zygon.configuration;

/**
 *
 * @author zygon
 */
public interface Configurable {
    public void configure(Configuration configuration);
    public Configuration getConfiguration();
    public String getId();
}
