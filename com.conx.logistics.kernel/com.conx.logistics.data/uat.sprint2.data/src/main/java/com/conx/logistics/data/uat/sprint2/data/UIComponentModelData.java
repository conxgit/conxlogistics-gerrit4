package com.conx.logistics.data.uat.sprint2.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;

import com.conx.logistics.kernel.datasource.dao.services.IDataSourceDAOService;
import com.conx.logistics.kernel.datasource.domain.DataSource;
import com.conx.logistics.kernel.metamodel.dao.services.IEntityTypeDAOService;
import com.conx.logistics.kernel.ui.components.dao.services.IComponentDAOService;
import com.conx.logistics.kernel.ui.components.domain.AbstractConXComponent;
import com.conx.logistics.kernel.ui.components.domain.attachment.AttachmentEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.form.ConXCollapseableSectionForm;
import com.conx.logistics.kernel.ui.components.domain.form.ConXSimpleForm;
import com.conx.logistics.kernel.ui.components.domain.form.FieldSet;
import com.conx.logistics.kernel.ui.components.domain.layout.ConXGridLayout;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.LineEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.LineEditorContainerComponent;
import com.conx.logistics.kernel.ui.components.domain.masterdetail.MasterDetailComponent;
import com.conx.logistics.kernel.ui.components.domain.note.NoteEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.referencenumber.ReferenceNumberEditorComponent;
import com.conx.logistics.kernel.ui.components.domain.table.ConXTable;

public class UIComponentModelData {
	@SuppressWarnings("unused")
	public final static MasterDetailComponent createReceiveSearchMasterDetail(IComponentDAOService componentDAOService, IEntityTypeDAOService entityTypeDAOService,
			IDataSourceDAOService dataSourceDAOService, EntityManager em) throws Exception {
		// -- ML E.E.
		DataSource rcvDefaultDS = getDefaultRCVDS(entityTypeDAOService, dataSourceDAOService, em);
		DataSource basicRcvDS = getBasicFormRCVDS(entityTypeDAOService, dataSourceDAOService, em);
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
		ConXGridLayout gl2 = new ConXGridLayout();
		gl2 = (ConXGridLayout) componentDAOService.add((AbstractConXComponent) gl2);
		
		FieldSet simpleFormFieldSet = new FieldSet();
		simpleFormFieldSet.setCaption("Simple Form Field Set");
		simpleFormFieldSet.setLayout(gl2);
		simpleFormFieldSet = (FieldSet) componentDAOService.add((AbstractConXComponent) simpleFormFieldSet);

		// -- Simple Form
		ConXSimpleForm sform = new ConXSimpleForm(basicRcvDS, "Other");
		sform.setFieldSet(simpleFormFieldSet);
		sform = (ConXSimpleForm) componentDAOService.add((AbstractConXComponent) sform);
		
		LineEditorComponent rcvBasicFormLE = new LineEditorComponent(lecc.getCode() + "-basicAttrs", "Basic", lecc);
		rcvBasicFormLE.setContent(sform);
		rcvBasicFormLE = (LineEditorComponent) componentDAOService.add((AbstractConXComponent) rcvBasicFormLE);
		
		LineEditorComponent referenceNumbersLE = provideReferenceNumberEditor(componentDAOService, entityTypeDAOService, dataSourceDAOService, em, lecc);
		LineEditorComponent attachmentsLE = provideAttachmentLineEditor(componentDAOService, entityTypeDAOService, dataSourceDAOService, em, lecc);
		LineEditorComponent notesLE = provideNotesLineEditor(componentDAOService, entityTypeDAOService, dataSourceDAOService, em, lecc);
		
		lecc.getLineEditors().add(rcvBasicFormLE);
		lecc.getLineEditors().add(referenceNumbersLE);
		lecc.getLineEditors().add(attachmentsLE);
		lecc.getLineEditors().add(notesLE);
		
		rcvSearchMDC.setLineEditorPanel(lecc);
		
		ConXTable rcvSearchTable = new ConXTable();
//		rcvSearchTable.setRecordEditor(createReceiveMasterDetail(rcvSearchMDC, componentDAOService, entityTypeDAOService, dataSourceDAOService, em));
		rcvSearchTable = (ConXTable) componentDAOService.add((AbstractConXComponent) rcvSearchTable);
		rcvSearchMDC.setMasterComponent(rcvSearchTable);
		rcvSearchMDC = (MasterDetailComponent) componentDAOService.update((AbstractConXComponent) rcvSearchMDC);

		// --Layout
//		ConXGridLayout gl = new ConXGridLayout();
//		gl = (ConXGridLayout) componentDAOService.add((AbstractConXComponent) gl);

		// -- FieldSet: Collapseable form
//		FieldSet fs1 = new FieldSet(0, "Basic", gl);
//		fs1 = (FieldSet) componentDAOService.add((AbstractConXComponent) fs1);
//		Set<FieldSetField> fsfs = new HashSet<FieldSetField>();
//		FieldSetField fsf1;
//		int ordinalIndex = 0;
//		for (DataSourceField dsf : basicRcvDS.getDSFields()) {
//			fsf1 = new FieldSetField(ordinalIndex++, dsf, fs1);
//			fsfs.add(fsf1);
//		}
//		fs1.getFields().addAll(fsfs);
//		fs1 = (FieldSet) componentDAOService.update((AbstractConXComponent) fs1);
//
//		Set<FieldSet> collapseableFormFieldSets = new HashSet<FieldSet>();
//		collapseableFormFieldSets.add(fs1);

		// -- FormField's: Simple Form
//		Set<FieldSetField> simpleFormFields = new HashSet<FieldSetField>();
//		FieldSetField ff;
//		ordinalIndex = 0;
//		for (DataSourceField dsf : basicRcvDS.getDSFields()) {
//			ff = new FieldSetField(ordinalIndex++, dsf);
//			simpleFormFields.add(ff);
//		}
//		
//		// -- Collapseable Form
//		ConXCollapseableSectionForm cform = new ConXCollapseableSectionForm(basicRcvDS, collapseableFormFieldSets);
//		cform = (ConXCollapseableSectionForm) componentDAOService.add((AbstractConXComponent) cform);
//
//		// -- Update Line Editor
//		rcvBasicFormLE.setContent(sform);
//		rcvBasicFormLE = (LineEditorComponent) componentDAOService.update((AbstractConXComponent) rcvBasicFormLE);
		
		return rcvSearchMDC;
	}
	
	@SuppressWarnings("unused")
	public final static MasterDetailComponent createReceiveMasterDetail(MasterDetailComponent searchMDC, IComponentDAOService componentDAOService, IEntityTypeDAOService entityTypeDAOService,
			IDataSourceDAOService dataSourceDAOService, EntityManager em) throws Exception {
		// -- ML E.E.
		DataSource rcvDefaultDS = getDefaultRCVDS(entityTypeDAOService, dataSourceDAOService, em);
		DataSource basicRcvDS = getBasicFormRCVDS(entityTypeDAOService, dataSourceDAOService, em);
		MasterDetailComponent rcvMDC = new MasterDetailComponent("receive", "Receive", basicRcvDS);
		rcvMDC = (MasterDetailComponent) componentDAOService.add((AbstractConXComponent) rcvMDC);

		// -- RCV Search Table
		HashSet<FieldSet> fieldSetSet = new HashSet<FieldSet>();
		Set<LineEditorComponent> lineEditors = searchMDC.getLineEditorPanel().getLineEditors();
		AbstractConXComponent content;
		for (LineEditorComponent lec : lineEditors) {
			content = lec.getContent();
			if (content instanceof ConXSimpleForm) {
				fieldSetSet.add(((ConXSimpleForm) content).getFieldSet());
			} else if (content instanceof ConXCollapseableSectionForm) {
				fieldSetSet.addAll(((ConXCollapseableSectionForm) content).getFieldSetSet());
			}
		}
		ConXCollapseableSectionForm rcvForm = new ConXCollapseableSectionForm(basicRcvDS, fieldSetSet);
		rcvForm = (ConXCollapseableSectionForm) componentDAOService.add((AbstractConXComponent) rcvForm);
		rcvMDC.setMasterComponent(rcvForm);

		LineEditorContainerComponent lecc = new LineEditorContainerComponent(rcvMDC.getCode() + "-lineEditorContainerComponent", rcvMDC.getName() + " Line Editor");
		try {
			lecc = (LineEditorContainerComponent) componentDAOService.add((AbstractConXComponent) lecc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		rcvMDC.setLineEditorPanel(lecc);

		/**
		 * Line Editors
		 */

		// B. -- Reference Numbers
		LineEditorComponent referenceNumbersLE = provideReferenceNumberEditor(componentDAOService, entityTypeDAOService, dataSourceDAOService, em, lecc);

		// A. -- Attachments
		LineEditorComponent attachmentsLE = provideAttachmentLineEditor(componentDAOService, entityTypeDAOService, dataSourceDAOService, em, lecc);

		// B. -- Notes
		LineEditorComponent notesLE = provideNotesLineEditor(componentDAOService, entityTypeDAOService, dataSourceDAOService, em, lecc);

		// -- Update EE
		lecc.getLineEditors().add(referenceNumbersLE);
		lecc.getLineEditors().add(attachmentsLE);
		lecc.getLineEditors().add(notesLE);
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
		if (DataSourceData.RCV_DEFAULT_DS == null) {
			DataSourceData.provideDefaultReceiveDS(entityTypeDAOService, dataSourceDAOService, em);
		}

		return DataSourceData.RCV_DEFAULT_DS;
	}

	private static DataSource getBasicFormRCVDS(IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService, EntityManager em) throws Exception {
		if (DataSourceData.RCV_BASIC_DS == null) {
			DataSourceData.provideBasicFormReceiveDS(entityTypeDAOService, dataSourceDAOService, em);
		}

		return DataSourceData.RCV_BASIC_DS;
	}

	/**
	 * 
	 * 
	 * Generic DS's
	 * 
	 */
	private static DataSource getFileEntryDS(IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService, EntityManager em) throws Exception {
		if (DataSourceData.FE_DS == null) {
			DataSourceData.provideFileEntryDS(entityTypeDAOService, dataSourceDAOService, em);
		}

		return DataSourceData.FE_DS;
	}

	private static DataSource getNoteItemDS(IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService, EntityManager em) throws Exception {
		if (DataSourceData.NI_DS == null) {
			DataSourceData.provideNoteItemDS(entityTypeDAOService, dataSourceDAOService, em);
		}

		return DataSourceData.NI_DS;
	}

	private static DataSource getReferenceNumberDS(IEntityTypeDAOService entityTypeDAOService, IDataSourceDAOService dataSourceDAOService, EntityManager em) throws Exception {
		if (DataSourceData.RN_DS == null) {
			DataSourceData.provideReferenceNumberDS(entityTypeDAOService, dataSourceDAOService, em);
		}

		return DataSourceData.RN_DS;
	}
}