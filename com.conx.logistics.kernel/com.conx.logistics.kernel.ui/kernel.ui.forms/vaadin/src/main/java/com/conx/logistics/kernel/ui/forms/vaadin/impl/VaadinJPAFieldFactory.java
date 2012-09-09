package com.conx.logistics.kernel.ui.forms.vaadin.impl;

import com.vaadin.addon.jpacontainer.EntityContainer;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.fieldfactory.FieldFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectTranslator;
import com.vaadin.data.Item;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.NativeSelect;

public class VaadinJPAFieldFactory extends FieldFactory {
	private static final long serialVersionUID = -2958448405826099336L;

	public VaadinJPAFieldFactory() {
		super();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Field createManyToOneField(EntityContainer containerForProperty, Object itemId, Object propertyId, Component uiContext) {
		Class<?> type = containerForProperty.getType(propertyId);
		JPAContainer container = createJPAContainerFor(containerForProperty, type, false);

		AbstractSelect nativeSelect = constructReferenceSelect(containerForProperty, itemId, propertyId, uiContext, type);
		nativeSelect.setMultiSelect(false);
		nativeSelect.setWidth("100%");
		nativeSelect.setItemCaptionPropertyId("code");
		nativeSelect.setCaption(DefaultFieldFactory.createCaptionByPropertyId(propertyId));
		nativeSelect.setItemCaptionMode(NativeSelect.ITEM_CAPTION_MODE_PROPERTY);
		nativeSelect.setContainerDataSource(container);
		nativeSelect.setPropertyDataSource(new SingleSelectTranslator(nativeSelect));
		return nativeSelect;
	}

	@Override
	public Field createField(Item item, Object propertyId, Component uiContext) {
		return super.createField(item, propertyId, uiContext);
	}
}
