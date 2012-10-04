package com.conx.logistics.app.whse.rcv.rcv.pageflow.pages;

import java.util.HashMap;
import java.util.Map;

import com.conx.logistics.app.whse.rcv.rcv.domain.Receive;
import com.conx.logistics.kernel.datasource.domain.DataSource;
import com.conx.logistics.kernel.pageflow.services.BasePageFlowPage;
import com.conx.logistics.kernel.pageflow.services.IModelDrivenPageFlowPage;
import com.conx.logistics.kernel.ui.components.domain.page.TaskPage;
import com.conx.logistics.kernel.ui.components.domain.search.SearchGrid;
import com.conx.logistics.mdm.domain.metamodel.EntityType;

public class FindReceivePage extends BasePageFlowPage implements IModelDrivenPageFlowPage {
	private TaskPage componentModel;

	@Override
	public String getTaskName() {
		return "Find Receive";
	}

	@Override
	public TaskPage getComponentModel() {
		if (componentModel == null) {
			EntityType type = new EntityType("Receive", Receive.class, null, null, null, "whreceive");
			DataSource ds = new DataSource("searchgriddatasource", type);
			
			String[] visibleColumnIds = { "id", "code", "name", "dateCreated", "dateLastUpdated", "warehouse" };
			
			SearchGrid searchGrid = new SearchGrid();
			searchGrid.setDataSource(ds);
			searchGrid.setFormTitle("Receives");
			searchGrid.setVisibleColumnIds(visibleColumnIds);
			componentModel = new TaskPage(searchGrid);
		}
		return componentModel;
	}

	@Override
	public Class<?> getType() {
		return IModelDrivenPageFlowPage.class;
	}

	@Override
	public Map<Class<?>, String> getResultKeyMap() {
		if (this.resultKeyMap == null) {
			this.resultKeyMap = new HashMap<Class<?>, String>();
			this.resultKeyMap.put(Receive.class, "receiveOut");
		}
		return this.resultKeyMap;
	}

	@Override
	public Map<Class<?>, String> getParamKeyMap() {
		if (this.paramKeyMap == null) {
			this.paramKeyMap = new HashMap<Class<?>, String>();
		}
		return this.paramKeyMap;
	}

}
