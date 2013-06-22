/**
 * 
 */

package com.zygon.trade.market.data;

import com.zygon.trade.market.Message;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.joda.money.BigMoney;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author zygon
 */
@Entity
@Table(name="ticker", schema = "trade@cassandra_pu")
public class Ticker extends Message implements Serializable {
    
    @EmbeddedId
    private TradeableIndex idx;
    
    @Column(name="last_price")
    private BigMoney last;
    @Column(name="bid")
    private BigMoney bid;
    @Column(name="ask")
    private BigMoney ask;
    @Column(name="high")
    private BigMoney high;
    @Column(name="low")
    private BigMoney low;
    @Column(name="volume")
    private BigDecimal volume;
    
    public Ticker(TradeableIndex idx, BigMoney last, BigMoney bid, BigMoney ask, BigMoney high, BigMoney low, BigDecimal volume) {
        this.idx = idx;
        this.last = last;
        this.bid = bid;
        this.ask = ask;
        this.high = high;
        this.low = low;
        this.volume = volume;
    }
    
    private static TradeableIndex create(com.xeiam.xchange.dto.marketdata.Ticker tick) {
        TradeableIndex idx = new TradeableIndex();
        idx.setIdentifer(tick.getTradableIdentifier());
        idx.setTs(tick.getTimestamp().getTime());
        return idx;
    }
    
    public Ticker (com.xeiam.xchange.dto.marketdata.Ticker tick) {
        this (create(tick), tick.getLast(), tick.getBid(), tick.getAsk(), 
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
            toString = String.format("%s: last %s, bid %s, ask %s, high %s, low %s, volume %s %s", 
                this.getTradableIdentifier(),
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
