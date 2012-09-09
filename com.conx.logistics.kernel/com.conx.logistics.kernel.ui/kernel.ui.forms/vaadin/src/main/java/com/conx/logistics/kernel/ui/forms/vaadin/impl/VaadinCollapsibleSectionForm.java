package com.conx.logistics.kernel.ui.forms.vaadin.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.conx.logistics.kernel.ui.components.domain.form.ConXCollapseableSectionForm;
import com.conx.logistics.kernel.ui.components.domain.form.FieldSet;
import com.conx.logistics.kernel.ui.components.domain.form.FieldSetField;
import com.conx.logistics.kernel.ui.forms.vaadin.FormMode;
import com.conx.logistics.kernel.ui.forms.vaadin.IVaadinForm;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class VaadinCollapsibleSectionForm extends Form implements IVaadinForm {
	private static final long serialVersionUID = -639931L;

	private VaadinFormHeader header;
	private VaadinFormAlertPanel alertPanel;
	private Panel innerLayoutPanel;
	private VerticalLayout layout;
	private VerticalLayout innerLayout;
	private ConXCollapseableSectionForm componentForm;
	private FormMode mode;
	private HashMap<FieldSet, VaadinCollapsibleSectionFormSectionHeader> headers;
	private Set<IFormChangeListener> formChangeListeners;

	public VaadinCollapsibleSectionForm(ConXCollapseableSectionForm componentForm) {
		this.componentForm = componentForm;
		this.innerLayoutPanel = new Panel();
		this.layout = new VerticalLayout();
		this.header = new VaadinFormHeader();
		this.alertPanel = new VaadinFormAlertPanel();
		this.headers = new HashMap<FieldSet, VaadinCollapsibleSectionFormSectionHeader>();
		this.innerLayout = new VerticalLayout();
		this.formChangeListeners = new HashSet<VaadinCollapsibleSectionForm.IFormChangeListener>();
		
		initialize();
	}
	
	@SuppressWarnings("deprecation")
	private void initialize() {
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
		setFormFieldFactory(new VaadinJPAFieldFactory());
		setLayout(layout);
		// False so that commit() must be called explicitly
		setWriteThrough(false);
		// Disallow invalid data from acceptance by the container
		setInvalidCommitted(false);
	}

	@Override
	protected void attachField(Object propertyId, Field field)  {
		FieldSet fieldSet = componentForm.getFieldSetForField((String) propertyId);
		if (fieldSet != null) {
			FieldSetField fieldComponent = fieldSet.getFieldSetField((String) propertyId);
			if (fieldComponent.isReadOnly()) {
				field.setEnabled(false);
			}
			if (field instanceof AbstractComponent) {
				((AbstractComponent) field).setImmediate(true);
			}
			if (field instanceof TextField) {
				((TextField) field).addListener(new TextChangeListener() {
					private static final long serialVersionUID = 1892620376403798194L;

					@Override
					public void textChange(TextChangeEvent event) {
						onFormChanged();
					}
				});
			} else {
				field.addListener(new ValueChangeListener() {
					private static final long serialVersionUID = -6182433271255560793L;

					@Override
					public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
						onFormChanged();
					}
				});
			}
			VaadinCollapsibleSectionFormSectionHeader header = headers.get(fieldSet);
			if (header == null) {
				header = addFormSection(fieldSet);
			}
			field.setWidth("100%");
			header.getLayout().addComponent(field);
		}
	}
	
	public void onFormChanged() {
		for (IFormChangeListener listener : this.formChangeListeners) {
			listener.onFormChanged();
		}
	}
	
	public void addFormChangeListener(IFormChangeListener listener) {
		this.formChangeListeners.add(listener);
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
	
	public interface IFormChangeListener {
		public void onFormChanged();
	}
}
