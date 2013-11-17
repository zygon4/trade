
package com.zygon.trade.execution.exchange;

/**
 *
 * @author zygon
 */
public class AccountEvent extends ExchangeEvent {

    private final String accountId;
    
    public AccountEvent(String accountId) {
        super(EventType.ACCOUNT_STATUS);
        
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }
}
