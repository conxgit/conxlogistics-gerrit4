package com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.collapsibleForm.view;

import java.util.Collection;

import com.conx.logistics.kernel.ui.components.domain.form.ConXCollapseableSectionForm;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.IEntityEditorComponentView;
import com.conx.logistics.kernel.ui.forms.vaadin.impl.VaadinCollapsibleSectionForm.IFormChangeListener;
import com.vaadin.data.Item;

public interface ICollapsibleFormEditorView extends IEntityEditorComponentView {
	public void setItemDataSource(Item item);
	public void setItemDataSource(Item item, Collection<?> propertyIds);
	public void setForm(ConXCollapseableSectionForm formComponent);
	public void setFormTitle(String title);
	public void addFormChangeListener(IFormChangeListener listener);
	public void saveForm();
	public void validateForm();
	public void resetForm();
	public void resizeForm(int height);
}