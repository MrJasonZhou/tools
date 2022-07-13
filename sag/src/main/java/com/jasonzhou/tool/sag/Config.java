/**
 * 
 */
package com.jasonzhou.tool.sag;

import java.util.HashMap;
import java.util.Map;

/**
 * 設定情報
 * 
 * @author Jason Zhou
 *
 */
public class Config {

	private Map<String, String> properties = new HashMap<>();
	
	private Map<String, Object> defines = new HashMap<>();
	
	/**
	 * 属性を設定する
	 * 
	 * @param key	キー
	 * @param value	値
	 */
	public void setProperty(String key, String value) {
		properties.put(key, value);
	}
	
	/**
	 * 属性を取得する
	 * 
	 * @param key	キー
	 * @return	属性値
	 */
	public String getProperty(String key) {
		return properties.get(key);
	}
	
	/**
	 * 定義情報を取得する
	 * 
	 * @param name	定義名			
	 * @param tClass	定義情報クラス
	 * @return	定義情報
	 */
	public <T> T getDefine(String name, Class<T> tClass) {
		Object obj = defines.get(name);
		if (tClass.isAssignableFrom(obj.getClass())) {
			return (T) obj;
		}
		return null;
	}
	
	/**
	 * 定義情報を設定する
	 * 
	 * @param <T>	定義情報クラス
	 * @param name	定義名
	 * @param define	定義情報
	 */
	public <T> void setDefine(String name, T define) {
		defines.put(name, define);
	}
}
