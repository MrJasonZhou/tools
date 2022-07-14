/**
 * プロジェクト：NeoGuras
 */
package com.jasonzhou.tool.sag.excel;

/**
 * 値のフォーマット
 * 
 * @author 周
 *
 */
public interface ValueFormatter {
	
	/**
	 * パラメータを設定する
	 * 
	 * @param params　パラメータ
	 */
	public void setParams(String... params);

	/**
	 * 値を指定されたフォーマットを使って、フォーマットする
	 * 
	 * @param values	値
	 * @return　フォーマット結果
	 */
	public Object format(Object... values);
}
