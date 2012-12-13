package com.conx.logistics.app.whse.rcv.asn.pageflow.pages;

import java.util.HashMap;
import java.util.Map;

import com.conx.logistics.app.whse.rcv.asn.domain.ASN;
import com.conx.logistics.app.whse.rcv.asn.domain.ASNLine;
import com.conx.logistics.kernel.datasource.domain.DataSource;
import com.conx.logistics.kernel.datasource.domain.DataSourceField;
import com.conx.logistics.kernel.pageflow.services.BasePageFlowPage;
import com.conx.logistics.kernel.pageflow.services.IModelDrivenPageFlowPage;
import com.conx.logistics.kernel.ui.components.domain.attachment.AttachmentEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.editor.MultiLevelEntityEditor;
import com.conx.logistics.kernel.ui.components.domain.form.ConXCollapseableSectionForm;
import com.conx.logistics.kernel.ui.components.domain.form.ConXForm;
import com.conx.logistics.kernel.ui.components.domain.form.FieldSet;
import com.conx.logistics.kernel.ui.components.domain.form.FieldSetField;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.LineEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.LineEditorContainerComponent;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.MasterDetailComponent;
import com.conx.logistics.kernel.ui.components.domain.note.NoteEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.page.TaskPage;
import com.conx.logistics.kernel.ui.components.domain.referencenumber.ReferenceNumberEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.table.ConXTable;
import com.conx.logistics.mdm.domain.documentlibrary.FileEntry;
import com.conx.logistics.mdm.domain.metamodel.EntityType;
import com.conx.logistics.mdm.domain.note.NoteItem;
import com.conx.logistics.mdm.domain.referencenumber.ReferenceNumber;

public class ConfirmAsnPage extends BasePageFlowPage implements IModelDrivenPageFlowPage {
	private TaskPage componentModel;
	
	@Override
	public Map<Class<?>, String> getParamKeyMap() {
		if (this.paramKeyMap == null) {
			this.paramKeyMap = new HashMap<Class<?>, String>();
			this.paramKeyMap.put(ASN.class, "asnIn");
		}
		return this.paramKeyMap;
	}

	@Override
	public Map<Class<?>, String> getResultKeyMap() {
		if (this.resultKeyMap == null) {
			this.resultKeyMap = new HashMap<Class<?>, String>();
			this.resultKeyMap.put(ASN.class, "asnOut");
		}
		return this.resultKeyMap;
	}

	@Override
	public String getTaskName() {
		return "Confirm ASN";
	}

	@Override
	public Class<?> getType() {
		return IModelDrivenPageFlowPage.class;
	}

	private AttachmentEditorComponent buildAttachmentsEditor() {
		EntityType fileEntryType = new EntityType("File Entry", FileEntry.class, null, null, null, "sysdlfileentry");
		DataSource fileEntryDs = new DataSource("fileEntryDS", fileEntryType);
		DataSourceField fileEntryDsField = new DataSourceField("title", fileEntryDs, fileEntryDs, fileEntryType, "title", null);
		fileEntryDsField.setHidden(false);
		fileEntryDs.getDSFields().add(fileEntryDsField);
		fileEntryDsField = new DataSourceField("createDate", fileEntryDs, fileEntryDs, fileEntryType, "createDate", null);
		fileEntryDsField.setHidden(false);
		fileEntryDs.getDSFields().add(fileEntryDsField);
		fileEntryDsField = new DataSourceField("id", fileEntryDs, fileEntryDs, fileEntryType, "id", null);
		fileEntryDsField.setHidden(false);
		fileEntryDs.getDSFields().add(fileEntryDsField);
		fileEntryDsField = new DataSourceField("modifiedDate", fileEntryDs, fileEntryDs, fileEntryType, "modifiedDate", null);
		fileEntryDsField.setHidden(false);
		fileEntryDs.getDSFields().add(fileEntryDsField);
		fileEntryDsField = new DataSourceField("mimeType", fileEntryDs, fileEntryDs, fileEntryType, "mimeType", null);
		fileEntryDsField.setHidden(false);
		fileEntryDs.getDSFields().add(fileEntryDsField);
		fileEntryDsField = new DataSourceField("size", fileEntryDs, fileEntryDs, fileEntryType, "size", null);
		fileEntryDsField.setHidden(false);
		fileEntryDs.getDSFields().add(fileEntryDsField);

		return new AttachmentEditorComponent(fileEntryDs);
	}

	private ReferenceNumberEditorComponent buildReferenceNumbersEditor() {
		EntityType referenceNumberType = new EntityType("Reference Number", ReferenceNumber.class, null, null, null, "mdmreferencenumber");
		DataSource referenceNumberDs = new DataSource("referenceNumberDS", referenceNumberType);
		DataSourceField referenceNumberDsField = new DataSourceField("name", referenceNumberDs, referenceNumberDs, referenceNumberType, "Name", null);
		referenceNumberDsField.setHidden(false);
		referenceNumberDs.getDSFields().add(referenceNumberDsField);
		referenceNumberDsField = new DataSourceField("dateCreated", referenceNumberDs, referenceNumberDs, referenceNumberType, "Date Created", null);
		referenceNumberDsField.setHidden(false);
		referenceNumberDs.getDSFields().add(referenceNumberDsField);
		referenceNumberDsField = new DataSourceField("parentReferenceNumber", referenceNumberDs, referenceNumberDs, referenceNumberType,
				"Parent Reference Number", null);
		referenceNumberDsField.setValueXPath("name");
		referenceNumberDsField.setHidden(false);
		referenceNumberDs.getDSFields().add(referenceNumberDsField);
		referenceNumberDsField = new DataSourceField("value", referenceNumberDs, referenceNumberDs, referenceNumberType, "Value", null);
		referenceNumberDsField.setHidden(false);
		referenceNumberDs.getDSFields().add(referenceNumberDsField);
		referenceNumberDsField = new DataSourceField("type", referenceNumberDs, referenceNumberDs, referenceNumberType, "Type", null);
		referenceNumberDsField.setValueXPath("name");
		referenceNumberDsField.setHidden(false);
		referenceNumberDs.getDSFields().add(referenceNumberDsField);

		return new ReferenceNumberEditorComponent(referenceNumberDs);
	}

	private NoteEditorComponent buildNotesEditor() {
		EntityType noteType = new EntityType("Note Item", NoteItem.class, null, null, null, "mdmnoteitem");
		DataSource noteDs = new DataSource("noteDS", noteType);
		DataSourceField noteDsField = new DataSourceField("name", noteDs, noteDs, noteType, "Name", null);
		noteDsField.setHidden(false);
		noteDs.getDSFields().add(noteDsField);
		noteDsField = new DataSourceField("dateCreated", noteDs, noteDs, noteType, "Date Created", null);
		noteDsField.setHidden(false);
		noteDs.getDSFields().add(noteDsField);
		noteDsField = new DataSourceField("noteType", noteDs, noteDs, noteType, "Type", null);
		noteDsField.setValueXPath("name");
		noteDsField.setHidden(false);
		noteDs.getDSFields().add(noteDsField);
		noteDsField = new DataSourceField("content", noteDs, noteDs, noteType, "Content", null);
		noteDsField.setHidden(false);
		noteDs.getDSFields().add(noteDsField);

		return new NoteEditorComponent(noteDs);
	}

	private ConXTable buildAsnLineGrid(EntityType asnLineEntityType) {
		// Build AsnLine DataSource
		DataSource asnLineGridDataSource = new DataSource("asnlinegriddatasource", asnLineEntityType);
		DataSourceField asnLineGridDataSourceField = new DataSourceField("id", asnLineGridDataSource, asnLineGridDataSource, asnLineEntityType,
				"Name", null);
		asnLineGridDataSourceField.setHidden(false);
		asnLineGridDataSource.getDSFields().add(asnLineGridDataSourceField);
		asnLineGridDataSourceField = new DataSourceField("name", asnLineGridDataSource, asnLineGridDataSource, asnLineEntityType, "Date Created",
				null);
		asnLineGridDataSourceField.setHidden(false);
		asnLineGridDataSource.getDSFields().add(asnLineGridDataSourceField);
		asnLineGridDataSourceField = new DataSourceField("dateCreated", asnLineGridDataSource, asnLineGridDataSource, asnLineEntityType, "Value",
				null);
		asnLineGridDataSourceField.setHidden(false);
		asnLineGridDataSource.getDSFields().add(asnLineGridDataSourceField);
		// Build AsnLine Grid
		ConXTable asnLineGrid = new ConXTable(asnLineGridDataSource);
//		asnLineGrid.setRecordEditor(buildAsnLineMasterDetailComponent());

		return asnLineGrid;
	}

	private LineEditorContainerComponent buildAsnLineEditors(EntityType asnEntityType, EntityType asnLineEntityType) {
		LineEditorContainerComponent asnLineLineEditorContainer = new LineEditorContainerComponent("addasnlinespage-asnlinelineeditorcontainer",
				"addasnlinespage-asnlinelineeditorcontainer");

		LineEditorComponent asnLineGridLineEditor = new LineEditorComponent("addasnlinespage-asnlinegridlineeditor", "ASN Lines",
				asnLineLineEditorContainer);
		asnLineGridLineEditor.setContent(buildAsnLineGrid(asnLineEntityType));
		asnLineGridLineEditor.setOrdinal(0);
		asnLineLineEditorContainer.getLineEditors().add(asnLineGridLineEditor);
		LineEditorComponent attachmentsFormLineEditor = new LineEditorComponent("addasnlinespage-attachmentsasnlineeditor", "Attachments",
				asnLineLineEditorContainer);
		attachmentsFormLineEditor.setContent(buildAttachmentsEditor());
		attachmentsFormLineEditor.setOrdinal(1);
		asnLineLineEditorContainer.getLineEditors().add(attachmentsFormLineEditor);
		LineEditorComponent referenceNumbersFormLineEditor = new LineEditorComponent("addasnlinespage-referencenumbersasnlineeditor",
				"Reference Numbers", asnLineLineEditorContainer);
		referenceNumbersFormLineEditor.setContent(buildReferenceNumbersEditor());
		referenceNumbersFormLineEditor.setOrdinal(2);
		asnLineLineEditorContainer.getLineEditors().add(referenceNumbersFormLineEditor);
		LineEditorComponent notesFormLineEditor = new LineEditorComponent("addasnlinespage-notesasnlineeditor", "Notes", asnLineLineEditorContainer);
		notesFormLineEditor.setContent(buildNotesEditor());
		notesFormLineEditor.setOrdinal(3);
		asnLineLineEditorContainer.getLineEditors().add(notesFormLineEditor);

		return asnLineLineEditorContainer;
	}

	private ConXForm buildAsnForm(EntityType asnEntityType) {
		// Build Asn DataSource
		DataSource asnDataSource = new DataSource("asnlinebasicformdatasource", asnEntityType);
		// Build Asn Basic Form
		ConXCollapseableSectionForm basicForm = new ConXCollapseableSectionForm();
		basicForm.setDataSource(asnDataSource);
		basicForm.setEnabled(false);
		// Build General Section
		FieldSet generalFieldSet = new FieldSet(0);
		generalFieldSet.setCaption("General");
		// Id Field
		DataSourceField idDsField = new DataSourceField("id", asnDataSource, null, "Id");
		asnDataSource.getDSFields().add(idDsField);
		FieldSetField idField = new FieldSetField(0);
		idField.setDataSourceField(idDsField);
		idField.setReadOnly(true);
		idField.setDataSource(asnDataSource);
		generalFieldSet.getFields().add(idField);
		// Name Field
		DataSourceField nameDsField = new DataSourceField("name", asnDataSource, null, "Name");
		asnDataSource.getDSFields().add(nameDsField);
		FieldSetField nameField = new FieldSetField(1);
		nameField.setReadOnly(true);
		nameField.setDataSourceField(nameDsField);
		nameField.setDataSource(asnDataSource);
		generalFieldSet.getFields().add(nameField);
		// Date Created Field
		DataSourceField dateCreatedDsField = new DataSourceField("dateCreated", asnDataSource, null, "Date Created");
		asnDataSource.getDSFields().add(dateCreatedDsField);
		FieldSetField dateCreatedField = new FieldSetField(2);
		dateCreatedField.setReadOnly(true);
		dateCreatedField.setDataSourceField(dateCreatedDsField);
		dateCreatedField.setDataSource(asnDataSource);
		generalFieldSet.getFields().add(dateCreatedField);
		// Date Last Updated Field
		DataSourceField dateLastUpdatedDsField = new DataSourceField("dateLastUpdated", asnDataSource, null, "Date Last Updated");
		asnDataSource.getDSFields().add(dateLastUpdatedDsField);
		FieldSetField dateLastUpdatedField = new FieldSetField(3);
		dateLastUpdatedField.setReadOnly(true);
		dateLastUpdatedField.setDataSourceField(dateLastUpdatedDsField);
		dateLastUpdatedField.setDataSource(asnDataSource);
		generalFieldSet.getFields().add(dateLastUpdatedField);
		// Add General Section to Basic Form
		basicForm.getFieldSetSet().add(generalFieldSet);
		
		// Build Pick Up Section
		FieldSet pickupFieldSet = new FieldSet(0);
		pickupFieldSet.setCaption("Pick-Up");
		// Id Field
		DataSourceField pickupOrgDsField = new DataSourceField("pickup", asnDataSource, null, "Organization");
		pickupOrgDsField.setValueXPath("cfs");
		asnDataSource.getDSFields().add(pickupOrgDsField);
		FieldSetField pickupOrgField = new FieldSetField(0);
		pickupOrgField.setDataSourceField(pickupOrgDsField);
		pickupOrgField.setReadOnly(true);
		pickupOrgField.setDataSource(asnDataSource);
		pickupFieldSet.getFields().add(pickupOrgField);
		// Name Field
		DataSourceField pickupDateDsField = new DataSourceField("pickup", asnDataSource, null, "Pick-Up Date");
		pickupDateDsField.setValueXPath("estimatedPickup");
		asnDataSource.getDSFields().add(pickupDateDsField);
		FieldSetField pickupDateField = new FieldSetField(1);
		pickupDateField.setReadOnly(true);
		pickupDateField.setDataSourceField(pickupDateDsField);
		pickupDateField.setDataSource(asnDataSource);
		pickupFieldSet.getFields().add(pickupDateField);
		// Date Created Field
		DataSourceField pickupAddressDsField = new DataSourceField("pickup", asnDataSource, null, "Pick-Up Address");
		pickupAddressDsField.setValueXPath("cfsAddress");
		asnDataSource.getDSFields().add(pickupAddressDsField);
		FieldSetField pickupAddressField = new FieldSetField(2);
		pickupAddressField.setReadOnly(true);
		pickupAddressField.setDataSourceField(pickupAddressDsField);
		pickupAddressField.setDataSource(asnDataSource);
		pickupFieldSet.getFields().add(pickupAddressField);
		// Add PickUp Section to Basic Form
		basicForm.getFieldSetSet().add(pickupFieldSet);

		return basicForm;
	}

	private MultiLevelEntityEditor buildAsnMLEE() {
		// Build AsnLine EntityType
		EntityType asnEntityType = new EntityType("ASN", ASN.class, null, null, null, "whasn");
		EntityType asnLineEntityType = new EntityType("ASNLine", ASNLine.class, null, null, null, "whasnline");
		// Build AsnLine MasterDetailComponent
		MasterDetailComponent asnLineMasterDetail = new MasterDetailComponent("addasnlinespage-asnmasterdetail", "ASN");
		asnLineMasterDetail.setMasterComponent(buildAsnForm(asnEntityType));
		asnLineMasterDetail.setLineEditorPanel(buildAsnLineEditors(asnEntityType, asnLineEntityType));

		MultiLevelEntityEditor editorComponent = new MultiLevelEntityEditor();
		editorComponent.setContent(asnLineMasterDetail);

		return editorComponent;
	}

	@Override
	public TaskPage getComponentModel() {
		if (componentModel == null) {
			this.componentModel = new TaskPage(buildAsnMLEE());
		}
		return componentModel;
	}
}