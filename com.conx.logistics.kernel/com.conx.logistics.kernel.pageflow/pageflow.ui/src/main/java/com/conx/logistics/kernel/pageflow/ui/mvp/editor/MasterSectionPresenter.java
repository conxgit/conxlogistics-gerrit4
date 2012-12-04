package com.conx.logistics.kernel.pageflow.ui.mvp.editor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.EventBusManager;
import org.vaadin.mvp.presenter.BasePresenter;
import org.vaadin.mvp.presenter.IPresenter;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.conx.logistics.kernel.pageflow.ui.builder.VaadinPageDataBuilder;
import com.conx.logistics.kernel.pageflow.ui.builder.VaadinPageFactoryImpl;
import com.conx.logistics.kernel.pageflow.ui.ext.mvp.IConfigurablePresenter;
import com.conx.logistics.kernel.pageflow.ui.ext.mvp.IContainerItemPresenter;
import com.conx.logistics.kernel.pageflow.ui.ext.mvp.ILocalizedEventSubscriber;
import com.conx.logistics.kernel.pageflow.ui.mvp.editor.multilevel.MultiLevelEditorEventBus;
import com.conx.logistics.kernel.pageflow.ui.mvp.editor.view.IMasterSectionView;
import com.conx.logistics.kernel.pageflow.ui.mvp.editor.view.MasterSectionView;
import com.conx.logistics.kernel.ui.components.domain.AbstractConXComponent;
import com.conx.logistics.kernel.ui.factory.services.IEntityEditorFactory;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.Component;

/**
 * This presenter is responsible for the primary (top) section of a Master
 * Detail Layout. It manages its header and content presenters and uses the
 * factory to create them.
 * 
 * @author Sandile
 */
@Presenter(view = MasterSectionView.class)
public class MasterSectionPresenter extends
		BasePresenter<IMasterSectionView, MasterSectionEventBus> implements
		IConfigurablePresenter, IContainerItemPresenter {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private IPresenter<?, ? extends EventBus> headerPresenter;
	private IPresenter<?, ? extends EventBus> contentPresenter;
	private AbstractConXComponent componentModel;
	private VaadinPageFactoryImpl factory;
	private EventBusManager sectionEventBusManager;
	private Map<String, Object> config;

	@Override
	public void onConfigure(Map<String, Object> params) throws Exception {
		this.config = params;
		this.componentModel = (AbstractConXComponent) params
				.get(IEntityEditorFactory.COMPONENT_MODEL);
		this.factory = (VaadinPageFactoryImpl) params
				.get(IEntityEditorFactory.VAADIN_COMPONENT_FACTORY);
		// This map is used for shallow copying the parameters for child
		// components
		HashMap<String, Object> childParams = null;

		if (this.componentModel != null) {
			// A private ebm that creates a walled garden for events fired by
			// children
			this.sectionEventBusManager = new EventBusManager();
			this.sectionEventBusManager.register(MasterSectionEventBus.class,
					this);
			// Add the MLE Event Bus if it exists, we need it to switch editors
			EventBus mleEventBus = this.factory.getPresenterFactory()
					.getEventBusManager()
					.getEventBus(MultiLevelEditorEventBus.class);
			if (mleEventBus != null) {
				this.sectionEventBusManager.register(
						MultiLevelEditorEventBus.class, mleEventBus);
			}
			// Create the header and content presenter
			childParams = new HashMap<String, Object>(params);
			this.headerPresenter = this.factory
					.createMasterSectionHeaderPresenter(this.componentModel);
			this.contentPresenter = this.factory
					.createMasterSectionContentPresenter(this.componentModel,
							childParams);
			// If the header and content presenter have support for subscribing
			// to our private ebm, make sure they do
			if (this.headerPresenter instanceof ILocalizedEventSubscriber) {
				((ILocalizedEventSubscriber) this.headerPresenter)
						.subscribe(this.sectionEventBusManager);
			}
			if (this.contentPresenter instanceof ILocalizedEventSubscriber) {
				((ILocalizedEventSubscriber) this.contentPresenter)
						.subscribe(this.sectionEventBusManager);
			}
			// Provided they aren't respectively null, add our header and
			// content to our view
			if (this.headerPresenter != null) {
				this.getView().setHeader(
						(Component) this.headerPresenter.getView());
			}
			if (this.contentPresenter != null) {
				this.getView().setContent(
						(Component) this.contentPresenter.getView());
			} else {
				this.getView().setContent(
						this.factory.createComponent(this.componentModel));
			}
		}
	}

	@Override
	public void onSetItemDataSource(Item item, Container... containers)
			throws Exception {
		if (this.contentPresenter != null) {
			// If we can set the item datasource of the content presenter, lets
			// make it happen
			if (IContainerItemPresenter.class
					.isAssignableFrom(this.contentPresenter.getClass())) {
				((IContainerItemPresenter) this.contentPresenter)
						.onSetItemDataSource(item, containers);
			}
		} else {
			if (this.getView().getContent() != null) {
				if (containers.length == 1) {
					VaadinPageDataBuilder.applyItemDataSource(this.getView().getContent(), containers[0], item, this.factory.getPresenterFactory(), this.config);
				} else {
					throw new Exception("One container is needed to set item datasource with a content presenter.");
				}
			} else {
				throw new Exception(
						"Could not set the item datasource. The content presenter was null.");
			}
		}
	}
}
