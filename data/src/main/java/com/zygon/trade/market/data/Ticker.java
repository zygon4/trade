/**
 *
 */

package com.zygon.trade.market.data;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author zygon
 */
public class Ticker {

    private String tradeableIdentifer;
    private Date timestamp;
    private String source;
    private String currency;
    private BigDecimal last;
    private BigDecimal bid;
    private BigDecimal ask;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal volume;

    public Ticker(String tradeableIdentifer, Date ts, String source, String currency, BigDecimal last, BigDecimal bid,
            BigDecimal ask, BigDecimal high, BigDecimal low, BigDecimal volume) {
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

    public Ticker (com.xeiam.xchange.dto.marketdata.Ticker tick) {
        this (tick.getCurrencyPair().baseSymbol, getAdjustedDate(tick.getTimestamp()), "", tick.getCurrencyPair().counterSymbol,
                tick.getLast(), tick.getBid(), tick.getAsk(), tick.getHigh(), tick.getLow(), tick.getVolume());
    }

    public BigDecimal getAsk() {
        return this.ask;
    }

    public BigDecimal getBid() {
        return this.bid;
    }

    public String getCurrency() {
        return this.currency;
    }

    public BigDecimal getHigh() {
        return this.high;
    }

    public BigDecimal getLast() {
        return this.last;
    }

    public BigDecimal getLow() {
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

    public void setAsk(BigDecimal ask) {
        this.ask = ask;
    }

    public void setBid(BigDecimal bid) {
        this.bid = bid;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public void setLast(BigDecimal last) {
        this.last = last;
    }

    public void setLow(BigDecimal low) {
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
                this.last.toPlainString(),
                this.bid.toPlainString(),
                this.ask.toPlainString(),
                this.high.toPlainString(),
                this.low.toPlainString(),
                this.volume.toPlainString(),
                this.getTimestamp());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return toString;
    }
}
