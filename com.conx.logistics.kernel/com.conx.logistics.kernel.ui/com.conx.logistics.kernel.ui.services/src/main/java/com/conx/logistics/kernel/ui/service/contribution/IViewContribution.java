package com.conx.logistics.kernel.ui.service.contribution;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.presenter.BasePresenter;

import com.conx.logistics.kernel.ui.components.domain.AbstractConXComponent;
import com.conx.logistics.mdm.domain.application.Feature;
import com.vaadin.Application;

public interface IViewContribution {
	public AbstractConXComponent  getComponentModel(Application application, Feature feature);

	public String getIcon();

	public String getName();
	
	public String getCode();

	public Class<? extends BasePresenter<?, ? extends EventBus>> getPresenterClass();
}
