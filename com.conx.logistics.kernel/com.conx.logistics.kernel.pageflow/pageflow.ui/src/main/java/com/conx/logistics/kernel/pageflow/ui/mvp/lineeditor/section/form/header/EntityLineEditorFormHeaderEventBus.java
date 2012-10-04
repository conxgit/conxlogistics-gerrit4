package com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.form.header;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.annotation.Event;

import com.vaadin.data.Item;

public interface EntityLineEditorFormHeaderEventBus extends EventBus {
	@Event(handlers = { EntityLineEditorFormHeaderPresenter.class })
	public void setItemDataSource(Item item);
}
