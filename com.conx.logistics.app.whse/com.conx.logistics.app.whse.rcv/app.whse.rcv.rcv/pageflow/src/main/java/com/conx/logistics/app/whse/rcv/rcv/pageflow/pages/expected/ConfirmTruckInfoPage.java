package com.conx.logistics.app.whse.rcv.rcv.pageflow.pages.expected;

import com.conx.logistics.app.whse.rcv.rcv.domain.Receive;
import com.conx.logistics.kernel.datasource.domain.DataSource;
import com.conx.logistics.kernel.datasource.domain.DataSourceField;
import com.conx.logistics.kernel.metamodel.domain.EntityType;
import com.conx.logistics.kernel.pageflow.services.BasePageFlowPage;
import com.conx.logistics.kernel.pageflow.services.IModelDrivenPageFlowPage;
import com.conx.logistics.kernel.ui.components.domain.attachment.AttachmentEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.form.ConfirmActualsFieldSet;
import com.conx.logistics.kernel.ui.components.domain.form.ConfirmActualsFieldSetField;
import com.conx.logistics.kernel.ui.components.domain.form.ConfirmActualsForm;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.LineEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.LineEditorContainerComponent;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.MasterDetailComponent;
import com.conx.logistics.kernel.ui.components.domain.page.TaskPage;

public class ConfirmTruckInfoPage extends BasePageFlowPage implements IModelDrivenPageFlowPage {
	private TaskPage componentModel;
	
	@Override
	public String getTaskName() {
		return "Confirm Truck Info";
	}

	@Override
	public TaskPage getComponentModel() {
		if (componentModel == null) {
			EntityType type = new EntityType("Receive", Receive.class, null, null, null, "whreceive");
			DataSource ds = new DataSource("searchgriddatasource", type);
			
			DataSourceField expectedDriverLicenceDSField = new DataSourceField("expectedDriverLicenceDSField", ds, ds, type, "expectedDriverLicenceDSField", null);
			DataSourceField actualDriverLicenceDSField = new DataSourceField("actualDriverLicenceDSField", ds, ds, type, "actualDriverLicenceDSField", null);
			ConfirmActualsFieldSetField driverLicenceFieldSetField = new ConfirmActualsFieldSetField(0, expectedDriverLicenceDSField, actualDriverLicenceDSField);
			
			DataSourceField expectedDriverNameDSField = new DataSourceField("expectedDriverNameDSField", ds, ds, type, "expectedDriverNameDSField", null);
			DataSourceField actualDriverNameDSField = new DataSourceField("actualDriverNameDSField", ds, ds, type, "actualDriverNameDSField", null);
			ConfirmActualsFieldSetField driverNameFieldSetField = new ConfirmActualsFieldSetField(0, expectedDriverNameDSField, actualDriverNameDSField);
			
			DataSourceField expectedTruckLicenceNumDSField = new DataSourceField("expectedTruckLicenceNumDSField", ds, ds, type, "expectedTruckLicenceNumDSField", null);
			DataSourceField actualTruckLicenceNumDSField = new DataSourceField("actualTruckLicenceNumDSField", ds, ds, type, "actualTruckLicenceNumDSField", null);
			ConfirmActualsFieldSetField truckLicenceFieldSetField = new ConfirmActualsFieldSetField(0, expectedTruckLicenceNumDSField, actualTruckLicenceNumDSField);
			
			DataSourceField expectedSealNumberDSField = new DataSourceField("expectedSealNumberDSField", ds, ds, type, "expectedSealNumberDSField", null);
			DataSourceField actualSealNumberDSField = new DataSourceField("actualSealNumberDSField", ds, ds, type, "actualSealNumberDSField", null);
			ConfirmActualsFieldSetField sealNumberFieldSetField = new ConfirmActualsFieldSetField(0, expectedSealNumberDSField, actualSealNumberDSField);
			
			ConfirmActualsFieldSet truckInfoFieldSet = new ConfirmActualsFieldSet();
			truckInfoFieldSet.getFields().add(driverLicenceFieldSetField);
			driverLicenceFieldSetField.setFieldSet(truckInfoFieldSet);
			truckInfoFieldSet.getFields().add(driverNameFieldSetField);
			driverNameFieldSetField.setFieldSet(truckInfoFieldSet);
			truckInfoFieldSet.getFields().add(truckLicenceFieldSetField);
			truckLicenceFieldSetField.setFieldSet(truckInfoFieldSet);
			truckInfoFieldSet.getFields().add(sealNumberFieldSetField);
			sealNumberFieldSetField.setFieldSet(truckInfoFieldSet);
			
			ConfirmActualsForm truckInfoForm = new ConfirmActualsForm(ds, "Confirm Truck Info");
			truckInfoForm.setFieldSet(truckInfoFieldSet);
			truckInfoFieldSet.setForm(truckInfoForm);
			
			LineEditorContainerComponent lineEditorContainer = new LineEditorContainerComponent("confirmtruckinfolineeditorcontainer", "Confirm Truck Info Line Editor Container");
			
			AttachmentEditorComponent truckInfoAttachmentEditor = new AttachmentEditorComponent(ds);
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
	
}
