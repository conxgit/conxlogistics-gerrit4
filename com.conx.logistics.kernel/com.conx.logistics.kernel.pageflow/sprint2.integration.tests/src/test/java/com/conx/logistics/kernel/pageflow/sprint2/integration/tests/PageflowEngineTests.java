package com.conx.logistics.kernel.pageflow.sprint2.integration.tests;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.TransactionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.conx.logistics.app.whse.rcv.rcv.domain.Arrival;
import com.conx.logistics.app.whse.rcv.rcv.domain.Receive;
import com.conx.logistics.kernel.pageflow.services.IPageFlowManager;
import com.conx.logistics.kernel.pageflow.services.ITaskWizard;
import com.conx.logistics.mdm.domain.application.Feature;

@ContextConfiguration(locations = { "/META-INF/tm.jta-module-context.xml", "/META-INF/spring/persistence.datasource.mysql.local-module-context.xml",
		"/META-INF/spring/persistence.dynaconfiguration.local-module-context.xml", "/META-INF/datasource.dao.jpa.persistence-module-context.xml", "/META-INF/mdm.dao.services.impl-module-context.xml",
		"/META-INF/metamodel.dao.jpa.persistence-module-context.xml", "/META-INF/datasource.dao.jpa.persistence-module-context.xml", "/META-INF/components.dao.jpa.persistence-module-context.xml",
		"/META-INF/documentlibrary.remote.services.impl-module-context.xml", "/META-INF/app.whse.dao.jpa.persistence-module-context.xml",
		"/META-INF/app.whse.rcv.asn.dao.jpa.persistence-module-context.xml", "/META-INF/app.whse.rcv.rcv.dao.jpa.persistence-module-context.xml",
		"/META-INF/spring/data.uat.sprint2.data-module-context.xml", "/META-INF/kernel.pageflow.engine-module-context.xml" })
public class PageflowEngineTests extends AbstractTestNGSpringContextTests {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IPageFlowManager defaultPageFlowEngine;

	@Autowired
	private TransactionManager globalTransactionManager;
	
	@BeforeClass
	public void setUp() throws Exception {
		Assert.assertNotNull(defaultPageFlowEngine);
		Assert.assertNotNull(globalTransactionManager);
	}
	
	@AfterClass
	public void tearDown() throws Exception {
	}

	@Test(enabled = true)
	public void testProcessCarrierArrivalStatic() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();

			Feature onCompletionFeature = new Feature();
			onCompletionFeature.setId(100000L);
			params.put("onCompletionFeature", onCompletionFeature);
			params.put("processId", "whse.rcv.arrivalproc.ProcessCarrierArrivalV1.0");
			params.put("userId", "test");

			/**
			 * 
			 * ASN/Arrival Processing
			 * 
			 */
			// -- 1. Start process
			ITaskWizard wiz = defaultPageFlowEngine.createTaskWizard(params);
			Map<String, Object> res = wiz.getProperties();

			// -- 2. Complete FindReceive Task
			Receive rcv = new Receive();
			HashMap<String, Object> outParams = new HashMap<String, Object>();
			outParams.put("receiveOut", rcv);
			wiz = defaultPageFlowEngine.executeTaskWizard(wiz, outParams);

			// -- 3. Get output vars from AttachNewArrivalWIH and complete
			// ConfirmTruckInfo Task
			res = wiz.getProperties();
			Arrival arvl = (Arrival) res.get("Content");// output of
														// AttachNewArrivalWIH/input
														// of ConfirmTruckInfo

			outParams = new HashMap<String, Object>();
			outParams.put("arrivalOut", arvl);
			wiz = defaultPageFlowEngine.executeTaskWizard(wiz, outParams);

			// -- 4. Get output vars (w/ new PickUp) from SaveTruckInfoWIH and
			// complete ConfirmPickUp Task
			res = wiz.getProperties();
			arvl = (Arrival) res.get("Content");// output of
												// SaveTruckInfoWIH/input of
												// ConfirmPickUp

			outParams = new HashMap<String, Object>();
			outParams.put("arrivalOut", arvl);
			wiz = defaultPageFlowEngine.executeTaskWizard(wiz, outParams);

			// -- 5. Get output vars (w/ new DropOff) from SavePickUpWIH and
			// complete ConfirmDropOff Task
			res = wiz.getProperties();
			arvl = (Arrival) res.get("Content");// output of SavePickUpWIH/input
												// of ConfirmDropOff

			outParams = new HashMap<String, Object>();
			outParams.put("arrivalOut", arvl);
			wiz = defaultPageFlowEngine.executeTaskWizard(wiz, outParams);

			// -- 6. Get output vars from SaveDropOffWIH and complete
			// ProcessArrivalReceipts Task
			res = wiz.getProperties();
			arvl = (Arrival) res.get("Content");// output of
												// SaveDropOffWIH/input of
												// ProcessArrivalReceipts

			outParams = new HashMap<String, Object>();
			outParams.put("arrivalOut", arvl);
			wiz = defaultPageFlowEngine.executeTaskWizard(wiz, outParams);

			// -- 7. Complete FinalizeArrival
			res = wiz.getProperties();
			arvl = (Arrival) res.get("Content");// output of
												// ProcessArrivalReceipts

			outParams = new HashMap<String, Object>();
			outParams.put("arrivalOut", arvl);
			wiz = defaultPageFlowEngine.executeTaskWizard(wiz, outParams);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);
			e.printStackTrace();
		}
	}

	public void start() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();

			Feature onCompletionFeature = new Feature();
			onCompletionFeature.setId(100000L);
			params.put("onCompletionFeature", onCompletionFeature);
			params.put("processId", "whse.rcv.arrivalproc.ProcessCarrierArrivalV1.0");
			params.put("userId", "test");

			/**
			 * 
			 * ASN/Arrival Processing
			 * 
			 */
			// -- 1. Start process
			ITaskWizard wiz = defaultPageFlowEngine.createTaskWizard(params);
			Map<String, Object> res = wiz.getProperties();

			// -- 2. Complete FindReceive Task
			Receive rcv = new Receive();
			HashMap<String, Object> outParams = new HashMap<String, Object>();
			outParams.put("receiveOut", rcv);
			wiz = defaultPageFlowEngine.executeTaskWizard(wiz, outParams);

			// -- 3. Get output vars from AttachNewArrivalWIH and complete
			// ConfirmTruckInfo Task
			res = wiz.getProperties();
			Arrival arvl = (Arrival) res.get("Content");// output of
														// AttachNewArrivalWIH/input
														// of ConfirmTruckInfo

			outParams = new HashMap<String, Object>();
			outParams.put("arrivalOut", arvl);
			wiz = defaultPageFlowEngine.executeTaskWizard(wiz, outParams);

			// -- 4. Get output vars (w/ new PickUp) from SaveTruckInfoWIH and
			// complete ConfirmPickUp Task
			res = wiz.getProperties();
			arvl = (Arrival) res.get("Content");// output of
												// SaveTruckInfoWIH/input of
												// ConfirmPickUp

			outParams = new HashMap<String, Object>();
			outParams.put("arrivalOut", arvl);
			wiz = defaultPageFlowEngine.executeTaskWizard(wiz, outParams);

			// -- 5. Get output vars (w/ new DropOff) from SavePickUpWIH and
			// complete ConfirmDropOff Task
			res = wiz.getProperties();
			arvl = (Arrival) res.get("Content");// output of SavePickUpWIH/input
												// of ConfirmDropOff

			outParams = new HashMap<String, Object>();
			outParams.put("arrivalOut", arvl);
			wiz = defaultPageFlowEngine.executeTaskWizard(wiz, outParams);

			// -- 6. Get output vars from SaveDropOffWIH and complete
			// ProcessArrivalReceipts Task
			res = wiz.getProperties();
			arvl = (Arrival) res.get("Content");// output of
												// SaveDropOffWIH/input of
												// ProcessArrivalReceipts

			outParams = new HashMap<String, Object>();
			outParams.put("arrivalOut", arvl);
			wiz = defaultPageFlowEngine.executeTaskWizard(wiz, outParams);

			// -- 7. Complete FinalizeArrival
			res = wiz.getProperties();
			arvl = (Arrival) res.get("Content");// output of
												// ProcessArrivalReceipts

			outParams = new HashMap<String, Object>();
			outParams.put("arrivalOut", arvl);
			wiz = defaultPageFlowEngine.executeTaskWizard(wiz, outParams);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);
			e.printStackTrace();
		}

	}

	public void stop() {
	}
}
