package com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.lineeditor.grid;

import java.util.HashMap;

import javax.persistence.EntityManager;

import org.vaadin.mvp.eventbus.annotation.Event;

import com.conx.logistics.kernel.ui.components.domain.AbstractConXComponent;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.AbstractEntityEditorEventBus;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.data.Item;

public interface EntityLineGridEventBus extends AbstractEntityEditorEventBus {
	@Event(handlers = { EntityLineGridPresenter.class })
	public void start(AbstractEntityEditorEventBus entityEditorEventListener,  AbstractConXComponent aec, EntityManager em, HashMap<String,Object> extraParams);
	@SuppressWarnings("rawtypes")
	@Event(handlers = { EntityLineGridPresenter.class })
	public void entityItemEdit(EntityItem item);
	@SuppressWarnings("rawtypes")
	@Event(handlers = { EntityLineGridPresenter.class })
	public void entityItemAdded(EntityItem item);
	@Event(handlers = { EntityLineGridPresenter.class })
	public void delete(Item item);
}
