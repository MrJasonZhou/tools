/**  */
package com.jasonzhou.tool.sag.info;

/**
 * 位置情報
 * 
 * @author Jason Zhou
 *
 */
public class Position {
	
	/** 行 */
	private int row;
	
	/** 列 */
	private int col;
	
	public Position(int row, int col) {
		this.row = row;
		this.col = col;
	}

	/**
	 * 行 を取得する
	
	 * @return 行
	 */
	public int getRow() {
		return row;
	}

	/**
	 * 行 を設定する
	 *
	 * @param row 行
	 */
	public void setRow(int row) {
		this.row = row;
	}

	/**
	 * 列 を取得する
	
	 * @return 列
	 */
	public int getCol() {
		return col;
	}

	/**
	 * 列 を設定する
	 *
	 * @param col 列
	 */
	public void setCol(int col) {
		this.col = col;
	}
	

}
