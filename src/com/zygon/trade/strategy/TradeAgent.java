/**
 * 
 */

package com.zygon.trade.strategy;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.xeiam.xchange.dto.Order;
import static com.xeiam.xchange.dto.Order.OrderType.ASK;
import static com.xeiam.xchange.dto.Order.OrderType.BID;
import com.zygon.trade.execution.ExecutionController;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The highest-level agent which accepts known facts about a given market
 * and chooses to act on the information as it sees fit.
 *
 * @author zygon
 */
public class TradeAgent {

    private final String id;
    private final ExecutionController execController;
    private final Multimap<Order.OrderType, Trade> activeTrades = ArrayListMultimap.create();
    private final Logger logger = LoggerFactory.getLogger(TradeAgent.class);
    
    public TradeAgent(String id, ExecutionController execController) {
        
        if (id == null || execController == null) {
            throw new IllegalArgumentException("No null arguments permitted.");
        }
        
        this.id = id;
        this.execController = execController;
    }

    protected final Trade createTrade(Order.OrderType orderType, String tradeableIdentifer, BigDecimal price, double exitModifier, double stopLossModifier) {
        BigDecimal exitPrice = new BigDecimal(price.doubleValue() + (price.doubleValue() * exitModifier));
        BigDecimal stopLossPrice = new BigDecimal(price.doubleValue() +  (price.doubleValue() * stopLossModifier));
        
        return new Trade(this.getCurrency(), tradeableIdentifer, orderType, price, exitPrice, stopLossPrice);
    }
    
    protected final Trade createTrade(Order.OrderType orderType, String tradeableIdentifer, double exitModifier, double stopLossModifier) {
        return this.createTrade(orderType, tradeableIdentifer, this.execController.getMarketPrice(id), exitModifier, stopLossModifier);
    }
    
    protected final Logger getLogger() {
        return this.logger;
    }
    
    // For lack of a better term
    public void generateNewTrades() {
        Collection<Trade> trades = this.generateTrades();

        if (trades != null) {
            for (Trade trade : trades) {
                logger.debug("Generated trade " + trade);

                Order order = this.execController.generateOrder(id, trade.getType(), trade.getEntryPoint(), trade.getTradeableIdentifier().toString(), trade.getCurrency());
                this.execController.placeOrder(id, order);

                this.activeTrades.put(trade.getType(), trade);
            }
        }
    }
    
    protected Collection<Trade> generateTrades() {
        // No trades by default
        return null;
    }
    
    // sad child has to ask for the trades and gets an immutable copy.
    protected void getActiveTrades(Order.OrderType type, List<Trade> trades) {
        trades.addAll(Collections.unmodifiableCollection(this.activeTrades.get(type)));
    }
    
    protected final String getCurrency() {
        return this.execController.getCurrency();
    }
    
    public String getId() {
        return this.id;
    }
    
    public final boolean hasActiveTrades() {
        return !this.activeTrades.isEmpty();
    }
    
    protected final boolean isEqualOrAbove (BigDecimal price) {
        return price.doubleValue() >= this.execController.getMarketPrice(id).doubleValue();
    }
    
    protected final boolean isEqualOrBelow (BigDecimal price) {
        return this.execController.getMarketPrice(id).doubleValue() <= price.doubleValue();
    }
    
    public void manageActiveTrades() {
        if (this.hasActiveTrades()) {
            
            // Check if we've reached our exit points
            List<Trade> trades = new ArrayList<>();
            
            trades.addAll(this.activeTrades.get(ASK));
            trades.addAll(this.activeTrades.get(BID));
            
            Iterator<Trade> itor = trades.iterator();
            while (itor.hasNext()) {
                Trade trade = itor.next();
                
                if (trade.hasExitPoint()) {
                    if (reachedExitPoint(trade)) {
                        Order order = this.execController.generateOrder(this.id, trade.getExitType(), trade.getExitPoint(), trade.getTradeableIdentifier().toString(), trade.getCurrency());
                        this.execController.placeOrder(this.id, order);

                        itor.remove();
                        
                        this.activeTrades.remove(trade.getType(), trade);
                    }
                }
            }
        }
    }
    
    protected void processInformation(IndicationProcessor.Response response) {
        // nothing by default
    }
    
    // i like this - follow up with "reachedEntryPoint()"?
    private boolean reachedExitPoint(Trade trade) {

        switch (trade.getType()) {
            case ASK:
                if (trade.getStopLossPoint() != null && this.isEqualOrAbove(trade.getStopLossPoint())) {
                    this.logger.info("Hit stop loss. Attempting to buy back.");
                    // if we sold and we're above the stop
                    return true;
                }
                // we sold - check if we are below what we want to buy back at
                if (this.isEqualOrBelow(trade.getExitPoint())) {
                    this.logger.info("Hit exit point. Attempting to buy back.");
                    return true;
                }
                break;
            case BID:
                if (trade.getStopLossPoint() != null && this.isEqualOrBelow(trade.getStopLossPoint())) {
                    this.logger.info("Hit stop loss. Attempting to sell back.");
                    // if we bought and we're below the stop
                    return true;
                }
                // we bought - check if we are above what we want to re-buy at
                if (this.isEqualOrAbove(trade.getExitPoint())) {
                    this.logger.info("Hit exit point. Attempting to sell back.");
                    return true;
                }
                break;
        }

        return false;
    }
}
