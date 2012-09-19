package com.conx.logistics.kernel.workspace.ui.dashboard.view;

import org.vaadin.sasha.portallayout.PortalLayout;

import com.conx.logistics.kernel.ui.filteredtable.FilterTable;
import com.conx.logistics.kernel.workspace.ui.ext.task.grid.TaskGrid;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class DashboardView extends VerticalLayout implements IDashboardView {
	
	private GridLayout mainLayout = new GridLayout(1,2);
	private TaskGrid myTasksTable;
	private Table myAlertsTable;
	
    public class DashboardPortal extends PortalLayout {
        
        public DashboardPortal() {
            //setWidth("100%");
            //setHeight("800px");
            //addCloseListener(DashboardView.this);
            //addCollapseListener(DashboardView.this);
            setMargin(true);
        }
    }	
	
    private final PortalLayout myTasksPortal = new DashboardPortal()  {
        public void addComponent(Component c, int position) {
            super.addComponent(c, position);
            clearPortletStyleNames(c);
            addPortletStyleName(c, "green");
            setClosable(c, false);
        };
    };
    
    private final PortalLayout myAlertsPortal = new DashboardPortal()  {
        public void addComponent(Component c, int position) {
            super.addComponent(c, position);
            clearPortletStyleNames(c);
            addPortletStyleName(c, "yellow");
            setClosable(c, false);
        };
    };	
	
	public DashboardView() {
		mainLayout.setSizeFull();
		addComponent(mainLayout);
	}


	@Override
	public void init() {
		buildPortals();
		buildTaskTable();
		buildAlertsTable();
	}

	private void buildPortals() {
		mainLayout.addComponent(myTasksPortal, 0, 0);
		mainLayout.addComponent(myAlertsPortal, 0, 1);
	}


	private void buildTaskTable() {
		this.myTasksTable = new TaskGrid();
		
		this.myTasksPortal.addComponent(this.myTasksTable);
		this.myTasksTable.setCaption("My Tasks");
		this.myTasksPortal.setIcon(new ThemeResource("chart.png"));
        
        final HorizontalLayout header =  new HorizontalLayout();
        final TextField filterField = new TextField();
        final NativeSelect filterType = new NativeSelect();
        final Label caption = new Label("Filter: ");
        filterField.setImmediate(true);
        header.setSizeUndefined();
        header.addComponent(caption);
        header.addComponent(filterField);
        header.addComponent(filterType);
        header.setSpacing(true);
        header.setComponentAlignment(caption, Alignment.MIDDLE_LEFT);
        header.setComponentAlignment(filterType, Alignment.MIDDLE_LEFT);
        //myTasksPortal.setHeaderComponent(this.myTasksTable, header);
	}
	
	private void buildAlertsTable() {
		this.myAlertsTable = new Table();
		this.myAlertsPortal.addComponent(this.myAlertsTable);
		this.myAlertsTable.setCaption("My Alerts");
		this.myAlertsPortal.setIcon(new ThemeResource("chart.png"));
        
        final HorizontalLayout header =  new HorizontalLayout();
        final TextField filterField = new TextField();
        final NativeSelect filterType = new NativeSelect();
        final Label caption = new Label("Filter: ");
        filterField.setImmediate(true);
        header.setSizeUndefined();
        header.addComponent(caption);
        header.addComponent(filterField);
        header.addComponent(filterType);
        header.setSpacing(true);
        header.setComponentAlignment(caption, Alignment.MIDDLE_LEFT);
        header.setComponentAlignment(filterType, Alignment.MIDDLE_LEFT);
        //myAlertsPortal.setHeaderComponent(this.myAlertsTable, header);
	}


	public TaskGrid getMyTasksTable() {
		return myTasksTable;
	}	
}
