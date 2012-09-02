package com.conx.logistics.kernel.ui.components.domain.form;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.conx.logistics.kernel.datasource.domain.DataSourceField;
import com.conx.logistics.kernel.ui.components.domain.AbstractConXField;

@Entity
public class FieldSetField extends AbstractConXField implements Serializable {
	private static final long serialVersionUID = -4371216577505004553L;

	@OneToOne
	private FieldSet fieldSet;
	
	@OneToOne
	private DataSourceField field;
	
	private int ordinal;
	
	public FieldSetField() {
		super("fieldSetField");
		this.ordinal = -1;
	}
	
	public FieldSetField(int ordinal) {
		super("fieldSetField");
		this.ordinal = ordinal;
	}
	
	public FieldSetField(int ordinal, DataSourceField field) {
		this(ordinal);
		this.field = field;
	}
	
	public FieldSetField(int ordinal, DataSourceField field, FieldSet fieldSet) {
		this(ordinal, field);
		this.fieldSet = fieldSet;
	}

	public FieldSet getFieldSet() {
		return fieldSet;
	}

	public void setFieldSet(FieldSet fieldSet) {
		this.fieldSet = fieldSet;
	}

	public DataSourceField getDataSourceField() {
		return field;
	}

	public void setDataSourceField(DataSourceField dsField) {
		this.field = dsField;
	}

	public int getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}
}
