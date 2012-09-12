package com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.lineeditor.grid.header;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.ConfigurableBasePresenter;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.MultiLevelEntityEditorEventBus;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.MultiLevelEntityEditorPresenter;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.lineeditor.grid.header.view.EntityLineGridHeaderView;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.lineeditor.grid.header.view.IEntityLineGridHeaderView;
import com.conx.logistics.kernel.ui.factory.services.IEntityEditorFactory;
import com.conx.logistics.kernel.ui.service.contribution.IMainApplication;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@Presenter(view = EntityLineGridHeaderView.class)
public class EntityLineGridHeaderPresenter extends ConfigurableBasePresenter<IEntityLineGridHeaderView, EntityLineGridHeaderEventBus> {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private boolean initialized = false;
	private MultiLevelEntityEditorPresenter parentPresenter;

	private MultiLevelEntityEditorPresenter multiLevelEntityEditorPresenter;
	private IMainApplication mainApplication;
	private MultiLevelEntityEditorEventBus entityEditorEventListener;

	public EntityLineGridHeaderPresenter() {
		super();
	}

	public MultiLevelEntityEditorPresenter getParentPresenter() {
		return parentPresenter;
	}

	public void setParentPresenter(MultiLevelEntityEditorPresenter parentPresenter) {
		this.parentPresenter = parentPresenter;
	}

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

	@Override
	public void configure() {
		this.multiLevelEntityEditorPresenter = (MultiLevelEntityEditorPresenter) getConfig().get(IEntityEditorFactory.FACTORY_PARAM_MVP_CURRENT_MLENTITY_EDITOR_PRESENTER);
		this.mainApplication = (IMainApplication) getConfig().get(IEntityEditorFactory.FACTORY_PARAM_MAIN_APP);
		this.entityEditorEventListener = multiLevelEntityEditorPresenter.getEventBus();
	}
}
