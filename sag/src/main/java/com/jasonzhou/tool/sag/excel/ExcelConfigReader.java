/**
 * 
 */
package com.jasonzhou.tool.sag.excel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jasonzhou.tool.sag.Config;
import com.jasonzhou.tool.sag.ConfigReader;
import com.jasonzhou.tool.sag.config.ListableProperty;
import com.jasonzhou.tool.sag.config.SimpleProperty;
import com.jasonzhou.tool.sag.info.Position;
import com.jasonzhou.tool.sag.info.VarDefine;
import com.jasonzhou.tool.sag.util.ExcelUtils;
import com.jasonzhou.tool.sag.util.SagUtil;

/**
 * Excelファイルから設定情報を読込む
 * 
 * @author Jason Zhou
 *
 */
public class ExcelConfigReader<C extends Config> extends ConfigReader<C>  {

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
	public C load(InputStream is, Class<C> cClass) throws Exception {
		Workbook book = null;
		C config = cClass.getDeclaredConstructor().newInstance();
		try {
			book = ExcelUtils.load(is);
			//最初の
			Sheet propertySheet = book.getSheet(SHEET_CONFIG);
			loadProperties(propertySheet, config);
			//対象シート
			for (String sheetName : StringUtils.split(config.getProperty(PROPERTY_DEFINE_SHEETS), ",")) {
				sheetName = StringUtils.trim(sheetName);
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
						SagUtil.set(config, sheetName, define);
						//config.setDefine(sheetName, define);
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
	
	/**
	 * シートから属性値を読込んで、対象のインスタンスに設定する
	 * 
	 * @param <T>		対象クラス
	 * @param sheet		シート
	 * @param config	設定情報
	 * @param tClass	対象クラス
	 * @return	対象のインスタンス
	 * @throws Exception
	 */
	private <T> T read(Sheet sheet, Config config, Class<T> tClass) throws Exception {
		T t = tClass.getDeclaredConstructor().newInstance();
		//単純な属性
		if (tClass.isAssignableFrom(SimpleProperty.class)) {
			Class<SimpleProperty> cls = (Class<SimpleProperty>) tClass;
			SimpleProperty s = (SimpleProperty) t;
			readSimple(sheet, config, s);
		}
		//リスト属性
		if (tClass.isAssignableFrom(ListableProperty.class)) {
			Class<ListableProperty<?>> cls = (Class<ListableProperty<?>>)  tClass;
			ListableProperty<?> l = (ListableProperty<?>) t;
			readList(sheet, config, l);
		}
		
			
		
		return t;
	}

	private <T extends SimpleProperty> T readSimple(Sheet sheet, Config config, T t)  throws Exception {
		//レイアウトを取得する
		List<VarDefine> vdList = parseLayout(sheet, checkSimple);
		if (vdList == null || vdList.isEmpty()) {
			return t;
		}
		//行番号
		int rowNo = vdList.get(0).getPosistion().getRow() + 1;
		//列番号
		int colNo = vdList.get(0).getPosistion().getCol();
		//先頭列はNULLじゃない場合
		while(!ExcelUtils.isBlank(sheet, rowNo, colNo)) {
			for(VarDefine vd : vdList) {
				String propertyName = vd.getVarName();
				String text = ExcelUtils.getCellText(sheet, rowNo, vd.getPosistion().getCol());
				if (StringUtils.isNotBlank(propertyName)) {
					//値を設定する
					SagUtil.set(t, propertyName, text);
				}
			}
			rowNo++;
		}
		return t;
	}

	private <T extends ListableProperty<?>> T readList(Sheet sheet, Config config, T t)  throws Exception {
		//レイアウトを取得する
		List<VarDefine> vdList = parseLayout(sheet, checkList);
		if (vdList == null || vdList.isEmpty()) {
			return t;
		}
		//行番号
		int rowNo = vdList.get(0).getPosistion().getRow() + 1;
		//列番号
		int colNo = vdList.get(0).getPosistion().getCol();
		//先頭列はNULLじゃない場合
		while(!ExcelUtils.isBlank(sheet, rowNo, colNo)) {
			//属性インスタンスを作成する
			Object bean = t.newElement();
			for(VarDefine vd : vdList) {
				String propertyName = vd.getVarName();
				String text = ExcelUtils.getCellText(sheet, rowNo, vd.getPosistion().getCol());
				if (StringUtils.isNotBlank(propertyName)) {
					//値を設定する
					SagUtil.set(bean, propertyName, text);
				}
			}
			t.add(bean);
			rowNo++;
		}
		return t;
	}
	
	private List<VarDefine> parseLayout(Sheet sheet, CheckVarType check) {
		List<VarDefine> list = new ArrayList<>();
		for (int rowNo = sheet.getFirstRowNum(); rowNo <= sheet.getLastRowNum(); rowNo++) {
			Row row = sheet.getRow(rowNo);
			if (row != null) {
				for (int colNo = row.getFirstCellNum(); colNo <= row.getLastCellNum(); colNo++) {
					Cell cell = row.getCell(colNo);
					if (cell != null) {
						if (check.check(cell)) {
							Position pos = new Position(rowNo, colNo);
							VarDefine vd = VarDefine.create(pos, ExcelUtils.getCellText(cell));
						}
					}
				}
			}
		}
		return list;
	}
	CheckVarType checkSimple = (cell) -> { return !StringUtils.startsWith(ExcelUtils.getCellComment(cell), ":"); }; 
	CheckVarType checkList = (cell) -> { return StringUtils.startsWith(ExcelUtils.getCellComment(cell), ":"); }; 
}


interface CheckVarType{
	boolean check(Cell cell) ;
}

