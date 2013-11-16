
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
    
//    private PriceObjective generatePriceObjective(double currentPrice, MACDZeroCross zeroCross, MACDSignalCross signalCross) {
//        
//        double profitPoint = -1.0;
//        double stopLossPoint = -1.0;
//        
//        if (zeroCross.crossAboveZero() && signalCross.crossAboveSignal()) {
//            stopLossPoint = currentPrice - STOP_LOSS_MODIFER;
//            profitPoint = currentPrice + PROFIT_MODIFIER;
//        } else if (!zeroCross.crossAboveZero() && !signalCross.crossAboveSignal()) {
//            stopLossPoint = currentPrice + STOP_LOSS_MODIFER;
//            profitPoint = currentPrice - PROFIT_MODIFIER;
//        } else {
//            throw new IllegalStateException("Unable to activate in the current state");
//        }
//        
//        return new PriceObjective(profitPoint, stopLossPoint);
//    }
    
    static int fakeTradeId = 0;
    
    @Override
    public Collection<Trade> getTrades() {
         
        Collection<Trade> trades = new ArrayList<>();
        
        Signal entrySignal = this.meetsEntryConditions();
        if (entrySignal != null) {
            PriceObjective priceObjective = new PriceObjective(500.0, 400.0);
            TradeSignal tradeSignal = new TradeSignal(TradeSignal.Decision.BUY, 1.0, "BTC", "USD", priceObjective, TradeUrgency.MEDIUM, "'cause");
            trades.add(new Trade("trade"+(fakeTradeId++), tradeSignal));
        }
        
        return trades;
    }
    
    private Signal meetsEntryConditions() {
        
        String entrySignal = null;

        // play a trending market        
        MarketDirection dir = this.getDirection().getMarketDirection();
        
        if (dir == MarketDirection.UP) {
            entrySignal = dir.name();
        } else if (dir == MarketDirection.DOWN) {
            entrySignal = dir.name();
        }
        
        Signal signal = null;
        if (entrySignal != null) {
            signal = new Signal(entrySignal);
        }
        
        return signal;
    }
}
