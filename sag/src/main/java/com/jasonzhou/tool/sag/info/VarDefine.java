/**  */
package com.jasonzhou.tool.sag.info;

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
	 * @param name	 	変数名
	 * @return	変数定義
	 */
	public static VarDefine create(Position pos, String name) {
		VarDefine vd = new VarDefine();
		vd.setPosistion(pos);
		//変数名
		vd.setVarName(name);
		return vd;
	}
	
	@Override
	public String toString() {
		return "VarDefine:[pos=("+posistion.getRow() + "," + posistion.getCol() +"), name=" + getVarName() + "]";
	}
}
