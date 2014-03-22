/**
 * 
 */

package com.zygon.trade.execution.exchange.mtgox;

import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.mtgox.v2.service.polling.MtGoxPollingAccountService;
import com.zygon.trade.execution.AccountController;
import com.zygon.trade.execution.ExchangeException;
import java.io.IOException;
import org.joda.money.CurrencyUnit;

/**
 *
 * @author zygon
 */
public class MtGoxAcctController implements AccountController {

    private final MtGoxPollingAccountService accountService;

    public MtGoxAcctController(MtGoxPollingAccountService accntService) {
        this.accountService = accntService;
    }
    
    @Override
    public AccountInfo getAccountInfo(String username) throws ExchangeException {
        try {
            return this.accountService.getAccountInfo();
        } catch (IOException io) {
            throw new ExchangeException("Error accessing information for " + username, io);
        }
    }

    @Override
    public double getHigh(String username, CurrencyUnit currency) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getLow(String username, CurrencyUnit currency) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getMaximumDrawDown(String username, CurrencyUnit currency) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
