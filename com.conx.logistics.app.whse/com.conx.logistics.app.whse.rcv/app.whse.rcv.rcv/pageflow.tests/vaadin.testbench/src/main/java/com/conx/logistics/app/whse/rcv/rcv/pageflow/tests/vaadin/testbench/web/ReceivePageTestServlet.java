package com.conx.logistics.app.whse.rcv.rcv.pageflow.tests.vaadin.testbench.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;

@Service
@Transactional
public class PageTestServlet extends AbstractApplicationServlet {
	private static final long serialVersionUID = -2094817122713873299L;
	
	static private ReceivePageTestApplication testApp;
	
	public ReceivePageTestApplication getMainApp() {
		return testApp;
	}

	@Autowired
	public void setMainApp(ReceivePageTestApplication mainApp) {
		PageTestServlet.testApp = mainApp;
	}

    @Override
    protected Class<? extends Application> getApplicationClass() {
        return ReceivePageTestApplication.class;
    }

    @Override
    protected Application getNewApplication(HttpServletRequest request)	throws ServletException {
        return testApp;
    }
}	
