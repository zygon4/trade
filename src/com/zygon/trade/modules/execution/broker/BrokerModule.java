
package com.zygon.trade.modules.execution.broker;

import com.zygon.trade.Module;
import com.zygon.trade.ParentModule;
import com.zygon.trade.execution.AccountController;
import com.zygon.trade.execution.exchange.simulation.SimulationBinding;
import com.zygon.trade.trade.TradeBroker;

/**
 *
 * @author zygon
 */
public class BrokerModule extends ParentModule {

    public static final String ID = "broker";
    
    private final Broker broker = new Broker("mtgox");
    {
        this.broker.setBroker(new TradeBroker("joe", SimulationBinding.createInstance()));
    }
    private final Broker[] brokers = {broker};
    
    public BrokerModule() {
        super(ID, null, Broker.class);
    }

    @Override
    protected void doWriteStatus(StringBuilder sb) {
        for (Broker broker : this.brokers) {
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
        for (Broker broker : this.brokers) {
            if (broker.getDisplayname().equals(brokerName)) {
                return broker;
            }
        }
        
        return null;
    }
    
    @Override
    public Module[] getModules() {
        return this.brokers;
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void uninitialize() {
        
    }
}
