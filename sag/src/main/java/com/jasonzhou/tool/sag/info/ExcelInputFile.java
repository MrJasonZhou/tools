/**  */
package com.jasonzhou.tool.sag.info;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jasonzhou.tool.sag.config.Config;
import com.jasonzhou.tool.sag.config.InputDataDefine;
import com.jasonzhou.tool.sag.config.Task;
import com.jasonzhou.tool.sag.func.Functions;
import com.jasonzhou.tool.sag.func.IFunction;
import com.jasonzhou.tool.sag.util.ExcelUtils;
import com.jasonzhou.tool.sag.util.SpringUtil;

/**
 * @author Jason Zhou
 *
 */
public class ExcelInputFile {
	
    private static Logger logger = LoggerFactory.getLogger(ExcelInputFile.class);
    
	private static final String TARGET_TEXT = "targetText";
	
	private static final String LOOP_NO = "loop:no";

	private static final String RECORD_VALID = "funcRecordValid";
	
	/**
	 * 対象シートリストを取得する
	 * 
	 * @param config	設定情報
	 * @param task	タスク情報
	 * @return	対象シートリスト
	 * @throws IOException
	 */
	private static List<Sheet> getTargetSheets(Config config, Task task) throws IOException {
		Workbook book = ExcelUtils.load(new FileInputStream(task.getInputFile()));
		List<Sheet> list = new ArrayList<>();
		String targetText = config.getVar(TARGET_TEXT);
		//targetText未定義
		if (StringUtils.isBlank(targetText)) {
			list.add(book.getSheetAt(0));
		} else {
			//変数定義
			VarDefine vd = config.getInputDataDefine().get(TARGET_TEXT);
			if (vd == null) {
				list.add(book.getSheetAt(0));
			} else {
				Iterator<Sheet> it = book.sheetIterator();
				while(it.hasNext()) {
					Sheet sheet = it.next();
					Row row = sheet.getRow(vd.getPosistion().getRow());
					Cell cell = row.getCell(vd.getPosistion().getCol());
					String text = cell.getStringCellValue();
					if (StringUtils.equals(text, targetText)) {
						list.add(sheet);
					}
				}
			}
		}
		
		return list;
	}
	
	
	/**
	 * EXCEL入力データを取得する
	 * 
	 * @param config	設定情報
	 * @param task	タスク情報
	 * @return		入力データ
	 * @throws IOException 
	 */
	public static List<InputData> parse(Config config, Task task) throws IOException {
		
		List<InputData>list = new ArrayList<>();
		
		//入力データ定義情報
		InputDataDefine dataDefine = config.getInputDataDefine();
		//対象シート
		List<Sheet> sheetList = getTargetSheets(config, task);
		for (Sheet sheet : sheetList) {
			InputData data = new InputData();
			data.setSheetName(sheet.getSheetName());
			logger.debug("ロードファイル： " + task.getInputFile() + " シート：" + sheet.getSheetName());
			//グローバル変数
			for (Map.Entry<String, VarDefine> entry : dataDefine.getGlobalMap().entrySet()) {
				//変数定義
				VarDefine vd = entry.getValue();
				//変数値
				String value = ExcelUtils.getCellText(sheet, vd.getPosistion().getRow(), vd.getPosistion().getCol());
				data.addGlobal(new Variable(vd, value));
				if (StringUtils.isBlank(value)) {
					logger.warn("未定義のグローバル変数：" + vd);
				}
			}
			//LOOP変数
			//開始行
			int rowNo = dataDefine.getLoopStartRowNo();
			int loopBreakCount = 0; 
			String loopNo = LOOP_NO;
			if (!StringUtils.isBlank(config.getVar(LOOP_NO))) {
				loopNo = config.getVar(LOOP_NO);
			}
			VarDefine noDefine = dataDefine.get(loopNo);
			while (loopBreakCount < 5) {
				String idText = ExcelUtils.getCellText(sheet, rowNo, noDefine.getPosistion().getCol());
				if (StringUtils.isBlank(idText)) {
					loopBreakCount++;
				} else {
					loopBreakCount = 0;
					//レコード情報を生成する
					Map<String, Variable> record = new HashMap<>();
					for (Map.Entry<String, VarDefine> entry : dataDefine.getLoopMap().entrySet()) {
						VarDefine vd = entry.getValue();
						String value = ExcelUtils.getCellText(sheet, rowNo, vd.getPosistion().getCol());
						record.put(vd.getVarName(), new Variable(vd, value));
					}
					String funcExpress = config.getVar(RECORD_VALID);
					boolean skip = false;
					if (StringUtils.isNotBlank(funcExpress)) {
						Object result = Functions.exexcute(funcExpress, config, task, data, record);
						if (result != null && result instanceof Boolean && !(Boolean) result) {
							skip = true;
						}
					}
					if (!skip) {
						data.addLoop(record);
					}
				}
				rowNo++;
			}
			list.add(data);
		}
		
		return list;
	}

}
