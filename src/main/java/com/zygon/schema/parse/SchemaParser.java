
package com.zygon.schema.parse;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author zygon
 */
public interface SchemaParser {
    public ConfigurationSchema parse(String schemaResourceName, InputStream is) throws IOException ;
}
