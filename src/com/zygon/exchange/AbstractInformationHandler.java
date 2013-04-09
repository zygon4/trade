/**
 * 
 */

package com.zygon.exchange;

/**
 * @author zygon
 */
public abstract class AbstractInformationHandler<T_IN> implements InformationHandler<T_IN> {

    private final String name;
    private InformationHandler<T_IN> handler;

    protected AbstractInformationHandler(String name) {
        this.name = name;
    }

    @Override
    public void handle(T_IN t) {
        if (this.handler != null) {
            this.handler.handle(t);
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public void setHandler(InformationHandler<T_IN> handler) {
        this.handler = handler;
    }
}
