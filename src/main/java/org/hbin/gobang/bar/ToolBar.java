package org.hbin.gobang.bar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import org.apache.log4j.Logger;
import org.hbin.gobang.frame.ChessBoard;
import org.hbin.gobang.util.Close;

public class ToolBar extends JToolBar {
	private static final Logger log = Logger.getLogger(ToolBar.class);
	
	private static final long serialVersionUID = 6541578925743580898L;
	
	private JButton startBtn;
	private JButton exitBtn;
	
	private ChessBoard chessBoard;

	public ToolBar(ChessBoard chessBoard) {
		this.chessBoard = chessBoard;
		
		init();
	}

	private void init() {
		Icon startImage = new ImageIcon(this.getClass().getResource("/images/start.png"));
		Icon exitImage = new ImageIcon(this.getClass().getResource("/images/exit.png"));
		
		startBtn = new JButton(startImage);
		exitBtn = new JButton(exitImage);
		
		startBtn.setToolTipText("start");
		exitBtn.setToolTipText("exit");
		startBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				log.info("startBtn clicked.");
				chessBoard.getGameInfo().init();
			}
		});
		
		exitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				log.info("exitBtn clicked.");
				Close.close(chessBoard);
			}
		});
		
		add(startBtn);
		add(exitBtn);
		setFloatable(false);
	}
}