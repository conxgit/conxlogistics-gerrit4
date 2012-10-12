package com.conx.logistics.kernel.pageflow.ui.builder;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.conx.logistics.kernel.pageflow.ui.ext.form.VaadinCollapsibleConfirmActualsForm;
import com.conx.logistics.kernel.pageflow.ui.ext.form.VaadinConfirmActualsForm;
import com.conx.logistics.kernel.pageflow.ui.mvp.PagePresenter;
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

	public static void applyParamData(PagePresenter source, Component component, Map<String, Object> params) throws ClassNotFoundException {
		if (component instanceof EntitySearchGrid) {
			applyParamDataToEntitySearchGrid(source, params, (EntitySearchGrid) component);
		} else if (component instanceof ConXVerticalSplitPanel) {
			applyParamDataToConXVerticalSplitPanel(source, params, (ConXVerticalSplitPanel) component);
		} else if (component instanceof VaadinConfirmActualsForm) {
			applyParamDataToVaadinConfirmActualsForm(source, params, (VaadinConfirmActualsForm) component);
		} else if (component instanceof VaadinCollapsibleConfirmActualsForm) {
			applyParamDataToVaadinCollapsibleConfirmActualsForm(source, params, (VaadinCollapsibleConfirmActualsForm) component);
		} else if (component instanceof TabSheet) {
			applyParamDataToTabSheet(source, params, (TabSheet) component);
		} else if (component instanceof AbstractComponentContainer) {
			applyParamDataToAbstractComponentContainer(source, params, (AbstractComponentContainer) component);
		}
	}

	private static void applyParamDataToAttachmentView(PagePresenter source, Map<String, Object> params, AttachmentEditorView view) {
		try {
			Class<?> type = view.getComponentModel().getDataSource().getEntityType().getJavaType();
			Collection<String> paramKeys = params.keySet();
			Object paramEntry = null;
			for (String paramKey : paramKeys) {
				paramEntry = params.get(paramKey);
				if (paramEntry != null) {
					if (type.isAssignableFrom(paramEntry.getClass())) {
						BeanItem<BaseEntity> newItem = new BeanItem<BaseEntity>((BaseEntity) paramEntry);
						view.getEventBus().setItemDataSource(newItem);
						return;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void applyParamDataToAbstractComponentContainer(PagePresenter source, Map<String, Object> params, AbstractComponentContainer layout) throws ClassNotFoundException {
		if (layout instanceof AttachmentEditorView) {
			applyParamDataToAttachmentView(source, params, (AttachmentEditorView) layout);
		} else {
			Iterator<Component> componentIterator = layout.getComponentIterator();
			while (componentIterator.hasNext()) {
				applyParamData(source, componentIterator.next(), params);
			}
		}
	}

	private static void applyParamDataToTabSheet(PagePresenter source, Map<String, Object> params, TabSheet tabSheet) throws ClassNotFoundException {
		Iterator<Component> componentIterator = tabSheet.getComponentIterator();
		while (componentIterator.hasNext()) {
			applyParamData(source, componentIterator.next(), params);
		}
	}

	private static void applyParamDataToEntitySearchGrid(PagePresenter source, Map<String, Object> params, EntitySearchGrid searchGrid) throws ClassNotFoundException {
		SearchGrid componentModel = searchGrid.getComponentModel();
		Container container = (Container) source.getContainerProvider().createPersistenceContainer(componentModel.getDataSource().getEntityType().getJavaType());
		searchGrid.setContainerDataSource(container);
	}

	private static void applyParamDataToConXVerticalSplitPanel(PagePresenter source, Map<String, Object> params, ConXVerticalSplitPanel splitPanel) throws ClassNotFoundException {
		applyParamData(source, splitPanel.getFirstComponent(), params);
		applyParamData(source, splitPanel.getSecondComponent(), params);
	}

	private static void applyParamDataToVaadinConfirmActualsForm(PagePresenter source, Map<String, Object> params, VaadinConfirmActualsForm form) throws ClassNotFoundException {
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
			// addNestedProperties(container,
			// form.getComponentModel().getDataSource());
			for (String nestedFieldName : form.getComponentModel().getDataSource().getNestedFieldNames()) {
				container.addNestedContainerProperty(nestedFieldName);
			}
			BeanItem<BaseEntity> baseEntity = container.addBean((BaseEntity) formItemEntity);
			form.setItemDataSource(baseEntity, baseEntity.getItemPropertyIds(), source.getEntityTypeDAOService(), container, source.getContainerProvider());
			form.setTitle(baseEntity.getItemProperty("name").getValue().toString());
		}
	}

	private static void applyParamDataToVaadinCollapsibleConfirmActualsForm(PagePresenter source, Map<String, Object> params, VaadinCollapsibleConfirmActualsForm form) throws ClassNotFoundException {
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
			// addNestedProperties(container,
			// form.getComponentModel().getDataSource());
			for (String nestedFieldName : form.getComponentModel().getDataSource().getNestedFieldNames()) {
				container.addNestedContainerProperty(nestedFieldName);
			}
			BeanItem<BaseEntity> baseEntity = container.addBean((BaseEntity) formItemEntity);
			form.setItemDataSource(baseEntity, baseEntity.getItemPropertyIds(), source.getEntityTypeDAOService(), container, source.getContainerProvider());
			form.setTitle(baseEntity.getItemProperty("name").getValue().toString());
		}
	}
}
