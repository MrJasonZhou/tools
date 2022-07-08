/**  */
package com.jasonzhou.tool.sag.info;

import org.apache.commons.lang3.StringUtils;

/**
 * 変数型
 * 
 * @author Jason Zhou
 *
 */
public enum VarType {
	
	STRING
	,INT
	,FLOAT
	,DATE
	,DATETIME
	;
	
	/**
	 * 変数型文字列から変数型を取得する
	 * 
	 * @param name	変数型文字列	
	 * @return	変数型
	 */
	public VarType as(String name) {
		for (VarType vt : VarType.values()) {
			if (StringUtils.equalsIgnoreCase(name, vt.name())) {
				return vt;
			}
		}
		return STRING;
	}
	

}
