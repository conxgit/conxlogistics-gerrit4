package com.conx.logistics.kernel.ui.forms.vaadin.impl.field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.conx.logistics.kernel.metamodel.dao.services.IEntityTypeDAOService;
import com.conx.logistics.kernel.persistence.services.IEntityContainerProvider;
import com.conx.logistics.kernel.ui.forms.vaadin.impl.VaadinJPAFieldFactory;
import com.conx.logistics.mdm.domain.BaseEntity;
import com.conx.logistics.mdm.domain.geolocation.Address;
import com.conx.logistics.mdm.domain.metamodel.BasicAttribute;
import com.conx.logistics.mdm.domain.metamodel.EntityType;
import com.conx.logistics.mdm.domain.metamodel.EntityTypeAttribute;
import com.conx.logistics.mdm.domain.metamodel.SingularAttribute;
import com.conx.logistics.mdm.domain.organization.Contact;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectTranslator;
import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeListener;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Field;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.VerticalLayout;

public class VaadinRelationField extends VerticalLayout implements Field, Container.Viewer, Container.ItemSetChangeNotifier {
	private static final long serialVersionUID = 1L;

	private AbstractSelect selector;
	private VaadinRelationFieldForm form;
	private VerticalLayout formPanel;
	private JPAContainer<?> container;
	private VaadinJPAFieldFactory fieldFactory;
	private IEntityContainerProvider provider;
	private Object subPropertyId;
	private IEntityTypeDAOService entityTypeDao;

	public VaadinRelationField(Class<?> propertyType, Object subPropertyId, IEntityContainerProvider provider, IEntityTypeDAOService entityTypeDao) {
		this.subPropertyId = subPropertyId;
		this.entityTypeDao = entityTypeDao;
		this.provider = provider;

		JPAContainer<?> jpaContainer = (JPAContainer<?>) this.provider.createPersistenceContainer(propertyType);
		this.container = jpaContainer;

		this.selector = new NativeSelect();
		this.selector.setMultiSelect(false);
		this.selector.setItemCaptionMode(NativeSelect.ITEM_CAPTION_MODE_PROPERTY);
		this.selector.setItemCaptionPropertyId("name");
		this.selector.setContainerDataSource(this.container);
		this.selector.setImmediate(true);
		this.selector.setWidth("100%");
		this.selector.setPropertyDataSource(new SingleSelectTranslator(this.selector));
		this.selector.addListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				updateForm(event.getProperty().getValue());
			}
		});

		this.fieldFactory = new VaadinJPAFieldFactory();

		this.form = new VaadinRelationFieldForm();
		this.form.setFormFieldFactory(this.fieldFactory);
		
		this.formPanel = new VerticalLayout();
		this.formPanel.setWidth("100%");
		this.formPanel.addComponent(this.form);
		this.formPanel.setVisible(false);

		this.addComponent(this.selector);
		this.addComponent(this.formPanel);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		this.formPanel.setVisible(enabled);
	}

	private List<String> getAddressPropertyIds() {
		ArrayList<String> ids = new ArrayList<String>();
		ids.add("street1");
		ids.add("street2");
		ids.add("state");
		ids.add("email");
		ids.add("unloco");
		ids.add("zipCode");
		return ids;
	}

	private List<String> getContactPropertyIds() {
		ArrayList<String> ids = new ArrayList<String>();
		ids.add("firstName");
		ids.add("lastName");
		ids.add("officePhoneNumber");
		ids.add("cellPhoneNumber");
		ids.add("faxPhoneNumber");
		ids.add("email");
		return ids;
	}

	private List<String> getSubPropertyIds(Class<?> type) {
		if (Address.class.isAssignableFrom(type)) {
			return getAddressPropertyIds();
		} else if (Contact.class.isAssignableFrom(type)) {
			return getContactPropertyIds();
		} else {
			List<String> subPropertyIds = new ArrayList<String>();
			if (this.entityTypeDao != null) {
				try {
					EntityType entityType = this.entityTypeDao.provide(type);
					Set<EntityTypeAttribute> attributes = entityType.getDeclaredAttributes();
					for (EntityTypeAttribute attribute : attributes) {
						if (attribute.getAttribute() instanceof BasicAttribute || attribute.getAttribute() instanceof SingularAttribute) {
							subPropertyIds.add(attribute.getAttribute().getName());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return subPropertyIds;
		}
	}

	private void updateForm(Object itemId) {
		Item newItem = container.getItem(itemId);
		if (newItem != null) {
			Property subProperty = newItem.getItemProperty(this.subPropertyId);
			if (subProperty != null && subProperty.getValue() != null) {
				JPAContainer<?> formContainer = (JPAContainer<?>) this.provider.createPersistenceContainer(subProperty.getType());
				if (formContainer != null) {
					if (subProperty.getValue() instanceof BaseEntity) {
						Item formItem = formContainer.getItem(((BaseEntity) subProperty.getValue()).getId());
						if (formItem != null) {
							this.form.setItemDataSource(formItem, getSubPropertyIds(subProperty.getType()));
							this.form.setReadOnly(true);
							this.form.requestRepaint();
							this.formPanel.setVisible(true);
							return;
						}
					}
				}
			}
		}
		this.formPanel.setVisible(false);
		// The form could not be updated because something was null
		// this.formWrapper.setVisible(false);
	}

	@Override
	public void focus() {
		super.focus();
	}

	@Override
	public boolean isInvalidCommitted() {
		return false;
	}

	@Override
	public void setInvalidCommitted(boolean isCommitted) {
	}

	@Override
	public void commit() throws SourceException, InvalidValueException {
		this.selector.commit();
	}

	@Override
	public void discard() throws SourceException {
		this.selector.discard();
	}

	@Override
	public boolean isWriteThrough() {
		return false;
	}

	@Override
	public void setWriteThrough(boolean writeThrough) throws SourceException, InvalidValueException {
	}

	@Override
	public boolean isReadThrough() {
		return false;
	}

	@Override
	public void setReadThrough(boolean readThrough) throws SourceException {
	}

	@Override
	public boolean isModified() {
		return this.selector.isModified();
	}

	@Override
	public void addValidator(Validator validator) {
		this.selector.addValidator(validator);
	}

	@Override
	public void removeValidator(Validator validator) {
		this.selector.removeValidator(validator);
	}

	@Override
	public Collection<Validator> getValidators() {
		return this.selector.getValidators();
	}

	@Override
	public boolean isValid() {
		return this.selector.isValid();
	}

	@Override
	public void validate() throws InvalidValueException {
		// this.form.validate();
		this.selector.validate();
	}

	@Override
	public boolean isInvalidAllowed() {
		return this.selector.isInvalidAllowed();
	}

	@Override
	public void setInvalidAllowed(boolean invalidValueAllowed) throws UnsupportedOperationException {
		this.selector.setInvalidAllowed(invalidValueAllowed);
	}

	@Override
	public Object getValue() {
		return this.selector.getValue();
	}

	@Override
	public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
		this.selector.setValue(newValue);
	}

	@Override
	public Class<?> getType() {
		return this.selector.getType();
	}

	@Override
	public void addListener(ValueChangeListener listener) {
		this.selector.addListener(listener);
	}

	@Override
	public void removeListener(ValueChangeListener listener) {
		this.selector.removeListener(listener);
	}

	@Override
	public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
		this.selector.valueChange(event);
	}

	@Override
	public void setPropertyDataSource(Property newDataSource) {
		this.selector.setPropertyDataSource(newDataSource);
	}

	@Override
	public Property getPropertyDataSource() {
		return this.selector.getPropertyDataSource();
	}

	@Override
	public int getTabIndex() {
		return this.selector.getTabIndex();
	}

	@Override
	public void setTabIndex(int tabIndex) {
		this.selector.setTabIndex(tabIndex);
	}

	@Override
	public boolean isRequired() {
		return this.selector.isRequired();
	}

	@Override
	public void setRequired(boolean required) {
		this.selector.setRequired(required);
	}

	@Override
	public void setRequiredError(String requiredMessage) {
		this.selector.setRequiredError(requiredMessage);
	}

	@Override
	public String getRequiredError() {
		return this.selector.getRequiredError();
	}

	@Override
	public void setContainerDataSource(Container newDataSource) {
	}

	@Override
	public Container getContainerDataSource() {
		return this.selector.getContainerDataSource();
	}

	@Override
	public void addListener(ItemSetChangeListener listener) {
		this.selector.addListener(listener);
	}

	@Override
	public void removeListener(ItemSetChangeListener listener) {
		this.selector.addListener(listener);
	}

}
