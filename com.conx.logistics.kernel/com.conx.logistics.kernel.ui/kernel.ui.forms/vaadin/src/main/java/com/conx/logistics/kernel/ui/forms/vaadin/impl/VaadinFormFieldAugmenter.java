package com.conx.logistics.kernel.ui.forms.vaadin.impl;

import com.conx.logistics.kernel.ui.components.domain.AbstractConXField;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Field;

public class VaadinFormFieldAugmenter {
	public static void augment(final Field field, final ValueChangeListener valueChangeListener) {
		field.addListener(valueChangeListener);
	}
	
	public static void augment(Field field, AbstractConXField componentModel) {
		if (componentModel.isReadOnly()) {
			field.setEnabled(false);
		} 
		if (componentModel.isRequired()) {
			field.setRequired(true);
			field.setRequiredError(field.getCaption() + " is a required field.");
		}
		if (field instanceof AbstractComponent) {
			((AbstractComponent) field).setImmediate(true);
		}
		if (componentModel.getDataSourceField() != null && componentModel.getDataSourceField().getTitle() != null) {
			field.setCaption(componentModel.getDataSourceField().getTitle());
		}
		
		if (field instanceof AbstractField) {
			((AbstractField) field).setValidationVisible(false);
		}
	}
	
	public static void augment(final Field field, AbstractConXField componentModel, final ValueChangeListener valueChangeListener) {
		augment(field, componentModel);
		augment(field, valueChangeListener);
	}
}
