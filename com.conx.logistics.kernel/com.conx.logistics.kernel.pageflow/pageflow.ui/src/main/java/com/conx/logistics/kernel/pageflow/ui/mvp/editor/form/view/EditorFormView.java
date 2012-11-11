package com.conx.logistics.kernel.pageflow.ui.mvp.editor.form.view;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.presenter.IPresenter;
import org.vaadin.mvp.uibinder.annotation.UiField;

import com.conx.logistics.kernel.pageflow.ui.mvp.editor.form.EditorFormPresenter;
import com.conx.logistics.kernel.ui.forms.vaadin.impl.VaadinForm;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class EditorFormView extends VerticalLayout implements IEditorFormView {
	private static final long serialVersionUID = 1L;
	
	private EditorFormPresenter owner;
	private VaadinForm form;
	private Component header;
	
	@UiField
	VerticalLayout mainLayout;

	@Override
	public IPresenter<?, ? extends EventBus> getOwner() {
		return this.owner;
	}

	@Override
	public void setOwner(EditorFormPresenter owner) {
		this.owner = owner;
	}

	@Override
	public void setForm(VaadinForm form) {
		if (this.form != null) {
			this.mainLayout.removeComponent(this.form);
		}
		this.form = form;
		this.mainLayout.addComponent(this.form);
		this.mainLayout.setExpandRatio(this.form, 1.0f);
	}

	@Override
	public VaadinForm getForm() {
		return this.form;
	}

	@Override
	public void setHeader(Component header) {
		if (this.header != null) {
			this.mainLayout.removeComponent(this.header);
		}
		this.header = header;
		this.mainLayout.addComponentAsFirst(this.header);
	}

	@Override
	public Component getHeader() {
		return this.header;
	}

}
