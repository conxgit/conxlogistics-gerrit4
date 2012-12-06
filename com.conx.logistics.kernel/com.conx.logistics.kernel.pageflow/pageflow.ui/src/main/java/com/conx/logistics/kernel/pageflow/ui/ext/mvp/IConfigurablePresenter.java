package com.conx.logistics.kernel.pageflow.ui.ext.mvp;

import java.util.Map;

public interface IConfigurablePresenter {
	public void onConfigure(Map<String, Object> params) throws Exception;
}
