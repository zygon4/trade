
package com.zygon.trade.agent.signal;

import com.xeiam.xchange.currency.Currencies;
import com.zygon.trade.agent.TradeSignal;
import com.zygon.trade.market.model.indication.Indication;
import com.zygon.trade.market.model.indication.market.Direction;
import com.zygon.trade.market.model.indication.market.Direction.MarketDirection;
import com.zygon.trade.market.model.indication.market.MACDSignalCross;
import com.zygon.trade.market.model.indication.market.MACDZeroCross;

/**
 *
 * @author zygon
 */
public class MACDSignalGenerator extends SignalGeneratorImpl {

    public MACDSignalGenerator() {
    }
    
    // This should probably be introduced as a higher level concept as well.
    private MarketDirection getMarketDirection(Indication indication) {
        // TBD: tradeable BS ?
        MACDZeroCross zeroCross = (MACDZeroCross) this.getMarketConditions().getIndication(MACDZeroCross.ID, Currencies.BTC);
        MACDSignalCross signalCross = (MACDSignalCross) this.getMarketConditions().getIndication(MACDSignalCross.ID, Currencies.BTC);
        
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
    protected TradeSignal doGetSignal(Indication indication) {
        MarketDirection marketDirection = this.getMarketDirection(indication);
        
        // TODO: there should be some generic market direction already happening
        // but this specific market direction impl is also valuable. Market
        // direction can/should be extendable by the signal processing.
        this.getMarketConditions().putIndication(new Direction(Currencies.BTC, System.currentTimeMillis(), marketDirection), null);
        
        // TODO: magic
        
        return TradeSignal.DO_NOTHING;
    }
    
//    private Signal meetsEntryConditions() {
//        
//        String entrySignal = null;
//
//        // play a trending market
//        MarketDirection dir = this.getMarketDirection();
//        if (dir == MarketDirection.UP) {
//            entrySignal = dir.name();
//        } else if (dir == MarketDirection.DOWN) {
//            entrySignal = dir.name();
//        }
//
//        if (entrySignal != null) {
//            entry = new Signal(entrySignal);
//        }
//        
//        return entry;
//    }
}
