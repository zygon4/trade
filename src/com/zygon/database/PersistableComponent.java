/**
 * 
 */

package com.zygon.database;

/**
 *
 * @author zygon
 */
public class PersistableComponent<T> {

    public final T component;
    
    public PersistableComponent(T component) {
        this.component = component;
    }

    public T getComponent() {
        return this.component;
    }
}
