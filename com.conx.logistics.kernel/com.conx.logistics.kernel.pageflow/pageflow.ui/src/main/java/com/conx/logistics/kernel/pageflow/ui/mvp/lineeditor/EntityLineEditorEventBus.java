package com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor;

import java.util.Map;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.annotation.Event;

import com.vaadin.data.Item;

public interface EntityLineEditorEventBus extends EventBus {
	@Event(handlers = { EntityLineEditorPresenter.class })
	public void configure(Map<String, Object> params);
	@Event(handlers = { EntityLineEditorPresenter.class })
	public void setItemDataSource(Item item);
}
