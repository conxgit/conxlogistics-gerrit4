package com.conx.logistics.kernel.ui.components.domain.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.conx.logistics.kernel.datasource.domain.DataSource;
import com.conx.logistics.kernel.datasource.domain.DataSourceField;
import com.conx.logistics.kernel.ui.components.domain.AbstractConXField;
import com.conx.logistics.kernel.ui.components.domain.layout.AbstractConXLayout;

@Entity
public class ConXCollapseableSectionForm extends ConXForm {
	
	@Transient
	private Map<String,FieldSet> fieldSetMap = null;
	
	@Transient
	private Map<String,DataSourceField> fieldMap = null;	

	
	@OneToMany
	private List<FieldSet> fieldSetList = new ArrayList<FieldSet>();
	
	
	public ConXCollapseableSectionForm() {
		super("collapseableSectionForm");
	}


	public List<FieldSet> getFieldSetList() {
		return fieldSetList;
	}
	
	public Map<String, FieldSet> getFieldSetMap() {
		if (fieldSetMap == null)
		{
			fieldSetMap = new HashMap<String, FieldSet>();
			Map<String, DataSourceField> fm;
			for (FieldSet fieldSet : getFieldSetList())
			{
				fm = fieldSet.getFieldMap();
				for (String fieldName : fm.keySet())
				{
					fieldSetMap.put(fieldName, fieldSet);
				}
			}
		}
		return fieldSetMap;
	}	
	
	public Map<String, DataSourceField> getFieldMap() {
		if (fieldMap == null)
		{
			fieldMap = new HashMap<String, DataSourceField>();
			Map<String, DataSourceField> fm;
			for (FieldSet fieldSet : getFieldSetList())
			{
				fm = fieldSet.getFieldMap();
				fieldMap.putAll(fm);
			}
		}
		return fieldMap;
	}		
	
	public FieldSet getFieldSetForField(String fieldName)
	{
		return getFieldSetMap().get(fieldName);
	}
	
	public DataSourceField getField(String fieldName)
	{
		return getFieldMap().get(fieldName);
	}	


	public void setFieldSetList(List<FieldSet> fieldSetList) {
		this.fieldSetList = fieldSetList;
	}


	public ConXCollapseableSectionForm(DataSource ds,AbstractConXLayout layout, List<FieldSet> fieldSetList) {
		this(ds,layout);
		this.fieldSetList = fieldSetList;
	}
	
	public ConXCollapseableSectionForm(DataSource ds,AbstractConXLayout layout) {
		this();
		setLayout(layout);
		setDataSource(ds);
	}	
}
