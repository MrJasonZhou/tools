/**
 *
 */
package com.jasonzhou.tool.sag;

/**
 * 設定情報を読込むクラス
 *
 * @author Jason Zhou
 *
 */
public abstract class ConfigReader<C extends Config> {

	/**
	 * 設定情報をロードする
	 *
	 * @return	設定情報
	 * @throws Exception
	 */
	public abstract C load(Class<C> cClass) throws Exception;
}
