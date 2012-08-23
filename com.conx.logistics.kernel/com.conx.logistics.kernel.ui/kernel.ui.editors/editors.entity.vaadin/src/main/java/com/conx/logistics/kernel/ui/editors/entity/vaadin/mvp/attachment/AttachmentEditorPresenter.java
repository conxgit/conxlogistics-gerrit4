package com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.attachment;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.conx.logistics.kernel.documentlibrary.remote.services.IRemoteDocumentRepository;
import com.conx.logistics.kernel.ui.components.domain.AbstractConXComponent;
import com.conx.logistics.kernel.ui.components.domain.attachment.AttachmentEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.LineEditorComponent;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.attachment.AttachmentEditorToolStrip;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.fieldfactory.ConXFieldFactory;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.table.EntityGridFilterManager;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.upload.AttachmentForm;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.AbstractEntityEditorEventBus;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.ConfigurableBasePresenter;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.attachment.view.IAttachmentEditorView;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.lineeditor.view.EntityLineEditorSectionView;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.lineeditor.view.IEntityLineEditorSectionView;
import com.conx.logistics.kernel.ui.filteredtable.gwt.client.ui.FilterTable;
import com.conx.logistics.mdm.dao.services.documentlibrary.IFolderDAOService;
import com.conx.logistics.mdm.domain.BaseEntity;
import com.conx.logistics.mdm.domain.documentlibrary.FileEntry;
import com.conx.logistics.mdm.domain.documentlibrary.Folder;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.JPAContainerItem;
import com.vaadin.addon.jpacontainer.util.DefaultQueryModifierDelegate;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

@Presenter(view = EntityLineEditorSectionView.class)
public class AttachmentEditorPresenter
		extends
		ConfigurableBasePresenter<IAttachmentEditorView, AttachmentEditorEventBus>
		implements Property.ValueChangeListener {
	private static final long serialVersionUID = 1L;

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private boolean initialized = false;
	private LineEditorComponent lineEditorSectionComponentModel;

	private JPAContainer entityContainer;

	private AbstractEntityEditorEventBus entityEditorEventListener;

	private AttachmentForm form;

	private Set<String> visibleFieldNames;

	private IRemoteDocumentRepository docRepo;

	private String folderId;

	private Folder docFolder;

	private VerticalLayout defaultPanel;

	private AbstractConXComponent attachmentComponent;

	private EntityManager entityManager;

	private HashMap<String, Object> extraInitParams;

	private IFolderDAOService docFolderDAOService;

	private ConfigurableBasePresenter mainEventBus;

	private List<String> formVisibleFieldNames;

	private AttachmentEditorToolStrip toolStrip;

	private VerticalSplitPanel splitPanel;
	
	private EntityItem newFE;

	public AttachmentEditorPresenter() {
		super();
	}

	private void resetViewContentPanel(Layout panel) {
		((IEntityLineEditorSectionView) getView()).getMainLayout()
				.removeAllComponents();
		((IEntityLineEditorSectionView) getView()).getMainLayout()
				.addComponent(panel);
	}

	private void initialize() throws ClassNotFoundException {
		// - View: Create attachment grid and layout
		EntityGridFilterManager gridManager = new EntityGridFilterManager();
		FilterTable grid = new FilterTable();
		grid.setSizeFull();
		grid.setSelectable(true);
		grid.setNullSelectionAllowed(false);
		grid.setFilterDecorator(gridManager);
		grid.setFiltersVisible(true);

		this.splitPanel = new VerticalSplitPanel();
		this.form = new AttachmentForm(
				(AttachmentEditorEventBus)this.getEventBus(),
				(AbstractEntityEditorEventBus)this.mainEventBus.getEventBus(),
				this.entityManager,
				this.docRepo,
				this.docFolderDAOService,
				this.splitPanel);
		form.setFormFieldFactory(new ConXFieldFactory());

		// - Create upload form
		final VerticalLayout uploadLayout = new VerticalLayout();
		uploadLayout.setHeight("100%");
		uploadLayout.setWidth("100%");
		uploadLayout.addComponent(form);

		// - Toolstrip
		this.toolStrip = new AttachmentEditorToolStrip();
		this.toolStrip.setEditButtonEnabled(false);
		this.toolStrip.setDeleteButtonEnabled(false);
		this.toolStrip.addNewButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -60083075517936436L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (splitPanel.getSplitPosition() == 100) {
					splitPanel.setSplitPosition(50);
					//New FileEntry
					newFE = entityContainer.createEntityItem(new FileEntry());
					form.setMode(AttachmentForm.FormMode.CREATING);
					form.setItemDataSource(newFE, AttachmentEditorPresenter.this.formVisibleFieldNames);					
				} else {
					splitPanel.setSplitPosition(100);
					if (newFE != null && newFE.getItemId() != null) {
						entityContainer.removeItem(newFE.getItemId());
					}
					newFE = null;
				}
			}
		});
		
		VerticalLayout gridLayout = new VerticalLayout();
		gridLayout.setSizeFull();
		gridLayout.addComponent(toolStrip);
		gridLayout.addComponent(grid);
		gridLayout.setExpandRatio(grid, 1.0f);

		splitPanel.setStyleName("conx-entity-grid");
		splitPanel.addComponent(gridLayout);
		splitPanel.addComponent(uploadLayout);
		splitPanel.setSizeFull();
		splitPanel.setSplitPosition(100);

		// - Presenter: Init grid
		Set<String> nestedFieldNames = attachmentComponent.getDataSource()
				.getNestedFieldNames();
		for (String npp : nestedFieldNames) {
			entityContainer.addNestedContainerProperty(npp);
		}
		grid.setContainerDataSource(entityContainer);

		this.visibleFieldNames = attachmentComponent.getDataSource()
				.getVisibleFieldNames();
		this.formVisibleFieldNames = Arrays.asList("docType");
		grid.setVisibleColumns(visibleFieldNames.toArray(new String[0]));
		grid.addListener(new ItemClickListener() {
			private static final long serialVersionUID = 7230326485331772539L;

			public void itemClick(ItemClickEvent event) {
				AttachmentEditorPresenter.this.toolStrip.setEditButtonEnabled(true);
				AttachmentEditorPresenter.this.toolStrip.setDeleteButtonEnabled(true);
				JPAContainerItem item = (JPAContainerItem) event.getItem();
				Object entity = item.getEntity();
				entity.toString();
				form.setMode(AttachmentForm.FormMode.EDITING);
				form.setItemDataSource(item, formVisibleFieldNames);
				splitPanel.setSplitPosition(50);
				// AttachmentEditorPresenter.this.entityEditorEventListener.entityItemEdit(item);
			}
		});

		resetViewContentPanel(splitPanel);
		this.setInitialized(true);
	}

	// MultiLevelEntityEditorEventBus implementation
	public void onEntityItemEdit(EntityItem item) {
		BaseEntity entity = (BaseEntity) item.getEntity();
		this.docFolder = entity.getDocFolder();
		if (!isInitialized()) {
			try {
				initialize();
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				String stacktrace = sw.toString();
				logger.error(stacktrace);
			}
		}	
		this.form.setDocFolder(this.docFolder);
		updateQueryFilter();				
	}

	private void updateQueryFilter() {
		this.entityContainer.getEntityProvider().setQueryModifierDelegate(
				new DefaultQueryModifierDelegate() {
					@Override
					public void filtersWillBeAdded(
							CriteriaBuilder criteriaBuilder,
							CriteriaQuery<?> query, List<Predicate> predicates) {
						Root<?> fromFileEntry = query.getRoots().iterator()
								.next();

						// Add a "WHERE age > 116" expression
						Path<Folder> parentFolder = fromFileEntry
								.<Folder> get("folder");
						predicates.add(criteriaBuilder.equal(parentFolder,
								AttachmentEditorPresenter.this.docFolder));
					}
				});
	}
	
	public void onEntityItemAdded(EntityItem item) {
		this.entityContainer.refresh();
	}	
	
	public void onFileEntryAdded(FileEntry fe) {
		this.entityContainer.addEntity(fe);
	}

	@Override
	public void bind() {
		try {
			this.defaultPanel = new VerticalLayout();
			this.defaultPanel.setCaption("Select Record");
			resetViewContentPanel(this.defaultPanel);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);
		}		
	}

	@Override
	public void valueChange(ValueChangeEvent event) {

	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	@Override
	public void configure() {
		this.mainEventBus = (ConfigurableBasePresenter) getConfig().get("multiLevelEntityEditorPresenter");		
		
		this.docRepo = (IRemoteDocumentRepository) getConfig().get(
				"iremoteDocumentRepository");

		this.docFolderDAOService = (IFolderDAOService) getConfig().get(
				"ifolderDAOService");		
		
		this.attachmentComponent = (AttachmentEditorComponent) getConfig().get(
				"componentModel");
		this.entityManager = (EntityManager) getConfig().get("em");

		// - Create FileEntry container
		this.entityContainer = JPAContainerFactory.make(FileEntry.class,
				entityManager);
	}
}
