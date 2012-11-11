package com.conx.logistics.kernel.ui.factory.services.data;

public interface IDAOProvider {
	public <T> T provideByDAOClass(Class<T> daoClass);
}
