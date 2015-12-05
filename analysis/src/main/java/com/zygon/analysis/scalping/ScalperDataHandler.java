package com.zygon.analysis.scalping;

import com.xeiam.xchange.dto.trade.LimitOrder;
import com.zygon.data1.Data;
import com.zygon.data1.DataProvider;
import com.zygon.data1.contract.MarketDataContract;
import com.zygon.data1.data.OpenOrderBook;
import java.util.Collection;


/**
 *
 * @author zygon
 */
public class ScalperDataHandler implements DataProvider.DataHandler<MarketDataContract> {



    public static void main(String[] args) {

    }

    public ScalperDataHandler() {

    }

    private final Scalper scalper = new Scalper();

    @Override
    public void handle(MarketDataContract contract, Data data) {

        OpenOrderBook book = (OpenOrderBook) data;

        Collection<LimitOrder> generatedOrders = scalper.generateOrders(book);

    }
}
