package com.conx.logistics.kernel.ui.vaadin.common;

import com.conx.logistics.kernel.ui.vaadin.common.gwt.client.ui.VConXSplitPanelVertical;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.ClientWidget.LoadStyle;

/**
 * A vertical split panel contains two components and lays them vertically. The
 * first component is above the second component.
 * 
 * <pre>
 *      +--------------------------+
 *      |                          |
 *      |  The first component     |
 *      |                          |
 *      +==========================+  <-- splitter
 *      |                          |
 *      |  The second component    |
 *      |                          |
 *      +--------------------------+
 * </pre>
 * 
 */
@SuppressWarnings("serial")
@ClientWidget(value = VConXSplitPanelVertical.class, loadStyle = LoadStyle.EAGER)
public class ConXVerticalSplitPanel extends ConXAbstractSplitPanel {

	public ConXVerticalSplitPanel() {
		super();
        setSizeFull();
	}
}
