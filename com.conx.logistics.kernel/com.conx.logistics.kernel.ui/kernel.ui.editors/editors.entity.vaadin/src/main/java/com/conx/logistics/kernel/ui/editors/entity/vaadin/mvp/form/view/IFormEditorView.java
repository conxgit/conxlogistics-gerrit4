package com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.form.view;

import java.util.Collection;

import com.conx.logistics.kernel.ui.components.domain.form.ConXForm;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.IEntityEditorComponentView;
import com.vaadin.data.Item;

public interface IFormEditorView extends IEntityEditorComponentView {
	public void setItemDataSource(Item item, Collection<?> propertyIds);
	public void setForm(ConXForm formComponent);
}