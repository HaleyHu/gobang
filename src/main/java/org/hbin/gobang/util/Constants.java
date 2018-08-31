package org.hbin.gobang.util;

public class Constants {
	public static final int CHESSBOARD_WIDTH = PropertyUtil.getInt("chessboard.width", 100);
	public static final int CHESSBOARD_HEIGHT = PropertyUtil.getInt("chessboard.height", 100);
	public static final int CHESSBOARD_CELL = PropertyUtil.getInt("chessboard.cell", 15);
	public static final int CHESSBOARD_CELL_SIZE = PropertyUtil.getInt("chessboard.cell.size", 40);
	public static final int CHESSBOARD_OFFSET_OUTER = PropertyUtil.getInt("chessboard.offset.outer", 30);
	public static final int CHESSBOARD_OFFSET_INNER = PropertyUtil.getInt("chessboard.offset.inner", 20);
	public static final int CHESSBOARD_STAR_RADIUS = PropertyUtil.getInt("chessboard.star.radius", 6);
}
