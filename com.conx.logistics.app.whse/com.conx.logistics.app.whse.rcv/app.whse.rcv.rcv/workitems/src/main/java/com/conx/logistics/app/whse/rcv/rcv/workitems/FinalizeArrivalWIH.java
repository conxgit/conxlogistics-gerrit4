package com.conx.logistics.app.whse.rcv.rcv.workitems;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.drools.process.instance.WorkItemHandler;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.conx.logistics.app.whse.rcv.rcv.dao.services.IArrivalDAOService;
import com.conx.logistics.app.whse.rcv.rcv.domain.Arrival;

@Transactional
@Repository
public class FinalizeArrivalWIH implements WorkItemHandler {
	private static final Logger logger = LoggerFactory.getLogger(FinalizeArrivalWIH.class);

	@Autowired
	private IArrivalDAOService arrivalDAOService;
	
	@Autowired
	private PlatformTransactionManager kernelSystemTransManager;

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		Arrival arrival = null;
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setName("whse.app.page.sk");
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.kernelSystemTransManager.getTransaction(def);
		try {
			Arrival arvl = (Arrival)workItem.getParameter("arrivalIn");
			arvl = arrivalDAOService.update(arvl);
			this.kernelSystemTransManager.commit(status);
			
			Map<String, Object> results = new HashMap<String, Object>();
			results.put("arrivalOut",arrival);
			manager.completeWorkItem(workItem.getId(), results);
		} catch (Exception e) {
			this.kernelSystemTransManager.rollback(status);
			
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);
			
			throw new IllegalStateException("FinalizeArrivalWIH:\r\n"+stacktrace, e);
		} catch (Error e) {
			this.kernelSystemTransManager.rollback(status);
			
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);
			
			throw new IllegalStateException("FinalizeArrivalWIH:\r\n"+stacktrace, e);			
		}			
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		// TODO Auto-generated method stub

	}

}
