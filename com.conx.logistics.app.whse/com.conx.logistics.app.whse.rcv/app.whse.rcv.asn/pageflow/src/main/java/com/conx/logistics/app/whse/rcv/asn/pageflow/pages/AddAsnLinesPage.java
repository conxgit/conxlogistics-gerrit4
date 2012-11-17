package com.conx.logistics.app.whse.rcv.asn.pageflow.pages;

import java.util.HashMap;
import java.util.Map;

import com.conx.logistics.app.whse.rcv.asn.domain.ASN;
import com.conx.logistics.kernel.datasource.domain.DataSource;
import com.conx.logistics.kernel.pageflow.services.BasePageFlowPage;
import com.conx.logistics.kernel.pageflow.services.IModelDrivenPageFlowPage;
import com.conx.logistics.kernel.ui.components.domain.page.TaskPage;
import com.conx.logistics.kernel.ui.components.domain.search.SearchGrid;
import com.conx.logistics.mdm.domain.metamodel.EntityType;
import com.conx.logistics.mdm.domain.organization.Organization;

public class AddAsnLinesPage extends BasePageFlowPage implements IModelDrivenPageFlowPage {
private TaskPage componentModel;
	
	@Override
	public Map<Class<?>, String> getParamKeyMap() {
		if (this.paramKeyMap == null) {
			this.paramKeyMap = new HashMap<Class<?>, String>();
		}
		return this.paramKeyMap;
	}

	@Override
	public Map<Class<?>, String> getResultKeyMap() {
		if (this.resultKeyMap == null) {
			this.resultKeyMap = new HashMap<Class<?>, String>();
			this.resultKeyMap.put(ASN.class, "asnOut");
		}
		return this.resultKeyMap;
	}

	@Override
	public String getTaskName() {
		return "AddAsnLinesPage";
	}

	@Override
	public Class<?> getType() {
		return IModelDrivenPageFlowPage.class;
	}
	

	@Override
	public TaskPage getComponentModel() {
		if (componentModel == null) {
			EntityType type = new EntityType("Organization", Organization.class, null, null, null, "reforganization");
			DataSource ds = new DataSource("orgsearchgriddatasource", type);
			
			String[] visibleColumnIds = { "id", "code", "name", "dateCreated" };
			
			SearchGrid searchGrid = new SearchGrid();
			searchGrid.setDataSource(ds);
			searchGrid.setFormTitle("Organization");
			searchGrid.setVisibleColumnIds(visibleColumnIds);
			componentModel = new TaskPage(searchGrid);
		}
		return componentModel;
	}
}