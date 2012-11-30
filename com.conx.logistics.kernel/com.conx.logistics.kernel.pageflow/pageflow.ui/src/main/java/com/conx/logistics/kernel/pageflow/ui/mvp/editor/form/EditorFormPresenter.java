package com.conx.logistics.kernel.pageflow.ui.mvp.editor.form;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.eventbus.EventBusManager;
import org.vaadin.mvp.presenter.BasePresenter;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.conx.logistics.kernel.pageflow.services.IPageComponent;
import com.conx.logistics.kernel.pageflow.ui.builder.VaadinPageDataBuilder;
import com.conx.logistics.kernel.pageflow.ui.builder.VaadinPageFactoryImpl;
import com.conx.logistics.kernel.pageflow.ui.ext.mvp.IConfigurablePresenter;
import com.conx.logistics.kernel.pageflow.ui.ext.mvp.lineeditor.section.IEditorContentPresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.editor.form.view.EditorFormView;
import com.conx.logistics.kernel.pageflow.ui.mvp.editor.form.view.IEditorFormView;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.form.header.EntityLineEditorFormHeaderEventBus;
import com.conx.logistics.kernel.ui.components.domain.form.ConXForm;
import com.conx.logistics.kernel.ui.factory.services.IEntityEditorFactory;
import com.conx.logistics.kernel.ui.factory.services.data.IDAOProvider;
import com.conx.logistics.kernel.ui.forms.vaadin.impl.VaadinForm;
import com.conx.logistics.kernel.ui.forms.vaadin.listeners.IFormChangeListener;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

@Presenter(view = EditorFormView.class)
public class EditorFormPresenter extends BasePresenter<IEditorFormView, EditorFormEventBus> implements IEditorContentPresenter,
		IConfigurablePresenter, IFormChangeListener {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private ConXForm formComponent;
	private VaadinPageFactoryImpl factory;
	private EventBusManager sectionEventBusManager;
	private IDAOProvider daoProvider;
	private Map<String, Object> config;

	@Override
	public void onSetItemDataSource(Item item, Container... container) throws Exception {
		if (container.length == 1) {
			VaadinPageDataBuilder.applyItemDataSource(false, this.getView().getForm(), container[0], item, this.factory.getPresenterFactory(),
					this.config);
		} else {
			throw new Exception("Could not set item datasource. Expected one container, but got " + container.length);
		}
	}

	@Override
	public void onConfigure(Map<String, Object> params) throws Exception {
		this.config = params;
		this.formComponent = (ConXForm) params.get(IEntityEditorFactory.FACTORY_PARAM_MVP_COMPONENT_MODEL);
		this.factory = (VaadinPageFactoryImpl) params.get(IEntityEditorFactory.VAADIN_COMPONENT_FACTORY);
		this.daoProvider = (IDAOProvider) params.get(IPageComponent.DAO_PROVIDER);

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
			if (this.getView().getForm().getItemDataSource() instanceof BeanItem<?>) {
				Object bean = ((BeanItem<?>) this.getView().getForm().getItemDataSource()).getBean();
				VaadinPageDataBuilder.saveInstance(bean, this.daoProvider);
			}
			
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
		this.sectionEventBusManager.register(EditorFormEventBus.class, getEventBus());
	}
}
