/**
 * 
 */
package com.jasonzhou.tool.sag.config;

import java.util.ArrayList;
import java.util.List;

/**
 * タスク情報リスト
 * 
 * @author Jason Zhou
 *
 */
public class Tasks implements ListableProperty<Task> {
	
	/**  */
	private static final long serialVersionUID = -7260447996465738441L;
	private List<Task> list = new ArrayList<>();

	@Override
	public void add(Task t) {
		list.add(t);
	}

	@Override
	public List<Task> getList() {
		return list;
	}

	@Override
	public Task newElement() {
		return new Task();
	}

	@Override
	public Class<Task> elementClass() {
		return Task.class;
	}

}
