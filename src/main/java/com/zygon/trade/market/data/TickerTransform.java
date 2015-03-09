
package com.zygon.trade.market.data;

import com.zygon.database.DataTransform;
import com.zygon.database.Persistable;
import com.zygon.database.PersistableComponent;
import com.zygon.database.cassandra.CassDAO;
import com.zygon.database.cassandra.CassandraPersistableComponent;
import java.math.BigDecimal;
import java.util.Date;
import me.prettyprint.hector.api.beans.Composite;

/**
 *
 * @author zygon
 */
public class TickerTransform implements DataTransform<Ticker> {

    @Override
    public Persistable<Composite, Date, Composite> transformTo(Ticker ticker) {
        Composite key = new Composite();
        
        key.addComponent(ticker.getTradableIdentifier(), CassDAO.UTF8Type);
        key.addComponent(ticker.getCurrency(), CassDAO.UTF8Type);
        key.addComponent(ticker.getSource(), CassDAO.UTF8Type);
        
        CassandraPersistableComponent<Composite> keyComponent = new CassandraPersistableComponent<Composite>(key, CassDAO.CompositeType);
        
        CassandraPersistableComponent<Date> nameComponent = new CassandraPersistableComponent<Date>(ticker.getTimestamp(), CassDAO.DateType);
        
        Composite value = new Composite();
        value.addComponent(ticker.getLast().toString(), CassDAO.UTF8Type);
        value.addComponent(ticker.getBid().toString(), CassDAO.UTF8Type);
        value.addComponent(ticker.getAsk().toString(), CassDAO.UTF8Type);
        value.addComponent(ticker.getHigh().toString(), CassDAO.UTF8Type);
        value.addComponent(ticker.getLow().toString(), CassDAO.UTF8Type);
        value.addComponent(ticker.getVolume().doubleValue(), CassDAO.DoubleType);
        
        CassandraPersistableComponent<Composite> valueComponent = new CassandraPersistableComponent<Composite>(value, CassDAO.CompositeType);
        
        Persistable<Composite, Date, Composite> persistable = new Persistable<Composite, Date, Composite>(keyComponent, nameComponent, valueComponent);
        
        return persistable;
    }
    
    private Ticker doTransformFrom(Persistable<Composite, Date, Composite> val) {
        
        /*
         * private Date timestamp;
            private String source;
            private String currency;
            * 
            private BigMoney last;
            private BigMoney bid;
            private BigMoney ask;
            private BigMoney high;
            private BigMoney low;
            private BigDecimal volume;
         */
        
        PersistableComponent<Composite> keyComponent = val.getKey();
        String tradeable = keyComponent.getComponent().get(0, CassDAO.UTF8Type);
        String currency = keyComponent.getComponent().get(1, CassDAO.UTF8Type);
        String source = keyComponent.getComponent().get(2, CassDAO.UTF8Type);
        
        PersistableComponent<Date> nameComponent = val.getName();
        Date timestamp = nameComponent.getComponent();
        
        PersistableComponent<Composite> value = val.getValue();
        BigDecimal last = new BigDecimal(value.getComponent().get(0, CassDAO.UTF8Type));
        BigDecimal bid = new BigDecimal(value.getComponent().get(1, CassDAO.UTF8Type));
        BigDecimal ask = new BigDecimal(value.getComponent().get(2, CassDAO.UTF8Type));
        BigDecimal high = new BigDecimal(value.getComponent().get(3, CassDAO.UTF8Type));
        BigDecimal low = new BigDecimal(value.getComponent().get(4, CassDAO.UTF8Type));
        BigDecimal volume = new BigDecimal(value.getComponent().get(5, CassDAO.DoubleType));
        
        return new Ticker(tradeable, timestamp, source, currency, last, bid, ask, high, low, volume);
    }
    
    @Override
    public <K, N, V> Ticker transformFrom(Persistable<K, N, V> val) {
        Persistable<Composite, Date, Composite> value = (Persistable<Composite, Date, Composite>) val;
        return this.doTransformFrom(value);
    }
}
