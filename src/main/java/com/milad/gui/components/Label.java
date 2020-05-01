package com.milad.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

public class Label extends JComponent {
	private static final long serialVersionUID = 1L;

	private String value;
	private Color color;

	private List<String> lines;

	public Label() {
		value = "";
	}

	public Label(String text) {
		setText(text);
	}

	public void setText(String text) {
		if (text == null) {
			value = "";
		} else {
			value = text;
		}
	}
	
	public String getText() {
		return value;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		if (color != null) {
			g2.setPaint(color);
		}

		lines = split(g2);

		FontRenderContext context = g2.getFontRenderContext();
		Font font = g2.getFont();
		Rectangle2D bounds = font.getStringBounds(lines.get(0), context);

		int x = 0;
		int y = 0;

		if (getAlignmentY() == CENTER_ALIGNMENT) {
			y += (int) (getHeight() - bounds.getHeight() * lines.size()) / 2;
		} else if (getAlignmentY() == BOTTOM_ALIGNMENT) {
			y += (int) (getHeight() - bounds.getHeight() * lines.size());
		}

		for (int i = 0; i < lines.size(); i++) {
			bounds = font.getStringBounds(lines.get(i), context);
			
			if (getAlignmentX() == CENTER_ALIGNMENT) {
				x = (int) ((getWidth() - bounds.getWidth()) / 2);
			} else if (getAlignmentX() == RIGHT_ALIGNMENT) {
				x = (int) (getWidth() - bounds.getWidth());
			}

			y += bounds.getHeight();
			g2.drawString(lines.get(i), x, y);
		}
	}

	public List<String> split(Graphics2D g) {
		ArrayList<String> lines = new ArrayList<>();

		int prev = 0;
		for (int i = 0; i < value.length(); i++) {
			if (value.charAt(i) == '\n') {
				lines.add(value.substring(prev, i));
				prev = i + 1;
			}
		}
		lines.add(value.substring(prev, value.length()));

		FontRenderContext context = g.getFontRenderContext();
		Font font = g.getFont();

		int limit = findMaxLength(value, font.getStringBounds(value, context).getWidth(), getWidth());
		Rectangle2D bounds;
		String temp;
		for (int i = 0; i < lines.size(); i++) {
			temp = lines.get(i);
			bounds = font.getStringBounds(temp, context);

			if (bounds.getWidth() > getWidth()) {
				int j;
				for (j = limit; j > 0; j--) {
					if (Character.isWhitespace(temp.charAt(j))) {
						String leftPart = temp.substring(0, j);
						if (font.getStringBounds(leftPart, context).getWidth() > getWidth())
							continue;

						lines.set(i, leftPart);
						lines.add(i + 1, temp.substring(j + 1));
						break;
					}
				}

				if (j == 0) {
					int newLen = limit - 3;
					if (newLen <= temp.length() && newLen >= 0) {
						temp = temp.substring(0, newLen).concat("...");
					}

					lines.set(i, temp);
				}
			}
		}

		lines.trimToSize();
		return lines;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	private int findMaxLength(String str, double strWidth, double compWidth) {
		return (int) (compWidth / (strWidth / countVisibleCharacters(str)));
	}

	private int countVisibleCharacters(String s) {
		int count = 0;

		for (char c : s.toCharArray()) {
			if (!Character.isISOControl(c)) {
				count++;
			}
		}

		return count;
	}

	public Dimension getPreferredSize() {
		Graphics2D g = (Graphics2D) getGraphics();

		FontRenderContext context = g.getFontRenderContext();
		Rectangle2D bounds = g.getFont().getStringBounds(value, context);

		return new Dimension((int) bounds.getWidth(), (int) bounds.getHeight());
	}
	
	public boolean equals(Object otherObject) {
		if (otherObject == this) {
			return true;
		}
		
		if (otherObject == null || getClass() != otherObject.getClass()) {
			return false;
		}
		
		Label other = (Label) otherObject;
		
		return value.equals(other.getText());
	}
	
	public int hashCode() {
		return value.hashCode() * 17 + 2;
	}
	
	public String toString() {
		return "Label[text=\"" + value + "\"]";
	}
}
