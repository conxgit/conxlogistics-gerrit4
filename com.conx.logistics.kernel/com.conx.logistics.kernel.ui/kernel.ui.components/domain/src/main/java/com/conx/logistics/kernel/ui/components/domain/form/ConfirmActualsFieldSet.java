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

import com.conx.logistics.kernel.ui.components.domain.AbstractConXComponent;

@Entity
public class ConfirmActualsFieldSet extends AbstractConXComponent {
	private static final long serialVersionUID = -2451618330566408888L;

	@Transient
	private Map<String, ConfirmActualsFieldSetField> fieldSetFieldMap = null;

	@OneToOne
	private ConXForm form;

	@OneToMany(mappedBy = "fieldSet", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<ConfirmActualsFieldSetField> fields = new HashSet<ConfirmActualsFieldSetField>();

	public ConfirmActualsFieldSet() {
		super("confirmactualsfieldset");
	}

	public Set<ConfirmActualsFieldSetField> getFields() {
		return fields;
	}

	public ConXForm getForm() {
		return form;
	}

	public void setForm(ConXForm form) {
		this.form = form;
	}

	public Map<String, ConfirmActualsFieldSetField> getFieldSetFieldMap() {
		if (fieldSetFieldMap == null) {
			fieldSetFieldMap = new HashMap<String, ConfirmActualsFieldSetField>();
			for (ConfirmActualsFieldSetField field : getFields()) {
				putConfirmActualsFieldSetField(field);
				// fieldSetFieldMap.put(field.getExpectedDataSourceField().getName(),
				// field);
				// fieldSetFieldMap.put(field.getActualDataSourceField().getName(),
				// field);
			}
		}
		return fieldSetFieldMap;
	}

	private String getExpectedDataSourceFieldName(ConfirmActualsFieldSetField field) {
		if (field.getExpectedDataSourceField().getValueXPath() == null) {
			return field.getExpectedDataSourceField().getName();
		} else {
			return field.getExpectedDataSourceField().getJPAPath();
		}
	}

	private String getActualDataSourceFieldName(ConfirmActualsFieldSetField field) {
		if (field.getActualDataSourceField().getValueXPath() == null) {
			return field.getActualDataSourceField().getName();
		} else {
			return field.getActualDataSourceField().getJPAPath();
		}
	}

	private void putConfirmActualsFieldSetField(ConfirmActualsFieldSetField field) {
		String expectedDataSourceFieldName = getExpectedDataSourceFieldName(field), actualDataSourceFieldName = getActualDataSourceFieldName(field);
		if (expectedDataSourceFieldName != null && actualDataSourceFieldName != null) {
			fieldSetFieldMap.put(expectedDataSourceFieldName, field);
			fieldSetFieldMap.put(actualDataSourceFieldName, field);
		}
	}

	public ConfirmActualsFieldSetField getFieldSetField(String fieldName) {
		return getFieldSetFieldMap().get(fieldName);
	}

	public boolean isExpected(Object propertyId) {
		for (ConfirmActualsFieldSetField field : getFields()) {
			if (getExpectedDataSourceFieldName(field).equals(propertyId)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isActual(Object propertyId) {
		for (ConfirmActualsFieldSetField field : getFields()) {
			if (getActualDataSourceFieldName(field).equals(propertyId)) {
				return true;
			}
		}
		return false;
	}
}
