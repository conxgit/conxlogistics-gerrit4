package com.conx.logistics.kernel.pageflow.ui.mvp;

import java.util.Map;

public interface IPageDataHandler {
	public void setParameterData(PagePresenter source, Map<String, Object> params);
	public Map<String, Object> getResultData(PagePresenter source);
}
