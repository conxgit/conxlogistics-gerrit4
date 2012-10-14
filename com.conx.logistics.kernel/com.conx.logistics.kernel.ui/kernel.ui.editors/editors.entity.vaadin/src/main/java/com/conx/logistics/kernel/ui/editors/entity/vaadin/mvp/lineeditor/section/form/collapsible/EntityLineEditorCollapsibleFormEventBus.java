package com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.lineeditor.section.form.collapsible;

import org.vaadin.mvp.eventbus.annotation.Event;

import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.AbstractEntityEditorEventBus;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.data.Item;

public interface EntityLineEditorCollapsibleFormEventBus extends AbstractEntityEditorEventBus {
	@SuppressWarnings("rawtypes")
	@Event(handlers = { EntityLineEditorCollapsibleFormPresenter.class })
	public void entityItemEdit(EntityItem item);

	@SuppressWarnings("rawtypes")
	@Event(handlers = { EntityLineEditorCollapsibleFormPresenter.class })
	public void entityItemAdded(EntityItem item);

	@Event(handlers = { EntityLineEditorCollapsibleFormPresenter.class })
	public void setItemDataSource(Item item);

	@Event(handlers = { EntityLineEditorCollapsibleFormPresenter.class })
	public void saveForm();

	@Event(handlers = { EntityLineEditorCollapsibleFormPresenter.class })
	public void validateForm();

	@Event(handlers = { EntityLineEditorCollapsibleFormPresenter.class })
	public void resetForm();

	@Event(handlers = { EntityLineEditorCollapsibleFormPresenter.class })
	public void resizeForm(int newHeight);
}
