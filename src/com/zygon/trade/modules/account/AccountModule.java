
package com.zygon.trade.modules.account;

import com.zygon.trade.Module;
import com.zygon.trade.ParentModule;

/**
 *
 * @author zygon
 */
public class AccountModule extends ParentModule {

    public static final String ID = "account";
    
    
    private final Account mtgoxJoe = new Account("mtgox-joe");
    {
        mtgoxJoe.setBrokerName("mtgox");
        mtgoxJoe.setAccountId("joe");
    }
    private final Account[] accounts = {mtgoxJoe};
    
    public AccountModule() {
        super(ID, Account.class);
    }
    
    @Override
    public void initialize() {
        
    }

    @Override
    public void uninitialize() {
        
    }

    @Override
    public Module[] getModules() {
        return this.accounts;
    }
}
