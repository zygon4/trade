
package com.zygon.data;

import java.io.IOException;

/**
 * The purpose of this interface is to allow anonymous data sets to write 
 * their content in a single shot.
 *
 * @author zygon
 */
public interface DataSource<T> {
    public void writeData(Handler<T> handler) throws IOException;
}
