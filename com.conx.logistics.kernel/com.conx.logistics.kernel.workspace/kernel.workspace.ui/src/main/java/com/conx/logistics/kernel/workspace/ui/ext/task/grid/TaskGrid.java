package com.conx.logistics.kernel.workspace.ui.ext.task.grid;

import com.conx.logistics.kernel.ui.filteredtable.FilterDecorator;
import com.conx.logistics.kernel.ui.filteredtable.FilterGenerator;
import com.conx.logistics.kernel.ui.filteredtable.FilterTable;
import com.vaadin.data.Container.Filter;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.VerticalLayout;

public class TaskGrid extends VerticalLayout implements FilterDecorator,
		FilterGenerator {
	private FilterTable grid;
	public TaskGrid() {
		initialize();
	}
	
	private void initialize() {
		this.grid.setSizeFull();
		this.grid.setMultiSelect(false);
		this.grid.setSelectable(true);
		this.grid.setNullSelectionAllowed(false);
		this.grid.setFilterDecorator(this);
		this.grid.setFilterGenerator(this);
		this.grid.setFiltersVisible(true);
		this.grid.addListener(new ItemClickListener() {
			private static final long serialVersionUID = -4650994592971165778L;

			@Override
			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick()) {
					//onEdit(event.getItemId());
				} else if (event.getButton() == ItemClickEvent.BUTTON_LEFT) {
					//onSelect(event.getItemId());
				}
			}
		});

		setSizeFull();
		setStyleName("conx-entity-grid");
		addComponent(grid);
		setExpandRatio(grid, 1.0f);
	}


	@Override
	public Filter generateFilter(Object propertyId, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEnumFilterDisplayName(Object propertyId, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource getEnumFilterIcon(Object propertyId, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBooleanFilterDisplayName(Object propertyId, boolean value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource getBooleanFilterIcon(Object propertyId, boolean value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isTextFilterImmediate(Object propertyId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getTextChangeTimeout(Object propertyId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getFromCaption() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToCaption() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSetCaption() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getClearCaption() {
		// TODO Auto-generated method stub
		return null;
	}

}
