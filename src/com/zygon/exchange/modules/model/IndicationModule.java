/**
 *
 */
package com.zygon.exchange.modules.model;

import com.zygon.exchange.Module;
import com.zygon.exchange.market.model.indication.InformationManager;

/**
 *
 * @author zygon
 */
public class IndicationModule extends Module {

    private final InformationManager infoMgmt;
    
    public IndicationModule(String name, InformationManager infoMgmt) {
        super(name);
        
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
        // nothing to do..
    }

    @Override
    public void uninitialize() {
        // nothing to do..
    }
}
