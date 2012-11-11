package com.conx.logistics.app.whse.im.dao.tests;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.conx.logistics.app.whse.dao.services.IWarehouseDAOService;
import com.conx.logistics.app.whse.domain.warehouse.Warehouse;
import com.conx.logistics.app.whse.im.dao.services.IStockItemDAOService;
import com.conx.logistics.app.whse.im.domain.stockitem.StockItem;
import com.conx.logistics.app.whse.rcv.asn.dao.services.IASNDAOService;
import com.conx.logistics.app.whse.rcv.asn.domain.ASN;
import com.conx.logistics.app.whse.rcv.asn.shared.type.TRANSMODE;
import com.conx.logistics.app.whse.rcv.rcv.dao.services.IArrivalDAOService;
import com.conx.logistics.app.whse.rcv.rcv.dao.services.IArrivalReceiptDAOService;
import com.conx.logistics.app.whse.rcv.rcv.dao.services.IReceiveDAOService;
import com.conx.logistics.app.whse.rcv.rcv.dao.services.IReceiveLineDAOService;
import com.conx.logistics.app.whse.rcv.rcv.domain.Arrival;
import com.conx.logistics.app.whse.rcv.rcv.domain.ArrivalReceipt;
import com.conx.logistics.app.whse.rcv.rcv.domain.Receive;
import com.conx.logistics.app.whse.rcv.rcv.domain.ReceiveLine;
import com.conx.logistics.app.whse.rcv.rcv.domain.ReceiveLineStockItemSet;
import com.conx.logistics.mdm.dao.services.product.IDimUnitDAOService;
import com.conx.logistics.mdm.dao.services.product.IPackUnitDAOService;
import com.conx.logistics.mdm.dao.services.product.IProductDAOService;
import com.conx.logistics.mdm.dao.services.product.IProductTypeDAOService;
import com.conx.logistics.mdm.dao.services.product.IWeightUnitDAOService;
import com.conx.logistics.mdm.domain.constants.DimUnitCustomCONSTANTS;
import com.conx.logistics.mdm.domain.constants.PackUnitCustomCONSTANTS;
import com.conx.logistics.mdm.domain.constants.ProductTypeCustomCONSTANTS;
import com.conx.logistics.mdm.domain.constants.WeightUnitCustomCONSTANTS;
import com.conx.logistics.mdm.domain.product.Product;

@ContextConfiguration(locations = { "/META-INF/tm.jta-module-context.xml", "/META-INF/spring/persistence.datasource.mysql.local-module-context.xml",
		"/META-INF/spring/persistence.dynaconfiguration.local-module-context.xml", "/META-INF/mdm.dao.services.impl-module-context.xml", "/META-INF/metamodel.dao.jpa.persistence-module-context.xml",
		"/META-INF/app.whse.dao.jpa.persistence-module-context.xml", "/META-INF/documentlibrary.remote.services.impl-module-context.xml",
		"/META-INF/app.whse.rcv.asn.dao.jpa.persistence-module-context.xml", "/META-INF/app.whse.rcv.rcv.dao.jpa.persistence-module-context.xml",
		"/META-INF/spring/data.uat.sprint2.data-module-context.xml", "/META-INF/spring/whse.im.dao.jpa.persistence-module-context.xml" })
public class StockItemDAOTests extends AbstractTestNGSpringContextTests {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private IReceiveDAOService receiveDAOService;
	@Autowired
	private IReceiveLineDAOService receiveLineDAOService;
	@Autowired
	private IPackUnitDAOService packUnitDAOService;
	@Autowired
	private IWeightUnitDAOService weightUnitDAOService;
	@Autowired
	private IDimUnitDAOService dimUnitDAOService;
	@Autowired
	private IASNDAOService asnDAOService;
	@Autowired
	private IProductDAOService productDAOService;
	@Autowired
	private IProductTypeDAOService productTypeDAOService;
	@Autowired
	private IStockItemDAOService stockItemDAOService;
	@Autowired
	private IArrivalDAOService arrivalDAOService;
	@Autowired
	private IArrivalReceiptDAOService arrivalReceiptDAOService;
	@Autowired
	private IWarehouseDAOService warehouseDAOService;
	@Autowired
	private JtaTransactionManager jtaTransactionManager;


	private ASN asn;
	private Product product;
	private Receive receive;
	private ReceiveLine receiveLine;
	private ReceiveLineStockItemSet receiveLineStockItemSet;
	private Arrival arrival;
	private ArrivalReceipt arrivalReceipt;

	private void generateTestData() throws ClassNotFoundException, Exception {
		Warehouse warehouse = this.warehouseDAOService.provide("DFLT", "Default");
		
		this.asn = new ASN();
		this.asn.setOuterPackUnit(this.packUnitDAOService.provide(PackUnitCustomCONSTANTS.TYPE_BOX, PackUnitCustomCONSTANTS.TYPE_BOX_DESCRIPTION));
		this.asn.setWeightUnit(this.weightUnitDAOService.provide(WeightUnitCustomCONSTANTS.TYPE_KG, WeightUnitCustomCONSTANTS.TYPE_KG_DESCRIPTION));
		this.asn.setDimUnit(this.dimUnitDAOService.provide(DimUnitCustomCONSTANTS.TYPE_M, DimUnitCustomCONSTANTS.TYPE_M_DESCRIPTION));
		this.asn.setVolUnit(this.dimUnitDAOService.provide(DimUnitCustomCONSTANTS.TYPE_CF, DimUnitCustomCONSTANTS.TYPE_CF_DESCRIPTION));
		this.asn.setExpectedTotalOuterPackCount(1);
		this.asn.setExpectedTotalweight(10.0);
		this.asn.setExpectedTotalLen(1.0);
		this.asn.setExpectedTotalWidth(1.0);
		this.asn.setWarehouse(warehouse);
		this.asn.setExpectedTotalHeight(1.0);
		this.asn.setExpectedTotalVolume(1.0);
		this.asn.setMode(TRANSMODE.AIR);
		this.asn = this.asnDAOService.add(asn);

		this.product = new Product();
		this.product.setName("Bible");
		this.product.setOuterPackUnit(this.packUnitDAOService.provide(PackUnitCustomCONSTANTS.TYPE_PKG, PackUnitCustomCONSTANTS.TYPE_PKG_DESCRIPTION));
		this.product.setOuterPackCount(1);
		this.product.setProductType(this.productTypeDAOService.provide(ProductTypeCustomCONSTANTS.TYPE_ConXGeneric, ProductTypeCustomCONSTANTS.TYPE_ConXGeneric));
		this.product = this.productDAOService.add(product);

		this.receive = this.receiveDAOService.process(this.asn);

		this.receiveLine = new ReceiveLine();
		this.receiveLine.setProduct(product);
		this.receiveLine.setExpectedTotalLen(1.0);
		this.receiveLine.setExpectedTotalWidth(1.0);
		this.receiveLine.setExpectedTotalHeight(1.0);
		this.receiveLine.setExpectedTotalVolume(1.0);
		this.receiveLine.setLineNumber(1);
		this.receiveLine = this.receiveLineDAOService.add(this.receiveLine, this.receive.getId());

		this.arrival = new Arrival();
		this.arrival = this.arrivalDAOService.add(this.arrival, this.receive);
		
		this.receiveLineStockItemSet = new ReceiveLineStockItemSet();
		this.receiveLineStockItemSet.setArrival(this.arrival);
		this.receiveLineStockItemSet = this.receiveLineDAOService.add(this.receiveLineStockItemSet, this.receiveLine.getId());

		this.arrivalReceipt = new ArrivalReceipt();
		this.arrivalReceipt = this.arrivalDAOService.addArrivalReceipt(this.arrival.getId(), this.arrivalReceipt);
	}

	@BeforeClass
	public void setUp() throws Exception {
		Assert.assertNotNull(em);
		Assert.assertNotNull(receiveDAOService);
		Assert.assertNotNull(receiveLineDAOService);
		Assert.assertNotNull(packUnitDAOService);
		Assert.assertNotNull(weightUnitDAOService);
		Assert.assertNotNull(dimUnitDAOService);
		Assert.assertNotNull(asnDAOService);
		Assert.assertNotNull(productDAOService);
		Assert.assertNotNull(productTypeDAOService);
		Assert.assertNotNull(stockItemDAOService);
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setName("whse.im.dao.tests");
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.jtaTransactionManager.getTransaction(def);
		try {
			generateTestData();
			this.jtaTransactionManager.commit(status);
			Assert.assertTrue(true);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);
			this.jtaTransactionManager.rollback(status);
			throw e;
		}

		
	}

	@AfterClass
	public void tearDown() throws Exception {
	}

	@Test(enabled = true)
	public void testAddOneOfGroup() throws Exception {
		StockItem stockItem = new StockItem();
		stockItem = this.stockItemDAOService.addOneOfGroup(stockItem, this.receiveLine.getId(), this.arrivalReceipt.getId());
		Assert.assertTrue(stockItem != null);
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	public IReceiveDAOService getReceiveDAOService() {
		return receiveDAOService;
	}

	public void setReceiveDAOService(IReceiveDAOService receiveDAOService) {
		this.receiveDAOService = receiveDAOService;
	}

	public IReceiveLineDAOService getReceiveLineDAOService() {
		return receiveLineDAOService;
	}

	public void setReceiveLineDAOService(IReceiveLineDAOService receiveLineDAOService) {
		this.receiveLineDAOService = receiveLineDAOService;
	}

	public IPackUnitDAOService getPackUnitDAOService() {
		return packUnitDAOService;
	}

	public void setPackUnitDAOService(IPackUnitDAOService packUnitDAOService) {
		this.packUnitDAOService = packUnitDAOService;
	}

	public IWeightUnitDAOService getWeightUnitDAOService() {
		return weightUnitDAOService;
	}

	public void setWeightUnitDAOService(IWeightUnitDAOService weightUnitDAOService) {
		this.weightUnitDAOService = weightUnitDAOService;
	}

	public IDimUnitDAOService getDimUnitDAOService() {
		return dimUnitDAOService;
	}

	public void setDimUnitDAOService(IDimUnitDAOService dimUnitDAOService) {
		this.dimUnitDAOService = dimUnitDAOService;
	}

	public IASNDAOService getAsnDAOService() {
		return asnDAOService;
	}

	public void setAsnDAOService(IASNDAOService asnDAOService) {
		this.asnDAOService = asnDAOService;
	}

	public IProductDAOService getProductDAOService() {
		return productDAOService;
	}

	public void setProductDAOService(IProductDAOService productDAOService) {
		this.productDAOService = productDAOService;
	}

	public IProductTypeDAOService getProductTypeDAOService() {
		return productTypeDAOService;
	}

	public void setProductTypeDAOService(IProductTypeDAOService productTypeDAOService) {
		this.productTypeDAOService = productTypeDAOService;
	}

	public ReceiveLineStockItemSet getReceiveLineStockItemSet() {
		return receiveLineStockItemSet;
	}

	public void setReceiveLineStockItemSet(ReceiveLineStockItemSet receiveLineStockItemSet) {
		this.receiveLineStockItemSet = receiveLineStockItemSet;
	}

	public IStockItemDAOService getStockItemDAOService() {
		return stockItemDAOService;
	}

	public void setStockItemDAOService(IStockItemDAOService stockItemDAOService) {
		this.stockItemDAOService = stockItemDAOService;
	}

	public IArrivalDAOService getArrivalDAOService() {
		return arrivalDAOService;
	}

	public void setArrivalDAOService(IArrivalDAOService arrivalDAOService) {
		this.arrivalDAOService = arrivalDAOService;
	}

	public IArrivalReceiptDAOService getArrivalReceiptDAOService() {
		return arrivalReceiptDAOService;
	}

	public void setArrivalReceiptDAOService(IArrivalReceiptDAOService arrivalReceiptDAOService) {
		this.arrivalReceiptDAOService = arrivalReceiptDAOService;
	}

	public IWarehouseDAOService getWarehouseDAOService() {
		return warehouseDAOService;
	}

	public void setWarehouseDAOService(IWarehouseDAOService warehouseDAOService) {
		this.warehouseDAOService = warehouseDAOService;
	}
}
