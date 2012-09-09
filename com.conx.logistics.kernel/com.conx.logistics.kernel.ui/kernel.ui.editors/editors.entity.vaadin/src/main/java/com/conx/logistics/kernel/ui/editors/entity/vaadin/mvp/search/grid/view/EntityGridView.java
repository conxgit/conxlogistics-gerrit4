package com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.search.grid.view;

import org.vaadin.mvp.uibinder.annotation.UiField;

import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.table.EntityEditorGrid;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.table.EntityEditorGrid.IEditListener;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.table.EntityEditorGrid.ISelectListener;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.VerticalLayout;

public class EntityGridView extends VerticalLayout implements IEntityGridView {
	private static final long serialVersionUID = 1L;

	@UiField
	VerticalLayout mainLayout;
	
	private EntityEditorGrid grid;
	
	public EntityGridView() {
		setSizeFull();
	}
	
	@Override
	public void setContainerDataSource(Container container) {
		grid.setContainerDataSource(container);
	}

	@Override
	public void init() {
		if (mainLayout != null) {
			this.grid = new EntityEditorGrid();
			mainLayout.removeAllComponents();
			mainLayout.addComponent(grid);
			mainLayout.setExpandRatio(grid, 1.0f);
		}
	}

	@Override
	public void setVisibleColumns(Object[] columnIds) {
		this.grid.setVisibleColumns(columnIds);
	}

	@Override
	public void addEditListener(IEditListener listener) {
		this.grid.addEditListener(listener);
	}

	@Override
	public void addSelectListener(ISelectListener listener) {
		this.grid.addSelectListener(listener);
	}

	@Override
	public void deleteItem(Item item) throws Exception {
		this.grid.deleteItem(item);
	}
}
