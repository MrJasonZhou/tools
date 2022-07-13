/**
 * 
 */
package com.jasonzhou.tool.sag;

/**
 * コンテキストのインタフェース
 * 
 * @author Jason Zhou
 *
 */
public interface IContext {
	
	/**
	 * オブジェクトを登録する
	 * 
	 * @param name　オブジェクト名	
	 * @param obj　オブジェクト
	 */
	public void registe(String name , Object obj) ;
	
	/**
	 * 登録されたオブジェクトを取得する
	 * 
	 * @param name　		オブジェクト名	
	 * @param tClass	オブジェクトクラス
	 * @return　オブジェクト
	 */
	public <T> T get(String name , Class<T> tClass);

}
