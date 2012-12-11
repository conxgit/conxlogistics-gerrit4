package com.conx.logistics.kernel.ui.components.domain.custom;

import javax.persistence.Transient;

import com.conx.logistics.kernel.ui.components.domain.AbstractConXComponent;
import com.conx.logistics.kernel.ui.components.domain.custom.provider.ICustomContentProvider;

public class CustomConXComponent extends AbstractConXComponent {
	private static final long serialVersionUID = 3319015549762729408L;
	
	@Transient
	private ICustomContentProvider customComponent;

	public CustomConXComponent() {
		super("hardcodedcomponent");
	}
	
	public CustomConXComponent(ICustomContentProvider customComponent) {
		this();
		this.customComponent = customComponent;
	}

	public ICustomContentProvider getHardCodedComponent() {
		return customComponent;
	}

	public void setHardCodedComponent(ICustomContentProvider hardCodedComponent) {
		this.customComponent = hardCodedComponent;
	}

}
