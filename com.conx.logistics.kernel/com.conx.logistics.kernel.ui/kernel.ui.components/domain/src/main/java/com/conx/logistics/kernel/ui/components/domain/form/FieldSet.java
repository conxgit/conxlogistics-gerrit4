package com.conx.logistics.kernel.ui.components.domain.form;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.conx.logistics.kernel.datasource.domain.DataSourceField;
import com.conx.logistics.kernel.ui.components.domain.AbstractConXComponent;
import com.conx.logistics.kernel.ui.components.domain.layout.AbstractConXLayout;

@Entity
public class FieldSet extends AbstractConXComponent {
	private static final long serialVersionUID = -806259542376394951L;

	@Transient
	private Map<String, DataSourceField> fieldMap = null;
	
	@OneToOne
	private ConXForm form;

	@OneToMany(mappedBy = "fieldSet", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<FieldSetField> fields = new HashSet<FieldSetField>();

	@OneToOne
	private AbstractConXLayout layout;

	private int ordinal;
	
	public FieldSet() {
		super("fieldSet");
		this.ordinal = -1;
	}

	public FieldSet(int ordinal) {
		super("fieldSet");
		this.ordinal = ordinal;
	}

	public FieldSet(int ordinal, String caption, AbstractConXLayout layout) {
		this(ordinal);
		this.setCaption(caption);
		this.setLayout(layout);
	}

	public FieldSet(int ordinal, String caption, Set<FieldSetField> fields, AbstractConXLayout layout) {
		this(ordinal, caption, layout);
		this.fields = fields;
	}

	public Set<FieldSetField> getFields() {
		return fields;
	}

	public void setFields(Set<FieldSetField> fields) {
		this.fields = fields;
	}

	public Map<String, DataSourceField> getFieldMap() {
		if (fieldMap == null) {
			fieldMap = new HashMap<String, DataSourceField>();
			for (FieldSetField field : getFields()) {
				fieldMap.put(field.getDataSourceField().getName(), field.getDataSourceField());
			}
		}
		return fieldMap;
	}

	public Boolean hasField(String fieldName) {
		return getFieldMap().keySet().contains(fieldName);
	}

	public DataSourceField getField(String fieldName) {
		return getFieldMap().get(fieldName);
	}

	public AbstractConXLayout getLayout() {
		return layout;
	}

	public void setLayout(AbstractConXLayout layout) {
		this.layout = layout;
	}

	public int getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}

	public ConXForm getForm() {
		return form;
	}

	public void setForm(ConXForm form) {
		this.form = form;
	}
}
