package com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.form.simple;

import java.util.Map;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.annotation.Event;

public interface EntityLineEditorSimpleFormEventBus extends EventBus {
	@Event(handlers = { EntityLineEditorSimpleFormPresenter.class })
	public void configure(Map<String, Object> params);
	
	@Event(handlers = { EntityLineEditorSimpleFormPresenter.class })
	public void saveForm();

	@Event(handlers = { EntityLineEditorSimpleFormPresenter.class })
	public void validateForm();

	@Event(handlers = { EntityLineEditorSimpleFormPresenter.class })
	public void resetForm();

	@Event(handlers = { EntityLineEditorSimpleFormPresenter.class })
	public void resizeForm(int newHeight);
}
