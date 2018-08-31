package org.hbin.gobang.util;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Close {
	public static void close(JFrame frame){
		int result = JOptionPane.showConfirmDialog(frame, "确定要退出？", "退出",
				JOptionPane.OK_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			System.exit(0);
		}
	}
}
