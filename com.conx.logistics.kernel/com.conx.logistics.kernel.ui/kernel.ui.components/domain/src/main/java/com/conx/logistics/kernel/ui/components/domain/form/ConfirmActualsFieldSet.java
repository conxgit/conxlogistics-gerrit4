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
				fieldSetFieldMap.put(field.getExpectedDataSourceField().getName(), field);
				fieldSetFieldMap.put(field.getActualDataSourceField().getName(), field);
			}
		}
		return fieldSetFieldMap;
	}
	
	public ConfirmActualsFieldSetField getFieldSetField(String fieldName) {
		return getFieldSetFieldMap().get(fieldName);
	}
	
	public boolean isExpected(Object propertyId) {
		for (ConfirmActualsFieldSetField field : getFields()) {
			if (field.getExpectedDataSourceField().getName().equals(propertyId)) {
				return true;
			}
		}
		return false;
	}
}
