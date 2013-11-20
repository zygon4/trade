
package com.zygon.trade.modules.execution.broker;

import com.zygon.trade.Module;
import com.zygon.trade.ParentModule;
import com.zygon.trade.execution.AccountController;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author zygon
 */
public class BrokerModule extends ParentModule {

    public static final String ID = "broker";
    
    public BrokerModule() {
        super(ID, null, Broker.class);
    }

    @Override
    protected void doWriteStatus(StringBuilder sb) {
        for (Broker broker : this.getBrokers()) {
            sb.append(broker.getDisplayname()).append('\n');
            broker.writeTradeSummary(sb);
            sb.append('\n');
        }
    }

    public AccountController getAccountController(String brokerName, String accountID) {
        
        Broker broker = this.getBroker(brokerName);
        
        if (broker != null) {
            return broker.getBroker().getExchange().getAccountController();
        }
        
        return null;
    }

    public Broker getBroker(String brokerName) {
        for (Broker broker : this.getBrokers()) {
            if (broker.getDisplayname().equals(brokerName)) {
                return broker;
            }
        }
        
        return null;
    }

    private Collection<Broker>getBrokers() {
        Collection<Broker> brokers = new ArrayList<Broker>();
        
        for (Module broker : this.getModules()) {
            brokers.add((Broker) broker);
        }
        
        return brokers;
    }
    
    @Override
    public void initialize() {
        
    }

    @Override
    public void uninitialize() {
        
    }
}
