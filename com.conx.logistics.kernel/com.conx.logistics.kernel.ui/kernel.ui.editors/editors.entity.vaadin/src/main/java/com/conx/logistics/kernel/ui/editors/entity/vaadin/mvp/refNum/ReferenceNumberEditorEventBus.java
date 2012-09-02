package com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.refNum;

import org.vaadin.mvp.eventbus.annotation.Event;

import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.AbstractEntityEditorEventBus;
import com.conx.logistics.mdm.domain.note.NoteItem;
import com.vaadin.addon.jpacontainer.EntityItem;

public interface ReferenceNumberEditorEventBus extends AbstractEntityEditorEventBus {
	@SuppressWarnings("rawtypes")
	@Event(handlers = { ReferenceNumberEditorPresenter.class })
	public void entityItemEdit(EntityItem item);
	
	@SuppressWarnings("rawtypes")
	@Event(handlers = { ReferenceNumberEditorPresenter.class })
	public void entityItemAdded(EntityItem item);		
	
	@Event(handlers = { ReferenceNumberEditorPresenter.class })
	public void noteItemAdded(NoteItem ni);
}
