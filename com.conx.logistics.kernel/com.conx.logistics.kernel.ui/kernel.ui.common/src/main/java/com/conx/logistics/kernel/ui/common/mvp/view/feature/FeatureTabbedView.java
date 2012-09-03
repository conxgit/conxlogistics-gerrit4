package com.conx.logistics.kernel.ui.common.mvp.view.feature;

import java.util.HashMap;
import java.util.Map;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.presenter.IPresenter;
import org.vaadin.mvp.presenter.IPresenterFactory;

import com.conx.logistics.common.utils.Validator;
import com.conx.logistics.kernel.ui.common.mvp.LaunchableViewEventBus;
import com.conx.logistics.kernel.ui.common.mvp.MainMVPApplication;
import com.conx.logistics.kernel.ui.common.mvp.docviewer.DocViewerPresenter;
import com.conx.logistics.kernel.ui.components.domain.AbstractConXComponent;
import com.conx.logistics.kernel.ui.factory.services.IEntityEditorFactory;
import com.conx.logistics.kernel.ui.service.contribution.ITaskActionContribution;
import com.conx.logistics.kernel.ui.service.contribution.IViewContribution;
import com.conx.logistics.mdm.domain.application.DocViewFeature;
import com.conx.logistics.mdm.domain.application.Feature;
import com.conx.logistics.mdm.domain.documentlibrary.FileEntry;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;

public class FeatureTabbedView extends TabSheet implements IFeatureView {
	private static final long serialVersionUID = 1987628349762L;

	private HashMap<Feature, Component> viewCache;
	private Feature currentFeature;
	private MainMVPApplication app;
	private IPresenter<?, ? extends EventBus> viewPresenter;
	private IEntityEditorFactory entityEditorFactory;

	private HashMap<String, Object> entityFactoryPresenterParams;
	
	public FeatureTabbedView(MainMVPApplication app, IPresenter<?, ? extends EventBus> viewPresenter) {
		this.app = app;
		this.viewPresenter = viewPresenter;
		this.entityEditorFactory = app.getEntityEditorFactory();
		this.viewCache = new HashMap<Feature, Component>();
		
		this.entityFactoryPresenterParams = new HashMap<String, Object>();
		this.entityFactoryPresenterParams.putAll(app.getEntityFactoryPresenterParams());
		
		setSizeFull();
		setStyleName("conx-entity-editor-detail-tabsheet");
	}

	public void setFeature(Feature feature) {
		if (feature != currentFeature) {
			if (Validator.isNotNull(feature.getCode())) {
				currentFeature = feature;
				Component componentFeature = getComponentFor(feature);
				if (getTab(componentFeature) != null) {
					this.setSelectedTab(componentFeature);
				} else {
					addTab(componentFeature, feature.getName());
				}
			}
			else if (feature instanceof DocViewFeature)
			{
				currentFeature = feature;
				IPresenterFactory pf = this.app.getPresenterFactory();
				DocViewerPresenter viewPresenter = (DocViewerPresenter)pf.createPresenter(DocViewerPresenter.class);
				FileEntry fe = ((DocViewFeature)feature).getFileEntry();
				viewPresenter.getEventBus().viewDocument(fe);

				Component view = (Component)viewPresenter.getView();				
				if (getTab(view) != null) {
					this.setSelectedTab(view);
				} else {
					addTab(view, feature.getName());
				}				
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Component getComponentFor(Feature feature) {
		Component view = null;
		if (feature.isTaskFeature()) {
			// IUIContributionManager cm = this.app.getUiContributionManager();
			String viewCode = feature.getExternalCode();
			String processId = feature.getCode();

			ITaskActionContribution ac = (ITaskActionContribution) app.getActionContributionByCode(viewCode);

			if (viewCode != null && processId != null && ac != null) {
				Map<String, Object> props = new HashMap<String, Object>();
				props.put("userId", "john");
				props.put("processId", processId);
				props.put("onCompletionFeature", feature.getOnCompletionFeature());
				props.put("onCompletionViewPresenter", this.viewPresenter);

				try {
					view = ac.execute(this.app, props);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (Validator.isNotNull(feature.getCode())) {
			view = viewCache.get(feature);
			if (view == null) {
				// IUIContributionManager cm =
				// this.app.getUiContributionManager();
				String viewCode = feature.getCode();
				IViewContribution vc = app.getViewContributionByCode(viewCode);
				if (vc != null) {
					try {
						AbstractConXComponent componentModel = vc.getComponentModel(this.app, feature);
						if (Validator.isNotNull(componentModel) && Validator.isNotNull(entityEditorFactory))
						{
							this.entityFactoryPresenterParams.put(IEntityEditorFactory.FACTORY_PARAM_MVP_CURRENT_APP_PRESENTER, this.viewPresenter);
							Map<IPresenter<?, ? extends EventBus>, EventBus> mvp = (Map<IPresenter<?, ? extends EventBus>, EventBus>) entityEditorFactory.create(componentModel,this.entityFactoryPresenterParams);
							IPresenter<?, ? extends EventBus> viewPresenter = mvp.keySet().iterator().next();
							// ((LaunchableViewEventBus)viewPresenter.getEventBus()).launch(this.app);
							view = (Component) viewPresenter.getView();
						}
						else 
						{
							IPresenterFactory pf = this.app.getPresenterFactory();
							IPresenter<?, ? extends EventBus> viewPresenter = pf.createPresenter(vc.getPresenterClass());					
							((LaunchableViewEventBus)viewPresenter.getEventBus()).launch(this.app);

							view = (Component)viewPresenter.getView();
						}
						view.setSizeFull();
					} catch (Exception e) {
						e.printStackTrace();
					}
					viewCache.put(feature, view);
				}
			}

		}
		return view;
	}

	public IEntityEditorFactory getEntityEditorFactory() {
		return entityEditorFactory;
	}

	public void setEntityEditorFactory(IEntityEditorFactory entityEditorFactory) {
		this.entityEditorFactory = entityEditorFactory;
	}

}
