package com.conx.logistics.mdm.dao.services;

import java.util.List;

import com.conx.logistics.mdm.domain.geolocation.AddressType;

public interface IAddressTypeDAOService {
	public AddressType get(long id);
	
	public List<AddressType> getAll();
	
	public AddressType getByCode(String code);	

	public AddressType add(AddressType record);

	public void delete(AddressType record);

	public AddressType update(AddressType record);
	
	public AddressType provide(AddressType record);
	
	public AddressType provide(String code, String name);
	
	public void provideDefaults();
}
