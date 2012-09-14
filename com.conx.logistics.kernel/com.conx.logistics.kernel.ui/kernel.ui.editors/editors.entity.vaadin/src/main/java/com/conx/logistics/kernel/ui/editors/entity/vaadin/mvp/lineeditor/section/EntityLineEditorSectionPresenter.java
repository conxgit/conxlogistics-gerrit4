package com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.lineeditor.section;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.presenter.IPresenter;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.conx.logistics.kernel.ui.components.domain.form.ConXCollapseableSectionForm;
import com.conx.logistics.kernel.ui.components.domain.form.ConXSimpleForm;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.LineEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.table.ConXDetailTable;
import com.conx.logistics.kernel.ui.editors.builder.vaadin.VaadinEntityEditorFactoryImpl;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.AbstractEntityEditorEventBus;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.ConfigurableBasePresenter;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.ConfigurablePresenterFactory;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.lineeditor.section.form.collapsible.EntityLineEditorCollapsibleFormPresenter;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.lineeditor.section.form.header.EntityLineEditorFormHeaderPresenter;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.lineeditor.section.form.simple.EntityLineEditorSimpleFormPresenter;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.lineeditor.section.grid.header.EntityLineEditorGridHeaderPresenter;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.lineeditor.section.view.EntityLineEditorSectionView;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.lineeditor.section.view.IEntityLineEditorSectionView;
import com.conx.logistics.kernel.ui.factory.services.IEntityEditorFactory;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.ui.Component;

@Presenter(view = EntityLineEditorSectionView.class)
public class EntityLineEditorSectionPresenter extends ConfigurableBasePresenter<IEntityLineEditorSectionView, EntityLineEditorSectionEventBus> {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private IPresenter<?, ? extends EventBus> headerPresenter;
	private IPresenter<?, ? extends EventBus> contentPresenter;
	private LineEditorComponent componentModel;

	@SuppressWarnings("rawtypes")
	public void onEntityItemEdit(EntityItem item) {
		if (this.contentPresenter != null) {
			((AbstractEntityEditorEventBus) this.contentPresenter.getEventBus()).entityItemEdit(item);
		}
	}

	@SuppressWarnings("rawtypes")
	public void onEntityItemAdded(EntityItem item) {
		if (this.contentPresenter != null) {
			((AbstractEntityEditorEventBus) this.contentPresenter.getEventBus()).entityItemAdded(item);
		}
	}

	@Override
	public void bind() {
		if (this.headerPresenter != null) {
			this.getView().setHeader((Component) this.headerPresenter.getView());
		}
		if (this.contentPresenter != null) {
			this.getView().setContent((Component) this.contentPresenter.getView());
		}
	}

	@Override
	public void configure() {
		this.componentModel = (LineEditorComponent) getConfig().get(IEntityEditorFactory.FACTORY_PARAM_MVP_COMPONENT_MODEL);

		if (componentModel != null) {
			ConfigurablePresenterFactory presenterFactory = (ConfigurablePresenterFactory) getConfig().get(IEntityEditorFactory.FACTORY_PARAM_MVP_PRESENTER_FACTORY);
			
			getConfig().put(IEntityEditorFactory.FACTORY_PARAM_MVP_LINE_EDITOR_SECTION_PRESENTER, this);
			if (componentModel.getContent() instanceof ConXCollapseableSectionForm ||
					componentModel.getContent() instanceof ConXSimpleForm) {
				IPresenter<?, ? extends EventBus> presenter = presenterFactory.createPresenter(EntityLineEditorFormHeaderPresenter.class);
				if (presenter != null) {
					this.headerPresenter = presenter;
				}
			} else if (componentModel.getContent() instanceof ConXDetailTable) {
				IPresenter<?, ? extends EventBus> presenter = presenterFactory.createPresenter(EntityLineEditorGridHeaderPresenter.class);
				if (presenter != null) {
					this.headerPresenter = presenter;
				}
			}
			
			VaadinEntityEditorFactoryImpl entityFactory = new VaadinEntityEditorFactoryImpl(presenterFactory);
			Map<IPresenter<?, ? extends EventBus>, EventBus> resultMap = entityFactory.create(this.componentModel.getContent(), getConfig());
			if (resultMap != null) {
				this.contentPresenter = resultMap.keySet().iterator().next();
			}
		}
	}
	
	public void onSaveForm() {
		if (componentModel.getContent() instanceof ConXCollapseableSectionForm) {
			((EntityLineEditorCollapsibleFormPresenter) this.contentPresenter).getEventBus().saveForm();
		} else if (componentModel.getContent() instanceof ConXSimpleForm) {
			((EntityLineEditorSimpleFormPresenter) this.contentPresenter).getEventBus().saveForm();
		}
	}

	public void onValidateForm() {
		if (componentModel.getContent() instanceof ConXCollapseableSectionForm) {
			((EntityLineEditorCollapsibleFormPresenter) this.contentPresenter).getEventBus().validateForm();
		} else if (componentModel.getContent() instanceof ConXSimpleForm) {
			((EntityLineEditorSimpleFormPresenter) this.contentPresenter).getEventBus().validateForm();
		}
	}

	public void onResetForm() {
		if (componentModel.getContent() instanceof ConXCollapseableSectionForm) {
			((EntityLineEditorCollapsibleFormPresenter) this.contentPresenter).getEventBus().resetForm();
		} else if (componentModel.getContent() instanceof ConXSimpleForm) {
			((EntityLineEditorSimpleFormPresenter) this.contentPresenter).getEventBus().resetForm();
		}
	}

	public void onResizeForm(int newHeight) {
		if (componentModel.getContent() instanceof ConXCollapseableSectionForm) {
			((EntityLineEditorCollapsibleFormPresenter) this.contentPresenter).getEventBus().resizeForm(newHeight);
		} else if (componentModel.getContent() instanceof ConXSimpleForm) {
			((EntityLineEditorSimpleFormPresenter) this.contentPresenter).getEventBus().resizeForm(newHeight);
		}
	}
}
