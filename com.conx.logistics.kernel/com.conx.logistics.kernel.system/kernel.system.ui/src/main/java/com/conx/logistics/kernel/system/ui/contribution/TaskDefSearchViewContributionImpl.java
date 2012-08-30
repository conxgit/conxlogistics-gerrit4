package com.conx.logistics.kernel.system.ui.contribution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.presenter.BasePresenter;
import org.vaadin.mvp.presenter.FactoryPresenter;

import com.conx.logistics.kernel.system.ui.ModuleEditorPresenter;
import com.conx.logistics.kernel.system.ui.TaskDefSearchPresenter;
import com.conx.logistics.kernel.ui.components.domain.AbstractConXComponent;
import com.conx.logistics.kernel.ui.service.contribution.IApplicationViewContribution;
import com.conx.logistics.kernel.ui.service.contribution.IViewContribution;
import com.conx.logistics.mdm.domain.application.Feature;
import com.vaadin.Application;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.VerticalLayout;

public class TaskDefSearchViewContributionImpl implements IViewContribution {
	
	protected final static String VIEWCODE = "KERNEL.CONTROLPANEL.WFMNGMT.WTDFS.SEARCH";

	protected Logger logger = LoggerFactory.getLogger(TaskDefSearchViewContributionImpl.class);

	private Component view;

	private Form userForm;

	public String getIcon() {
		return "icons/application_view_columns.png";
	}

	public String getName() {
		return "Task Definition Search View";
	}

	@Override
	public String getCode() {
		return VIEWCODE;
	}

	@Override
	public AbstractConXComponent getComponentModel(Application application,
			Feature feature) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends BasePresenter<?, ? extends EventBus>> getPresenterClass() {
		return TaskDefSearchPresenter.class;
	}	
}