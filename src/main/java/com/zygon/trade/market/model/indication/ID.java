/**
 * 
 */

package com.zygon.trade.market.model.indication;

import com.zygon.trade.market.util.Classification;

/**
 *
 * @author zygon
 * 
 * This is the bud of a Indication ID class
 */
public class ID implements Identifier {
    
    private final String id;
    private final Classification classification;
    
    public ID(String id, Classification classification) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        if (classification == null) {
            throw new IllegalArgumentException("classification cannot be null");
        }
        
        this.id = id;
        this.classification = classification;
    }
    
    @Override
    public boolean equals(Identifier id) {
        if (id != null) {
            if (this.getID().equals(id.getID()) && this.classification == id.getClassification()) {           
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public Classification getClassification() {
        return this.classification;
    }

    @Override
    public String getID() {
        return this.id;
    }

    @Override
    public String toString() {
        return this.getID();
    }
}
