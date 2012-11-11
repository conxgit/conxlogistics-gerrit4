package com.conx.logistics.kernel.pageflow.ui.mvp.editor.multilevel.view;

import org.vaadin.mvp.uibinder.IUiBindable;

import com.conx.logistics.kernel.pageflow.ui.ext.mvp.IVaadinPageComponentView;
import com.conx.logistics.kernel.pageflow.ui.mvp.editor.multilevel.MultiLevelEditorPresenter;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.MasterDetailComponent;
import com.vaadin.ui.Component;

public interface IMultiLevelEditorView extends IUiBindable, IVaadinPageComponentView {
	public void setContent(Component component);
	public Component getContent();
	public void setOwner(MultiLevelEditorPresenter presenter);
	public void updateBreadCrumb(MasterDetailComponent[] componentModelEnum);
	public void init();
}
