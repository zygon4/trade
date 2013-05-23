/**
 * 
 */

package com.zygon.trade.execution;

/**
 *
 * @author zygon
 */
public class ExchangeException extends Exception {

    static final long serialVersionUID = 1l;
    
    public ExchangeException(String message, Throwable cause) {
        super(message, cause);
    }
}
