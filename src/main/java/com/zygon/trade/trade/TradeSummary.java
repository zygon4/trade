/**
 * 
 */

package com.zygon.trade.trade;

/**
 *
 * This would be nice as a more robust statistics mechanism vs adhoc summaries.
 * 
 * @author zygon
 */
public class TradeSummary {

    private final String name;
    
    private int profitableTrades = 0;
    private int loosingTrades = 0;
    private double profitableTradeTotal;
    private double loosingTradeTotal;
    private double netProfit;

    public TradeSummary(String name) {
        this.name = name;
    }
    
    public synchronized void add(TradeSummary summary) {
        this.profitableTrades += summary.profitableTrades;
        this.loosingTrades += summary.loosingTrades;
        this.profitableTradeTotal += summary.profitableTradeTotal;
        this.loosingTradeTotal += summary.loosingTradeTotal;
        this.netProfit += summary.netProfit;
    }
    
    public synchronized void add(double netGain) {
        if (netGain > 0.0) {
            this.profitableTrades ++;
            this.profitableTradeTotal += netGain;
        } else if (netGain < 0.0) {
            this.loosingTrades ++;
            this.loosingTradeTotal += netGain;
        }
        
        this.netProfit += netGain;
    }
    
    public int getLoosingTrades() {
        return this.loosingTrades;
    }

    public double getLoosingTradeTotal() {
        return this.loosingTradeTotal;
    }

    public double getNetProfit() {
        return this.netProfit;
    }
    
    public int getProfitableTrades() {
        return this.profitableTrades;
    }

    public double getProfitableTradeTotal() {
        return this.profitableTradeTotal;
    }
    
    public String getSummaryStmt() {
        float winners = getProfitableTrades();
        float loosers = getLoosingTrades();
        float pc = (winners/(winners + loosers)) * 100;
        
        return String.format("%s: %f/%f: %f%% win ratio. %f net profit.", 
                this.name,
                winners, winners + loosers, pc, this.netProfit);
    }

    @Override
    public String toString() {
        return this.getSummaryStmt();
    }
}
