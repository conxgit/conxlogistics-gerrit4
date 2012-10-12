package com.conx.logistics.data.uat.sprint2.data;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.conx.logistics.app.whse.rcv.rcv.domain.Receive;
import com.conx.logistics.app.whse.rcv.rcv.domain.ReceiveLine;
import com.conx.logistics.kernel.datasource.dao.services.IDataSourceDAOService;
import com.conx.logistics.kernel.datasource.domain.DataSource;
import com.conx.logistics.kernel.datasource.domain.DataSourceField;
import com.conx.logistics.kernel.metamodel.dao.services.IEntityTypeDAOService;
import com.conx.logistics.mdm.domain.documentlibrary.FileEntry;
import com.conx.logistics.mdm.domain.metamodel.EntityType;
import com.conx.logistics.mdm.domain.note.NoteItem;
import com.conx.logistics.mdm.domain.referencenumber.ReferenceNumber;

public class DataSourceData {
	/** Logger available to subclasses */
	protected static final Log logger = LogFactory.getLog(DataSourceData.class);

	public final static DataSource provideDefaultReceiveDS(IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService, EntityManager em) throws Exception {
		EntityType rcvET = EntityTypeData.provide(entityTypeDAOService, em, Receive.class);

		String[] visibleFieldNames = { "id", "code", "name", "dateCreated", "dateLastUpdated", "warehouse" };
		List<String> visibleFieldNamesSet = Arrays.asList(visibleFieldNames);
		
		DataSource receiveDS = dataSourceDAOService.provideCustomDataSource("default", rcvET, visibleFieldNamesSet);
		HashSet<DataSourceField> flds = new HashSet<DataSourceField>(receiveDS.getDSFields());
		for (DataSourceField fld : flds) {
			if (visibleFieldNamesSet.contains(fld.getName()))
				fld.setHidden(false);
			else
				fld.setHidden(true);

			if ("warehouse".equals(fld.getName())) {
				fld.setValueXPath("name");
			}
		}

		receiveDS = dataSourceDAOService.update(receiveDS);

		return receiveDS;
	}
	
	public final static DataSource provideDefaultReceiveLineDS(IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService, EntityManager em) throws Exception {
		EntityType rcvLineET = EntityTypeData.provide(entityTypeDAOService, em, ReceiveLine.class);

		DataSource receiveLineDS;
		try {

			String[] visibleFieldNames = { "id", "code", "name", "dateCreated", "dateLastUpdated","lineNumber","expectedOuterPackCount","expectedProdTotalWeight","expectedProdTotalVolume","product" };
			List<String> visibleFieldNamesSet = Arrays.asList(visibleFieldNames);
			receiveLineDS = dataSourceDAOService.provideCustomDataSource("default", rcvLineET, visibleFieldNamesSet);
			
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

		String[] visibleFieldNames = { "id", "code", "name", "dateCreated", "dateLastUpdated", "warehouse" };
		List<String> visibleFieldNamesSet = Arrays.asList(visibleFieldNames);
		
		DataSource receiveDS = dataSourceDAOService.provideCustomDataSource("basic", rcvET, visibleFieldNamesSet);
		HashSet<DataSourceField> flds = new HashSet<DataSourceField>(receiveDS.getDSFields());
		for (DataSourceField fld : flds) {
			if (visibleFieldNamesSet.contains(fld.getName()))
				fld.setHidden(false);
			else
				fld.setHidden(true);

			if ("warehouse".equals(fld.getName())) {
				fld.setValueXPath("name");
			}
		}

		receiveDS = dataSourceDAOService.update(receiveDS);

		return receiveDS;
	}
	
	public final static DataSource provideWeightDimsFormReceiveDS(IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService, EntityManager em) throws Exception {
		EntityType rcvET = EntityTypeData.provide(entityTypeDAOService, em, Receive.class);
		
		String[] visibleFieldNames = { "id", "code", "name", "dateCreated", "dateLastUpdated", "warehouse", "expectedTotalweight", "weightUnit", "expectedTotalVolume", "volUnit" };
		List<String> visibleFieldNamesSet = Arrays.asList(visibleFieldNames);
		DataSource weightDimsRcvDS = dataSourceDAOService.provideCustomDataSource("weightDims", rcvET, visibleFieldNamesSet);
		
		DataSourceField expectedTotalWeight = weightDimsRcvDS.getField("expectedTotalweight");
		expectedTotalWeight.setRequired(true);
		expectedTotalWeight = dataSourceDAOService.update(expectedTotalWeight);
		DataSourceField weightUnit = weightDimsRcvDS.getField("weightUnit");
		weightUnit.setRequired(true);
		weightUnit = dataSourceDAOService.update(weightUnit);
		DataSourceField expectedTotalVolume = weightDimsRcvDS.getField("expectedTotalVolume");
		expectedTotalVolume.setRequired(true);
		expectedTotalVolume = dataSourceDAOService.update(expectedTotalVolume);
		DataSourceField volumeUnit = weightDimsRcvDS.getField("volUnit");
		volumeUnit.setRequired(true);
		volumeUnit = dataSourceDAOService.update(volumeUnit);

		return weightDimsRcvDS;
	}

	public final static DataSource provideFileEntryDS(IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService, EntityManager em) throws Exception {
		EntityType feET = EntityTypeData.provide(entityTypeDAOService, em, FileEntry.class);
		String[] visibleFieldNames = { "title", "size", "createDate", "modifiedDate", "dateCreated", "docType", "mimeType" };
		List<String> visibleFieldNamesSet = Arrays.asList(visibleFieldNames);
		DataSource feDS = dataSourceDAOService.provideCustomDataSource("default", feET, visibleFieldNamesSet);

		HashSet<DataSourceField> flds = new HashSet<DataSourceField>(feDS.getDSFields());
		for (DataSourceField fld : flds) {
			if (visibleFieldNamesSet.contains(fld.getName()))
				fld.setHidden(false);
			else
				fld.setHidden(true);

			if ("docType".equals(fld.getName())) {
				fld.setValueXPath("code");
			}
		}

		feDS = dataSourceDAOService.update(feDS);

		return feDS;
	}

	public final static DataSource provideNoteItemDS(IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService, EntityManager em) throws Exception {
		EntityType niET = EntityTypeData.provide(entityTypeDAOService, em, NoteItem.class);
		String[] visibleFieldNames = { "id", "code", "name", "dateCreated", "dateLastUpdated", "content" };
		List<String> visibleFieldNamesSet = Arrays.asList(visibleFieldNames);
		DataSource niDS = dataSourceDAOService.provideCustomDataSource("default", niET, visibleFieldNamesSet);
		
		HashSet<DataSourceField> flds = new HashSet<DataSourceField>(niDS.getDSFields());
		for (DataSourceField fld : flds) {
			if (visibleFieldNamesSet.contains(fld.getName()))
				fld.setHidden(false);
			else
				fld.setHidden(true);
		}

		niDS = dataSourceDAOService.update(niDS);

		return niDS;
	}

	public final static DataSource provideReferenceNumberDS(IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService, EntityManager em) throws Exception {
		EntityType rnET = EntityTypeData.provide(entityTypeDAOService, em, ReferenceNumber.class);
		String[] visibleFieldNames = { "dateCreated", "dateLastUpdated", "value", "type" };
		List<String> visibleFieldNamesSet = Arrays.asList(visibleFieldNames);
		DataSource rnDS = dataSourceDAOService.provideCustomDataSource("default", rnET, visibleFieldNamesSet);

		HashSet<DataSourceField> flds = new HashSet<DataSourceField>(rnDS.getDSFields());
		for (DataSourceField fld : flds) {
			if (visibleFieldNamesSet.contains(fld.getName()))
				fld.setHidden(false);
			else
				fld.setHidden(true);
		}

		rnDS = dataSourceDAOService.update(rnDS);

		return rnDS;
	}
}
