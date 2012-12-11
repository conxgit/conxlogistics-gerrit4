package com.conx.logistics.kernel.ui.components.domain.custom.provider.factory;

import com.conx.logistics.kernel.ui.components.domain.custom.provider.presenter.ICustomContentPresenter;

public interface ICustomContentPresenterFactory {
	/**
	 * Creates a presenter of the provided type.
	 * 
	 * @param presenterType
	 * 			The java type of the presenter to be created
	 * @return
	 * 			The created presenter
	 */
	public ICustomContentPresenter create(Class<?> presenterType);
}
