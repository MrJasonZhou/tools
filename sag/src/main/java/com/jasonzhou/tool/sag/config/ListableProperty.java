/**
 * 
 */
package com.jasonzhou.tool.sag.config;

import java.io.Serializable;
import java.util.List;

/**
 * リスト属性クラス
 * 
 * @author Jason Zhou
 *
 */
public interface ListableProperty<T> extends Serializable {

	/**
	 * 属性を追加する
	 * 
	 * @param t	属性
	 */
	public void add(T t);
	
	/**
	 * 属性のリストを取得する
	 * 
	 * @return 属性のリスト
	 */
	public List<T> getList();
	
	/**
	 * 属性インスタンスを作成する
	 * 
	 * @return 属性インスタンス	
	 */
	public T newElement();
	
	/**
	 * 属性クラスを返す
	 * 
	 * @return	属性クラス
	 */
	public Class<T> elementClass();
	
}
