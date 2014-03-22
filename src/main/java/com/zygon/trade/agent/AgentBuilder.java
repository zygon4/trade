
package com.zygon.trade.agent;

import com.zygon.data.RawDataWriter;
import com.zygon.trade.trade.TradeGenerator;
import com.zygon.trade.market.data.Interpreter;
import com.zygon.trade.market.model.indication.Identifier;
import com.zygon.trade.trade.TradeBroker;
import java.util.Collection;

/**
 *
 * @author zygon
 */
public class AgentBuilder<T> {

    // These are Agent specific
    private String name;
    private Collection<Interpreter<T>> interpreters;
    private TradeBroker broker;
    private RawDataWriter<T> dataWriter;
    
    // These are Strategy specific
    private Collection<Identifier> supportedIndicators;
    private TradeGenerator tradeGenerator;
    
    public Agent<T> build() {
        Agent<T> agent = new Agent<T>(this.name, this.interpreters, this.supportedIndicators, this.tradeGenerator, this.broker);
        if (this.dataWriter != null) {
            agent.setDataWriter(this.dataWriter);
        }
        return agent;
    }

    public String getName() {
        return this.name;
    }
    
    public void setTradeBroker(TradeBroker broker) {
        this.broker = broker;
    }

    public void setDataWriter(RawDataWriter<T> dataWriter) {
        this.dataWriter = dataWriter;
    }

    public void setInterpreters(Collection<Interpreter<T>> interpreters) {
        this.interpreters = interpreters;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTradeGenerator(TradeGenerator signalGenerator) {
        this.tradeGenerator = signalGenerator;
    }

    public void setSupportedIndicators(Collection<Identifier> supportedIndicators) {
        this.supportedIndicators = supportedIndicators;
    }
}
