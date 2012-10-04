package com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section;

import java.util.Map;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.annotation.Event;

import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.form.header.EntityLineEditorFormHeaderPresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.grid.EntityLineEditorGridPresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.grid.header.EntityLineEditorGridHeaderPresenter;
import com.vaadin.data.Item;

public interface EntityLineEditorSectionEventBus extends EventBus {
	@Event(handlers = { EntityLineEditorGridPresenter.class })
	public void createItem();
	@Event(handlers = { EntityLineEditorGridPresenter.class })
	public void editItem();
	@Event(handlers = { EntityLineEditorGridPresenter.class })
	public void deleteItem();
	@Event(handlers = { EntityLineEditorGridPresenter.class })
	public void printGrid();
	
	@Event(handlers = { EntityLineEditorGridHeaderPresenter.class })
	public void itemSelected();
	@Event(handlers = { EntityLineEditorGridHeaderPresenter.class })
	public void itemsDepleted();
	
	@Event(handlers = { EntityLineEditorSectionPresenter.class })
	public void saveForm();
	@Event(handlers = { EntityLineEditorSectionPresenter.class })
	public void validateForm();
	@Event(handlers = { EntityLineEditorSectionPresenter.class })
	public void resetForm();
	@Event(handlers = { EntityLineEditorSectionPresenter.class })
	public void resizeForm(int newHeight);
	@Event(handlers = { EntityLineEditorSectionPresenter.class })
	public void configure(Map<String, Object> params);
	@Event(handlers = { EntityLineEditorSectionPresenter.class })
	public void setItemDataSource(Item item);
	
	@Event(handlers = { EntityLineEditorFormHeaderPresenter.class })
	public void formChanged();
	@Event(handlers = { EntityLineEditorFormHeaderPresenter.class })
	public void formValidated();
}
