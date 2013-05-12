/**
 *
 */
package com.zygon.trade.modules.model;

import com.zygon.trade.Module;
import com.zygon.trade.market.model.indication.InformationManager;
import com.zygon.trade.modules.data.DataModule;

/**
 *
 * @author zygon
 */
public class InformationModule extends Module {

    private final DataModule dataModule;
    private final InformationManager infoMgmt;
    
    public InformationModule(String name, DataModule dataModule, InformationManager infoMgmt) {
        super(name);
        
        this.dataModule = dataModule;
        this.infoMgmt = infoMgmt;
    }

    public InformationManager getInfoMgmt() {
        return this.infoMgmt;
    }
    
    @Override
    public Module[] getModules() {
        return null;
    }

    @Override
    public void initialize() {
        this.dataModule.initialize();
        this.infoMgmt.initialize();
    }

    @Override
    public void uninitialize() {
        this.infoMgmt.uninitialize();
        this.dataModule.uninitialize();
    }
}
