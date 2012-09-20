package com.conx.logistics.kernel.pageflow.services;

public interface IPageFlowPage {
	public static final String PROCESS_ID = "PROCESS_ID";
	public static final String TASK_NAME = "TASK_NAME";
	
	public String getTaskName();
	
	public Class<?> getType();
}
