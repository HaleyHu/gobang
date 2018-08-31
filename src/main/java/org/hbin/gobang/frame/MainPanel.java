package org.hbin.gobang.frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.hbin.gobang.game.GameInfo;
import org.hbin.gobang.util.Constants;

public class MainPanel extends JPanel {
	private static final long serialVersionUID = -1091601324704602253L;
	
	private static final int CELL = Constants.CHESSBOARD_CELL;
	private static final int CELL_SIZE = Constants.CHESSBOARD_CELL_SIZE;
	
	private static final int OFFSET_OUTER = Constants.CHESSBOARD_OFFSET_OUTER;
	private static final int OFFSET_INNER = Constants.CHESSBOARD_OFFSET_INNER;
	
	private static final int STAR_RADIUS = Constants.CHESSBOARD_STAR_RADIUS;

	private Image blackImage;
	private Image whiteImage;
	private Image newBlackImage;
	private Image newWhiteImage;
	
	private ChessBoard chessBoard;
	
	public MainPanel(ChessBoard chessBoard) {
		this.chessBoard = chessBoard;
		init();
	}
	
	private void init() {
		int size = (CELL - 1) * CELL_SIZE + OFFSET_OUTER * 2 + OFFSET_INNER * 2;
		setSize(size, size + 20);

		blackImage = new ImageIcon(this.getClass().getResource("/images/black_chess.png")).getImage();
		whiteImage = new ImageIcon(this.getClass().getResource("/images/white_chess.png")).getImage();
		newBlackImage = new ImageIcon(this.getClass().getResource("/images/new_black_chess.png")).getImage();
		newWhiteImage = new ImageIcon(this.getClass().getResource("/images/new_white_chess.png")).getImage();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.RED);
		g.drawRect(OFFSET_OUTER, OFFSET_OUTER, CELL * CELL_SIZE, CELL * CELL_SIZE);
		
		int offset = OFFSET_OUTER + OFFSET_INNER;
		g.setColor(Color.BLACK);
		for (int i = 0; i < CELL; i++) {
			//棋盘横线
			g.drawLine(offset, offset + i * CELL_SIZE, offset + (CELL - 1) * CELL_SIZE, offset + i * CELL_SIZE);
			//棋盘竖线
			g.drawLine(offset + i * CELL_SIZE, offset, offset + i * CELL_SIZE, offset + (CELL - 1) * CELL_SIZE);
		}

		g.setFont(new Font(null, Font.PLAIN, 12));
		for (int i = 0; i < CELL; i++) {
			//棋盘左侧坐标
			g.drawString(i + 1 + "", i < 9 ? OFFSET_OUTER - 13 : OFFSET_OUTER - 20, OFFSET_OUTER + OFFSET_INNER + 3 + i * CELL_SIZE);

			//棋盘顶部坐标
			g.drawString((char)('a' + i) + "", OFFSET_OUTER + OFFSET_INNER - 2 + i * CELL_SIZE, OFFSET_OUTER - 5);
		}

		//画星
		offset += CELL_SIZE * 3;
		fillOval(g, offset,  offset, STAR_RADIUS);
		fillOval(g, offset,  offset + CELL_SIZE * 8, STAR_RADIUS);
		fillOval(g, offset + CELL_SIZE * 4,  offset + CELL_SIZE * 4, STAR_RADIUS);
		fillOval(g, offset + CELL_SIZE * 8,  offset, STAR_RADIUS);
		fillOval(g, offset + CELL_SIZE * 8,  offset + CELL_SIZE * 8, STAR_RADIUS);
		
		Point p = chessBoard.getGameInfo().getCurrent();
		if(p != null) {
			offset = OFFSET_OUTER + OFFSET_INNER - CELL_SIZE / 2;
			boolean currentType = chessBoard.getGameInfo().isCurrentType();
			if(currentType) {
				boolean turn = chessBoard.getGameInfo().getTurn();
				g.drawImage(turn ? newBlackImage : newWhiteImage, offset + p.x * CELL_SIZE, offset + p.y * CELL_SIZE, this);
			}
			Color temp = g.getColor();
			g.setColor(currentType ? Color.GREEN : Color.RED);
			g.drawRect(offset + p.x * CELL_SIZE, offset + p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
			g.setColor(temp);
		}
		
		//画棋子
		int[][] map = chessBoard.getGameInfo().getMap();
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				drawChess(g, i, j, map[i][j]);
			}
		}
	}
	
	private void fillOval(Graphics g, int x, int y, int r) {
		g.fillOval(x - r, y - r, r * 2, r * 2);
	}
	
	private void drawChess(Graphics g, int row, int column, int chessType) {
		int offset = OFFSET_OUTER + OFFSET_INNER - CELL_SIZE / 2;
		int x = offset + row * CELL_SIZE;
		int y = offset + column * CELL_SIZE;
		
		switch(chessType) {
		case GameInfo.CHESS_BLACK:
			g.drawImage(blackImage, x, y, this);
			break;
		case GameInfo.CHESS_WHITE:
			g.drawImage(whiteImage, x, y, this);
			break;
		default:
			break;
		}
	}

	public Point convert(int x, int y) {
		if (x < OFFSET_OUTER + OFFSET_INNER - CELL_SIZE / 2
				|| x > OFFSET_OUTER + OFFSET_INNER - CELL_SIZE / 2 + CELL * CELL_SIZE
				|| y < OFFSET_OUTER + OFFSET_INNER + 80 - CELL_SIZE / 2
				|| y > OFFSET_OUTER + OFFSET_INNER + 80 - CELL_SIZE / 2 + CELL * CELL_SIZE) {
			return null;
		}
		int i = (x - OFFSET_OUTER - OFFSET_INNER + CELL_SIZE / 2) / CELL_SIZE;
		int j = (y - OFFSET_OUTER - OFFSET_INNER - 80 + CELL_SIZE / 2) / CELL_SIZE;
		return new Point(i, j);
	}
}
