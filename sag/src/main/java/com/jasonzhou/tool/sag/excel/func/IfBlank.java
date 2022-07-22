/**
 * 
 */
package com.jasonzhou.tool.sag.excel.func;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;

import com.jasonzhou.tool.sag.excel.IFunction;
import com.jasonzhou.tool.sag.util.ExcelUtils;

/**
 * セル値はブランクの場合、引数を返す
 *
 * @author Jason Zhou
 *
 */
public class IfBlank implements IFunction {

	@Override
	public Object execute(Cell cell, String params) {
		String text = ExcelUtils.getCellText(cell);
		return StringUtils.defaultIfBlank(text, params);
	}

}
