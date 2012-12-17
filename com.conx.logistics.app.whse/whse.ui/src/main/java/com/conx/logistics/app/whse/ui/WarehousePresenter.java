package com.conx.logistics.app.whse.ui;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.presenter.BasePresenter;
import org.vaadin.mvp.presenter.IPresenter;
import org.vaadin.mvp.presenter.IPresenterFactory;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.conx.logistics.app.whse.ui.navigation.WarehouseNavigationPresenter;
import com.conx.logistics.app.whse.ui.navigation.view.IWarehouseNavigationView;
import com.conx.logistics.app.whse.ui.view.IWarehouseView;
import com.conx.logistics.app.whse.ui.view.WarehouseView;
import com.conx.logistics.kernel.ui.common.mvp.MainMVPApplication;
import com.conx.logistics.kernel.ui.common.mvp.view.feature.FeatureTabbedView;
import com.conx.logistics.mdm.domain.application.DocViewFeature;
import com.conx.logistics.mdm.domain.application.Feature;
import com.conx.logistics.mdm.domain.application.ReportViewFeature;
import com.conx.logistics.mdm.domain.documentlibrary.FileEntry;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@Presenter(view = WarehouseView.class)
public class WarehousePresenter extends BasePresenter<IWarehouseView, WarehouseEventBus> {

	private MainMVPApplication application;
	private FeatureTabbedView fv;

	private WarehouseNavigationPresenter navPresenter;

	private IPresenter<?, ? extends EventBus> contentPresenter;

	public void onStart(MainMVPApplication app) {
		// keep a reference to the application instance
		this.application = app;

		// set the applications main windows (the view)
		// this.application.setMainWindow((Window) this.view);

		// load the nav presenter
		IPresenterFactory pf = application.getPresenterFactory();
		this.navPresenter = (WarehouseNavigationPresenter) pf.createPresenter(WarehouseNavigationPresenter.class);
		this.navPresenter.getEventBus().start(this.application);

		IWarehouseNavigationView navView = this.navPresenter.getView();
		this.view.setNavigation(navView);

		// load fv
		fv = new FeatureTabbedView(this.application, this);
		this.view.setContent(this.fv);
	}

	public void onOpenModule(Class<? extends BasePresenter<?, ? extends EventBus>> presenter) {
		// load the menu presenter
		IPresenterFactory pf = application.getPresenterFactory();
		this.contentPresenter = pf.createPresenter(presenter);
		this.view.setContent((Component) this.contentPresenter.getView());
	}

	public void onOpenFeatureView(Feature feature) {
		fv.setFeature(feature);
	}

	public void onCloseFeatureView(Feature feature) {
		fv.clearFeature(feature);
	}

	public void onShowDialog(Window dialog) {
		this.application.getMainWindow().addWindow(dialog);
	}

	public void onOpenDocument(FileEntry fileEntry) {
		fv.setFeature(new DocViewFeature(fileEntry));
	}
	
	public void onOpenDocument(String url, String caption) {
		fv.setFeature(new ReportViewFeature(url, caption));
	}

	@Override
	public void bind() {
		VerticalLayout mainLayout = this.view.getMainLayout();
		HorizontalSplitPanel layoutPanel = this.view.getSplitLayout();
		layoutPanel.setStyleName("main-split");
		layoutPanel.setSizeFull();
		mainLayout.setSizeFull();
		mainLayout.setExpandRatio(layoutPanel, 1.0f);
		layoutPanel.setSplitPosition(200, HorizontalSplitPanel.UNITS_PIXELS);
	}
}
