
package com.zygon.trade.agent.signal;

import com.xeiam.xchange.currency.Currencies;
import com.zygon.trade.trade.TradeSignal;
import com.zygon.trade.market.model.indication.Indication;
import com.zygon.trade.market.model.indication.market.Direction;
import com.zygon.trade.market.model.indication.market.Direction.MarketDirection;
import com.zygon.trade.market.model.indication.market.MACDSignalCross;
import com.zygon.trade.market.model.indication.market.MACDZeroCross;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author zygon
 */
public class MACDSignalGenerator extends SignalGeneratorImpl {

    public MACDSignalGenerator() {
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

    @Override
    protected Collection<TradeSignal> getTradeSignals() {
         
        Collection<TradeSignal> signals = new ArrayList<>();
        
        signals.add(TradeSignal.DO_NOTHING);
        
        return signals;
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
