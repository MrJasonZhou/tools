/**
 * 
 */
package com.jasonzhou.tool.sag;

import java.io.InputStream;

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
	 * @param is	設定情報の入力ストリーム
	 * @return	設定情報
	 * @throws Exception
	 */
	public abstract C load(InputStream is, Class<C> cClass) throws Exception;
}
