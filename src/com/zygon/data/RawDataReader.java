
package com.zygon.data;

import com.google.gson.stream.JsonReader;
import com.zygon.trade.market.data.Ticker;
import com.zygon.trade.market.data.TickerReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zygon
 */
public class RawDataReader<T> implements DataSource<T> {

    private static final Logger logger = LoggerFactory.getLogger(RawDataReader.class);
    private final String filePath;
    
    public RawDataReader(String filePath) {
        this.filePath = filePath;
    }
    
    protected T doRead(JsonReader reader) throws IOException {
        return null;
    }

    @Override
    public void writeData(Handler<T> handler) throws IOException {
        BufferedReader bufferedReader = null;
        
        try {
            bufferedReader = new BufferedReader(new FileReader(this.filePath));
            
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                JsonReader jsonReader = new JsonReader(new StringReader(line));
                T t = null;
                
                try {
                    t = doRead(jsonReader);
                } catch (IOException io) {
                    logger.debug("Error parsing line: " + line, io);
                }
                
                if (t != null) {
                    handler.handle(t);
                }
                
                try { jsonReader.close(); } catch (Throwable ignore) {}
            }
            
        } finally {
            if (bufferedReader != null) {
                try { bufferedReader.close(); } catch (Throwable ignore) {}
            }
        }
    }
    
    public static void main (String[] args) throws IOException {
        TickerReader reader = new TickerReader("/tmp/tmp-ticker.txt");
        
        reader.writeData(new Handler<Ticker>() {

            @Override
            public void handle(Ticker r) {
                System.out.println(r);
            }
        });
    }
}
