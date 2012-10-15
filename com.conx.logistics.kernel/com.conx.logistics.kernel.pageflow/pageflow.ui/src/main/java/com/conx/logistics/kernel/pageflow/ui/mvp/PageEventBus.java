package com.conx.logistics.kernel.pageflow.ui.mvp;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.annotation.Event;

import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.EntityLineEditorPresenter;
import com.vaadin.data.Item;

public interface PageEventBus extends EventBus {
	@Event(handlers = { EntityLineEditorPresenter.class })
	public void setItemDataSource(Item item);
}
