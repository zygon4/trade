/**
 * 
 */

package com.zygon.trade.strategy;

import com.google.common.eventbus.Subscribe;
import com.zygon.trade.market.model.indication.Indication;

/**
 *
 * @author zygon
 * 
 * TBD: make a class and implement InformationHandler?
 */
public interface IndicationProcessor<T_IN extends Indication> {
    
    public static enum Advice {
        DO_NOTHING,
        BUY,
        SELL;
    }
    
    public static enum Evidence {
        BEAR,
        BULL,
        STABLE,
        VOLATILE;
    }
    
    public static class Response {
        public final Advice advice;
        public final Evidence evidence;

        public Response(Advice advice, Evidence evidence) {
            this.advice = advice;
            this.evidence = evidence;
        }
        
        public boolean isEquals (Response resp) {
            
            boolean adviceEqual = false;
            if (this.getAdvice() != null) {
                adviceEqual = resp.getAdvice() != null && this.getAdvice() == resp.getAdvice();
            } else {
                adviceEqual = resp.getAdvice() == null;
            }
            
            boolean evidenceEqual = false;
            if (this.getEvidence() != null) {
                evidenceEqual = resp.getEvidence() != null && this.getEvidence() == resp.getEvidence();
            } else {
                evidenceEqual = resp.getEvidence() == null;
            }
            
            return adviceEqual && evidenceEqual;
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
            return String.format("Advice [%s], Evidence [%s]", 
                    this.advice != null ? this.advice.name() : "none", 
                    this.evidence != null ? this.evidence.name() : "none");
        }
    }
    
    @Subscribe
    public void process(T_IN in);
}
