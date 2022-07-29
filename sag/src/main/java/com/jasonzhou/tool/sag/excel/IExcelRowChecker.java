/**
 * 
 */
package com.jasonzhou.tool.sag.excel;

import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;

import com.jasonzhou.tool.sag.Config;
import com.jasonzhou.tool.sag.info.VarDefine;

/**
 * Excelの明細行は対象であるかをチェックする
 *
 * @author Jason Zhou
 *
 */
public interface IExcelRowChecker {

	/**
	 * Excel行は対象であるかをチェックする
	 * 
	 * @param config	設定情報
	 * @param sheet		シート
	 * @param rowNo		行番号
	 * @param varDefineList		変数定義のリスト
	 * @return	true：対象である、false：対象でない
	 */
	public boolean check(Config config, Sheet sheet, int rowNo, List<VarDefine> varDefineList);
}
