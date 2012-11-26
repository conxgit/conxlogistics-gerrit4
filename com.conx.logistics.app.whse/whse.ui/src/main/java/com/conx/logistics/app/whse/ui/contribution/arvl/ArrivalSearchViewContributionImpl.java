package com.conx.logistics.app.whse.ui.contribution.arvl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.presenter.BasePresenter;

import com.conx.logistics.kernel.ui.components.dao.services.IComponentDAOService;
import com.conx.logistics.kernel.ui.components.domain.AbstractConXComponent;
import com.conx.logistics.kernel.ui.service.contribution.IViewContribution;
import com.conx.logistics.mdm.domain.application.Feature;
import com.vaadin.Application;

public class ArrivalSearchViewContributionImpl implements IViewContribution {
	
	protected final static String VIEWCODE = "WHSE.RCVNG.ARVLS.SEARCH_ARVL";

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private AbstractConXComponent arrivalSearchViewComponentModel;

	private IComponentDAOService componentDAOService;

	@Override
	public String getIcon() {
		return "icons/application_view_columns.png";
	}

	@Override
	public String getName() {
		return "Arrivals";
	}

	@Override
	public String getCode() {
		return VIEWCODE;
	}

	@Override
	public AbstractConXComponent getComponentModel(
			Application application, Feature feature) {
		if (this.arrivalSearchViewComponentModel == null) {
			String componentName = feature.getComponentModelCode();
			this.arrivalSearchViewComponentModel = componentDAOService.getByCode(componentName);
		}
		return this.arrivalSearchViewComponentModel;
	}

	@Override
	public Class<? extends BasePresenter<?, ? extends EventBus>> getPresenterClass() {
		return null;
	}

	public void setComponentDAOService(IComponentDAOService componentDAOService) {
		this.componentDAOService = componentDAOService;
	}
}
