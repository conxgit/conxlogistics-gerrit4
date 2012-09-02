package com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.table;

import com.vaadin.addon.jpacontainer.EntityItem;

public interface IRowEditListener {
	public void onRowEdit(EntityItem<?> item);
}
