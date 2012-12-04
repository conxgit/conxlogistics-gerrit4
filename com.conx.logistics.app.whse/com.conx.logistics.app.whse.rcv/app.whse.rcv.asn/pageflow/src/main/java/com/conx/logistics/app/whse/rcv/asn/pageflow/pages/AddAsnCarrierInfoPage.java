package com.conx.logistics.app.whse.rcv.asn.pageflow.pages;

import java.util.HashMap;
import java.util.Map;

import com.conx.logistics.app.whse.rcv.asn.domain.ASN;
import com.conx.logistics.kernel.datasource.domain.DataSource;
import com.conx.logistics.kernel.datasource.domain.DataSourceField;
import com.conx.logistics.kernel.pageflow.services.BasePageFlowPage;
import com.conx.logistics.kernel.pageflow.services.IModelDrivenPageFlowPage;
import com.conx.logistics.kernel.ui.components.domain.attachment.AttachmentEditorComponent;
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
import com.conx.logistics.mdm.domain.documentlibrary.FileEntry;
import com.conx.logistics.mdm.domain.metamodel.EntityType;
import com.conx.logistics.mdm.domain.note.NoteItem;
import com.conx.logistics.mdm.domain.referencenumber.ReferenceNumber;

public class AddAsnCarrierInfoPage extends BasePageFlowPage implements
		IModelDrivenPageFlowPage {
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
		return "AddAsnCarrierInfoPage";
	}

	@Override
	public Class<?> getType() {
		return IModelDrivenPageFlowPage.class;
	}

	private AttachmentEditorComponent buildAttachmentsEditor() {
		EntityType fileEntryType = new EntityType("File Entry",
				FileEntry.class, null, null, null, "sysdlfileentry");
		DataSource fileEntryDs = new DataSource("fileEntryDS", fileEntryType);
		DataSourceField fileEntryDsField = new DataSourceField("title",
				fileEntryDs, fileEntryDs, fileEntryType, "title", null);
		fileEntryDsField.setHidden(false);
		fileEntryDs.getDSFields().add(fileEntryDsField);
		fileEntryDsField = new DataSourceField("createDate", fileEntryDs,
				fileEntryDs, fileEntryType, "createDate", null);
		fileEntryDsField.setHidden(false);
		fileEntryDs.getDSFields().add(fileEntryDsField);
		fileEntryDsField = new DataSourceField("id", fileEntryDs, fileEntryDs,
				fileEntryType, "id", null);
		fileEntryDsField.setHidden(false);
		fileEntryDs.getDSFields().add(fileEntryDsField);
		fileEntryDsField = new DataSourceField("modifiedDate", fileEntryDs,
				fileEntryDs, fileEntryType, "modifiedDate", null);
		fileEntryDsField.setHidden(false);
		fileEntryDs.getDSFields().add(fileEntryDsField);
		fileEntryDsField = new DataSourceField("mimeType", fileEntryDs,
				fileEntryDs, fileEntryType, "mimeType", null);
		fileEntryDsField.setHidden(false);
		fileEntryDs.getDSFields().add(fileEntryDsField);
		fileEntryDsField = new DataSourceField("size", fileEntryDs,
				fileEntryDs, fileEntryType, "size", null);
		fileEntryDsField.setHidden(false);
		fileEntryDs.getDSFields().add(fileEntryDsField);

		return new AttachmentEditorComponent(fileEntryDs);
	}

	private ReferenceNumberEditorComponent buildReferenceNumbersEditor() {
		EntityType referenceNumberType = new EntityType("Reference Number",
				ReferenceNumber.class, null, null, null, "mdmreferencenumber");
		DataSource referenceNumberDs = new DataSource("referenceNumberDS",
				referenceNumberType);
		DataSourceField referenceNumberDsField = new DataSourceField("name",
				referenceNumberDs, referenceNumberDs, referenceNumberType,
				"Name", null);
		referenceNumberDsField.setHidden(false);
		referenceNumberDs.getDSFields().add(referenceNumberDsField);
		referenceNumberDsField = new DataSourceField("dateCreated",
				referenceNumberDs, referenceNumberDs, referenceNumberType,
				"Date Created", null);
		referenceNumberDsField.setHidden(false);
		referenceNumberDs.getDSFields().add(referenceNumberDsField);
		referenceNumberDsField = new DataSourceField("parentReferenceNumber",
				referenceNumberDs, referenceNumberDs, referenceNumberType,
				"Parent Reference Number", null);
		referenceNumberDsField.setValueXPath("name");
		referenceNumberDsField.setHidden(false);
		referenceNumberDs.getDSFields().add(referenceNumberDsField);
		referenceNumberDsField = new DataSourceField("value",
				referenceNumberDs, referenceNumberDs, referenceNumberType,
				"Value", null);
		referenceNumberDsField.setHidden(false);
		referenceNumberDs.getDSFields().add(referenceNumberDsField);
		referenceNumberDsField = new DataSourceField("type", referenceNumberDs,
				referenceNumberDs, referenceNumberType, "Type", null);
		referenceNumberDsField.setValueXPath("name");
		referenceNumberDsField.setHidden(false);
		referenceNumberDs.getDSFields().add(referenceNumberDsField);

		return new ReferenceNumberEditorComponent(referenceNumberDs);
	}

	private NoteEditorComponent buildNotesEditor() {
		EntityType noteType = new EntityType("Note Item", NoteItem.class, null,
				null, null, "mdmnoteitem");
		DataSource noteDs = new DataSource("noteDS", noteType);
		DataSourceField noteDsField = new DataSourceField("name", noteDs,
				noteDs, noteType, "Name", null);
		noteDsField.setHidden(false);
		noteDs.getDSFields().add(noteDsField);
		noteDsField = new DataSourceField("dateCreated", noteDs, noteDs,
				noteType, "Date Created", null);
		noteDsField.setHidden(false);
		noteDs.getDSFields().add(noteDsField);
		noteDsField = new DataSourceField("noteType", noteDs, noteDs, noteType,
				"Type", null);
		noteDsField.setValueXPath("name");
		noteDsField.setHidden(false);
		noteDs.getDSFields().add(noteDsField);
		noteDsField = new DataSourceField("content", noteDs, noteDs, noteType,
				"Content", null);
		noteDsField.setHidden(false);
		noteDs.getDSFields().add(noteDsField);

		return new NoteEditorComponent(noteDs);
	}

	private LineEditorContainerComponent buildLineEditors() {
		LineEditorContainerComponent asnLineLineEditorContainer = new LineEditorContainerComponent(
				"addasncarrier-asnlinelineeditorcontainer",
				"addasncarrier-asnlinelineeditorcontainer");

		LineEditorComponent attachmentsFormLineEditor = new LineEditorComponent(
				"addasncarrier-attachmentsasnlinelineeditor", "Attachments",
				asnLineLineEditorContainer);
		attachmentsFormLineEditor.setContent(buildAttachmentsEditor());
		attachmentsFormLineEditor.setOrdinal(3);
		asnLineLineEditorContainer.getLineEditors().add(
				attachmentsFormLineEditor);
		LineEditorComponent referenceNumbersFormLineEditor = new LineEditorComponent(
				"addasncarrier-referencenumbersasnlinelineeditor",
				"Reference Numbers", asnLineLineEditorContainer);
		referenceNumbersFormLineEditor
				.setContent(buildReferenceNumbersEditor());
		referenceNumbersFormLineEditor.setOrdinal(4);
		asnLineLineEditorContainer.getLineEditors().add(
				referenceNumbersFormLineEditor);
		LineEditorComponent notesFormLineEditor = new LineEditorComponent(
				"addasncarrier-notesasnlinelineeditor", "Notes",
				asnLineLineEditorContainer);
		notesFormLineEditor.setContent(buildNotesEditor());
		notesFormLineEditor.setOrdinal(5);
		asnLineLineEditorContainer.getLineEditors().add(notesFormLineEditor);

		return asnLineLineEditorContainer;
	}

	private ConXForm buildEditorForm() {
		// Build Asn EntityType
		EntityType asnEntityType = new EntityType("ASN", ASN.class, null, null,
				null, "whasn");
		// Build Asn DataSource
		DataSource asnDataSource = new DataSource("asnlinebasicformdatasource",
				asnEntityType);
		// Build Asn Basic Form
		ConXCollapseableSectionForm editorForm = new ConXCollapseableSectionForm();
		editorForm.setDataSource(asnDataSource);
		// Build Org Section
		FieldSet orgFieldSet = new FieldSet(0);
		orgFieldSet.setCaption("Driver Details");
		// Driver Id Field
		DataSourceField driverIdDsField = new DataSourceField("pickup",
				asnDataSource, null, "Driver Id");
		driverIdDsField.setValueXPath("driverId");
		asnDataSource.getDSFields().add(driverIdDsField);
		FieldSetField driverIdField = new FieldSetField(0);
		driverIdField.setDataSourceField(driverIdDsField);
		driverIdField.setDataSource(asnDataSource);
		orgFieldSet.getFields().add(driverIdField);
		// Vehicle Id Field
		DataSourceField vehicleIdDsField = new DataSourceField("pickup",
				asnDataSource, null, "Vehicle Id");
		vehicleIdDsField.setValueXPath("vehicleId");
		asnDataSource.getDSFields().add(vehicleIdDsField);
		FieldSetField vehicleIdField = new FieldSetField(1);
		vehicleIdField.setDataSourceField(vehicleIdDsField);
		vehicleIdField.setDataSource(asnDataSource);
		orgFieldSet.getFields().add(vehicleIdField);
		// Bol # Field
		DataSourceField bolNumDsField = new DataSourceField("pickup",
				asnDataSource, null, "Bol #");
		bolNumDsField.setValueXPath("bolNumber");
		asnDataSource.getDSFields().add(bolNumDsField);
		FieldSetField bolNumField = new FieldSetField(1);
		bolNumField.setDataSourceField(bolNumDsField);
		bolNumField.setDataSource(asnDataSource);
		orgFieldSet.getFields().add(bolNumField);
		// Seal # Field
		DataSourceField sealNumDsField = new DataSourceField("pickup",
				asnDataSource, null, "Seal #");
		sealNumDsField.setValueXPath("sealNumber");
		asnDataSource.getDSFields().add(sealNumDsField);
		FieldSetField sealNumField = new FieldSetField(1);
		sealNumField.setDataSourceField(sealNumDsField);
		sealNumField.setDataSource(asnDataSource);
		orgFieldSet.getFields().add(sealNumField);
		// Add Org Section to Form
		editorForm.getFieldSetSet().add(orgFieldSet);

		return editorForm;
	}

	private MasterDetailComponent buildEditor() {
		// Build MasterDetailComponent
		MasterDetailComponent asnLineMasterDetail = new MasterDetailComponent(
				"addasncarrier-asnmasterdetail", "ASN");
		asnLineMasterDetail.setMasterComponent(buildEditorForm());
		asnLineMasterDetail.setLineEditorPanel(buildLineEditors());

		return asnLineMasterDetail;
	}

	@Override
	public TaskPage getComponentModel() {
		if (this.componentModel == null) {
			this.componentModel = new TaskPage(buildEditor());
		}
		return this.componentModel;
	}
}