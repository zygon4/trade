
package com.zygon.data;

import com.xeiam.xchange.currency.Currencies;
import com.xeiam.xchange.dto.marketdata.Ticker;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author davec
 */
public class FeedTester {

    private static Properties getProps(String cls, String tradeable, String currency) {
        Properties props = new Properties();
        props.setProperty(Context.PROP_CLS, cls);
        props.setProperty(Context.PROP_NAME, cls+":"+tradeable+"|"+currency);
        props.setProperty("tradeable", tradeable);
        props.setProperty("currency", currency);
        return props;
    }
    
    public static void main(String[] args) throws InterruptedException {
        
        String[][] pairs = {
            {Currencies.BTC, Currencies.USD},
            {Currencies.BTC, Currencies.JPY},
            {Currencies.BTC, Currencies.GBP},
        };
        
        List<EventFeed<Ticker>> eventFeeds = new ArrayList<EventFeed<Ticker>>();
        
        FeedProvider fp = new FeedProviderImpl();
        
        for (String[] pair : pairs) {
            Feed feed = fp.createFeed(new Context(getProps("enterprise.db.data.feed.currency.mtgox.MtGoxFeed", pair[0], pair[1])));
            eventFeeds.add((EventFeed<Ticker>) feed);
        }
        
        Feed krakenFeed = fp.createFeed(new Context(getProps("enterprise.db.data.feed.currency.kraken.KrakenFeed", Currencies.BTC, Currencies.USD)));
        eventFeeds.add((EventFeed<Ticker>) krakenFeed);
        
        final EventFeed.Registration<Ticker> reg = new EventFeed.Registration<Ticker>() {

            @Override
            public void handle(Ticker r) {
                System.out.println(r);
            }
        };
        
        
        for (EventFeed<Ticker> feed : eventFeeds) {
            feed.register(reg);
        }
        
        Thread.sleep (120000);
        
        for (EventFeed<Ticker> feed : eventFeeds) {
            feed.unregister(reg);
        }
        
        Thread.sleep (5000);
    }
    
}
