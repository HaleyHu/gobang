package org.hbin.gobang.frame;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.hbin.gobang.bar.Menu;
import org.hbin.gobang.bar.ToolBar;
import org.hbin.gobang.game.GameInfo;
import org.hbin.gobang.listener.GameMouseAdapter;
import org.hbin.gobang.util.Constants;

public class ChessBoard extends JFrame{

	private static final long serialVersionUID = -4917106135096729844L;
	private MainPanel mainPanel;
	private GameInfo gameInfo;
	
	public ChessBoard() {
		init();
	}
	
	public GameInfo getGameInfo() {
		return gameInfo;
	}
	
	public MainPanel getMainPanel() {
		return mainPanel;
	}
	
	private void init() {
		setTitle("五子棋");
		setSize(Constants.CHESSBOARD_WIDTH, Constants.CHESSBOARD_HEIGHT);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(new ImageIcon(this.getClass().getResource("/images/logo.png")).getImage());
		
		//初始化菜单栏和工具栏
		setJMenuBar(new Menu(this));
		add(new ToolBar(this), BorderLayout.PAGE_START);
		GameMouseAdapter adapter = new GameMouseAdapter(this);
		addMouseListener(adapter);
		addMouseMotionListener(adapter);

		//初始化主面板
		mainPanel = new MainPanel(this);
		add(mainPanel, BorderLayout.CENTER);
		
		gameInfo = new GameInfo(this);
		
		setVisible(true);
	}
}