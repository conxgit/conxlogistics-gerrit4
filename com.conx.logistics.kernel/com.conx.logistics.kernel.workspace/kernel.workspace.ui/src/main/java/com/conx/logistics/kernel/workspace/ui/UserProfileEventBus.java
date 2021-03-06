package com.conx.logistics.kernel.workspace.ui;

import org.vaadin.mvp.eventbus.annotation.Event;

import com.conx.logistics.kernel.ui.common.mvp.LaunchableViewEventBus;
import com.conx.logistics.kernel.ui.common.mvp.MainMVPApplication;
import com.conx.logistics.kernel.ui.common.mvp.MainPresenter;
import com.vaadin.ui.Window;

public interface UserProfileEventBus extends LaunchableViewEventBus {
  @Event(handlers = { UserProfilePresenter.class })
  public void launch(MainMVPApplication app);	

  @Event(handlers = { UserProfilePresenter.class })
  public void createUser();

  @Event(handlers = { UserProfilePresenter.class })
  public void removeUser();

  @Event(handlers = { MainPresenter.class })
  public void showDialog(Window dialog);

  @Event(handlers = { UserProfilePresenter.class })
  public void saveUser();
  
  @Event(handlers = { UserProfilePresenter.class })
  public void cancelEditUser();

}
