package org.hbin.game.gobang;

import java.awt.EventQueue;

import javax.swing.JFrame;

import org.hbin.game.gobang.component.MainFrame;

public class Gobang {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame f = new MainFrame();
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f.setVisible(true);
			}
		});
	}
}
