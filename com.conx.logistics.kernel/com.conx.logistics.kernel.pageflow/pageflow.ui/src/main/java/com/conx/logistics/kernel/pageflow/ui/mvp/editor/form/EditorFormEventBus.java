package com.conx.logistics.kernel.pageflow.ui.mvp.editor.form;

import java.util.Map;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.annotation.Event;

import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.form.EntityLineEditorFormPresenter;
import com.vaadin.data.Item;

public interface EditorFormEventBus extends EventBus {
	@Event(handlers = { EntityLineEditorFormPresenter.class })
	public void configure(Map<String, Object> params);
	
	@Event(handlers = { EntityLineEditorFormPresenter.class })
	public void validate();
	
	@Event(handlers = { EntityLineEditorFormPresenter.class })
	public void save();
	
	@Event(handlers = { EntityLineEditorFormPresenter.class })
	public void reset();
	
	@Event(handlers = { EntityLineEditorFormPresenter.class })
	public void setItemDataSource(Item item);
}
