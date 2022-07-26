/**
 * 
 */
package com.jasonzhou.tool.sag.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * 項目情報
 * 
 * @author Jason Zhou
 *
 */
public class FieldInfo implements Serializable {

	private int index = 0;
	/**  */
	private static final long serialVersionUID = 1695007824542641409L;

	/** 項目名 */
	private String name;
	
	/** 項目説明 */
	private String caption;
	
	/** 項目タイプ */
	private String type;
	
	/** 項目レングス */
	private String lengthText;
	
	/** 項目小数点以下桁数 */
	private String decimalText;

	/** 項目はNULL可能か */
	private String nullableText;
	
	/** 項目は主キーの一部か */
	private String pkText;
	
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
		if (StringUtils.isNumeric(lengthText)) {
			return Integer.parseInt(lengthText);
		} else {
			return 0;
		}
	}

	/**
	 * 項目レングス を設定する
	 *
	 * @param lengthText 項目レングス
	 */
	public void setLength(String lengthText) {
		this.lengthText = lengthText;
	}

	/**
	 * 項目小数点以下桁数 を取得する
	 *
	 * @return 項目小数点以下桁数
	 */
	public int getDecimal() {
		if (StringUtils.isNumeric(decimalText)) {
			return Integer.parseInt(decimalText);
		} else {
			return 0;
		}
	}

	/**
	 * 項目小数点以下桁数 を設定する
	 *
	 * @param decimalText 項目小数点以下桁数
	 */
	public void setDecimal(String decimalText) {
		this.decimalText = decimalText;
	}

	/**
	 * 項目はNULL可能か を取得する
	 *
	 * @return 項目はNULL可能か
	 */
	public Boolean isNullable() {
		return StringUtils.isBlank(nullableText);
	}

	/**
	 * 項目はNULL可能か を設定する
	 *
	 * @param nullableText 項目はNULL可能か
	 */
	public void setNullable(String nullableText) {
		this.nullableText = nullableText;
	}

	/**
	 * 項目は主キーの一部か を取得する
	 *
	 * @return 項目は主キーの一部か
	 */
	public Boolean isPk() {
		return StringUtils.isBlank(pkText);
	}

	/**
	 * 項目は主キーの一部か を設定する
	 *
	 * @param pkText 項目は主キーの一部か
	 */
	public void setPk(String pkText) {
		this.pkText = pkText;
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
	public void setIndex(String indexText) {
		index++;
		if (StringUtils.isNotBlank(indexText)) {
			indexNo.add(String.valueOf(index));
		}
	}
}
