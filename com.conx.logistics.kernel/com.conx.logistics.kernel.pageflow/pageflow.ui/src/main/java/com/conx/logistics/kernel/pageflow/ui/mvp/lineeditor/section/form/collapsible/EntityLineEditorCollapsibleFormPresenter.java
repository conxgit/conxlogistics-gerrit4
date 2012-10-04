package com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.form.collapsible;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.presenter.BasePresenter;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.EntityLineEditorSectionEventBus;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.EntityLineEditorSectionPresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.form.collapsible.view.EntityLineEditorCollapsibleFormView;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.form.collapsible.view.IEntityLineEditorCollapsibleFormView;
import com.conx.logistics.kernel.ui.components.domain.form.ConXCollapseableSectionForm;
import com.conx.logistics.kernel.ui.factory.services.IEntityEditorFactory;
import com.conx.logistics.kernel.ui.forms.vaadin.listeners.IFormChangeListener;
import com.conx.logistics.mdm.domain.BaseEntity;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.data.Item;

@Presenter(view = EntityLineEditorCollapsibleFormView.class)
public class EntityLineEditorCollapsibleFormPresenter extends BasePresenter<IEntityLineEditorCollapsibleFormView, EntityLineEditorCollapsibleFormEventBus> implements IFormChangeListener {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private boolean initialized = false;
	private EntityLineEditorSectionPresenter lineEditorSectionPresenter;
	private ConXCollapseableSectionForm formComponent;
	private EntityItem<?> itemDataSource;
	private BaseEntity entity;
	private EntityLineEditorSectionEventBus lineEditorSectionEventBus;

	private void initialize() {
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

	public void onConfigure(Map<String, Object> params) {
		this.lineEditorSectionPresenter = (EntityLineEditorSectionPresenter) params.get(IEntityEditorFactory.FACTORY_PARAM_MVP_LINE_EDITOR_SECTION_PRESENTER);
		this.formComponent = (ConXCollapseableSectionForm) params.get(IEntityEditorFactory.FACTORY_PARAM_MVP_COMPONENT_MODEL);
		this.lineEditorSectionEventBus = lineEditorSectionPresenter.getEventBus();
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
		lineEditorSectionEventBus.formChanged();
	}

	public void onFormValidated() {
		lineEditorSectionEventBus.formValidated();
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
