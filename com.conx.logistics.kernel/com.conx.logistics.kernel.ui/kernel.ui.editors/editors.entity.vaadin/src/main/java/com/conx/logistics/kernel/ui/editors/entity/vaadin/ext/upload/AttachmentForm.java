package com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.conx.logistics.common.utils.StringUtil;
import com.conx.logistics.kernel.documentlibrary.remote.services.IRemoteDocumentRepository;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.AbstractEntityEditorEventBus;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.ConfigurableBasePresenter;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.attachment.AttachmentEditorEventBus;
import com.conx.logistics.mdm.dao.services.documentlibrary.IFolderDAOService;
import com.conx.logistics.mdm.domain.documentlibrary.DocType;
import com.conx.logistics.mdm.domain.documentlibrary.FileEntry;
import com.conx.logistics.mdm.domain.documentlibrary.Folder;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Form;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;

public class AttachmentForm extends Form {
	private static final long serialVersionUID = -7906217975800435620L;
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private GridLayout layout;

	private IRemoteDocumentRepository docRepo;

	private Folder docFolder;

	protected String sourceFileName;

	protected String mimeType;

	protected String title;

	protected String description;

	protected String folderId;

	protected String tempSourceFileName;

	private IFolderDAOService docFolderDAOService;

	private EntityManager em;

	private AbstractEntityEditorEventBus abstractEntityEditorEventBus;

	private AttachmentEditorEventBus attachmentEditorEventBus;

	private Button uploadToDocRepoButton;
	
	public AttachmentForm(
			AttachmentEditorEventBus attachmentEditorEventBus, 
			AbstractEntityEditorEventBus abstractEntityEditorEventBus, EntityManager em,
			IRemoteDocumentRepository docRepo, 
			IFolderDAOService docFolderDAOService) {
		this.docRepo = docRepo;
		this.docFolderDAOService = docFolderDAOService;
		this.em = em;
		this.abstractEntityEditorEventBus = abstractEntityEditorEventBus;
		this.attachmentEditorEventBus = attachmentEditorEventBus;
		
		layout = new GridLayout(4, 3);
		layout.setWidth("100%");
		layout.setMargin(true);
		layout.setSpacing(true);
		
		setLayout(layout);
		setSizeFull();
		initialize();
	}

	private void initialize() {
		final Label stateField = new Label();
		stateField.setCaption("Upload State");
		stateField.setValue("Idle");
		
//		final ComboBox fileType = new ComboBox();
//		fileType.setCaption("Attachment Type");
//		fileType.setInputPrompt("Select a file type");
//		fileType.setNullSelectionAllowed(false);
//		fileType.setContainerDataSource(JPAContainerFactory.make(DocType.class, em));
		
		final Label fileNameField = new Label();
		fileNameField.setCaption("File");
		fileNameField.setValue("No file uploaded");
		
		final ProgressIndicator progressBar = new ProgressIndicator();
		progressBar.setCaption("Progress");
        progressBar.setVisible(false);
		
		Upload upload = new Upload();
        upload.setImmediate(true);
        upload.setButtonCaption("Browse");
        upload.setReceiver(new Receiver() {
			private static final long serialVersionUID = 2055417259914716331L;

			@Override
			public OutputStream receiveUpload(String filename, String mimeType) {
				FileOutputStream fos = null;
				File tempFile = null;
				
				String prefix = null;
				String suffix = null;
				
				String[] fileNameTokens = StringUtil.split(filename,".");
				if (fileNameTokens.length == 1)
				{
					prefix = filename;
				}
				else
				{
					prefix = fileNameTokens[0];
					suffix = "."+fileNameTokens[fileNameTokens.length-1];
				}
				
				try {
					tempFile = File.createTempFile(prefix, suffix);
					tempFile.deleteOnExit();
					fos = new FileOutputStream(tempFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				AttachmentForm.this.tempSourceFileName = tempFile.getAbsolutePath();
				
				return fos;
			}
		});
        upload.addListener(new Upload.StartedListener() {
			private static final long serialVersionUID = 6192550643127666044L;

			public void uploadStarted(StartedEvent event) {
                progressBar.setValue(0f);
                progressBar.setVisible(true);
                progressBar.setPollingInterval(500);
                stateField.setValue("Uploading");
                fileNameField.setValue(event.getFilename());
            }
        });
        upload.addListener(new Upload.ProgressListener() {
			private static final long serialVersionUID = 1830359180657976775L;

			public void updateProgress(long readBytes, long contentLength) {
                progressBar.setValue(new Float(readBytes / (float) contentLength));
            }

        });
        upload.addListener(new Upload.FinishedListener() {
			private static final long serialVersionUID = 982087759549669305L;

			public void uploadFinished(FinishedEvent event) {
				AttachmentForm.this.sourceFileName = event.getFilename();
				AttachmentForm.this.mimeType = event.getMIMEType();
            }
        });
		
		HorizontalLayout uploadLayout = new HorizontalLayout();
		uploadLayout.setSpacing(true);
		uploadLayout.addComponent(fileNameField);
		uploadLayout.addComponent(upload);
		uploadLayout.setComponentAlignment(upload, Alignment.BOTTOM_LEFT);
		
		TextArea description = new TextArea();
		description.setCaption("Description");
		description.setRows(3);
		description.setColumns(0);
		description.setWidth("100%");
		
		this.uploadToDocRepoButton = new Button("Upload");
		this.uploadToDocRepoButton.addListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				AttachmentForm.this.description = (String)AttachmentForm.this.getItemDataSource().getItemProperty("description").getValue();
				AttachmentForm.this.folderId = Long.toString(AttachmentForm.this.docFolder.getFolderId());
				AttachmentForm.this.title = (String)AttachmentForm.this.getItemDataSource().getItemProperty("title").getValue();
				DocType docTypeValue = (DocType)AttachmentForm.this.getItemDataSource().getItemProperty("docType").getValue();
				
				FileEntry fileEntry = null;
				try {
					fileEntry = AttachmentForm.this.docRepo.addorUpdateFileEntry(AttachmentForm.this.docFolder, 
							docTypeValue, 
							AttachmentForm.this.tempSourceFileName, 
							AttachmentForm.this.mimeType, 
							AttachmentForm.this.sourceFileName, 
							AttachmentForm.this.title);
				} catch (Exception e) {
					StringWriter sw = new StringWriter();
					e.printStackTrace(new PrintWriter(sw));
					String stacktrace = sw.toString();
					logger.error(stacktrace);
					throw new RuntimeException("Error uploading doc "+sourceFileName+" to server", e);
				}
				
/*				JPAContainer<FileEntry> entityContainer = JPAContainerFactory.make(FileEntry.class,
						AttachmentForm.this.em);
				entityContainer.applyFilters();
				EntityItem<FileEntry> fileEntryItem = entityContainer.getItem(fileEntry.getId());*/
				
				abstractEntityEditorEventBus.entityItemAdded(null);
				
				//attachmentEditorEventBus.fileEntryAdded(fileEntry);
				
				stateField.setValue("Idle");
                progressBar.setVisible(false);
			}
		});
		
		HorizontalLayout toolStrip = new HorizontalLayout();
		toolStrip.setWidth("100%");
		toolStrip.setHeight("30px");
		toolStrip.addComponent(uploadToDocRepoButton);
        
        layout.addComponent(stateField);
//        layout.addComponent(fileType);
        layout.addComponent(uploadLayout);
        layout.addComponent(description, 0, 1, 1, 1);
        layout.addComponent(progressBar, 2, 0, 3, 0);
        layout.addComponent(toolStrip, 0, 2, 3, 2);
	}

	public IRemoteDocumentRepository getDocRepo() {
		return docRepo;
	}

	public Folder getDocFolder() {
		return docFolder;
	}

	public void setDocFolder(Folder docFolder) {
		this.docFolder = docFolder;
	}
	
}
