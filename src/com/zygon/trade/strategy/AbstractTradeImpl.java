/**
 * 
 */

package com.zygon.trade.strategy;

import com.xeiam.xchange.currency.Currencies;
import com.xeiam.xchange.dto.Order;
import com.zygon.trade.execution.ExchangeException;
import com.zygon.trade.execution.ExecutionController;
import com.zygon.trade.execution.MarketConditions;
import static com.zygon.trade.strategy.TradeType.LONG;
import static com.zygon.trade.strategy.TradeType.SHORT;

/**
 * Work in progress...
 *
 * @author zygon
 */
public abstract class AbstractTradeImpl implements TradeImpl {

    private static final double MIN_TRADE_VOLUME = 0.04;
    
    // TODO: configurable
    private static final double TRADE_RISK_PERCENTAGE   = 0.01;
    
    protected double getTradeVolume(double accntBalance, double currentPrice) {
        return Math.max((accntBalance * TRADE_RISK_PERCENTAGE) / currentPrice, MIN_TRADE_VOLUME);
    }
    
    // TODO: configurable
    private static final double PROFIT_MODIFIER         = 0.100;
    private static final double STOP_LOSS_MODIFER       = 0.010;
    
    private final String name;
    private final String id;
    private final String tradeableIdentifier;
    private final ExecutionController controller;
    
    private TradeInfo tradeInfo = null;
    
    protected static final class TradeInfo {
        private final double entryPoint;
        private final double profitPoint;
        private final double stopLossPoint;
        private final double volume;
        private final TradeType type;

        public TradeInfo(double entryPoint, double profitPoint, double stopLossPoint, double volume, TradeType type) {
            this.entryPoint = entryPoint;
            this.profitPoint = profitPoint;
            this.stopLossPoint = stopLossPoint;
            this.volume = volume;
            this.type = type;
        }

        public double getEntryPoint() {
            return entryPoint;
        }

        public double getProfitPoint() {
            return profitPoint;
        }

        public double getStopLossPoint() {
            return stopLossPoint;
        }

        public TradeType getType() {
            return type;
        }

        public double getVolume() {
            return volume;
        }
    }
    
    public AbstractTradeImpl(String name, String id, String tradeableIdentifier, ExecutionController controller) {
        this.name = name;
        this.id = id;
        this.tradeableIdentifier = tradeableIdentifier;
        this.controller = controller;
    }
    
    @Override
    public void activate(MarketConditions marketConditions) throws ExchangeException {
        
        if (this.tradeInfo != null) {
            throw new IllegalStateException();
        }
        
        this.tradeInfo = this.createTradeInfo(marketConditions);
        
        // place market order - assume synchronous fill for now.
        Order order = this.controller.generateMarketOrder(this.id, this.tradeInfo.type.getOrderType(), 
                this.tradeInfo.volume, this.getTradeableIdentifier(), Currencies.USD);
        
        try {
            this.controller.placeOrder(this.id, order);
        } catch (ExchangeException ee) {
            this.reset();
            throw ee;
        }
    }

    /**
     * Attempts to cancel the active order
     * @throws ExchangeException 
     */
    @Override
    public void cancel() throws ExchangeException {
        // Using market orders for now - nothing to explicitly cancel
        
        this.reset();
    }

    @Override
    public double close(MarketConditions marketConditions) throws ExchangeException {
        
        if (this.tradeInfo == null) {
            throw new IllegalStateException();
        }
        
        double currentPrice = marketConditions.getPrice(Currencies.BTC).getValue();
        double priceMargin = 0.0;
        Order.OrderType orderType = null;
        
        switch (this.tradeInfo.type) {
            case LONG:
                orderType = Order.OrderType.ASK;
                priceMargin = currentPrice - this.tradeInfo.entryPoint;
                break;
            case SHORT:
                orderType = Order.OrderType.BID;
                priceMargin = this.tradeInfo.entryPoint - currentPrice;
                break;
        }
        
        // place market order - assume synchronous fill for now.
        Order order = this.getController().generateMarketOrder(this.getId(), orderType, this.tradeInfo.volume, this.getTradeableIdentifier(), Currencies.USD);
        
        this.getController().placeOrder(this.getId(), order);
        
        double profit = priceMargin * this.tradeInfo.volume;
        this.reset();
        
        return profit;
    }
    
    protected final ExecutionController getController() {
        return this.controller;
    }

    @Override
    public String getDisplayIdentifier() {
        return this.name;
    }

    public final String getId() {
        return this.id;
    }

    protected String getTradeableIdentifier() {
        return this.tradeableIdentifier;
    }
    
    protected final TradeInfo getTradeInfo() {
        return this.tradeInfo;
    }
    
    @Override
    public TradeMonitor getTradeMonitor() {
        // TODO:
        return new TradeMonitor();
    }
    
    protected abstract TradeInfo createTradeInfo(MarketConditions marketConditions);

    @Override
    public Signal meetsEntryConditions(MarketConditions marketConditions) {
        // other default conditions?
        return null;
    }
    
    private static final String EXIT_STOP_LOSS = "STOP_LOSS";
    private static final String EXIT_TAKE_PROFIT = "TAKE_PROFIT";
    
    @Override
    public Signal meetsExitConditions(MarketConditions marketConditions) {
        double price = marketConditions.getPrice(this.getTradeableIdentifier()).getValue();
        
        String exitSignal = null;
        
        switch (this.tradeInfo.type) {
            case LONG:
                if (price <= this.tradeInfo.stopLossPoint) {
                    exitSignal = EXIT_STOP_LOSS;
                } else if(price >= this.tradeInfo.profitPoint) {
                    exitSignal = EXIT_TAKE_PROFIT;
                }
                break;
            case SHORT:
                if (price >= this.tradeInfo.stopLossPoint) {
                    exitSignal = EXIT_STOP_LOSS;
                } else if (price <= this.tradeInfo.profitPoint) {
                    exitSignal = EXIT_TAKE_PROFIT;
                }
                break;
        }
        
        return exitSignal != null ? new Signal(exitSignal) : null;
    }
    
    protected void reset() {
        this.tradeInfo = null;
    }
}
