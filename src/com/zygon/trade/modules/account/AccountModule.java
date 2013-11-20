
package com.zygon.trade.modules.account;

import com.zygon.trade.ParentModule;

/**
 *
 * @author zygon
 */
public class AccountModule extends ParentModule {

    public static final String ID = "account";
    
    public AccountModule() {
        super(ID, Account.class);
    }
    
    @Override
    public void initialize() {
        
    }

    @Override
    public void uninitialize() {
        
    }
}
