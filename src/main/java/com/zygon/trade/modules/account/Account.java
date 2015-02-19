
package com.zygon.trade.modules.account;

import com.google.common.collect.Lists;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.dto.trade.Wallet;
import com.zygon.configuration.Configuration;
import com.zygon.trade.Module;
import com.zygon.trade.Schema;
import com.zygon.trade.execution.AccountController;
import com.zygon.trade.execution.ExchangeException;
import com.zygon.trade.modules.execution.broker.BrokerModule;
import java.util.Collection;
import org.joda.money.CurrencyUnit;

/**
 *
 * @author zygon
 */
public class Account extends Module {

    private static Collection<String> getSupportedCommands() {
        Collection<String> supportedCommands = Lists.newArrayList();
        
        // TBD
        
        return supportedCommands;
    }
    
    private static final Schema SCHEMA = new Schema("account_schema.json");
    
    private String brokerName;
    private String accountID;
    private AccountController accountController = null;
    
    public Account(String name) {
        super(name, SCHEMA, getSupportedCommands());
    }

    @Override
    public void configure(Configuration configuration) {
        super.configure(configuration);
        // TBD: if someone changes the id/broker.. need to reset any hooks
        
        this.brokerName = configuration.getStringValue("broker");
        this.accountID = configuration.getStringValue("accountId");
    }

    @Override
    protected void doWriteStatus(StringBuilder sb) {
        try {
            AccountInfo accountInfo = this.accountController.getAccountInfo(this.getDisplayname());
            
            for (Wallet wallet : accountInfo.getWallets()) {
                sb.append(wallet.getBalance()).append('\n');
                sb.append("High: ").append(this.accountController.getHigh(this.brokerName, CurrencyUnit.of(wallet.getCurrency()))).append('\n');
                sb.append("Low: ").append(this.accountController.getLow(this.brokerName, CurrencyUnit.of(wallet.getCurrency()))).append('\n');
                sb.append("Maximum drawdown: ").append(this.accountController.getMaximumDrawDown(this.brokerName, CurrencyUnit.of(wallet.getCurrency()))).append('\n');
            }
        } catch (ExchangeException ex) {
            this.getLogger().error("Error getting account information for: " + this.getDisplayname(), ex);
        }
    }
    
    @Override
    protected void hook() {
        super.hook();
        BrokerModule module = (BrokerModule) this.getModule(BrokerModule.ID);
        this.accountController = module.getAccountController(this.brokerName, this.accountID);
        if (this.accountController == null) {
            this.getLogger().debug("Unable to find account controller for " + this.brokerName + ": " + this.accountID);
        }
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void uninitialize() {
        
    }
}
