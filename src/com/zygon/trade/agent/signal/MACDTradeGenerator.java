
package com.zygon.trade.agent.signal;

import com.xeiam.xchange.currency.Currencies;
import com.zygon.trade.market.model.indication.Indication;
import com.zygon.trade.market.model.indication.market.Direction;
import com.zygon.trade.market.model.indication.market.Direction.MarketDirection;
import com.zygon.trade.market.model.indication.market.MACDSignalCross;
import com.zygon.trade.market.model.indication.market.MACDZeroCross;
import com.zygon.trade.trade.Trade;
import com.zygon.trade.trade.TradeSignal;
import com.zygon.trade.trade.TradeUrgency;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author zygon
 */
public class MACDTradeGenerator extends TradeGeneratorImpl {

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

//    protected TradeInfo createTradeInfo(MarketConditions marketConditions) {
//        MACDZeroCross zeroCross = (MACDZeroCross) marketConditions.getIndication(MACDZeroCross.ID, this.getTradeableIdentifier());
//        MACDSignalCross signalCross = (MACDSignalCross) marketConditions.getIndication(MACDSignalCross.ID, this.getTradeableIdentifier());
//        
//        double entryPoint = -1.0;
//        double profitPoint = -1.0;
//        double stopLossPoint = -1.0;
//        double volume = 0.0;
//        TradeType type = null;
//        
//        double currentPrice = marketConditions.getPrice(this.getTradeableIdentifier()).getValue();
//        entryPoint = currentPrice;
//        
//        if (zeroCross.crossAboveZero() && signalCross.crossAboveSignal()) {
//            type = TradeType.LONG;
//            stopLossPoint = currentPrice - STOP_LOSS_MODIFER;
//            profitPoint = currentPrice + PROFIT_MODIFIER;
//        } else if (!zeroCross.crossAboveZero() && !signalCross.crossAboveSignal()) {
//            type = TradeType.SHORT;
//            stopLossPoint = currentPrice + STOP_LOSS_MODIFER;
//            profitPoint = currentPrice - PROFIT_MODIFIER;
//        } else {
//            throw new IllegalStateException("Unable to activate in the current state");
//        }
//        
//        double accntBalance = this.getController().getBalance(this.getId(), Currencies.USD);
//        volume = getTradeVolume(accntBalance, currentPrice);
//        
//        return new TradeInfo(entryPoint, profitPoint, stopLossPoint, volume, type);
//    }
    
    @Override
    protected Collection<Trade> getTrades() {
         
        Collection<Trade> trades = new ArrayList<>();
        
        Signal entrySignal = this.meetsEntryConditions();
        if (entrySignal != null) {
//            TradeSignal tradeSignal = new TradeSignal(TradeSignal.Decision.BUY, volume, null, null, TradeUrgency.MEDIUM, null)
//            trades.add(new Trade(entrySignal.toString(), new TradeSignal));
        }
        
        return Collections.EMPTY_LIST;
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
