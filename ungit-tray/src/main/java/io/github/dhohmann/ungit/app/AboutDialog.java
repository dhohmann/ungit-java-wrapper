package io.github.dhohmann.ungit.app;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EtchedBorder;

import io.github.dhohmann.ungit.ui.StatusBar;

public class AboutDialog extends JDialog {

	private static final long serialVersionUID = 5148697426574753720L;
	
	private StatusBar statusbar;

	public AboutDialog() {
		super((JFrame) null, "About");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		setSize(new Dimension(300, 200));
		setLocationRelativeTo(null);

		java.awt.EventQueue.invokeLater(() -> {
			setVisible(true);
		});

		buildUI();
		repaint();
	}

	private void buildUI() {
		Container c = getContentPane();
		c.setLayout(new BorderLayout());

		JPanel content = new JPanel();
		content.setBorder(javax.swing.BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		content.setLayout(new GridBagLayout());
		c.add(content, BorderLayout.CENTER);
		
		buildUITitle(content);
		buildUIUngitVersion(content);

		statusbar = new StatusBar("Loading...", "0:00");
		c.add(statusbar, BorderLayout.SOUTH);
	}
	
	private void buildUITitle(Container content) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.ipadx = 5;
		constraints.ipady = 5;
		constraints.gridx = 0;
		constraints.gridy = 0;
		content.add(new JLabel("title"), constraints);
	}
	
	private void buildUIUngitVersion(Container content) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.ipadx = 5;
		constraints.ipady = 5;
		constraints.gridx = 0;
		constraints.gridy = 1;
		content.add(new JLabel("version"), constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 1;
		content.add(new JLabel("1.5.1"), constraints);
	}

	public void t() {
		JPanel processIndicator = new JPanel();
		processIndicator.setLayout(new GridBagLayout());
		JProgressBar processbar = new JProgressBar();
		processbar.setIndeterminate(true);
		processIndicator.add(processbar, new GridBagConstraints());
		getContentPane().add(processIndicator);
	}
}
