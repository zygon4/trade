
package com.zygon.trade.modules.agent;

import com.zygon.trade.Module;
import com.zygon.trade.ParentModule;

/**
 *
 * @author zygon
 */
public class AgentModule extends ParentModule {

    // TBD: this child should be the product of a create command
    private final Agent child = new Agent("simpleAgent");
    
    public AgentModule(String name) {
        super(name, Agent.class);
    }

    @Override
    public Module[] getModules() {
        return new Module[] {this.child};
    }
    
    @Override
    public void initialize() {
        
    }

    @Override
    public void uninitialize() {
        
    }
}
