
package com.zygon.data.set;

import com.zygon.data.DataSourceLoader;
import com.zygon.data.Context;
import com.zygon.data.Handler;
import com.zygon.database.DataTransform;
import com.zygon.database.Database;
import java.io.IOException;

/**
 * This represents a data set for the system to utilize.  A data set might be 
 * used during a strategy simulation or as seed data for a data feed.  A "uri" 
 * must be present in the context.  The uri's scheme will dictate the 
 * interpretation of the data.  The data will be stored internally to the 
 * system.
 * 
 * TBD:
 * Things we need:
 * - Data source via String/reflection to go from raw source => in memory object
 * - A way to store the data internally (e.g. a cassandra transform) to go from
 *   in memory object => serialized local storage
 *
 * @author zygon
 */
public class DataSet<T> {
    
    public static final String URI = "uri";
    public static final String DATA_TRANSFORM_CLS = "transform-cls";
    
    private final Context ctx;
    private final String name;
    
    private final DataSourceLoader<T> dataSource;
    private final DataTransform<T> dataTransform;
    private final Database database;
    
    public DataSet(Context ctx, DataSourceLoader<T> dataSource, DataTransform<T> dataTransform, Database database) {
        this.ctx = ctx;
        this.name = ctx.getName();
        
        // TODO: context to pass in path to data source?
        this.dataSource = dataSource;
        this.dataTransform = dataTransform;
        this.database = database;
        
//        String uri = ctx.getProperty(URI);
//        if (uri == null) {
//            throw new IllegalArgumentException(URI + " must be present");
//        }
//        
//        URI dataSetURI = null;
//        
//        try {
//            dataSetURI = new URI(uri);
//        } catch (URISyntaxException uriSyntax) {
//            throw new IllegalArgumentException(uriSyntax);
//        }
//        
//        DataSourceLoader<T> provider = null;
//        
//        if (dataSetURI.getScheme().equals("file")) {
//            // TODO: new csv provider
//        } else {
//            throw new UnsupportedOperationException("schema not supported: " + dataSetURI.getScheme());
//        }
//        
//        this.dataSource = provider;
//        
//        String transformCls = this.ctx.getProperty(DATA_TRANSFORM_CLS);
//        if (transformCls == null) {
//            throw new IllegalArgumentException(DATA_TRANSFORM_CLS + " must be present");
//        }
//        
//        this.dataTransform
        
        // TODO:
//        this.database.addTransform(this.dataTransform);
    }

    public void delete() {
        // TODO: remove data, remove any transforms
//        this.database.removeTransform(this.dataTransform);
    }
    
    public String getName() {
        return this.name;
    }
    
    /**
     * Causes the data set to be refreshed.  Subsequent calls to writeData will
     * reflect any changes that occurred during the last call to reload.
     * @throws IOException 
     */
    public synchronized void reload() throws IOException {
        this.dataSource.writeData(new Handler<T>() {
            @Override
            public void handle(T r) {
                
                // TODO:
//                database.persist(r);
            }
        });
    }
    
    public synchronized void writeData (Handler<T> handler) throws IOException {
        // TODO: read from database instead of direct source (ie flat-file0
        this.dataSource.writeData(handler);
    }
}
