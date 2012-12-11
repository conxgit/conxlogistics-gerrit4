package com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.notes;

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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.vaadin.mvp.eventbus.EventBusManager;
import org.vaadin.mvp.presenter.BasePresenter;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.conx.logistics.kernel.pageflow.services.IPageComponent;
import com.conx.logistics.kernel.pageflow.ui.ext.mvp.IConfigurablePresenter;
import com.conx.logistics.kernel.pageflow.ui.ext.mvp.lineeditor.section.ILineEditorSectionContentPresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.notes.view.INotesEditorView;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.notes.view.NotesEditorView;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.notes.view.NotesEditorView.ICreateNotesListener;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.notes.view.NotesEditorView.ISaveNotesListener;
import com.conx.logistics.kernel.persistence.services.IEntityContainerProvider;
import com.conx.logistics.kernel.ui.components.domain.note.NoteEditorComponent;
import com.conx.logistics.kernel.ui.factory.services.IEntityEditorFactory;
import com.conx.logistics.kernel.ui.factory.services.data.IDAOProvider;
import com.conx.logistics.kernel.ui.forms.vaadin.FormMode;
import com.conx.logistics.mdm.dao.services.note.INoteDAOService;
import com.conx.logistics.mdm.domain.BaseEntity;
import com.conx.logistics.mdm.domain.note.Note;
import com.conx.logistics.mdm.domain.note.NoteItem;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.util.DefaultQueryModifierDelegate;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

@Presenter(view = NotesEditorView.class)
public class NotesEditorPresenter extends BasePresenter<INotesEditorView, NotesEditorEventBus> implements ICreateNotesListener,
		ISaveNotesListener, ILineEditorSectionContentPresenter, IConfigurablePresenter {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private boolean initialized = false;
	private Set<String> visibleFieldNames;
	private List<String> formVisibleFieldNames;
	private Item newEntityItem;
	private BaseEntity entity;
	private NoteEditorComponent noteComponent;
	private JPAContainer<NoteItem> entityContainer;
	private IEntityContainerProvider entityContainerProvider;
	private IDAOProvider daoProvider;

	@SuppressWarnings("unchecked")
	private void initialize() {
		this.entityContainer = (JPAContainer<NoteItem>) this.entityContainerProvider.createNonCachingPersistenceContainer(NoteItem.class);
		this.visibleFieldNames = this.noteComponent.getDataSource().getVisibleFieldNames();
		this.formVisibleFieldNames = Arrays.asList("noteType", "content");
		Set<String> nestedFieldNames = noteComponent.getDataSource().getNestedFieldNames();
		for (String npp : nestedFieldNames) {
			entityContainer.addNestedContainerProperty(npp);
		}

		this.getView().init();
		this.getView().addCreateNotesListener(this);
		this.getView().addSaveNotesListener(this);
		this.getView().setContainerDataSource(entityContainer, visibleFieldNames, formVisibleFieldNames);
		this.getView().showContent();
		this.setInitialized(true);
	}

	@SuppressWarnings("rawtypes")
	private Object getBean(Item item) {
		if (item instanceof EntityItem) {
			return ((EntityItem) item).getEntity();
		} else if (item instanceof BeanItem) {
			return ((BeanItem) item).getBean();
		} else {
			return null;
		}
	}

	@Override
	public void onSetItemDataSource(Item item, Container... container) throws Exception {
		Object bean = getBean(item);
		if (bean instanceof BaseEntity) {
			this.entity = (BaseEntity) bean;
			if (this.entity.getNote() == null) {
				DefaultTransactionDefinition def = new DefaultTransactionDefinition();
				def.setName("pageflow.ui.data");
				def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
				TransactionStatus status = this.daoProvider.provideByDAOClass(PlatformTransactionManager.class).getTransaction(def);

				try {
					this.entity = this.daoProvider.provideByDAOClass(INoteDAOService.class).provideNoteForEntity(this.entity);
					this.daoProvider.provideByDAOClass(PlatformTransactionManager.class).commit(status);
				} catch (Exception e) {
					e.printStackTrace();
					this.daoProvider.provideByDAOClass(PlatformTransactionManager.class).rollback(status);
				}
			}

			if (!isInitialized()) {
				initialize();
			}

			updateQueryFilter();
		}
	}

	private void updateQueryFilter() {
		this.entityContainer.getEntityProvider().setQueryModifierDelegate(new DefaultQueryModifierDelegate() {
			@Override
			public void filtersWillBeAdded(CriteriaBuilder criteriaBuilder, CriteriaQuery<?> query, List<Predicate> predicates) {
				Root<?> fromFileEntry = query.getRoots().iterator().next();

				Path<Note> parentNote = fromFileEntry.<Note> get("note");
				Path<Long> pathId = parentNote.get("id");
				predicates.add(criteriaBuilder.equal(pathId, NotesEditorPresenter.this.entity.getNote().getId()));
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
	public void onConfigure(Map<String, Object> params) {
		this.noteComponent = (NoteEditorComponent) params.get(IEntityEditorFactory.COMPONENT_MODEL);
		this.entityContainerProvider = (IEntityContainerProvider) params.get(IEntityEditorFactory.CONTAINER_PROVIDER);
		this.daoProvider = (IDAOProvider) params.get(IPageComponent.DAO_PROVIDER);
	}

	@Override
	public void onSaveNotes(Item item) {
		this.newEntityItem = null;
		this.getView().hideDetail();
	}

	@Override
	public void onCreateNotes() {
		NoteItem noteItem = new NoteItem();
		noteItem.setNote(this.entity.getNote());

		Object newId = this.entityContainer.addEntity(noteItem);
		this.newEntityItem = this.entityContainer.getItem(newId);
		this.getView().setItemDataSource(newEntityItem, FormMode.CREATING);
		this.getView().showDetail();
	}

	@Override
	public void subscribe(EventBusManager eventBusManager) {
	}
}
