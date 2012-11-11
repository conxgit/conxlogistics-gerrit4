package com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.form;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.eventbus.EventBusManager;
import org.vaadin.mvp.presenter.BasePresenter;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.conx.logistics.kernel.pageflow.ui.builder.VaadinPageDataBuilder;
import com.conx.logistics.kernel.pageflow.ui.builder.VaadinPageFactoryImpl;
import com.conx.logistics.kernel.pageflow.ui.ext.mvp.IConfigurablePresenter;
import com.conx.logistics.kernel.pageflow.ui.ext.mvp.lineeditor.section.ILineEditorSectionContentPresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.form.header.EntityLineEditorFormHeaderEventBus;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.form.view.EntityLineEditorFormView;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.form.view.IEntityLineEditorFormView;
import com.conx.logistics.kernel.ui.components.domain.form.ConXForm;
import com.conx.logistics.kernel.ui.factory.services.IEntityEditorFactory;
import com.conx.logistics.kernel.ui.forms.vaadin.impl.VaadinForm;
import com.conx.logistics.kernel.ui.forms.vaadin.listeners.IFormChangeListener;
import com.vaadin.data.Container;
import com.vaadin.data.Item;

@Presenter(view = EntityLineEditorFormView.class)
public class EntityLineEditorFormPresenter extends BasePresenter<IEntityLineEditorFormView, EntityLineEditorFormEventBus> implements ILineEditorSectionContentPresenter,
		IConfigurablePresenter, IFormChangeListener {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private ConXForm formComponent;
	private VaadinPageFactoryImpl factory;
	private EventBusManager sectionEventBusManager;
	private Map<String, Object> config;
	
	@Override
	public void onSetItemDataSource(Item item, Container... container) throws Exception {
		if (container.length == 1) {
			VaadinPageDataBuilder.applyItemDataSource(this.getView().getForm(), container[0], item, this.factory.getPresenterFactory(), this.config);
		} else {
			throw new Exception("Could not set item datasource. Expected one container, but got " + container.length);
		}
	}

	@Override
	public void onConfigure(Map<String, Object> params) throws Exception {
		this.config = params;
		this.formComponent = (ConXForm) params.get(IEntityEditorFactory.FACTORY_PARAM_MVP_COMPONENT_MODEL);
		this.factory = (VaadinPageFactoryImpl) params.get(IEntityEditorFactory.VAADIN_COMPONENT_FACTORY);
		
		if (this.sectionEventBusManager != null) {
			this.sectionEventBusManager.register(EntityLineEditorFormHeaderEventBus.class, this);
		}
		
		this.getView().setForm((VaadinForm) this.factory.createComponent(this.formComponent));
		this.getView().addListener(this);
	}

	@Override
	public void onFormChanged() {
		this.sectionEventBusManager.fireAnonymousEvent("enableValidate");
		this.sectionEventBusManager.fireAnonymousEvent("disableSave");
		this.sectionEventBusManager.fireAnonymousEvent("enableReset");
	}
	
	public void onValidate() throws Exception {
		if (this.getView().getForm().validateForm()) {
			this.sectionEventBusManager.fireAnonymousEvent("disableValidate");
			this.sectionEventBusManager.fireAnonymousEvent("enableSave");
			this.sectionEventBusManager.fireAnonymousEvent("enableReset");
		} else {
			this.sectionEventBusManager.fireAnonymousEvent("disableValidate");
			this.sectionEventBusManager.fireAnonymousEvent("disableSave");
			this.sectionEventBusManager.fireAnonymousEvent("enableReset");
		}
	}
	
	public void onSave() throws Exception {
		if (this.getView().getForm().saveForm()) {
			this.sectionEventBusManager.fireAnonymousEvent("disableValidate");
			this.sectionEventBusManager.fireAnonymousEvent("disableSave");
			this.sectionEventBusManager.fireAnonymousEvent("disableReset");
		} else {
			this.sectionEventBusManager.fireAnonymousEvent("disableValidate");
			this.sectionEventBusManager.fireAnonymousEvent("disableSave");
			this.sectionEventBusManager.fireAnonymousEvent("enableReset");
		}
	}
	
	public void onReset() throws Exception {
		this.getView().getForm().resetForm();
		this.sectionEventBusManager.fireAnonymousEvent("disableValidate");
		this.sectionEventBusManager.fireAnonymousEvent("disableSave");
		this.sectionEventBusManager.fireAnonymousEvent("disableReset");
	}

	@Override
	public void subscribe(EventBusManager eventBusManager) {
		this.sectionEventBusManager = eventBusManager;
		this.sectionEventBusManager.register(EntityLineEditorFormEventBus.class, getEventBus());
	}
}
