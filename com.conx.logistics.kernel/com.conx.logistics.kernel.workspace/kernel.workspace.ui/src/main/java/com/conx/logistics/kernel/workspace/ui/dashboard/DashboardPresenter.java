package com.conx.logistics.kernel.workspace.ui.dashboard;

import org.vaadin.mvp.presenter.FactoryPresenter;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.conx.logistics.kernel.ui.common.mvp.MainMVPApplication;
import com.conx.logistics.kernel.workspace.ui.dashboard.view.DashboardView;
import com.conx.logistics.kernel.workspace.ui.dashboard.view.IDashboardView;

@Presenter(view = DashboardView.class)
public class DashboardPresenter extends FactoryPresenter<IDashboardView, DashboardEventBus> {

  @Override
  public void bind() {
	  getView().init();
	  //((DashboardView)getView()).getMyTasksTable()
  }
  
  public void onLaunch(MainMVPApplication mainApp)
  {
	  
  }
}
