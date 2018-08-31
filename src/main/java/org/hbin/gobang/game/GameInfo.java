package org.hbin.gobang.game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.hbin.gobang.bean.Step;
import org.hbin.gobang.frame.ChessBoard;
import org.hbin.gobang.util.Constants;

public class GameInfo {
	public static final Logger log = Logger.getLogger(GameInfo.class);
	
	public static final int RESULT_NONE = 0;
	public static final int RESULT_BLACK_WIN = 1;
	public static final int RESULT_WHITE_WIN = 2;
	public static final int RESULT_DRAW = 3;

	public static final boolean TURN_BLACK = true;
	public static final boolean TURN_WHITE = false;

	public static final int CHESS_NONE = 0;
	public static final int CHESS_BLACK = 1;
	public static final int CHESS_WHITE = 2;

	private int[][] map;
	private boolean turn;
	private boolean gameOver;
	/** 比赛结果，1-黑胜，2-白胜，3-平局 */
	private int result;
	
	private Point current;
	private boolean currentType;
	
	private List<Step> steps;
	
	private ChessBoard chessBoard;

	public GameInfo(ChessBoard chessBoard) {
		this.chessBoard = chessBoard;
		init();
	}
	
	public boolean isGameOver() {
		return gameOver;
	}
	
	public int[][] getMap(){
		return map;
	}
	
	public boolean getTurn() {
		return turn;
	}
	
	public Point getCurrent() {
		return current;
	}

	public void setCurrent(Point current) {
		this.current = current;
	}
	
	public boolean isCurrentType() {
		return currentType;
	}

	public void setCurrentType(boolean currentType) {
		this.currentType = currentType;
	}

	public List<Step> getSteps() {
		return steps;
	}

	public void init() {
		map = new int[Constants.CHESSBOARD_CELL][Constants.CHESSBOARD_CELL];
		turn = TURN_BLACK;
		gameOver = false;
		result = RESULT_NONE;
		chessBoard.getMainPanel().repaint();
		
		steps = new ArrayList<Step>();
	}

	/**
	 * 当前位置能否接收一方的着法
	 * 
	 * @param row 棋子横坐标
	 * @param column 棋子纵坐标
	 * @param turn 下棋方，true-黑棋，false-白棋
	 * @return
	 */
	private boolean accept(int row, int column, boolean turn) {
		if(gameOver) {
			int result = JOptionPane.showConfirmDialog(chessBoard, "再来一局？", "继续",
					JOptionPane.OK_CANCEL_OPTION);
			if(result == JOptionPane.OK_OPTION) {
				init();
				return false;
			}
		}
		// 校验下棋方
		if (this.turn != turn) {
			return false;
		}
		// 校验坐标
		if (row < 0 || row > 14 || column < 0 || column > 14) {
			return false;
		}
		// 校验当前位置是否有棋子
		if (map[row][column] != CHESS_NONE) {
			return false;
		}

		// TODO 禁止着法判断

		return true;
	}

	public void step(int row, int column, boolean turn) {
		if (accept(row, column, turn)) {
			map[row][column] = turn ? CHESS_BLACK : CHESS_WHITE;
			steps.add(new Step(row, column, turn));
			log.info("[GameInfo step()] " + row + ", " + column + ": " + (map[row][column] == CHESS_BLACK ? "B" : "W"));
			chessBoard.repaint();

			result = isGameOver(row, column);
			if (result != RESULT_NONE) {
				gameOver = true;
				if(result == RESULT_DRAW) {
					log.info("[GameInfo step()] game over, result: draw.");
					JOptionPane.showMessageDialog(chessBoard, "这是一个平局，继续加油哟！");
				} else {
					log.info("[GameInfo step()] game over, result: " + (result == RESULT_BLACK_WIN ? "black win.":"white win."));
					String msg = "<html><div><span style='color:red;font-size:20px;'>" + (result == RESULT_BLACK_WIN ? "黑棋": "白棋") + "</span> 获胜<br/><br/>恭喜本局获胜方!</div></html>";
					JOptionPane.showMessageDialog(chessBoard, msg, "恭喜", JOptionPane.INFORMATION_MESSAGE);
				}
				return;
			}

			this.turn = !this.turn;
		}
	}

	/**
	 * 判断游戏是否结束
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public int isGameOver(int row, int column) {
		if (hasFiveInHorizontalDirection(row, column) || hasFiveInVerticalDirection(row, column)
				|| hasFiveInCastAwayDirection(row, column) || hasFiveInShortPausingDirection(row, column)) {
			log.info("[GameInfo hasFiveInRow] true");
			return turn ? RESULT_BLACK_WIN : RESULT_WHITE_WIN;
		}
		if (isFull()) {
			return RESULT_DRAW;
		}
		return RESULT_NONE;
	}

	/**
	 * 判断水平方向是否五连
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public boolean hasFiveInHorizontalDirection(int row, int column) {
		int num = 0;
		int chess = turn ? CHESS_BLACK : CHESS_WHITE;

		for (int i = 0; i < Constants.CHESSBOARD_CELL; i++) {
			if (map[row][i] == chess) {
				num++;
			} else {
				num = 0;
			}

			if (num >= 5) {
				log.info("[GameInfo hasFiveInHorizontalDirection()] true: " + row + ", " + column);
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断竖直方向是否五连
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public boolean hasFiveInVerticalDirection(int row, int column) {
		int num = 0;
		int chess = turn ? CHESS_BLACK : CHESS_WHITE;

		for (int i = 0; i < Constants.CHESSBOARD_CELL; i++) {
			if (map[i][column] == chess) {
				num++;
			} else {
				num = 0;
			}

			if (num >= 5) {
				log.info("[GameInfo hasFiveInVerticalDirection()] true: " + row + ", " + column);
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断撇方向是否五连
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public boolean hasFiveInCastAwayDirection(int row, int column) {
		if (row + column < 5 && row + column > 25) {
			return false;
		}

		int num = 0;
		int chess = turn ? CHESS_BLACK : CHESS_WHITE;

		int i = row;
		int j = column;
		while (map[i][j] == chess) {
			num++;

			i -= 1;
			j += 1;
			if (i < 0 || j > 14) {
				break;
			}
		}

		i = row;
		j = column;
		while (map[i][j] == chess) {
			num++;

			i += 1;
			j -= 1;
			if (i > 14 || j < 0) {
				break;
			}
		}

		if (num - 1 >= 5) {
			log.info("[GameInfo hasFiveInCastAwayDirection()] true: " + row + ", " + column);
			return true;
		}

		return false;
	}

	/**
	 * 判断捺方向是否五连
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public boolean hasFiveInShortPausingDirection(int row, int column) {
		if (row - column > 10 || column - row > 10) {
			return false;
		}

		int num = 0;
		int chess = turn ? CHESS_BLACK : CHESS_WHITE;

		int i = row;
		int j = column;
		while (map[i][j] == chess) {
			num++;

			i -= 1;
			j -= 1;
			if (i < 0 || j < 0) {
				break;
			}
		}

		i = row;
		j = column;
		while (map[i][j] == chess) {
			num++;

			i += 1;
			j += 1;
			if (i > 14 || j > 14) {
				break;
			}
		}

		if (num - 1 >= 5) {
			log.info("[GameInfo hasFiveInShortPausingDirection()] true: " + row + ", " + column);
			return true;
		}
		return false;
	}

	/**
	 * 是否下满棋子了
	 * 
	 * @return
	 */
	public boolean isFull() {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (map[i][j] == CHESS_NONE) {
					return false;
				}
			}
		}
		log.info("[GameInfo isFull()] true.");
		return true;
	}
}