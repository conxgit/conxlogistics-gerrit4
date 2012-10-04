package com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.form.simple.view;

import java.util.Collection;

import org.vaadin.mvp.uibinder.IUiBindable;

import com.conx.logistics.kernel.ui.components.domain.form.ConXSimpleForm;
import com.conx.logistics.kernel.ui.forms.vaadin.listeners.IFormChangeListener;
import com.vaadin.data.Item;

public interface IEntityLineEditorSimpleFormView extends IUiBindable {
	public void setItemDataSource(Item item, Collection<?> propertyIds);
	public void setForm(ConXSimpleForm formComponent);
	public void setItemDataSource(Item item);
	public void setFormTitle(String title);
	public void addFormChangeListener(IFormChangeListener listener);
	public void saveForm();
	public boolean validateForm();
	public void resetForm();
	public void resizeForm(int height);
}