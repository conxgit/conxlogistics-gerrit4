package com.conx.logistics.kernel.ui.forms.vaadin.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.conx.logistics.kernel.ui.components.domain.form.ConXSimpleForm;
import com.conx.logistics.kernel.ui.components.domain.form.FieldSet;
import com.conx.logistics.kernel.ui.components.domain.form.FieldSetField;
import com.conx.logistics.kernel.ui.forms.vaadin.FormMode;
import com.conx.logistics.kernel.ui.forms.vaadin.IVaadinForm;
import com.conx.logistics.kernel.ui.forms.vaadin.listeners.IFormChangeListener;
import com.vaadin.data.Item;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class VaadinSimpleForm extends Form implements IVaadinForm {
	private static final long serialVersionUID = -639931L;

	private VaadinFormHeader header;
	private VaadinFormAlertPanel alertPanel;
	private VerticalLayout layout;
	private Panel innerLayoutPanel;
	private GridLayout innerLayout;
	private ConXSimpleForm componentForm;
	private Set<Field> fields;
	private Set<IFormChangeListener> formChangeListeners;

	private FormMode mode;

	public VaadinSimpleForm(ConXSimpleForm componentForm) {
		this.header = new VaadinFormHeader();
		this.layout = new VerticalLayout();
		this.innerLayout = new GridLayout(4, 3);
		this.alertPanel = new VaadinFormAlertPanel();
		this.componentForm = componentForm;
		this.formChangeListeners = new HashSet<IFormChangeListener>();
		this.fields = new HashSet<Field>();

		initialize();
	}

	@SuppressWarnings("deprecation")
	private void initialize() {
		this.alertPanel.setVisible(false);
		this.alertPanel.addCloseListener(new ClickListener() {
			private static final long serialVersionUID = 5815832688929242745L;

			@Override
			public void click(ClickEvent event) {
				VaadinSimpleForm.this.alertPanel.setVisible(false);
			}
		});

		this.innerLayout.setWidth("100%");
		this.innerLayout.setSpacing(true);
		this.innerLayout.setMargin(true);
		this.innerLayout.setHeight(-1, UNITS_PIXELS);

		this.innerLayoutPanel = new Panel();
		this.innerLayoutPanel.setSizeFull();
		this.innerLayoutPanel.getLayout().setMargin(false, true, false, true);
		this.innerLayoutPanel.setStyleName("light");
		this.innerLayoutPanel.addComponent(innerLayout);

		this.layout.setWidth("100%");
		this.layout.setStyleName("conx-entity-editor-form");
		this.layout.addComponent(alertPanel);
		this.layout.addComponent(header);
		this.layout.addComponent(innerLayoutPanel);
		this.layout.setExpandRatio(innerLayoutPanel, 1.0f);

		setImmediate(true);
		setFormMode(FormMode.EDITING);
		setFormFieldFactory(new VaadinJPAFieldFactory());
		setLayout(layout);
		// False so that commit() must be called explicitly
		setWriteThrough(false);
		// Disallow invalid data from acceptance by the container
		setInvalidCommitted(false);
	}

	public FormMode getMode() {
		return mode;
	}

	@Override
	public void setFormMode(FormMode mode) {
		this.mode = mode;
		switch (mode) {
		case CREATING:
			this.header.setAction("Creating");
			break;
		case EDITING:
			this.header.setAction("Editing");
			break;
		}
	}

	@Override
	public void setTitle(String title) {
		this.header.setTitle(title);
	}

	@Override
	protected void attachField(Object propertyId, com.vaadin.ui.Field field) {
		if (propertyId == null || field == null) {
			return;
		}
		FieldSet fieldSet = componentForm.getFieldSet();
		if (fieldSet != null) {
			FieldSetField fieldComponent = fieldSet.getFieldSetField((String) propertyId);
			if (fieldComponent != null) {
				fields.add(field);
				VaadinFormFieldAugmenter.augment(field, fieldComponent, new ValueChangeListener() {
					private static final long serialVersionUID = -6182433271255560793L;

					@Override
					public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
						onFormChanged();
					}
				});
				field.setWidth("100%");
				innerLayout.addComponent(field);
			}
		}
	}

	public void addFormChangeListener(IFormChangeListener listener) {
		this.formChangeListeners.add(listener);
	}

	@Override
	public void setItemDataSource(Item newDataSource, Collection<?> propertyIds) {
		this.innerLayout.removeAllComponents();
		this.fields.clear();
		super.setItemDataSource(newDataSource, propertyIds);
	}
	
	@Override
	public void setItemDataSource(Item newDataSource) {
		this.innerLayout.removeAllComponents();
		this.fields.clear();
		super.setItemDataSource(newDataSource);
	}

	@Override
	public FormMode getFormMode() {
		return mode;
	}

	public void onFormChanged() {
		for (IFormChangeListener listener : this.formChangeListeners) {
			listener.onFormChanged();
		}
	}

	public void resetForm() {
		this.alertPanel.setVisible(false);
		setItemDataSource(getItemDataSource());
	}

	public boolean validateForm() {
		boolean firstErrorFound = false;
		for (Field field : fields) {
			try {
				field.validate();
				field.removeStyleName("conx-form-field-error");
			} catch (InvalidValueException e) {
				field.addStyleName("conx-form-field-error");
				if (!firstErrorFound) {
					this.alertPanel.setMessage(e.getMessage());
					this.alertPanel.setVisible(true);
					firstErrorFound = true;
				}
			}
		}
		if (firstErrorFound) {
			return false;
		} else {
			this.alertPanel.setVisible(false);
			return true;
		}
	}
}
