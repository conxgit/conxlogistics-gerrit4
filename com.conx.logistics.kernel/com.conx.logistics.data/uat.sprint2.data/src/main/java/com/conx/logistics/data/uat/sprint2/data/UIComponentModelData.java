package com.conx.logistics.data.uat.sprint2.data;

import java.util.Set;

import javax.persistence.EntityManager;

import com.conx.logistics.kernel.datasource.dao.services.IDataSourceDAOService;
import com.conx.logistics.kernel.datasource.domain.DataSource;
import com.conx.logistics.kernel.datasource.domain.DataSourceField;
import com.conx.logistics.kernel.metamodel.dao.services.IEntityTypeDAOService;
import com.conx.logistics.kernel.ui.components.dao.services.IComponentDAOService;
import com.conx.logistics.kernel.ui.components.domain.AbstractConXComponent;
import com.conx.logistics.kernel.ui.components.domain.attachment.AttachmentEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.form.ConXDetailForm;
import com.conx.logistics.kernel.ui.components.domain.form.ConXSimpleForm;
import com.conx.logistics.kernel.ui.components.domain.form.FieldSet;
import com.conx.logistics.kernel.ui.components.domain.form.FieldSetField;
import com.conx.logistics.kernel.ui.components.domain.layout.ConXGridLayout;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.LineEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.LineEditorContainerComponent;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.MasterDetailComponent;
import com.conx.logistics.kernel.ui.components.domain.note.NoteEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.referencenumber.ReferenceNumberEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.table.ConXDetailTable;
import com.conx.logistics.kernel.ui.components.domain.table.ConXTable;
import com.conx.logistics.mdm.domain.BaseEntity;

public class UIComponentModelData {
	@SuppressWarnings("unused")
	public final static MasterDetailComponent createReceiveSearchMasterDetail(IComponentDAOService componentDAOService, IEntityTypeDAOService entityTypeDAOService,
			IDataSourceDAOService dataSourceDAOService, EntityManager em) throws Exception {
		// -- ML E.E.
		DataSource rcvDefaultDS = getDefaultRCVDS(entityTypeDAOService, dataSourceDAOService, em);
		DataSource basicRcvDS = getBasicFormRCVDS(entityTypeDAOService, dataSourceDAOService, em);
		DataSource weightDimsRcvDS = getWeightDimsRCVDS(entityTypeDAOService, dataSourceDAOService, em);
		MasterDetailComponent rcvSearchMDC = new MasterDetailComponent("searchReceives", "Receives", basicRcvDS);
		rcvSearchMDC = (MasterDetailComponent) componentDAOService.add((AbstractConXComponent) rcvSearchMDC);

		LineEditorContainerComponent lecc = new LineEditorContainerComponent(rcvSearchMDC.getCode() + "-lineEditorContainerComponent", rcvSearchMDC.getName() + " Line Editor");
		try {
			lecc = (LineEditorContainerComponent) componentDAOService.add((AbstractConXComponent) lecc);
		} catch (Exception e) {
			e.printStackTrace();
		}

		/**
		 * Line Editors
		 */
		// A. -- RCV Basic Line Editor
		FieldSet basicFormFieldSet = createFieldSet("Basic", basicRcvDS, componentDAOService);
		FieldSet weightDimsFormFieldSet = createFieldSet("Weight & Dimensions", weightDimsRcvDS, componentDAOService);

		
		
		// -- Simple Form
		ConXSimpleForm rcvBasicForm = new ConXSimpleForm(basicRcvDS, "Basic");
		rcvBasicForm.setFieldSet(basicFormFieldSet);
		rcvBasicForm = (ConXSimpleForm) componentDAOService.add((AbstractConXComponent) rcvBasicForm);
		
		ConXSimpleForm rcvWeightDimsForm = new ConXSimpleForm(weightDimsRcvDS, "Weight & Dimensions");
		rcvWeightDimsForm.setFieldSet(weightDimsFormFieldSet);
		rcvWeightDimsForm = (ConXSimpleForm) componentDAOService.add((AbstractConXComponent) rcvWeightDimsForm);

		LineEditorComponent rcvBasicFormLE = new LineEditorComponent(lecc.getCode() + "-basicAttrs", "Basic", lecc);
		rcvBasicFormLE.setContent(rcvBasicForm);
		rcvBasicFormLE = (LineEditorComponent) componentDAOService.add((AbstractConXComponent) rcvBasicFormLE);
		
		LineEditorComponent rcvWeightDimsFormLE = new LineEditorComponent(lecc.getCode() + "-weightDimsAttrs", "Weight & Dimensions", lecc);
		rcvWeightDimsFormLE.setContent(rcvWeightDimsForm);
		rcvWeightDimsFormLE = (LineEditorComponent) componentDAOService.add((AbstractConXComponent) rcvWeightDimsFormLE);

		// -- RcvLines
		DataSource rcvLineDefaultDS = getRcvLineDefaultDS(entityTypeDAOService, dataSourceDAOService, em);
		ConXTable rcvLinesTable = new ConXDetailTable();
		rcvLinesTable.setDataSource(rcvLineDefaultDS);
		rcvLinesTable = (ConXTable) componentDAOService.add((AbstractConXComponent) rcvLinesTable);
		LineEditorComponent rcvLinesLE = new LineEditorComponent(lecc.getCode() + "-rcvLines", "Receive Lines", lecc);
		rcvLinesLE.setContent(rcvLinesTable);
		rcvLinesLE = (LineEditorComponent) componentDAOService.add((AbstractConXComponent) rcvLinesLE);		
		
		
		// -- RefNum's,Attachments, and Notes
		LineEditorComponent referenceNumbersLE = provideReferenceNumberEditor(componentDAOService, entityTypeDAOService, dataSourceDAOService, em, lecc);
		LineEditorComponent attachmentsLE = provideAttachmentLineEditor(componentDAOService, entityTypeDAOService, dataSourceDAOService, em, lecc);
		LineEditorComponent notesLE = provideNotesLineEditor(componentDAOService, entityTypeDAOService, dataSourceDAOService, em, lecc);

		lecc.getLineEditors().add(rcvLinesLE);
		lecc.getLineEditors().add(rcvWeightDimsFormLE);
		lecc.getLineEditors().add(rcvBasicFormLE);
		lecc.getLineEditors().add(referenceNumbersLE);
		lecc.getLineEditors().add(attachmentsLE);
		lecc.getLineEditors().add(notesLE);

		rcvSearchMDC.setLineEditorPanel(lecc);

		ConXTable rcvSearchTable = new ConXTable();
		rcvSearchTable.setDataSource(basicRcvDS);
		rcvSearchTable.setRecordEditor(createReceiveFormMasterDetail(rcvSearchMDC, componentDAOService, entityTypeDAOService, dataSourceDAOService, em));
		rcvSearchTable = (ConXTable) componentDAOService.add((AbstractConXComponent) rcvSearchTable);
		rcvSearchMDC.setMasterComponent(rcvSearchTable);
		rcvSearchMDC = (MasterDetailComponent) componentDAOService.update((AbstractConXComponent) rcvSearchMDC);

		// --Layout
		// ConXGridLayout gl = new ConXGridLayout();
		// gl = (ConXGridLayout) componentDAOService.add((AbstractConXComponent)
		// gl);

		// -- FieldSet: Collapseable form
		// FieldSet fs1 = new FieldSet(0, "Basic", gl);
		// fs1 = (FieldSet) componentDAOService.add((AbstractConXComponent)
		// fs1);
		// Set<FieldSetField> fsfs = new HashSet<FieldSetField>();
		// FieldSetField fsf1;
		// int ordinalIndex = 0;
		// for (DataSourceField dsf : basicRcvDS.getDSFields()) {
		// fsf1 = new FieldSetField(ordinalIndex++, dsf, fs1);
		// fsfs.add(fsf1);
		// }
		// fs1.getFields().addAll(fsfs);
		// fs1 = (FieldSet) componentDAOService.update((AbstractConXComponent)
		// fs1);
		//
		// Set<FieldSet> collapseableFormFieldSets = new HashSet<FieldSet>();
		// collapseableFormFieldSets.add(fs1);

		// -- FormField's: Simple Form
		// Set<FieldSetField> simpleFormFields = new HashSet<FieldSetField>();
		// FieldSetField ff;
		// ordinalIndex = 0;
		// for (DataSourceField dsf : basicRcvDS.getDSFields()) {
		// ff = new FieldSetField(ordinalIndex++, dsf);
		// simpleFormFields.add(ff);
		// }
		//
		// // -- Collapseable Form
		// ConXCollapseableSectionForm cform = new
		// ConXCollapseableSectionForm(basicRcvDS, collapseableFormFieldSets);
		// cform = (ConXCollapseableSectionForm)
		// componentDAOService.add((AbstractConXComponent) cform);
		//
		// // -- Update Line Editor
		// rcvBasicFormLE.setContent(sform);
		// rcvBasicFormLE = (LineEditorComponent)
		// componentDAOService.update((AbstractConXComponent) rcvBasicFormLE);

		return rcvSearchMDC;
	}

	@SuppressWarnings("unused")
	public final static MasterDetailComponent createReceiveFormMasterDetail(MasterDetailComponent searchMDC, IComponentDAOService componentDAOService, IEntityTypeDAOService entityTypeDAOService,
			IDataSourceDAOService dataSourceDAOService, EntityManager em) throws Exception {
		// -- ML E.E.
		DataSource rcvDefaultDS = getDefaultRCVDS(entityTypeDAOService, dataSourceDAOService, em);
		DataSource basicRcvDS = getBasicFormRCVDS(entityTypeDAOService, dataSourceDAOService, em);
		DataSource weightDimsRcvDS = getWeightDimsRCVDS(entityTypeDAOService, dataSourceDAOService, em);
		MasterDetailComponent rcvMDC = new MasterDetailComponent("receive", "Receive", basicRcvDS);
		rcvMDC = (MasterDetailComponent) componentDAOService.add((AbstractConXComponent) rcvMDC);

		LineEditorContainerComponent lecc = new LineEditorContainerComponent(rcvMDC.getCode() + "-lineEditorContainerComponent", rcvMDC.getName() + " Line Editor");
		lecc = (LineEditorContainerComponent) componentDAOService.add((AbstractConXComponent) lecc);

		LineEditorComponent referenceNumbersLE = provideReferenceNumberEditor(componentDAOService, entityTypeDAOService, dataSourceDAOService, em, lecc);
		LineEditorComponent attachmentsLE = provideAttachmentLineEditor(componentDAOService, entityTypeDAOService, dataSourceDAOService, em, lecc);
		LineEditorComponent notesLE = provideNotesLineEditor(componentDAOService, entityTypeDAOService, dataSourceDAOService, em, lecc);

		lecc.getLineEditors().add(referenceNumbersLE);
		lecc.getLineEditors().add(attachmentsLE);
		lecc.getLineEditors().add(notesLE);

		lecc = (LineEditorContainerComponent) componentDAOService.update((AbstractConXComponent) lecc);

		FieldSet simpleFormFieldSet = createFieldSet("Basic", basicRcvDS, componentDAOService);
		FieldSet weightDimsFormFieldSet = createFieldSet("Weight & Dimensions", weightDimsRcvDS, componentDAOService);

		ConXDetailForm rcvForm = new ConXDetailForm();
		rcvForm.setCaption("Receive");
		rcvForm.getFieldSetSet().add(simpleFormFieldSet);
		rcvForm.getFieldSetSet().add(weightDimsFormFieldSet);
		rcvForm = (ConXDetailForm) componentDAOService.add((AbstractConXComponent) rcvForm);

		simpleFormFieldSet.setForm(rcvForm);
		simpleFormFieldSet = (FieldSet) componentDAOService.update((AbstractConXComponent) simpleFormFieldSet);
		
		weightDimsFormFieldSet.setForm(rcvForm);
		weightDimsFormFieldSet = (FieldSet) componentDAOService.update((AbstractConXComponent) weightDimsFormFieldSet);

		rcvMDC.setMasterComponent(rcvForm);
		rcvMDC.setLineEditorPanel(lecc);
		rcvMDC = (MasterDetailComponent) componentDAOService.update((AbstractConXComponent) rcvMDC);

		return rcvMDC;
	}

	private static LineEditorComponent provideNotesLineEditor(IComponentDAOService componentDAOService, IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService,
			EntityManager em, LineEditorContainerComponent lecc) throws Exception {
		LineEditorComponent notesLE = new LineEditorComponent(lecc.getCode() + "-notes", "Notes", lecc);
		notesLE = (LineEditorComponent) componentDAOService.add((AbstractConXComponent) notesLE);
		lecc.getLineEditors().add(notesLE);

		// -- NoteItem DS
		DataSource noteItemDS = getNoteItemDS(entityTypeDAOService, dataSourceDAOService, em);

		// -- Notes Table
		NoteEditorComponent nec = new NoteEditorComponent(noteItemDS);
		nec = (NoteEditorComponent) componentDAOService.update((AbstractConXComponent) nec);
		notesLE.setContent(nec);
		notesLE.setDataSource(noteItemDS);
		notesLE = (LineEditorComponent) componentDAOService.update((AbstractConXComponent) notesLE);
		return notesLE;
	}

	private static LineEditorComponent provideReferenceNumberEditor(IComponentDAOService componentDAOService, IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService,
			EntityManager em, LineEditorContainerComponent lecc) throws Exception {
		LineEditorComponent rnsLE = new LineEditorComponent(lecc.getCode() + "-reference-nums", "Reference Numbers", lecc);
		rnsLE = (LineEditorComponent) componentDAOService.add((AbstractConXComponent) rnsLE);
		lecc.getLineEditors().add(rnsLE);

		// -- NoteItem DS
		DataSource referenceNumberDS = getReferenceNumberDS(entityTypeDAOService, dataSourceDAOService, em);

		// -- Notes Table
		ReferenceNumberEditorComponent nec = new ReferenceNumberEditorComponent(referenceNumberDS);
		nec = (ReferenceNumberEditorComponent) componentDAOService.update((AbstractConXComponent) nec);
		rnsLE.setContent(nec);
		rnsLE.setDataSource(referenceNumberDS);
		rnsLE = (LineEditorComponent) componentDAOService.update((AbstractConXComponent) rnsLE);
		return rnsLE;
	}

	private static LineEditorComponent provideAttachmentLineEditor(IComponentDAOService componentDAOService, IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService,
			EntityManager em, LineEditorContainerComponent lecc) throws Exception {
		LineEditorComponent attachmentsLE = new LineEditorComponent(lecc.getCode() + "-attachments", "Attachments", lecc);
		attachmentsLE = (LineEditorComponent) componentDAOService.add((AbstractConXComponent) attachmentsLE);

		// -- FileEntry DS
		DataSource fileEntryDS = getFileEntryDS(entityTypeDAOService, dataSourceDAOService, em);

		// -- Attachement Table
		AttachmentEditorComponent aec = new AttachmentEditorComponent(fileEntryDS);
		aec = (AttachmentEditorComponent) componentDAOService.update((AbstractConXComponent) aec);
		attachmentsLE.setContent(aec);
		attachmentsLE.setDataSource(fileEntryDS);
		attachmentsLE = (LineEditorComponent) componentDAOService.update((AbstractConXComponent) attachmentsLE);
		return attachmentsLE;
	}

	private static DataSource getDefaultRCVDS(IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService, EntityManager em) throws Exception {
		return DataSourceData.provideDefaultReceiveDS(entityTypeDAOService, dataSourceDAOService, em);
	}

	private static DataSource getBasicFormRCVDS(IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService, EntityManager em) throws Exception {
		return DataSourceData.provideBasicFormReceiveDS(entityTypeDAOService, dataSourceDAOService, em);
	}
	
	private static DataSource getRcvLineDefaultDS(IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService, EntityManager em) throws Exception {
		return DataSourceData.provideDefaultReceiveLineDS(entityTypeDAOService, dataSourceDAOService, em);
	}	
	
	private static DataSource getWeightDimsRCVDS(IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService, EntityManager em) throws Exception {
		return DataSourceData.provideWeightDimsFormReceiveDS(entityTypeDAOService, dataSourceDAOService, em);
	}
	
	private static DataSource getFileEntryDS(IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService, EntityManager em) throws Exception {
		return DataSourceData.provideFileEntryDS(entityTypeDAOService, dataSourceDAOService, em);
	}

	private static DataSource getNoteItemDS(IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService, EntityManager em) throws Exception {
		return DataSourceData.provideFileEntryDS(entityTypeDAOService, dataSourceDAOService, em);
	}

	private static DataSource getReferenceNumberDS(IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService, EntityManager em) throws Exception {
		return DataSourceData.provideFileEntryDS(entityTypeDAOService, dataSourceDAOService, em);
	}

	private static FieldSet createFieldSet(String caption, DataSource dataSource, IComponentDAOService componentDAOService) {
		ConXGridLayout gridLayout = new ConXGridLayout();
		gridLayout = (ConXGridLayout) componentDAOService.add((AbstractConXComponent) gridLayout);

		FieldSet fieldSet = new FieldSet();
		fieldSet.setCaption(caption);
		fieldSet.setLayout(gridLayout);
		fieldSet = (FieldSet) componentDAOService.add((AbstractConXComponent) fieldSet);

		int fieldIndex = 0;
		FieldSetField field = null;
		Set<DataSourceField> dsFields = dataSource.getDSFields();
		for (DataSourceField dsField : dsFields) {
			field = new FieldSetField(fieldIndex++, dsField);
			if (dsField.getName().equals(BaseEntity.BASIC_ENTITY_ATTRIBUTE_ID) || dsField.getName().equals(BaseEntity.BASIC_ENTITY_ATTRIBUTE_NAME)
					|| dsField.getName().equals(BaseEntity.BASIC_ENTITY_ATTRIBUTE_CODE) || dsField.getName().equals(BaseEntity.BASIC_ENTITY_ATTRIBUTE_DATE_CREATED)
					|| dsField.getName().equals(BaseEntity.BASIC_ENTITY_ATTRIBUTE_DATE_LAST_UPDATED)) {
				field.setReadOnly(true);
			}
			field.setFieldSet(fieldSet);
			field = (FieldSetField) componentDAOService.add((AbstractConXComponent) field);

			fieldSet.getFields().add(field);
		}

		fieldSet = (FieldSet) componentDAOService.update((AbstractConXComponent) fieldSet);

		return fieldSet;
	}
}