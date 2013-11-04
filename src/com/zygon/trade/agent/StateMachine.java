
package com.zygon.trade.agent;

import com.zygon.trade.market.model.indication.Indication;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zygon
 */
public class StateMachine {

    // This is meant to have a more robust criteria mechanism, price objectives,
    // market state, etc.
    public static class Criteria {
        private final Collection<Indication> indications;

        public Criteria(Collection<Indication> indications) {
            this.indications = indications;
        }

        public boolean matches(Indication indication) {
            for (Indication ind : this.indications) {
                if (ind.getId().equals(indication.getId())) {
                    return true;
                }
            }
            
            return false;
        }
    }
    
    public static class State {
        private final Map<Criteria, State> transitionsByIndication = new HashMap<Criteria, State>();
        
        
    }
    
    private State currentState = null;

    public StateMachine(State initialState) {
        this.currentState = initialState;
    }
    
    public void drive (Indication indication) {
        for (Criteria criteria : this.currentState.transitionsByIndication.keySet()) {
            if (criteria.matches(indication)) {
                this.currentState = this.currentState.transitionsByIndication.get(criteria);
            }
        }
    }
    
}
