package com.conx.logistics.app.whse.rcv.rcv.pageflow.pages.jit;

import java.util.HashMap;
import java.util.Map;

import com.conx.logistics.app.whse.im.domain.stockitem.StockItem;
import com.conx.logistics.app.whse.rcv.rcv.domain.Arrival;
import com.conx.logistics.app.whse.rcv.rcv.domain.ArrivalReceipt;
import com.conx.logistics.app.whse.rcv.rcv.domain.ArrivalReceiptLine;
import com.conx.logistics.app.whse.rcv.rcv.domain.ReceiveLine;
import com.conx.logistics.kernel.datasource.domain.DataSource;
import com.conx.logistics.kernel.datasource.domain.DataSourceField;
import com.conx.logistics.kernel.pageflow.services.BasePageFlowPage;
import com.conx.logistics.kernel.pageflow.services.IModelDrivenPageFlowPage;
import com.conx.logistics.kernel.ui.components.domain.attachment.AttachmentEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.editor.MultiLevelEntityEditor;
import com.conx.logistics.kernel.ui.components.domain.form.CollapsiblePhysicalAttributeConfirmActualsForm;
import com.conx.logistics.kernel.ui.components.domain.form.ConXCollapseableSectionForm;
import com.conx.logistics.kernel.ui.components.domain.form.ConXSimpleForm;
import com.conx.logistics.kernel.ui.components.domain.form.FieldSet;
import com.conx.logistics.kernel.ui.components.domain.form.FieldSetField;
import com.conx.logistics.kernel.ui.components.domain.form.PhysicalAttributeConfirmActualsFieldSet;
import com.conx.logistics.kernel.ui.components.domain.form.PhysicalAttributeConfirmActualsFieldSetField;
import com.conx.logistics.kernel.ui.components.domain.layout.ConXGridLayout;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.CreateNewLineEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.LineEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.LineEditorContainerComponent;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.MasterDetailComponent;
import com.conx.logistics.kernel.ui.components.domain.page.TaskPage;
import com.conx.logistics.kernel.ui.components.domain.referencenumber.ReferenceNumberEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.table.ConXTable;
import com.conx.logistics.kernel.ui.components.domain.table.EntityMatchGrid;
import com.conx.logistics.mdm.domain.documentlibrary.FileEntry;
import com.conx.logistics.mdm.domain.metamodel.EntityType;
import com.conx.logistics.mdm.domain.note.NoteItem;
import com.conx.logistics.mdm.domain.referencenumber.ReferenceNumber;

public class ProcessDynamicArrivalReceiptsPage extends BasePageFlowPage implements IModelDrivenPageFlowPage {

	private TaskPage taskPage;

	@Override
	public String getTaskName() {
		return "Process Arrival Receipts";
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
	
	private MasterDetailComponent buildArrivalReceiptLineMasterDetail(DataSource fileEntryDataSource, DataSource referenceNumberDs, DataSource arrivalReceiptDataSource) {
		/**
		 * Create receiveLine grid: ds etc
		 */
		EntityType receiveLineType = new EntityType("Receive Line", ReceiveLine.class, null, null, null, "whreceiveline");
		DataSource receiveLineDs = new DataSource("receiveLineDS", receiveLineType);
		DataSourceField receiveLineDsField = new DataSourceField("name", receiveLineDs, receiveLineDs, receiveLineType, "Name", null);
		receiveLineDsField.setHidden(false);
		receiveLineDs.getDSFields().add(receiveLineDsField);
		receiveLineDsField = new DataSourceField("status", receiveLineDs, receiveLineDs, receiveLineType, "Status", null);
		receiveLineDsField.setHidden(false);
		receiveLineDs.getDSFields().add(receiveLineDsField);
		receiveLineDsField = new DataSourceField("product", receiveLineDs, receiveLineDs, receiveLineType, "Product", null);
		receiveLineDsField.setValueXPath("name");
		receiveLineDsField.setHidden(false);
		receiveLineDs.getDSFields().add(receiveLineDsField);
		receiveLineDsField = new DataSourceField("parentReceive", receiveLineDs, receiveLineDs, receiveLineType, "Owner", null);
		receiveLineDsField.setValueXPath("name");
		receiveLineDsField.setHidden(false);
		receiveLineDsField = new DataSourceField("arrivedInnerPackCount", receiveLineDs, receiveLineDs, receiveLineType, "Matched Stock Items", null);
		receiveLineDsField.setHidden(false);
		receiveLineDs.getDSFields().add(receiveLineDsField);
		receiveLineDsField = new DataSourceField("expectedInnerPackCount", receiveLineDs, receiveLineDs, receiveLineType, "Expected Stock Items", null);
		receiveLineDsField.setHidden(false);
		receiveLineDs.getDSFields().add(receiveLineDsField);

		/**
		 * Create stockItem dataSources
		 */		
		EntityType stockItemType = new EntityType("Stock Item", StockItem.class, null, null, null, "whstockitem");

		
		//-- stockItem dataSources: stockItemDS
		DataSource stockItemDs = new DataSource("stockItemDS", stockItemType);
		DataSourceField stockItemDsField = new DataSourceField("name", stockItemDs, stockItemDs, stockItemType, "Name", null);
		stockItemDsField.setHidden(false);
		stockItemDs.getDSFields().add(stockItemDsField);
		stockItemDsField = new DataSourceField("status", stockItemDs, stockItemDs, stockItemType, "Status", null);
		stockItemDsField.setHidden(false);
		stockItemDs.getDSFields().add(stockItemDsField);
		stockItemDsField = new DataSourceField("dateCreated", stockItemDs, stockItemDs, stockItemType, "Date Created", null);
		stockItemDsField.setHidden(false);
		stockItemDs.getDSFields().add(stockItemDsField);

		//- StockItem Grid
		EntityMatchGrid grid = new EntityMatchGrid(receiveLineDs, stockItemDs);
		grid.setDynamic(true);

		//- StockItem LineEditor Container
		LineEditorContainerComponent lecc = new LineEditorContainerComponent();
		
		//-- LE: New StockItem  Form
		DataSource newStockItemDS = new DataSource("newStockItemDS", stockItemType);
		FieldSet newSIFormFieldSet = new FieldSet();
		
		//--- Shipper
		DataSourceField dsField = new DataSourceField("shipper", newStockItemDS, newStockItemDS, stockItemType, "Shipper", null);
		dsField.setRequired(true);
		newStockItemDS.getDSFields().add(dsField);
		FieldSetField fsf = new FieldSetField(0, dsField, null);
		newSIFormFieldSet.getFields().add(fsf);
		
		//--- Consignee
		dsField = new DataSourceField("consignee", newStockItemDS, newStockItemDS, stockItemType, "Consignee", null);
		dsField.setRequired(true);
		newStockItemDS.getDSFields().add(dsField);
		fsf = new FieldSetField(0, dsField, null);
		newSIFormFieldSet.getFields().add(fsf);
		
		//--- Product
		dsField = new DataSourceField("product", newStockItemDS, newStockItemDS, stockItemType, "Product", null);
		dsField.setRequired(true);
		newStockItemDS.getDSFields().add(dsField);
		fsf = new FieldSetField(0, dsField, null);
		newSIFormFieldSet.getFields().add(fsf);		
		
		//--- Product
		dsField = new DataSourceField("groupSize", newStockItemDS, newStockItemDS, stockItemType, "Group Size", null);
		dsField.setRequired(true);
		newStockItemDS.getDSFields().add(dsField);
		fsf = new FieldSetField(0, dsField, null);
		newSIFormFieldSet.getFields().add(fsf);		
		
		//--- Product
		dsField = new DataSourceField("innerPackCount", newStockItemDS, newStockItemDS, stockItemType, "Quantity", null);
		dsField.setRequired(true);
		newStockItemDS.getDSFields().add(dsField);
		fsf = new FieldSetField(0, dsField, null);
		newSIFormFieldSet.getFields().add(fsf);	
		
		ConXSimpleForm newSIForm = new ConXSimpleForm(newStockItemDS);
		newSIForm.setFieldSet(newSIFormFieldSet);
		newSIForm.setCaption("Required Fields");
		
		CreateNewLineEditorComponent cnlec = new CreateNewLineEditorComponent(lecc);
		cnlec.setOrdinal(2);
		cnlec.setCaption("Enter New StockItem Fields");
		cnlec.setContent(newSIForm);
		lecc.getLineEditors().add(cnlec);

		
		//-- LE: StockItem Attachments
		LineEditorComponent lec = new LineEditorComponent(lecc);
		lec.setCaption("Attachments");
		lec.setOrdinal(100);
		lec.setContent(new AttachmentEditorComponent(fileEntryDataSource));
		lecc.getLineEditors().add(lec);
		
		//-- LE: StockItem Ref Nums
		lec = new LineEditorComponent(lecc);
		lec.setCaption("Reference Numbers");
		lec.setOrdinal(101);
		lec.setContent(new ReferenceNumberEditorComponent(referenceNumberDs));
		lecc.getLineEditors().add(lec);
		
		DataSource ds = new DataSource("stockItemDS", stockItemType);
		
		//-- LE: StockItem Organization Form
		ConXCollapseableSectionForm orgForm = new ConXCollapseableSectionForm(ds);
		orgForm.setCaption("Organization");
		FieldSet orgFieldSet = new FieldSet();
		orgFieldSet.setCaption("General");
		orgFieldSet.setForm(orgForm);
		dsField = new DataSourceField("shipper", ds, ds, stockItemType, "Shipper Organization", null);
		ds.getDSFields().add(dsField);
		fsf = new FieldSetField(0, dsField, orgFieldSet);
		orgFieldSet.getFields().add(fsf);
		dsField = new DataSourceField("consignee", ds, ds, stockItemType, "Consignee Organization", null);
		ds.getDSFields().add(dsField);
		fsf = new FieldSetField(0, dsField, orgFieldSet);
		orgFieldSet.getFields().add(fsf);
		orgForm.getFieldSetSet().add(orgFieldSet);
		
		//-- LE: StockItem Basic Form
		ConXCollapseableSectionForm basicForm = new ConXCollapseableSectionForm(ds);
		basicForm.setCaption("Basic");
		FieldSet fieldSet = new FieldSet();
		
		fieldSet.setCaption("General");
		dsField = new DataSourceField("location", ds, ds, stockItemType, "Location", null);
		ds.getDSFields().add(dsField);
		fsf = new FieldSetField(0, dsField, fieldSet);
		fieldSet.getFields().add(fsf);
		dsField = new DataSourceField("product", ds, ds, stockItemType, "Product", null);
		ds.getDSFields().add(dsField);
		fsf = new FieldSetField(0, dsField, fieldSet);
		fieldSet.getFields().add(fsf);
		basicForm.getFieldSetSet().add(fieldSet);
		
		lec = new LineEditorComponent(lecc);
		lec.setOrdinal(1);
		lec.setCaption("Basic");
		lec.setContent(basicForm);
		lecc.getLineEditors().add(lec);
		
		lec = new LineEditorComponent(lecc);
		lec.setOrdinal(2);
		lec.setCaption("Organization");
		lec.setContent(orgForm);
		lecc.getLineEditors().add(lec);
		
		ds = new DataSource("stockItemDS", stockItemType);
		
		//-- LE: StockItem Weight & Dims Form
		CollapsiblePhysicalAttributeConfirmActualsForm weightDimsForm = new CollapsiblePhysicalAttributeConfirmActualsForm(ds, "Confirm Truck Info");
		// Weight FieldSet
		PhysicalAttributeConfirmActualsFieldSet physicalAttributefieldSet = new PhysicalAttributeConfirmActualsFieldSet();
		physicalAttributefieldSet.setCaption("Weight");
		
		// Weight Field
		DataSourceField expectedDsField = new DataSourceField("expectedWeight", ds, ds, stockItemType, "Weight", null);
		ds.getDSFields().add(expectedDsField);
		DataSourceField expectedUnitDsField = new DataSourceField("expectedWeightUnit", ds, ds, stockItemType, "Weight Unit", null);
		ds.getDSFields().add(expectedUnitDsField);
		DataSourceField actualDsField = new DataSourceField("weight", ds, ds, stockItemType, "Weight", null);
		ds.getDSFields().add(actualDsField);
		DataSourceField actualUnitDsField = new DataSourceField("weightUnit", ds, ds, stockItemType, "Weight Unit", null);
		ds.getDSFields().add(actualUnitDsField);
		PhysicalAttributeConfirmActualsFieldSetField fieldSetField = new PhysicalAttributeConfirmActualsFieldSetField(0, expectedDsField, expectedUnitDsField, actualDsField, actualUnitDsField);
		fieldSetField.setCaption("Weight");
		physicalAttributefieldSet.getFields().add(fieldSetField);
		fieldSetField.setFieldSet(physicalAttributefieldSet);
		// Finish Weight FieldSet
		weightDimsForm.getFieldSetSet().add(physicalAttributefieldSet);
		// Dimensions FieldSet
		physicalAttributefieldSet = new PhysicalAttributeConfirmActualsFieldSet();
		physicalAttributefieldSet.setCaption("Dimensions");
		// Length Field
		expectedDsField = new DataSourceField("expectedLength", ds, ds, stockItemType, "Length", null);
		ds.getDSFields().add(expectedDsField);
		expectedUnitDsField = new DataSourceField("expectedLengthUnit", ds, ds, stockItemType, "Length Unit", null);
		ds.getDSFields().add(expectedUnitDsField);
		actualDsField = new DataSourceField("length", ds, ds, stockItemType, "Length", null);
		ds.getDSFields().add(actualDsField);
		actualUnitDsField = new DataSourceField("lengthUnit", ds, ds, stockItemType, "Length Unit", null);
		ds.getDSFields().add(actualUnitDsField);
		fieldSetField = new PhysicalAttributeConfirmActualsFieldSetField(0, expectedDsField, expectedUnitDsField, actualDsField, actualUnitDsField);
		fieldSetField.setCaption("Length");
		physicalAttributefieldSet.getFields().add(fieldSetField);
		fieldSetField.setFieldSet(physicalAttributefieldSet);
		// Width Field
		expectedDsField = new DataSourceField("expectedWidth", ds, ds, stockItemType, "Width", null);
		ds.getDSFields().add(expectedDsField);
		expectedUnitDsField = new DataSourceField("expectedWidthUnit", ds, ds, stockItemType, "Width Unit", null);
		ds.getDSFields().add(expectedUnitDsField);
		actualDsField = new DataSourceField("width", ds, ds, stockItemType, "Width", null);
		ds.getDSFields().add(actualDsField);
		actualUnitDsField = new DataSourceField("widthUnit", ds, ds, stockItemType, "Width Unit", null);
		ds.getDSFields().add(actualUnitDsField);
		fieldSetField = new PhysicalAttributeConfirmActualsFieldSetField(0, expectedDsField, expectedUnitDsField, actualDsField, actualUnitDsField);
		fieldSetField.setCaption("Width");
		physicalAttributefieldSet.getFields().add(fieldSetField);
		fieldSetField.setFieldSet(physicalAttributefieldSet);
		// Height Field
		expectedDsField = new DataSourceField("expectedHeight", ds, ds, stockItemType, "Height", null);
		ds.getDSFields().add(expectedDsField);
		expectedUnitDsField = new DataSourceField("expectedHeightUnit", ds, ds, stockItemType, "Height Unit", null);
		ds.getDSFields().add(expectedUnitDsField);
		actualDsField = new DataSourceField("height", ds, ds, stockItemType, "Height", null);
		ds.getDSFields().add(actualDsField);
		actualUnitDsField = new DataSourceField("heightUnit", ds, ds, stockItemType, "Height Unit", null);
		ds.getDSFields().add(actualUnitDsField);
		fieldSetField = new PhysicalAttributeConfirmActualsFieldSetField(0, expectedDsField, expectedUnitDsField, actualDsField, actualUnitDsField);
		fieldSetField.setCaption("Height");
		physicalAttributefieldSet.getFields().add(fieldSetField);
		fieldSetField.setFieldSet(physicalAttributefieldSet);
		// Volume Field
		expectedDsField = new DataSourceField("expectedVolume", ds, ds, stockItemType, "Volume", null);
		ds.getDSFields().add(expectedDsField);
		expectedUnitDsField = new DataSourceField("expectedVolumeUnit", ds, ds, stockItemType, "Volume Unit", null);
		ds.getDSFields().add(expectedUnitDsField);
		actualDsField = new DataSourceField("volume", ds, ds, stockItemType, "Volume", null);
		ds.getDSFields().add(actualDsField);
		actualUnitDsField = new DataSourceField("volumeUnit", ds, ds, stockItemType, "Volume Unit", null);
		ds.getDSFields().add(actualUnitDsField);
		fieldSetField = new PhysicalAttributeConfirmActualsFieldSetField(0, expectedDsField, expectedUnitDsField, actualDsField, actualUnitDsField);
		fieldSetField.setCaption("Volume");
		physicalAttributefieldSet.getFields().add(fieldSetField);
		fieldSetField.setFieldSet(physicalAttributefieldSet);
		// Finish Dimension FieldSet
		weightDimsForm.getFieldSetSet().add(physicalAttributefieldSet);
		// Quantity FieldSet
		physicalAttributefieldSet = new PhysicalAttributeConfirmActualsFieldSet();
		physicalAttributefieldSet.setCaption("Quantity");
		// Inner Stock Field
		expectedDsField = new DataSourceField("expectedInnerPackCount", ds, ds, stockItemType, "Inner Stock", null);
		ds.getDSFields().add(expectedDsField);
		expectedUnitDsField = new DataSourceField("expectedInnerPackUnit", ds, ds, stockItemType, "Inner Pack Unit", null);
		ds.getDSFields().add(expectedUnitDsField);
		actualDsField = new DataSourceField("innerPackCount", ds, ds, stockItemType, "Inner Stock", null);
		ds.getDSFields().add(actualDsField);
		actualUnitDsField = new DataSourceField("innerPackUnit", ds, ds, stockItemType, "Inner Pack Unit", null);
		ds.getDSFields().add(actualUnitDsField);
		fieldSetField = new PhysicalAttributeConfirmActualsFieldSetField(0, expectedDsField, expectedUnitDsField, actualDsField, actualUnitDsField);
		fieldSetField.setCaption("Inner Stock");
		physicalAttributefieldSet.getFields().add(fieldSetField);
		fieldSetField.setFieldSet(physicalAttributefieldSet);
		// Outer Stock Field
		expectedDsField = new DataSourceField("expectedOuterPackCount", ds, ds, stockItemType, "Outer Stock", null);
		ds.getDSFields().add(expectedDsField);
		expectedUnitDsField = new DataSourceField("expectedOuterPackUnit", ds, ds, stockItemType, "Outer Pack Unit", null);
		ds.getDSFields().add(expectedUnitDsField);
		actualDsField = new DataSourceField("outerPackCount", ds, ds, stockItemType, "Outer Stock", null);
		ds.getDSFields().add(actualDsField);
		actualUnitDsField = new DataSourceField("outerPackUnit", ds, ds, stockItemType, "Outer Pack Unit", null);
		ds.getDSFields().add(actualUnitDsField);
		fieldSetField = new PhysicalAttributeConfirmActualsFieldSetField(0, expectedDsField, expectedUnitDsField, actualDsField, actualUnitDsField);
		fieldSetField.setCaption("Outer Stock");
		physicalAttributefieldSet.getFields().add(fieldSetField);
		fieldSetField.setFieldSet(physicalAttributefieldSet);
		// Finish Quantity FieldSet
		weightDimsForm.getFieldSetSet().add(physicalAttributefieldSet);

		lec = new LineEditorComponent(lecc);
		lec.setOrdinal(3);
		lec.setCaption("Weight & Dimensions");
		lec.setContent(weightDimsForm);
		lecc.getLineEditors().add(lec);
		

		//-- MDE
		MasterDetailComponent mdc = new MasterDetailComponent();
		mdc.setMasterComponent(grid);
		mdc.setLineEditorPanel(lecc);
		mdc.setName("Arrival Receipt Line");
		return mdc;
	}

	private MasterDetailComponent buildArrivalReceiptMasterDetail(DataSource fileEntryDataSource, DataSource referenceNumberDs, DataSource arrivalReceiptDataSource) {
		EntityType arrivalReceiptLineType = new EntityType("Arrival Receipt Line", ArrivalReceiptLine.class, null, null, null, "wharrivalreceiptline");
		DataSource arrivalReceiptLineDs = new DataSource("arrivalReceiptLineDS", arrivalReceiptLineType);
		DataSourceField arrivalReceiptLineDsField = new DataSourceField("name", arrivalReceiptLineDs, arrivalReceiptLineDs, arrivalReceiptLineType, "Name", null);
		arrivalReceiptLineDsField.setHidden(false);
		arrivalReceiptLineDs.getDSFields().add(arrivalReceiptLineDsField);
		arrivalReceiptLineDsField = new DataSourceField("status", arrivalReceiptLineDs, arrivalReceiptLineDs, arrivalReceiptLineType, "Status", null);
		arrivalReceiptLineDsField.setHidden(false);
		arrivalReceiptLineDs.getDSFields().add(arrivalReceiptLineDsField);
		arrivalReceiptLineDsField = new DataSourceField("receiveLine", arrivalReceiptLineDs, arrivalReceiptLineDs, arrivalReceiptLineType, "Receive Line", null);
		arrivalReceiptLineDsField.setValueXPath("name");
		arrivalReceiptLineDsField.setHidden(false);
		arrivalReceiptLineDs.getDSFields().add(arrivalReceiptLineDsField);
		arrivalReceiptLineDsField = new DataSourceField("dateCreated", arrivalReceiptLineDs, arrivalReceiptLineDs, arrivalReceiptLineType, "Date Created", null);
		arrivalReceiptLineDsField.setHidden(false);
		arrivalReceiptLineDs.getDSFields().add(arrivalReceiptLineDsField);

		ConXCollapseableSectionForm form = new ConXCollapseableSectionForm(arrivalReceiptDataSource);
		form.setReadOnly(false);
		form.getFieldSetSet().add(buildBasicFieldSet(arrivalReceiptDataSource));

		LineEditorContainerComponent lecc = new LineEditorContainerComponent();

		LineEditorComponent lec = new LineEditorComponent(lecc);
		lec.setCaption("Attachments");
		lec.setOrdinal(100);
		lec.setContent(new AttachmentEditorComponent(fileEntryDataSource));
		lecc.getLineEditors().add(lec);

		ConXTable table = new ConXTable(arrivalReceiptLineDs);
		table.setRecordEditor(buildArrivalReceiptLineMasterDetail(fileEntryDataSource, referenceNumberDs, arrivalReceiptDataSource));

		lec = new LineEditorComponent(lecc);
		lec.setCaption("Arrival Receipt Lines");
		lec.setOrdinal(1);
		lec.setContent(table);
		lecc.getLineEditors().add(lec);

		MasterDetailComponent mdc = new MasterDetailComponent();
		mdc.setMasterComponent(form);
		mdc.setLineEditorPanel(lecc);
		mdc.setName("Arrival Receipt");
		return mdc;
	}

	private MasterDetailComponent buildArrivalMasterDetail(DataSource fileEntryDataSource) {
		EntityType arrivalReceiptType = new EntityType("Arrival Receipt", ArrivalReceipt.class, null, null, null, "wharrivalreceipt");
		DataSource arrivalReceiptDs = new DataSource("arrivalReceiptDS", arrivalReceiptType);
		DataSourceField arrivalReceiptDsField = new DataSourceField("name", arrivalReceiptDs, arrivalReceiptDs, arrivalReceiptType, "Name", null);
		arrivalReceiptDsField.setHidden(false);
		arrivalReceiptDs.getDSFields().add(arrivalReceiptDsField);
		arrivalReceiptDsField = new DataSourceField("status", arrivalReceiptDs, arrivalReceiptDs, arrivalReceiptType, "Status", null);
		arrivalReceiptDsField.setHidden(false);
		arrivalReceiptDs.getDSFields().add(arrivalReceiptDsField);
		arrivalReceiptDsField = new DataSourceField("commercialRecord", arrivalReceiptDs, arrivalReceiptDs, arrivalReceiptType, "Commercial Record", null);
		arrivalReceiptDsField.setValueXPath("name");
		arrivalReceiptDsField.setHidden(false);
		arrivalReceiptDs.getDSFields().add(arrivalReceiptDsField);
		arrivalReceiptDsField = new DataSourceField("consignee", arrivalReceiptDs, arrivalReceiptDs, arrivalReceiptType, "ModifiedDate", null);
		arrivalReceiptDsField.setValueXPath("name");
		arrivalReceiptDsField.setHidden(false);
		arrivalReceiptDs.getDSFields().add(arrivalReceiptDsField);
		arrivalReceiptDsField = new DataSourceField("shipper", arrivalReceiptDs, arrivalReceiptDs, arrivalReceiptType, "Mime Type", null);
		arrivalReceiptDsField.setValueXPath("name");
		arrivalReceiptDsField.setHidden(false);
		arrivalReceiptDs.getDSFields().add(arrivalReceiptDsField);
		arrivalReceiptDsField = new DataSourceField("shippedFrom", arrivalReceiptDs, arrivalReceiptDs, arrivalReceiptType, "Size", null);
		arrivalReceiptDsField.setValueXPath("name");
		arrivalReceiptDsField.setHidden(false);
		arrivalReceiptDs.getDSFields().add(arrivalReceiptDsField);

		EntityType referenceNumberType = new EntityType("Reference Number", ReferenceNumber.class, null, null, null, "mdmreferencenumber");
		DataSource referenceNumberDs = new DataSource("referenceNumberDS", referenceNumberType);
		DataSourceField referenceNumberDsField = new DataSourceField("name", referenceNumberDs, referenceNumberDs, referenceNumberType, "Name", null);
		referenceNumberDsField.setHidden(false);
		referenceNumberDs.getDSFields().add(referenceNumberDsField);
		referenceNumberDsField = new DataSourceField("dateCreated", referenceNumberDs, referenceNumberDs, referenceNumberType, "Date Created", null);
		referenceNumberDsField.setHidden(false);
		referenceNumberDs.getDSFields().add(referenceNumberDsField);
		referenceNumberDsField = new DataSourceField("parentReferenceNumber", referenceNumberDs, referenceNumberDs, referenceNumberType, "Parent Reference Number", null);
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

		EntityType arrivalType = new EntityType("Arrival", Arrival.class, null, null, null, "wharrival");
		DataSource arrivalDs = new DataSource("arrivalDS", arrivalType);

		ConXCollapseableSectionForm form = new ConXCollapseableSectionForm(arrivalDs);
		form.setReadOnly(false);
		form.getFieldSetSet().add(buildASNFieldSet(arrivalDs, arrivalType));
		form.getFieldSetSet().add(buildReceiveFieldSet(arrivalDs, arrivalType));
		form.getFieldSetSet().add(buildBasicFieldSet(arrivalDs));

		LineEditorContainerComponent lecc = new LineEditorContainerComponent();

		LineEditorComponent lec = new LineEditorComponent(lecc);
		lec.setCaption("Attachments");
		lec.setOrdinal(100);
		lec.setContent(new AttachmentEditorComponent(fileEntryDataSource));
		lecc.getLineEditors().add(lec);

		ConXTable arrRptGrid = new ConXTable(arrivalReceiptDs);
		arrRptGrid.setRecordEditor(buildArrivalReceiptMasterDetail(fileEntryDataSource, referenceNumberDs, arrivalReceiptDs));

		lec = new LineEditorComponent(lecc);
		lec.setCaption("Arrival Receipts");
		lec.setOrdinal(1);
		lec.setContent(arrRptGrid);
		lecc.getLineEditors().add(lec);

		lec = new LineEditorComponent(lecc);
		lec.setCaption("Reference Numbers");
		lec.setOrdinal(101);
		lec.setContent(new ReferenceNumberEditorComponent(referenceNumberDs));
		lecc.getLineEditors().add(lec);

//		lec = new LineEditorComponent(lecc);
//		lec.setCaption("Notes");
//		lec.setContent(new NoteEditorComponent(noteDs));
//		lecc.getLineEditors().add(lec);

		MasterDetailComponent mdc = new MasterDetailComponent();
		mdc.setMasterComponent(form);
		mdc.setLineEditorPanel(lecc);
		mdc.setName("Arrival");
		return mdc;
	}

	@Override
	public TaskPage getComponentModel() {
		if (taskPage == null) {
			EntityType feType = new EntityType("File Entry", FileEntry.class, null, null, null, "sysdlfileentry");
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

			MultiLevelEntityEditor mlee = new MultiLevelEntityEditor();
			mlee.setContent(buildArrivalMasterDetail(feDs));

			this.taskPage = new TaskPage(mlee);
		}

		return this.taskPage;
	}

	private FieldSet buildASNFieldSet(DataSource arrivalDs, EntityType arrivalType) {
		// ASN Field Set
		FieldSet fieldSet = new FieldSet(1, "ASN", new ConXGridLayout());
		fieldSet.setName("ASN");
		// Name Field
		DataSourceField arrivalDsField = new DataSourceField("receive", arrivalDs, arrivalDs, arrivalType, "Name", null);
		arrivalDsField.setValueXPath("asn/name");
		arrivalDsField.setHidden(false);
		arrivalDs.getDSFields().add(arrivalDsField);
		FieldSetField formFsf = new FieldSetField(0, arrivalDsField);
		formFsf.setRequired(true);
		formFsf.setReadOnly(true);
		fieldSet.getFields().add(formFsf);
		// Unit Field
		arrivalDsField = new DataSourceField("receive", arrivalDs, arrivalDs, arrivalType, "Outer Pack Unit", null);
		arrivalDsField.setValueXPath("asn/outerPackUnit");
		arrivalDsField.setHidden(false);
		arrivalDs.getDSFields().add(arrivalDsField);
		formFsf = new FieldSetField(0, arrivalDsField);
		formFsf.setRequired(true);
		formFsf.setReadOnly(true);
		fieldSet.getFields().add(formFsf);
		// Unit Field
		arrivalDsField = new DataSourceField("receive", arrivalDs, arrivalDs, arrivalType, "Weight Unit", null);
		arrivalDsField.setValueXPath("asn/weightUnit");
		arrivalDsField.setHidden(false);
		arrivalDs.getDSFields().add(arrivalDsField);
		formFsf = new FieldSetField(0, arrivalDsField);
		formFsf.setRequired(true);
		formFsf.setReadOnly(true);
		fieldSet.getFields().add(formFsf);
		// Unit Field
		arrivalDsField = new DataSourceField("receive", arrivalDs, arrivalDs, arrivalType, "Dim Unit", null);
		arrivalDsField.setValueXPath("asn/dimUnit");
		arrivalDsField.setHidden(false);
		arrivalDs.getDSFields().add(arrivalDsField);
		formFsf = new FieldSetField(0, arrivalDsField);
		formFsf.setRequired(true);
		formFsf.setReadOnly(true);
		fieldSet.getFields().add(formFsf);
		// Unit Field
		arrivalDsField = new DataSourceField("receive", arrivalDs, arrivalDs, arrivalType, "Vol Unit", null);
		arrivalDsField.setValueXPath("asn/volUnit");
		arrivalDsField.setHidden(false);
		arrivalDs.getDSFields().add(arrivalDsField);
		formFsf = new FieldSetField(0, arrivalDsField);
		formFsf.setRequired(true);
		formFsf.setReadOnly(true);
		fieldSet.getFields().add(formFsf);
		// Unit Field
		arrivalDsField = new DataSourceField("receive", arrivalDs, arrivalDs, arrivalType, "Consignee", null);
		arrivalDsField.setValueXPath("asn/consignee");
		arrivalDsField.setHidden(false);
		arrivalDs.getDSFields().add(arrivalDsField);
		formFsf = new FieldSetField(0, arrivalDsField);
		formFsf.setRequired(true);
		formFsf.setReadOnly(true);
		fieldSet.getFields().add(formFsf);

		return fieldSet;
	}

	private FieldSet buildReceiveFieldSet(DataSource arrivalDs, EntityType arrivalType) {
		// Receive Field Set
		FieldSet fieldSet = new FieldSet(2, "Receive", new ConXGridLayout());
		fieldSet.setName("Receive");
		// Name Field
		DataSourceField arrivalDsField = new DataSourceField("receive", arrivalDs, arrivalDs, arrivalType, "Name", null);
		arrivalDsField.setValueXPath("name");
		arrivalDsField.setHidden(false);
		arrivalDs.getDSFields().add(arrivalDsField);
		FieldSetField formFsf = new FieldSetField(0, arrivalDsField);
		formFsf.setRequired(true);
		formFsf.setReadOnly(true);
		fieldSet.getFields().add(formFsf);
		// Unit Field
		arrivalDsField = new DataSourceField("receive", arrivalDs, arrivalDs, arrivalType, "Expected Total Weight", null);
		arrivalDsField.setValueXPath("expectedTotalweight");
		arrivalDsField.setHidden(false);
		arrivalDs.getDSFields().add(arrivalDsField);
		formFsf = new FieldSetField(0, arrivalDsField);
		formFsf.setRequired(true);
		formFsf.setReadOnly(true);
		fieldSet.getFields().add(formFsf);
		// Unit Field
		arrivalDsField = new DataSourceField("receive", arrivalDs, arrivalDs, arrivalType, "Expected Total Width", null);
		arrivalDsField.setValueXPath("expectedTotalWidth");
		arrivalDsField.setHidden(false);
		arrivalDs.getDSFields().add(arrivalDsField);
		formFsf = new FieldSetField(0, arrivalDsField);
		formFsf.setRequired(true);
		formFsf.setReadOnly(true);
		fieldSet.getFields().add(formFsf);
		// Unit Field
		arrivalDsField = new DataSourceField("receive", arrivalDs, arrivalDs, arrivalType, "Expected Total Volume", null);
		arrivalDsField.setValueXPath("expectedTotalVolume");
		arrivalDsField.setHidden(false);
		arrivalDs.getDSFields().add(arrivalDsField);
		formFsf = new FieldSetField(0, arrivalDsField);
		formFsf.setRequired(true);
		formFsf.setReadOnly(true);
		fieldSet.getFields().add(formFsf);
		// Unit Field
		arrivalDsField = new DataSourceField("receive", arrivalDs, arrivalDs, arrivalType, "Receive Type", null);
		arrivalDsField.setValueXPath("rcvType");
		arrivalDsField.setHidden(false);
		arrivalDs.getDSFields().add(arrivalDsField);
		formFsf = new FieldSetField(0, arrivalDsField);
		formFsf.setRequired(true);
		formFsf.setReadOnly(true);
		fieldSet.getFields().add(formFsf);
		// Unit Field
		arrivalDsField = new DataSourceField("receive", arrivalDs, arrivalDs, arrivalType, "Status", null);
		arrivalDsField.setValueXPath("status");
		arrivalDsField.setHidden(false);
		arrivalDs.getDSFields().add(arrivalDsField);
		formFsf = new FieldSetField(0, arrivalDsField);
		formFsf.setRequired(true);
		formFsf.setReadOnly(true);
		fieldSet.getFields().add(formFsf);
		// Unit Field
		arrivalDsField = new DataSourceField("receive", arrivalDs, arrivalDs, arrivalType, "Date Last Updated", null);
		arrivalDsField.setValueXPath("lastUpdatedDate");
		arrivalDsField.setHidden(false);
		arrivalDs.getDSFields().add(arrivalDsField);
		formFsf = new FieldSetField(0, arrivalDsField);
		formFsf.setRequired(true);
		formFsf.setReadOnly(true);
		fieldSet.getFields().add(formFsf);

		return fieldSet;
	}

	@SuppressWarnings("unchecked")
	private FieldSet buildBasicFieldSet(DataSource ds) {
		// Receive Field Set
		FieldSet fieldSet = new FieldSet(0, "Basic", new ConXGridLayout());
		fieldSet.setName("Basic");
		// Name Field
		DataSourceField arrivalDsField = new DataSourceField("name", ds, ds, ds.getEntityType(), "Name", null);
		arrivalDsField.setHidden(false);
		ds.getDSFields().add(arrivalDsField);
		FieldSetField formFsf = new FieldSetField(0, arrivalDsField);
		formFsf.setRequired(true);
		formFsf.setReadOnly(true);
		fieldSet.getFields().add(formFsf);

		try {
			if (ds.getEntityType().getJavaType().isAssignableFrom(Arrival.class)) {
				// Unit Field
				arrivalDsField = new DataSourceField("actualLocalTrans", ds, ds, ds.getEntityType(), "Actual Local Trans", null);
				arrivalDsField.setHidden(false);
				ds.getDSFields().add(arrivalDsField);
				formFsf = new FieldSetField(0, arrivalDsField);
				formFsf.setRequired(true);
				formFsf.setReadOnly(true);
				fieldSet.getFields().add(formFsf);
				// Unit Field
				arrivalDsField = new DataSourceField("actualShipper", ds, ds, ds.getEntityType(), "Actual Shipper", null);
				arrivalDsField.setHidden(false);
				ds.getDSFields().add(arrivalDsField);
				formFsf = new FieldSetField(0, arrivalDsField);
				formFsf.setRequired(true);
				formFsf.setReadOnly(true);
				fieldSet.getFields().add(formFsf);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return fieldSet;
	}
}
