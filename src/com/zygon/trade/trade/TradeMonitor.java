/**
 * 
 */

package com.zygon.trade.trade;

import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.Order.OrderType;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.dto.trade.MarketOrder;
import com.zygon.trade.execution.OrderProvider;
import static com.zygon.trade.trade.TradeType.LONG;
import static com.zygon.trade.trade.TradeType.SHORT;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;

/**
 * This might become a "trade manager" in the future.  Being a full manager
 * would mean it would have a trade broker as well.
 *
 * @author zygon
 */
public class TradeMonitor {
    
    /*pkg*/ static enum State {
        INIT,
        OPEN_FILLING,
        OPEN,
        CLOSED_FILLING,
        CLOSED
    }
    
    /*pkg*/ static class TradeState {
        private final TradeSignal signal;
        private final double requiredFillVolume; // on both sides of the trade
        private final Map<OrderType,Map<Double,Double>> fillVolumeByPriceAndType = new HashMap<OrderType,Map<Double,Double>>();
        
        private BigDecimal openingPrice = null;
        private BigDecimal closingPrice = null;
        
        private State state = State.INIT;

        public TradeState(TradeSignal signal, double requiredFill) {
            if (signal == null) {
                throw new IllegalArgumentException("No null arguments permitted");
            }
            if (requiredFill <= 0.0) {
                throw new IllegalArgumentException("Required fill must be greater than zero");
            }
            this.signal = signal;
            this.requiredFillVolume = requiredFill;
        }

        public double calculateClosingProfit () {
        
            if (this.state != State.CLOSED) {
                throw new IllegalStateException("Must be " + State.CLOSED.name());
            }
            
            BigDecimal priceMargin = null;

            switch (this.signal.getTradeType()) {
                case LONG:
                    priceMargin = this.closingPrice.subtract(this.openingPrice);
                    break;
                case SHORT:
                    priceMargin = this.openingPrice.subtract(this.closingPrice);
                    break;
            }

            return priceMargin.multiply(BigDecimal.valueOf(this.requiredFillVolume)).doubleValue();
        }
        
        private static final String EXIT_STOP_LOSS = "STOP_LOSS";
        private static final String EXIT_TAKE_PROFIT = "TAKE_PROFIT";

        /**
         * Returns a signal symbolizing that the trade should be closed, null otherwise.
         * @param signal
         * @param currentPrice
         * @return a signal symbolizing that the trade should be closed, null otherwise.
         */
        /*pkg*/ Signal getExitSignal(double currentPrice) {

            String exitSignal = null;
            
            if (this.state == State.OPEN) {
                switch (signal.getTradeType()) {
                    case LONG:
                        if (currentPrice <= signal.getPriceObjective().getStopLoss()) {
                            exitSignal = EXIT_STOP_LOSS;
                        } else if(currentPrice >= signal.getPriceObjective().getTakeProfit()) {
                            exitSignal = EXIT_TAKE_PROFIT;
                        }
                        break;
                    case SHORT:
                        if (currentPrice >= signal.getPriceObjective().getStopLoss()) {
                            exitSignal = EXIT_STOP_LOSS;
                        } else if (currentPrice <= signal.getPriceObjective().getTakeProfit()) {
                            exitSignal = EXIT_TAKE_PROFIT;
                        }
                        break;
                }
            }
            
            return exitSignal != null ? new Signal(exitSignal) : null;
        }
        
        private Map<Double,Double> getFillVolumeByPrice() {
            OrderType orderType = (this.state == State.OPEN_FILLING || this.state == State.OPEN) ? 
                    this.signal.getTradeType().getOrderType() : this.signal.getTradeType().getCounterOrderType();
            
            Map<Double,Double> fillVolByPrice = this.fillVolumeByPriceAndType.get(orderType);
            
            if (fillVolByPrice == null) {
                fillVolByPrice = new HashMap<Double,Double>();
                this.fillVolumeByPriceAndType.put(orderType, fillVolByPrice);
            }
            
            return fillVolByPrice;
        }
        
        public double getCurrentFill() {
            double currentFill = 0.0;
            
            if (this.getState() != State.INIT) {
                for (Double fillVolume : this.getFillVolumeByPrice().values()) {
                    currentFill += fillVolume;
                }
            }
            
            return currentFill;
        }

        public double getRequiredFillVolume() {
            return this.requiredFillVolume;
        }
        
        public State getState() {
            return this.state;
        }

        /*pkg*/ synchronized void notifyFill (double fillPrice, double fillAmmount) {
            if (fillPrice <= 0.0) {
                throw new IllegalArgumentException("fillPrice must be greater than zero");
            }
            
            if (fillAmmount <= 0.0) {
                throw new IllegalArgumentException("fillAmmount must be greater than zero");
            }
            
            double totalCurrentFill = this.getCurrentFill();
            
            if (fillAmmount + totalCurrentFill > this.requiredFillVolume) {
                throw new IllegalArgumentException("Overfill by: " + ((fillAmmount + totalCurrentFill) - this.requiredFillVolume));
            }
            
            if (this.state == State.CLOSED || this.state == State.OPEN) {
                throw new IllegalStateException("Not filling in state: " + this.state.name());
            }
            
            Double fillVolumeByPrice = this.getFillVolumeByPrice().get(fillPrice);
            
            if (fillVolumeByPrice == null) {
                fillVolumeByPrice = 0.0;
            }
            
            fillVolumeByPrice += fillAmmount;
            this.getFillVolumeByPrice().put(fillPrice, fillVolumeByPrice);
            
            if ((totalCurrentFill + fillAmmount) == this.requiredFillVolume) {
                State transitionState = null;
                switch (this.state) {
                    case CLOSED_FILLING:
                        transitionState = State.CLOSED;
                        this.closingPrice = BigDecimal.valueOf(fillPrice);
                        break;
                    case OPEN_FILLING:
                        transitionState = State.OPEN;
                        this.openingPrice = BigDecimal.valueOf(fillPrice);
                        break;
                }
                this.setState(transitionState);
            }
        }

        /*pkg*/ void setState(State state) {
            if (state.ordinal() < this.state.ordinal()) {
                throw new IllegalArgumentException("State cannot move backwards");
            }
            
            if (state.ordinal() == this.state.ordinal() && (this.state != State.OPEN_FILLING && this.state != State.CLOSED_FILLING)) {
                throw new IllegalArgumentException("State can only stay the same if " + State.OPEN_FILLING.name() + " or " + State.CLOSED_FILLING);
            }
            
            this.state = state;
        }
    }
    
    // These are method to encode/decode into an order key.  When an order
    // key comes back to us for a fill, etc. we can find the original trade.
    // I'm not always a huge fan of this kind of string manipulation but 
    // it'll do for now.
    /*pkg*/ static String generateOrderKey (String tradeId, String orderId) {
        return String.format("%s|%s", tradeId, orderId);
    }
    /*pkg*/ static String generateOrderKey (int tradeId, int orderId) {
        return String.format("%d|%d", tradeId, orderId);
    }
    /*pkg*/ static String getTradeId(String orderKey) {
        return orderKey.split("\\|")[0];
    }
    /*pkg*/ static String getOrderId(String orderKey) {
        return orderKey.split("\\|")[1];
    }
    
    // The original trade and some helper pieces
    private final Trade trade;
    private final AccountInfo accountInfo;
    private final OrderProvider orderProvider;
    private final Map<String,TradeSignal> signalsByOrderId = new HashMap<String,TradeSignal>();
    private final Map<TradeSignal,TradeState> stateBySignal = new HashMap<TradeSignal,TradeState>();
    
    private long startTime = 0;
    private Signal entryReason;
    private double enterPrice;
    private String tradeId;
    private TradePostMortem postMortem = null;
    
    /*pkg*/ TradeMonitor(Trade trade, AccountInfo accountInfo, OrderProvider orderProvider) {
        this.trade = trade;
        this.accountInfo = accountInfo;
        this.orderProvider = orderProvider;
    }
    
    private void close(Signal exitReason) {
        
        if (this.postMortem == null) {
            long exitTime = System.currentTimeMillis();
            double totalProfit = 0.0;
            
            for (TradeState state : this.stateBySignal.values()) {
                totalProfit += state.calculateClosingProfit();
            }

            long duration = exitTime - this.startTime;
            
            this.postMortem = new TradePostMortem(this.entryReason, exitReason, duration, totalProfit);
        }
    }
    
    private static int orderId = 0;
    
    /*pkg*/ Collection<Order> open (Signal entryReason, String tradeId, double price) {
        
        if (this.isClosed()) {
            throw new IllegalStateException("closed");
        }
        
        this.startTime = System.currentTimeMillis();
        this.entryReason = entryReason;
        this.tradeId = tradeId;
        this.enterPrice = price;
        
        Collection<Order> orders = new ArrayList<Order>();
        
        for (TradeSignal signal : this.trade.getTradeSignals()) {
            
            signal.getPriceObjective().setPrice(this.enterPrice);
            BigMoney signalCurrencybalance = this.accountInfo.getBalance(CurrencyUnit.of(signal.getCurrency()));
            double desiredVolume = signal.getVolumeObjective().getVolume(signalCurrencybalance.getAmount().doubleValue(), this.enterPrice);

            String orderKey = generateOrderKey(this.tradeId, String.valueOf(orderId++));
            // TODO: limit order by not generating an order immediately - wait for the ticker notification instead
            MarketOrder order = this.orderProvider.getMarketOrder(orderKey, signal.getDecision().getType().getOrderType(), 
                             desiredVolume, signal.getTradeableIdentifier(), signal.getCurrency());
            orders.add(order);
            
            TradeState state = new TradeState(signal, desiredVolume);
            
            // TODO: only if market order go to open_filling - otherwise wait
            // for the price
            state.setState(State.OPEN_FILLING);
            
            synchronized (this.signalsByOrderId) {
                this.stateBySignal.put(signal, state);
                this.signalsByOrderId.put(orderKey, signal);
            }
        }
        
        return orders;
    }
    
    public TradePostMortem getPostMortem() {
        return this.postMortem;
    }
    
    public boolean isClosed() {
        return this.getPostMortem() != null;
    }
    
    /*pkg*/ synchronized void notifyOrderFill(String orderId, double fillPrice, double fillAmmount) {
        
        if (this.isClosed()) {
            throw new IllegalStateException("closed");
        }
        
        TradeSignal signal = null;
        
        synchronized (this.signalsByOrderId) {
            signal = this.signalsByOrderId.get(orderId);
            
            if (signal != null) {
                TradeState state = this.stateBySignal.get(signal);
                state.notifyFill(fillPrice, fillAmmount);
            }
            
            boolean allClosed = true;
            
            for (TradeState state : this.stateBySignal.values()) {
                if (state.getState() != State.CLOSED) {
                    allClosed = false;
                    break;
                }
            }
            
            if (allClosed) {
                this.close(new Signal("TODO:exit-signals"));
                // TODO: close the trade here?  This is where it seems to make
                // sense - when the trade is fully complete - but the exit reason
                // is determined previously in the notifyPriceUpdate.. so?
            }
        }
    }
    
    /*pkg*/ synchronized Collection<Order> notifyPriceUpdate(double price) {
        
        if (this.isClosed()) {
            throw new IllegalStateException("closed");
        }
        
        Collection<Order> orders = new ArrayList<Order>();
        
        for (Entry<TradeSignal, TradeState> entry : this.stateBySignal.entrySet()) {
            TradeSignal signal = entry.getKey();
            TradeState state = entry.getValue();
            
            if (state.getState() == State.INIT) {
                // TODO: Limit order. check if this trade can start and set
                // its state to OPEN_FILLING
            } else if (state.getState() == State.OPEN) {
                Signal exit = state.getExitSignal(price);
                if (exit != null) {
//                    this.log.info("Closing order " + orderId + " of trade " + trade.getTradeId() + " due to " + exitSignal.getName());
                    String orderKey = generateOrderKey(this.tradeId, String.valueOf(orderId++));
                    
                    MarketOrder closeOrder = this.orderProvider.getMarketOrder(orderKey, state.signal.getTradeType().getCounterOrderType(), 
                            state.getRequiredFillVolume(), state.signal.getTradeableIdentifier(), state.signal.getCurrency());
                    
                    orders.add(closeOrder);
                    
                    state.setState(State.CLOSED_FILLING);
                    
                    synchronized (this.signalsByOrderId) {
                        this.signalsByOrderId.put(orderKey, signal);
                    }
                }
            }
        }
        
        return orders;
    }
    
    public final String getTradeId() {
        return this.tradeId;
    }
}
