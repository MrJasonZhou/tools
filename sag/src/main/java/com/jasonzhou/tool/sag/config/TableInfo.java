/**
 * 
 */
package com.jasonzhou.tool.sag.config;

import java.util.ArrayList;
import java.util.List;

/**
 * テーブル情報
 * 
 * @author Jason
 *
 */
public class TableInfo implements ListableProperty<FieldInfo>, SimpleProperty {

	/**  */
	private static final long serialVersionUID = 2983446096914329805L;
	
	private List<FieldInfo> list = new ArrayList<>();
	
	/** 物理名 */
	private String name;
	
	/** 論理名 */
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
}
