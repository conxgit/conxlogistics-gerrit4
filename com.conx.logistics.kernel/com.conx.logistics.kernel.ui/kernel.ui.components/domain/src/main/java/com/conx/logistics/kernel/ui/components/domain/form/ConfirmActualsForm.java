package com.conx.logistics.kernel.ui.components.domain.form;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.conx.logistics.kernel.datasource.domain.DataSource;

@Entity
public class ConfirmActualsForm extends ConXForm {
	private static final long serialVersionUID = 2274128688130249656L;
	
	@OneToOne(cascade=CascadeType.ALL)
	private ConfirmActualsFieldSet fieldSet;
	
	public ConfirmActualsForm() {
		super("confirmactualsform");
	}
	
	public ConfirmActualsForm(DataSource ds) {
		this();
		this.setDataSource(ds);
	}
	
	public ConfirmActualsForm(DataSource ds, String caption) {
		this(ds);
		this.setCaption(caption);
	}

	public ConfirmActualsFieldSet getFieldSet() {
		return fieldSet;
	}

	public void setFieldSet(ConfirmActualsFieldSet fieldSet) {
		this.fieldSet = fieldSet;
	}

}
