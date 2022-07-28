/**
 * 
 */
package com.jasonzhou.tool.sag;

import java.io.InputStream;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.ClassPathResource;

import com.jasonzhou.tool.sag.config.SagConfig;
import com.jasonzhou.tool.sag.config.SagConfigReader;
import com.jasonzhou.tool.sag.util.ExcelUtils;
import com.jasonzhou.tool.sag.util.SpringUtil;

/**
 *　テスト用クラス
 *
 * @author Jason Zhou
 *
 */
public class Test {
	
	private <T> void testGetSubClass(Class<T> tClass) {
		
		Set<Class<? extends T>> set = SpringUtil.getSubTypesOf(tClass);
		System.out.println(tClass.getCanonicalName() + "のサブクラス：");
		for (Class<? extends T> clz : set) {
			System.out.println(clz.getName());
		}
	}
	
	private void testLoadExcel(InputStream is) throws Exception {
		SagConfigReader reader = new SagConfigReader();
		SagConfig config = reader.load(is, SagConfig.class);
	}
	
	private void testMergeRangion(InputStream is) throws Exception {
		Workbook book = ExcelUtils.load(is);
		Sheet sheet = book.getSheet("merge");
		for (int rowNo = sheet.getFirstRowNum(); rowNo < sheet.getLastRowNum(); rowNo++) {
			Row row = sheet.getRow(rowNo);
			for (short colNo = row.getFirstCellNum(); colNo < row.getLastCellNum(); colNo++) {
				String text = ExcelUtils.getCellText(sheet, rowNo, colNo);
				String comment = ExcelUtils.getCellComment(sheet, rowNo, colNo);
				System.out.println("cell[" + rowNo + ", " + colNo + "] text=[" + text + "], comment =[" + comment + "]");
			}
			System.out.println("--------------------------------------------------");
		}
		book.close();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Test test = new Test();
		InputStream is = test.getClass().getResourceAsStream("/sagConfig.xlsm");
		test.testLoadExcel(is);
	}

}
