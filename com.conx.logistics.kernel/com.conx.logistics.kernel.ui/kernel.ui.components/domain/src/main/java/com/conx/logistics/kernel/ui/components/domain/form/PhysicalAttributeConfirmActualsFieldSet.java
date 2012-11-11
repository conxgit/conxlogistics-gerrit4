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
public class PhysicalAttributeConfirmActualsFieldSet extends AbstractConXComponent {
	private static final long serialVersionUID = -2451618330566408888L;

	@Transient
	private Map<String, PhysicalAttributeConfirmActualsFieldSetField> fieldSetFieldMap = null;

	@OneToOne
	private ConXForm form;

	@OneToMany(mappedBy = "fieldSet", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<PhysicalAttributeConfirmActualsFieldSetField> fields = new HashSet<PhysicalAttributeConfirmActualsFieldSetField>();

	public PhysicalAttributeConfirmActualsFieldSet() {
		super("confirmactualsfieldset");
	}

	public Set<PhysicalAttributeConfirmActualsFieldSetField> getFields() {
		return fields;
	}

	public ConXForm getForm() {
		return form;
	}

	public void setForm(ConXForm form) {
		this.form = form;
	}

	public Map<String, PhysicalAttributeConfirmActualsFieldSetField> getFieldSetFieldMap() {
		if (fieldSetFieldMap == null) {
			fieldSetFieldMap = new HashMap<String, PhysicalAttributeConfirmActualsFieldSetField>();
			for (PhysicalAttributeConfirmActualsFieldSetField field : getFields()) {
				putConfirmActualsFieldSetField(field);
				// fieldSetFieldMap.put(field.getExpectedDataSourceField().getName(),
				// field);
				// fieldSetFieldMap.put(field.getActualDataSourceField().getName(),
				// field);
			}
		}
		return fieldSetFieldMap;
	}

	private String getExpectedDataSourceFieldName(PhysicalAttributeConfirmActualsFieldSetField field) {
		if (field.getExpectedDataSourceField().getValueXPath() == null) {
			return field.getExpectedDataSourceField().getName();
		} else {
			return field.getExpectedDataSourceField().getJPAPath();
		}
	}
	
	private String getExpectedUnitDataSourceFieldName(PhysicalAttributeConfirmActualsFieldSetField field) {
		if (field.getExpectedUnitDataSourceField().getValueXPath() == null) {
			return field.getExpectedUnitDataSourceField().getName();
		} else {
			return field.getExpectedUnitDataSourceField().getJPAPath();
		}
	}

	private String getActualDataSourceFieldName(PhysicalAttributeConfirmActualsFieldSetField field) {
		if (field.getActualDataSourceField().getValueXPath() == null) {
			return field.getActualDataSourceField().getName();
		} else {
			return field.getActualDataSourceField().getJPAPath();
		}
	}
	
	private String getActualUnitDataSourceFieldName(PhysicalAttributeConfirmActualsFieldSetField field) {
		if (field.getActualUnitDataSourceField().getValueXPath() == null) {
			return field.getActualUnitDataSourceField().getName();
		} else {
			return field.getActualUnitDataSourceField().getJPAPath();
		}
	}

	private void putConfirmActualsFieldSetField(PhysicalAttributeConfirmActualsFieldSetField field) {
		String expectedDataSourceFieldName = getExpectedDataSourceFieldName(field), 
				expectedUnitDataSourceFieldName = getExpectedUnitDataSourceFieldName(field), 
				actualDataSourceFieldName = getActualDataSourceFieldName(field), 
				actualUnitDataSourceFieldName = getActualUnitDataSourceFieldName(field);
		if (expectedDataSourceFieldName != null && actualDataSourceFieldName != null) {
			fieldSetFieldMap.put(expectedDataSourceFieldName, field);
			fieldSetFieldMap.put(expectedUnitDataSourceFieldName, field);
			fieldSetFieldMap.put(actualDataSourceFieldName, field);
			fieldSetFieldMap.put(actualUnitDataSourceFieldName, field);
		}
	}

	public PhysicalAttributeConfirmActualsFieldSetField getFieldSetField(String fieldName) {
		return getFieldSetFieldMap().get(fieldName);
	}

	public boolean isExpected(Object propertyId) {
		PhysicalAttributeConfirmActualsFieldSetField field = getFieldSetFieldMap().get(propertyId);
		if (getExpectedDataSourceFieldName(field).equals(propertyId)) {
			return true;
		}
		return false;
	}
	
	public boolean isExpectedUnit(Object propertyId) {
		PhysicalAttributeConfirmActualsFieldSetField field = getFieldSetFieldMap().get(propertyId);
		if (getExpectedUnitDataSourceFieldName(field).equals(propertyId)) {
			return true;
		}
		return false;
	}
	
	public boolean isActual(Object propertyId) {
		PhysicalAttributeConfirmActualsFieldSetField field = getFieldSetFieldMap().get(propertyId);
		if (getActualDataSourceFieldName(field).equals(propertyId)) {
			return true;
		}
		return false;
	}
	
	public boolean isActualUnit(Object propertyId) {
		PhysicalAttributeConfirmActualsFieldSetField field = getFieldSetFieldMap().get(propertyId);
		if (getActualUnitDataSourceFieldName(field).equals(propertyId)) {
			return true;
		}
		return false;
	}
}
