/**  */
package com.jasonzhou.tool.sag.util;

import org.springframework.context.ApplicationContext;

/**
 * @author Jason Zhou
 *
 */
public class SpringUtil {

	private static ApplicationContext ac;

	/**
	 * アプリケーションコンテキスト を設定する
	 *
	 * @param ac アプリケーションコンテキスト
	 */
	public static void setApplicationContext(ApplicationContext ac) {
		SpringUtil.ac = ac;
	}
	
	/**
	 * Beanを取得する
	 * 
	 * @param name	Beanの名称
	 * @param requiredType	Beanのクラス
	 * @return	Beanのインスタンス
	 */
	public static <T> T getBean(String name, Class<T> requiredType) {
		return ac.getBean(name, requiredType);
	}

	/**
	 * Beanを取得する
	 * 
	 * @param requiredType	Beanのクラス
	 * @return	Beanのインスタンス
	 */
	public static <T> T getBean(Class<T> requiredType) {
		return ac.getBean(requiredType);
	}

}
