
package com.zygon.trade.modules.agent;

import com.zygon.trade.ParentModule;
import java.util.Collection;

/**
 *
 * @author zygon
 */
public class AgentModule extends ParentModule {

    public static final String ID = "agent";
    
    public AgentModule() {
        super(ID, Agent.class);
    }

    @Override
    protected void doWriteStatus(StringBuilder sb) {
        Collection<Agent> agents = this.getChildrenModules();
        for (Agent agent : agents) {
            sb.append(agent.getDisplayname()).append(": ").append(agent.getAgent().getStrategySummary()).append('\n');
        }
    }
    
    @Override
    public void initialize() {
        
    }

    @Override
    public void uninitialize() {
        
    }
}
