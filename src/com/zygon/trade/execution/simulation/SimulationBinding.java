/**
 * 
 */

package com.zygon.trade.execution.simulation;

import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.dto.trade.MarketOrder;
import com.xeiam.xchange.dto.trade.Wallet;
import com.zygon.trade.execution.ExecutionController;
import com.zygon.trade.execution.MarketConditions;
import com.zygon.trade.execution.OrderBookProvider;
import com.zygon.trade.execution.OrderProvider;
import com.zygon.trade.execution.TradeExecutor;
import com.zygon.trade.execution.management.AccountController;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zygon
 */
public class SimulationBinding implements ExecutionController.Binding {
    
    private static final class SimulationAccountController implements AccountController {

        private final String user;
        private final CurrencyUnit currency;
        private double ammount = 0.0;
        
        private double high = 0.0;
        private double low = 0.0;
        
        public SimulationAccountController(String user, CurrencyUnit currency, double ammount) {
            this.user = user;
            this.currency = currency;
            this.ammount = ammount;
            this.high = this.ammount;
            this.low = this.ammount;
        }
        
        public synchronized void add(BigDecimal ammount, CurrencyUnit currency) {
            if (!this.currency.equals(currency)) {
                // the scope of the simulation has gone beyond what it is currently intended.
                throw new IllegalArgumentException();
            }
            
            this.ammount += ammount.doubleValue();
            this.high = Math.max(this.high, this.ammount);
        }
        
        public synchronized void subtract(BigDecimal ammount, CurrencyUnit currency) {
            if (!this.currency.equals(currency)) {
                // the scope of the simulation has gone beyond what it is currently intended.
                throw new IllegalArgumentException();
            }
            
            this.ammount -= ammount.doubleValue();
            this.low = Math.min(this.low, this.ammount);
        }
        
        @Override
        public AccountInfo getAccountInfo() {
            return new AccountInfo(this.user, Arrays.asList(new Wallet(this.currency.getCurrencyCode(), BigMoney.of(this.currency, this.ammount))));
        }

        public double getBalance() {
            return this.ammount;
        }
        
        public double getMaxDrawDown() {
            return this.low;
        }
        
        public double getMaxProfit() {
            return this.high;
        }
    }
    
    private static final class SimulationOrderBookProvider implements OrderBookProvider {

        private final List<Order> orders = new ArrayList<>();
        
        @Override
        public void getOpenOrders(List<Order> orders) {
            orders.addAll(this.orders);
        }
    }
    
    private static final class SimulationOrderProvider implements OrderProvider {

        @Override
        public Order get(Order.OrderType type, double tradableAmount, String tradableIdentifier, String transactionCurrency) {
            Order order = new MarketOrder(type, new BigDecimal(tradableAmount), tradableIdentifier, transactionCurrency);
            
            return order;
        }
    }
    
    private static final class SimulationTradeExecutor implements TradeExecutor {

        private final Logger log = LoggerFactory.getLogger(SimulationTradeExecutor.class);
        
        private final SimulationAccountController accntController;
        private final MarketConditions marketConditions;

        public SimulationTradeExecutor(SimulationAccountController accntController, MarketConditions marketConditions) {
            this.accntController = accntController;
            this.marketConditions = marketConditions;
        }
        
        @Override
        public void cancel(String orderId) {
            this.log.info("Cancelling orderId: {}", orderId);
        }

        @Override
        public void execute(Order order) {
            BigDecimal marketPrice = this.marketConditions.getPrice().value();
            
            this.log.info("Executing order: {} at price {}", order, marketPrice);
            
            // Because this is a market order we're just estimating what the market price might be.
            BigDecimal ammount = order.getTradableAmount().multiply(marketPrice);
            
            if (order.getType() == Order.OrderType.BID) {
                this.accntController.subtract(ammount, CurrencyUnit.of(order.getTransactionCurrency()));
            } else {
                this.accntController.add(ammount, CurrencyUnit.of(order.getTransactionCurrency()));
            }
            
            this.log.info("Account balance {}, high {}, low {}", 
                    this.accntController.getBalance(), this.accntController.getMaxProfit(), this.accntController.getMaxDrawDown());
        }
    }
    
    private final String user;
    private final SimulationAccountController accntController;
    private final MarketConditions marketConditions;
    private final OrderBookProvider orderBookProvider;
    private final OrderProvider orderProvider;
    private final TradeExecutor tradeExecutor;

    public SimulationBinding(String user, CurrencyUnit currency, double ammount, MarketConditions marketConditions) {
        this.user = user;
        this.accntController = new SimulationAccountController(this.user, currency, ammount);
        this.marketConditions = marketConditions;
        this.orderBookProvider = new SimulationOrderBookProvider();
        this.orderProvider = new SimulationOrderProvider();
        this.tradeExecutor = new SimulationTradeExecutor(this.accntController, this.marketConditions);
    }
    
    @Override
    public AccountController getAccountController(String id) {
        return this.accntController;
    }

    @Override
    public OrderBookProvider getOrderBookProvider(String id) {
        return this.orderBookProvider;
    }

    @Override
    public OrderProvider getOrderProvider(String id) {
        return this.orderProvider;
    }

    @Override
    public TradeExecutor getTradeExecutor(String id) {
        return this.tradeExecutor;
    }
}
