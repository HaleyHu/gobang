package org.hbin.gobang.frame;

import java.awt.TextArea;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.hbin.gobang.bean.Step;

public class ChessHistoryFrame extends JFrame {
	private static final long serialVersionUID = -4606184129319731374L;
	private TextArea textArea;
	
	public ChessHistoryFrame() {
		init();
	}
	
	private void init() {
		textArea = new TextArea(10, 20);
		textArea.setEditable(false);
		JScrollPane jsp = new JScrollPane(textArea);
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(jsp);
		
		setTitle("查看棋谱");
		setSize(200, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	public void initData(List<Step> steps) {
		System.out.println("clean");
		textArea.setText("");
		if(steps == null || steps.size() == 0) {
			System.out.println("null or empty");
			textArea.append("对局还没有开始呢~");
		} else {
			System.out.println("append: " + steps.size());
			for (int i = 0; i < steps.size(); i++) {
				textArea.append((i + 1) + "\t" + steps.get(i).toString() + "\r\n");
			}
		}
	}
}
