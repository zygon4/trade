/**
 * 
 */

package com.zygon.trade.strategy.impl;

import com.zygon.trade.strategy.TradeType;

/**
 *
 * @author zygon
 */
public class TradeSignal {

    public static final TradeSignal DO_NOTHING = new TradeSignal(Decision.DO_NOTHING, 0.0, null, null, null, null);
    
    public static enum Decision {
        BUY (TradeType.LONG),
        SELL (TradeType.SHORT),
        DO_NOTHING (null);

        private final TradeType type;

        private Decision(TradeType type) {
            this.type = type;
        }
        
        public final TradeType getType() {
            return this.type;
        }
    }
    
    private final Decision decision;
    private final double volume;
    private final String tradeableIdentifier;
    private final String currency;
    private final TradeUrgency tradeUrgency;
    private final String reason;

    public TradeSignal(Decision decision, double volume, String tradeableIdentifier, String currency, TradeUrgency tradeUrgency, String reason) {
        this.decision = decision;
        this.volume = volume;
        this.tradeableIdentifier = tradeableIdentifier;
        this.currency = currency;
        this.tradeUrgency = tradeUrgency;
        this.reason = reason;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Decision getDecision() {
        return this.decision;
    }

    public String getReason() {
        return this.reason;
    }

    public String getTradeableIdentifier() {
        return this.tradeableIdentifier;
    }
    
    public TradeUrgency getTradeUrgency() {
        return this.tradeUrgency;
    }

    public TradeType getTradeType() {
        return this.decision.getType();
    }

    public double getVolume() {
        return this.volume;
    }
    
    public boolean takeAction() {
        return this.decision != Decision.DO_NOTHING;
    }

    @Override
    public String toString() {
        return String.format("%s - %s - %s - %s - %s - %s", this.getDecision().name(), this.getVolume(), 
                this.getTradeableIdentifier(), this.getCurrency(), this.getTradeUrgency().name(), this.getReason());
    }
}
