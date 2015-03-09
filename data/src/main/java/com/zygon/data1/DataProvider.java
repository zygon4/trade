
package com.zygon.data1;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.AbstractScheduledService;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author zygon
 * @param <P>
 * @param <C>
 */
public abstract class DataProvider<P, C extends DataContract> implements Comparable<DataProvider> {
    
    private final class DataContractServer extends AbstractScheduledService {

        private final P provider;
        private final C dataContract;
        private final AbstractScheduledService.Scheduler scheduler;

        public DataContractServer(C dataContract) throws IOException {
            this.dataContract = dataContract;
            this.provider = DataProvider.this.createProvider(this.dataContract);
            this.scheduler = AbstractScheduledService.Scheduler.newFixedRateSchedule(0, this.dataContract.getCacheTime(), this.dataContract.getCacheTimeUnits());
        }

        public P getProvider() {
            return this.provider;
        }

        @Override
        protected void runOneIteration() throws Exception {
            Data data = DataProvider.this.getData(this.provider, this.dataContract);
            DataProvider.this.handle(this.dataContract, data);
        }

        @Override
        protected AbstractScheduledService.Scheduler scheduler() {
            return this.scheduler;
        }
    }
    
    private final String name;
    private final Set<C> dataContracts = Sets.newHashSet();
    private final Map<String,DataContractServer> dataServersByName = Maps.newHashMap();
    
    public DataProvider(String name) {
        Preconditions.checkNotNull(name);
        
        this.name = name;
    }
    
    public void add (C dataContract) {
        synchronized (this.dataContracts) {
            Preconditions.checkState(!this.dataContracts.contains(dataContract));
            this.dataContracts.add(dataContract);
        }
    }
    
    @Override
    public int compareTo(DataProvider t) {
        return this.name.compareTo(t.name);
    }
    
    public final void connect() {
        try {
            this.doConnect();
        } catch (IOException io) {
            // TODO: log
        }
    }

    // Maybe slightly misnamed - could be "data source"
    protected abstract P createProvider(C dataContract) throws IOException;

    public final void disConnect() {
        try {
            this.doDisconnect();
        } catch (IOException io) {
            // TODO: log
        }
    }

    protected void doConnect() throws IOException {
        // Nothing to do by default
    }

    protected void doDisconnect() throws IOException {
        // Nothing to do by default
    }

    private String createCollectionKey(C dataContract) {
        return this.getName()+"|"+dataContract.getContractName().replace(" ", "");
    }

    protected abstract Data getData(P provider, C dataContract) throws IOException;

    public final String getName() {
        return this.name;
    }

    protected void handle(C contract, Data data) {
        
    }

    public final void start (C dataContract) {
        synchronized (this.dataContracts) {
            Preconditions.checkState(this.dataContracts.contains(dataContract));

            try {
                // Locking a little extra long, shouldn't be a problem
                this.startContract(dataContract);
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
    }

    // Can override for custom behavior
    protected void startContract(C dataContract) throws IOException {
        synchronized (this.dataServersByName) {
            String contractCollectionKey = this.createCollectionKey(dataContract);

            Preconditions.checkState(!this.dataServersByName.containsKey(contractCollectionKey));
            
            try {
                DataContractServer dataServer = new DataContractServer(dataContract);
                
                this.dataServersByName.put(contractCollectionKey, dataServer);
                
                dataServer.startAsync();
                
                try {
                    dataServer.awaitRunning(10, TimeUnit.MINUTES);
                } catch (TimeoutException toe) {
                    toe.printStackTrace();
                }
                
            } catch (Exception e) {
                this.dataServersByName.remove(contractCollectionKey);
                throw new IOException(e);
            }
        }
    }

    public final void stop (C dataContract) {
        synchronized (this.dataContracts) {
            Preconditions.checkState(this.dataContracts.contains(dataContract));

            try {
                // Locking a little extra long, shouldn't be a problem
                this.stopContract(dataContract);
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
    }

    // Can override for custom behavior
    protected void stopContract(C dataContract) throws IOException{
        synchronized (this.dataServersByName) {
            String contractCollectionKey = this.createCollectionKey(dataContract);
            
            Preconditions.checkState(this.dataServersByName.containsKey(contractCollectionKey));
            
            DataContractServer dataServer = this.dataServersByName.remove(contractCollectionKey);
            dataServer.stopAsync();
            
            try {
                dataServer.awaitTerminated(10, TimeUnit.MINUTES);
            } catch (TimeoutException toe) {
                // Didn't shutdown cleanly.. TBD: something better
                toe.printStackTrace();
            }
        }
    }
}
