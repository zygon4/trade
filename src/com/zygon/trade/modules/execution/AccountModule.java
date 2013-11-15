/**
 *
 */
package com.zygon.trade.modules.execution;

import com.zygon.trade.Module;
import com.zygon.trade.Request;
import com.zygon.trade.Response;

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
    
    public void getAccountSummary(StringBuilder sb, String userName) {
        
        // TODO: reengineer this when trading comes back online
        
//        AccountInfo accountInfo = null;
//        try {
//            accountInfo = this.execModule.getController().getAccountInfo(userName);
//            sb.append(accountInfo.getUsername());
//            List<Wallet> wallets = accountInfo.getWallets();
//
//            if (!wallets.isEmpty()) {
//                sb.append ('\n');
//            }
//
//            for (Wallet w : wallets) {
//                sb.append(w.getCurrency());
//                sb.append(" - "); // TODO: format string for spacing
//                sb.append(w.getBalance().getAmount().doubleValue());
//            }
//        } catch (ExchangeException ee) {
//            sb.append("An error occured retrieving account summary for ").append(userName);
//        }
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
    public Response getOutput(Request request) {
        
        String output = null;
        
        if (request.isCommandRequest()) {
            if (this.isAccountSummaryRequest(request)) {
                StringBuilder sb = new StringBuilder();

                if (request.hasArguments()) {
                    String user = this.getUserName(request);
                    if (user != null) {
                        this.getAccountSummary(sb, user);
                    }
                } else {
                    // TODO: get a list of all the users to generate summaries
                }

                output = sb.toString();
            } else {
                output = "Unknown command: " + request.getCommandName();
            }
        } else if (request.isListCommandRequest()) {
            output = " - acnt <user>";
        }
        
        return new Response(output);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void uninitialize() {

    }
}
