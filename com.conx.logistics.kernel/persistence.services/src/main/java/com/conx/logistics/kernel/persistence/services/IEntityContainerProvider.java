package com.conx.logistics.kernel.persistence.services;

import javax.persistence.EntityManagerFactory;

public interface IEntityContainerProvider {
	public Object createPersistenceContainer(Class<?> entityClass);
	public Object createBeanContainer(Class<?> entityClass);
	public EntityManagerFactory getEmf();
}
