package com.conx.logistics.kernel.pageflow.ui.mvp.editor.grid.view;

import org.vaadin.mvp.uibinder.IUiBindable;

import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.table.EntityEditorGrid.IEditListener;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.table.EntityEditorGrid.ISelectListener;
import com.vaadin.data.Container;
import com.vaadin.data.Item;

public interface IEditorGridView extends IUiBindable {
	public void setContainerDataSource(Container container);

	public void setVisibleColumns(Object[] columnIds);

	public void addEditListener(IEditListener listener);

	public void addSelectListener(ISelectListener listener);

	public void deleteItem(Item item) throws Exception;
	
	public void init();
}