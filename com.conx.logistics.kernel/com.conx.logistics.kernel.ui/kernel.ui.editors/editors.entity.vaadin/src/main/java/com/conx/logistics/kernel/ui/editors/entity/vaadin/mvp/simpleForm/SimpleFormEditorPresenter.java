package com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.simpleForm;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.conx.logistics.kernel.ui.components.domain.form.ConXForm;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.ConfigurableBasePresenter;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.MultiLevelEntityEditorPresenter;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.simpleForm.view.ISimpleFormEditorView;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.simpleForm.view.SimpleFormEditorView;
import com.conx.logistics.kernel.ui.factory.services.IEntityEditorFactory;
import com.conx.logistics.kernel.ui.service.contribution.IMainApplication;
import com.conx.logistics.mdm.domain.BaseEntity;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.data.Item;

@Presenter(view = SimpleFormEditorView.class)
public class SimpleFormEditorPresenter extends ConfigurableBasePresenter<ISimpleFormEditorView, SimpleFormEditorEventBus> {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private boolean initialized = false;
	private MultiLevelEntityEditorPresenter parentPresenter;
	@SuppressWarnings({ "unused", "rawtypes" })
	private ConfigurableBasePresenter mlEntityPresenter;
	private ConXForm formComponent;
	@SuppressWarnings("unused")
	private IMainApplication mainApplication;
	private BaseEntity entity;
	private Set<String> visibleFieldNames;
	private Item itemDataSource;

	public SimpleFormEditorPresenter() {
	}

	public MultiLevelEntityEditorPresenter getParentPresenter() {
		return parentPresenter;
	}

	public void setParentPresenter(MultiLevelEntityEditorPresenter parentPresenter) {
		this.parentPresenter = parentPresenter;
	}
	
	private void initialize() {
		this.getView().init();
		this.getView().setForm(formComponent);
		if (this.itemDataSource != null) {
			onEntityItemEdit((EntityItem<?>) this.itemDataSource);
		}
		this.setInitialized(true);
	}

	@Override
	public void bind() {
	}
	
	@SuppressWarnings("rawtypes")
	public void onEntityItemEdit(EntityItem item) {
		this.setEntity((BaseEntity) item.getEntity());
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
		this.visibleFieldNames = formComponent.getDataSource().getVisibleFieldNames();
		this.getView().setItemDataSource(item, visibleFieldNames);
	}

	public void onEntityItemAdded(EntityItem<?> item) {
		// this.entityContainer.refresh();
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void configure() {
		this.mlEntityPresenter = (ConfigurableBasePresenter) getConfig().get(IEntityEditorFactory.FACTORY_PARAM_MVP_CURRENT_MLENTITY_EDITOR_PRESENTER);
		this.formComponent = (ConXForm) getConfig().get(IEntityEditorFactory.FACTORY_PARAM_MVP_COMPONENT_MODEL);
		this.mainApplication = (IMainApplication) getConfig().get(IEntityEditorFactory.FACTORY_PARAM_MAIN_APP);
		this.itemDataSource = (Item) getConfig().get(IEntityEditorFactory.FACTORY_PARAM_MVP_ITEM_DATASOURCE);
	}

	public BaseEntity getEntity() {
		return entity;
	}

	public void setEntity(BaseEntity entity) {
		this.entity = entity;
	}
}
