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
 * @author Jason
 *
 */
public class FieldInfo implements Serializable {

	/**  */
	private static final long serialVersionUID = 5706465218697528946L;
	
	/** 物理名 */
	private String name;
	
	/** 論理名 */
	private String caption;
	
	/** タイプ */
	private String type;
	
	/** レングス */
	private int length;
	
	/** 小数点以下桁数 */
	private int decimalLength;
	
	/** 主キー対象か */
	private boolean pk = false;
	
	/** インデックス対象であれば、対象となるインデックスの番号リスト */
	private List<Integer> indexs = new ArrayList<>();
	
	/**　説明情報 */
	private String comment;

	/**
	 * 物理名 を取得する
	 * 
	 * @return 物理名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 物理名 を設定する
	 * 
	 * @param name 物理名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 論理名 を取得する
	 * 
	 * @return 論理名
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * 論理名 を設定する
	 * 
	 * @param caption 論理名
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * タイプ を取得する
	 * 
	 * @return タイプ
	 */
	public String getType() {
		return type;
	}

	/**
	 * タイプ を設定する
	 * 
	 * @param type タイプ
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * レングス を取得する
	 * 
	 * @return レングス
	 */
	public int getLength() {
		return length;
	}

	/**
	 * レングス を設定する
	 * 
	 * @param length レングス
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * 小数点以下桁数 を取得する
	 * 
	 * @return 小数点以下桁数
	 */
	public int getDecimalLength() {
		return decimalLength;
	}

	/**
	 * 小数点以下桁数 を設定する
	 * 
	 * @param decimalLength 小数点以下桁数
	 */
	public void setDecimalLength(int decimalLength) {
		this.decimalLength = decimalLength;
	}

	/**
	 * 主キー対象か を取得する
	 * 
	 * @return 主キー対象か
	 */
	public boolean isPk() {
		return pk;
	}

	/**
	 * 主キー対象か を設定する
	 * 
	 * @param pk 主キー対象か
	 */
	public void setPk(boolean pk) {
		this.pk = pk;
	}

	/**
	 * インデックス対象であれば、対象となるインデックスの番号リスト を取得する
	 * 
	 * @return インデックス対象であれば、対象となるインデックスの番号リスト
	 */
	public List<Integer> getIndexs() {
		return indexs;
	}

	/**
	 * インデックス対象であれば、対象となるインデックスの番号リスト を設定する
	 * 
	 * @param indexs インデックス対象であれば、対象となるインデックスの番号リスト
	 */
	public void setIndexs(List<Integer> indexs) {
		this.indexs = indexs;
	}

	/**
	 * 説明情報 を取得する
	 * 
	 * @return 説明情報
	 * 
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * 説明情報 を設定する
	 * 
	 * @param comment 説明情報
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

}
