package com.conx.logistics.kernel.ui.forms.vaadin.impl.field;

import com.conx.logistics.kernel.ui.forms.vaadin.impl.VaadinBeanFieldFactory;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.fieldfactory.JPAContainerCustomField;
import com.vaadin.addon.jpacontainer.util.HibernateUtil;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.VerticalLayout;


@SuppressWarnings("serial")
public class VaadinSelectDetail extends JPAContainerCustomField implements Property.ValueChangeListener {

	private final VaadinBeanFieldFactory fieldFactory;
	private Class<?> referencedType;

	@SuppressWarnings("rawtypes")
	private JPAContainer container;
	private ComboBox select;
	private String backReferencePropertyId;
	private Object masterEntity;
	private final Object propertyId;
	private final BeanItemContainer<?> containerForProperty;
	private final Object itemId;

	/**
	 * @param containerForProperty
	 * @param itemId
	 * @param propertyId
	 * @param uiContext
	 */
	public VaadinSelectDetail(VaadinBeanFieldFactory fieldFactory, BeanItemContainer<?> containerForProperty, Object itemId, Object propertyId, Component uiContext) {
		this.fieldFactory = fieldFactory;
		this.containerForProperty = containerForProperty;
		this.itemId = itemId;
		this.propertyId = propertyId;

		buildContainer();

		buildLayout();

		setCaption(DefaultFieldFactory.createCaptionByPropertyId(propertyId));
	}

	private void buildContainer() {
		// FIXME buffered mode
		Class<?> masterEntityClass = containerForProperty.getBeanType();
		referencedType = fieldFactory.detectReferencedType(fieldFactory.getFactory(), propertyId, masterEntityClass);
		container = fieldFactory.createJPAContainerFor(referencedType, false);
		backReferencePropertyId = HibernateUtil.getMappedByProperty(containerForProperty.getItem(itemId).getBean(), propertyId.toString());
		masterEntity = containerForProperty.getItem(itemId).getBean();
		Filter filter = new Compare.Equal(backReferencePropertyId, masterEntity);
		container.addContainerFilter(filter);
	}

	private void buildLayout() {
		VerticalLayout vl = new VerticalLayout();
		buildSelect();
		vl.addComponent(select);

		setCompositionRoot(vl);
	}
	
	@Override
	public void valueChange(Property.ValueChangeEvent event) {
	}

	private void buildSelect() {
		select = new ComboBox(null, container);
		select.setItemCaptionMode(ComboBox.ITEM_CAPTION_MODE_ID);
		select.setItemCaptionPropertyId("name");
		select.setNullSelectionAllowed(false);
		select.setImmediate(true);
		select.addListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.addon.jpacontainer.fieldfactory.JPAContainerCustomField#getType
	 * ()
	 */
	@Override
	public Class<?> getType() {
		return referencedType;
	}
}