/**
 * 
 */

package com.zygon.trade.strategy;

import com.xeiam.xchange.dto.Order;
import com.zygon.trade.execution.ExecutionController;
import java.math.BigDecimal;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The highest-level agent which accepts known facts about a given market
 * and chooses to act on the information as it sees fit.
 *
 * @author zygon
 */
public class TradeAgent {

    private final class TradeTask implements Runnable {

        @Override
        public void run() {
            Trade trade = TradeAgent.this.generateTrade();
            
            if (trade != null) {
                logger.info("Generated trade " + trade);
                
                BigDecimal tradeableAmount = trade.getEntryPoint() != null ? new BigDecimal(trade.getEntryPoint().doubleValue()) : null;
                Order order = execController.generateOrder(id, trade.getType(), tradeableAmount, trade.getTradeableIdentifier().toString(), trade.getCurrency());
                execController.placeOrder(id, order);
            }
        }
    }
    
    private final String id;
    private final ExecutionController execController;
    private final Logger logger = LoggerFactory.getLogger(TradeAgent.class);
    
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    
    public TradeAgent(String id, ExecutionController execController) {
        
        if (id == null || execController == null) {
            throw new IllegalArgumentException("No null arguments permitted.");
        }
        
        this.id = id;
        this.execController = execController;
        this.executor.scheduleAtFixedRate(new TradeTask(), 30, 30, TimeUnit.SECONDS);
    }

    protected final ExecutionController getExecController() {
        return this.execController;
    }
    
    protected final Logger getLogger() {
        return this.logger;
    }
    
    protected Trade generateTrade() {
        // No trade by default
        return null;
    }
    
    public void handle(IndicationProcessor.Response response) {
        this.logger.debug("Handling response " + response);
    }
}
