
package com.zygon.trade.agent;

import com.zygon.data.EventFeed;
import com.zygon.trade.market.Message;
import com.zygon.trade.market.data.Interpreter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Right now an Agent encompasses a few things: receiving raw information
 * from the exchanges (e.g. tickers), interpreting the information into
 * messages, analyzing the messages to form trading signal, and finally,
 * processing the signals by translating them into orders.
 *
 * @author zygon
 */
public class Agent<T> implements EventFeed.Handler<T> {

    private final String name;
    private final Logger log;
    private final Collection<Interpreter<T>> interpreters;
    
    // This is just the easiest way to wire an "agent" and the "strategy" - not necessarily the best way. 
    // In fact, an Agent and Strategy need to be re-thought out.
    private final Strategy strategy;
    
    public Agent(String name, Collection<Interpreter<T>> interpreters, Strategy strategy) {
        this.name = name;
        this.log = LoggerFactory.getLogger(this.name);
        this.interpreters = interpreters;
        this.strategy = strategy;
    }

    @Override
    public void handle(T t) {
        if (this.interpreters != null) {
            
            List<Message> messages = new ArrayList<>();
            // TODO: use CompletionService for async processing
            
            for (Interpreter<T> trans : this.interpreters) {
                Message[] translated = trans.interpret(t);
                if (translated != null) {
                    messages.addAll(Arrays.asList(translated));
                }
            }
            
            if (!messages.isEmpty()) {
                this.strategy.send(messages);
            }
            
        } else {
            this.log.trace("No translator available to process " + t);
        }
    }
    
    public void initialize() {
        this.strategy.start();
    }
    
    public void uninitialize() {
        this.strategy.stop();
    }
}
