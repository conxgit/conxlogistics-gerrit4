package com.conx.logistics.kernel.ui.common.mvp;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.annotation.Event;

import com.conx.logistics.mdm.domain.application.Feature;
import com.conx.logistics.mdm.domain.documentlibrary.FileEntry;

public interface StartableApplicationEventBus extends EventBus {
	@Event(handlers = { MainPresenter.class })
	public void start(MainMVPApplication app);

	@Event(handlers = { MainPresenter.class })
	public void openDocument(FileEntry fileEntry);

	@Event(handlers = { MainPresenter.class })
	public void openFeatureView(Feature feature);

	@Event(handlers = { MainPresenter.class })
	public void closeFeatureView(Feature feature);
}
