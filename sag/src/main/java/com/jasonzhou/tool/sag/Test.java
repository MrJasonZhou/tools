/**
 * 
 */
package com.jasonzhou.tool.sag;

import java.util.Set;

import com.jasonzhou.tool.sag.excel.IFunction;
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Test test = new Test();
		test.testGetSubClass(IFunction.class);

	}

}
