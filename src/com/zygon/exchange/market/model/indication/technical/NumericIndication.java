/**
 * 
 */

package com.zygon.exchange.market.model.indication.technical;

import com.zygon.exchange.market.model.indication.Indication;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class NumericIndication extends Indication {
    
    private static String getName(Classification clazz, Aggregation type, long duration, TimeUnit units) {
        return type.name()+"_"+clazz.name()+"_"+duration+"_"+units.toString();
    }
    
    private static String STMT_FMT = "select tradableIdentifier, %s(%s), timestamp "
            + "from %s(tradableIdentifier='%s').win:ext_timed(timestamp, %d %s)";
    
    public NumericIndication (String security, Classification clazz, Aggregation type, long duration, TimeUnit units) {
        super(getName(clazz, type, duration, units), 
              getName(clazz, type, duration, units),
              clazz.getClassName(), 
              String.format(STMT_FMT, type.getEsperToken(), clazz.getEsperToken(), getName(clazz, type, duration, units), 
                security, duration, EsperUtil.toEsper(units)));
    }
}
