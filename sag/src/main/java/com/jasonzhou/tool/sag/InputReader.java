/**
 *
 */
package com.jasonzhou.tool.sag;

import java.util.Collection;

/**
 * 設定情報リーダーから設定情報を取得してドキュメントファイルを読取、入力データを取得するインタフェース
 *
 * @param <R>	設定情報リーダークラス
 * @param <T>	入力データクラス
 *
 * @author Jason Zhou
 *
 */
public interface InputReader<R extends ConfigReader<? extends Config>, T> {

	/**
	 * 設定情報を読み取り、入力データを取得する
	 *
	 * @param r 設定情報リーダー
	 * @return　入力データのコレクション
	 */
	public Collection<T> read(R r);
}
