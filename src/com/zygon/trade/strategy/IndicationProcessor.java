/**
 * 
 */

package com.zygon.trade.strategy;

import com.google.common.eventbus.Subscribe;
import com.zygon.trade.market.model.indication.Indication;

/**
 *
 * @author zygon
 */
public interface IndicationProcessor<T_IN extends Indication> {
    
    public static enum Advice {
        DO_NOTHING,
        BUY,
        SELL;
    }
    
    public static enum Evidence {
        BEAR,
        BULL;
    }
    
    public static class Response {
        public final Advice advice;
        public final Evidence evidence;

        public Response(Advice advice, Evidence evidence) {
            this.advice = advice;
            this.evidence = evidence;
        }
        
        public Advice getAdvice() {
            return this.advice;
        }

        public Evidence getEvidence() {
            return this.evidence;
        }
        
        public boolean hasAdvice() {
            return this.advice != null;
        }
        
        public boolean hasEvidence() {
            return this.evidence != null;
        }

        @Override
        public String toString() {
            return String.format("Advice [%s], Evidence [%s]", this.advice.name(), this.evidence.name());
        }
    }
    
    @Subscribe
    public Response process(T_IN in);
}
