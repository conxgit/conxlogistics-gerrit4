package com.conx.logistics.app.whse.rcv.asn.workitems;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.drools.process.instance.WorkItemHandler;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.conx.logistics.app.whse.rcv.asn.domain.ASN;

@Transactional
@Repository
public class AcceptASNWIH implements WorkItemHandler {
	private static final Logger logger = LoggerFactory.getLogger(AcceptASNWIH.class);

	@PersistenceContext
	private EntityManager em;

	private PlatformTransactionManager globalJtaTransactionManager;

	public void setGlobalJtaTransactionManager(PlatformTransactionManager globalJtaTransactionManager) {
		this.globalJtaTransactionManager = globalJtaTransactionManager;
	}

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		ASN asnIn = (ASN) workItem.getParameter("asnIn");
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_MANDATORY);
		TransactionStatus status = globalJtaTransactionManager.getTransaction(def);

		Map<String, Object> output = new HashMap<String, Object>();
		try {
			if (asnIn.getPickup() != null) {
				asnIn.setPickup(em.merge(asnIn.getPickup()));
			}
			if (asnIn.getDropOff() != null) {
				asnIn.setDropOff(em.merge(asnIn.getDropOff()));
			}

			asnIn = em.merge(asnIn);
			globalJtaTransactionManager.commit(status);

			output.put("asnOut", asnIn);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);

			globalJtaTransactionManager.rollback(status);
		}

		manager.completeWorkItem(workItem.getId(), output);
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
	}
}
