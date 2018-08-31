package org.hbin.gobang.listener;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.apache.log4j.Logger;
import org.hbin.gobang.frame.ChessBoard;
import org.hbin.gobang.game.GameInfo;

public class GameMouseAdapter extends MouseAdapter {
	private static final Logger log = Logger.getLogger(GameMouseAdapter.class);
	private ChessBoard chessBoard;
	
	public GameMouseAdapter(ChessBoard chessBoard) {
		this.chessBoard = chessBoard;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() == 2) {
			log.info("click: " + e.getX() + ", " + e.getY());
			Point p = chessBoard.getMainPanel().convert(e.getX(), e.getY());
			if(p != null) {
				log.info("point: [" + p.x + ", " + p.y + "]");
				GameInfo gameInfo = chessBoard.getGameInfo();
				gameInfo.step(p.x, p.y, gameInfo.getTurn());
			}
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		log.info("mouseMoved: " + e.getX() + ", " + e.getY());
		Point p = chessBoard.getMainPanel().convert(e.getX(), e.getY());

		if(p != null) {
			log.info("mouseMoved: " + p.x + ", " + p.y);
			if(p != chessBoard.getGameInfo().getCurrent()) {
				log.info("update current: " + p);
				chessBoard.getGameInfo().setCurrent(p);
				boolean currentType = chessBoard.getGameInfo().getMap()[p.x][p.y] == GameInfo.CHESS_NONE;
				chessBoard.getGameInfo().setCurrentType(currentType);
				chessBoard.getMainPanel().repaint();
			}
		}
	}
}
