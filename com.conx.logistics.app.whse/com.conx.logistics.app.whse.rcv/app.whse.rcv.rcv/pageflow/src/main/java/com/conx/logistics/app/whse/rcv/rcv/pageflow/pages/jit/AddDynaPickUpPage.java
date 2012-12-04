package com.conx.logistics.app.whse.rcv.rcv.pageflow.pages.jit;

import java.util.HashMap;
import java.util.Map;

import com.conx.logistics.app.whse.rcv.rcv.domain.Arrival;
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

public class AddDynaPickUpPage extends BasePageFlowPage implements IModelDrivenPageFlowPage {

	private TaskPage componentModel;

	@Override
	public String getTaskName() {
		return "Add Pick Up";
	}

	@Override
	public Class<?> getType() {
		return IModelDrivenPageFlowPage.class;
	}
	
	@Override
	public Map<Class<?>, String> getResultKeyMap() {
		if (this.resultKeyMap == null) {
			this.resultKeyMap = new HashMap<Class<?>, String>();
			this.resultKeyMap.put(Arrival.class, "arrivalOut");
		}
		return this.resultKeyMap;
	}

	@Override
	public Map<Class<?>, String> getParamKeyMap() {
		if (this.paramKeyMap == null) {
			this.paramKeyMap = new HashMap<Class<?>, String>();
			this.paramKeyMap.put(Arrival.class, "arrivalIn");
		}
		return this.paramKeyMap;
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
		LineEditorContainerComponent arvlLineLineEditorContainer = new LineEditorContainerComponent(
				"addarvlpickup-arvllinelineeditorcontainer",
				"addarvlpickup-arvllinelineeditorcontainer");

		LineEditorComponent attachmentsFormLineEditor = new LineEditorComponent(
				"addarvlpickup-attachmentsarvllinelineeditor", "Attachments",
				arvlLineLineEditorContainer);
		attachmentsFormLineEditor.setContent(buildAttachmentsEditor());
		attachmentsFormLineEditor.setOrdinal(3);
		arvlLineLineEditorContainer.getLineEditors().add(
				attachmentsFormLineEditor);
		LineEditorComponent referenceNumbersFormLineEditor = new LineEditorComponent(
				"addarvlpickup-referencenumbersarvllinelineeditor",
				"Reference Numbers", arvlLineLineEditorContainer);
		referenceNumbersFormLineEditor
				.setContent(buildReferenceNumbersEditor());
		referenceNumbersFormLineEditor.setOrdinal(4);
		arvlLineLineEditorContainer.getLineEditors().add(
				referenceNumbersFormLineEditor);
		LineEditorComponent notesFormLineEditor = new LineEditorComponent(
				"addarvlpickup-notesarvllinelineeditor", "Notes",
				arvlLineLineEditorContainer);
		notesFormLineEditor.setContent(buildNotesEditor());
		notesFormLineEditor.setOrdinal(5);
		arvlLineLineEditorContainer.getLineEditors().add(notesFormLineEditor);

		return arvlLineLineEditorContainer;
	}

	private ConXForm buildEditorForm() {
		// Build Asn EntityType
		EntityType arvlEntityType = new EntityType("Arrival", Arrival.class, null, null,
				null, "wharrival");
		// Build Asn DataSource
		DataSource arvlDataSource = new DataSource("arvllinebasicformdatasource",
				arvlEntityType);
		// Build Asn Basic Form
		ConXCollapseableSectionForm editorForm = new ConXCollapseableSectionForm();
		editorForm.setDataSource(arvlDataSource);
		// Build Org Section
		FieldSet orgFieldSet = new FieldSet(0);
		orgFieldSet.setCaption("Organization");
		// Org Field
		DataSourceField orgDsField = new DataSourceField("actualPickUp",
				arvlDataSource, null, "Pick Up Organization");
		orgDsField.setValueXPath("actualCfs");
		arvlDataSource.getDSFields().add(orgDsField);
		FieldSetField orgField = new FieldSetField(0);
		orgField.setDataSourceField(orgDsField);
		orgField.setDataSource(arvlDataSource);
		orgFieldSet.getFields().add(orgField);
		// Contact Field
		DataSourceField contactDsField = new DataSourceField("actualPickUp",
				arvlDataSource, null, "Contact");
		contactDsField.setValueXPath("actualCfsContact");
		contactDsField.setParentDataSourceField(orgDsField);
		orgDsField.getChildDataSourceFields().add(contactDsField);
		arvlDataSource.getDSFields().add(contactDsField);
		FieldSetField contactField = new FieldSetField(1);
		contactField.setDataSourceField(contactDsField);
		contactField.setDataSource(arvlDataSource);
		orgFieldSet.getFields().add(contactField);
		// Address Field
		DataSourceField addressDsField = new DataSourceField("actualPickUp",
				arvlDataSource, null, "Address");
		addressDsField.setValueXPath("actualCfsAddress");
		addressDsField.setParentDataSourceField(orgDsField);
		orgDsField.getChildDataSourceFields().add(addressDsField);
		arvlDataSource.getDSFields().add(addressDsField);
		FieldSetField addressField = new FieldSetField(1);
		addressField.setDataSourceField(addressDsField);
		addressField.setDataSource(arvlDataSource);
		orgFieldSet.getFields().add(addressField);
		// Add Org Section to Form
		editorForm.getFieldSetSet().add(orgFieldSet);

		// Build Details Section
		FieldSet detailsFieldSet = new FieldSet(0);
		detailsFieldSet.setCaption("Pick Up Details");

		// Address Field
		DataSourceField pickupAddressDsField = new DataSourceField("actualPickUp",
				arvlDataSource, null, "Pick Up From Address");
		pickupAddressDsField.setValueXPath("actualPickUpFromAddress");
		pickupAddressDsField.setParentDataSourceField(orgDsField);
		orgDsField.getChildDataSourceFields().add(pickupAddressDsField);
		arvlDataSource.getDSFields().add(pickupAddressDsField);
		FieldSetField pickupAddressField = new FieldSetField(1);
		pickupAddressField.setDataSourceField(pickupAddressDsField);
		pickupAddressField.setDataSource(arvlDataSource);
		detailsFieldSet.getFields().add(pickupAddressField);
		// Pick Up Address Field
		DataSourceField dateTimeDsField = new DataSourceField("actualPickUp",
				arvlDataSource, null, "Pick Up Date & Time");
		dateTimeDsField.setValueXPath("actualPickup");
		arvlDataSource.getDSFields().add(dateTimeDsField);
		FieldSetField dateTimeField = new FieldSetField(3);
		dateTimeField.setDataSourceField(dateTimeDsField);
		dateTimeField.setDataSource(arvlDataSource);
		detailsFieldSet.getFields().add(dateTimeField);
		// Add Details Section to Form
		editorForm.getFieldSetSet().add(detailsFieldSet);

		return editorForm;
	}

	private MasterDetailComponent buildEditor() {
		// Build MasterDetailComponent
		MasterDetailComponent arvlLineMasterDetail = new MasterDetailComponent(
				"addarvlpickup-arvlmasterdetail", "Arrival");
		arvlLineMasterDetail.setMasterComponent(buildEditorForm());
		arvlLineMasterDetail.setLineEditorPanel(buildLineEditors());

		return arvlLineMasterDetail;
	}

	@Override
	public TaskPage getComponentModel() {
		if (this.componentModel == null) {
			this.componentModel = new TaskPage(buildEditor());
		}
		return this.componentModel;
	}
}
