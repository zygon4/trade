/**
 *
 */

package com.zygon.trade.execution.exchange.xchange;

import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.dto.trade.MarketOrder;
import com.xeiam.xchange.service.polling.trade.PollingTradeService;
import com.zygon.trade.execution.ExchangeException;
import com.zygon.trade.execution.TradeExecutor;
import java.io.IOException;

/**
 *
 * @author zygon
 */
public class XChangeTradeExecutor implements TradeExecutor {

    private final PollingTradeService pollingTradeService;

    public XChangeTradeExecutor(PollingTradeService pollingTradeService) {
        this.pollingTradeService = pollingTradeService;
    }

    @Override
    public void cancel(String username, String orderId) throws ExchangeException {
        try {
        this.pollingTradeService.cancelOrder(orderId);
        } catch (IOException io) {
            throw new ExchangeException("Error cancelling order " + orderId + " for user " + username, io);
        }
    }

    @Override
    public String execute(String username, Order order) throws ExchangeException {

        String orderId = null;

        if (order instanceof LimitOrder) {
            try {
                orderId = this.pollingTradeService.placeLimitOrder((LimitOrder)order);
            } catch (IOException io) {
                throw new ExchangeException("Error executing order " + order + " for user " + username, io);
            }
        } else if (order instanceof MarketOrder) {
            try {
                orderId = this.pollingTradeService.placeMarketOrder((MarketOrder)order);
            } catch (IOException io) {
                throw new ExchangeException("Error executing order " + order + " for user " + username, io);
            }
        }

        // TODO: understand what it means for a market order to be cancelled
        // and what is returned when that happens.  For now throw an exception
        // if we return no order id.
        if (orderId == null) {
            throw new ExchangeException("Received no order id when placing order: " + order);
        }

        return orderId;
    }
}
