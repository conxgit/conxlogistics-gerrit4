package com.conx.logistics.kernel.ui.components.domain.table;

import javax.persistence.Entity;

import com.conx.logistics.kernel.datasource.domain.DataSource;

@Entity
public class ConXDetailTable extends ConXTable {

	public ConXDetailTable() {
		super("detailtable");
	}
	
	public ConXDetailTable(DataSource dataSource) {
		this();
		super.setDataSource(dataSource);
	}	
}
