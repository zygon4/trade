/**
 *
 */
package com.zygon.trade.modules.execution;

import com.zygon.trade.Module;

/**
 *
 * @author zygon
 */
public class AccountModule extends Module {

    private final ExecutionModule execModule;

    /*pkg*/ AccountModule(String name, ExecutionModule execModule) {
        super("Account-"+name);
	this.execModule = execModule;
    }
    
    // TODO: account summary object?
    // We are starting to get into the realm of
    // user interaction - oogabooga!
    public void getAccountSummary(StringBuilder sb, String id) {
	AccountInfo accountInfo = this.getController().getAccountInfo(id);

	sb.append(accountInfo.getUser());
	List<Wallet> wallets = accountInfo.getWallets();
	
	if (!wallets.isEmpty()) {
	    sb.append ('\n');
	}

	for (Wallet w : wallets) {
	    sb.append(w.getCurrency());
	    sb.append(" - "); // TODO: format string for spacing
	    sb.append(w.getBalance().getAmmount().doubleValue());
	}
    }

    @Override
    public Module[] getModules() {
	return null;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void uninitialize() {

    }
}
