
package com.zygon.trade.market.data;

import com.google.gson.stream.JsonReader;
import com.zygon.data.RawDataReader;
import java.io.IOException;

/**
 *
 * @author zygon
 */
public class TickerReader extends RawDataReader<Ticker> {

    public TickerReader(String filePath) {
        super(filePath);
    }

    @Override
    protected Ticker doRead(JsonReader reader) throws IOException {
        return TickerUtil.read(reader);
    }
}
