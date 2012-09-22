package com.conx.logistics.kernel.pageflow.services;

import java.util.Map;

import org.vaadin.teemu.wizards.WizardStep;

public interface IPageComponent extends WizardStep {
	public static final String CONX_ENTITY_MANAGER_FACTORY = "CONX_ENTITY_MANAGER_FACTORY";
	public static final String JTA_GLOBAL_TRANSACTION_MANAGER = "JTA_GLOBAL_TRANSACTION_MANAGER";
	public static final String ENTITY_CONTAINER_PROVIDER = "ENTITY_CONTAINER_PROVIDER";
	public static final String TASK_WIZARD = "TASK_WIZARD";
	public static final String PAGE_FLOW_PAGE_CHANGE_EVENT_HANDLER = "PAGE_FLOW_PAGE_CHANGE_EVENT_HANDLER";
	public static final String MVP_PRESENTER_FACTORY = "MVP_PRESENTER_FACTORY";
	
	public void init(Map<String, Object> initParams);
	public void setParameterData(Map<String, Object> params);
	public Map<String, Object> getResultData();
	public boolean isExecuted();
	public void setExecuted(boolean executed);
}
