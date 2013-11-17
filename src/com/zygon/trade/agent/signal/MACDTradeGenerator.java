
package com.zygon.trade.agent.signal;

import com.xeiam.xchange.currency.Currencies;
import com.zygon.trade.market.model.indication.Indication;
import com.zygon.trade.market.model.indication.market.Direction;
import com.zygon.trade.market.model.indication.market.Direction.MarketDirection;
import com.zygon.trade.market.model.indication.market.MACDSignalCross;
import com.zygon.trade.market.model.indication.market.MACDZeroCross;
import com.zygon.trade.trade.PriceObjective;
import com.zygon.trade.trade.Trade;
import com.zygon.trade.trade.TradeSignal;
import com.zygon.trade.trade.TradeType;
import com.zygon.trade.trade.TradeUrgency;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author zygon
 */
public class MACDTradeGenerator extends TradeGeneratorImpl {

    private static final double PROFIT_MODIFIER         = 4.950;
    private static final double STOP_LOSS_MODIFER       = 0.2350;
    
    public MACDTradeGenerator() {
    }

    @Override
    protected void getAdditionalIndications(Collection<Indication> additional) {
        MarketDirection marketDirection = this.getMarketDirection();
        
        additional.add(new Direction(Currencies.BTC, System.currentTimeMillis(), marketDirection));
    }
    
    private Direction getDirection() {
        return (Direction) this.getMarketConditions().getIndication(Direction.ID, Currencies.BTC);
    }
    
    private MACDZeroCross getZeroCross() {
        return (MACDZeroCross) this.getMarketConditions().getIndication(MACDZeroCross.ID, Currencies.BTC);
    }
    
    private MACDSignalCross getSignalCross() {
        return (MACDSignalCross) this.getMarketConditions().getIndication(MACDSignalCross.ID, Currencies.BTC);
    }
    
    // MarketDirection should probably be introduced as a higher level concept as well.
    private MarketDirection getMarketDirection() {
        MACDZeroCross zeroCross = this.getZeroCross();
        MACDSignalCross signalCross = this.getSignalCross();
        
        MarketDirection dir = null;
        
        if (zeroCross != null && signalCross != null) {
            if (zeroCross.crossAboveZero() && signalCross.crossAboveSignal()) {
                dir = MarketDirection.UP;
            } else if (!zeroCross.crossAboveZero() && !signalCross.crossAboveSignal()) {
                dir = MarketDirection.DOWN;
            }
        }

        if (dir == null) {
            dir = MarketDirection.NEUTRAL; // maybe neutral - maybe unsure.
        }
        
        return dir;
    }
    
    private TradeSignal createTradeSignal() {
        
        MarketDirection marketDirection = this.getDirection().getMarketDirection();
        
        double volume = 0.0;
        TradeType tradeType = null;
        
        if (marketDirection == MarketDirection.UP) {
            tradeType = TradeType.LONG;
            
        } else if (marketDirection == MarketDirection.DOWN) {
            tradeType = TradeType.SHORT;
        }
        
        if (tradeType != null) {
            PriceObjective priceObjective = new PriceObjective(tradeType, PriceObjective.Modifier.PERCENT, 0.02, 0.01);
            TradeSignal tradeSignal = new TradeSignal(TradeSignal.Decision.BUY, 0.10, "BTC", "USD", priceObjective, TradeUrgency.MEDIUM, marketDirection.name());

            return tradeSignal;
        }
        
        return null;
    }
    
    static int fakeTradeId = 0;
    
    @Override
    public Collection<Trade> getTrades() {
         
        Collection<Trade> trades = new ArrayList<>();
        
        TradeSignal tradeSignal = createTradeSignal();
        if (tradeSignal != null) {
            trades.add(new Trade("trade"+(fakeTradeId++), tradeSignal));
        }
        
        return trades;
    }
}
