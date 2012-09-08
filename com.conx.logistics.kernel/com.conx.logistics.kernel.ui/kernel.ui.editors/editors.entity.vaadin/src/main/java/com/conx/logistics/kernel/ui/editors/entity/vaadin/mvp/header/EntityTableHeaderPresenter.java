package com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.header;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.ConfigurableBasePresenter;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.MultiLevelEntityEditorPresenter;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.header.view.EntityTableHeaderView;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.header.view.IEntityTableHeaderView;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@Presenter(view = EntityTableHeaderView.class)
public class EntityTableHeaderPresenter extends ConfigurableBasePresenter<IEntityTableHeaderView, EntityTableHeaderEventBus>
		implements Property.ValueChangeListener {
	private static final long serialVersionUID = 1L;

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private boolean initialized = false;
	private MultiLevelEntityEditorPresenter parentPresenter;

	public EntityTableHeaderPresenter() {
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
		try {
			this.setInitialized(true);
			this.getView().init();
			
			this.getView().getPrintButton().addListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					EntityTableHeaderPresenter.this.getEventBus().printClicked();
				}
			});
			
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);
		}		
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	@Override
	public void configure() {
	}
}
