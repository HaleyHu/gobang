package org.hbin.game.gobang.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BarStatus extends JPanel {
	private static final long serialVersionUID = 1277087990006330578L;
	private JLabel infoLabel;
	
	public BarStatus() {
		init();
	}

	private void init() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(50, 25));
		infoLabel = new JLabel("下一步：黑棋", JLabel.LEFT);
		setBackground(Color.LIGHT_GRAY);
		add(infoLabel);
	}
	
	public void setText(String info) {
		if (info != null) {
			infoLabel.setText(" " + info);
		}
	}
}
