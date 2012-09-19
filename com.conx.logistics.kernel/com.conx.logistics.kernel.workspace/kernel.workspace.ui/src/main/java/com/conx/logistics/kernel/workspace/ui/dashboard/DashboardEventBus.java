package com.conx.logistics.kernel.workspace.ui.dashboard;

import org.vaadin.mvp.eventbus.annotation.Event;

import com.conx.logistics.kernel.ui.common.mvp.LaunchableViewEventBus;
import com.conx.logistics.kernel.ui.common.mvp.MainMVPApplication;
import com.conx.logistics.kernel.ui.common.mvp.MainPresenter;
import com.vaadin.ui.Window;

public interface DashboardEventBus extends LaunchableViewEventBus {
  @Event(handlers = { DashboardPresenter.class })
  public void launch(MainMVPApplication app);	

  @Event(handlers = { DashboardPresenter.class })
  public void createUser();

  @Event(handlers = { DashboardPresenter.class })
  public void removeUser();

  @Event(handlers = { MainPresenter.class })
  public void showDialog(Window dialog);

  @Event(handlers = { DashboardPresenter.class })
  public void saveUser();
  
  @Event(handlers = { DashboardPresenter.class })
  public void cancelEditUser();
}
