/**
 * 
 */

package com.zygon.exchange.market.model.indication;

import com.zygon.exchange.market.model.indication.technical.Numeric;

/**
 *
 * @author zygon
 */
public enum Classification {

    PRICE ("price", Numeric.class),
    VOLUME ("volume", Numeric.class);
//    DEPTH ("depth", null);
    
    private final String id;
    private final Class clazz;
    private final String className;

    private Classification(String id, Class clazz) {
        this.id = id;
        this.clazz = clazz;
        this.className = this.clazz.getName();
    }

    public Class getClazz() {
        return this.clazz;
    }
    
    public String getClassName() {
        return className;
    }

    public String getId() {
        return id;
    }
}
