package com.conx.logistics.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.conx.logistics.kernel.ui.common.mvp.MainMVPApplication;
import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;

@SuppressWarnings("serial")
@Service
@Transactional
public class AutowiringApplicationServlet extends AbstractApplicationServlet {
	private WebApplicationContext webApplicationContext;

	/**
	 * Called by the servlet container to indicate to a servlet that the servlet
	 * is being placed into service.
	 * 
	 * @param servletConfig
	 *            the object containing the servlet's configuration and
	 *            initialization parameters
	 * @throws javax.servlet.ServletException
	 *             if an exception has occurred that interferes with the
	 *             servlet's normal operation.
	 */
	@Override
	public void init(javax.servlet.ServletConfig servletConfig) throws javax.servlet.ServletException {
		super.init(servletConfig);
		try {
			this.webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletConfig.getServletContext());
		} catch (IllegalStateException e) {
			throw new ServletException("could not locate containing WebApplicationContext");
		}
	}

	/**
	 * Get the containing Spring {@link WebApplicationContext}. This only works
	 * after the servlet has been initialized (via {@link #init init()}).
	 * 
	 * @throws ServletException
	 *             if the operation fails
	 */
	protected final WebApplicationContext getWebApplicationContext() throws ServletException {
		if (this.webApplicationContext == null)
			throw new ServletException("can't retrieve WebApplicationContext before init() is invoked");
		return this.webApplicationContext;
	}

	/**
	 * Get the {@link AutowireCapableBeanFactory} associated with the containing
	 * Spring {@link WebApplicationContext}. This only works after the servlet
	 * has been initialized (via {@link #init init()}).
	 * 
	 * @throws ServletException
	 *             if the operation fails
	 */
	protected final AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws ServletException {
		try {
			return getWebApplicationContext().getAutowireCapableBeanFactory();
		} catch (IllegalStateException e) {
			throw new ServletException("containing context " + getWebApplicationContext() + " is not autowire-capable", e);
		}
	}

	@Override
	protected Class<? extends Application> getApplicationClass() {
		return MainMVPApplication.class;
	}

	/**
	 * Create and configure a new instance of the configured application class.
	 * 
	 * <p>
	 * The implementation in {@link AutowiringApplicationServlet} delegates to
	 * {@link #getAutowireCapableBeanFactory getAutowireCapableBeanFactory()},
	 * then invokes {@link AutowireCapableBeanFactory#createBean
	 * AutowireCapableBeanFactory.createBean()} using the configured
	 * {@link Application} class.
	 * </p>
	 * 
	 * @param request
	 *            the triggering {@link HttpServletRequest}
	 * @throws ServletException
	 *             if creation or autowiring fails
	 */
	@Override
	protected Application getNewApplication(HttpServletRequest request) throws ServletException {
		Class<? extends Application> cl = null;
		try {
			cl = getApplicationClass();
			AutowireCapableBeanFactory beanFactory = getAutowireCapableBeanFactory();
			return (Application) beanFactory.autowire(cl, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
		} catch (BeansException e) {
			if (cl == null) {
				throw new ServletException("failed to create new application instance", e);
			} else {
				throw new ServletException("failed to create new instance of " + cl, e);
			}
		}
	}
}
