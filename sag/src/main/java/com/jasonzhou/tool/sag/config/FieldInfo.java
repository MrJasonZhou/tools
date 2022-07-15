/**
 * 
 */
package com.jasonzhou.tool.sag.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 項目情報
 * 
 * @author Jason Zhou
 *
 */
public class FieldInfo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1695007824542641409L;

	/** 項目名 */
	private String name;
	
	/** 項目説明 */
	private String caption;
	
	/** 項目タイプ */
	private String type;
	
	/** 項目レングス */
	private int length;
	
	/** 項目小数点以下桁数 */
	private int decimal;
	
	/** 項目はNULL可能か */
	private Boolean nullable;
	
	/** 項目は主キーの一部か */
	private Boolean pk;
	
	/** 項目はインデックス対象の場合インデックス番号リスト */
	private List<String> indexNo = new ArrayList<>();

	/**
	 * 項目名 を取得する
	 *
	 * @return 項目名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 項目名 を設定する
	 *
	 * @param name 項目名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 項目説明 を取得する
	 *
	 * @return 項目説明
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * 項目説明 を設定する
	 *
	 * @param caption 項目説明
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * 項目タイプ を取得する
	 *
	 * @return 項目タイプ
	 */
	public String getType() {
		return type;
	}

	/**
	 * 項目タイプ を設定する
	 *
	 * @param type 項目タイプ
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 項目レングス を取得する
	 *
	 * @return 項目レングス
	 */
	public int getLength() {
		return length;
	}

	/**
	 * 項目レングス を設定する
	 *
	 * @param length 項目レングス
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * 項目小数点以下桁数 を取得する
	 *
	 * @return 項目小数点以下桁数
	 */
	public int getDecimal() {
		return decimal;
	}

	/**
	 * 項目小数点以下桁数 を設定する
	 *
	 * @param decimal 項目小数点以下桁数
	 */
	public void setDecimal(int decimal) {
		this.decimal = decimal;
	}

	/**
	 * 項目はNULL可能か を取得する
	 *
	 * @return 項目はNULL可能か
	 */
	public Boolean getNullable() {
		return nullable;
	}

	/**
	 * 項目はNULL可能か を設定する
	 *
	 * @param nullable 項目はNULL可能か
	 */
	public void setNullable(Boolean nullable) {
		this.nullable = nullable;
	}

	/**
	 * 項目は主キーの一部か を取得する
	 *
	 * @return 項目は主キーの一部か
	 */
	public Boolean getPk() {
		return pk;
	}

	/**
	 * 項目は主キーの一部か を設定する
	 *
	 * @param pk 項目は主キーの一部か
	 */
	public void setPk(Boolean pk) {
		this.pk = pk;
	}

	/**
	 * 項目はインデックス対象の場合インデックス番号リスト を取得する
	 *
	 * @return 項目はインデックス対象の場合インデックス番号リスト
	 */
	public List<String> getIndexNo() {
		return indexNo;
	}

	/**
	 * 項目はインデックス対象の場合インデックス番号リスト を設定する
	 *
	 * @param indexNo 項目はインデックス対象の場合インデックス番号リスト
	 */
	public void setIndexNo(List<String> indexNo) {
		this.indexNo = indexNo;
	}
	
}
