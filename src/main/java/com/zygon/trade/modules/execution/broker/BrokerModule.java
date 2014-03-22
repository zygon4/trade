
package com.zygon.trade.modules.execution.broker;

import com.zygon.trade.ParentModule;
import com.zygon.trade.execution.AccountController;
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
        Collection<Broker> brokers = this.getChildrenModules();
        
        for (Broker broker : brokers) {
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
        Collection<Broker> brokers = this.getChildrenModules();
        
        for (Broker broker : brokers) {
            if (broker.getDisplayname().equals(brokerName)) {
                return broker;
            }
        }
        
        return null;
    }
    
    @Override
    public void initialize() {
        
    }

    @Override
    public void uninitialize() {
        
    }
}
