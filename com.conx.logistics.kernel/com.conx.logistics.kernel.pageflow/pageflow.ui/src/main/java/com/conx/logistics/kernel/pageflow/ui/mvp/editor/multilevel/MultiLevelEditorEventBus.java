package com.conx.logistics.kernel.pageflow.ui.mvp.editor.multilevel;

import java.util.Map;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.annotation.Event;

import com.conx.logistics.kernel.ui.components.domain.masterdetail.MasterDetailComponent;
import com.conx.logistics.mdm.domain.documentlibrary.FileEntry;
import com.vaadin.data.Container;
import com.vaadin.data.Item;

public interface MultiLevelEditorEventBus extends EventBus {
	@Event(handlers = { MultiLevelEditorPresenter.class })
	public void configure(Map<String, Object> params);
	@Event(handlers = { MultiLevelEditorPresenter.class })
	public void viewDocument(FileEntry viewable);
	@Event(handlers = { MultiLevelEditorPresenter.class })
	public void viewDocument(String url, String caption);
	@Event(handlers = { MultiLevelEditorPresenter.class })
	public void renderEditor(MasterDetailComponent componentModel);
	@Event(handlers = { MultiLevelEditorPresenter.class })
	public void renderEditor(MasterDetailComponent componentModel, Item item, Container itemContainer);
}
