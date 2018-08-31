package org.hbin.gobang.bean;

import org.hbin.gobang.util.StringUtil;

public class Step {
	private int i;
	private int row;
	private int column;
	private boolean player;

	public Step() {
	}
	
	public Step(int row, int column, boolean player) {
		this.row = row;
		this.column = column;
		this.player = player;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public boolean isPlayer() {
		return player;
	}

	public void setPlayer(boolean player) {
		this.player = player;
	}

	@Override
	public String toString() {
		return (player ? "B" : "W") + (char)('a' + row) + "" + (column + 1);
	}
	
	public static Step parse(String step) {
		try {
			if(step != null) {
				if(step.matches("([B|W][a-o][1-9])|([B|W][a-o]1[0-5])")) {
					boolean player = step.startsWith("B");
					int row = step.charAt(1) - 'a';
					int column = StringUtil.toInt(step.substring(2)) - 1;
					return new Step(row, column, player);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
