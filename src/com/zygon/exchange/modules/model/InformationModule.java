/**
 *
 */
package com.zygon.exchange.modules.model;

import com.zygon.exchange.Module;
import com.zygon.exchange.market.model.indication.InformationManager;
import com.zygon.exchange.modules.data.DataModule;

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
