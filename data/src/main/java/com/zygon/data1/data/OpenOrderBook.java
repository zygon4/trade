
package com.zygon.data1.data;

import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.zygon.data1.Data;
import java.util.List;

/**
 *
 * @author zygon
 */
public class OpenOrderBook implements Data {

    private final OrderBook book;

    public OpenOrderBook(OrderBook book) {
        this.book = book;
    }

    public List<LimitOrder> getAsks() {
        return book.getAsks();
    }

    public List<LimitOrder> getBids() {
        return book.getBids();
    }
}
