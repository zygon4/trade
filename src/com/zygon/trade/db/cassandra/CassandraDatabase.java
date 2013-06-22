/**
 * 
 */

package com.zygon.trade.db.cassandra;

import com.zygon.trade.db.Database;
import java.io.IOException;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zygon
 */
public class CassandraDatabase implements Database {
    
    private static final Logger logger = LoggerFactory.getLogger(CassandraDatabase.class);
    
    // TODO: collapse into single database
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("cassandra_pu");
    private EntityManager em = this.emf.createEntityManager();

    public CassandraDatabase() {
        this.em.setProperty("cql.version", "3.0.0");
    }

    
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
        try {
            return this.em.find(cls, key);
        } catch (Exception e) {
            logger.error("error retrieving "+ key, e);
        }
        
        return null;
    }

    @Override
    public <T> Collection<T> retrieve(Class<T> cls, String query) {
        try {
            em.clear();
            TypedQuery<T> q = em.createQuery(query, cls);
            q.setMaxResults(Integer.MAX_VALUE);
            return q.getResultList();
        } catch (Exception e) {
            logger.error("error retrieving with query"+ query, e);
        }
        
        return null;
    }

    @Override
    public void store(Object obj) {
        try {
            this.em.persist(obj);
        } catch (Exception e) {
            logger.error("error persisting: " + obj, e);
        }
    }
}
