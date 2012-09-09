package com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.collapsibleForm.view;

import java.util.Collection;

import org.vaadin.mvp.uibinder.annotation.UiField;

import com.conx.logistics.kernel.ui.components.domain.form.ConXCollapseableSectionForm;
import com.conx.logistics.kernel.ui.forms.vaadin.impl.VaadinCollapsibleSectionForm;
import com.conx.logistics.kernel.ui.forms.vaadin.impl.VaadinCollapsibleSectionForm.IFormChangeListener;
import com.vaadin.data.Item;
import com.vaadin.ui.VerticalLayout;

public class CollapsibleFormEditorView extends VerticalLayout implements ICollapsibleFormEditorView {
	private static final long serialVersionUID = 1L;

	@UiField
	VerticalLayout mainLayout;
	
	private VaadinCollapsibleSectionForm form;
	
	public CollapsibleFormEditorView() {
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
	public void setForm(ConXCollapseableSectionForm formComponent) {
		this.form = new VaadinCollapsibleSectionForm(formComponent);
		this.form.setSizeFull();
		this.form.setTitle(formComponent.getCaption());
		this.mainLayout.removeAllComponents();
		this.mainLayout.addComponent(this.form);
	}

	@Override
	public void setFormTitle(String title) {
		this.form.setTitle(title);
	}

	@Override
	public void saveForm() {
		this.form.commit();
	}

	@Override
	public void validateForm() {
		this.form.validate();
	}

	@Override
	public void resetForm() {
		this.form.setItemDataSource(this.form.getItemDataSource());
	}

	@Override
	public void setItemDataSource(Item item) {
		this.form.setItemDataSource(item);
	}

	@Override
	public void addFormChangeListener(IFormChangeListener listener) {
		this.form.addFormChangeListener(listener);
	}

	@Override
	public void resizeForm(int height) {
		this.form.getLayout().setHeight(height + "px");
	}
}
