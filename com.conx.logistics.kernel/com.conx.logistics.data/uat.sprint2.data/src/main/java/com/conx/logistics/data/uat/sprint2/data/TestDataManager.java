package com.conx.logistics.data.uat.sprint2.data;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.conx.logistics.app.whse.dao.services.IDockTypeDAOService;
import com.conx.logistics.app.whse.dao.services.IWarehouseDAOService;
import com.conx.logistics.app.whse.domain.constants.WarehouseCustomCONSTANTS;
import com.conx.logistics.app.whse.domain.warehouse.Warehouse;
import com.conx.logistics.app.whse.rcv.asn.dao.services.IASNDAOService;
import com.conx.logistics.app.whse.rcv.asn.dao.services.IASNDropOffDAOService;
import com.conx.logistics.app.whse.rcv.asn.dao.services.IASNPickupDAOService;
import com.conx.logistics.app.whse.rcv.asn.domain.ASN;
import com.conx.logistics.app.whse.rcv.asn.domain.ASNDropOff;
import com.conx.logistics.app.whse.rcv.asn.domain.ASNLine;
import com.conx.logistics.app.whse.rcv.asn.domain.ASNPickup;
import com.conx.logistics.app.whse.rcv.rcv.dao.services.IReceiveDAOService;
import com.conx.logistics.app.whse.rcv.rcv.domain.Receive;
import com.conx.logistics.kernel.datasource.dao.services.IDataSourceDAOService;
import com.conx.logistics.kernel.datasource.domain.DataSource;
import com.conx.logistics.kernel.documentlibrary.remote.services.IRemoteDocumentRepository;
import com.conx.logistics.kernel.metamodel.dao.services.IEntityTypeDAOService;
import com.conx.logistics.kernel.ui.components.dao.services.IComponentDAOService;
import com.conx.logistics.mdm.dao.services.IAddressDAOService;
import com.conx.logistics.mdm.dao.services.IAddressTypeAddressDAOService;
import com.conx.logistics.mdm.dao.services.IAddressTypeDAOService;
import com.conx.logistics.mdm.dao.services.IContactDAOService;
import com.conx.logistics.mdm.dao.services.IContactTypeContactDAOService;
import com.conx.logistics.mdm.dao.services.IContactTypeDAOService;
import com.conx.logistics.mdm.dao.services.ICountryDAOService;
import com.conx.logistics.mdm.dao.services.ICountryStateDAOService;
import com.conx.logistics.mdm.dao.services.IEntityMetadataDAOService;
import com.conx.logistics.mdm.dao.services.IOrganizationDAOService;
import com.conx.logistics.mdm.dao.services.IUnlocoDAOService;
import com.conx.logistics.mdm.dao.services.currency.ICurrencyUnitDAOService;
import com.conx.logistics.mdm.dao.services.documentlibrary.IDocTypeDAOService;
import com.conx.logistics.mdm.dao.services.documentlibrary.IFolderDAOService;
import com.conx.logistics.mdm.dao.services.note.INoteDAOService;
import com.conx.logistics.mdm.dao.services.product.IDimUnitDAOService;
import com.conx.logistics.mdm.dao.services.product.IPackUnitDAOService;
import com.conx.logistics.mdm.dao.services.product.IProductDAOService;
import com.conx.logistics.mdm.dao.services.product.IProductTypeDAOService;
import com.conx.logistics.mdm.dao.services.product.IWeightUnitDAOService;
import com.conx.logistics.mdm.dao.services.referencenumber.IReferenceNumberDAOService;
import com.conx.logistics.mdm.dao.services.referencenumber.IReferenceNumberTypeDAOService;
import com.conx.logistics.mdm.domain.constants.AddressTypeCustomCONSTANTS;
import com.conx.logistics.mdm.domain.constants.ContactTypeCustomCONSTANTS;
import com.conx.logistics.mdm.domain.constants.DimUnitCustomCONSTANTS;
import com.conx.logistics.mdm.domain.constants.DocTypeCustomCONSTANTS;
import com.conx.logistics.mdm.domain.constants.NoteTypeCustomCONSTANTS;
import com.conx.logistics.mdm.domain.constants.PackUnitCustomCONSTANTS;
import com.conx.logistics.mdm.domain.constants.ProductTypeCustomCONSTANTS;
import com.conx.logistics.mdm.domain.constants.ReferenceNumberTypeCustomCONSTANTS;
import com.conx.logistics.mdm.domain.constants.WeightUnitCustomCONSTANTS;
import com.conx.logistics.mdm.domain.documentlibrary.DocType;
import com.conx.logistics.mdm.domain.documentlibrary.FileEntry;
import com.conx.logistics.mdm.domain.geolocation.Address;
import com.conx.logistics.mdm.domain.geolocation.AddressType;
import com.conx.logistics.mdm.domain.geolocation.AddressTypeAddress;
import com.conx.logistics.mdm.domain.geolocation.Country;
import com.conx.logistics.mdm.domain.geolocation.CountryState;
import com.conx.logistics.mdm.domain.geolocation.Unloco;
import com.conx.logistics.mdm.domain.metadata.DefaultEntityMetadata;
import com.conx.logistics.mdm.domain.metamodel.EntityType;
import com.conx.logistics.mdm.domain.note.NoteItem;
import com.conx.logistics.mdm.domain.note.NoteType;
import com.conx.logistics.mdm.domain.organization.Contact;
import com.conx.logistics.mdm.domain.organization.ContactType;
import com.conx.logistics.mdm.domain.organization.ContactTypeContact;
import com.conx.logistics.mdm.domain.organization.Organization;
import com.conx.logistics.mdm.domain.product.Product;
import com.conx.logistics.mdm.domain.referencenumber.ReferenceNumber;
import com.conx.logistics.mdm.domain.referencenumber.ReferenceNumberType;

public class TestDataManager implements ITestDataManager {
	protected static Logger logger = LoggerFactory.getLogger(TestDataManager.class);

	private EntityManagerFactory conxlogisticsEMF;

	private PlatformTransactionManager globalTransactionManager;

	private IOrganizationDAOService orgDaoService;
	private ICountryDAOService countryDaoService;
	private ICountryStateDAOService countryStateDaoService;
	private IUnlocoDAOService unlocoDaoService;
	private IAddressDAOService addressDaoService;
	private IAddressTypeDAOService addressTypeDaoService;
	private IAddressTypeAddressDAOService addressTypeAddressDaoService;

	private IPackUnitDAOService packUnitDaoService;
	private IDimUnitDAOService dimUnitDaoService;
	private IWeightUnitDAOService weightUnitDaoService;
	private IProductTypeDAOService productTypeDaoService;
	private IProductDAOService productDaoService;
	private ICurrencyUnitDAOService currencyUnitDAOService;
	private IASNDAOService asnDaoService;
	private IASNPickupDAOService asnPickupDAOService;
	private IASNDropOffDAOService asnDropOffDAOService;

	private IContactDAOService contactDAOService;
	private IContactTypeDAOService contactTypeDAOService;
	private IContactTypeContactDAOService contactTypeContactDAOService;
	private IDocTypeDAOService docTypeDOAService;
	private IDockTypeDAOService dockTypeDOAService;
	private IEntityMetadataDAOService entityMetadataDAOService;

	private IReferenceNumberTypeDAOService referenceNumberTypeDaoService;
	private IReferenceNumberDAOService referenceNumberDaoService;

	private UserTransaction userTransaction;

	private IWarehouseDAOService whseDAOService;
	private IReceiveDAOService rcvDaoService;
	private IComponentDAOService componentDAOService;
	private IEntityTypeDAOService entityTypeDAOService;
	private IDataSourceDAOService dataSourceDAOService;
	private IRemoteDocumentRepository documentRepositoryService;
	private IFolderDAOService folderDAOService;
	private INoteDAOService noteDAOService;

	private EntityManager em;

	public void start() {
		this.em = conxlogisticsEMF.createEntityManager();

		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setName("uat.sprint2.data");
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		def.setTimeout(1000);
		TransactionStatus status = this.globalTransactionManager.getTransaction(def);
		try {
			// this.userTransaction.begin();
			TestDataManager.generateData(em, orgDaoService, countryDaoService, countryStateDaoService, unlocoDaoService, addressDaoService, addressTypeDaoService, addressTypeAddressDaoService,
					packUnitDaoService, dimUnitDaoService, weightUnitDaoService, productTypeDaoService, productDaoService, currencyUnitDAOService, asnDaoService, asnPickupDAOService,
					asnDropOffDAOService, contactDAOService, contactTypeDAOService, contactTypeContactDAOService, docTypeDOAService, dockTypeDOAService, entityMetadataDAOService,
					referenceNumberTypeDaoService, referenceNumberDaoService, rcvDaoService, componentDAOService, entityTypeDAOService, dataSourceDAOService, whseDAOService,
					documentRepositoryService, folderDAOService, noteDAOService);
			// this.userTransaction.commit();
			 this.globalTransactionManager.commit(status);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);
			this.globalTransactionManager.rollback(status);
//
//			try {
//				this.userTransaction.rollback();
//			} catch (IllegalStateException e1) {
//				e1.printStackTrace();
//			} catch (SecurityException e1) {
//				e1.printStackTrace();
//			} catch (SystemException e1) {
//				e1.printStackTrace();
//			}

			throw new RuntimeException(stacktrace, e);
		}
	}

	public void stop() {
		if (this.em != null && this.em.isOpen())
			this.em.close();
	}

	public static void generateData(EntityManager em, IOrganizationDAOService orgDaoService, ICountryDAOService countryDaoService, ICountryStateDAOService countryStateDaoService,
			IUnlocoDAOService unlocoDaoService, IAddressDAOService addressDaoService, IAddressTypeDAOService addressTypeDaoService, IAddressTypeAddressDAOService addressTypeAddressDaoService,
			IPackUnitDAOService packUnitDaoService, IDimUnitDAOService dimUnitDaoService, IWeightUnitDAOService weightUnitDaoService, IProductTypeDAOService productTypeDaoService,
			IProductDAOService productDaoService, ICurrencyUnitDAOService currencyUnitDAOService, IASNDAOService asnDaoService, IASNPickupDAOService asnPickupDAOService,
			IASNDropOffDAOService asnDropOffDAOService, IContactDAOService contactDAOService, IContactTypeDAOService contactTypeDAOService, IContactTypeContactDAOService contactTypeContactDAOService,
			IDocTypeDAOService docTypeDOAService, IDockTypeDAOService dockTypeDOAService, IEntityMetadataDAOService entityMetadataDAOService,
			IReferenceNumberTypeDAOService referenceNumberTypeDaoService, IReferenceNumberDAOService referenceNumberDaoService, IReceiveDAOService rcvDaoService,
			IComponentDAOService componentDAOService, IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService, IWarehouseDAOService whseDAOService,
			IRemoteDocumentRepository documentRepositoryService, IFolderDAOService folderDAOService, INoteDAOService noteDAOService) throws Exception {

		// Required for ASN
		ASN[] asn = createSprint1Data(null, em, orgDaoService, countryDaoService, countryStateDaoService, unlocoDaoService, addressDaoService, addressTypeDaoService, addressTypeAddressDaoService,
				packUnitDaoService, dimUnitDaoService, weightUnitDaoService, productTypeDaoService, productDaoService, currencyUnitDAOService, asnDaoService, asnPickupDAOService,
				asnDropOffDAOService, contactDAOService, contactTypeDAOService, contactTypeContactDAOService, docTypeDOAService, dockTypeDOAService, entityMetadataDAOService,
				referenceNumberTypeDaoService, referenceNumberDaoService, rcvDaoService, componentDAOService, entityTypeDAOService, dataSourceDAOService, whseDAOService);
		createPrint2Data(asn, null, em, orgDaoService, countryDaoService, countryStateDaoService, unlocoDaoService, addressDaoService, packUnitDaoService, dimUnitDaoService, weightUnitDaoService,
				productTypeDaoService, productDaoService, currencyUnitDAOService, asnDaoService, asnPickupDAOService, asnDropOffDAOService, contactDAOService, docTypeDOAService, dockTypeDOAService,
				entityMetadataDAOService, referenceNumberTypeDaoService, referenceNumberDaoService, rcvDaoService, componentDAOService, entityTypeDAOService, dataSourceDAOService, whseDAOService,
				documentRepositoryService, folderDAOService, noteDAOService);

		// Create Datasource/Component models
		UIComponentModelData.createReceiveSearchMasterDetail(componentDAOService, entityTypeDAOService, dataSourceDAOService, em);
		UIComponentModelData.createArrivalSearchMasterDetail(componentDAOService, entityTypeDAOService, dataSourceDAOService, em);
	}

	public static Receive[] createPrint2Data(ASN[] asns, PlatformTransactionManager globalTransactionManager, EntityManager em, IOrganizationDAOService orgDaoService,
			ICountryDAOService countryDaoService, ICountryStateDAOService countryStateDaoService, IUnlocoDAOService unlocoDaoService, IAddressDAOService addressDaoService,
			IPackUnitDAOService packUnitDaoService, IDimUnitDAOService dimUnitDaoService, IWeightUnitDAOService weightUnitDaoService, IProductTypeDAOService productTypeDaoService,
			IProductDAOService productDaoService, ICurrencyUnitDAOService currencyUnitDAOService, IASNDAOService asnDaoService, IASNPickupDAOService asnPickupDAOService,
			IASNDropOffDAOService asnDropOffDAOService, IContactDAOService contactDAOService, IDocTypeDAOService docTypeDOAService, IDockTypeDAOService dockTypeDOAService,
			IEntityMetadataDAOService entityMetadataDAOService, IReferenceNumberTypeDAOService referenceNumberTypeDaoService, IReferenceNumberDAOService referenceNumberDaoService,
			IReceiveDAOService rcvDaoService, IComponentDAOService componentDAOService, IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService,
			IWarehouseDAOService whseDAOService, IRemoteDocumentRepository documentRepositoryService, IFolderDAOService folderDAOService, INoteDAOService noteDAOService)
			throws ClassNotFoundException, Exception {

		Receive[] rcvs = new Receive[2];
		/**
		 * 
		 * provide Defaults
		 * 
		 */
		docTypeDOAService.provideDefaults();
		noteDAOService.provideDefaults();

		/**
		 * 
		 * Receive #1
		 * 
		 */

		ASN asn = asns[0];

		Receive rcv = rcvDaoService.process(asn);

		/**
		 * 
		 * Add attachment
		 * 
		 */
		URL testfile = TestDataManager.class.getResource("/bol.pdf");
		File file = new File(testfile.toURI());
		DocType dt = docTypeDOAService.getByCode(DocTypeCustomCONSTANTS.TYPE_BOL_CODE);
		// FileEntry fe = rcvDaoService.addAttachment(rcv.getId(),file,
		// "Bill Of Laden", "Bill Of Laden", "application/pdf", dt);
		FileEntry fe = rcvDaoService.addAttachment(rcv, file, "Bill Of Laden", "Bill Of Laden", "application/pdf", dt);

		testfile = TestDataManager.class.getResource("/SamplePurchaseRequisition.jpg");
		file = new File(testfile.toURI());
		dt = docTypeDOAService.getByCode(DocTypeCustomCONSTANTS.TYPE_PO_CODE);
		fe = rcvDaoService.addAttachment(rcv.getId(), file, "PO", "PO", "image/jpeg", dt);

		/**
		 * 
		 * Add attachment
		 * 
		 */
		NoteType nt = noteDAOService.getByNoteTypeCode(NoteTypeCustomCONSTANTS.TYPE_OTHER_CODE);
		NoteItem note = rcvDaoService.addNoteItem(rcv.getId(), "call when it starts arriving", nt);

		/**
		 * 
		 * ASN Arrival
		 * 
		 */

		/**
		 * 
		 * Dynamic Arrival
		 * 
		 */
		rcvs[0] = rcv;

		/**
		 * 
		 * Receive #2
		 * 
		 */

		asn = asns[1];

		rcv = rcvDaoService.process(asn);

		/**
		 * 
		 * Add attachment
		 * 
		 */
		testfile = TestDataManager.class.getResource("/bol.pdf");
		file = new File(testfile.toURI());
		dt = docTypeDOAService.getByCode(DocTypeCustomCONSTANTS.TYPE_BOL_CODE);
		fe = rcvDaoService.addAttachment(rcv.getId(), file, "Bill Of Laden", "Bill Of Laden", "application/pdf", dt);

		testfile = TestDataManager.class.getResource("/SamplePurchaseRequisition.jpg");
		file = new File(testfile.toURI());
		dt = docTypeDOAService.getByCode(DocTypeCustomCONSTANTS.TYPE_PO_CODE);
		fe = rcvDaoService.addAttachment(rcv.getId(), file, "PO", "PO", "image/jpeg", dt);

		/**
		 * 
		 * Add attachment
		 * 
		 */
		nt = noteDAOService.getByNoteTypeCode(NoteTypeCustomCONSTANTS.TYPE_OTHER_CODE);
		note = rcvDaoService.addNoteItem(rcv.getId(), "call when it starts arriving", nt);

		/**
		 * 
		 * ASN Arrival
		 * 
		 */

		/**
		 * 
		 * Dynamic Arrival
		 * 
		 */
		rcvs[1] = rcv;

		return rcvs;
	}

	public static ASN[] createSprint1Data(PlatformTransactionManager globalTransactionManager, EntityManager em, IOrganizationDAOService orgDaoService, ICountryDAOService countryDaoService,
			ICountryStateDAOService countryStateDaoService, IUnlocoDAOService unlocoDaoService, IAddressDAOService addressDaoService, IAddressTypeDAOService addressTypeDaoService,
			IAddressTypeAddressDAOService addressTypeAddressDaoService, IPackUnitDAOService packUnitDaoService, IDimUnitDAOService dimUnitDaoService, IWeightUnitDAOService weightUnitDaoService,
			IProductTypeDAOService productTypeDaoService, IProductDAOService productDaoService, ICurrencyUnitDAOService currencyUnitDAOService, IASNDAOService asnDaoService,
			IASNPickupDAOService asnPickupDAOService, IASNDropOffDAOService asnDropOffDAOService, IContactDAOService contactDAOService, IContactTypeDAOService contactTypeDAOService,
			IContactTypeContactDAOService contactTypeContactDAOService, IDocTypeDAOService docTypeDOAService, IDockTypeDAOService dockTypeDOAService,
			IEntityMetadataDAOService entityMetadataDAOService, IReferenceNumberTypeDAOService referenceNumberTypeDaoService, IReferenceNumberDAOService referenceNumberDaoService,
			IReceiveDAOService rcvDaoService, IComponentDAOService componentDAOService, IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService,
			IWarehouseDAOService whseDAOService) throws Exception {
		ASN[] asns = new ASN[2];
		ASN asn = null;

		/**
		 * Org Data: TD ORG 1.0, 4.0, 6.0, 7.0
		 * 
		 * Prod Data: TD PRD 2.0, 3.0, 4.0
		 * 
		 * Ref IDs: TD RIDTYP 2.0, 3.0, 4.0
		 * 
		 */

		try {
			Organization record = orgDaoService.getByCode("TESCUS1");
			logger.info("Checking Sprint # 1 UAT data");
			if (record == null) {
				logger.info("Generating Sprint # 1 UAT ");
				/**
				 * Org Data: TD ORG 1.0, 4.0, 6.0, 7.0
				 */
				// -- Unlocos:
				Country de = countryDaoService.provide("DE", "Germany");
				CountryState csdefra = countryStateDaoService.provide("HE", "Hessen", de.getId());
				Unloco defra = unlocoDaoService.provide("DEFRA", "", "Frankfurt am Main", de.getId(), csdefra.getId());

				Country gb = countryDaoService.provide("GB", "United Kingdom");
				CountryState csgbhlr = countryStateDaoService.provide("GTL", "Greater London", de.getId());
				Unloco gbhlr = unlocoDaoService.provide("GBLHR", "", "Heathrow Apt/London	LHR", de.getId(), csdefra.getId());

				Country us = countryDaoService.provide("US", "United States");
				CountryState csusbwi = countryStateDaoService.provide("MD", "Maryland", us.getId());
				Unloco usbwi = unlocoDaoService.provide("USBWI", "", "Washington-Baltimore Int Apt", de.getId(), csdefra.getId());

				CountryState csusdfw = countryStateDaoService.provide("TX", "Texas", us.getId());
				Unloco usdfw = unlocoDaoService.provide("USDFW", "", "Dallas-Fort Worth Int Apt", de.getId(), csdefra.getId());

				CountryState csusntn = countryStateDaoService.provide("MA", "Massachusetts", us.getId());
				Unloco usntn = unlocoDaoService.provide("USNTN", "", "Newton", de.getId(), csdefra.getId());

				CountryState csussfo = countryStateDaoService.provide("CA", "California", us.getId());
				Unloco ussfo = unlocoDaoService.provide("USSFO", "", "San Francisco", de.getId(), csdefra.getId());

				EntityType type = entityTypeDAOService.provide(Organization.class);

				// -- Orgs:
				DefaultEntityMetadata orgEMD = entityMetadataDAOService.getByClass(Organization.class);
				// ------------ 1.0-TESCUS1:
				Organization tescus1 = new Organization();
				tescus1.setName("Test Customer 1");
				tescus1.setCode("TESCUS1");
				tescus1 = orgDaoService.add(tescus1);

				Address tescus1_addr = addressDaoService.provide(type, tescus1.getId(), "123 Main St Suite 1", null, null, null, "USDFW", null, us.getCode(), us.getName(), null, null);
				AddressType tescus1_addr_type = addressTypeDaoService.provide(AddressTypeCustomCONSTANTS.TYPE_MAIN, AddressTypeCustomCONSTANTS.TYPE_MAIN_DESCRIPTION);
				AddressTypeAddress tescus1_ata = addressTypeAddressDaoService.provide(tescus1_addr_type, tescus1_addr);
				tescus1_ata.setOwnerEntityId(tescus1.getId());
				tescus1_ata.setOwnerEntityTypeId(type.getId());
				addressTypeAddressDaoService.update(tescus1_ata);
				tescus1.getAddressTypeAddresses().add(tescus1_ata);
				tescus1 = orgDaoService.update(tescus1);

				Contact tescus1_contact = new Contact();
				tescus1_contact.setFirstName("Aaron");
				tescus1_contact.setLastName("Anderson");
				tescus1_contact = contactDAOService.provide(orgEMD, tescus1.getId(), tescus1_contact);
				ContactType tescus1_contact_type = contactTypeDAOService.provide(ContactTypeCustomCONSTANTS.TYPE_MAIN, ContactTypeCustomCONSTANTS.TYPE_MAIN_DESCRIPTION);
				ContactTypeContact tescus1_ctc = contactTypeContactDAOService.provide(tescus1_contact_type, tescus1_contact);
				tescus1_ctc.setOwnerEntityId(tescus1_ata.getId());
				tescus1_ctc = contactTypeContactDAOService.update(tescus1_ctc);
				tescus1.getContactTypeContacts().add(tescus1_ctc);

				tescus1 = orgDaoService.update(tescus1);

				// ------------ 4.0-TESCAR1:
				Organization tescar1 = new Organization();
				tescar1.setName("Test Carrier 1");
				tescar1.setCode("TESCAR1");
				tescar1 = orgDaoService.add(tescar1);

				Address tescar1_addr = addressDaoService.provide(entityTypeDAOService.provide(Organization.class), tescar1.getId(), "123 Main St	Suite 1", null, null, null, "USDFW", null,
						us.getCode(), us.getName(), null, null);
				AddressType tescar1_addr_type = addressTypeDaoService.provide(AddressTypeCustomCONSTANTS.TYPE_MAIN, AddressTypeCustomCONSTANTS.TYPE_MAIN_DESCRIPTION);
				AddressTypeAddress tescar1_ata = addressTypeAddressDaoService.provide(tescar1_addr_type, tescar1_addr);
				tescar1_ata.setOwnerEntityId(tescar1.getId());
				tescar1_ata.setOwnerEntityTypeId(type.getId());
				addressTypeAddressDaoService.update(tescar1_ata);
				tescar1.getAddressTypeAddresses().add(tescar1_ata);
				tescar1 = orgDaoService.update(tescar1);

				Contact tescar1_contact = new Contact();
				tescar1_contact.setFirstName("Don");
				tescar1_contact.setLastName("Davis");
				tescar1_contact = contactDAOService.provide(orgEMD, tescar1.getId(), tescar1_contact);
				ContactType tescar1_contact_type = contactTypeDAOService.provide(ContactTypeCustomCONSTANTS.TYPE_MAIN, ContactTypeCustomCONSTANTS.TYPE_MAIN_DESCRIPTION);
				ContactTypeContact tescar1_ctc = contactTypeContactDAOService.provide(tescar1_contact_type, tescar1_contact);
				tescar1_ctc.setOwnerEntityId(tescar1_ata.getId());
				tescar1_ctc = contactTypeContactDAOService.update(tescar1_ctc);
				tescar1.getContactTypeContacts().add(tescar1_ctc);

				tescar1 = orgDaoService.update(tescar1);

				// ------------ 6.0-TESLOC1:
				Organization tesloc1 = new Organization();
				tesloc1.setName("Test Location 1");
				tesloc1.setCode("TESLOC1");
				tesloc1 = orgDaoService.add(tesloc1);

				Address tesloc1_addr = addressDaoService.provide(entityTypeDAOService.provide(Organization.class), tesloc1.getId(), "7 West Penn St", null, null, null, "USNTN", null, us.getCode(),
						us.getName(), null, null);
				AddressType tesloc1_addr_type = addressTypeDaoService.provide(AddressTypeCustomCONSTANTS.TYPE_MAIN, AddressTypeCustomCONSTANTS.TYPE_MAIN_DESCRIPTION);
				AddressTypeAddress tesloc1_ata = addressTypeAddressDaoService.provide(tesloc1_addr_type, tesloc1_addr);
				tesloc1_ata.setOwnerEntityId(tesloc1.getId());
				tesloc1_ata.setOwnerEntityTypeId(type.getId());
				addressTypeAddressDaoService.update(tesloc1_ata);
				tesloc1.getAddressTypeAddresses().add(tesloc1_ata);
				tesloc1 = orgDaoService.update(tesloc1);

				Contact tesloc1_contact = new Contact();
				tesloc1_contact.setFirstName("Jon");
				tesloc1_contact.setLastName("Drews");
				tesloc1_contact = contactDAOService.provide(orgEMD, tesloc1.getId(), tesloc1_contact);
				ContactType tesloc1_contact_type = contactTypeDAOService.provide(ContactTypeCustomCONSTANTS.TYPE_MAIN, ContactTypeCustomCONSTANTS.TYPE_MAIN_DESCRIPTION);
				ContactTypeContact tesloc1_ctc = contactTypeContactDAOService.provide(tesloc1_contact_type, tesloc1_contact);
				tesloc1_ctc.setOwnerEntityId(tesloc1_ata.getId());
				tesloc1_ctc = contactTypeContactDAOService.update(tesloc1_ctc);
				tesloc1.getContactTypeContacts().add(tesloc1_ctc);

				tesloc1 = orgDaoService.update(tesloc1);

				/**
				 * Prod Data: TD PRD 2.0, 3.0, 4.0
				 */
				// -- PRD 2.0
				whseDAOService.provideDefaults();
				packUnitDaoService.provideDefaults();
				dimUnitDaoService.provideDefaults();
				weightUnitDaoService.provideDefaults();
				productTypeDaoService.provideDefaults();
				referenceNumberTypeDaoService.provideDefaults();
				currencyUnitDAOService.provideDefaults();
				docTypeDOAService.provideDefaults();
				dockTypeDOAService.provideDefaults();
				addressTypeDaoService.provideDefaults();
				contactTypeDAOService.provideDefaults();
				/**
				 * Ref IDs: TD RIDTYP 2.0, 3.0, 4.0
				 */
				referenceNumberTypeDaoService.provideDefaults();
				Product prd2 = productDaoService.provide("fooite1", "banana's", ProductTypeCustomCONSTANTS.TYPE_Food_Item, PackUnitCustomCONSTANTS.TYPE_PCE, WeightUnitCustomCONSTANTS.TYPE_LB,
						DimUnitCustomCONSTANTS.TYPE_FT, DimUnitCustomCONSTANTS.TYPE_CF, "GEN", null);
				Product prd3 = productDaoService.provide("hazmat1", "Jet Fuel", ProductTypeCustomCONSTANTS.TYPE_Hazardous_Material, PackUnitCustomCONSTANTS.TYPE_PCE,
						WeightUnitCustomCONSTANTS.TYPE_LB, DimUnitCustomCONSTANTS.TYPE_FT, DimUnitCustomCONSTANTS.TYPE_CF, "GEN", null);
				Product prd4 = productDaoService.provide("textil1", "Clothing", ProductTypeCustomCONSTANTS.TYPE_Textiles, PackUnitCustomCONSTANTS.TYPE_PCE, WeightUnitCustomCONSTANTS.TYPE_LB,
						DimUnitCustomCONSTANTS.TYPE_FT, DimUnitCustomCONSTANTS.TYPE_CF, "GEN", null);

				/**
				 * 
				 * Sample ASN#1
				 * 
				 */
				DefaultEntityMetadata asnEMD = entityMetadataDAOService.getByClass(ASN.class);
				// - Ref Numbers
				ReferenceNumberType fedexRefType = referenceNumberTypeDaoService.getByCode(ReferenceNumberTypeCustomCONSTANTS.TYPE_FEDEX);
				ReferenceNumber rn1 = new ReferenceNumber();
				rn1.setValue("122345678899");
				rn1.setType(fedexRefType);
				rn1.setEntityMetadata(asnEMD);
				rn1 = referenceNumberDaoService.add(rn1);

				ReferenceNumber rn2 = new ReferenceNumber();
				rn2.setValue("998877665544332211");
				rn2.setType(fedexRefType);
				rn2.setEntityMetadata(asnEMD);
				rn2 = referenceNumberDaoService.add(rn2);

				Set<ReferenceNumber> refNumList = new HashSet<ReferenceNumber>();
				refNumList.add(rn1);
				refNumList.add(rn2);

				asn = new ASN();
				asn.setCode("ASN1");
				asn = asnDaoService.add(asn);

				asn = asnDaoService.addRefNums(asn.getId(), refNumList);

				Iterator<ReferenceNumber> rnsIt = asn.getRefNumbers().iterator();

				ASNLine al1 = new ASNLine();
				al1.setCode("AL1");
				al1.setProduct(prd2);
				// al1.setRefNumber(rnsIt.next());
				al1.setLineNumber(0);
				al1.setExpectedInnerPackCount(8);
				al1.setExpectedOuterPackCount(12);
				al1.setExpectedTotalWeight(28.0);
				al1.setExpectedTotalVolume(12.54);
				al1.setExpectedTotalLen(9.3);
				al1.setExpectedTotalWidth(1.0);
				al1.setExpectedTotalHeight(1.39);
				al1.setDescription("A90234708-3292389 Laptop Package");

				ASNLine al2 = new ASNLine();
				al2.setCode("AL2");
				al2.setProduct(prd3);
				// al2.setRefNumber(rnsIt.next());
				al2.setLineNumber(0);
				al2.setExpectedInnerPackCount(18);
				al2.setExpectedOuterPackCount(2);
				al2.setExpectedTotalWeight(28.0);
				al2.setExpectedTotalVolume(12.54);
				al2.setExpectedTotalLen(9.3);
				al2.setExpectedTotalWidth(1.0);
				al2.setExpectedTotalHeight(1.39);
				al2.setDescription("AODK-DLKDJ WKIWKWI");

				Set<ASNLine> asnLineList = new HashSet<ASNLine>();
				asnLineList.add(al1);
				asnLineList.add(al2);

				ASNPickup pickup1 = new ASNPickup();
				ASNDropOff dropOff1 = new ASNDropOff();

				pickup1.setCode("PKUP1");
				pickup1.setPickUpFrom(tescus1);
				pickup1.setPickUpFromAddress(tescus1_ata);
				pickup1.setLocalTrans(tescar1);
				pickup1.setLocalTransAddress(tescus1_ata);
				pickup1.setDriverId("DRV001");
				pickup1.setVehicleId("DRV001");
				pickup1.setBolNumber("DRV001");
				pickup1.setVehicleId("DRV001");
				pickup1.setEstimatedPickup(new Date());

				pickup1 = asnPickupDAOService.add(pickup1);

				dropOff1.setDropOffAt(tescus1);
				dropOff1.setCode("DRPOF1");
				dropOff1.setDropOffAtAddress(tescus1_addr);
				dropOff1.setEstimatedDropOff(new Date());
				dropOff1 = asnDropOffDAOService.add(dropOff1);

				Warehouse warehouse = whseDAOService.getByCode(WarehouseCustomCONSTANTS.DEFAULT_WAREHOUSE_CODE);
				asn.setWarehouse(warehouse);

				asn = asnDaoService.addLines(asn, asnLineList);
				asn = asnDaoService.addLocalTrans(asn, pickup1, dropOff1);
				asn = asnDaoService.update(asn);

				// rn1.setEntityPK(asn.getId());
				// rn2.setEntityPK(asn.getId());
				// referenceNumberDaoService.update(rn1);
				// referenceNumberDaoService.update(rn2);

				// asn = asnDaoService.update(asn);

				asns[0] = asn;

				/**
				 * 
				 * Sample ASN#2
				 * 
				 */
				// - Ref Numbers
				ReferenceNumberType fedexRefType2 = referenceNumberTypeDaoService.getByCode(ReferenceNumberTypeCustomCONSTANTS.TYPE_CarrierId_NAME);
				ReferenceNumber rn21 = new ReferenceNumber();
				rn21.setValue("12238888888888");
				rn21.setType(fedexRefType2);
				rn21.setEntityMetadata(asnEMD);
				rn21 = referenceNumberDaoService.add(rn21);

				ReferenceNumber rn22 = new ReferenceNumber();
				rn22.setValue("99884444444444");
				rn22.setType(fedexRefType2);
				rn22.setEntityMetadata(asnEMD);
				rn22 = referenceNumberDaoService.add(rn22);

				Set<ReferenceNumber> refNumList2 = new HashSet<ReferenceNumber>();
				refNumList2.add(rn21);
				refNumList2.add(rn22);

				asn = new ASN();
				asn.setCode("ASN21");
				asn = asnDaoService.add(asn);

				asn = asnDaoService.addRefNums(asn.getId(), refNumList2);

				Iterator<ReferenceNumber> rnsIt2 = asn.getRefNumbers().iterator();

				ASNLine al21 = new ASNLine();
				al21.setCode("AL21");
				al21.setProduct(prd2);
				// al21.setRefNumber(rnsIt2.next());
				al21.setLineNumber(0);
				al21.setExpectedInnerPackCount(8);
				al21.setExpectedOuterPackCount(12);
				al21.setExpectedTotalWeight(28.0);
				al21.setExpectedTotalVolume(12.54);
				al21.setExpectedTotalLen(9.3);
				al21.setExpectedTotalWidth(1.0);
				al21.setExpectedTotalHeight(1.39);
				al21.setDescription("A90234708-1111111 Desktop Package");

				ASNLine al22 = new ASNLine();
				al22.setCode("AL22");
				al22.setProduct(prd4);
				// al2.setRefNumber(rnsIt2.next());
				al22.setLineNumber(0);
				al22.setExpectedInnerPackCount(18);
				al22.setExpectedOuterPackCount(2);
				al22.setExpectedTotalWeight(28.0);
				al22.setExpectedTotalVolume(12.54);
				al22.setExpectedTotalLen(9.3);
				al22.setExpectedTotalWidth(1.0);
				al22.setExpectedTotalHeight(1.39);
				al22.setDescription("AODK-DLKDJ YHNNNNN");

				Set<ASNLine> asnLineList2 = new HashSet<ASNLine>();
				asnLineList2.add(al21);
				asnLineList2.add(al22);

				pickup1 = new ASNPickup();
				dropOff1 = new ASNDropOff();

				pickup1.setCode("PKUP21");
				pickup1.setPickUpFrom(tescus1);
				pickup1.setPickUpFromAddress(tescus1_ata);
				pickup1.setLocalTrans(tescar1);
				pickup1.setLocalTransAddress(tescar1_ata);
				pickup1.setDriverId("DRV0021");
				pickup1.setVehicleId("DRV0021");
				pickup1.setBolNumber("DRV0021");
				pickup1.setVehicleId("DRV0021");
				pickup1.setEstimatedPickup(new Date());

				pickup1 = asnPickupDAOService.add(pickup1);

				dropOff1.setDropOffAt(tescus1);
				dropOff1.setCode("DRPOF21");
				dropOff1.setDropOffAtAddress(tescus1_addr);
				dropOff1.setEstimatedDropOff(new Date());
				dropOff1 = asnDropOffDAOService.add(dropOff1);

				asn.setWarehouse(warehouse);

				asn = asnDaoService.addLines(asn, asnLineList2);
				asn = asnDaoService.addLocalTrans(asn, pickup1, dropOff1);
				asn = asnDaoService.update(asn);

				// asn = asnDaoService.update(asn);

				asns[1] = asn;
			}
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);

			throw e;
		}

		return asns;
	}

	public static Collection<DataSource> createDataSourceSprint2Data(IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService, EntityManager em) throws Exception {
		HashSet<DataSource> dataSources = new HashSet<DataSource>();
		dataSources.add(DataSourceData.provideDefaultReceiveDS(entityTypeDAOService, dataSourceDAOService, em));
		dataSources.add(DataSourceData.provideBasicFormReceiveDS(entityTypeDAOService, dataSourceDAOService, em));
		dataSources.add(DataSourceData.provideFileEntryDS(entityTypeDAOService, dataSourceDAOService, em));
		dataSources.add(DataSourceData.provideNoteItemDS(entityTypeDAOService, dataSourceDAOService, em));
		dataSources.add(DataSourceData.provideReferenceNumberDS(entityTypeDAOService, dataSourceDAOService, em));
		dataSources.add(DataSourceData.provideDefaultReceiveLineDS(entityTypeDAOService, dataSourceDAOService, em));
		dataSources.add(DataSourceData.provideWeightDimsFormReceiveDS(entityTypeDAOService, dataSourceDAOService, em));

		return dataSources;
	}

	public static void setLogger(Logger logger) {
		TestDataManager.logger = logger;
	}

	public void setConxlogisticsEMF(EntityManagerFactory conxlogisticsEMF) {
		this.conxlogisticsEMF = conxlogisticsEMF;
	}

	public void setGlobalTransactionManager(PlatformTransactionManager globalTransactionManager) {
		this.globalTransactionManager = globalTransactionManager;
	}

	public void setOrgDaoService(IOrganizationDAOService orgDaoService) {
		this.orgDaoService = orgDaoService;
	}

	public void setCountryDaoService(ICountryDAOService countryDaoService) {
		this.countryDaoService = countryDaoService;
	}

	public void setCountryStateDaoService(ICountryStateDAOService countryStateDaoService) {
		this.countryStateDaoService = countryStateDaoService;
	}

	public void setUnlocoDaoService(IUnlocoDAOService unlocoDaoService) {
		this.unlocoDaoService = unlocoDaoService;
	}

	public void setAddressDaoService(IAddressDAOService addressDaoService) {
		this.addressDaoService = addressDaoService;
	}

	public void setPackUnitDaoService(IPackUnitDAOService packUnitDaoService) {
		this.packUnitDaoService = packUnitDaoService;
	}

	public void setDimUnitDaoService(IDimUnitDAOService dimUnitDaoService) {
		this.dimUnitDaoService = dimUnitDaoService;
	}

	public void setWeightUnitDaoService(IWeightUnitDAOService weightUnitDaoService) {
		this.weightUnitDaoService = weightUnitDaoService;
	}

	public void setProductTypeDaoService(IProductTypeDAOService productTypeDaoService) {
		this.productTypeDaoService = productTypeDaoService;
	}

	public void setProductDaoService(IProductDAOService productDaoService) {
		this.productDaoService = productDaoService;
	}

	public void setCurrencyUnitDAOService(ICurrencyUnitDAOService currencyUnitDAOService) {
		this.currencyUnitDAOService = currencyUnitDAOService;
	}

	public void setAsnDaoService(IASNDAOService asnDaoService) {
		this.asnDaoService = asnDaoService;
	}

	public void setAsnPickupDAOService(IASNPickupDAOService asnPickupDAOService) {
		this.asnPickupDAOService = asnPickupDAOService;
	}

	public void setAsnDropOffDAOService(IASNDropOffDAOService asnDropOffDAOService) {
		this.asnDropOffDAOService = asnDropOffDAOService;
	}

	public void setContactDAOService(IContactDAOService contactDAOService) {
		this.contactDAOService = contactDAOService;
	}

	public void setDocTypeDOAService(IDocTypeDAOService docTypeDOAService) {
		this.docTypeDOAService = docTypeDOAService;
	}

	public void setDockTypeDOAService(IDockTypeDAOService dockTypeDOAService) {
		this.dockTypeDOAService = dockTypeDOAService;
	}

	public void setEntityMetadataDAOService(IEntityMetadataDAOService entityMetadataDAOService) {
		this.entityMetadataDAOService = entityMetadataDAOService;
	}

	public void setReferenceNumberTypeDaoService(IReferenceNumberTypeDAOService referenceNumberTypeDaoService) {
		this.referenceNumberTypeDaoService = referenceNumberTypeDaoService;
	}

	public void setReferenceNumberDaoService(IReferenceNumberDAOService referenceNumberDaoService) {
		this.referenceNumberDaoService = referenceNumberDaoService;
	}

	public void setWhseDAOService(IWarehouseDAOService whseDAOService) {
		this.whseDAOService = whseDAOService;
	}

	public void setRcvDaoService(IReceiveDAOService rcvDaoService) {
		this.rcvDaoService = rcvDaoService;
	}

	public void setComponentDAOService(IComponentDAOService componentDAOService) {
		this.componentDAOService = componentDAOService;
	}

	public void setEntityTypeDAOService(IEntityTypeDAOService entityTypeDAOService) {
		this.entityTypeDAOService = entityTypeDAOService;
	}

	public void setDataSourceDAOService(IDataSourceDAOService dataSourceDAOService) {
		this.dataSourceDAOService = dataSourceDAOService;
	}

	public void setDocumentRepositoryService(IRemoteDocumentRepository documentRepositoryService) {
		this.documentRepositoryService = documentRepositoryService;
	}

	public void setFolderDAOService(IFolderDAOService folderDAOService) {
		this.folderDAOService = folderDAOService;
	}

	public void setNoteDAOService(INoteDAOService noteDAOService) {
		this.noteDAOService = noteDAOService;
	}

	public IAddressTypeDAOService getAddressTypeDaoService() {
		return addressTypeDaoService;
	}

	public void setAddressTypeDaoService(IAddressTypeDAOService addressTypeDaoService) {
		this.addressTypeDaoService = addressTypeDaoService;
	}

	public IAddressTypeAddressDAOService getAddressTypeAddressDaoService() {
		return addressTypeAddressDaoService;
	}

	public void setAddressTypeAddressDaoService(IAddressTypeAddressDAOService addressTypeAddressDaoService) {
		this.addressTypeAddressDaoService = addressTypeAddressDaoService;
	}

	public IContactTypeDAOService getContactTypeDAOService() {
		return contactTypeDAOService;
	}

	public void setContactTypeDAOService(IContactTypeDAOService contactTypeDAOService) {
		this.contactTypeDAOService = contactTypeDAOService;
	}

	public IContactTypeContactDAOService getContactTypeContactDAOService() {
		return contactTypeContactDAOService;
	}

	public void setContactTypeContactDAOService(IContactTypeContactDAOService contactTypeContactDAOService) {
		this.contactTypeContactDAOService = contactTypeContactDAOService;
	}

	public UserTransaction getUserTransaction() {
		return userTransaction;
	}

	public void setUserTransaction(UserTransaction userTransaction) {
		this.userTransaction = userTransaction;
	}
}
