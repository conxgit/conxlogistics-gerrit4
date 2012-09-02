package com.conx.logistics.kernel.ui.forms.vaadin.impl;

import java.util.Collection;

import com.conx.logistics.kernel.ui.components.domain.form.ConXSimpleForm;
import com.conx.logistics.kernel.ui.forms.vaadin.FormMode;
import com.conx.logistics.kernel.ui.forms.vaadin.IVaadinForm;
import com.vaadin.data.Item;
import com.vaadin.ui.Form;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.VerticalLayout;

public class VaadinSimpleForm extends Form implements IVaadinForm {
	private static final long serialVersionUID = -639931L;

	private VaadinFormHeader header;
	private VerticalLayout layout;
	private GridLayout innerLayout;
	
	private FormMode mode;

	public VaadinSimpleForm(ConXSimpleForm componentForm) {
		this.header = new VaadinFormHeader();
		this.layout = new VerticalLayout();
		this.innerLayout = new GridLayout(4, 3);
		
		initialize();
	}
	
	private void initialize() {
		this.header = new VaadinFormHeader();
		this.header.setTitle("Note");
		
		this.innerLayout.setWidth("100%");
		this.innerLayout.setSpacing(true);
		this.innerLayout.setMargin(true);
		this.innerLayout.setHeight(-1, UNITS_PIXELS);
		
		this.layout.setWidth("100%");
		this.layout.setStyleName("conx-entity-editor-form");
		this.layout.addComponent(header);
		this.layout.addComponent(innerLayout);
		
		setSizeFull();
		setFormMode(FormMode.EDITING);
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
		field.setWidth("100%");
		innerLayout.addComponent(field);
	}
	
	@Override
	public void setItemDataSource(Item newDataSource, Collection<?> propertyIds) {
		this.innerLayout.removeAllComponents();
		super.setItemDataSource(newDataSource, propertyIds);
	}

	@Override
	public FormMode getFormMode() {
		return mode;
	}
}
