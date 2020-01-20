package io.github.dhohmann.ungit.ui.layout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FormElement extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel label;
	private JComponent component;
	
	public FormElement(String text) {
		this(new JLabel(text));
	}

	public FormElement(JComponent component) {
	}
	
	public FormElement(String label, String text) {
		this(label, new JLabel(text));
	}
	
	public FormElement(String label, JComponent component) {

	}

}
