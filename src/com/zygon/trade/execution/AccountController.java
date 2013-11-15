/**
 * 
 */

package com.zygon.trade.execution;

import com.xeiam.xchange.dto.account.AccountInfo;

/**
 *
 * @author zygon
 */
public interface AccountController {
    public AccountInfo getAccountInfo(String username) throws ExchangeException;
}
