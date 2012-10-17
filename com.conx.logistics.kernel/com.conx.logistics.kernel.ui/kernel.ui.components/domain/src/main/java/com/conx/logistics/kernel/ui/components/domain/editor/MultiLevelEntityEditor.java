package com.conx.logistics.kernel.ui.components.domain.editor;

import com.conx.logistics.kernel.ui.components.domain.masterdetail.MasterDetailComponent;

public class MultiLevelEntityEditor extends EntityEditor {
	private static final long serialVersionUID = 1L;
	
	private MasterDetailComponent content;

	public MultiLevelEntityEditor() {
		super("multilevelentityeditor");
	}

	public MasterDetailComponent getContent() {
		return content;
	}

	public void setContent(MasterDetailComponent content) {
		this.content = content;
	}
}
