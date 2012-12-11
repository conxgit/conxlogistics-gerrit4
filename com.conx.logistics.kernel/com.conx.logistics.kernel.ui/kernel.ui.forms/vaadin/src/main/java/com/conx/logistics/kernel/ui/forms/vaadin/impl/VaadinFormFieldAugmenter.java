package com.conx.logistics.kernel.ui.forms.vaadin.impl;

import com.conx.logistics.kernel.ui.components.domain.AbstractConXField;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Field;

public class VaadinFormFieldAugmenter {
	public static void augment(final Field field, final ValueChangeListener valueChangeListener) {
		field.addListener(valueChangeListener);
//		if (field instanceof TextField) {
//			((TextField) field).addListener(new FieldEvents.TextChangeListener() {
//				private static final long serialVersionUID = 1892620376403798194L;
//
//				@Override
//				public void textChange(TextChangeEvent event) {
//					field.setValue(event.getText());
//					valueChangeListener.valueChange(new ValueChangeEvent() {
//						private static final long serialVersionUID = 1L;
//
//						@Override
//						public Property getProperty() {
//							return field.getPropertyDataSource();
//						}
//					});
//				}
//			});
//		} else {
//			field.addListener(valueChangeListener);
//		}
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
	}
	
	public static void augment(final Field field, AbstractConXField componentModel, final ValueChangeListener valueChangeListener) {
		augment(field, componentModel);
		augment(field, valueChangeListener);
	}
}
