package com.conx.logistics.kernel.ui.forms.vaadin.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.conx.logistics.kernel.datasource.domain.DataSourceField;
import com.conx.logistics.kernel.metamodel.dao.services.IEntityTypeDAOService;
import com.conx.logistics.kernel.ui.components.domain.form.ConXForm;
import com.conx.logistics.mdm.domain.metamodel.EntityType;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.util.DefaultQueryModifierDelegate;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.AbstractSelect;
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
	private IEntityTypeDAOService entityTypeDao;

	public VaadinForm() {
		setFormFieldFactory(new VaadinBeanFieldFactory());
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
			String[] path = dsField.getJPAPath().split(".");
			return path[(path.length - 1 < 0) ? 0 : path.length - 1];
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

			HashSet<Object> addedPropertyIds = new HashSet<Object>();
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
		this.entityTypeDao = entityTypeDao;
		this.setItemDataSource(newDataSource, propertyIds);
	}
	
	public void setItemDataSource(Item newDataSource, Collection<?> propertyIds, IEntityTypeDAOService entityTypeDao, BeanItemContainer<?> itemParentContainer) {
		if (this.getFormFieldFactory() instanceof VaadinBeanFieldFactory) {
			((VaadinBeanFieldFactory) this.getFormFieldFactory()).setContainer(itemParentContainer);
		}
		this.setItemDataSource(newDataSource, propertyIds);
	}
	
	public void setItemDataSource(Item newDataSource, Collection<?> propertyIds, IEntityTypeDAOService entityTypeDao, BeanItemContainer<?> itemParentContainer, EntityManagerFactory entityManagerFactory) {
		if (this.getFormFieldFactory() instanceof VaadinBeanFieldFactory) {
			((VaadinBeanFieldFactory) this.getFormFieldFactory()).setFactory(entityManagerFactory);
		}
		this.setItemDataSource(newDataSource, propertyIds, entityTypeDao);
	}

	private ValueChangeListener buildDependenceListener(Field childListenerField) {
		if (childListenerField instanceof AbstractSelect) {
			return new ValueDependenceListener(((AbstractSelect) childListenerField).getContainerDataSource());
		}
		
		return null;
	}

	private void applyDependenceListener(final Field field, final ValueChangeListener dependenceListener) {
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

	private Field initField(Item itemDataSource, Object propertyId, Set<Object> addedPropertyIds) {
		if (!addedPropertyIds.contains(addedPropertyIds) && itemDataSource.getItemPropertyIds().contains(propertyId)) {
			Field childListenerField = null;
			Collection<Object> childListenerFieldPropertyIds = getChildDataSourceFieldPropertyIds(propertyId);
			for (final Object childListenerFieldPropertyId : childListenerFieldPropertyIds) {
				childListenerField = initField(itemDataSource, childListenerFieldPropertyId, addedPropertyIds);
				if (childListenerField != null) {
					applyDependenceListener(childListenerField, buildDependenceListener(childListenerField));
				}
			}
			
			final Property p = itemDatasource.getItemProperty(propertyId);
			if (p != null) {
				final Field f = getFormFieldFactory().createField(itemDatasource, propertyId, this);
				if (f != null) {
					bindPropertyToField(propertyId, p, f);
					addField(propertyId, f);
					addedPropertyIds.add(propertyId);

					return f;
				}
			}
		}
		return null;
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

	private EntityType getEntityType(Class<?> type) throws Exception {
		if (entityTypeDao != null) {
			return entityTypeDao.provide(type);
		}
		return null;
	}

	protected class ValueDependenceListener implements ValueChangeListener {
		private Container childFieldContainer;

		public ValueDependenceListener(Container childFieldContainer) {
			this.childFieldContainer = childFieldContainer;
		}

		@Override
		public void valueChange(final Property.ValueChangeEvent event) {
			if (childFieldContainer instanceof JPAContainer) {
				JPAContainer<?> jpaContainer = (JPAContainer<?>) childFieldContainer;
				jpaContainer.removeAllContainerFilters();
				jpaContainer.getEntityProvider().setQueryModifierDelegate(new DefaultQueryModifierDelegate() {
					@Override
					public void filtersWillBeAdded(CriteriaBuilder criteriaBuilder, CriteriaQuery<?> query, List<Predicate> predicates) {
						try {
							Root<?> root = query.getRoots().iterator().next();
							Path<?> ownerEntityIdPath = root.get("ownerEntityId");
							Path<?> ownerEntityTypeIdPath = root.get("ownerEntityType").get("id");

							EntityType type = getEntityType(((JPAContainer<?>) childFieldContainer).getEntityClass());
							if (type == null) {
								return;
							}
							
							Object ownerEntityId = event.getProperty().getValue();							
							Object ownerEntityTypeId = type.getId();

							if (ownerEntityId != null && ownerEntityTypeId != null) {
								predicates.add(criteriaBuilder.and(criteriaBuilder.equal(ownerEntityIdPath, ownerEntityId), criteriaBuilder.equal(ownerEntityTypeIdPath, ownerEntityTypeId)));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				jpaContainer.applyFilters();
			}
		}
	}
}
