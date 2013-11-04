
package com.zygon.trade.agent;

import com.zygon.trade.trade.TradeSignal;
import com.zygon.trade.execution.ExecutionController;

/**
 *
 * @author zygon
 */
public class SignalHandler {
    
    private final ExecutionController execution;

    public SignalHandler(ExecutionController execution) {
        this.execution = execution;
    }
    
    public void handle(TradeSignal signal) {
        System.out.println("Handling: " + signal);
    }
}
