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
    
    // lame string key for now - but it should work fine.
    
    // Tradable identifier??
    private  String getKey(Identifier id, Aggregation aggregation) {
        return id.getID()+"_"+aggregation.getDuration().name()+"_"+
                aggregation.getUnits().name()+"_"+aggregation.getType().getVal();
    }
    
    private final Map<String, Indication> indications = new HashMap<>();
    
    public final void put (Identifier id, Aggregation aggregation, Indication indication) {
        String key = this.getKey(id, aggregation);
        
        synchronized(this.indications) {
            this.indications.put(key, indication);
        }
    }
    
    public final Indication get (Identifier id, Aggregation aggregation) {
        Indication val = null;
        String key = this.getKey(id, aggregation);
        
        synchronized (this.indications) {
            val = this.indications.get(key);
        }
        
        return val;
    }
}
