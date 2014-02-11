
package com.zygon.trade.trade;

import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.dto.trade.Wallet;
import com.zygon.trade.execution.ExchangeException;
import com.zygon.trade.execution.OrderProvider;
import com.zygon.trade.execution.exchange.simulation.SimulationExchange;
import com.zygon.trade.trade.TradeMonitor.TradeState;
import java.util.Arrays;
import java.util.Collection;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author zygon
 */
public class TradeMonitorTester {

    @Test
    public void testTradeState() {
        
        PriceObjective priceObjective = new PriceObjective(TradeSignal.Decision.BUY.getType(), PriceObjective.Modifier.PERCENT, 10.0, 5.0);
        VolumeObjective volumeObjective = new VolumeObjective(VolumeObjective.Modifier.PERCENT, 50.0);
        TradeSignal tradeSignal = new TradeSignal(TradeSignal.Decision.BUY, volumeObjective, "BTC", "USD", priceObjective, TradeUrgency.MEDIUM, "test");
        
        TradeState state = new TradeMonitor.TradeState(tradeSignal, volumeObjective.getVolume(100.0, 1.0));
        
        Assert.assertTrue(state.getState() == TradeMonitor.State.INIT);
        Assert.assertEquals(50.0, state.getRequiredFillVolume(), 0.0);
        Assert.assertEquals(0.0, state.getCurrentFill(), 0.0);
        
        // Declare we want to start the trade
        tradeSignal.getPriceObjective().setPrice(1.0);
        state.setState(TradeMonitor.State.OPEN_FILLING);
        
        Assert.assertTrue(state.getState() == TradeMonitor.State.OPEN_FILLING);
        
        // Fill one side of the trade
        state.notifyFill(1.0, 50.0);
        
        Assert.assertTrue(state.getState() == TradeMonitor.State.OPEN);
        Assert.assertEquals(50.0, state.getCurrentFill(), 0.0);
        
        // Test profit: this trade requires 10% higher, so 1.1
        Assert.assertNull(state.getExitSignal(1.01));
        Signal takeProfitExitSignal = state.getExitSignal(1.1);
        Assert.assertNotNull(takeProfitExitSignal);
        
        // Test stop loss
        Assert.assertNull(state.getExitSignal(0.96));
        Signal stopLossExitSignal = state.getExitSignal(0.95);
        Assert.assertNotNull(stopLossExitSignal);
        
        // Declare we want to start the second half of the trade
        state.setState(TradeMonitor.State.CLOSED_FILLING);
        Assert.assertTrue(state.getState() == TradeMonitor.State.CLOSED_FILLING);
        
        state.notifyFill(2.0, 50.0);
        
        Assert.assertTrue(state.getState() == TradeMonitor.State.CLOSED);
        Assert.assertEquals(50.0, state.getCurrentFill(), 0.0);
        
        double profit = state.calculateClosingProfit();
        
        Assert.assertEquals((2.0 - 1.0) * 50, profit, 0.0);
    }
    
    @Test
    public void testTradeMonitorProfit() throws ExchangeException {
        PriceObjective priceObjective = new PriceObjective(TradeSignal.Decision.BUY.getType(), PriceObjective.Modifier.PERCENT, 10.0, 5.0);
        VolumeObjective volumeObjective = new VolumeObjective(VolumeObjective.Modifier.PERCENT, 50.0);
        TradeSignal tradeSignal = new TradeSignal(TradeSignal.Decision.BUY, volumeObjective, "BTC", "USD", priceObjective, TradeUrgency.MEDIUM, "test");
        
        Trade trade = new Trade(tradeSignal);
        
        Wallet[] testWallet = new Wallet[]{
                    new Wallet("USD", BigMoney.of(CurrencyUnit.USD, 100.0)),
                    new Wallet("BTC", BigMoney.of(CurrencyUnit.of("BTC"), 10.0))
                };
        
        AccountInfo testAccountInfo = new AccountInfo("joe", Arrays.asList(testWallet));
        OrderProvider testOrderProvider = new SimulationExchange.SimulationOrderProvider();
        
        TradeMonitor monitor = new TradeMonitor(trade, testAccountInfo, testOrderProvider);
        
        Collection<Order> openOrders = monitor.open(new Signal("testing"), "0", 1.0);
        Assert.assertFalse(monitor.isClosed());
        Assert.assertEquals(1, openOrders.size());
        
        String orderId = null;
        for (Order order : openOrders) {
            orderId = order.getId();
            break;
        }
        
        // Fill the opening order
        monitor.notifyOrderFill(orderId, 1.0, 50.0);
        
        
        // send in a price which can close the trade
        Collection<Order> closeOrders = monitor.notifyPriceUpdate(1.1);
        
        Assert.assertFalse(monitor.isClosed());
        Assert.assertEquals(1, closeOrders.size());
        
        orderId = null;
        for (Order order : closeOrders) {
            orderId = order.getId();
            break;
        }
        
        // Fill the closing order
        monitor.notifyOrderFill(orderId, 1.1, 50.0);
        
        Assert.assertTrue(monitor.isClosed());
        
        TradePostMortem postMortem = monitor.getPostMortem();
        Assert.assertNotNull(postMortem);
        Assert.assertTrue(postMortem.profitableTrade());
        Assert.assertEquals(5.0, postMortem.getProfit(), 0.0);
    }
    
    @Test
    public void testTradeMonitorStopLoss() throws ExchangeException {
        PriceObjective priceObjective = new PriceObjective(TradeSignal.Decision.BUY.getType(), PriceObjective.Modifier.PERCENT, 10.0, 5.0);
        VolumeObjective volumeObjective = new VolumeObjective(VolumeObjective.Modifier.PERCENT, 50.0);
        TradeSignal tradeSignal = new TradeSignal(TradeSignal.Decision.BUY, volumeObjective, "BTC", "USD", priceObjective, TradeUrgency.MEDIUM, "test");
        
        Trade trade = new Trade(tradeSignal);
        
        Wallet[] testWallet = new Wallet[]{
                    new Wallet("USD", BigMoney.of(CurrencyUnit.USD, 100.0)),
                    new Wallet("BTC", BigMoney.of(CurrencyUnit.of("BTC"), 10.0))
                };
        
        AccountInfo testAccountInfo = new AccountInfo("joe", Arrays.asList(testWallet));
        OrderProvider testOrderProvider = new SimulationExchange.SimulationOrderProvider();
        
        TradeMonitor monitor = new TradeMonitor(trade, testAccountInfo, testOrderProvider);
        
        Collection<Order> openOrders = monitor.open(new Signal("testing"), "0", 1.0);
        Assert.assertFalse(monitor.isClosed());
        Assert.assertEquals(1, openOrders.size());
        
        String orderId = null;
        for (Order order : openOrders) {
            orderId = order.getId();
            break;
        }
        
        // Fill the opening order
        monitor.notifyOrderFill(orderId, 1.0, 50.0);
        
        
        // send in a price which can close the trade
        Collection<Order> closeOrders = monitor.notifyPriceUpdate(0.95);
        
        Assert.assertFalse(monitor.isClosed());
        Assert.assertEquals(1, closeOrders.size());
        
        orderId = null;
        for (Order order : closeOrders) {
            orderId = order.getId();
            break;
        }
        
        // Fill the closing order
        monitor.notifyOrderFill(orderId, 0.95, 50.0);
        
        Assert.assertTrue(monitor.isClosed());
        
        TradePostMortem postMortem = monitor.getPostMortem();
        Assert.assertNotNull(postMortem);
        Assert.assertFalse(postMortem.profitableTrade());
        Assert.assertEquals(-2.5, postMortem.getProfit(), 0.0);
    }
}
