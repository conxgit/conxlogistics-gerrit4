package com.conx.logistics.kernel.ui.components.domain.form;

import javax.persistence.Entity;

import com.conx.logistics.kernel.datasource.domain.DataSource;
import com.conx.logistics.kernel.ui.components.domain.AbstractConXField;

@Entity
public abstract class ConXForm extends AbstractConXField {
	private static final long serialVersionUID = 2526927001704625541L;

	public ConXForm(String typeId) {
		super(typeId);
	}
	
	public ConXForm(String typeId, DataSource ds) {
		this(typeId);
		this.setDataSource(ds);
	}
}
