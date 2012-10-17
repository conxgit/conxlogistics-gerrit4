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
import com.conx.logistics.kernel.ui.components.domain.page.TaskPage;
import com.conx.logistics.mdm.domain.documentlibrary.FileEntry;
import com.conx.logistics.mdm.domain.metamodel.EntityType;

public class ConfirmTruckInfoPage extends BasePageFlowPage implements IModelDrivenPageFlowPage {
	private TaskPage componentModel;
	
	@Override
	public String getTaskName() {
		return "Confirm Truck Info";
	}

	@Override
	public TaskPage getComponentModel() {
		if (componentModel == null) {
			DataSourceField expectedDsField = null;
			DataSourceField actualDsField = null;
			ConfirmActualsFieldSetField fieldSetField = null;
			ConfirmActualsFieldSet fieldSet = null;
			
			EntityType type = new EntityType("Arrival", Arrival.class, null, null, null, "wharrival");
			DataSource ds = new DataSource("searchgriddatasource", type);
			
			CollapsibleConfirmActualsForm truckInfoForm = new CollapsibleConfirmActualsForm(ds, "Confirm Truck Info");
			
			fieldSet = new ConfirmActualsFieldSet();
			fieldSet.setCaption("Driver Info");
			
			expectedDsField = new DataSourceField("receive", ds, ds, type, "expectedDriverId", null);
			expectedDsField.setValueXPath("expectedPickUp/driverId");
			ds.getDSFields().add(expectedDsField);
			actualDsField = new DataSourceField("actualPickUp", ds, ds, type, "actualDriverId", null);
			actualDsField.setValueXPath("actualDriverId");
			ds.getDSFields().add(actualDsField);
			fieldSetField = new ConfirmActualsFieldSetField(0, expectedDsField, actualDsField);
			fieldSetField.setCaption("Driver Id");
			fieldSet.getFields().add(fieldSetField);
			fieldSetField.setFieldSet(fieldSet);
			
			expectedDsField = new DataSourceField("receive", ds, ds, type, "expectedVehicleId", null);
			expectedDsField.setValueXPath("expectedPickUp/vehicleId");
			ds.getDSFields().add(expectedDsField);
			actualDsField = new DataSourceField("actualPickUp", ds, ds, type, "actualVehicleId", null);
			actualDsField.setValueXPath("actualVehicleId");
			ds.getDSFields().add(actualDsField);
			fieldSetField = new ConfirmActualsFieldSetField(0, expectedDsField, actualDsField);
			fieldSetField.setCaption("Vehicle Id");
			fieldSet.getFields().add(fieldSetField);
			fieldSetField.setFieldSet(fieldSet);
			
			expectedDsField = new DataSourceField("receive", ds, ds, type, "expectedBolNum", null);
			expectedDsField.setValueXPath("expectedPickUp/bolNumber");
			ds.getDSFields().add(expectedDsField);
			actualDsField = new DataSourceField("actualPickUp", ds, ds, type, "actualBolNum", null);
			actualDsField.setValueXPath("actualBolNumber");
			ds.getDSFields().add(actualDsField);
			fieldSetField = new ConfirmActualsFieldSetField(0, expectedDsField, actualDsField);
			fieldSetField.setCaption("Bol #");
			fieldSet.getFields().add(fieldSetField);
			fieldSetField.setFieldSet(fieldSet);
			
			expectedDsField = new DataSourceField("receive", ds, ds, type, "expectedSealNum", null);
			expectedDsField.setValueXPath("expectedPickUp/sealNumber");
			ds.getDSFields().add(expectedDsField);
			actualDsField = new DataSourceField("actualPickUp", ds, ds, type, "actualSealNum", null);
			actualDsField.setValueXPath("actualSealNumber");
			ds.getDSFields().add(actualDsField);
			fieldSetField = new ConfirmActualsFieldSetField(0, expectedDsField, actualDsField);
			fieldSetField.setCaption("Seal #");
			fieldSet.getFields().add(fieldSetField);
			fieldSetField.setFieldSet(fieldSet);
			
			truckInfoForm.getFieldSetSet().add(fieldSet);
			fieldSet.setForm(truckInfoForm);
			
			fieldSet = new ConfirmActualsFieldSet();
			fieldSet.setCaption("Contact & Address");
			
			expectedDsField = new DataSourceField("receive", ds, ds, type, "expectedCfs", null);
			expectedDsField.setValueXPath("expectedPickUp/cfs");
			ds.getDSFields().add(expectedDsField);
			DataSourceField actualOrgDsField = new DataSourceField("actualPickUp", ds, ds, type, "actualCfs", null);
			actualOrgDsField.setValueXPath("actualCfs");
			ds.getDSFields().add(actualOrgDsField);
			fieldSetField = new ConfirmActualsFieldSetField(0, expectedDsField, actualOrgDsField);
			fieldSetField.setCaption("Organization");
			fieldSet.getFields().add(fieldSetField);
			fieldSetField.setFieldSet(fieldSet);
			
			expectedDsField = new DataSourceField("receive", ds, ds, type, "expectedCfsAddress", null);
			expectedDsField.setValueXPath("expectedPickUp/cfsAddress");
			ds.getDSFields().add(expectedDsField);
			DataSourceField actualAddressDsField = new DataSourceField("actualPickUp", ds, ds, type, "actualCfsAddress", null);
			actualAddressDsField.setValueXPath("actualCfsAddress");
			actualAddressDsField.setParentDataSourceField(actualOrgDsField);
			actualOrgDsField.getChildDataSourceFields().add(actualAddressDsField);
			ds.getDSFields().add(actualAddressDsField);
			fieldSetField = new ConfirmActualsFieldSetField(0, expectedDsField, actualAddressDsField);
			fieldSetField.setCaption("Address");
			fieldSet.getFields().add(fieldSetField);
			fieldSetField.setFieldSet(fieldSet);
			
			expectedDsField = new DataSourceField("receive", ds, ds, type, "expectedCfsContact", null);
			expectedDsField.setValueXPath("expectedPickUp/cfsContact");
			ds.getDSFields().add(expectedDsField);
			DataSourceField actualContactDsField = new DataSourceField("actualPickUp", ds, ds, type, "actualCfsContact", null);
			actualContactDsField.setValueXPath("actualCfsContact");
			actualContactDsField.setParentDataSourceField(actualOrgDsField);
			actualOrgDsField.getChildDataSourceFields().add(actualContactDsField);
			ds.getDSFields().add(actualContactDsField);
			fieldSetField = new ConfirmActualsFieldSetField(0, expectedDsField, actualContactDsField);
			fieldSetField.setCaption("Contact");
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
			lineEditorContainer.getLineEditors().add(truckInfoAttachmentLineEditor);
			
			MasterDetailComponent masterDetailComponent = new MasterDetailComponent("confirmtruckinfoformmasterdetail", "Confirm Truck Info Form Master Detail Component");
			masterDetailComponent.setMasterComponent(truckInfoForm);
			masterDetailComponent.setLineEditorPanel(lineEditorContainer);
			
			componentModel = new TaskPage(masterDetailComponent);
		}
		return componentModel;
	}

	@Override
	public Class<?> getType() {
		return IModelDrivenPageFlowPage.class;
	}
	
	@Override
	public Map<Class<?>, String> getResultKeyMap() {
		if (this.resultKeyMap == null) {
			this.resultKeyMap = new HashMap<Class<?>, String>();
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
