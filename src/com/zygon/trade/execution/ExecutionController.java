/**
 * 
 */

package com.zygon.trade.execution;

import com.xeiam.xchange.dto.Order;
import com.zygon.trade.execution.management.AccountController;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zygon
 */
public final class ExecutionController {

    // make instance based?
    private static final Logger log = LoggerFactory.getLogger(ExecutionController.class);
    
    /**
     * This represents everything the controller needs to manipulate an order
     * from placing it, monitoring it, canceling it, updating the proper
     * OrderBook, handling accounting, etc.
     */
    public static interface Binding {
        public AccountController getAccountController(String id);
        public MarketConditionsProvider getMarketConditionsProvider(String id);
        public OrderBookProvider getOrderBookProvider(String id);
        public TradeExecutor getTradeExecutor(String id);
        public OrderProvider getOrderProvider(String id);
    }

    private final String currency;
    private final Binding binding;

    public ExecutionController(String currency, Binding binding) {
        this.currency = currency;
        this.binding = binding;
    }
    
    public void cancelOrder (String id, String orderId) {
        // TODO: log impl with timestamps
        log.info("{} Cancel order request for order id ", new Date(), orderId);
        
        this.binding.getTradeExecutor(id).cancel(orderId);
    }

    public String getCurrency() {
        return this.currency;
    }
    
    public BigDecimal getMarketPrice(String id) {
        return this.binding.getMarketConditionsProvider(id).get().getPrice();
    }
    
    public void getOpenOrders(String id, List<Order> orders) {
        // could trace
        this.binding.getOrderBookProvider(id).getOpenOrders(orders);
    }
    
    public Order generateOrder(String id, Order.OrderType type, 
            BigDecimal tradableAmount, String tradableIdentifier, String transactionCurrency) {
        // could trace
        if (tradableAmount == null) {
            tradableAmount = this.getMarketPrice(id);
        }
        return this.binding.getOrderProvider(id).get(type, tradableAmount, tradableIdentifier, transactionCurrency);
    }
    
    public void placeOrder (String id, Order order) {
        // TODO: log impl with timestamps
        log.info("{} Place order request {}", new Date(), order);
        
        this.binding.getTradeExecutor(id).execute(order);
    }
}
