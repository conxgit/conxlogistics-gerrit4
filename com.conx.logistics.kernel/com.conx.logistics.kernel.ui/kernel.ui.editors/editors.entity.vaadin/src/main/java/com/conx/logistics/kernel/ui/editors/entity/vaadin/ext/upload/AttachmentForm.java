package com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.upload;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.conx.logistics.kernel.documentlibrary.remote.services.IRemoteDocumentRepository;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.attachment.AttachmentFormReceiver;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.lineeditor.form.VaadinFormHeader;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.AbstractEntityEditorEventBus;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.mvp.attachment.AttachmentEditorEventBus;
import com.conx.logistics.mdm.dao.services.documentlibrary.IFolderDAOService;
import com.conx.logistics.mdm.domain.documentlibrary.DocType;
import com.conx.logistics.mdm.domain.documentlibrary.FileEntry;
import com.conx.logistics.mdm.domain.documentlibrary.Folder;
import com.vaadin.data.Property;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

@SuppressWarnings("unused")
public class AttachmentForm extends Form {
	private static final long serialVersionUID = -7906217975800435620L;

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private VerticalLayout layout;
	private GridLayout innerLayout;
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
	private Label fileNameField;
	private Label fileTypeField;
	private VaadinFormHeader header;
	private Label uploadStateField;
	private FormMode mode;
	private TextArea fileDescription;
	private VerticalSplitPanel parent;
	private Field attachedField;
	
	public AttachmentForm(AttachmentEditorEventBus attachmentEditorEventBus,
			AbstractEntityEditorEventBus abstractEntityEditorEventBus,
			EntityManager em, IRemoteDocumentRepository docRepo,
			IFolderDAOService docFolderDAOService, VerticalSplitPanel parent) {
		this.docRepo = docRepo;
		this.docFolderDAOService = docFolderDAOService;
		this.em = em;
		this.abstractEntityEditorEventBus = abstractEntityEditorEventBus;
		this.attachmentEditorEventBus = attachmentEditorEventBus;
		this.parent = parent;
		this.attachedField = null;

		innerLayout = new GridLayout(4, 4);
		innerLayout.setWidth("100%");
		innerLayout.setSpacing(true);
		innerLayout.setMargin(true);
		innerLayout.setHeight(-1, UNITS_PIXELS);

		layout = new VerticalLayout();
		layout.setWidth("100%");
		layout.setStyleName("conx-entity-editor-form");

		setLayout(layout);
		setSizeFull();
		setWriteThrough(false);
		initialize();
	}

	private void initialize() {
		header = new VaadinFormHeader();
		header.setAction("Editing");
		header.setTitle("Attachment");

		fileNameField = new Label();
		fileNameField.setStyleName("conx-entity-editor-readonly-field");
		fileNameField.setCaption("File");
		fileNameField.setHeight("24px");
		fileNameField.setContentMode(Label.CONTENT_XHTML);
		fileNameField.setValue("<i>No file uploaded</i>");

		fileTypeField = new Label();
		fileTypeField.setStyleName("conx-entity-editor-readonly-field");
		fileTypeField.setCaption("File Type");
		fileTypeField.setWidth("100%");
		fileTypeField.setHeight("24px");
		fileTypeField.setContentMode(Label.CONTENT_XHTML);
		fileTypeField.setValue("<i>No file uploaded</i>");

		uploadStateField = new Label();
		uploadStateField.setStyleName("conx-entity-editor-readonly-field");
		uploadStateField.setCaption("Upload State");
		uploadStateField.setWidth("100%");
		uploadStateField.setHeight("24px");
		uploadStateField.setContentMode(Label.CONTENT_XHTML);
		uploadStateField.setValue("<i>Idle</i>");

		Upload upload = new Upload();
		upload.setImmediate(true);
		upload.setButtonCaption("...");
		upload.addStyleName("small");
		upload.setReceiver(new AttachmentFormReceiver(this));
		upload.addListener(new Upload.StartedListener() {
			private static final long serialVersionUID = 6192550643127666044L;

			public void uploadStarted(StartedEvent event) {
				uploadStateField.setValue("Started");
				fileNameField.setValue(event.getFilename());
				AttachmentForm.this.title = event.getFilename();
			}
		});
		upload.addListener(new Upload.ProgressListener() {
			private static final long serialVersionUID = 1830359180657976775L;

			public void updateProgress(long readBytes, long contentLength) {
				uploadStateField.setValue(new Float((readBytes / (float) contentLength) * 100) + "%");
			}

		});
		upload.addListener(new Upload.FinishedListener() {
			private static final long serialVersionUID = 982087759549669305L;

			public void uploadFinished(FinishedEvent event) {
				AttachmentForm.this.sourceFileName = event.getFilename();
				AttachmentForm.this.mimeType = event.getMIMEType();
				fileTypeField.setValue(event.getMIMEType());
				uploadStateField.setValue("Uploaded");
			}
		});

		HorizontalLayout fileUploadLayout = new HorizontalLayout();
		fileUploadLayout.setSpacing(true);
		fileUploadLayout.addComponent(fileNameField);
		fileUploadLayout.addComponent(upload);
		fileUploadLayout.setWidth("100%");
		fileUploadLayout.setExpandRatio(fileNameField, 1.0f);
		fileUploadLayout.setComponentAlignment(upload, Alignment.BOTTOM_LEFT);

		fileDescription = new TextArea();
		fileDescription.setCaption("Description");
		fileDescription.setRows(3);
		fileDescription.setColumns(0);
		fileDescription.setWidth("100%");
		fileDescription.setHeight("100%");

		this.uploadToDocRepoButton = new Button();
		this.uploadToDocRepoButton.addListener(new ClickListener() {
			private static final long serialVersionUID = 5272247497155767335L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (mode.equals(FormMode.CREATING)) {
					AttachmentForm.this.description = (String) AttachmentForm.this.fileDescription.getValue();
					AttachmentForm.this.folderId = Long.toString(AttachmentForm.this.docFolder.getFolderId());
					attachedField.commit();
					DocType docTypeValue = (DocType) AttachmentForm.this
							.getItemDataSource().getItemProperty("docType")
							.getValue();
					FileEntry fileEntry = null;
					try {
						fileEntry = AttachmentForm.this.docRepo
								.addorUpdateFileEntry(
										AttachmentForm.this.docFolder,
										docTypeValue,
										AttachmentForm.this.tempSourceFileName,
										AttachmentForm.this.mimeType,
										AttachmentForm.this.sourceFileName,
										AttachmentForm.this.title);
						AttachmentForm.this.parent.setSplitPosition(100);
					} catch (Exception e) {
						StringWriter sw = new StringWriter();
						e.printStackTrace(new PrintWriter(sw));
						String stacktrace = sw.toString();
						logger.error(stacktrace);
						throw new RuntimeException("Error uploading doc "
								+ sourceFileName + " to server", e);
					}

					abstractEntityEditorEventBus.entityItemAdded(null);
				} else if (mode.equals(FormMode.EDITING)) {
					AttachmentForm.this.description = (String) AttachmentForm.this.fileDescription.getValue();
					AttachmentForm.this.folderId = Long.toString(AttachmentForm.this.docFolder.getFolderId());
					attachedField.commit();
					DocType docTypeValue = (DocType) AttachmentForm.this
							.getItemDataSource().getItemProperty("docType")
							.getValue();

					FileEntry fileEntry = null;
					try {
						fileEntry = AttachmentForm.this.docRepo
								.addorUpdateFileEntry(
										AttachmentForm.this.docFolder,
										docTypeValue,
										AttachmentForm.this.tempSourceFileName,
										AttachmentForm.this.mimeType,
										AttachmentForm.this.sourceFileName,
										AttachmentForm.this.title);
						AttachmentForm.this.parent.setSplitPosition(100);
					} catch (Exception e) {
						StringWriter sw = new StringWriter();
						e.printStackTrace(new PrintWriter(sw));
						String stacktrace = sw.toString();
						logger.error(stacktrace);
						throw new RuntimeException("Error uploading doc "
								+ sourceFileName + " to server", e);
					}

					abstractEntityEditorEventBus.entityItemAdded(null);
				}
			}
		});

		header.addExtraComponent(uploadToDocRepoButton);

		innerLayout.addComponent(fileUploadLayout, 0, 0, 0, 0);
		innerLayout.addComponent(fileTypeField, 0, 1, 0, 1);
		innerLayout.addComponent(uploadStateField, 1, 0, 1, 0);
		innerLayout.addComponent(fileDescription, 2, 0, 3, 1);

		setMode(FormMode.CREATING);
		layout.addComponent(header);
		layout.addComponent(innerLayout);
	}

	@Override
	protected void attachField(Object propertyId, com.vaadin.ui.Field field) {
		if (attachedField == null) {
			field.setWidth("100%");
			innerLayout.addComponent(field, 1, 1, 1, 1);
			attachedField = field;
		}
	}

	public void setMode(FormMode mode) {
		this.mode = mode;
		switch (mode) {
		case CREATING:
			this.header.setAction("Creating");
			this.uploadToDocRepoButton.setCaption("Upload");
			fileTypeField.setValue("<i>No file uploaded</i>");
			fileNameField.setValue("<i>No file uploaded</i>");
			uploadStateField.setValue("<i>Idle</i>");
			break;
		case EDITING:
			this.header.setAction("Editing");
			this.uploadToDocRepoButton.setCaption("Update");
			break;
		}
	}

	private void updateStaticFields(com.vaadin.data.Item newDataSource) {
		attachedField = null;
		innerLayout.removeComponent(1, 1);
		uploadStateField.setValue("Uploaded");
		Property description = newDataSource.getItemProperty("description");
		Property mimeType = newDataSource.getItemProperty("mimeType");
		Property title = newDataSource.getItemProperty("title");
		if (description != null && description.getValue() != null) {
			fileDescription.setValue(description.getValue());
		}
		if (mimeType != null && mimeType.getValue() != null) {
			fileTypeField.setValue(mimeType.getValue());
		}
		if (title != null && title.getValue() != null) {
			fileNameField.setValue(title.getValue());
			header.setTitle("Attachment (" + title.getValue() + ")");
		}
	}

	@Override
	public void setItemDataSource(com.vaadin.data.Item newDataSource, Collection<?> propertyIds) {
		updateStaticFields(newDataSource);
		super.setItemDataSource(newDataSource, propertyIds);
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

	public void setTempSourceFile(String absolutePath) {
		this.tempSourceFileName = absolutePath;
	}

	public enum FormMode {
		EDITING,
		CREATING
	}
}
