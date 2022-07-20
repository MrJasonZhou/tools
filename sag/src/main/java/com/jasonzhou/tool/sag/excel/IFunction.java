/**
 * 
 */
package com.jasonzhou.tool.sag.excel;

import org.apache.poi.ss.usermodel.Cell;

/**
 * セル値を処理するため関数インタフェース
 *
 * @author Jason Zhou
 *
 */
public interface IFunction {

	/**
	 * セル値を処理する
	 * 
	 * @param cell	セル
	 * @param params	引数（カンマ区切り複数可）
	 * @return 処理された値
	 */
	public Object execute(Cell cell, String params) ;
}
