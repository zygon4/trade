/**
 * 
 */

package com.zygon.trade.market.data;

import com.zygon.trade.market.Message;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zygon
 * 
 */
public class DataProcessor<T_IN> {

    /**
     * Responsible for (optionally) interpreting incoming data into another 
     * form.  This could be time based aggregation, filtering, etc.
     */
    public static interface Interpreter<T_IN> {
        public Message[] interpret(T_IN data);
    }
    
    private final Logger log;
    private final Collection<Interpreter<T_IN>> translators;
    
    // TODO remove the name?
    public DataProcessor(String name, Collection<Interpreter<T_IN>> interpreters) {
        this.log = LoggerFactory.getLogger(name);
        this.translators = interpreters;
    }
    
    public Collection<Message> process(T_IN t) {
        
        List<Message> messages = new ArrayList<>();
        
        if (this.translators != null) {
            
            // TODO: use CompletionService for async processing
            
            for (Interpreter<T_IN> trans : this.translators) {
                Message[] translated = trans.interpret(t);
                if (translated != null) {
                    messages.addAll(Arrays.asList(translated));
                }
            }
        } else {
            this.log.trace("No translator available to process " + t);
        }
        
        return messages;
    }
}
