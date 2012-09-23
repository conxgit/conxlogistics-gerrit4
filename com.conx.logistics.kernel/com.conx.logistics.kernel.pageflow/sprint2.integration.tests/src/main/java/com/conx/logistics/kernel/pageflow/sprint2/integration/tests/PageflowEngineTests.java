package com.conx.logistics.kernel.pageflow.sprint2.integration.tests;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.TransactionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.conx.logistics.app.whse.rcv.rcv.domain.Arrival;
import com.conx.logistics.app.whse.rcv.rcv.domain.Receive;
import com.conx.logistics.kernel.pageflow.services.IPageFlowManager;
import com.conx.logistics.kernel.pageflow.services.ITaskWizard;
import com.conx.logistics.mdm.domain.application.Feature;

@Transactional
public class PageflowEngineTests {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private IPageFlowManager defaultPageFlowEngine;
	
	@Autowired
	private TransactionManager globalTransactionManager;
	
	public void start() {
		try {
			Map<String,Object> params = new HashMap<String, Object>();
			
			Feature   onCompletionFeature = new Feature();
			onCompletionFeature.setId(100000L);
			params.put("onCompletionFeature",onCompletionFeature);
			params.put("processId", "whse.rcv.arrivalproc.ProcessCarrierArrivalV1.0");
			params.put("userId", "test");
			
			/**
			 * 
			 * ASN/Arrival Processing
			 * 
			 */
			//-- 1. Start process
			ITaskWizard wiz = defaultPageFlowEngine.createTaskWizard(params);			
			Map<String, Object> res = wiz.getProperties();
			
			//-- 2. Complete FindReceive Task
			Receive rcv = new Receive();
			HashMap<String,Object> outParams = new HashMap<String, Object>();
			outParams.put("receiveOut",rcv);
			wiz = defaultPageFlowEngine.executeTaskWizard(wiz, outParams);
			
			
			//-- 3.  Get output vars from AttachNewArrivalWIH and complete ConfirmTruckInfo Task
			res = wiz.getProperties();
			Arrival arvl = (Arrival)res.get("Content");// output of AttachNewArrivalWIH/input of ConfirmTruckInfo
			
			outParams = new HashMap<String, Object>();
			outParams.put("arrivalOut",arvl);
			wiz = defaultPageFlowEngine.executeTaskWizard(wiz, outParams);
			
			//-- 4.  Get output vars (w/ new PickUp) from SaveTruckInfoWIH and complete ConfirmPickUp Task
			res = wiz.getProperties();
			arvl = (Arrival)res.get("Content");// output of SaveTruckInfoWIH/input of ConfirmPickUp
			
			outParams = new HashMap<String, Object>();
			outParams.put("arrivalOut",arvl);
			wiz = defaultPageFlowEngine.executeTaskWizard(wiz, outParams);	
			
			//-- 5.  Get output vars (w/ new DropOff) from SavePickUpWIH and complete ConfirmDropOff Task
			res = wiz.getProperties();
			arvl = (Arrival)res.get("Content");// output of SavePickUpWIH/input of ConfirmDropOff
			
			outParams = new HashMap<String, Object>();
			outParams.put("arrivalOut",arvl);
			wiz = defaultPageFlowEngine.executeTaskWizard(wiz, outParams);				
			
			//-- 6.  Get output vars from SaveDropOffWIH and complete ProcessArrivalReceipts Task
			res = wiz.getProperties();
			arvl = (Arrival)res.get("Content");// output of SaveDropOffWIH/input of ProcessArrivalReceipts			
			
			outParams = new HashMap<String, Object>();
			outParams.put("arrivalOut",arvl);
			wiz = defaultPageFlowEngine.executeTaskWizard(wiz, outParams);	
			
			
			//-- 7.  Complete FinalizeArrival
			res = wiz.getProperties();
			arvl = (Arrival)res.get("Content");// output of ProcessArrivalReceipts
			
			outParams = new HashMap<String, Object>();
			outParams.put("arrivalOut",arvl);
			wiz = defaultPageFlowEngine.executeTaskWizard(wiz, outParams);				
			
			/***
			 * Change VARS
			 */
/*			rn.setCode("ABCD");
			HashMap<String, Object> procVarMap = new HashMap<String, Object>();
			procVarMap.put("asnRefNumMap", asnRefNumMap);
			Map<String, Object> procInstVars = defaultPageFlowEngine.updateProcessInstanceVariables(wiz, procVarMap);
			procVarMap = (HashMap<String, Object>)procInstVars.get("asnRefNumMap");
			HashSet<ReferenceNumber> refSet = (HashSet<ReferenceNumber>)procVarMap.get("asnRefNumCollection");
			rn = refSet.iterator().next();
			
			//-- 4. Complete ASN Lines Human and get vars
			HashMap<String, Object> asnASNLineProductMap = new HashMap<String, Object>();
			outParams = new HashMap<String, Object>();
			outParams.putAll(res);			
			HashSet<ASNLine> asnLines = new HashSet<ASNLine>();
			ASNLine line = new ASNLine();
			line.setLineNumber(1);
			asnLines.add(line);
			asnASNLineProductMap.put("asnLinesCollection", asnLines);	
			asnASNLineProductMap.put("productsCollection", new HashSet<Product>());
			
			outParams.put("asnASNLineProductMapOut", asnASNLineProductMap);
			wiz = defaultPageFlowEngine.executeTaskWizard(wiz, outParams);
			res = wiz.getProperties();
			res = (HashMap<String,Object>)res.get("Content");
			
			//-- 4. Complete Local Trans Human and get vars
			HashMap<String, Object> asnLocalTransMap = new HashMap<String, Object>();
			
			outParams = new HashMap<String, Object>();				
			ASNPickup asnp = new ASNPickup();
			ASNDropOff asnd = new ASNDropOff();
			asnLocalTransMap.put("asnPickup", asnp);	
			asnLocalTransMap.put("asnDropoff", asnd);
			
			outParams.put("asnLocalTransMapOut", asnLocalTransMap);
			
			wiz = defaultPageFlowEngine.executeTaskWizard(wiz, outParams);
			res = wiz.getProperties();
			res = (HashMap<String,Object>)res.get("Content");
			
			//-- 5. Complete Accept ASN and get vars
			outParams = new HashMap<String, Object>();
			outParams.putAll(res);
			wiz = defaultPageFlowEngine.executeTaskWizard(wiz, outParams);
			res = wiz.getProperties();
			res = (HashMap<String,Object>)res.get("Content");*/
			
			// ProcessInstanceRef pi =
			//bpmService.newInstance("whse.rcv.asn.CreateNewASNByOrgV1.0");
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
