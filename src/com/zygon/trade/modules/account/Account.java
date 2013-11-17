
package com.zygon.trade.modules.account;

import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.dto.trade.Wallet;
import com.zygon.trade.Module;
import com.zygon.trade.execution.AccountController;
import com.zygon.trade.execution.ExchangeException;
import com.zygon.trade.modules.execution.broker.BrokerModule;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author zygon
 */
public class Account extends Module {

    private static Collection<String> getSupportedCommands() {
        Collection<String> supportedCommands = new ArrayList<String>();
        
        // TBD
        
        return supportedCommands;
    }
    
    private String brokerName;
    private String accountID;
    private AccountController accountController = null;
    
    public Account(String name) {
        // TODO: schema
        super(name, null, getSupportedCommands());
    }

    @Override
    protected void doWriteStatus(StringBuilder sb) {
        try {
            AccountInfo accountInfo = this.accountController.getAccountInfo(this.getDisplayname());
            
            for (Wallet wallet : accountInfo.getWallets()) {
                sb.append(wallet.getBalance()).append('\n');
            }
        } catch (ExchangeException ex) {
            this.getLogger().error("Error getting account information for: " + this.getDisplayname(), ex);
        }
    }

    
    @Override
    public Module[] getModules() {
        return null;
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

    // These should come from config
    void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    void setAccountId(String accountId) {
        this.accountID = accountId;
    }
}
