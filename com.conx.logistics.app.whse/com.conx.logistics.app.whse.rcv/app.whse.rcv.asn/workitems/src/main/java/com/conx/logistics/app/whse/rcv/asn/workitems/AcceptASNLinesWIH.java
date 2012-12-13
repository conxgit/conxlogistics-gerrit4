package com.conx.logistics.app.whse.rcv.asn.workitems;

import java.util.HashMap;
import java.util.Map;

import org.drools.process.instance.WorkItemHandler;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.conx.logistics.app.whse.rcv.asn.dao.services.IASNDAOService;
import com.conx.logistics.app.whse.rcv.asn.domain.ASN;

public class AcceptASNLinesWIH implements WorkItemHandler {
	@Autowired
	private IASNDAOService asnDao;
	@Autowired
	private PlatformTransactionManager kernelSystemTransManager;

	public void setAsnDao(IASNDAOService asnDao) {
		this.asnDao = asnDao;
	}
	
	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		Map<String, Object> parameters = workItem.getParameters();
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setName("whse.app.page.sk");
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.kernelSystemTransManager.getTransaction(def);
		
		ASN asn = null;
		try {
			asn = this.asnDao.get(((ASN) parameters.get("asnIn")).getId());
			this.kernelSystemTransManager.commit(status);
		} catch (Exception e) {
			this.kernelSystemTransManager.rollback(status);
			e.printStackTrace();
			manager.abortWorkItem(workItem.getId());
		}
		
		//Map<String, Object> output = new HashMap<String, Object>();
		//output.put("asnParamsOut",asnParamsIn);
		
		if (parameters.get("asnIn") != null) {
			Map<String, Object> results = new HashMap<String, Object>();
			results.put("asnOut", asn);
			manager.completeWorkItem(workItem.getId(), results);
		} else {
			manager.completeWorkItem(workItem.getId(), null);
		}
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		// TODO Auto-generated method stub
		
	}

}
