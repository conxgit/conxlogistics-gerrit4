package com.conx.logistics.app.whse.rcv.rcv.workitems.expected;

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
import org.springframework.transaction.annotation.Transactional;

import com.conx.logistics.app.whse.rcv.rcv.dao.services.IReceiveDAOService;
import com.conx.logistics.app.whse.rcv.rcv.domain.Arrival;
import com.conx.logistics.app.whse.rcv.rcv.domain.Receive;
import com.conx.logistics.mdm.dao.services.IOrganizationDAOService;

@Transactional
@Repository
public class AttachNewArrivalWIH implements WorkItemHandler {
	private static final Logger logger = LoggerFactory.getLogger(AttachNewArrivalWIH.class);
	
	@Autowired
	private IOrganizationDAOService orgDao;
	
	@Autowired
	private IReceiveDAOService receiveDAOService;


	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		try
		{
			Receive rcv = (Receive)workItem.getParameter("receiveIn");
			
			Arrival arrival = new Arrival();//receiveDAOService.attachArrival(rcv);
			
			Map<String, Object> results = new HashMap<String, Object>();
			results.put("arrivalOut",arrival);
			manager.completeWorkItem(workItem.getId(), results);
		}
		catch (Exception e)
		{
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);
			
			throw new IllegalStateException("AttachNewArrivalWIH:\r\n"+stacktrace, e);
		}	
		catch (Error e)
		{
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);
			
			throw new IllegalStateException("AttachNewArrivalWIH:\r\n"+stacktrace, e);			
		}			
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		// TODO Auto-generated method stub

	}

}
