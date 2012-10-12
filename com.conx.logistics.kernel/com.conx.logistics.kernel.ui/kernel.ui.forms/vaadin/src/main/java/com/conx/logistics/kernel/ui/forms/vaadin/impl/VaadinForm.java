package com.conx.logistics.kernel.ui.forms.vaadin.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.conx.logistics.kernel.datasource.domain.DataSourceField;
import com.conx.logistics.kernel.metamodel.dao.services.IEntityTypeDAOService;
import com.conx.logistics.kernel.persistence.services.IEntityContainerProvider;
import com.conx.logistics.kernel.ui.components.domain.form.ConXForm;
import com.conx.logistics.kernel.ui.forms.vaadin.impl.field.VaadinPlaceHolderField;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Container.ItemSetChangeListener;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.MethodProperty.MethodException;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.terminal.CompositeErrorMessage;
import com.vaadin.terminal.ErrorMessage;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class VaadinForm extends Form {
	private ConXForm componentModel;

	private int gridlayoutCursorX = -1;
	private int gridlayoutCursorY = -1;

	private Item itemDatasource;
	private Collection<?> visibleItemProperties;

	public VaadinForm() {
	}

	public VaadinForm(Layout formLayout, ConXForm componentModel) {
		super(formLayout);
		this.componentModel = componentModel;
	}

	public VaadinForm(Layout formLayout, ConXForm componentModel, FormFieldFactory fieldFactory) {
		super(formLayout, fieldFactory);
		this.componentModel = componentModel;
	}

	private String getPropertyId(DataSourceField dsField) {
		if (dsField.getJPAPath() != null) {
			return dsField.getJPAPath();
		} else {
			return dsField.getName();
		}
	}

	private Collection<Object> getChildDataSourceFieldPropertyIds(Object propertyId) {
		HashSet<Object> childListenerFieldPropertyIds = new HashSet<Object>();
		Set<DataSourceField> dsFields = this.componentModel.getDataSource().getDSFields();
		DataSourceField thisDsField = null;
		for (DataSourceField dsField : dsFields) {
			if (getPropertyId(dsField).equals(propertyId)) {
				thisDsField = dsField;
			}
		}

		if (thisDsField == null) {
			return childListenerFieldPropertyIds;
		}

		Set<DataSourceField> childDataSourceFields = thisDsField.getChildDataSourceFields();
		for (DataSourceField dsField : childDataSourceFields) {
			childListenerFieldPropertyIds.add(getPropertyId(dsField));
		}

		return childListenerFieldPropertyIds;
	}

	@Override
	public void setItemDataSource(Item newDataSource, Collection<?> propertyIds) {
		if (this.componentModel != null) {
			if (getLayout() instanceof GridLayout) {
				GridLayout gl = (GridLayout) getLayout();
				if (gridlayoutCursorX == -1) {
					// first setItemDataSource, remember initial cursor
					gridlayoutCursorX = gl.getCursorX();
					gridlayoutCursorY = gl.getCursorY();
				} else {
					// restore initial cursor
					gl.setCursorX(gridlayoutCursorX);
					gl.setCursorY(gridlayoutCursorY);
				}
			}

			// Removes all fields first from the form
			removeAllProperties();

			// Sets the datasource
			itemDatasource = newDataSource;

			// If the new datasource is null, just set null datasource
			if (itemDatasource == null) {
				requestRepaint();
				return;
			}

			HashMap<Object, Field> addedPropertyIds = new HashMap<Object, Field>();
			// Adds all the properties to this form
			for (final Iterator<?> i = propertyIds.iterator(); i.hasNext();) {
				final Object id = i.next();
				final Property property = itemDatasource.getItemProperty(id);
				if (id != null && property != null) {
					initField(itemDatasource, id, addedPropertyIds);
				}
			}
		}
	}

	public void setItemDataSource(Item newDataSource, Collection<?> propertyIds, IEntityTypeDAOService entityTypeDao) {
		this.setItemDataSource(newDataSource, propertyIds);
	}

	public void setItemDataSource(Item newDataSource, Collection<?> propertyIds, IEntityTypeDAOService entityTypeDao, BeanItemContainer<?> itemParentContainer,
			IEntityContainerProvider containerProvider) {
		VaadinBeanFieldFactory formFieldFactory = new VaadinBeanFieldFactory();
		formFieldFactory.setEntityTypeDao(entityTypeDao);
		formFieldFactory.setContainer(itemParentContainer);
		formFieldFactory.setContainerProvider(containerProvider);

		this.setFormFieldFactory(formFieldFactory);
		this.setItemDataSource(newDataSource, propertyIds, entityTypeDao);
	}

	private ValueChangeListener buildDependenceListener(Field childListenerField) {
		if (childListenerField instanceof Container.Viewer) {
			return new ValueDependenceListener(((Container.Viewer) childListenerField).getContainerDataSource());
		}

		return null;
	}

	private ItemSetChangeListener buildRefreshListener(Field listenerField) {
		if (listenerField instanceof Container.Viewer) {
			return new ContainerRefreshListener(listenerField);
		}

		return null;
	}

	private void applyRefreshListener(Field field, ItemSetChangeListener listener) {
		if (field instanceof Container.ItemSetChangeNotifier) {
			((Container.ItemSetChangeNotifier) field).addListener(listener);
		}
	}

	private void applyDependenceListener(final Field field, final ValueChangeListener dependenceListener) {
		if (field != null && dependenceListener != null) {
			if (field instanceof TextField) {
				((TextField) field).addListener(new TextChangeListener() {

					@Override
					public void textChange(TextChangeEvent event) {
						dependenceListener.valueChange(new Property.ValueChangeEvent() {

							@Override
							public Property getProperty() {
								return field.getPropertyDataSource();
							}
						});
					}
				});
			} else {
				field.addListener(dependenceListener);
			}
		}
	}

	private Field initField(Item itemDataSource, Object propertyId, Map<Object, Field> addedPropertyIds) {
		if (!addedPropertyIds.containsKey(propertyId) && itemDataSource.getItemPropertyIds().contains(propertyId)) {
			final Property p = itemDatasource.getItemProperty(propertyId);
			if (p != null) {
				final Field f = getFormFieldFactory().createField(itemDatasource, propertyId, this);
				if (f != null) {
					Collection<Object> childListenerFieldPropertyIds = getChildDataSourceFieldPropertyIds(propertyId);
					for (final Object childListenerFieldPropertyId : childListenerFieldPropertyIds) {
						Field childListenerField = addedPropertyIds.get(childListenerFieldPropertyId);
						if (childListenerField == null) {
							childListenerField = initField(itemDataSource, childListenerFieldPropertyId, addedPropertyIds);
						}
						childListenerField.setEnabled(false);
						applyDependenceListener(f, buildDependenceListener(childListenerField));
						applyRefreshListener(childListenerField, buildRefreshListener(childListenerField));
					}

					try {
						addedPropertyIds.put(propertyId, f);
						bindPropertyToField(propertyId, p, f);
						if (isNestedParentPropertyNull(f)) {
							throw new MethodException(p, "Nested Parent Properties for Property " + propertyId + " were null.");
						}
						clearErrorMessage(f);
						addField(propertyId, f);
						return f;
					} catch (Exception e) {
						e.printStackTrace();
						Field placeHolderField = new VaadinPlaceHolderField();
						attachField(propertyId, placeHolderField);
						requestRepaint();
						return placeHolderField;
					}
				}
			}
		}
		return null;
	}

	private void clearErrorMessage(final Field f) {
		if (f instanceof AbstractField) {
			((AbstractField) f).setComponentError(null);
		}
	}

	// private boolean isBasicField(final Field f) {
	// return f instanceof TextField || f instanceof CheckBox;
	// }

	private boolean isNestedParentPropertyNull(final Field f) {
		if (f instanceof AbstractComponent) {
			ErrorMessage error = ((AbstractComponent) f).getErrorMessage();
			if (error != null) {
				if (error instanceof CompositeErrorMessage) {
					Iterator<ErrorMessage> iterator = ((CompositeErrorMessage) error).iterator();
					ErrorMessage errorMessage = null;
					while (iterator.hasNext()) {
						errorMessage = iterator.next();
						if (errorMessage instanceof SourceException) {
							Throwable[] causes = ((SourceException) errorMessage).getCauses();
							for (Throwable cause : causes) {
								if (cause instanceof MethodException) {
									if (cause.getCause() instanceof NullPointerException) {
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public Collection<?> getVisibleItemProperties() {
		return visibleItemProperties;
	}

	@Override
	public void setVisibleItemProperties(Collection<?> visibleProperties) {
		visibleItemProperties = visibleProperties;
		Object value = getValue();
		if (value == null) {
			value = itemDatasource;
		}
		setFormDataSource(value, getVisibleItemProperties());
	}

	protected class ContainerRefreshListener implements ItemSetChangeListener {
		private Field childField;

		public ContainerRefreshListener(Field childField) {
			this.childField = childField;
		}

		@Override
		public void containerItemSetChange(ItemSetChangeEvent event) {
			if (childField instanceof Container.Viewer) {
				Container container = ((Container.Viewer) this.childField).getContainerDataSource();
				Collection<?> ids = container.getItemIds();
				if (ids.size() == 0) {
					childField.setValue(null);
					childField.setEnabled(false);
				} else {
					childField.setEnabled(true);
					if (childField instanceof Field) {
						((Field) childField).setValue(ids.iterator().next());
					}
				}
			}
		}
	}

	protected class ValueDependenceListener implements ValueChangeListener {
		private Container childFieldContainer;
		private Filter parentFilter;
		private Object entityId;

		public ValueDependenceListener(Container childFieldContainer) {
			this.childFieldContainer = childFieldContainer;
		}

		@Override
		public void valueChange(final Property.ValueChangeEvent event) {
			if (childFieldContainer instanceof JPAContainer) {
				if (event != null && event.getProperty() != null && event.getProperty().getValue() != null) {
					this.entityId = event.getProperty().getValue();
					if (parentFilter != null) {
						((JPAContainer<?>) this.childFieldContainer).removeContainerFilter(this.parentFilter);
					}
					this.parentFilter = new com.vaadin.data.util.filter.Compare.Equal("ownerEntityId", this.entityId);
					((JPAContainer<?>) this.childFieldContainer).addContainerFilter(this.parentFilter);
					((JPAContainer<?>) this.childFieldContainer).applyFilters();
				}
			}
		}
	}

	public ConXForm getComponentModel() {
		return componentModel;
	}

	public void setComponentModel(ConXForm componentModel) {
		this.componentModel = componentModel;
	}
}
