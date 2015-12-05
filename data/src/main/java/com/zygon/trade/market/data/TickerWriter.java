
package com.zygon.trade.market.data;

import com.google.gson.stream.JsonWriter;
import com.zygon.data.RawDataWriter;
import java.io.IOException;

/**
 *
 * @author zygon
 */
public class TickerWriter extends RawDataWriter<Ticker> {

    public TickerWriter(String filePath) {
        super(filePath);
    }
    
    @Override
    protected void doLog(Ticker t, JsonWriter writer) throws IOException {
        TickerUtil.write(t, writer);
    }
}
