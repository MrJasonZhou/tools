/**
 *
 */
package com.jasonzhou.tool.sag.excel;

import java.util.Collection;

import com.jasonzhou.tool.sag.InputReader;
import com.jasonzhou.tool.sag.config.SagConfig;
import com.jasonzhou.tool.sag.config.TableInfo;

/**
 * Excelの定義ファイルリーダー
 *
 * @author Jason Zhou
 *
 */
public class ExcelInputReader implements InputReader<ExcelConfigReader<SagConfig>, TableInfo>{

	@Override
	public Collection<TableInfo> read(ExcelConfigReader<SagConfig> r) {
		return null;
	}


}
