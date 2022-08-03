/**
 *
 */
package com.jasonzhou.tool.sag;

/**
 * テンプレートエンジンのインタフェース
 *
 * @author Jason Zhou
 *
 */
public interface ITemplateEngineer<C extends Config> {

	/**
	 * 設定情報から実行す
	 *
	 * @param config	設定情報
	 * @throws Exception
	 */
	public void execute(C config) throws Exception;

	/**
	 * 式を評価する
	 *
	 * @param express	式
	 * @param model     モデル情報
	 * @return	評価結果
	 * @throws Exception
	 */
	public Object eval(String express, Object model) throws Exception;
}
