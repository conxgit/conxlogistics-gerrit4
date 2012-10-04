package com.conx.logistics.kernel.pageflow.ui.builder;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.conx.logistics.kernel.datasource.domain.DataSource;
import com.conx.logistics.kernel.pageflow.ui.ext.form.VaadinCollapsibleConfirmActualsForm;
import com.conx.logistics.kernel.pageflow.ui.ext.form.VaadinConfirmActualsForm;
import com.conx.logistics.kernel.pageflow.ui.mvp.PagePresenter;
import com.conx.logistics.kernel.ui.components.domain.search.SearchGrid;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.search.EntitySearchGrid;
import com.conx.logistics.kernel.ui.vaadin.common.ConXVerticalSplitPanel;
import com.conx.logistics.mdm.domain.BaseEntity;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Component;

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
				if (type.isAssignableFrom(dataObj.getClass())) {
					resultMap.put(resultKeyMap.get(type), dataObj);
				}
			}
		}
		
		return resultMap;
	}
	
	public static void applyParamData(PagePresenter source, Component component, Map<String, Object> params) throws ClassNotFoundException {
		if (component instanceof EntitySearchGrid) {
			EntitySearchGrid searchGrid = (EntitySearchGrid) component;
			SearchGrid componentModel = searchGrid.getComponentModel();
			Container container = (Container) source.getContainerProvider().createPersistenceContainer(componentModel.getDataSource().getEntityType().getJavaType());
			searchGrid.setContainerDataSource(container);
		} else if (component instanceof ConXVerticalSplitPanel) {
			ConXVerticalSplitPanel splitPanel = (ConXVerticalSplitPanel) component;
			applyParamData(source, splitPanel.getFirstComponent(), params);
			applyParamData(source, splitPanel.getSecondComponent(), params);
		} else if (component instanceof VaadinConfirmActualsForm) {
			VaadinConfirmActualsForm form = (VaadinConfirmActualsForm) component;
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
				addNestedProperties(container, form.getComponentModel().getDataSource());
				BeanItem<BaseEntity> baseEntity = container.addBean((BaseEntity) formItemEntity);
				form.setItemDataSource(baseEntity, baseEntity.getItemPropertyIds(), source.getEntityTypeDAOService(), container, source.getContainerProvider().getEmf());
				form.setTitle(baseEntity.getItemProperty("name").getValue().toString());
			}
		} else if (component instanceof VaadinCollapsibleConfirmActualsForm) {
			VaadinCollapsibleConfirmActualsForm form = (VaadinCollapsibleConfirmActualsForm) component;
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
				addNestedProperties(container, form.getComponentModel().getDataSource());
				BeanItem<BaseEntity> baseEntity = container.addBean((BaseEntity) formItemEntity);
				form.setItemDataSource(baseEntity, baseEntity.getItemPropertyIds(), source.getEntityTypeDAOService(), container, source.getContainerProvider().getEmf());
				form.setTitle(baseEntity.getItemProperty("name").getValue().toString());
			}
		}
	}
	
	private static void addNestedProperties(BeanItemContainer<?> container, DataSource ds) {
		Set<String> nestedFieldNames = ds.getNestedFieldNames();
		for (String nestedFieldName : nestedFieldNames) {
			container.addNestedContainerProperty(nestedFieldName);
		}
	}
}
