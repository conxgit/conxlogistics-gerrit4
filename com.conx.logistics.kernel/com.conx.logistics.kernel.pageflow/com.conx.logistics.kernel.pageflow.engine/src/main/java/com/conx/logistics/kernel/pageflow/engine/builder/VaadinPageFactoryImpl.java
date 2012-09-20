package com.conx.logistics.kernel.pageflow.engine.builder;

import java.util.HashMap;
import java.util.Map;

import com.conx.logistics.kernel.datasource.domain.DataSource;
import com.conx.logistics.kernel.pageflow.services.BasePageComponent;
import com.conx.logistics.kernel.pageflow.services.ICustomDrivenPageFlowPage;
import com.conx.logistics.kernel.pageflow.services.IModelDrivenPageFlowPage;
import com.conx.logistics.kernel.pageflow.services.IPageComponent;
import com.conx.logistics.kernel.pageflow.services.IPageFlowPage;
import com.conx.logistics.kernel.ui.components.domain.page.TaskPage;
import com.conx.logistics.kernel.ui.components.domain.search.SearchGrid;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.search.EntitySearchGrid;
import com.vaadin.data.Container;

public class VaadinPageFactoryImpl {
	public static final String CONX_ENTITY_MANAGER_FACTORY = "CONX_ENTITY_MANAGER_FACTORY";
	public static final String JTA_GLOBAL_TRANSACTION_MANAGER = "JTA_GLOBAL_TRANSACTION_MANAGER";
	public static final String ENTITY_CONTAINER_PROVIDER = "ENTITY_CONTAINER_PROVIDER";
	public static final String TASK_WIZARD = "TASK_WIZARD";
	public static final String PAGE_FLOW_PAGE_CHANGE_EVENT_HANDLER = "PAGE_FLOW_PAGE_CHANGE_EVENT_HANDLER";

	public IPageComponent create(final IPageFlowPage page, Map<String, Object> initParams) {
		IPageComponent pageComponent = null;
		if (isInstanceOf(page, IModelDrivenPageFlowPage.class)) {
			TaskPage taskPage = ((IModelDrivenPageFlowPage) page).getComponentModel();
			if (taskPage.getContent() instanceof SearchGrid) {
				pageComponent = new BasePageComponent((IPageFlowPage) page, new EntitySearchGrid((SearchGrid) taskPage.getContent())) {

					@Override
					public void setParameterData(Map<String, Object> params) {
						try {
							DataSource ds = ((IModelDrivenPageFlowPage) this.getPage()).getComponentModel().getContent().getDataSource();
							Container container = (Container) this.getContainerProvider().createPersistenceContainer(ds.getEntityType().getJavaType());
							((EntitySearchGrid) this.getContent()).setContainerDataSource(container);
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					}

					@Override
					public Map<String, Object> getResultData() {
						HashMap<String, Object> res = new HashMap<String, Object>();
						res.put("receive", ((EntitySearchGrid) getContent()).getSelectedEntity());
						return res;
					}
				};
			}
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
