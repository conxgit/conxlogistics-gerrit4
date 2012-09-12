package com.conx.logistics.kernel.ui.common.entityprovider.jta;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.addon.jpacontainer.provider.CachingMutableLocalEntityProvider;

public class CustomCachingMutableLocalEntityProvider<T> extends
		CachingMutableLocalEntityProvider<T> {
	
	protected static Logger logger = LoggerFactory.getLogger(CustomCachingMutableLocalEntityProvider.class);

	private EntityManagerFactory entityManagerFactory;
	private UserTransaction userTransaction;

	private EntityManager entityManager;

	public CustomCachingMutableLocalEntityProvider(Class<T> entityClass) {
		super(entityClass);
	}
	
	public CustomCachingMutableLocalEntityProvider(Class<T> entityClass, 
												  EntityManagerFactory entityManagerFactory,
												  UserTransaction userTransaction) {
		super(entityClass);
		this.entityManagerFactory = entityManagerFactory;
		this.entityManager = entityManagerFactory.createEntityManager();
		this.userTransaction = userTransaction;
	}	
	
    @Override
    public boolean isEntitiesDetached() {
        return false;
    }

    @Override
    protected void runInTransaction(Runnable operation) {
        try {
        	this.userTransaction.begin();
        	this.entityManager.joinTransaction();
            operation.run();
            this.userTransaction.commit();
        } catch (Exception e) {
            try {
            	this.userTransaction.rollback();
            } catch (Exception e2) {
            	logger.error("Rollback failed", e2);
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public EntityManager getEntityManager() {
        return this.entityManager;
    }
}
