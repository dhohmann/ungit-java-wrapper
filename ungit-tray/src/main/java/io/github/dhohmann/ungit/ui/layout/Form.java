package io.github.dhohmann.ungit.ui.layout;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class Form extends JPanel {

	private static final long serialVersionUID = 1L;

	private List<FormGroup> groups = new ArrayList<>();

	public Form() {
		
	}

	public void addGroup(FormGroup group) {
		if(!groups.contains(group)) {
			groups.add(group);
		}
	}
	
}
