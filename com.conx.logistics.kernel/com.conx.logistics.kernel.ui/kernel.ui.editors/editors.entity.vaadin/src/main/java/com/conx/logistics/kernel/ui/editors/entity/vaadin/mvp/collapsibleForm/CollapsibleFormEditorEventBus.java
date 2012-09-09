package com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.collapsibleForm;

import org.vaadin.mvp.eventbus.annotation.Event;

import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.AbstractEntityEditorEventBus;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.data.Item;

public interface CollapsibleFormEditorEventBus extends AbstractEntityEditorEventBus {
	@SuppressWarnings("rawtypes")
	@Event(handlers = { CollapsibleFormEditorPresenter.class })
	public void entityItemEdit(EntityItem item);
	
	@SuppressWarnings("rawtypes")
	@Event(handlers = { CollapsibleFormEditorPresenter.class })
	public void entityItemAdded(EntityItem item);
	
	@Event(handlers = { CollapsibleFormEditorPresenter.class })
	public void setItemDataSource(Item item);
}
