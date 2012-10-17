package com.conx.logistics.kernel.pageflow.ui.builder;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.presenter.PresenterFactory;

import com.conx.logistics.kernel.pageflow.ui.ext.form.VaadinCollapsibleConfirmActualsForm;
import com.conx.logistics.kernel.pageflow.ui.ext.form.VaadinConfirmActualsForm;
import com.conx.logistics.kernel.pageflow.ui.mvp.PagePresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.attachment.AttachmentEditorEventBus;
import com.conx.logistics.kernel.pageflow.ui.mvp.attachment.view.AttachmentEditorView;
import com.conx.logistics.kernel.ui.components.domain.search.SearchGrid;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.search.EntitySearchGrid;
import com.conx.logistics.kernel.ui.vaadin.common.ConXVerticalSplitPanel;
import com.conx.logistics.mdm.domain.BaseEntity;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;

public class VaadinPageDataBuilder {
	public static Set<Object> buildResultData(Component component) {
		HashSet<Object> dataSet = new HashSet<Object>();
		if (component instanceof EntitySearchGrid) {
			dataSet.add(((EntitySearchGrid) component).getSelectedEntity());
		} else if (component instanceof ConXVerticalSplitPanel) {
			ConXVerticalSplitPanel splitPanel = (ConXVerticalSplitPanel) component;
			dataSet.addAll(buildResultData(splitPanel.getFirstComponent()));
			dataSet.addAll(buildResultData(splitPanel.getSecondComponent()));
		} else if (component instanceof VaadinConfirmActualsForm) {
			VaadinConfirmActualsForm form = (VaadinConfirmActualsForm) component;
			dataSet.add(form.getItemEntity());
		} else if (component instanceof VaadinCollapsibleConfirmActualsForm) {
			VaadinCollapsibleConfirmActualsForm form = (VaadinCollapsibleConfirmActualsForm) component;
			dataSet.add(form.getItemEntity());
		}

		return dataSet;
	}

	public static Map<String, Object> buildResultDataMap(Collection<?> data, Map<Class<?>, String> resultKeyMap) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		Collection<Class<?>> types = resultKeyMap.keySet();
		for (Object dataObj : data) {
			for (Class<?> type : types) {
				if (dataObj != null && type != null) {
					if (type.isAssignableFrom(dataObj.getClass())) {
						resultMap.put(resultKeyMap.get(type), dataObj);
					}
				}
			}
		}

		return resultMap;
	}

	public static void applyParamData(PagePresenter source, Component component, Map<String, Object> params, PresenterFactory presenterFactory) throws ClassNotFoundException {
		if (component instanceof EntitySearchGrid) {
			applyParamDataToEntitySearchGrid(source, params, (EntitySearchGrid) component, presenterFactory);
		} else if (component instanceof ConXVerticalSplitPanel) {
			applyParamDataToConXVerticalSplitPanel(source, params, (ConXVerticalSplitPanel) component, presenterFactory);
		} else if (component instanceof VaadinConfirmActualsForm) {
			applyParamDataToVaadinConfirmActualsForm(source, params, (VaadinConfirmActualsForm) component, presenterFactory);
		} else if (component instanceof VaadinCollapsibleConfirmActualsForm) {
			applyParamDataToVaadinCollapsibleConfirmActualsForm(source, params, (VaadinCollapsibleConfirmActualsForm) component, presenterFactory);
		} else if (component instanceof TabSheet) {
			applyParamDataToTabSheet(source, params, (TabSheet) component, presenterFactory);
		} else if (component instanceof AbstractComponentContainer) {
			applyParamDataToAbstractComponentContainer(source, params, (AbstractComponentContainer) component, presenterFactory);
		}
	}

	private static void applyParamDataToAbstractComponentContainer(PagePresenter source, Map<String, Object> params, AbstractComponentContainer layout, PresenterFactory presenterFactory) throws ClassNotFoundException {
		if (layout instanceof AttachmentEditorView) {
//			applyParamDataToAttachmentView(source, params, (AttachmentEditorView) layout);
		} else {
			Iterator<Component> componentIterator = layout.getComponentIterator();
			while (componentIterator.hasNext()) {
				applyParamData(source, componentIterator.next(), params, presenterFactory);
			}
		}
	}

	private static void applyParamDataToTabSheet(PagePresenter source, Map<String, Object> params, TabSheet tabSheet, PresenterFactory presenterFactory) throws ClassNotFoundException {
		Iterator<Component> componentIterator = tabSheet.getComponentIterator();
		while (componentIterator.hasNext()) {
			applyParamData(source, componentIterator.next(), params, presenterFactory);
		}
	}

	private static void applyParamDataToEntitySearchGrid(PagePresenter source, Map<String, Object> params, EntitySearchGrid searchGrid, PresenterFactory presenterFactory) throws ClassNotFoundException {
		SearchGrid componentModel = searchGrid.getComponentModel();
		Container container = (Container) source.getContainerProvider().createPersistenceContainer(componentModel.getDataSource().getEntityType().getJavaType());
		searchGrid.setContainerDataSource(container);
	}

	private static void applyParamDataToConXVerticalSplitPanel(PagePresenter source, Map<String, Object> params, ConXVerticalSplitPanel splitPanel, PresenterFactory presenterFactory) throws ClassNotFoundException {
		applyParamData(source, splitPanel.getFirstComponent(), params, presenterFactory);
		applyParamData(source, splitPanel.getSecondComponent(), params, presenterFactory);
	}

	private static void applyParamDataToVaadinConfirmActualsForm(PagePresenter source, Map<String, Object> params, VaadinConfirmActualsForm form, PresenterFactory presenterFactory) throws ClassNotFoundException {
		Class<?> type = form.getComponentModel().getDataSource().getEntityType().getJavaType();
		Collection<String> paramKeys = params.keySet();
		Object paramEntry = null, formItemEntity = null;
		for (String paramKey : paramKeys) {
			paramEntry = params.get(paramKey);
			if (paramEntry != null) {
				if (type.isAssignableFrom(paramEntry.getClass())) {
					formItemEntity = paramEntry;
					break;
				}
			}
		}

		if (formItemEntity != null) {
			@SuppressWarnings("unchecked")
			BeanItemContainer<BaseEntity> container = (BeanItemContainer<BaseEntity>) source.getContainerProvider().createBeanContainer(type);
			for (String nestedFieldName : form.getComponentModel().getDataSource().getNestedFieldNames()) {
				container.addNestedContainerProperty(nestedFieldName);
			}
			BeanItem<BaseEntity> baseEntity = container.addBean((BaseEntity) formItemEntity);
			form.setItemDataSource(baseEntity, baseEntity.getItemPropertyIds(), source.getEntityTypeDAOService(), container, source.getContainerProvider());
			form.setTitle(baseEntity.getItemProperty("name").getValue().toString());
		}
	}

	private static void applyParamDataToVaadinCollapsibleConfirmActualsForm(PagePresenter source, Map<String, Object> params, VaadinCollapsibleConfirmActualsForm form, PresenterFactory presenterFactory) throws ClassNotFoundException {
		Class<?> type = form.getComponentModel().getDataSource().getEntityType().getJavaType();
		Collection<String> paramKeys = params.keySet();
		Object paramEntry = null, formItemEntity = null;
		for (String paramKey : paramKeys) {
			paramEntry = params.get(paramKey);
			if (paramEntry != null) {
				if (type.isAssignableFrom(paramEntry.getClass())) {
					formItemEntity = paramEntry;
					break;
				}
			}
		}

		if (formItemEntity != null) {
			@SuppressWarnings("unchecked")
			BeanItemContainer<BaseEntity> container = (BeanItemContainer<BaseEntity>) source.getContainerProvider().createBeanContainer(type);
			for (String nestedFieldName : form.getComponentModel().getDataSource().getNestedFieldNames()) {
				container.addNestedContainerProperty(nestedFieldName);
			}
			BeanItem<BaseEntity> baseEntity = container.addBean((BaseEntity) formItemEntity);
			form.setItemDataSource(baseEntity, baseEntity.getItemPropertyIds(), source.getEntityTypeDAOService(), container, source.getContainerProvider());
			form.setTitle(baseEntity.getItemProperty("name").getValue().toString());
			
			AttachmentEditorEventBus eventBus = presenterFactory.getEventBusManager().getEventBus(AttachmentEditorEventBus.class);
			if (eventBus != null) {
				eventBus.setItemDataSource(baseEntity);
			}
		}
	}
}
