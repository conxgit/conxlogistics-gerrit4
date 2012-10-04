package com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.grid;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.presenter.BasePresenter;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.conx.logistics.common.utils.StringUtil;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.grid.view.EntityLineEditorGridView;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.grid.view.IEntityLineEditorGridView;
import com.conx.logistics.kernel.ui.components.domain.table.ConXTable;
import com.conx.logistics.kernel.ui.factory.services.IEntityEditorFactory;
import com.conx.logistics.kernel.ui.service.contribution.IMainApplication;
import com.conx.logistics.mdm.domain.BaseEntity;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.util.DefaultQueryModifierDelegate;
import com.vaadin.data.Item;

@Presenter(view = EntityLineEditorGridView.class)
public class EntityLineEditorGridPresenter extends BasePresenter<IEntityLineEditorGridView, EntityLineEditorGridEventBus> {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private boolean initialized = false;
	private JPAContainer<?> entityContainer;
	private IMainApplication mainApplication;
	private ConXTable tableComponent;
	private Class<?> entityClass;
	private EntityItem<?> parentEntityItem;
	
	private void initialize() {
		String[] visibleFieldNames = this.tableComponent.getDataSource().getVisibleFieldNames().toArray(new String[0]);

		this.getView().setContainerDataSource(this.entityContainer);
		this.getView().setVisibleColumns(visibleFieldNames);

		// -- Done
		this.setInitialized(true);
	}

	@Override
	public void bind() {
		// initialize();
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
		this.entityContainer.removeAllContainerFilters();
		this.entityContainer.getEntityProvider().setQueryModifierDelegate(new DefaultQueryModifierDelegate() {
			@Override
			public void filtersWillBeAdded(CriteriaBuilder criteriaBuilder, CriteriaQuery<?> query, List<Predicate> predicates) {
				Root<?> lineEntityRoot = query.getRoots().iterator().next();

				String fkPath = EntityLineEditorGridPresenter.this.tableComponent.getDataSource().getForeignKeyPath();
				Path<?> fkParentIdPath = null;
				String[] fkPathTokens = StringUtil.split(fkPath, ".");
				for (String token : fkPathTokens) {
					if (fkParentIdPath == null)
						fkParentIdPath = lineEntityRoot.get(token);
					else
						fkParentIdPath = fkParentIdPath.get(token);
				}
				predicates.add(criteriaBuilder.equal(fkParentIdPath, ((BaseEntity) EntityLineEditorGridPresenter.this.parentEntityItem.getEntity()).getId()));
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

	public void onConfigure(Map<String, Object> params) {
		this.tableComponent = (ConXTable) params.get(IEntityEditorFactory.FACTORY_PARAM_MVP_COMPONENT_MODEL);
		this.mainApplication = (IMainApplication) params.get(IEntityEditorFactory.FACTORY_PARAM_MAIN_APP);
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

	public void onDelete(Item item) throws Exception {
		this.getView().deleteItem(item);
	}
}
