package com.conx.logistics.kernel.pageflow.ui.mvp.editor.form.header;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.presenter.BasePresenter;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.conx.logistics.kernel.pageflow.ui.mvp.editor.form.EditorFormPresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.editor.form.header.view.EditorFormHeaderView;
import com.conx.logistics.kernel.pageflow.ui.mvp.editor.form.header.view.IEditorFormHeaderView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@Presenter(view = EditorFormHeaderView.class)
public class EditorFormHeaderPresenter extends BasePresenter<IEditorFormHeaderView, EditorFormHeaderEventBus> {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private boolean initialized = false;
//	private EditorFormPresenter gridPresenter;

	@Override
	public void bind() {
		this.setInitialized(true);
		this.getView().init();
		this.getView().addVerifyListener(new ClickListener() {
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
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}
	
	public void attachGridPresenter(EditorFormPresenter gridPresenter) {
//		this.gridPresenter = gridPresenter;
	}

	public void onConfigure(Map<String, Object> params) {
	}
}
