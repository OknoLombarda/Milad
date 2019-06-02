package com.milad.gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public class GBC extends GridBagConstraints {
	private static final long serialVersionUID = 546863289045908743L;

	public GBC(int gridx, int gridy) {
		this.gridx = gridx;
		this.gridy = gridy;
	}
	
	public GBC(int gridx, int gridy, int gridwidth, int gridheight) {
		this(gridx, gridy);
		this.gridwidth = gridwidth;
		this.gridheight = gridheight;
	}
	
	public GBC setAnchor(int anchor) {
		this.anchor = anchor;
		return this;
	}
	
	public GBC setFill(int fill) {
		this.fill = fill;
		return this;
	}
	
	public GBC setWeight(double weightx, double weighty) {
		this.weightx = weightx;
		this.weighty = weighty;
		return this;
	}
	
	public GBC setInsets(int distance) {
		this.insets = new Insets(distance, distance, distance, distance);
		return this;
	}
	
	public GBC setInsets(int top, int left, int bottom, int right) {
		this.insets = new Insets(top, left, bottom, right);
		return this;
	}
	
	public GBC setIpad(int ipadx, int ipady) {
		this.ipadx = ipadx;
		this.ipady = ipady;
		return this;
	}
}

