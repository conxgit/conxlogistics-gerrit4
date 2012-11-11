package com.conx.logistics.app.whse.rcv.rcv.dao.services.impl;

import java.io.File;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.conx.logistics.app.whse.rcv.rcv.dao.services.IArrivalDAOService;
import com.conx.logistics.app.whse.rcv.rcv.dao.services.IArrivalReceiptDAOService;
import com.conx.logistics.app.whse.rcv.rcv.domain.Arrival;
import com.conx.logistics.app.whse.rcv.rcv.domain.ArrivalReceipt;
import com.conx.logistics.app.whse.rcv.rcv.domain.ArrivalReceiptLine;
import com.conx.logistics.app.whse.rcv.rcv.domain.Receive;
import com.conx.logistics.app.whse.rcv.rcv.domain.types.ARRIVALRECEIPTLINESTATUS;
import com.conx.logistics.app.whse.rcv.rcv.domain.types.ARRIVALRECEIPTSTATUS;
import com.conx.logistics.mdm.domain.documentlibrary.DocType;
import com.conx.logistics.mdm.domain.documentlibrary.FileEntry;
import com.conx.logistics.mdm.domain.note.NoteItem;
import com.conx.logistics.mdm.domain.note.NoteType;

@Transactional
@Repository
public class ArrivalReceiptDAOImpl implements IArrivalReceiptDAOService {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * Spring will inject a managed JPA {@link EntityManager} into this field.
	 */
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private IArrivalDAOService arrivalDAOService;

	@Override
	public List<Arrival> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrivalReceipt add(Long parentArrivalId, ArrivalReceipt newRecord) throws Exception {
//		em.joinTransaction();
		newRecord.setStatus(ARRIVALRECEIPTSTATUS.ARRIVING);

		// -- Create underlying receive
		Arrival parentArrvl = arrivalDAOService.get(parentArrivalId);
		Receive parentRcv = parentArrvl.getReceive();

		int lineCount = parentArrvl.getReceipts().size() + 1;
		String name = parentArrvl.getName() + "-R" + String.format("%02d", lineCount);
		
		ArrivalReceipt existingRecord = getArrivalReceiptByCode(name);
		if (existingRecord == null) {
			newRecord.setName(name);
			newRecord.setCode(name);
		} else {
			newRecord = existingRecord;
		}
		
		newRecord.setParentArrival(parentArrvl);
		
		newRecord = em.merge(newRecord);
		return newRecord;
	}

	@Override
	public ArrivalReceiptLine addArrivalReceiptLine(Long arrivalReceiptId, ArrivalReceiptLine newRecord) throws Exception {
		ArrivalReceipt parentArrivalReceipt = get(arrivalReceiptId);

		int lineCount = parentArrivalReceipt.getRcptLines().size() + 1;
		String name = parentArrivalReceipt.getName() + "-L" + String.format("%02d", lineCount);

		ArrivalReceiptLine existingRecord = getArrivalReceiptLineByCode(name);
		if (existingRecord == null) {
			newRecord.setName(name);
			newRecord.setCode(name);
		} else {
			newRecord = existingRecord;
		}
		
		newRecord.setStatus(ARRIVALRECEIPTLINESTATUS.AWAITING_ARRIVAL);
		newRecord.setParentArrivalReceipt(parentArrivalReceipt);

		newRecord = em.merge(newRecord);

		return newRecord;
	}

	@Override
	public FileEntry addAttachment(Long arvlId, File sourceFile, String title, String description, String mimeType, DocType attachmentType) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileEntry addAttachment(Long arvlId, String sourceFileName, String title, String description, String mimeType, DocType attachmentType) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NoteItem addNoteItem(Long arvlId, String content, NoteType noteType) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(ArrivalReceipt record) {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrivalReceipt update(ArrivalReceipt record) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrivalReceipt get(long id) {
		return em.getReference(ArrivalReceipt.class, id);
	}

	public ArrivalReceipt add(ArrivalReceipt newRecord, Long arrivalPK, Long warehousePK) throws Exception {
		newRecord.setStatus(ARRIVALRECEIPTSTATUS.ARRIVING);

		Arrival arrival = em.getReference(Arrival.class, arrivalPK);
		newRecord.setParentArrival(arrival);

		int lineCount = arrival.getReceipts().size() + 1;
		String name = arrival.getName() + "-R" + String.format("%02d", lineCount);

		newRecord.setName(name);
		newRecord.setCode(name);
		newRecord.setConsignee(arrival.getReceive().getConsignee());
		newRecord.setShipper(arrival.getReceive().getConsignor());

		newRecord = em.merge(newRecord);

		return newRecord;
	}
	
	public ArrivalReceipt getArrivalReceiptByCode(String code) {
		try {
			TypedQuery<ArrivalReceipt> q = em.createQuery("select o from com.conx.logistics.app.whse.rcv.rcv.domain.ArrivalReceipt o WHERE o.code = :code",ArrivalReceipt.class);
			q.setParameter("code", code);
			return q.getSingleResult();
		} catch (NoResultException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrivalReceiptLine getArrivalReceiptLineByCode(String string) {
		try {
			TypedQuery<ArrivalReceiptLine> q = em.createQuery("select o from com.conx.logistics.app.whse.rcv.rcv.domain.ArrivalReceiptLine o WHERE o.code = :code",ArrivalReceiptLine.class);
			q.setParameter("code", string);
			return q.getSingleResult();
		} catch (NoResultException e) {
			e.printStackTrace();
			return null;
		}
	}
}
