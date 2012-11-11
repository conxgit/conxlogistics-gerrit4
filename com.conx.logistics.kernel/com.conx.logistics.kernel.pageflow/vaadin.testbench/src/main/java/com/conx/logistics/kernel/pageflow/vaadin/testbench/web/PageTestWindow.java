package com.conx.logistics.kernel.pageflow.vaadin.testbench.web;

import java.util.Map;

import com.conx.logistics.kernel.pageflow.services.IPageComponent;
import com.vaadin.ui.Window;

public class PageTestWindow extends Window {
	private static final long serialVersionUID = 1L;
	
	private IPageComponent pageComponent;

	public PageTestWindow() {
		this.setTheme("conx");
	}
	
	public PageTestWindow(IPageComponent pageComponent, Map<String, Object> inputParams) {
		this.setTheme("conx");
		this.setPageComponent(pageComponent, inputParams);
	}

	public IPageComponent getPageComponent() {
		return pageComponent;
	}
	
	public void setPageComponent(IPageComponent pageComponent, Map<String, Object> inputParams) {
		this.removeComponent(this.pageComponent.getContent());
		this.pageComponent = pageComponent;
		this.pageComponent.setParameterData(inputParams);
		this.pageComponent.getContent().setSizeFull();
		this.addComponent(this.pageComponent.getContent());
	}
}
