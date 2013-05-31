/**
 * 
 */

package com.zygon.trade.execution.simulation;

import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.dto.trade.MarketOrder;
import com.xeiam.xchange.dto.trade.Wallet;
import com.zygon.trade.execution.ExecutionController;
import com.zygon.trade.execution.MarketConditions;
import com.zygon.trade.execution.OrderBookProvider;
import com.zygon.trade.execution.OrderProvider;
import com.zygon.trade.execution.TradeExecutor;
import com.zygon.trade.execution.AccountController;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zygon
 */
public class SimulationBinding implements ExecutionController.Binding {
    
    private static final double EXCHANGE_FEE = 0.006; // percentage
    
    private static class WalletInfo {
        private double ammount = 0.0;
        private final String currency;
        private double high = 0.0;
        private double low = 0.0;

        public WalletInfo(Wallet wallet) {
            this.currency = wallet.getCurrency();
            this.ammount = wallet.getBalance().getAmount().doubleValue();
            this.high = this.ammount;
            this.low = this.ammount;
        }

        public double getAmmount() {
            return ammount;
        }

        public String getCurrency() {
            return currency;
        }

        public double getHigh() {
            return high;
        }

        public double getLow() {
            return low;
        }
        
        private Wallet getWallet() {
            return new Wallet(this.currency, BigMoney.of(CurrencyUnit.getInstance(this.currency), this.ammount));
        }
    }
    
    private static final class SimulationAccountController implements AccountController {

        private Map<CurrencyUnit, WalletInfo> walletsByCurrency = new HashMap<>();
        private final String user;
        private double fees = 0.0;
        
        public SimulationAccountController(String user, Wallet ...wallets) {
            this.user = user;
            
            for (Wallet wallet : wallets) {
                this.walletsByCurrency.put(CurrencyUnit.getInstance(wallet.getCurrency()), new WalletInfo(wallet));
            }
        }
        
        public synchronized void addFee(BigDecimal fee) {
            this.fees += fee.doubleValue();
        }
        
        public synchronized void add(BigDecimal ammount, CurrencyUnit currency) {
            WalletInfo wallet = this.walletsByCurrency.get(currency);
            
            wallet.ammount += ammount.doubleValue();
            wallet.high = Math.max(wallet.getHigh(), wallet.getAmmount());
        }
        
        public synchronized void subtract(BigDecimal ammount, CurrencyUnit currency) {
            WalletInfo wallet = this.walletsByCurrency.get(currency);
            
            wallet.ammount -= ammount.doubleValue();
            wallet.low = Math.min(wallet.getLow(), wallet.getAmmount());
        }
        
        @Override
        public AccountInfo getAccountInfo(String username) {
            List<Wallet> wallets = new ArrayList<>();
            for (WalletInfo info : this.walletsByCurrency.values()) {
                wallets.add(info.getWallet());
            }
            
            return new AccountInfo(this.user, wallets);
        }

        public double getBalance(CurrencyUnit currency) {
            WalletInfo wallet = this.walletsByCurrency.get(currency);
            return wallet.getAmmount();
        }

        public Set<CurrencyUnit> getCurrencies() {
            return this.walletsByCurrency.keySet();
        }
        
        public double getFees() {
            return this.fees;
        }
        
        public double getMaxDrawDown(CurrencyUnit currency) {
            WalletInfo wallet = this.walletsByCurrency.get(currency);
            return wallet.getLow();
        }
        
        public double getMaxProfit(CurrencyUnit currency) {
            WalletInfo wallet = this.walletsByCurrency.get(currency);
            return wallet.getHigh();
        }
    }
    
    private static final class SimulationOrderBookProvider implements OrderBookProvider {

        private final List<LimitOrder> orders = new ArrayList<>();
        
        @Override
        public void getOpenOrders(List<LimitOrder> orders) {
            orders.addAll(this.orders);
        }

        @Override
        public void getOrderBook(String username, OrderBook orders, String tradeableIdentifer, String currency) {
            
        }
    }
    
    private static final class SimulationOrderProvider implements OrderProvider {

        @Override
        public LimitOrder getLimitOrder(Order.OrderType type, double tradableAmount, String tradableIdentifier, String transactionCurrency, double limitPrice) {
            return new LimitOrder(type, BigDecimal.valueOf(tradableAmount), tradableIdentifier, transactionCurrency, BigMoney.of(CurrencyUnit.USD, limitPrice));
        }

        @Override
        public MarketOrder getMarketOrder(Order.OrderType type, double tradableAmount, String tradableIdentifier, String transactionCurrency) {
            return new MarketOrder(type, BigDecimal.valueOf(tradableAmount), tradableIdentifier, transactionCurrency);
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
        public void cancel(String username, String orderId) {
            this.log.info("Cancelling orderId: {}", orderId);
        }

        @Override
        public String execute(String username, Order order) {
            BigDecimal marketPrice = this.marketConditions.getPrice().value();
            
            this.log.info("Executing order: {} at price {}", order, marketPrice);
            
            // Because this is a market order we're just estimating what the market price might be.
            BigDecimal amount = order.getTradableAmount().multiply(marketPrice);
            
            BigDecimal fee = amount.multiply(BigDecimal.valueOf(EXCHANGE_FEE));
            this.accntController.addFee(fee);
            
            amount = amount.subtract(fee);
            
            // simulate a buy by adding the tradable and subtracting the transaction currency
            if (order.getType() == Order.OrderType.BID) {
                this.accntController.add(order.getTradableAmount(), CurrencyUnit.of(order.getTradableIdentifier()));
                this.accntController.subtract(amount, CurrencyUnit.of(order.getTransactionCurrency()));
            } else {
                // simulate a sell by subtracting the tradable and adding the transaction currency
                this.accntController.subtract(order.getTradableAmount(), CurrencyUnit.of(order.getTradableIdentifier()));
                this.accntController.add(amount, CurrencyUnit.of(order.getTransactionCurrency()));
            }
            
            for (CurrencyUnit unit : this.accntController.getCurrencies()) {
                this.log.info("Account balance {}, high {}, low {}", 
                        this.accntController.getBalance(unit), this.accntController.getMaxProfit(unit), 
                        this.accntController.getMaxDrawDown(unit), this.accntController.getFees());
            }
            this.log.info("Total fees {}", this.accntController.getFees());
            
            return "orderid";
        }
    }
    
    private final String user;
    private final SimulationAccountController accntController;
    private final MarketConditions marketConditions;
    private final OrderBookProvider orderBookProvider;
    private final OrderProvider orderProvider;
    private final TradeExecutor tradeExecutor;

    public SimulationBinding(String username, Wallet[] wallets, MarketConditions marketConditions) {
        this.user = username;
        this.accntController = new SimulationAccountController(this.user, wallets);
        this.marketConditions = marketConditions;
        this.orderBookProvider = new SimulationOrderBookProvider();
        this.orderProvider = new SimulationOrderProvider();
        this.tradeExecutor = new SimulationTradeExecutor(this.accntController, this.marketConditions);
    }
    
    @Override
    public AccountController getAccountController() {
        return this.accntController;
    }

    @Override
    public OrderBookProvider getOrderBookProvider() {
        return this.orderBookProvider;
    }

    @Override
    public OrderProvider getOrderProvider() {
        return this.orderProvider;
    }

    @Override
    public TradeExecutor getTradeExecutor() {
        return this.tradeExecutor;
    }
}
