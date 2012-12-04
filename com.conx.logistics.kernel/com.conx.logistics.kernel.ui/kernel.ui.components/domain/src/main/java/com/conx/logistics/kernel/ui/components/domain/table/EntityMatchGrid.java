package com.conx.logistics.kernel.ui.components.domain.table;

import com.conx.logistics.kernel.datasource.domain.DataSource;
import com.conx.logistics.kernel.ui.components.domain.AbstractConXComponent;

public class EntityMatchGrid extends ConXTable {
	private static final long serialVersionUID = -4200861331740833004L;
	
	private DataSource unmatchedDataSource;
	private DataSource matchedDataSource;

	public EntityMatchGrid() {
		super("matchgrid");
	}
	
	public EntityMatchGrid(DataSource unmatchedDataSource, DataSource matchedDataSource) {
		this();
		this.setDataSource(matchedDataSource);
		this.unmatchedDataSource = unmatchedDataSource;
		this.matchedDataSource = matchedDataSource;
	}

	public DataSource getUnmatchedDataSource() {
		return unmatchedDataSource;
	}

	public void setUnmatchedDataSource(DataSource unmatchedDataSource) {
		this.unmatchedDataSource = unmatchedDataSource;
	}

	public DataSource getMatchedDataSource() {
		this.setDataSource(matchedDataSource);
		return matchedDataSource;
	}

	public void setMatchedDataSource(DataSource matchedDataSource) {
		this.matchedDataSource = matchedDataSource;
	}
}
