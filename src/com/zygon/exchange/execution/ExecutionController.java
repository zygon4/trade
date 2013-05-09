/**
 * 
 */

package com.zygon.exchange.execution;

import com.xeiam.xchange.dto.Order;
import com.zygon.exchange.management.AccountController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zygon
 */
public final class ExecutionController {

    // make instance based?
    private static final Logger log = LoggerFactory.getLogger(ExecutionController.class);
    
    // This wants to have a mapping of execution objects to an id of some kind.
    // The purpose being that someone can register new bindings and then
    // it is ready to process orders/transactions from that id.
    
    /**
     * This represents everything the controller needs to manipulate an order
     * from placing it, monitoring it, canceling it, updating the proper
     * OrderBook, handling accounting, etc.
     */
    public static interface Binding {
        public AccountController getAccountController(String id);
        public OrderBookProvider getOrderBookProvider(String id);
        public TradeExecutor getTradeExecutor(String id);
        public OrderProvider getOrderProvider(String id);
    }
    
    private final Map<String, Binding> bindingsByName = new HashMap<>();
    
    public void cancelOrder (String bindingName, String id, String orderId) {
        Binding binding = getBinding(bindingName);
        
        log.info("Cancel order request for order id " + orderId + " from " + bindingName);
        
        binding.getTradeExecutor(id).cancel(orderId);
    }
    
    private Binding getBinding (String bindingName) {
        Binding binding;
        
        synchronized (this.bindingsByName) {
            binding = this.bindingsByName.get(bindingName);
        }
        
        if (binding == null) {
            throw new IllegalArgumentException("No binding for " + bindingName + " registered");
        }
        
        return binding;
    }
    
    public void getOpenOrders(String bindingName, String id, List<Order> orders) {
        Binding binding = getBinding(bindingName);
        binding.getOrderBookProvider(id).getOpenOrders(orders);
    }
    
    public void placeOrder (String bindingName, String id, Order order) {
        Binding binding = getBinding(bindingName);
        
        log.info("Place order request " + order + " from " + bindingName);
        
        binding.getTradeExecutor(id).execute(order);
    }
    
    public void register (String bindingName, Binding binding) {
        
        if (binding == null) {
            throw new IllegalArgumentException("Binding cannot be null");
        }
        
        synchronized (this.bindingsByName) {
            if (bindingsByName.containsKey(bindingName)) {
                throw new IllegalArgumentException("Binding for " + bindingName + " is already registered");
            }

            this.bindingsByName.put(bindingName, binding);
        }
    }
    
    public void unregister (String bindingName) {
        synchronized (this.bindingsByName) {
            if (this.bindingsByName.remove(bindingName) == null) {
                throw new IllegalArgumentException("No binding for " + bindingName + " registered");
            }
        }
    }
}
