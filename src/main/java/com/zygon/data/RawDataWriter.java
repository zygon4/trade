
package com.zygon.data;

import com.google.gson.stream.JsonWriter;
import com.zygon.trade.market.data.Ticker;
import com.zygon.trade.market.data.TickerWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Date;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;

/**
 *
 * @author zygon
 */
public class RawDataWriter<T> {

    private final String filePath;
    
    public RawDataWriter(String filePath) {
        this.filePath = filePath;
    }
    
    protected void doLog(T t, JsonWriter writer) throws IOException {
        
    }
    
    public synchronized void log (T t) throws IOException {
        
        File outFile = new File(this.filePath);
        
        if (!outFile.exists()) {
            outFile.createNewFile();
        }
        
        FileOutputStream fos = null;
        JsonWriter writer = null;
        
        try {
            fos = new FileOutputStream(this.filePath, true);
            StringWriter stringWriter = new StringWriter();
            writer = new JsonWriter(stringWriter);
            
            doLog(t, writer);
            
            String msg = stringWriter.toString();
            msg = msg.replace('\r', ' ');
            msg = msg.replace('\n', ' ');
            
            fos.write(msg.getBytes());
            fos.write('\n');
            
        } finally {
            if (writer != null) {
                try { writer.close(); } catch (Throwable ignore) {}
            }
            if (fos != null) {
                try { fos.close(); } catch (Throwable ignore) {}
            }
        }
    }
    
    public static void main (String[] args) throws IOException {
        TickerWriter writer = new TickerWriter("/tmp/tmp-ticker.txt");
        writer.log(
            new Ticker(
                "EUR", 
                new Date(), 
                "BOX", 
                "USD", 
                BigMoney.of(CurrencyUnit.USD, 1.34), 
                BigMoney.of(CurrencyUnit.USD, 1.37), 
                BigMoney.of(CurrencyUnit.USD, 1.32), 
                BigMoney.of(CurrencyUnit.USD, 1.37), 
                BigMoney.of(CurrencyUnit.USD, 1.33), 
                BigDecimal.valueOf(99.9)));
    }
    
}
