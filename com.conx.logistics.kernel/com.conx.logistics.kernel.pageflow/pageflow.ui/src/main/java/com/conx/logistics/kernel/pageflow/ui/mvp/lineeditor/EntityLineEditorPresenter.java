package com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.presenter.BasePresenter;
import org.vaadin.mvp.presenter.IPresenter;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.conx.logistics.kernel.pageflow.ui.builder.VaadinPageFactoryImpl;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.EntityLineEditorSectionPresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.view.EntityLineEditorView;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.view.IEntityLineEditorView;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.LineEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.LineEditorContainerComponent;
import com.conx.logistics.kernel.ui.factory.services.IEntityEditorFactory;
import com.vaadin.data.Item;
import com.vaadin.ui.Component;

@Presenter(view = EntityLineEditorView.class)
public class EntityLineEditorPresenter extends BasePresenter<IEntityLineEditorView, EntityLineEditorEventBus> {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private Set<IPresenter<?, ? extends EventBus>> mvpCache = new HashSet<IPresenter<?, ? extends EventBus>>();
	private Map<IPresenter<?, ? extends EventBus>, String> linePresenter2CaptionCache = new HashMap<IPresenter<?, ? extends EventBus>, String>();

	public EntityLineEditorPresenter() {
	}

	@Override
	public void bind() {
	}

	public void onConfigure(Map<String, Object> params) {
		LineEditorContainerComponent componentModel = (LineEditorContainerComponent) params.get(IEntityEditorFactory.COMPONENT_MODEL);
		VaadinPageFactoryImpl presenterFactory = (VaadinPageFactoryImpl) params.get(IEntityEditorFactory.VAADIN_COMPONENT_FACTORY);

		HashMap<String, Object> childParams = null;
		
		Set<LineEditorComponent> lecs = componentModel.getLineEditors();
		for (LineEditorComponent lec : lecs) {
			childParams = new HashMap<String, Object>(params);
			EntityLineEditorSectionPresenter presenter = (EntityLineEditorSectionPresenter) presenterFactory.createPresenter(lec, childParams);
			linePresenter2CaptionCache.put(presenter, lec.getCaption());
			mvpCache.add(presenter);
		}
		
		String caption = null;
		for (IPresenter<?, ? extends EventBus> presenter : mvpCache) {
			caption = linePresenter2CaptionCache.get(presenter);
			((IEntityLineEditorView) getView()).getMainLayout().addTab((Component) presenter.getView(), caption);
		}
	}

	public void onSetItemDataSource(Item item) {
		if (item != null) {
			for (IPresenter<?, ? extends EventBus> presenter : mvpCache) {
				((EntityLineEditorSectionPresenter) presenter).onSetItemDataSource(item);
			}
		}
	}
}
