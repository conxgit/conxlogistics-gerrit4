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

@Transactional
public class PageflowEngineDynamicReceiveTests {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private IPageFlowManager defaultPageFlowEngine;
	
	@Autowired
	private TransactionManager globalTransactionManager;
	
	public void start() {
		try {
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("processId", "whse.rcv.arrivalproc.ProcessCarrierArrivalV1.0");
			params.put("userId", "conxuser");
			
			/**
			 * 
			 * ASN/Arrival Processing
			 * 
			 */
			//-- 1. Start process
			ITaskWizard wiz = defaultPageFlowEngine.createTaskWizard(params);			
			Map<String, Object> res = wiz.getProperties();
			
			//-- 2. Complete FindReceive Task
			Receive rcv = null;
			HashMap<String,Object> outParams = new HashMap<String, Object>();
			//outParams.put("receiveOut",rcv);
			wiz = defaultPageFlowEngine.executeTaskWizard(wiz, outParams);
			
			
			//-- 3.  Get output vars from CreateNewDynaArrivalWIH and complete AddDynaTruckInfo Task
			res = wiz.getProperties();
			Arrival arvl = (Arrival)res.get("Content");// output of CreateNewDynaArrivalWIH/input of AddDynaTruckInfo
			
			outParams = new HashMap<String, Object>();
			outParams.put("arrivalOut",arvl);
			wiz = defaultPageFlowEngine.executeTaskWizard(wiz, outParams);
			
			//-- 4.  Get output vars (w/ new PickUp) from SaveDynaTruckInfoWIH and complete AddDynaPickUp Task
			res = wiz.getProperties();
			arvl = (Arrival)res.get("Content");// output of SaveDynaTruckInfoWIH/input of AddDynaPickUp
			
			outParams = new HashMap<String, Object>();
			outParams.put("arrivalOut",arvl);
			wiz = defaultPageFlowEngine.executeTaskWizard(wiz, outParams);	
			
			//-- 5.  Get output vars (w/ new DropOff) from SaveDynaPickUpWIH and complete AddDynaDropOff Task
			res = wiz.getProperties();
			arvl = (Arrival)res.get("Content");// output of SaveDynaPickUpWIH/input of AddDynaDropOff
			
			outParams = new HashMap<String, Object>();
			outParams.put("arrivalOut",arvl);
			wiz = defaultPageFlowEngine.executeTaskWizard(wiz, outParams);				
			
			//-- 6.  Get output vars from SaveDynaDropOffWIH and complete ProcessDynaArrivalReceipts Task
			res = wiz.getProperties();
			arvl = (Arrival)res.get("Content");// output of SaveDynaDropOffWIH/input of ProcessDynaArrivalReceipts			
			
			outParams = new HashMap<String, Object>();
			outParams.put("arrivalOut",arvl);
			wiz = defaultPageFlowEngine.executeTaskWizard(wiz, outParams);	
			
			
			//-- 7.  Get output vars from ProcessDynaArrivalReceipts and complete FinalizeArrival Task
			res = wiz.getProperties();
			arvl = (Arrival)res.get("Content");// output of ProcessDynaArrivalReceipts/input of FinalizeArrival			
			
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
