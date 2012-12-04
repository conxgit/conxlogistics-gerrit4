package com.conx.logistics.kernel.ui.components.domain.search;

import javax.persistence.Entity;

import com.conx.logistics.kernel.ui.components.domain.AbstractConXComponent;
import com.conx.logistics.kernel.ui.components.domain.table.ConXTable;

@Entity
public class SearchGrid extends AbstractConXComponent {
	private static final long serialVersionUID = -3549577034613359094L;
	
	private String formTitle;
	private String[] visibleColumnIds;

	public SearchGrid() {
		super("searchgrid");
	}
	
	public void setFormTitle(String formTitle) {
		this.formTitle = formTitle;
	}

	public String getFormTitle() {
		return this.formTitle;
	}

	public String[] getVisibleColumnIds() {
		return visibleColumnIds;
	}

	public void setVisibleColumnIds(String[] visibleColumnIds) {
		this.visibleColumnIds = visibleColumnIds;
	}
	
}
