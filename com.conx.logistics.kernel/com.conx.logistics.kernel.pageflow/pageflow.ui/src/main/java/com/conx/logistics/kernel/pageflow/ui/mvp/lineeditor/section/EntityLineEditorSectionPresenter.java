package com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.presenter.BasePresenter;
import org.vaadin.mvp.presenter.IPresenter;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.conx.logistics.kernel.pageflow.ui.builder.VaadinPageFactoryImpl;
import com.conx.logistics.kernel.pageflow.ui.mvp.attachment.AttachmentEditorPresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.form.collapsible.EntityLineEditorCollapsibleFormPresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.form.header.EntityLineEditorFormHeaderPresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.form.simple.EntityLineEditorSimpleFormPresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.grid.header.EntityLineEditorGridHeaderPresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.view.EntityLineEditorSectionView;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.section.view.IEntityLineEditorSectionView;
import com.conx.logistics.kernel.ui.components.domain.form.ConXCollapseableSectionForm;
import com.conx.logistics.kernel.ui.components.domain.form.ConXSimpleForm;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.LineEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.table.ConXDetailTable;
import com.conx.logistics.kernel.ui.factory.services.IEntityEditorFactory;
import com.vaadin.data.Item;
import com.vaadin.ui.Component;

@Presenter(view = EntityLineEditorSectionView.class)
public class EntityLineEditorSectionPresenter extends BasePresenter<IEntityLineEditorSectionView, EntityLineEditorSectionEventBus> {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private IPresenter<?, ? extends EventBus> headerPresenter;
	private IPresenter<?, ? extends EventBus> contentPresenter;
	private LineEditorComponent componentModel;
	private VaadinPageFactoryImpl factory;

	public void onConfigure(Map<String, Object> params) {
		this.componentModel = (LineEditorComponent) params.get(IEntityEditorFactory.COMPONENT_MODEL);
		this.factory = (VaadinPageFactoryImpl) params.get(IEntityEditorFactory.VAADIN_COMPONENT_FACTORY);
		
		HashMap<String, Object> childParams = null;
		
		if (componentModel != null) {
			params.put(IEntityEditorFactory.FACTORY_PARAM_MVP_LINE_EDITOR_SECTION_PRESENTER, this);
			if (componentModel.getContent() instanceof ConXCollapseableSectionForm ||
					componentModel.getContent() instanceof ConXSimpleForm) {
				IPresenter<?, ? extends EventBus> presenter = this.factory.getPresenterFactory().createPresenter(EntityLineEditorFormHeaderPresenter.class);
				if (presenter != null) {
					this.headerPresenter = presenter;
				}
			} else if (componentModel.getContent() instanceof ConXDetailTable) {
				IPresenter<?, ? extends EventBus> presenter = this.factory.getPresenterFactory().createPresenter(EntityLineEditorGridHeaderPresenter.class);
				if (presenter != null) {
					this.headerPresenter = presenter;
				}
			}
			
			childParams = new HashMap<String, Object>(params);
			this.contentPresenter = this.factory.createPresenter(this.componentModel.getContent(), childParams);
			
			if (this.headerPresenter != null) {
				this.getView().setHeader((Component) this.headerPresenter.getView());
			}
			if (this.contentPresenter != null) {
				this.getView().setContent((Component) this.contentPresenter.getView());
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
	
	public void onSetItemDataSource(Item item) {
		if (this.contentPresenter instanceof AttachmentEditorPresenter) {
			((AttachmentEditorPresenter) this.contentPresenter).onSetItemDataSource(item);
		}
	}
}
