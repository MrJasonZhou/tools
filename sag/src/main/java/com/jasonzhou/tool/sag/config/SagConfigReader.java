/**
 *
 */
package com.jasonzhou.tool.sag.config;

import java.io.InputStream;

import com.jasonzhou.tool.sag.excel.ExcelConfigReader;

/**
 * ソース自動生成設定情報の読み取り
 *
 * @author Jason
 *
 */
public class SagConfigReader extends ExcelConfigReader<SagConfig> {

	public SagConfigReader(InputStream is) {
		super(is);
	}

}
