
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
 *
 * @author zygon
 */
public abstract class Agent<T> implements EventFeed.Handler<T> {

    private final String name;
    private final Logger log;
    private final Collection<Interpreter<T>> interpretters;
    
    public Agent(String name, Collection<Interpreter<T>> interpreters) {
        this.name = name;
        this.log = LoggerFactory.getLogger(this.name);
        this.interpretters = interpreters;
    }

    protected abstract void handle (List<Message> messages);
    
    @Override
    public void handle(T t) {
        if (this.interpretters != null) {
            
            List<Message> messages = new ArrayList<>();
            // TODO: use CompletionService for async processing
            
            for (Interpreter<T> trans : this.interpretters) {
                Message[] translated = trans.interpret(t);
                if (translated != null) {
                    messages.addAll(Arrays.asList(translated));
                }
            }
            
            this.handle(messages);
            
        } else {
            this.log.trace("No translator available to process " + t);
        }
    }
}
