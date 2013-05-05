/**
 * 
 */

package com.zygon.exchange.strategy;

import com.zygon.exchange.market.model.indication.Indication;

/**
 *
 * @author zygon
 */
public interface Strategy<T_IN extends Indication> {
    
    public static enum Advice {
        DO_NOTHING,
        BUY,
        SELL;
    }
    
    public static class Response {
        public final Advice advice;

        public Response(Advice advice) {
            this.advice = advice;
        }

        public Advice getAdvice() {
            return this.advice;
        }
    }
    
    // keep it simple for now - make a StrategyResponse
    public Response handle(T_IN in);
}
