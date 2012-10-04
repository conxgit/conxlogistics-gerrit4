package com.conx.logistics.data.uat.sprint2.data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.conx.logistics.app.whse.rcv.rcv.domain.Receive;
import com.conx.logistics.kernel.datasource.dao.services.IDataSourceDAOService;
import com.conx.logistics.kernel.datasource.domain.DataSource;
import com.conx.logistics.kernel.datasource.domain.DataSourceField;
import com.conx.logistics.kernel.metamodel.dao.services.IEntityTypeDAOService;
import com.conx.logistics.mdm.domain.BaseEntity;
import com.conx.logistics.mdm.domain.metamodel.EntityType;

@ContextConfiguration(locations = { "/META-INF/tm.jta-module-context.xml", "/META-INF/spring/persistence.datasource.mysql.local-module-context.xml",
		"/META-INF/spring/persistence.dynaconfiguration.local-module-context.xml", "/META-INF/datasource.dao.jpa.persistence-module-context.xml", "/META-INF/mdm.dao.services.impl-module-context.xml",
		"/META-INF/metamodel.dao.jpa.persistence-module-context.xml", "/META-INF/datasource.dao.jpa.persistence-module-context.xml", "/META-INF/components.dao.jpa.persistence-module-context.xml",
		"/META-INF/documentlibrary.remote.services.impl-module-context.xml", "/META-INF/app.whse.dao.jpa.persistence-module-context.xml",
		"/META-INF/app.whse.rcv.asn.dao.jpa.persistence-module-context.xml", "/META-INF/app.whse.rcv.rcv.dao.jpa.persistence-module-context.xml",
		"/META-INF/spring/data.uat.sprint2.data-module-context.xml" })
public class DataSourceTests extends AbstractTestNGSpringContextTests {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private EntityManagerFactory conxLogisticsManagerFactory;
	@Autowired
	private IEntityTypeDAOService entityTypeDAOService;
	@Autowired
	private IDataSourceDAOService dataSourceDAOService;

	@BeforeClass
	public void setUp() throws Exception {
		Assert.assertNotNull(applicationContext);
		Assert.assertNotNull(entityTypeDAOService);
		Assert.assertNotNull(conxLogisticsManagerFactory);
		Assert.assertNotNull(dataSourceDAOService);
	}

	@AfterClass
	public void tearDown() throws Exception {
	}
	
	@Test(enabled = false)
	public void testDefaultReceiveDataSource() throws Exception {
		EntityType rcvET = EntityTypeData.provide(entityTypeDAOService, em, Receive.class);
		Assert.assertNotNull(rcvET, "Provided EntityType rcvET was NULL.");

		DataSource receiveDS = dataSourceDAOService.provide(rcvET);
		Assert.assertNotNull(rcvET, "Provided DataSource receiveDS was NULL.");
		Assert.assertTrue(receiveDS.getDSFields().size() > 0, "No DataSourceFields were provided");

		String[] visibleFieldNames = { "id", "code", "name", "dateCreated", "dateLastUpdated", "warehouse" };
		List<String> visibleFieldNamesSet = Arrays.asList(visibleFieldNames);
		HashSet<DataSourceField> flds = new HashSet<DataSourceField>(receiveDS.getDSFields());
		for (DataSourceField fld : flds) {
			Assert.assertNotNull(fld, "A DataSourceField was NULL.");
			if (visibleFieldNamesSet.contains(fld.getName()))
				fld.setHidden(false);
			else
				fld.setHidden(true);

			if ("warehouse".equals(fld.getName())) {
				fld.setValueXPath("name");
			}
		}

		receiveDS = dataSourceDAOService.update(receiveDS);
		Assert.assertNotNull(receiveDS, "The DataSource was not updated successfully.");
	}
	
	@Test(enabled = false)
	public void testBasicReceiveDataSource() throws Exception {
		EntityType rcvET = EntityTypeData.provide(entityTypeDAOService, em, Receive.class);
		Assert.assertNotNull(rcvET, "Provided EntityType rcvET was NULL.");

		DataSource receiveDS = dataSourceDAOService.provide(rcvET);
		Assert.assertNotNull(rcvET, "Provided DataSource receiveDS was NULL.");
		Assert.assertTrue(receiveDS.getDSFields().size() > 0, "No DataSourceFields were provided");
		
		DataSourceField id = dataSourceDAOService.getFieldByName(receiveDS, BaseEntity.BASIC_ENTITY_ATTRIBUTE_ID);
		Assert.assertNotNull(id, "The DataSourceField id was NULL.");
		DataSourceField code = dataSourceDAOService.getFieldByName(receiveDS, BaseEntity.BASIC_ENTITY_ATTRIBUTE_CODE);
		Assert.assertNotNull(code, "The DataSourceField code was NULL.");
		DataSourceField name = dataSourceDAOService.getFieldByName(receiveDS, BaseEntity.BASIC_ENTITY_ATTRIBUTE_NAME);
		Assert.assertNotNull(name, "The DataSourceField name was NULL.");
		DataSourceField dateCreated = dataSourceDAOService.getFieldByName(receiveDS, BaseEntity.BASIC_ENTITY_ATTRIBUTE_DATE_CREATED);
		Assert.assertNotNull(dateCreated, "The DataSourceField dateCreated was NULL.");
		DataSourceField dateLastUpdated = dataSourceDAOService.getFieldByName(receiveDS, BaseEntity.BASIC_ENTITY_ATTRIBUTE_DATE_LAST_UPDATED);
		Assert.assertNotNull(dateLastUpdated, "The DataSourceField dateLastUpdated was NULL.");
		DataSourceField warehouse = dataSourceDAOService.getFieldByName(receiveDS, "warehouse");
		Assert.assertNotNull(warehouse, "The DataSourceField warehouse was NULL.");
		warehouse.setValueXPath("code");

		DataSourceField fields[] = { id, code, name, dateCreated, dateLastUpdated, warehouse };
		Set<DataSourceField> fieldSet = new HashSet<DataSourceField>(Arrays.asList(fields));
		receiveDS = dataSourceDAOService.addFields(receiveDS, fieldSet);
		Assert.assertNotNull(receiveDS, "The DataSourceFields were not added successfully.");
		receiveDS = dataSourceDAOService.update(receiveDS);
		Assert.assertNotNull(receiveDS, "The DataSource was not updated successfully.");
	}
	
	@Test(enabled = false)
	public void testFileEntryDataSource() throws Exception {
		DataSource ds = DataSourceData.provideFileEntryDS(entityTypeDAOService, dataSourceDAOService, em);
		Assert.assertNotNull(ds, "The DataSource was not updated successfully.");
	}
	
	@Test(enabled = false)
	public void testNoteItemDataSource() throws Exception {
		DataSource ds = DataSourceData.provideNoteItemDS(entityTypeDAOService, dataSourceDAOService, em);
		Assert.assertNotNull(ds, "The DataSource was not updated successfully.");
	}
	
	@Test(enabled = false)
	public void testReferenceNumberDataSource() throws Exception {
		DataSource ds = DataSourceData.provideReferenceNumberDS(entityTypeDAOService, dataSourceDAOService, em);
		Assert.assertNotNull(ds, "The DataSource was not updated successfully.");
	}
	
	@Test(enabled = false)
	public void testDefaultReceiveLineDataSource() throws Exception {
		DataSource ds = DataSourceData.provideDefaultReceiveLineDS(entityTypeDAOService, dataSourceDAOService, em);
		Assert.assertNotNull(ds, "The DataSource was not updated successfully.");
	}
	
	@Test(enabled = false)
	public void testWeightAndDimensionsDataSource() throws Exception {
		EntityType rcvET = EntityTypeData.provide(entityTypeDAOService, em, Receive.class);
		Assert.assertNotNull(rcvET, "Provided EntityType rcvET was NULL.");

		DataSource receiveDS = dataSourceDAOService.provide(rcvET);
		Assert.assertNotNull(rcvET, "Provided DataSource receiveDS was NULL.");
		Assert.assertTrue(receiveDS.getDSFields().size() > 0, "No DataSourceFields were provided");

		DataSourceField expectedTotalWeight = dataSourceDAOService.getFieldByName(receiveDS, "expectedTotalweight");
		Assert.assertNotNull(expectedTotalWeight, "The DataSourceField expectedTotalWeight was NULL.");
		expectedTotalWeight.setRequired(true);
		expectedTotalWeight = dataSourceDAOService.update(expectedTotalWeight);
		DataSourceField weightUnit = dataSourceDAOService.getFieldByName(receiveDS, "weightUnit");
		Assert.assertNotNull(weightUnit, "The DataSourceField weightUnit was NULL.");
		weightUnit.setRequired(true);
		weightUnit = dataSourceDAOService.update(weightUnit);
		DataSourceField expectedTotalVolume = dataSourceDAOService.getFieldByName(receiveDS, "expectedTotalVolume");
		Assert.assertNotNull(expectedTotalVolume, "The DataSourceField expectedTotalVolume was NULL.");
		expectedTotalVolume.setRequired(true);
		expectedTotalVolume = dataSourceDAOService.update(expectedTotalVolume);
		DataSourceField volumeUnit = dataSourceDAOService.getFieldByName(receiveDS, "volUnit");
		Assert.assertNotNull(volumeUnit, "The DataSourceField volumeUnit was NULL.");
		volumeUnit.setRequired(true);
		volumeUnit = dataSourceDAOService.update(volumeUnit);
		
		DataSourceField fields[] = { expectedTotalWeight, weightUnit, expectedTotalVolume, volumeUnit };
		Set<DataSourceField> fieldSet = new HashSet<DataSourceField>(Arrays.asList(fields));
		receiveDS = dataSourceDAOService.addFields(receiveDS, fieldSet);
		Assert.assertNotNull(receiveDS, "The DataSourceFields were not added successfully.");
		receiveDS = dataSourceDAOService.update(receiveDS);
		Assert.assertNotNull(receiveDS, "The DataSource was not updated successfully.");
	}
}
