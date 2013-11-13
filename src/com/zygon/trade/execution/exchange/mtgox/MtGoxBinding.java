/**
 * 
 */

package com.zygon.trade.execution.exchange.mtgox;

import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.mtgox.v2.service.polling.MtGoxPollingAccountService;
import com.xeiam.xchange.mtgox.v2.service.polling.MtGoxPollingMarketDataService;
import com.xeiam.xchange.mtgox.v2.service.polling.MtGoxPollingTradeService;
import com.zygon.trade.execution.AccountController;
import com.zygon.trade.execution.ExecutionController;
import com.zygon.trade.execution.OrderBookProvider;
import com.zygon.trade.execution.OrderProvider;
import com.zygon.trade.execution.TradeExecutor;
import org.joda.money.CurrencyUnit;

/**
 *
 * @author zygon
 */
public class MtGoxBinding implements ExecutionController.Binding {

    private final MtGoxAcctController accntController;
    private final OrderBookProvider orderBookProvider;
    private final OrderProvider orderProvider;
    private final TradeExecutor tradeExecutor;

    public MtGoxBinding(ExchangeSpecification exchangeSpec, CurrencyUnit currency) {
        this.accntController = new MtGoxAcctController(new MtGoxPollingAccountService(exchangeSpec));
        
        MtGoxPollingTradeService tradeService = new MtGoxPollingTradeService(exchangeSpec);
        MtGoxPollingMarketDataService marketDataService = new MtGoxPollingMarketDataService(exchangeSpec);
        
        this.orderBookProvider = new MtGoxOrderBookProvider(tradeService, marketDataService);
        this.orderProvider = new MtGoxOrderProvider(currency);
        this.tradeExecutor = new MtGoxTradeExecutor(tradeService);
    }
    
    @Override
    public AccountController getAccountController() {
        return this.accntController;
    }

    @Override
    public OrderBookProvider getOrderBookProvider() {
        return this.orderBookProvider;
    }

    @Override
    public OrderProvider getOrderProvider() {
        return this.orderProvider;
    }

    @Override
    public TradeExecutor getTradeExecutor() {
        return this.tradeExecutor;
    }
}
