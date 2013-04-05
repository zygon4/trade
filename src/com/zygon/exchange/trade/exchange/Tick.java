/**
 * 
 */

package com.zygon.exchange.trade.exchange;

import java.math.BigDecimal;
import org.joda.money.BigMoney;

/**
 *
 * @author zygon
 * Ticker [tradableIdentifier=BTC, last=USD 16.68798, bid=USD 16.68798, 
 * ask=USD 16.68800, high=USD 16.70000, low=USD 15.63412, volume=36784.26471552, 
 * timestamp=Mon Jan 21 13:23:57 CET 2013]
 * 
 */
@Deprecated
final class Tick {
    private final String security;
    private final BigMoney last;
    private final BigMoney bid;
    private final BigMoney ask;
    private final BigMoney high;
    private final BigMoney low;
    private final BigDecimal vol;
    private final long timestamp;

    public Tick(String security, BigMoney last, BigMoney bid, BigMoney ask, BigMoney high, BigMoney low, BigDecimal vol, long timestamp) {
        this.security = security;
        this.last = last;
        this.bid = bid;
        this.ask = ask;
        this.high = high;
        this.low = low;
        this.vol = vol;
        this.timestamp = timestamp;
    }

    public double getAsk() {
        return ask.getAmount().doubleValue();
    }

    public double getBid() {
        return bid.getAmount().doubleValue();
    }

    public double getHigh() {
        return high.getAmount().doubleValue();
    }

    public double getLast() {
        return last.getAmount().doubleValue();
    }

    public double getLow() {
        return low.getAmount().doubleValue();
    }

    public String getSecurity() {
        return security;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getVol() {
        return vol.doubleValue();
    }
    
    @Override
    public String toString() {
        return String.format("%s, last: %s, bid: %s, ask: %s, high: %s, low: %s, vol: %s, ts: %d", 
                this.security, last, bid, ask, high, low, vol, timestamp);
    }
}
