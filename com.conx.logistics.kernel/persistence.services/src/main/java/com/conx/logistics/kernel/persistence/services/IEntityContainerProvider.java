package com.conx.logistics.kernel.persistence.services;

public interface IEntityContainerProvider {
	public Object createPersistenceContainer(Class<?> entityClass);
}
