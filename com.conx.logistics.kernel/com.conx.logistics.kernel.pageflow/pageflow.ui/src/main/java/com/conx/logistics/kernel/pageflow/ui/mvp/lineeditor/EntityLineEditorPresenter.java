package com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor;

import java.util.HashMap;
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
public class EntityLineEditorPresenter extends BasePresenter<IEntityLineEditorView, EntityLineEditorEventBus> implements IConfigurablePresenter, IContainerItemPresenter {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private Map<LineEditorComponent, EntityLineEditorSectionPresenter> lineEditorCache = new HashMap<LineEditorComponent, EntityLineEditorSectionPresenter>();

	@Override
	public void onConfigure(Map<String, Object> params) {
		LineEditorContainerComponent componentModel = (LineEditorContainerComponent) params.get(IEntityEditorFactory.COMPONENT_MODEL);
		VaadinPageFactoryImpl presenterFactory = (VaadinPageFactoryImpl) params.get(IEntityEditorFactory.VAADIN_COMPONENT_FACTORY);

		HashMap<String, Object> childParams = null;
		Set<LineEditorComponent> lecs = componentModel.getLineEditors();
		for (LineEditorComponent lec : lecs) {
			if (this.lineEditorCache.get(lec) == null) {
				childParams = new HashMap<String, Object>(params);
				try {
					EntityLineEditorSectionPresenter presenter = (EntityLineEditorSectionPresenter) presenterFactory.createPresenter(lec, childParams);
					this.lineEditorCache.put(lec, presenter);
					((IEntityLineEditorView) getView()).getMainLayout().addTab((Component) presenter.getView(), lec.getCaption());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onSetItemDataSource(Item item, Container...containers) throws Exception {
		Exception caughtException = null;
		Set<LineEditorComponent> lineEditorComponents = lineEditorCache.keySet();
		for (LineEditorComponent lineEditorComponent : lineEditorComponents) {
			try {
				((EntityLineEditorSectionPresenter) lineEditorCache.get(lineEditorComponent)).onSetItemDataSource(item, containers);
			} catch (Exception e) {
				caughtException = e;
			}
		}

		if (caughtException != null) {
			// throw caughtException;
		}
	}
}
