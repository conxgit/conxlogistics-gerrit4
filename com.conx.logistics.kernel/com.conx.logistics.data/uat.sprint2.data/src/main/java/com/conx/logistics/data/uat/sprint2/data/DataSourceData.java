package com.conx.logistics.data.uat.sprint2.data;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.conx.logistics.app.whse.rcv.rcv.domain.Receive;
import com.conx.logistics.app.whse.rcv.rcv.domain.ReceiveLine;
import com.conx.logistics.kernel.datasource.dao.services.IDataSourceDAOService;
import com.conx.logistics.kernel.datasource.domain.DataSource;
import com.conx.logistics.kernel.datasource.domain.DataSourceField;
import com.conx.logistics.kernel.metamodel.dao.services.IEntityTypeDAOService;
import com.conx.logistics.mdm.domain.BaseEntity;
import com.conx.logistics.mdm.domain.documentlibrary.FileEntry;
import com.conx.logistics.mdm.domain.metamodel.EntityType;
import com.conx.logistics.mdm.domain.note.NoteItem;
import com.conx.logistics.mdm.domain.referencenumber.ReferenceNumber;

public class DataSourceData {
	/** Logger available to subclasses */
	protected static final Log logger = LogFactory.getLog(DataSourceData.class);

	public static DataSource RCV_BASIC_DS = null;
	public static DataSource RCV_DEFAULT_DS = null;
	public static DataSource RCV_LINE_DEFAULT_DS = null;
	public static DataSource RCV_WEIGHT_DIMS_DS = null;

	public static DataSource FE_DS = null;
	public static DataSource NI_DS = null;
	public static DataSource RN_DS = null;

	public final static DataSource provideDefaultReceiveDS(IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService, EntityManager em) throws Exception {
		EntityType rcvET = EntityTypeData.provide(entityTypeDAOService, em, Receive.class);

		DataSource receiveDS = dataSourceDAOService.provide(rcvET);
		receiveDS.setCode("defaultReceiveDS");
		receiveDS.setName("DefaultReceiveDS");
		receiveDS = dataSourceDAOService.update(receiveDS);
		
		String testString = "";
		for (DataSourceField dsf : receiveDS.getDSFields()) {
			testString += dsf.getName() + ";";
		}

		String[] visibleFieldNames = { "id", "code", "name", "dateCreated", "dateLastUpdated", "warehouse" };
		List<String> visibleFieldNamesSet = Arrays.asList(visibleFieldNames);
		HashSet<DataSourceField> flds = new HashSet<DataSourceField>(receiveDS.getDSFields());
		for (DataSourceField fld : flds) {
			if (visibleFieldNamesSet.contains(fld.getName()))
				fld.setHidden(false);
			else
				fld.setHidden(true);

			if ("warehouse".equals(fld.getName())) {
				fld.setValueXPath("name");
			}
			
//			dataSourceDAOService.update(fld);
		}

		receiveDS = dataSourceDAOService.update(receiveDS);

		RCV_DEFAULT_DS = receiveDS;

		return receiveDS;
	}
	
	public final static DataSource provideDefaultReceiveLineDS(IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService, EntityManager em) throws Exception {
		EntityType rcvLineET = EntityTypeData.provide(entityTypeDAOService, em, ReceiveLine.class);

		DataSource receiveLineDS;
		try {
			receiveLineDS = dataSourceDAOService.provide(rcvLineET);
			receiveLineDS.setCode("defaultReceiveLineDS");
			receiveLineDS.setName("DefaultReceiveLineDS");
			receiveLineDS = dataSourceDAOService.update(receiveLineDS);

			String[] visibleFieldNames = { "id", "code", "name", "dateCreated", "dateLastUpdated","lineNumber","expectedOuterPackCount","expectedProdTotalWeight","expectedProdTotalVolume","product" };
			List<String> visibleFieldNamesSet = Arrays.asList(visibleFieldNames);
			for (DataSourceField fld : receiveLineDS.getDSFields()) {
				if (visibleFieldNamesSet.contains(fld.getName()))
					fld.setHidden(false);
				else
					fld.setHidden(true);

				if ("product".equals(fld.getName())) {
					fld.setValueXPath("code");
				}
				
				if ("parentReceive".equals(fld.getName())) {
					fld.setForeignKey("parentReceive.id");
				}
			}

			receiveLineDS = dataSourceDAOService.update(receiveLineDS);

			RCV_LINE_DEFAULT_DS = receiveLineDS;
		}
		catch (Exception e) 
		{
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);
			throw e;
		}	

		return receiveLineDS;
	}	

	public final static DataSource provideBasicFormReceiveDS(IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService, EntityManager em) throws Exception {
		EntityType rcvET = EntityTypeData.provide(entityTypeDAOService, em, Receive.class);
		DataSource receiveDS = new DataSource("receiveBasicAttrDS", rcvET);
		receiveDS = dataSourceDAOService.add(receiveDS);

		DataSourceField id = dataSourceDAOService.getFieldByName(RCV_DEFAULT_DS, BaseEntity.BASIC_ENTITY_ATTRIBUTE_ID);
		DataSourceField code = dataSourceDAOService.getFieldByName(RCV_DEFAULT_DS, BaseEntity.BASIC_ENTITY_ATTRIBUTE_CODE);
		DataSourceField name = dataSourceDAOService.getFieldByName(RCV_DEFAULT_DS, BaseEntity.BASIC_ENTITY_ATTRIBUTE_NAME);
		DataSourceField dateCreated = dataSourceDAOService.getFieldByName(RCV_DEFAULT_DS, BaseEntity.BASIC_ENTITY_ATTRIBUTE_DATE_CREATED);
		DataSourceField dateLastUpdated = dataSourceDAOService.getFieldByName(RCV_DEFAULT_DS, BaseEntity.BASIC_ENTITY_ATTRIBUTE_DATE_LAST_UPDATED);

		DataSourceField warehouse = dataSourceDAOService.getFieldByName(RCV_DEFAULT_DS, "warehouse");
		warehouse.setValueXPath("code");

		DataSourceField fields[] = { id, code, name, dateCreated, dateLastUpdated, warehouse };
		Set<DataSourceField> fieldSet = new HashSet<DataSourceField>(Arrays.asList(fields));

		receiveDS = dataSourceDAOService.addFields(receiveDS, fieldSet);

		receiveDS = dataSourceDAOService.update(receiveDS);

		RCV_BASIC_DS = receiveDS;

		return receiveDS;
	}
	
	public final static DataSource provideWeightDimsFormReceiveDS(IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService, EntityManager em) throws Exception {
		EntityType rcvET = EntityTypeData.provide(entityTypeDAOService, em, Receive.class);
		DataSource receiveDS = new DataSource("receiveWeightDimsAttrDS", rcvET);
		receiveDS = dataSourceDAOService.add(receiveDS);

		DataSourceField expectedTotalWeight = dataSourceDAOService.getFieldByName(RCV_DEFAULT_DS, "expectedTotalweight");
		expectedTotalWeight.setRequired(true);
		expectedTotalWeight = dataSourceDAOService.update(expectedTotalWeight);
		DataSourceField weightUnit = dataSourceDAOService.getFieldByName(RCV_DEFAULT_DS, "weightUnit");
		weightUnit.setRequired(true);
		weightUnit = dataSourceDAOService.update(weightUnit);
		DataSourceField expectedTotalVolume = dataSourceDAOService.getFieldByName(RCV_DEFAULT_DS, "expectedTotalVolume");
		expectedTotalVolume.setRequired(true);
		expectedTotalVolume = dataSourceDAOService.update(expectedTotalVolume);
		DataSourceField volumeUnit = dataSourceDAOService.getFieldByName(RCV_DEFAULT_DS, "volUnit");
		volumeUnit.setRequired(true);
		volumeUnit = dataSourceDAOService.update(volumeUnit);

		DataSourceField fields[] = { expectedTotalWeight, weightUnit, expectedTotalVolume, volumeUnit };
		Set<DataSourceField> fieldSet = new HashSet<DataSourceField>(Arrays.asList(fields));

		receiveDS = dataSourceDAOService.addFields(receiveDS, fieldSet);

		receiveDS = dataSourceDAOService.update(receiveDS);

		RCV_WEIGHT_DIMS_DS = receiveDS;

		return receiveDS;
	}

	public final static DataSource provideFileEntryDS(IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService, EntityManager em) throws Exception {
		EntityType feET = EntityTypeData.provide(entityTypeDAOService, em, FileEntry.class);

		DataSource feDS = dataSourceDAOService.provide(feET);
		feDS.setCode("fileEntryDS");
		feDS.setName("fileEntryDS");
		feDS = dataSourceDAOService.update(feDS);

		String[] visibleFieldNames = { "title", "size", "createDate", "modifiedDate", "dateCreated", "docType", "mimeType" };
		List<String> visibleFieldNamesSet = Arrays.asList(visibleFieldNames);
		for (DataSourceField fld : feDS.getDSFields()) {
			if (visibleFieldNamesSet.contains(fld.getName()))
				fld.setHidden(false);
			else
				fld.setHidden(true);

			if ("docType".equals(fld.getName())) {
				fld.setValueXPath("code");
			}
		}

		feDS = dataSourceDAOService.update(feDS);

		FE_DS = feDS;

		return feDS;
	}

	public final static DataSource provideNoteItemDS(IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService, EntityManager em) throws Exception {
		EntityType niET = EntityTypeData.provide(entityTypeDAOService, em, NoteItem.class);

		DataSource niDS = dataSourceDAOService.provide(niET);
		niDS.setCode("noteDS");
		niDS.setName("noteDS");
		niDS = dataSourceDAOService.update(niDS);

		String[] visibleFieldNames = { "id", "code", "name", "dateCreated", "dateLastUpdated", "content" };
		List<String> visibleFieldNamesSet = Arrays.asList(visibleFieldNames);
		for (DataSourceField fld : niDS.getDSFields()) {
			if (visibleFieldNamesSet.contains(fld.getName()))
				fld.setHidden(false);
			else
				fld.setHidden(true);
		}

		niDS = dataSourceDAOService.update(niDS);

		NI_DS = niDS;

		return niDS;
	}

	public final static DataSource provideReferenceNumberDS(IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService, EntityManager em) throws Exception {
		EntityType rnET = EntityTypeData.provide(entityTypeDAOService, em, ReferenceNumber.class);

		DataSource rnDS = dataSourceDAOService.provide(rnET);
		rnDS.setCode("referenceNumberDS");
		rnDS.setName("referenceNumberDS");
		rnDS = dataSourceDAOService.update(rnDS);

		String[] visibleFieldNames = { "dateCreated", "dateLastUpdated", "value", "type" };
		List<String> visibleFieldNamesSet = Arrays.asList(visibleFieldNames);
		for (DataSourceField fld : rnDS.getDSFields()) {
			if (visibleFieldNamesSet.contains(fld.getName()))
				fld.setHidden(false);
			else
				fld.setHidden(true);
		}

		rnDS = dataSourceDAOService.update(rnDS);

		RN_DS = rnDS;

		return rnDS;
	}
}
