
package com.zygon.data1;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AbstractScheduledService;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
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

    public static interface DataHandler<T extends DataContract> {
        public void handle(T contract, Data data);
    }

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
            Data data = null;
            try {
                data = DataProvider.this.getData(this.provider, this.dataContract);
            } catch (IOException io) {
                io.printStackTrace(System.err);
            }
            if (data != null) {
                DataProvider.this.handle(this.dataContract, data);
            }
        }

        @Override
        protected AbstractScheduledService.Scheduler scheduler() {
            return this.scheduler;
        }
    }

    private final String name;
    private final Map<String,DataContractServer> dataServersByName = Maps.newHashMap();
    private final Map<C,Set<DataHandler<C>>> dataHandlersByContract;

    public DataProvider(String name, Map<C,Set<DataHandler<C>>> dataHandlersByContract) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(dataHandlersByContract);

        this.name = name;
        this.dataHandlersByContract = Collections.unmodifiableMap(dataHandlersByContract);
    }

    public DataProvider(String name) {
        this(name, Collections.EMPTY_MAP);
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

    public final Collection<C> getContracts() {
        return ImmutableSet.copyOf(this.dataHandlersByContract.keySet());
    }

    protected abstract Data getData(P provider, C dataContract) throws IOException;

    public final String getName() {
        return this.name;
    }

    protected void handle(C contract, Data data) {
        Set<DataHandler<C>> handlers = this.dataHandlersByContract.get(contract);

        for (DataHandler<C> handler : handlers) {
            handler.handle(contract, data);
        }
    }

    public final void start (C dataContract) {
        synchronized (this.dataHandlersByContract) {
            Preconditions.checkState(this.dataHandlersByContract.containsKey(dataContract));

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
        synchronized (this.dataHandlersByContract) {
            Preconditions.checkState(this.dataHandlersByContract.containsKey(dataContract));

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
