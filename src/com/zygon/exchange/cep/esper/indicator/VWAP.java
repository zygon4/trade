/**
 * 
 */

package com.zygon.exchange.cep.esper.indicator;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EventBean;
import com.xeiam.xchange.Currencies;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.zygon.exchange.InformationHandler;
import com.zygon.exchange.cep.esper.EsperEventIndicator;
import java.util.Collection;

/**
 *
 * @author zygon
 */
public class VWAP extends EsperEventIndicator {
    
    // TODO: real vwap
//    private static final String STATEMENT_FMT = "select * from " +
//                "VWAP(security='%s').win:length(%d) " +
//                "having avg(last) > 50.0";
    
    /*
     * expression midPrice { x => (x.buy + x.sell) / 2 } 
     * select midPrice(md) from MarketDataEvent as md
     */
    
    /*
     * expression weightedSentiment { (x, y) => x.price * y.sentiment } 
     * select weightedSentiment(md, news) 
     * from MarketDataEvent.std:lastevent() as md, NewsEvent.std:lastevent() news
     */
    
    // TODO/TBD: determine how to store tick delta information (volume change, last change, etc)
    // and use those for this.  Either store in Tick or manage Esper multi windows using insert statements.
    private static final String STATEMENT_FMT =
            "expression midPrice { x => (x.bid + x.ask) / 2 } " +
            "expression vwap { x => ((x.last * avg(x.vol))) / avg(x.vol) } " + // not really vwap because we don't have volume slices.. just the total
            "select vwap(tick) " +
            "from VWAP(security='%s').win:length(%d) as tick " +
            "having tick.bid < vwap(tick)";
    
    private static final String STMT = String.format(STATEMENT_FMT, Currencies.BTC, 30);
    
    private static Configuration create() {
        Configuration cepConfig = new Configuration();
        cepConfig.addEventType("VWAP", Ticker.class.getName());
        return cepConfig;
    }
    
    private VWAP(Collection<InformationHandler<EventBean>> eventProcessors, Configuration config, String statement) {
        super("WVAP", eventProcessors, config, statement);
    }
    
    public VWAP(Collection<InformationHandler<EventBean>> eventProcessors) {
        this(eventProcessors, create(), STMT);
    }
}
