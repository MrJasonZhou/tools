package com.jasonzhou.tool.sag.config;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jasonzhou.tool.sag.info.Position;
import com.jasonzhou.tool.sag.info.VarDefine;
import com.jasonzhou.tool.sag.util.ExcelUtils;

/**
 * Excel形式の設定ファイル
 * 
 * @author Jason Zhou
 *
 */
@Component
public class ExcelConfig extends Config {
	
    private static Logger logger = LoggerFactory.getLogger(ExcelConfig.class);
	
	/** シート：設定情報（グローバル変数） */
	private static final String SHEET_CONFIG = "config";
	private static final int GLOBAL_START_ROW = 1;
	private static final int GLOBAL_START_COL = 1;
	
	/** シート：設定情報（タスク情報） */
	private static final String SHEET_TASK= "task";
	private static final int TASK_START_ROW = 4;
	private static final int TASK_START_COL = 1; 

	/** シート：設定情報（テンプレート定義情報） */
	private static final String SHEET_TEMPLATE= "template";
	private static final int TEMPLATE_START_ROW = 1;
	private static final int TEMPLATE_START_COL = 1; 
	
	/** シート：設定情報（マッピング定義情報） */
	private static final String SHEET_MAPPING= "mapping";
	private static final int MAPPING_START_ROW = 1;
	private static final int MAPPING_START_COL = 1; 

	/** シート：設定情報（入力データレイアウト） */
	private static final String SHEET_INPUT_DATA_DEFINE= "layout";
	
	private String getKbnString(String kbn, String value) {
		logger.debug("getKbnString('" + kbn + "', '" + value + "');" );
		return StringUtils.isBlank(kbn) ? value : kbn + "." +value;
	}

	/**
	 * 初期化
	 * 
	 * @param fileName	設定ファイル名
	 * @throws IOException 
	 */
	@Override
	public void load(String fileName) throws IOException {
		Workbook book = ExcelUtils.load(new FileInputStream(fileName));
		//設定情報（グローバル変数）を取得する
		loadGlobalVars(book.getSheet(SHEET_CONFIG));
		//区分値
		String kbn = getVar(GLOBAL_KBN);
		Sheet sheet = book.getSheet(getKbnString(kbn, SHEET_CONFIG));
		if (sheet != null) {
			loadGlobalVars(sheet);
		}
		//設定情報（タスク情報）を取得する
		loadTask(book.getSheet(getKbnString(kbn, SHEET_TASK)));
		//設定情報（マッピング情報）を取得する
		loadMappingDefine(book.getSheet(getKbnString(kbn, SHEET_MAPPING)));
		//設定情報（テンプレート定義情報）を取得する
		loadTemplate(book.getSheet(getKbnString(kbn, SHEET_TEMPLATE)));
		//設定情報（入力ファイルレイアウト情報）を取得する
		loadInputFileDefine(book.getSheet(getKbnString(kbn, SHEET_INPUT_DATA_DEFINE)));
	}
	
	/**
	 * 設定情報（グローバル変数）を取得する
	 * 
	 * @param sheet	ワークシート
	 */
	private void loadGlobalVars(Sheet sheet) {
		int rowNo = GLOBAL_START_ROW;
		int colNo = GLOBAL_START_COL;
		while (StringUtils.length(ExcelUtils.getCellText(sheet, rowNo, colNo)) != 0) {
			String name = ExcelUtils.getCellText(sheet, rowNo, colNo);
			String value = ExcelUtils.getCellText(sheet, rowNo, colNo+1);
			registeVar(name, value);
			rowNo++;
		}
	}
	
	/**
	 * 設定情報（タスク情報）を取得する
	 * 
	 * @param sheet	ワークシート
	 */
	private void loadTask(Sheet sheet) {
		int rowNo = TASK_START_ROW;
		int colNo = TASK_START_COL;
		while (StringUtils.length(ExcelUtils.getCellText(sheet, rowNo, colNo)) != 0) {
			Task task = new Task();
			task.setInputFile(ExcelUtils.getCellText(sheet, rowNo, colNo));
			registeTask(task);
			logger.debug("タスク生成：対象ファイル＝" + task.getInputFile());
			rowNo++;
		}
	}

	/**
	 * 設定情報（テンプレート定義情報）を取得する
	 * 
	 * @param sheet	ワークシート
	 */
	private void loadTemplate(Sheet sheet) {
		int rowNo = TEMPLATE_START_ROW;
		while (StringUtils.length(ExcelUtils.getCellText(sheet, rowNo, TEMPLATE_START_COL)) != 0) {
			TemplateDefine define = new TemplateDefine();
			define.setTemplateFileName(ExcelUtils.getCellText(sheet, rowNo, TEMPLATE_START_COL ));
			define.setSwitchExpress(ExcelUtils.getCellText(sheet, rowNo, TEMPLATE_START_COL + 1));
			define.setOutputFile(ExcelUtils.getCellText(sheet, rowNo, TEMPLATE_START_COL + 2));
			addTemplateDefine(define);
			logger.debug("テンプレート定義情報生成：テンプレートファイル＝" + define.getTemplateFileName());
			rowNo++;
		}
	}	
	
	/**
	 * 設定情報（入力ファイルレイアウト情報）を取得する
	 * 
	 * @param sheet	ワークシート
	 */
	private void loadMappingDefine(Sheet sheet) {
		if (sheet == null) {
			return;
		}
		int rowNo = MAPPING_START_ROW;
		while (StringUtils.length(ExcelUtils.getCellText(sheet, rowNo, MAPPING_START_COL)) != 0) {
			String id = ExcelUtils.getCellText(sheet, rowNo, MAPPING_START_COL );
			MappingDefine md = MappingDefine.getDefine(id);
			md.set(ExcelUtils.getCellText(sheet, rowNo, MAPPING_START_COL + 1 )
					,ExcelUtils.getCellText(sheet, rowNo, MAPPING_START_COL + 2)
					);
			rowNo++;
		}
		setMappingDefine(MappingDefine.getDefine("*"));
	}	

	/**
	 * 設定情報（入力ファイルレイアウト情報）を取得する
	 * 
	 * @param sheet	ワークシート
	 */
	private void loadInputFileDefine(Sheet sheet) {
		for (int rowNo = 0 ; rowNo < sheet.getLastRowNum(); rowNo++) {
			Row row = sheet.getRow(rowNo);
			if (row != null) {
				for (int colNo = 0; colNo < row.getLastCellNum(); colNo++) {
					Cell cell = row.getCell(colNo);
					if (cell != null) {
						Comment comment = cell.getCellComment();
						if (comment != null) {
							String commentText = comment.getString().getString();
							if (StringUtils.isNotBlank(commentText)) {
								VarDefine vd = VarDefine.create(new Position(rowNo, colNo), commentText);
								inputDataDefine.registe(vd);
							}
						}
					}
				}
			}

		}
	}	
}
