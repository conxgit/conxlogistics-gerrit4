package com.conx.logistics.web.tools;

import java.util.Collection;

import com.vaadin.terminal.Paintable;
import com.vaadin.terminal.gwt.widgetsetutils.ClassPathExplorer;

public class ExplorerWidgetsets {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Collection<Class<? extends Paintable>> ans = ClassPathExplorer.getPaintablesHavingWidgetAnnotation();
		for (Class<? extends Paintable> cls : ans)
		{
			System.out.println(cls.getName());
		}
	}

}
