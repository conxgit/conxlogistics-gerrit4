package com.conx.logistics.kernel.pageflow.services;

import com.conx.logistics.kernel.ui.components.domain.page.TaskPage;

public interface IModelDrivenPageFlowPage extends IPageFlowPage {
	public TaskPage getComponentModel();
}