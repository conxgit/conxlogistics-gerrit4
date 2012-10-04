package com.conx.logistics.mdm.dao.services;

import java.util.List;

import com.conx.logistics.mdm.domain.metadata.DefaultEntityMetadata;
import com.conx.logistics.mdm.domain.organization.ContactType;

public interface IContactTypeDAOService {
	public ContactType get(long id);
	
	public List<ContactType> getAll();
	
	public ContactType getByCode(String code);

	public ContactType add(ContactType record);

	public void delete(ContactType record);

	public ContactType update(ContactType record);
	
	public ContactType provide(ContactType record);
	
	public ContactType provide(String code, String name);
	
	public void provideDefaults();
}
