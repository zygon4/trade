/**
 * 
 */

package com.zygon.trade.market.data;

import com.zygon.trade.market.Message;
import java.math.BigDecimal;
import java.util.Date;
import org.joda.money.BigMoney;

/**
 *
 * @author zygon
 */
public class Ticker extends Message {
    
    private TradeableIndex idx;
    
    private String currency;
    private BigMoney last;
    private BigMoney bid;
    private BigMoney ask;
    private BigMoney high;
    private BigMoney low;
    private BigDecimal volume;
    
    public Ticker(TradeableIndex idx, String currency, BigMoney last, BigMoney bid, 
            BigMoney ask, BigMoney high, BigMoney low, BigDecimal volume) {
        this.idx = idx;
        this.currency = currency;
        this.last = last;
        this.bid = bid;
        this.ask = ask;
        this.high = high;
        this.low = low;
        this.volume = volume;
    }
    
    private static TradeableIndex create(com.xeiam.xchange.dto.marketdata.Ticker tick) {
        TradeableIndex idx = new TradeableIndex(tick.getTradableIdentifier(), tick.getTimestamp().getTime());
        return idx;
    }
    
    public Ticker (com.xeiam.xchange.dto.marketdata.Ticker tick, String currency) {
        this (create(tick), currency, tick.getLast(), tick.getBid(), tick.getAsk(), 
                tick.getHigh(), tick.getLow(), tick.getVolume());
    }

    public Ticker() {
    }

    public BigMoney getAsk() {
        return this.ask;
    }

    public BigMoney getBid() {
        return this.bid;
    }
    
    public String getCurrency() {
        return this.currency;
    }

    public BigMoney getHigh() {
        return this.high;
    }

    public BigMoney getLast() {
        return this.last;
    }

    public BigMoney getLow() {
        return this.low;
    }

    public String getTradableIdentifier() {
        return this.idx.getIdentifer();
    }

    public String getSource() {
        return this.idx.getSource();
    }
    
    public BigDecimal getVolume() {
        return this.volume;
    }

    public long getTimestamp() {
        return this.idx.getTs();
    }

    public void setAsk(BigMoney ask) {
        this.ask = ask;
    }

    public void setBid(BigMoney bid) {
        this.bid = bid;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public void setHigh(BigMoney high) {
        this.high = high;
    }

    public void setIdx(TradeableIndex idx) {
        this.idx = idx;
    }
    
    public void setLast(BigMoney last) {
        this.last = last;
    }

    public void setLow(BigMoney low) {
        this.low = low;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        String toString = null;
        try {
            toString = String.format("%s/%s: last %s, bid %s, ask %s, high %s, low %s, volume %s %s", 
                this.getTradableIdentifier(),
                this.getCurrency(),
                this.last.getAmount().toPlainString(),
                this.bid.getAmount().toPlainString(),
                this.ask.getAmount().toPlainString(),
                this.high.getAmount().toPlainString(),
                this.low.getAmount().toPlainString(),
                this.volume.toPlainString(),
                new Date(this.getTimestamp()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return toString;
    }
}
