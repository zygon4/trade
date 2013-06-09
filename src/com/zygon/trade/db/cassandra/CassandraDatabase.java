/**
 * 
 */

package com.zygon.trade.db.cassandra;

import com.zygon.trade.db.Database;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author zygon
 */
public class CassandraDatabase implements Database {
    
    // TODO: collapse into single database
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("cassandra_pu");
    private EntityManager em = emf.createEntityManager();

    @Override
    public void close() throws IOException {
        try {
            this.em.close();
            this.em = null;
        } catch (Exception e) {
            if (this.em != null) {
                this.em = null;
            }
            
            try {
                this.emf.close();
                this.emf = null;
            } catch (Exception e2) {
                if (this.emf != null) {
                    this.emf = null;
                }
            }
        }
    }
    
    @Override
    public String getName() {
        return "CassDB";
    }
    
    @Override
    public <T> T retrieve(Class<T> cls, Object key) {
        return this.em.find(cls, key);
    }

    @Override
    public void store(Object obj) {
        this.em.persist(obj);
    }
}
