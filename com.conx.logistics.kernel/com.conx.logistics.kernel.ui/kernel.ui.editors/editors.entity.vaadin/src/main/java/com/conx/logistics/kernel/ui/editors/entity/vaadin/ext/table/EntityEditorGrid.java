package com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.table;

import java.util.HashSet;
import java.util.Set;

import com.conx.logistics.kernel.ui.filteredtable.FilterDecorator;
import com.conx.logistics.kernel.ui.filteredtable.FilterGenerator;
import com.conx.logistics.kernel.ui.filteredtable.FilterTable;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.VerticalLayout;

public class EntityEditorGrid extends VerticalLayout implements FilterDecorator, FilterGenerator {
	private static final long serialVersionUID = 2367339435187822029L;

	private Set<IEditListener> editListenerSet;
	private Set<ISelectListener> selectListenerSet;

	private FilterTable grid;

	public EntityEditorGrid() {
		this.grid = new FilterTable();
		this.editListenerSet = new HashSet<EntityEditorGrid.IEditListener>();
		this.selectListenerSet = new HashSet<EntityEditorGrid.ISelectListener>();

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
					onEdit(event.getItemId());
				} else if (event.getButton() == ItemClickEvent.BUTTON_LEFT) {
					onSelect(event.getItemId());
				}
			}
		});

		setSizeFull();
		setStyleName("conx-entity-grid");
		addComponent(grid);
		setExpandRatio(grid, 1.0f);
	}

	private void onEdit(Object id) {
		if (id != null) {
			Item item = this.grid.getItem(id);
			for (IEditListener listener : editListenerSet) {
				listener.onEdit(item);
			}
		}
	}

	public void addEditListener(IEditListener listener) {
		editListenerSet.add(listener);
	}

	private void onSelect(Object id) {
		if (id != null) {
			Item item = this.grid.getItem(id);
			for (ISelectListener listener : selectListenerSet) {
				listener.onSelect(item);
			}
		}
	}

	public void addSelectListener(ISelectListener listener) {
		selectListenerSet.add(listener);
	}

	public void setContainerDataSource(Container container) {
		this.grid.setContainerDataSource(container);
	}

	public void setVisibleColumns(Object[] visibleColumnIds) {
		this.grid.setVisibleColumns(visibleColumnIds);
	}

	public interface IEditListener {
		public void onEdit(Item item);
	}

	public interface ISelectListener {
		public void onSelect(Item item);
	}

	@Override
	public Filter generateFilter(Object propertyId, Object value) {
		return null;
	}

	@Override
	public String getEnumFilterDisplayName(Object propertyId, Object value) {
		return null;
	}

	@Override
	public Resource getEnumFilterIcon(Object propertyId, Object value) {
		return null;
	}

	@Override
	public String getBooleanFilterDisplayName(Object propertyId, boolean value) {
		return null;
	}

	@Override
	public Resource getBooleanFilterIcon(Object propertyId, boolean value) {
		return null;
	}

	@Override
	public boolean isTextFilterImmediate(Object propertyId) {
		return false;
	}

	@Override
	public int getTextChangeTimeout(Object propertyId) {
		return 0;
	}

	@Override
	public String getFromCaption() {
		return null;
	}

	@Override
	public String getToCaption() {
		return null;
	}

	@Override
	public String getSetCaption() {
		return null;
	}

	@Override
	public String getClearCaption() {
		return null;
	}
	
	public void deleteItem(Item item) throws Exception {
		if (!this.grid.removeItem(item)) {
			throw new Exception("Could not delete item" + item.toString());
		}
	}
}
