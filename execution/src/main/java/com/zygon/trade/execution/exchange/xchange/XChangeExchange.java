
package com.zygon.trade.execution.exchange.xchange;

import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.mtgox.v2.service.polling.MtGoxPollingAccountService;
import com.xeiam.xchange.mtgox.v2.service.polling.MtGoxPollingMarketDataService;
import com.xeiam.xchange.mtgox.v2.service.polling.MtGoxPollingTradeService;
import com.xeiam.xchange.mtgox.v2.service.streaming.MtGoxStreamingConfiguration;
import com.xeiam.xchange.service.streaming.StreamingExchangeService;
import com.zygon.trade.execution.exchange.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * XChange
 *
 * @author zygon
 */
public class XChangeExchange extends Exchange {

    private static final Logger log = LoggerFactory.getLogger(XChangeExchange.class);

    private static MtGoxStreamingConfiguration getStreamingConfig() {
        MtGoxStreamingConfiguration mtGoxStreamingConfiguration = new MtGoxStreamingConfiguration(10, 10000, 60000, true, "TODO");
        return mtGoxStreamingConfiguration;
    }

    private static ExchangeSpecification getExchangeSpecification(Class<?> xChangeClazz) {
        ExchangeSpecification spec = new ExchangeSpecification(xChangeClazz.getTypeName());
        // what else?
//        spec.setSslUri("https://mtgox.com");

        return spec;
    }

    private static StreamingExchangeService getService(Class<?> xChangeClazz) {
        return ExchangeFactory.INSTANCE.createExchange(getExchangeSpecification(xChangeClazz)).getStreamingExchangeService(getStreamingConfig());
    }

    public XChangeExchange(Class<?> xChangeClazz) {
        super (new XChangeAcctController(new MtGoxPollingAccountService(getExchangeSpecification(xChangeClazz))),
               new XChangeOrderBookProvider(new MtGoxPollingTradeService(getExchangeSpecification(xChangeClazz)),
                       new MtGoxPollingMarketDataService(getExchangeSpecification(xChangeClazz))),
               new XChangeOrderProvider(),
               new XChangeTradeExecutor(new MtGoxPollingTradeService(getExchangeSpecification(xChangeClazz))),
               new XChangeEventProvider(getService(xChangeClazz)));
    }
}
