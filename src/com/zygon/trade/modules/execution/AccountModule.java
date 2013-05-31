/**
 *
 */
package com.zygon.trade.modules.execution;

import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.dto.trade.Wallet;
import com.zygon.trade.Module;
import java.util.List;
import java.util.Map;

/**
 *
 * @author zygon
 */
public class AccountModule extends Module {

    private static String USER = "_u";
    
    private final ExecutionModule execModule;

    /*pkg*/ AccountModule(String name, ExecutionModule execModule) {
        super("Account-"+name);
	this.execModule = execModule;
    }
    
    // TODO: account summary object?
    public void getAccountSummary(StringBuilder sb, String userName) {
	AccountInfo accountInfo = this.execModule.getController().getAccountInfo(userName);

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
    public Object getOutput(Map<String, Object> input) {
        
        String user = null;
        if (input.containsKey(USER)) {
            user = (String) input.get(user);
        }
        
        StringBuilder sb = new StringBuilder();
        
        this.getAccountSummary(sb, user);
        
        return sb.toString();
    }

    @Override
    public void initialize() {

    }

    @Override
    public void uninitialize() {

    }
}
