package com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.form.collapsible;

import java.util.Map;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.annotation.Event;

import com.vaadin.data.Item;

public interface EntityLineEditorCollapsibleFormEventBus extends EventBus {
	@Event(handlers = { EntityLineEditorCollapsibleFormPresenter.class })
	public void configure(Map<String, Object> params);
	
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
