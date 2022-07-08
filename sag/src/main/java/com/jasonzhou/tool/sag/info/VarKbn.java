/**  */
package com.jasonzhou.tool.sag.info;

import org.apache.commons.lang3.StringUtils;

/**
 * 変数区分
 * 
 * @author Jason Zhou
 *
 */
public enum VarKbn {

	 GLOBAL
	 ,LOOP
	 ;
	
	/**
	 * 変数区分文字列から変数区分を取得する
	 * 
	 * @param name	変数区分文字列
	 * @return　変数区分
	 */
	public static VarKbn as(String name) {
		 for (VarKbn vt  :VarKbn.values()) {
			 if (StringUtils.equalsIgnoreCase(name, vt.name())) {
				 return vt;
			 }
		 }
		 return null;
	 }
}
