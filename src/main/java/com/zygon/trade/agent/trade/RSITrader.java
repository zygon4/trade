
package com.zygon.trade.agent.trade;

import com.google.common.collect.Lists;
import com.xeiam.xchange.currency.Currencies;
import com.zygon.trade.market.model.indication.numeric.RSI;
import com.zygon.trade.trade.PriceObjective;
import com.zygon.trade.trade.Trade;
import com.zygon.trade.trade.TradeSignal;
import com.zygon.trade.trade.TradeSignal.Decision;
import com.zygon.trade.trade.TradeUrgency;
import com.zygon.trade.trade.VolumeObjective;
import java.util.Collection;

/**
 *
 * @author zygon
 */
public class RSITrader extends TradeGeneratorImpl {

    private static final boolean INVERTED               = false;
    
//    private static final double PROFIT_MODIFIER         = 7.5;
//    private static final double STOP_LOSS_MODIFER       = 0.5;
    
    private static final double PROFIT_MODIFIER         = 1600;
    private static final double STOP_LOSS_MODIFER       = 80;
    
    private static final double VOLUME_MODIFER          = 25;
    
    public RSITrader() {
    }

    private RSI getRelativeStrengthIndex() {
        return (RSI) this.getMarketConditions().getIndication(RSI.ID, Currencies.BTC);
    }
    
    private TradeSignal createTradeSignal() {
        
        RSI rsi = this.getRelativeStrengthIndex();
        
        Decision decision = null;
        String reason = null;
        
        if (rsi.getValue() > 70.0) {
            decision = !INVERTED ? Decision.SELL : Decision.BUY;
            reason = "above-rsi-threshold";
        } else if (rsi.getValue() < 30.0) {
            decision = !INVERTED ? Decision.BUY : Decision.SELL;
            reason = "below-rsi-threshold";
        }
        
        if (decision != null) {
            PriceObjective priceObjective = new PriceObjective(decision.getType(), PriceObjective.Modifier.PIP, PROFIT_MODIFIER, STOP_LOSS_MODIFER);
            VolumeObjective volumeObjective = new VolumeObjective(VolumeObjective.Modifier.PERCENT, VOLUME_MODIFER);
            TradeSignal tradeSignal = new TradeSignal(decision, volumeObjective, "BTC", "USD", priceObjective, TradeUrgency.MEDIUM, reason);

            return tradeSignal;
        }
        
        return null;
    }
    
    @Override
    public Collection<Trade> getTrades() {
         
        Collection<Trade> trades = Lists.newArrayList();
        
        TradeSignal tradeSignal = createTradeSignal();
        if (tradeSignal != null) {
            trades.add(new Trade(tradeSignal));
        } 
        // This is just for fun
//        else {
//            PriceObjective priceObjective = new PriceObjective(TradeSignal.Decision.BUY.getType(), PriceObjective.Modifier.PERCENT, PROFIT_MODIFIER, STOP_LOSS_MODIFER);
//            VolumeObjective volumeObjective = new VolumeObjective(VolumeObjective.Modifier.PERCENT, VOLUME_MODIFER);
//            tradeSignal = new TradeSignal(TradeSignal.Decision.BUY, volumeObjective, "BTC", "USD", priceObjective, TradeUrgency.MEDIUM, "test");
//            
//            trades.add(new Trade(tradeSignal));
//        }
        
        return trades;
    }
}
