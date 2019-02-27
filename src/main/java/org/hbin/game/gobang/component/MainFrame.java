package org.hbin.game.gobang.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import org.hbin.game.gobang.util.SwingUtil;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = -3323955636588028668L;
	private JToolBar toolbar;
	private JButton startButton, backButton, exitButton;
	
	private ChessBoard boardPanel;
	private BarStatus barStatus;
	
	public MainFrame() {
		setTitle("单机版五子棋");
		
		toolbar = new JToolBar("工具栏");
		startButton = new JButton("重新开始");
		backButton = new JButton("悔棋");
		exitButton = new JButton("退出");
		ActionListener l = new ActionMonitor();
		startButton.addActionListener(l);
		backButton.addActionListener(l);
		exitButton.addActionListener(l);
		toolbar.setMargin(new Insets(5, 5, 5, 5));
		toolbar.setFloatable(false);
		
		Dimension separatorSize = new Dimension(10, 0);
		toolbar.add(startButton);
		toolbar.addSeparator(separatorSize);
		toolbar.add(backButton);
        toolbar.addSeparator(separatorSize);
		toolbar.add(exitButton);
		
		add(toolbar, BorderLayout.NORTH);
		
		boardPanel = new ChessBoard();
		add(boardPanel);
		
		barStatus = new BarStatus();
		add(barStatus, BorderLayout.SOUTH);
		
		pack();
		setResizable(false);
		
		SwingUtil.center(this);
	}
	
	public ChessBoard getBoardPanel() {
		return boardPanel;
	}

	public BarStatus getBarStatus() {
		return barStatus;
	}

	class ActionMonitor implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
//			if(e.getSource() == startButton) {
//				boardPanel.restartGame();
//			} else if(e.getSource() == backButton) {
//				boardPanel.goBack();
//			} else if(e.getSource() == exitButton) {
//				System.exit(0);
//			}
		}
	}
}
