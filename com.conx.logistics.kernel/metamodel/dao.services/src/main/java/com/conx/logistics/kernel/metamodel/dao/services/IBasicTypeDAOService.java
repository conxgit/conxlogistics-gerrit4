package com.conx.logistics.kernel.metamodel.dao.services;

import java.util.List;

import com.conx.logistics.mdm.domain.metamodel.BasicType;

public interface IBasicTypeDAOService {
	public BasicType get(long id);

	public List<BasicType> getAll();

	public BasicType getByClass(Class BasicClass);

	public BasicType add(BasicType record);

	public void delete(BasicType record);

	public BasicType update(BasicType record);

	public BasicType provide(Class basicJavaClass) throws ClassNotFoundException;
}
