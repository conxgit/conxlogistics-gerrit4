package com.conx.logistics.kernel.pageflow.engine.builder;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.vaadin.mvp.eventbus.EventBusManager;
import org.vaadin.mvp.presenter.IPresenterFactory;
import org.vaadin.mvp.presenter.PresenterFactory;

import com.conx.logistics.kernel.datasource.domain.DataSource;
import com.conx.logistics.kernel.pageflow.services.BasePageComponent;
import com.conx.logistics.kernel.pageflow.services.ICustomDrivenPageFlowPage;
import com.conx.logistics.kernel.pageflow.services.IModelDrivenPageFlowPage;
import com.conx.logistics.kernel.pageflow.services.IPageComponent;
import com.conx.logistics.kernel.pageflow.services.IPageFlowPage;
import com.conx.logistics.kernel.pageflow.ui.mvp.IPageDataHandler;
import com.conx.logistics.kernel.pageflow.ui.mvp.PagePresenter;
import com.conx.logistics.kernel.ui.components.domain.page.TaskPage;
import com.conx.logistics.kernel.ui.components.domain.search.SearchGrid;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.search.EntitySearchGrid;
import com.vaadin.data.Container;

public class VaadinPageFactoryImpl {
	
	public VaadinPageFactoryImpl() {
	}

	public IPageComponent create(final IPageFlowPage page, Map<String, Object> initParams) {
		IPageComponent pageComponent = null;
		if (isInstanceOf(page, IModelDrivenPageFlowPage.class)) {
			PresenterFactory presenterFactory = new PresenterFactory(new EventBusManager(), Locale.getDefault());
			PagePresenter pagePresenter = (PagePresenter) presenterFactory.createPresenter(PagePresenter.class);
			TaskPage taskPage = ((IModelDrivenPageFlowPage) page).getComponentModel();
			if (taskPage.getContent() instanceof SearchGrid) {
				EntitySearchGrid vaadinComponent =  new EntitySearchGrid((SearchGrid) taskPage.getContent());
				vaadinComponent.setStatusEnabled(true);
				
				pagePresenter.setPage(page);
				pagePresenter.setPageContent(vaadinComponent);
				pagePresenter.setDataHandler(new IPageDataHandler() {
					
					@Override
					public void setParameterData(PagePresenter source, Map<String, Object> params) {
						try {
							DataSource ds = ((IModelDrivenPageFlowPage) source.getPage()).getComponentModel().getContent().getDataSource();
							Container container = (Container) source.getContainerProvider().createPersistenceContainer(ds.getEntityType().getJavaType());
							((EntitySearchGrid) source.getPageContent()).setContainerDataSource(container);
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
					
					@Override
					public Map<String, Object> getResultData(PagePresenter source) {
						HashMap<String, Object> res = new HashMap<String, Object>();
						res.put("receive", ((EntitySearchGrid) source.getView()).getSelectedEntity());
						return res;
					}
				});
			}
			pageComponent = pagePresenter;
			
			if (pageComponent == null) {
				return null;
			}
		} else if (isInstanceOf(page, ICustomDrivenPageFlowPage.class)) {
			pageComponent = ((ICustomDrivenPageFlowPage) page).getPageComponent();
		} else {
			return null;
		}
		pageComponent.init(initParams);
		return pageComponent;
	}

	public boolean isInstanceOf(IPageFlowPage page, Class<?> type) {
		return page.getType().isAssignableFrom(type);
	}
}
