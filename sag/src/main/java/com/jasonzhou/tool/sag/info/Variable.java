/**  */
package com.jasonzhou.tool.sag.info;

/**
 * @author Jason Zhou
 *
 */
public class Variable {

	/** 変数定義 */
	private VarDefine define;

	/** 値 */
	private String value;

	/**
	 * コンストラクタ
	 * 
	 * @param define 変数定義
	 * @param value   値
	 */
	public Variable(VarDefine define, String value) {
		this.define = define;
		this.value = value;
	}
	/**
	 * 変数定義 を取得する
	 * 
	 * @return 変数定義
	 */
	public VarDefine getDefine() {
		return define;
	}

	/**
	 * 変数定義 を設定する
	 *
	 * @param define 変数定義
	 */
	public void setdefine(VarDefine define) {
		this.define = define;
	}

	/**
	 * 値 を取得する
	 * 
	 * @return 値
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 値 を設定する
	 *
	 * @param value 値
	 */
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Variable:[" + define + ", value=" + getValue() + "]";
	}
}
