package com.zygon.trade.execution.exchange;

/**
 *
 * @author davec
 */
public interface EventListener {
    public void handle (ExchangeEvent event);
}
