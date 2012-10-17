package com.conx.logistics.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;

@Service
@Transactional
public class MainAppServlet extends AbstractApplicationServlet {
	private static final long serialVersionUID = -2094817122713873299L;
	
	static private MockApp mockApp;
	
	public MockApp getMainApp() {
		return mockApp;
	}

	@Autowired
	public void setMainApp(MockApp mainApp) {
		MainAppServlet.mockApp = mainApp;
	}

    @Override
    protected Class<? extends Application> getApplicationClass() {
        return MockApp.class;
    }

    @Override
    protected Application getNewApplication(HttpServletRequest request)	throws ServletException {
        return mockApp;
    }
}	
