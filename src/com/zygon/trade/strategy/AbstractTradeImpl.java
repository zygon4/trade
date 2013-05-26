/**
 * 
 */

package com.zygon.trade.strategy;

import com.xeiam.xchange.Currencies;
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

    // TODO: configurable
    private static final double TRADE_RISK_PERCENTAGE   = 0.10; // 10 percent for now to keep things interesting while testing
    
    protected double getTradeVolume(double accntBalance, double currentPrice) {
        return (accntBalance * TRADE_RISK_PERCENTAGE) / currentPrice;
    }
    
    // TODO: configurable
    private static final double PROFIT_MODIFIER         = 0.100;
    private static final double STOP_LOSS_MODIFER       = 0.010;
    
    private final String name;
    private final String id;
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
    
    public AbstractTradeImpl(String name, String id, ExecutionController controller) {
        this.name = name;
        this.id = id;
        this.controller = controller;
    }
    
    @Override
    public void activate(MarketConditions marketConditions) throws ExchangeException {
        
        if (this.tradeInfo != null) {
            throw new IllegalStateException();
        }
        
        this.tradeInfo = this.createTradeInfo(marketConditions);
        
        Order order = this.controller.generateOrder(this.id, this.tradeInfo.type.getOrderType(), 
                this.tradeInfo.volume, marketConditions.getTradeableIdentifier(), Currencies.USD);
        
        try {
            this.controller.placeOrder(this.id, order);
        } catch (ExchangeException ee) {
            this.reset();
            throw ee;
        }
        
        // place order - assume synchronous fill for now.
    }

    /**
     * Attempts to cancel the active order.  If this throws an exception no
     * attempt is made to clean up the state - users are required to attempt
     * a second cancel or close.
     * @throws ExchangeException 
     */
    @Override
    public void cancel() throws ExchangeException {
        if (this.tradeInfo == null) {
            throw new IllegalStateException();
        }
        
        this.controller.cancelOrder(this.id, "TODO: order id");
        
        this.reset();
    }

    @Override
    public double close(MarketConditions marketConditions) throws ExchangeException {
        
        double currentPrice = marketConditions.getPrice().getValue();
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
        
        Order order = this.getController().generateOrder(this.getId(), orderType, this.tradeInfo.volume, marketConditions.getTradeableIdentifier(), Currencies.USD);
        
        try {
            this.getController().placeOrder(this.getId(), order);
        } catch (ExchangeException ee) {
            this.reset();
            throw ee;
        }
        
        // place order - assume synchronous fill for now.
        
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
    public boolean meetsEntryConditions(MarketConditions marketConditions) {
        // other default conditions?
        return false;
    }
    
    @Override
    public boolean meetsExitConditions(MarketConditions marketConditions) {
        double price = marketConditions.getPrice().getValue();
        
        switch (this.tradeInfo.type) {
            case LONG:
                if (price <= this.tradeInfo.stopLossPoint || price >= this.tradeInfo.profitPoint) {
                    return true;
                }
                break;
            case SHORT:
                if (price >= this.tradeInfo.stopLossPoint || price <= this.tradeInfo.profitPoint) {
                    return true;
                }
                break;
        }
        
        return false;
    }
    
    protected void reset() {
        this.tradeInfo = null;
    }
}
