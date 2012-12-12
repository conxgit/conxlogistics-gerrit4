package com.conx.logistics.kernel.pageflow.ui.builder;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.vaadin.mvp.eventbus.EventBusManager;
import org.vaadin.mvp.presenter.PresenterFactory;

import com.conx.logistics.app.whse.im.dao.services.IStockItemDAOService;
import com.conx.logistics.app.whse.im.domain.stockitem.StockItem;
import com.conx.logistics.app.whse.rcv.asn.dao.services.IASNDAOService;
import com.conx.logistics.app.whse.rcv.asn.domain.ASN;
import com.conx.logistics.app.whse.rcv.asn.domain.ASNLine;
import com.conx.logistics.app.whse.rcv.rcv.dao.services.IArrivalDAOService;
import com.conx.logistics.app.whse.rcv.rcv.dao.services.IArrivalReceiptDAOService;
import com.conx.logistics.app.whse.rcv.rcv.domain.Arrival;
import com.conx.logistics.app.whse.rcv.rcv.domain.ArrivalReceipt;
import com.conx.logistics.app.whse.rcv.rcv.domain.ArrivalReceiptLine;
import com.conx.logistics.app.whse.rcv.rcv.domain.Receive;
import com.conx.logistics.app.whse.rcv.rcv.domain.ReceiveLine;
import com.conx.logistics.app.whse.rcv.rcv.domain.types.RECEIVELINESTATUS;
import com.conx.logistics.kernel.datasource.domain.DataSource;
import com.conx.logistics.kernel.metamodel.dao.services.IEntityTypeDAOService;
import com.conx.logistics.kernel.pageflow.services.IPageComponent;
import com.conx.logistics.kernel.pageflow.ui.ext.form.VaadinCollapsibleConfirmActualsForm;
import com.conx.logistics.kernel.pageflow.ui.ext.form.VaadinConfirmActualsForm;
import com.conx.logistics.kernel.pageflow.ui.ext.grid.VaadinMatchGrid;
import com.conx.logistics.kernel.pageflow.ui.ext.grid.VaadinMatchGrid.IMatchListener;
import com.conx.logistics.kernel.pageflow.ui.ext.mvp.IVaadinDataComponent;
import com.conx.logistics.kernel.pageflow.ui.mvp.editor.MasterSectionEventBus;
import com.conx.logistics.kernel.pageflow.ui.mvp.editor.multilevel.MultiLevelEditorPresenter;
import com.conx.logistics.kernel.pageflow.ui.mvp.editor.multilevel.view.MultiLevelEditorView;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.EntityLineEditorEventBus;
import com.conx.logistics.kernel.pageflow.ui.mvp.lineeditor.view.EntityLineEditorView;
import com.conx.logistics.kernel.persistence.services.IEntityContainerProvider;
import com.conx.logistics.kernel.ui.components.domain.search.SearchGrid;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.search.EntitySearchGrid;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.table.EntityEditorGrid;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.table.EntityEditorGrid.ISelectListener;
import com.conx.logistics.kernel.ui.factory.services.IEntityEditorFactory;
import com.conx.logistics.kernel.ui.factory.services.data.IDAOProvider;
import com.conx.logistics.kernel.ui.forms.vaadin.impl.VaadinCollapsibleSectionForm;
import com.conx.logistics.kernel.ui.forms.vaadin.impl.VaadinForm;
import com.conx.logistics.kernel.ui.vaadin.common.ConXVerticalSplitPanel;
import com.conx.logistics.mdm.domain.BaseEntity;
import com.conx.logistics.mdm.domain.metamodel.EntityType;
import com.conx.logistics.mdm.domain.metamodel.EntityTypeAttribute;
import com.conx.logistics.mdm.domain.metamodel.PluralAttribute;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerItem;
import com.vaadin.addon.jpacontainer.filter.Filters;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;

public class VaadinPageDataBuilder {
	public static Set<Object> buildResultData(Component component) {
		HashSet<Object> dataSet = new HashSet<Object>();
		if (component instanceof IVaadinDataComponent) {
			Object data = ((IVaadinDataComponent) component).getData();
			if (data instanceof Collection) {
				dataSet.addAll((Collection<?>) data);
			} else if (data instanceof Object) {
				dataSet.add(data);
			}
		} else if (component instanceof EntitySearchGrid) {
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
		} else {
			if (component instanceof AbstractOrderedLayout) {
				Iterator<Component> iterator = ((AbstractOrderedLayout) component).getComponentIterator();
				Component nextComponent = null;
				while (iterator.hasNext()) {
					nextComponent = iterator.next();
					dataSet.addAll(buildResultData(nextComponent));
				}
			}
		}

		return dataSet;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, Object> buildResultDataMap(Map<String, Object> parameterData, Collection<?> data,
			Map<Class<?>, String> resultKeyMap) {
		Set<String> parameterDataKeySet = parameterData.keySet();
		for (String parameterDataKey : parameterDataKeySet) {
			((Collection) data).add(parameterData.get(parameterDataKey));
		}

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

	public static void applyParamData(Map<String, Object> config, Component component, Map<String, Object> params,
			PresenterFactory presenterFactory) throws Exception {
		if (component instanceof EntitySearchGrid) {
			applyParamDataToEntitySearchGrid(config, params, (EntitySearchGrid) component, presenterFactory);
		} else if (component instanceof VaadinConfirmActualsForm) {
			applyParamDataToVaadinConfirmActualsForm(config, params, (VaadinConfirmActualsForm) component, presenterFactory);
		} else if (component instanceof VaadinCollapsibleConfirmActualsForm) {
			applyParamDataToVaadinCollapsibleConfirmActualsForm(config, params, (VaadinCollapsibleConfirmActualsForm) component,
					presenterFactory);
		} else if (component instanceof VaadinCollapsibleSectionForm) {
			applyParamDataToVaadinCollapsibleSectionForm(config, params, (VaadinCollapsibleSectionForm) component, presenterFactory);
		} else if (component instanceof MultiLevelEditorView) {
			applyParamDataToMultiLevelEditorView(config, params, (MultiLevelEditorView) component);
		} else {
			// Component containers should always be picked last
			if (component instanceof AbstractComponentContainer) {
				applyParamDataToAbstractComponentContainer(config, params, (AbstractComponentContainer) component, presenterFactory);
			} else if (component instanceof TabSheet) {
				applyParamDataToTabSheet(config, params, (TabSheet) component, presenterFactory);
			} else if (component instanceof ConXVerticalSplitPanel) {
				applyParamDataToConXVerticalSplitPanel(config, params, (ConXVerticalSplitPanel) component, presenterFactory);
			}
		}
	}

	public static void applyItemDataSource(Component component, Container itemContainer, Item item,
			final PresenterFactory presenterFactory, Map<String, Object> config) throws Exception {
		applyItemDataSource(true, component, itemContainer, item, presenterFactory, config);
	}

	public static void applyItemDataSource(boolean isEventDeclaritive, Component component, Container itemContainer, Item item,
			final PresenterFactory presenterFactory, Map<String, Object> config) throws Exception {
		if (component instanceof ConXVerticalSplitPanel) {
			applyItemDataSource(isEventDeclaritive, ((ConXVerticalSplitPanel) component).getFirstComponent(), itemContainer, item,
					presenterFactory, config);
			applyItemDataSource(isEventDeclaritive, ((ConXVerticalSplitPanel) component).getSecondComponent(), itemContainer, item,
					presenterFactory, config);
		} else if (component instanceof VaadinForm) {
			if (item instanceof BeanItem && itemContainer instanceof BeanItemContainer) {
				IDAOProvider daoProvider = (IDAOProvider) config.get(IPageComponent.DAO_PROVIDER);
				IEntityContainerProvider containerProvider = (IEntityContainerProvider) config
						.get(IPageComponent.ENTITY_CONTAINER_PROVIDER);

				if (daoProvider == null) {
					throw new Exception("IDAOProvider was not supplied by the config map.");
				} else if (containerProvider == null) {
					throw new Exception("IEntityContainerProvider was not supplied by the config map.");
				}

				IEntityTypeDAOService entityTypeDao = daoProvider.provideByDAOClass(IEntityTypeDAOService.class);

				if (entityTypeDao == null) {
					throw new Exception("IEntityTypeDAOService was not supplied by the DAO Provider.");
				}

				EventBusManager ebm = null;
				if (config.get(IPageComponent.EVENT_BUS_MANAGER) != null) {
					ebm = (EventBusManager) config.get(IPageComponent.EVENT_BUS_MANAGER);
				} else {
					ebm = presenterFactory.getEventBusManager();
				}

				applyItemDataSourceToVaadinForm(isEventDeclaritive, (VaadinForm) component, (BeanItem<?>) item,
						(BeanItemContainer<?>) itemContainer, config, ebm);
			} else {
				throw new Exception(
						"Could not apply item data source to VaadinForm since item and itemContainer were not of type BeanItem and BeanItemContainer.");
			}
		} else if (component instanceof EntityLineEditorView) {
			// Halt the tree at this point
		} else if (component instanceof VaadinMatchGrid) {
			IEntityContainerProvider provider = (IEntityContainerProvider) config.get(IPageComponent.ENTITY_CONTAINER_PROVIDER);
			IDAOProvider daoProvider = (IDAOProvider) config.get(IPageComponent.DAO_PROVIDER);

			if (provider == null) {
				throw new Exception("IEntityContainerProvider was not supplied by the config map.");
			} else if (daoProvider == null) {
				throw new Exception("IDAOProvider was not supplied by the config map.");
			}

			applyItemDataSourceToVaadinMatchGrid(item, component, presenterFactory, provider, daoProvider);
		} else if (component instanceof EntityEditorGrid) {
			IEntityContainerProvider provider = (IEntityContainerProvider) config.get(IPageComponent.ENTITY_CONTAINER_PROVIDER);
			IDAOProvider daoProvider = (IDAOProvider) config.get(IPageComponent.DAO_PROVIDER);

			if (provider == null) {
				throw new Exception("IEntityContainerProvider was not supplied by the config map.");
			} else if (daoProvider == null) {
				throw new Exception("IDAOProvider was not supplied by the config map.");
			}

			applyItemDataSourceToVaadinMatchGrid(item, component, presenterFactory, provider, daoProvider);
		} else {
			if (component instanceof TabSheet) {
				Iterator<Component> componentIterator = ((TabSheet) component).getComponentIterator();
				while (componentIterator.hasNext()) {
					applyItemDataSource(isEventDeclaritive, componentIterator.next(), itemContainer, item, presenterFactory, config);
				}
			} else if (component instanceof AbstractComponentContainer) {
				Iterator<Component> componentIterator = ((AbstractComponentContainer) component).getComponentIterator();
				while (componentIterator.hasNext()) {
					applyItemDataSource(isEventDeclaritive, componentIterator.next(), itemContainer, item, presenterFactory, config);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static void applyItemDataSourceToVaadinMatchGrid(Item item, Component component, final PresenterFactory presenterFactory,
			IEntityContainerProvider containerProvider, final IDAOProvider daoProvider) throws ClassNotFoundException {
		@SuppressWarnings({ "rawtypes" })
		final Object itemBean = (item instanceof JPAContainerItem<?>) ? ((JPAContainerItem) item).getEntity()
				: (item instanceof BeanItem<?>) ? ((BeanItem) item).getBean() : null;
		((VaadinMatchGrid) component).setItemBean(itemBean);
		((VaadinMatchGrid) component).setDaoProvider(daoProvider);

		final BeanItemContainer<BaseEntity> matchedContainer = (BeanItemContainer<BaseEntity>) containerProvider
				.createBeanContainer(((VaadinMatchGrid) component).getMatchedContainerType());
		for (String nestedFieldName : ((VaadinMatchGrid) component).getComponentModel().getMatchedDataSource().getNestedFieldNames()) {
			matchedContainer.addNestedContainerProperty(nestedFieldName);
		}

		@SuppressWarnings("rawtypes")
		JPAContainer unmatchedContainer = (JPAContainer) containerProvider.createPersistenceContainer(((VaadinMatchGrid) component)
				.getUnmatchedContainerType());
		for (String nestedFieldName : ((VaadinMatchGrid) component).getComponentModel().getUnmatchedDataSource().getNestedFieldNames()) {
			unmatchedContainer.addNestedContainerProperty(nestedFieldName);
		}

		((VaadinMatchGrid) component).setUnMatchedContainer(unmatchedContainer);
		((VaadinMatchGrid) component).setMatchedContainer(matchedContainer);
		((VaadinMatchGrid) component).addListener(new IMatchListener() {

			@Override
			public void onUnmatch(Item matchedItemId) {
				EntityLineEditorEventBus eventBus = presenterFactory.getEventBusManager().getEventBus(EntityLineEditorEventBus.class);
				if (eventBus != null) {
					eventBus.setItemDataSource(null);
				}
			}

			@Override
			public void onMatch(Item matchedItem, Item matchedItemParent) {
				EntityLineEditorEventBus eventBus = presenterFactory.getEventBusManager().getEventBus(EntityLineEditorEventBus.class);
				if (eventBus != null) {
					eventBus.setItemDataSource(matchedItem, matchedContainer);
				}
			}
		});
		((VaadinMatchGrid) component).addListener(new ISelectListener() {

			@Override
			public void onSelect(Item item) {
				EntityLineEditorEventBus eventBus = presenterFactory.getEventBusManager().getEventBus(EntityLineEditorEventBus.class);
				if (eventBus != null) {
					eventBus.setItemDataSource(item, matchedContainer);
				}
			}
		});

		if (((VaadinMatchGrid) component).isDynamic()) {
			try {
				((VaadinMatchGrid) component).addParentConsumptionFilter(Filters.eq("status", RECEIVELINESTATUS.ARRIVING));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (ReceiveLine.class.isAssignableFrom(((VaadinMatchGrid) component).getUnmatchedContainerType())) {
				try {
					((VaadinMatchGrid) component).addParentConsumptionFilter(Filters.not(Filters.eq("status", RECEIVELINESTATUS.ARRIVED)));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void applyParamDataToMultiLevelEditorView(Map<String, Object> config, Map<String, Object> params,
			MultiLevelEditorView view) throws Exception {
		IEntityContainerProvider provider = (IEntityContainerProvider) config.get(IPageComponent.ENTITY_CONTAINER_PROVIDER);

		if (provider == null) {
			throw new Exception("IEntityContainerProvider was not supplied by the config map.");
		}

		DataSource ds = ((MultiLevelEditorPresenter) view.getOwner()).getCurrentEditorComponentModel().getMasterComponent().getDataSource();
		Class<?> type = ds.getEntityType().getJavaType();
		Object formItemEntity = getParameterByClass(params, type);
		if (formItemEntity != null) {
			@SuppressWarnings("unchecked")
			BeanItemContainer<BaseEntity> container = (BeanItemContainer<BaseEntity>) provider.createBeanContainer(type);
			for (String nestedFieldName : ds.getNestedFieldNames()) {
				container.addNestedContainerProperty(nestedFieldName);
			}
			BeanItem<BaseEntity> baseEntity = container.addBean((BaseEntity) formItemEntity);
			((MultiLevelEditorPresenter) view.getOwner()).onSetItemDataSource(baseEntity, container);
		}
	}

	private static void applyParamDataToVaadinCollapsibleSectionForm(Map<String, Object> config, Map<String, Object> params,
			VaadinCollapsibleSectionForm form, PresenterFactory presenterFactory) throws Exception {
		IEntityContainerProvider provider = (IEntityContainerProvider) config.get(IPageComponent.ENTITY_CONTAINER_PROVIDER);

		if (provider == null) {
			throw new Exception("IEntityContainerProvider was not supplied by the config map.");
		}

		Class<?> type = form.getComponentModel().getDataSource().getEntityType().getJavaType();
		Object formItemEntity = getParameterByClass(params, type);
		if (formItemEntity != null) {
			@SuppressWarnings("unchecked")
			BeanItemContainer<BaseEntity> container = (BeanItemContainer<BaseEntity>) provider.createBeanContainer(type);
			for (String nestedFieldName : form.getComponentModel().getDataSource().getNestedFieldNames()) {
				container.addNestedContainerProperty(nestedFieldName);
			}
			BeanItem<BaseEntity> baseEntity = container.addBean((BaseEntity) formItemEntity);
			applyItemDataSourceToVaadinForm(true, form, baseEntity, container, config, presenterFactory.getEventBusManager());
		}
	}

	private static void applyParamDataToAbstractComponentContainer(Map<String, Object> config, Map<String, Object> params,
			AbstractComponentContainer layout, PresenterFactory presenterFactory) throws Exception {
		Iterator<Component> componentIterator = layout.getComponentIterator();
		while (componentIterator.hasNext()) {
			try {
				applyParamData(config, componentIterator.next(), params, presenterFactory);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void applyParamDataToTabSheet(Map<String, Object> config, Map<String, Object> params, TabSheet tabSheet,
			PresenterFactory presenterFactory) throws Exception {
		Iterator<Component> componentIterator = tabSheet.getComponentIterator();
		while (componentIterator.hasNext()) {
			try {
				applyParamData(config, componentIterator.next(), params, presenterFactory);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void applyParamDataToEntitySearchGrid(Map<String, Object> config, Map<String, Object> params,
			EntitySearchGrid searchGrid, PresenterFactory presenterFactory) throws Exception {
		IEntityContainerProvider provider = (IEntityContainerProvider) config.get(IPageComponent.ENTITY_CONTAINER_PROVIDER);

		if (provider == null) {
			throw new Exception("IEntityContainerProvider was not supplied by the config map.");
		}

		SearchGrid componentModel = searchGrid.getComponentModel();
		Container container = (Container) provider.createPersistenceContainer(componentModel.getDataSource().getEntityType().getJavaType());
		searchGrid.setContainerDataSource(container);
	}

	private static void applyParamDataToConXVerticalSplitPanel(Map<String, Object> config, Map<String, Object> params,
			ConXVerticalSplitPanel splitPanel, PresenterFactory presenterFactory) throws Exception {
		try {
			applyParamData(config, splitPanel.getFirstComponent(), params, presenterFactory);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			applyParamData(config, splitPanel.getSecondComponent(), params, presenterFactory);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void applyParamDataToVaadinConfirmActualsForm(Map<String, Object> config, Map<String, Object> params,
			VaadinConfirmActualsForm form, PresenterFactory presenterFactory) throws Exception {
		IEntityContainerProvider provider = (IEntityContainerProvider) config.get(IPageComponent.ENTITY_CONTAINER_PROVIDER);

		if (provider == null) {
			throw new Exception("IEntityContainerProvider was not supplied by the config map.");
		}

		Class<?> type = form.getComponentModel().getDataSource().getEntityType().getJavaType();
		Object formItemEntity = getParameterByClass(params, type);
		if (formItemEntity != null) {
			@SuppressWarnings("unchecked")
			BeanItemContainer<BaseEntity> container = (BeanItemContainer<BaseEntity>) provider.createBeanContainer(type);
			for (String nestedFieldName : form.getComponentModel().getDataSource().getNestedFieldNames()) {
				container.addNestedContainerProperty(nestedFieldName);
			}
			BeanItem<BaseEntity> baseEntity = container.addBean((BaseEntity) formItemEntity);
			applyItemDataSourceToVaadinForm(true, form, baseEntity, container, config, presenterFactory.getEventBusManager());
		}
	}

	private static void applyParamDataToVaadinCollapsibleConfirmActualsForm(Map<String, Object> config, Map<String, Object> params,
			VaadinCollapsibleConfirmActualsForm form, PresenterFactory presenterFactory) throws Exception {
		IEntityContainerProvider provider = (IEntityContainerProvider) config.get(IPageComponent.ENTITY_CONTAINER_PROVIDER);

		if (provider == null) {
			throw new Exception("IEntityContainerProvider was not supplied by the config map.");
		}

		Class<?> type = form.getComponentModel().getDataSource().getEntityType().getJavaType();
		Object formItemEntity = getParameterByClass(params, type);
		if (formItemEntity != null) {
			@SuppressWarnings("unchecked")
			BeanItemContainer<BaseEntity> container = (BeanItemContainer<BaseEntity>) provider.createBeanContainer(type);
			for (String nestedFieldName : form.getComponentModel().getDataSource().getNestedFieldNames()) {
				container.addNestedContainerProperty(nestedFieldName);
			}
			BeanItem<BaseEntity> baseEntity = container.addBean((BaseEntity) formItemEntity);
			applyItemDataSourceToVaadinForm(true, form, baseEntity, container, config, presenterFactory.getEventBusManager());
		}
	}

	/**************************************************************************/
	/**************************** UTILITY METHODS *****************************/
	/**************************************************************************/
	private static void applyItemDataSourceToVaadinForm(boolean isEventDeclaritive, VaadinForm form, BeanItem<?> item,
			BeanItemContainer<?> container, Map<String, Object> config, EventBusManager ebm) throws Exception {
		IDAOProvider daoProvider = (IDAOProvider) config.get(IPageComponent.DAO_PROVIDER);
		IEntityContainerProvider containerProvider = (IEntityContainerProvider) config.get(IPageComponent.ENTITY_CONTAINER_PROVIDER);

		if (daoProvider == null) {
			throw new Exception("IDAOProvider was not supplied by the config map.");
		} else if (containerProvider == null) {
			throw new Exception("IEntityContainerProvider was not supplied by the config map.");
		}

		IEntityTypeDAOService entityTypeDao = daoProvider.provideByDAOClass(IEntityTypeDAOService.class);

		if (entityTypeDao == null) {
			throw new Exception("IEntityTypeDAOService was not supplied by the DAO Provider.");
		}

		form.setItemDataSource(item, item.getItemPropertyIds(), entityTypeDao, container, containerProvider);

		String title = typeToTitle(item.getBean().getClass());
		if (item.getItemProperty("name") != null && item.getItemProperty("name").getValue() != null) {
			title += " (" + item.getItemProperty("name").getValue().toString() + ")";
		}
		form.setTitle(title);

		if (isEventDeclaritive) {
			EntityLineEditorEventBus entityLineEditorEventBus = ebm.getEventBus(EntityLineEditorEventBus.class);
			if (entityLineEditorEventBus != null) {
				entityLineEditorEventBus.setItemDataSource(item, container);
			}
		}
	}

	private static Object getParameterByClass(Map<String, Object> params, Class<?> type) {
		Collection<String> paramKeys = params.keySet();
		Object paramEntry = null;
		for (String paramKey : paramKeys) {
			paramEntry = params.get(paramKey);
			if (paramEntry != null) {
				if (type.isAssignableFrom(paramEntry.getClass())) {
					return paramEntry;
				}
			}
		}

		return null;
	}

	private static Map<Class<?>, Collection<Object>> buildParamInstanceMap(Object[] parentInstances) {
		HashMap<Class<?>, Collection<Object>> paramInstanceMap = new HashMap<Class<?>, Collection<Object>>();
		for (Object parentInstance : parentInstances) {
			if (parentInstance != null) {
				Collection<Object> collection = paramInstanceMap.get(parentInstance.getClass());
				if (collection == null) {
					collection = new LinkedList<Object>();
					paramInstanceMap.put(parentInstance.getClass(), collection);
				}

				if (!collection.contains(parentInstance)) {
					collection.add(parentInstance);
				}
			}
		}
		return paramInstanceMap;
	}

	// FIXME Add an applyItemDataSource impl for master section grids
	@SuppressWarnings("unused")
	private static PluralAttribute provideGridAttribute(Class<?> propertyType, Object bean, IDAOProvider daoProvider) throws Exception {
		PluralAttribute gridAttribute = null;
		EntityType beanEntityType = daoProvider.provideByDAOClass(IEntityTypeDAOService.class).provide(bean.getClass()), attributeType = null;
		Set<EntityTypeAttribute> beanAttributes = beanEntityType.getAllDeclaredAttributes();
		for (EntityTypeAttribute attribute : beanAttributes) {
			if (attribute.getAttribute() instanceof PluralAttribute) {
				attributeType = attribute.getAttribute().getEntityType();
				if (attributeType != null) {
					if (propertyType.isAssignableFrom(attributeType.getJavaType())) {
						gridAttribute = (PluralAttribute) attribute.getAttribute();
						return gridAttribute;
					}
				}
			}
		}
		return gridAttribute;
	}

	public static void saveNewInstance(Object instance, IDAOProvider daoProvider, EventBusManager eventBusManager,
			Map<String, Object> config, Object... parentInstances) throws Exception {
		if (instance instanceof BaseEntity && ((BaseEntity) instance).getId() == null) {
			MultiLevelEditorPresenter mlePresenter = (MultiLevelEditorPresenter) config
					.get(IEntityEditorFactory.FACTORY_PARAM_MVP_CURRENT_MLENTITY_EDITOR_PRESENTER);
			if (mlePresenter == null) {
				throw new Exception("The MLE presenter was null, so the entity could not be saved.");
			}

			Item currentEditorItemDataSource = mlePresenter.getCurrentItemDataSource();
			Object bean = null;
			if (currentEditorItemDataSource instanceof BeanItem<?>) {
				bean = ((BeanItem<?>) currentEditorItemDataSource).getBean();
			} else if (currentEditorItemDataSource instanceof JPAContainerItem<?>) {
				bean = ((JPAContainerItem<?>) currentEditorItemDataSource).getEntity();
			} else {
				throw new Exception("The current MLE presenter's item datasource was the wrong type.");
			}

			if (bean == null) {
				throw new Exception("The bean of the MLE presenter's item datasource was null.");
			} else {
				instance = saveInstance(instance, daoProvider, bean);
			}

			MasterSectionEventBus masterSectionEventBus = eventBusManager.getEventBus(MasterSectionEventBus.class);
			if (masterSectionEventBus != null) {
				masterSectionEventBus.addNewBeanItem(instance);
			} else {
				throw new Exception("EntityLineEditorEventBus could not be fetched from the event bus manager.");
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T saveInstance(T instance, IDAOProvider daoProvider, Object... parentInstances) throws Exception {
		if (daoProvider != null) {
			if (instance instanceof StockItem) {
				if (((BaseEntity) instance).getId() == null) {
					Map<Class<?>, Collection<Object>> paramInstanceMap = buildParamInstanceMap(parentInstances);
					if (parentInstances.length == 2) {
						if (paramInstanceMap.get(ReceiveLine.class) != null && paramInstanceMap.get(ArrivalReceiptLine.class) != null) {
							DefaultTransactionDefinition def = new DefaultTransactionDefinition();
							def.setName("pageflow.ui.data");
							def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
							TransactionStatus status = daoProvider.provideByDAOClass(PlatformTransactionManager.class).getTransaction(def);
							T result = null;
							try {
								result = (T) daoProvider.provideByDAOClass(IStockItemDAOService.class).addOneOfGroup(
										(StockItem) instance,
										((ReceiveLine) paramInstanceMap.get(ReceiveLine.class).iterator().next()).getId(),
										((ArrivalReceiptLine) paramInstanceMap.get(ArrivalReceiptLine.class).iterator().next())
												.getParentArrivalReceipt().getId(),
										((ArrivalReceiptLine) paramInstanceMap.get(ArrivalReceiptLine.class).iterator().next()).getId());
								if (!status.isCompleted()) {
									daoProvider.provideByDAOClass(PlatformTransactionManager.class).commit(status);
								}
							} catch (Exception e) {
								daoProvider.provideByDAOClass(PlatformTransactionManager.class).rollback(status);
								e.printStackTrace();
							}
							return result;
						} else {
							throw new IllegalArgumentException("saveInstance(StockItem) needs a ReceiveLine and an ArrivalReceipt.");
						}
					} else if (parentInstances.length == 1) {
						if (paramInstanceMap.get(ArrivalReceiptLine.class) != null) {
							DefaultTransactionDefinition def = new DefaultTransactionDefinition();
							def.setName("pageflow.ui.data");
							def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
							TransactionStatus status = daoProvider.provideByDAOClass(PlatformTransactionManager.class).getTransaction(def);
							T result = null;
							try {
								result = (T) daoProvider.provideByDAOClass(IStockItemDAOService.class).addDynamicStockItem(
										(StockItem) instance,
										((ArrivalReceiptLine) paramInstanceMap.get(ArrivalReceiptLine.class).iterator().next())
												.getParentArrivalReceipt().getId(),
										((ArrivalReceiptLine) paramInstanceMap.get(ArrivalReceiptLine.class).iterator().next()).getId());
								if (!status.isCompleted()) {
									daoProvider.provideByDAOClass(PlatformTransactionManager.class).commit(status);
								}
							} catch (Exception e) {
								daoProvider.provideByDAOClass(PlatformTransactionManager.class).rollback(status);
								e.printStackTrace();
							}
							return result;
						}
					} else {
						throw new IllegalArgumentException("saveInstance(StockItem) needs a ReceiveLine and an ArrivalReceipt OR just an ArrivalReceipt.");
					}
				} else {
					daoProvider.provideByDAOClass(IStockItemDAOService.class).update((StockItem) instance);
				}
			} else if (instance instanceof Arrival) {
				if (((BaseEntity) instance).getId() == null) {
					if (parentInstances.length == 1) {
						if (parentInstances[0] instanceof Receive) {
							DefaultTransactionDefinition def = new DefaultTransactionDefinition();
							def.setName("pageflow.ui.data");
							def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
							TransactionStatus status = daoProvider.provideByDAOClass(PlatformTransactionManager.class).getTransaction(def);
							T result = null;
							try {
								result = (T) daoProvider.provideByDAOClass(IArrivalDAOService.class).add((Arrival) instance,
										(Receive) parentInstances[0]);
								daoProvider.provideByDAOClass(PlatformTransactionManager.class).commit(status);
							} catch (Exception e) {
								daoProvider.provideByDAOClass(PlatformTransactionManager.class).rollback(status);
								e.printStackTrace();
							}
							return result;
						} else {
							throw new IllegalArgumentException("saveInstance(Arrival) needs a Receive.");
						}
					} else {
						throw new IllegalArgumentException("saveInstance(Arrival) needs a Receive.");
					}
				} else {
					daoProvider.provideByDAOClass(IArrivalDAOService.class).update((Arrival) instance);
				}
			} else if (instance instanceof ArrivalReceipt) {
				if (((BaseEntity) instance).getId() == null) {
					if (parentInstances.length == 1) {
						if (parentInstances[0] instanceof Arrival) {
							DefaultTransactionDefinition def = new DefaultTransactionDefinition();
							def.setName("pageflow.ui.data");
							def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
							TransactionStatus status = daoProvider.provideByDAOClass(PlatformTransactionManager.class).getTransaction(def);
							T result = null;
							try {
								result = (T) daoProvider.provideByDAOClass(IArrivalReceiptDAOService.class).add(
										((Arrival) parentInstances[0]).getId(), (ArrivalReceipt) instance);
								daoProvider.provideByDAOClass(PlatformTransactionManager.class).commit(status);
							} catch (Exception e) {
								daoProvider.provideByDAOClass(PlatformTransactionManager.class).rollback(status);
								e.printStackTrace();
							}
							return result;
						} else {
							throw new IllegalArgumentException("saveInstance(ArrivalReceipt) needs an Arrival.");
						}
					} else {
						throw new IllegalArgumentException("saveInstance(ArrivalReceipt) needs an Arrival.");
					}
				} else {
					daoProvider.provideByDAOClass(IArrivalReceiptDAOService.class).update((ArrivalReceipt) instance);
				}
			} else if (instance instanceof ArrivalReceiptLine) {
				if (((BaseEntity) instance).getId() == null) {
					if (parentInstances.length == 1) {
						if (parentInstances[0] instanceof ArrivalReceipt) {
							DefaultTransactionDefinition def = new DefaultTransactionDefinition();
							def.setName("pageflow.ui.data");
							def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
							TransactionStatus status = daoProvider.provideByDAOClass(PlatformTransactionManager.class).getTransaction(def);
							T result = null;
							try {
								result = (T) daoProvider.provideByDAOClass(IArrivalReceiptDAOService.class).addArrivalReceiptLine(
										((ArrivalReceipt) parentInstances[0]).getId(), (ArrivalReceiptLine) instance);
								daoProvider.provideByDAOClass(PlatformTransactionManager.class).commit(status);
							} catch (Exception e) {
								daoProvider.provideByDAOClass(PlatformTransactionManager.class).rollback(status);
								e.printStackTrace();
							}
							return result;
						} else {
							throw new IllegalArgumentException("saveInstance(ArrivalReceiptLine) needs an ArrivalReceipt.");
						}
					} else {
						throw new IllegalArgumentException("saveInstance(ArrivalReceiptLine) needs an ArrivalReceipt.");
					}
				} else {
					daoProvider.provideByDAOClass(IArrivalReceiptDAOService.class).updateArrivalReceiptLine((ArrivalReceiptLine) instance);
				}
			} else if (instance instanceof ASNLine) {
				if (((BaseEntity) instance).getId() == null) {
					if (parentInstances.length == 1) {
						if (parentInstances[0] instanceof ASN) {
							DefaultTransactionDefinition def = new DefaultTransactionDefinition();
							def.setName("pageflow.ui.data");
							def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
							TransactionStatus status = daoProvider.provideByDAOClass(PlatformTransactionManager.class).getTransaction(def);
							T result = null;
							try {
								result = (T) daoProvider.provideByDAOClass(IASNDAOService.class).addLine((ASNLine) instance, ((ASN) parentInstances[0]).getId());
								daoProvider.provideByDAOClass(PlatformTransactionManager.class).commit(status);
							} catch (Exception e) {
								daoProvider.provideByDAOClass(PlatformTransactionManager.class).rollback(status);
								e.printStackTrace();
							}
							return result;
						} else {
							throw new IllegalArgumentException("saveInstance(ASNLine) needs an ASN.");
						}
					} else {
						throw new IllegalArgumentException("saveInstance(ASNLine) needs an ASN.");
					}
				} else {
					return (T) daoProvider.provideByDAOClass(IASNDAOService.class).update((ASNLine) instance);
				}
			}

			// If no DAO match was made, just return the unpersisted instance
			return instance;
		} else {
			throw new Exception("The IDAOProvider was null.");
		}
	}

	private static String typeToTitle(Class<?> type) {
		String simpleName = type.getSimpleName(), title = "";
		String[] sections = simpleName.split("(?=\\p{Upper})");
		boolean isFirst = true;
		for (String section : sections) {
			if (!isFirst) {
				if (section.length() > 1) {
					title += " ";
				}
			}
			title += section;
			if (!"".equals(section)) {
				isFirst = false;
			}
		}
		return title;
	}
}
