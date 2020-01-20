package io.github.dhohmann.ungit.ui;

import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

public class StatusBar extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel statusLabel;
	private JLabel infoLabel;

	public StatusBar() {
		setBorder(javax.swing.BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		setLayout(new BorderLayout());
		
		statusLabel = new JLabel();
		add(statusLabel, BorderLayout.WEST);
		
		infoLabel = new JLabel();
		add(infoLabel, BorderLayout.EAST);
	}
	
	public StatusBar(String status) {
		this();
		
		setStatus(status);
	}
	public StatusBar(String status, String info) {
		this();
		
		setStatus(status);
		setInfo(info);
	}

	public void setStatus(String status) {
		statusLabel.setText(status);
	}
	
	public void setIcon(Icon icon) {
		statusLabel.setIcon(icon);
	}
	
	public void setInfo(String info) {
		infoLabel.setText(info);
	}
	

}
