
package com.zygon.trade.agent.simulation;

import com.google.common.collect.Lists;
import com.xeiam.xchange.dto.trade.Wallet;
import com.zygon.data.Context;
import com.zygon.data.Handler;
import com.zygon.data.set.DataSet;
import com.zygon.trade.agent.Agent;
import com.zygon.trade.agent.trade.RSITrader;
import com.zygon.trade.execution.MarketConditions;
import com.zygon.trade.execution.exchange.simulation.SimulationExchange;
import com.zygon.trade.market.data.Interpreter;
import com.zygon.trade.market.data.Ticker;
import com.zygon.trade.market.data.TickerReader;
import com.zygon.trade.market.data.interpret.RSIInterpreter;
import com.zygon.trade.market.data.interpret.TickerMACD;
import com.zygon.trade.market.model.indication.Identifier;
import com.zygon.trade.market.model.indication.numeric.RSI;
import com.zygon.trade.market.util.Duration;
import com.zygon.trade.market.util.MovingAverage;
import com.zygon.trade.trade.TradeBroker;
import com.zygon.trade.trade.TradeGenerator;
import com.zygon.trade.trade.TradeSummary;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


/**
 *
 * @author zygon
 * @param <T>
 */
public class Simulation<T> {

    private class SimExchange<T> extends SimulationExchange<T> {

        private Handler<T> extraHandler;

        public SimExchange(String username, Wallet[] wallets, MarketConditions marketConditions) {
            super(username, wallets, marketConditions);
        }

        @Override
        public void handle(T r) {
            super.handle(r);
            this.extraHandler.handle(r);
        }

        private void setExtraHandler(Handler<T> extraHandler) {
            this.extraHandler = extraHandler;
        }
    }

    private class UnifyingHandler implements Handler<T> {

        @Override
        public void handle(T data) {
            agent.handle(data);
            simExchange.handle(data);
        }
    }

    private final DataSet<T> dataSet;
    private final Agent<T> agent;
    private final SimExchange simExchange;
    private final UnifyingHandler handler = new UnifyingHandler();

    public Simulation(DataSet<T> dataSet, Collection<Interpreter<T>> interpreters,
                 Collection<Identifier> supportedIndicators,
                 TradeGenerator tradeGenerator, Wallet[] wallets) {
        this.dataSet = dataSet;

        this.simExchange = new SimExchange("sim-user", wallets, new MarketConditions("sim"));

        TradeBroker broker = new TradeBroker("sim-accnt", this.simExchange);
        this.agent = new Agent<>("sim-agent", interpreters, supportedIndicators, tradeGenerator, broker);

        this.simExchange.setExtraHandler(this.agent);
    }

    public TradeSummary run() throws IOException, InterruptedException {

        this.simExchange.start();

        Thread.sleep(5000);

        this.agent.initialize();

        Thread.sleep(5000);

        this.dataSet.writeData(this.simExchange);

//        System.out.println("Enter anything to continue");
//        System.in.read();
        Thread.sleep(5000);

        this.agent.uninitialize();

        Thread.sleep(5000);

        this.simExchange.stop();

        Thread.sleep(5000);

        return this.agent.getStrategySummary();
    }

    private static Collection<Interpreter<Ticker>> createInterpretters() {
        Collection<Interpreter<Ticker>> interpreters = Lists.newArrayList();

        MovingAverage leading = new MovingAverage(Duration._12, TimeUnit.DAYS, new MovingAverage.ExponentialValueFn(), 2);
        MovingAverage lagging = new MovingAverage(Duration._26, TimeUnit.DAYS, new MovingAverage.ExponentialValueFn(), 2);
        MovingAverage macd = new MovingAverage(Duration._9, TimeUnit.DAYS, new MovingAverage.ExponentialValueFn(), 2);
        interpreters.add(new TickerMACD(leading, lagging, macd));

        MovingAverage gains = new MovingAverage(Duration._12, TimeUnit.HOURS, new MovingAverage.ExponentialValueFn(), 2);
        MovingAverage losses = new MovingAverage(Duration._12, TimeUnit.HOURS, new MovingAverage.ExponentialValueFn(), 2);
        RSIInterpreter rsiInterpreter = new RSIInterpreter(gains, losses);
        interpreters.add(rsiInterpreter);

        return interpreters;
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println(new Date(1385855551016L));
        System.out.println(new Date(1386130283519L));


        Properties props = new Properties();
        props.put(Context.PROP_NAME, "sim");
        props.put(Context.PROP_CLS, "");
        Context ctx = new Context(props);
        String dataSetFilePath = "/home/zygon/opt/trade/system/data/agent/macd/macd_ticker.txt";
        DataSet<Ticker> dataSet = new com.zygon.data.set.DataSet<Ticker>(ctx, new TickerReader(new File(dataSetFilePath).getAbsolutePath()), null, null);

        TradeSummary totalSummary = new TradeSummary("total");

        try {
            for (int i = 0; i < 10; i++) {

                Collection<Interpreter<Ticker>> interpreters = createInterpretters();

                Wallet[] wallets = new Wallet[]{
                    new Wallet("USD", BigDecimal.valueOf(1100.0)),
                    new Wallet("BTC", BigDecimal.valueOf(1.5))
                };


                Simulation<Ticker> simulation = new Simulation<>(dataSet, interpreters, Lists.newArrayList(Arrays.asList(RSI.ID)), new RSITrader(), wallets);
                TradeSummary summary = simulation.run();
                totalSummary.add(summary);
                System.out.println(i+1 + ":" + summary + " net: " + totalSummary.getNetProfit());
            }
        } catch (IOException io) {
            io.printStackTrace();
        }

        System.out.println(" -- Total -------");
        System.out.println(totalSummary);
    }
}
