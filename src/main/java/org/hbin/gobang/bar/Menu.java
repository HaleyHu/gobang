package org.hbin.gobang.bar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;
import org.hbin.gobang.frame.ChessBoard;
import org.hbin.gobang.frame.ChessHistoryFrame;
import org.hbin.gobang.util.Close;

public class Menu extends JMenuBar {
	private static final Logger log = Logger.getLogger(Menu.class);
	private static final long serialVersionUID = -7347867316427538455L;
	private JMenu gameMenu, viewMenu, helpMenu;
	private JMenuItem startItem, exitItem;
	private JMenuItem viewItem;
	private JMenuItem helpItem, aboutItem;
	
	private ChessHistoryFrame chessHistory;
	private ChessBoard chessBoard;
	
	public Menu(ChessBoard chessBoard) {
		this.chessBoard = chessBoard;
		init();
	}

	private void init() {
		Icon startImage = new ImageIcon(this.getClass().getResource("/images/start.png"));
		Icon exitImage = new ImageIcon(this.getClass().getResource("/images/exit.png"));
		Icon helpImage = new ImageIcon(this.getClass().getResource("/images/help.png"));
		Icon aboutImage = new ImageIcon(this.getClass().getResource("/images/about.png"));
		
		//游戏菜单
		gameMenu = new JMenu("游戏(G)");
		startItem = new JMenuItem("开始(S)", startImage);
		exitItem = new JMenuItem("退出(X)", exitImage);
		
		gameMenu.setMnemonic(KeyEvent.VK_G);
		startItem.setMnemonic(KeyEvent.VK_S);
		exitItem.setMnemonic(KeyEvent.VK_X);
		
		startItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				log.info("startItem clicked.");
				chessBoard.getGameInfo().init();
			}
		});
		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				log.info("exitItem clicked.");
				Close.close(chessBoard);
			}
		});
		
		gameMenu.add(startItem);
		gameMenu.add(exitItem);
		
		add(gameMenu);
		
		//查看菜单
		viewMenu = new JMenu("查看(V)");
		viewItem = new JMenuItem("查看棋谱(P)");
		viewMenu.setMnemonic(KeyEvent.VK_V);
		viewItem.setMnemonic(KeyEvent.VK_P);
		viewItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chessHistory = new ChessHistoryFrame();
				chessHistory.initData(chessBoard.getGameInfo().getSteps());
			}
		});
		viewMenu.add(viewItem);
		add(viewMenu);
		
		//帮助菜单
		helpMenu = new JMenu("帮助(H)");
		helpItem = new JMenuItem("查看帮助(H)", helpImage);
		aboutItem = new JMenuItem("关于(A)", aboutImage);

		//设置快捷键和助记符
		helpMenu.setMnemonic(KeyEvent.VK_H);
		helpItem.setMnemonic(KeyEvent.VK_H);
		aboutItem.setMnemonic(KeyEvent.VK_A);
		helpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,  
                KeyEvent.CTRL_MASK));

		helpItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				log.info("helpItem clicked.");
				String title = "帮助";
				String content = "<html><div><br/>落子：双击落子。<br/><br/>祝您玩得开心！<br/><br/>五子棋<br/></div>";
				JOptionPane.showMessageDialog(chessBoard, content, title,
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		aboutItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				log.info("aboutItem clicked.");
				String title = "关于";
				String content = "<html><div>五子棋 v1.0<br/><br/><span style='color:red;font-size:20px'>五子棋</span><br/>" +
						"<br/>版本：<i>1.0</i><br/>作者：<i>斌斌</i><br/>邮箱：<i>cn.binbin@qq.com</i><br/><br/></div></html>";
				JOptionPane.showMessageDialog(chessBoard, content, title,
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		helpMenu.add(helpItem);
		helpMenu.addSeparator();
		helpMenu.add(aboutItem);
		
		add(helpMenu);
	}

	public ChessBoard getChessBoard() {
		return chessBoard;
	}
}
