package com.conx.logistics.app.whse.rcv.rcv.dao.services;

import java.io.File;
import java.util.List;
import java.util.Set;

import com.conx.logistics.app.whse.rcv.rcv.domain.Arrival;
import com.conx.logistics.app.whse.rcv.rcv.domain.ArrivalReceipt;
import com.conx.logistics.app.whse.rcv.rcv.domain.Receive;
import com.conx.logistics.mdm.domain.documentlibrary.DocType;
import com.conx.logistics.mdm.domain.documentlibrary.FileEntry;
import com.conx.logistics.mdm.domain.note.NoteItem;
import com.conx.logistics.mdm.domain.note.NoteType;


public interface IArrivalDAOService {
	public List<Arrival> getAll();

	public Arrival add(Arrival arvl, Receive parentReceive) throws Exception;
	
	public Arrival addArrivalReceipt(Long arrivalId, ArrivalReceipt receipt) throws Exception;
	
	public FileEntry addAttachment(Long arvlId, File sourceFile, String title, String description, String mimeType, DocType attachmentType) throws Exception;
	
	public FileEntry addAttachment(Long arvlId, String sourceFileName, String title, String description, String mimeType, DocType attachmentType) throws Exception;
	
	public NoteItem addNoteItem(Long arvlId, String content, NoteType noteType) throws Exception;	
	
	public void delete(Arrival record);

	public Arrival update(Arrival record);

	public Arrival get(long id);

}
