
package com.zygon.data1;

/**
 *
 * @author zygon
 * @param <T>
 */
public interface Handler<T extends Data> {
    public void handle(T data);
}
