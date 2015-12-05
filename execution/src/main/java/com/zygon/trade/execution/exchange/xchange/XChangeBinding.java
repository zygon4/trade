/**
 *
 */

package com.zygon.trade.execution.exchange.xchange;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.service.polling.marketdata.PollingMarketDataService;
import com.xeiam.xchange.service.polling.trade.PollingTradeService;
import com.zygon.trade.execution.AccountController;
import com.zygon.trade.execution.ExecutionController;
import com.zygon.trade.execution.OrderBookProvider;
import com.zygon.trade.execution.OrderProvider;
import com.zygon.trade.execution.TradeExecutor;

/**
 *
 * @author zygon
 */
public class XChangeBinding implements ExecutionController.Binding {

    private final XChangeAcctController accntController;
    private final OrderBookProvider orderBookProvider;
    private final OrderProvider orderProvider;
    private final TradeExecutor tradeExecutor;

    public XChangeBinding(ExchangeSpecification exchangeSpec) {

        Exchange createExchange = ExchangeFactory.INSTANCE.createExchange(exchangeSpec.getExchangeClassName());
        createExchange.applySpecification(exchangeSpec);

        this.accntController = new XChangeAcctController(createExchange.getPollingAccountService());

        PollingTradeService tradeService = createExchange.getPollingTradeService();
        PollingMarketDataService marketDataService = createExchange.getPollingMarketDataService();

        this.orderBookProvider = new XChangeOrderBookProvider(tradeService, marketDataService);
        this.orderProvider = new XChangeOrderProvider();
        this.tradeExecutor = new XChangeTradeExecutor(tradeService);
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
