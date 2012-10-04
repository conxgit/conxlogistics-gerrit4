package com.conx.logistics.kernel.ui.components.domain.form;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.conx.logistics.kernel.datasource.domain.DataSource;

@Entity
public class CollapsibleConfirmActualsForm extends ConXForm {
	private static final long serialVersionUID = 2274128688130249656L;
	
	@OneToMany(mappedBy = "form", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<ConfirmActualsFieldSet> fieldSetSet = new HashSet<ConfirmActualsFieldSet>();
	
	public CollapsibleConfirmActualsForm() {
		super("collapsibleconfirmactualsform");
	}
	
	public CollapsibleConfirmActualsForm(DataSource ds) {
		this();
		this.setDataSource(ds);
	}
	
	public CollapsibleConfirmActualsForm(DataSource ds, String caption) {
		this(ds);
		this.setCaption(caption);
	}

	public Set<ConfirmActualsFieldSet> getFieldSetSet() {
		return fieldSetSet;
	}

	public void setFieldSetSet(Set<ConfirmActualsFieldSet> fieldSetSet) {
		this.fieldSetSet = fieldSetSet;
	}

	public ConfirmActualsFieldSet getFieldSetForField(String fieldName) {
		for (ConfirmActualsFieldSet fs : fieldSetSet) {
			if (fs.getFieldSetField(fieldName) != null) {
				return fs;
			}
		}
		return null;
	}
}
