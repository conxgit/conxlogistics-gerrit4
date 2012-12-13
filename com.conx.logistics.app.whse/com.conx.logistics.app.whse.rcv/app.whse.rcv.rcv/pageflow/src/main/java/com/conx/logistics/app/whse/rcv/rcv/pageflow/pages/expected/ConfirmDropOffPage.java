package com.conx.logistics.app.whse.rcv.rcv.pageflow.pages.expected;

import java.util.HashMap;
import java.util.Map;

import com.conx.logistics.app.whse.rcv.rcv.domain.Arrival;
import com.conx.logistics.kernel.datasource.domain.DataSource;
import com.conx.logistics.kernel.datasource.domain.DataSourceField;
import com.conx.logistics.kernel.pageflow.services.BasePageFlowPage;
import com.conx.logistics.kernel.pageflow.services.IModelDrivenPageFlowPage;
import com.conx.logistics.kernel.ui.components.domain.attachment.AttachmentEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.form.CollapsibleConfirmActualsForm;
import com.conx.logistics.kernel.ui.components.domain.form.ConfirmActualsFieldSet;
import com.conx.logistics.kernel.ui.components.domain.form.ConfirmActualsFieldSetField;
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

public class ConfirmDropOffPage extends BasePageFlowPage implements IModelDrivenPageFlowPage {

	private TaskPage componentModel;

	@Override
	public String getTaskName() {
		return "Confirm Drop Off";
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

	@Override
	public TaskPage getComponentModel() {
		if (componentModel == null) {
			DataSourceField expectedDsField = null;
			ConfirmActualsFieldSetField fieldSetField = null;
			ConfirmActualsFieldSet fieldSet = null;
			
			EntityType type = new EntityType("Arrival", Arrival.class, null, null, null, "wharrival");
			DataSource ds = new DataSource("searchgriddatasource", type);
			
			CollapsibleConfirmActualsForm truckInfoForm = new CollapsibleConfirmActualsForm(ds, "Confirm Truck Info");
			
			fieldSet = new ConfirmActualsFieldSet();
			fieldSet.setCaption("Contact & Address");
			
			expectedDsField = new DataSourceField("receive", ds, ds, type, "expectedCfs", null);
			expectedDsField.setValueXPath("expectedDropOff/cfs");
			ds.getDSFields().add(expectedDsField);
			DataSourceField actualOrgDsField = new DataSourceField("actualDropOff", ds, ds, type, "actualCfs", null);
			actualOrgDsField.setValueXPath("actualCfs");
			ds.getDSFields().add(actualOrgDsField);
			fieldSetField = new ConfirmActualsFieldSetField(0, expectedDsField, actualOrgDsField);
			fieldSetField.setCaption("Organization");
			fieldSet.getFields().add(fieldSetField);
			fieldSetField.setFieldSet(fieldSet);
			
			expectedDsField = new DataSourceField("receive", ds, ds, type, "expectedCfsAddress", null);
			expectedDsField.setValueXPath("expectedDropOff/cfsAddress");
			ds.getDSFields().add(expectedDsField);
			DataSourceField actualAddressDsField = new DataSourceField("actualDropOff", ds, ds, type, "actualCfsAddress", null);
			actualAddressDsField.setValueXPath("actualCfsAddress");
			actualAddressDsField.setParentDataSourceField(actualOrgDsField);
			actualOrgDsField.getChildDataSourceFields().add(actualAddressDsField);
			ds.getDSFields().add(actualAddressDsField);
			fieldSetField = new ConfirmActualsFieldSetField(0, expectedDsField, actualAddressDsField);
			fieldSetField.setCaption("Address");
			fieldSet.getFields().add(fieldSetField);
			fieldSetField.setFieldSet(fieldSet);
			
			expectedDsField = new DataSourceField("receive", ds, ds, type, "expectedCfsContact", null);
			expectedDsField.setValueXPath("expectedDropOff/dropMode");
			ds.getDSFields().add(expectedDsField);
			DataSourceField actualContactDsField = new DataSourceField("actualDropOff", ds, ds, type, "actualCfsContact", null);
			actualContactDsField.setValueXPath("actualDropMode");
//			actualContactDsField.setParentDataSourceField(actualOrgDsField);
//			actualOrgDsField.getChildDataSourceFields().add(actualContactDsField);
			ds.getDSFields().add(actualContactDsField);
			fieldSetField = new ConfirmActualsFieldSetField(0, expectedDsField, actualContactDsField);
			fieldSetField.setCaption("Drop Mode");
			fieldSet.getFields().add(fieldSetField);
			fieldSetField.setFieldSet(fieldSet);
			
			truckInfoForm.getFieldSetSet().add(fieldSet);
			fieldSet.setForm(truckInfoForm);
			
			LineEditorContainerComponent lineEditorContainer = new LineEditorContainerComponent("confirmtruckinfolineeditorcontainer", "Confirm Truck Info Line Editor Container");
			AttachmentEditorComponent attachmentComponent = new AttachmentEditorComponent(ds);
			LineEditorComponent lineEditorComponent = new LineEditorComponent("confirmtruckinfolineeditorcomponent", "Attachments", lineEditorContainer);
			lineEditorComponent.setContent(attachmentComponent);
			
			EntityType feType = new EntityType("FileEntry", FileEntry.class, null, null, null, "sysdlfileentry");
			DataSource feDs = new DataSource("fileEntryDS", feType);
			DataSourceField feDsField = new DataSourceField("title", feDs, feDs, feType, "title", null);
			feDsField.setHidden(false);
			feDs.getDSFields().add(feDsField);
			feDsField = new DataSourceField("createDate", feDs, feDs, feType, "createDate", null);
			feDsField.setHidden(false);
			feDs.getDSFields().add(feDsField);
			feDsField = new DataSourceField("id", feDs, feDs, feType, "id", null);
			feDsField.setHidden(false);
			feDs.getDSFields().add(feDsField);
			feDsField = new DataSourceField("modifiedDate", feDs, feDs, feType, "modifiedDate", null);
			feDsField.setHidden(false);
			feDs.getDSFields().add(feDsField);
			feDsField = new DataSourceField("mimeType", feDs, feDs, feType, "mimeType", null);
			feDsField.setHidden(false);
			feDs.getDSFields().add(feDsField);
			feDsField = new DataSourceField("size", feDs, feDs, feType, "size", null);
			feDsField.setHidden(false);
			feDs.getDSFields().add(feDsField);
			
			AttachmentEditorComponent truckInfoAttachmentEditor = new AttachmentEditorComponent(feDs);
			LineEditorComponent truckInfoAttachmentLineEditor = new LineEditorComponent("truckinfoattachmentlineeditor", "Attachments", lineEditorContainer);
			truckInfoAttachmentLineEditor.setContent(truckInfoAttachmentEditor);
			truckInfoAttachmentLineEditor.setOrdinal(3);
			lineEditorContainer.getLineEditors().add(truckInfoAttachmentLineEditor);
			
			LineEditorComponent referenceNumbersFormLineEditor = new LineEditorComponent("addasnpickup-referencenumbersasnlinelineeditor",
					"Reference Numbers", lineEditorContainer);
			referenceNumbersFormLineEditor.setContent(buildReferenceNumbersEditor());
			referenceNumbersFormLineEditor.setOrdinal(4);
			lineEditorContainer.getLineEditors().add(referenceNumbersFormLineEditor);
			LineEditorComponent notesFormLineEditor = new LineEditorComponent("addasnpickup-notesasnlinelineeditor", "Notes",
					lineEditorContainer);
			notesFormLineEditor.setContent(buildNotesEditor());
			notesFormLineEditor.setOrdinal(5);
			lineEditorContainer.getLineEditors().add(notesFormLineEditor);
			
			MasterDetailComponent masterDetailComponent = new MasterDetailComponent("confirmtruckinfoformmasterdetail", "Confirm Truck Info Form Master Detail Component");
			masterDetailComponent.setMasterComponent(truckInfoForm);
			masterDetailComponent.setLineEditorPanel(lineEditorContainer);
			
			componentModel = new TaskPage(masterDetailComponent);
		}
		return this.componentModel;
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
	
}
