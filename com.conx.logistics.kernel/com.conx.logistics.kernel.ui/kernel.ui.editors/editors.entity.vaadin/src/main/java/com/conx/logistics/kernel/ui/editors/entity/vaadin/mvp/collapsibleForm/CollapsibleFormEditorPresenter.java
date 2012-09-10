package com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.collapsibleForm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.conx.logistics.kernel.ui.components.domain.form.ConXCollapseableSectionForm;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.ConfigurableBasePresenter;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.MultiLevelEntityEditorEventBus;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.MultiLevelEntityEditorPresenter;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.collapsibleForm.view.CollapsibleFormEditorView;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.collapsibleForm.view.ICollapsibleFormEditorView;
import com.conx.logistics.kernel.ui.factory.services.IEntityEditorFactory;
import com.conx.logistics.kernel.ui.forms.vaadin.impl.VaadinCollapsibleSectionForm.IFormChangeListener;
import com.conx.logistics.kernel.ui.service.contribution.IMainApplication;
import com.conx.logistics.mdm.domain.BaseEntity;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.data.Item;

@Presenter(view = CollapsibleFormEditorView.class)
public class CollapsibleFormEditorPresenter extends ConfigurableBasePresenter<ICollapsibleFormEditorView, CollapsibleFormEditorEventBus> implements IFormChangeListener {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private boolean initialized = false;
	private MultiLevelEntityEditorPresenter parentPresenter;
	private MultiLevelEntityEditorPresenter multiLevelEntityEditorPresenter;
	private ConXCollapseableSectionForm formComponent;
	private EntityItem<?> itemDataSource;
	@SuppressWarnings("unused")
	private IMainApplication mainApplication;
	private BaseEntity entity;
	private MultiLevelEntityEditorEventBus entityEditorEventListener;

	public MultiLevelEntityEditorPresenter getParentPresenter() {
		return parentPresenter;
	}

	public void setParentPresenter(MultiLevelEntityEditorPresenter parentPresenter) {
		this.parentPresenter = parentPresenter;
	}

	private void initialize() {
		this.getView().init();
		this.getView().setForm(formComponent);
		this.getView().addFormChangeListener(this);
		this.setInitialized(true);
	}

	@Override
	public void bind() {
	}

	@SuppressWarnings("rawtypes")
	public void onEntityItemEdit(EntityItem item) {
		onSetItemDataSource(item);
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

	@Override
	public void configure() {
		this.multiLevelEntityEditorPresenter = (MultiLevelEntityEditorPresenter) getConfig().get(IEntityEditorFactory.FACTORY_PARAM_MVP_CURRENT_MLENTITY_EDITOR_PRESENTER);
		this.formComponent = (ConXCollapseableSectionForm) getConfig().get(IEntityEditorFactory.FACTORY_PARAM_MVP_COMPONENT_MODEL);
		this.mainApplication = (IMainApplication) getConfig().get(IEntityEditorFactory.FACTORY_PARAM_MAIN_APP);
		this.entityEditorEventListener = multiLevelEntityEditorPresenter.getEventBus();
	}

	public BaseEntity getEntity() {
		return entity;
	}

	public void setEntity(BaseEntity entity) {
		this.entity = entity;
	}

	public void onSetItemDataSource(Item item) {
		if (item != null) {
			this.itemDataSource = (EntityItem<?>) item;
			this.setEntity((BaseEntity) this.itemDataSource.getEntity());
			if (!isInitialized()) {
				try {
					initialize();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			this.getView().setItemDataSource(item);
			this.getView().setFormTitle(formComponent.getCaption() + " (" + this.itemDataSource.getItemProperty("code").getValue().toString() + ")");
		}
	}

	@Override
	public void onFormChanged() {
		entityEditorEventListener.formChanged();
	}

	public void onFormValidated() {
		entityEditorEventListener.formValidated();
	}

	public void onSaveForm() {
		this.getView().saveForm();
	}

	public void onValidateForm() {
		if (this.getView().validateForm()) {
			this.onFormValidated();
		}
	}

	public void onResetForm() {
		this.getView().resetForm();
	}

	public void onResizeForm(int newHeight) {
		this.getView().resizeForm(newHeight);
	}
}
