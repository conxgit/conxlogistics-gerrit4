package com.conx.logistics.kernel.ui.editors.entity.vaadin.ext;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

public class EntityEditorToolStrip extends HorizontalLayout {
	private static final long serialVersionUID = 2452919399202554279L;

	public static final String TOOLSTRIP_IMG_DELETE_PNG = "toolstrip/img/delete.png";
	public static final String TOOLSTRIP_IMG_EDIT_PNG = "toolstrip/img/edit.png";
	public static final String TOOLSTRIP_IMG_CREATE_PNG = "toolstrip/img/new.png";
	public static final String TOOLSTRIP_IMG_SAVE_PNG = "toolstrip/img/save.png";
	public static final String TOOLSTRIP_IMG_ATTACH_PNG = "toolstrip/img/attach.png";
	public static final String TOOLSTRIP_IMG_PRINT_PNG = "toolstrip/img/print.png";
	public static final String TOOLSTRIP_IMG_VERIFY_PNG = "toolstrip/img/verify.png";
	public static final String TOOLSTRIP_IMG_RESET_PNG = "toolstrip/img/reset.png";
	public static final String TOOLSTRIP_IMG_SEARCH_PNG = "toolstrip/img/search.png";
	public static final String TOOLSTRIP_IMG_HIDE_FILTER_PNG = "toolstrip/img/hide-filter.png";
	public static final String TOOLSTRIP_IMG_FILTER_PNG = "toolstrip/img/filter.png";
	public static final String TOOLSTRIP_IMG_CLEAR_PNG = "toolstrip/img/clear.png";

	private HorizontalLayout leftLayout;
	private HorizontalLayout rightLayout;

	public EntityEditorToolStrip() {
		setStyleName("conx-entity-toolstrip");
		setHeight("40px");
		setWidth("100%");
		
		leftLayout = new HorizontalLayout();
		leftLayout.setHeight("28px");
		leftLayout.setStyleName("conx-entity-toolstrip-left");
		leftLayout.setSpacing(true);
		
		rightLayout = new HorizontalLayout();
		rightLayout.setHeight("28px");
		rightLayout.setStyleName("conx-entity-toolstrip-right");
		
		addComponent(leftLayout);
		addComponent(rightLayout);
		
		setComponentAlignment(leftLayout, Alignment.MIDDLE_LEFT);
		setComponentAlignment(rightLayout, Alignment.MIDDLE_RIGHT);
	}
	
	public EntityEditorToolStripButton addToolStripButton(String iconUrl) {
		EntityEditorToolStripButton button = new EntityEditorToolStripButton(iconUrl);
		leftLayout.addComponent(button);
		return button;
	}
	
	public void removeToolStripButton(EntityEditorToolStripButton button) {
		leftLayout.removeComponent(button);
	}
	
	public void addContextComponent(Component component) {
		rightLayout.addComponent(component);
	}
	
	public void removeContextComponent(Component component) {
		rightLayout.removeComponent(component);
	}
	
	public class EntityEditorToolStripButton extends Button {
		private static final long serialVersionUID = -6850572740737479916L;

		public EntityEditorToolStripButton(String iconUrl) {
			setIcon(new ThemeResource(iconUrl));
			setStyleName("conx-entity-toolstrip-button");
			setHeight("28px");
		}
	}
}
