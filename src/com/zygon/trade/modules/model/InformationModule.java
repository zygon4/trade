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

    @Override
    public Module[] getModules() {
        return new Module[]{this.dataModule};
    }

    @Override
    public void initialize() {
        this.infoMgmt.initialize();
        this.dataModule.getDataManager().setInfoHandler(this.infoMgmt);
    }

    @Override
    public void uninitialize() {
        this.infoMgmt.uninitialize();
        this.dataModule.getDataManager().setInfoHandler(null);
    }
}
