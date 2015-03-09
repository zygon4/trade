
package com.zygon.trade.market.data;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 *
 * @author zygon
 */
public class TickerUtil {

    private TickerUtil() {}

    public static double getMidPrice(Ticker in) {
        return in.getAsk().add(in.getBid()).divide(BigDecimal.valueOf(2), RoundingMode.UP).doubleValue();
    }
    
    public static Ticker read(JsonReader reader) throws IOException {
        
        String tradeableIdentifer = null;
        Date timestamp = null;
        String source = null;
        String currency = null;
        BigDecimal last = null;
        BigDecimal bid = null;
        BigDecimal ask = null;
        BigDecimal high = null;
        BigDecimal low = null;
        BigDecimal volume = null;
        
        reader.beginObject();
        while (reader.hasNext()) {

           String field = reader.nextName();
           if (field.equals("tradeableIdentifer")) {
               tradeableIdentifer = reader.nextString();
           } else if (field.equals("timestamp")) {
               timestamp = new Date(reader.nextLong());
           } else if (field.equals("source")) {
               source = reader.nextString();
           } else if (field.equals("currency")) {
               currency = reader.nextString();
           } else if (field.equals("last")) {
               last = BigDecimal.valueOf(reader.nextDouble());
           } else if (field.equals("bid")) {
               bid = BigDecimal.valueOf(reader.nextDouble());
           } else if (field.equals("ask")) {
               ask = BigDecimal.valueOf(reader.nextDouble());
           } else if (field.equals("high")) {
               high = BigDecimal.valueOf(reader.nextDouble());
           } else if (field.equals("low")) {
               low = BigDecimal.valueOf(reader.nextDouble());
           } else if (field.equals("volume")) {
               volume = BigDecimal.valueOf(reader.nextDouble());
           }
        }
        reader.endObject();
        
        return new Ticker(tradeableIdentifer, timestamp, source, currency, last, bid, ask, high, low, volume);
    }
    
    public static void write(Ticker t, JsonWriter writer) throws IOException {
        
        writer.beginObject();
        
        writer.name("tradeableIdentifer").value(t.getTradableIdentifier());
        writer.name("timestamp").value(t.getTimestamp().getTime());
        writer.name("source").value(t.getSource());
        writer.name("currency").value(t.getCurrency());
        writer.name("last").value(t.getLast().doubleValue());
        writer.name("bid").value(t.getBid().doubleValue());
        writer.name("ask").value(t.getAsk().doubleValue());
        writer.name("high").value(t.getHigh().doubleValue());
        writer.name("low").value(t.getLow().doubleValue());
        writer.name("volume").value(t.getVolume().doubleValue());
        
        writer.endObject();
    }
}
