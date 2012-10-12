package com.conx.logistics.kernel.datasource.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.conx.logistics.mdm.domain.MultitenantBaseEntity;
import com.conx.logistics.mdm.domain.metamodel.EntityType;

@Entity
@Table(name = "sysdsdatasource")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class DataSource extends MultitenantBaseEntity {
	@ManyToOne
	private DataSource superDataSource = null;

	@Transient
	private Set<String> visibleFieldNames = null;

	@Transient
	private Set<String> nestedFieldNames = null;

	@Transient
	private String foreignKeyPath = null;

	@Transient
	private Map<String, DataSourceField> dataSourceFieldMap = null;

	@ManyToOne
	private EntityType entityType;

	@OneToMany(mappedBy = "parentDataSource", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<DataSourceField> dSfields = new HashSet<DataSourceField>();

	public EntityType getEntityType() {
		return entityType;
	}

	public void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}

	public Set<DataSourceField> getDSFields() {
		return dSfields;
	}

	public Set<DataSourceField> getAllDSFields() {
		HashSet<DataSourceField> allDsFields = new HashSet<DataSourceField>();
		this.populateDataSourceFieldSet(allDsFields);
		return Collections.unmodifiableSet(allDsFields);
	}

	public DataSource() {
	}

	public void setDSFields(Set<DataSourceField> dSfields) {
		this.dSfields = dSfields;
	}

	public DataSource(String code, EntityType entityType) {
		super();
		setCode(code);
		setName(code);
		this.entityType = entityType;
	}

	public Set<String> getNestedFieldNames() {
		getVisibleFieldNames();
		return nestedFieldNames;
	}

	public String getForeignKeyPath() {
		if (this.foreignKeyPath == null) {
			Set<DataSourceField> dsFields = getDSFields();
			for (DataSourceField dsf : dsFields) {
				if (dsf.getForeignKey() != null) {
					this.foreignKeyPath = dsf.getForeignKey();
					break;
				}
			}
		}

		return this.foreignKeyPath;
	}

	public Set<String> getVisibleFieldNames() {
		if (visibleFieldNames == null) {
			visibleFieldNames = new HashSet<String>();
			nestedFieldNames = new HashSet<String>();
			Set<DataSourceField> dsFields = getAllDSFields();
			String derivedFieldName;
			for (DataSourceField dsf : dsFields) {
				if (!dsf.getHidden()) {
					if (dsf.isNestedAttribute()) {
						derivedFieldName = dsf.getJPAPath();
						nestedFieldNames.add(derivedFieldName);
						visibleFieldNames.add(derivedFieldName);
					} else {
						derivedFieldName = dsf.getName();
						visibleFieldNames.add(dsf.getName());
					}
				}
			}
		}
		return visibleFieldNames;
	}
	
	private Map<String, DataSourceField> provideDataSourceFieldMap() {
		if (dataSourceFieldMap == null) {
			dataSourceFieldMap = new HashMap<String, DataSourceField>();
			Set<DataSourceField> dsFields = getDSFields();
			for (DataSourceField dsf : dsFields) {
				dataSourceFieldMap.put(dsf.getName(), dsf);
			}
		}
		return this.dataSourceFieldMap;
	}
	
	public Map<String, DataSourceField> provideSuperDataSourceFieldMap() {
		if (dataSourceFieldMap == null) {
			dataSourceFieldMap = new HashMap<String, DataSourceField>();
			Set<DataSourceField> dsFields = getAllDSFields();
			for (DataSourceField dsf : dsFields) {
				dataSourceFieldMap.put(dsf.getName(), dsf);
			}
		}
		return this.dataSourceFieldMap;
	}
	
	public DataSourceField getInherittedField(String dsFieldName) {
		Map<String, DataSourceField> fieldMap = provideDataSourceFieldMap();
		DataSourceField field = fieldMap.get(dsFieldName);
		if (field == null && this.superDataSource != null) {
			return this.superDataSource.getInherittedField(dsFieldName);
		} else {
			return dataSourceFieldMap.get(dsFieldName);
		}
	}

	public DataSourceField getField(String dsFieldName) {
		Map<String, DataSourceField> fieldMap = provideDataSourceFieldMap();
		return fieldMap.get(dsFieldName);
	}

	private void populateDataSourceFieldSet(Set<DataSourceField> dataSourceFieldSet) {
		if (superDataSource != null) {
			superDataSource.populateDataSourceFieldSet(dataSourceFieldSet);
		}
		dataSourceFieldSet.addAll(this.getDSFields());
	}

	public String toString() {
		int indent = 0;

		StringBuilder sb = new StringBuilder();
		sb.append(indent(indent) + "{\n");

		sb.append(indent(indent + 1) + "name: " + getName());
		sb.append(indent(indent + 1) + "type: " + getEntityType().getJpaEntityName());

		sb.append(indent(indent + 1) + "fields : [\n");

		for (DataSourceField dsf : getDSFields()) {
			sb.append(indent(indent + 2) + dsf.toString() + ";");
		}

		sb.append(indent(indent + 1) + "]");

		sb.append(indent(indent) + "}\n");

		return sb.toString();
	}

	static String indent(int indent) {
		String res = "";
		for (int i = 0; i < indent; i++)
			res += res + "\t";

		return res;
	}

	public DataSource getSuperDataSource() {
		return superDataSource;
	}

	public void setSuperDataSource(DataSource superDataSource) {
		this.superDataSource = superDataSource;
	}
}
