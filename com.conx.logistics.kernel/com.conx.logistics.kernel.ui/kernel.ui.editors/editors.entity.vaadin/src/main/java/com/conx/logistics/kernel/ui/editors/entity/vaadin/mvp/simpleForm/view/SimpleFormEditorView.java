package com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.simpleForm.view;

import java.util.Collection;

import org.vaadin.mvp.uibinder.annotation.UiField;

import com.conx.logistics.kernel.ui.components.domain.form.ConXForm;
import com.conx.logistics.kernel.ui.components.domain.form.ConXSimpleForm;
import com.conx.logistics.kernel.ui.forms.vaadin.IVaadinForm;
import com.conx.logistics.kernel.ui.forms.vaadin.impl.VaadinSimpleForm;
import com.vaadin.data.Item;
import com.vaadin.ui.VerticalLayout;

public class SimpleFormEditorView extends VerticalLayout implements ISimpleFormEditorView {
	private static final long serialVersionUID = 1L;

	@UiField
	VerticalLayout mainLayout;
	
	private IVaadinForm form;
	
	public SimpleFormEditorView() {
	}
	
	@Override
	public void setItemDataSource(Item item, Collection<?> propertyIds) {
		this.form.setItemDataSource(item, propertyIds);
	}

	@Override
	public void init() {
		setSizeFull();
	}

	@Override
	public void setForm(ConXForm formComponent) {
		if (formComponent instanceof ConXSimpleForm) {
			this.form = new VaadinSimpleForm((ConXSimpleForm) formComponent);
		}
		this.form.setSizeFull();
		this.form.setTitle(formComponent.getCaption());
		this.mainLayout.removeAllComponents();
		this.mainLayout.addComponent(form);
	}
}
