package com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.attachment.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.vaadin.mvp.uibinder.annotation.UiField;

import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.FormMode;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.attachment.AttachmentEditorToolStrip;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.attachment.AttachmentForm;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.fieldfactory.ConXFieldFactory;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.table.EntityGridFilterManager;
import com.conx.logistics.kernel.ui.filteredtable.FilterTable;
import com.conx.logistics.mdm.domain.documentlibrary.DocType;
import com.conx.logistics.mdm.domain.documentlibrary.FileEntry;
import com.vaadin.addon.jpacontainer.JPAContainerItem;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

public class AttachmentEditorView extends VerticalLayout implements IAttachmentEditorView {
	private static final long serialVersionUID = 1L;

	@UiField
	private VerticalLayout mainLayout;

	private VerticalSplitPanel splitPanel;
	private AttachmentEditorToolStrip toolStrip;
	private VerticalLayout masterLayout;
	private VerticalLayout detailLayout;
	private FilterTable grid;
	private AttachmentForm form;
	private Collection<?> visibleFormFields;
	private VerticalLayout defaultPanel;
	private boolean detailHidden;

	private Set<ICreateAttachmentListener> createAttachmentListenerSet;
	private Set<ISaveAttachmentListener> saveAttachmentListenerSet;
	private Set<IInspectAttachmentListener> inspectAttachmentListenerSet;

	public AttachmentEditorView() {
		this.grid = new FilterTable();
		this.splitPanel = new VerticalSplitPanel();
		this.toolStrip = new AttachmentEditorToolStrip();
		this.masterLayout = new VerticalLayout();
		this.detailLayout = new VerticalLayout();
		this.form = new AttachmentForm();
		this.defaultPanel = new VerticalLayout();
		this.detailHidden = true;
		this.createAttachmentListenerSet = new HashSet<ICreateAttachmentListener>();
		this.saveAttachmentListenerSet = new HashSet<ISaveAttachmentListener>();
		this.inspectAttachmentListenerSet = new HashSet<IInspectAttachmentListener>();
	}

	@Override
	public void init() {
		Label defaultPanelCaption = new Label();
		defaultPanelCaption.setValue("<i>Select an item from the grid</i>");
		defaultPanelCaption.setContentMode(Label.CONTENT_XHTML);

		this.defaultPanel.setSizeFull();
		this.defaultPanel.addComponent(defaultPanelCaption);
		this.defaultPanel.setComponentAlignment(defaultPanelCaption, Alignment.MIDDLE_CENTER);

		EntityGridFilterManager gridManager = new EntityGridFilterManager();

		this.grid.setSizeFull();
		this.grid.setSelectable(true);
		this.grid.setNullSelectionAllowed(false);
		this.grid.setFilterDecorator(gridManager);
		this.grid.setFiltersVisible(true);
		this.grid.addListener(new ItemClickListener() {
			private static final long serialVersionUID = -7680485120452162721L;

			@SuppressWarnings("unchecked")
			@Override
			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick()) {
					JPAContainerItem<FileEntry> jpaItem = (JPAContainerItem<FileEntry>) event.getItem();
					onInspectAttachment(jpaItem.getEntity());
				} else {
					if (visibleFormFields == null) {
						ArrayList<String> tempVisibleFormFields = new ArrayList<String>();
						tempVisibleFormFields.add("code");
						visibleFormFields = tempVisibleFormFields;
					}
	
					AttachmentEditorView.this.toolStrip.setEditButtonEnabled(true);
					AttachmentEditorView.this.toolStrip.setDeleteButtonEnabled(true);
					form.setMode(FormMode.EDITING);
					form.setItemDataSource(event.getItem(), visibleFormFields);
					if (detailHidden) {
						showDetail();
					}
				}
			}
		});

		this.toolStrip = new AttachmentEditorToolStrip();
		this.toolStrip.setEditButtonEnabled(false);
		this.toolStrip.setDeleteButtonEnabled(false);
		this.toolStrip.addNewButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -60083075517936436L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (detailHidden) {
					onCreateAttachment();
				} else {
					hideDetail();
				}
			}
		});

		this.masterLayout.setSizeFull();
		this.masterLayout.removeAllComponents();
		this.masterLayout.addComponent(toolStrip);
		this.masterLayout.addComponent(grid);
		this.masterLayout.setExpandRatio(grid, 1.0f);

		this.form.setFormFieldFactory(new ConXFieldFactory());
		this.form.setView(this);

		this.detailLayout.setSizeFull();
		this.detailLayout.addComponent(form);

		this.splitPanel.removeAllComponents();
		this.splitPanel.setStyleName("conx-entity-grid");
		this.splitPanel.addComponent(masterLayout);
		this.splitPanel.addComponent(detailLayout);
		this.splitPanel.setSizeFull();

		hideDetail();
		hideContent();

		setSizeFull();
	}

	@Override
	public void showContent() {
		this.mainLayout.removeAllComponents();
		this.mainLayout.addComponent(this.splitPanel);
	}

	@Override
	public void hideContent() {
		this.mainLayout.removeAllComponents();
		this.mainLayout.addComponent(this.defaultPanel);
	}

	@Override
	public void showDetail() {
		this.form.setEnabled(true);
		this.splitPanel.setSplitPosition(50);
		this.splitPanel.setLocked(false);
		this.detailHidden = false;
	}

	@Override
	public void hideDetail() {
		this.form.setEnabled(false);
		this.splitPanel.setSplitPosition(100);
		this.splitPanel.setLocked(true);
		this.detailHidden = true;
	}

	public Layout getMainLayout() {
		return mainLayout;
	}

	@Override
	public void setContainerDataSource(Container container, Collection<?> visibleGridColumns, Collection<?> visibleFormFields) {
		this.grid.setContainerDataSource(container);
		this.grid.setVisibleColumns(visibleGridColumns.toArray());
		this.visibleFormFields = visibleFormFields;
	}

	@Override
	public void addCreateAttachmentListener(ICreateAttachmentListener listener) {
		createAttachmentListenerSet.add(listener);
	}
	
	@Override
	public void addSaveAttachmentListener(ISaveAttachmentListener listener) {
		saveAttachmentListenerSet.add(listener);
	}
	
	@Override
	public void addInspectAttachmentListener(IInspectAttachmentListener listener) {
		inspectAttachmentListenerSet.add(listener);
	}

	private void onCreateAttachment() {
		for (ICreateAttachmentListener listener : this.createAttachmentListenerSet) {
			listener.onCreateAttachment();
		}
	}
	
	private void onInspectAttachment(FileEntry fileEntry) {
		for (IInspectAttachmentListener listener : this.inspectAttachmentListenerSet) {
			listener.onInspectAttachment(fileEntry);
		}
	}

	@Override
	public void setItemDataSource(Item item, FormMode mode) {
		form.setMode(mode);
		form.setItemDataSource(item, visibleFormFields);
	}
	
	public void onSaveAttachment(Item item, DocType attachmentType, String sourceFileName, String mimeType, String title, String description) {
		for (ISaveAttachmentListener listener : this.saveAttachmentListenerSet) {
			listener.onSaveAttachment(item, attachmentType, sourceFileName, mimeType, title, description);
		}
	}
	
	public interface IInspectAttachmentListener {
		public void onInspectAttachment(FileEntry fileEntry);
	}
	
	public interface ICreateAttachmentListener {
		public void onCreateAttachment();
	}
	
	public interface ISaveAttachmentListener {
		public boolean onSaveAttachment(Item item, DocType attachmentType, String sourceFileName, String mimeType, String title, String description);
	}
}
