package com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.lineeditor.grid;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.conx.logistics.common.utils.StringUtil;
import com.conx.logistics.kernel.ui.components.domain.table.ConXTable;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.table.EntityEditorGrid.IEditListener;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.table.EntityEditorGrid.ISelectListener;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.ConfigurableBasePresenter;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.MultiLevelEntityEditorEventBus;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.MultiLevelEntityEditorPresenter;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.lineeditor.grid.view.EntityLineGridView;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.lineeditor.grid.view.IEntityLineGridView;
import com.conx.logistics.kernel.ui.factory.services.IEntityEditorFactory;
import com.conx.logistics.kernel.ui.service.contribution.IMainApplication;
import com.conx.logistics.mdm.domain.BaseEntity;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerItem;
import com.vaadin.addon.jpacontainer.util.DefaultQueryModifierDelegate;
import com.vaadin.data.Item;

@Presenter(view = EntityLineGridView.class)
public class EntityLineGridPresenter extends ConfigurableBasePresenter<IEntityLineGridView, EntityLineGridEventBus> implements IEditListener, ISelectListener {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private boolean initialized = false;
	private MultiLevelEntityEditorPresenter parentPresenter;
	private JPAContainer<?> entityContainer;
	private MultiLevelEntityEditorEventBus entityEditorEventListener;
	private IMainApplication mainApplication;
	private ConXTable tableComponent;
	private MultiLevelEntityEditorPresenter multiLevelEntityEditorPresenter;
	private Class<?> entityClass;
	private EntityItem parentEntityItem;

	public EntityLineGridPresenter() {
	}

	public MultiLevelEntityEditorPresenter getParentPresenter() {
		return parentPresenter;
	}

	public void setParentPresenter(MultiLevelEntityEditorPresenter parentPresenter) {
		this.parentPresenter = parentPresenter;
	}


	private void initialize() {
		this.getView().init();
		
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
		//initialize();
	}

	@SuppressWarnings("rawtypes")
	public void onEntityItemAdded(EntityItem item) {		
	}
	
	@SuppressWarnings("rawtypes")
	public void onEntityItemEdit(EntityItem item) {
		this.parentEntityItem = item;
		if (!isInitialized()) {
			try {
				initialize();
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				String stacktrace = sw.toString();
				logger.error(stacktrace);
			}
		}
		updateQueryFilter();
	}
	
	private void updateQueryFilter() {
		this.entityContainer.getEntityProvider().setQueryModifierDelegate(new DefaultQueryModifierDelegate() {
			@Override
			public void filtersWillBeAdded(CriteriaBuilder criteriaBuilder, CriteriaQuery<?> query, List<Predicate> predicates) {
				Root<?> lineEntityRoot = query.getRoots().iterator().next();

				String fkPath = EntityLineGridPresenter.this.tableComponent.getDataSource().getForeignKeyPath();
				Path fkParentIdPath = null;
				String[] fkPathTokens = StringUtil.split(fkPath, ".");
				for (String token : fkPathTokens)
				{
					if (fkParentIdPath == null)
						fkParentIdPath = lineEntityRoot.get(token);
					else
						fkParentIdPath = fkParentIdPath.get(token);
				}
				predicates.add(criteriaBuilder.equal(fkParentIdPath, ((BaseEntity)EntityLineGridPresenter.this.parentEntityItem.getEntity()).getId()));
			}
		});
		this.entityContainer.applyFilters();
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