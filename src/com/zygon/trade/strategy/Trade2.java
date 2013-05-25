/**
 * 
 */

package com.zygon.trade.strategy;

import com.zygon.trade.execution.ExchangeException;
import com.zygon.trade.execution.MarketConditions;
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
    
    private final MarketConditions marketConditions;
    private final TradeImpl impl;
    
    private TradeState tradeState = TradeState.OPEN;
    private long entryTimestamp;
    private long exitTimestamp;
    private double closingProfit;

    public Trade2(MarketConditions marketConditions, TradeImpl helper) {
        this.marketConditions = marketConditions;
        this.impl = helper;
    }
    
    public final void activateTrade() {
        
        Lock writeLock = this.tradeStateLock.writeLock();
        try {
            writeLock.lock();
            
            checkState(TradeState.OPEN);
            
            this.logger.info("Activating trade");
            
            this.impl.activate(this.marketConditions);
            this.entryTimestamp = System.currentTimeMillis();
            this.tradeState = TradeState.ACTIVE;
            
            this.logger.info("Activated at {}", new Date(this.entryTimestamp));
        } catch (ExchangeException ee) {
            this.logger.error("Caught exception while activating", ee);
        } finally {
            writeLock.unlock();
        }
    }
    
    public final boolean canActivate() {
        // Will throw an exception in your face if you didn't check for state
        // being OPEN.
        checkState(TradeState.OPEN);
        
        return this.impl.meetsEntryConditions(marketConditions);
    }
    
    public final boolean canClose() {
        // Will throw an exception in your face if you didn't check for state
        // being ACTIVE.
        checkState(TradeState.ACTIVE);
        
        return this.impl.meetsExitConditions(marketConditions);
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
    
    public final void closeTrade() {
        
        Lock writeLock = this.tradeStateLock.writeLock();
        try {
            writeLock.lock();
        
            checkState(TradeState.ACTIVE);
            
            this.logger.info("Closing trade");
            
            this.closingProfit = this.impl.close(marketConditions);
            this.exitTimestamp = System.currentTimeMillis();
            this.tradeState = TradeState.CLOSED;
            
            this.logger.info("Closed at {}", new Date(this.exitTimestamp));
        } catch (ExchangeException ee) {
            this.logger.error("Caught exception while closing", ee);
        } finally {
            writeLock.unlock();
        }
    }
    
    public final TradeMonitor getMonitor() {
        checkState(TradeState.ACTIVE);
        return this.impl.getTradeMonitor();
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
    
    /**
     * Returns the Trade back to OPEN.  The trade must be already CLOSED.
     */
    public final TradePostMortem reset() {
        Lock writeLock = this.tradeStateLock.writeLock();
        try {
            writeLock.lock();
        
            checkState(TradeState.CLOSED);
            
            TradePostMortem postMortem = new TradePostMortem(this.exitTimestamp - this.entryTimestamp, this.closingProfit);
            
            this.entryTimestamp = -1;
            this.exitTimestamp = -1;
            this.closingProfit = -1;
            this.tradeState = TradeState.OPEN;
            
            this.logger.debug("Reset at {}", new Date(this.exitTimestamp));
            
            
            
            return postMortem;
        } finally {
            writeLock.unlock();
        }
    }
}
