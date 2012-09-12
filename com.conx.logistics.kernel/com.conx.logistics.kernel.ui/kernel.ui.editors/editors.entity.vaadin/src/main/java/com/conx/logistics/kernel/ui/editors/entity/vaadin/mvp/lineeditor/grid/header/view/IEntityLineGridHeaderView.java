package com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.lineeditor.grid.header.view;

import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.IEntityEditorComponentView;
import com.vaadin.ui.Button.ClickListener;

public interface IEntityLineGridHeaderView  extends IEntityEditorComponentView {
	public void addCreateListener(ClickListener listener);
	public void setCreateEnabled(boolean isEnabled);
	public boolean isCreateEnabled();
	public void addEditListener(ClickListener listener);
	public void setEditEnabled(boolean isEnabled);
	public boolean isEditEnabled();
	public void addDeleteListener(ClickListener listener);
	public void setDeleteEnabled(boolean isEnabled);
	public boolean isDeleteEnabled();
	public void addPrintListener(ClickListener listener);
	public void setPrintEnabled(boolean isEnabled);
	public boolean isPrintEnabled();
}