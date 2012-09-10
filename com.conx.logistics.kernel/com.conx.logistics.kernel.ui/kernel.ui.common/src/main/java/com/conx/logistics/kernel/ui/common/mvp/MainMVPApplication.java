package com.conx.logistics.kernel.ui.common.mvp;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.EventBusManager;
import org.vaadin.mvp.presenter.BasePresenter;
import org.vaadin.mvp.presenter.IPresenter;
import org.vaadin.mvp.presenter.IPresenterFactory;
import org.vaadin.mvp.presenter.PresenterFactory;
import org.vaadin.mvp.uibinder.UiBinderException;

import com.conx.logistics.common.utils.Validator;
import com.conx.logistics.kernel.documentlibrary.remote.services.IRemoteDocumentRepository;
import com.conx.logistics.kernel.pageflow.services.IPageFlowManager;
import com.conx.logistics.kernel.portal.remote.services.IPortalOrganizationService;
import com.conx.logistics.kernel.portal.remote.services.IPortalRoleService;
import com.conx.logistics.kernel.portal.remote.services.IPortalUserService;
import com.conx.logistics.kernel.system.dao.services.application.IApplicationDAOService;
import com.conx.logistics.kernel.ui.common.data.container.EntityTypeContainerFactory;
import com.conx.logistics.kernel.ui.common.entityprovider.jta.CustomCachingMutableLocalEntityProvider;
import com.conx.logistics.kernel.ui.common.ui.menu.app.AppMenuEntry;
import com.conx.logistics.kernel.ui.factory.services.IEntityEditorFactory;
import com.conx.logistics.kernel.ui.service.IUIContributionManager;
import com.conx.logistics.kernel.ui.service.contribution.IActionContribution;
import com.conx.logistics.kernel.ui.service.contribution.IApplicationViewContribution;
import com.conx.logistics.kernel.ui.service.contribution.IMainApplication;
import com.conx.logistics.kernel.ui.service.contribution.IViewContribution;
import com.conx.logistics.mdm.dao.services.IEntityMetadataDAOService;
import com.conx.logistics.mdm.dao.services.documentlibrary.IFolderDAOService;
import com.conx.logistics.mdm.domain.constants.RoleCustomCONSTANTS;
import com.conx.logistics.mdm.domain.user.User;
import com.sun.syndication.io.impl.Base64;
import com.vaadin.Application;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.util.EntityManagerPerRequestHelper;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;

public class MainMVPApplication extends Application implements IMainApplication,HttpServletRequestListener {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	/** Per application (session) event bus manager */
	private EventBusManager ebm = new EventBusManager();
	/** Per application presenter factory */
	// create an instance of a default presenter factory
	private PresenterFactory presenterFactory = null;

	/** Main presenter */
	private IPresenter<?, ? extends EventBus> mainPresenter;
	private IUIContributionManager uiContributionManager;

	private IApplicationDAOService applicationDAOService;
	
	private IFolderDAOService folderDAOService;
	
	private IRemoteDocumentRepository remoteDocumentRepository;
	
	private boolean appServiceInititialized = false;

	private PlatformTransactionManager kernelSystemTransManager;
	
	private UserTransaction userTransaction;

	private EntityManagerFactory kernelSystemEntityManagerFactory;	
	
	private IPageFlowManager pageFlowEngine;
	
	private IEntityEditorFactory entityEditorFactory;
	
	/** UI contributions management*/
	private final Map<String,IApplicationViewContribution> appContributions = Collections
			.synchronizedMap(new HashMap<String,IApplicationViewContribution>());
	private final Map<String,IViewContribution> viewContributions = Collections
			.synchronizedMap(new HashMap<String,IViewContribution>());	
	private final Map<String,IActionContribution> actionContributions = Collections
			.synchronizedMap(new HashMap<String,IActionContribution>());	
	
	private volatile boolean initialized = false;

	private MainEventBus mainEventBus;

	private EntityTypeContainerFactory entityTypeContainerFactory;

	private EntityManagerPerRequestHelper entityManagerPerRequestHelper;

	private HashMap<String, Object> entityFactoryPresenterParams;
	
	private IEntityMetadataDAOService entityMetaDataDAOService;
	
	private IPortalUserService portalUserService;
	
	private IPortalRoleService portalRoleService;	
	
	private IPortalOrganizationService portalOrganizationService;
	
	private User currentUser;

	@Override
	public void init() {
		try 
		{
			setTheme("conx");
			
			//Presenter factory
			this.presenterFactory = new PresenterFactory(ebm, getLocale());
			this.presenterFactory.setApplication(this);
			
			//Create container manager/helper
			this.entityManagerPerRequestHelper = new EntityManagerPerRequestHelper();
			
			// request an instance of MainPresenter
			mainPresenter = this.presenterFactory.createPresenter(MainPresenter.class);
			mainEventBus = (MainEventBus) mainPresenter.getEventBus();
			
			
			//Create EntityFactory Presenter params
			this.entityFactoryPresenterParams = new HashMap<String, Object>();
			this.entityFactoryPresenterParams.put(IEntityEditorFactory.FACTORY_PARAM_MVP_EVENTBUS_MANAGER,ebm);
			this.entityFactoryPresenterParams.put(IEntityEditorFactory.FACTORY_PARAM_MVP_LOCALE,getLocale());
			this.entityFactoryPresenterParams.put(IEntityEditorFactory.FACTORY_PARAM_MVP_ENTITYMANAGERPERREQUESTHELPER,this.entityManagerPerRequestHelper);
			this.entityFactoryPresenterParams.put(IEntityEditorFactory.FACTORY_PARAM_MVP_ENTITY_MANAGER_FACTORY,this.kernelSystemEntityManagerFactory);
			this.entityFactoryPresenterParams.put(IEntityEditorFactory.FACTORY_PARAM_IDOCLIB_REPO_SERVICE,this.remoteDocumentRepository);
			this.entityFactoryPresenterParams.put(IEntityEditorFactory.FACTORY_PARAM_IFOLDER_SERVICE,this.folderDAOService);
			this.entityFactoryPresenterParams.put(IEntityEditorFactory.FACTORY_PARAM_MAIN_APP,this);
			this.entityFactoryPresenterParams.put(IEntityEditorFactory.FACTORY_PARAM_IENTITY_METADATA_SERVICE, entityMetaDataDAOService);
			
			mainEventBus.start(this);
			
			//By default, add workspace
			IApplicationViewContribution ac = appContributions.get("KERNEL.WORKSPACE");
			if (Validator.isNotNull(ac))
			{
				Class<? extends BasePresenter<?, ? extends EventBus>> acClass = ac.getPresenterClass();
				//IPresenter acPresenter = this.presenterFactory.createPresenter(acClass);
				//StartableApplicationEventBus acSB = (StartableApplicationEventBus)acPresenter.getEventBus();
				//acSB.start(this);
				mainEventBus.openApplication(acClass,ac.getName(),ac.getIcon(),false);
			}
			
			initialized = true;
			//AppMenuEntry[] entries = createAppMenuEntries();
			//mainEventBus.updateAppContributions(entries);		
			

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);
		}

	}
	
	public void bindEntityMetaDataDAOService(
			IEntityMetadataDAOService entityMetaDataDAOService, Map properties) {
			logger.debug("bindEntityMetaDataDAOService()");
			this.entityMetaDataDAOService = entityMetaDataDAOService;
	}
	
	public void unbindEntityMetaDataDAOService(
			IEntityMetadataDAOService entityMetaDataDAOService, Map properties) {
			logger.debug("unbindEntityMetaDataDAOService()");
			this.entityMetaDataDAOService  = null;
	}      
	
	/**
	 * 
	 * HttpServletRequestListener
	 * 
	 */
	@Override
    public void onRequestStart(HttpServletRequest request, HttpServletResponse response) {
		//authenticate(request);
		Map pns = request.getParameterMap();//email,pwd
		String email = (String)pns.get("email");
		String pwd = (String)pns.get("pwd");
		String screenName = null;
		
		if (Validator.isNull(currentUser))
		{
			if (Validator.isNotNull(email)) //Normal login
			{
				try {
					currentUser = portalUserService.provideUserByEmailAddress(email);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else//Test/Dev login
			{
				email = "test@liferay.com";
				screenName = "ConX Test Admin";
				currentUser = new User();
				currentUser.setEmailAddress(email);
				currentUser.setScreenName(screenName);
			}
		}
		
		//Start request helper
		if (this.entityManagerPerRequestHelper != null)//Init called already
			this.entityManagerPerRequestHelper.requestStart();
	}
	
	 private boolean authenticate(HttpServletRequest req)
	 {
	  String authhead=req.getHeader("Authorization");

	  if(authhead!=null)
	  {
	   //*****Decode the authorisation String*****
	   String usernpass=Base64.decode(authhead.substring(6));
	   //*****Split the username from the password*****
	   String user=usernpass.substring(0,usernpass.indexOf(":"));
	   String password=usernpass.substring(usernpass.indexOf(":")+1);

	   if (user.equals("user") && password.equals("pass"))
	    return true;
	  }
	  
	  return false;
	 }

	@Override
	public void onRequestEnd(HttpServletRequest request, HttpServletResponse response) {
		if (this.entityManagerPerRequestHelper != null)//Init called already
			this.entityManagerPerRequestHelper.requestEnd();
	}	

	@Override
	public Object createPersistenceContainer(Class entityClass) {
		//-- Create JTA JPA Container Entity Provider
		CustomCachingMutableLocalEntityProvider provider = new CustomCachingMutableLocalEntityProvider(entityClass,
													this.kernelSystemEntityManagerFactory,
													this.userTransaction);
		
		EntityManager em = this.kernelSystemEntityManagerFactory.createEntityManager();
		JPAContainer container = JPAContainerFactory.make(entityClass, (EntityManager)null);
		container.setEntityProvider(provider);
		//this.entityManagerPerRequestHelper.addContainer(container);
		
		return container;
	}
	
	public IPresenterFactory getPresenterFactory() {
		return this.presenterFactory;
	}
	
	public void bindActionContribution(IActionContribution actionContribution,
			Map properties) {
		String code = (String)properties.get(IUIContributionManager.UISERVICE_PROPERTY_CODE);
		if (Validator.isNotNull(code))
		{
			logger.info("bindActionContribution("+code+")");
			actionContributions.put(code,actionContribution);
			if (initialized) {
				/*
				tabSheet.addTab(viewContribution.getView(this), viewContribution
						.getName(), new ThemeResource(viewContribution.getIcon()));
				*/
			}
		}
		else
		{
			logger.error("bindViewContribution has no code associated with it. Registration failed.");
		}
	}

	public void unbindActionContribution(IActionContribution actionContribution,
			Map properties) {
		String code = (String)properties.get(IUIContributionManager.UISERVICE_PROPERTY_CODE);
		if (Validator.isNotNull(code))
		{
			logger.info("unbindActionContribution("+code+")");
			actionContributions.remove(code);
			if (initialized) {
				/*
				tabSheet.addTab(viewContribution.getView(this), viewContribution
						.getName(), new ThemeResource(viewContribution.getIcon()));
				*/
			}
		}
		else
		{
			logger.error("unbindActionContribution has no code associated with it. Deregistration failed.");
		}
	}		


	public void bindViewContribution(IViewContribution viewContribution,
			Map properties) {
		String code = (String)properties.get(IUIContributionManager.UISERVICE_PROPERTY_CODE);
		if (Validator.isNotNull(code))
		{
			logger.info("bindViewContribution("+code+")");
			viewContributions.put(code,viewContribution);
			if (initialized) {
				/*
				tabSheet.addTab(viewContribution.getView(this), viewContribution
						.getName(), new ThemeResource(viewContribution.getIcon()));
				*/
			}
		}
		else
		{
			logger.error("bindViewContribution has no code associated with it. Registration failed.");
		}
	}

	public void unbindViewContribution(IViewContribution viewContribution,
			Map properties) {
		String code = (String)properties.get(IUIContributionManager.UISERVICE_PROPERTY_CODE);
		if (Validator.isNotNull(code))
		{
			logger.info("unbindViewContribution("+code+")");
			viewContributions.remove(code);
			if (initialized) {
				/*
				tabSheet.addTab(viewContribution.getView(this), viewContribution
						.getName(), new ThemeResource(viewContribution.getIcon()));
				*/
			}
		}
		else
		{
			logger.error("unbindViewContribution has no code associated with it. Deregistration failed.");
		}
	}	
	
	public void bindApplicationContribution(IApplicationViewContribution appContribution,
			Map properties) throws UiBinderException {
		String code = (String)properties.get(IUIContributionManager.UISERVICE_PROPERTY_CODE);
		if (Validator.isNotNull(code))
		{
			logger.info("bindApplicationContribution("+code+")");
			appContributions.put(code,appContribution);
			if (initialized) {
				AppMenuEntry entry = createAppMenuEntry(appContribution);
				mainEventBus.addAppContribution(entry);
			}
		}
		else
		{
			logger.error("bindApplicationContribution has no code associated with it. Registration failed.");
		}
	}

	public void unbindApplicationContribution(
			IApplicationViewContribution appContribution, Map properties) throws UiBinderException {
		String code = (String)properties.get(IUIContributionManager.UISERVICE_PROPERTY_CODE);
		if (Validator.isNotNull(code))
		{
			logger.info("unbindApplicationContribution("+code+")");
			appContributions.remove(code);
			if (initialized) {
				AppMenuEntry entry = createAppMenuEntry(appContribution);
				mainEventBus.removeAppContribution(entry);
			}
		}
		else
		{
			logger.error("unbindApplicationContribution has no code associated with it. Deregistration failed.");
		}
	}	
	
	private AppMenuEntry createAppMenuEntry(IApplicationViewContribution avc) throws UiBinderException {
		return new AppMenuEntry(avc.getCode(),avc.getName(), avc.getIcon(), avc.getApplicationComponent(this));
	}
	
	public AppMenuEntry[] createAppMenuEntries() throws UiBinderException {
		if (appContributions.isEmpty())
			return new AppMenuEntry[]{};
		else
		{
			ArrayList<AppMenuEntry> entries = new ArrayList<AppMenuEntry>();
			for (IApplicationViewContribution ac : appContributions.values())
			{
				entries.add(new AppMenuEntry(ac.getCode(),ac.getName(), ac.getIcon(), ac.getApplicationComponent(this)));
			}
			return entries.toArray(new AppMenuEntry[]{});
		}
	}

	
	public void bindApplicationDAOService(
			IApplicationDAOService applicationDAOService, Map properties) {
		logger.debug("bindApplicationDAOService()");
		this.applicationDAOService = applicationDAOService;
		if (!appServiceInititialized)
			initAppService();
	}

	private void initAppService() {
		if (Validator.isNotNull(this.kernelSystemTransManager) && Validator.isNotNull(this.applicationDAOService))
		{
			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			// explicitly setting the transaction name is something that can only be done programmatically
			def.setName("sendFlightScheduleUpdate");
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
			TransactionStatus status = this.kernelSystemTransManager.getTransaction(def);			
			try {
				this.applicationDAOService.provideControlPanelApplication();
				this.kernelSystemTransManager.commit(status);	
			} 
			catch (Exception e) 
			{
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				String stacktrace = sw.toString();
				logger.error(stacktrace);
				
				this.kernelSystemTransManager.rollback(status);
			}
			appServiceInititialized = true;
		}
	}

	public void unbindApplicationDAOService(
			IApplicationDAOService applicationDAOService, Map properties) {
		logger.debug("unbindApplicationDAOService()");
		this.applicationDAOService = null;
		appServiceInititialized = false;
	}
	
	public void unbindPageFlowEngine(
			IPageFlowManager pageflowEngine, Map properties) {
		logger.debug("unbindPageFlowEngine()");
		this.pageFlowEngine  = null;
	}
	
	public void bindPageFlowEngine(
			IPageFlowManager pageflowEngine, Map properties) {
		logger.debug("bindPageFlowEngine()");
		this.pageFlowEngine  = pageflowEngine;
		this.pageFlowEngine.setMainApplication(this);
	}

	public void bindKernelSystemTransManager(
			PlatformTransactionManager kernelSystemTransManager, Map properties) {
		logger.debug("bindKernelSystemTransManager()");
		this.kernelSystemTransManager = kernelSystemTransManager;
		if (!appServiceInititialized)
			initAppService();		
	}
	
	public void unbindKernelSystemTransManager(
			PlatformTransactionManager kernelSystemTransManager, Map properties) {
		logger.debug("unbindKernelSystemTransManager()");
		this.kernelSystemTransManager = null;
		appServiceInititialized = false;
	}
	
	public void bindUserTransaction(
			UserTransaction userTransaction, Map properties) {
		logger.debug("bindUserTransaction()");
		this.userTransaction = userTransaction;		
	}
	
	public void unbindUserTransaction(
			UserTransaction userTransaction, Map properties) {
		logger.debug("unbindUserTransaction()");
		this.userTransaction = null;	
	}	
	
	public void bindKernelSystemEntityManagerFactory(
			EntityManagerFactory kernelSystemEntityManagerFactory, Map properties) {
		logger.debug("bindKernelSystemEntityManagerFactory()");
		this.entityTypeContainerFactory  = new EntityTypeContainerFactory(kernelSystemEntityManagerFactory.createEntityManager());
		this.kernelSystemEntityManagerFactory = kernelSystemEntityManagerFactory;
	}

	public void unbindKernelSystemEntityManagerFactory(
			PlatformTransactionManager kernelSystemTransManager, Map properties) {
		logger.debug("unbindKernelSystemEntityManagerFactory()");
		this.entityTypeContainerFactory  = null;
		this.kernelSystemEntityManagerFactory = null;
	}		
	
	public void bindEntityEditorFactory(
			IEntityEditorFactory entityEditorFactory, Map properties) {
		logger.debug("bindEntityEditorFactory()");
		this.entityEditorFactory = entityEditorFactory;
	}

	public void unbindEntityEditorFactory(
			IEntityEditorFactory entityEditorFactory, Map properties) {
		logger.debug("unbindEntityEditorFactory()");
		this.entityEditorFactory  = null;
	}	
	
	public void bindRemoteDocumentRepository(
			IRemoteDocumentRepository remoteDocumentRepository, Map properties) {
		logger.debug("bindRemoteDocumentRepository()");
		this.remoteDocumentRepository = remoteDocumentRepository;
	}

	public void unbindRemoteDocumentRepository(
			IRemoteDocumentRepository remoteDocumentRepository, Map properties) {
		logger.debug("unbindRemoteDocumentRepository()");
		this.remoteDocumentRepository  = null;
	}	
	
	public void bindFolderDAOService(
			IFolderDAOService folderDAOService, Map properties) {
		logger.debug("bindFolderDAOService()");
		this.folderDAOService = folderDAOService;
	}

	public void unbindFolderDAOService(
			IFolderDAOService folderDAOService, Map properties) {
		logger.debug("unbindFolderDAOService()");
		this.folderDAOService  = null;
	}		


	public IUIContributionManager getUiContributionManager() {
		return uiContributionManager;
	}	
	
	public EntityManagerFactory getKernelSystemEntityManagerFactory() {
		return kernelSystemEntityManagerFactory;
	}
	

	public EntityTypeContainerFactory getEntityTypeContainerFactory() {
		return entityTypeContainerFactory;
	}
	

	public IRemoteDocumentRepository getRemoteDocumentRepository() {
		return remoteDocumentRepository;
	}

	public EventBusManager getEeventBusManager() {
		return ebm;
	}
	
	public EventBus createEventBuss(Class eventBusClass, IPresenter presenter)
	{
		return ebm.register(eventBusClass, presenter);
	}
	
	public IViewContribution getViewContributionByCode(String code)
	{
		return viewContributions.get(code);
	}
	
	public IActionContribution getActionContributionByCode(String code)
	{
		return actionContributions.get(code);
	}
	
	public IApplicationViewContribution getApplicationContributionByCode(String code)
	{
		return appContributions.get(code);
	}

	public IEntityEditorFactory getEntityEditorFactory() {
		return entityEditorFactory;
	}

	public void setEntityEditorFactory(IEntityEditorFactory entityEditorFactory) {
		this.entityEditorFactory = entityEditorFactory;
	}

	public EntityManagerPerRequestHelper getEntityManagerPerRequestHelper() {
		return entityManagerPerRequestHelper;
	}

	public void setEntityManagerPerRequestHelper(
			EntityManagerPerRequestHelper entityManagerPerRequestHelper) {
		this.entityManagerPerRequestHelper = entityManagerPerRequestHelper;
	}

	public HashMap<String, Object> getEntityFactoryPresenterParams() {
		return entityFactoryPresenterParams;
	}

	public IPortalUserService getPortalUserService() {
		return portalUserService;
	}

	public void setPortalUserService(IPortalUserService portalUserService) {
		this.portalUserService = portalUserService;
	}

	public IPortalOrganizationService getPortalOrganizationService() {
		return portalOrganizationService;
	}

	public void setPortalOrganizationService(
			IPortalOrganizationService portalOrganizationService) {
		this.portalOrganizationService = portalOrganizationService;
	}

	public IPortalRoleService getPortalRoleService() {
		return portalRoleService;
	}

	public void setPortalRoleService(IPortalRoleService portalRoleService) {
		this.portalRoleService = portalRoleService;
	}
}