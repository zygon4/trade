/**
 * 
 */

package com.zygon.exchange.modules.trade;

import au.com.bytecode.opencsv.CSVReader;
import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.dto.marketdata.Trade;
import com.zygon.exchange.trade.exchange.mtgox.MtGoxExchange;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import org.joda.money.BigMoney;

/**
 *
 * @author zygon
 */
public class MtGox extends AbstractTradeModule<Ticker> {

    private final boolean loadHistoric;
    
    public MtGox(boolean loadHistoric) {
        super("MtGox", new MtGoxExchange());
        
        this.loadHistoric = loadHistoric;
    }
    
//    // again, consider using a library for csv stuff .. OGNL or something
//    private static Trade generate(String[] csv) {
//        long timestamp = Long.decode(csv[0]);
//        BigMoney price = BigMoney.parse("USD " + csv[1]);
//        BigDecimal vol = BigDecimal.valueOf(Double.parseDouble(csv[2]));
//        return new Trade(Order.OrderType.BID, vol, null, null, price, null)
//        return new Trade(Security.BTC.name(), price, vol, timestamp);
//    }
//    
//    // ghetto translation into a Tick such that it can be dispatched.
//    private static Ticker translate (Trade trade) {
//        return new Ticker(trade.getSecurity(), trade.getBigPrice(), trade.getBigPrice(), 
//                trade.getBigPrice(), trade.getBigPrice(), trade.getBigPrice(), 
//                trade.getBigVol(), trade.getTimestamp());
//    }

    @Override
    public void initialize() {
        super.initialize();
        
        // TBD: consider pushing this up to the parent class and having some 
        // sort of historic data provider
        if (this.loadHistoric) {
//            CSVReader reader = null;
//            try {
//                // TBD: a better resource load mech
//                reader = new CSVReader(new BufferedReader(new FileReader("/media/external/code/src/exchange/src/com/zygon/exchange/modules/trade/trades.csv")));
//                
//                String[] line = null;
//                while ((line = reader.readNext()) != null) {
//                    Trade trade = generate(line);
//                    Ticker tick = translate(trade);
//                    this.getExchange().getFeedDistributor().send(tick);
//                }
//                
//            } catch (FileNotFoundException fnfe) {
//                // TODO: ..log
//                fnfe.printStackTrace();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            } finally {
//                try { reader.close(); } catch (IOException ignore) {}
//            }
        }
    }
}
