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
import com.conx.logistics.kernel.pageflow.ui.ext.form.VaadinCollapsiblePhysicalAttributeConfirmActualsForm;
import com.conx.logistics.kernel.pageflow.ui.ext.grid.VaadinMatchGrid;
import com.conx.logistics.kernel.pageflow.ui.ext.mvp.IConfigurablePresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.IPageDataHandler;
import com.conx.logistics.kernel.pageflow.ui.mvp.PagePresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.editor.form.EditorFormPresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.editor.multilevel.MultiLevelEditorPresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.EntityLineEditorPresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.EntityLineEditorSectionPresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.attachment.AttachmentEditorPresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.form.EntityLineEditorFormPresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.form.header.EntityLineEditorFormHeaderPresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.grid.EntityLineEditorGridPresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.grid.header.EntityLineEditorGridHeaderPresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.refnum.ReferenceNumberEditorPresenter;
import com.conx.logistics.kernel.ui.components.domain.AbstractConXComponent;
import com.conx.logistics.kernel.ui.components.domain.attachment.AttachmentEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.editor.MultiLevelEntityEditor;
import com.conx.logistics.kernel.ui.components.domain.form.CollapsibleConfirmActualsForm;
import com.conx.logistics.kernel.ui.components.domain.form.CollapsiblePhysicalAttributeConfirmActualsForm;
import com.conx.logistics.kernel.ui.components.domain.form.ConXCollapseableSectionForm;
import com.conx.logistics.kernel.ui.components.domain.form.ConXForm;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.LineEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.LineEditorContainerComponent;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.MasterDetailComponent;
import com.conx.logistics.kernel.ui.components.domain.page.TaskPage;
import com.conx.logistics.kernel.ui.components.domain.referencenumber.ReferenceNumberEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.search.SearchGrid;
import com.conx.logistics.kernel.ui.components.domain.table.ConXTable;
import com.conx.logistics.kernel.ui.components.domain.table.EntityMatchGrid;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.search.EntitySearchGrid;
import com.conx.logistics.kernel.ui.factory.services.IEntityEditorFactory;
import com.conx.logistics.kernel.ui.forms.vaadin.impl.VaadinCollapsibleSectionForm;
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

	public VaadinPageFactoryImpl(Map<String, Object> config, PresenterFactory presenterFactory) {
		this.config = config;
		this.presenterFactory = presenterFactory;
	}

	private void setPagePresenterData(PagePresenter pagePresenter, Component component) {
		pagePresenter.setPageContent(component);
		pagePresenter.setDataHandler(new IPageDataHandler() {

			@Override
			public void setParameterData(PagePresenter source, Map<String, Object> params) {
				try {
					VaadinPageDataBuilder.applyParamData(source.getConfig(), source.getPageContent(), params, VaadinPageFactoryImpl.this.presenterFactory);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public Object getResultData(PagePresenter source) {
				Map<String, Object> resultDataMap = new HashMap<String, Object>(VaadinPageDataBuilder.buildResultDataMap(source.getParameterData(),
						VaadinPageDataBuilder.buildResultData(source.getPageContent()), source.getPage().getResultKeyMap()));
				return resultDataMap;
			}
		});
	}
	
	public Component createComponent(AbstractConXComponent componentModel) {
		if (componentModel instanceof MasterDetailComponent) {
			ConXVerticalSplitPanel splitPanel = new ConXVerticalSplitPanel();
			splitPanel.setSizeFull();
			splitPanel.setSplitPosition(50);

			if (((MasterDetailComponent) componentModel).getMasterComponent() != null) {
				splitPanel.setFirstComponent(create(((MasterDetailComponent) componentModel).getMasterComponent()));
			}
			if (((MasterDetailComponent) componentModel).getLineEditorPanel() != null) {
				splitPanel.setSecondComponent(create(((MasterDetailComponent) componentModel).getLineEditorPanel()));
			}
			return splitPanel;
		} else if (componentModel instanceof SearchGrid) {
			EntitySearchGrid component = new EntitySearchGrid((SearchGrid) componentModel);
			component.setStatusEnabled(true);
			return component;
		} else if (componentModel instanceof CollapsibleConfirmActualsForm) {
			return new VaadinCollapsibleConfirmActualsForm((CollapsibleConfirmActualsForm) componentModel);
		} else if (componentModel instanceof ConXCollapseableSectionForm) {
			return new VaadinCollapsibleSectionForm((ConXCollapseableSectionForm) componentModel);
		} else if (componentModel instanceof EntityMatchGrid) {
			return new VaadinMatchGrid((EntityMatchGrid) componentModel);
		} else if (componentModel instanceof CollapsiblePhysicalAttributeConfirmActualsForm) {
			return new VaadinCollapsiblePhysicalAttributeConfirmActualsForm((CollapsiblePhysicalAttributeConfirmActualsForm) componentModel);
		}
		
		// FIXME this should return null
		return new VerticalLayout();
	}

	public Component create(AbstractConXComponent componentModel) {
		try {
			if (correspondsToPresenter(componentModel)) {
				IPresenter<?, ? extends EventBus> presenter = createPresenter(componentModel, new HashMap<String, Object>(config));
				return (Component) presenter.getView();
			} else {
				return createComponent(componentModel);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new VerticalLayout();
	}

	public IPresenter<?, ? extends EventBus> createPresenter(AbstractConXComponent componentModel, Map<String, Object> params) throws Exception {
		params.put(IEntityEditorFactory.COMPONENT_MODEL, componentModel);
		params.put(IEntityEditorFactory.VAADIN_COMPONENT_FACTORY, this);

		IPresenter<?, ? extends EventBus> presenter = null;
		if (componentModel instanceof LineEditorContainerComponent) {
			presenter = presenterFactory.createPresenter(EntityLineEditorPresenter.class);
		} else if (componentModel instanceof LineEditorComponent) {
			presenter = presenterFactory.createPresenter(EntityLineEditorSectionPresenter.class);
		} else if (componentModel instanceof MultiLevelEntityEditor) {
			presenter = presenterFactory.createPresenter(MultiLevelEditorPresenter.class);
		} else if (ConXForm.class.isAssignableFrom(componentModel.getClass())) {
			presenter = presenterFactory.createPresenter(EditorFormPresenter.class);
		}

		if (presenter instanceof IConfigurablePresenter) {
			((IConfigurablePresenter) presenter).onConfigure(params);
		}

		return presenter;
	}

	public IPresenter<?, ? extends EventBus> createLineEditorSectionContentPresenter(AbstractConXComponent componentModel, Map<String, Object> params) throws Exception {
		params.put(IEntityEditorFactory.COMPONENT_MODEL, componentModel);
		params.put(IEntityEditorFactory.VAADIN_COMPONENT_FACTORY, this);

		IPresenter<?, ? extends EventBus> presenter = null;
		if (componentModel instanceof AttachmentEditorComponent) {
			presenter = presenterFactory.createPresenter(AttachmentEditorPresenter.class);
		} else if (componentModel instanceof ReferenceNumberEditorComponent) {
			presenter = presenterFactory.createPresenter(ReferenceNumberEditorPresenter.class);
		} else if (ConXTable.class.isAssignableFrom(componentModel.getClass())) {
			presenter = presenterFactory.createPresenter(EntityLineEditorGridPresenter.class);
		} else if (ConXForm.class.isAssignableFrom(componentModel.getClass())) {
			presenter = presenterFactory.createPresenter(EntityLineEditorFormPresenter.class);
		}

		if (presenter instanceof IConfigurablePresenter) {
			((IConfigurablePresenter) presenter).onConfigure(params);
		}

		return presenter;
	}
	
	public IPresenter<?, ? extends EventBus> createLineEditorSectionHeaderPresenter(AbstractConXComponent componentModel) throws Exception {
		IPresenter<?, ? extends EventBus> presenter = null;
		if (ConXForm.class.isAssignableFrom(componentModel.getClass())) {
			presenter = this.presenterFactory.createPresenter(EntityLineEditorFormHeaderPresenter.class);
		} else if (ConXTable.class.isAssignableFrom(componentModel.getClass())) {
			presenter = this.presenterFactory.createPresenter(EntityLineEditorGridHeaderPresenter.class);
		}

		return presenter;
	}

	public IPageComponent createPage(final IPageFlowPage page, Map<String, Object> initParams) {
		if (isInstanceOf(page, IModelDrivenPageFlowPage.class)) {
			PagePresenter pagePresenter = (PagePresenter) presenterFactory.createPresenter(PagePresenter.class);
			pagePresenter.setPage(page);
			this.config.putAll(initParams);

			AbstractConXComponent componentModel = ((TaskPage) ((IModelDrivenPageFlowPage) page).getComponentModel()).getContent();
			Component component = create(componentModel);
			setPagePresenterData(pagePresenter, component);
			pagePresenter.init(this.config);

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
		if (componentModel instanceof LineEditorContainerComponent || componentModel instanceof LineEditorComponent || componentModel instanceof MultiLevelEntityEditor
				|| ConXForm.class.isAssignableFrom(componentModel.getClass())) {
			return true;
		}
		return false;
	}

	public PresenterFactory getPresenterFactory() {
		return this.presenterFactory;
	}

	public Map<String, Object> getConfig() {
		return this.config;
	}
}
