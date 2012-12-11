package com.conx.logistics.kernel.ui.components.domain.custom.provider;

import com.conx.logistics.kernel.ui.components.domain.custom.provider.factory.ICustomContentPresenterFactory;
import com.conx.logistics.kernel.ui.components.domain.custom.provider.presenter.ICustomContentPresenter;

public interface ICustomContentProvider {
	/**
	 * Provides a hard-coded content presenter which is rendered according to
	 * the employed user interface library.
	 * 
	 * @param presenterFactory 
	 * 		The UI framework specific implementation of an MVP presenter factory
	 * @return
	 * 		The custom content presenter
	 */
	public ICustomContentPresenter provideContentPresenter(ICustomContentPresenterFactory presenterFactory);
}
