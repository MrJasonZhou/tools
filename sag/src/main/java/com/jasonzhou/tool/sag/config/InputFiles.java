/**
 * 
 */
package com.jasonzhou.tool.sag.config;

import java.util.ArrayList;
import java.util.List;

/**
 * 入力ファイル情報
 * 
 * @author Jason Zhou
 *
 */
public class InputFiles implements ListableProperty<InputFile>{
	
	/**  */
	private static final long serialVersionUID = 1L;
	private List<InputFile> list = new ArrayList<>();
	@Override
	public void add(InputFile t) {
		list.add(t);
		
	}
	@Override
	public List<InputFile> getList() {
		return list;
	}
	@Override
	public InputFile newElement() {
		return new InputFile();
	}
	@Override
	public Class<InputFile> elementClass() {
		return InputFile.class;
	}
}
