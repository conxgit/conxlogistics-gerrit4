package com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.presenter.BasePresenter;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.conx.logistics.kernel.pageflow.ui.builder.VaadinPageFactoryImpl;
import com.conx.logistics.kernel.pageflow.ui.ext.mvp.IConfigurablePresenter;
import com.conx.logistics.kernel.pageflow.ui.ext.mvp.IContainerItemPresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.EntityLineEditorSectionPresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.view.EntityLineEditorView;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.view.IEntityLineEditorView;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.LineEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.LineEditorContainerComponent;
import com.conx.logistics.kernel.ui.factory.services.IEntityEditorFactory;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.Component;

@Presenter(view = EntityLineEditorView.class)
public class EntityLineEditorPresenter extends BasePresenter<IEntityLineEditorView, EntityLineEditorEventBus> implements IConfigurablePresenter,
		IContainerItemPresenter {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private Map<LineEditorComponent, EntityLineEditorSectionPresenter> lineEditorSectionCache;
	private List<LineEditorComponent> lineEditorSectionOrdinalCache;

	private VaadinPageFactoryImpl presenterFactory;
	private LineEditorContainerComponent componentModel;
	private Map<String, Object> config;

	@Override
	public void onConfigure(Map<String, Object> params) {
		this.componentModel = (LineEditorContainerComponent) params.get(IEntityEditorFactory.COMPONENT_MODEL);
		this.presenterFactory = (VaadinPageFactoryImpl) params.get(IEntityEditorFactory.VAADIN_COMPONENT_FACTORY);
		this.config = params;
		this.lineEditorSectionCache = new HashMap<LineEditorComponent, EntityLineEditorSectionPresenter>();
		this.lineEditorSectionOrdinalCache = new LinkedList<LineEditorComponent>();

		this.getView().init();
	}

	@Override
	public void onSetItemDataSource(Item item, Container... containers) throws Exception {
		Set<LineEditorComponent> lecs = null;

		if (this.lineEditorSectionOrdinalCache.size() == 0) {
			lecs = this.componentModel.getLineEditors();

			LineEditorComponent currentOrderedLineEditorSectionComponent = null;
			for (LineEditorComponent lineEditorSectionComponent : lecs) {
				if (this.lineEditorSectionCache.get(lineEditorSectionComponent) == null) {
					if (this.lineEditorSectionOrdinalCache.size() > 0) {
						boolean isLineEditorAdded = false;
						for (int i = 0; i < this.lineEditorSectionOrdinalCache.size(); i++) {
							currentOrderedLineEditorSectionComponent = this.lineEditorSectionOrdinalCache.get(i);
							if (lineEditorSectionComponent.getOrdinal() < currentOrderedLineEditorSectionComponent.getOrdinal()) {
								this.lineEditorSectionOrdinalCache.add(i, lineEditorSectionComponent);
								isLineEditorAdded = true;
								break;
							}
						}
						
						if (!isLineEditorAdded) {
							this.lineEditorSectionOrdinalCache.add(lineEditorSectionComponent);
						}
					} else {
						this.lineEditorSectionOrdinalCache.add(0, lineEditorSectionComponent);
					}
					
					this.lineEditorSectionCache.put(lineEditorSectionComponent, (EntityLineEditorSectionPresenter) presenterFactory.createPresenter(lineEditorSectionComponent, this.config));
				}
			}
			
			EntityLineEditorSectionPresenter presenter = null;
			for (LineEditorComponent orderedLineEditorComponent : this.lineEditorSectionOrdinalCache) {
				presenter = this.lineEditorSectionCache.get(orderedLineEditorComponent);
				((IEntityLineEditorView) getView()).addTab((Component) presenter.getView(), orderedLineEditorComponent.getCaption());
			}
		}

		Exception lastCaughtException = null;
		Set<LineEditorComponent> lineEditorComponents = this.lineEditorSectionCache.keySet();
		for (LineEditorComponent lineEditorComponent : lineEditorComponents) {
			try {
				((EntityLineEditorSectionPresenter) this.lineEditorSectionCache.get(lineEditorComponent)).onSetItemDataSource(item, containers);
			} catch (Exception e) {
				lastCaughtException = e;
				e.printStackTrace();
			}
		}

		if (lastCaughtException != null) {
			throw lastCaughtException;
		}
	}
}
