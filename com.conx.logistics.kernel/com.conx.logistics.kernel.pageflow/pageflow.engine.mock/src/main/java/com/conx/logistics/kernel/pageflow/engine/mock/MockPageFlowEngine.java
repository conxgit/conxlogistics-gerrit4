package com.conx.logistics.kernel.pageflow.engine.mock;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

import org.springframework.transaction.PlatformTransactionManager;

import com.conx.logistics.kernel.bpm.services.IBPMService;
import com.conx.logistics.kernel.metamodel.dao.services.IEntityTypeDAOService;
import com.conx.logistics.kernel.pageflow.services.IPageFlowManager;
import com.conx.logistics.kernel.pageflow.services.IPageFlowSession;
import com.conx.logistics.kernel.pageflow.services.ITaskWizard;
import com.conx.logistics.mdm.domain.task.TaskDefinition;

public class MockPageFlowEngine implements IPageFlowManager {
	
	public void start() {
	}

	public void stop() {
	}

	@Override
	public IPageFlowSession startPageFlowSession(String userid, TaskDefinition td) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITaskWizard createTaskWizard(Map<String, Object> properties) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITaskWizard executeTaskWizard(ITaskWizard tw, Object data) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITaskWizard resumeProcessInstanceTaskWizard(String processInstanceId, Map<String, Object> properties) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPageFlowSession closePageFlowSession(String userid, Long pageFlowSessionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPageFlowSession resumePageFlowSession(String userid, Long pageFlowSessionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> updateProcessInstanceVariables(ITaskWizard tw, Map<String, Object> varsToUpdate) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityManagerFactory getConXEntityManagerfactory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PlatformTransactionManager getJTAGlobalTransactionManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBPMService getBPMService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IEntityTypeDAOService getEntityTypeDAOService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserTransaction getUserTransaction() {
		// TODO Auto-generated method stub
		return null;
	}

}
