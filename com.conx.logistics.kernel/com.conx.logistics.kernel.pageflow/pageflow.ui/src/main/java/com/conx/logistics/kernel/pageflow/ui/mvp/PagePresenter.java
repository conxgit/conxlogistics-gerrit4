package com.conx.logistics.kernel.pageflow.ui.mvp;

import java.util.Map;

import org.springframework.transaction.PlatformTransactionManager;
import org.vaadin.mvp.presenter.BasePresenter;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.conx.logistics.kernel.pageflow.event.IPageFlowPageChangedEventHandler;
import com.conx.logistics.kernel.pageflow.services.IPageComponent;
import com.conx.logistics.kernel.pageflow.services.IPageFlowPage;
import com.conx.logistics.kernel.pageflow.services.ITaskWizard;
import com.conx.logistics.kernel.pageflow.ui.mvp.view.IPageView;
import com.conx.logistics.kernel.pageflow.ui.mvp.view.PageView;
import com.conx.logistics.kernel.persistence.services.IEntityContainerProvider;
import com.vaadin.ui.Component;

@Presenter(view = PageView.class)
public class PagePresenter extends BasePresenter<IPageView, PageEventBus> implements IPageComponent {
	private boolean executed;
	private IPageFlowPage page;
	private IPageDataHandler dataHandler;

	private PlatformTransactionManager ptm;
	private IPageFlowPageChangedEventHandler pfpEventHandler;
	private ITaskWizard wizard;
	private IEntityContainerProvider containerProvider;
	private Map<String, Object> parameterData;
	
	public void setPageContent(Component content) {
		this.getView().setContent(content);
	}
	
	public Component getPageContent() {
		return this.getView().getContent();
	}

	public void setPage(IPageFlowPage page) {
		this.page = page;
	}

	public IPageFlowPage getPage() {
		return this.page;
	}

	@Override
	public String getCaption() {
		if (this.page != null) {
			return this.page.getTaskName();
		}
		return null;
	}

	@Override
	public Component getContent() {
		return (Component) this.getView();
	}

	@Override
	public boolean onAdvance() {
		return true;
	}

	@Override
	public boolean onBack() {
		return true;
	}

	@Override
	public void init(Map<String, Object> initParams) {
		this.ptm = (PlatformTransactionManager) initParams.get(IPageComponent.JTA_GLOBAL_TRANSACTION_MANAGER);
		this.pfpEventHandler = (IPageFlowPageChangedEventHandler) initParams.get(IPageComponent.PAGE_FLOW_PAGE_CHANGE_EVENT_HANDLER);
		this.wizard = (ITaskWizard) initParams.get(IPageComponent.TASK_WIZARD);
		this.containerProvider = (IEntityContainerProvider) initParams.get(IPageComponent.ENTITY_CONTAINER_PROVIDER);
	}

	@Override
	public void setParameterData(Map<String, Object> params) {
		this.parameterData = params;
		if (this.dataHandler != null) {
			this.dataHandler.setParameterData(this, params);
		}
	}

	public Map<String, Object> getParameterData() {
		return this.parameterData;
	}

	@Override
	public Map<String, Object> getResultData() {
		if (this.dataHandler != null) {
			return this.dataHandler.getResultData(this);
		} else {
			return null;
		}
	}

	@Override
	public boolean isExecuted() {
		return this.executed;
	}

	@Override
	public void setExecuted(boolean executed) {
		this.executed = executed;
	}

	public PlatformTransactionManager getPlatformTransactionManager() {
		return ptm;
	}

	public IPageFlowPageChangedEventHandler getPageFlowPageEventHandler() {
		return pfpEventHandler;
	}

	public ITaskWizard getWizard() {
		return wizard;
	}

	public IEntityContainerProvider getContainerProvider() {
		return containerProvider;
	}

	public IPageDataHandler getDataHandler() {
		return dataHandler;
	}

	public void setDataHandler(IPageDataHandler dataHandler) {
		this.dataHandler = dataHandler;
	}
}
