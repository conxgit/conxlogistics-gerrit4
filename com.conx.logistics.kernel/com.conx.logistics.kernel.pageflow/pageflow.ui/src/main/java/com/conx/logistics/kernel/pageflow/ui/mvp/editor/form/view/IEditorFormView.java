package com.conx.logistics.kernel.pageflow.ui.mvp.editor.form.view;

import org.vaadin.mvp.uibinder.IUiBindable;

import com.conx.logistics.kernel.pageflow.ui.ext.mvp.IVaadinPageComponentView;
import com.conx.logistics.kernel.pageflow.ui.mvp.editor.form.EditorFormPresenter;
import com.conx.logistics.kernel.ui.forms.vaadin.impl.VaadinForm;
import com.vaadin.ui.Component;

public interface IEditorFormView extends IUiBindable, IVaadinPageComponentView {
	public void setOwner(EditorFormPresenter owner);
	public void setForm(VaadinForm form);
	public VaadinForm getForm();
	public void setHeader(Component header);
	public Component getHeader();
}
