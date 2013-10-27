
package com.zygon.trade.modules.agent;

import com.zygon.trade.Configuration;
import com.zygon.trade.Module;
import com.zygon.trade.Schema;
import com.zygon.trade.modules.data.DataModule;

/**
 *
 * @author zygon
 */
public class Agent extends Module {

    private static final Schema SCHEMA = new Schema("agent_schema.json");
    
    private com.zygon.trade.agent.Agent agent = null;
    
    public Agent(String name) {
        super (name); // TODO: schema
    }
    
    @Override
    public Module[] getModules() {
        return null;
    }

    @Override
    protected void hook() {
        super.hook();
        DataModule module = (DataModule) this.getModule("data");
        module.register(this.agent);
    }
    
    @Override
    public void initialize() {
        
    }

    // this is for testing/development only
    /*pkg*/ void setAgent(com.zygon.trade.agent.Agent agent) {
        this.agent = agent;
    }
    
    @Override
    protected void unhook() {
        super.unhook();
        DataModule module = (DataModule) this.getModule("data");
        module.unregister(this.agent);
    }
    
    @Override
    public void uninitialize() {
        
    }

    @Override
    public void configure(Configuration configuration) {
        super.configure(configuration); //To change body of generated methods, choose Tools | Templates.
    }
}
