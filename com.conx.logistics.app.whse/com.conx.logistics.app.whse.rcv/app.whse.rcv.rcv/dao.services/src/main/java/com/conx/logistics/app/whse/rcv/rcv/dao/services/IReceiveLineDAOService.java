package com.conx.logistics.app.whse.rcv.rcv.dao.services;

import java.util.Collection;
import java.util.List;

import com.conx.logistics.app.whse.rcv.rcv.domain.Arrival;
import com.conx.logistics.app.whse.rcv.rcv.domain.ReceiveLine;
import com.conx.logistics.app.whse.rcv.rcv.domain.ReceiveLineStockItemSet;
import com.conx.logistics.app.whse.rcv.rcv.domain.types.RECEIVELINESTATUS;
import com.conx.logistics.mdm.domain.documentlibrary.DocType;
import com.conx.logistics.mdm.domain.documentlibrary.FileEntry;
import com.conx.logistics.mdm.domain.note.NoteItem;
import com.conx.logistics.mdm.domain.note.NoteType;

public interface IReceiveLineDAOService {
	public List<ReceiveLine> getAll();

	public Arrival attachArrival(ReceiveLine targetRcv) throws ClassNotFoundException, Exception;

	public FileEntry addAttachment(Long rcvLinePK, String sourceFileName, String title, String description, String mimeType, DocType attachmentType) throws Exception;

	public NoteItem addNoteItem(Long rcvId, String content, NoteType noteType) throws Exception;

	public void delete(ReceiveLine record);

	public ReceiveLine update(ReceiveLine record);

	public ReceiveLine get(long id);

	public Double calculateExpectedProductTotalWeightInLbs(ReceiveLine receiveLine);

	public Double calculateExpectedProductTotalVolumeInCf(ReceiveLine receiveLine);

	public List<ReceiveLine> findReceiveLinesByINList(Collection<ReceiveLine> rcvLineList) throws Exception;

	public List<ReceiveLine> findReceiveLinesByCode(String code) throws Exception;

	public List<ReceiveLine> findReceiveLinesByStatusAndReceive(RECEIVELINESTATUS status, Long receivePK) throws Exception;

	public ReceiveLine add(ReceiveLine record, Long receivePK);
	
	public ReceiveLineStockItemSet add(ReceiveLineStockItemSet itemSet, Long receiveLinePK);
	
	public ReceiveLineStockItemSet update(ReceiveLineStockItemSet itemSet);
	
	public ReceiveLineStockItemSet getReceiveLineStockItemSet(Long id);
	
	public void delete(ReceiveLineStockItemSet itemSet);
}
