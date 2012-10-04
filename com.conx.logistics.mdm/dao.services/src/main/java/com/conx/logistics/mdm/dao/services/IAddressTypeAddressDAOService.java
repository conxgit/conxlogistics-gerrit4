package com.conx.logistics.mdm.dao.services;

import java.util.List;

import com.conx.logistics.mdm.domain.geolocation.Address;
import com.conx.logistics.mdm.domain.geolocation.AddressType;
import com.conx.logistics.mdm.domain.geolocation.AddressTypeAddress;

public interface IAddressTypeAddressDAOService {
	public AddressTypeAddress get(long id);
	
	public List<AddressTypeAddress> getAll();
	
	public AddressTypeAddress getByCode(String code);	
	
	public AddressTypeAddress getById(Long id);

	public AddressTypeAddress add(AddressTypeAddress record);

	public void delete(AddressTypeAddress record);

	public AddressTypeAddress update(AddressTypeAddress record);
	
	public AddressTypeAddress provide(AddressTypeAddress record);
	
	public AddressTypeAddress provide(AddressType type, Address address);	
}
