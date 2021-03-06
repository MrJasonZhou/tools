/**  */
package com.jasonzhou.tool.sag.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.format.CellFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellCopyPolicy;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jasonzhou.tool.sag.excel.ValueFormatter;


/**
 * Excelファイルを操作するUtil
 *
 * @author Jason Zhou
 *
 */
public class ExcelUtils {

	private static Map<Sheet, List<CellRangeAddress >> mapRange = new HashMap<>();

	private static Map<Workbook, Map<String, CellStyle>> cellLineStyles = new HashMap<>();

	private static DataFormatter df = new DataFormatter();

	/**
	 * シートの結合セル情報を取得し、キャシューさせる
	 *
	 * @param sheet	シート
	 */
	private static void loadCellRange(Sheet sheet) {
		synchronized (sheet) {
			if (mapRange.containsKey(sheet)) {
				return ;
			}
			List<CellRangeAddress > list = new ArrayList<>();
			int numMergedRegions = sheet.getNumMergedRegions();
			for (int i = 0; i < numMergedRegions; i++) {
				CellRangeAddress range = sheet.getMergedRegion(i);
				list.add(range);
			}
			list.sort((a, b) ->
			a.getFirstRow()> b.getFirstRow() ? 1 : a.getFirstRow() == b.getFirstRow() ? Integer.compare(a.getFirstColumn(), b.getFirstColumn()):-1 );
			mapRange.put(sheet, list);
		}

	}

	/**
	 * シートの行を削除する
	 *
	 * @param sheet	シート
	 * @param rowNo	行番号
	 */
	public static void deleteRow(Sheet sheet, int rowNo) {
		sheet.shiftRows(rowNo + 1, sheet.getLastRowNum(), -1);
		//sheet.removeRow(sheet.getRow(rowNo));
	}

	/**
	 * シートの行をクリアする
	 *
	 * @param sheet	シート
	 * @param rowNo	行番号
	 */
	public static void clearRow(Sheet sheet, int rowNo) {
		sheet.removeRow(sheet.getRow(rowNo));
	}

	/**
	 * 指定された位置の結合範囲を取得する
	 *
	 * @param sheet	シート
	 * @param rowNo	行番号
	 * @param colNo　列番号
	 * @return　結合された場合、結合範囲を返す。結合されていない場合、nullを返す
	 */
	private static CellRangeAddress getCellRange(Sheet sheet, int rowNo, int colNo) {
		loadCellRange(sheet);
		for (CellRangeAddress range : mapRange.get(sheet)) {
			if (rowNo >= range.getFirstRow()
					&& rowNo <= range.getLastRow()
					&& colNo >= range.getFirstColumn()
					&& colNo <= range.getLastColumn()) {
				return range;
			} else {
				if (rowNo > range.getLastRow() || rowNo == range.getLastRow() && colNo > range.getLastColumn()) {
					break;
				}
			}

		}
		return null;
	}

	/**
	 * 指定行と列のセルを取得する（結合された場合、最初のセル以外、nullを返す）
	 *
	 * @param sheet	シート
	 * @param rowNo	指定行
	 * @param colNo	指定列
	 * @return	セル情報
	 */
	public static Cell getCell(Sheet sheet, int rowNo, int colNo) {
		return getCell(sheet, rowNo, colNo, false);
	}

	/**
	 * 指定行と列のセルを取得する
	 *
	 * @param sheet	シート
	 * @param rowNo	指定行
	 * @param colNo	指定列
	 * @param asFirstCell 結合された場合、結合エリアの最初のセルを返す
	 * @return	セル情報
	 */
	public static Cell getCell(Sheet sheet, int rowNo, int colNo, boolean asFirstCell) {
		CellRangeAddress range = getCellRange(sheet, rowNo, colNo);
		if (range != null) {
			//最初のセル以外
			if (!asFirstCell && (rowNo != range.getFirstRow() || colNo != range.getFirstColumn())) {
				return null;
			}
			rowNo = range.getFirstRow();
			colNo = range.getFirstColumn();
		}
		Row row = sheet.getRow(rowNo);
		if (row == null) {
			row = sheet.createRow(rowNo);
		}
		Cell cell = row.getCell(colNo);
		if (cell == null) {
			cell = row.createCell(colNo);;
		}
		return cell;
	}

	/**
	 * 設定ファイルを読み込む
	 *
	 * @param is	設定ファイルのストリーム
	 * @return	Excelファイル
	 * @throws IOException
	 */
	public static Workbook load(InputStream is) throws  IOException {
		return new SagWorkBook(is);
	}

	/**
	 * Excelファイルを作成する
	 *
	 * @return	Excelファイル
	 * @throws IOException
	 */
	public static Workbook  create() throws  IOException {

		Workbook workbook  =  new XSSFWorkbook();
		return workbook;

	}

	/**
	 * 指定するフォーマットのCellスタイルを取得する
	 *
	 * @param workbook		Excelファイル
	 * @param cellFormat		セルフォーマット
	 * @return	指定するフォーマットのCellスタイル
	 */
	private static CellStyle getCellStyle(Workbook workbook, CellFormatter...  cellFormats) {

		Map<String, CellStyle> map = cellLineStyles.get(workbook);
		List<String> formatList = new ArrayList<>();
		for (CellFormatter cf : cellFormats) {
			formatList.add(cf.toString());
		}
		String key = StringUtils.join(formatList, ":");

		if (map == null) {
			map = new HashMap<>();
			cellLineStyles.put(workbook, map);
		}
		CellStyle cellStyle = map.get(key);
		if (cellStyle == null) {
			cellStyle = workbook.createCellStyle();
			for (CellFormatter cf : cellFormats) {
				cf.format(cellStyle);
			}
			map.put(key, cellStyle);
		}
		return cellStyle;
	}


	/**
	 * Excelファイルを保存する
	 *
	 * @param workbook	Workbook
	 * @param fileName		ファイル名
	 * @throws Exception
	 */
	public static void save(Workbook workbook, String fileName) throws Exception {
		File file = new File(fileName);
		String pathName = file.getParent();
		if (StringUtils.isNotBlank(pathName)) {
			File path = new File(pathName);
			if (!path.exists()) {
				path.mkdirs();
			}
		}
		try(FileOutputStream out = new FileOutputStream(fileName)) {
			workbook.write(out);
		}
	}

	/**
	 * セルのコメント情報を取得する
	 *
	 * @param sheet	シート
	 * @param rowNo		行番号
	 * @param colNo		列番号
	 * @return	コメント情報
	 */
	public static String getCellComment(Sheet sheet, int rowNo, int colNo) {
		Cell cell = getCell(sheet, rowNo, colNo, false);
		if (cell == null) {
			return "";
		}
		return getCellComment(cell);
	}

	/**
	 * セルのコメント情報を取得する
	 *
	 * @param cell	セル
	 * @return	コメント情報
	 */
	public static String getCellComment(Cell cell) {
		if (cell == null) {
			return "";
		}
		Comment comment = cell.getCellComment();
		if (comment == null) {
			return "";
		}
		RichTextString tx = comment.getString();
		if (tx == null) {
			return "";
		}
		return tx.getString();
	}

	/**
	 * セルのテキスト情報を取得する
	 *
	 * @param cell	セル
	 * @return	セルのテキスト情報
	 */
	public static String getCellText(Cell cell) {
		if (cell == null) {
			return "";
		}
		return df.formatCellValue(cell);
	}

	/**
	 * セルのテキスト情報を取得する
	 *
	 * @param row	行
	 * @param colNo	列番号
	 * @return	セルのテキスト情報
	 */
	private static String getCellText(Row row, int colNo) {
		if (colNo > row.getLastCellNum()) {
			return "";
		}
		return getCellText(row.getCell(colNo));
	}

	/**
	 * セルのテキスト情報を取得する
	 *
	 * @param sheet シート
	 * @param rowNo	行番号
	 * @param colNo	列番号
	 * @return	セルのテキスト情報
	 */
	public static String getCellText(Sheet sheet, int rowNo, int colNo) {
		Cell cell = getCell(sheet, rowNo, colNo, false);
		return getCellText(cell);
	}

	/**
	 * セルのテキスト情報を設定する
	 *
	 * @param cell	セル
	 * @param text	テキスト情報
	 */
	public static void setCellText(Cell cell, String text) {
		if (text == null) {
			text = "";
		}
		cell.setCellValue(text);;
	}

	/**
	 * セルのテキスト情報を設定する
	 *
	 * @param cell	セル
	 * @param text	テキスト情報
	 */
	public static void setCellValue(Cell cell,  Object... values) {
		ValueFormatter vf = null;
		setCellValue(cell, vf, values);
	}

	/**
	 * セルのテキスト情報を設定する
	 *
	 * @param cell	セル
	 * @param vf 	値フォーマット
	 * @param text	テキスト情報
	 */
	public static void setCellValue(Cell cell, ValueFormatter vf, Object... values) {
		Object value = values[0];
		if (vf != null) {
			value = vf.format(values);
		}
		if (value == null) {
			cell.setBlank();
		} else {
			//Integer
			if (value instanceof Integer) {
				Integer i = (Integer) value;
				cell.setCellValue(i);
			}
			//Long
			if (value instanceof Long) {
				Long l = (Long) value;
				cell.setCellValue(l);
			}
			//String
			if (value instanceof String) {
				String s = (String) value;
				cell.setCellValue(s);
			}
			//Date
			if (value instanceof Date) {
				Date d = (Date) value;
				cell.setCellValue(d);
			}
			//BigDecimal
			if (value instanceof BigDecimal) {
				BigDecimal bd = (BigDecimal) value;
				cell.setCellValue(bd.doubleValue());
			}
		}
	}

	/**
	 * セルをフォーマットする
	 *
	 * @param sheet	シート
	 * @param rowNo	行番号
	 * @param colNo	カラム番号
	 * @param cellFormat セルフォーマット
	 */
	public static void formatCell(Sheet sheet, int rowNo, int colNo, CellFormatter... cellFormat) {
		Cell cell = getCell(sheet, rowNo, colNo, false);
		//結合されたセル処理を飛ばす
		if (cell == null) {
			return;
		}
		CellStyle style = getCellStyle(sheet.getWorkbook(), cellFormat);
		cell.setCellStyle(style);
	}

	/**
	 * シートを取得する
	 *
	 * @param workbook		Excelファイル
	 * @param sheetName		シート名
	 * @return	ソート
	 */
	public static Sheet getSheet(Workbook workbook, String sheetName) {
		Sheet sheet = workbook.getSheet(sheetName);
		if (sheet == null) {
			sheet = workbook.createSheet(sheetName);
		}
		return sheet;
	}

	/**
	 * シートのタブ色を設定する
	 *
	 * @param sheet	シート
	 * @param color	タブ色
	 */
	public static void setTabColor(Sheet sheet, IndexedColors color) {
		if (sheet instanceof XSSFSheet) {
			XSSFSheet xSheet = (XSSFSheet) sheet;
			byte[] rgb=DefaultIndexedColorMap.getDefaultRGB(color.getIndex());
			xSheet.setTabColor(new XSSFColor(rgb,null));
		}
	}

	/**
	 * シートの位置を調整する
	 *
	 * @param sheet	シート
	 * @param pos	位置
	 */
	public static void setSheetOrder(Sheet sheet,int pos) {
		Workbook book = sheet.getWorkbook();
		book.setSheetOrder(sheet.getSheetName(), pos);
	}

	/**
	 * POIで行をコピーする処理
	 * @param book ワークブック
	 * @param sheet ワークシート
	 * @param srcRowNo コピー元の行インデックス
	 * @param destRowNo コピー先の行インデックス
	 */
	public static void copyRow(Workbook book, Sheet sheet, int srcRowNo, int destRowNo) {
		if (sheet instanceof XSSFSheet) {
			XSSFSheet s = (XSSFSheet) sheet;
			if (sheet.getRow(destRowNo) == null) {
				sheet.createRow(destRowNo);
			}
			s.copyRows(srcRowNo, srcRowNo, destRowNo, new CellCopyPolicy());
		} else {
			_copyRow(book, sheet, srcRowNo, destRowNo);
		}
	}

	/**
	 * POIで行をコピーする処理
	 * @param book ワークブック
	 * @param sheet ワークシート
	 * @param srcRowNo コピー元の行インデックス
	 * @param destRowNo コピー先の行インデックス
	 */
	private static void _copyRow(Workbook book, Sheet sheet, int srcRowNo, int destRowNo) {
		Row newRow = sheet.getRow(destRowNo);
		Row srcRow = sheet.getRow(srcRowNo);

		if (newRow != null) {
			//コピー先に行が既に存在する場合、１行下にずらす
			sheet.shiftRows(destRowNo, sheet.getLastRowNum(), 1);
			newRow = sheet.createRow(destRowNo);
		} else {
			//存在しない場合は作成
			newRow = sheet.createRow(destRowNo);
		}

		// セルの型、スタイル、値などをすべてコピーする
		for (int i = 0; i < srcRow.getLastCellNum(); i++) {
			Cell oldCell = srcRow.getCell(i);
			// コピー元の行が存在しない場合、処理を中断
			if (oldCell == null) {
				continue;
			}
			Cell newCell = newRow.createCell(i);


			//スタイルのコピー
			CellStyle newCellStyle = book.createCellStyle();
			newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
			newCell.setCellStyle(newCellStyle);

			//コメントのコピー
			if (oldCell.getCellComment() != null) {
				newCell.setCellComment(oldCell.getCellComment());
			}

			//ハイパーリンクのコピー
			if (oldCell.getHyperlink() != null) {
				newCell.setHyperlink(oldCell.getHyperlink());
			}

			//セル型のコピー
			//newCell.setCellType(oldCell.getCellType());

			//セルの値をコピー
			switch (oldCell.getCellType()) {
			case BLANK:
				newCell.setCellValue(oldCell.getStringCellValue());
				break;
			case BOOLEAN:
				newCell.setCellValue(oldCell.getBooleanCellValue());
				break;
			case ERROR:
				newCell.setCellErrorValue(oldCell.getErrorCellValue());
				break;
			case FORMULA:
				newCell.setCellFormula(oldCell.getCellFormula());
				break;
			case NUMERIC:
				newCell.setCellValue(oldCell.getNumericCellValue());
				break;
			case STRING:
				newCell.setCellValue(oldCell.getRichStringCellValue());
				break;
			default:
				newCell.setBlank();
				break;
			}
		}

		//セル結合のコピー
		for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
			CellRangeAddress cellRangeAddress = sheet.getMergedRegion(i);
			if (cellRangeAddress.getFirstRow() == srcRow.getRowNum()) {
				CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
						newRow.getRowNum() + cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow(),
						cellRangeAddress.getFirstColumn(), cellRangeAddress.getLastColumn());
				sheet.addMergedRegion(newCellRangeAddress);
			}
		}
	}

	//	/**
	//	 * ValueFormatterを取得する
	//	 *
	//	 * @param format	フォーマット
	//	 * @return　ValueFormatterのインスタンス
	//	 */
	//	public static ValueFormatter getValueFormatter(String format) {
	//		ValueFormatter vf = vfMap.get(format);
	//		if (vf != null) {
	//			return vf;
	//		}
	//
	//		String[] arr = ConvertUtil.split(format, ":");
	//		String name = arr[0];
	//		vf =SpringUtil.getApplicationContext().getBean(name, ValueFormatter.class);
	//		if (arr.length > 1) {
	//			String[] params = ConvertUtil.split(arr[1], ",");
	//			vf.setParams(params);
	//		}
	//		vfMap.put(format, vf);
	//		return vf;
	//	}

	/**
	 * 指定されたコメント内容からコメント→セルテキストのマップを取得する
	 *
	 * @param row	指定された行
	 * @param comments	コメントの配列
	 * @return	マップ（キー＝コメント、値＝セルテキスト）
	 */
	public static Map<String,String> getTextByComment(Row row, String... comments) {
		Map<String, String> map = new HashMap<>();
		for (int i = row.getFirstCellNum(); i <= row.getLastCellNum(); i++) {
			Cell cell = row.getCell(i);
			if (cell != null) {
				Comment comment = cell.getCellComment();
				if (comment != null) {
					String commentText = cell.getCellComment().getString().getString();
					if (StringUtils.equalsAnyIgnoreCase(commentText, comments)) {
						String text = getCellText(cell);
						map.put(commentText, text);
					}
				}
			}
		}
		return map;

	}

	/**
	 * 指定されたコメント内容からコメント→カラム番号のマップを取得する
	 *
	 * @param row	指定された行
	 * @param comments	コメントの配列
	 * @return	マップ（キー＝コメント、値＝カラム番号）
	 */
	public static Map<String,Integer> getColIndexByComment(Row row, String... comments) {
		Map<String, Integer> map = new HashMap<>();
		for (int i = row.getFirstCellNum(); i <= row.getLastCellNum(); i++) {
			Cell cell = row.getCell(i);
			if (cell != null) {
				Comment comment = cell.getCellComment();
				if (comment != null) {
					String commentText = cell.getCellComment().getString().getString();
					if (StringUtils.equalsAnyIgnoreCase(commentText, comments)) {
						map.put(commentText, i);
					}
				}
			}
		}
		return map;
	}

	/**
	 * シートの指定する位置に内容はあるかを判断する
	 *
	 * @param sheet	シート
	 * @param rowNo	行番号
	 * @param colNo	列番号
	 * @return	true：内容なし、false：内容あり
	 */
	public static boolean isBlank(Sheet sheet, int rowNo, int colNo) {
		Row row = sheet.getRow(rowNo);
		if (row == null) {
			return true;
		}
		Cell cell = row.getCell(colNo);
		if (cell == null) {
			return true;
		}
		String text = getCellText(cell);
		return StringUtils.isBlank(text);
	}

	/**
	 * Excelブックをクローズ時イベント
	 *
	 * @param book	Excelブック
	 */
	static void onClose(Workbook book) {
		List<Sheet> list = new ArrayList<>();
		mapRange.forEach(
				(sheet,range) -> {if (sheet.getWorkbook() == book) {list.add(sheet);}});
		list.forEach( sheet -> mapRange.remove(sheet));
		cellLineStyles.remove(book);
	}
}


/**
 *　Sag専用なExcelブック
 *
 * @author Jason Zhou
 *
 */
class SagWorkBook extends XSSFWorkbook {

	public SagWorkBook(InputStream is) throws IOException {
		super(is);
	}

	@Override
	public void close() throws IOException {
		ExcelUtils.onClose(this);
		super.close();

	}

}


