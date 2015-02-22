
package com.zygon.trade;

import com.zygon.configuration.MetaData;
import com.zygon.configuration.Configuration;
import com.xeiam.xchange.currency.CurrencyPair;
import com.zygon.trade.market.data.Ticker;
import com.zygon.trade.modules.account.Account;
import com.zygon.trade.modules.account.AccountModule;
import com.zygon.trade.modules.agent.Agent;
import com.zygon.trade.modules.agent.AgentModule;
import com.zygon.trade.modules.ui.CLIModule;
import com.zygon.trade.modules.data.DataFeed;
import com.zygon.trade.modules.data.DataModule;
import com.zygon.trade.modules.data.DataSet;
import com.zygon.trade.modules.data.DataSetModule;
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
        AgentModule agentModule = new AgentModule();
        Configuration agentModuleConfig = new Configuration(agentModule.getSchema());
        agentModuleConfig.setStringValue("name", agentModule.getDisplayname());
        this.metadataById.put(agentModule.getDisplayname(), 
                MetaDataHelper.createServerMetaProperties(agentModule.getDisplayname(), agentModuleConfig, "com.zygon.trade.modules.agent.AgentModule"));
        
        
        Agent agent = new Agent("macd");
        Configuration agentConfig = new Configuration(agent.getSchema());
        agentConfig.setStringValue("name", agent.getDisplayname());
        // TBD: set: interpretters, strategy, etc. For now it's hardcoded in the agent
        agentConfig.setStringValue("broker", "mtgox");
        this.metadataById.put(agent.getDisplayname(), 
                MetaDataHelper.createServerMetaProperties(agent.getDisplayname(), agentConfig, "com.zygon.trade.modules.agent.Agent"));
        
        DataSetModule dataSetModule = new DataSetModule();
        Configuration dataSetModuleConfig = new Configuration(dataSetModule.getSchema());
        dataSetModuleConfig.setStringValue("name", dataSetModule.getDisplayname());
        this.metadataById.put(dataSetModule.getDisplayname(), 
                MetaDataHelper.createServerMetaProperties(dataSetModule.getDisplayname(), dataSetModuleConfig, "com.zygon.trade.modules.data.DataSetModule"));
        
        
        DataSet mtgoxTickerDataSet = new DataSet("mtgox-ticker-data");
        Configuration mtgoxTickerDataSetConfig = new Configuration(mtgoxTickerDataSet.getSchema());
        mtgoxTickerDataSetConfig.setStringValue("name", mtgoxTickerDataSet.getDisplayname());
        mtgoxTickerDataSetConfig.setStringValue("data-uri", "file:///home/zygon/opt/trade/system/data/agent/macd/macd_ticker.txt");
        this.metadataById.put(mtgoxTickerDataSet.getDisplayname(), 
                MetaDataHelper.createServerMetaProperties(mtgoxTickerDataSet.getDisplayname(), mtgoxTickerDataSetConfig, "com.zygon.trade.modules.data.DataSet"));
        
        
        DataModule data = new DataModule();
        Configuration dataConfig = new Configuration(data.getSchema());
        dataConfig.setStringValue("name", data.getDisplayname());
        this.metadataById.put("data", 
                MetaDataHelper.createServerMetaProperties("data", dataConfig, "com.zygon.trade.modules.data.DataModule"));
        
        
        DataFeed<Ticker> mtgoxTicker = new DataFeed<Ticker>("mtgox-ticker");
        Configuration mtgoxConfig = new Configuration(mtgoxTicker.getSchema());
        mtgoxConfig.setStringValue("name", mtgoxTicker.getDisplayname());
        mtgoxConfig.setStringValue("class", "com.zygon.trade.market.data.mtgox.MtGoxFeed");
        mtgoxConfig.setStringValue("tradeable", CurrencyPair.BTC_USD.baseCurrency);
        mtgoxConfig.setStringValue("currency", CurrencyPair.BTC_USD.counterCurrency);
//        mtgoxConfig.setStringValue("data-set-identifier", mtgoxTickerDataSet.getDisplayname());
        this.metadataById.put(mtgoxTicker.getDisplayname(), 
                MetaDataHelper.createServerMetaProperties(mtgoxTicker.getDisplayname(), mtgoxConfig, "com.zygon.trade.modules.data.DataFeed"));
        
        
        BrokerModule brokerModule = new BrokerModule();
        Configuration brokerConfig = new Configuration(brokerModule.getSchema());
        brokerConfig.setStringValue("name", brokerModule.getDisplayname());
        this.metadataById.put(brokerModule.getDisplayname(), 
                MetaDataHelper.createServerMetaProperties(brokerModule.getDisplayname(), brokerConfig, "com.zygon.trade.modules.execution.broker.BrokerModule"));
        
        
        Broker broker = new Broker("mtgox");
        Configuration mtgoxBrokerConfig = new Configuration(broker.getSchema());
        mtgoxBrokerConfig.setStringValue("accountId", "joe");
        mtgoxBrokerConfig.setStringValue("name", broker.getDisplayname());
        this.metadataById.put(broker.getDisplayname(), 
                MetaDataHelper.createServerMetaProperties(broker.getDisplayname(), mtgoxBrokerConfig, "com.zygon.trade.modules.execution.broker.Broker"));
        
        
        AccountModule accountModule = new AccountModule();
        Configuration accountModuleConfig = new Configuration(accountModule.getSchema());
        accountModuleConfig.setStringValue("name", accountModule.getDisplayname());
        this.metadataById.put(accountModule.getDisplayname(), 
                MetaDataHelper.createServerMetaProperties(accountModule.getDisplayname(), accountModuleConfig, "com.zygon.trade.modules.account.AccountModule"));
        
        
        Account account = new Account("joe");
        Configuration accountConfig = new Configuration(account.getSchema());
        accountConfig.setStringValue("name", account.getDisplayname());
        accountConfig.setStringValue("broker", "mtgox");
        accountConfig.setStringValue("accountId", "joe");
        this.metadataById.put(account.getDisplayname(), 
                MetaDataHelper.createServerMetaProperties(account.getDisplayname(), accountConfig, "com.zygon.trade.modules.account.Account"));
        
        CLIModule cliModule = new CLIModule("cli");
        Configuration cliConfig = new Configuration(cliModule.getSchema());
        cliConfig.setStringValue("name", cliModule.getDisplayname());
        this.metadataById.put(cliModule.getDisplayname(), 
                MetaDataHelper.createServerMetaProperties(cliModule.getDisplayname(), cliConfig, "com.zygon.trade.modules.ui.CLIModule"));
        
        
        UserInterfaceModule userInterfaceModule = new UserInterfaceModule();
        Configuration userInterfaceModuleConfig = new Configuration(userInterfaceModule.getSchema());
        userInterfaceModuleConfig.setStringValue("name", userInterfaceModule.getDisplayname());
        this.metadataById.put(userInterfaceModule.getDisplayname(), 
                MetaDataHelper.createServerMetaProperties(userInterfaceModule.getDisplayname(), userInterfaceModuleConfig, "com.zygon.trade.modules.ui.UserInterfaceModule"));        
        
        
        WebConsole webConsole = new WebConsole("default");
        Configuration webConsoleConfig = new Configuration(webConsole.getSchema());
        webConsoleConfig.setStringValue("name", webConsole.getDisplayname());
        webConsoleConfig.setIntValue("port", 8080);
        this.metadataById.put(webConsole.getDisplayname(), 
                MetaDataHelper.createServerMetaProperties(webConsole.getDisplayname(), webConsoleConfig, "com.zygon.trade.modules.ui.WebConsole"));
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
