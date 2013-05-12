/**
 * 
 */

package com.zygon.trade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zygon
 */
public abstract class AbstractInformationHandler<T_IN> implements InformationHandler<T_IN> {

    private final String name;
    private final Logger log;
    private InformationHandler<T_IN> handler;

    protected AbstractInformationHandler(String name) {
        this.name = name;
        this.log = LoggerFactory.getLogger(this.name);
    }

    protected Logger getLog() {
        return this.log;
    }
    
    @Override
    public void handle(T_IN t) {
        this.log.trace("handling " + t);
        if (this.handler != null) {
            this.handler.handle(t);
        }
    }

    public String getName() {
        return this.name;
    }

    @Override
    public void setHandler(InformationHandler<T_IN> handler) {
        this.handler = handler;
    }
}
