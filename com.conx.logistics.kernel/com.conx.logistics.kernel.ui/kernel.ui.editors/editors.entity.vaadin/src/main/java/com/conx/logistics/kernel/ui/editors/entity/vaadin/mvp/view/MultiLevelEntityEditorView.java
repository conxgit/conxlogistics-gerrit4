package com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.view;

import org.vaadin.mvp.uibinder.annotation.UiField;

import com.conx.logistics.kernel.ui.vaadin.common.ConXAbstractSplitPanel.ISplitPositionChangeListener;
import com.conx.logistics.kernel.ui.vaadin.common.ConXVerticalSplitPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class MultiLevelEntityEditorView extends VerticalLayout implements IMultiLevelEntityEditorView {
	private static final long serialVersionUID = 1L;
	
	@UiField
	VerticalLayout mainLayout;

	private Component breadCrumb; // Index 0
	private Component header; // Index 1
	private ConXVerticalSplitPanel splitPanel; // Index 2
	private Component footer; // Index 3

	public MultiLevelEntityEditorView() {
		setSizeFull();
	}

	public void init() {
		splitPanel = new ConXVerticalSplitPanel();
		splitPanel.setSizeFull();
		splitPanel.setImmediate(true);
		splitPanel.setSplitPosition(50);
		splitPanel.setStyleName("conx-entity-editor");
		mainLayout.addComponent(splitPanel);
		mainLayout.setExpandRatio(splitPanel, 1.0f);
	}
	
	@Override
	public void setBreadCrumb(Component component) {
		if (breadCrumb != null) {
			mainLayout.removeComponent(breadCrumb);
		}
		breadCrumb = component;
		mainLayout.addComponent(breadCrumb, 0);
	}

	@Override
	public void setHeader(Component component) {
		if (header != null) {
			mainLayout.removeComponent(header);
		}
		header = component;
		if (breadCrumb != null) {
			mainLayout.addComponent(header, 1);
		} else {
			mainLayout.addComponent(header, 0);
		}
	}

	@Override
	public void setMaster(Component component) {
		splitPanel.setFirstComponent(component);
	}

	@Override
	public void setDetail(Component component) {
		splitPanel.setSecondComponent(component);
	}

	@Override
	public void setFooter(Component component) {
		if (footer != null) {
			mainLayout.removeComponent(footer);
		}
		footer = component;
		int index = 1;
		if (breadCrumb != null) {
			index++;
		}
		if (header != null) {
			index++;
		}
		mainLayout.addComponent(footer, index);
	}

	@Override
	public void addSplitPositionChangeListener(ISplitPositionChangeListener listener) {
		this.splitPanel.addSplitPositionChangeListener(listener);
	}
}
