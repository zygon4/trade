
package com.zygon.trade.modules.agent;

import com.zygon.trade.Module;
import com.zygon.trade.ParentModule;
import com.zygon.trade.agent.PriceAgent;
import com.zygon.trade.agent.SimpleMAAgent;

/**
 *
 * @author zygon
 */
public class AgentModule extends ParentModule {

    // TBD: this child should be the product of a create command
    private final Agent agent = new Agent("a");
    
    public AgentModule(String name) {
        super(name, Agent.class);
        
        this.agent.setAgent(new PriceAgent(name+"_agent"));
    }

    @Override
    public Module[] getModules() {
        return new Module[] {
            this.agent
        };
    }
    
    @Override
    public void initialize() {
        
    }

    @Override
    public void uninitialize() {
        
    }
}
