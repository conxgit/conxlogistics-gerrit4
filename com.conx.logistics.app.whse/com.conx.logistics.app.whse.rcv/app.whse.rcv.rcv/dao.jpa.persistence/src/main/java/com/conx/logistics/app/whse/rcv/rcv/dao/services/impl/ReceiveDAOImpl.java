package com.conx.logistics.app.whse.rcv.rcv.dao.services.impl;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.conx.logistics.app.whse.dao.services.IWarehouseDAOService;
import com.conx.logistics.app.whse.domain.warehouse.Warehouse;
import com.conx.logistics.app.whse.rcv.asn.domain.ASN;
import com.conx.logistics.app.whse.rcv.asn.domain.ASNDropOff;
import com.conx.logistics.app.whse.rcv.asn.domain.ASNLine;
import com.conx.logistics.app.whse.rcv.asn.domain.ASNPickup;
import com.conx.logistics.app.whse.rcv.rcv.dao.services.IArrivalDAOService;
import com.conx.logistics.app.whse.rcv.rcv.dao.services.IReceiveDAOService;
import com.conx.logistics.app.whse.rcv.rcv.domain.Arrival;
import com.conx.logistics.app.whse.rcv.rcv.domain.DropOff;
import com.conx.logistics.app.whse.rcv.rcv.domain.Pickup;
import com.conx.logistics.app.whse.rcv.rcv.domain.Receive;
import com.conx.logistics.app.whse.rcv.rcv.domain.ReceiveLine;
import com.conx.logistics.app.whse.rcv.rcv.domain.types.RECEIVELINESTATUS;
import com.conx.logistics.kernel.documentlibrary.remote.services.IRemoteDocumentRepository;
import com.conx.logistics.kernel.metamodel.dao.services.IEntityTypeDAOService;
import com.conx.logistics.mdm.dao.services.IOrganizationDAOService;
import com.conx.logistics.mdm.domain.documentlibrary.DocType;
import com.conx.logistics.mdm.domain.documentlibrary.FileEntry;
import com.conx.logistics.mdm.domain.documentlibrary.Folder;
import com.conx.logistics.mdm.domain.metamodel.EntityType;
import com.conx.logistics.mdm.domain.note.Note;
import com.conx.logistics.mdm.domain.note.NoteItem;
import com.conx.logistics.mdm.domain.note.NoteType;
import com.conx.logistics.mdm.domain.organization.Organization;
import com.conx.logistics.mdm.domain.product.Product;

/**
 * Implementation of {@link Receive} that uses JPA for persistence.
 * <p />
 * <p/>
 * This class is marked as {@link Transactional}. The Spring configuration for
 * this module, enables AspectJ weaving for adding transaction demarcation to
 * classes annotated with <code>@Transactional</code>.
 */
@Transactional
@Repository
public class ReceiveDAOImpl implements IReceiveDAOService {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * Spring will inject a managed JPA {@link EntityManager} into this field.
	 */
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private IRemoteDocumentRepository documentRepositoryService;

	@Autowired
	private IEntityTypeDAOService entityTypeDAOService;

	@Autowired
	private IArrivalDAOService arrivalDAOService;

	@Autowired
	private IOrganizationDAOService orgDAOService;

	@Autowired
	private IWarehouseDAOService warehouseDAOService;

	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public Receive get(long id) {
		try {
			logger.info("test...");
			return em.find(Receive.class, id);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);
			return null;
		}
	}

	@Override
	public List<Receive> getAll() {
		return em.createQuery("select o from com.conx.logistics.app.whse.rcv.rcv.domain.Receive o", Receive.class).getResultList();
	}

	@Override
	public Receive add(Receive record) {
		record = em.merge(record);
		return record;
	}

	@Override
	public void delete(Receive record) {
		em.remove(record);
	}

	@Override
	public Receive update(Receive record) {
		return em.merge(record);
	}

	public Receive addLines(Long rcvId, Set<ReceiveLine> lines) throws Exception {
		Receive rcv = null;
		try {
			rcv = em.getReference(Receive.class, rcvId);
			Product prod = null;
			for (ReceiveLine line : lines) {
				line.setParentReceive(rcv);

				prod = em.merge(line.getProduct());
				line.setProduct(prod);

				line = (ReceiveLine) em.merge(line);
				rcv.getRcvLines().add(line);
			}

			rcv = update(rcv);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);

			throw e;
		}

		return rcv;
	}

	@Override
	public Receive process(ASN asn) throws Exception {
		assert asn.getWarehouse() != null;

		Receive rcv = new Receive();
		rcv = add(rcv);

		assignCode(rcv, asn);
		rcv.setWarehouse(asn.getWarehouse());
		rcv.setShippedFrom(asn.getShippedFrom());
		rcv.setConsignee(asn.getConsignee());
		rcv.setVolUnit(asn.getVolUnit());
		rcv.setWeightUnit(asn.getWeightUnit());
		rcv.setExpectedTotalLen(asn.getExpectedTotalLen());
		rcv.setExpectedTotalWidth(asn.getExpectedTotalWidth());
		rcv.setExpectedTotalHeight(asn.getExpectedTotalHeight());
		rcv.setDimUnit(asn.getDimUnit());
		rcv.setOuterPackUnit(asn.getOuterPackUnit());
		rcv.setTenant(asn.getTenant());

		// TODO MAKE SURE THE ASN'S PICKUP AND DROPOFF GET COPIED INTO THE
		// RECEIVE

		// -- RcvLine's
		for (ASNLine asnLine : asn.getAsnLines()) {
			rcv.getRcvLines().add(copyRcvLine(asnLine, rcv));
		}

		// Doc Folder
		EntityType et = entityTypeDAOService.provide(Receive.class);
		Folder fldr = documentRepositoryService.provideFolderForEntity(et, rcv.getId());
		fldr = em.merge(fldr);
		rcv.setDocFolder(fldr);

		// Note
		Note note = new Note();
		note.setCode(rcv.getCode());
		note = em.merge(note);
		rcv.setNote(note);

		rcv = em.merge(rcv);

		return rcv;
	}

	@Override
	public FileEntry addAttachment(Long rcvId, String sourceFileName, String title, String description, String mimeType,
			DocType attachmentType) throws Exception {
		Receive rcv = get(rcvId);
		FileEntry fe = documentRepositoryService.addorUpdateFileEntry(rcv, attachmentType, sourceFileName, mimeType, title, description);
		rcv = update(rcv);

		return fe;
	}

	@Override
	public FileEntry addAttachment(Long rcvId, File sourceFile, String title, String description, String mimeType, DocType attachmentType)
			throws Exception {
		Receive rcv = get(rcvId);
		FileEntry fe = documentRepositoryService.addorUpdateFileEntry(rcv, attachmentType, sourceFile, mimeType, title, description);
		rcv = update(rcv);

		return fe;
	}

	@Override
	public FileEntry addAttachment(Receive rcv, File sourceFile, String title, String description, String mimeType, DocType attachmentType)
			throws Exception {
		FileEntry fe = documentRepositoryService.addorUpdateFileEntry(rcv, attachmentType, sourceFile, mimeType, title, description);
		rcv = update(rcv);

		return fe;
	}

	private DropOff copyDropOff(ASNDropOff asnDropOff) {
		DropOff dro = new DropOff();
		dro = em.merge(dro);

		dro.setDockType(asnDropOff.getDockType());

		return dro;
	}

	private Pickup copyPickUp(ASNPickup asnPickUp) {
		Pickup pu = new Pickup();
		pu = em.merge(pu);

		pu.setDockType(asnPickUp.getDockType());

		return pu;
	}

	private ReceiveLine copyRcvLine(ASNLine asnLine, Receive rcv) {
		ReceiveLine rl = new ReceiveLine();
		int lineCount = rcv.getRcvLines().size() + 1;
		String name = rcv.getName() + "-" + String.format("%02d", lineCount);

		rl.setName(name);
		rl.setLineNumber(lineCount);
		rl.setCode(name);
		rl.setStatus(RECEIVELINESTATUS.AWAITING_ARRIVAL);
		rl.setExpectedInnerPackCount(asnLine.getExpectedInnerPackCount());
		rl.setExpectedInnerPackCount(asnLine.getExpectedOuterPackCount());
		rl.setExpectedTotalHeight(asnLine.getExpectedTotalHeight());
		rl.setExpectedTotalLen(asnLine.getExpectedTotalLen());
		rl.setExpectedTotalVolume(asnLine.getExpectedTotalVolume());
		rl.setExpectedTotalWeight(asnLine.getExpectedTotalWeight());
		rl.setExpectedTotalWidth(asnLine.getExpectedTotalWidth());
		// Replace null with defaults
		if (rl.getExpectedInnerPackCount() == null)
			rl.setExpectedInnerPackCount(1);
		if (rl.getExpectedOuterPackCount() == null)
			rl.setExpectedOuterPackCount(0);
		if (rl.getExpectedTotalWeight() == null)
			rl.setExpectedTotalWeight(0.0);
		if (rl.getExpectedTotalVolume() == null)
			rl.setExpectedTotalVolume(0.0);
		if (rl.getExpectedTotalLen() == null)
			rl.setExpectedTotalLen(0.0);
		if (rl.getExpectedTotalHeight() == null)
			rl.setExpectedTotalHeight(0.0);
		if (rl.getExpectedTotalWidth() == null)
			rl.setExpectedTotalWidth(0.0);

		rl.setProduct(asnLine.getProduct());
		rl.setParentReceive(rcv);

		rl = em.merge(rl);

		return rl;
	}

	private void assignCode(Receive newRecord) {
		String format = String.format("%%0%dd", 6);
		String paddedId = String.format(format, newRecord.getId());
		String code = "R" + paddedId;
		newRecord.setName(code);
		newRecord.setCode(code);
	}

	private void assignCode(Receive receive, ASN parentAsn) {
		if (parentAsn == null || parentAsn.getName() == null) {
			assignCode(receive);
		} else {
			String format = String.format("%%0%dd", 3);
			String paddedId = String.format(format, receive.getId());
			String code = parentAsn.getName() + "-R" + String.valueOf(paddedId);
			receive.setName(code);
			receive.setCode(code);
		}
	}

	@Override
	public NoteItem addNoteItem(Long rcvId, String content, NoteType noteType) throws Exception {
		Receive rcv = get(rcvId);
		NoteItem ni = new NoteItem();
		ni.setParentNote(rcv.getNote());
		ni.setNoteType(noteType);
		ni.setContent(content);
		ni = em.merge(ni);

		rcv.getNote().getNotes().add(ni);

		rcv = em.merge(rcv);

		return ni;
	}

	@Override
	public Arrival attachArrival(Receive targetRcv) throws ClassNotFoundException, Exception {

		targetRcv = em.merge(targetRcv);

		Arrival arvl = new Arrival();
		arvl = arrivalDAOService.add(arvl, targetRcv);

		targetRcv.getArrivals().add(arvl);
		targetRcv = update(targetRcv);

		return arvl;
	}

	@Override
	public Receive provide(Organization shipper, Organization consignee, Warehouse warehouse) {
		Query q = em
				.createQuery("select o from com.conx.logistics.app.whse.rcv.rcv.domain.Receive o WHERE o.consignee.id = :consigneeId AND o.shipper.id = :shipperId");
		q.setParameter("consigneeId", consignee.getId());
		q.setParameter("shipperId", shipper.getId());

		List<?> result = q.getResultList();
		if (result.size() > 0) {
			return (Receive) result.get(0);
		} else {
			Receive rcv = new Receive();
			rcv.setShipper(shipper);
			rcv.setConsignee(consignee);
			rcv.setWarehouse(warehouse);
			rcv = em.merge(rcv);
			assignCode(rcv);
			return em.merge(rcv);
		}
	}

	@Override
	public Receive provideDefault() {
		Organization defaultOrg = this.orgDAOService.provideDefault();
		Warehouse defaultWarehouse = this.warehouseDAOService.provideDefault();
		return provide(defaultOrg, defaultOrg, defaultWarehouse);
	}
}
