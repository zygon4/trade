
package com.zygon.trade.execution.exchange;

import com.zygon.trade.execution.ExchangeException;

/**
 *
 * @author zygon
 */
public interface ExchangeEventProvider {
    ExchangeEvent getEvent() throws ExchangeException;
}
