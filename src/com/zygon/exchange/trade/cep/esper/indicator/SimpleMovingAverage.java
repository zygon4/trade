/**
 * 
 */

package com.zygon.exchange.trade.cep.esper.indicator;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EventBean;
import com.xeiam.xchange.Currencies;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.zygon.exchange.trade.cep.EventProcessor;
import com.zygon.exchange.trade.cep.esper.EsperEventIndicator;

/**
 *
 * @author zygon
 */
public class SimpleMovingAverage extends EsperEventIndicator {
    
//    private static final String STATEMENT_FMT =
//            "expression midPrice { x => (x.bid + x.ask) / 2 } " +
//            "select avg(midPrice(tick)) " +
//            "from SMA(security='%s').win:time(%d sec) as tick " +
//            "having ( (avg(midPrice(tick)) < (midPrice(tick) - %d)) or "
//            + "       (avg(midPrice(tick)) > (midPrice(tick) + %d)))";
    
    private static final String STATEMENT_FMT =
            "expression midPrice { x => (x.bid + x.ask) / 2 } " +
            "select avg(tick.last) " +
            "from SMA(security='%s').win:ext_timed(timestamp, %d seconds) as tick";
    
//    private static final String STMT = String.format(STATEMENT_FMT, Security.BTC.name(), 30);
    
    private static Configuration create() {
        Configuration cepConfig = new Configuration();
        cepConfig.addEventType("SMA", Ticker.class.getName());
        return cepConfig;
    }
    
    private SimpleMovingAverage(EventProcessor<EventBean>[] eventProcessors, Configuration config, String statement) {
        super(eventProcessors, config, statement);
    }
    
    public SimpleMovingAverage(EventProcessor<EventBean>[] eventProcessors, int minutes, int percentFromMid) {
        this(eventProcessors, create(), String.format(STATEMENT_FMT, Currencies.BTC, minutes, percentFromMid, percentFromMid));
    }
}
