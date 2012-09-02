package com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.notes.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.vaadin.mvp.uibinder.annotation.UiField;

import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.FormMode;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.fieldfactory.ConXFieldFactory;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.notes.NoteEditorForm;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.notes.NotesEditorToolStrip;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.table.EntityGridFilterManager;
import com.conx.logistics.kernel.ui.filteredtable.FilterTable;
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

public class NotesEditorView extends VerticalLayout implements INotesEditorView {
	private static final long serialVersionUID = 1L;
	
	@UiField
	private VerticalLayout mainLayout;

	private VerticalSplitPanel splitPanel;
	private NotesEditorToolStrip toolStrip;
	private VerticalLayout masterLayout;
	private VerticalLayout detailLayout;
	private FilterTable grid;
	private NoteEditorForm form;
	private Collection<?> visibleFormFields;
	private VerticalLayout defaultPanel;
	private boolean detailHidden;
	
	private Set<ICreateNoteListener> createNoteListenerSet;
	private Set<ISaveNoteListener> saveNoteListenerSet;
	
	public NotesEditorView() {
		this.grid = new FilterTable();
		this.splitPanel = new VerticalSplitPanel();
		this.toolStrip = new NotesEditorToolStrip();
		this.masterLayout = new VerticalLayout();
		this.detailLayout = new VerticalLayout();
		this.form = new NoteEditorForm();
		this.defaultPanel = new VerticalLayout();
		this.detailHidden = true;
		this.createNoteListenerSet = new HashSet<ICreateNoteListener>();
		this.saveNoteListenerSet = new HashSet<ISaveNoteListener>();
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

			@Override
			public void itemClick(ItemClickEvent event) {
				if (visibleFormFields == null) {
					ArrayList<String> tempVisibleFormFields = new ArrayList<String>();
					tempVisibleFormFields.add("code");
					visibleFormFields = tempVisibleFormFields;
				}
				
				NotesEditorView.this.toolStrip.setEditButtonEnabled(true);
				NotesEditorView.this.toolStrip.setDeleteButtonEnabled(true);
				form.setMode(FormMode.EDITING);
				form.setItemDataSource(event.getItem(), visibleFormFields);
				if (detailHidden) {
					showDetail();
				}
			}
		});
		
		this.toolStrip = new NotesEditorToolStrip();
		this.toolStrip.setEditButtonEnabled(false);
		this.toolStrip.setDeleteButtonEnabled(false);
		this.toolStrip.addNewButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -60083075517936436L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (detailHidden) {
					onCreateNote();
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
	public void setContainerDataSource(Container container,
			Collection<?> visibleGridColumns, Collection<?> visibleFormFields) {
		this.grid.setContainerDataSource(container);
		this.grid.setVisibleColumns(visibleGridColumns.toArray());
		this.visibleFormFields = visibleFormFields;
	}
	
	public void onCreateNote() {
		for (ICreateNoteListener listener : createNoteListenerSet) {
			listener.onCreateNote();
		}
	}
	
	public void onSaveNote(Item item) {
		for (ISaveNoteListener listener : saveNoteListenerSet) {
			listener.onSaveNote(item);
		}
	}
	
	@Override
	public void setItemDataSource(Item item, FormMode mode) {
		form.setMode(mode);
		form.setItemDataSource(item, visibleFormFields);
	}
	
	@Override
	public void addCreateNoteListener(ICreateNoteListener listener) {
		createNoteListenerSet.add(listener);
	}
	
	@Override
	public void addSaveNoteListener(ISaveNoteListener listener) {
		saveNoteListenerSet.add(listener);
	}
	
	public interface ICreateNoteListener {
		public void onCreateNote();
	}
	
	public interface ISaveNoteListener {
		public void onSaveNote(Item item);
	}
}
