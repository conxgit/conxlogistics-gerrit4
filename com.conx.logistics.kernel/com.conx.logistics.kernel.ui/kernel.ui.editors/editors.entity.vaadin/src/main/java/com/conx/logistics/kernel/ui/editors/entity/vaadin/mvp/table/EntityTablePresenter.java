package com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.table;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Set;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.conx.logistics.kernel.datasource.domain.DataSource;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.MasterDetailComponent;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.AbstractEntityEditorEventBus;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.ConfigurableBasePresenter;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.MultiLevelEntityEditorPresenter;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.table.view.EntityTableView;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.table.view.IEntityTableView;
import com.conx.logistics.kernel.ui.factory.services.IEntityEditorFactory;
import com.conx.logistics.kernel.ui.filteredtable.FilterGenerator;
import com.conx.logistics.kernel.ui.filteredtable.FilterTableExcelExport;
import com.conx.logistics.kernel.ui.service.contribution.IMainApplication;
import com.conx.logistics.mdm.domain.BaseEntity;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerItem;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;

@Presenter(view = EntityTableView.class)
public class EntityTablePresenter extends ConfigurableBasePresenter<IEntityTableView, EntityTableEventBus>
		implements Property.ValueChangeListener {
	private static final long serialVersionUID = 1L;

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private boolean initialized = false;
	
	private MultiLevelEntityEditorPresenter parentPresenter;

	private EntityManager kernelSystemEntityManager;

	private EntityManager em;

	private DataSource dataSource;

	private Class javaEntityClass;

	private JPAContainer entityContainer;

	private AbstractEntityEditorEventBus entityEditorEventListener;

	public EntityTablePresenter() {
	}

	public MultiLevelEntityEditorPresenter getParentPresenter() {
		return parentPresenter;
	}

	public void setParentPresenter(MultiLevelEntityEditorPresenter parentPresenter) {
		this.parentPresenter = parentPresenter;
	}
	
	@Override
	public void bind() {
		//-- Instanciate View and Table
		this.getView().init();
		
		getView().getTable().setContainerDataSource(this.entityContainer);	
		//getView().getTable().setFilterGenerator(new DemoFilterGenerator());
		
		String[] visibleFieldNames = this.dataSource.getVisibleFieldNames().toArray(new String[0]);
		getView().getTable().setVisibleColumns(visibleFieldNames);
		getView().getTable().addListener(new ItemClickListener() {
			private static final long serialVersionUID = 7230326485331772539L;

			public void itemClick(ItemClickEvent event) {
				JPAContainerItem item = (JPAContainerItem)event.getItem();
				BaseEntity entity = (BaseEntity)item.getEntity();
				entity.toString();
				EntityTablePresenter.this.entityEditorEventListener.entityItemEdit(item);
			}
		});
		
		
		
		//-- Done
		this.setInitialized(true);		
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		
	}
	
	public void onEntityItemAdded(EntityItem item) {
		//this.entityContainer.refresh();
	}	
	
	public void onPrintClicked() {
		FilterTableExcelExport excelExport = new FilterTableExcelExport(getView().getTable());
        excelExport.excludeCollapsedColumns();
        excelExport.setReportTitle("Demo Report");
        excelExport.export();		
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	@Override
	public void configure() {
		try {
			MasterDetailComponent masterDetailComponent = (MasterDetailComponent)getConfig().get(IEntityEditorFactory.FACTORY_PARAM_MVP_COMPONENT_MODEL);
			MultiLevelEntityEditorPresenter multiLevelEntityEditorPresenter = (MultiLevelEntityEditorPresenter)getConfig().get(IEntityEditorFactory.FACTORY_PARAM_MVP_CURRENT_MLENTITY_EDITOR_PRESENTER);
			IMainApplication mainApplication = (IMainApplication) getConfig().get(IEntityEditorFactory.FACTORY_PARAM_MAIN_APP);
			
			this.dataSource = masterDetailComponent.getDataSource();
			this.javaEntityClass = this.dataSource.getEntityType().getJavaType();
			this.entityEditorEventListener = multiLevelEntityEditorPresenter.getEventBus();
			
			//-- Create datasource/container from md.dataSource
			this.entityContainer = (JPAContainer)mainApplication.createPersistenceContainer(this.javaEntityClass);
			Set<String> nestedFieldNames = this.dataSource.getNestedFieldNames();
			for (String npp : nestedFieldNames)
			{
				this.entityContainer.addNestedContainerProperty(npp);
			}			
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);
		}
	}
	
	public class DemoFilterGenerator implements FilterGenerator {

	    public Filter generateFilter(Object propertyId, Object value) {
	        if ("id".equals(propertyId)) {
	            /* Create an 'equals' filter for the ID field */
	            if (value != null && value instanceof String) {
	                try {
	                    return new Compare.Equal(propertyId,
	                            Integer.parseInt((String) value));
	                } catch (NumberFormatException ignored) {
	                    // If no integer was entered, just generate default filter
	                }
	            }
	        }
	        // For other properties, use the default filter
	        return null;
	    }

	}
}
