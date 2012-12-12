package com.conx.logistics.kernel.ui.factory.services.impl.data;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

import com.conx.logistics.app.whse.dao.services.ILocationDAOService;
import com.conx.logistics.app.whse.im.dao.services.IStockItemDAOService;
import com.conx.logistics.app.whse.rcv.asn.dao.services.IASNDAOService;
import com.conx.logistics.app.whse.rcv.rcv.dao.services.IArrivalDAOService;
import com.conx.logistics.app.whse.rcv.rcv.dao.services.IArrivalReceiptDAOService;
import com.conx.logistics.app.whse.rcv.rcv.dao.services.IReceiveDAOService;
import com.conx.logistics.app.whse.rcv.rcv.dao.services.IReceiveLineDAOService;
import com.conx.logistics.kernel.documentlibrary.remote.services.IRemoteDocumentRepository;
import com.conx.logistics.kernel.metamodel.dao.services.IEntityTypeDAOService;
import com.conx.logistics.kernel.system.dao.services.application.IFeatureDAOService;
import com.conx.logistics.kernel.ui.factory.services.data.IDAOProvider;
import com.conx.logistics.mdm.dao.services.IBaseEntityDAOService;
import com.conx.logistics.mdm.dao.services.IEntityMetadataDAOService;
import com.conx.logistics.mdm.dao.services.documentlibrary.IDocTypeDAOService;
import com.conx.logistics.mdm.dao.services.documentlibrary.IFolderDAOService;
import com.conx.logistics.mdm.dao.services.note.INoteDAOService;
import com.conx.logistics.mdm.dao.services.referencenumber.IReferenceNumberDAOService;

public class DAOProvider implements IDAOProvider {
	private Map<Class<?>, Object> daoCache;
//	@Autowired
	private PlatformTransactionManager globalTransactionManager;
//	@Autowired
	private IFolderDAOService folderDAOService;
//	@Autowired
	private IRemoteDocumentRepository remoteDocumentRepositoryService;
//	@Autowired
	private IEntityMetadataDAOService entityMetadataDAOService;
//	@Autowired
	private IEntityTypeDAOService entityTypeDAOService;
//	@Autowired
	private IReferenceNumberDAOService referenceNumberDAOService;
//	@Autowired
	private IDocTypeDAOService docTypeDAOService;
//	@Autowired
	private INoteDAOService noteDAOService;
//	@Autowired
//	private IFileEntryDAOService fileEntryDAOService;
//	@Autowired
	private IArrivalDAOService arrivalDAOService;
//	@Autowired
	private IArrivalReceiptDAOService arrivalReceiptDAOService;
//	@Autowired
	private IReceiveDAOService receiveDAOService;
//	@Autowired
	private IReceiveLineDAOService receiveLineDAOService;
//	@Autowired
	private IStockItemDAOService stockItemDAOService;
//	@Autowired
	private ILocationDAOService locationDAOService;
//	@Autowired
	private IBaseEntityDAOService baseEntityDAOService;
//  @Autowired
	private IFeatureDAOService featureDAOService;
//  @Autowired
	private IASNDAOService asnDaoService;
	
	public void start() {
	}
	
	public void stop() {
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T provideByDAOClass(Class<T> daoClass) {
		return (T) daoCache.get(daoClass);
	}

/*	@SuppressWarnings("unused")
	@Override
	public void runInTransaction(Runnable runnable) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setName("services.dao.provider");
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		def.setTimeout(300);
		TransactionStatus status = this.globalTransactionManager.getTransaction(def);
		try {
			runnable.run();
//			this.globalTransactionManager.commit(status);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			this.globalTransactionManager.rollback(status);
		}
	}*/

	public PlatformTransactionManager getGlobalTransactionManager() {
		return globalTransactionManager;
	}

	@Autowired
	public void setGlobalTransactionManager(PlatformTransactionManager globalTransactionManager) {
		this.provideDaoCache().put(PlatformTransactionManager.class, globalTransactionManager);
		this.globalTransactionManager = globalTransactionManager;
	}

	/*@Override
	public TransactionStatus beginTransaction() {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setName("services.dao.provider");
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = this.globalTransactionManager.getTransaction(def);
		return status;
	}

	@Override
	public void endTransaction(TransactionStatus status) {
		this.globalTransactionManager.commit(status);
	}

	@Override
	public void rollbackTransaction(TransactionStatus status) {
		this.globalTransactionManager.rollback(status);
	}*/

	public IFolderDAOService getFolderDAOService() {
		return folderDAOService;
	}

	@Autowired
	public void setFolderDAOService(IFolderDAOService folderDAOService) {
		this.provideDaoCache().put(IFolderDAOService.class, folderDAOService);
		this.folderDAOService = folderDAOService;
	}

	public IEntityMetadataDAOService getEntityMetadataDAOService() {
		return entityMetadataDAOService;
	}

	@Autowired
	public void setEntityMetadataDAOService(IEntityMetadataDAOService entityMetadataDAOService) {
		this.provideDaoCache().put(IEntityMetadataDAOService.class, entityMetadataDAOService);
		this.entityMetadataDAOService = entityMetadataDAOService;
	}

	public IEntityTypeDAOService getEntityTypeDAOService() {
		return entityTypeDAOService;
	}

	@Autowired
	public void setEntityTypeDAOService(IEntityTypeDAOService entityTypeDAOService) {
		this.provideDaoCache().put(IEntityTypeDAOService.class, entityTypeDAOService);
		this.entityTypeDAOService = entityTypeDAOService;
	}

	public IReferenceNumberDAOService getReferenceNumberDAOService() {
		return referenceNumberDAOService;
	}

	@Autowired
	public void setReferenceNumberDAOService(IReferenceNumberDAOService referenceNumberDAOService) {
		this.provideDaoCache().put(IReferenceNumberDAOService.class, referenceNumberDAOService);
		this.referenceNumberDAOService = referenceNumberDAOService;
	}

	public IDocTypeDAOService getDocTypeDAOService() {
		return docTypeDAOService;
	}

	@Autowired
	public void setDocTypeDAOService(IDocTypeDAOService docTypeDAOService) {
		this.provideDaoCache().put(IDocTypeDAOService.class, docTypeDAOService);
		this.docTypeDAOService = docTypeDAOService;
	}

	public INoteDAOService getNoteDAOService() {
		return noteDAOService;
	}

	@Autowired
	public void setNoteDAOService(INoteDAOService noteDAOService) {
		this.provideDaoCache().put(INoteDAOService.class, noteDAOService);
		this.noteDAOService = noteDAOService;
	}

	/*public IFileEntryDAOService getFileEntryDAOService() {
		return fileEntryDAOService;
	}

	@Autowired
	public void setFileEntryDAOService(IFileEntryDAOService fileEntryDAOService) {
		this.provideDaoCache().put(IFileEntryDAOService.class, fileEntryDAOService);
		this.fileEntryDAOService = fileEntryDAOService;
	}*/

	public IArrivalDAOService getArrivalDAOService() {
		return arrivalDAOService;
	}

	@Autowired
	public void setArrivalDAOService(IArrivalDAOService arrivalDAOService) {
		this.provideDaoCache().put(IArrivalDAOService.class, arrivalDAOService);
		this.arrivalDAOService = arrivalDAOService;
	}

	public IArrivalReceiptDAOService getArrivalReceiptDAOService() {
		return arrivalReceiptDAOService;
	}

	@Autowired
	public void setArrivalReceiptDAOService(IArrivalReceiptDAOService arrivalReceiptDAOService) {
		this.provideDaoCache().put(IArrivalReceiptDAOService.class, arrivalReceiptDAOService);
		this.arrivalReceiptDAOService = arrivalReceiptDAOService;
	}

	public IReceiveDAOService getReceiveDAOService() {
		return receiveDAOService;
	}

	@Autowired
	public void setReceiveDAOService(IReceiveDAOService receiveDAOService) {
		this.provideDaoCache().put(IReceiveDAOService.class, receiveDAOService);
		this.receiveDAOService = receiveDAOService;
	}

	public IReceiveLineDAOService getReceiveLineDAOService() {
		return receiveLineDAOService;
	}

	@Autowired
	public void setReceiveLineDAOService(IReceiveLineDAOService receiveLineDAOService) {
		this.provideDaoCache().put(IReceiveLineDAOService.class, receiveLineDAOService);
		this.receiveLineDAOService = receiveLineDAOService;
	}

	public IStockItemDAOService getStockItemDAOService() {
		return stockItemDAOService;
	}

	@Autowired
	public void setStockItemDAOService(IStockItemDAOService stockItemDAOService) {
		this.provideDaoCache().put(IStockItemDAOService.class, stockItemDAOService);
		this.stockItemDAOService = stockItemDAOService;
	}

	public ILocationDAOService getLocationDAOService() {
		return locationDAOService;
	}

	@Autowired
	public void setLocationDAOService(ILocationDAOService locationDAOService) {
		this.provideDaoCache().put(ILocationDAOService.class, locationDAOService);
		this.locationDAOService = locationDAOService;
	}

	public IBaseEntityDAOService getBaseEntityDAOService() {
		return baseEntityDAOService;
	}

	@Autowired
	public void setBaseEntityDAOService(IBaseEntityDAOService baseEntityDAOService) {
		this.provideDaoCache().put(IBaseEntityDAOService.class, baseEntityDAOService);
		this.baseEntityDAOService = baseEntityDAOService;
	}
	
	private Map<Class<?>, Object> provideDaoCache() {
		if (this.daoCache == null) {
			this.daoCache = new HashMap<Class<?>, Object>();
		}
		return this.daoCache;
	}

	public IRemoteDocumentRepository getRemoteDocumentRepositoryService() {
		return remoteDocumentRepositoryService;
	}

	@Autowired
	public void setRemoteDocumentRepositoryService(IRemoteDocumentRepository remoteDocumentRepositoryService) {
		this.provideDaoCache().put(IRemoteDocumentRepository.class, remoteDocumentRepositoryService);
		this.remoteDocumentRepositoryService = remoteDocumentRepositoryService;
	}

	public IFeatureDAOService getFeatureDAOService() {
		return featureDAOService;
	}

	@Autowired
	public void setFeatureDAOService(IFeatureDAOService featureDAOService) {
		this.provideDaoCache().put(IFeatureDAOService.class, featureDAOService);
		this.featureDAOService = featureDAOService;
	}

	public IASNDAOService getAsnDaoService() {
		return asnDaoService;
	}

	@Autowired
	public void setAsnDaoService(IASNDAOService asnDaoService) {
		this.provideDaoCache().put(IASNDAOService.class, asnDaoService);
		this.asnDaoService = asnDaoService;
	}
}
