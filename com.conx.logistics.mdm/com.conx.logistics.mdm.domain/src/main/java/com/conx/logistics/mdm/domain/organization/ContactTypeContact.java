package com.conx.logistics.mdm.domain.organization;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.conx.logistics.mdm.domain.IRelationEntity;
import com.conx.logistics.mdm.domain.MultitenantBaseEntity;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name="refcontacttypecontact")
public class ContactTypeContact extends MultitenantBaseEntity implements IRelationEntity {
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	private ContactType type;
	
	@OneToOne
	@JoinColumn()
	private Contact contact;
	
	public ContactTypeContact() {
	}
	
	public ContactTypeContact(ContactType type, Contact contact) {
		this.type = type;
		this.contact = contact;
	}

	public ContactType getType() {
		return type;
	}

	public void setType(ContactType type) {
		this.type = type;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	@Override
	public Object getIdentifierPropertyId() {
		return "type";
	}

	@Override
	public Object getEntityPropertyId() {
		return "contact";
	}

}
