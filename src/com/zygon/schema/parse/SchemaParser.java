
package com.zygon.schema.parse;

import com.zygon.schema.Schema;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author zygon
 */
public interface SchemaParser {
    public Schema parse(InputStream is) throws IOException ;
}
