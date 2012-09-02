package com.conx.logistics.kernel.ui.forms.vaadin;

import java.util.Collection;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;

public interface IVaadinForm extends Component {
	public void setItemDataSource(Item item, Collection<?> propertyIds);
	public FormMode getFormMode();
	public void setFormMode(FormMode mode);
	public void setTitle(String title);
}
