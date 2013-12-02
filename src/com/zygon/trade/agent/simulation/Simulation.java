
package com.zygon.trade.agent.simulation;

import com.xeiam.xchange.dto.trade.Wallet;
import com.zygon.data.Context;
import com.zygon.data.Handler;
import com.zygon.data.set.DataSet;
import com.zygon.trade.agent.Agent;
import com.zygon.trade.agent.Strategy;
import com.zygon.trade.agent.trade.MACDTradeGenerator;
import com.zygon.trade.execution.MarketConditions;
import com.zygon.trade.execution.exchange.simulation.SimulationExchange;
import com.zygon.trade.market.data.Interpreter;
import com.zygon.trade.market.data.Ticker;
import com.zygon.trade.market.data.TickerReader;
import com.zygon.trade.market.data.interpret.TickerMACD;
import com.zygon.trade.market.model.indication.Identifier;
import com.zygon.trade.market.model.indication.market.MACDSignalCross;
import com.zygon.trade.market.model.indication.market.MACDZeroCross;
import com.zygon.trade.market.util.Duration;
import com.zygon.trade.market.util.MovingAverage;
import com.zygon.trade.trade.TradeBroker;
import com.zygon.trade.trade.TradeSummary;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;



/**
 *
 * @author zygon
 */
public class Simulation<T> {
    
    private class UnifyingHandler implements Handler<T> {

        @Override
        public void handle(T data) {
            simExchange.handle(data);
            agent.handle(data);
        }
    }
    
    private final DataSet<T> dataSet;
    private final Agent<T> agent;
    private final SimulationExchange simExchange;
    private final UnifyingHandler handler = new UnifyingHandler();

    public Simulation(DataSet<T> dataSet, Collection<Interpreter<T>> interpreters, Strategy strategy, Wallet[] wallets) {
        this.dataSet = dataSet;
        this.simExchange = new SimulationExchange("sim-user", wallets, new MarketConditions("sim"));
        TradeBroker broker = new TradeBroker("sim-accnt", this.simExchange);
        this.agent = new Agent<T>("sim-agent", interpreters, strategy, broker);
    }
    
    public TradeSummary run() throws IOException, InterruptedException {
        
        this.simExchange.start();
        this.agent.initialize();
        
        this.dataSet.writeData(this.handler);
        
        System.out.println("Enter anything to continue");
        System.in.read();
        
        this.agent.uninitialize();
        this.simExchange.stop();
        
        return this.agent.getStrategySummary();
    }
    
    public static void main(String[] args) throws IOException, InterruptedException {
        
        Properties props = new Properties();
        props.put(Context.PROP_NAME, "sim");
        props.put(Context.PROP_CLS, "");
        Context ctx = new Context(props);
        String dataSetFilePath = "/home/zygon/opt/trade/system/data/agent/macd/macd_ticker.txt";
        DataSet<Ticker> dataSet = new com.zygon.data.set.DataSet<Ticker>(ctx, new TickerReader(new File(dataSetFilePath).getAbsolutePath()), null, null);
        
        Collection<Interpreter<Ticker>> interpreters = new ArrayList<>();
        
        MovingAverage leading = new MovingAverage(Duration._4, TimeUnit.HOURS, new MovingAverage.ExponentialValueFn(), 2);
        MovingAverage lagging = new MovingAverage(Duration._24, TimeUnit.HOURS, new MovingAverage.ExponentialValueFn(), 2);
        MovingAverage macd = new MovingAverage(Duration._1, TimeUnit.HOURS, new MovingAverage.ExponentialValueFn(), 2);
        interpreters.add(new TickerMACD(leading, lagging, macd));
        
        Wallet[] wallets = new Wallet[]{
                    new Wallet("USD", BigMoney.of(CurrencyUnit.USD, 1000.0)),
                    new Wallet("BTC", BigMoney.of(CurrencyUnit.of("BTC"), 10.0))
                };
        
        Strategy strategy = new Strategy("sim-strategy", new ArrayList<Identifier>(Arrays.asList(MACDZeroCross.ID, MACDSignalCross.ID)), new MACDTradeGenerator());
        
        Simulation<Ticker> simulation = new Simulation<Ticker>(dataSet, interpreters, strategy, wallets);
        
        TradeSummary summary = null;
        
        try {
            summary = simulation.run();
            System.out.println(summary);
        } catch (IOException io) {
            io.printStackTrace();
        }
        
    }
}
