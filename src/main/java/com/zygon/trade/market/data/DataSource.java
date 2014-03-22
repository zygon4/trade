
package com.zygon.trade.market.data;

import com.zygon.data.Handler;
import com.zygon.trade.market.model.indication.Indication;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;

/**
 * This is a class to take in raw data feed from any source and turn it into 
 * usable data via interpreters.  Then, whom ever is subscribed to it can
 * receive its indications.
 *
 * @author zygon
 * 
 * TODO: better name
 */
public class DataSource<T> implements Handler<T> {

    // TODO: more about source type
    
    private final Collection<Interpreter<T>> interpreters;
    private final String identifier;
    
    private CompletionService<Indication[]> completionService = null;
    private DataSourceManager managerCtx = null;

    public DataSource(Collection<Interpreter<T>> interpreters, String identifier) {
        this.interpreters = interpreters;
        this.identifier = identifier;
    }

    public final String getIdentifier() {
        return this.identifier;
    }
    
    @Override
    public void handle(T r) {
        try {
            Collection<Indication> interpretData = this.interpretData(r);
            
            for (Indication ind : interpretData) {
                this.managerCtx.handle(this, ind);
            }
            
        } catch (ExecutionException exece) {
            // TBD:
        } catch (InterruptedException intrEx) {
            // TBD:
        }
    }
    
    private Collection<Indication> interpretData (final T t) throws InterruptedException, ExecutionException {
        Collection<Indication> messages = new ArrayList<>();
        
        int synchronousActions = 0;
        
        for (final Interpreter<T> trans : this.interpreters) {
            
            completionService.submit(new Callable<Indication[]>() {

                @Override
                public Indication[] call() throws Exception {
                    return trans.interpret(t);
                }
            });
            
            synchronousActions ++;
        }
        
        for (int i = 0; i < synchronousActions; i++) {
            Indication[] interpretResult = completionService.take().get();
            if (interpretResult != null && interpretResult.length != 0) {
                messages.addAll(Arrays.asList(interpretResult));
            }
         }
        
        return messages;
    }

    /*pkg*/ void setCompletionService(CompletionService<Indication[]> completionService) {
        this.completionService = completionService;
    }

    /*pkg*/ void setIndicationHandler(DataSourceManager managerCtx) {
        this.managerCtx = managerCtx;
    }
}
