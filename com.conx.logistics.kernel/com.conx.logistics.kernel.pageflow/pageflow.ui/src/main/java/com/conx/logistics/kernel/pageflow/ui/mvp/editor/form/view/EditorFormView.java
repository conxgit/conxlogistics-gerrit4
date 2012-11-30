package com.conx.logistics.kernel.pageflow.ui.mvp.editor.form.view;

import org.vaadin.mvp.uibinder.annotation.UiField;

import com.conx.logistics.kernel.ui.forms.vaadin.impl.VaadinForm;
import com.conx.logistics.kernel.ui.forms.vaadin.listeners.IFormChangeListener;
import com.vaadin.ui.VerticalLayout;

public class EditorFormView extends VerticalLayout implements IEditorFormView {
	private static final long serialVersionUID = 1L;

	@UiField
	VerticalLayout mainLayout;

	private VaadinForm form;

	public EditorFormView() {
		setSizeFull();
	}

	@Override
	public void setForm(VaadinForm form) throws Exception {
		try {
			this.mainLayout.replaceComponent(this.form, form);
			this.form = form;
			this.mainLayout.setExpandRatio(this.form, 1.0f);
		} catch (NullPointerException e) {
			throw new Exception("The view was never bound, so the form could not be set.");
		}
	}

	@Override
	public void addListener(IFormChangeListener listener) throws Exception {
		if (this.form != null) {
			this.form.addListener(listener);
		} else {
			throw new Exception("Could not call addListener since the form was never set.");
		}
	}

	@Override
	public VaadinForm getForm() throws Exception {
		return this.form;
	}
}
