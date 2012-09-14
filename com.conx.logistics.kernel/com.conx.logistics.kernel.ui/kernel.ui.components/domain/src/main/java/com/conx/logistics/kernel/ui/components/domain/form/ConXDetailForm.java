package com.conx.logistics.kernel.ui.components.domain.form;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class ConXDetailForm extends ConXForm {
	private static final long serialVersionUID = -3105118712425596679L;

	@OneToMany(mappedBy = "form", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<FieldSet> fieldSetSet = new HashSet<FieldSet>();

	public ConXDetailForm() {
		super("detailForm");
	}
	
	public ConXDetailForm(Set<FieldSet> fieldSetSet) {
		super("detailForm");
		this.fieldSetSet = fieldSetSet;
	}
	
	public Set<FieldSet> getFieldSetSet() {
		return fieldSetSet;
	}

	public void setFieldSetSet(Set<FieldSet> fieldSetSet) {
		this.fieldSetSet = fieldSetSet;
	}
	
	public FieldSet getFieldSetForField(String fieldName) {
		for (FieldSet fs : fieldSetSet) {
			if (fs.getFieldSetField(fieldName) != null) {
				return fs;
			}
		}
		return null;
	}
}
