/**
 * 
 */

package com.zygon.trade.database;

/**
 *
 * @author zygon
 */
public class PersistableComponent<T> {

    public final T component;
    
    public PersistableComponent(T key) {
        this.component = key;
    }

    public T getComponent() {
        return component;
    }
}





