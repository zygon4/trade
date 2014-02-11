/**
 * 
 */

package com.zygon.trade.market.data;

import java.math.BigDecimal;
import java.util.Date;
import org.joda.money.BigMoney;

/**
 *
 * @author zygon
 */
public class Ticker {
    
    private String tradeableIdentifer;
    private Date timestamp;
    private String source;
    private String currency;
    private BigMoney last;
    private BigMoney bid;
    private BigMoney ask;
    private BigMoney high;
    private BigMoney low;
    private BigDecimal volume;
    
    public Ticker(String tradeableIdentifer, Date ts, String source, String currency, BigMoney last, BigMoney bid, 
            BigMoney ask, BigMoney high, BigMoney low, BigDecimal volume) {
        // TODO: uncomment this and restrict inputs
//        if (tradeableIdentifer == null) {
//            throw new IllegalArgumentException("tradeableIdentifer cannot be null");
//        }
//        if (currency == null) {
//            throw new IllegalArgumentException("currency cannot be null");
//        }
//        if (source == null) {
//            throw new IllegalArgumentException("source cannot be null");
//        }
        this.tradeableIdentifer = tradeableIdentifer;
        this.timestamp = ts;
        this.source = source;
        this.currency = currency;
        this.last = last;
        this.bid = bid;
        this.ask = ask;
        this.high = high;
        this.low = low;
        this.volume = volume;
    }
    
    private static Date getAdjustedDate (Date tickTime) {
        return new Date(tickTime.getTime() + (1000 * 60 * 5));
    }
    
    public Ticker (com.xeiam.xchange.dto.marketdata.Ticker tick, String currency) {
        this (tick.getTradableIdentifier(), getAdjustedDate(tick.getTimestamp()), "", currency, tick.getLast(), tick.getBid(), tick.getAsk(), 
                tick.getHigh(), tick.getLow(), tick.getVolume());
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
        return this.tradeableIdentifer;
    }

    public String getSource() {
        return this.source;
    }
    
    public BigDecimal getVolume() {
        return this.volume;
    }

    public Date getTimestamp() {
        return this.timestamp;
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
    
    public void setLast(BigMoney last) {
        this.last = last;
    }

    public void setLow(BigMoney low) {
        this.low = low;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setTradeableIdentifer(String tradeableIdentifer) {
        this.tradeableIdentifer = tradeableIdentifer;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        String toString = null;
        try {
            toString = String.format("%s/%s/%s: last %s, bid %s, ask %s, high %s, low %s, volume %s %s", 
                this.getTradableIdentifier(),
                this.getCurrency(),
                this.getSource(),
                this.last.getAmount().toPlainString(),
                this.bid.getAmount().toPlainString(),
                this.ask.getAmount().toPlainString(),
                this.high.getAmount().toPlainString(),
                this.low.getAmount().toPlainString(),
                this.volume.toPlainString(),
                this.getTimestamp());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return toString;
    }
}
