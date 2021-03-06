
package com.zygon.trade.execution.exchange;

import com.xeiam.xchange.dto.trade.Wallet;

/**
 *
 * @author zygon
 */
public class AccountWalletUpdate extends AccountEvent {

    private final Wallet wallet;
    
    public AccountWalletUpdate(String accountId, Wallet wallet) {
        super(accountId);
        
        this.wallet = wallet;
    }

    public Wallet getWallet() {
        return this.wallet;
    }
}
