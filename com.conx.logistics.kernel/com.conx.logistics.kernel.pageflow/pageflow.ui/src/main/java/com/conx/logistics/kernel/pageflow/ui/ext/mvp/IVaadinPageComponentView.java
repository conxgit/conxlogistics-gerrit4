package com.conx.logistics.kernel.pageflow.ui.ext.mvp;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.presenter.IPresenter;

public interface IVaadinPageComponentView {
	public IPresenter<?, ? extends EventBus> getOwner();
}
