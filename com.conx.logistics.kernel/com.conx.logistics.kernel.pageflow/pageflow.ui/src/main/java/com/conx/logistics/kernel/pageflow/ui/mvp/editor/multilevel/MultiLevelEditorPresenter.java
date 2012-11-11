package com.conx.logistics.kernel.pageflow.ui.mvp.editor.multilevel;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.vaadin.mvp.eventbus.EventBusManager;
import org.vaadin.mvp.presenter.BasePresenter;
import org.vaadin.mvp.presenter.PresenterFactory;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.conx.logistics.kernel.pageflow.ui.builder.VaadinPageDataBuilder;
import com.conx.logistics.kernel.pageflow.ui.builder.VaadinPageFactoryImpl;
import com.conx.logistics.kernel.pageflow.ui.ext.mvp.IConfigurablePresenter;
import com.conx.logistics.kernel.pageflow.ui.ext.mvp.IContainerItemPresenter;
import com.conx.logistics.kernel.pageflow.ui.ext.mvp.IVaadinDataComponent;
import com.conx.logistics.kernel.pageflow.ui.mvp.editor.multilevel.view.IMultiLevelEditorView;
import com.conx.logistics.kernel.pageflow.ui.mvp.editor.multilevel.view.MultiLevelEditorView;
import com.conx.logistics.kernel.ui.components.domain.editor.MultiLevelEntityEditor;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.MasterDetailComponent;
import com.conx.logistics.kernel.ui.factory.services.IEntityEditorFactory;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.Component;

@Presenter(view = MultiLevelEditorView.class)
public class MultiLevelEditorPresenter extends BasePresenter<IMultiLevelEditorView, MultiLevelEditorEventBus> implements IVaadinDataComponent, IConfigurablePresenter, IContainerItemPresenter {
	private VaadinPageFactoryImpl factory;
	private MultiLevelEntityEditor componentModel;
	private Map<MasterDetailComponent, Component> editorCache;
	private Map<MasterDetailComponent, VaadinPageFactoryImpl> factoryCache;
	private Stack<MasterDetailComponent> editorStack;
	private MasterDetailComponent originEditorComponent;
	private Map<String, Object> config;

	@Override
	public void onConfigure(Map<String, Object> params) {
		this.editorCache = new HashMap<MasterDetailComponent, Component>();
		this.factoryCache = new HashMap<MasterDetailComponent, VaadinPageFactoryImpl>();
		this.editorStack = new Stack<MasterDetailComponent>();
		
		this.config = params;
		this.componentModel = (MultiLevelEntityEditor) params.get(IEntityEditorFactory.COMPONENT_MODEL);
		this.factory = (VaadinPageFactoryImpl) params.get(IEntityEditorFactory.VAADIN_COMPONENT_FACTORY);
		this.originEditorComponent = this.componentModel.getContent();

		this.getView().init();
		this.getView().setOwner(this);

		onRenderEditor(this.originEditorComponent);
	}

	private VaadinPageFactoryImpl provideLocalizedFactory(MasterDetailComponent componentModel) {
		VaadinPageFactoryImpl localizedFactory = this.factoryCache.get(componentModel);
		if (localizedFactory == null) {
			PresenterFactory externalPresenterFactory = this.factory.getPresenterFactory(), localPresenterFactory = null;
			EventBusManager localEventBusManager = new EventBusManager();
			// Register this presenter and its event bus in the localized event
			// bus manager
			localEventBusManager.register(MultiLevelEditorEventBus.class, this);
			localPresenterFactory = new PresenterFactory(localEventBusManager, externalPresenterFactory.getLocale());
			localizedFactory = new VaadinPageFactoryImpl(this.factory.getConfig(), localPresenterFactory);
			// Store this localized factory in the cache
			this.factoryCache.put(componentModel, localizedFactory);
		}
		// Return the factory directly associated with the component model
		return localizedFactory;
	}

	private Component prepareEditor(MasterDetailComponent componentModel) {
		// Ensure that the top of the stack matches the referenced
		// componentModel
		if (this.editorStack.contains(componentModel)) {
			MasterDetailComponent highestEditorComponent = this.editorStack.peek();
			while (highestEditorComponent != null && !highestEditorComponent.equals(componentModel) && !highestEditorComponent.equals(this.originEditorComponent)) {
				this.editorStack.pop();
				highestEditorComponent = this.editorStack.peek();
			}
		} else {
			this.editorStack.push(componentModel);
		}
		// Get the Vaadin Component for the referenced componentModel
		Component editorComponent = this.editorCache.get(componentModel);
		if (editorComponent == null) {
			editorComponent = provideLocalizedFactory(componentModel).create(componentModel);
			this.editorCache.put(componentModel, editorComponent);
		}
		// Update the Bread Crumb
		this.getView().updateBreadCrumb(this.editorStack.toArray(new MasterDetailComponent[] {}));

		return editorComponent;
	}

	public void onRenderEditor(MasterDetailComponent componentModel) {
		this.getView().setContent(prepareEditor(componentModel));
	}

	public void onRenderEditor(MasterDetailComponent componentModel, Item item, Container itemContainer) throws Exception {
		Component editorComponent = prepareEditor(componentModel);
		VaadinPageDataBuilder.applyItemDataSource(editorComponent, itemContainer, item, provideLocalizedFactory(componentModel).getPresenterFactory(), this.config);
		this.getView().setContent(editorComponent);
	}

	@Override
	public Object getData() {
		Component originalEditor = this.editorCache.get(this.componentModel.getContent());
		if (originalEditor != null) {
			return VaadinPageDataBuilder.buildResultData(originalEditor);
		}
		return null;
	}

	public MasterDetailComponent getCurrentEditorComponentModel() {
		return this.editorStack.peek();
	}

	@Override
	public void onSetItemDataSource(Item item, Container... containers) throws Exception {
		if (containers.length == 1) {
			MasterDetailComponent mdc = getCurrentEditorComponentModel();
			Component editorComponent = this.editorCache.get(mdc);
			VaadinPageDataBuilder.applyItemDataSource(editorComponent, containers[0], item, provideLocalizedFactory(mdc).getPresenterFactory(), this.config);
		} else {
			throw new Exception("Multi Level Editor supports one and only one container for onSetItemDataSource(Item, Container...)");
		}
	}
}