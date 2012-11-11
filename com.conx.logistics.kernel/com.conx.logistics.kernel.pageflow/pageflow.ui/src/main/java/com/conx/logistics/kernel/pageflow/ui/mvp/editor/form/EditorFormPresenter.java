package com.conx.logistics.kernel.pageflow.ui.mvp.editor.form;

import java.util.Map;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.presenter.BasePresenter;
import org.vaadin.mvp.presenter.IPresenter;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.conx.logistics.kernel.pageflow.ui.builder.VaadinPageDataBuilder;
import com.conx.logistics.kernel.pageflow.ui.builder.VaadinPageFactoryImpl;
import com.conx.logistics.kernel.pageflow.ui.ext.form.VaadinCollapsibleConfirmActualsForm;
import com.conx.logistics.kernel.pageflow.ui.ext.form.VaadinConfirmActualsForm;
import com.conx.logistics.kernel.pageflow.ui.ext.mvp.IConfigurablePresenter;
import com.conx.logistics.kernel.pageflow.ui.ext.mvp.IContainerItemPresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.editor.form.header.EditorFormHeaderPresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.editor.form.view.EditorFormView;
import com.conx.logistics.kernel.pageflow.ui.mvp.editor.form.view.IEditorFormView;
import com.conx.logistics.kernel.ui.components.domain.form.CollapsibleConfirmActualsForm;
import com.conx.logistics.kernel.ui.components.domain.form.ConXCollapseableSectionForm;
import com.conx.logistics.kernel.ui.components.domain.form.ConXForm;
import com.conx.logistics.kernel.ui.components.domain.form.ConfirmActualsForm;
import com.conx.logistics.kernel.ui.factory.services.IEntityEditorFactory;
import com.conx.logistics.kernel.ui.forms.vaadin.impl.VaadinCollapsibleSectionForm;
import com.conx.logistics.kernel.ui.forms.vaadin.impl.VaadinForm;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.Component;

@Presenter(view = EditorFormView.class)
public class EditorFormPresenter extends BasePresenter<IEditorFormView, EditorFormEventBus> implements IConfigurablePresenter, IContainerItemPresenter {
	private ConXForm formComponent;
	private VaadinPageFactoryImpl factory;
	private IPresenter<?, ? extends EventBus> headerPresenter;
	private Map<String, Object> config;

	@Override
	public void onConfigure(Map<String, Object> params) throws Exception {
		this.config = params;
		this.formComponent = (ConXForm) params.get(IEntityEditorFactory.FACTORY_PARAM_MVP_COMPONENT_MODEL);
		this.factory = (VaadinPageFactoryImpl) params.get(IEntityEditorFactory.VAADIN_COMPONENT_FACTORY);

		this.headerPresenter = this.factory.getPresenterFactory().createPresenter(EditorFormHeaderPresenter.class);
		((EditorFormHeaderPresenter) this.headerPresenter).attachGridPresenter(this);
		this.getView().setForm(createVaadinForm());
		this.getView().setOwner(this);
	}

	private VaadinForm createVaadinForm() throws Exception {
		if (this.formComponent instanceof ConfirmActualsForm) {
			return new VaadinConfirmActualsForm((ConfirmActualsForm) this.formComponent);
		} else if (this.formComponent instanceof CollapsibleConfirmActualsForm) {
			return new VaadinCollapsibleConfirmActualsForm((CollapsibleConfirmActualsForm) this.formComponent);
		} else if (this.formComponent instanceof ConXCollapseableSectionForm) {
			this.getView().setHeader((Component) this.headerPresenter.getView());
			return new VaadinCollapsibleSectionForm((ConXCollapseableSectionForm) this.formComponent);
		}

		throw new Exception("The provided component model could not be turned into a VaadinForm");
	}

	@Override
	public void onSetItemDataSource(Item item, Container... container) throws Exception {
		if (container.length == 1) {
			VaadinPageDataBuilder.applyItemDataSource(this.getView().getForm(), container[0], item, this.factory.getPresenterFactory(), this.config);
		} else {
			throw new Exception("Could not set item datasource. Expected one container, but got " + container.length);
		}
	}
}
