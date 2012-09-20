package com.conx.logistics.kernel.ui.components.domain.page;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.conx.logistics.kernel.ui.components.domain.AbstractConXComponent;

@Entity
public class TaskPage extends AbstractConXComponent {
	private static final long serialVersionUID = -89799981390933973L;
	
	@OneToOne
	private AbstractConXComponent content;

	public TaskPage() {
		super("taskpage");
	}
	
	public TaskPage(AbstractConXComponent content) {
		this();
		this.content = content;
	}

	public AbstractConXComponent getContent() {
		return content;
	}

	public void setContent(AbstractConXComponent content) {
		this.content = content;
	}

}
