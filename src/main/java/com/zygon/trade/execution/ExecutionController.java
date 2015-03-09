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
        public AccountController getAccountController();
        public OrderBookProvider getOrderBookProvider();
        public TradeExecutor getTradeExecutor();
        public OrderProvider getOrderProvider();
    }

    private final Binding binding;
    
    public ExecutionController(Binding binding) {
        this.binding = binding;
    }
    
    public void cancelOrder (String username, String orderId) throws ExchangeException {
        // TODO: log impl with timestamps
        log.info("{} Cancel order request for order id {}", new Date(), orderId);
        
        this.binding.getTradeExecutor().cancel(username, orderId);
    }

    public void getOpenOrders(List<LimitOrder> orders) {
        // could trace
        this.binding.getOrderBookProvider().getOpenOrders(orders);
    }
    
    public void getOrderBook(String username, OrderBook book, String tradeableIdentifier, String currency) {
        // could trace
        this.binding.getOrderBookProvider().getOrderBook(username, book, tradeableIdentifier, currency);
    }
    
    public LimitOrder generateLimitOrder(String id, Order.OrderType type, 
            double tradableAmount, String tradableIdentifier, String transactionCurrency, double price) {
        return this.binding.getOrderProvider().getLimitOrder(id, type, tradableAmount, tradableIdentifier, transactionCurrency, price);
    }
    
    public MarketOrder generateMarketOrder(String id, Order.OrderType type, 
            double tradableAmount, String tradableIdentifier, String transactionCurrency) {
        return this.binding.getOrderProvider().getMarketOrder(id, type, tradableAmount, tradableIdentifier, transactionCurrency);
    }

    public AccountInfo getAccountInfo(String username) throws ExchangeException {
	return this.binding.getAccountController().getAccountInfo(username);
    }

    private Wallet getWallet(String username, String currency) throws ExchangeException {
        AccountInfo accountInfo = this.getAccountInfo(username);
        
        // check for null account info? check for user name equality?
        
        for (Wallet wallet : accountInfo.getWallets()) {
            if (wallet.getCurrency().equals(currency)) {
                return wallet;
            }
        }
        
        return null;
    }
    
    public double getBalance(String username, String currency) throws ExchangeException {
        Wallet wallet = this.getWallet(username, currency);
        
        if (wallet == null) {
            // TBD: open an account on the fly with 0 monies?
            
            throw new IllegalStateException("No wallet of currency " + currency + " found");
        }
        
        return wallet.getBalance().doubleValue();
    }
    
    public String placeOrder (String username, Order order) throws ExchangeException {
        // TODO: log impl with timestamps
        log.info("{} Place order request {}", new Date(), order);
        
        // For now let the order fail if there is not enough funds, etc.
        return this.binding.getTradeExecutor().execute(username, order);
        
        // TBD: post-trade operations?
    }
}
