
package com.zygon.trade.market.data;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;

/**
 *
 * @author zygon
 */
public class TickerUtil {

    private TickerUtil() {}

    public static double getMidPrice(Ticker in) {
        return in.getAsk().plus(in.getBid()).dividedBy(2, RoundingMode.UP).getAmount().doubleValue();
    }
    
    public static Ticker read(JsonReader reader) throws IOException {
        
        String tradeableIdentifer = null;
        Date timestamp = null;
        String source = null;
        String currency = null;
        BigMoney last = null;
        BigMoney bid = null;
        BigMoney ask = null;
        BigMoney high = null;
        BigMoney low = null;
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
               last = BigMoney.of(CurrencyUnit.of(currency), reader.nextDouble());
           } else if (field.equals("bid")) {
               bid = BigMoney.of(CurrencyUnit.of(currency), reader.nextDouble());
           } else if (field.equals("ask")) {
               ask = BigMoney.of(CurrencyUnit.of(currency), reader.nextDouble());
           } else if (field.equals("high")) {
               high = BigMoney.of(CurrencyUnit.of(currency), reader.nextDouble());
           } else if (field.equals("low")) {
               low = BigMoney.of(CurrencyUnit.of(currency), reader.nextDouble());
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
        writer.name("last").value(t.getLast().getAmount().doubleValue());
        writer.name("bid").value(t.getBid().getAmount().doubleValue());
        writer.name("ask").value(t.getAsk().getAmount().doubleValue());
        writer.name("high").value(t.getHigh().getAmount().doubleValue());
        writer.name("low").value(t.getLow().getAmount().doubleValue());
        writer.name("volume").value(t.getVolume().doubleValue());
        
        writer.endObject();
    }
}
