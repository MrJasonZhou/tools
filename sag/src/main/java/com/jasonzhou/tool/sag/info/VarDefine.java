/**  */
package com.jasonzhou.tool.sag.info;

import org.apache.commons.lang3.StringUtils;

/**
 * 変数定義情報
 * 
 * @author Jason Zhou
 *
 */
public class VarDefine {

	/**  変数名 */
	private String varName;
	
	/** 位置情報 */
	private Position posistion;
	
	/** 変数型 */
	private VarType type;
	
	/** 変数区分 */
	private VarKbn kbn;
	
	/** 必須フラグ */
	private boolean required = false;
	
	private VarDefine() {
		
	}

	/**
	 * 変数名 を取得する
	
	 * @return 変数名
	 */
	public String getVarName() {
		return varName;
	}

	/**
	 * 変数名 を設定する
	 *
	 * @param varName 変数名
	 */
	public void setVarName(String varName) {
		this.varName = varName;
	}

	/**
	 * 位置情報 を取得する
	
	 * @return 位置情報
	 */
	public Position getPosistion() {
		return posistion;
	}

	/**
	 * 位置情報 を設定する
	 *
	 * @param posistion 位置情報
	 */
	public void setPosistion(Position posistion) {
		this.posistion = posistion;
	}

	/**
	 * 変数型 を取得する
	
	 * @return 変数型
	 */
	public VarType getType() {
		return type;
	}

	/**
	 * 変数型 を設定する
	 *
	 * @param type 変数型
	 */
	public void setType(VarType type) {
		this.type = type;
	}

	/**
	 * 変数区分 を取得する
	
	 * @return 変数区分
	 */
	public VarKbn getKbn() {
		return kbn;
	}

	/**
	 * 変数区分 を設定する
	 *
	 * @param kbn 変数区分
	 */
	public void setKbn(VarKbn kbn) {
		this.kbn = kbn;
	}

	/**
	 * 必須フラグ を取得する
	
	 * @return 必須フラグ
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * 変数定義を取得する
	 * 
	 * @param pos		位置情報
	 * @param defineText	定義文字列（[区分]:[型] 変数名）
	 * @return	変数定義
	 */
	public static VarDefine create(Position pos, String defineText) {
		VarDefine vd = new VarDefine();
		vd.setPosistion(pos);
		//必須フラグ
		if (StringUtils.startsWith(defineText, "*")) {
			vd.required = true;
			defineText = StringUtils.substring(defineText, 1);
		}
		String[] arr1 = StringUtils.split(defineText, ":");
		//デフォルト区分＝グローバル
		vd.setKbn(VarKbn.GLOBAL);
		if (arr1.length >= 2) {
			vd.setKbn(VarKbn.as(arr1[0]));
			//defineText = arr1[1];
		}
		arr1 = StringUtils.split(defineText);
		String name ;
		//デフォルト型：文字列
		if (arr1.length < 2) {
			vd.setType(VarType.STRING);
			name = arr1[0];
		} else {
			name = arr1[1];
		}
		//変数名
		vd.setVarName(name);
		return vd;
	}
	
	@Override
	public String toString() {
		return "VarDefine:[pos=(kbn=" + getKbn() + ", "+posistion.getRow() + "," + posistion.getCol() +"), name=" + getVarName() + "]";
	}
}
