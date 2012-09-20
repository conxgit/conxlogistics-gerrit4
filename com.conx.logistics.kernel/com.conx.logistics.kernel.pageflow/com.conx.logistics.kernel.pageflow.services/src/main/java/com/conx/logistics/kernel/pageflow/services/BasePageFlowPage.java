package com.conx.logistics.kernel.pageflow.services;

public abstract class BasePageFlowPage implements IPageFlowPage {
	@Override
	public abstract String getTaskName();
	@Override
	public abstract Class<?> getType();
}
