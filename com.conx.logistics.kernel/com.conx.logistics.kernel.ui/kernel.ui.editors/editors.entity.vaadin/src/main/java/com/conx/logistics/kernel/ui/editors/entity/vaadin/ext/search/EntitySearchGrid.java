package com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.search;

import com.conx.logistics.kernel.ui.components.domain.search.SearchGrid;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.EntityEditorToolStrip;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.EntityEditorToolStrip.EntityEditorToolStripButton;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.table.EntityGridFilterManager;
import com.conx.logistics.kernel.ui.filteredtable.FilterTable;
import com.conx.logistics.kernel.ui.forms.vaadin.impl.VaadinSearchForm;
import com.conx.logistics.kernel.ui.vaadin.common.ConXVerticalSplitPanel;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerItem;
import com.vaadin.data.Container;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.VerticalLayout;

public class EntitySearchGrid extends VerticalLayout {
	private static final long serialVersionUID = 5347124943163564312L;

	private VaadinSearchForm searchForm;
	private FilterTable grid;
	private ConXVerticalSplitPanel splitPanel;
	private EntityEditorToolStrip formToolStrip;
	private EntityEditorToolStrip gridToolStrip;
	private SearchGrid componentModel;

	public EntitySearchGrid(SearchGrid componentModel) {
		this.componentModel = componentModel;
		this.searchForm = new VaadinSearchForm();
		this.grid = new FilterTable();
		this.splitPanel = new ConXVerticalSplitPanel();
		this.formToolStrip = new EntityEditorToolStrip();
		this.gridToolStrip = new EntityEditorToolStrip();

		initialize();
	}

	private void initialize() {
		final EntityEditorToolStripButton applyFilterButton = this.formToolStrip.addToolStripButton(EntityEditorToolStrip.TOOLSTRIP_IMG_SEARCH_PNG);
		applyFilterButton.addListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("rawtypes")
			@Override
			public void buttonClick(ClickEvent event) {
				((JPAContainer) EntitySearchGrid.this.grid.getContainerDataSource()).removeAllContainerFilters();
				EntitySearchGrid.this.searchForm.buildQuery();
				((JPAContainer) EntitySearchGrid.this.grid.getContainerDataSource()).applyFilters();
			}
		});

		final EntityEditorToolStripButton resetFilterButton = this.formToolStrip.addToolStripButton(EntityEditorToolStrip.TOOLSTRIP_IMG_RESET_PNG);
		resetFilterButton.addListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				EntitySearchGrid.this.searchForm.resetForm();
			}
		});

		final EntityEditorToolStripButton clearFilterButton = this.formToolStrip.addToolStripButton(EntityEditorToolStrip.TOOLSTRIP_IMG_CLEAR_PNG);
		clearFilterButton.addListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("rawtypes")
			@Override
			public void buttonClick(ClickEvent event) {
				EntitySearchGrid.this.searchForm.clearForm();
				((JPAContainer) EntitySearchGrid.this.grid.getContainerDataSource()).removeAllContainerFilters();
			}
		});

		final ThemeResource filterIcon = new ThemeResource(EntityEditorToolStrip.TOOLSTRIP_IMG_FILTER_PNG);
		final ThemeResource hideFilterIcon = new ThemeResource(EntityEditorToolStrip.TOOLSTRIP_IMG_HIDE_FILTER_PNG);
		final EntityEditorToolStripButton showFilterButton = this.gridToolStrip.addToolStripButton(EntityEditorToolStrip.TOOLSTRIP_IMG_FILTER_PNG);
		showFilterButton.addListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (EntitySearchGrid.this.splitPanel.getSplitPosition() == 0) {
					EntitySearchGrid.this.splitPanel.setSplitPosition(50);
					EntitySearchGrid.this.splitPanel.setLocked(false);
					showFilterButton.setIcon(hideFilterIcon);
				} else {
					EntitySearchGrid.this.splitPanel.setSplitPosition(0);
					EntitySearchGrid.this.splitPanel.setLocked(true);
					showFilterButton.setIcon(filterIcon);
				}
			}
		});

		this.searchForm.setTitle(componentModel.getFormTitle());

		EntityGridFilterManager gridManager = new EntityGridFilterManager();

		this.grid.setSizeFull();
		this.grid.setMultiSelect(false);
		this.grid.setSelectable(true);
		this.grid.setNullSelectionAllowed(false);
		this.grid.setFilterDecorator(gridManager);
		this.grid.setFilterGenerator(gridManager);
		this.grid.setFiltersVisible(true);
		this.grid.setImmediate(true);
		this.grid.addListener(new ItemClickListener() {
			private static final long serialVersionUID = -4650994592971165778L;

			@Override
			public void itemClick(ItemClickEvent event) {
				if (event.getButton() == ItemClickEvent.BUTTON_LEFT) {
					EntitySearchGrid.this.searchForm.setItemDataSource(EntitySearchGrid.this.grid.getItem(event.getItemId()));
				}
			}
		});

		this.searchForm.setSizeFull();

		VerticalLayout formWrapper = new VerticalLayout();
		formWrapper.setSizeFull();
		formWrapper.addComponent(this.formToolStrip);
		formWrapper.addComponent(this.searchForm);
		formWrapper.setExpandRatio(this.searchForm, 1.0f);

		VerticalLayout gridWrapper = new VerticalLayout();
		gridWrapper.setSizeFull();
		gridWrapper.setStyleName("conx-entity-grid");
		gridWrapper.addComponent(this.gridToolStrip);
		gridWrapper.addComponent(this.grid);
		gridWrapper.setExpandRatio(this.grid, 1.0f);

		this.splitPanel = new ConXVerticalSplitPanel();
		this.splitPanel.setSizeFull();
		this.splitPanel.setImmediate(true);
		this.splitPanel.setSplitPosition(0);
		this.splitPanel.setStyleName("conx-entity-editor");
		EntitySearchGrid.this.splitPanel.setLocked(true);
		this.splitPanel.setFirstComponent(formWrapper);
		this.splitPanel.setSecondComponent(gridWrapper);

		setWidth("100%");
		// setHeight("400px");
		setHeight("100%");
		addComponent(this.splitPanel);
		setExpandRatio(this.splitPanel, 1.0f);
	}

	public void setContainerDataSource(Container container) {
		this.grid.setContainerDataSource(container);
		this.searchForm.setContainer(container);
		this.grid.setVisibleColumns(this.componentModel.getVisibleColumnIds());
	}

	public Object getSelectedEntity() {
		if (this.grid.getValue() != null) {
			return ((JPAContainerItem<?>) this.grid.getItem(this.grid.getValue())).getEntity();
		}
		return null;
	}
}
