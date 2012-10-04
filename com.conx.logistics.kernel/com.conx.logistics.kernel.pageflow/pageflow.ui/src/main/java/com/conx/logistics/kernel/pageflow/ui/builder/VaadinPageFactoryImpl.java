package com.conx.logistics.kernel.pageflow.ui.builder;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.EventBusManager;
import org.vaadin.mvp.presenter.IPresenter;
import org.vaadin.mvp.presenter.PresenterFactory;

import com.conx.logistics.kernel.pageflow.services.ICustomDrivenPageFlowPage;
import com.conx.logistics.kernel.pageflow.services.IModelDrivenPageFlowPage;
import com.conx.logistics.kernel.pageflow.services.IPageComponent;
import com.conx.logistics.kernel.pageflow.services.IPageFlowPage;
import com.conx.logistics.kernel.pageflow.ui.ext.form.VaadinCollapsibleConfirmActualsForm;
import com.conx.logistics.kernel.pageflow.ui.ext.form.VaadinConfirmActualsForm;
import com.conx.logistics.kernel.pageflow.ui.mvp.IPageDataHandler;
import com.conx.logistics.kernel.pageflow.ui.mvp.PagePresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.attachment.AttachmentEditorEventBus;
import com.conx.logistics.kernel.pageflow.ui.mvp.attachment.AttachmentEditorPresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.EntityLineEditorEventBus;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.EntityLineEditorPresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.EntityLineEditorSectionEventBus;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.EntityLineEditorSectionPresenter;
import com.conx.logistics.kernel.ui.components.domain.AbstractConXComponent;
import com.conx.logistics.kernel.ui.components.domain.attachment.AttachmentEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.form.CollapsibleConfirmActualsForm;
import com.conx.logistics.kernel.ui.components.domain.form.ConfirmActualsForm;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.LineEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.LineEditorContainerComponent;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.MasterDetailComponent;
import com.conx.logistics.kernel.ui.components.domain.page.TaskPage;
import com.conx.logistics.kernel.ui.components.domain.search.SearchGrid;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.search.EntitySearchGrid;
import com.conx.logistics.kernel.ui.factory.services.IEntityEditorFactory;
import com.conx.logistics.kernel.ui.vaadin.common.ConXVerticalSplitPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class VaadinPageFactoryImpl {

	private PresenterFactory presenterFactory;
	private Map<String, Object> config;

	public VaadinPageFactoryImpl(Map<String, Object> config) {
		this.config = config;
		this.presenterFactory = new PresenterFactory(new EventBusManager(), Locale.getDefault());
	}

	private void setPagePresenterData(PagePresenter pagePresenter, Component component) {
		pagePresenter.setPageContent(component);
		pagePresenter.setDataHandler(new IPageDataHandler() {
			
			@Override
			public void setParameterData(PagePresenter source, Map<String, Object> params) {
				try {
					VaadinPageDataBuilder.applyParamData(source, source.getPageContent(), params);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public Object getResultData(PagePresenter source) {
				return VaadinPageDataBuilder.buildResultDataMap(VaadinPageDataBuilder.buildResultData(source.getPageContent()), source.getPage().getResultKeyMap());
			}
		});
	}

	public Component createComponent(AbstractConXComponent componentModel) {
		if (correspondsToPresenter(componentModel)) {
			IPresenter<?, ? extends EventBus> presenter = createPresenter(componentModel, new HashMap<String, Object>(config));
			return (Component) presenter.getView();
		}
		
		if (componentModel instanceof MasterDetailComponent) {
			ConXVerticalSplitPanel splitPanel = new ConXVerticalSplitPanel();
			splitPanel.setSizeFull();
			splitPanel.setSplitPosition(50);

			if (((MasterDetailComponent) componentModel).getMasterComponent() != null) {
				splitPanel.setFirstComponent(createComponent(((MasterDetailComponent) componentModel).getMasterComponent()));
			}
			if (((MasterDetailComponent) componentModel).getLineEditorPanel() != null) {
				splitPanel.setSecondComponent(createComponent(((MasterDetailComponent) componentModel).getLineEditorPanel()));
			}
			return splitPanel;
		} else if (componentModel instanceof ConfirmActualsForm) {
			return new VaadinConfirmActualsForm((ConfirmActualsForm) componentModel);
		} else if (componentModel instanceof SearchGrid) {
			EntitySearchGrid component = new EntitySearchGrid((SearchGrid) componentModel);
			component.setStatusEnabled(true);
			return component;
		} else if (componentModel instanceof CollapsibleConfirmActualsForm) {
			return new VaadinCollapsibleConfirmActualsForm((CollapsibleConfirmActualsForm) componentModel);
		}
		
		return new VerticalLayout();
	}

	public IPresenter<?, ? extends EventBus> createPresenter(AbstractConXComponent componentModel, Map<String, Object> params) {
		params.putAll(this.config);
		params.put(IEntityEditorFactory.COMPONENT_MODEL, componentModel);
		params.put(IEntityEditorFactory.VAADIN_COMPONENT_FACTORY, this);
		
		IPresenter<?, ? extends EventBus> presenter = null;
		if (componentModel instanceof AttachmentEditorComponent) {
			presenter = presenterFactory.createPresenter(AttachmentEditorPresenter.class);
			((AttachmentEditorEventBus) presenter.getEventBus()).configure(params);
		} else if (componentModel instanceof LineEditorContainerComponent) {
			presenter = presenterFactory.createPresenter(EntityLineEditorPresenter.class);
			((EntityLineEditorEventBus) presenter.getEventBus()).configure(params);
		} else if (componentModel instanceof LineEditorComponent) {
			presenter = presenterFactory.createPresenter(EntityLineEditorSectionPresenter.class);
			((EntityLineEditorSectionEventBus) presenter.getEventBus()).configure(params);
		}
		
		return presenter;
	}

	public IPageComponent createPage(final IPageFlowPage page, Map<String, Object> initParams) {
		if (isInstanceOf(page, IModelDrivenPageFlowPage.class)) {
//			this.presenterFactory.setEventManager(new EventBusManager());
			PagePresenter pagePresenter = (PagePresenter) presenterFactory.createPresenter(PagePresenter.class);
			pagePresenter.setPage(page);
			
			AbstractConXComponent componentModel = ((TaskPage) ((IModelDrivenPageFlowPage) page).getComponentModel()).getContent();
			Component component = createComponent(componentModel);
			setPagePresenterData(pagePresenter, component);
			pagePresenter.init(initParams);
			
			return pagePresenter;
		} else if (isInstanceOf(page, ICustomDrivenPageFlowPage.class)) {
			IPageComponent pageComponent = ((ICustomDrivenPageFlowPage) page).getPageComponent();
			return pageComponent;
		} else {
			return null;
		}
	}

	public boolean isInstanceOf(IPageFlowPage page, Class<?> type) {
		return page.getType().isAssignableFrom(type);
	}
	
	public boolean correspondsToPresenter(AbstractConXComponent componentModel) {
		if (componentModel instanceof AttachmentEditorComponent || 
				componentModel instanceof LineEditorContainerComponent || 
				componentModel instanceof LineEditorComponent) {
			return true;
		}
		return false;
	}
	
	public PresenterFactory getPresenterFactory() {
		return this.presenterFactory;
	}
}
