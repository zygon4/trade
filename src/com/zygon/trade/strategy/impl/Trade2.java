/**
 * 
 */

package com.zygon.trade.strategy.impl;

import com.zygon.trade.agent.TradeSignal;
import com.xeiam.xchange.dto.Order;
import com.zygon.trade.execution.ExchangeException;
import com.zygon.trade.execution.ExecutionController;
import com.zygon.trade.execution.MarketConditions;
import com.zygon.trade.strategy.TradeMonitor;
import com.zygon.trade.strategy.TradePostMortem;
import com.zygon.trade.strategy.TradeSummary;
import static com.zygon.trade.strategy.TradeType.LONG;
import static com.zygon.trade.strategy.TradeType.SHORT;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zygon
 * @version 1.0
 */
public final class Trade2 {

    // This tenatively/cautiously ordinal in terms of trade progression.
    public static enum TradeState {
        OPEN,
        ACTIVE,
        CLOSED
    }
    
    private final Logger logger = LoggerFactory.getLogger(Trade2.class);
    private final ReentrantReadWriteLock tradeStateLock = new ReentrantReadWriteLock();
    private final TradeSummary tradeSummary;
    private final String id;
    private final String tradeableIdentifier;
    private final Strategy strategy;
    private final ExecutionController controller;
    
    private TradeState tradeState = TradeState.OPEN;
    private long entryTimestamp;
    private long exitTimestamp;
    private double enterMarketPrice;
    
    public Trade2(String id, String tradeableIdentifier, Strategy strat, ExecutionController controller) {
        this.id = id;
        this.tradeableIdentifier = tradeableIdentifier;
        this.strategy = strat;
        this.controller = controller;
        this.tradeSummary = new TradeSummary(this.id);
    }
    
    public final void enterMarket(MarketConditions marketConditions) {
        
        Lock writeLock = this.tradeStateLock.writeLock();
        try {
            writeLock.lock();
            
            checkState(TradeState.OPEN);
            
            TradeSignal entrySignal = this.strategy.getEntrySignal(marketConditions);
            
            this.logger.info("Entering market based on signal {}", entrySignal);
            
            // consider letting the user enter the market
            if (entrySignal.getDecision() != TradeSignal.Decision.DO_NOTHING) {
                this.enterMarketPrice = marketConditions.getPrice(this.tradeableIdentifier).getValue();
                
                // place market order - assume synchronous fill for now.
                Order order = this.controller.generateMarketOrder(this.id, entrySignal.getTradeType().getOrderType(), 
                        entrySignal.getVolume(), entrySignal.getTradeableIdentifier(), entrySignal.getCurrency());

                this.controller.placeOrder(this.id, order);
            }
            
            this.entryTimestamp = System.currentTimeMillis();
            this.tradeState = TradeState.ACTIVE;
            
            this.logger.info("Entered market at {}", new Date(this.entryTimestamp));
        } catch (ExchangeException ee) {
            this.logger.error("Caught exception while entering market", ee);
            this.reset();
        } finally {
            writeLock.unlock();
        }
    }
    
    public final boolean canEnterMarket(MarketConditions marketConditions) {
        // Will throw an exception in your face if you didn't check for state
        // being OPEN.
        checkState(TradeState.OPEN);
        
        return this.strategy.getEntrySignal(marketConditions).takeAction();
    }
    
    // WORRY ABOUT CANCEL LATER
    
//    public final boolean canCancel() {
//        // Will throw an exception in your face if you didn't check for state
//        // being ACTIVE.
//        checkState(TradeState.ACTIVE);
//        
//        // TODO: cancel reasons such as the trade being open for too long.
//        
//        return false;
//    }
//    
//    /**
//     * Immediately attempts to cancel the open trade.
//     */
//    public final void cancel() {
//        Lock writeLock = this.tradeStateLock.writeLock();
//        try {
//            writeLock.lock();
//            
//            this.logger.info("Cancelling trade {}", this.strategy.getDisplayIdentifier());
//            
//            if (this.getTradeState() == TradeState.ACTIVE) {
//                this.strategy.cancel();
//            }
//            
//            this.exitTimestamp = System.currentTimeMillis();
//            this.tradeState = TradeState.CLOSED;
//            
//            this.logger.info("Cancelled at {}", new Date(this.exitTimestamp));
//        } catch (ExchangeException ee) {
//            this.logger.error("Caught exception while cancelling", ee);
//        } finally {
//            writeLock.unlock();
//        }
//    }
    
    public final boolean canExitMarket(MarketConditions marketConditions) {
        // Will throw an exception in your face if you didn't check for state
        // being ACTIVE.
        checkState(TradeState.ACTIVE);
        
        return this.strategy.getExitSignal(marketConditions).takeAction();
    }
    
    private void checkState(TradeState desiredState) {
        this.tradeStateLock.readLock().lock();
        try {
            if (this.tradeState != desiredState) {
                throw new IllegalStateException(
                        String.format("Current state [%s] cannot transition to [%s]", 
                        this.tradeState.name(), desiredState.name()));
            }
        } finally {
            this.tradeStateLock.readLock().unlock();
        }
    }
    
    public final TradePostMortem exitMarket(MarketConditions marketConditions) {
        
        Lock writeLock = this.tradeStateLock.writeLock();
        try {
            writeLock.lock();
        
            checkState(TradeState.ACTIVE);
            
            TradeSignal exitSignal = this.strategy.getExitSignal(marketConditions);
            
            this.logger.info("Closing trade based on signal {}", exitSignal);
            
            double currentPrice = marketConditions.getPrice(this.tradeableIdentifier).getValue();
            double priceMargin = 0.0;
            
            switch (exitSignal.getTradeType()) {
                case LONG:
                    priceMargin = currentPrice - this.enterMarketPrice;
                    break;
                case SHORT:
                    priceMargin = this.enterMarketPrice - currentPrice;
                    break;
            }
            
            // place market order - assume synchronous fill for now.
            // consider placing in the user's realm
            Order order = this.controller.generateMarketOrder(this.id, exitSignal.getTradeType().getOrderType(), 
                    exitSignal.getVolume(), exitSignal.getTradeableIdentifier(), exitSignal.getCurrency());
            this.controller.placeOrder(this.id, order);
            
            this.logger.info("Closed at {}", new Date(this.exitTimestamp));
            
            double profit = priceMargin * exitSignal.getVolume();
            
            this.exitTimestamp = System.currentTimeMillis();
            this.tradeState = TradeState.OPEN;
            
            this.tradeSummary.add(profit);
            
            TradePostMortem postMortem = new TradePostMortem(null, null,
                    this.exitTimestamp - this.entryTimestamp, profit);
            
            this.reset();
            
            return postMortem;
            
        } catch (ExchangeException ee) {
            this.logger.error("Caught exception while closing", ee);
            this.reset();
            
            // Tactical decision to cancel the active trade.
            // TBD
//            this.cancel();
            
        } finally {
            writeLock.unlock();
        }
        
        return null;
    }
    
    public String getDisplayIdentifier() {
        return this.strategy.getDisplayIdentifier();
    }
    
    public final TradeMonitor getMonitor() {
        checkState(TradeState.ACTIVE);
        return new TradeMonitor();
    }
    
    public final TradeState getTradeState() {
        
        TradeState state = null;
        
        // Read lock required here?
        this.tradeStateLock.readLock().lock();
        try {
            state = this.tradeState;
        } finally {
            this.tradeStateLock.readLock().unlock();
        }
        
        return state;
    }

    public TradeSummary getTradeSummary() {
        return this.tradeSummary;
    }
    
    /**
     * Returns the Trade back to OPEN.
     */
    public final void reset() {
        Lock writeLock = this.tradeStateLock.writeLock();
        try {
            writeLock.lock();
        
            this.entryTimestamp = -1;
            this.exitTimestamp = -1;
            this.enterMarketPrice = -1.0;
            this.tradeState = TradeState.OPEN;
            
            this.logger.debug("Reset at {}", new Date(this.exitTimestamp));
            
        } finally {
            writeLock.unlock();
        }
    }
}
