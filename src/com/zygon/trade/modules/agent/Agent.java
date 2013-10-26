
package com.zygon.trade.modules.agent;

import com.zygon.trade.Module;
import com.zygon.trade.Schema;
import com.zygon.trade.agent.SimpleMAAgent;
import com.zygon.trade.modules.data.FeedModule;

/**
 *
 * @author zygon
 */
public class Agent extends Module {

    private static final Schema SCHEMA = new Schema("agent_schema.json");
    
    private final com.zygon.trade.agent.SimpleMAAgent agent;
    
    public Agent(String name) {
        super (name); // TODO: schema
        
        this.agent = new SimpleMAAgent(name+"_agent");
    }
    
    @Override
    public Module[] getModules() {
        return null;
    }

    @Override
    protected void hook() {
        super.hook();
        FeedModule module = (FeedModule) this.getModule("mtgox-ticker");
        module.register(this.agent);
    }
    
    @Override
    public void initialize() {
        
    }

    @Override
    protected void unhook() {
        super.unhook();
        FeedModule module = (FeedModule) this.getModule("mtgox-ticker");
        module.unregister(this.agent);
    }
    
    @Override
    public void uninitialize() {
        
    }
}
