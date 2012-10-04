package com.conx.logistics.app.whse.rcv.rcv.pageflow.pages.jit;

import java.util.HashMap;
import java.util.Map;

import com.conx.logistics.kernel.pageflow.services.BasePageFlowPage;
import com.conx.logistics.kernel.pageflow.services.IModelDrivenPageFlowPage;
import com.conx.logistics.kernel.ui.components.domain.page.TaskPage;

public class AddDynaPickUpPage extends BasePageFlowPage implements IModelDrivenPageFlowPage {

	@Override
	public String getTaskName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskPage getComponentModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getType() {
		return IModelDrivenPageFlowPage.class;
	}
	
	@Override
	public Map<Class<?>, String> getResultKeyMap() {
		if (this.resultKeyMap == null) {
			this.resultKeyMap = new HashMap<Class<?>, String>();
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
