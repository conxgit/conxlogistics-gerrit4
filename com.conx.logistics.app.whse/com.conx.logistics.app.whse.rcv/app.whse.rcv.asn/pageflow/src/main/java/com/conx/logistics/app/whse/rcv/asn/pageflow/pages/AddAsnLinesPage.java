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

public class AddAsnLinesPage extends BasePageFlowPage implements IModelDrivenPageFlowPage {
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
		return "AddAsnLinesPage";
	}

	@Override
	public Class<?> getType() {
		return IModelDrivenPageFlowPage.class;
	}

	private ConXForm buildBasicForm(EntityType asnLineEntityType) {
		// Build AsnLine DataSource
		DataSource asnLineDataSource = new DataSource("asnlinebasicformdatasource", asnLineEntityType);
		// Build AsnLine Basic Form
		ConXCollapseableSectionForm basicForm = new ConXCollapseableSectionForm();
		basicForm.setEnabled(false);
		basicForm.setDataSource(asnLineDataSource);
		// Build General Section
		FieldSet generalFieldSet = new FieldSet(0);
		generalFieldSet.setCaption("Basic");
		// Id Field
		DataSourceField idDsField = new DataSourceField("id", asnLineDataSource, null, "Id");
		asnLineDataSource.getDSFields().add(idDsField);
		FieldSetField idField = new FieldSetField(0);
		idField.setReadOnly(true);
		idField.setDataSourceField(idDsField);
		idField.setDataSource(asnLineDataSource);
		generalFieldSet.getFields().add(idField);
		// Name Field
		DataSourceField nameDsField = new DataSourceField("name", asnLineDataSource, null, "Name");
		asnLineDataSource.getDSFields().add(nameDsField);
		FieldSetField nameField = new FieldSetField(1);
		nameField.setReadOnly(true);
		nameField.setDataSourceField(nameDsField);
		nameField.setDataSource(asnLineDataSource);
		generalFieldSet.getFields().add(nameField);
		// Date Created Field
		DataSourceField dateCreatedDsField = new DataSourceField("dateCreated", asnLineDataSource, null, "Date Created");
		asnLineDataSource.getDSFields().add(dateCreatedDsField);
		FieldSetField dateCreatedField = new FieldSetField(2);
		dateCreatedField.setReadOnly(true);
		dateCreatedField.setDataSourceField(dateCreatedDsField);
		dateCreatedField.setDataSource(asnLineDataSource);
		generalFieldSet.getFields().add(dateCreatedField);
		// Date Last Updated Field
		DataSourceField dateLastUpdatedDsField = new DataSourceField("dateLastUpdated", asnLineDataSource, null, "Date Last Updated");
		asnLineDataSource.getDSFields().add(dateLastUpdatedDsField);
		FieldSetField dateLastUpdatedField = new FieldSetField(3);
		dateLastUpdatedField.setReadOnly(true);
		dateLastUpdatedField.setDataSourceField(dateLastUpdatedDsField);
		dateLastUpdatedField.setDataSource(asnLineDataSource);
		generalFieldSet.getFields().add(dateLastUpdatedField);
		// Status Field
		DataSourceField statusDsField = new DataSourceField("ASNLINESTATUS", asnLineDataSource, null, "Status");
		asnLineDataSource.getDSFields().add(statusDsField);
		FieldSetField statusField = new FieldSetField(0);
		statusField.setReadOnly(true);
		statusField.setDataSourceField(statusDsField);
		statusField.setDataSource(asnLineDataSource);
		generalFieldSet.getFields().add(statusField);
		// Add General Section to Basic Form
		basicForm.getFieldSetSet().add(generalFieldSet);

		// Build Parent Section
		FieldSet parentFieldSet = new FieldSet(1);
		parentFieldSet.setCaption("Parent");
		// Id Field
		DataSourceField parentIdDsField = new DataSourceField("parentASN", asnLineDataSource, null, "Id");
		parentIdDsField.setValueXPath("id");
		asnLineDataSource.getDSFields().add(parentIdDsField);
		FieldSetField parentIdField = new FieldSetField(0);
		parentIdField.setReadOnly(true);
		parentIdField.setDataSourceField(parentIdDsField);
		parentIdField.setDataSource(asnLineDataSource);
		parentFieldSet.getFields().add(parentIdField);
		// Name Field
		DataSourceField parentNameDsField = new DataSourceField("parentASN", asnLineDataSource, null, "Name");
		parentNameDsField.setValueXPath("parentName");
		asnLineDataSource.getDSFields().add(parentNameDsField);
		FieldSetField parentNameField = new FieldSetField(1);
		parentNameField.setReadOnly(true);
		parentNameField.setDataSourceField(parentNameDsField);
		parentNameField.setDataSource(asnLineDataSource);
		parentFieldSet.getFields().add(parentNameField);
		// Date Created Field
		DataSourceField parentDateCreatedDsField = new DataSourceField("parentASN", asnLineDataSource, null, "Date Created");
		parentDateCreatedDsField.setValueXPath("parentDateCreated");
		asnLineDataSource.getDSFields().add(parentDateCreatedDsField);
		FieldSetField parentDateCreatedField = new FieldSetField(2);
		parentDateCreatedField.setReadOnly(true);
		parentDateCreatedField.setDataSourceField(parentDateCreatedDsField);
		parentDateCreatedField.setDataSource(asnLineDataSource);
		parentFieldSet.getFields().add(parentDateCreatedField);
		// Date Last UpparentDated Field
		DataSourceField parentDateLastUpparentDatedDsField = new DataSourceField("parentASN", asnLineDataSource, null, "Date Last Updated");
		parentDateLastUpparentDatedDsField.setValueXPath("parentDateLastUpdated");
		asnLineDataSource.getDSFields().add(parentDateLastUpparentDatedDsField);
		FieldSetField parentDateLastUpparentDatedField = new FieldSetField(3);
		parentDateLastUpparentDatedField.setReadOnly(true);
		parentDateLastUpparentDatedField.setDataSourceField(parentDateLastUpparentDatedDsField);
		parentDateLastUpparentDatedField.setDataSource(asnLineDataSource);
		parentFieldSet.getFields().add(parentDateLastUpparentDatedField);
		// Add Parent Section to Basic Form
		basicForm.getFieldSetSet().add(parentFieldSet);

		return basicForm;
	}

	private ConXForm buildDetailForm(EntityType asnLineEntityType) {
		// Build AsnLine DataSource
		DataSource asnLineDataSource = new DataSource("asnlinedetailformdatasource", asnLineEntityType);
		// Build AsnLine Detail Form
		ConXCollapseableSectionForm detailForm = new ConXCollapseableSectionForm();
		detailForm.setDataSource(asnLineDataSource);

		// Build Product Section
		FieldSet productFieldSet = new FieldSet(0);
		productFieldSet.setCaption("Product");
		// Status Field
		DataSourceField productDsField = new DataSourceField("product", asnLineDataSource, null, "Product");
		asnLineDataSource.getDSFields().add(productDsField);
		FieldSetField productField = new FieldSetField(0);
		productField.setDataSourceField(productDsField);
		productField.setDataSource(asnLineDataSource);
		productFieldSet.getFields().add(productField);
		// Add Dimensions Section to Detail Form
		detailForm.getFieldSetSet().add(productFieldSet);

		return detailForm;
	}

	private ConXForm buildWeightDimensionsForm(EntityType asnLineEntityType) {
		// Build AsnLine DataSource
		DataSource asnLineDataSource = new DataSource("asnlineweightdimensionsformdatasource", asnLineEntityType);
		// Build AsnLine Detail Form
		ConXCollapseableSectionForm weightDimensionsForm = new ConXCollapseableSectionForm();
		weightDimensionsForm.setDataSource(asnLineDataSource);
		// Build Weight Section
		FieldSet weightFieldSet = new FieldSet(0);
		weightFieldSet.setCaption("Weight");
		// Expected Total Weight Field
		DataSourceField expectedTotalWeightDsField = new DataSourceField("expectedTotalWeight", asnLineDataSource, null, "Expected Total Weight");
		asnLineDataSource.getDSFields().add(expectedTotalWeightDsField);
		FieldSetField expectedTotalWeightField = new FieldSetField(0);
		expectedTotalWeightField.setDataSourceField(expectedTotalWeightDsField);
		expectedTotalWeightField.setDataSource(asnLineDataSource);
		weightFieldSet.getFields().add(expectedTotalWeightField);
		// Add Weight Section to Detail Form
		weightDimensionsForm.getFieldSetSet().add(weightFieldSet);

		// Build Dimensions Section
		FieldSet dimensionsFieldSet = new FieldSet(0);
		dimensionsFieldSet.setCaption("Dimensions");
		// Expected Total Length Field
		DataSourceField expectedTotalLengthDsField = new DataSourceField("expectedTotalLength", asnLineDataSource, null, "Expected Total Length");
		asnLineDataSource.getDSFields().add(expectedTotalLengthDsField);
		FieldSetField expectedTotalLengthField = new FieldSetField(0);
		expectedTotalLengthField.setDataSourceField(expectedTotalLengthDsField);
		expectedTotalLengthField.setDataSource(asnLineDataSource);
		dimensionsFieldSet.getFields().add(expectedTotalLengthField);
		// Expected Total Width Field
		DataSourceField expectedTotalWidthDsField = new DataSourceField("expectedTotalWidth", asnLineDataSource, null, "Expected Total Width");
		asnLineDataSource.getDSFields().add(expectedTotalWidthDsField);
		FieldSetField expectedTotalWidthField = new FieldSetField(0);
		expectedTotalWidthField.setDataSourceField(expectedTotalWidthDsField);
		expectedTotalWidthField.setDataSource(asnLineDataSource);
		dimensionsFieldSet.getFields().add(expectedTotalWidthField);
		// Expected Total Height Field
		DataSourceField expectedTotalHeightDsField = new DataSourceField("expectedTotalHeight", asnLineDataSource, null, "Expected Total Height");
		asnLineDataSource.getDSFields().add(expectedTotalHeightDsField);
		FieldSetField expectedTotalHeightField = new FieldSetField(0);
		expectedTotalHeightField.setDataSourceField(expectedTotalHeightDsField);
		expectedTotalHeightField.setDataSource(asnLineDataSource);
		dimensionsFieldSet.getFields().add(expectedTotalHeightField);
		// Expected Total Volume Field
		DataSourceField expectedTotalVolumeDsField = new DataSourceField("expectedTotalVolume", asnLineDataSource, null, "Expected Total Volume");
		asnLineDataSource.getDSFields().add(expectedTotalVolumeDsField);
		FieldSetField expectedTotalVolumeField = new FieldSetField(0);
		expectedTotalVolumeField.setDataSourceField(expectedTotalVolumeDsField);
		expectedTotalVolumeField.setDataSource(asnLineDataSource);
		dimensionsFieldSet.getFields().add(expectedTotalVolumeField);
		// Add Dimensions Section to Detail Form
		weightDimensionsForm.getFieldSetSet().add(dimensionsFieldSet);

		// Build Quantity Section
		FieldSet quantityFieldSet = new FieldSet(0);
		quantityFieldSet.setCaption("Quantity");
		// Expected Inner Pack Count Field
		DataSourceField expectedInnerPackCountDsField = new DataSourceField("expectedInnerPackCount", asnLineDataSource, null,
				"Expected Inner Pack Count");
		asnLineDataSource.getDSFields().add(expectedInnerPackCountDsField);
		FieldSetField expectedInnerPackCountField = new FieldSetField(0);
		expectedInnerPackCountField.setDataSourceField(expectedInnerPackCountDsField);
		expectedInnerPackCountField.setDataSource(asnLineDataSource);
		quantityFieldSet.getFields().add(expectedInnerPackCountField);
		// Expected Outer Pack Count Field
		DataSourceField expectedOuterPackCountDsField = new DataSourceField("expectedOuterPackCount", asnLineDataSource, null,
				"Expected Outer Pack Count");
		asnLineDataSource.getDSFields().add(expectedOuterPackCountDsField);
		FieldSetField expectedOuterPackCountField = new FieldSetField(0);
		expectedOuterPackCountField.setDataSourceField(expectedOuterPackCountDsField);
		expectedOuterPackCountField.setDataSource(asnLineDataSource);
		quantityFieldSet.getFields().add(expectedOuterPackCountField);
		// Add Quantity Section to Detail Form
		weightDimensionsForm.getFieldSetSet().add(quantityFieldSet);

		return weightDimensionsForm;
	}

	// private SearchGrid buildProductSelectionGrid(EntityType
	// asnLineEntityType) {
	// EntityType type = new EntityType("Product", Product.class, null, null,
	// null, "mdmproduct");
	// DataSource ds = new
	// DataSource("addasnlinespage-productsearchgriddatasource", type);
	//
	// String[] visibleColumnIds = { "id", "code", "name", "dateCreated",
	// "description" };
	//
	// SearchGrid searchGrid = new SearchGrid();
	// searchGrid.setDataSource(ds);
	// searchGrid.setFormTitle("Product");
	// searchGrid.setVisibleColumnIds(visibleColumnIds);
	//
	// return searchGrid;
	// }

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

	private LineEditorContainerComponent buildAsnLineLineEditors(EntityType asnLineEntityType) {
		LineEditorContainerComponent asnLineLineEditorContainer = new LineEditorContainerComponent("addasnlinespage-asnlinelineeditorcontainer",
				"addasnlinespage-asnlinelineeditorcontainer");

		LineEditorComponent detailFormLineEditor = new LineEditorComponent("addasnlinespage-detailasnlinelineeditor", "Detail",
				asnLineLineEditorContainer);
		detailFormLineEditor.setContent(buildDetailForm(asnLineEntityType));
		detailFormLineEditor.setOrdinal(0);
		asnLineLineEditorContainer.getLineEditors().add(detailFormLineEditor);
		// LineEditorComponent productFormLineEditor = new
		// LineEditorComponent("addasnlinespage-productasnlinelineeditor",
		// "Product",
		// asnLineLineEditorContainer);
		// productFormLineEditor.setContent(buildProductSelectionGrid(asnLineEntityType));
		// productFormLineEditor.setOrdinal(1);
		// asnLineLineEditorContainer.getLineEditors().add(productFormLineEditor);
		LineEditorComponent weightDimensionsFormLineEditor = new LineEditorComponent("addasnlinespage-weightdimensionsasnlinelineeditor",
				"Weight Dimensions", asnLineLineEditorContainer);
		weightDimensionsFormLineEditor.setContent(buildWeightDimensionsForm(asnLineEntityType));
		weightDimensionsFormLineEditor.setOrdinal(2);
		asnLineLineEditorContainer.getLineEditors().add(weightDimensionsFormLineEditor);
		LineEditorComponent attachmentsFormLineEditor = new LineEditorComponent("addasnlinespage-attachmentsasnlinelineeditor", "Attachments",
				asnLineLineEditorContainer);
		attachmentsFormLineEditor.setContent(buildAttachmentsEditor());
		attachmentsFormLineEditor.setOrdinal(3);
		asnLineLineEditorContainer.getLineEditors().add(attachmentsFormLineEditor);
		LineEditorComponent referenceNumbersFormLineEditor = new LineEditorComponent("addasnlinespage-referencenumbersasnlinelineeditor",
				"Reference Numbers", asnLineLineEditorContainer);
		referenceNumbersFormLineEditor.setContent(buildReferenceNumbersEditor());
		referenceNumbersFormLineEditor.setOrdinal(4);
		asnLineLineEditorContainer.getLineEditors().add(referenceNumbersFormLineEditor);
		LineEditorComponent notesFormLineEditor = new LineEditorComponent("addasnlinespage-notesasnlinelineeditor", "Notes",
				asnLineLineEditorContainer);
		notesFormLineEditor.setContent(buildNotesEditor());
		notesFormLineEditor.setOrdinal(5);
		asnLineLineEditorContainer.getLineEditors().add(notesFormLineEditor);

		return asnLineLineEditorContainer;
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
		asnLineGrid.setRecordEditor(buildAsnLineMasterDetailComponent());

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

		return basicForm;
	}

	private MasterDetailComponent buildAsnLineMasterDetailComponent() {
		// Build AsnLine EntityType
		EntityType asnLineEntityType = new EntityType("ASNLine", ASNLine.class, null, null, null, "whasnline");
		// Build AsnLine MasterDetailComponent
		MasterDetailComponent asnLineMasterDetail = new MasterDetailComponent("addasnlinespage-asnlinemasterdetail", "ASN Line");
		asnLineMasterDetail.setMasterComponent(buildBasicForm(asnLineEntityType));
		asnLineMasterDetail.setLineEditorPanel(buildAsnLineLineEditors(asnLineEntityType));

		return asnLineMasterDetail;
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