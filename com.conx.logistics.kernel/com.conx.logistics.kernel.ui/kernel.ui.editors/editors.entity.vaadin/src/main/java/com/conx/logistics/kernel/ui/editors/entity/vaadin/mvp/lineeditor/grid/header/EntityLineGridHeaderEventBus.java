package com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.lineeditor.grid.header;

import java.util.HashMap;

import javax.persistence.EntityManager;

import org.vaadin.mvp.eventbus.annotation.Event;

import com.conx.logistics.kernel.ui.components.domain.AbstractConXComponent;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.AbstractEntityEditorEventBus;
import com.vaadin.addon.jpacontainer.EntityItem;

public interface EntityLineGridHeaderEventBus extends AbstractEntityEditorEventBus {
	@Event(handlers = { EntityLineGridHeaderPresenter.class })
	public void start(AbstractEntityEditorEventBus entityEditorEventListener,  AbstractConXComponent aec, EntityManager em, HashMap<String,Object> extraParams);
	@Event(handlers = { EntityLineGridHeaderPresenter.class })
	public void entityItemEdit(EntityItem item);
	@Event(handlers = { EntityLineGridHeaderPresenter.class })
	public void entityItemAdded(EntityItem item);		
}
