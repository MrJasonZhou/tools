/**
 * 
 */
package com.jasonzhou.tool.sag.excel;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
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
public class ExcelConfigReader<C extends Config> extends ConfigReader<C> implements Closeable {

    private static Logger logger = LoggerFactory.getLogger(ExcelConfigReader.class);
	/** シート：設定情報（グローバル変数） */
	private static final String SHEET_CONFIG = "config";
	private static final int GLOBAL_START_ROW = 1;
	private static final int GLOBAL_START_COL = 1;
	private Map<String, List<CellRangeAddress>> mapRangeAddress = new HashMap<>();

	/**
	 * 設定情報：属性　定義対象シート
	 */
	private static final String PROPERTY_DEFINE_SHEETS = "define.sheets";
	
	private Map<String, List<VarDefine>> varMap = new HashMap<>();
	
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
			for (String sheetName : SagUtil.split(config.getProperty(PROPERTY_DEFINE_SHEETS), ",")) {
				String propertyName = StringUtils.trim(sheetName);
				Sheet sheet = book.getSheet(propertyName);
				if (sheet == null) {
					String msg = "シートを見つかりませんでした。シート名＝" + propertyName;
					logger.error(msg);
					throw new IOException(msg);
				}
				String clsName = config.getProperty(propertyName + ".class");
				if (StringUtils.isNotBlank(clsName)) {
					try {
						Class<?> cls = Class.forName(clsName);
						Object property=read(sheet, config, cls);
						SagUtil.set(config, propertyName, property);
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
	 * シートの併合されたエリアを取得する
	 * 
	 * @param sheet	シート
	 * @return　併合されたエリアリスト
	 */
	private List<CellRangeAddress> getCellRangions(Sheet sheet) {
		String key = sheet.getSheetName();
		if (mapRangeAddress.containsKey(key)) {
			return mapRangeAddress.get(key);
		} else {
			List<CellRangeAddress> list = sheet.getMergedRegions();
			if (list == null) {
				list = new ArrayList<>();
			}
			mapRangeAddress.put(key, list);
			return list;
		}
	}
	
	private boolean inMergedRangion(Sheet sheet, int rowNo, int colNo) {
		List<CellRangeAddress> list = getCellRangions(sheet);
		for (CellRangeAddress cra : list) {
			if (rowNo < cra.getFirstRow() || rowNo > cra.getLastRow()
				|| colNo < cra.getFirstColumn() || colNo > cra.getLastColumn()) {
				
			} else {
				if (rowNo >= cra.getFirstRow() && rowNo <= cra.getLastRow()
						&& colNo >= cra.getFirstColumn() || colNo >= cra.getLastColumn()) {
				}
			}
		}
		
		return false;
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
		if (SimpleProperty.class.isAssignableFrom(tClass)) {
			Class<SimpleProperty> cls = (Class<SimpleProperty>) tClass;
			SimpleProperty s = (SimpleProperty) t;
			readSimple(sheet, config, s);
		}
		//リスト属性
		if (ListableProperty.class.isAssignableFrom(tClass)) {
			Class<ListableProperty<?>> cls = (Class<ListableProperty<?>>)  tClass;
			ListableProperty<?> l = (ListableProperty<?>) t;
			readList(sheet, config, l);
		}
		
			
		
		return t;
	}

	private IExcelRowChecker defaultRowCheck = (config, sheet, rowNo,vdList) -> {
		return StringUtils.isNotBlank(ExcelUtils.getCellText(sheet, rowNo, vdList.get(0).getPosistion().getCol()));
	};
	
	
	/**
	 * 行は処理対象であるかを判断するチェッカーを取得する
	 * 
	 * @param config	設定情報
	 * @param sheet		対象となるシート
	 * @return	行は処理対象であるかを判断するチェッカー
	 */
	private IExcelRowChecker getRowChecker(Config config, Sheet sheet) {
		String checkerClassName = config.getProperty(sheet.getSheetName() + ".rowChecker");
		IExcelRowChecker rowChecker = null;
		if (StringUtils.isBlank(checkerClassName)) {
			rowChecker = defaultRowCheck;
		} else {
			try {
				Class<IExcelRowChecker> clz = (Class<IExcelRowChecker>)Class.forName(checkerClassName);
				rowChecker = clz.getDeclaredConstructor().newInstance();
			}catch(Exception e) {
				logger.warn("RowCheckeロード時エラーが発生しました。", e);
				rowChecker = defaultRowCheck;
			}
		}
		return rowChecker;
	}
	private <T extends SimpleProperty> T readSimple(Sheet sheet, Config config, T t)  throws Exception {
		//レイアウトを取得する
		List<VarDefine> vdList = parseLayout(sheet, checkSimple);
		if (vdList == null || vdList.isEmpty()) {
			return t;
		}
		varMap.put(sheet.getSheetName() + ":simple", vdList);
		for(VarDefine vd : vdList) {
			String propertyName = vd.getVarName();
			String text = ExcelUtils.getCellText(sheet, vd.getPosistion().getRow(), vd.getPosistion().getCol());
			if (StringUtils.isNotBlank(propertyName)) {
				//値を設定する
				SagUtil.set(t, propertyName, text);
			}
		}
		return t;
	}

	private <E, T extends ListableProperty<E>> T readList(Sheet sheet, Config config, T t)  throws Exception {
		//レイアウトを取得する
		List<VarDefine> vdList = parseLayout(sheet, checkList);
		if (vdList == null || vdList.isEmpty()) {
			return t;
		}
		varMap.put(sheet.getSheetName() + ":list", vdList);
		//行番号
		int rowNo = vdList.get(0).getPosistion().getRow();
		//行は処理対象であるかを判断するチェッカーを取得する
		IExcelRowChecker rowChecker = getRowChecker(config, sheet);
		//行は対象であるかを判断する
		while(rowChecker.check(config, sheet, rowNo, vdList)) {
			//属性インスタンスを作成する
			Object bean = t.newElement();
			for(VarDefine vd : vdList) {
				//先頭の「:」を取り除く
				String propertyName = StringUtils.substring(vd.getVarName(), 1);
				String text = ExcelUtils.getCellText(sheet, rowNo, vd.getPosistion().getCol());
				if (StringUtils.isNotBlank(propertyName)) {
					//値を設定する
					SagUtil.set(bean, propertyName, text);
				}
			}
			
			t.add((E)bean);
			rowNo++;
		}
		return t;
	}
	
	private List<VarDefine> parseLayout(Sheet sheet, CheckVarType check) {
		List<VarDefine> list = new ArrayList<>();
		for (int rowNo = sheet.getFirstRowNum(); rowNo < sheet.getLastRowNum(); rowNo++) {
			Row row = sheet.getRow(rowNo);
			if (row != null) {
				for (int colNo = row.getFirstCellNum(); colNo < row.getLastCellNum(); colNo++) {
					Cell cell = ExcelUtils.getCell(sheet, rowNo, colNo);
					if (cell != null) {
						if (check.check(cell)) {
							Position pos = new Position(rowNo, colNo);
							String varName = StringUtils.trim(ExcelUtils.getCellComment(cell));
							VarDefine vd = VarDefine.create(pos, varName);
							list.add(vd);
						}
					}
				}
			}
		}
		return list;
	}
	CheckVarType checkSimple = (cell) -> { String comment = ExcelUtils.getCellComment(cell); return  StringUtils.isNotBlank(comment) && !StringUtils.startsWith(comment, ":"); }; 
	CheckVarType checkList = (cell) -> { return StringUtils.startsWith(ExcelUtils.getCellComment(cell), ":"); };

	@Override
	public void close() throws IOException {
		mapRangeAddress.clear();
		
	} 
}


interface CheckVarType{
	boolean check(Cell cell) ;
}

