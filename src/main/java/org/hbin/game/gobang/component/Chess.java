package org.hbin.game.gobang.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

public class Chess {
	public static final int DIAMETER = ChessBoard.SPAN - 2;
	/** 棋子在棋盘中的X索引 */
	private int col;
	/** 棋子在棋盘中的Y索引 */
	private int row;
	/** 颜色 */
	private Color color;

	public Chess(int col, int row, Color color) {
		this.col = col;
		this.row = row;
		this.color = color;
	}

	public int getCol() {
		return col;
	}

	public int getRow() {
		return row;
	}

	public Color getColor() {
		return color;
	}

	public void draw(Graphics g) {
		int xPos = col * ChessBoard.SPAN + ChessBoard.MARGIN;
		int yPos = row * ChessBoard.SPAN + ChessBoard.MARGIN;

		int x = xPos + DIAMETER / 4;
		int y = yPos - DIAMETER / 4;
		float[] f = { 0f, 1f };

		RadialGradientPaint paint = null;
		if (color == Color.BLACK) {
			Color[] c = new Color[]{ Color.GRAY, Color.BLACK };
			paint = new RadialGradientPaint(x, y, DIAMETER, f, c);
		} else if (color == Color.WHITE) {
			Color[] c = new Color[]{ Color.WHITE, Color.BLACK };
			paint = new RadialGradientPaint(x, y, DIAMETER * 2, f, c);
		}
		Graphics2D g2 = (Graphics2D) g;
		g2.setPaint(paint);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);
		Ellipse2D e = new Ellipse2D.Float(xPos - DIAMETER / 2, yPos - DIAMETER / 2, DIAMETER, DIAMETER);
		g2.fill(e);
	}
}
