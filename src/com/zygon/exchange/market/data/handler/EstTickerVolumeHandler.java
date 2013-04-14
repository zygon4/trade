/**
 * 
 */

package com.zygon.exchange.market.data.handler;

import com.xeiam.xchange.Currencies;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.zygon.exchange.InformationHandler;
import com.zygon.exchange.market.Volume;
import com.zygon.exchange.market.data.DataHandler;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;

/**
 *
 * @author zygon
 */
public class EstTickerVolumeHandler extends DataHandler<Ticker, Object> {
    
    public EstTickerVolumeHandler(String name, Collection<InformationHandler<Object>> targets) {
        super(name, targets);
    }
    
    private static final double SECS_IN_DAY = 60 * 60 * 24;
    
    @Override
    protected Volume translate(Ticker in) {
        // squirrely gymnastics.. the volume returned I believe is the 24 hour volume
        // so multiply by the 15 second ticker cache and divide by 24 hours
        // worth of seconds
        
        return new Volume(in.getTradableIdentifier(), 
                        Currencies.USD, 
                        (in.getVolume().multiply(BigDecimal.valueOf(15.0))).divide(BigDecimal.valueOf(SECS_IN_DAY), RoundingMode.DOWN), 
                        in.getTimestamp().getTime());
    }
}
