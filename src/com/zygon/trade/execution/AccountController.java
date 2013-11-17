/**
 * 
 */

package com.zygon.trade.execution;

import com.xeiam.xchange.dto.account.AccountInfo;
import org.joda.money.CurrencyUnit;

/**
 *
 * @author zygon
 */
public interface AccountController {
    // TBD: this probably needs to expand to allow for different account types.
    // For example accounts hosted on target sites, or accounts hosted in 
    // different banks, etc.
    public AccountInfo getAccountInfo(String username) throws ExchangeException;
    
    public double getHigh(String username, CurrencyUnit currency);
    
    public double getLow(String username, CurrencyUnit currency);
    
    public double getMaximumDrawDown(String username, CurrencyUnit currency);
}
