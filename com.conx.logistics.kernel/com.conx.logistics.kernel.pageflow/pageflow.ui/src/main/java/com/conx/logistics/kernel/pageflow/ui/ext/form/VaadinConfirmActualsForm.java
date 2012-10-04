package com.conx.logistics.kernel.pageflow.ui.ext.form;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.conx.logistics.kernel.ui.components.domain.form.ConfirmActualsFieldSet;
import com.conx.logistics.kernel.ui.components.domain.form.ConfirmActualsFieldSetField;
import com.conx.logistics.kernel.ui.components.domain.form.ConfirmActualsForm;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.EntityEditorToolStrip;
import com.conx.logistics.kernel.ui.editors.entity.vaadin.ext.EntityEditorToolStrip.EntityEditorToolStripButton;
import com.conx.logistics.kernel.ui.forms.vaadin.FormMode;
import com.conx.logistics.kernel.ui.forms.vaadin.impl.VaadinForm;
import com.conx.logistics.kernel.ui.forms.vaadin.impl.VaadinFormAlertPanel;
import com.conx.logistics.kernel.ui.forms.vaadin.impl.VaadinFormFieldAugmenter;
import com.conx.logistics.kernel.ui.forms.vaadin.impl.VaadinFormHeader;
import com.conx.logistics.kernel.ui.forms.vaadin.impl.VaadinJPAFieldFactory;
import com.conx.logistics.kernel.ui.forms.vaadin.listeners.IFormChangeListener;
import com.vaadin.data.Item;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.event.MouseEvents;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class VaadinConfirmActualsForm extends VaadinForm {
	private static final long serialVersionUID = -639931L;

	private EntityEditorToolStrip toolStrip;
	private EntityEditorToolStripButton verifyButton;
	private EntityEditorToolStripButton saveButton;
	private EntityEditorToolStripButton resetButton;
	private VaadinFormHeader header;
	private VaadinFormAlertPanel alertPanel;
	private VerticalLayout layout;
	private Panel innerLayoutPanel;
	private GridLayout innerLayout;
	private GridLayout captionLayout;
	private GridLayout expectedLayout;
	private GridLayout actualLayout;
	private ConfirmActualsForm componentForm;
	private Set<Field> fields;
	private Set<IFormChangeListener> formChangeListeners;
	private HashMap<ConfirmActualsFieldSetField, Integer> fieldIndexMap;
	private int nextIndex;
	private Embedded placeholder;

	private FormMode mode;

	public VaadinConfirmActualsForm(ConfirmActualsForm componentForm) {
		this.header = new VaadinFormHeader();
		this.layout = new VerticalLayout();
		this.innerLayout = new GridLayout(3, 1);
		this.captionLayout = new GridLayout(1, 1);
		this.expectedLayout = new GridLayout(1, 1);
		this.actualLayout = new GridLayout(1, 1);
		this.alertPanel = new VaadinFormAlertPanel();
		this.componentForm = componentForm;
		this.formChangeListeners = new HashSet<IFormChangeListener>();
		this.fields = new HashSet<Field>();
		this.fieldIndexMap = new HashMap<ConfirmActualsFieldSetField, Integer>();
		this.nextIndex = 1;
		this.placeholder = new Embedded();

		initialize();
	}

	@SuppressWarnings("deprecation")
	private void initialize() {
		this.placeholder.setHeight("22px");
		this.placeholder.setWidth("1px");
		
		this.toolStrip = new EntityEditorToolStrip();

		this.verifyButton = this.toolStrip.addToolStripButton(EntityEditorToolStrip.TOOLSTRIP_IMG_VERIFY_PNG);
		this.verifyButton.setEnabled(false);
		this.verifyButton.addListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				validateForm();
			}
		});

		this.saveButton = this.toolStrip.addToolStripButton(EntityEditorToolStrip.TOOLSTRIP_IMG_SAVE_PNG);
		this.saveButton.setEnabled(false);
		this.saveButton.addListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
			}
		});

		this.resetButton = this.toolStrip.addToolStripButton(EntityEditorToolStrip.TOOLSTRIP_IMG_RESET_PNG);
		this.resetButton.setEnabled(true);
		this.resetButton.addListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				resetForm();
			}
		});
		
		this.alertPanel.setVisible(false);
		this.alertPanel.addCloseListener(new MouseEvents.ClickListener() {
			private static final long serialVersionUID = 5815832688929242745L;

			@Override
			public void click(MouseEvents.ClickEvent event) {
				VaadinConfirmActualsForm.this.alertPanel.setVisible(false);
			}
		});
		
		this.captionLayout.setWidth("100%");
		this.captionLayout.setSpacing(true);

		this.expectedLayout.setWidth("100%");
		this.expectedLayout.setSpacing(true);

		this.actualLayout.setWidth("100%");
		this.actualLayout.setSpacing(true);

		this.innerLayout.setWidth("65%");
		this.innerLayout.setSpacing(true);
		this.innerLayout.setMargin(true);
		this.innerLayout.addComponent(captionLayout, 0, 0, 0, 0);
		this.innerLayout.addComponent(expectedLayout, 1, 0, 1, 0);
		this.innerLayout.addComponent(actualLayout, 2, 0, 2, 0);
		this.innerLayout.setColumnExpandRatio(0, 0.166f);
		this.innerLayout.setColumnExpandRatio(1, 0.417f);
		this.innerLayout.setColumnExpandRatio(2, 0.417f);

		this.innerLayoutPanel = new Panel();
		this.innerLayoutPanel.setSizeFull();
		this.innerLayoutPanel.getLayout().setMargin(false, true, false, true);
		this.innerLayoutPanel.setStyleName("light");
		this.innerLayoutPanel.addComponent(innerLayout);

		this.layout.setSizeFull();
		this.layout.setStyleName("conx-entity-editor-form");
		this.layout.addComponent(toolStrip);
		this.layout.addComponent(header);
		this.layout.addComponent(alertPanel);
		this.layout.addComponent(innerLayoutPanel);
		this.layout.setExpandRatio(innerLayoutPanel, 1.0f);

		setImmediate(true);
		setSizeFull();
		setFormMode(FormMode.EDITING);
		setFormFieldFactory(new VaadinJPAFieldFactory());
		setLayout(layout);
		// False so that commit() must be called explicitly
		setWriteThrough(false);
		// Disallow invalid data from acceptance by the container
		setInvalidCommitted(false);
	}

	public FormMode getMode() {
		return mode;
	}

	public void setFormMode(FormMode mode) {
		this.mode = mode;
		switch (mode) {
		case CREATING:
			this.header.setAction("Creating");
			break;
		case EDITING:
			this.header.setAction("Editing");
			break;
		}
	}

	public void setTitle(String title) {
		this.header.setTitle(title);
	}

	@Override
	protected void attachField(Object propertyId, com.vaadin.ui.Field field) {
		if (propertyId == null || field == null) {
			return;
		}
		ConfirmActualsFieldSet fieldSet = componentForm.getFieldSet();
		if (fieldSet != null) {
			ConfirmActualsFieldSetField fieldComponent = fieldSet.getFieldSetField((String) propertyId);
			if (fieldComponent != null) {
				Integer index = fieldIndexMap.get(fieldComponent);
				if (index == null) {
					index = nextIndex;
					nextIndex++;
					fieldIndexMap.put(fieldComponent, index);
				}

				fields.add(field);
				field.setWidth("100%");
				VaadinFormFieldAugmenter.augment(field, fieldComponent, new ValueChangeListener() {
					private static final long serialVersionUID = -6182433271255560793L;

					@Override
					public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
						onFormChanged();
					}
				});

				if (fieldSet.isExpected(propertyId)) {
					if (index >= this.expectedLayout.getRows()) {
						this.expectedLayout.setRows(index + 1);
					}
					
					if (index >= this.captionLayout.getRows()) {
						this.captionLayout.setRows(index + 1);
					}
					
					Label label = new Label(field.getCaption());
					label.setStyleName("conx-confirm-actuals-field-caption");
					label.setWidth("100%");
					captionLayout.addComponent(label, 0, index, 0, index);
					
					field.setEnabled(false);
					field.setCaption(null);
					expectedLayout.addComponent(field, 0, index, 0, index);
				} else {
					if (index >= this.actualLayout.getRows()) {
						this.actualLayout.setRows(index + 1);
					}
					
					field.setCaption(null);
					actualLayout.addComponent(field, 0, index, 0, index);
				}
			}
		}
	}

	public void addFormChangeListener(IFormChangeListener listener) {
		this.formChangeListeners.add(listener);
	}

	@Override
	public void setItemDataSource(Item newDataSource, Collection<?> propertyIds) {
		this.nextIndex = 1;
		this.fieldIndexMap.clear();
		this.captionLayout.removeAllComponents();
		this.captionLayout.addComponent(this.placeholder, 0, 0, 0, 0);
		this.expectedLayout.removeAllComponents();
		this.expectedLayout.addComponent(new VaadinConfirmActualsFormSectionHeader("Expected"), 0, 0, 0, 0);
		this.actualLayout.removeAllComponents();
		this.actualLayout.addComponent(new VaadinConfirmActualsFormSectionHeader("Actual"), 0, 0, 0, 0);
		this.fields.clear();
		super.setItemDataSource(newDataSource, propertyIds);
	}

	@Override
	public void setItemDataSource(Item newDataSource) {
		this.nextIndex = 1;
		this.fieldIndexMap.clear();
		this.captionLayout.removeAllComponents();
		this.captionLayout.addComponent(this.placeholder, 0, 0, 0, 0);
		this.expectedLayout.removeAllComponents();
		this.expectedLayout.addComponent(new VaadinConfirmActualsFormSectionHeader("Expected"), 0, 0, 0, 0);
		this.actualLayout.removeAllComponents();
		this.actualLayout.addComponent(new VaadinConfirmActualsFormSectionHeader("Actual"), 0, 0, 0, 0);
		this.fields.clear();
		super.setItemDataSource(newDataSource);
	}

	public FormMode getFormMode() {
		return mode;
	}

	public void onFormChanged() {
		for (IFormChangeListener listener : this.formChangeListeners) {
			listener.onFormChanged();
		}
		this.saveButton.setEnabled(false);
		this.verifyButton.setEnabled(true);
		this.resetButton.setEnabled(true);
	}

	public void resetForm() {
		this.alertPanel.setVisible(false);
		setItemDataSource(getItemDataSource());
		this.saveButton.setEnabled(false);
		this.verifyButton.setEnabled(false);
		this.resetButton.setEnabled(false);
	}

	public boolean validateForm() {
		boolean firstErrorFound = false;
		for (Field field : fields) {
			try {
				field.validate();
				field.removeStyleName("conx-form-field-error");
			} catch (InvalidValueException e) {
				field.addStyleName("conx-form-field-error");
				if (!firstErrorFound) {
					this.alertPanel.setMessage(e.getMessage());
					this.alertPanel.setVisible(true);
					firstErrorFound = true;
				}
			}
		}
		if (firstErrorFound) {
			this.verifyButton.setEnabled(false);
			return false;
		} else {
			this.alertPanel.setVisible(false);
			this.verifyButton.setEnabled(false);
			this.saveButton.setEnabled(true);
			return true;
		}
	}
	
	public Object getItemEntity() {
		return null;
	}

	public ConfirmActualsForm getComponentModel() {
		return this.componentForm;
	}
}
