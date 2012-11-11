package com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.form.view;

import org.vaadin.mvp.uibinder.IUiBindable;

import com.conx.logistics.kernel.ui.forms.vaadin.impl.VaadinForm;
import com.conx.logistics.kernel.ui.forms.vaadin.listeners.IFormChangeListener;

public interface IEntityLineEditorFormView extends IUiBindable {
	public void setForm(VaadinForm form) throws Exception;
	public VaadinForm getForm() throws Exception;
	public void addListener(IFormChangeListener listener) throws Exception;
}