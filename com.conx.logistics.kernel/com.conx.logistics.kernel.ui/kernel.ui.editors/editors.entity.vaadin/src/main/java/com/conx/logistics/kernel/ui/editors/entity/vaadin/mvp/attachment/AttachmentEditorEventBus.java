package com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.attachment;

import java.util.HashMap;

import javax.persistence.EntityManager;

import org.vaadin.mvp.eventbus.annotation.Event;

import com.conx.logistics.kernel.ui.components.domain.AbstractConXComponent;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.AbstractEntityEditorEventBus;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.footer.EntityTableFooterPresenter;
import com.conx.logistics.mdm.domain.documentlibrary.FileEntry;
import com.vaadin.addon.jpacontainer.EntityItem;

public interface AttachmentEditorEventBus extends AbstractEntityEditorEventBus {
	@Event(handlers = { AttachmentEditorPresenter.class })
	public void start(AbstractEntityEditorEventBus entityEditorEventListener, AbstractConXComponent aec, EntityManager em, HashMap<String,Object> extraParams);

	@Event(handlers = { AttachmentEditorPresenter.class })
	public void entityItemEdit(EntityItem item);
	
	@Event(handlers = { AttachmentEditorPresenter.class })
	public void entityItemAdded(EntityItem item);		
	
	@Event(handlers = { AttachmentEditorPresenter.class })
	public void fileEntryAdded(FileEntry fe);		
}
