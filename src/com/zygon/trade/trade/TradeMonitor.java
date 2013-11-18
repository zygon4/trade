/**
 * 
 */

package com.zygon.trade.trade;

import static com.zygon.trade.trade.TradeType.LONG;
import static com.zygon.trade.trade.TradeType.SHORT;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Skeleton right now.. to be used to keep a watchful eye on how the active
 * trades are.
 * 
 *  TBD: should this monitor aspect merge with the actual Trade object? Why
 * have two objects?
 *
 * @author zygon
 */
public class TradeMonitor {
    
    // TBD: "valid" flag to state that the monitor is active on a trade?

    private long startTime = 0;
    private long exitTime = 0;
    private final Map<String, Double> orderVolumes = new HashMap<String, Double>();
    private final Map<String, TradeSignal> signals = new HashMap<String, TradeSignal>();
    private final Set<String> completedSignals = new HashSet<String>();
    private double enterPrice;
    private String tradeId;
    
    private volatile double profit = 0.0;
    
    private double calculateClosingProfit (TradeType type, double entryPrice, double exitPrice, double volume) {
        
        double priceMargin = 0.0;
        
        switch (type) {
            case LONG:
                priceMargin = exitPrice - entryPrice;
                break;
            case SHORT:
                priceMargin = entryPrice - exitPrice;
                break;
        }
        
        return priceMargin * volume;
    }
    
    public void close(long time, double price) {
        this.exitTime = time;
        
        double totalProfit = 0.0;
        
        for (String id : this.signals.keySet()) {
            TradeSignal signal = this.signals.get(id);
            double volume = this.orderVolumes.get(id);
            totalProfit += calculateClosingProfit(signal.getTradeType(), this.enterPrice, price, volume);
        }
        
        this.profit += totalProfit;
    }
    
    public final double getProfit() {
        return this.profit;
    }

    public final long getDuration() {
        return this.exitTime - this.startTime;
    }

    public double getEnterPrice() {
        return this.enterPrice;
    }

    public double getEnterVolume(String orderId) {
        return this.orderVolumes.get(orderId);
    }

    public TradeSignal getSignal(String id) {
        return this.signals.get(id);
    }

    public String getTradeId() {
        return this.tradeId;
    }

    public Map<String, TradeSignal> getSignals() {
        return this.signals;
    }
    
    public boolean hasOpenSignals() {
        return this.completedSignals.size() != this.signals.size();
    }
    
    public void notifyComplete (String id) {
        if (this.signals.containsKey(id)) {
            this.completedSignals.add(id);
        }
    }

    public void open (String tradeId, long time, double price) {
        this.tradeId = tradeId;
        this.startTime = time;
        this.enterPrice = price;
    }
    
    public void addSignal(String id, TradeSignal signal, double volume) {
        this.signals.put(id, signal);
        this.orderVolumes.put(id, volume);
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }
}
