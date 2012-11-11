package com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.EventBusManager;
import org.vaadin.mvp.presenter.BasePresenter;
import org.vaadin.mvp.presenter.IPresenter;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.conx.logistics.kernel.pageflow.ui.builder.VaadinPageFactoryImpl;
import com.conx.logistics.kernel.pageflow.ui.ext.mvp.IConfigurablePresenter;
import com.conx.logistics.kernel.pageflow.ui.ext.mvp.IContainerItemPresenter;
import com.conx.logistics.kernel.pageflow.ui.ext.mvp.ILocalizedEventSubscriber;
import com.conx.logistics.kernel.pageflow.ui.ext.mvp.lineeditor.section.ILineEditorSectionContentPresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.editor.multilevel.MultiLevelEditorEventBus;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.view.EntityLineEditorSectionView;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.view.IEntityLineEditorSectionView;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.LineEditorComponent;
import com.conx.logistics.kernel.ui.factory.services.IEntityEditorFactory;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.Component;

@Presenter(view = EntityLineEditorSectionView.class)
public class EntityLineEditorSectionPresenter extends BasePresenter<IEntityLineEditorSectionView, EntityLineEditorSectionEventBus> implements IConfigurablePresenter, IContainerItemPresenter {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private IPresenter<?, ? extends EventBus> headerPresenter;
	private IPresenter<?, ? extends EventBus> contentPresenter;
	private LineEditorComponent componentModel;
	private VaadinPageFactoryImpl factory;
	private EventBusManager sectionEventBusManager;

	@Override
	public void onConfigure(Map<String, Object> params) throws Exception {
		this.componentModel = (LineEditorComponent) params.get(IEntityEditorFactory.COMPONENT_MODEL);
		this.factory = (VaadinPageFactoryImpl) params.get(IEntityEditorFactory.VAADIN_COMPONENT_FACTORY);

		HashMap<String, Object> childParams = null;

		if (this.componentModel != null) {
			this.sectionEventBusManager = new EventBusManager();
			this.sectionEventBusManager.register(EntityLineEditorSectionEventBus.class, this);
			// Add the MLE Event Bus if it exists
			EventBus mleEventBus = this.factory.getPresenterFactory().getEventBusManager().getEventBus(MultiLevelEditorEventBus.class);
			if (mleEventBus != null) {
				this.sectionEventBusManager.register(MultiLevelEditorEventBus.class, mleEventBus);
			}

			childParams = new HashMap<String, Object>(params);
			this.headerPresenter = this.factory.createLineEditorSectionHeaderPresenter(this.componentModel.getContent());
			this.contentPresenter = this.factory.createLineEditorSectionContentPresenter(this.componentModel.getContent(), childParams);

			if (this.headerPresenter instanceof ILocalizedEventSubscriber) {
				((ILocalizedEventSubscriber) this.headerPresenter).subscribe(this.sectionEventBusManager);
			}
			if (this.contentPresenter instanceof ILocalizedEventSubscriber) {
				((ILocalizedEventSubscriber) this.contentPresenter).subscribe(this.sectionEventBusManager);
			}

			if (this.headerPresenter != null) {
				this.getView().setHeader((Component) this.headerPresenter.getView());
			}
			if (this.contentPresenter != null) {
				this.getView().setContent((Component) this.contentPresenter.getView());
			}
		}
	}

	@Override
	public void onSetItemDataSource(Item item, Container... containers) throws Exception {
		if (this.contentPresenter != null) {
			if (ILineEditorSectionContentPresenter.class.isAssignableFrom(this.contentPresenter.getClass())) {
				((ILineEditorSectionContentPresenter) this.contentPresenter).onSetItemDataSource(item, containers);
			}
		} else {
			throw new Exception("Could not set the item datasource. The content presenter was null.");
		}
	}
}
