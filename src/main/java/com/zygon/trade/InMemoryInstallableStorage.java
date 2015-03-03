
package com.zygon.trade;

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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zygon
 */
@Deprecated
public class InMemoryInstallableStorage implements InstallableStorage {

    private Installable create(final InstallableMetaData metaData) {
        return new Installable() {

            @Override
            public String getId() {
                return metaData.getId();
            }

            @Override
            public InstallableMetaData getInstallableMetaData() {
                return metaData;
            }
        };
    }
    
    private final Map<String, Installable> metadataById = new HashMap<>();
    
    {
        AgentModule agentModule = new AgentModule();
        Configuration agentModuleConfig = new Configuration(agentModule.getConfiguration().getSchema());
        agentModuleConfig.setStringValue("name", agentModule.getDisplayname());
        this.metadataById.put(agentModule.getDisplayname(), 
                create(InstallableMetaDataHelper.createServerMetaProperties(agentModule.getDisplayname(), agentModuleConfig, "com.zygon.trade.modules.agent.AgentModule")));
        
        
        Agent agent = new Agent("macd");
        Configuration agentConfig = new Configuration(agent.getConfiguration().getSchema());
        agentConfig.setStringValue("name", agent.getDisplayname());
        // TBD: set: interpretters, strategy, etc. For now it's hardcoded in the agent
        agentConfig.setStringValue("broker", "mtgox");
        this.metadataById.put(agent.getDisplayname(), 
                create(InstallableMetaDataHelper.createServerMetaProperties(agent.getDisplayname(), agentConfig, "com.zygon.trade.modules.agent.Agent")));
        
        DataSetModule dataSetModule = new DataSetModule();
        Configuration dataSetModuleConfig = new Configuration(dataSetModule.getConfiguration().getSchema());
        dataSetModuleConfig.setStringValue("name", dataSetModule.getDisplayname());
        this.metadataById.put(dataSetModule.getDisplayname(), 
                create(InstallableMetaDataHelper.createServerMetaProperties(dataSetModule.getDisplayname(), dataSetModuleConfig, "com.zygon.trade.modules.data.DataSetModule")));
        
        
        DataSet mtgoxTickerDataSet = new DataSet("mtgox-ticker-data");
        Configuration mtgoxTickerDataSetConfig = new Configuration(mtgoxTickerDataSet.getConfiguration().getSchema());
        mtgoxTickerDataSetConfig.setStringValue("name", mtgoxTickerDataSet.getDisplayname());
        mtgoxTickerDataSetConfig.setStringValue("data-uri", "file:///home/zygon/opt/trade/system/data/agent/macd/macd_ticker.txt");
        this.metadataById.put(mtgoxTickerDataSet.getDisplayname(), 
                create(InstallableMetaDataHelper.createServerMetaProperties(mtgoxTickerDataSet.getDisplayname(), mtgoxTickerDataSetConfig, "com.zygon.trade.modules.data.DataSet")));
        
        
        DataModule data = new DataModule();
        Configuration dataConfig = new Configuration(data.getConfiguration().getSchema());
        dataConfig.setStringValue("name", data.getDisplayname());
        this.metadataById.put("data", 
                create(InstallableMetaDataHelper.createServerMetaProperties("data", dataConfig, "com.zygon.trade.modules.data.DataModule")));
        
        
        DataFeed<Ticker> mtgoxTicker = new DataFeed<Ticker>("mtgox-ticker");
        Configuration mtgoxConfig = new Configuration(mtgoxTicker.getConfiguration().getSchema());
        mtgoxConfig.setStringValue("name", mtgoxTicker.getDisplayname());
        mtgoxConfig.setStringValue("class", "com.zygon.trade.market.data.mtgox.MtGoxFeed");
        mtgoxConfig.setStringValue("tradeable", CurrencyPair.BTC_USD.baseCurrency);
        mtgoxConfig.setStringValue("currency", CurrencyPair.BTC_USD.counterCurrency);
//        mtgoxConfig.setStringValue("data-set-identifier", mtgoxTickerDataSet.getDisplayname());
        this.metadataById.put(mtgoxTicker.getDisplayname(), 
                create(InstallableMetaDataHelper.createServerMetaProperties(mtgoxTicker.getDisplayname(), mtgoxConfig, "com.zygon.trade.modules.data.DataFeed")));
        
        
        BrokerModule brokerModule = new BrokerModule();
        Configuration brokerConfig = new Configuration(brokerModule.getConfiguration().getSchema());
        brokerConfig.setStringValue("name", brokerModule.getDisplayname());
        this.metadataById.put(brokerModule.getDisplayname(), 
                create(InstallableMetaDataHelper.createServerMetaProperties(brokerModule.getDisplayname(), brokerConfig, "com.zygon.trade.modules.execution.broker.BrokerModule")));
        
        
        Broker broker = new Broker("mtgox");
        Configuration mtgoxBrokerConfig = new Configuration(broker.getConfiguration().getSchema());
        mtgoxBrokerConfig.setStringValue("accountId", "joe");
        mtgoxBrokerConfig.setStringValue("name", broker.getDisplayname());
        this.metadataById.put(broker.getDisplayname(), 
                create(InstallableMetaDataHelper.createServerMetaProperties(broker.getDisplayname(), mtgoxBrokerConfig, "com.zygon.trade.modules.execution.broker.Broker")));
        
        
        AccountModule accountModule = new AccountModule();
        Configuration accountModuleConfig = new Configuration(accountModule.getConfiguration().getSchema());
        accountModuleConfig.setStringValue("name", accountModule.getDisplayname());
        this.metadataById.put(accountModule.getDisplayname(), 
                create(InstallableMetaDataHelper.createServerMetaProperties(accountModule.getDisplayname(), accountModuleConfig, "com.zygon.trade.modules.account.AccountModule")));
        
        
        Account account = new Account("joe");
        Configuration accountConfig = new Configuration(account.getConfiguration().getSchema());
        accountConfig.setStringValue("name", account.getDisplayname());
        accountConfig.setStringValue("broker", "mtgox");
        accountConfig.setStringValue("accountId", "joe");
        this.metadataById.put(account.getDisplayname(), 
                create(InstallableMetaDataHelper.createServerMetaProperties(account.getDisplayname(), accountConfig, "com.zygon.trade.modules.account.Account")));
        
        CLIModule cliModule = new CLIModule();
        Configuration cliConfig = new Configuration(cliModule.getConfiguration().getSchema());
        cliConfig.setStringValue("name", cliModule.getDisplayname());
        this.metadataById.put(cliModule.getDisplayname(), 
                create(InstallableMetaDataHelper.createServerMetaProperties(cliModule.getDisplayname(), cliConfig, "com.zygon.trade.modules.ui.CLIModule")));
        
        
        UserInterfaceModule userInterfaceModule = new UserInterfaceModule();
        Configuration userInterfaceModuleConfig = new Configuration(userInterfaceModule.getConfiguration().getSchema());
        userInterfaceModuleConfig.setStringValue("name", userInterfaceModule.getDisplayname());
        this.metadataById.put(userInterfaceModule.getDisplayname(), 
                create(InstallableMetaDataHelper.createServerMetaProperties(userInterfaceModule.getDisplayname(), userInterfaceModuleConfig, "com.zygon.trade.modules.ui.UserInterfaceModule")));        
        
        
        WebConsole webConsole = new WebConsole("default");
        Configuration webConsoleConfig = new Configuration(webConsole.getConfiguration().getSchema());
        webConsoleConfig.setStringValue("name", webConsole.getDisplayname());
        webConsoleConfig.setIntValue("port", 8080);
        this.metadataById.put(webConsole.getDisplayname(), 
                create(InstallableMetaDataHelper.createServerMetaProperties(webConsole.getDisplayname(), webConsoleConfig, "com.zygon.trade.modules.ui.WebConsole")));
    }
    
    @Override
    public String[] getStoredIds() {
        return this.metadataById.keySet().toArray(new String[this.metadataById.keySet().size()]);
    }

    @Override
    public void remove(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Installable retrieve(String id) {
        return this.metadataById.get(id);
    }

    @Override
    public void store(Installable installable) {
        this.metadataById.put(installable.getId(), installable);
    }
}
