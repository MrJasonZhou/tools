/**
 * 
 */
package com.jasonzhou.tool.sag;

import com.jasonzhou.tool.sag.config.Config;

/**
 * テンプレートエンジンのインタフェース
 * 
 * @author Jason Zhou
 *
 */
public interface ITemplateEngineer {

	/**
	 * 設定情報から実行す
	 * 
	 * @param config	設定情報
	 * @throws Exception
	 */
	public void execute(Config config) throws Exception;
	
	/**
	 * 式を評価する
	 * 
	 * @param express	式
	 * @return	評価結果
	 * @throws Exception
	 */
	public Object eval(String express) throws Exception;
}
