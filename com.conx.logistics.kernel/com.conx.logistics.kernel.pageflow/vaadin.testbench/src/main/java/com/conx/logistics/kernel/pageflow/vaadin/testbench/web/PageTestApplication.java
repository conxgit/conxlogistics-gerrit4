package com.conx.logistics.kernel.pageflow.vaadin.testbench.web;

import java.util.HashMap;

import com.conx.logistics.kernel.pageflow.ui.builder.VaadinPageFactoryImpl;
import com.vaadin.Application;

public class PageTestApplication extends Application {
	private static final long serialVersionUID = 1L;
	
	private VaadinPageFactoryImpl factory;
	
	public PageTestApplication() {
		this.factory = new VaadinPageFactoryImpl(new HashMap<String, Object>());
	}
	
	@Override
	public void init() {
	}

	protected VaadinPageFactoryImpl factory() {
		return factory;
	}

}
