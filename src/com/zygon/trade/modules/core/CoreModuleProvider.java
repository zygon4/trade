/**
 * 
 */

package com.zygon.trade.modules.core;

import com.zygon.trade.Module;
import com.zygon.trade.ModuleProvider;

/**
 *
 * @author zygon
 */
public class CoreModuleProvider implements ModuleProvider {

    private final UIModule uiModule;
    private final Module[] modules;
    
    public CoreModuleProvider() {
        this.uiModule = new UIModule("UI");
        this.modules = new Module[]{ this.uiModule };
    }
    
    @Override
    public Module[] getModules() {
        return this.modules;
    }
}
