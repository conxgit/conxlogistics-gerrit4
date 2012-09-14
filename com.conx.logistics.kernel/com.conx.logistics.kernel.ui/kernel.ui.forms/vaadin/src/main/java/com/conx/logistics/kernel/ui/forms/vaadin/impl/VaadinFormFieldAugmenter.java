package com.conx.logistics.kernel.ui.forms.vaadin.impl;

import com.conx.logistics.kernel.ui.components.domain.form.FieldSetField;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;

public class VaadinFormFieldAugmenter {
	public static void augment(Field field, FieldSetField componentModel) {
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
	}
	
	public static void augment(final Field field, FieldSetField componentModel, final ValueChangeListener valueChangeListener) {
		augment(field, componentModel);
		if (field instanceof TextField) {
			((TextField) field).addListener(new FieldEvents.TextChangeListener() {
				private static final long serialVersionUID = 1892620376403798194L;

				@Override
				public void textChange(TextChangeEvent event) {
					valueChangeListener.valueChange(new ValueChangeEvent() {
						private static final long serialVersionUID = 1L;

						@Override
						public Property getProperty() {
							return field.getPropertyDataSource();
						}
					});
				}
			});
		} else {
			field.addListener(valueChangeListener);
		}
	}
}
