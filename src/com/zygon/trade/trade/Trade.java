
package com.zygon.trade.trade;

/**
 *
 * @author zygon
 */
public class Trade {
    
    private final TradeSignal[] tradeSignals;

    private State state = State.PENDING;
    private long id = -1;
    private long lastStartTime = -1;
    private long lastDuration = -1;
    
    // TODO: Order sensitivity: eg parallel execution vs in-order
    
    public static enum State {
        PENDING,
        ACTIVE,
        CLOSED
    }
    
    public Trade(TradeSignal ...tradeSignals) {
        if (tradeSignals == null || tradeSignals.length == 0) {
            throw new IllegalStateException("Trade signals must be provided.");
        }
        this.tradeSignals = tradeSignals;
    }

    public long getId() {
        return this.id;
    }

    public long getLastDuration() {
        return this.lastDuration;
    }
    
    public TradeSignal[] getTradeSignals() {
        return this.tradeSignals;
    }

    /*pkg*/ final void setId(long id) {
        this.id = id;
    }
    
    // TBD: should this state information stay here?  Should we merge with the 
    // TradeMonitor object?
    
    /*pkg*/ void notifyState (long ts, State state) {
        
        switch (this.state) {
            case ACTIVE:
                switch (state) {
                    case CLOSED:
                        // TODO:
                        break;
                    case PENDING:
                        throw new IllegalStateException("Unable to transition from " + this.state.name() + " to " + state.name());
                }
                break;
            case CLOSED:
                switch (state) {
                    case ACTIVE:
                        break;
                    case PENDING:
                        throw new IllegalStateException("Unable to transition from " + this.state.name() + " to " + state.name());
                }
                break;
            case PENDING:
                switch (state) {
                    case ACTIVE:
                        // TODO:
                        break;
                    case CLOSED:
                        throw new IllegalStateException("Unable to transition from " + this.state.name() + " to " + state.name());
                }
                break;
        }
    }
    
    /*pkg*/ void notifyClosed(long ts) {
        if (this.state != State.ACTIVE) {
            throw new IllegalStateException("Unable to transition from " + this.state.name() + " to " + State.ACTIVE);
        }
        if (ts < this.lastStartTime) {
            throw new IllegalStateException("End time cannot be less than start time");
        }
        
        this.lastDuration = ts - this.lastStartTime;
        this.lastStartTime = -1;
        this.state = State.CLOSED;
    }
    
    // Notifies the Trade's particular signal that it's open
    /*pkg*/ void notifyOpen(long ts) {
        if (this.state != State.CLOSED) {
            throw new IllegalStateException("Unable to transition from " + this.state.name() + " to " + State.ACTIVE);
        }
        this.lastStartTime = ts;
        this.lastDuration = -1;
        this.state = State.ACTIVE;
    }
}
