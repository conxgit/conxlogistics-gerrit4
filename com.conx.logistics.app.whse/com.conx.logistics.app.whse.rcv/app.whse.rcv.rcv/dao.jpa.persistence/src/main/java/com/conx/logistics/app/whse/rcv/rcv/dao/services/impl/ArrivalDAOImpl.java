package com.conx.logistics.app.whse.rcv.rcv.dao.services.impl;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.conx.logistics.app.whse.rcv.rcv.dao.services.IArrivalDAOService;
import com.conx.logistics.app.whse.rcv.rcv.domain.Arrival;
import com.conx.logistics.app.whse.rcv.rcv.domain.ArrivalReceipt;
import com.conx.logistics.app.whse.rcv.rcv.domain.Receive;
import com.conx.logistics.kernel.documentlibrary.remote.services.IRemoteDocumentRepository;
import com.conx.logistics.kernel.metamodel.dao.services.IEntityTypeDAOService;
import com.conx.logistics.mdm.domain.documentlibrary.DocType;
import com.conx.logistics.mdm.domain.documentlibrary.FileEntry;
import com.conx.logistics.mdm.domain.documentlibrary.Folder;
import com.conx.logistics.mdm.domain.metamodel.EntityType;
import com.conx.logistics.mdm.domain.note.Note;
import com.conx.logistics.mdm.domain.note.NoteItem;
import com.conx.logistics.mdm.domain.note.NoteType;

/**
 * Implementation of {@link Arrival} that uses JPA for persistence.
 * <p />
 * <p/>
 * This class is marked as {@link Transactional}. The Spring configuration for
 * this module, enables AspectJ weaving for adding transaction demarcation to
 * classes annotated with <code>@Transactional</code>.
 */
@Transactional
@Repository
public class ArrivalDAOImpl implements IArrivalDAOService {
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

	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public Arrival get(long id) {
		return em.getReference(Arrival.class, id);
	}

	@Override
	public List<Arrival> getAll() {
		return em.createQuery("select o from com.conx.logistics.app.whse.rcv.rcv.domain.Arrival o", Arrival.class).getResultList();
	}

	private void assignCode(Arrival arrival) {
		String format = String.format("%%0%dd", 6);
		String paddedId = String.format(format, arrival.getId());
		String code = "AVL" + paddedId;
		arrival.setName(code);
		arrival.setCode(code);
	}

	private void assignCode(Arrival arrival, Receive parentReceive) {
		if (parentReceive == null || parentReceive.getName() == null) {
			assignCode(arrival);
		} else {
			String format = String.format("%%0%dd", 3);
			String paddedId = String.format(format, arrival.getId());
			String code = parentReceive.getName() + "-AVL" + String.valueOf(paddedId);
			arrival.setName(code);
			arrival.setCode(code);
		}
	}

	@Override
	public Arrival add(Arrival arvl, Receive parentReceive) throws Exception {
		arvl.setReceive(parentReceive);
		arvl = em.merge(arvl);
		assignCode(arvl, parentReceive);

		// Doc Folder
		EntityType et = entityTypeDAOService.provide(Arrival.class);
		Folder fldr = documentRepositoryService.provideFolderForEntity(et, arvl.getId());
		fldr = em.merge(fldr);
		arvl.setDocFolder(fldr);

		// Note
		Note note = new Note();
		note.setCode(arvl.getCode());
		note = em.merge(note);
		arvl.setNote(note);

		arvl = em.merge(arvl);
		em.flush();
		return arvl;
	}

	@Override
	public void delete(Arrival record) {
		em.remove(record);
	}

	@Override
	public Arrival update(Arrival record) {
		return em.merge(record);
	}

	public ArrivalReceipt addArrivalReceipt(Long arrivalId, ArrivalReceipt receipt) throws Exception {
		Arrival arrv = null;
		try {
			arrv = em.getReference(Arrival.class, arrivalId);
			receipt.setParentArrival(arrv);
			receipt.setOwnerEntityId(arrivalId);
			receipt = em.merge(receipt);
			arrv.getReceipts().add(receipt);
			arrv = update(arrv);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);

			throw e;
		}

		return receipt;
	}

	@Override
	public FileEntry addAttachment(Long arvlId, String sourceFileName, String title, String description, String mimeType,
			DocType attachmentType) throws Exception {
		Arrival arvl = get(arvlId);
		FileEntry fe = documentRepositoryService.addorUpdateFileEntry(arvl, attachmentType, sourceFileName, mimeType, title, description);
		arvl = update(arvl);

		return fe;
	}

	@Override
	public FileEntry addAttachment(Long arvlId, File sourceFile, String title, String description, String mimeType, DocType attachmentType)
			throws Exception {
		Arrival arvl = get(arvlId);
		FileEntry fe = documentRepositoryService.addorUpdateFileEntry(arvl, attachmentType, sourceFile, mimeType, title, description);
		arvl = update(arvl);

		return fe;
	}

	@Override
	public NoteItem addNoteItem(Long arvlId, String content, NoteType noteType) throws Exception {
		Arrival arvl = get(arvlId);
		NoteItem ni = new NoteItem();
		ni.setParentNote(arvl.getNote());
		ni.setNoteType(noteType);
		ni.setContent(content);
		ni = em.merge(ni);

		arvl.getNote().getNotes().add(ni);

		arvl = em.merge(arvl);

		return ni;
	}
}
