package com.conx.logistics.app.whse.rcv.rcv.dao.services.impl;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.conx.logistics.app.whse.rcv.rcv.dao.services.IArrivalDAOService;
import com.conx.logistics.app.whse.rcv.rcv.dao.services.IReceiveDAOService;
import com.conx.logistics.app.whse.rcv.rcv.dao.services.IReceiveLineDAOService;
import com.conx.logistics.app.whse.rcv.rcv.domain.Arrival;
import com.conx.logistics.app.whse.rcv.rcv.domain.Receive;
import com.conx.logistics.app.whse.rcv.rcv.domain.ReceiveLine;
import com.conx.logistics.app.whse.rcv.rcv.domain.ReceiveLineStockItemSet;
import com.conx.logistics.app.whse.rcv.rcv.domain.types.RECEIVELINESTATUS;
import com.conx.logistics.kernel.documentlibrary.remote.services.IRemoteDocumentRepository;
import com.conx.logistics.kernel.metamodel.dao.services.IEntityTypeDAOService;
import com.conx.logistics.mdm.dao.services.product.IDimUnitDAOService;
import com.conx.logistics.mdm.dao.services.product.IUnitConversionService;
import com.conx.logistics.mdm.dao.services.product.IWeightUnitDAOService;
import com.conx.logistics.mdm.domain.constants.DimUnitCustomCONSTANTS;
import com.conx.logistics.mdm.domain.constants.WeightUnitCustomCONSTANTS;
import com.conx.logistics.mdm.domain.documentlibrary.DocType;
import com.conx.logistics.mdm.domain.documentlibrary.FileEntry;
import com.conx.logistics.mdm.domain.note.NoteItem;
import com.conx.logistics.mdm.domain.note.NoteType;
import com.conx.logistics.mdm.domain.product.DimUnit;
import com.conx.logistics.mdm.domain.product.WeightUnit;

@Transactional
@Repository
public class ReceiveLineDAOImpl implements IReceiveLineDAOService {
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private IRemoteDocumentRepository documentRepositoryService;

	@Autowired
	private IEntityTypeDAOService entityTypeDAOService;

	@Autowired
	private IArrivalDAOService arrivalDAOService;

	@Autowired
	private IDimUnitDAOService dimUnitDAOService;

	@Autowired
	private IWeightUnitDAOService weightUnitDAOService;

	@Autowired
	private IUnitConversionService unitConversionService;

	@Autowired
	private IReceiveDAOService receiveDAOService;

	@Override
	public List<ReceiveLine> getAll() {
		return em.createQuery("select o from com.conx.logistics.app.whse.rcv.rcv.domain.ReceiveLine o", ReceiveLine.class).getResultList();
	}

	@Override
	public ReceiveLine add(ReceiveLine record, Long parentReceivePK) {
		assert parentReceivePK != null;
		assert record != null;
		Receive receive = this.receiveDAOService.get(parentReceivePK);
		assert receive != null;

		String format = String.format("%%0%dd", 6);
		String paddedId = String.format(format, receive.getRcvLines().size() + 1);
		String code = "R" + receive.getCode() + paddedId;
		record.setName(code);
		record.setCode(code);
		record.setArrivedInnerPackCount(1);
		record.setParentReceive(receive);
		record = em.merge(record);
		em.flush();
		assert record != null;

		receive.getRcvLines().add(record);
		this.receiveDAOService.update(receive);

		assert record.getId() != null;
		return record;
	}

	@Override
	public Arrival attachArrival(ReceiveLine targetRcv) throws ClassNotFoundException, Exception {
		throw new UnsupportedOperationException("attachArrival has not been implemented");
	}

	@Override
	public FileEntry addAttachment(Long rcvLinePK, String sourceFileName, String title, String description, String mimeType, DocType attachmentType) throws Exception {
		throw new UnsupportedOperationException("addAttachment has not been implemented");
	}

	@Override
	public NoteItem addNoteItem(Long rcvId, String content, NoteType noteType) throws Exception {
		throw new UnsupportedOperationException("addNoteItem has not been implemented");
	}

	@Override
	public void delete(ReceiveLine record) {
		em.remove(record);
	}

	@SuppressWarnings("unchecked")
	public List<ReceiveLine> findReceiveLinesByINList(Collection<ReceiveLine> rcvLineList) {
		if (rcvLineList == null || rcvLineList.size() == 0)
			throw new IllegalArgumentException("The rcvLineList argument is required");
		Query q = em.createQuery("SELECT ReceiveLine FROM ReceiveLine AS ReceiveLine ERE ReceiveLine IN (:rcvLineList)");
		q.setParameter("rcvLineList", rcvLineList);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ReceiveLine> findReceiveLinesByCode(String code) {
		if (code == null)
			throw new IllegalArgumentException("The code argument is required");
		Query q = em.createQuery("SELECT ReceiveLine FROM ReceiveLine AS ReceiveLine WHERE ReceiveLine.code = :code");
		q.setParameter("code", code);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ReceiveLine> findReceiveLinesByStatusAndReceive(RECEIVELINESTATUS status, Long receivePK) {
		assert (receivePK != null);
		assert (status != null);
		
		Query q = em.createQuery("SELECT ReceiveLine FROM ReceiveLine AS ReceiveLine WHERE ReceiveLine.status = :status AND ReceiveLine.parentReceive.id = :receiveId");
		q.setParameter("status", status);
		q.setParameter("receiveId", receivePK);
		return q.getResultList();
	}

	@Override
	public ReceiveLine update(ReceiveLine record) {
		return em.merge(record);
	}

	@Override
	public ReceiveLine get(long id) {
		return em.getReference(ReceiveLine.class, id);
	}

	@Override
	public Double calculateExpectedProductTotalWeightInLbs(ReceiveLine receiveLine) {
		Double weight = receiveLine.getExpectedProductTotalWeight();
		WeightUnit weightUnit = receiveLine.getProduct().getWeightUnit();
		WeightUnit lbsWeightUnit = this.weightUnitDAOService.provide(WeightUnitCustomCONSTANTS.TYPE_LB, WeightUnitCustomCONSTANTS.TYPE_LB_DESCRIPTION);
		return this.unitConversionService.convertWeight(weight, weightUnit, lbsWeightUnit);
	}

	@Override
	public Double calculateExpectedProductTotalVolumeInCf(ReceiveLine receiveLine) {
		Double volume = receiveLine.getExpectedProductTotalVolume();
		DimUnit dimUnit = receiveLine.getProduct().getDimUnit();
		DimUnit cfDimUnit = this.dimUnitDAOService.provide(DimUnitCustomCONSTANTS.TYPE_CF, DimUnitCustomCONSTANTS.TYPE_CF_DESCRIPTION);
		return this.unitConversionService.convertDimension(volume, dimUnit, cfDimUnit);
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	public IRemoteDocumentRepository getDocumentRepositoryService() {
		return documentRepositoryService;
	}

	public void setDocumentRepositoryService(IRemoteDocumentRepository documentRepositoryService) {
		this.documentRepositoryService = documentRepositoryService;
	}

	public IEntityTypeDAOService getEntityTypeDAOService() {
		return entityTypeDAOService;
	}

	public void setEntityTypeDAOService(IEntityTypeDAOService entityTypeDAOService) {
		this.entityTypeDAOService = entityTypeDAOService;
	}

	public IArrivalDAOService getArrivalDAOService() {
		return arrivalDAOService;
	}

	public void setArrivalDAOService(IArrivalDAOService arrivalDAOService) {
		this.arrivalDAOService = arrivalDAOService;
	}

	public IDimUnitDAOService getDimUnitDAOService() {
		return dimUnitDAOService;
	}

	public void setDimUnitDAOService(IDimUnitDAOService dimUnitDAOService) {
		this.dimUnitDAOService = dimUnitDAOService;
	}

	public IWeightUnitDAOService getWeightUnitDAOService() {
		return weightUnitDAOService;
	}

	public void setWeightUnitDAOService(IWeightUnitDAOService weightUnitDAOService) {
		this.weightUnitDAOService = weightUnitDAOService;
	}

	public IUnitConversionService getUnitConversionService() {
		return unitConversionService;
	}

	public void setUnitConversionService(IUnitConversionService unitConversionService) {
		this.unitConversionService = unitConversionService;
	}

	@Override
	public ReceiveLineStockItemSet add(ReceiveLineStockItemSet record, Long receiveLinePK) {
		assert record != null;
		assert receiveLinePK != null;
		ReceiveLine receiveLine = get(receiveLinePK);
		assert receiveLine != null;

		String format = String.format("%%0%dd", 6);
		String paddedId = String.format(format, receiveLine.getRlStockItems().size() + 1);
		String code = "IS" + receiveLine.getCode() + paddedId;
		record.setName(code);
		record.setCode(code);
		record.setReceiveLine(receiveLine);
		record = em.merge(record);

		receiveLine.getRlStockItems().add(record);
		update(receiveLine);

		return record;
	}

	@Override
	public ReceiveLineStockItemSet update(ReceiveLineStockItemSet itemSet) {
		return em.merge(itemSet);
	}

	@Override
	public ReceiveLineStockItemSet getReceiveLineStockItemSet(Long id) {
		return em.getReference(ReceiveLineStockItemSet.class, id);
	}

	@Override
	public void delete(ReceiveLineStockItemSet itemSet) {
		em.remove(itemSet);
	}

}
