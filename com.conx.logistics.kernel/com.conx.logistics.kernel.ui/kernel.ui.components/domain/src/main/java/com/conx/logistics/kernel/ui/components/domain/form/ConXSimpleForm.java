package com.conx.logistics.kernel.ui.components.domain.form;

import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.conx.logistics.kernel.datasource.domain.DataSource;
import com.conx.logistics.kernel.datasource.domain.DataSourceField;
import com.conx.logistics.kernel.ui.components.domain.layout.AbstractConXLayout;

@Entity
public class ConXSimpleForm extends ConXForm {
	private static final long serialVersionUID = -2009961101456261981L;
	
	@OneToOne(cascade=CascadeType.ALL)
	private FieldSet fieldSet;

	public ConXSimpleForm() {
		super("simpleForm");
	}
	
	public ConXSimpleForm(DataSource ds) {
		this();
		this.setDataSource(ds);
	}
	
	public ConXSimpleForm(DataSource ds, String caption) {
		this(ds);
		this.setCaption(caption);
	}
	
	public ConXSimpleForm(DataSource ds, String caption, AbstractConXLayout layout) {
		this(ds, caption);
		this.fieldSet.setLayout(layout);
	}
	
	public ConXSimpleForm(DataSource ds, String caption, AbstractConXLayout layout, Set<FieldSetField> fields) {
		this(ds, caption, layout);
		this.fieldSet.setFields(fields);
	}
	
	public Set<FieldSetField> getFields() {
		return this.fieldSet.getFields();
	}

	public void setFields(Set<FieldSetField> fields) {
		this.fieldSet.setFields(fields);
	}

	public Map<String, DataSourceField> getFieldMap() {
		return fieldSet.getFieldMap();
	}

	public Boolean hasField(String fieldName) {
		return fieldSet.getFieldMap().keySet().contains(fieldName);
	}

	public DataSourceField getField(String fieldName) {
		return fieldSet.getFieldMap().get(fieldName);
	}

	public AbstractConXLayout getLayout() {
		return fieldSet.getLayout();
	}

	public void setLayout(AbstractConXLayout layout) {
		this.fieldSet.setLayout(layout);
	}
	
	@Override
	public String getCaption() {
		if (this.fieldSet == null) {
			return super.getCaption();
		} else {
			return this.fieldSet.getCaption();
		}
	}
	
	@Override
	public void setCaption(String caption) {
		if (this.fieldSet != null) {
			super.setCaption(caption);
		}
	}

	public FieldSet getFieldSet() {
		return fieldSet;
	}

	public void setFieldSet(FieldSet fieldSet) {
		this.fieldSet = fieldSet;
	}
}
