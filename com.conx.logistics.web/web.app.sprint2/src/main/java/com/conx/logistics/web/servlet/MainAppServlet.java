package com.conx.logistics.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.conx.logistics.kernel.ui.common.mvp.MainMVPApplication;
import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;

@Service
@Transactional
public class MainAppServlet /*extends AbstractApplicationServlet*/ {
/*
	protected final Logger log = LoggerFactory.getLogger(getClass().getName());

	static private MainMVPApplication mainApp;

	public MainMVPApplication getMainApp() {
		return mainApp;
	}

	@Autowired
	public void setMainApp(MainMVPApplication mainApp) {
		this.mainApp = mainApp;
	}

    @Override
    protected Class<? extends Application> getApplicationClass() {
        return MainMVPApplication.class;
    }

    @Override
    protected Application getNewApplication(HttpServletRequest request)	throws ServletException {
        return mainApp;
    }*/
}	
