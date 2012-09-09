package com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.simpleForm;

import org.vaadin.mvp.eventbus.annotation.Event;

import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.AbstractEntityEditorEventBus;
import com.vaadin.addon.jpacontainer.EntityItem;

public interface SimpleFormEditorEventBus extends AbstractEntityEditorEventBus {
	@SuppressWarnings("rawtypes")
	@Event(handlers = { SimpleFormEditorPresenter.class })
	public void entityItemEdit(EntityItem item);
	
	@SuppressWarnings("rawtypes")
	@Event(handlers = { SimpleFormEditorPresenter.class })
	public void entityItemAdded(EntityItem item);
}
