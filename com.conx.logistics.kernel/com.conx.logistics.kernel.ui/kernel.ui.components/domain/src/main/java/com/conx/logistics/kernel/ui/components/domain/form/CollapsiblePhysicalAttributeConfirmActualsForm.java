package com.conx.logistics.kernel.ui.components.domain.form;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.conx.logistics.kernel.datasource.domain.DataSource;

@Entity
public class CollapsiblePhysicalAttributeConfirmActualsForm extends ConXForm {
	private static final long serialVersionUID = 2274128688130249656L;
	
	@OneToMany(mappedBy = "form", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<PhysicalAttributeConfirmActualsFieldSet> fieldSetSet = new HashSet<PhysicalAttributeConfirmActualsFieldSet>();
	
	public CollapsiblePhysicalAttributeConfirmActualsForm() {
		super("collapsiblephysicalattributeconfirmactualsform");
	}
	
	public CollapsiblePhysicalAttributeConfirmActualsForm(DataSource ds) {
		this();
		this.setDataSource(ds);
	}
	
	public CollapsiblePhysicalAttributeConfirmActualsForm(DataSource ds, String caption) {
		this(ds);
		this.setCaption(caption);
	}

	public Set<PhysicalAttributeConfirmActualsFieldSet> getFieldSetSet() {
		return fieldSetSet;
	}

	public void setFieldSetSet(Set<PhysicalAttributeConfirmActualsFieldSet> fieldSetSet) {
		this.fieldSetSet = fieldSetSet;
	}

	public PhysicalAttributeConfirmActualsFieldSet getFieldSetForField(String fieldName) {
		for (PhysicalAttributeConfirmActualsFieldSet fs : fieldSetSet) {
			if (fs.getFieldSetField(fieldName) != null) {
				return fs;
			}
		}
		return null;
	}
}
