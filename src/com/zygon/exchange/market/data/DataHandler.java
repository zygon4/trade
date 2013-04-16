/**
 * 
 */

package com.zygon.exchange.market.data;

import com.zygon.exchange.InformationBuffer;
import com.zygon.exchange.InformationHandler;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author zygon
 * 
 */
public class DataHandler<T_IN> extends InformationBuffer<Object> {

    /**
     * Responsible for (optionally) interpreting incoming data into another form.
     *  
     */
    public static interface Interpreter<T_IN> {
        public Object interpret(T_IN data);
    }
    
    private Collection<Interpreter<T_IN>> translators;
    
    public DataHandler(String name, InformationHandler<Object> target, Collection<Interpreter<T_IN>> interpreters) {
        super(name, Collections.unmodifiableList(Arrays.asList(target)));
        
        this.translators = interpreters;
    }
    
    public DataHandler(String name, Collection<Interpreter<T_IN>> interpreters) {
        this (name, null, interpreters);
    }
    
    public DataHandler(String name) {
        this (name, null);
    }

    @Override
    public void handle(Object t) {
        if (this.translators == null) {
            super.handle(t);
        } else {
            for (Interpreter trans : this.translators) {
                Object translated = trans.interpret(t);
                super.handle(translated);
            }
        }
    }

    public void setTranslators(Collection<Interpreter<T_IN>> translators) {
        this.translators = translators;
    }
}
