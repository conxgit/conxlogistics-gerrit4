package com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.search.grid;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.conx.logistics.kernel.ui.components.domain.table.ConXTable;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.table.EntityEditorGrid.IEditListener;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.table.EntityEditorGrid.ISelectListener;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.ConfigurableBasePresenter;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.MultiLevelEntityEditorEventBus;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.MultiLevelEntityEditorPresenter;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.search.grid.view.EntityGridView;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.search.grid.view.IEntityGridView;
import com.conx.logistics.kernel.ui.factory.services.IEntityEditorFactory;
import com.conx.logistics.kernel.ui.service.contribution.IMainApplication;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerItem;
import com.vaadin.data.Item;

@Presenter(view = EntityGridView.class)
public class EntityGridPresenter extends ConfigurableBasePresenter<IEntityGridView, EntityGridEventBus> implements IEditListener, ISelectListener {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private boolean initialized = false;
	private MultiLevelEntityEditorPresenter parentPresenter;
	private JPAContainer<?> entityContainer;
	private MultiLevelEntityEditorEventBus entityEditorEventListener;
	private IMainApplication mainApplication;
	private ConXTable tableComponent;
	private MultiLevelEntityEditorPresenter multiLevelEntityEditorPresenter;
	private Class<?> entityClass;

	public EntityGridPresenter() {
	}

	public MultiLevelEntityEditorPresenter getParentPresenter() {
		return parentPresenter;
	}

	public void setParentPresenter(MultiLevelEntityEditorPresenter parentPresenter) {
		this.parentPresenter = parentPresenter;
	}

	private void initialize() {
		String[] visibleFieldNames = this.tableComponent.getDataSource().getVisibleFieldNames().toArray(new String[0]);

		this.getView().init();
		this.getView().setContainerDataSource(this.entityContainer);
		this.getView().setVisibleColumns(visibleFieldNames);
		this.getView().addEditListener(this);
		this.getView().addSelectListener(this);

		// -- Done
		this.setInitialized(true);
	}

	@Override
	public void bind() {
		initialize();
	}

	@SuppressWarnings("rawtypes")
	public void onEntityItemAdded(EntityItem item) {
		// this.entityContainer.refresh();
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	@Override
	public void configure() {
		this.tableComponent = (ConXTable) getConfig().get(IEntityEditorFactory.FACTORY_PARAM_MVP_COMPONENT_MODEL);
		this.multiLevelEntityEditorPresenter = (MultiLevelEntityEditorPresenter) getConfig().get(IEntityEditorFactory.FACTORY_PARAM_MVP_CURRENT_MLENTITY_EDITOR_PRESENTER);
		this.mainApplication = (IMainApplication) getConfig().get(IEntityEditorFactory.FACTORY_PARAM_MAIN_APP);
		this.entityEditorEventListener = multiLevelEntityEditorPresenter.getEventBus();
		try {
			this.entityClass = this.tableComponent.getDataSource().getEntityType().getJavaType();
			this.entityContainer = (JPAContainer<?>) mainApplication.createPersistenceContainer(this.entityClass);
			Set<String> nestedFieldNames = this.tableComponent.getDataSource().getNestedFieldNames();
			for (String nestedFieldName : nestedFieldNames) {
				this.entityContainer.addNestedContainerProperty(nestedFieldName);
			}
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onSelect(Item item) {
		entityEditorEventListener.entityItemEdit((JPAContainerItem<?>) item);
	}

	@Override
	public void onEdit(Item item) {
		if (this.tableComponent != null && this.tableComponent.getRecordEditor() != null) {
			entityEditorEventListener.editItem(item, this.tableComponent.getRecordEditor());
		}
	}
	
	public void onDelete(Item item) throws Exception {
		this.getView().deleteItem(item);
	}
}