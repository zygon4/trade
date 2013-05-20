/**
 * 
 */

package com.zygon.trade.execution.simulation;

import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.dto.trade.MarketOrder;
import com.xeiam.xchange.dto.trade.Wallet;
import com.zygon.trade.execution.ExecutionController;
import com.zygon.trade.execution.MarketConditionsProvider;
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

        private final AccountInfo accntInfo;
        
        public SimulationAccountController(String user, CurrencyUnit currency, double ammount) {
            accntInfo = new AccountInfo(user, Arrays.asList(new Wallet(currency.getCurrencyCode(), BigMoney.of(currency, ammount))));
        }
        
        
        @Override
        public AccountInfo getAccountInfo() {
            return this.accntInfo;
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
        public Order get(Order.OrderType type, BigDecimal tradableAmount, String tradableIdentifier, String transactionCurrency) {
            Order order = new MarketOrder(type, tradableAmount, tradableIdentifier, transactionCurrency);
            
            return order;
        }
    }
    
    private static final class SimulationTradeExecutor implements TradeExecutor {

        private final Logger log = LoggerFactory.getLogger(SimulationTradeExecutor.class);
        
        @Override
        public void cancel(String orderId) {
            this.log.info("Cancelling orderId: " + orderId);
        }

        @Override
        public void execute(Order order) {
            this.log.info("Executing order: " + order);
        }
    }
    
    private final String user;
    private final AccountController accntController;
    private final MarketConditionsProvider marketConditionsProvider;
    private final OrderBookProvider orderBookProvider;
    private final OrderProvider orderProvider;
    private final TradeExecutor tradeExecutor;

    public SimulationBinding(String user, CurrencyUnit currency, double ammount, MarketConditionsProvider marketConditionsProvider) {
        this.user = user;
        this.accntController = new SimulationAccountController(this.user, currency, ammount);
        this.marketConditionsProvider = marketConditionsProvider;
        this.orderBookProvider = new SimulationOrderBookProvider();
        this.orderProvider = new SimulationOrderProvider();
        this.tradeExecutor = new SimulationTradeExecutor();
    }
    
    @Override
    public AccountController getAccountController(String id) {
        return this.accntController;
    }

    @Override
    public MarketConditionsProvider getMarketConditionsProvider(String id) {
        return this.marketConditionsProvider;
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
