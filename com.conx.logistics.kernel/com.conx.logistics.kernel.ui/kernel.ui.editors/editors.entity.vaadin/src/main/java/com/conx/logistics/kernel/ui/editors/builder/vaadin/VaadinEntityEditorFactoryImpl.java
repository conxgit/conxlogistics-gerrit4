package com.conx.logistics.kernel.ui.editors.builder.vaadin;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.EventBusManager;
import org.vaadin.mvp.presenter.IPresenter;

import com.conx.logistics.kernel.documentlibrary.remote.services.IRemoteDocumentRepository;
import com.conx.logistics.kernel.ui.components.domain.AbstractConXComponent;
import com.conx.logistics.kernel.ui.components.domain.attachment.AttachmentEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.form.ConXForm;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.MasterDetailComponent;
import com.conx.logistics.kernel.ui.components.domain.note.NoteEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.referencenumber.ReferenceNumberEditorComponent;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.ConfigurablePresenterFactory;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.MultiLevelEntityEditorEventBus;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.MultiLevelEntityEditorPresenter;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.attachment.AttachmentEditorPresenter;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.customizer.ConfigurablePresenterFactoryCustomizer;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.form.FormEditorPresenter;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.notes.NotesEditorPresenter;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.refNum.ReferenceNumberEditorPresenter;
import com.conx.logistics.kernel.ui.factory.services.IEntityEditorFactory;

@Transactional
@Repository
public class VaadinEntityEditorFactoryImpl implements IEntityEditorFactory {

	@SuppressWarnings("unused")
	private IRemoteDocumentRepository remoteDocumentRepository;

	private ConfigurablePresenterFactory factory;

	public VaadinEntityEditorFactoryImpl() {
	}

	public VaadinEntityEditorFactoryImpl(ConfigurablePresenterFactory factory) {
		this.factory = factory;
	}

	@Override
	public Map<IPresenter<?, ? extends EventBus>, EventBus> create(AbstractConXComponent conXComponent, Map<String, Object> params) {
		Map<IPresenter<?, ? extends EventBus>, EventBus> res = null;

		if (conXComponent instanceof AttachmentEditorComponent) {
			final IPresenter<?, ? extends EventBus> presenter = factory.createPresenter(AttachmentEditorPresenter.class);
			final EventBus eventBus = presenter.getEventBus();

			res = new HashMap<IPresenter<?, ? extends EventBus>, EventBus>();
			res.put(presenter, eventBus);
		} else if (conXComponent instanceof NoteEditorComponent) {
			IPresenter<?, ? extends EventBus> presenter = factory.createPresenter(NotesEditorPresenter.class);
			EventBus eventBus = presenter.getEventBus();

			res = new HashMap<IPresenter<?, ? extends EventBus>, EventBus>();
			res.put(presenter, eventBus);
		} else if (conXComponent instanceof ReferenceNumberEditorComponent) {
			IPresenter<?, ? extends EventBus> presenter = factory.createPresenter(ReferenceNumberEditorPresenter.class);
			EventBus eventBus = presenter.getEventBus();

			res = new HashMap<IPresenter<?, ? extends EventBus>, EventBus>();
			res.put(presenter, eventBus);
		} else if (conXComponent instanceof MasterDetailComponent) {
			params.put(IEntityEditorFactory.FACTORY_PARAM_MVP_COMPONENT_MODEL, conXComponent);
			ConfigurablePresenterFactory presenterFactory = null;
			EventBusManager ebm = (EventBusManager) params.get(IEntityEditorFactory.FACTORY_PARAM_MVP_EVENTBUS_MANAGER);

			if (params.containsKey(IEntityEditorFactory.FACTORY_PARAM_MVP_PRESENTER_FACTORY)) {
				presenterFactory = (ConfigurablePresenterFactory) params.get(IEntityEditorFactory.FACTORY_PARAM_MVP_PRESENTER_FACTORY);
			} else {
				ebm = (EventBusManager) params.get(IEntityEditorFactory.FACTORY_PARAM_MVP_EVENTBUS_MANAGER);
				Locale locale = (Locale) params.get(IEntityEditorFactory.FACTORY_PARAM_MVP_LOCALE);
				presenterFactory = new ConfigurablePresenterFactory(ebm, locale);
				presenterFactory.setCustomizer(new ConfigurablePresenterFactoryCustomizer(params));
				params.put(IEntityEditorFactory.FACTORY_PARAM_MVP_PRESENTER_FACTORY, presenterFactory);
			}

			final IPresenter<?, ? extends EventBus> mainPresenter = presenterFactory.createPresenter(MultiLevelEntityEditorPresenter.class);
			final MultiLevelEntityEditorEventBus mainEventBus = (MultiLevelEntityEditorEventBus) mainPresenter.getEventBus();

			res = new HashMap<IPresenter<?, ? extends EventBus>, EventBus>();
			res.put(mainPresenter, mainEventBus);
		} else if (conXComponent instanceof ConXForm) {
			IPresenter<?, ? extends EventBus> presenter = factory.createPresenter(FormEditorPresenter.class);
			EventBus eventBus = presenter.getEventBus();

			res = new HashMap<IPresenter<?, ? extends EventBus>, EventBus>();
			res.put(presenter, eventBus);
		}
		return res;
	}

	public void setRemoteDocumentRepository(IRemoteDocumentRepository remoteDocumentRepository) {
		this.remoteDocumentRepository = remoteDocumentRepository;
	}
}
