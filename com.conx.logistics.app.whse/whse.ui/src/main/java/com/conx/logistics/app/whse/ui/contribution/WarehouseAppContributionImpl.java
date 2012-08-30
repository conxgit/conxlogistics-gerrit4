package com.conx.logistics.app.whse.ui.contribution;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.presenter.BasePresenter;
import org.vaadin.mvp.presenter.IPresenterFactory;
import org.vaadin.mvp.uibinder.UiBinderException;

import com.conx.logistics.app.whse.dao.services.IWarehouseApplicationDAOService;
import com.conx.logistics.app.whse.ui.WarehouseEventBus;
import com.conx.logistics.app.whse.ui.WarehousePresenter;
import com.conx.logistics.kernel.ui.common.mvp.MainMVPApplication;
import com.conx.logistics.kernel.ui.components.domain.AbstractConXComponent;
import com.conx.logistics.kernel.ui.service.contribution.IApplicationViewContribution;
import com.conx.logistics.mdm.domain.application.Feature;
import com.vaadin.Application;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;

@Transactional
@Repository
public class WarehouseAppContributionImpl implements IApplicationViewContribution {
	
	public final static String VIEWCODE = "WHSE";

	protected Logger logger = LoggerFactory.getLogger(this.getClass());	
    
	private PlatformTransactionManager globalTransactionManager;    
    
    private IWarehouseApplicationDAOService warehouseApplicationDAOService;

	private Component view;

	private Form userForm;

	public String getIcon() {
		return "custom/img/download_box.png";
	}

	public String getName() {
		return "Warehouse";
	}
	
	public void setGlobalTransactionManager(
			PlatformTransactionManager globalTransactionManager) {
		this.globalTransactionManager = globalTransactionManager;
	}

	public void setWarehouseApplicationDAOService(
			IWarehouseApplicationDAOService warehouseApplicationDAOService) {
		this.warehouseApplicationDAOService = warehouseApplicationDAOService;
	}

	public void start()
	{
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setName("WarehouseAppContributionImpl");
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.globalTransactionManager.getTransaction(def);	
		try
		{
			warehouseApplicationDAOService.provideApplicationMetadata();			
			this.globalTransactionManager.commit(status);
		}
		catch (Exception e) 
		{
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);
			
			this.globalTransactionManager.rollback(status);
		}		
	}

	@Override
	public String getCode() {
		return VIEWCODE;
	}


	@Override
	public Component getApplicationComponent(Application app) throws UiBinderException {
		IPresenterFactory pf = ((MainMVPApplication)app).getPresenterFactory();
		WarehousePresenter cpp =  (WarehousePresenter)pf.createPresenter(WarehousePresenter.class);
		WarehouseEventBus eb = (WarehouseEventBus) cpp.getEventBus();
		eb.start((MainMVPApplication)app);
		return (Component)cpp.getView();
	}

	@Override
	public AbstractConXComponent getComponentModel(Application application,
			Feature feature) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends BasePresenter<?, ? extends EventBus>> getPresenterClass() {
		return WarehousePresenter.class;
	}
}
