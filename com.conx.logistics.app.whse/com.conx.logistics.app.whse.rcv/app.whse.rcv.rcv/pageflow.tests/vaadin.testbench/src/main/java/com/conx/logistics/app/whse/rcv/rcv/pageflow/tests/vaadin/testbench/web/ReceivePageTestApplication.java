package com.conx.logistics.app.whse.rcv.rcv.pageflow.tests.vaadin.testbench.web;

import com.conx.logistics.kernel.pageflow.vaadin.testbench.web.PageTestApplication;
import com.conx.logistics.kernel.pageflow.vaadin.testbench.web.PageTestWindow;
import com.vaadin.ui.Window;

public class ReceivePageTestApplication extends PageTestApplication {
	private static final long serialVersionUID = 1L;
	
	@Override
	public void init() {
		setMainWindow(new PageTestWindow());
	}
	
	@Override
    public Window getWindow(String name) {
        // Multitab support, return new windows for each tab
        Window window = super.getWindow(name);
        if (window == null && name != null) {
            window = new PageTestWindow();
            window.setName(name);
            addWindow(window);
        }
        return window;
    }

}
