/**
 * 
 */

package com.zygon.exchange.modules.trade;

import com.zygon.exchange.Module;
import com.zygon.exchange.trade.FeedHandler;
import com.zygon.exchange.trade.FeedProcessor;
import com.zygon.exchange.trade.FeedProvider;
import com.zygon.exchange.trade.exchange.Exchange;

/**
 *
 * @author zygon
 */
public abstract class AbstractTradeModule<FEED_TYPE> extends Module {
    
    private final Exchange<FEED_TYPE> exchange;
    private final FeedProvider<FEED_TYPE> feedProvider;
    private final FeedHandler<FEED_TYPE> feedDistributor;
    
    private final FeedProcessor feedManager = new FeedProcessor();
    
    protected AbstractTradeModule(String name, Exchange<FEED_TYPE> exchange) {
        super(name);
        
        this.exchange = exchange;
        this.feedProvider = this.exchange.getFeedProvider();
        this.feedDistributor = this.exchange.getFeedDistributor();
    }

    protected final Exchange<FEED_TYPE> getExchange() {
        return this.exchange;
    }
    
    @Override
    public Module[] getModules() {
        return null;
    }
    
    @Override
    public void initialize() {
        this.feedManager.register(this.feedProvider, this.feedDistributor);
        
        // TOD: move this somewhere else
        this.feedManager.initialize();
        
//        System.out.println(this.exchange.getFeedProvider().get());
//        try {Thread.sleep (1000); } catch (Throwable ignore) {}
//        System.out.println(this.exchange.getTicker());
        
//        this.exchange.placeMarketOrder(new Order(Order.Type.BID, new BigDecimal(0.1)));
    }

    @Override
    public void uninitialize() {
        this.feedManager.uninitialize();
        this.feedManager.unregister(this.feedProvider);
    }
}
