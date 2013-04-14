/**
 * 
 */

package com.zygon.exchange.market.model.indication.technical;

import com.zygon.exchange.market.Price;
import com.zygon.exchange.market.model.indication.Indication;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class AveragePrice extends Indication {
    
    private static String getName(long duration, TimeUnit units) {
        return "AVG_PRICE_"+duration+"_"+units.toString();
    }
    
    private static String STMT_FMT = "select avg(price) "
            + "from %s(tradableIdentifier='%s').win:ext_timed(timestamp, %d %s)";
    
    public AveragePrice (String security, long duration, TimeUnit units) {
        super(getName(duration, units), 
              getName(duration, units),//              Price.class.getSimpleName(), 
              Price.class.getName(), 
              String.format(STMT_FMT, getName(duration, units), security, duration, EsperUtil.toEsper(units)));
    }
}
