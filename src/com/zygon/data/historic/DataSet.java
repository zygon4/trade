
package com.zygon.data.historic;

/**
 *
 * @author zygon
 */
public class DataSet<T> {
    
    private final String name;
    private final String filePath;

    public DataSet(String name, String filePath) {
        this.name = name;
        this.filePath = filePath;
    }

    public String getName() {
        return this.name;
    }
    
    
}
