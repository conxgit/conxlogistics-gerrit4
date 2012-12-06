package com.conx.logistics.kernel.ui.forms.vaadin.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import com.conx.logistics.kernel.ui.components.domain.form.ConXCollapseableSectionForm;
import com.conx.logistics.kernel.ui.components.domain.form.FieldSet;
import com.conx.logistics.kernel.ui.components.domain.form.FieldSetField;
import com.conx.logistics.kernel.ui.forms.vaadin.FormMode;
import com.conx.logistics.kernel.ui.forms.vaadin.IVaadinForm;
import com.conx.logistics.kernel.ui.forms.vaadin.impl.VaadinFormAlertPanel.AlertType;
import com.vaadin.data.Buffered;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.ui.Field;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class VaadinCollapsibleSectionForm extends VaadinForm implements IVaadinForm {
	private static final long serialVersionUID = -639931L;

	private VaadinFormHeader header;
	private VaadinFormAlertPanel alertPanel;
	private Panel innerLayoutPanel;
	private VerticalLayout layout;
	private VerticalLayout innerLayout;
	private ConXCollapseableSectionForm componentForm;
	private FormMode mode;
	private HashMap<FieldSet, VaadinCollapsibleSectionFormSectionHeader> headers;
	private HashMap<Field, FieldSetField> fields;

	public VaadinCollapsibleSectionForm(ConXCollapseableSectionForm componentForm) {
		this.setComponentModel(componentForm);
		this.componentForm = componentForm;
		this.innerLayoutPanel = new Panel();
		this.layout = new VerticalLayout();
		this.header = new VaadinFormHeader();
		this.alertPanel = new VaadinFormAlertPanel();
		this.headers = new HashMap<FieldSet, VaadinCollapsibleSectionFormSectionHeader>();
		this.fields = new HashMap<Field, FieldSetField>();
		this.innerLayout = new VerticalLayout();

		initialize();
	}

	@SuppressWarnings("deprecation")
	private void initialize() {
		this.alertPanel.setVisible(false);
		this.alertPanel.addCloseListener(new ClickListener() {
			private static final long serialVersionUID = 5815832688929242745L;

			@Override
			public void click(ClickEvent event) {
				VaadinCollapsibleSectionForm.this.alertPanel.setVisible(false);
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
		this.layout.addComponent(header);
		this.layout.addComponent(alertPanel);
		this.layout.addComponent(innerLayoutPanel);
		this.layout.setExpandRatio(innerLayoutPanel, 1.0f);

		setImmediate(true);
		setFormMode(FormMode.EDITING);
		setLayout(layout);
		// False so that commit() must be called explicitly
		setWriteThrough(false);
		// Disallow invalid data from acceptance by the container
		setInvalidCommitted(false);
	}

	@Override
	protected void attachField(Object propertyId, Field field) {
		FieldSet fieldSet = componentForm.getFieldSetForField((String) propertyId);
		if (fieldSet != null) {
			FieldSetField fieldComponent = fieldSet.getFieldSetField((String) propertyId);
			if (fieldComponent != null) {
				fields.put(field, fieldComponent);
				VaadinFormFieldAugmenter.augment(field, fieldComponent);
				if (this.getComponentModel().isReadOnly()) {
					field.setReadOnly(true);
				}
				VaadinCollapsibleSectionFormSectionHeader header = headers.get(fieldSet);
				if (header == null) {
					header = addFormSection(fieldSet);
				}
				field.setWidth("100%");
				header.getLayout().addComponent(field);
			}
		}
	}

	private VaadinCollapsibleSectionFormSectionHeader addFormSection(FieldSet fieldSet) {
		VaadinFormGridLayout content = new VaadinFormGridLayout();
		content.setMargin(false, true, false, true);
		VaadinCollapsibleSectionFormSectionHeader header = new VaadinCollapsibleSectionFormSectionHeader(fieldSet, content);
		innerLayout.addComponent(header);
		innerLayout.addComponent(content);
		headers.put(fieldSet, header);
		return header;
	}

	@Override
	public FormMode getFormMode() {
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
	public void setItemDataSource(com.vaadin.data.Item newDataSource, Collection<?> propertyIds) {
		this.innerLayout.removeAllComponents();
		this.headers.clear();
		super.setItemDataSource(newDataSource, propertyIds);
	}

	@Override
	public void setItemDataSource(com.vaadin.data.Item newDataSource) {
		this.innerLayout.removeAllComponents();
		this.headers.clear();
		super.setItemDataSource(newDataSource);
	}

	public boolean validateForm() {
		boolean firstErrorFound = false;
		Set<FieldSet> formFieldHeaders = headers.keySet();
		for (FieldSet fieldSet : formFieldHeaders) {
			headers.get(fieldSet).removeStyleName("conx-form-header-error");
		}
		Set<Field> formFields = fields.keySet();
		for (Field field : formFields) {
			VaadinCollapsibleSectionFormSectionHeader formFieldHeader = getFieldHeader(field);
			if (formFieldHeader != null) {
				try {
					field.validate();
					field.removeStyleName("conx-form-field-error");
				} catch (InvalidValueException e) {
					field.addStyleName("conx-form-field-error");
					formFieldHeader.addStyleName("conx-form-header-error");
					if (!firstErrorFound) {						
						this.alertPanel.setMessage(e.getMessage());
						this.alertPanel.setVisible(true);
						firstErrorFound = true;
					}
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
	
	@Override
	public boolean saveForm() {
		LinkedList<SourceException> problems = null;

		// Only commit on valid state if so requested
		if (!isInvalidCommitted() && !isValid()) {
			validate();
		}

		Set<Field> fieldSet = fields.keySet();

		// Try to commit all
		for (Field field : fieldSet) {
			try {
				// Commit only non-readonly fields.
				if (!field.isReadOnly()) {
					field.commit();
				}
			} catch (final Buffered.SourceException e) {
				if (problems == null) {
					problems = new LinkedList<SourceException>();
				}
				problems.add(e);
			}
		}

		// No problems occurred
		if (problems == null) {
			this.alertPanel.setAlertType(AlertType.SUCCESS);
			this.alertPanel.setMessage(this.header.getTitle() + " was saved successfully.");
			this.alertPanel.setVisible(true);
			return true;
		} else {
			this.alertPanel.setAlertType(AlertType.ERROR);
			this.alertPanel.setMessage(problems.iterator().next().getMessage());
			this.alertPanel.setVisible(true);
			return false;
		}
	}

	private VaadinCollapsibleSectionFormSectionHeader getFieldHeader(Field field) {
		FieldSetField fsf = fields.get(field);
		if (fsf != null) {
			FieldSet fs = fsf.getFieldSet();
			if (fs != null) {
				return headers.get(fs);
			}
		}
		return null;
	}

	public void resetForm() {
		this.alertPanel.setVisible(false);
		setItemDataSource(getItemDataSource());
	}
	
	public ConXCollapseableSectionForm getComponentModel() {
		return this.componentForm;
	}
}
