/**
 * 
 */

package com.zygon.trade.execution.exchange.mtgox;

import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.dto.trade.MarketOrder;
import com.xeiam.xchange.mtgox.v2.service.polling.MtGoxPollingTradeService;
import com.zygon.trade.execution.ExchangeException;
import com.zygon.trade.execution.TradeExecutor;
import java.io.IOException;

/**
 *
 * @author zygon
 */
public class MtGoxTradeExecutor implements TradeExecutor {

    private final MtGoxPollingTradeService mtGoxPollingTradeService;

    public MtGoxTradeExecutor(MtGoxPollingTradeService mtGoxPollingTradeService) {
        this.mtGoxPollingTradeService = mtGoxPollingTradeService;
    }

    @Override
    public void cancel(String username, String orderId) throws ExchangeException {
        try {
        this.mtGoxPollingTradeService.cancelOrder(orderId);
        } catch (IOException io) {
            throw new ExchangeException("Error cancelling order " + orderId + " for user " + username, io);
        }
    }
    
    @Override
    public String execute(String username, Order order) throws ExchangeException {
        
        String orderId = null;
        
        if (order instanceof LimitOrder) {
            try {
                orderId = this.mtGoxPollingTradeService.placeLimitOrder((LimitOrder)order);
            } catch (IOException io) {
                throw new ExchangeException("Error executing order " + order + " for user " + username, io);
            }
        } else if (order instanceof MarketOrder) {
            try {
                orderId = this.mtGoxPollingTradeService.placeMarketOrder((MarketOrder)order);
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