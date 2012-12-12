package com.conx.logistics.kernel.ui.components.domain.form;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.conx.logistics.kernel.datasource.domain.DataSource;

@Entity
public class ConXCollapseableSectionForm extends ConXForm {
	private static final long serialVersionUID = 7927975246835400006L;

	@OneToMany(mappedBy = "form", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<FieldSet> fieldSetSet = new HashSet<FieldSet>();

	private int columnsLimit;

	public ConXCollapseableSectionForm() {
		super("collapseableSectionForm");
	}

	public ConXCollapseableSectionForm(DataSource ds) {
		super("collapseableSectionForm", ds);
	}

	public ConXCollapseableSectionForm(DataSource ds, Set<FieldSet> fieldSetSet) {
		super("collapseableSectionForm", ds);
		this.fieldSetSet = fieldSetSet;
	}

	public Set<FieldSet> getFieldSetSet() {
		return fieldSetSet;
	}

	public void setFieldSetSet(Set<FieldSet> fieldSetSet) {
		this.fieldSetSet = fieldSetSet;
	}

	public FieldSet getFieldSetForField(String fieldName) {
		for (FieldSet fs : fieldSetSet) {
			if (fs.getFieldSetField(fieldName) != null) {
				return fs;
			}
		}
		return null;
	}

	/**
	 * Gets the maximum columns in the grid layout of the collapsible form.
	 * 
	 * @return column limit
	 */
	public int getColumnsLimit() {
		return columnsLimit;
	}

	/**
	 * Sets the maximum columns in the grid layout of the collapsible form.
	 * 
	 * @param columnsLimit limit to how many columns there are
	 */
	public void setColumnsLimit(int columnsLimit) {
		this.columnsLimit = columnsLimit;
	}
}
