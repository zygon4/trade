/**
 *
 */
package com.zygon.trade.modules.execution;

import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.dto.trade.Wallet;
import com.zygon.trade.Module;
import com.zygon.trade.Request;
import java.util.List;

/**
 *
 * @author zygon
 */
public class AccountModule extends Module {

    private static final String ACCNT_SUMMARY = "acnt";
    
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
    
    private String getUserName (Request request) {
        return ((String[]) request.get(Request.ARGS))[0];
    }
    
    private boolean isAccountSummaryRequest(Request request) {
        return request.isCommand(ACCNT_SUMMARY);
    }
    
    @Override
    public Object getOutput(Request request) {
        
        String output = null;
        
        if (request.isCommandRequest()) {
            if (this.isAccountSummaryRequest(request)) {
                StringBuilder sb = new StringBuilder();

                String user = this.getUserName(request);
                if (user != null) {
                    this.getAccountSummary(sb, user);
                } else {
                    // TODO: get a list of all the users to generate account
                    // summaries
                }

                output = sb.toString();
            } else {
                output = "Unknown command: " + request.getCommand();
            }
        } else if (request.isListCommandRequest()) {
            output = " - acnt <user>";
        }
        
        return output;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void uninitialize() {

    }
}
