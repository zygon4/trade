/**
 * 
 */

package com.zygon.trade.execution;

import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.dto.trade.MarketOrder;
import com.xeiam.xchange.dto.trade.Wallet;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The heart of the trade engine. It uses an internal "binding" to execute
 * operations.
 *
 * @author zygon
 * @version 1.0
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
        public OrderBookProvider getOrderBookProvider(String id);
        public TradeExecutor getTradeExecutor(String id);
        public OrderProvider getOrderProvider(String id);
    }

    private final Binding binding;

    public ExecutionController(Binding binding) {
        this.binding = binding;
    }
    
    public void cancelOrder (String id, String orderId) throws ExchangeException {
        // TODO: log impl with timestamps
        log.info("{} Cancel order request for order id {}", new Date(), orderId);
        
        this.binding.getTradeExecutor(id).cancel(orderId);
    }

    public void getOpenOrders(String id, List<LimitOrder> orders) {
        // could trace
        this.binding.getOrderBookProvider(id).getOpenOrders(orders);
    }
    
    public void getOrderBook(String id, OrderBook book, String tradeableIdentifier, String currency) {
        // could trace
        this.binding.getOrderBookProvider(id).getOrderBook(book, tradeableIdentifier, currency);
    }
    
    public LimitOrder generateLimitOrder(String id, Order.OrderType type, 
            double tradableAmount, String tradableIdentifier, String transactionCurrency, double price) {
        return this.binding.getOrderProvider(id).getLimitOrder(type, tradableAmount, tradableIdentifier, transactionCurrency, price);
    }
    
    public MarketOrder generateMarketOrder(String id, Order.OrderType type, 
            double tradableAmount, String tradableIdentifier, String transactionCurrency) {
        return this.binding.getOrderProvider(id).getMarketOrder(type, tradableAmount, tradableIdentifier, transactionCurrency);
    }
    
    private Wallet getWallet(String id, String currency) {
        AccountInfo accountInfo = this.binding.getAccountController(id).getAccountInfo();
        
        // user name of the account??
        for (Wallet wallet : accountInfo.getWallets()) {
            if (wallet.getCurrency().equals(currency)) {
                return wallet;
            }
        }
        
        return null;
    }
    
    public double getBalance(String id, String currency) {
        Wallet wallet = this.getWallet(id, currency);
        
        if (wallet == null) {
            // TBD: open an account on the fly with 0 monies?
            
            throw new IllegalStateException("No wallet of currency " + currency + " found");
        }
        
        return wallet.getBalance().getAmount().doubleValue();
    }
    
    public String placeOrder (String id, Order order) throws ExchangeException {
        // TODO: log impl with timestamps
        log.info("{} Place order request {}", new Date(), order);
        
        // For now let the order fail if there is not enough funds, etc.
        return this.binding.getTradeExecutor(id).execute(order);
        
        // TBD: post-trade operations?
    }
}
