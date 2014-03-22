/**
 * 
 */

package com.zygon.trade.trade;

/**
 *
 * @author zygon
 */
public class TradeSignal {

    public static final TradeSignal DO_NOTHING = new TradeSignal(Decision.DO_NOTHING, new VolumeObjective(VolumeObjective.Modifier.PERCENT, 0), null, null, null, null, null);
    
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
    private final VolumeObjective volumeObjective;
    private final String tradeableIdentifier;
    private final String currency;
    private final PriceObjective tradeObjectives;
    private final TradeUrgency tradeUrgency;
    private final String reason;

    public TradeSignal(Decision decision, VolumeObjective volume, String tradeableIdentifier, 
            String currency, PriceObjective objective, TradeUrgency tradeUrgency, String reason) {
        this.decision = decision;
        this.volumeObjective = volume;
        this.tradeableIdentifier = tradeableIdentifier;
        this.currency = currency;
        this.tradeObjectives = objective;
        this.tradeUrgency = tradeUrgency;
        this.reason = reason;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Decision getDecision() {
        return this.decision;
    }

    public PriceObjective getPriceObjective() {
        return this.tradeObjectives;
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

    public VolumeObjective getVolumeObjective() {
        return this.volumeObjective;
    }
    
    public boolean takeAction() {
        return this.decision != Decision.DO_NOTHING;
    }

    @Override
    public String toString() {
        return String.format("%s - %s - %s - %s - %s - %s", this.getDecision().name(), this.getVolumeObjective(), 
                this.getTradeableIdentifier(), this.getCurrency(), this.getTradeUrgency().name(), this.getReason());
    }
}
