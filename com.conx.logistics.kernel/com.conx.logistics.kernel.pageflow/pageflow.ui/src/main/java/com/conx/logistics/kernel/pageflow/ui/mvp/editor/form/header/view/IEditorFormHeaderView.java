package com.conx.logistics.kernel.pageflow.ui.mvp.editor.form.header.view;

import org.vaadin.mvp.uibinder.IUiBindable;

import com.vaadin.ui.Button.ClickListener;

public interface IEditorFormHeaderView  extends IUiBindable {
	public void addVerifyListener(ClickListener listener);
	public void setVerifyEnabled(boolean isEnabled);
	public boolean isVerifyEnabled();
	public void addEditListener(ClickListener listener);
	public void setEditEnabled(boolean isEnabled);
	public boolean isEditEnabled();
	public void addResetListener(ClickListener listener);
	public void setResetEnabled(boolean isEnabled);
	public boolean isResetEnabled();
	public void init();
}