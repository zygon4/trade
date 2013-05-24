/**
 * 
 */

package com.zygon.trade.strategy;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.xeiam.xchange.dto.Order;
import static com.xeiam.xchange.dto.Order.OrderType.ASK;
import static com.xeiam.xchange.dto.Order.OrderType.BID;
import com.zygon.trade.execution.ExchangeException;
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
 * Subject to a lot of change. May become interface/abstract at some point.
 *
 * @author zygon
 */
public class TradeAgent {

    private final String id;
    private final ExecutionController execController;
    private final Multimap<Order.OrderType, Trade> activeTrades = ArrayListMultimap.create();
    private final Logger logger = LoggerFactory.getLogger(TradeAgent.class);
    private final TradeSummary tradeSummary = new TradeSummary();
    
    public TradeAgent(String id, ExecutionController execController) {
        
        if (id == null || execController == null) {
            throw new IllegalArgumentException("No null arguments permitted.");
        }
        
        this.id = id;
        this.execController = execController;
    }

    protected final Trade createTrade(Order.OrderType orderType, double volume, String tradeableIdentifer, BigDecimal price, double exitModifier, double stopLossModifier) {
        BigDecimal exitPrice = new BigDecimal(price.doubleValue() + (price.doubleValue() * exitModifier));
        BigDecimal stopLossPrice = new BigDecimal(price.doubleValue() +  (price.doubleValue() * stopLossModifier));
        
        return new Trade(orderType, volume, this.getTransactionCurrency(), tradeableIdentifer, price, exitPrice, stopLossPrice);
    }
    
    protected final Trade createTrade(Order.OrderType orderType, double volume, String tradeableIdentifer, double exitModifier, double stopLossModifier) {
        return this.createTrade(orderType, volume, tradeableIdentifer, this.execController.getMarketPrice(id), exitModifier, stopLossModifier);
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

                Order order = this.execController.generateOrder(id, trade.getType(), trade.getVolume(), trade.getTradeableIdentifier().toString(), trade.getCurrency());
                try {
                    this.execController.placeOrder(id, order);
                    
                    this.activeTrades.put(trade.getType(), trade);
                } catch (ExchangeException ee) {
                    logger.error(ee.getMessage() != null ? ee.getMessage() : "unexpected error", ee);
                }
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
    
    protected final double getMarketPrice() {
        return this.execController.getMarketPrice(id).doubleValue();
    }

    public TradeSummary getTradeSummary() {
        return this.tradeSummary;
    }
    
    protected final String getTransactionCurrency() {
        return this.execController.getTransactionCurrency();
    }
    
    public String getId() {
        return this.id;
    }
    
    protected BigDecimal getAccountBalance(String currency) {
        return this.execController.getBalance(this.id, currency);
    }
    
    public final boolean hasActiveTrades() {
        return !this.activeTrades.isEmpty();
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
                    BigDecimal currentPrice = this.execController.getMarketPrice(id);
                    ExitStatus exitStatus = trade.getExitStatus(currentPrice);
                    
                    if (exitStatus.isAtExitPoint()) {
                        Order order = this.execController.generateOrder(
                                this.id, trade.getExitType(), trade.getVolume(), trade.getTradeableIdentifier().toString(), trade.getCurrency());
                        
                        try {
                            this.execController.placeOrder(this.id, order);
                            itor.remove();
                            this.activeTrades.remove(trade.getType(), trade);
                            
                        } catch (ExchangeException ee) {
                            logger.error(ee.getMessage() != null ? ee.getMessage() : "unexpected error", ee);
                        }
                        
                        switch (exitStatus.getReason()) {
                            case STOP_LOSS:
                                this.tradeSummary.addLoosingTrade();
                                break;
                            case TAKE_PROFIT:
                                this.tradeSummary.addProfitableTrade();
                                break;
                        }
                        
                        // TODO: move this to a more appropriate location
                        TradeSummary summary = getTradeSummary();

                        this.logger.info("Executed {} profitable trades, {} loosing trades", 
                                summary.getProfitableTrades(), summary.getLoosingTrades());
                    }
                }
            }
        }
    }
    
    protected void processInformation(IndicationProcessor.Response response) {
        // nothing by default
    }
}
