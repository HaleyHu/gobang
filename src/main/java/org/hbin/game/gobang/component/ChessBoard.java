package org.hbin.game.gobang.component;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.hbin.game.gobang.client.GobangClient;

public class ChessBoard extends JPanel {
	private static final long serialVersionUID = 1606271597348141300L;
	/** 边距 */
	public static final int MARGIN = 15;
	/** 网格宽度 */
	public static final int SPAN = 30;
	/** 棋盘行数 */
	public static final int ROWS = 15;
	/** 棋盘列数 */
	public static final int COLS = 15;
	/** 棋盘星位的坐标 */
	public static final int[][] STARS = {{3, 3}, {COLS - 4, 3}, {COLS / 2, ROWS / 2}, {3, ROWS - 4}, {COLS - 4, ROWS - 4}};
	
	/** 记录已经下在棋盘上的棋子的数组 */
	private List<Chess> chessList;
	
	private Chess temp;
	
//	/** 当前棋盘上棋子的个数 */
//	private int chessCount;
	/** 下一步轮到哪一方下棋，默认开始是黑棋先 */
	private boolean isBlack = true;
	/** 是否正在游戏 */
	private boolean isGamming;
	
	private boolean isTurn = false;
	
	private Image img;
	
//	private MainFrame mainFrame;
	
	private GobangClient client;

    public ChessBoard(GobangClient client) {
        this.client = client;
		//img = Toolkit.getDefaultToolkit().getImage("images/board.jpg");
		img = new ImageIcon(getClass().getResource("/images/gobang/board.jpg")).getImage();
		chessList = new ArrayList<>();
		addMouseListener(new MouseMonitor());
		addMouseMotionListener(new MouseMotionMonitor());
	}
	
	public void setBlack(boolean isBlack) {
        this.isBlack = isBlack;
    }

    public boolean isGamming() {
        return isGamming;
    }

    public boolean isTurn() {
        return isTurn;
    }

    public void setGamming(boolean isGamming) {
        this.isGamming = isGamming;
    }

    @Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(img, 0, 0, this);
		
		//画横线
		for (int i = 0; i < ROWS; i++) {
			g.drawLine(MARGIN, MARGIN + i * SPAN, MARGIN + (COLS - 1) * SPAN, MARGIN + i * SPAN);
		}
		
		//画竖线
		for (int i = 0; i < COLS; i++) {
			g.drawLine(MARGIN + i * SPAN, MARGIN, MARGIN + i * SPAN, MARGIN + (ROWS - 1) * SPAN);
		}
		
		// 画星位
		for (int i = 0; i < STARS.length; i++) {
			g.fillRect(MARGIN + STARS[i][0] * SPAN - 2, MARGIN + STARS[i][1] * SPAN - 2, 5, 5);
		}
		
		// 画棋子
		for (int i = 0; i < chessList.size(); i++) {
			chessList.get(i).draw(g);
			if (i == chessList.size() - 1) {
				int xPos = chessList.get(i).getCol() * SPAN + MARGIN;
				int yPos = chessList.get(i).getRow() * SPAN + MARGIN;
				g.setColor(Color.red);
				g.drawRect(xPos - Chess.DIAMETER / 2, yPos - Chess.DIAMETER / 2, Chess.DIAMETER, Chess.DIAMETER);
			}
		}
		
		if(temp != null) {
			temp.draw(g);
			int xPos = temp.getCol() * SPAN + MARGIN;
			int yPos = temp.getRow() * SPAN + MARGIN;
			g.setColor(Color.GREEN);
			g.drawRect(xPos - Chess.DIAMETER / 2, yPos - Chess.DIAMETER / 2, Chess.DIAMETER, Chess.DIAMETER);
		}
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(MARGIN * 2 + SPAN * (COLS - 1), MARGIN * 2 + SPAN * (ROWS - 1));
	}
	
	/**
	 * 检测位置（col, row）是否已经有棋子
	 * @param col
	 * @param row
	 * @return
	 */
	private boolean hasChess(int col, int row) {
		for (int i = 0; i < chessList.size(); i++) {
			Chess chess = chessList.get(i);
			if(chess != null && chess.getCol() == col && chess.getRow() == row) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 检测位置（col, row）是否有黑子或白子
	 * @param col
	 * @param row
	 * @param color
	 * @return
	 */
	private boolean hasChess(int col, int row, Color color) {
		for (int i = 0; i < chessList.size(); i++) {
			Chess chess = chessList.get(i);
			if(chess != null && chess.getCol() == col && chess.getRow() == row && chess.getColor() == color) {
				return true;
			}
		}
		return false;
	}
	
//	/**
//	 * 重新开始
//	 */
//	public void restartGame() {
//		// 消除棋子
//		chessList = new ArrayList<Chess>();
//		mainFrame.getBarStatus().setText("下一步：黑棋");
//		
//		//恢复游戏相关变量值
//		isBlack = true;
//		isGamming = true;
//		repaint();
//	}
	
//	/**
//	 * 悔棋
//	 */
//	public void goBack() {
//		if(chessList.size() == 0) {
//			return;
//		}
//		chessList.remove(chessList.size() - 1);
//		String name = isBlack ? "白棋" : "黑棋";
//		mainFrame.getBarStatus().setText("下一步：" + name);
//		
//		isBlack = !isBlack;
//		repaint();
//	}

	/**
	 * 判断胜负
	 * @param col
	 * @param row
	 * @return
	 */
	private boolean isWin(int col, int row) {
		Color color = isBlack ? Color.BLACK : Color.WHITE;
		return isWinHorizontal(col, row, color) || isWinVertical(col, row, color) || isWinLeftTop(col, row, color)
				|| isWinRightTop(col, row, color);
	}
	
	/**
	 * 横向
	 * @param col
	 * @param row
	 * @return
	 */
	private boolean isWinHorizontal(int col, int row, Color color) {
		int count = 1;
		for (int i = col - 1; i >= 0; i--) {
			if (hasChess(i, row, color)) {
				count++;
			} else {
				break;
			}
		}
		for (int i = col + 1; i < COLS; i++) {
			if (hasChess(i, row, color)) {
				count++;
			} else {
				break;
			}
		}
//		System.out.println("isWinHorizontal: count=" + count + ", " + (count >= 5));
		return count >= 5;
	}

	/**
	 * 纵向
	 * @param col
	 * @param row
	 * @return
	 */
	private boolean isWinVertical(int col, int row, Color color) {
		int count = 1;
		for (int i = row - 1; i >= 0; i--) {
			if (hasChess(col, i, color)) {
				count++;
			} else {
				break;
			}
		}
		for (int i = row + 1; i < ROWS; i++) {
			if (hasChess(col, i, color)) {
				count++;
			} else {
				break;
			}
		}
//		System.out.println("isWinVertical: " + count + ", " + (count >= 5));
		return count >= 5;
	}

	/**
	 * 往左上或右下
	 * @param col
	 * @param row
	 * @return
	 */
	private boolean isWinLeftTop(int col, int row, Color color) {
		int count = 1;
		for (int i = col - 1, j = row - 1; i >= 0 && j >= 0; i--, j--) {
			if (hasChess(i, j, color)) {
				count++;
			} else {
				break;
			}
		}
		for (int i = col + 1, j = row + 1; i < COLS && j < ROWS; i++, j++) {
			if (hasChess(i, j, color)) {
				count++;
			} else {
				break;
			}
		}
//		System.out.println("isWinLeftTop: " + count + ", " + (count >= 5));
		return count >= 5;
	}

	/**
	 * 往右上或左下
	 * @param col
	 * @param row
	 * @return
	 */
	private boolean isWinRightTop(int col, int row, Color color) {
		int count = 1;
		for (int i = col + 1, j = row - 1; i < COLS && j >= 0; i++, j--) {
			if (hasChess(i, j, color)) {
				count++;
			} else {
				break;
			}
		}
		for (int i = col - 1, j = row + 1; i >= 0 && j < ROWS; i--, j++) {
			if (hasChess(i, j, color)) {
				count++;
			} else {
				break;
			}
		}
//		System.out.println("isWinRightTop: " + count + ", " + (count >= 5));
		return count >= 5;
	}
	
	public void addOpponentChess(int col, int row) {
	    Chess chess = new Chess(col, row, isBlack ? Color.WHITE:Color.BLACK);
	    chessList.add(chess);
	    isTurn = true;
	    repaint();
	}
	
	public void gameOver(boolean win) {
	    resetGame();
        
        String template = win ? "恭喜，%s赢了！" : "遗憾，%s输了！";
        String msg = String.format(template, isBlack ? "黑棋":"白棋");
        JOptionPane.showMessageDialog(ChessBoard.this, msg);
	}
	
	public void resetGame() {
	    chessList = new ArrayList<>();
	    isGamming = false;
	    repaint();
	    client.getControlPanel().getJoinButton().setEnabled(true);
	}
	
	class MouseMonitor extends MouseAdapter {
		@Override
		public void mouseReleased(MouseEvent e) {
			System.out.println("mouseReleased");
			temp = null;
			action(e);
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
//			System.out.println("mousePressed");
			if (!isGamming) {
				return;
			}
            if (!isTurn) {
                return;
            }
			int col = (e.getX() - MARGIN + SPAN / 2) / SPAN;
			int row = (e.getY() - MARGIN + SPAN / 2) / SPAN;
			// 落在棋盘外不能下棋
			if(col < 0 || col >= COLS || row < 0 || row >= ROWS) {
				return;
			}
			// 如果已经有棋子不能下棋
			if(hasChess(col, row)) {
				return;
			}
			
			temp = new Chess(col, row, isBlack ? Color.BLACK : Color.WHITE);
			
			repaint();
		}

		private void action(MouseEvent e) {
			if (!isGamming) {
				return;
			}
            if (!isTurn) {
                return;
            }
			int col = (e.getX() - MARGIN + SPAN / 2) / SPAN;
			int row = (e.getY() - MARGIN + SPAN / 2) / SPAN;
			// 落在棋盘外不能下棋
			if(col < 0 || col >= COLS || row < 0 || row >= ROWS) {
				return;
			}
			// 如果已经有棋子不能下棋
			if(hasChess(col, row)) {
				return;
			}
			
			Chess chess = new Chess(col, row, isBlack ? Color.BLACK : Color.WHITE);
			chessList.add(chess);
			
			repaint();
			
			isTurn = false;
			client.getC().go(col, row);
			
			if(isWin(col, row)) {
			    client.getC().win();
//				String name = isBlack ? "黑棋":"白棋";
//				String msg = String.format("恭喜，%s赢了！", name);
//				mainFrame.getBarStatus().setText(msg);
//				JOptionPane.showMessageDialog(ChessBoard.this, msg);
//				isGamming = false;
//				return;
			}
//			
//			String name = isBlack ? "白棋" : "黑棋";
//			mainFrame.getBarStatus().setText("下一步：" + name);
			isBlack = !isBlack;
		}
	}
	
	class MouseMotionMonitor extends MouseMotionAdapter {
		@Override
		public void mouseMoved(MouseEvent e) {
			System.out.println("MouseMotionMonitor mouseMoved");
			int col = (e.getX() - MARGIN + SPAN / 2) / SPAN;
			int row = (e.getY() - MARGIN + SPAN / 2) / SPAN;
			if(col < 0 || col >= COLS || row < 0 || row >= ROWS || !isGamming || hasChess(col, row)) {
				ChessBoard.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			} else {
				ChessBoard.this.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			System.out.println("MouseMotionMonitor mouseDragged");
			
			if (!isGamming) {
				temp = null;
				repaint();
				return;
			}
			int col = (e.getX() - MARGIN + SPAN / 2) / SPAN;
			int row = (e.getY() - MARGIN + SPAN / 2) / SPAN;
			// 落在棋盘外不能下棋
			if(col < 0 || col >= COLS || row < 0 || row >= ROWS) {
				temp = null;
				repaint();
				return;
			}
			// 如果已经有棋子不能下棋
			if(hasChess(col, row)) {
				temp = null;
				repaint();
				return;
			}
			
			temp = new Chess(col, row, isBlack ? Color.BLACK : Color.WHITE);
			repaint();
		}
	}
}
