package com.milad.gui.components;

import java.awt.Rectangle;

import javax.swing.JPanel;

public class AutoscrollSafePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private boolean preventAutroscroll;

	@Override
	public void scrollRectToVisible(Rectangle rect) {
		if (!preventAutroscroll) {
			super.scrollRectToVisible(rect);
		}
	}
	
	public void setPreventAutoscroll(boolean preventAutosctoll) {
		this.preventAutroscroll = preventAutosctoll;
	}
}
