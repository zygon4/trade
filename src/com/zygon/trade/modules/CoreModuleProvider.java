
package com.zygon.trade;

/**
 * So what's the deal with this class?
 * The intent is to build the core modules.
 * The problem is that this is executed without
 * knowledge of the other modules and specifically
 * their execution bindings.. how to bring to-
 * gether the ExecutionModule with bindings found
 * elsewhere?
 *
 * @author zygon
 */
public class CoreModuleProvider implements ModuleProvider {
    
    public Module[] getModules() {
	return null;
    }
}
