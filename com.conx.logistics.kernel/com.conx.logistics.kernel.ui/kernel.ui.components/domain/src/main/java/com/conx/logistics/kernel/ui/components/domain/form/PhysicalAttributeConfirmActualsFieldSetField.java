package com.conx.logistics.kernel.ui.components.domain.form;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.conx.logistics.kernel.datasource.domain.DataSourceField;
import com.conx.logistics.kernel.ui.components.domain.AbstractConXField;

@Entity
public class PhysicalAttributeConfirmActualsFieldSetField extends AbstractConXField implements Serializable {
	private static final long serialVersionUID = -6947507271706691279L;

	@OneToOne
	private PhysicalAttributeConfirmActualsFieldSet fieldSet;
	
	@OneToOne
	private DataSourceField expectedDataSourceField;
	
	@OneToOne
	private DataSourceField expectedUnitDataSourceField;
	
	@OneToOne
	private DataSourceField actualDataSourceField;
	
	@OneToOne
	private DataSourceField actualUnitDataSourceField;
	
	private int ordinal;
	
	public PhysicalAttributeConfirmActualsFieldSetField() {
		super("fieldSetField");
		this.ordinal = -1;
	}
	
	public PhysicalAttributeConfirmActualsFieldSetField(int ordinal) {
		super("fieldSetField");
		this.ordinal = ordinal;
	}
	
	public PhysicalAttributeConfirmActualsFieldSetField(int ordinal, DataSourceField expectedDataSourceField, DataSourceField expectedUnitDataSourceField, DataSourceField actualDataSourceField, DataSourceField actualUnitDataSourceField) {
		this(ordinal);
		this.expectedDataSourceField = expectedDataSourceField;
		this.expectedUnitDataSourceField = expectedUnitDataSourceField;
		this.actualDataSourceField = actualDataSourceField;
		this.actualUnitDataSourceField = actualUnitDataSourceField;
	}

	public PhysicalAttributeConfirmActualsFieldSet getFieldSet() {
		return fieldSet;
	}

	public void setFieldSet(PhysicalAttributeConfirmActualsFieldSet fieldSet) {
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

	public DataSourceField getExpectedUnitDataSourceField() {
		return expectedUnitDataSourceField;
	}

	public void setExpectedUnitDataSourceField(DataSourceField expectedUnitDataSourceField) {
		this.expectedUnitDataSourceField = expectedUnitDataSourceField;
	}

	public DataSourceField getActualUnitDataSourceField() {
		return actualUnitDataSourceField;
	}

	public void setActualUnitDataSourceField(DataSourceField actualUnitDataSourceField) {
		this.actualUnitDataSourceField = actualUnitDataSourceField;
	}
}
