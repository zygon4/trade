/**
 * 
 */

package com.zygon.trade.strategy;

import com.xeiam.xchange.dto.Order;
import static com.xeiam.xchange.dto.Order.OrderType.ASK;
import static com.xeiam.xchange.dto.Order.OrderType.BID;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * The intent for this class is a way to frame an active Trade.  A collection of
 * trades could be (for instance) cropped up waiting for the respective exit
 * points.
 * 
 * @author zygon
 * 
 * Version 1.0 - subject to a lot of change.
 */
public class Trade {

    private final Logger logger = LoggerFactory.getLogger(Trade.class);
    private final Order.OrderType type;
    private final BigDecimal volume;
    private final String transactionCurrency;
    private final String tradeableIdentifier;
    private final BigDecimal entryPoint;
    private final BigDecimal takeProfitPoint;
    private final BigDecimal stopLossPoint;

    // TBD: exit point classification/rules? e.g. exit is "double entry"
    
    public Trade(Order.OrderType type, double volume, String transactionCurrency, 
            String tradeableIdentifier, BigDecimal entryPoint, BigDecimal takeProfitPoint, BigDecimal stopLossPoint) {
        
        if (transactionCurrency == null || tradeableIdentifier == null || type == null) {
            throw new IllegalArgumentException("Must provide currency, tradeableIdentifier, and type");
        }
        
        // check if the entry/exits are sane versus the bid/ask??
        
        this.type = type;
        this.volume = BigDecimal.valueOf(volume);
        this.transactionCurrency = transactionCurrency;
        this.tradeableIdentifier = tradeableIdentifier;
        this.entryPoint = entryPoint; // Can be null implying a market order
        this.takeProfitPoint = takeProfitPoint; // Can be null implying no assumed exit
        this.stopLossPoint = stopLossPoint; // Can be null implying no stop loss point
    }
    
    public Trade(String currency, String tradeableIdentifier, Order.OrderType type) {
        this (type, 0.0, currency, tradeableIdentifier, null, null, null);
    }

    public final String getCurrency() {
        return this.transactionCurrency;
    }

    public BigDecimal getEntryPoint() {
        return this.entryPoint;
    }

    public BigDecimal getTakeProfitPoint() {
        return this.takeProfitPoint;
    }
    
    public final Order.OrderType getExitType() {
        return this.type == Order.OrderType.ASK ? Order.OrderType.BID : Order.OrderType.ASK;
    }

    public final BigDecimal getStopLossPoint() {
        return this.stopLossPoint;
    }

    public final String getTradeableIdentifier() {
        return this.tradeableIdentifier;
    }

    public final Order.OrderType getType() {
        return this.type;
    }
    
    public final BigDecimal getVolume() {
        return this.volume;
    }
    
    private final double getVol() {
        return this.volume.doubleValue();
    }
    
    public final boolean hasExitPoint() {
        return this.takeProfitPoint != null || this.stopLossPoint != null;
    }
    
    private double getProfitLoss(double entryPrice, double exitPrice, double volume) {
        return (exitPrice - entryPrice) * volume;
    }
    
    public ExitStatus getExitStatus(BigDecimal currentPrice) {

        switch (getType()) {
            case ASK:
                if (this.getStopLossPoint() != null && currentPrice.doubleValue() >= this.getStopLossPoint().doubleValue()) {
                    this.logger.info("Hit stop loss. Attempting to buy back. Expected loss of {}", 
                            getProfitLoss(this.getEntryPoint().doubleValue(), this.getStopLossPoint().doubleValue(), this.getVol()));
                    // if we sold and we're above the stop
                    return new ExitStatus(true, ExitStatus.ExitReason.STOP_LOSS);
                }
                // we sold - check if we are below what we want to buy back at
                if (currentPrice.doubleValue() <= this.getTakeProfitPoint().doubleValue()) {
                    this.logger.info("Hit exit point. Attempting to buy back. Expected profit of {}", 
                            getProfitLoss(this.getEntryPoint().doubleValue(), this.getTakeProfitPoint().doubleValue(), this.getVol()));
                    return new ExitStatus(true, ExitStatus.ExitReason.TAKE_PROFIT);
                }
                break;
            case BID:
                if (this.getStopLossPoint() != null && currentPrice.doubleValue() <= this.getStopLossPoint().doubleValue()) {
                    this.logger.info("Hit stop loss. Attempting to sell back. Expected loss of {}", 
                            getProfitLoss(this.getEntryPoint().doubleValue(), this.getStopLossPoint().doubleValue(), this.getVol()));
                    // if we bought and we're below the stop
                    return new ExitStatus(true, ExitStatus.ExitReason.STOP_LOSS);
                }
                // we bought - check if we are above what we want to sell at
                if (currentPrice.doubleValue() >= this.getTakeProfitPoint().doubleValue()) {
                    this.logger.info("Hit exit point. Attempting to sell back. Expected profit of {}", 
                            getProfitLoss(this.getEntryPoint().doubleValue(), this.getTakeProfitPoint().doubleValue(), this.getVol()));
                    return new ExitStatus(true, ExitStatus.ExitReason.TAKE_PROFIT);
                }
                break;
        }

        return new ExitStatus(false);
    }
}
