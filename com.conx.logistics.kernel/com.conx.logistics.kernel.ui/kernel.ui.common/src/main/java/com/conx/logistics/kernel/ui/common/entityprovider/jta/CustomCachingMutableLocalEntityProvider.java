package com.conx.logistics.kernel.ui.common.entityprovider.jta;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

import com.vaadin.addon.jpacontainer.provider.CachingMutableLocalEntityProvider;

public class CustomCachingMutableLocalEntityProvider<T> extends
		CachingMutableLocalEntityProvider<T> {

	private EntityManagerFactory entityManagerFactory;
	private UserTransaction userTransaction;

	public CustomCachingMutableLocalEntityProvider(Class<T> entityClass) {
		super(entityClass);
	}
	
	public CustomCachingMutableLocalEntityProvider(Class<T> entityClass, 
												  EntityManagerFactory entityManagerFactory,
												  UserTransaction userTransaction) {
		super(entityClass);
		this.entityManagerFactory = entityManagerFactory;
		this.userTransaction = userTransaction;
	}	
	
    @Override
    public boolean isEntitiesDetached() {
        return false;
    }

    @Override
    protected void runInTransaction(Runnable operation) {
        Util.runInJTATransaction(userTransaction, operation);
    }

    @Override
    public EntityManager getEntityManager() {
        return Util.getEntityManager(this.entityManagerFactory);
    }
}
