/**
 *
 */
package com.zygon.trade.modules.execution;

import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.dto.trade.Wallet;
import com.zygon.trade.Module;
import java.util.List;

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
	AccountInfo accountInfo = this.execModule.getController().getAccountInfo(id);

	sb.append(accountInfo.getUsername());
	List<Wallet> wallets = accountInfo.getWallets();
	
	if (!wallets.isEmpty()) {
	    sb.append ('\n');
	}

	for (Wallet w : wallets) {
	    sb.append(w.getCurrency());
	    sb.append(" - "); // TODO: format string for spacing
	    sb.append(w.getBalance().getAmount().doubleValue());
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
