package com.conx.logistics.mdm.domain.application;

import javax.persistence.Entity;
import javax.persistence.Transient;

import com.conx.logistics.mdm.domain.BaseEntity;
import com.conx.logistics.mdm.domain.documentlibrary.FileEntry;

@Entity
public class DocViewFeature extends Feature {
	@Transient 
	private FileEntry fileEntry;
	
	@Transient
	private BaseEntity ownerEntity;
 
    public DocViewFeature()
    {
    }

	public DocViewFeature(FileEntry fileEntry) {
		super();
		this.fileEntry = fileEntry;
		setName(fileEntry.getDocType().getCode()+"/"+fileEntry.getTitle());
	}

	public FileEntry getFileEntry() {
		return fileEntry;
	}

	public void setFileEntry(FileEntry fileEntry) {
		this.fileEntry = fileEntry;
	}
}
