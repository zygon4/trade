
package com.zygon.trade;

import com.xeiam.xchange.currency.CurrencyPair;
import com.zygon.trade.modules.account.Account;
import com.zygon.trade.modules.account.AccountModule;
import com.zygon.trade.modules.agent.Agent;
import com.zygon.trade.modules.agent.AgentModule;
import com.zygon.trade.modules.ui.CLIModule;
import com.zygon.trade.modules.data.DataFeed;
import com.zygon.trade.modules.data.DataModule;
import com.zygon.trade.modules.execution.broker.Broker;
import com.zygon.trade.modules.execution.broker.BrokerModule;
import com.zygon.trade.modules.ui.UserInterfaceModule;
import com.zygon.trade.modules.ui.WebConsole;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zygon
 */
public class InMemoryInstallableStorage implements InstallableStorage {

    private final Map<String, MetaData> metadataById = new HashMap<>();
    
    {
        DataModule data = new DataModule();
        Configuration dataConfig = new Configuration(data.getSchema());
        dataConfig.setValue("name", data.getDisplayname());
        this.metadataById.put("data", new MetaData("data", "com.zygon.trade.modules.data.DataModule", data, dataConfig));
        DataFeed mtgoxTicker = new DataFeed("mtgox-ticker");
        Configuration mtgoxConfig = new Configuration(mtgoxTicker.getSchema());
        
        
        mtgoxConfig.setValue("name", mtgoxTicker.getDisplayname());
        mtgoxConfig.setValue("class", "com.zygon.trade.market.data.mtgox.MtGoxFeed");
        mtgoxConfig.setValue("tradeable", CurrencyPair.BTC_USD.baseCurrency);
        mtgoxConfig.setValue("currency", CurrencyPair.BTC_USD.counterCurrency);
        this.metadataById.put(mtgoxTicker.getDisplayname(), new MetaData(mtgoxTicker.getDisplayname(), "com.zygon.trade.modules.data.DataFeed", mtgoxTicker, mtgoxConfig));
        
        
        BrokerModule brokerModule = new BrokerModule();
        Configuration brokerConfig = new Configuration(brokerModule.getSchema());
        brokerConfig.setValue("name", brokerModule.getDisplayname());
        this.metadataById.put(brokerModule.getDisplayname(), new MetaData(brokerModule.getDisplayname(), "com.zygon.trade.modules.execution.broker.BrokerModule", brokerModule, brokerConfig));
        
        
        Broker broker = new Broker("mtgox");
        Configuration mtgoxBrokerConfig = new Configuration(broker.getSchema());
        mtgoxBrokerConfig.setValue("accountId", "joe");
        mtgoxBrokerConfig.setValue("name", broker.getDisplayname());
        this.metadataById.put(broker.getDisplayname(), new MetaData(broker.getDisplayname(), "com.zygon.trade.modules.execution.broker.Broker", broker, mtgoxBrokerConfig));
        
        
        AccountModule accountModule = new AccountModule();
        Configuration accountModuleConfig = new Configuration(accountModule.getSchema());
        accountModuleConfig.setValue("name", accountModule.getDisplayname());
        this.metadataById.put(accountModule.getDisplayname(), new MetaData(accountModule.getDisplayname(), "com.zygon.trade.modules.account.AccountModule", accountModule, accountModuleConfig));
        
        
        Account account = new Account("joe");
        Configuration accountConfig = new Configuration(account.getSchema());
        accountConfig.setValue("name", account.getDisplayname());
        accountConfig.setValue("broker", "mtgox");
        accountConfig.setValue("accountId", "joe");
        this.metadataById.put(account.getDisplayname(), new MetaData(account.getDisplayname(), "com.zygon.trade.modules.account.Account", account, accountConfig));
        
        
        AgentModule agentModule = new AgentModule();
        Configuration agentModuleConfig = new Configuration(agentModule.getSchema());
        agentModuleConfig.setValue("name", agentModule.getDisplayname());
        this.metadataById.put(agentModule.getDisplayname(), new MetaData(agentModule.getDisplayname(), "com.zygon.trade.modules.agent.AgentModule", agentModule, agentModuleConfig));
        
        
        Agent agent = new Agent("macd");
        Configuration agentConfig = new Configuration(agent.getSchema());
        agentConfig.setValue("name", agent.getDisplayname());
        // TBD: set: interpretters, strategy, etc. For now it's hardcoded in the agent
        agentConfig.setValue("broker", "mtgox");
        this.metadataById.put(agent.getDisplayname(), new MetaData(agent.getDisplayname(), "com.zygon.trade.modules.agent.Agent", agent, agentConfig));
        
        
        CLIModule cliModule = new CLIModule("cli");
        Configuration cliConfig = new Configuration(cliModule.getSchema());
        cliConfig.setValue("name", cliModule.getDisplayname());
        this.metadataById.put(cliModule.getDisplayname(), new MetaData(cliModule.getDisplayname(), "com.zygon.trade.modules.ui.CLIModule", cliModule, cliConfig));
        
        UserInterfaceModule userInterfaceModule = new UserInterfaceModule();
        Configuration userInterfaceModuleConfig = new Configuration(userInterfaceModule.getSchema());
        userInterfaceModuleConfig.setValue("name", userInterfaceModule.getDisplayname());
        this.metadataById.put(userInterfaceModule.getDisplayname(), new MetaData(userInterfaceModule.getDisplayname(), "com.zygon.trade.modules.ui.UserInterfaceModule", userInterfaceModule, userInterfaceModuleConfig));        
        
        WebConsole webConsole = new WebConsole("default");
        Configuration webConsoleConfig = new Configuration(webConsole.getSchema());
        webConsoleConfig.setValue("name", webConsole.getDisplayname());
        webConsoleConfig.setValue("port", "8080");
        this.metadataById.put(webConsole.getDisplayname(), new MetaData(webConsole.getDisplayname(), "com.zygon.trade.modules.ui.WebConsole", webConsole, webConsoleConfig));
    }
    
    @Override
    public String[] getStoredIds() {
        return this.metadataById.keySet().toArray(new String[this.metadataById.keySet().size()]);
    }

    @Override
    public MetaData retrieve(String id) {
        return this.metadataById.get(id);
    }

    @Override
    public void store(String id, MetaData metadata) {
        this.metadataById.put(id, metadata);
    }    
}
