package com.conx.logistics.kernel.ui.components.domain.form;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.conx.logistics.kernel.datasource.domain.DataSourceField;
import com.conx.logistics.kernel.ui.components.domain.AbstractConXField;

@Entity
public class ConfirmActualsFieldSetField extends AbstractConXField implements Serializable {
	private static final long serialVersionUID = -6947507271706691279L;

	@OneToOne
	private ConfirmActualsFieldSet fieldSet;
	
	@OneToOne
	private DataSourceField expectedDataSourceField;
	
	@OneToOne
	private DataSourceField actualDataSourceField;
	
	private int ordinal;
	
	public ConfirmActualsFieldSetField() {
		super("fieldSetField");
		this.ordinal = -1;
	}
	
	public ConfirmActualsFieldSetField(int ordinal) {
		super("fieldSetField");
		this.ordinal = ordinal;
	}
	
	public ConfirmActualsFieldSetField(int ordinal, DataSourceField expectedDataSourceField, DataSourceField actualDataSourceField) {
		this(ordinal);
		this.expectedDataSourceField = expectedDataSourceField;
		this.actualDataSourceField = actualDataSourceField;
	}

	public ConfirmActualsFieldSet getFieldSet() {
		return fieldSet;
	}

	public void setFieldSet(ConfirmActualsFieldSet fieldSet) {
		this.fieldSet = fieldSet;
	}

	public DataSourceField getExpectedDataSourceField() {
		return expectedDataSourceField;
	}

	public void setExpectedDataSourceField(DataSourceField dsField) {
		this.expectedDataSourceField = dsField;
	}
	
	public DataSourceField getActualDataSourceField() {
		return actualDataSourceField;
	}

	public void setActualDataSourceField(DataSourceField dsField) {
		this.actualDataSourceField = dsField;
	}

	public int getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}
	
	public boolean isRequired() {
		if (this.expectedDataSourceField != null) {
			return this.expectedDataSourceField.getRequired();
		}
		return false;
	}
}
