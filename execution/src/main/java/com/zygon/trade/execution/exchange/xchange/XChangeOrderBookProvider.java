/**
 *
 */

package com.zygon.trade.execution.exchange.xchange;

import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.dto.trade.OpenOrders;
import com.xeiam.xchange.service.polling.marketdata.PollingMarketDataService;
import com.xeiam.xchange.service.polling.trade.PollingTradeService;
import com.zygon.trade.execution.OrderBookProvider;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author zygon
 */
public class XChangeOrderBookProvider implements OrderBookProvider {

    private final PollingTradeService pollingTradeService;
    private final PollingMarketDataService pollingMarketDataService;

    public XChangeOrderBookProvider(PollingTradeService pollingTradeService, PollingMarketDataService pollingMarketDataService) {
        this.pollingTradeService = pollingTradeService;
        this.pollingMarketDataService = pollingMarketDataService;
    }

    @Override
    public void getOpenOrders(List<LimitOrder> orders) {
        try {
            OpenOrders openOrders = this.pollingTradeService.getOpenOrders();
            orders.addAll(openOrders.getOpenOrders());
        } catch (IOException io) {
            // TBD:
            io.printStackTrace();
        }
    }

    @Override
    public void getOrderBook(String username, OrderBook orders, String tradeableIdentifer, String currency) {
        try {
            OrderBook openOrders = this.pollingMarketDataService.getOrderBook(new CurrencyPair(tradeableIdentifer, currency));
            orders.getAsks().addAll(openOrders.getAsks());
            orders.getBids().addAll(openOrders.getBids());
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
