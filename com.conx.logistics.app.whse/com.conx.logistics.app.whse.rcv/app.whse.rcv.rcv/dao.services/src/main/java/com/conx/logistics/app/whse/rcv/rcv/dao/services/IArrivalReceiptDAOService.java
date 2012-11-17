package com.conx.logistics.app.whse.rcv.rcv.dao.services;

import java.io.File;
import java.util.List;

import com.conx.logistics.app.whse.rcv.rcv.domain.Arrival;
import com.conx.logistics.app.whse.rcv.rcv.domain.ArrivalReceipt;
import com.conx.logistics.app.whse.rcv.rcv.domain.ArrivalReceiptLine;
import com.conx.logistics.mdm.domain.documentlibrary.DocType;
import com.conx.logistics.mdm.domain.documentlibrary.FileEntry;
import com.conx.logistics.mdm.domain.note.NoteItem;
import com.conx.logistics.mdm.domain.note.NoteType;

public interface IArrivalReceiptDAOService {
	public List<Arrival> getAll();

	public ArrivalReceiptLine addArrivalReceiptLine(Long arrivalReceiptId, ArrivalReceiptLine arrivalReceptLine) throws Exception;
	
	public ArrivalReceiptLine updateArrivalReceiptLine(ArrivalReceiptLine arrivalReceptLine);
	
	public FileEntry addAttachment(Long arvlId, File sourceFile, String title, String description, String mimeType, DocType attachmentType) throws Exception;
	
	public FileEntry addAttachment(Long arvlId, String sourceFileName, String title, String description, String mimeType, DocType attachmentType) throws Exception;
	
	public NoteItem addNoteItem(Long arvlId, String content, NoteType noteType) throws Exception;	
	
	public void delete(ArrivalReceipt record);

	public ArrivalReceipt update(ArrivalReceipt record);

	public ArrivalReceipt get(long id);

	public ArrivalReceipt add(Long parentArrivalId, ArrivalReceipt newRecord) throws Exception;
}
