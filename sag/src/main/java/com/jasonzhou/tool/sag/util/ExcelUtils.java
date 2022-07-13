/**  */
package com.jasonzhou.tool.sag.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * Excelファイルを操作するUtil
 * 
 * @author Jason Zhou
 *
 */
public class ExcelUtils {
	
	private static Map<Sheet, List<CellRangeAddress >> mapRange = new HashMap<>();
	
	private static Map<Workbook, FormulaEvaluator> mapFormulaEvaluator = new HashMap<>();
	
	private static DataFormatter df = new DataFormatter();

	/**
	 * シートの結合セル情報を取得し、キャシューさせる
	 * 
	 * @param sheet	シート
	 */
	private static void loadCellRange(Sheet sheet) {
		if (mapRange.containsKey(sheet)) {
			return ;
		}
		List<CellRangeAddress > list = new ArrayList<>();
		for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			list.add(range);
		}
		mapRange.put(sheet, list);
	}
	
	/**
	 * 指定行と列のセルを取得する
	 * 
	 * @param sheet	シート
	 * @param rowNo	指定行
	 * @param colNo	指定列
	 * @return	セル情報
	 */
	private static Cell getCell(Sheet sheet, int rowNo, int colNo) {
		if (!mapRange.containsKey(sheet)) {
			loadCellRange(sheet);
		}
		for (CellRangeAddress range : mapRange.get(sheet)) {
			if (rowNo >= range.getFirstRow() && rowNo <= range.getLastRow() && colNo >= range.getFirstColumn() && colNo <= range.getLastColumn()) {
				return sheet.getRow(range.getFirstRow()).getCell(range.getFirstColumn());
			}
		}
		if (rowNo > sheet.getLastRowNum()) {
			return null;
		}
		Row row = sheet.getRow(rowNo);
		if (colNo > row.getLastCellNum()) {
			return null;
		}
		Cell cell = row.getCell(colNo);
		return cell;
	}

	/**
	 * 設定ファイルを読み込む
	 * 
	 * @param fileName	ファイル名
	 * @return	Excelファイル
	 * @throws IOException 
	 */
	private static Workbook load(String fileName) throws  IOException {

        FileInputStream is =  new FileInputStream(fileName);
        if (fileName.toLowerCase().endsWith("xlsx") || fileName.toLowerCase().endsWith("xlsm")) {
        	Workbook book = new XSSFWorkbook(is); 
        	mapFormulaEvaluator.put(book, new XSSFFormulaEvaluator((XSSFWorkbook) book));
            return book;
        } else if (fileName.toLowerCase().endsWith("xls") ) {
        	Workbook book = new HSSFWorkbook(is);
        	mapFormulaEvaluator.put(book, new HSSFFormulaEvaluator((HSSFWorkbook) book));
        	return book;
        } else {
        	is.close();
            throw new IOException("ファイルフォーマットエラー");
        }
	}
	
	/**
	 * 設定ファイルを読み込む
	 * 
	 * @param fileName	ファイル名
	 * @return	Excelファイル
	 * @throws IOException 
	 */
	public static Workbook  load(InputStream is) throws  IOException {

    	Workbook book = new XSSFWorkbook(is); 
    	mapFormulaEvaluator.put(book, new XSSFFormulaEvaluator((XSSFWorkbook) book));
        return book;
	}	
	/**
	 * セルのテキスト情報を取得する
	 * 
	 * @param sheet	シート
	 * @param rowNo		行番号
	 * @param colNo		列番号
	 * @return	テキスト情報
	 */
	public static String getCellText(Sheet sheet, int rowNo, int colNo) {
		return getCellText(getCell(sheet, rowNo, colNo));
	}

	/**
	 * セルのテキスト情報を取得する
	 * 
	 * @param cell	セル
	 * @return	セルのテキスト情報
	 */
	private static String getCellText(Cell cell) {
		if (cell == null) {
			return "";
		}
//		String result = "";
//	     switch(cell.getCellType()) {
//	      case STRING:
//	        result =  cell.getStringCellValue();
//	        break;
//	      case NUMERIC:
//	        if(DateUtil.isCellDateFormatted(cell)) {
//	          result = nvl(cell.getDateCellValue());
//	        } else {
//	          result =  nvl(cell.getNumericCellValue());
//	        }
//	        break;
//	      case BOOLEAN:
//	    	  result =  nvl(cell.getBooleanCellValue());
//	        break;
//	      case FORMULA:
//	    	  result =  nvl(cell.getCellFormula());
//	        break;
//	      case ERROR:
//	    	  result =  nvl(cell.getErrorCellValue());
//	        break;
//	      case BLANK:
//	        break;
//	       default :
//	    	   break;
//	      }
		if (cell.getCellType() == CellType.FORMULA) {
			FormulaEvaluator formulaEvaluator=mapFormulaEvaluator.get(cell.getSheet().getWorkbook());
			if (formulaEvaluator != null) {
				String value= formulaEvaluator.evaluate(cell).formatAsString();
				if (StringUtils.endsWith(value, ".0")) {
					value = StringUtils.left(value, StringUtils.length(value) - 2);
				}
				return value;
			} else {
			     return df.formatCellValue(cell);
			}
		} else {
		     return df.formatCellValue(cell);
		}
	}
}
