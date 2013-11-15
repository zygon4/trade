/**
 * 
 */

package com.zygon.trade.strategy;

import com.zygon.trade.trade.Signal;
import com.zygon.trade.trade.TradeSummary;
import com.zygon.trade.trade.TradePostMortem;
import com.zygon.trade.trade.TradeMonitor;
import com.zygon.trade.execution.ExchangeException;
import com.zygon.trade.execution.MarketConditions;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zygon
 * @version 1.0
 */
@Deprecated
public final class Trade {

    
    // NOTE: prototypical
    private static final int MAX_TRADE_COUNT = 4; // duration unspecified here
    private volatile int tradeCount = 0;
    private final ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
    {
        exec.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        tradeCount = 0; // reset
                    }
                }, 
                1, 1, TimeUnit.HOURS);
    }
    
    
    // This tenatively/cautiously ordinal in terms of trade progression.
    public static enum TradeState {
        OPEN,
        ACTIVE,
        CLOSED
    }
    
    private final Logger logger = LoggerFactory.getLogger(Trade.class);
    private final ReentrantReadWriteLock tradeStateLock = new ReentrantReadWriteLock();
    private final TradeSummary tradeSummary;
    
    private final TradeImpl strategy;
    
    private MarketConditions marketConditions;
    private TradeState tradeState = TradeState.OPEN;
    private Signal entrySignal;
    private Signal exitSignal;
    private long entryTimestamp;
    private long exitTimestamp;
    private double closingProfit;

    public Trade(TradeImpl strategy) {
        this.strategy = strategy;
        this.tradeSummary = new TradeSummary(this.strategy.getDisplayIdentifier());
    }
    
    public final void activateTrade() {
        
        Lock writeLock = this.tradeStateLock.writeLock();
        try {
            writeLock.lock();
            
            checkState(TradeState.OPEN);
            
            this.logger.info("Activating trade {}", this.strategy.getDisplayIdentifier());
            
            this.strategy.activate(this.marketConditions);
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
        
        if (this.tradeCount < MAX_TRADE_COUNT) {
            this.entrySignal = this.strategy.meetsEntryConditions(marketConditions);
            boolean canActivate = this.entrySignal != null;
            
            if (canActivate) {
                tradeCount ++;
            }
            
            return canActivate;
        }
        
        return false;
    }
    
    public final boolean canCancel() {
        // Will throw an exception in your face if you didn't check for state
        // being ACTIVE.
        checkState(TradeState.ACTIVE);
        
        // TODO: cancel reasons such as the trade being open for too long.
        
        return false;
    }
    
    /**
     * Immediately attempts to cancel the open trade.
     */
    public final void cancel() {
        Lock writeLock = this.tradeStateLock.writeLock();
        try {
            writeLock.lock();
            
            this.logger.info("Cancelling trade {}", this.strategy.getDisplayIdentifier());
            
            if (this.getTradeState() == TradeState.ACTIVE) {
                this.strategy.cancel();
            }
            
            this.exitTimestamp = System.currentTimeMillis();
            this.tradeState = TradeState.CLOSED;
            
            this.logger.info("Cancelled at {}", new Date(this.exitTimestamp));
        } catch (ExchangeException ee) {
            this.logger.error("Caught exception while cancelling", ee);
        } finally {
            writeLock.unlock();
        }
    }
    
    public final boolean canClose() {
        // Will throw an exception in your face if you didn't check for state
        // being ACTIVE.
        checkState(TradeState.ACTIVE);
        
        this.exitSignal = this.strategy.meetsExitConditions(marketConditions);
        
        return this.exitSignal != null;
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
            
            this.logger.info("Closing trade {}", this.strategy.getDisplayIdentifier());
            
            this.closingProfit = this.strategy.close(marketConditions);
            this.exitTimestamp = System.currentTimeMillis();
            this.tradeState = TradeState.CLOSED;
            
            this.tradeSummary.add(this.closingProfit);
            
            this.logger.info("Closed at {}", new Date(this.exitTimestamp));
        } catch (ExchangeException ee) {
            this.logger.error("Caught exception while closing", ee);
            
            // Tactical decision to cancel the active trade.
            this.cancel();
            
        } finally {
            writeLock.unlock();
        }
    }
    
    public String getDisplayIdentifier() {
        return this.strategy.getDisplayIdentifier();
    }
    
    public final TradeMonitor getMonitor() {
        checkState(TradeState.ACTIVE);
        return this.strategy.getTradeMonitor();
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

    public final TradeSummary getTradeSummary() {
        return this.tradeSummary;
    }
    
    public String getTradeStatusString() {
        
        // Read lock to view the entire status atomically
        this.tradeStateLock.readLock().lock();
        try {
            StringBuilder sb = new StringBuilder();
            
            sb.append(this.strategy.getDisplayIdentifier()).append(" - ");
            TradeState state = this.getTradeState();
            sb.append(state.name());
            switch (state) {
                case ACTIVE:
                    sb.append(" - ").append(this.entryTimestamp).append('-');
                    sb.append(this.entrySignal);
                    break;
                case CLOSED:
                    sb.append(" - ").append(this.exitTimestamp).append('-');
                    sb.append(this.exitSignal);
                    break;
            }
        
            return sb.toString();
        } finally {
            this.tradeStateLock.readLock().unlock();
        }
    }
    
    /**
     * Returns the Trade back to OPEN.  The trade must be already CLOSED.
     */
    public final TradePostMortem reset() {
        Lock writeLock = this.tradeStateLock.writeLock();
        try {
            writeLock.lock();
        
            checkState(TradeState.CLOSED);
            
            TradePostMortem postMortem = new TradePostMortem(this.entrySignal, this.exitSignal, 
                    this.exitTimestamp - this.entryTimestamp, this.closingProfit);
            
            this.entrySignal = null;
            this.exitSignal = null;
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
    
    /*pkg*/ final void set(MarketConditions marketConditions) {
        if (this.marketConditions != null) {
            // I don't like mutable data.. this should be set at-most-once.
            throw new IllegalStateException();
        }
        
        this.marketConditions = marketConditions;
    }

    @Override
    public String toString() {
        return this.getTradeStatusString();
    }
}
