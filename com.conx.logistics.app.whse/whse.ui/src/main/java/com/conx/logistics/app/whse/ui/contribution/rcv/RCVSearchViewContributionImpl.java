package com.conx.logistics.app.whse.ui.contribution.rcv;

import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.presenter.BasePresenter;

import com.conx.logistics.kernel.ui.components.dao.services.IComponentDAOService;
import com.conx.logistics.kernel.ui.components.domain.AbstractConXComponent;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.MasterDetailComponent;
import com.conx.logistics.kernel.ui.service.contribution.IViewContribution;
import com.conx.logistics.mdm.domain.application.Feature;
import com.vaadin.Application;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;

@Transactional
@Repository
public class RCVSearchViewContributionImpl implements IViewContribution {
	
	protected final static String VIEWCODE = "RCVNG.RCV.SEARCH";

	protected Logger logger = LoggerFactory.getLogger(RCVSearchViewContributionImpl.class);

	private Component view;

	private Form userForm;
	
	private IComponentDAOService componentDAOService;

	@Override
	public String getIcon() {
		return "icons/application_view_columns.png";
	}

	@Override
	public String getName() {
		return "Receives";
	}

	@Override
	public String getCode() {
		return VIEWCODE;
	}

	@Override
	public AbstractConXComponent getComponentModel(
			Application application, Feature feature) {
		String componentName = feature.getComponentModelCode();
		MasterDetailComponent md = (MasterDetailComponent)componentDAOService.getByCode(componentName);
		return md;
	}
	
	@Override
	public Class<? extends BasePresenter<?, ? extends EventBus>> getPresenterClass() {
		// TODO Auto-generated method stub
		return null;
	}	

	public void setComponentDAOService(IComponentDAOService componentDAOService) {
		this.componentDAOService = componentDAOService;
	}
}
