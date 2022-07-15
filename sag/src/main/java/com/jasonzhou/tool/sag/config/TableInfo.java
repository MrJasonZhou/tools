/**
 * 
 */
package com.jasonzhou.tool.sag.config;

import java.util.ArrayList;
import java.util.List;

/**
 * テーブル情報
 *
 * @author Jason Zhou
 *
 */
public class TableInfo implements ListableProperty<FieldInfo>, SimpleProperty {

	/**  */
	private static final long serialVersionUID = -999556608842538794L;
	
	private List<FieldInfo> list = new ArrayList<>();
	
	/** テーブル名 */
	private String name;
	
	/** テーブル説明 */
	private String caption;
	
	@Override
	public void add(FieldInfo t) {
		list.add(t);
	}

	@Override
	public List<FieldInfo> getList() {
		return list;
	}

	@Override
	public FieldInfo newElement() {
		return new FieldInfo();
	}

	@Override
	public Class<FieldInfo> elementClass() {
		return FieldInfo.class;
	}

	/**
	 * テーブル名 を取得する
	 *
	 * @return テーブル名
	 */
	public String getName() {
		return name;
	}

	/**
	 * テーブル名 を設定する
	 *
	 * @param name テーブル名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * テーブル説明 を取得する
	 *
	 * @return テーブル説明
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * テーブル説明 を設定する
	 *
	 * @param caption テーブル説明
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

}
