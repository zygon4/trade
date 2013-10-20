
package com.zygon.trade.modules.data;

import com.zygon.schema.parse.*;
import com.zygon.schema.ConfigurationSchema;
import java.io.InputStream;

/**
 *
 * @author zygon
 */
public class Tester {
    
    private static final String PKGBASE;

    static {
        final String pkgName = Tester.class.getPackage().getName();
        PKGBASE = "/" + pkgName.replace(".", "/");
    }
    
    public static void main(String[] args) throws Exception {
        JSONSchemaParser schemaParser = new JSONSchemaParser();
        
        InputStream io = Tester.class.getResourceAsStream("data_schema.json");
        
        ConfigurationSchema schema = schemaParser.parse("data_schema.json", io);
    }
}
