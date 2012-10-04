package com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.grid.header;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.presenter.BasePresenter;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.grid.header.view.EntityLineEditorGridHeaderView;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.grid.header.view.IEntityLineEditorGridHeaderView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@Presenter(view = EntityLineEditorGridHeaderView.class)
public class EntityLineEditorGridHeaderPresenter extends BasePresenter<IEntityLineEditorGridHeaderView, EntityLineEditorGridHeaderEventBus> {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private boolean initialized = false;

	@Override
	public void bind() {
		this.setInitialized(true);
		this.getView().init();
		this.getView().addCreateListener(new ClickListener() {
			private static final long serialVersionUID = -54023801L;

			@Override
			public void buttonClick(ClickEvent event) {
			}
		});
		this.getView().addEditListener(new ClickListener() {
			private static final long serialVersionUID = -52023801L;

			@Override
			public void buttonClick(ClickEvent event) {
			}
		});
		this.getView().addDeleteListener(new ClickListener() {
			private static final long serialVersionUID = -50023801L;

			@Override
			public void buttonClick(ClickEvent event) {
			}
		});
		this.getView().addPrintListener(new ClickListener() {
			private static final long serialVersionUID = -99023801L;

			@Override
			public void buttonClick(ClickEvent event) {
			}
		});
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public void onConfigure(Map<String, Object> params) {
	}
}
