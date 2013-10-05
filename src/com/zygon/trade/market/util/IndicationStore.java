package com.zygon.trade.market.util;

import com.zygon.trade.market.model.indication.Aggregation;
import com.zygon.trade.market.model.indication.Identifier;
import com.zygon.trade.market.model.indication.Indication;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zygon
 */
public final class IndicationStore {
    
    private  String getKey(Identifier id, String tradeableIdentifier, Aggregation aggregation) {
        String key = id.getID()+"_"+tradeableIdentifier;
        if (aggregation != null) {
            key += "_"+aggregation.getDuration().name()+"_"+aggregation.getUnits().name()+"_"+aggregation.getType().getVal();
        }
        return key;
    }
    
    private final Map<String, Indication> indications = new HashMap<>();
    
    public final void put (Indication indication, Aggregation aggregation) {
        if (indication == null) {
            throw new IllegalArgumentException();
        }
        String key = this.getKey(indication.getId(), indication.getTradableIdentifier(), aggregation);
        
        synchronized(this.indications) {
            this.indications.put(key, indication);
        }
    }
    
    public final Indication get (Identifier id, String tradeableIdentifier, Aggregation aggregation) {
        if (id == null || tradeableIdentifier == null) {
            throw new IllegalArgumentException();
        }
        Indication val = null;
        String key = this.getKey(id, tradeableIdentifier, aggregation);
        
        synchronized (this.indications) {
            val = this.indications.get(key);
        }
        
        return val;
    }
    
    public final Indication get (Indication indication, Aggregation aggregation) {
        return this.get(indication.getId(), indication.getTradableIdentifier(), aggregation);
    }
}
