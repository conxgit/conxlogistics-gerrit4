package com.conx.logistics.kernel.ui.components.domain.masterdetail;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.conx.logistics.kernel.ui.components.domain.AbstractConXComponent;

@Entity
public class LineEditorComponent extends AbstractConXComponent {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	private LineEditorContainerComponent mainComponent;
	
	@OneToOne 
	private AbstractConXComponent content;
	
	private int ordinal;
	
	public LineEditorComponent(String code, String caption,LineEditorContainerComponent mainComponent, int ordinal) {
		super("lineeditorcomponent");
		this.ordinal = ordinal;
		setCode(code);
		setName(caption);
		setCaption(caption);
		this.mainComponent = mainComponent;
	}
	
	public LineEditorComponent(String code, String caption,LineEditorContainerComponent mainComponent) {
		this();
		setCode(code);
		setName(caption);
		setCaption(caption);
		this.mainComponent = mainComponent;
	}

	public LineEditorComponent() {
		super("lineeditorcomponent");
		this.ordinal = -1;
	}

	public LineEditorComponent(LineEditorContainerComponent mainComponent) {
		this();
		this.mainComponent = mainComponent;
	}

	public LineEditorContainerComponent getMainComponent() {
		return mainComponent;
	}

	public void setMainComponent(LineEditorContainerComponent mainComponent) {
		this.mainComponent = mainComponent;
	}

	public AbstractConXComponent getContent() {
		return content;
	}

	public void setContent(AbstractConXComponent content) {
		this.content = content;
	}
	
	public int getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}
}
