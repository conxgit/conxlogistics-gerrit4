package com.conx.logistics.app.whse.rcv.rcv.dao.services;

import java.io.File;
import java.util.List;

import com.conx.logistics.app.whse.domain.warehouse.Warehouse;
import com.conx.logistics.app.whse.rcv.asn.domain.ASN;
import com.conx.logistics.app.whse.rcv.rcv.domain.Arrival;
import com.conx.logistics.app.whse.rcv.rcv.domain.Receive;
import com.conx.logistics.mdm.domain.documentlibrary.DocType;
import com.conx.logistics.mdm.domain.documentlibrary.FileEntry;
import com.conx.logistics.mdm.domain.note.NoteItem;
import com.conx.logistics.mdm.domain.note.NoteType;
import com.conx.logistics.mdm.domain.organization.Organization;
import com.conx.logistics.mdm.domain.product.Product;


public interface IReceiveDAOService {
	public List<Receive> getAll();

	public Receive add(Receive record);
	
	public Receive process(ASN asn) throws ClassNotFoundException, Exception;
	
	public Arrival attachArrival(Receive targetRcv) throws ClassNotFoundException, Exception;
	
	public FileEntry addAttachment(Long rcvId, String sourceFileName, String title, String description, String mimeType, DocType attachmentType) throws Exception;
	
	public FileEntry addAttachment(Long rcvId, File sourceFile, String title, String description, String mimeType, DocType attachmentType) throws Exception;	
	
	public NoteItem addNoteItem(Long rcvId, String content, NoteType noteType) throws Exception;	
	
	public void delete(Receive record);

	public Receive update(Receive record);
	
	public Receive provide(Organization shipper, Organization consignee, Warehouse warehouse);
	
	public Receive provideDefault();

	public Receive get(long id);

	public FileEntry addAttachment(Receive rcv, File sourceFile, String title, String description, String mimeType, DocType attachmentType) throws Exception;
}
