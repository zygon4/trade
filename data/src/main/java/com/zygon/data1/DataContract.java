
package com.zygon.data1;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * A data contract service agreement
 *
 * @author zygon
 */
public class DataContract implements Comparable<DataContract> {
    
    private final String contractName;
    private final String dataIdentifier;
    private final int cacheTime;
    private final TimeUnit cacheTimeUnits;
    
    // TBD: key/cert - in the impl for now, quantity per time period, data contract price,
    // history of dips?
    
    public DataContract(String contractName, String dataIdentifier, int cacheTime, TimeUnit cacheTimeUnits) {
        this.contractName = contractName;
        this.dataIdentifier = dataIdentifier;
        this.cacheTime = cacheTime;
        this.cacheTimeUnits = cacheTimeUnits;
    }
    
    public DataContract(String contractName, String dataIdentifier) {
        this(contractName, dataIdentifier, 30, TimeUnit.SECONDS); // TODO: default constants
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if(obj == null || (this.getClass() != obj.getClass())) {
            return false;
        }
        
        DataContract other = (DataContract) obj;
        
        return this.contractName.equals(other.contractName) && 
               this.dataIdentifier.equals(other.dataIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.contractName, this.dataIdentifier);
    }
    
    @Override
    public int compareTo(DataContract t) {
        return this.contractName.compareTo(t.contractName);
    }

    public int getCacheTime() {
        return this.cacheTime;
    }
    
    public TimeUnit getCacheTimeUnits() {
        return this.cacheTimeUnits;
    }
    
    public String getContractName() {
        return this.contractName;
    }

    public String getDataIdentifier() {
        return this.dataIdentifier;
    }
}
