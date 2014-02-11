
package com.zygon.trade.modules.data;

import com.zygon.trade.ParentModule;

/**
 *
 * @author zygon
 */
public class DataSetModule extends ParentModule {

    public static final String ID = "dataset";
    
    public DataSetModule() {
        super(ID, DataSet.class);
    }

    @Override
    public void initialize() {
        // Nothing to do
    }

    @Override
    public void uninitialize() {
        // Nothing to do
    }
}
