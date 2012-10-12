package com.conx.logistics.kernel.pageflow.ui.mvp.attachment;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.presenter.BasePresenter;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.conx.logistics.kernel.documentlibrary.remote.services.IRemoteDocumentRepository;
import com.conx.logistics.kernel.pageflow.ui.mvp.attachment.view.AttachmentEditorView;
import com.conx.logistics.kernel.pageflow.ui.mvp.attachment.view.AttachmentEditorView.ICreateAttachmentListener;
import com.conx.logistics.kernel.pageflow.ui.mvp.attachment.view.AttachmentEditorView.IInspectAttachmentListener;
import com.conx.logistics.kernel.pageflow.ui.mvp.attachment.view.AttachmentEditorView.ISaveAttachmentListener;
import com.conx.logistics.kernel.pageflow.ui.mvp.attachment.view.IAttachmentEditorView;
import com.conx.logistics.kernel.persistence.services.IEntityContainerProvider;
import com.conx.logistics.kernel.ui.components.domain.attachment.AttachmentEditorComponent;
import com.conx.logistics.kernel.ui.factory.services.IEntityEditorFactory;
import com.conx.logistics.kernel.ui.forms.vaadin.FormMode;
import com.conx.logistics.mdm.dao.services.documentlibrary.IFolderDAOService;
import com.conx.logistics.mdm.domain.BaseEntity;
import com.conx.logistics.mdm.domain.documentlibrary.DocType;
import com.conx.logistics.mdm.domain.documentlibrary.FileEntry;
import com.conx.logistics.mdm.domain.documentlibrary.Folder;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.util.DefaultQueryModifierDelegate;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@Presenter(view = AttachmentEditorView.class)
public class AttachmentEditorPresenter extends BasePresenter<IAttachmentEditorView, AttachmentEditorEventBus> implements ICreateAttachmentListener, ISaveAttachmentListener, IInspectAttachmentListener {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private boolean initialized = false;
	private JPAContainer<FileEntry> entityContainer;
	private Set<String> visibleFieldNames;
	private IRemoteDocumentRepository docRepo;
	private Folder docFolder;
	private List<String> formVisibleFieldNames;
	private EntityItem<FileEntry> newEntityItem;
	@SuppressWarnings("unused")
	private IFolderDAOService docFolderDAOService;
	private AttachmentEditorComponent attachmentComponent;
	private IEntityContainerProvider entityContainerProvider;

	@SuppressWarnings("unchecked")
	private void initialize() {
		this.entityContainer = (JPAContainer<FileEntry>) this.entityContainerProvider.createPersistenceContainer(FileEntry.class);
		this.visibleFieldNames = attachmentComponent.getDataSource().getVisibleFieldNames();
		this.formVisibleFieldNames = Arrays.asList("docType");
		Set<String> nestedFieldNames = attachmentComponent.getDataSource().getNestedFieldNames();
		for (String npp : nestedFieldNames) {
			entityContainer.addNestedContainerProperty(npp);
		}
		this.getView().init();
		this.getView().addCreateAttachmentListener(this);
		this.getView().addSaveAttachmentListener(this);
		this.getView().addInspectAttachmentListener(this);
		this.getView().setContainerDataSource(entityContainer, visibleFieldNames, formVisibleFieldNames);
		this.getView().showContent();
		this.setInitialized(true);
	}

	public void onSetItemDataSource(Item item) {
		BaseEntity entity = null;
		if (item instanceof BeanItem) {
			entity = (BaseEntity) ((BeanItem<?>) item).getBean();
		} else if (item instanceof EntityItem) {
			entity = (BaseEntity) ((EntityItem<?>) item).getEntity();
		}

		if (entity != null) {
			this.docFolder = entity.getDocFolder();
			if (!initialized) {
				initialize();
			}
			updateQueryFilter();
		}
	}

	private void updateQueryFilter() {
		this.entityContainer.removeAllContainerFilters();
		this.entityContainer.getEntityProvider().setQueryModifierDelegate(new DefaultQueryModifierDelegate() {
			@Override
			public void filtersWillBeAdded(CriteriaBuilder criteriaBuilder, CriteriaQuery<?> query, List<Predicate> predicates) {
				Root<?> fromFileEntry = query.getRoots().iterator().next();

				// Add a "WHERE age > 116" expression
				Path<Folder> parentFolder = fromFileEntry.<Folder> get("folder");
				Path<Long> pathId = parentFolder.get("id");
				predicates.add(criteriaBuilder.equal(pathId, AttachmentEditorPresenter.this.docFolder.getId()));
			}
		});
		this.entityContainer.applyFilters();
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	@Override
	public void bind() {
		super.bind();
		this.getView().setEventBus(this.getEventBus());
	}

	public void onConfigure(Map<String, Object> params) {
		this.docRepo = (IRemoteDocumentRepository) params.get(IEntityEditorFactory.FACTORY_PARAM_IDOCLIB_REPO_SERVICE);
		this.docFolderDAOService = (IFolderDAOService) params.get(IEntityEditorFactory.FACTORY_PARAM_IFOLDER_SERVICE);
		this.attachmentComponent = (AttachmentEditorComponent) params.get(IEntityEditorFactory.COMPONENT_MODEL);
		this.entityContainerProvider = (IEntityContainerProvider) params.get(IEntityEditorFactory.CONTAINER_PROVIDER);

		getView().addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = -7680485120452162721L;

			@Override
			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick()) {
					EntityItem<?> jpaItem = (EntityItem<?>) event.getItem();
					onInspectAttachment((FileEntry) jpaItem.getEntity());
				} else {
					getView().entityItemSingleClicked((EntityItem<?>) event.getItem());
				}
			}
		});

		getView().addNewItemListener(new ClickListener() {
			private static final long serialVersionUID = -60083075517936436L;

			@Override
			public void buttonClick(ClickEvent event) {
				getView().newEntityItemActioned();
			}
		});
	}

	public void onSaveForm(DocType attachmentType, String sourceFileName, String mimeType, String title, String description) throws Exception {
		// this.docRepo.addorUpdateFileEntry(this.docFolder, attachmentType,
		// sourceFileName, mimeType, title, description);
		this.entityContainer.refresh();
	}

	@Override
	public void onCreateAttachment() {
		this.newEntityItem = this.entityContainer.createEntityItem(new FileEntry());
		this.getView().setItemDataSource(this.newEntityItem, FormMode.CREATING);
		this.getView().showDetail();
	}

	@Override
	public boolean onSaveAttachment(Item item, DocType attachmentType, String sourceFileName, String mimeType, String title, String description) {
		try {
			this.docRepo.addorUpdateFileEntry(this.docFolder, attachmentType, sourceFileName, mimeType, title, description);
			this.entityContainer.refresh();
			// this.onEntityItemAdded((EntityItem<?>) item);
			this.getView().hideDetail();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void onInspectAttachment(FileEntry fileEntry) {
		// TODO furnish this
	}
}
