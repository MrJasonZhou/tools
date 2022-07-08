/**  */
package com.jasonzhou.tool.sag.func;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.jasonzhou.tool.sag.config.Config;
import com.jasonzhou.tool.sag.config.Task;
import com.jasonzhou.tool.sag.info.InputData;
import com.jasonzhou.tool.sag.info.Variable;
import com.jasonzhou.tool.sag.util.SpringUtil;

/**
 * 関数操作
 * 
 * @author Jason Zhou
 *
 */
public class Functions {
	
	public static final String FUNC_EVAL = "eval";
	
	/**
	 * 関数実行
	 *
	 * @param express 関数名
	 * @param config	設定情報
	 * @param task	タスク情報
	 * @param data	入力データ
	 * @param record	レコード
	 * @return	関数実行結果
	 * 
	 */
	public static Object exexcute(String express, Config config, Task task, InputData data, Map<String, Variable> record) {
		String funcName = parseFuncName(express);
		Object[] args = parseArgs(express);
		IFunction func = SpringUtil.getBean(funcName, IFunction.class);
		if (func != null) {
			Object result = func.execute(config, task, data, record, args);
			return result;
		}
		return null;
	}
	
	/**
	 * 関数式から関数名を取得する
	 * 
	 * @param express	関数式
	 * @return	関数名
	 */
	public static String parseFuncName(String express) {
		int pos = StringUtils.indexOf(express, "(");
		if (pos == -1) {
			return express;
		} else {
			return StringUtils.trim(StringUtils.left(express, pos));
		}
	}
	
	/**
	 * 関数式から引数の配列を取得する
	 * 
	 * @param express	関数式
	 * @return	引数の配列
	 */
	public static Object[] parseArgs(String express) {
		List<Object> list = new ArrayList<>();
		int pos = StringUtils.indexOf(express, "(");
		if (pos != -1) {
			express = StringUtils.substring(express, pos + 1) ;
			pos = StringUtils.indexOf(express, ")");
			if (pos != -1) {
				express = StringUtils.trim(StringUtils.left(express, pos));
				list = spliteArgs(express);
			}
		}
		return list.toArray();
	}

	private static List<Object> spliteArgs(String args) {
		List<Object> list = new ArrayList<>();
		while (StringUtils.length(StringUtils.trim(args)) != 0) {
			//文字列開始記号
			if (StringUtils.equals(StringUtils.left(args, 1), "'")) {
				int pos = StringUtils.indexOf(args, "'", 1);
				if (pos == -1) {
					args += "'";
					pos = StringUtils.indexOf(args, "'", 1);
				}
				String arg = StringUtils.substring(args, 1, pos);
				list.add(arg);
				args = StringUtils.trim(StringUtils.substring(args, pos+1));
				if (StringUtils.equals(StringUtils.left(args, 1), ",")) {
					args = StringUtils.trim(StringUtils.substring(args, 1));
				} else {
					args = "";
				}
			} else {
				int pos = StringUtils.indexOf(args, ",");
				if (pos == -1) {
					String arg = StringUtils.trim(args);
					if (StringUtils.isNumeric(arg)) {
						Long l = Long.parseLong(arg);
						if (l > Integer.MAX_VALUE) {
							list.add(l);
						} else {
							list.add(l.intValue());
						}
					}
					args = "";
				} else {
					String arg = StringUtils.substring(args, 0, pos);
					if (StringUtils.isNumeric(arg)) {
						Long l = Long.parseLong(arg);
						if (l > Integer.MAX_VALUE) {
							list.add(l);
						} else {
							list.add(l.intValue());
						}
					}
					args = StringUtils.trim(StringUtils.substring(args, pos + 1));
				}
			}
		}
		return list;
	}

}
