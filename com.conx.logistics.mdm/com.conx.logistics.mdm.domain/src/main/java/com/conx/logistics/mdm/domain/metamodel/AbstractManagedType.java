package com.conx.logistics.mdm.domain.metamodel;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.metamodel.Type.PersistenceType;

@Entity
public abstract class AbstractManagedType extends AbstractType {
	
	@OneToOne
	private  AbstractManagedType superType;

    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private Set<EntityTypeAttribute> declaredAttributes
			= new HashSet<EntityTypeAttribute>();
    
/*    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private final Set<BasicAttribute> basicAttributes
			= new HashSet<BasicAttribute>();    
    
    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private final Set<SingularAttribute> declaredSingularAttributes
			= new HashSet<SingularAttribute>();
    
    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private final Set<PluralAttribute> declaredPluralAttributes
			= new HashSet<PluralAttribute>();*/

	public AbstractManagedType(String name, Class javaType,
			AbstractManagedType superType,PersistenceType persistentType) {
		super(name, javaType, javaType.getName(), javaType.getSimpleName(),persistentType);
		this.superType = superType;
	}

	public AbstractManagedType getSuperType() {
		return superType;
	}

	public void setSuperType(AbstractManagedType superType) {
		this.superType = superType;
	}

	public Set<EntityTypeAttribute> getDeclaredAttributes() {
		return declaredAttributes;
	}
	
	public Set<EntityTypeAttribute> getAllDeclaredAttributes() {
		HashSet<EntityTypeAttribute> attributes = new HashSet<EntityTypeAttribute>();
		addDeclaredAttributes(attributes, this);
		return attributes;
	}
	
	private void addDeclaredAttributes(HashSet<EntityTypeAttribute> attributes, AbstractManagedType type) {
		if (type.getSuperType() != null) {
			addDeclaredAttributes(attributes, type.getSuperType());
		}
		
		attributes.addAll(type.getDeclaredAttributes());
	}
	
/*	public Set<SingularAttribute> getDeclaredSingularAttributes() {
		return declaredSingularAttributes;
	}

	public Set<PluralAttribute> getDeclaredPluralAttributes() {
		return declaredPluralAttributes;
	} 
	
	public Set<BasicAttribute> getBasicAttributes() {
		return basicAttributes;
	}
*/
	public AbstractManagedType() {
		super();
	}	
}
