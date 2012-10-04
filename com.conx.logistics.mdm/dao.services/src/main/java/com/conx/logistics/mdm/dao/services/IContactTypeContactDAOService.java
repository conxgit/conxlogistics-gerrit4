package com.conx.logistics.mdm.dao.services;

import java.util.List;

import com.conx.logistics.mdm.domain.organization.Contact;
import com.conx.logistics.mdm.domain.organization.ContactType;
import com.conx.logistics.mdm.domain.organization.ContactTypeContact;

public interface IContactTypeContactDAOService {
	public ContactTypeContact get(long id);
	
	public List<ContactTypeContact> getAll();
	
	public ContactTypeContact getByCode(String code);	
	
	public ContactTypeContact getByTypeAndContact(ContactType type, Contact contact);

	public ContactTypeContact add(ContactTypeContact record);

	public void delete(ContactTypeContact record);

	public ContactTypeContact update(ContactTypeContact record);
	
	public ContactTypeContact provide(ContactTypeContact record);
	
	public ContactTypeContact provide(ContactType type, Contact address);	
}
