/**
 * 
 */

package com.zygon.trade.market.model.indication;

/**
 *
 * @author zygon
 * 
 * This is the bud of a Indication ID class
 */
public class ID implements Identifier {
    
    private final String id;
    private final Classification classification;
    private final Aggregation aggregation;

    public ID(String id, Classification classification, Aggregation aggregation) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        if (classification == null) {
            throw new IllegalArgumentException("classification cannot be null");
        }
        
        this.id = id;
        this.classification = classification;
        this.aggregation = aggregation;
    }

    public ID(String id, Classification classification) {
        this(id, classification, null);
    }
    
    @Override
    public boolean equals(Identifier id) {
        if (id != null) {
            if (this.getID().equals(id.getID()) && this.classification == id.getClassification()) {
                
                // TODO: finish
//                if (this.aggregation != null) {
//                    if (id )
//                }
                return true;
            }
        }
        
        return false;
    }

    @Override
    public Aggregation getAggregation() {
        return this.aggregation;
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
