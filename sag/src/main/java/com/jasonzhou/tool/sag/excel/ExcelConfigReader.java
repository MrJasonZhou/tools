/**
 * 
 */
package com.jasonzhou.tool.sag.excel;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jasonzhou.tool.sag.Config;
import com.jasonzhou.tool.sag.ConfigReader;
import com.jasonzhou.tool.sag.util.ExcelUtils;

/**
 * Excelファイルから設定情報を読込む
 * 
 * @author Jason Zhou
 *
 */
public class ExcelConfigReader extends ConfigReader {

    private static Logger logger = LoggerFactory.getLogger(ExcelConfigReader.class);
	/** シート：設定情報（グローバル変数） */
	private static final String SHEET_CONFIG = "config";
	private static final int GLOBAL_START_ROW = 1;
	private static final int GLOBAL_START_COL = 1;

	/**
	 * 設定情報：属性　定義対象シート
	 */
	private static final String PROPERTY_DEFINE_SHEETS = "define.sheets";
	
	@Override
	public Config load(InputStream is) throws Exception {
		Workbook book = null;
		Config config = new Config();
		try {
			book = ExcelUtils.load(is);
			//最初の
			Sheet propertySheet = book.getSheet(SHEET_CONFIG);
			loadProperties(propertySheet, config);
			//対象シート
			for (String sheetName : StringUtils.split(config.getProperty(PROPERTY_DEFINE_SHEETS), ",")) {
				Sheet sheet = book.getSheet(sheetName);
				if (sheet == null) {
					String msg = "シートを見つかりませんでした。シート名＝" + sheetName;
					logger.error(msg);
					throw new IOException(msg);
				}
				String clsName = config.getProperty(sheetName + ".class");
				if (StringUtils.isNotBlank(clsName)) {
					try {
						Class<?> cls = Class.forName(clsName);
						Object define=read(sheet, config, cls);
						config.setDefine(sheetName, define);
					}catch (Exception e) {
						logger.error("クラスロード中エラーが発生しました。クラス名＝" + clsName, e);
					}
				}
			}
		} finally {
			if (book != null) {
				book.close();
			}
		}
		return config;
	}

	/**
	 * 設定情報（属性）を取得する
	 * 
	 * @param sheet	ワークシート
	 * @param config 設定情報
	 */
	private void loadProperties(Sheet sheet, Config config) {
		int rowNo = GLOBAL_START_ROW;
		int colNo = GLOBAL_START_COL;
		while (StringUtils.isNotBlank(ExcelUtils.getCellText(sheet, rowNo, colNo))) {
			String name = ExcelUtils.getCellText(sheet, rowNo, colNo);
			String value = ExcelUtils.getCellText(sheet, rowNo, colNo+1);
			config.setProperty(name, value);
			rowNo++;
		}
	}
	
	private <T> T read(Sheet sheet, Config config, Class<T> tClass) {
		T t;
		try {
			t = tClass.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			logger.error("読取中エラーは発生しました。", e);
			return null;
		}
		
		return t;
	}
}
