
package com.zygon.schema.parse;

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
        
        InputStream io = Tester.class.getResourceAsStream("test_schema.json");
        
        ConfigurationSchema schema = schemaParser.parse("test_schema.json", io);
    }
}
