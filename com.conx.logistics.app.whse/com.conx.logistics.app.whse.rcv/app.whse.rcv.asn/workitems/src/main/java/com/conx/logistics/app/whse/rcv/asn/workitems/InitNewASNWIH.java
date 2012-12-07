package com.conx.logistics.app.whse.rcv.asn.workitems;

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

import com.conx.logistics.app.whse.rcv.asn.dao.services.IASNDAOService;
import com.conx.logistics.app.whse.rcv.asn.domain.ASN;

@Transactional
@Repository
public class InitNewASNWIH implements WorkItemHandler {
	private static final Logger logger = LoggerFactory.getLogger(InitNewASNWIH.class);

	@Autowired
	private IASNDAOService asnDao;
//	@Autowired
//	private IOrganizationDAOService orgDao;
	@Autowired
	private PlatformTransactionManager globalJtaTransactionManager;

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		ASN newasn = null;

		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_MANDATORY);
		TransactionStatus status = globalJtaTransactionManager.getTransaction(def);

		try {
			newasn = new ASN();
			newasn = asnDao.add(newasn);
			globalJtaTransactionManager.commit(status);
			// newasn.setCode("TESCUS1" + (new Date().toString()));
			// newasn = asnDao.add(newasn);
			// newasn.setTenant(org);
			// newasn = asnDao.update(newasn);
			// Map<String, Object> asnParamsOut = new HashMap<String, Object>();
			// asnParamsOut.put("asn", newasn);
			Map<String, Object> results = new HashMap<String, Object>();
			results.put("asnOut", newasn);
			// results.put("asnVarMapOut",asnVarMapOut);
			manager.completeWorkItem(workItem.getId(), results);
			// WIUtils.waitTillCompleted(workItem,1000L);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);

			globalJtaTransactionManager.rollback(status);
		} catch (Error e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);

			globalJtaTransactionManager.rollback(status);
		}
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		// TODO Auto-generated method stub

	}

	public PlatformTransactionManager getGlobalJtaTransactionManager() {
		return globalJtaTransactionManager;
	}

	public void setGlobalJtaTransactionManager(PlatformTransactionManager globalJtaTransactionManager) {
		this.globalJtaTransactionManager = globalJtaTransactionManager;
	}

}
