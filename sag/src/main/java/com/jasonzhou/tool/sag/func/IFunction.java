/**  */
package com.jasonzhou.tool.sag.func;

import java.util.Map;

import com.jasonzhou.tool.sag.config.Config;
import com.jasonzhou.tool.sag.config.Task;
import com.jasonzhou.tool.sag.info.InputData;
import com.jasonzhou.tool.sag.info.Variable;

/**
 * 内部関数のインタフェース
 * 
 * @author Jason Zhou
 *
 */
public interface IFunction {
	
	/**
	 * 関数実行
	 * 
	 * @param config	設定情報
	 * @param task	タスク情報
	 * @param data	入力データ
	 * @param record	レコード
	 * @param args 引数
	 * @return	関数実行結果
	 * 
	 */
	public Object execute(Config config, Task task, InputData data, Map<String, Variable> record, Object... args); 

}
